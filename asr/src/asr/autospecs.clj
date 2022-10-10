(ns asr.autospecs
  (:use [asr.utils]
        [asr.specs])

  (:require [asr.parsed :refer [big-map-of-speclets-from-terms
                                big-list-of-stuff
                                head-from-kind-form
                                symconst-stuffss-by-term
                                symconst-stuffs]])

  (:require [clojure.pprint                :refer [pprint]]
            [clojure.spec.alpha            :as    s       ]
            [clojure.test.check.generators :as    tgen    ]
            [clojure.spec.gen.alpha        :as    gen     ]
            ))


;;                     __                                              _
;;  ____ __  ___ __   / _|_ _ ___ _ __    ____  _ _ __  __ ___ _ _  __| |_
;; (_-< '_ \/ -_) _| |  _| '_/ _ \ '  \  (_-< || | '  \/ _/ _ \ ' \(_-<  _|
;; /__/ .__/\___\__| |_| |_| \___/_|_|_| /__/\_, |_|_|_\__\___/_||_/__/\__|
;;    |_|                                    |__/
;;     _         __  __
;;  __| |_ _  _ / _|/ _|
;; (_-<  _| || |  _|  _|
;; /__/\__|\_,_|_| |_|

(defn spec-from-symconst-stuff
  "Construct and register all (approximately) 72 symconst
  head-specs:

  A spec is *registered* into a hidden Clojure Spec Registry by
  side-effect and is associated with a namespaced keyword produced
  by `asr.utils/nskw-kebab-from`. For example,
  `:asr.autospecs/implementation`.

  All specs, head-specs and term-specs alike must be registered
  before being referred-to. We break co-recursive cycles by
  registering defective autospecs then backpatching them by
  hand-written specs in namespace `asr.specs`. For example, the
  term-spec for `::symbol` refers to the term-spec for
  `::symbol-table`, which refers to the term-spec for `::symbol`.
  Clojure.spec can't tolerate that, but it can tolerate a
  defective term-spec for `::symbol-table` that we backpatch
  later by hand.
  "
  [symconst-stuff]
  (let [symconst (-> symconst-stuff :form :ASDL-SYMCONST)
        nskw (nskw-kebab-from symconst)]
    `(s/def ~nskw #{(quote ~(symbol symconst))})))


;;                               _                        __
;;  ____  _ _ __  __ ___ _ _  __| |_   ____ __  ___ __   / _|___ _ _
;; (_-< || | '  \/ _/ _ \ ' \(_-<  _| (_-< '_ \/ -_) _| |  _/ _ \ '_|
;; /__/\_, |_|_|_\__\___/_||_/__/\__| /__/ .__/\___\__| |_| \___/_|
;;     |__/                              |_|
;;  _
;; | |_ ___ _ _ _ __
;; |  _/ -_) '_| '  \
;;  \__\___|_| |_|_|_|

(defn symconst-spec-for-term
  "For each term, spec a `set` containing its alternative heads,
  e.g., the term `binop` is one of the ten heads `Add`, `Sub`, and
  so on, to `BitRShift`. The spec produced by this function must
  be `eval`'ed to register the spec.
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


;;     _                  _
;;  __| |_  _ _ __  _ __ (_)___ ___
;; / _` | || | '  \| '  \| / -_|_-<
;; \__,_|\_,_|_|_|_|_|_|_|_\___/__/

(defn dummy-generator-for-heads
  "Insert a list of random length of random identifiers. For
  producing dummy specs that are later backpatched by hand."
  [heads]
  (tgen/let [head (s/gen heads)
             rest (gen/list (s/gen :asr.specs/identifier))]
    (cons head rest)))


(defn dummy-lpred
  "Check simply that the instance is a list with an appropriate head
  and zero or more items of any type. For producing dummy specs
  that are later backpatched by hand."
  [heads]
  (s/and seq?
         (fn [lyst] (-> lyst count (>= 1)))
         (fn [lyst] (-> lyst first heads))))


;;                        __                                     _ _
;;  ____ __  ___ __ ___  / _|___ _ _   __ ___ _ __  _ __  ___ __(_) |_ ___ ___
;; (_-< '_ \/ -_) _(_-< |  _/ _ \ '_| / _/ _ \ '  \| '_ \/ _ (_-< |  _/ -_|_-<
;; /__/ .__/\___\__/__/ |_| \___/_|   \__\___/_|_|_| .__/\___/__/_|\__\___/__/
;;    |_|                                          |_|

(defn heads-for-composite
  "Produce a list of symbolic heads (like `RealUnaryMinus` and
  `ArraySection`), from a term like `:asr.autospecs/expr`."
  [term]
  (->> big-map-of-speclets-from-terms
       term
       (map :ASDL-COMPOSITE)
       (map :ASDL-HEAD)
       (map symbol)
       set
       #_echo))


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


(let [heads (heads-for-composite ::ttype)]
  (->> (s/def ::ttype
         (s/with-gen
           (dummy-lpred heads)
           (fn [] (dummy-generator-for-heads heads))))))


;;  __ _ _ _ __ _ ___
;; / _` | '_/ _` (_-<
;; \__,_|_| \__, /__/
;;          |___/

(defn spec-from-arg
  "Convert multiplicities from `asr.parsed` into clojure.spec equivalents.
  "
  [arg]
  (let [type (nskw-kebab-from (:ASDL-TYPE arg))
        nym (:ASDL-NYM arg)]
    (case (:MULTIPLICITY arg)
      :asr.parsed/once         `(s/spec ~type)
      :asr.parsed/at-most-once `(s/? (s/spec ~type))
      :asr.parsed/zero-or-more `(s/* (s/spec ~type)))))


(defn spec-from-args
  "Write a spec to be eval'ed later from an args tuple."
  [args]
  (let [nyms (->> args (map :ASDL-NYM)           #_echo)
        kyms (->> nyms (map (comp keyword name)) #_echo)
        specules (->> args (map spec-from-arg)   #_echo)
        riffle (-> (interleave kyms specules)    #_echo)]
    `(s/cat ~@riffle)))


(defn spec-from-head-and-args
  "Write a spec to be eval'ed later from a head and an args tuple."
  [head args]
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

(defn tuple-head-spec-from-stuff
  "Write a spec to be eval'ed later from tuple stuff (see
  `asr.parsed` for definition of *stuff*)."
  [tuple-stuff]
  #_(println "tuple-head-spec-from-stuff")
  (let [nskw (-> tuple-stuff :head name nskw-kebab-from #_echo)
        args (-> tuple-stuff :form :ASDL-ARGS           #_echo)]
    `(s/def ~nskw ~(spec-from-args args))))  ;; side effect!


(def tuple-stuffs
  "List of all six tuple stuffs. There are six tuple heads. Their
  names will change from run-to-run because the names are
  gensymmed.
  "
  (->> big-list-of-stuff
       (filter #(= (:kind %) :ASDL-TUPLE))))


(def tuple-stuffss-by-term
  "Lists of tuple stuffs for each term. As before, we really need
  clojure.specs for the terms corresponding to the heads, an
  inverted index.
  "
  (partition-by :term tuple-stuffs))


;; ### Register

;; To register the 6 term-specs, `eval` them.

(defn tuple-term-spec-from-stuffs
  "Write a tuple term-spec to be eval'ed later from tuple stuffs
  (see `asr.parsed` for definition of *stuff*)."
  [stuffs]
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
       echo
       (filter #(= term (-> % :term)))
       echo
       (map tuple-head-spec-from-stuff)
       (map eval)
       echo)

  (->> tuple-stuffss-by-term
       (filter #(= term (-> % first :term)))
       echo
       (map tuple-term-spec-from-stuffs)
       (map eval)
       echo))


;;                       __                                            _ _
;;  ____ __  ___ __ ___ / _|_ _ ___ _ __ ___ __ ___ _ __  _ __  ___ __(_) |_ ___
;; (_-< '_ \/ -_) _|___|  _| '_/ _ \ '  \___/ _/ _ \ '  \| '_ \/ _ (_-< |  _/ -_)
;; /__/ .__/\___\__|   |_| |_| \___/_|_|_|  \__\___/_|_|_| .__/\___/__/_|\__\___|
;;    |_|                                                |_|

(defn spec-from-composite
  "Write specs as data lists and `eval` them later. Turns out it's
  necessary to do that, and it's a beneficial accident lest we
  clutter up the namespace of specs.

  Composites and tuples have lists of type-var pairs, that is, of
  args. We've already handled arg lists in `spec-from-args` above.

  Assumes specs for all tuples' heads and terms have already been
  registered. Don't call this function too early.
  "
  [composite]
  (let [head (-> composite :ASDL-HEAD symbol echo)
        nskw (-> head nskw-kebab-from        echo)
        args (-> composite :ASDL-ARGS        #_echo)]
    `(s/def ~nskw ~(spec-from-head-and-args head args))))


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
  "Side-effecting automated items for the spec registry. W.I.P."
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

(do-synnasr)
