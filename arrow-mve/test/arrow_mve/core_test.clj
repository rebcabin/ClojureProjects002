(ns arrow-mve.core-test
  (:require [clojure.test :refer :all]
            [arrow-mve.core :refer :all]))

(deftest test-one-a
  (is (= 1 (extract-one {:a 1, :b 2}))))

(deftest test-one-b
  (is (= 1 (-> {:a 1, :b 2}
               extract-one))))

(deftest test-two-a
  (is (= 1 (#'arrow-mve.core/extract-two
            {:a 1, :b 2}))))

(deftest test-two-b
  (is (= {:x 3.14, :y 2.72}
         (#'arrow-mve.core/extract-two
          {:a {:x 3.14, :y 2.72}, :b 2}))))
