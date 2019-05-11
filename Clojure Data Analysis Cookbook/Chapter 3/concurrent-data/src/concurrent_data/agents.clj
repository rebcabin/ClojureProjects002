
;; NB: Do this one for 03.02.
(ns concurrent-data.agents
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv])
  (:use [concurrent-data.utils]))

#_
(do
  (require '[clojure.java.io :as io]
           '[clojure.data.csv :as csv]))

;; From http://census.ire.org/data/bulkdata.html
;; State: Virginia (51)
;; Summary Level: Place (160)
;; Table: P35. FAMILIES
(def data-file "data/all_160_in_51.P35.csv")

(defn ->int
  ([i] (Integer. i)))

(defn sum-item
  "This sums the number of families/housing unit in the input collection."
  ([fields] (partial sum-item fields))
  ([fields accum item]
   (mapv + accum (map ->int (map item fields)))))

(defn sum-items
  "This folds sum-item over a collection."
  ([accum fields coll]
   (reduce (sum-item fields) accum coll)))

(defn accum-sums
  "This accumulates the sums."
  ([a b]
   (mapv + a b)))

(defn force-val
  ([a]
   (await a)
   @a))

(defn div-vec
  ([[a b]] (float (/ a b))))

(defn main
  ([data-file] (main data-file [:P035001 :HU100] 5 5))
  ([data-file fields agent-count chunk-count]
   (let [mzero (mapv (constantly 0) fields)
         agents (map agent (take agent-count (repeat mzero)))]
     (dorun
       (->>
         (lazy-read-csv data-file)
         with-header
         (partition-all chunk-count)
         (map #(send %1 sum-items fields %2) (cycle agents))))
     (->>
       agents
       (map force-val)
       (reduce accum-sums mzero)
       div-vec))))

