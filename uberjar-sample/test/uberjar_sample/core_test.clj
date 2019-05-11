(ns uberjar-sample.core-test
  (:use clojure.test
        uberjar-sample.core))

(defn a-fixture [test-fn]
  (let [v 1] (assert (= v 1)))
  (test-fn)
  (let [v 2] (assert (= v 2))))

(use-fixtures :each a-fixture)

(deftest a-test
  (testing "I'm FIXED."
    (is (= 1 1))))

(deftest b-test
  (testing "I'm ok too"
    (is (= 2 2))))

