(ns skew-zip.core-test
  (:require [clojure.test :refer :all]
            [skew-zip.core :refer :all]))

(def ^:private v1 (vec (range 65 75)))

(deftest part-test
  (testing "ordinary and corner cases of part"
    (is (= (part v1 3 7) [72 73 74]))
    (is (thrown? AssertionError (part v1 3 8)))
    (is (thrown? AssertionError (part v1 3 -1)))
    (is (= v1 (part v1 (count v1) 0)))
    (is (= [] (part v1 0 5)))
    (is (= [] (part [] 0 0)))
    (is (thrown? AssertionError (part [] 1 0)))
    (is (thrown? AssertionError (part [] -1 0)))
    ))

(deftest replace-part-test
  (testing "ordinary and corner cases of replace-part"
    (is (= ['a 'b 'c 68 69 70 71 72 73 74] (replace-part v1 ['a 'b 'c] 0)))
    (is (= ['b 'c 67 68 69 70 71 72 73 74] (replace-part v1 ['a 'b 'c] -1)))
    (is (= ['c 66 67 68 69 70 71 72 73 74] (replace-part v1 ['a 'b 'c] -2)))
    (is (= [65 66 67 68 69 70 71 72 73 74] (replace-part v1 ['a 'b 'c] -3)))
    (is (= [65 66 67 68 69 70 71 72 73 74] (replace-part v1 ['a 'b 'c] -4)))
    (is (= [65 66 67 68 69 70 71 'a 'b 'c] (replace-part v1 ['a 'b 'c] 7)))
    (is (= [65 66 67 68 69 70 71 72 'a 'b] (replace-part v1 ['a 'b 'c] 8)))
    (is (= [65 66 67 68 69 70 71 72 73 'a] (replace-part v1 ['a 'b 'c] 9)))
    (is (= [65 66 67 68 69 70 71 72 73 74] (replace-part v1 ['a 'b 'c] 10)))
    (is (= [65 66 67 68 69 70 71 72 73 74] (replace-part v1 ['a 'b 'c] 11)))
    ))

(deftest swizzle-part-test
  (testing "swizzle-part"
    (is (= (part v1 3 7) (part (swizzle-part v1 3 0) 3 7)))
    (is (thrown? AssertionError (swizzle-part v1 10 1)))
    (is (thrown? AssertionError (swizzle-part v1  9 2)))
    (is (= v1 (swizzle-part v1 0 5)))
    ))

(deftest swizzle-test
  (testing "swizzle"
    (is (reduce #(and %1 %2) true (repeatedly 100 #(= v1 (sort (swizzle v1  3))))))
    (is (reduce #(and %1 %2) true (repeatedly 100 #(= v1 (sort (swizzle v1  4))))))
    (is (reduce #(and %1 %2) true (repeatedly 100 #(= v1 (sort (swizzle v1  7))))))
    (is (reduce #(and %1 %2) true (repeatedly 100 #(= v1 (sort (swizzle v1 10))))))
    ))
