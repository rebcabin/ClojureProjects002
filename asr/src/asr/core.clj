(ns asr.core
  (:gen-class)

  (:use [asr.utils] ; TODO: winnow
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
            [clojure.set                   :as    set           ]
            [clojure.algo.monads           :as    cam           ]))


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


;;  _ _   _     _                      _   _
;; (_|_) (_)_ _| |_ ___ __ _ ___ _ _  | |_| |_ _  _ _ __  ___
;;  _ _  | | ' \  _/ -_) _` / -_) '_| |  _|  _| || | '_ \/ -_)
;; (_|_) |_|_||_\__\___\__, \___|_|    \__|\__|\_, | .__/\___|
;;                     |___/                   |__/|_|
;;  ___ ___ _ __  _ _  __ _ ____ _
;; (_-</ -_) '  \| ' \/ _` (_-< '_|
;; /__/\___|_|_|_|_||_\__,_/__/_|

(s/def ::integer-ttype-semnasr
  (s/spec              ; means "nestable" not "spliceable" in other "regex" specs
   (s/cat :head        #{'Integer}
          :kind        #{1 2 4 8} ;; i8, i16, i32, i64
          :dimensionss (s/+ ::dimensions))))

#_(->> ::integer-ttype-semnasr
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
(-> ::integer-scalar-ttype-semnasr (s/exercise 4))
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

#_(-> ::i64-scalar-ttype-semnasr (s/exercise 2))
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


;;  _     _                                     _            _
;; (_)_ _| |_ ___ __ _ ___ _ _   __ ___ _ _  __| |_ __ _ _ _| |_
;; | | ' \  _/ -_) _` / -_) '_| / _/ _ \ ' \(_-<  _/ _` | ' \  _|
;; |_|_||_\__\___\__, \___|_|   \__\___/_||_/__/\__\__,_|_||_\__|
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


;;  _     _                      _    _
;; (_)_ _| |_ ___ __ _ ___ _ _  | |__(_)_ _    ___ _ __
;; | | ' \  _/ -_) _` / -_) '_| | '_ \ | ' \  / _ \ '_ \
;; |_|_||_\__\___\__, \___|_|   |_.__/_|_||_| \___/ .__/
;;               |___/                            |_|
;;  ___ ___ _ __  _ _  __ _ ____ _
;; (_-</ -_) '  \| ' \/ _` (_-< '_|
;; /__/\___|_|_|_|_||_\__,_/__/_|

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


;;       _                     _    _       _ _______
;;  _ __| |_  _ __ _ __ _ __ _| |__| |___  (_)__ /_  )
;; | '_ \ | || / _` / _` / _` | '_ \ / -_) | ||_ \/ /
;; | .__/_|\_,_\__, \__, \__,_|_.__/_\___| |_|___/___|
;; |_|         |___/|___/
;;           _ _   _              _   _
;;  __ _ _ _(_) |_| |_  _ __  ___| |_(_)__
;; / _` | '_| |  _| ' \| '  \/ -_)  _| / _|
;; \__,_|_| |_|\__|_||_|_|_|_\___|\__|_\__|

;;    _        _                  __      _
;;   (_)__    ( )__ _  ___ ___ __/ /  ___( )
;;  / / _ \    V/  ' \/ _ `/ // / _ \/ -_)V
;; /_/_//_/    /_/_/_/\_,_/\_, /_.__/\__/
;;                        /___/

;;; Any computation steps in the maybe monad that produce nil
;;; cause the entire computation to produce nil. This is a great
;;; way to compose computations that have individual, independent
;;; failure modes like divide-by-zero, raising zero to a negative
;;; power, structural defects, and so on. If anything at all goes
;;; wrong, the whole thing produces nil. You don't need in-line
;;; nil checks littering your code.

(defn maybe-unchecked-divide-int
  "Return nil on zero divide in cam's maybe monad."
  [x y]
  (cam/domonad
   cam/maybe-m
   [a x, b y ; TODO: why not (m-result x), (m-result y)?
    :when (not (zero? b))]
   (unchecked-divide-int a b)))


(defn maybe-quot
  "Return nil on zero divide in cam's maybe monad."
  [x y]
  (cam/domonad
   cam/maybe-m
   [a x, b y ; TODO: why not (m-result x), (m-result y)?
    :when (not (zero? b))]
   (quot a b)))


(cam/with-monad cam/maybe-m
  (def maybe-abs   (cam/m-lift 1 abs))
  (def maybe-zero? (cam/m-lift 1 zero?))
  (def maybe-even? (cam/m-lift 1 even?))
  (def maybe-neg?  (cam/m-lift 1 neg?))
  (def maybe-pos?  (cam/m-lift 1 pos?)))


(defn fast-int-exp-maybe-pluggable
  "O(lg(n)) x^n, x, n zero, pos, or neg, pluggable primitives for
  base operations. Produces `nil` if `(zero? x)` and `(neg? n)` or
  underflow with negative exponent. Use it in the maybe monad of
  clojure.algo.monads, more composable than alternatives that
  check for zero to negative powers.

  Partially evaluate this on its operations, for example:

      (partial fast-int-exp-maybe-pluggable
               maybe-unchecked-multiply-int,
               maybe-unchecked-divide-int,
               maybe-unchecked-subtract-int)
  "
  [maybe-mul, maybe-div, maybe-sub, x n]
  (cam/domonad
   cam/maybe-m
   [v (loop [acc (m-result 1),  b (m-result x),  e (maybe-abs n)]
        (if (maybe-zero? e)
          acc
          (if (maybe-even? e)
            (recur        acc        (maybe-mul b b) (maybe-div e 2))
            (recur  (maybe-mul acc b)       b        (maybe-sub e 1)))))]
   (if (neg? n)
     (maybe-div 1 v)
     v)))


(def maybe-fast-unchecked-i32-exp
  "Produces `(maybe/just 0)` for 2^32, 2^33, ... . Underflows
  negative exponents to nil. Div by zero or 0 to a negative power
  produce nil. Spins unchecked multiplications. Spins large (>=
  32) powers of 2 on 0. See core_test.clj"
  (cam/with-monad cam/maybe-m
    (partial fast-int-exp-maybe-pluggable
             (cam/m-lift 2 unchecked-multiply-int),
             maybe-unchecked-divide-int,
             (cam/m-lift 2 unchecked-subtract-int))))


(def asr-i32-unchecked-binop->clojure-op
  "Substitute particular arithmetic ops for spec ops in Clojure.
  Our arithmetic is double-pluggable: the power operations is
  pluggable (see `fast-unchecked-i32-exp`, and the entire
  collection of operations is pluggable, one level up."
  (cam/with-monad cam/maybe-m
    {'Add       (cam/m-lift 2 unchecked-add-int),
     'Sub       (cam/m-lift 2 unchecked-subtract-int),
     'Mul       (cam/m-lift 2 unchecked-multiply-int),
     'Div       maybe-unchecked-divide-int,
     'Pow       maybe-fast-unchecked-i32-exp,
     'BitAnd    (cam/m-lift 2 #(.intValue (bit-and         %1 %2))),
     'BitOr     (cam/m-lift 2 #(.intValue (bit-or          %1 %2))),
     'BitXor    (cam/m-lift 2 #(.intValue (bit-xor         %1 %2))),
     'BitLShift (cam/m-lift 2 #(.intValue (bit-shift-left  %1 %2))),
     'BitRShift (cam/m-lift 2 #(.intValue (bit-shift-right %1 %2)))}))

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

(defn i32-bin-op-leaf-semsem-gen-pluggable
  "Given an ops-map from ASR binops to implementations, generate i32
  ASR IntegerBinOp leaf node. It's the generator for
  spec ::i32-bin-op-leaf-semsem."
  [ops-map]
  (let [tt '(Integer 4 [])
        ic (fn [i] (list 'IntegerConstant i tt))]
    (tgen/let [left  (s/gen ::i32)
               binop (s/gen #_#{'Div 'Pow} :asr.autospecs/binop)
               right (s/gen ::i32)]
      (let [value ((ops-map binop) left right)]
        (if (nil? value)  nil
          (list 'IntegerBinOp
                (ic left)
                binop
                (ic right)
                tt
                (ic value)))))))


(defn i32-bin-op-value-slot
  [i32bop]
  (second (nth i32bop 5)))


(s/def ::i32-bin-op-leaf-semsem
  (s/with-gen
    (fn [i32bop]
      (try
        (let [[head left op right ttype value]  i32bop]
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
                     lv rv) vv))))
        (catch UnsupportedOperationException e
          #_"bad structure" false)
        (catch IllegalArgumentException e
          #_"bad operator" false)
        (catch NullPointerException e
          #_"something else bad" false)))

    (fn [] (i32-bin-op-leaf-semsem-gen-pluggable
            asr-i32-unchecked-binop->clojure-op))))

;;; See core_test.clj for examples showing propagated failures.

;;  _ _______   _    _
;; (_)__ /_  ) | |__(_)_ _    ___ _ __   ___ ___ _ __  ___ ___ _ __
;; | ||_ \/ /  | '_ \ | ' \  / _ \ '_ \ (_-</ -_) '  \(_-</ -_) '  \
;; |_|___/___| |_.__/_|_||_| \___/ .__/ /__/\___|_|_|_/__/\___|_|_|_|
;;                               |_|

;;; forward reference (backpatch later)

(s/def ::i32-bin-op-semsem
  (s/or :i32bop ::i32-bin-op-leaf-semsem
        :i32con ::i32-constant-semnasr))


(defn maybe-value-i32-semsem
  "Given an IntegerConstant or an IntegerBinOp (icobo), fetch the
  value, if there is one. Return it in cam's maybe monad: get nil
  if there is anything invalid about the input. An IntegerBinOp is
  semsem-valid if its two inputs, left and right, are semsem-valid
  and its output value equals the operator applied to the two
  inputs.

  Propagates any nils in the inputs.
  "
  [icobo]
  (cond ;; order matters
    ,(s/valid? ::i32-constant-semnasr icobo)
    (let [[_, v, _] icobo] v)           ; count be nil
    ,(s/valid? ::i32-bin-op-semsem icobo)
    (let [[_, _, _, _, _, cv] icobo]
      (let [[_, v, _] cv] v))           ; could be nil
    ,:else
    nil))


(defn i32-bin-op-semsem-gen-pluggable
  "Given an ops-map from ASR binops to implementations, generate an
  i32 ASR IntegerBinOp node, recursively. TODO: put in cam's maybe
  monad."
  [ops-map]
  (cam/with-monad cam/maybe-m
    (let [tt   '(Integer 4 [])
          ic    (fn [i] (list 'IntegerConstant i tt))
          res   (fn [l b r v] (list 'IntegerBinOp l b r tt v))
          meval (cam/m-lift 1 maybe-value-i32-semsem)]
      (gen/one-of
       [ ;; base case
        (s/gen ::i32-bin-op-leaf-semsem)
        ;; recurse
        (tgen/let [lbop   (s/gen ::i32-bin-op-semsem)
                   rbop   (s/gen ::i32-bin-op-semsem)
                   binop_ (s/gen :asr.autospecs/binop)]
          (cam/domonad
           cam/maybe-m
           [left  (meval lbop)
            right (meval rbop)
            value ((ops-map binop_) left right)]
           (res lbop binop_ rbop (ic value))))]))))

(gen/sample (i32-bin-op-semsem-gen-pluggable
             asr-i32-unchecked-binop->clojure-op) 5)


(let [the-gen (i32-bin-op-semsem-gen-pluggable
               asr-i32-unchecked-binop->clojure-op)]
  (s/def ::i32-bin-op-semsem
    (s/with-gen
      (s/or
       ,:base
       ::i32-bin-op-leaf-semsem
       ,:recurse
       (let [or-leaf (s/or :leaf   ::i32-bin-op-leaf-semsem
                           :branch ::i32-bin-op-semsem)]
         (s/cat :head  #{'IntegerBinOp}
                :left  or-leaf
                :op    :asr.autospecs/binop
                :right or-leaf
                :ttype ::i32-scalar-ttype-semnasr
                :value ::i32-constant-semnasr)))
      (fn [] the-gen))))

(binding [s/*recursion-limit* 4]
  (gen/sample (s/gen ::i32-bin-op-semsem) 1))


(s/valid? ::i32-bin-op-semsem
          '(IntegerBinOp
            (IntegerConstant -1 (Integer 4 []))
            Add
            (IntegerConstant 284 (Integer 4 []))
            (Integer 4 [])
            (IntegerConstant 283 (Integer 4 []))))

(s/describe ::i32-bin-op-semsem)


;;  ___         _        __   ___             _         _   _
;; | __|_ _  __| |  ___ / _| | _ \_ _ ___  __| |_  _ __| |_(_)___ _ _
;; | _|| ' \/ _` | / _ \  _| |  _/ '_/ _ \/ _` | || / _|  _| / _ \ ' \
;; |___|_||_\__,_| \___/_|   |_| |_| \___/\__,_|\_,_\__|\__|_\___/_||_|

;; Experimental stuff.


;;  _     _                      _    _
;; (_)_ _| |_ ___ __ _ ___ _ _  | |__(_)_ _    ___ _ __
;; | | ' \  _/ -_) _` / -_) '_| | '_ \ | ' \  / _ \ '_ \
;; |_|_||_\__\___\__, \___|_|   |_.__/_|_||_| \___/ .__/
;;               |___/                            |_|
;;            _
;;  __ _ _  _| |_ ___ ____ __  ___ __
;; / _` | || |  _/ _ (_-< '_ \/ -_) _|
;; \__,_|\_,_|\__\___/__/ .__/\___\__|
;;                      |_|

(println "Experimental hand-aided autospec for IntegerBinOp ~~~> integer-bin-op")

(let [integer-bin-op-stuff ;; SynNASR
      (filter #(= (:head %) :asr.autospecs/IntegerBinOp)
              big-list-of-stuff)]
  (-> (asr.autospecs/spec-from-composite
       (-> integer-bin-op-stuff
           first
           :form
           :ASDL-COMPOSITE))
      eval
      echo
      ))

#_
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
