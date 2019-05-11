(ns side-files.core-test
  (:require [clojure.test :refer :all]
            [side-files.core :refer :all]))

(deftest a-test
  (testing "I work great for functions defined in core.clj."
    (is (= 42 (f 41)))
    (is (= 1764 (g 42)))
    ))
