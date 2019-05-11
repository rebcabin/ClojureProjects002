(ns die-harder.core-test
  (:require [clojure.test :refer :all :exclude [report]]
            [taoensso.timbre :as timbre]
            [die-harder.core :refer :all]
            ))

(timbre/refer-timbre) ; Provides useful Timbre aliases in this ns

(def mjis (make-jugs [3 5]))

;;; Another trick for bfs'ing.

(info "running tests.")

(defn node-values   [nodes]  (map    first nodes))
(defn node-children [nodes]  (mapcat next  nodes))
(defn node-bfs      [nodes]
  (if (not (empty? nodes))
    (cons (node-values nodes)
          (node-bfs (node-children nodes)))))

(defn tree-symmetric? [tree]
  (every?
   (fn [pal] (= pal (reverse pal)))
   (node-bfs (list tree))))

(deftest tree-tests
  (let [t1 [1 [2 [3] [4]] [2 [4] [3]]]]
    (is (= (node-values    (vector t1))  [1]))
    (is (= (node-children  (vector t1))  [[2 [3] [4]] [2 [4] [3]]]))
    (is (= (node-bfs       (vector t1))  [[1] [2 2] [3 4 4 3]]))
    (is (= (node-values    (list   t1))  [1]))
    (is (= (node-children  (list   t1))  [[2 [3] [4]] [2 [4] [3]]]))
    (is (= (node-bfs       (list   t1))  [[1] [2 2] [3 4 4 3]]))
    (is (tree-symmetric? t1))
    ))

(defn queue [& stuff] (into clojure.lang.PersistentQueue/EMPTY stuff))
(def  pp             clojure.pprint/pprint)
(defn- or-default
  "Fetch first optional value from function arguments preceded by &."
  [val default] (if val (first val) default))

(defn bfs-eager [tree-vector & visitor-]
  (let [visitor (or-default visitor- identity)]
   (loop [ret   []
          queue (queue tree-vector)]
     (if (seq queue)
       (let [[node & children] (peek queue)]
         (visitor node)
         (recur (conj ret node)
                (into (pop queue) children)))
       ret))))

(defn bfs-lazy [tree-vector & visitor-]
  (let [vistor (or-default visitor- identity)]
   ((fn step [queue]
      (lazy-seq
       (when (seq queue)
         (let [[node & children] (peek queue)]
           (cons node
                 (step (into (pop queue) children)))))))
    (queue tree-vector))))

(deftest trees-test
  (let [t0     [1 [2 [4] [5]] [3 [6]]]
        answer (sort (flatten t0))]
    (is (= answer  (bfs-eager t0)))
    (is (= answer  (bfs-lazy  t0)))))

;; Immutables die-hard

(deftest utilities-test
  (let []
    (is (thrown? Exception (gcd 0 0)))
    (is (thrown? Exception (gcd 0 42)))
    (is (thrown? Exception (gcd 42 0)))
    (is (thrown? ArithmeticException (divides? 0 0)))
    (is (thrown? ArithmeticException (divides? 0 42)))
    (is (divides?  2  4))
    (is (divides?  2 -4))
    (is (divides? -2 -4))
    (is (divides? -2  4))
    (is (not (divides?  2  47)))
    (is (not (divides?  2 -47)))
    (is (not (divides? -2 -47)))
    (is (not (divides? -2  47)))
    (is (= 100 (integer-power 10 2)))
    (is (= 100 (gcd (integer-power 10 2)
                    (integer-power 10 3))))
    (is (= 1 (gcd 3 5)))
    (is (= 1 (gcd 3 5 7)))
    (is (= 5 (gcd 5 15)))
    (is (= 5 (gcd 5 15 45)))))

(deftest immutable-pour-test
  (= 0 (-> mjis (pour-from 0 1))))

(defn- test-games [capacities]
  (is (= (let [target (rand-int (inc (apply + capacities)))]
           (play-game capacities target)
           (play-game capacities target try-non-trivial-moves)))))

(defn get-total [a-map]
  (->> a-map :states (map :amount) (apply +)))

(deftest immutables-test
  (testing "jugs, immutable version"
    (is (= 3 (:capacity (mjis 0))))
    (is (= 5 (:capacity (mjis 1))))
    (is (= [0 0] (map :amount mjis)))
    (is (= 3 (-> mjis
                 (fill-jug 0)
                 (get-jug  0)
                 :amount)))
    (is (= 0 (-> mjis
                 (fill-jug  0)
                 (spill-jug 0)
                 (get-jug   0)
                 :amount)))
    (is (= 4 (-> mjis
                 (fill-jug  1)
                 (pour-from 0 1)
                 (spill-jug 0)
                 (pour-from 0 1)
                 (fill-jug  1)
                 (pour-from 0 1)
                 (get-jug   1)
                 :amount
                 )))
    (is (= '(die-harder.core/fill-jug 42) (gen-fill 42)))
    (is (= {:id 1, :capacity 5, :amount 4}
           (reduce execute-move
                   mjis
                   '((die-harder.core/fill-jug  1)
                     (die-harder.core/pour-from 0 1)
                     (die-harder.core/spill-jug 0)
                     (die-harder.core/pour-from 0 1)
                     (die-harder.core/fill-jug  1)
                     (die-harder.core/pour-from 0 1)
                     (die-harder.core/get-jug  1)))))
    (is (detect-win (reduce execute-move
                            mjis
                            '((die-harder.core/fill-jug  1)
                              (die-harder.core/pour-from 0 1)
                              (die-harder.core/spill-jug 0)
                              (die-harder.core/pour-from 0 1)
                              (die-harder.core/fill-jug  1)
                              (die-harder.core/pour-from 0 1)
                              (die-harder.core/spill-jug 0)))
                    4))
    (test-games [3 5])
    (test-games [3 5 7])
    (is (empty? (play-game [3 6] 4)))
    (is (= 2 (->> (play-game [3 6 8] 2) first get-total)))
    (is (= 3 (->> (play-game [3 6] 3)   first get-total)))
))

(defn with-trivials-vesus-no-trivials
  []
  (let [args [{:states (make-jugs [3 5 7]), :trace []}
                ['(die-harder.core/fill-jug 0)]
                (inc (rand-int 15))
                #{}
                1
                10]]
    (= (take 4 (p :non-trivials  (apply try-non-trivial-moves args)))
       (take 4 (p :with-trivials (apply try-moves             args))))
    ))

#_(profile :info :Arithmetic (dotimes [n 10] (with-trivials-vesus-no-trivials)))

;;; Mutables section

(def mjs (make-jug-refs [3 5]))

(defn get-jug-amount [i]
  (-> mjs (get-jug-ref-attribute i :amount)))

(defn are-amounts [i j]
  (is (= i (get-jug-amount 0)))
  (is (= j (get-jug-amount 1))))

(deftest mutables-test
  (testing "jugs, mutable ref version"
    (is (= 3 (-> mjs (get-jug-ref 0) deref :capacity)))
    (is (= 5 (-> mjs (get-jug-ref 1) deref :capacity)))

    (is (= 3 (-> mjs (get-jug-ref-attribute 0 :capacity))))
    (is (= 5 (-> mjs (get-jug-ref-attribute 1 :capacity))))

    (is (= 3 (do (fill-jug-ref mjs 0) (get-jug-amount 0))))
    (is (= 3 (get-jug-amount 0)))

    (is (= 1 1))

    (do (fill-jug-ref  mjs 1)    (are-amounts 3 5)
        (spill-jug-ref mjs 0)    (are-amounts 0 5)
        (pour-from-ref mjs 0 1)  (are-amounts 3 2)
        (spill-jug-ref mjs 0)    (are-amounts 0 2)
        (pour-from-ref mjs 0 1)  (are-amounts 2 0)
        (fill-jug-ref  mjs 1)    (are-amounts 2 5)
        (pour-from-ref mjs 0 1)  (are-amounts 3 4)
        )))

;;; Junkyard

#_(ns my.data)

#_(defrecord Employee [name surname])

; Namescape 2 in "my/queries.clj", where a defrecord is used
#_(ns my.queries
  (:require my.data)
  (:import [my.data Employee]))

#_(do
  "Employees named Albert:"
  (filter #(= "Albert" (.name %))
    [(Employee. "Albert" "Smith")
     (Employee. "John" "Maynard")
     (Employee. "Albert" "Cheng")]))

#_(defn check-put [a-set a-member]
  (let [in? (contains? a-set a-member)]
    (if (not in?)
      [false (conj a-set a-member)]
      [true  a-set])
    ))

#_(defn- queue [& stuff] (into clojure.lang.PersistentQueue/EMPTY stuff))
#_(def ^:private pp clojure.pprint/pprint)

#_(defn random-move [jugs]
  (let [n (count jugs)
        i (rand-int n)
        j (rand-int-excluding n i)]
    (rand-nth `((fill-jug ~i)
                (spill-jug ~i)
                (pour-from ~i ~j)))))

#_(defn rand-int-excluding [n i]
  (loop [k (rand-int n)]
            (if (== k i)
              (recur (rand-int n))
              k)))
