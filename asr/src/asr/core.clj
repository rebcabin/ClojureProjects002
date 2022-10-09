(ns asr.core
  (:gen-class)
  (:use [asr.utils]
        [asr.data]
        [asr.grammar]
        [asr.parsed]
        [asr.autospecs])
  (:require [clojure.spec.alpha            :as    s             ]
            [clojure.pprint                :refer [pprint]      ]
            [clojure.zip                   :as    zip           ]
            [clojure.spec.gen.alpha        :as    gen           ]
            [clojure.spec.test.alpha       :as    stest         ]
            [clojure.test.check.generators :as    tgen          ]
            [clojure.math.numeric-tower    :refer [expt]        ] ;; bigint
            [clojure.inspector             :refer [inspect-tree]]
            [clojure.math                  :refer [pow]         ] ;; int
            [clojure.set                   :as    set           ]))

(println "+-------------------------------+")
(println "|                               |")
(println "|     Try this at the REPL:     |")
(println "|                               |")
(println "|     (-main)                   |")
(println "|     (s/exercise ::binop)      |")
(println "|     (s/describe ::binop)      |")
(println "|                               |")
(println "+-------------------------------+")

;;  ___                   _               _        _   _  _         _
;; | __|_ ___ __  ___ _ _(_)_ __  ___ _ _| |_ __ _| | | || |___ _ _(_)______ _ _
;; | _|\ \ / '_ \/ -_) '_| | '  \/ -_) ' \  _/ _` | | | __ / _ \ '_| |_ / _ \ ' \
;; |___/_\_\ .__/\___|_| |_|_|_|_\___|_||_\__\__,_|_| |_||_\___/_| |_/__\___/_||_|
;;         |_|

;;; Code below this line, up to the lines marked "Production
;;; Horizon" is experimental. Most of it concerns automatically
;;; generating syntactically valid nonsense ASR
;;; programs (SynNASR).

#_
(defn symconst-spec-for-term
  "### Symconst Spec for Term [sic]

  For each term, write a `set` containing its alternative heads,
  e.g., the term `binop` is one of the ten heads `Add`, `Sub`, and
  so on, to `BitRShift`.

  To unit-test `spec-for-term`, `eval` one of them and check it:
  "
  [stuffs-for-term]
  (let [term (-> stuffs-for-term first :term)
        ;; same for all! TODO: assert
        term-nskw (nskw-kebab-from (name term))
        ss1
        (->> stuffs-for-term
             (map (fn [stuff]
                    (let [head
                          (head-from-kind-form
                           (:kind stuff) (:form stuff))]
                      (-> head symbol)))))]
    `(s/def ~term-nskw (set (quote ~ss1)))))


;;; TEACHING NOTE: Experiment that failed.

"## Spec for *identifier*

We can't use just `symbol?` because it generates namespaced
symbols, and they aren't useful for testing LPython. We'll need a
custom
generator (<https://clojure.org/guides/spec#_custom_generators>).

The following attempt has performance problems and will be
discarded. We save it as a lesson in this kind of dead end.
"

#_(def identifier-re #"[a-zA-Z_][a-zA-Z0-9_]*")

#_(s/def ::identifier
  (s/with-gen
    symbol?
    (fn []
      (gen/such-that
       #(re-matches
         identifier-re
         (name %))
       (gen/symbol)))))

;;; Better solution that, sadly but harmlessly, lacks underscores
;;; because gen/char-alpha doesn't generate underscores. TODO: fix
;;; this.

(let [alpha-re #"[a-zA-Z]"  ;; The famous "let over lambda."
      alphameric-re #"[a-zA-Z0-9]*"]
  (def alpha?
    #(re-matches alpha-re %))
  (def alphameric?
    #(re-matches alphameric-re %))
  (defn identifier? [s]
    (and (alpha? (subs s 0 1))
         (alphameric? (subs s 1))))
  (def identifier-generator
    (tgen/let [c (gen/char-alpha)
               s (gen/string-alphanumeric)]
      (str c s)))
  (s/def ::identifier  ;; side effects the spec registry!
    (s/with-gen
      identifier?
      (fn [] identifier-generator))))


(defn heads-for-composite
  "Produce a list of symbolic heads (like 'RealUnaryMinus and
  'ArraySection), from a term like :asr.core/expr. See
  all-heads-for-exprs-test in core_test.clj."
  [term]
  (->> big-map-of-speclets-from-terms
       term
       (map :ASDL-COMPOSITE)
       (map :ASDL-HEAD)
       (map symbol)
       set))


(defn dummy-generator-for-heads
  "A dummy generator for argument lists for heads which just
  inserts a list of random length of random identifiers. Not
  suitable long-term."
  [heads]
  (tgen/let [head (s/gen heads)
             rest (gen/list (s/gen ::identifier))]
    (cons head rest)))


(defn dummy-lpred
  "A predicate for dummy specs that checks simply that the
  instance is a list with an appropriate head and zero or
  more items of any type. Not suitable long-term."
  [heads]
  (s/and seq?
         (fn [lyst] (-> lyst count (>= 1)))
         (fn [lyst] (-> lyst first heads))))


;;                        __                                     _ _
;;  ____ __  ___ __ ___  / _|___ _ _   __ ___ _ __  _ __  ___ __(_) |_ ___ ___
;; (_-< '_ \/ -_) _(_-< |  _/ _ \ '_| / _/ _ \ '  \| '_ \/ _ (_-< |  _/ -_|_-<
;; /__/ .__/\___\__/__/ |_| \___/_|   \__\___/_|_|_| .__/\___/__/_|\__\___/__/
;;    |_|                                          |_|


;;; Try (s/exercise ::symbol) and (s/exercise ::expr in the REPL.

(let [heads (heads-for-composite ::symbol)]
  (s/def ::symbol
    (s/with-gen
      (dummy-lpred heads)
      (fn [] (dummy-generator-for-heads heads)))))


(let [heads (heads-for-composite ::expr)]
  (s/def ::expr
    (s/with-gen
      (dummy-lpred heads)
      (fn [] (dummy-generator-for-heads heads)))))


(let [heads (heads-for-composite ::stmt)]
  (s/def ::stmt
    (s/with-gen
      (dummy-lpred heads)
      (fn [] (dummy-generator-for-heads heads)))))


;;  __ _ _ _ __ _ ___
;; / _` | '_/ _` (_-<
;; \__,_|_| \__, /__/
;;          |___/


(defn spec-from-arg
  "### Spec Fragment from Arg, Args

  Convert multiplicities into clojure.spec equivalents.
  "
  [arg]
  (let [type (nskw-kebab-from (:ASDL-TYPE arg))
        nym (:ASDL-NYM arg)]
    (case (:MULTIPLICITY arg)
      :asr.parsed/once `(s/spec ~type)
      :asr.parsed/at-most-once `(s/? (s/spec ~type))
      :asr.parsed/zero-or-more `(s/* (s/spec ~type)))))


(defn spec-from-args [args]
  (let [nyms (->> args (map :ASDL-NYM)           #_echo)
        kyms (->> nyms (map (comp keyword name)) #_echo)
        specules (->> args (map spec-from-arg)   #_echo)
        riffle (-> (interleave kyms specules)    #_echo)]
    `(s/cat ~@riffle)))


(defn spec-from-head-and-args [head args]
  (let [nyms (->> args (map :ASDL-NYM)           #_echo)
        kyms (->> nyms (map (comp keyword name)) #_echo)
        specules (->> args (map spec-from-arg)   #_echo)
        riffle (-> (interleave kyms specules)    #_echo)
        lpred  #(= % head)
        headed (cons :head (cons lpred riffle))]
    `(s/cat ~@headed)))

;;  _             _
;; | |_ _  _ _ __| |___ ___
;; |  _| || | '_ \ / -_|_-<
;;  \__|\_,_| .__/_\___/__/
;;          |_|


(defn tuple-head-spec-from-stuff [tuple-stuff]
  #_(println "tuple-head-spec-from-stuff")
  (let [nskw (-> tuple-stuff :head name nskw-kebab-from #_echo)
        args (-> tuple-stuff :form :ASDL-ARGS           #_echo)]
    `(s/def ~nskw ~(spec-from-args args))))  ;; side effect!


(def tuple-stuffs
  "# Tuple Specs

  There are six tuple heads. Their names will change from
  run-to-run because the names are gensymmed.
  "
  (filter #(= (:kind %) :ASDL-TUPLE)
          big-list-of-stuff))


(def tuple-stuffss-by-term
  "## Tuple Term-Specs

  As before, we really need clojure.specs for the terms
  corresponding to the heads.

  ### Tuple Stuffss [sic] by Term (one extra level of lists)
  "
  (partition-by :term tuple-stuffs))


;; ### Register

;; To register the 6 term-specs, `eval` them.

(defn tuple-term-spec-from-stuffs [stuffs]
  #_(println "tuple-term-spec-from-stuffs")
  (let [term (-> stuffs first :term                      #_echo)
        nskw (-> term name nskw-kebab-from               #_echo)
        head (-> stuffs first :head name nskw-kebab-from #_echo)]
    `(s/def ~nskw (s/spec ~head))))


;;   __            __                   __
;;  / /___ _____  / /__   ______ ______/ /__
;; / __/ // / _ \/ / -_) / __/ // / __/ / -_)
;; \__/\_,_/ .__/_/\__/  \__/\_, /\__/_/\__/
;;        /_/               /___/
;;    __                __    _
;;   / /  _______ ___ _/ /__ (_)__  ___ _
;;  / _ \/ __/ -_) _ `/  '_// / _ \/ _ `/
;; /_.__/_/  \__/\_,_/_/\_\/_/_//_/\_, /
;;                                /___/


;;; Manual topological sort shows we must spec ::dimension before
;;; the others.

(defn do-one-tuple-spec-head-and-term!
  "Spec one tuple type, head-spec and term-spec, by term."
  [term]

  (->> tuple-stuffs
      (filter #(= term (-> % :term)))
      (map tuple-head-spec-from-stuff)
      (map eval)
      echo)

 (->> tuple-stuffss-by-term
      (filter #(= term (-> % first :term)))
      (map tuple-term-spec-from-stuffs)
      (map eval)
      echo))


;;                _         _     _        _    _
;;  ____  _ _ __ | |__  ___| |___| |_ __ _| |__| |___
;; (_-< || | '  \| '_ \/ _ \ |___|  _/ _` | '_ \ / -_)
;; /__/\_, |_|_|_|_.__/\___/_|    \__\__,_|_.__/_\___|
;;     |__/


;; # Hand-Written Term Spec for SymbolTable

;; ASDL doesn't offer an easy way to specify maps, but
;; Clojure.spec does. ASR.asdl doesn't have a spec for
;; `SymbolTable`, so we write one by hand and upgrade it as we go
;; along.

;; We cannot define this spec until we define the others
;; because `(s/spec ::symbol)` doesn't exist yet. We'll backpatch
;; it later


(s/def ::symbol-table
  (s/cat
   :head #(= % 'SymbolTable)
   :unique-id int?
   :symbols (s/map-of keyword?
                      (s/spec ::symbol))))


(defn spec-from-composite
  "# Back-patching Symbol

  TODO

  # First Composite Spec: `TranslationUnit`

  Write specs as data lists and `eval` them later. Turns out it's
  necessary to do that, and it's a beneficial accident lest we
  clutter up the namespace of specs.

  Composites and tuples have lists of type-var pairs, that is, of
  args. We've already handled arg lists in `spec-from-args` above.

  Specs for all tuples' heads and terms have already been
  registered.

  Specs for all symconsts' heads and terms have already been
  registered.
  "
  [composite]
  (let [head (-> composite :ASDL-HEAD symbol echo)
        nskw (-> head nskw-kebab-from        echo)
        args (-> composite :ASDL-ARGS        #_echo)]
    `(s/def ~nskw ~(spec-from-head-and-args head args))))


;;                       _        _
;;  ____ __  ___ __   __| |_ __ _| |_ ___
;; (_-< '_ \/ -_) _| (_-<  _/ _` |  _(_-<
;; /__/ .__/\___\__| /__/\__\__,_|\__/__/
;;    |_|


(defn only-asr-specs
  "Filter non-asr specs from the keys of the spec registry."
  []
  (filter
   #(= (namespace %) "asr.core")
   (keys (s/registry))))


(defn check-registry
  "Print specs defined in the namespace 'asr.core.' Call this at the
  REPL."
  []
  (pprint (only-asr-specs)))


(defn count-asr-specs
  "Count the asr specs. Call this at the REPL."
  []
  (count (only-asr-specs)))


;;     _
;;  __| |___ ___ ____  _ _ _  _ _  __ _ ____ _
;; / _` / _ \___(_-< || | ' \| ' \/ _` (_-< '_|
;; \__,_\___/   /__/\_, |_||_|_||_\__,_/__/_|
;;                  |__/

;; At this point, we distinguish syntactically correct nonsense
;; ASR (SynNASR) from semantically constrained nonsense
;; ASR (SemNASR).
;;
;; - SynNASR is for testing error paths in the ASR backends. A
;;   random utterance is overwhelmingly unlikely to be meaningful,
;;   but it must NEVER crash a back-end nor cause it to go into an
;;   infinite loop (spin). An example is a IntegerBinOp expression
;;   with a string and a float as arguments. ASDL allows this, but
;;   backends must reject it.
;;
;; - SemNASR is for testing happy paths in the backends. SemNASR
;;   should be semantically valid, we should be able to
;;   independently compute results, and we should be able to
;;   round-trip examples. For example, an IntegerBinOp with two
;;   IntegerConstant instances of the same kind, say i16, should
;;   generate code to compute the results, or perhaps, with
;;   optimizations turned on, compute the results at compile time.
;;   Types must match and zero divisors must be rejected. Such
;;   checks are distinct layers or sibling domains of semantics.
;;
;; We will write Clojure specs for both SynNASR and SemNASR.
;; clojure.spec.gen.alpha, clojure.spec.test.alpha, and
;; clojure.test.check.generators give us ways to quickly generate
;; large numbers of NASR strings/trees. SynNASR is the default
;; because we can generate SynNASR directly from the ASDL grammar.
;; Names of terms and heads from ASDL with no `semnasr` suffix are
;; SynNASR. SemNASR requires humans to write at least parts of the
;; specs. Our first example of SemNASR will be IntegerBinOp.

(defn do-synnasr
  "Automated items for the spec registry. W.I.P."
  []

  (print "symconst head specs: ")

  (->> symconst-stuffs
       (map spec-from-symconst-stuff)
       (map eval)
       count
       echo)

  (print "symconst term specs: ")

  (->> symconst-stuffss-by-term
       (map symconst-spec-for-term)
       (map eval)
       count
       echo)

;;; We need a cycle-breaking spec for dimension to bootstrap the
;;; following constructions.

  (println "cycle-breaking with ::dimension")

  (do-one-tuple-spec-head-and-term! ::dimension)

  (println "tuple heads and terms are 1-to-1, unlike symconsts an composites.")
  (print "tuple head specs: ")

  (->> tuple-stuffs
       (map tuple-head-spec-from-stuff)
       (map eval)
       count
       echo)

  (print "tuple term specs: ")

  (->> tuple-stuffss-by-term
       (map tuple-term-spec-from-stuffs)
       #_echo
       (map eval)
       count
       echo)

  ;;  _ _               _         _
  ;; (_|_)____  _ _ __ | |__  ___| |
  ;;  _ _(_-< || | '  \| '_ \/ _ \ |
  ;; (_|_)__/\_, |_|_|_|_.__/\___/_|
  ;;         |__/

  (println "dummy spec for symbol: ")

  (let [heads (heads-for-composite ::symbol)]
    (->> (s/def ::symbol
           (s/with-gen
             (dummy-lpred heads)
             (fn [] (dummy-generator-for-heads heads))))
         echo))
  (pprint (s/describe ::symbol))

  ;;  _ _
  ;; (_|_)_____ ___ __ _ _
  ;;  _ _/ -_) \ / '_ \ '_|
  ;; (_|_)___/_\_\ .__/_|
  ;;             |_|

  (println "dummy spec for expr: ")

  (let [heads (heads-for-composite ::expr)]
    (->> (s/def ::expr
           (s/with-gen
             (dummy-lpred heads)
             (fn [] (dummy-generator-for-heads heads))))
         echo))
  (pprint (s/describe ::expr))

  ;;  _ _    _        _
  ;; (_|_)__| |_ _ __| |_
  ;;  _ _(_-<  _| '  \  _|
  ;; (_|_)__/\__|_|_|_\__|

  (println "dummy spec for stmt: ")

  (let [heads (heads-for-composite ::stmt)]
    (->> (s/def ::stmt
           (s/with-gen
             (dummy-lpred heads)
             (fn [] (dummy-generator-for-heads heads))))
         echo))
  (pprint (s/describe ::stmt))

  ;;  _ _ _   _
  ;; (_|_) |_| |_ _  _ _ __  ___
  ;;  _ _|  _|  _| || | '_ \/ -_)
  ;; (_|_)\__|\__|\_, | .__/\___|
  ;;              |__/|_|

  (println "dummy spec for ttype: ")

  (let [heads (heads-for-composite ::ttype)]
    (->> (s/def ::ttype
           (s/with-gen
             (dummy-lpred heads)
             (fn [] (dummy-generator-for-heads heads))))
         echo))
  (pprint (s/describe ::ttype))


  ;; This isn't good enough. Let's write some head specs for it by
  ;; hand.

  (s/def ::int   int?)
  (s/def ::float float?)
  (s/def ::bool  (s/or :clj-bool boolean?
                       :asr-bool #(or (= % '.true.) (= % '.false))))

  ;; ttype
  ;;     = Integer(int kind, dimension* dims)
  ;;     | ...

  ;; WORK-IN-PROGRESS
  )

;;  ___             _         _   _            _  _         _
;; | _ \_ _ ___  __| |_  _ __| |_(_)___ _ _   | || |___ _ _(_)______ _ _
;; |  _/ '_/ _ \/ _` | || / _|  _| / _ \ ' \  | __ / _ \ '_| |_ / _ \ ' \
;; |_| |_| \___/\__,_|\_,_\__|\__|_\___/_||_| |_||_\___/_| |_/__\___/_||_|

;; Code below this line is ready for testing in production.

;; Define some automatically generated SynNASR specs. This is
;; experimental, but some of the subsequent production specs
;; may depend on some of these definitions.

(do-synnasr)

;;  _ _ _     _                        _    _
;; (_|_|_)_ _| |_ ___ __ _ ___ _ _ ___| |__(_)_ _ ___ ___ _ __
;;  _ _| | ' \  _/ -_) _` / -_) '_|___| '_ \ | ' \___/ _ \ '_ \
;; (_|_)_|_||_\__\___\__, \___|_|     |_.__/_|_||_|  \___/ .__/
;;                   |___/                               |_|

;;  ___     _                      _   _
;; |_ _|_ _| |_ ___ __ _ ___ _ _  | |_| |_ _  _ _ __  ___
;;  | || ' \  _/ -_) _` / -_) '_| |  _|  _| || | '_ \/ -_)
;; |___|_||_\__\___\__, \___|_|    \__|\__|\_, | .__/\___|
;;                 |___/                   |__/|_|


;; NOTA BENE: s/cat specs wrapped in s/spec are nestable.
;; Unwrapped regex specs are spliced. Regex specs arise from s/*,
;; s/+, s/?, s/alt, s/cat, as explained here
;; https://clojure.org/guides/spec#_sequences. This point bears
;; emphasis because, although the docs are clear, it takes some
;; experience to internalize the conceptual differences between
;; regex sequences and normal specs combined with s/and and s/or.

;;  _ _    _ _                   _
;; (_|_)__| (_)_ __  ___ _ _  __(_)___ _ _
;;  _ _/ _` | | '  \/ -_) ' \(_-< / _ \ ' \
;; (_|_)__,_|_|_|_|_\___|_||_/__/_\___/_||_|

;; Redefinition; try (s/exercise ::dimension

(defn bigint?
  "Doesn't seem to be defined in system-supplied libraries."
  [n]
  (instance? clojure.lang.BigInt n))

;; Overwrite print-method for clojure BigInt to get rid of
;; the "N" at the end (can't do this inside (-main) lest
;; compile errors).

(import '(java.io Writer))
(defmethod print-method clojure.lang.BigInt
  [b, ^Writer w]
  (.write w (str b))
  #_(.write "N"))

(s/def ::bignat
  (s/with-gen
    (s/and bigint? #(>= % 0))
    ;; size-bounded-bignat is not public, else I would call it
    (fn [] tgen/size-bounded-bigint)))

;; C-c C-v C-f C-c e to generate pretty-printed comments. Then
;; stub off the call to save a tiny bit of runtime. Remove the
;; #_ and press C-c C-c in the expression to see results in a
;; CIDER Emacs buffer. We follow this convenience convention
;; frequently in this development section. Comments are cheap.

#_(->> ::bignat s/exercise (map second))
;; => (7 13 63 98225932 4572 28 31914670493 80 252 256185)


(s/def ::dimensions
  (s/coll-of (s/or :nat-int nat-int?, :bigint ::bignat)
             :min-count 0,
             :max-count 2,
             :into []))

#_(-> ::dimensions (s/exercise 4))
;; => ([[1 0] [[:nat-int 1] [:bigint 0]]]
;;     [[1 0] [[:nat-int 1] [:nat-int 0]]]
;;     [[] []]
;;     [[459] [[:bigint 459]]])

(s/def ::integer-ttype-semnasr
  (s/spec              ; means "nestable" not "spliceable" in other "regex" specs
   (s/cat :head        #{'Integer}
          :kind        #{1 2 4 8} ;; i8, i16, i32, i64
          :dimensionss (s/+ ::dimensions))))

#_(->> ::integer-ttype-semnasr
     s/gen
     gen/generate)
;; => (Integer ;; head
;;     4       ;; kind
;;     [3223318265799195456]  ;; (dimensionss), bunch of dimensions
;;     []
;;     [28]
;;     [8 313679744843364260991]
;;     [87100772691971102709151610292570444761])


;; Often, we need a scalar that has no dimensionss [sic].

(s/def ::integer-scalar-ttype-semnasr
  (s/spec
   (s/cat :head        #{'Integer}
          :kind        #{1 2 4 8}
          :dimensionss #{[]})))

(-> ::integer-scalar-ttype-semnasr
      (s/exercise 4))
;; => ([(Integer 2 []) {:head Integer, :kind 2, :dimensionss []}]
;;     [(Integer 4 []) {:head Integer, :kind 4, :dimensionss []}]
;;     [(Integer 1 []) {:head Integer, :kind 1, :dimensionss []}]
;;     [(Integer 8 []) {:head Integer, :kind 8, :dimensionss []}])

;; Particular ones so we can match the "kind," i8 thru i64, by
;; hand. Written in this funny way for test-generation.

(do
  (s/def ::i8-scalar-ttype-semnasr
    (s/spec ; nestable
     (s/cat :head        #{'Integer}
            :kind        #{1}
            :dimensionss #{[]})))

  (s/def ::i16-scalar-ttype-semnasr
    (s/spec ; nestable
     (s/cat :head        #{'Integer}
            :kind        #{2}
            :dimensionss #{[]})))

  (s/def ::i32-scalar-ttype-semnasr
    (s/spec ; nestable
     (s/cat :head        #{'Integer}
            :kind        #{4}
            :dimensionss #{[]})))

  (s/def ::i64-scalar-ttype-semnasr
    (s/spec ; nestable
     (s/cat :head        #{'Integer}
            :kind        #{8}
            :dimensionss #{[]}))))

#_(-> ::i64-scalar-ttype-semnasr
    (s/exercise 2))
;; => ([(Integer 8 []) {:head Integer, :kind 8, :dimensionss []}]
;;     [(Integer 8 []) {:head Integer, :kind 8, :dimensionss []}])

;; TEACHING NOTE: With nesting. Because every spec is wrapped in
;; s/spec, results are nested under s/alt.

#_(-> (s/alt ::i8-scalar-ttype-semnasr
           ::i16-scalar-ttype-semnasr
           ::i32-scalar-ttype-semnasr
           ::i64-scalar-ttype-semnasr)
    (s/exercise 2))
;; => ([[(Integer 2 [])]
;;      [:asr.core/i8-scalar-ttype-semnasr
;;       {:head Integer, :kind 2, :dimensionss []}]]
;;     [[(Integer 8 [])]
;;      [:asr.core/i32-scalar-ttype-semnasr
;;       {:head Integer, :kind 8, :dimensionss []}]])

;; TEACHING NOTE: Without nesting. Results are not nested under
;; s/or.

#_(-> (s/or :1 ::i8-scalar-ttype-semnasr
          :2 ::i16-scalar-ttype-semnasr
          :4 ::i32-scalar-ttype-semnasr
          :8 ::i64-scalar-ttype-semnasr)
    (s/exercise 2))
;; => ([(Integer 8 []) [:8 {:head Integer, :kind 8, :dimensionss []}]]
;;     [(Integer 1 []) [:1 {:head Integer, :kind 1, :dimensionss []}]])


;;  _     _                               _
;; (_)_ _| |_ ___ __ _ ___ _ _  __ ____ _| |_  _ ___ ___
;; | | ' \  _/ -_) _` / -_) '_| \ V / _` | | || / -_|_-<
;; |_|_||_\__\___\__, \___|_|    \_/\__,_|_|\_,_\___/__/
;;               |___/


;; Mid-level specs for fixed-width integers.

(letfn [(b [e] (expt 2 (- e 1)))        ; ::i8, ::i16, ::i32, ::i64
        (gmkr [e]
          (let [b_ (b e)]
            (tgen/large-integer* {:min (- b_) :max (- b_ 1)})))
        (smkr [e]
          (let [b_ (b e)]
            (s/and int? #(>= % (- b_)) #(< % b_))))]
  (let [gi8  (fn [] (gmkr 8))
        gi16 (fn [] (gmkr 16))
        gi32 (fn [] (gmkr 32))
        gi64 (fn [] (gmkr 64))
        si8  (smkr 8)
        si16 (smkr 16)
        si32 (smkr 32)
        si64 (smkr 64)]
    (s/def ::i8  (s/spec  si8 :gen  gi8)) ; s/spec means
    (s/def ::i16 (s/spec si16 :gen gi16)) ; "nestable"
    (s/def ::i32 (s/spec si32 :gen gi32))
    (s/def ::i64 (s/spec si64 :gen gi64))))

;; for interactive testing in CIDER:
;; (s/exercise ::i8)
;; (s/exercise ::i16)
;; (s/exercise ::i32)
;; (s/exercise ::i64 100)

(assert (s/valid? ::i32 (Integer/MAX_VALUE)))
(assert (s/valid? ::i32 0))

;;  ____              ___      _        _   _
;; |_  /___ _ _ ___  | _ \___ (_)___ __| |_(_)___ _ _
;;  / // -_) '_/ _ \ |   / -_)| / -_) _|  _| / _ \ ' \
;; /___\___|_| \___/ |_|_\___|/ \___\__|\__|_\___/_||_|
;;                          |__/

(letfn [(b [e] (expt 2 (- e 1)))       ; ::i8, ::i16, ::i32, ::i64
        (gmkr [e]
          (let [b_ (b e)]
            (tgen/large-integer* {:min (- b_) :max (- b_ 1)})))
        (smkr [e]
          (let [b_ (b e)]
            (s/and int?
                   #(not (zero? %))
                   #(>= % (- b_)) #(< % b_))))]
  (let [gi8nz  (fn [] (gmkr 8))
        gi16nz (fn [] (gmkr 16))
        gi32nz (fn [] (gmkr 32))
        gi64nz (fn [] (gmkr 64))
        si8nz  (smkr 8)
        si16nz (smkr 16)
        si32nz (smkr 32)
        si64nz (smkr 64)]
    (s/def ::i8nz  (s/spec  si8nz :gen  gi8nz)) ; s/spec means
    (s/def ::i16nz (s/spec si16nz :gen gi16nz)) ; "nestable"
    (s/def ::i32nz (s/spec si32nz :gen gi32nz))
    (s/def ::i64nz (s/spec si64nz :gen gi64nz))))

;; for interactive testing in CIDER:
;; (s/exercise ::i8nz)
;; (s/exercise ::i16nz)
;; (s/exercise ::i32nz)
;; (s/exercise ::i64nz)

(assert (s/valid? ::i32nz (Integer/MAX_VALUE)))
(assert (not (s/valid? ::i32nz 0)))

;;  _     _                                       _            _
;; (_)_ _| |_ ___ __ _ ___ _ _ ___ __ ___ _ _  __| |_ __ _ _ _| |_
;; | | ' \  _/ -_) _` / -_) '_|___/ _/ _ \ ' \(_-<  _/ _` | ' \  _|
;; |_|_||_\__\___\__, \___|_|     \__\___/_||_/__/\__\__,_|_||_\__|
;;               |___/

(do
  (s/def ::i8-constant-semnasr
    (s/spec
     (s/cat :head  #{'IntegerConstant}
            :value ::i8
            :ttype ::i8-scalar-ttype-semnasr)))

  (s/def ::i16-constant-semnasr
    (s/spec
     (s/cat :head  #{'IntegerConstant}
            :value ::i16
            :ttype ::i16-scalar-ttype-semnasr)))

  (s/def ::i32-constant-semnasr
    (s/spec
     (s/cat :head  #{'IntegerConstant}
            :value ::i32
            :ttype ::i32-scalar-ttype-semnasr)))

  (s/def ::i64-constant-semnasr
    (s/spec
     (s/cat :head  #{'IntegerConstant}
            :value ::i64
            :ttype ::i64-scalar-ttype-semnasr))))

(s/def ::integer-constant-semnasr
  (s/or :i8  ::i8-constant-semnasr
        :i16 ::i16-constant-semnasr
        :i32 ::i32-constant-semnasr
        :i64 ::i64-constant-semnasr))

;; for interactive testing in CIDER:
#_(-> ::integer-constant-semnasr
    (s/exercise 2))
;; => ([(IntegerConstant 0 (Integer 2 []))
;;      [:i16
;;       {:head IntegerConstant,
;;        :value 0,
;;        :ttype {:head Integer, :kind 2, :dimensionss []}}]]
;;     [(IntegerConstant -1 (Integer 4 []))
;;      [:i32
;;       {:head IntegerConstant,
;;        :value -1,
;;        :ttype {:head Integer, :kind 4, :dimensionss []}}]])


(do
  (s/def ::i8-non-zero-constant-semnasr
    (s/spec
     (s/cat :head  #{'IntegerConstant}
            :value ::i8nz
            :ttype ::i8-scalar-ttype-semnasr)))

  (s/def ::i16-non-zero-constant-semnasr
    (s/spec
     (s/cat :head  #{'IntegerConstant}
            :value ::i16nz
            :ttype ::i16-scalar-ttype-semnasr)))

  (s/def ::i32-non-zero-constant-semnasr
    (s/spec
     (s/cat :head  #{'IntegerConstant}
            :value ::i32nz
            :ttype ::i32-scalar-ttype-semnasr)))

  (s/def ::i64-non-zero-constant-semnasr
    (s/spec
     (s/cat :head  #{'IntegerConstant}
            :value ::i64nz
            :ttype ::i64-scalar-ttype-semnasr))))

(s/def ::integer-non-zero-constant-semnasr
  (s/or :i8  ::i8-non-zero-constant-semnasr
        :i16 ::i16-non-zero-constant-semnasr
        :i32 ::i32-non-zero-constant-semnasr
        :i64 ::i64-non-zero-constant-semnasr))

(-> ::integer-non-zero-constant-semnasr
    (s/exercise 4))
;; => ([(IntegerConstant 1 (Integer 2 []))
;;      [:i16
;;       {:head IntegerConstant,
;;        :value 1,
;;        :ttype {:head Integer, :kind 2, :dimensionss []}}]]
;;     [(IntegerConstant -1 (Integer 8 []))
;;      [:i64
;;       {:head IntegerConstant,
;;        :value -1,
;;        :ttype {:head Integer, :kind 8, :dimensionss []}}]]
;;     [(IntegerConstant -1 (Integer 8 []))
;;      [:i64
;;       {:head IntegerConstant,
;;        :value -1,
;;        :ttype {:head Integer, :kind 8, :dimensionss []}}]]
;;     [(IntegerConstant -1 (Integer 1 []))
;;      [:i8
;;       {:head IntegerConstant,
;;        :value -1,
;;        :ttype {:head Integer, :kind 1, :dimensionss []}}]])


;;  _     _                        _    _                    ___ ___ __  __
;; (_)_ _| |_ ___ __ _ ___ _ _ ___| |__(_)_ _ ___ ___ _ __  / __| __|  \/  |
;; | | ' \  _/ -_) _` / -_) '_|___| '_ \ | ' \___/ _ \ '_ \ \__ \ _|| |\/| |
;; |_|_||_\__\___\__, \___|_|     |_.__/_|_||_|  \___/ .__/ |___/___|_|  |_|
;;               |___/                               |_|


;; This section tests some SemNASR, some semantically valid
;; nonsense ASR programs. There are multiple levels of semantics,
;; and the programs in this section test them in layers.
;;
;; 1. Do types match? The tests here guarantee that by
;; construction. i32 binops can have only arguments and returns of
;; ttype [sic] i32. Ditto for i8, i16, and i64.
;;
;; 2. In the case of answers computable at compile time, do
;; answers match the computation? We will have test generators
;; that cover both yes and no cases.
;;
;; 3. Division by zero? We will have testers that generate yes and
;; no cases when the divisor is zero at compile time.
;;
;; Yes cases are expected to round-trip: the ASR should go into
;; the compiler and come back out in semantically equivalent form.
;; Testing semantical equivalence is a big TODO. No cases are
;; expected to trip compiler errors and warnings as appropriate.
;;
;; These testers are not concerned with run time.
;;
;; These testers are not concerned with SynNASR, syntactically
;; valid nonsense ASR programs. Such programs almost never make
;; semantical sense. They are for testing compiler robustness. The
;; compiler must never spin or crash.

;;; Because trees of i32 binops are of arbitrary depth, we write a
;;; recursive spec for them. This spec exercises the ASR
;;; head "IntegerBinOp"
;;;
;;;   IntegerBinOp(expr left, binop op, expr right,
;;;                ttype type, expr? value)
;;;
;;; for the cases where left, right, and value are IntegerBinOps
;;; or IntegerConstants, the base case for recursion. Later, we
;;; extend the cases to other ASR exprs. This spec WILL generate
;;;
;;; - zero divisors
;;;
;;; - cases where a result is computable but NOT provided
;;;
;;; - cases where the provided answer for a computed result is
;;;   incorrect
;;;
;;; It will NOT generate type mismatches.

(s/def ::i32-bin-op-semnasr
  (s/or
   ;; The base case is necessary. Try commenting it out and
   ;; running "lein test" at a terminal.
   :base
   (s/cat :head  #{'IntegerBinOp}
          :left  ::i32-constant-semnasr
          :op    ::binop
          :right ::i32-constant-semnasr
          :ttype ::i32-scalar-ttype-semnasr
          :value (s/? ::i32-constant-semnasr))

   :recurse
   (let [or-leaf (s/or :leaf   ::i32-constant-semnasr
                       :branch ::i32-bin-op-semnasr)]
     (s/cat :head  #{'IntegerBinOp}
            :left  or-leaf
            :op    ::binop
            :right or-leaf
            :ttype ::i32-scalar-ttype-semnasr
            :value (s/? or-leaf))) ))

#_(s/exercise ::binop)
;; => ([BitOr BitOr]
;;     [BitXor BitXor]
;;     [Div Div]
;;     [BitLShift BitLShift]
;;     [BitRShift BitRShift]
;;     [BitXor BitXor]
;;     [Div Div]
;;     [BitOr BitOr]
;;     [BitRShift BitRShift]
;;     [Div Div])

(s/def ::binop-no-div
  (set/difference (eval (s/describe ::binop))
                  #{'Div}))

#_(s/exercise ::binop-no-div)
;; => ([Mul Mul]
;;     [Add Add]
;;     [BitOr BitOr]
;;     [BitOr BitOr]
;;     [BitLShift BitLShift]
;;     [BitRShift BitRShift]
;;     [BitXor BitXor]
;;     [BitAnd BitAnd]
;;     [Sub Sub]
;;     [BitAnd BitAnd])

;; To visualize the tree, uncomment this and do "lein run" at a
;; terminal.
#_(-> ::i32-bin-op-semnasr
    (s/exercise 25)
    inspect-tree)


(s/def ::i32-bin-op-semnasr-no-zero-divisor
  (s/or
   ;; The base case is necessary. Try commenting it out and
   ;; running "lein test" at a terminal.
   :base-no-div
   (s/cat :head  #{'IntegerBinOp}
          :left  ::i32-constant-semnasr
          :op    ::binop-no-div
          :right ::i32-constant-semnasr
          :ttype ::i32-scalar-ttype-semnasr
          :value (s/? ::i32-constant-semnasr))

   :base-no-zero-divisor
   (s/cat :head  #{'IntegerBinOp}
          :left  ::i32-constant-semnasr
          :op    #{'Div}
          :right ::i32-non-zero-constant-semnasr
          :ttype ::i32-scalar-ttype-semnasr
          :value (s/? ::i32-constant-semnasr))

   :recurse-no-div
   (let [or-leaf (s/or :leaf   ::i32-constant-semnasr
                       :branch ::i32-bin-op-semnasr-no-zero-divisor)]
     (s/cat :head  #{'IntegerBinOp}
            :left  or-leaf
            :op    ::binop-no-div
            :right or-leaf
            :ttype ::i32-scalar-ttype-semnasr
            :value (s/? or-leaf)))

   :recurse-no-zero-divisor
   (let [or-leaf (s/or :leaf   ::i32-constant-semnasr
                       :branch ::i32-bin-op-semnasr-no-zero-divisor)
         nz-leaf (s/or :nz-leaf ::i32-non-zero-constant-semnasr
                       :branch ::i32-bin-op-semnasr-no-zero-divisor)]
     (s/cat :head  #{'IntegerBinOp}
            :left  or-leaf
            :op    #{'Div}
            :right nz-leaf
            :ttype ::i32-scalar-ttype-semnasr
            :value (s/? or-leaf)))))


(gen/generate (s/gen ::i32))
(gen/generate (s/gen ::binop))

;; for generation:
(defn i32-constant-semnasr
  [value]
  (let [b (expt 2 31)]
    (assert (and (>= value (- b)) (< value b))))
  (list 'IntegerConstant
        value
        '(Integer 4 [])))

(gen/generate (tgen/return (i32-constant-semnasr 42)))

(s/describe ::binop)
;; => (set
;;     '(Add Sub Mul Div Pow BitAnd BitOr BitXor BitLShift BitRShift))
(s/def ::binop-no-bits
  (set/difference
   (eval (s/describe ::binop))
   #{'BitAnd 'BitOr 'BitXor 'BitLShift, 'BitRShift}))

(s/exercise ::binop-no-bits)
;; => ([Pow Pow]
;;     [Add Add]
;;     [Div Div]
;;     [Sub Sub]
;;     [Pow Pow]
;;     [Mul Mul]
;;     [Sub Sub]
;;     [Mul Mul]
;;     [Sub Sub]
;;     [Add Add])

(defn fast-int-exp-pluggable
  "O(lg(n)) x^n, n pos or neg, pluggable primitives for base
  operations.

  Partially evaluate this on its operations, for example:
  (partial unchecked-multiply-int,
           unchecked-divide-int,
           unchecked-subtract-int,
           Integer/MIN_VALUE)"
  [mul, div, sub, underflow-val, x n]
  (if (neg? n)
    (let [trial (fast-int-exp-pluggable
                 mul, div, sub, underflow-val,
                 x (- n))]
      (case trial
        ;; In case x^(abs n) == 0
        0 underflow-val
        ;; Most often, (quot 1 trial) is zero, but sometimes it's
        ;; 1/1 = 1.
        (quot 1 trial)))
    (loop [acc 1,  b x,  e n]
      (if (zero? e)
        acc
        (if (even? e)
          (recur       acc   (mul b b) (div e 2))
          (recur  (mul acc b)     b    (sub e 1)))))))

(def fast-unchecked-exp-int
  "Produces zero for 2^32, 2^33, ... . Underflows negative exponents
  to Integer/MIN_VALUE. Spins unchecked multiplications. Spins
  large (>= 32) powers of 2 on 0."
  (partial fast-int-exp-pluggable
           unchecked-multiply-int,
           unchecked-divide-int,
           unchecked-subtract-int,
           Integer/MIN_VALUE))

;; ----------------------------------------------------------------
;; Because our multiplication plugin is unchecked, this can spin
;; round and round and round on seemingly random values:
;;
#_(fast-unchecked-exp-int -481 211)
;; => 1387939935
#_(fast-unchecked-exp-int 481 211)
;; => -1387939935
;; ----------------------------------------------------------------
;; If the base is a positive or negative power of 2, this function
;; will spin on 0:
;;
#_(fast-unchecked-exp-int 32 499)
;; => 0
#_(fast-unchecked-exp-int -32 499)
;; => 0
;; ----------------------------------------------------------------
;; Try 2 on some negative exponents, quotient-ed to zero when
;; small in abs or underflowing to Integer/MIN_VALUE when large:
;;
#_(map (partial fast-unchecked-exp-int 2) (range -37 4 4))
;; => (-2147483648 -2147483648 0 0 0 0 0 0 0 0 8)
;; ----------------------------------------------------------------
;; Try it on some large exponents; once it hits 0, it stays there:
;;
#_(map (partial fast-unchecked-exp-int 2) '(10 20 24 30 31 32 33))
;; => (1024 1048576 16777216 1073741824 -2147483648 0 0)
;; ----------------------------------------------------------------

(def asr-i32-unchecked-binop->clojure-op
  {'Add unchecked-add-int,
   'Sub unchecked-subtract-int,
   'Mul unchecked-multiply-int,
   'Div unchecked-divide-int,
   'Pow fast-unchecked-exp-int,
   'BitAnd bit-and,
   'BitOr bit-or,
   'BitXor bit-xor,
   'BitLShift bit-shift-left,
   'BitRShift bit-shift-right})

(defn i32-bin-op-leaf-gen-pluggable
  "with pluggable operations"
  [ops-map]
  (tgen/let [left  (s/gen ::i32)
             binop (s/gen #{'Pow 'Div} #_::binop-no-bits)
             right (case binop
                     Div (s/gen ::i32nz)  ; don't / 0
                     Pow (if (zero? left) ; don't 0^(negative int)
                           (tgen/fmap abs (s/gen ::i32))
                           (s/gen ::i32)))
             value (tgen/return  ((ops-map binop)  left right))]
    (let [tt '(Integer 4 [])
          ic (fn [i] (list 'IntegerConstant i tt))]
      (list 'IntegerBinOp (ic left) binop (ic right)
            tt (ic value)))))

(gen/sample (i32-bin-op-leaf-gen-pluggable
             asr-i32-unchecked-binop->clojure-op) 20)
;; => ((IntegerBinOp
;;      (IntegerConstant 0 (Integer 4 []))
;;      Div
;;      (IntegerConstant -1 (Integer 4 []))
;;      (Integer 4 [])
;;      (IntegerConstant 0 (Integer 4 [])))
;;     (IntegerBinOp
;;      (IntegerConstant 0 (Integer 4 []))
;;      Div
;;      (IntegerConstant -1 (Integer 4 []))
;;      (Integer 4 [])
;;      (IntegerConstant 0 (Integer 4 [])))
;;     (IntegerBinOp
;;      (IntegerConstant 0 (Integer 4 []))
;;      Pow
;;      (IntegerConstant 1 (Integer 4 []))
;;      (Integer 4 [])
;;      (IntegerConstant 0 (Integer 4 [])))
;;     (IntegerBinOp
;;      (IntegerConstant -1 (Integer 4 []))
;;      Pow
;;      (IntegerConstant -1 (Integer 4 []))
;;      (Integer 4 [])
;;      (IntegerConstant -1 (Integer 4 [])))
;;     (IntegerBinOp
;;      (IntegerConstant -1 (Integer 4 []))
;;      Div
;;      (IntegerConstant 1 (Integer 4 []))
;;      (Integer 4 [])
;;      (IntegerConstant -1 (Integer 4 [])))
;;     (IntegerBinOp
;;      (IntegerConstant 0 (Integer 4 []))
;;      Div
;;      (IntegerConstant -1 (Integer 4 []))
;;      (Integer 4 [])
;;      (IntegerConstant 0 (Integer 4 [])))
;;     (IntegerBinOp
;;      (IntegerConstant -3 (Integer 4 []))
;;      Div
;;      (IntegerConstant 30 (Integer 4 []))
;;      (Integer 4 [])
;;      (IntegerConstant 0 (Integer 4 [])))
;;     (IntegerBinOp
;;      (IntegerConstant -1 (Integer 4 []))
;;      Pow
;;      (IntegerConstant 5 (Integer 4 []))
;;      (Integer 4 [])
;;      (IntegerConstant -1 (Integer 4 [])))
;;     (IntegerBinOp
;;      (IntegerConstant -4 (Integer 4 []))
;;      Pow
;;      (IntegerConstant 2 (Integer 4 []))
;;      (Integer 4 [])
;;      (IntegerConstant 16 (Integer 4 [])))
;;     (IntegerBinOp
;;      (IntegerConstant 2 (Integer 4 []))
;;      Div
;;      (IntegerConstant 7 (Integer 4 []))
;;      (Integer 4 [])
;;      (IntegerConstant 0 (Integer 4 [])))
;;     (IntegerBinOp
;;      (IntegerConstant -32 (Integer 4 []))
;;      Div
;;      (IntegerConstant 42 (Integer 4 []))
;;      (Integer 4 [])
;;      (IntegerConstant 0 (Integer 4 [])))
;;     (IntegerBinOp
;;      (IntegerConstant 0 (Integer 4 []))
;;      Pow
;;      (IntegerConstant 635 (Integer 4 []))
;;      (Integer 4 [])
;;      (IntegerConstant 0 (Integer 4 [])))
;;     (IntegerBinOp
;;      (IntegerConstant -481 (Integer 4 []))
;;      Pow
;;      (IntegerConstant 211 (Integer 4 []))
;;      (Integer 4 [])
;;      (IntegerConstant 1387939935 (Integer 4 [])))
;;     (IntegerBinOp
;;      (IntegerConstant -493 (Integer 4 []))
;;      Pow
;;      (IntegerConstant 1 (Integer 4 []))
;;      (Integer 4 [])
;;      (IntegerConstant -493 (Integer 4 [])))
;;     (IntegerBinOp
;;      (IntegerConstant 142 (Integer 4 []))
;;      Pow
;;      (IntegerConstant -15 (Integer 4 []))
;;      (Integer 4 [])
;;      (IntegerConstant 0 (Integer 4 [])))
;;     (IntegerBinOp
;;      (IntegerConstant 1781 (Integer 4 []))
;;      Div
;;      (IntegerConstant -22 (Integer 4 []))
;;      (Integer 4 [])
;;      (IntegerConstant -80 (Integer 4 [])))
;;     (IntegerBinOp
;;      (IntegerConstant -3 (Integer 4 []))
;;      Div
;;      (IntegerConstant -5896 (Integer 4 []))
;;      (Integer 4 [])
;;      (IntegerConstant 0 (Integer 4 [])))
;;     (IntegerBinOp
;;      (IntegerConstant -52 (Integer 4 []))
;;      Pow
;;      (IntegerConstant -1197 (Integer 4 []))
;;      (Integer 4 [])
;;      (IntegerConstant -2147483648 (Integer 4 [])))
;;     (IntegerBinOp
;;      (IntegerConstant 44 (Integer 4 []))
;;      Div
;;      (IntegerConstant -16960 (Integer 4 []))
;;      (Integer 4 [])
;;      (IntegerConstant 0 (Integer 4 [])))
;;     (IntegerBinOp
;;      (IntegerConstant -16940 (Integer 4 []))
;;      Pow
;;      (IntegerConstant -22475 (Integer 4 []))
;;      (Integer 4 [])
;;      (IntegerConstant -2147483648 (Integer 4 []))))


;;; TODO: Note that MOD, REM, QUOTIENT are missing!


;;  ___         _        __   ___             _         _   _
;; | __|_ _  __| |  ___ / _| | _ \_ _ ___  __| |_  _ __| |_(_)___ _ _
;; | _|| ' \/ _` | / _ \  _| |  _/ '_/ _ \/ _` | || / _|  _| / _ \ ' \
;; |___|_||_\__,_| \___/_|   |_| |_| \___/\__,_|\_,_\__|\__|_\___/_||_|

;;; END OF PRODUCTION HORIZON
;;; Code below this line is, again, experimental

;; The following breaks some semantics by mixing integer kinds.
;; It's not fully syntactical, but might be useful.

(s/def ::integer-bin-op-mixed-kind-base-semnasr
  (s/tuple
   #{'IntegerBinOp}
   ::integer-constant-semnasr ; stack overflow if integer-bin-op-semnsasr
   ::binop
   ::integer-constant-semnasr
   ::integer-scalar-ttype-semnasr))

;; for interactive testing in CIDER:
;; C-x C-e after the closing parenthesis
#_(->> ::integer-bin-op-mixed-kind-base-semnasr
     s/gen
     gen/generate)

;;                     _        _   _    _
;;  __ _ _ _  ___ __ _| |_ ___ (_) | |__(_)_ _    ___ _ __
;; / _` | ' \(_-</ _` |  _|_ / | | | '_ \ | ' \  / _ \ '_ \
;; \__,_|_||_/__/\__,_|\__/__| |_| |_.__/_|_||_| \___/ .__/
;;                                                   |_|

;; The following ansatz is automatically created from the ASDL
;; parse and satisfies a conformance test in core_test.clj.

(let [integer-bin-op-stuff ;; SynNASR
      (filter #(= (:head %) :asr.core/IntegerBinOp)
              big-list-of-stuff)]
  (-> (spec-from-composite
       (-> integer-bin-op-stuff
           first
           :form
           :ASDL-COMPOSITE
           echo))
      echo
      eval))

;;  _       _        _                   _
;; | |_ ___| |_ __ _| |  __ ___ _  _ _ _| |_
;; |  _/ _ \  _/ _` | | / _/ _ \ || | ' \  _|
;;  \__\___/\__\__,_|_| \__\___/\_,_|_||_\__|

(print "total number of specs registered: ")
(println (count-asr-specs))

;; (pprint (s/exercise ::identifier))
;; (pprint (s/exercise ::expr))
;; (pprint (s/exercise ::dimension))

(println "Please see the tests. Main doesn't do a whole lot ... yet.")


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
