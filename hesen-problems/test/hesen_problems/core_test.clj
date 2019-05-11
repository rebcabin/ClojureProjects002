(ns hesen-problems.core-test
  (:use [clojure.test :exclude [report]])
  (:require [rx.lang.clojure.interop :as rx]
            [hesen-problems.core :refer :all])
  (:import [rx
             Observable
             Observer
             subscriptions.Subscriptions
             subjects.Subject
             subjects.PublishSubject]))

(deftest pull-model-test
  (testing "agents for a pull-model dataflow graph; agent a pulls inputs
  from agents b and c"
    (let [a (agent 100)
          b (agent 200)
          c (agent 300)]
      (send a + @b @c)
      (is (= 600 (do (await-for 5000 a) @a))))))

(deftest push-model-test
  (testing "publish-subjects for push-model dataflow graph; subjects b
  and c push inputs to subject a"
    (let [b (PublishSubject/create)
          c (PublishSubject/create)
          a (.zip b c (rx/fn [b c] (+ b c)))
          r (subscribe-collectors a)
          ]
      (.onNext b 200)
      (.onNext c 400)
      (.onCompleted b)
      (.onCompleted c)
      (-> r
          report
          :onNext)))

  (testing "basic observable functionality"
    (is (= [1 2]
           (-> (Observable/from [1 2 3]) ; an obl of length 3
               (.take 2)                 ; an obl of length 2
               subscribe-collectors      ; waits for completion
               report
               :onNext)                 ; produce results
           )))
  )

(deftest a-test
  (testing "chained function application"

    (is (= {:accumulated-results [0 5], :vertical 79}
           (chain-all
            [(fn [a b] {:vertical (+ a b) :horizontal (- a b)})]
            [42]
            {:accumulated-results [0] :vertical 37}
            )))

    (is (= {:accumulated-results [0 30 -1547], :vertical 4285}
           (chain-all
            [(fn [a b] {:vertical (+ a b) :horizontal (- a b)})
             (fn [a b] {:vertical (+ (* a a) (* b b))
                       :horizontal (- (* a a) (* b b))})]
            [42 37]
            {:accumulated-results [0] :vertical 12}
            ))
        )))
