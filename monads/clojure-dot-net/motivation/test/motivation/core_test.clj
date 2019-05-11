(ns motivation.core-test
  (:require [clojure.test :refer :all]
            [motivation.core :refer :all]))

(deftest a-test
  (testing "half-double"
    (is (=
         [5 20]
         (half-double 10)))))
