;;; Run this file by going to the project directory (the directory with
;;; 'project.clj' in it) and saying 'lein repl'.

;;; If you're using emacs with nrepl (see
;;; http://clojure-doc.org/articles/tutorials/emacs.html for setup
;;; info), run this entire file by first "jacking in" (Ctrl-c, Meta-j),
;;; then evaluating the whole file (Ctrl-c, Ctrl-k).  Eval individual
;;; expressions by placing the cursor after the last closing-parenthesis
;;; and typing Ctrl-c, Ctrl-e (for "evaluate").  Access documentation
;;; for any Clojure primitive by putting the cursor (which emacs calls
;;; "point") inside or behind the primitive and typing Ctrl-c,
;;; Ctrl-d. Find the help for the rest of the nrepl mode by typing
;;; Ctrl-h, m.

;;; If you're using Leiningen from a terminal, type "lein repl" in the
;;; project directory (the directory containing the file "project.clj").

;;; With emacs, the most important thing to learn is "Paredit."  It
;;; takes most of the pain out of parentheses and nesting.  There is a
;;; lot of info about it on the web (see http://emacsrocks.com/e14.html
;;; particularly), and the help is good.  The two biggies are
;;; paredit-forward-slurp-sexp, whose help you can find by typing
;;; Ctrl-h, k, Ctrl-Shift-) and paredit-splice-sexp (Ctrl-h, k, Meta-s).
;;; Take the time to learn them.  Slurp has three friends:
;;; paredit-forward-barf-sexp (Ctrl-h, k, Ctrl-Shift-} ) and the
;;; backwards versions of slurp and barf.  They're next most
;;; important. Finally, you'll want to learn paredit-wrap-round Ctrl-h,
;;; k, Shift-Meta-(.

;;; Re-indent deranged code by putting point at the beginning of the
;;; code and typing Ctrl-Alt-Q.  Move around at the expression level by
;;; Ctrl-Alt-F (forward) and Ctrl-Alt-B (backward); Ctrl-Alt-D (down a
;;; level) and Ctrl-Alt-U (up a level). Ctrl-Alt-Space at the beginning
;;; of an open paren will select the entire subtree.

;;; Here are the namespaces we use; compare this list with the
;;; :dependencies in the project.clj file, which specifies the libraries
;;; and packages to download that contain these namespaces:

(ns expt1.core
  (:require [clojure.zip             :as zip    ]
            [clojure.xml             :as xml    ]
            [net.cgrand.enlive-html  :as html   ]
            [clj-http.client         :as http   ]
            [clojure.data.json       :as cdjson ]
            clojure.string
            clojure.pprint
            [clojure.reflect         :as r      ]
            [rx.lang.clojure.interop :as rx     ]
            )
  (:use     [clojail.core            :only [sandbox]]
            [clojail.testers         :only [blacklist-symbols
                                            blacklist-objects
                                            secure-tester
                                            ]])
  (:refer-clojure :exclude [distinct])
  (:import [rx
            Observable
            Observer
            subscriptions.Subscriptions
            subjects.Subject
            subjects.PublishSubject])
  )

(def hit-wikipedia
  "Set to 'false' or 'nil' during development to avoid hitting the web
   site too much"
  true)

;;; First up is an example of metaprogramming: code that rewrites code. A
;;; principal advantage of lisps in general and Clojure in particular is that
;;; the text of your program is just more data you can manipulate. Because
;;; code is just more data, we can rewrite code at compile time or at
;;; runtime. Pdump just prints an unevaluated expression, its value, and then
;;; produces the value, just as if we had called "identity" on the
;;; value. Right away, we have an example of something that's hard to do --
;;; really hard -- in non-lisps. To access, let alone manipulate, the text of
;;; code, you would need, typically, an object model for the entire language,
;;; reflection, a decompiler, and more. Often, people can't afford the time
;;; and trouble to do all that and they just write strings manually that
;;; contain the unevaluated code, as in
;;;
;;;     printf("1 + 2 ~~> ", 1 + 2);
;;;
;;; But here we can do it once-and-for all with a "macro." Macros, in general,
;;; receive their arguments as unevaluated data, and then have special
;;; operators to insinuate (a.k.a. "interpolate") these expressions into loci
;;; where they will get evaluated.

(defmacro pdump
  "Monitoring and debugging macro with semantics of 'identity'."
  [x]
  ;; Local variables with hash "#" after their names are guaranteed fresh;
  ;; they won't collide with any variables in the insinuated
  ;; expressions.
  ;;
  ;; The result of a macro is usually a backticked expression. Backtick is
  ;; "syntax-quote," similar to quasiquote in Scheme. It specifies the CODE,
  ;; as data in list brackets (parentheses) that you want to replace a call of
  ;; pdump. Its meaning here is "Please replace (pdump whatever) with the
  ;; quoted code, after various insinuations marked with tildes and at-signs."

  ;; First, we bind ("assign") the value of the expression x to a fresh
  ;; variable x#.

  `(let [x# (try ~x (catch Exception e# (str e#)))]
     (do (println "----------------")
         ;; First, print the given expression quoted, i.e., unevaluated.
         (clojure.pprint/pprint '~x)
         ;; Then print a funny arrow that means "evaluates-to" or "becomes":
         (println "~~>")
         ;; Finally, print the value of the expression.
         (clojure.pprint/pprint x#)
         x#)))

;;; We need a 'catch-less' variant to run inside the jail (sandbox)
;;; because the sandbox doesn't allow catches.

(defmacro catchless-pdump
  "Monitoring and debugging macro with semantics of 'identity', for use
   where 'catch' is not allowed."
  [x]
  `(let [x#  ~x]
     (do (println "----------------")
         (clojure.pprint/pprint '~x)
         (println "~~>")
         (clojure.pprint/pprint x#)
         x#)))

;;;   ___                  _       ___  _
;;;  / __|___ _ _  ___ _ _(_)__   / _ \| |__ ___ ___ _ ___ _____ _ _
;;; | (_ / -_) ' \/ -_) '_| / _| | (_) | '_ (_-</ -_) '_\ V / -_) '_|
;;;  \__/\___|_||_\___|_| |_\__|  \___/|_.__/__/\___|_|  \_/\___|_|
;;;

(defn- or-default
  "Fetch first optional value from function arguments preceded by &."
  [val default] (if val (first val) default))

(defn subscribe-collectors
  "Subscribe asynchronous collectors to any observable; produce a map
   containing a :subscription object for .unsubscribing and a :reporter
   function for retrieving values from the collectors. Default wait-time
   is 1 second until timeout."
  [obl & optional-wait-time]
  (let [wait-time (or-default optional-wait-time 1000)
        onNextCollector      (agent [])  ; seq of all values sent
        onErrorCollector     (atom  nil) ; only need one value
        onCompletedCollector (promise)]  ; can wait on another thread
    (let [collect-next
          (rx/action [item] (send onNextCollector
                                  (fn [state] (conj state item))))
          collect-error
          (rx/action [excp] (reset! onErrorCollector excp))

          collect-completed
          (rx/action [    ] (deliver onCompletedCollector true))

          report-collectors
          (fn []
            (identity ;; pdump ;; for verbose output, use pdump.
             { ;; Wait on onCompleted BEFORE waiting on onNext because
               ;; the onNext-agent's await-for can return too quickly,
               ;; before messages from other threads are drained from
               ;; its queue.  This code depends on order-of-evaluation
               ;; assumptions in the map.

              :onCompleted (deref onCompletedCollector wait-time false)

              :onNext      (do (await-for wait-time onNextCollector)
                               ;; Produce results even if timed out
                               @onNextCollector)

              :onError     @onErrorCollector
              }))]
      {:subscription
       (.subscribe obl collect-next collect-error collect-completed)

       :reporter
       report-collectors})))

(defn report
  "Given subscribed collectors, produce results obtained so far."
  [subscribed-collectors]
  (pdump ((:reporter subscribed-collectors))))

;;;  ___        _____     _           _      _
;;; | _ \__ __ |_   _|  _| |_ ___ _ _(_)__ _| |
;;; |   /\ \ /   | || || |  _/ _ \ '_| / _` | |
;;; |_|_\/_\_\   |_| \_,_|\__\___/_| |_\__,_|_|

;;;  ___ _        _      _   _
;;; / __| |_  _ _(_)_ _ | |_(_)_ _  __ _
;;; \__ \ ' \| '_| | ' \| / / | ' \/ _` |
;;; |___/_||_|_| |_|_||_|_\_\_|_||_\__, |
;;;                                |___/

;;; How to shrink a collection; first, for a collection entirely in memory:

(pdump
 (take 2 [1 2 3]))

;;; "Map" is a way to transform values in a collection; it doesn't shrink the
;;; collection, but we introduce "coordinate-free" programming here: you don't
;;; need loop indices, so off-by-one errors are much harder to write.

(pdump
 (map (fn [x] (* x x))
      (take 2 [1 2 3])))

;;; If we want our lambda expression to refer to variables from the
;;; environment, we must be careful.

(pdump
 (let [x 42]
   (map (fn [x] (* x x))
        (take 2 [1 2 3]))))

;;; The mistake is kind of obvious in this little example, but much
;;; harder to spot if the lambda is big or deep. The local variable
;;; shadows the variable we want from outside. We can manually rename
;;; it:

(pdump
 (let [x 42]
   (map (fn [y] (* y x))
        (take 2 [1 2 3]))))

;;; but now we risk shadowing a "y" we might want. Better is to use
;;; fresh variables, just as with macros:

(pdump
 (let [x 42]
   (map (fn [x#] (* x# x))
        (take 2 [1 2 3]))))

;;; This is safe, but a little heavy for most circumstances where we can
;;; "lexically see" the non-parameters (a.k.a. "free variables"). That's
;;; why "lexical scoping" is the norm nowadays (it wasn't always so, and
;;; there are notable exceptions like "this" in JavaScript). We note it
;;; here just in passing.

;;; Nicer (fluent, chaining) syntax using "shove" or "thrush" macros
;;; (this one shoves predecessors into last position of successors
;;; because most Clojure higher-order operators for in-memory
;;; collections take collection arguments in last position and produce
;;; collections of kind "sequence.").

(->> [1 2 3]
     (take 2)
     (map (fn [x] (* x x)))
     pdump)

;;; The following shows how easy it is to insert new ops in a transform
;;; chain.

(->> [1 2 3]
     (filter even?)
     (take 2)                           ; "take" is robust on short colls
     (map (fn [x] (* x x)))
     pdump)

;;;  _   _                                         _           _ _ _
;;; | |_(_)_ __  ___ ___ ____ __  __ _ __ ___   __| |_  _ __ _| (_) |_ _  _
;;; |  _| | '  \/ -_)___(_-< '_ \/ _` / _/ -_) / _` | || / _` | | |  _| || |
;;;  \__|_|_|_|_\___|   /__/ .__/\__,_\__\___| \__,_|\_,_\__,_|_|_|\__|\_, |
;;;                        |_|                                         |__/

;;; COLLECTIONS OF DATA DISTRIBUTED IN TIME ACT JUST LIKE COLLECTIONS
;;; DISTRIBUTED IN SPACE.  Expect the same operators.

;;;                     _ _           _            __
;;;  __ ___  ___ _ _ __| (_)_ _  __ _| |_ ___ ___ / _|_ _ ___ ___
;;; / _/ _ \/ _ \ '_/ _` | | ' \/ _` |  _/ -_)___|  _| '_/ -_) -_)
;;; \__\___/\___/_| \__,_|_|_||_\__,_|\__\___|   |_| |_| \___\___|
;;;                                          _
;;;  _ __ _ _ ___  __ _ _ _ __ _ _ __  _ __ (_)_ _  __ _
;;; | '_ \ '_/ _ \/ _` | '_/ _` | '  \| '  \| | ' \/ _` |
;;; | .__/_| \___/\__, |_| \__,_|_|_|_|_|_|_|_|_||_\__, |
;;; |_|           |___/                            |___/

;;; COORDINATE-FREE PROGRAMMING: replace index-loops with higher-order
;;; operators -- operators that take function arguments (lambda expressions or
;;; closures).  The functions operate individually on the values in the
;;; collection.  Replace expressions like
;;;
;;;     for (i = 0; i < ARRAY_LEN; i++)
;;;         println(array[i]);
;;;
;;; with expressions like
;;;
;;;     array.map(element => println(element))
;;;
;;; Replace the oordinate-FULL expression array[i] with the
;;; coordinate-free expression depending on the fresh variable
;;; "element," a parameter iteratively *bound* to the values in the
;;; collection (the array).  The lambda expression is a "callback"
;;; invoked by the higher-order operator "map."  "Map" is called
;;; "higher-order" because it is a function that takes functions as
;;; arguments.
;;;
;;; Once coordinates are gone, there is nothing intrinsic to or explicit
;;; in the expression to say where the value came from: it can come from
;;; memory or from an asynchronous, distributed stream.  Thus, the
;;; expression can, in principle, be used over any collection
;;; independently of the "coordinate system", i.e., the PRIMARY KEY,
;;; used to index the values.
;;;
;;; We can easily shift from a space coordinate system to a time
;;; coordinate system without changing the "business logic" underneath,
;;; often without changing the code at all (Dave Ray from netflix has a
;;; short video on this). Here, we will endure the minor change from ->>
;;; (thread into last slot always) for space coordinates to -> (thread
;;; into first slot always) for time coordinates. See also rplevy's
;;; "swiss arrows" that let you thread into an explicit box, <>,
;;; separately for each expression in a chain.
;;;
;;; ****************************************************************
;;; Abstraction away from coordinates or indices is the principal
;;; abstraction of functional -- and therefore reactive -- programming.
;;; ****************************************************************
;;;

(-> (Observable/from [1 2 3])          ; an obl of length 3
    (.take 2)                          ; an obl of length 2
    subscribe-collectors               ; waits for completion
    report)                            ; produce results

;;; In space, we transform collections-in-memory into
;;; collections-in-memory; in time, we transform collections-in-time
;;; into collections-in-time. Collections in memory, in Clojure, are
;;; lists, vectors, maps, sets, lazy-sequences, and more. Generically,
;;; Clojure calls them "sequences" because they all implement an ISeq
;;; interface. Collection in time, in Rx and rxJava, are "observables."
;;; Outside Clojure specifically, the term "sequence" refers to any
;;; collection, over any coordinate system, space or time or primary
;;; key, that is ordered with duplicates allowed. In the rest of this
;;; document, we use the term "sequence" to mean a Clojure sequence: a
;;; collection distributed in space (memory) and the term "observable,"
;;; or "obl" for short, to mean a Clojure / Rx / rxJava collection
;;; distributed over time, synchronously or asynchronously, and the term
;;; "collection" when we don't distinguish the two.

;;; The following separates out the intermediate observables so that we
;;; can observe them separately.

(let [xs (Observable/from [1 2 3])
      ys (-> xs (.take 2))
      xscs (subscribe-collectors xs)
      yscs (subscribe-collectors ys)]
  (report xscs)
  (report yscs)
  )

;;; We can do all the stuff we did before with (Clojure) sequences

(-> (Observable/from [1 2 3])
    (.map (rx/fn [x] (* x x)))
    (.take 2)
    subscribe-collectors
    report)

;;; Notice we (somewhat stupidly) put the map before the take. A
;;; miniature "query optimizer," as a small macro, can move all maps
;;; after takes because the values not taken are not observable (unless
;;; an explicit observer is subscribed, a circumstance the macro can
;;; easily detect). I won't write the macro here, merely noting that the
;;; final result does not depend on the order of appearance of the map
;;; and the take, and that
;;; ****************************************************************
;;; it's reasonably easy to write sophisticated query optimizers in
;;; small macros.
;;; ****************************************************************

(-> (Observable/from [1 2 3])
    (.take 2)
    (.map (rx/fn [x] (* x x)))
    subscribe-collectors
    report)


(-> (Observable/from [1 2 3])
    (.filter (rx/fn* even?))
    (.take 2)
    (.map (rx/fn [x] (* x x)))
    subscribe-collectors
    report)

;;; Here we wrap the "lexical environment," i.e., the "let", around the
;;; entire chain since the arrow blindly insinuates values in the first
;;; positions of all its forms, mangling the "let." That's a principal
;;; motivation for swiss arrows, which let you insinuate values wherever
;;; you want them. But that is a refinement for another time and place.

(let [x 42]
  (-> (Observable/from [1 2 3])
      (.filter (rx/fn* even?))
      (.take 2)
      (.map (rx/fn [x#] (* x x#)))
      subscribe-collectors
      report))

;;; The '->' operator shoves its predecessor into the FIRST position of
;;; its successor because most rxjava operators take collection
;;; arguments in FIRST position (heritage of C# 'extension methods',
;;; where collection arguments are 'this' in the privileged first
;;; position.

;;; Clojure's sequence operators, like "take", work for infinite, lazy
;;; sequences.  These are an intermediate step between sequences fully
;;; realized in memory and sequences that produce values forever over
;;; time.

;;; Lazy sequences produce values over time, driven by internal forces
;;; in the particular Clojure session.  Observables produce values over
;;; time, driven by anything, including external forces.

(->> (repeat 42)
     (take 2)
     pdump)

;;; Observables can produce infinite sequences, but it's harder to
;;; demonstrate in examples.  Don't call "Observable/from" on an
;;; infinite lazy sequence; it realizes the whole thing and never
;;; finishes.

(-> (Observable/from (take 10 (repeat 42)))
    (.take 2)
    subscribe-collectors
    report)

;;; Filter out odd numbers and keep the first two of that intermediate
;;; result.  Write the predicate function-argument of the higher-order
;;; "filter" operator as an anonymous "rxjava" function.  Because java
;;; doesn't (yet) have first-class anonymous functions, we must declare
;;; it with an rx/fn rather than with the standard, Clojure "fn."  (This
;;; is a gargoyle that I hope will go away because it limits our claim
;;; that EXACTLY the same code can run over space and over time).

(-> (Observable/from [1 2 3 4 5 6])
    (.filter (rx/fn [n] (== 0 (mod n 2)))) ; same as "even?"
    (.take 2)                              ; keeps only the first two
    subscribe-collectors
    report)

;;; Here is another way of doing the same thing, only with a named
;;; predicate-function argument.  "rx/fn*" converts a first-class
;;; Clojure function into a rxjava function.

(-> (Observable/from [1 2 3 4 5 6])
    (.filter (rx/fn* even?))
    (.take 2)
    subscribe-collectors
    report)

;;; Here is the space version:

(->> [1 2 3 4 5 6]
     (filter even?)
     (take 2)
     pdump)

;;;   ___                _
;;;  / __|_ _ _____ __ _(_)_ _  __ _
;;; | (_ | '_/ _ \ V  V / | ' \/ _` |
;;;  \___|_| \___/\_/\_/|_|_||_\__, |
;;;                            |___/

;;; Filtering removes values, potentially shrinking a collection.
;;; Mapping transforms values, not changing the length of a collection.
;;; How do we make a collection bigger?

;;; Let's transform each number x into a collection of numbers, adding x
;;; to some familiar constants, then flattening the results exactly
;;; once.

;;; Most methods that lengthen sequences rely on mapMany, called
;;; "SelectMany" in many Rx documents (.e.g., http://bit.ly/18Bot23) and
;;; is similar to Clojure's "mapcat", up to order of parameters.

;;; First, the space version:

(->> [1 2 3]
     (take 2)
     ;; For each x in the collection, map a function of one parameter
     ;; over the fixed vector [42 43 44].  The function will add its
     ;; argument to x; that is, the function is a closure over x.  The
     ;; function produces a vector; mapcat flattens the results just
     ;; once.
     (mapcat
      (fn [x]
        (map (partial + x)
             [42 43 44])))
     pdump)

;;; Now, the time version:

(-> (Observable/from [1 2 3])
    (.take 2)
    ;; With an observervable, mapMany's function argument must produce
    ;; a nested observable, which mapMany will flatten exactly once:
    (.mapMany
     (rx/fn [x]
       (Observable/from
        (map (partial + x)
             [42 43 44]))))
    subscribe-collectors
    report)

;;; Note, the name of "mapMany" has changed since rxJava 0.12, the
;;; version used here. We'll have to research the new name when we
;;; upgrade rxJava versions.

;;;  ___ _       _                  _
;;; / __| |_ _ _(_)_ _  __ _   _ __| |__ _ _  _ ___
;;; \__ \  _| '_| | ' \/ _` | | '_ \ / _` | || (_-<
;;; |___/\__|_| |_|_||_\__, | | .__/_\__,_|\_, /__/
;;;                    |___/  |_|          |__/

;;; Shortening first, both space and time versions.

(->> ["one" "two" "three"]
     (take 2)
     pdump)

(-> (Observable/from ["one" "two" "three"])
    (.take 2)
    subscribe-collectors
    report
    )

;;; "seq" explodes strings into lazy sequences of characters:

(pdump
 (seq "one"))

(def string-explode
  "Explode a string into characters (purposeful alias of seq)."
  seq)

;;; Now, growing:

(pdump
 (->> ["one" "two" "three"]
      (mapcat string-explode)))

(-> (Observable/from ["one" "two" "three"])
    (.mapMany
     (rx/fn [string]
       (Observable/from (string-explode string))))
    subscribe-collectors
    report)

;;;   __
;;;  / _|_ _ ___ _ __ ___ ___ ___ __ _
;;; |  _| '_/ _ \ '  \___(_-</ -_) _` |
;;; |_| |_| \___/_|_|_|  /__/\___\__, |
;;;                                 |_|

;;; Clean up the repeated, ugly (Observable/from ...) calls into a
;;; composition, but we can't (comp Observable/from ...) because it's a
;;; Java method and does not implement Clojure IFn.  Fix this by
;;; wrapping it in a function:

(defn from-seq
  "Wrap rxjava's 'Observable/from' in a Clojure fn so it can be
  composed."
  [s]
  (Observable/from s))

;;; Now we have a pretty function we can compose with string-explode:

(-> (from-seq ["one" "two" "three"])
    (.mapMany (rx/fn* (comp from-seq string-explode)))
    subscribe-collectors
    report)

;;;     _ _    _   _         _
;;;  __| (_)__| |_(_)_ _  __| |_
;;; / _` | (_-<  _| | ' \/ _|  _|
;;; \__,_|_/__/\__|_|_||_\__|\__|


;;; Rx has a couple of operators: "disinct" and "distinctUntilChanged."
;;; Fake them as follows, to show how to build new operators in Clojure.

(-> (from-seq ["one" "two" "three"])
    (.mapMany (rx/fn* (comp from-seq string-explode)))

    ;; The following two implement "distinct".

    (.reduce #{} (rx/fn* conj))

    ;; We now have an observable containing just one hash-set of unique
    ;; characters.  Because Clojure hash-sets are automatically
    ;; seq'able, promote the hash-set back into an obl of chars as
    ;; follows.  We show below what happens if we forget the "mapMany."

    (.mapMany (rx/fn* from-seq))

    ;; This is ok because "distinct" MUST consume the entire obl
    ;; sequence before producing its values.  "Distinct" can't work on
    ;; a non-finite obl sequence.

    subscribe-collectors
    report)

;;; We can show what happens if we forget the "mapMany" by trying to
;;; take 2 from the obl produced by "reduce:"

(-> (from-seq ["one" "two" "three"])
    (.mapMany (rx/fn* (comp from-seq string-explode)))
    (.reduce #{} (rx/fn* conj))
    (.take 2)
    subscribe-collectors
    report)

;;; We get the whole set; it's been "imploded" by reduce! "MapMany"
;;; explodes the results back into an observable of single values:

(-> (from-seq ["one" "two" "three"])
    (.mapMany (rx/fn* (comp from-seq string-explode)))
    (.reduce #{} (rx/fn* conj))
    (.mapMany (rx/fn* from-seq))
    (.take 2)
    subscribe-collectors
    report)

;;; Package and test.

(defn distinct [obl]
  (-> obl
      (.reduce #{} (rx/fn* conj))
      (.mapMany (rx/fn* from-seq))))

(-> (from-seq ["one" "two" "three"])
    (.mapMany (rx/fn* (comp from-seq string-explode)))
    distinct
    subscribe-collectors
    report)

(-> (from-seq ["one" "two" "three"])
    (.mapMany (rx/fn* (comp from-seq string-explode)))
    distinct
    (.take 2)
    subscribe-collectors
    report)

;;; Distinct is "unstable" in the sense that it reorders its input.
;;; EXERCISE: a stable implementation.  HINT: use the set to check
;;; uniqueness and build a vector or list to keep order.

;;; You should be getting an intuition at this point that "mapcat" and
;;; "mapMany" are deeply fundamental. Indeed they are, and you have
;;; intuited one of the main concepts behind the dreaded "monads."

;;;     _ _                    _            _
;;;  __| (_)__ _ _ _ ___ _____(_)___ _ _   (_)
;;; / _` | / _` | '_/ -_|_-<_-< / _ \ ' \   _
;;; \__,_|_\__, |_| \___/__/__/_\___/_||_| (_)
;;;        |___/
;;;          _
;;;  _ _ ___| |_ _  _ _ _ _ _
;;; | '_/ -_)  _| || | '_| ' \
;;; |_| \___|\__|\_,_|_| |_||_|

;;; This helps with for "distinct-until-changed"

;;; "Return" lifts a value into a collection of length 1 so that the
;;; collection can be composed with others via the standard query
;;; operators.  (Theory point: this and "mapMany" are the two primitive
;;; operators in the library, completing the dire monads, and almost all
;;; the others can be built in terms of them.  History point: "return"
;;; is admittedly a lousy name.)

(defn return
  "Put item into an observable for composability via mapMany."
  [item]
  (from-seq [item]))

;;; With space-collections, "vector" and "list" act like "return."

(->>
 ["one" "two" "three"]
 (mapcat string-explode)
 (mapcat vector)                       ; acts like "identity" here
 pdump
 )

;;; Here is the "same" code in the time domain:

(-> (from-seq ["one" "two" "three"])
    (.mapMany (rx/fn* (comp from-seq string-explode)))
    (.mapMany (rx/fn* return))         ; acts like "identity" here
    subscribe-collectors
    report)


;;;     _ _    _   _         _
;;;  __| (_)__| |_(_)_ _  __| |_
;;; / _` | (_-<  _| | ' \/ _|  _|
;;; \__,_|_/__/\__|_|_||_\__|\__|
;;;      _   _     _   _ _  ___ _                          _
;;;     | | | |_ _| |_(_) |/ __| |_  __ _ _ _  __ _ ___ __| |
;;;     | |_| | ' \  _| | | (__| ' \/ _` | ' \/ _` / -_) _` |
;;;      \___/|_||_\__|_|_|\___|_||_\__,_|_||_\__, \___\__,_|
;;;                                           |___/

;;; DistinctUntilChanged collapses runs of the same value in a sequence into
;;; single instances of each value. [a a a x x x a a a] becomes [a x a].
;;;
;;; DistinctUntilChanged CAN work on infinite sequences.  Therefore the
;;; following, though correct, is unacceptable because it consumes the
;;; entire source before producing values.  With distinct-until-changed:
;;; we only need to remember one back.  Still, as a step to a better
;;; solution:

(-> (from-seq ["onnnnne" "tttwo" "thhrrrrree"])

    (.mapMany (rx/fn* (comp from-seq string-explode)))

    (.reduce [] (rx/fn [acc x]
                  (let [l (last acc)]
                    (if (and l (= x l)) ; accounts for legit nils
                      acc               ; don't accumulate
                      (conj acc x)))))

    ;; We now have a fully realized, non-lazy, singleton observable
    ;; containing a sequence containing representatives of runs of
    ;; non-distinct characters.  Explode it back into characters and
    ;; flatten it out exatly once:

    (.mapMany (rx/fn* from-seq))

    subscribe-collectors
    report)

;;;           __    __ _         _             _
;;;  _ _ ___ / _|  / _(_)_ _  __| |___  __ _  | |_  ___ _ __  ___
;;; | '_/ -_)  _| |  _| | ' \/ _` (_-< / _` | | ' \/ _ \ '  \/ -_)
;;; |_| \___|_|   |_| |_|_||_\__,_/__/ \__,_| |_||_\___/_|_|_\___|

;;; Better is to keep a mutable buffer of length one. It could be an
;;; atom if we had the opposite of "compare-and-set!."  We want an
;;; atomic primitive that sets the value only if it's NOT equal to its
;;; current value.  "compare-and set!" sets the atom to a new val if its
;;; current value is EQUAL to an old val.  It's easy enough to get the
;;; desired semantics with a Ref and software-transactional memory, the
;;; only wrinkle being that the container must be defined outside the
;;; function that mapMany applies.  However, this solution will not
;;; materialize the entire input sequence.

(let [exploded (-> (from-seq ["onnnnne" "tttwo" "thhrrrrree"])
                   (.mapMany (rx/fn* (comp from-seq string-explode))))
      last-container (ref [])]
  (-> exploded
      (.mapMany (rx/fn [obn]
                  (dosync
                   (let [l (last @last-container)]
                     (if (and l (= obn l))
                       (Observable/empty) ; shiny-new! like [] or ()
                       (do
                         (ref-set last-container [obn])
                         (return obn))))))) ; shiny-new!
      subscribe-collectors
      report))

;;; Package and test:

(defn distinct-until-changed
  "Produces an observable that collapses repeated values from its
  predecessor into a single value."
  [obl]
  (let [last-container (ref [])]
    (-> obl
        (.mapMany (rx/fn [obn]
                    (dosync
                     (let [l (last @last-container)]
                       (if (and l (= obn l))
                         (Observable/empty)
                         (do
                           (ref-set last-container [obn])
                           (return obn))))))))))

(->  (from-seq ["onnnnne" "tttwo" "thhrrrrree"])
     (.mapMany (rx/fn* (comp from-seq string-explode)))
     distinct-until-changed
     subscribe-collectors
     report)

;;; It's well-behaved on an empty input:

(->  (from-seq [])
     (.mapMany (rx/fn* (comp from-seq string-explode)))
     distinct-until-changed
     subscribe-collectors
     report)

;;; In a real application, we would do many more unit tests.

;;;  ___               _          _
;;; | _ \___ _ __  ___| |_ ___ __| |
;;; |   / -_) '  \/ _ \  _/ -_) _` |
;;; |_|_\___|_|_|_\___/\__\___\__,_|
;;;    ___               _
;;;   / _ \ _  _ ___ _ _(_)___ ___
;;;  | (_) | || / -_) '_| / -_|_-<
;;;   \__\_\\_,_\___|_| |_\___/__/


;;; Be sure to set a .java.policy file in the appropriate directory
;;; (HOME if you are running this as an ordinary user; in a configured
;;; directory on a server).  Here is a very liberal policy file (you
;;; don't want this in Production!)
;;;
;;; grant {
;;;   permission java.security.AllPermission;
;;; };
;;;

;;; If you just need to quote things, send 'em in:

;;; Symbols must be fully qualified (no implicit namespaces) in the
;;; sandbox.

(pdump
 (let [sb (sandbox secure-tester)]
   (sb '(-> (expt1.core/from-seq ["onnnnne" "tttwo" "thhrrrrree"])
            (.mapMany
             (rx.lang.clojure.interop/fn*
              (comp expt1.core/from-seq
                    expt1.core/string-explode)))
            expt1.core/distinct-until-changed
            expt1.core/subscribe-collectors
            expt1.core/report))))

;;; If you need to serialize them, just put them in strings, then read
;;; them, then eval them:

(defn run-jailed-queries
  [obl queries]
  (let [sb (sandbox secure-tester)
        es (read-string obl)
        qs (map read-string queries)
        ]
    (sb `(-> ~es ~@qs subscribe-collectors))))

(let [obl "(expt1.core/from-seq [\"onnnnne\" \"tttwo\" \"thhrrrrree\"])"
      queries [ "(.mapMany
                   (rx.lang.clojure.interop/fn*
                     (comp expt1.core/from-seq
                           expt1.core/string-explode)))"

                , "expt1.core/distinct-until-changed"
                  ]
      ]
  (report (run-jailed-queries obl queries)))


;;;  _  _     _    __ _ _            _         _            _   _
;;; | \| |___| |_ / _| (_)_ _____ __| |___ _ _(_)_ _____ __| | (_)
;;; | .` / -_)  _|  _| | \ \ /___/ _` / -_) '_| \ V / -_) _` |  _
;;; |_|\_\___|\__|_| |_|_/_\_\   \__,_\___|_| |_|\_/\___\__,_| (_)


;;;  ___              _
;;; / __|_  _ _ _  __| |_  _ _ ___ _ _  ___ _  _ ___
;;; \__ \ || | ' \/ _| ' \| '_/ _ \ ' \/ _ \ || (_-<
;;; |___/\_, |_||_\__|_||_|_| \___/_||_\___/\_,_/__/
;;;      |__/
;;;   ___  _                         _    _
;;;  / _ \| |__ ___ ___ _ ___ ____ _| |__| |___
;;; | (_) | '_ (_-</ -_) '_\ V / _` | '_ \ / -_)
;;;  \___/|_.__/__/\___|_|  \_/\__,_|_.__/_\___|
;;;

;;; An observable has only a -- therefore IS A -- "subscribe" method,
;;; which is a function of an observer.  When you read "subscribe,"
;;; think "for-each."  When called, the subscribe method subscribes the
;;; observer to the observations (a.k.a. "messages," "events,"
;;; "notifications"): the values produced by the observable.

(defn synchronous-observable
  "Convert a Clojure sequence into a custom Observable whose 'subscribe'
   method does not return until the observable completes, that is, a
   'blocking' observable."
  [the-seq]
  (Observable/create

   ;; This is 'subscribe:' a function of an observer:

   (rx/fn [obr]

     ;; Just call the observer's "onNext" handler until exhausted.

     (doseq [x the-seq] (-> obr (.onNext x)))

     ;; After sending all values, complete the sequence:

     (-> obr .onCompleted)

     ;; Return a no-op subscription.  Because this observable does not
     ;; return from its subscription call until it sends all messages
     ;; and completes, the thread receiving the subscription can't
     ;; unsubscribe until the observable completes, at which time there
     ;; is no point in unsubscribing.  We say that this observable
     ;; "blocks."  The returned subscription is pro-forma only.

     (Subscriptions/empty))))

;;; Test and Demo:

(defn flip
  "Produce a function that calls the given binary function with
  arguments in the opposite order."
  [g]
  (fn [x y] (g y x)))

;;; Test the synchronous observable; as an aside, this illustrates
;;; Clojure destructuring -- a nice way to bind multiple parameters at
;;; once to values appearing in bits of structure of the input:

(-> (synchronous-observable (range 50)) ; produces 0, 1, 2, ..., 50
    (.map    (rx/fn* #(str "SynchronousValue_" %)))
    (.map    (rx/fn* (partial (flip clojure.string/split) #"_")))
    (.map    (rx/fn [[a b]] [a (Integer/parseInt b)]))
    (.filter (rx/fn [[a b]] (= 0 (mod b 7))))
    subscribe-collectors
    report)


;;;  ___            _        __   ___                 ___
;;; |   \ _  _ __ _| |  ___ / _| |   \ __ ___ _____  | _ \__ _ _  _
;;; | |) | || / _` | | / _ \  _| | |) / _` \ V / -_) |   / _` | || |
;;; |___/ \_,_\__,_|_| \___/_|   |___/\__,_|\_/\___| |_|_\__,_|\_, |
;;;                                                            |__/

;;; Dave makes Rx look like Clojure; we make Clojure look like Rx.

(let [-map    (flip map)
      -filter (flip filter)]
  (-> (range 50)
      (-map    #(str "NonReactiveValue2.0_" %))
      (-map    (partial (flip clojure.string/split) #"_"))
      (-map    (fn [[a b]] [a (Integer/parseInt b)]))
      (-filter (fn [[a b]] (= 0 (mod b 7))))
      pdump))

;;; With these local definitions, "-map" and "-filter", the non-reactive
;;; version looks almost just like the reactive version.



;;;    _                    _
;;;   /_\   ____  _ _ _  __| |_  _ _ ___ _ _  ___ _  _ ___
;;;  / _ \ (_-< || | ' \/ _| ' \| '_/ _ \ ' \/ _ \ || (_-<
;;; /_/ \_\/__/\_, |_||_\__|_||_|_| \___/_||_\___/\_,_/__/
;;;            |__/
;;;   ___  _                         _    _
;;;  / _ \| |__ ___ ___ _ ___ ____ _| |__| |___
;;; | (_) | '_ (_-</ -_) '_\ V / _` | '_ \ / -_)
;;;  \___/|_.__/__/\___|_|  \_/\__,_|_.__/_\___|
;;;

(defn- future-cancelling-subscription
  "Create a future-cancelling subscription from a Clojure future."
  [f]
  (Subscriptions/create (rx/action [] (future-cancel f))))

(defn asynchronous-observable
  "Transform a Clojure sequence into a custom Observable whose
   'subscribe' method returns immediately and whose other actions --
   namely, onNext, onCompleted, onError -- occur on another thread."
  [the-seq]
  (Observable/create
   (rx/fn [obr]                         ; this is "subscribe"
     (let [f (future (doseq [x the-seq] (-> obr (.onNext x)))
                     (-> obr .onCompleted))]
       (future-cancelling-subscription f)))))

(-> (asynchronous-observable (range 50))
    (.map    (rx/fn* #(str "AsynchronousValue_" %)))
    (.map    (rx/fn* (partial (flip clojure.string/split) #"_")))
    (.map    (rx/fn [[a b]] [a (Integer/parseInt b)]))
    (.filter (rx/fn [[a b]] (= 0 (mod b 7))))
    subscribe-collectors
    report)

;;;  _   _   _             _ _         _
;;; | |_| |_| |_ _ __   __| (_)___ _ _| |_   __ _ ___
;;; | ' \  _|  _| '_ \ / _| | / -_) ' \  _| / _` (_-<
;;; |_||_\__|\__| .__/ \__|_|_\___|_||_\__| \__,_/__/
;;;             |_|
;;;          _                         _    _
;;;      ___| |__ ___ ___ _ ___ ____ _| |__| |___
;;;     / _ \ '_ (_-</ -_) '_\ V / _` | '_ \ / -_)
;;;     \___/_.__/__/\___|_|  \_/\__,_|_.__/_\___|

(defn asynch-wikipedia-article
  "Fetch a sequence of Wikipedia articles asynchronously with proper
   error handling."
  [names]
  (Observable/create
   (rx/fn [obr]                         ; my 'subscribe' or 'for-each'
     (let [f (future
               (try
                 (doseq [name names]
                   (-> obr
                       (.onNext
                       (html/html-resource
                         (java.net.URL.
                          (str "https://en.wikipedia.org/wiki/" name))))))
                 (catch Exception e (-> obr (.onError e))))
               (-> obr .onCompleted))]
       (future-cancelling-subscription f)))))

;;; The following is a left-over reminder from an earlier attempt that
;;; we can use the "zipper" to traverse and modify the html.  Powerful
;;; mojo; worth the reminder here.

(defn zip-str [s]
  (zip/xml-zip
   (xml/parse
    (java.io.ByteArrayInputStream.
     (.getBytes s)))))

;;; Have a develop-time switch so we don't pound the site too much.

(when hit-wikipedia
  (->>
   (((:reporter (subscribe-collectors
                 (asynch-wikipedia-article
                  [(rand-nth ["Atom" "Molecule" "Quark" "Boson" "Fermion"
                              "Electron" "Positron" "Muon" "Pion" "Kaon"
                              "Hadron" "Lepton" "Mezon"])
                   "NonExistentTitle"
                   (rand-nth ["Lion" "Tiger" "Bear" "Shark"])])
                 5000)))
    :onNext)
   (map #(html/select % [:title]))
   pdump))

;;;  _  _     _    __ _ _      __   ___    _
;;; | \| |___| |_ / _| (_)_ __ \ \ / (_)__| |___ ___ ___
;;; | .` / -_)  _|  _| | \ \ /  \ V /| / _` / -_) _ (_-<
;;; |_|\_\___|\__|_| |_|_/_\_\   \_/ |_\__,_\___\___/__/

(defn simulated-slow-thunk
  "Simulate the timing of a slow operation."
  [thunk & optional-delay]
  (let [delay (or-default optional-delay 50)]
    (Observable/create
     (rx/fn [obr]
       (let [f (future
                 (try
                   (Thread/sleep delay)
                   (-> obr (.onNext (thunk)))
                   (-> obr .onCompleted)
                   (catch Exception e (-> obr (.onError e))))) ]
         (future-cancelling-subscription f))))))

(defn- coin-toss
  "Choose between two alternatives with equal probability."
  [heads tails]
  (if (= 0 (rand-int 2)) heads tails))

(defn get-user
  "Asynchronously fetch user data into an observable of a hash-map."
  [user-id]
  (simulated-slow-thunk
   (fn []
     {:user-id user-id
      :name "Sam Harris"
      :preferred-language (coin-toss "en-us" "es-us") })
   60))

(-> (get-user 8765)
    subscribe-collectors
    report)

(defn get-video-bookmark
  "Asynchronously fetch bookmark for video into observable of an
  integer."
  [user-id, video-id]
  (simulated-slow-thunk
   (fn []
     {:video-id video-id
      :position (coin-toss 0 (rand-int 2500))})
   20))

(-> (get-video-bookmark 8765 23459876)
    subscribe-collectors
    report)

(defn get-video-metadata
  "Asynchronously fetch movie metadata for a given language into
  observable of a hash-map."
  [video-id, preferred-language]
  (simulated-slow-thunk
   (fn []
     {:video-id video-id
      :title (case preferred-language
               "en-us" "House of Cards: Episode 1"
               "es-us" "Cámara de Tarjetas: Episodio 1"
               "no-title")
      :director "David Fincher"
      :duration 3365})
   50))

(-> (get-video-metadata 23459876 (coin-toss "en-us" "es-us"))
    subscribe-collectors
    report)

(defn get-video-for-user
  "Get video metadata for a given user-id
  - video metadata
  - video bookmark position
  - user data
  Produces observable of a hash-map."
  [user-id video-id]
  (let [user-obl
        (-> (get-user user-id)
            (.map (rx/fn [user] {:user-name (:name user)
                                 :language  (:preferred-language user)})))
        bookmark-obl
        (-> (get-video-bookmark user-id video-id)
            (.map (rx/fn [bookmark] {:viewed-position (:position bookmark)})))

        video-metadata-obl
        (-> user-obl
            (.mapMany
             (rx/fn [user-hashmap]
               (get-video-metadata video-id (:language user-hashmap)))))]

    (-> (Observable/zip

         ;; take N observables ...

         bookmark-obl
         video-metadata-obl
         user-obl

         ;; and an N-ary function ...

         (rx/fn [bookmark-map metadata-map user-map]
           {:bookmark-map bookmark-map
            :metadata-map metadata-map
            :user-map     user-map}))

        ;; and convert format

        (.map (rx/fn [data]
                {:video-id       video-id
                 :user-id        user-id
                 :video-metadata (:metadata-map    data)
                 :language       (:language        (:user-map data))
                 :bookmark       (:viewed-position (:bookmark-map data))})))))

(-> (get-video-for-user 12345 78965)
    subscribe-collectors
    report
    )

;;;     _       __            _  _               _
;;;  _ | |__ _ / _|__ _ _ _  | || |_  _ ___ __ _(_)_ _
;;; | || / _` |  _/ _` | '_| | __ | || (_-</ _` | | ' \
;;;  \__/\__,_|_| \__,_|_|   |_||_|\_,_/__/\__,_|_|_||_|
;;;  ___                _
;;; | __|_ _____ _ _ __(_)___ ___ ___
;;; | _|\ \ / -_) '_/ _| (_-</ -_|_-<
;;; |___/_\_\___|_| \__|_/__/\___/__/

;;; Compare-contrast Java, JavaScript, and Datapath (an Amazon dialect
;;; of mini-Kanren)

;;;    ____                 _           ____
;;;   / __/_ _____ ________(_)__ ___   / __/
;;;  / _/ \ \ / -_) __/ __/ (_-</ -_) /__ \
;;; /___//_\_\\__/_/  \__/_/___/\__/ /____/

;;; Exercise 5: Use map() to project an array of videos into an array of
;;; {id,title} pairs.  For each video, project a {id,title} pair.

;;; (in Clojure, iterpret "pair" to mean "a hash-map with two elements")

(defn jslurp [filename]
  (-> (str "./src/expt1/" filename)
      slurp
      cdjson/read-str
      pdump
      ))

(-> (jslurp "Exercise_5.json")

    ;; Make all levels asynchronous (fuggle):

    asynchronous-observable

    ;; The following line is the one that should be compared /
    ;; contrasted with JavaScript & Datapath -- the surrounding lines
    ;; are just input & output.

    (.map
     (rx/fn [vid] {:id    (vid "id")
                   :title (vid "title")
                  }))

    subscribe-collectors
    report)

;;;
;;; in JavsScript, interpret "pair" to mean "an object with two
;;; properties"

;;; return newReleases
;;;   .map(
;;;     function (r) {
;;;       return { id:    r.id,
;;;                title: r.title
;;;       };
;;;     });


;;;
;;; Datapath
;;;

;;; (exist (r)                           ; declare your logic values
;;;   (and
;;;     (.* newReleases r)               ; the fact-base
;;;     (= result { id:    (. r "id"),   ; unify
;;;                 title: (. r "title"),
;;;        }
;;; ) ) )

;;;    ____                 _           ___
;;;   / __/_ _____ ________(_)__ ___   ( _ )
;;;  / _/ \ \ / -_) __/ __/ (_-</ -_) / _  |
;;; /___//_\_\\__/_/  \__/_/___/\__/  \___/

;;; Exercise 8: Chain filter and map to collect the ids of videos that
;;; have a rating of 5.0

;;; Select all videos with a rating of 5.0 and project the id field.

(-> (jslurp "Exercise_8.json")
    asynchronous-observable

    (.filter (rx/fn [vid] (== (vid "rating") 5.0)))
    (.map    (rx/fn [vid]     (vid "id"    )     ))

    subscribe-collectors
    report)

;;;
;;;  Javascript
;;;

;;; return newReleases
;;;   .filter(
;;;     function(r) {
;;;       return r.rating === 5.0;
;;;     })
;;;   .map(
;;;     function(r){
;;;       return r.id;
;;;     });


;;;
;;;  Datapath
;;;

;;; (exist (r)
;;;   (and
;;;     (.* newReleases r)
;;;     (. r "rating" 5.0)
;;;     (. r "id" id)                    ; instantiate the variable id
;;; ) )


;;;    ____                 _           ______
;;;   / __/_ _____ ________(_)__ ___   <  <  /
;;;  / _/ \ \ / -_) __/ __/ (_-</ -_)  / // /
;;; /___//_\_\\__/_/  \__/_/___/\__/  /_//_/

;;; Exercise 11: Use map() and mergeAll() to project and flatten the
;;; movie lists into an array of video ids.

;;; Restatement: Produce a flattened list of video ids from all movie
;;; lists.

;;; Remark: No "mergeAll" in rxjava / Clojure; look up "merge" here:
;;; http://netflix.github.io/RxJava/javadoc/rx/Observable.html

(-> (jslurp "Exercise_11.json")
    asynchronous-observable

    ;; Fetch the "videos" key out of each genre.
    (.map (rx/fn [genre]
            (asynchronous-observable (genre "videos"))))

    (Observable/merge)

    ;; Fetch the "id" key out of each vid.
    (.map (rx/fn [vid] (vid "id")))

    subscribe-collectors
    report)

;;;
;;; Javascript
;;;

;;; return movieLists
;;;   .map(
;;;     function(x) {
;;;       return x.videos;
;;;     })
;;;   .mergeAll()
;;;   .map(
;;;     function(x) {
;;;       return x.id;
;;;     });


;;;
;;; Datapath
;;;

;;; (.
;;;   (.*
;;;     (.
;;;       (.* movieLists) "videos") ) "id" id)


;;;    ____                 _           _______
;;;   / __/_ _____ ________(_)__ ___   <  / / /
;;;  / _/ \ \ / -_) __/ __/ (_-</ -_)  / /_  _/
;;; /___//_\_\\__/_/  \__/_/___/\__/  /_/ /_/

;;; Exercise 14: Use mapMany() to retrieve id, title, and 150x200 box
;;; art url for every video.
;;;
;;; I changed the original slightly so that "Chamber" has no 150x200 box
;;; art (to test the case where some input does not pass the filter) and
;;; so that "Fracture" has two 150x200 boxarts (to test that they're not
;;; improperly nested)

(-> (jslurp "Exercise_14.json")
    asynchronous-observable

    (.mapMany (rx/fn [genres] (-> (genres "videos")
                                  asynchronous-observable)))

    (.mapMany (rx/fn [vid]    (-> (vid "boxarts")
                                  asynchronous-observable
                                  (.filter (rx/fn [art]
                                             (and (== 150 (art "width"))
                                                  (== 200 (art "height")))))
                              (.map (rx/fn [art]
                                      ;; note closure over "vid"
                                      {:id    (vid "id"   )
                                       :title (vid "title")
                                       :url   (art "url"  )})))))

    subscribe-collectors
    report)

;;;
;;; Javascript
;;;

;;; return movieLists
;;;   .mapMany(function(m) { return m.videos })
;;;   .mapMany(
;;;     function(v) {
;;;       return v
;;;         .boxarts
;;;         .filter(
;;;           function(x) {
;;;             return x.width === 150
;;;               && x.height === 200;
;;;           })
;;;         .map(
;;;           function(x) {
;;;             return {
;;;               id: v.id,
;;;               title: v.title,
;;;               boxart: x.url
;;;             };
;;;           });
;;;     });


;;;
;;; Datapath
;;;

;;; Datapath avoids closure issues by instantiating all variables in a
;;; "unification" style. Bravo!
;;;
;;; (exist (v x)
;;;   (and
;;;     (.* (. (.* movieLists) "videos") v)
;;;     (.* (. v "boxarts") x)
;;;     (. x "width" 150)
;;;     (. x "height" 200)
;;;     (= result {
;;;          id:     (. v "id"   ),
;;;          title:  (. v "title"),
;;;          boxart: (. x "url"  )
;;;        }
;;; ) ) )


;;;    ____                 _           ___ ____
;;;   / __/_ _____ ________(_)__ ___   |_  / / /
;;;  / _/ \ \ / -_) __/ __/ (_-</ -_) / __/_  _/
;;; /___//_\_\\__/_/  \__/_/___/\__/ /____//_/

;;; Exercise 24: Retrieve each video's id, title, middle interesting
;;; moment time, and smallest box art url.

(-> (jslurp "Exercise_24.json")
    asynchronous-observable

    (.mapMany (rx/fn [genre] (-> (genre "videos")
                                 asynchronous-observable)))
    (.mapMany (rx/fn [vid]
                (let [arts (-> (vid "boxarts")
                               asynchronous-observable
                               (.reduce (rx/fn [c p]
                                          (if (< (* (c "height") (c "width"))
                                                 (* (p "height") (p "width")))
                                            c p))))
                      moments (-> (vid "interestingMoments")
                                  asynchronous-observable
                                  (.filter (rx/fn [moment]
                                             (= (moment "type") "Middle"))))
                      ]

                  (Observable/zip

                   arts
                   moments

                   (rx/fn [art moment]
                     {:id    (vid    "id"   )
                      :title (vid    "title")
                      :time  (moment "time" )
                      :url   (art    "url"  )}
                     )))
                ))

    subscribe-collectors
    report)

;;;
;;; Javascript
;;;

;;; return movieLists
;;;   .mapMany(
;;;     function(movieList) {
;;;       return movieList.videos;
;;;     })
;;;   .mapMany(
;;;     function(video) {
;;;       return Array.zip(
;;;         video
;;;           .boxarts
;;;           .reduce(
;;;             function(p, c) {
;;;               return
;;;                 c.width * c.height <
;;;                 p.width * p.height ? c : p;
;;;             }),
;;;         video
;;;           .interestingMoments
;;;           .filter(
;;;             function(m) {
;;;               return m.type === "Middle";
;;;             }),
;;;         function(b,m) {
;;;           return {
;;;             id: video.id,
;;;             title: video.title,
;;;             time: m.time,
;;;             url: b.url
;;;           };
;;;         });
;;;     });


;;;
;;; Datapath
;;;

;;; (exist (video boxart moment)
;;;   (and
;;;     (.* (. (.* movieLists) "videos") video)
;;;     (min
;;;       (size boxart)
;;;       (and
;;;         (.* (. video "boxarts") boxart)
;;;         (*
;;;           (. boxart "width")
;;;           (. boxart "height")
;;;           size))
;;;       boxart)
;;;     (.* (. video "interestingMoments") moment)
;;;     (. moment "type" "Middle")
;;;     (= result
;;;        { id:    (. video "id"   ),
;;;          title: (. video "title"),
;;;          url:   (. boxart "url" ),
;;;          time:  (. moment "time")
;;;        })
;;;   ))

;;;    ____                 _           ___  ____
;;;   / __/_ _____ ________(_)__ ___   |_  |/ __/
;;;  / _/ \ \ / -_) __/ __/ (_-</ -_) / __//__ \
;;; /___//_\_\\__/_/  \__/_/___/\__/ /____/____/

;;; Exercise 25: Join Arrays to Tree

;;; We have 2 arrays containing genre ids and videos respectively.  Each
;;; video has a listId field indicating its genre.  We want an array of
;;; genres, each with a name and a videos array.  The videos array will
;;; contain the video's id and title.

;;; Input

;;; lists:
;;;         [
;;;             {
;;;                 "id": 5434364,
;;;                 "name": "New Releases"
;;;             },
;;;             {
;;;                 "id": 65456475,
;;;                 name: "Thrillers"
;;;             }
;;;         ]
;;;
;;; videos:
;;;         [
;;;             {
;;;                 "listId": 5434364,
;;;                 "id": 65432445,
;;;                 "title": "The Chamber"
;;;             },
;;;             {
;;;                 "listId": 5434364,
;;;                 "id": 675465,
;;;                 "title": "Fracture"
;;;             },
;;;             {
;;;                 "listId": 65456475,
;;;                 "id": 70111470,
;;;                 "title": "Die Hard"
;;;             },
;;;             {
;;;                 "listId": 65456475,
;;;                 "id": 654356453,
;;;                 "title": "Bad Boys"
;;;             }
;;;         ]
;;; Output
;;;
;;; [
;;;     {
;;;         "name": "New Releases",
;;;         "videos": [
;;;             {
;;;                 "id": 65432445,
;;;                 "title": "The Chamber"
;;;             },
;;;             {
;;;                 "id": 675465,
;;;                 "title": "Fracture"
;;;             }
;;;         ]
;;;     },
;;;     {
;;;         "name": "Thrillers",
;;;         "videos": [
;;;             {
;;;                 "id": 70111470,
;;;                 "title": "Die Hard"
;;;             },
;;;             {
;;;                 "id": 654356453,
;;;                 "title": "Bad Boys"
;;;             }
;;;         ]
;;;     }
;;; ]

(let [lists  (-> (jslurp "Exercise_25_lists.json")  asynchronous-observable)
      videos (-> (jslurp "Exercise_25_videos.json") asynchronous-observable)]

  (-> lists
      (.map (rx/fn [lyst]
              {:name (lyst "name")
               :videos
               (-> videos
                   ;; The following contains the "join condition."
                   (.filter (rx/fn [vid] (== (vid "listId") (lyst "id"))))
                   (.map    (rx/fn [vid] {:id    (vid "id"   )
                                          :title (vid "title")}))
                   )
              }))

      subscribe-collectors
      report

      :onNext

      ((flip map)
       (fn [lyst]
         {:name   (lyst :name)
          :videos (-> (lyst :videos)
                      subscribe-collectors
                      report
                      :onNext)
          }))))

;;;
;;; Javascript
;;;

;;; return lists.map(
;;;   function (list) {
;;;     return {
;;;       name: list.name,
;;;       videos: videos
;;;         .filter(
;;;           function (video) {
;;;             return video.listId === list.id;
;;;           })
;;;         .map(
;;;           function (video) {
;;;             return {
;;;               id: video.id,
;;;               title: video.title
;;;             };
;;;           })
;;;     };
;;;   });

;;;
;;; Datapath
;;;

;;; (exist (list)
;;;   (and
;;;     (.* lists list)
;;;     (= result {
;;;         name:   (. list "name"),
;;;         videos:
;;;           (list (v)
;;;             (exist (video)
;;;               (and
;;;                 (.* videos video)
;;;                 (. video "listId" (. list "id"))
;;;                 (= v { id:    (. video "id"   ),
;;;                        title: (. video "title")
;;;                      }
;;;           ) ) ) )
;;;       }
;;; ) ) )



;;;    _  __           __             _____
;;;   / |/ /_ ____ _  / /  ___ ____  / ___/__ ___ _  ___ ___
;;;  /    / // /  ' \/ _ \/ -_) __/ / (_ / _ `/  ' \/ -_|_-<
;;; /_/|_/\_,_/_/_/_/_.__/\__/_/    \___/\_,_/_/_/_/\__/___/


(defn magic [x y]
  (lazy-seq (cons y (magic y (+ x y)))))

(def fibs (magic 1N 1N))

(pdump (first (drop 1000 fibs)))

(defn divides?
  "Tests whether k divides n; the order of the arguments is in the sense of
   an infix operator: read (divides? k n) as \"k divides? n\"."
  [k n] (== 0 (rem n k)))

(def does-not-divide? (complement divides?))

(defn sieve [xs]
  (if (empty? xs)
    ()
    (cons (first xs)
          (lazy-seq (sieve
                     (filter (partial does-not-divide? (first xs))
                             (rest xs)))))))

(def primes (sieve (cons 2 (iterate (partial + 2N) 3))))

(pdump (first (drop 1000 primes)))

;;;    ____     __     _         __
;;;   / __/_ __/ /    (_)__ ____/ /_
;;;  _\ \/ // / _ \  / / -_) __/ __/
;;; /___/\_,_/_.__/_/ /\__/\__/\__/
;;;              |___/

;;; Enable reactive scheme on REST.  Transformed observables are
;;; "Subjects:" both observers and observables.  As observers, they
;;; subscribe (in a privileged subscriber list) to their antecedents.
;;; As observables, they are ordinary.  They may suffer additional
;;; transformations, becoming antecedents of other observables.  Or they
;;; may be simply observed by egress observers.

(pdump
 (let [obl1 (PublishSubject/create)]

   (.onNext obl1 41)

   (let [obl2 (-> obl1
                  (.map (rx/fn [x] (+ 100 x)))
                  (.filter (rx/fn* even?))
                  (.mapMany (rx/fn [obn]
                              (Observable/create
                               (rx/fn [obr]
                                 (.onNext obr obn)
                                 (.onNext obr (* obn obn))
                                 (.onCompleted obr))))))
         result (subscribe-collectors obl2)]

     (.onNext obl1 42)
     (.onNext obl1 43)
     (.onNext obl1 44)

     (.unsubscribe (:subscription result))

     (.onNext obl1 45)
     (.onNext obl1 46)

     (.onCompleted obl1)

     ((:reporter result))
     )))

;;;           __ _        _   _
;;;  _ _ ___ / _| |___ __| |_(_)___ _ _
;;; | '_/ -_)  _| / -_) _|  _| / _ \ ' \
;;; |_| \___|_| |_\___\__|\__|_\___/_||_|

;;; The following shows how to print the current members of the
;;; Observable class.

(pdump
 (into #{}
       (map (comp #(% 1) first)
            (sort-by
             :name
             (filter
              :exception-types
              (:members (r/reflect Observable :ancestors true)))))))
