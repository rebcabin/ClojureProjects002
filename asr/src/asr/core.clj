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
            [clojure.math                  :refer [pow]         ] ;; int
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

(defn fast-int-exp-maybe-pluggable
  "O(lg(n)) x^n, x, n zero, pos, or neg, pluggable primitives for
  base operations in the maybe monad of clojure.algo.monads, more
  composable than alternatives that check for zero to negative
  powers. Produces `nil` if `(zero? x)` and `(neg? n)`. Produces
  `underflow-val` on underflow.

  Partially evaluate this on its operations, for example:

      (partial fast-int-exp-pluggable
               unchecked-multiply-int,
               unchecked-divide-int,
               unchecked-subtract-int,
               Integer/MIN_VALUE)

  or

      (partial fast-int-exp-pluggable
               unchecked-multiply-int,
               unchecked-divide-int,
               unchecked-subtract-int,
               0)
  "
  [mul, div, sub, underflow-val, x n]
  (if (and (zero? x) (neg? n))
    nil                                 ; cam-speak for "nothing"
    (if (neg? n)                        ; recurse
      (cam/domonad                      ; propagates "nil"
       cam/maybe-m
       [trial (fast-int-exp-maybe-pluggable
               mul, div, sub, underflow-val,
               x (- n))]
       (case trial
         ;; In case x^(abs n) == 0
         0 underflow-val
         ;; Most often, (quot 1 trial) is zero, but sometimes it's
         ;; 1/1 = 1. The following handles that case.
         (quot 1 trial)
         ))                             ; (pos? n): loop
      (loop [acc 1,  b x,  e n]
        (if (zero? e)
          acc
          (if (even? e)
            (recur       acc   (mul b b) (div e 2))
            (recur  (mul acc b)     b    (sub e 1))))))))


(def fast-unchecked-i32-exp-maybe
  "Produces `(maybe/just 0)` for 2^32, 2^33, ... . Underflows
  negative exponents to `(maybe/just 0)` (or perhaps
  to `(maybe/just Integer/MIN_VALUE)?). Spins unchecked
  multiplications. Spins large (>= 32) powers of 2 on 0. See
  core_test.clj"
  (partial fast-int-exp-maybe-pluggable
           unchecked-multiply-int,
           unchecked-divide-int,
           unchecked-subtract-int,
           0 #_Integer/MIN_VALUE))


;;          _ __  __         __           __    _
;;  _    __(_) /_/ /    ____/ /  ___ ____/ /__ (_)__  ___ _
;; | |/|/ / / __/ _ \  / __/ _ \/ -_) __/  '_// / _ \/ _ `/
;; |__,__/_/\__/_//_/  \__/_//_/\__/\__/_/\_\/_/_//_/\_, /
;;                                                  /___/

(defn fast-int-exp-pluggable
  "O(lg(n)) x^n, x, n zero, pos or neg, pluggable primitives for
  base operations. Asserts if (zero? x) and (neg? n). Produces
  `underflow-val` on underflow.

  Partially evaluate this on its operations, for example:

      (partial fast-int-exp-pluggable
               unchecked-multiply-int,
               unchecked-divide-int,
               unchecked-subtract-int,
               Integer/MIN_VALUE)

  or

      (partial fast-int-exp-pluggable
               unchecked-multiply-int,
               unchecked-divide-int,
               unchecked-subtract-int,
               0)
  "
  [mul, div, sub, underflow-val, x n]
  (assert (not (and (zero? x) (neg? n))))
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


(def fast-unchecked-i32-exp
  "Produces zero for 2^32, 2^33, ... . Underflows negative exponents
  to 0 (Integer/MIN_VALUE?). Spins unchecked multiplications.
  Spins large (>= 32) powers of 2 on 0. See core_test.clj"
  (partial fast-int-exp-pluggable
           unchecked-multiply-int,
           unchecked-divide-int,
           unchecked-subtract-int,
           0 #_Integer/MIN_VALUE))


(def asr-i32-unchecked-binop->clojure-op
  "Substitute particular arithmetic ops for spec ops in Clojure.
  Our arithmetic is double-pluggable: the power operations is
  pluggable (see `fast-unchecked-i32-exp`, and the entire
  collection of operations is pluggable, one level up."
  {'Add       unchecked-add-int,
   'Sub       unchecked-subtract-int,
   'Mul       unchecked-multiply-int,
   'Div       unchecked-divide-int,
   'Pow       fast-unchecked-i32-exp,
   'BitAnd    bit-and,
   'BitOr     bit-or,
   'BitXor    bit-xor,
   'BitLShift bit-shift-left,
   'BitRShift bit-shift-right})

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

(defn i32-bin-op-rhs-gen
  "Generate RHS valid for any plugged-in arithmetic. Avoid zero
  divisors. "
  [left, binop]
  (case binop
    Div (s/gen ::i32nz)  ; don't / 0
    Pow (if (zero? left) ; don't 0^(negative int)
          (tgen/fmap abs (s/gen ::i32))
          (s/gen ::i32))
    #_default (s/gen ::i32)))


(defn i32-bin-op-leaf-semsem-gen-pluggable
  "Given an ops-map from ASR binops to implementations, generate i32
  ASR IntegerBinOp leaf node. It's the generator for
  spec ::i32-bin-op-leaf-semsem"
  [ops-map]
  (tgen/let [left  (s/gen ::i32)
             binop (s/gen :asr.autospecs/binop)
             right (i32-bin-op-rhs-gen left binop)
             value (tgen/return ((ops-map binop) left right))]
    (let [tt '(Integer 4 [])
          ic (fn [i] (list 'IntegerConstant i tt))]
      (list 'IntegerBinOp (ic left) binop (ic right)
            tt (ic value)))))


(s/def ::i32-bin-op-leaf-semsem
  (s/with-gen
    (fn [x]
      (try
        (let [[head left op right ttype value]  x]
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
          #_"bad structure" false)))
    (fn [] (i32-bin-op-leaf-semsem-gen-pluggable
            asr-i32-unchecked-binop->clojure-op))))

#_(gen/generate (s/gen ::i32-bin-op-leaf-semsem))
;; => (IntegerBinOp
;;     (IntegerConstant -3093 (Integer 4 []))
;;     BitAnd
;;     (IntegerConstant -100753 (Integer 4 []))
;;     (Integer 4 [])
;;     (IntegerConstant -101781 (Integer 4 [])))


;;  _ _______   _    _
;; (_)__ /_  ) | |__(_)_ _    ___ _ __   ___ ___ _ __  ___ ___ _ __
;; | ||_ \/ /  | '_ \ | ' \  / _ \ '_ \ (_-</ -_) '  \(_-</ -_) '  \
;; |_|___/___| |_.__/_|_||_| \___/ .__/ /__/\___|_|_|_/__/\___|_|_|_|
;;                               |_|

;;; forward reference (backpatch later)

(s/def ::i32-bin-op-semsem
  ::i32-bin-op-leaf-semsem)


(defn maybe-value-i32-semsem
  "Given an IntegerConstant or an IntegerBinOp (icobo), fetch the
  value, if there is one. Return it in cam's maybe monad: get nil
  if there is anything invalid about the input. An IntegerBinOp is
  semsem-valid if its two inputs, left and right, are semsem-valid
  and its output value equals the operator applied to the two
  inputs.

  There are two difficult cases: explicit Div by zero and zero to
  a negative Pow, an implicit div-by-0 (0^0 is defined as 1).
  Because they both reduce to div-by-0, they are instances of the
  same difficult case.

  Alternative 1 (structural) is to exclude these cases, i.e.,
  never generate instances of div-by0.

  Alternative 2 (arithmetic) is to include these cases with an
  overflow sigil like Integer/MAX_VALUE in the value slot. Because
  the purpose of this entire project is to generate test strings
  for ASR back ends, and because instances might trip bugs in the
  back ends, this alternative is viable.

  Because the arithmetic is pluggable, both alternatives are easy
  to implement. "
  [icobo]
  (cond
    ,(s/valid? ::i32-bin-op-semsem icobo)
    (let [[_, _, _, _, _, cv] icobo]
      (let [[_, v, _] cv] v))
    ,(s/valid? ::i32-constant-semnasr icobo)
    (let [[_, v, _] icobo] v)
    ,:else
    nil))


(defn i32-bin-op-semsem-gen-pluggable
  "Given an ops-map from ASR binops to implementations, generate an
  i32 ASR IntegerBinOp node, recursively. TODO: put in cam's maybe
  monad."
  [ops-map]
  (gen/one-of
   ;; base case
   [(s/gen ::i32-bin-op-leaf-semsem)
    ;; recurse left
    (tgen/let [left-bop (s/gen ::i32-bin-op-leaf-semsem)
               binop_   (s/gen :asr.autospecs/binop)]
      (pprint {"left-bop" left-bop})
      (let [left  (maybe-value-i32-semsem left-bop)
            _     (assert left)
            binop (ops-map binop_)]
        (pprint {"left" left})
        (tgen/let [right (i32-bin-op-rhs-gen left binop)]
          (let [value (binop left right)
                tt    '(Integer 4 [])
                ic    (fn [i] (list 'IntegerConstant i tt))]
            (list 'IntegerBinOp
                  left-bop
                  binop
                  (ic right)
                  tt (ic value))))))
    ;; recurse right
    (tgen/let [right-bop (s/gen ::i32-bin-op-leaf-semsem)
               binop_    (s/gen :asr.autospecs/binop)]
      (pprint {"right-bop" right-bop})
      (let [right (maybe-value-i32-semsem right-bop) ; PRAY!
            _     (assert right)
            binop (ops-map binop_)]
        (pprint {"right" right})))
    ]))

(gen/generate (i32-bin-op-semsem-gen-pluggable
               asr-i32-unchecked-binop->clojure-op))


#_(s/def ::i32-bin-op-semsem
  (s/with-gen
    (s/or ,:base
          ::i32-bin-op-leaf-semsem
          ,:lrecurse
          (let [or-leaf (s/or :cleaf   ::i32-constant-semnasr
                              :branch ::i32-bin-op-semsem)]
            (s/cat :head  #{'IntegerBinOp}
                   :left  or-leaf
                   :op    :asr.autospecs/binop
                   :right or-leaf
                   :ttype ::i32-scalar-ttype-semnasr
                   :value (s/? or-leaf)))
          )
    (fn [] (gen/return 42))))

#_(gen/generate (s/gen ::i32-bin-op-semsem))


(s/valid? ::i32-bin-op-semsem
          '(IntegerBinOp
            (IntegerConstant -1 (Integer 4 []))
            Add
            (IntegerConstant 284 (Integer 4 []))
            (Integer 4 [])
            (IntegerConstant 283 (Integer 4 []))))

(s/describe ::i32-bin-op-semsem)

#_
(eval-i32-bin-op-semsem
 '(IntegerBinOp
   (IntegerConstant -131974 (Integer 4 []))
   Pow
   (IntegerConstant 630 (Integer 4 []))
   (Integer 4 [])
   (IntegerConstant 43 (Integer 4 []))))


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
