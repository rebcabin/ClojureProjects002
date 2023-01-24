(ns asr.expr.semnasr

  (:use     [asr.numbers])

  (:require [clojure.spec.alpha            :as    s         ]
            [clojure.spec.gen.alpha        :as    gen       ]
            [clojure.test.check.generators :as    tgen      ]))


;;  _     _                      _   _
;; (_)_ _| |_ ___ __ _ ___ _ _  | |_| |_ _  _ _ __  ___
;; | | ' \  _/ -_) _` / -_) '_| |  _|  _| || | '_ \/ -_)
;; |_|_||_\__\___\__, \___|_|    \__|\__|\_, | .__/\___|
;;               |___/                   |__/|_|

(s/def ::integer-ttype
  (s/spec              ; means "nestable" not "spliceable" in other "regex" specs
   (s/cat :head        #{'Integer}
          :grup        #{1 2 4 8} ;; i8, i16, i32, i64
          :dimensionss (s/+ :asr.specs/dimensions))))

#_
(->> ::integer-ttype
     s/gen
     gen/generate)
;; => (Integer
;;     1
;;     []
;;     [352256673885445370349486716111 1624332]
;;     [2]
;;     [86580034 64261]
;;     [8 458885090506053219617214830343524119916]
;;     [27173 1610173734]
;;     [48685 24306766029009794]
;;     [1150789934200849078335609340599 6]
;;     [8627550682561420745323677756919725945686847947214688 1361957]
;;     [12683960 712272279223862022492880049742732644816473428]
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

(s/def ::integer-scalar-ttype
  (s/spec
   (s/cat :head        #{'Integer}
          :grup        #{1 2 4 8}
          :dimensionss #{[]})))

#_
(-> ::integer-scalar-ttype (s/exercise 4))
;; => ([(Integer 2 []) {:head Integer, :grup 2, :dimensionss []}]
;;     [(Integer 4 []) {:head Integer, :grup 4, :dimensionss []}]
;;     [(Integer 1 []) {:head Integer, :grup 1, :dimensionss []}]
;;     [(Integer 8 []) {:head Integer, :grup 8, :dimensionss []}])


;; Particular ones so we can match the "kind," i8 thru i64, by
;; hand. Written in this funny way for test-generation.

(do
  (s/def ::i8-scalar-ttype
    (s/spec ; nestable
     (s/cat :head        #{'Integer}
            :grup        #{1}
            :dimensionss #{[]})))

  (s/def ::i16-scalar-ttype
    (s/spec ; nestable
     (s/cat :head        #{'Integer}
            :grup        #{2}
            :dimensionss #{[]})))

  (s/def ::i32-scalar-ttype
    (s/spec ; nestable
     (s/cat :head        #{'Integer}
            :grup        #{4}
            :dimensionss #{[]})))

  (s/def ::i64-scalar-ttype
    (s/spec ; nestable
     (s/cat :head        #{'Integer}
            :grup        #{8}
            :dimensionss #{[]}))))

#_
(-> ::i64-scalar-ttype (s/exercise 2))
;; => ([(Integer 8 []) {:head Integer, :grup 8, :dimensionss []}]
;;     [(Integer 8 []) {:head Integer, :grup 8, :dimensionss []}])

;; TEACHING NOTE: With nesting. Because every spec is wrapped in
;; s/spec, results are nested under s/alt.

#_
(-> (s/alt ::i8-scalar-ttype
           ::i16-scalar-ttype
           ::i32-scalar-ttype
           ::i64-scalar-ttype)
    (s/exercise 2))
;; => ([[(Integer 2 [])]
;;      [:asr.core/i8-scalar-ttype
;;       {:head Integer, :grup 2, :dimensionss []}]]
;;     [[(Integer 8 [])]
;;      [:asr.core/i32-scalar-ttype
;;       {:head Integer, :grup 8, :dimensionss []}]])

;; TEACHING NOTE: Without nesting. Results are not nested under
;; s/or.

#_
(-> (s/or :1 ::i8-scalar-ttype
          :2 ::i16-scalar-ttype
          :4 ::i32-scalar-ttype
          :8 ::i64-scalar-ttype)
    (s/exercise 2))
;; => ([(Integer 8 []) [:8 {:head Integer, :grup 8, :dimensionss []}]]
;;     [(Integer 1 []) [:1 {:head Integer, :grup 1, :dimensionss []}]])


;;  _     _                                     _            _
;; (_)_ _| |_ ___ __ _ ___ _ _   __ ___ _ _  __| |_ __ _ _ _| |_
;; | | ' \  _/ -_) _` / -_) '_| / _/ _ \ ' \(_-<  _/ _` | ' \  _|
;; |_|_||_\__\___\__, \___|_|   \__\___/_||_/__/\__\__,_|_||_\__|
;;               |___/

(do
  (s/def ::i8-constant
    (s/spec
     (s/cat :head  #{'IntegerConstant}
            :value :asr.numbers/i8
            :ttype :asr.expr.semnasr/i8-scalar-ttype)))

  (s/def ::i16-constant
    (s/spec
     (s/cat :head  #{'IntegerConstant}
            :value :asr.numbers/i16
            :ttype :asr.expr.semnasr/i16-scalar-ttype)))

  (s/def ::i32-constant
    (s/spec
     (s/cat :head  #{'IntegerConstant}
            :value :asr.numbers/i32
            :ttype :asr.expr.semnasr/i32-scalar-ttype)))

  (s/def ::i64-constant
    (s/spec
     (s/cat :head  #{'IntegerConstant}
            :value :asr.numbers/i64
            :ttype :asr.expr.semnasr/i64-scalar-ttype))))


(s/def ::integer-constant
  (s/or :i8  ::i8-constant
        :i16 ::i16-constant
        :i32 ::i32-constant
        :i64 ::i64-constant))

;; for interactive testing in CIDER:
#_(-> ::integer-constant
    (s/exercise 2))
;; => ([(IntegerConstant 0 (Integer 2 []))
;;      [:i16
;;       {:head IntegerConstant,
;;        :value 0,
;;        :ttype {:head Integer, :grup 2, :dimensionss []}}]]
;;     [(IntegerConstant -1 (Integer 4 []))
;;      [:i32
;;       {:head IntegerConstant,
;;        :value -1,
;;        :ttype {:head Integer, :grup 4, :dimensionss []}}]])


(do
  (s/def ::i8-non-zero-constant
    (s/spec
     (s/cat :head  #{'IntegerConstant}
            :value :asr.numbers/i8nz
            :ttype :asr.expr.semnasr/i8-scalar-ttype)))

  (s/def ::i16-non-zero-constant
    (s/spec
     (s/cat :head  #{'IntegerConstant}
            :value :asr.numbers/i16nz
            :ttype :asr.expr.semnasr/i16-scalar-ttype)))

  (s/def ::i32-non-zero-constant
    (s/spec
     (s/cat :head  #{'IntegerConstant}
            :value :asr.numbers/i32nz
            :ttype :asr.expr.semnasr/i32-scalar-ttype)))

  (s/def ::i64-non-zero-constant
    (s/spec
     (s/cat :head  #{'IntegerConstant}
            :value :asr.numbers/i64nz
            :ttype :asr.expr.semnasr/i64-scalar-ttype))))


(s/def ::integer-non-zero-constant
  (s/or :i8  ::i8-non-zero-constant
        :i16 ::i16-non-zero-constant
        :i32 ::i32-non-zero-constant
        :i64 ::i64-non-zero-constant))

#_
(-> ::integer-non-zero-constant
    (s/exercise 4))
;; => ([(IntegerConstant 1 (Integer 2 []))
;;      [:i16
;;       {:head IntegerConstant,
;;        :value 1,
;;        :ttype {:head Integer, :grup 2, :dimensionss []}}]]
;;     [(IntegerConstant -1 (Integer 8 []))
;;      [:i64
;;       {:head IntegerConstant,
;;        :value -1,
;;        :ttype {:head Integer, :grup 8, :dimensionss []}}]]
;;     [(IntegerConstant -1 (Integer 8 []))
;;      [:i64
;;       {:head IntegerConstant,
;;        :value -1,
;;        :ttype {:head Integer, :grup 8, :dimensionss []}}]]
;;     [(IntegerConstant -1 (Integer 1 []))
;;      [:i8
;;       {:head IntegerConstant,
;;        :value -1,
;;        :ttype {:head Integer, :grup 1, :dimensionss []}}]])


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

(s/def ::i32-bin-op
  (s/or
   ;; The base case is necessary. Try commenting it out and
   ;; running "lein test" at a terminal.
   :base
   (s/cat :head  #{'IntegerBinOp}
          :left  ::i32-constant
          :op    :asr.autospecs/binop
          :right ::i32-constant
          :ttype :asr.expr.semnasr/i32-scalar-ttype
          :value (s/? ::i32-constant))

   :recurse
   (let [or-leaf (s/or :leaf   ::i32-constant
                       :branch ::i32-bin-op)]
     (s/cat :head  #{'IntegerBinOp}
            :left  or-leaf
            :op    :asr.autospecs/binop
            :right or-leaf
            :ttype :asr.expr.semnasr/i32-scalar-ttype
            :value (s/? or-leaf))) ))


;; To visualize the tree, uncomment this and do "lein run" at a
;; terminal.
#_(-> ::i32-bin-op
      (s/exercise 25)
      inspect-tree)
