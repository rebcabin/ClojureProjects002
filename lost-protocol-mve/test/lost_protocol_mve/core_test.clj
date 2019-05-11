(ns lost-protocol-mve.core-test
  (:require [clojure.test :refer :all]
            [lost-protocol-mve.core :refer :all]))

(deftest a-test
  (testing "just the facts"
    (is (.(lost_protocol_mve.core.sayer. 42) say-it))))

(deftest b-test
  (testing "just the falsehoods"
    (is (not (.(lost_protocol_mve.core.sayer. 43) say-it)))))
