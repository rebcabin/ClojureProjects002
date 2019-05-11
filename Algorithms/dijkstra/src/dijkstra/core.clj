(ns dijkstra.core
  (:require [clojure.data.priority-map :as pmap]))

;;; confer http://hueypetersen.com/posts/2013/07/09/dijkstra-as-a-sequence/?utm_source=dlvr.it&utm_medium=twitter

(defprotocol IGraph
  (vertices  [ g         ])
  (neighbors [ g v       ])
  (add       [ g v1 v2 c ])
  (cost      [ g v1 v2   ]))

(defrecord UndirectedGraph [vs]
  IGraph
  (vertices  [_  ]      (keys vs))
  (neighbors [_ v]      (keys (vs v {})))
  (cost      [_ v1 v2]  (get-in vs [v1 v2]))
  (add       [_ v1 v2 c]
    (-> vs
        (assoc-in [v1 v2] c)
        (assoc-in [v2 v1] c)
        (UndirectedGraph.))))

(defrecord DirectedGraph [vs]
  IGraph
  (vertices  [_  ]      (keys vs))
  (neighbors [_ v]      (keys (vs v {})))
  (cost      [_ v1 v2]  (get-in vs [v1 v2]))
  (add       [_ v1 v2 c]
    (-> vs
        (assoc-in [v1 v2] c)
        (DirectedGraph.))))

(defn shortest-paths-linear [g start]
  ;; Apply a function named "explore" to some initial arguments.
  ;; Explore takes two structures, "explored" and "frontier," and
  ;; produces a lazy sequence of triples of destination vertex, total
  ;; cost to the destination, and path (sequence of vertices).
  ;;
  ;; "Explored" is a map of vertices and the sequence of vertices that
  ;; led up to them (? with costs ?)
  ;;
  ;; "Frontier" is a map of successors to cost-predecessor pairs. For
  ;; instance, a frontier like this
  ;;
  ;; { :v [1 :s] :w [4 :s] }
  ;;
  ;; reads as "to :v from :s by an edge of cost 1, and to :w from :s
  ;; by an edge of cost 4."
  ;;
  ;; "Explored" begins as the empty map, and "frontier" begins as the
  ;; map from "start" with a degenerate pair of cost zero (0) and no
  ;; predecessor.
  ((fn explore [explored frontier]
     (lazy-seq
      (if (empty? frontier)
        nil
        (let [ ;; "frontier" has the form
              ;; '{' <item>:( <succ> '[' <cost> <predec> ']' ) ... '}'
              ;; Min-key finds the item in the frontier with the
              ;; minimum cost, and the next line destructures the item
              ;; into a successor v, the cost-so-far, and v's
              ;; min-cost predecessor.
              [v [total-cost predecessor]]
              (apply min-key (comp first second) frontier)
              ;; "Explored" is a map of vertices and the the sequence
              ;; of vertices that led up to them. Using "explored" as
              ;; a function, look up the possibly-empty path already
              ;; computed TO the predecessor found above and add the
              ;; current successor vertex, v, to that path.
              path         (conj (explored predecessor []) v)
              ;; Update "explored" by adding the successor v and the
              ;; path that led up to it.
              explored     (assoc explored v path)
              ;; Now, move forward to another node. Treat v as a
              ;; provisional predecessor (it has been the successor up
              ;; to this point). Using "explore" as a predicate this
              ;; time, remove from the "neighbors" of v -- that is,
              ;; its successors -- every node that's already been
              ;; visited. That gives us the sequence of unexplored
              ;; nodes with predecessor v.
              unexplored   (remove explored (neighbors g v))
              ;; Make a fresh provisional frontier from a
              ;; comprehension; for each unexplored, create a
              ;; key-value pair and 'push' it into a fresh map; the
              ;; key is the unexplored node and the value is a pair of
              ;; cost and predecessor v. Every item in this
              ;; provisional frontier has predecessor v, that is, v in
              ;; the predecessor position of each cost-node pair.
              new-frontier (into {} (for [n unexplored]
                                      [n [(+ total-cost (cost g v n)) v]]))
              ;; The old frontier includes v itself in successor
              ;; position; that's how we found v, by picking it out of
              ;; the old frontier. Take v out of the old frontier and
              ;; merge the resulting structure with the new
              ;; provisional frontier, keeping the minimum-cost
              ;; alternative in the case of ties. This merging insures
              ;; that every node in the graph is eventually explored
              ;; because we don't lose the unexplored nodes in the old
              ;; frontier.
              frontier     (merge-with (partial min-key first)
                                       (dissoc frontier v)
                                       new-frontier)]
          (cons [v total-cost path]
                (explore explored frontier))))))
   {}                                   ; initial val of explored
   { start [0]}))                      ; initial val of frontier

(defn shortest-paths-log-linear [g start]
  ((fn explore [explored frontier]
     (lazy-seq
      (when-let [[v [total-cost predecessor]] (peek frontier)]
        (let [path         (conj (explored predecessor []) v)
              explored     (assoc explored v path)
              unexplored   (remove explored (neighbors g v))
              new-frontier (into {} (for [n unexplored]
                                      [n [(+ total-cost (cost g v n)) v]]))
              frontier     (merge-with (partial min-key first)
                                       (pop frontier)
                                       new-frontier)]
          (cons [v total-cost path]
                (explore explored frontier))))))
   {}                                   ; first val of explored
   (pmap/priority-map start [0]) ))

(defn shortest-path [g start dest]
  (let [not-destination? (fn [[vertex _]] (not= vertex dest))]
    (-> (shortest-paths-log-linear g start)
        (->> (drop-while not-destination?))
        first
        (nth 2))))

;;; http://hueypetersen.com/posts/2013/06/25/graph-traversal-with-clojure/

(defn- seq-graph-traverse [g s strategy visitor]
  (if (not (g s)) '()
      ((fn rec-bfs [explored frontier]
         (lazy-seq
          (if (empty? frontier)
            nil
            (let [v         (peek frontier)
                  neighbors (g v)]
              (cons (visitor v)
                    (rec-bfs
                     (into explored neighbors)
                     (into (pop frontier) (remove explored neighbors))))))))
       #{s} strategy)))

(defn seq-graph-bfs [g s & optional-visitor]
  (let [visitor (if optional-visitor (first optional-visitor) identity)]
    (seq-graph-traverse
     g
     s
     (conj (clojure.lang.PersistentQueue/EMPTY) s)
     visitor)))

(defn seq-graph-dfs [g s & optional-visitor]
  (let [visitor (if optional-visitor (first optional-visitor) identity)]
    (seq-graph-traverse
     g
     s
     (conj [] s)
     visitor)))
