(ns asr.arithmetic

  (:require [clojure.spec.alpha            :as    s         ]
            [clojure.pprint                :refer [pprint]  ]))


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
