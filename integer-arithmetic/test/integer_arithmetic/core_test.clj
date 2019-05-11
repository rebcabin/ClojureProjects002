(ns integer-arithmetic.core-test
  (:require [clojure.test :refer :all]
            [clojure.test.check :as tc]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [integer-arithmetic.core :refer :all]))

(deftest euclidean-quotient-tests
  (testing "ad-hoc test"
    (is (= 2 (euclidean-quotient 13 5))))
  (testing "nicomachus test"
    (is (= 2 (euclidean-quotient 49 21))))
  (testing "zero-dividend"
    (is (= 0 (euclidean-quotient 0 42))))
  (testing "zero divisor"
    (is (thrown? ArithmeticException
                 (euclidean-quotient 42 0)))))

(defspec euclidean-theorem
  10000
  (prop/for-all [n gen/int
                 d gen/int]
                (if (not (= d 0))
                  (let [r (rem n d)
                        q (corrected-euclidean-quotient n d)]
                    (and (= n (+ r (* q d)))
                         (= (quot n d) q)))
                  true ;; branch for zero divisor
                  )))

