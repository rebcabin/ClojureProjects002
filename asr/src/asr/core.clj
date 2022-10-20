(ns asr.core
  (:gen-class)

  (:use [asr.utils] ; TODO: winnow
        [asr.numbers]
        [asr.specs]
        [asr.parsed]
        [asr.autospecs]
        [asr.expr.semnasr])

  (:require [clojure.spec.alpha            :as    s         ]
            [clojure.pprint                :refer [pprint]  ]
            [clojure.spec.gen.alpha        :as    gen       ]
            [clojure.spec.test.alpha       :as    stest     ]
            [clojure.test.check.generators :as    tgen      ]
            [clojure.set                   :as    set       ]))


;;       _                     _    _       _ _______
;;  _ __| |_  _ __ _ __ _ __ _| |__| |___  (_)__ /_  )
;; | '_ \ | || / _` / _` / _` | '_ \ / -_) | ||_ \/ /
;; | .__/_|\_,_\__, \__, \__,_|_.__/_\___| |_|___/___|
;; |_|         |___/|___/
;;           _ _   _              _   _
;;  __ _ _ _(_) |_| |_  _ __  ___| |_(_)__
;; / _` | '_| |  _| ' \| '  \/ -_)  _| / _|
;; \__,_|_| |_|\__|_||_|_|_|_\___|\__|_\__|

(defn maybe-unchecked-divide-int
  "Return nil on zero divide or overflow."
  [x y]
  (if (zero? y) nil
      (try
       (unchecked-divide-int x y)
       (catch ArithmeticException e
         #_(pprint {:ArithEx e, :x x, :y y}) ; returns nil
         nil
         ))))


(defn maybe-quot
  "Return nil on zero divide."
  [x y]
  (if (zero? y) nil
      (quot x y)))


(defn maybe-div
  [x y]
  (if (zero? y) nil
      (/ x y)))


(defn maybe-float
  [x]
  (if (nil? x) nil
      (float x)))


(defn fast-int-exp-maybe-pluggable
  "O(lg(n)) x^n, x, n zero, pos, or neg, pluggable primitives for
  base operations. Can produce `nil` if `(zero? x)` and `(neg? n)`
  and `div` propagates nil. Produces `nil` if either `x` or `n`
  is nil.

  Partially evaluate this on its operations, for example:

      (partial fast-int-exp-maybe-pluggable
               unchecked-multiply-int,
               maybe-unchecked-divide-int,
               unchecked-subtract-int)
  "
  [mul, div, sub, x n]
  (try (let [v (loop [acc 1, b x, e (abs n)]
                 (if (zero? e)
                   acc
                   (if (even? e)
                     (recur       acc    (mul b b) (div e 2))
                     (recur  (mul acc b)      b    (sub e 1)))))]
         (if (neg? n)
           (div 1 v)                    ; Can produce nil.
           v))
       (catch NullPointerException ex
         #_(pprint {:NPE ex, :x x, :n n})
         nil)))


(def maybe-fast-unchecked-i32-exp
  "Produces `0` for `2^32, 2^33, ...` . Underflows negative
  exponents to nil. Div by zero or `0` to a negative power produce
  nil. Spins unchecked multiplications. Spins large (>= 32) powers
  of 2 on 0. See core_test.clj"
  (partial fast-int-exp-maybe-pluggable
           unchecked-multiply-int,
           maybe-unchecked-divide-int,
           unchecked-subtract-int))


(def asr-i32-unchecked-binop->clojure-op
  "Substitute particular arithmetic ops for spec ops in Clojure.
  Our arithmetic is double-pluggable: the power operations is
  pluggable (see `fast-unchecked-i32-exp`, and the entire
  collection of operations is pluggable, one level up."
  {'Add       unchecked-add-int,
   'Sub       unchecked-subtract-int,
   'Mul       unchecked-multiply-int,
   'Div       maybe-unchecked-divide-int,
   'Pow       maybe-fast-unchecked-i32-exp,
   'BitAnd    #(.intValue (bit-and         %1 %2)),
   'BitOr     #(.intValue (bit-or          %1 %2)),
   'BitXor    #(.intValue (bit-xor         %1 %2)),
   'BitLShift #(.intValue (bit-shift-left  %1 %2)),
   'BitRShift #(.intValue (bit-shift-right %1 %2))})

;;; TODO: Note that MOD, REM, QUOTIENT are missing!


;;     _          _    _                              _   _
;;  __| |___ _  _| |__| |___   ___ ___ _ __  __ _ _ _| |_(_)__ ___
;; / _` / _ \ || | '_ \ / -_) (_-</ -_) '  \/ _` | ' \  _| / _(_-<
;; \__,_\___/\_,_|_.__/_\___| /__/\___|_|_|_\__,_|_||_\__|_\__/__/

;; Sem-Sem means specs that are doubly semantically correct:
;;
;; - They have correct *types* in their argument lists
;;
;; - They have correct arithmetic for expressions that can (and
;;   should) be evaluated at compile time.


;;  _ _______   _    _                   _           __
;; (_)__ /_  ) | |__(_)_ _    ___ _ __  | |___ __ _ / _|
;; | ||_ \/ /  | '_ \ | ' \  / _ \ '_ \ | / -_) _` |  _|
;; |_|___/___| |_.__/_|_||_| \___/ .__/ |_\___\__,_|_|
;;                               |_|
;;  ___ ___ _ __  ___ ___ _ __
;; (_-</ -_) '  \(_-</ -_) '  \
;; /__/\___|_|_|_/__/\___|_|_|_|

;; see https://clojurians.slack.com/archives/C1B1BB2Q3/p1665776362948879
;;
;; OBSERVATION: I noticed a handy feature: specs created via
;; s/with-gen can automatically filter out nil. Is it generally
;; true that such specs never produce nil when the spec-part of
;; s/with-gen forbids nil? I guess, stated that way, it’s obvious
;; that it should be generally true.
;;
;; This is a nice feature for me because my project generators
;; create trees with level-crossing semantical constraints that
;; can go wrong in combinatorial ways. Any time anything goes
;; wrong (e.g., 0 to a negative power somewhere in the tree), I
;; just barf out nil as if in a maybe monad (it’s about 2% of the
;; time, so it’s fine). It’s nice that I get automatic filtering
;; of the generated values at the spec level. Actually, it’s
;; brilliant! It let me strip out the maybe monad from my
;; code (simplifying it greatly) and just rely on nil punning and
;; some-> and some->> operations.
;;
;; (s/def ::nil-producing-spec
;;   (s/or :nil nil? :int integer?))
;;
;; (def nil-producing-generator
;;   (s/gen ::nil-producing-spec))
;;
;; (gen/sample nil-producing-generator)
;; ;; => (nil nil -1 -2 nil 6 3 nil nil nil)
;;
;; (s/def ::nil-rejecting-spec
;;   (s/with-gen
;;     integer?
;;     (fn [] nil-producing-generator)))
;;
;; (gen/sample (s/gen ::nil-rejecting-spec))
;; ;; => (0 -4 -2 -5 0 -64 0 -4 6 0)

;; The code below is instrumented for debugging.

(def call-count      (atom 0))
(def div-0-count     (atom 0))
(def pow-neg-count   (atom 0))
(def other-nil-count (atom 0))

(defn i32-bin-op-leaf-semsem-gen-pluggable
  "Given an ops-map from ASR binops to implementations, return a
  generator for i32 ASR IntegerBinOp leaf nodes. It's the
  generator for spec ::i32-bin-op-leaf-semsem."
  [ops-map]
  (let [tt  '(Integer 4 [])
        ic   (fn [i] (list 'IntegerConstant i tt))
        res  (fn [l b r v]
               (list 'IntegerBinOp (ic l) b (ic r) tt (ic v)))]
    (tgen/let [left  (s/gen :asr.numbers/i32) ;; Div, Pow are the tough cases
               binop (s/gen #_#{'Div 'Pow} :asr.autospecs/binop)
               right (s/gen :asr.numbers/i32)]
      (let [value  ((ops-map binop) left right)
            result (if (nil? value)
                     nil
                     (res left binop right value))
            _      (swap! call-count inc)]
        (if (nil? result)
          (do (case binop
                Div       (swap! div-0-count inc)
                Pow       (swap! pow-neg-count inc)
                #_default (swap! other-nil-count inc))
              #_(pprint {:c @call-count :t "LEAF"
                       :l left :o binop :r right
                       :v value :res result})))
        result))))


;;; produces nil 2.6 percent of the time, always in div-by-0 or 0
;;; to negative power.

#_
(let [NTESTS 10000
      _ (reset! call-count 0)
      ibops (gen/sample
             (i32-bin-op-leaf-semsem-gen-pluggable
              asr-i32-unchecked-binop->clojure-op)
             NTESTS)
      nils     (filter nil? ibops)
      non-nils (filter (comp not nil?) ibops)
      cnils    (count nils)
      cnnils   (count non-nils)]
  (assert (= NTESTS (+ cnils cnnils)))
  {:cnils cnils, :cnnils cnnils,
   :ratio (some->> cnils (maybe-div cnnils) float),
   :pct-nils (some->> cnils (maybe-div NTESTS) (maybe-div 1.0) (* 100.0))
   :call-count @call-count
   :div-0-count @div-0-count
   :pow-neg-count @pow-neg-count
   :other-nil-count @other-nil-count})
;; => {:cnils 251,
;;     :cnnils 9749,
;;     :ratio 38.840637,
;;     :pct-nils 2.51,
;;     :call-count 10000,
;;     :div-0-count 234,
;;     :pow-neg-count 1486,
;;     :other-nil-count 0}


;; When I put that generator in the spec, I get automatic nil-
;; rejection! Nice!

(def bad-structure-count  (atom 0))
(def bad-operator-count   (atom 0))
(def something-else-count (atom 0))

(s/def ::i32-bin-op-leaf-semsem
  (s/with-gen
    ;; pred
    (fn [i32bop]
      (try
        (let [[head left op right ttype value]  i32bop]
          #_(pprint {:rsn "LEAF" :h head, :l left, :o op,
                   :t ttype, :v value})
          (let [,[lhead lv lttype] left
                ,[rhead rv rttype] right
                ,[vhead vv vttype] value
                ,ttcheck '(Integer 4 [])]
            (and (= 'IntegerBinOp head)
                 (= 'IntegerConstant lhead)
                 (= 'IntegerConstant rhead)
                 (= 'IntegerConstant vhead)
                 (= ttcheck ttype)
                 (= ttcheck lttype)
                 (= ttcheck rttype)
                 (= ttcheck vttype)
                 (= vv
                    ((op  asr-i32-unchecked-binop->clojure-op)  lv rv)))))
        (catch UnsupportedOperationException e
          (swap! bad-structure-count inc)
          ;; Because a generator produced a IntegerConstant
          ;; in a case that bottoms out recursion, e.g.,
          ;;
          ;;     {:rsn "LEAF", :h IntegerConstant,
          ;;      :l -1,       :o (Integer 4 []),
          ;;      :t nil,      :v nil}
          ;;
          #_"bad structure" false)
        (catch IllegalArgumentException e
          (swap! bad-operator-count inc)
          #_"bad operator" false)
        (catch NullPointerException e
          (swap! something-else-count inc)
          #_"something else bad" false)))
    ;; gen
    (fn [] (i32-bin-op-leaf-semsem-gen-pluggable
            asr-i32-unchecked-binop->clojure-op))))


(defn reset-instrumentation []
  (reset! call-count           0)
  (reset! div-0-count          0)
  (reset! pow-neg-count        0)
  (reset! other-nil-count      0)
  (reset! bad-structure-count  0)
  (reset! bad-operator-count   0)
  (reset! something-else-count 0))


(defn instrumentation-dict []
  {:call-count           @call-count
   :div-0-count          @div-0-count
   :pow-neg-count        @pow-neg-count
   :other-nil-count      @other-nil-count
   :bad-structure-count  @bad-structure-count
   :bad-operator-count   @bad-operator-count
   :something-else-count @something-else-count
   })

;; The difference to the example above is that cnils will be zero
;; but the call-count will be a little higher than NTESTS.

#_
(let [NTESTS 10000, _ (reset-instrumentation)
      ibops (gen/sample
             #_(i32-bin-op-leaf-semsem-gen-pluggable
                asr-i32-unchecked-binop->clojure-op)
             (s/gen ::i32-bin-op-leaf-semsem)
             NTESTS)
      nils     (filter nil? ibops)
      non-nils (filter (comp not nil?) ibops)
      cnils    (count nils)
      cnnils   (count non-nils)]
  (assert (= NTESTS (+ cnils cnnils)))
  (merge
   {:cnils cnils, :cnnils cnnils,
    :ratio (some->> cnils (maybe-div cnnils) float),
    :pct-nils (some->> cnils (maybe-div NTESTS) (maybe-div 1.0) (* 100.0))}
   (instrumentation-dict)))
;; => {:pow-neg-count 229,
;;     :pct-nils nil,
;;     :something-else-count 0,
;;     :cnils 0,
;;     :div-0-count 31,
;;     :ratio nil,
;;     :other-nil-count 0,
;;     :bad-structure-count 0,
;;     :cnnils 10000,
;;     :call-count 10260,
;;     :bad-operator-count 0}


;;; See core_test.clj for examples showing propagated failures.

;;  _ _______   _    _
;; (_)__ /_  ) | |__(_)_ _    ___ _ __   ___ ___ _ __  ___ ___ _ __
;; | ||_ \/ /  | '_ \ | ' \  / _ \ '_ \ (_-</ -_) '  \(_-</ -_) '  \
;; |_|___/___| |_.__/_|_||_| \___/ .__/ /__/\___|_|_|_/__/\___|_|_|_|
;;                               |_|

;;; defective version for a forward reference (backpatch later);
;;; Now, we let the generator produce constants to bottom out
;;; recursion, and the spec pred for ::i32-bin-op-leaf-semsem
;;; can produce "bad structure." It does so about half the time.
;;; Will we fix this?

(s/def ::i32-bin-op-semsem
  (s/or :i32bop ::i32-bin-op-leaf-semsem
        :i32con :asr.expr.semnasr/i32-constant))


(defn fetch-value-i32-bin-op-semsem
  "Given an IntegerConstant or an IntegerBinOp (icobo), fetch the
  value, if there is one. Return nil if there is anything invalid
  about the input. An IntegerBinOp is semsem-valid if its two
  inputs, left and right, are semsem-valid and its output value
  equals the operator applied to the two inputs.

  Notice this co-cursively calls ::i32-bin-op-semsem, which will
  call fetch-value-i32-bin-op-semsem via the generator
  i32-bin-op-semsem-gen-pluggable after ::i32-bin-op-semsem is
  backpatched below.

  Propagates nils.
  "
  [icobo]
  (cond ;; order matters
    ,(s/valid? :asr.expr.semnasr/i32-constant icobo)
    (let [[_, v, _] icobo] v)           ; could be nil
    ,(s/valid? ::i32-bin-op-semsem icobo)
    (let [[_, _, _, _, _, cv] icobo]
      (let [[_, v, _] cv] v))           ; could be nil
    ,:else
    nil))


(defn compute-i32-bin-op-value
  "Compute, rather than fetch, an i32-bin-op value."
  [ops-map icobo]
  (let [[h & s] icobo]
    (case h
      IntegerBinOp (let [[l o r t v] s
                         l- (compute-i32-bin-op-value ops-map l)
                         o- (ops-map o)
                         r- (compute-i32-bin-op-value ops-map r)]
                     (o- l- r-))
      IntegerConstant (let [[v t] s]
                        v))))


;; As above, this generator propagates nils.

(defn i32-bin-op-semsem-gen-pluggable
  "Given an ops-map from ASR binops to implementations, generate an
  i32 ASR IntegerBinOp node, recursively."
  [ops-map]
  (let [tt   '(Integer 4 [])
        ic    (fn [i] (list 'IntegerConstant i tt))
        res   (fn [l b r v] (list 'IntegerBinOp l b r tt (ic v)))
        meval fetch-value-i32-bin-op-semsem #_shorthand ]
    (gen/one-of
     [ ;; base case
      (s/gen ::i32-bin-op-leaf-semsem)
      ;; recurse
      (tgen/let [lbop  (s/gen ::i32-bin-op-semsem)
                 rbop  (s/gen ::i32-bin-op-semsem)
                 binop (s/gen :asr.autospecs/binop)]
        (let [left   (meval lbop)
              right  (meval rbop)
              value  ((ops-map binop) left right)
              result (if (nil? value)  nil
                         (res lbop binop rbop value))
              _      (swap! call-count inc)]
          #_(if (nil? result)
            (pprint {:c @call-count :t "RECURSE"
                     :l left :o binop :r right
                     :v value :res result}))
          result))])))


;; The number of nils rejected is (- call-count NTESTS).

#_
(let [NTESTS 10000, _ (reset-instrumentation)
      ibops (gen/sample ;; not LEAF in the next line.
             (i32-bin-op-semsem-gen-pluggable
              asr-i32-unchecked-binop->clojure-op)
             NTESTS)
      nils     (filter nil? ibops)
      non-nils (filter (comp not nil?) ibops)
      cnils    (count nils)
      cnnils   (count non-nils)]
  (assert (= NTESTS (+ cnils cnnils)))
  (merge
   {:cnils cnils, :cnnils cnnils,
    :ratio (some->> cnils (maybe-div cnnils) float),
    :pct-nils (some->> cnils (maybe-div NTESTS) (maybe-div 1.0) (* 100.0))}
   (instrumentation-dict)))
;; => {:pow-neg-count 236,
;;     :pct-nils 1.78,
;;     :something-else-count 0,
;;     :cnils 178,
;;     :div-0-count 30,
;;     :ratio 55.179775,
;;     :other-nil-count 0,
;;     :bad-structure-count 5008,
;;     :cnnils 9822,
;;     :call-count 15286,
;;     :bad-operator-count 0}


;;    __            __              __      __
;;   / /  ___ _____/ /__ ___  ___ _/ /_____/ /
;;  / _ \/ _ `/ __/  '_// _ \/ _ `/ __/ __/ _ \
;; /_.__/\_,_/\__/_/\_\/ .__/\_,_/\__/\__/_//_/
;;                    /_/

;;; Must define the new spec twice so that the old spec gets
;;; recursively backpatched.

;; TODO: These frequencies seem not to have a large effect
;; on the distribution of leaf counts (see stats.clj)

(def RELATIVE_RECURSION_FREQUENCY 95)
(def RELATIVE_BASE_FREQUENCY      05)


(dotimes [_ 2]
  (let [the-gen (i32-bin-op-semsem-gen-pluggable
                 asr-i32-unchecked-binop->clojure-op)]
    (s/def ::i32-bin-op-semsem
      (s/with-gen
        (s/or
         ,:base
         ::i32-bin-op-leaf-semsem
         ,:recurse
         (let [or-leaf
               (s/with-gen
                 (s/or ,:leaf
                        #_:asr.expr.semnasr/i32-constant ;; fails tests
                        ::i32-bin-op-leaf-semsem
                        ,:branch ::i32-bin-op-semsem)
                 (fn [] (gen/frequency
                         [[,RELATIVE_RECURSION_FREQUENCY
                           ;; THE OLD SPEC first time
                           ::i32-bin-op-semsem]
                          [,RELATIVE_BASE_FREQUENCY
                           ::i32-bin-op-leaf-semsem]])))]
           (s/cat :head  #{'IntegerBinOp}
                  :left  or-leaf
                  :op    :asr.autospecs/binop
                  :right or-leaf
                  :ttype :asr.expr.semnasr/i32-scalar-ttype
                  :value :asr.expr.semnasr/i32-constant)))
        (fn [] the-gen)))))

#_
(let [NTESTS 10000, _ (reset-instrumentation)
      ibops (gen/sample ;; not LEAF in the next line.
             (s/gen ::i32-bin-op-semsem)
             NTESTS)
      nils     (filter nil? ibops)
      non-nils (filter (comp not nil?) ibops)
      cnils    (count nils)
      cnnils   (count non-nils)]
  (assert (= NTESTS (+ cnils cnnils)))
  (merge
   {:cnils cnils, :cnnils cnnils,
    :ratio (some->> cnils (maybe-div cnnils) float),
    :pct-nils (some->> cnils (maybe-div NTESTS) (maybe-div 1.0) (* 100.0))}
   (instrumentation-dict)))
;; => {:pow-neg-count 359,
;;     :pct-nils nil,
;;     :something-else-count 0,
;;     :cnils 0,
;;     :div-0-count 53,
;;     :ratio nil,
;;     :other-nil-count 0,
;;     :bad-structure-count 5138,
;;     :cnnils 10000,
;;     :call-count 26335,
;;     :bad-operator-count 0}


(defn -i32-bin-op-semsem-leaf-count
  [lc, i32bop]
  (let [[h l o r t v] i32bop]
    (let [[lh & lresid] l]
      (case lh
        IntegerConstant (let [[rh & rresid] r]
                          (case rh
                            IntegerConstant (+ 2 lc)
                            (-i32-bin-op-semsem-leaf-count
                             (+ 1 lc) r)))
        (+ (-i32-bin-op-semsem-leaf-count lc l)
           (-i32-bin-op-semsem-leaf-count lc r))))))


(def i32-bin-op-semsem-leaf-count
  (partial -i32-bin-op-semsem-leaf-count 0))


;;  ___         _        __   ___             _         _   _
;; | __|_ _  __| |  ___ / _| | _ \_ _ ___  __| |_  _ __| |_(_)___ _ _
;; | _|| ' \/ _` | / _ \  _| |  _/ '_/ _ \/ _` | || / _|  _| / _ \ ' \
;; |___|_||_\__,_| \___/_|   |_| |_| \___/\__,_|\_,_\__|\__|_\___/_||_|

;; Experimental stuff.


;;  _       _        _                   _
;; | |_ ___| |_ __ _| |  __ ___ _  _ _ _| |_
;; |  _/ _ \  _/ _` | | / _/ _ \ || | ' \  _|
;;  \__\___/\__\__,_|_| \__\___/\_,_|_||_\__|

(defn only-asr-specs
  "Filter non-ASR specs from the keys of the spec registry."
  [asr-namespace-string]
  []
  (filter
   #(= (namespace %) asr-namespace-string)
   (keys (s/registry))))


(defn check-registry
  "Print specs defined in the various namespaces. Call this at the
  REPL (replacing missing codox for specs)."
  []
  (pprint {"core specs"         (only-asr-specs "asr.core")
           "parsed specs"       (only-asr-specs "asr.parsed")
           "hand-written specs" (only-asr-specs "asr.specs")
           "automatic specs"    (only-asr-specs "asr.autospecs")}))


(defn count-asr-core-specs
  "Count the asr.core specs. Call this at the REPL."
  []
  (count (only-asr-specs "asr.core")))


(println "total number of experimental specs registered in the core namespace: ")
(println (count-asr-core-specs))


(println "Please see the tests. Main doesn't do a whole lot ... yet.")


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
