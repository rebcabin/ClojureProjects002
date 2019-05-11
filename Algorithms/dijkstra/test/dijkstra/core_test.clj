(ns dijkstra.core-test
  (:require [clojure.test    :refer :all]
            [dijkstra.core   :refer :all]
            ;; [midje.sweet     :refer :all]
            [criterium.core  :as crit]
            [clojure.java.io :as io])
  (:import  [dijkstra.core DirectedGraph UndirectedGraph]))

#_(crit/bench (Thread/sleep 1000))

(deftest a-test
  (testing "test itself."
    (is (= 1 1))))

(def ^:private a-graph
  (DirectedGraph.
   {:s {:v 1, :w 4}
    :v {:w 2, :t 6}
    :w {:t 3 }
    :t {}}))

(def ^:private b-graph
  (DirectedGraph.
   {1 {2 1, 3 4}
    2 {3 2, 4 6}
    3 {4 3}
    4 {}}))

(deftest paths-test
  (is (=  (shortest-paths-linear a-graph :s)
          [[:s 0 [:s]]
           [:v 1 [:s :v]]
           [:w 3 [:s :v :w]]
           [:t 6 [:s :v :w :t]]]))

  (is (=  (shortest-paths-log-linear a-graph :s)
          [[:s 0 [:s]]
           [:v 1 [:s :v]]
           [:w 3 [:s :v :w]]
           [:t 6 [:s :v :w :t]]]))

  (is (= (shortest-path a-graph :s :t) [:s :v :w :t]))

  (is (= (shortest-path b-graph 1 4) [1 2 3 4]))
)

(facts "about shortest paths:"
       (fact "Linear-time shortest-path search starting at :s produces shortest paths to all other nodes."
             (shortest-paths-linear a-graph :s) =>
             [[:s 0 [:s]]
              [:v 1 [:s :v]]
              [:w 3 [:s :v :w]]
              [:t 6 [:s :v :w :t]]])
       (fact "Log-linear-time shortest-path search starting at :s produces the same results as linear-time shortest paths."
             (shortest-paths-log-linear a-graph :s) =>
             (shortest-paths-linear     a-graph :s))
       )

;;; Find BFG's here: http://snap.stanford.edu/data/

(def ^:private c-graph
  { :1 [:2 :3],
    :2 [:4],
    :3 [:4],
    :4 []
   })

(defn- kw-to-int [kw] (->> kw str rest (apply str) read-string))
(defn- square [x] (* x x))

(deftest traverse-test
  (is (= (seq-graph-dfs c-graph :1) [:1 :3 :4 :2]))
  (is (= (seq-graph-bfs c-graph :1) [:1 :2 :3 :4]))
  (is (= (seq-graph-dfs c-graph :1  (comp square kw-to-int)) [1 9 16 4]))
  (is (= (seq-graph-bfs c-graph :1  (comp square kw-to-int)) [1 4 9 16]))
  )

(deftest corner-cases-test
  (-> (= [] (seq-graph-dfs {} :1)) is)
  (-> (= [] (seq-graph-bfs {} :1)) is)

  (-> (= [:1] (seq-graph-dfs {:1 []} :1)) is)
  (-> (= [:1] (seq-graph-bfs {:1 []} :1)) is)

  (-> (= [] (seq-graph-dfs {:2 []} :1)) is)
  (-> (= [] (seq-graph-bfs {:2 []} :1)) is)

  (-> (= [:1] (seq-graph-dfs {:1 [:1]} :1)) is)
  (-> (= [:1] (seq-graph-bfs {:1 [:1]} :1)) is)
  )

(defn- with-resource [resource]
  (io/resource resource))

(defn- with-bfg-1 [op]
  (with-open [rdr (io/reader (io/resource "wiki-Vote.txt"))]
    (op (line-seq rdr))))

(def ^:private bfg-1-pairs
  (with-open [rdr (io/reader (io/resource "wiki-Vote.txt"))]
    (let [edges
          (doall (for [line (line-seq rdr)
                       :when (not (re-find #"^#" line))]
                   (map read-string (re-seq #"\d+" line))))]
      (map vec edges))))

(defn graph-from-pairs [pairs]
  (reduce
   (fn [hmap pair]
     (let [[v1 v2] pair
           tail (hmap v1)]
       (if tail
         (let [head (or (tail v2) 0)]
           (into hmap [[v1 (into tail [[v2 (inc head)]])]]))
         (into hmap [[v1 {v2 1}]]))
       ))
   {}
   pairs))

(deftest bfg-test
  (is (== 103693 (with-bfg-1 count)))
  (is (= [30 5534 2658 2014])) (shortest-path
      (DirectedGraph. (graph-from-pairs bfg-1-pairs))
      30 2014))

;;; Results of a criterium run:

;;; dijkstra.core-test> (crit/bench (doall (shortest-paths-log-linear g 30)))
;;; WARNING: JVM argument TieredStopAtLevel=1 is active, and may lead to unexpected results as JIT C2 compiler may not be active. See http://www.slideshare.net/CharlesNutter/javaone-2012-jvm-jit-for-dummies.
;;; Evaluation count : 240 in 60 samples of 4 calls.
;;;              Execution time mean : 267.182736 ms
;;;     Execution time std-deviation : 1.976548 ms
;;;    Execution time lower quantile : 265.749136 ms ( 2.5%)
;;;    Execution time upper quantile : 268.267255 ms (97.5%)
;;;                    Overhead used : 114.433129 ns
;;;
;;; Found 1 outliers in 60 samples (1.6667 %)
;;; 	low-severe	 1 (1.6667 %)
;;;  Variance from outliers : 1.6389 % Variance is slightly inflated by outliers

;;; dijkstra.core-test> (crit/bench (doall (shortest-paths-linear g 30)))
;;; WARNING: JVM argument TieredStopAtLevel=1 is active, and may lead to unexpected results as JIT C2 compiler may not be active. See http://www.slideshare.net/CharlesNutter/javaone-2012-jvm-jit-for-dummies.
;;; Evaluation count : 60 in 60 samples of 1 calls.
;;;              Execution time mean : 1.644806 sec
;;;     Execution time std-deviation : 22.473758 ms
;;;    Execution time lower quantile : 1.629214 sec ( 2.5%)
;;;    Execution time upper quantile : 1.716874 sec (97.5%)
;;;                    Overhead used : 114.433129 ns
;;;
;;; Found 10 outliers in 60 samples (16.6667 %)
;;; 	low-severe	 3 (5.0000 %)
;;; 	low-mild	 7 (11.6667 %)
;;;  Variance from outliers : 1.6389 % Variance is slightly inflated by outliers
