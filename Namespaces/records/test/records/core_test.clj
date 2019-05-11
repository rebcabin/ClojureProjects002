(ns records.core-test
  (:require [clojure.test :refer :all]
            [records.core :refer :all])
  (:import  [records.core my-vector]))

;;; http://stackoverflow.com/questions/18561059/trouble-referring-to-defrecord-symbols-in-clojure-unit-test

(deftest a-test
  (testing "adding to a my-vector"
    (is (= (hello) "hello"))
    (is (= (#'records.core/secret) "secret"))
    (is (= (@#'records.core/secret) "secret"))
    (is (= [42] (add (my-vector. []) 42)))))

