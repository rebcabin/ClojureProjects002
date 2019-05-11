(ns number-theory-clojars-exercise.core-test
  (:require [clojure.test :refer :all]
            [number-theory-clojars-exercise.core :refer :all]
            [number-theory.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 5 (gcd 5 15 45)))))

(deftest utilities-test
  (let []
    (is (thrown? Exception (gcd 0 0)))
    (is (thrown? Exception (gcd 0 42)))
    (is (thrown? Exception (gcd 42 0)))
    (is (thrown? ArithmeticException (divides? 0 0)))
    (is (thrown? ArithmeticException (divides? 0 42)))
    (is (divides?  2  4))
    (is (divides?  2 -4))
    (is (divides? -2 -4))
    (is (divides? -2  4))
    (is (not (divides?  2  47)))
    (is (not (divides?  2 -47)))
    (is (not (divides? -2 -47)))
    (is (not (divides? -2  47)))
    (is (= 100 (integer-power 10 2)))
    (is (= 100 (gcd (integer-power 10 2)
                    (integer-power 10 3))))

    (is (= 1 (integer-power 42 0)))
    (is (= 1 (integer-power -42 0)))
    (is (= 1 (integer-power 0 0)))
    (is (= 0 (integer-power 0 42)))
    (is (thrown? Exception (integer-power 0 -42)))
    (is (thrown? Exception (integer-power 15 -42)))
    (is (= 1 (gcd 3 5)))
    (is (= 1 (gcd 3 5 7)))
    (is (= 5 (gcd 5 15)))
    (is (= 5 (gcd 5 15 45)))))
