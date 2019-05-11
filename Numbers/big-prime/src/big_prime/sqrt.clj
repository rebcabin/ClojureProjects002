(ns big-prime.sqrt
  (:use [clojure.core.contracts :as contracts]))

;;; It's unclear whether ^clojure.lang.BigInt type hints actually
;;; improve perf.
;;; TODO: Use Criterium library to profile.
;;; TODO: Rename as number-theoretic functions.

(defn sum 
  ([] 0)
  ([x] x)
  ([x & more] (reduce + x more)))

(defn product
  ([] 1)
  ([x] x)
  ([x & more] (reduce * x more)))

(defn nt-average
  "Number-theoretic mean using `quot' instead of `/', which latter produces rationals"
  ([] 0)
  ([x] x)
  ([x & more]
     (quot (+ x (apply sum more))
           (inc (count more)))))

(defn abs
  "Absolute value"
  [x]
  (if (< x 0) (- x) x))

(defn square [x] (* x x))

(defn- nt-improve
  "Improve a guess of a square root by number-theoretic average with the quotient of the guess with the target: an adaptation of Newton's method to the integer domain."
  [guess x]
  (nt-average guess (quot x guess)))

(defn- good-enough?
  "A guess is good enough if its square is lte the target and the square of its increment is gt the target"
  [guess x]
  (and
   (<= (square guess) x)
   (>  (square (inc guess)) x)))

(defn- nt-try-sqrt [guess x]
  (if (good-enough? guess x)
    guess
    (recur (nt-improve guess x) x)))

(defn nt-sqrt
  "Number-theoretic square root (largest integer whose square is less than or equal to the target)"
  [x]
  (nt-try-sqrt 1 (bigint x)))

;;; TODO: Build up the contracts in here. See nt-power for an example.
;;; TODO: move nt-power to sqrt; rename sqrt to nt (for number-theoretic)

(defn nt-power [n m]
  ;; Also consider: (reduce * 1N (repeat m n))
  (letfn [(helper [n m acc]
             (cond
              (== m 0) 1N
              (== m 1) acc
              :else (recur n (dec m) (* n acc))))]
    (helper (bigint n) m (bigint n))))

(contracts/provide
 (nt-power
  "Constraints for number-theoretic power function"
  [n m] [(integer? m)
         (not (neg? m))
         (number? n)
         =>
         number?
         (if (pos? n) (pos? %) true)]))

