(ns integer-arithmetic.core
  (:require [clojure.pprint :refer :all]))

(import 'java.lang.ArithmeticException)

(defmacro explain-expr
  "Produce a string representation of the unevaluated expression x, concatenated
  to an arrow and a string representation of the result of evaluating x,
  including Exceptions should they arise."
  [x]
  `(str ~(str x) " ~~> "
        (try ~x (catch Exception e# (str e#)))))

(println (explain-expr (* 42 42)))
(println (explain-expr (let [x 1] x)))
(println (explain-expr (/ 6 0)))
(println (let [x 1] (explain-expr x)))
(let [y 37] (println (explain-expr (let [x 19] (* x y)))))
(let [y 37] (println (explain-expr (let [y 19] (* y y)))))

(defmacro explain-exprs
  "Produce string representations of the unevaluated expressions xs, concatenated
  to arrows and string representations of the results of evaluating each
  expression, including Exceptions should they arise."
  [& xs]
  (into [] (map (fn [x]
                  `(str ~(str x) " ~~> "
                        (try ~x (catch Exception e# (str e#)))))
                xs)))

(clojure.pprint/pprint
 (let [y 37]
   (explain-exprs
    (* 42 42)
    (let [x 19] (* x y))
    (let [y 19] (* y y))
    (* y y)
    (/ 6 0))))

(defmacro explanation-map
  "Produce a hashmap from string representations of the unevaluated expressions
  exprs to the results of evaluating each expression in exprs, including
  Exceptions should they arise."
  [& exprs]
  (into {}
        (map (fn [expr]
               `[~(str expr)
                 (try ~expr (catch Exception e# (str e#)))])
             exprs)))

(clojure.pprint/pprint
 (let [y 37]
   (explanation-map
    (* 42 42)
    (let [x 19] (* x y))
    (let [y 19] (* y y))
    (* y y)
    (/ 6 0))))


(defn euclidean-quotient [^Integer n ^Integer d]
  "Produces the quotient of integers n (numerator) and d (denominator) by
   Euclid's method of antanaresis. See
   https://www.britannica.com/biography/Euclid-Greek-mathematician and
   https://en.wikipedia.org/wiki/Euclidean_algorithm."
  (if (= d 0) (throw (ArithmeticException. "")))
  (loop [q -1      ;; bind q to -1, and
         t  n]     ;; bind t to n, then start computing ...
    ;; (println)
    (if (< t 0)    ;; if (< t 0) == t < 0 (move binary operator to the right)
      q            ;; true branch
      (recur       ;; false branch: goto "loop" ...
       (inc q)     ;; with q bound to (inc q)
       (- t d))))) ;; and t bound to (- t d) == t-d

(defn corrected-euclidean-quotient [^Integer n ^Integer d]
  "Produces the quotient of integers n (numerator) and d (denominator) by
   Euclid's method of antanaresis. See
   https://www.britannica.com/biography/Euclid-Greek-mathematician and
   https://en.wikipedia.org/wiki/Euclidean_algorithm."
  (if (= d 0) (throw (ArithmeticException. "")))
  (letfn [(sign [x] (if (< x 0) -1 1))]
    (let [cn (java.lang.Math/abs n)
          cd (java.lang.Math/abs d)
          sn (sign n)
          sd (sign d)]
      ;; (pdump cn) (pdump cd) (pdump sn) (pdump sd)
      (loop [q -1      ;; bind q to -1, and
             t  cn]    ;; bind t to n, then start computing ...
        ;; (pdump q)
        ;; (pdump t)
        ;; (println)
        (if (< t 0)    ;; if (< t 0) == t < 0 (move binary operator to the right)
          (* sn sd q)  ;; true branch
          (recur       ;; false branch: goto "loop" ...
           (inc q)     ;; with q bound to (inc q)
           (- t cd)))))))
