(ns asr.core
  (:gen-class)

  (:use [asr.utils]
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


;;  _    _      _     _  ___
;; | |__(_)__ _(_)_ _| ||__ \
;; | '_ \ / _` | | ' \  _|/_/
;; |_.__/_\__, |_|_||_\__(_)
;;        |___/


(defn bigint?
  "Doesn't seem to be defined in system-supplied libraries."
  [n]
  (instance? clojure.lang.BigInt n))


;;  _ _   _    _                _
;; (_|_) | |__(_)__ _ _ _  __ _| |_
;;  _ _  | '_ \ / _` | ' \/ _` |  _|
;; (_|_) |_.__/_\__, |_||_\__,_|\__|
;;              |___/

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

#_
(->> ::bignat s/exercise (map second))
;; => (7 13 63 98225932 4572 28 31914670493 80 252 256185)


;;  _ _      _ _                   _
;; (_|_)  __| (_)_ __  ___ _ _  __(_)___ _ _  ___
;;  _ _  / _` | | '  \/ -_) ' \(_-< / _ \ ' \(_-<
;; (_|_) \__,_|_|_|_|_\___|_||_/__/_\___/_||_/__/

(s/def ::dimensions
  (s/coll-of (s/or :nat-int nat-int?, :bigint ::bignat)
             :min-count 0,
             :max-count 2,
             :into []))

#_
(-> ::dimensions (s/exercise))
;; => ([[6] [[:bigint 6]]]
;;     [[0] [[:nat-int 0]]]
;;     [[13755145] [[:bigint 13755145]]]
;;     [[222953 793456588] [[:bigint 222953] [:bigint 793456588]]]
;;     [[8037912] [[:bigint 8037912]]]
;;     [[] []]
;;     [[6694 3] [[:bigint 6694] [:nat-int 3]]])


;;  _ _   _     _                        _   _
;; (_|_) (_)_ _| |_ ___ __ _ ___ _ _ ___| |_| |_ _  _ _ __  ___   ___ ___ _ __
;;  _ _  | | ' \  _/ -_) _` / -_) '_|___|  _|  _| || | '_ \/ -_) (_-</ -_) '  \
;; (_|_) |_|_||_\__\___\__, \___|_|      \__|\__|\_, | .__/\___| /__/\___|_|_|_|
;;                     |___/                     |__/|_|

(s/def ::integer-ttype-semnasr
  (s/spec              ; means "nestable" not "spliceable" in other "regex" specs
   (s/cat :head        #{'Integer}
          :kind        #{1 2 4 8} ;; i8, i16, i32, i64
          :dimensionss (s/+ ::dimensions))))

(->> ::integer-ttype-semnasr
     s/gen
     gen/generate)
;; => (Integer
;;     1
;;     []
;;     []
;;     [352256673885445370349486716111 1624332]
;;     [2]
;;     []
;;     [86580034 64261]
;;     [8 458885090506053219617214830343524119916]
;;     [27173 1610173734]
;;     [48685 24306766029009794]
;;     [1150789934200849078335609340599 6]
;;     [2 403103245552]
;;     [3]
;;     [253202094266565303897073]
;;     [38482940056007176601892386 290444503362439372561]
;;     [2720]
;;     [44]
;;     [8627550682561420745323677756919725945686847947214688 1361957]
;;     [12683960 712272279223862022492880049742732644816473428]
;;     [883899]
;;     [4060631819931453961107624898943598271095916602396458]
;;     [4 0]
;;     [25370]
;;     [2375688583602594078904527])


;;  _     _                                  _            _   _
;; (_)_ _| |_ ___ __ _ ___ _ _   ___ __ __ _| |__ _ _ _  | |_| |_ _  _ _ __  ___
;; | | ' \  _/ -_) _` / -_) '_| (_-</ _/ _` | / _` | '_| |  _|  _| || | '_ \/ -_)
;; |_|_||_\__\___\__, \___|_|   /__/\__\__,_|_\__,_|_|    \__|\__|\_, | .__/\___|
;;               |___/                                            |__/|_|

;; Often, we need a scalar that has no dimensionss [sic].

(s/def ::integer-scalar-ttype-semnasr
  (s/spec
   (s/cat :head        #{'Integer}
          :kind        #{1 2 4 8}
          :dimensionss #{[]})))

#_
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

#_
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
          :op    :asr.autospecs/binop
          :right ::i32-constant-semnasr
          :ttype ::i32-scalar-ttype-semnasr
          :value (s/? ::i32-constant-semnasr))

   :recurse
   (let [or-leaf (s/or :leaf   ::i32-constant-semnasr
                       :branch ::i32-bin-op-semnasr)]
     (s/cat :head  #{'IntegerBinOp}
            :left  or-leaf
            :op    :asr.autospecs/binop
            :right or-leaf
            :ttype ::i32-scalar-ttype-semnasr
            :value (s/? or-leaf))) ))


(s/def ::binop-no-div
  (set/difference (eval (s/describe :asr.autospecs/binop))
                  #{'Div}))


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


;; for generation:
(defn i32-constant-semnasr
  [value]
  (let [b (expt 2 31)]
    (assert (and (>= value (- b)) (< value b))))
  (list 'IntegerConstant
        value
        '(Integer 4 [])))

#_
(gen/generate (tgen/return (i32-constant-semnasr 42)))


;;  _                               _   _     _     _    _
;; (_)_ __  _ __ _ _ _____ _____ __| | (_)_ _| |_  | |__(_)_ _  ___ _ __
;; | | '  \| '_ \ '_/ _ \ V / -_) _` | | | ' \  _| | '_ \ | ' \/ _ \ '_ \
;; |_|_|_|_| .__/_| \___/\_/\___\__,_| |_|_||_\__| |_.__/_|_||_\___/ .__/
;;         |_|                                                     |_|

;; For testing, exclude bit-ops from IntegerBinOps.
(s/def ::binop-no-bits
  (set/difference
   (eval (s/describe :asr.autospecs/binop))
   #{'BitAnd 'BitOr 'BitXor 'BitLShift, 'BitRShift}))


(defn fast-int-exp-pluggable
  "O(lg(n)) x^n, n pos or neg, pluggable primitives for base
  operations.

  Partially evaluate this on its operations, for example:
  (partial fast-int-exp-pluggable
           unchecked-multiply-int,
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
;; Because our multiplication plugin is unchecked, this can iterate
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
  "Substitute particular arithmetic ops for spec ops in Clojure.
  Our arithmetic is double-pluggable: the power operations is
  pluggable (see `fast-unchecked-exp-int`, and the entire
  collection of operations is pluggable, one level up."
  {'Add       unchecked-add-int,
   'Sub       unchecked-subtract-int,
   'Mul       unchecked-multiply-int,
   'Div       unchecked-divide-int,
   'Pow       fast-unchecked-exp-int,
   'BitAnd    bit-and,
   'BitOr     bit-or,
   'BitXor    bit-xor,
   'BitLShift bit-shift-left,
   'BitRShift bit-shift-right})

;;; TODO: Note that MOD, REM, QUOTIENT are missing!


(defn i32-bin-op-leaf-gen-pluggable
  "i32 bin-op leaf generator with pluggable operations."
  [ops-map]
  (tgen/let [left  (s/gen ::i32)
             binop (s/gen :asr.autospecs/binop)
             right (case binop
                     Div (s/gen ::i32nz)  ; don't / 0
                     Pow (if (zero? left) ; don't 0^(negative int)
                           (tgen/fmap abs (s/gen ::i32))
                           (s/gen ::i32))
                     #_default (s/gen ::i32))
             value (tgen/return  ((ops-map binop)  left right))]
    (let [tt '(Integer 4 [])
          ic (fn [i] (list 'IntegerConstant i tt))]
      (list 'IntegerBinOp (ic left) binop (ic right)
            tt (ic value)))))


;;  _ _______    _    _
;; (_)__ /_  )__| |__(_)_ _ ___ ___ _ __ ___ ___ ___ _ __  ___ ___ _ __
;; | ||_ \/ /___| '_ \ | ' \___/ _ \ '_ \___(_-</ -_) '  \(_-</ -_) '  \
;; |_|___/___|  |_.__/_|_||_|  \___/ .__/   /__/\___|_|_|_/__/\___|_|_|_|
;;                                 |_|

;; Sem-Sem means specs that are doubly semantically correct:
;;
;; - They have correct *types* in their argument lists
;;
;; - They have correct arithmetic for expressions that can (and
;; - should) be evaluated at compile time.

(s/def ::i32-bin-op-leaf-semsem
  (s/with-gen
    (fn [x] (let [[head left op right ttype value]  x]
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
                     (= ((op asr-i32-unchecked-binop->clojure-op)
                         lv rv) vv)))))
    (fn [] (i32-bin-op-leaf-gen-pluggable
            asr-i32-unchecked-binop->clojure-op))))

;;; This is checked in core_test.clj.





#_(s/def ::i32-bin-op-semsem
  (s/or
   ;; The base case is necessary. Try commenting it out and
   ;; running "lein test" at a terminal. On second thought, don't.
   :base
   (s/cat :i32-bin-op-leaf-semsem)

   :recurse
   (let [or-leaf (s/or :leaf   ::i32-bin-op-leaf-semsem
                       :const  ::i32-constant-semnasr
                       :branch ::i32-bin-op-semsem)]
     (s/cat :head  #{'IntegerBinOp}
            :left  or-leaf
            :op    :asr.autospecs/binop
            :right or-leaf
            :ttype ::i32-scalar-ttype-semnasr
            :value (s/? or-leaf))) ))



;;  ___         _        __   ___             _         _   _
;; | __|_ _  __| |  ___ / _| | _ \_ _ ___  __| |_  _ __| |_(_)___ _ _
;; | _|| ' \/ _` | / _ \  _| |  _/ '_/ _ \/ _` | || / _|  _| / _ \ ' \
;; |___|_||_\__,_| \___/_|   |_| |_| \___/\__,_|\_,_\__|\__|_\___/_||_|

;; Experimental stuff.

(let [integer-bin-op-stuff ;; SynNASR
      (filter #(= (:head %) :asr.autospecs/IntegerBinOp)
              big-list-of-stuff)]
  (-> (spec-from-composite
       (-> integer-bin-op-stuff
           first
           :form
           :ASDL-COMPOSITE))
      eval
      #_echo
      ))

(s/describe :asr.autospecs/integer-bin-op)
;; => (cat
;;     :head
;;     #function[asr.autospecs/spec-from-head-and-args/lpred--2572]
;;     :left
;;     (spec :asr.autospecs/expr)
;;     :op
;;     (spec :asr.autospecs/binop)
;;     :right
;;     (spec :asr.autospecs/expr)
;;     :type
;;     (spec :asr.autospecs/ttype)
;;     :value
;;     (? (spec :asr.autospecs/expr)))


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
