(ns match.core-test
  (:require [clojure.test :refer :all]
            [match.core   :refer :all])
  (:use [clojure.core.match :only (match)])
  )

;;; See https://github.com/clojure/core.match/wiki/Basic-usage

(deftest basic-usage
  (testing "Matching Literals"
    (is (= 4 (let [x true
                   y true
                   z true]
               (match [x y z]
                      [_     false true] 1
                      [false true  _    ] 2
                      [_     _     false] 3
                      [_     _     true ] 4
                      :else 5))))
    (is (= 1 (let [x true]
               (match x                 ; none may have brackets
                      true  1           ; none...
                      false 2           ; none...
                      :else 3))))
    (is (= 1 (let [x true]
               (match [x]               ; or all must have brackets
                      [true]  1         ; all...
                      [false] 2         ; all...
                      :else 3))))
    )

  (testing "Binding"
    (is (= 2 (let [x 1
                   y 2]
               (match [x y]
                      [1 b] b
                      [a 2] a
                      :else nil)))))

  (testing "Sequential types"

    ;; Looks like patterns match directly against sequence types -- what
    ;; is the effect of the :seq keyword?

    (is (= :a2 (let [x [1 2 nil nil nil]]
                 (match [x]
                        [[1]              ] :a0
                        [[1 2]            ] :a1
                        [[1 2 nil nil nil]] :a2
                        :else               :a3))))

    (is (= :a2 (let [x [1 2 nil nil nil]]
                 (match [x]
                        [[1]              ]        :a0
                        [[1 2]            ]        :a1
                        [([1 2 nil nil nil] :seq)] :a2
                        :else                      :a3))))

    (is (= :a2 (let [x [1 2 nil nil nil]]
                 (match [x]             ; all must have brackets, or
                        [([1]               :seq)] :a0
                        [([1 2]             :seq)] :a1
                        [([1 2 nil nil nil] :seq)] :a2
                        :else                      :a3))))

    (is (= :a2 (let [x [1 2 nil nil nil]]
                 (match x               ; none may have brackets
                        ([1]               :seq) :a0
                        ([1 2]             :seq) :a1
                        ([1 2 nil nil nil] :seq) :a2
                        :else                      :a3)))))

  (testing "Vector types"
    (is (= :a2 (let [x [1 2 3]]
                 (match [x]             ; all must have brackets, or
                        [[_ _ 2]] :a0
                        [[1 1 3]] :a1
                        [[1 2 3]] :a2
                        :else     :a3))))
    (is (= :a2 (let [x [1 2 3]]
                 (match x               ; none may have brackets
                        [_ _ 2] :a0
                        [1 1 3] :a1
                        [1 2 3] :a2
                        :else   :a3)))))

  (testing "Rest patterns"
    (is (=  [:a1 [1 2]]
            (let [x '(1 2)]
              (match [x]
                     [([l]     :seq)]  :a0
                     [([& a]   :seq)] [:a1 [1 2]] ; first match
                     [([l & r] :seq)] [:a2 r]
                     :else             nil)
                     ))))

  (testing "Map patterns"
    (is (= :a1
           (let [x {:a 1 :b 1}]
             (match [x]
                    [{:a _ :b 2}]      :a0
                    [{:a 1 :b 1}]      :a1
                    [{:c 3 :d _ :e 4}] :a2
                    :else              nil))))
    (is (= :a1
           (let [x {:a 1 :b 1}]
             (match [x]
                    [{:a _ :b 2}]      :a0
                    [{:b 1 :a 1}]      :a1 ; maps are unordered
                    [{:c 3 :d _ :e 4}] :a2
                    :else              nil))))
    (is (= :no-match
           (let [x {:a 1 :b 1}]
             (match [x]
                    [{:c _}] :a0
                    :else    :no-match))))
    (is (= :a0
           (let [x {:a 1 :b 2}]
             (match [x]
                    [({:a _ :b 2} :only [:a :b])] :a0
                    [{:a 1 :c _}]                 :a1
                    [{:c 3 :d _ :e 4}]            :a2
                    :else                         nil))))
    (is (= :a1
           (let [x {:a 1 :b 2 :c 3}]
             (match [x]
                    [({:a _ :b 2} :only [:a :b])] :a0
                    [{:a 1 :c _}]                 :a1
                    [{:c 3 :d _ :e 4}]            :a2
                    :else                         nil)))))
  (testing "Or patterns"
    (is (= :a1
           (let [x 4   y 6   z 9]
             (match [x y z]
                    [(:or 1 2 3) _ _] :a0
                    [4 (:or 5 6 7) _] :a1
                    :else             nil)))))
  (testing "Guards"
    (is (= :a1
           (match [1 2]
                  [(_ :guard #(odd? %)) (_ :guard even?)] :a1
                  [(_ :guard #(odd? %))  _              ] :a2))))
    )
