(ns nuprng.core-test
  (:use clojure.test)
  (:require [nuprng.core :refer :all :exclude 'loaded-die]))

(def ^:private loaded-die
  [[:A 37] [:B 0] [:C 17] [:D 5] [:E 12] [:F 11] [:G 0]])
(def ^:private outcomes
  #{:A :C :D :E :F})

(deftest walker-test
  (testing "Sampling a given distribution by Walker's method."
    (is (= (S loaded-die)  82))
    (is (= (N loaded-die)   7))
    (is (= (L loaded-die) 574))
    (is (= (H loaded-die)  82))
    (is (= (augmentation-factor loaded-die) 7))
    (is (= (augmented loaded-die)
           [[:A 259] [:B 0] [:C 119] [:D 35] [:E 84] [:F 77] [:G 0]]))
    (is (= (fill-shortest 82 [] (augmented loaded-die))
           {:filled     [{:home [:B 0] :other [:A 82]}]
            :remaining  [[:A (- 259 82)]
                         [:G   0]
                         [:D  35]
                         [:F  77]
                         [:E  84]
                         [:C 119]]}))
    (is (contains? outcomes
                   (first (sample-walker 1 loaded-die))))
    (is (reduce
         #(and %1 %2)
         true
         (map #(contains? outcomes %)
              (sample-walker 1000 loaded-die))))))

(deftest run-length-test
  (testing "Sampling a given distribution by a run-length array.")
  (is (reduce
       #(and %1 %2)
       true
       (map #(contains? outcomes %)
            (sample-run-length-array 1000 loaded-die)))))

(deftest linear-test
  (testing "Sampling a given distribution by linear search.")
  (is (reduce
       #(and %1 %2)
       true
       (map #(contains? outcomes %)
            (sample-linearly 1000 loaded-die)))))

(deftest logarithmic-test
  (testing "Sampling a given distribution by logarithmic search.")
  (is (reduce
       #(and %1 %2)
       true
       (map #(contains? outcomes %)
            (sample-logarithmically 1000 loaded-die)))))
