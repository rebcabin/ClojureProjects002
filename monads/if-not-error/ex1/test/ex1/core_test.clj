(ns ex1.core-test
  (:require [clojure.test        :refer :all]
            [ex1.core            :refer :all]
            [clojure.algo.monads :refer :all]))
(deftest exception-throwing-test
  (testing "exceptions are thrown"
    (is (thrown? ArithmeticException (/ 1 0)))
    (is (thrown-with-msg? ArithmeticException #"Divide by zero" (/ 1 0)))
    ))

(deftest comprehension-test
  (testing "sequence monad and comprehension"
    (is (= (domonad sequence-m
                    [a (range 5)
                     b (range a)]
                    (* a b))
           (for [a (range 5)
                 b (range a)]
             (* a b)))
        "Monadic sequence equals for comprehension")))

(defn- divisible? [n k]
  (= 0 (rem n k)))

(def ^:private not-divisible?
  (complement divisible?))

(defn- divide-out [n k]
  (if (divisible? n k)
    (recur (quot n k) k)
    n))

(defn- error-returning-check-divisibility-by [k n]
  (let [q (divide-out n k)]
    (if (= q n)
      {:error (str n ": not divisible by " k)}
      q)))

(defn- exception-throwing-check-divisibility-by [k n]
  (let [q (divide-out n k)]
    (if (= q n)
      (throw (Exception.
              (str {:error (str n ": not divisible by " k)})))
      q)))

(defn- best-small-divisor-sample [a2]
  (try
    (->> a2
        (exception-throwing-check-divisibility-by 2)
        (exception-throwing-check-divisibility-by 3)
        (exception-throwing-check-divisibility-by 5)
        (exception-throwing-check-divisibility-by 7))
    (catch Exception e (.getMessage e)))
  )

()

(defn- ugly-small-divisor-sample [a2]
  (if (not-divisible? a2 2)
    {:error (str a2 ": not divisible by 2")}
    (let [a3 (quot a2 2)]
      (if (not-divisible? a3 3)
        {:error (str a3 ": not divisible by 3")}
        (let [a5 (quot a3 3)]
          (if (not-divisible? a5 5)
            {:error (str a5 ": not divisible by 5")}
            (let [a7 (quot a5 5)]
              (if (not-divisible? a7 7)
                {:error (str a7 ": not divisible by 7")}
                {:success (str a7 ": divisible by 2, 3, 5, and 7")}
                )
              )
            )
          )
        )
      )
    )
  )

(defn- not-pretty-enough-small-divisor-sample [a2]
  (with-monad if-not-error-m
    (->
     (m-bind (m-result a2 ) (fn [a2]  (m-result (error-returning-check-divisibility-by 2 a2))))
     (m-bind  (fn [a3]  (m-result (error-returning-check-divisibility-by 3 a3))))
     (m-bind  (fn [a5]  (m-result (error-returning-check-divisibility-by 5 a5))))
     (m-bind  (fn [a7]  (m-result (error-returning-check-divisibility-by 7 a7))))
     )))

(defn- prettier-small-divisor-sample [a2]
  (domonad if-not-error-m
           [a3  (error-returning-check-divisibility-by 2 a2)
            a5  (error-returning-check-divisibility-by 3 a3)
            a7  (error-returning-check-divisibility-by 5 a5)
            a11 (error-returning-check-divisibility-by 7 a7)
            ]
           a11))

(defn- even-prettier-small-divisor-sample [a2]
  (with-monad if-not-error-m
    ((m-chain
      [(partial error-returning-check-divisibility-by 2)
       (partial error-returning-check-divisibility-by 3)
       (partial error-returning-check-divisibility-by 5)
       (partial error-returning-check-divisibility-by 7)
       ])
     a2)))

(defn- prettiest-small-divisor-sample [a2]
  (with-monad if-not-error-m
    ((m-chain
      (vec (map #(partial error-returning-check-divisibility-by %)
                [2 3 5 7])))
     a2)))

(deftest if-not-error-monad-test
  (testing "the if-not-error-monad"
    (is (=
         (ugly-small-divisor-sample 42)
         (prettier-small-divisor-sample 42)))
    (is (=
         (ugly-small-divisor-sample 42)
         (not-pretty-enough-small-divisor-sample 42)))
    (is (=
         (ugly-small-divisor-sample 42)
         (even-prettier-small-divisor-sample 42)))
    (is (=
         (ugly-small-divisor-sample 42)
         (prettiest-small-divisor-sample 42)))    )
)
