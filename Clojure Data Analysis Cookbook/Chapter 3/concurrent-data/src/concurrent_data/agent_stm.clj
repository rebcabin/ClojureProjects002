
;; NB: Do this one for 03.04.
(ns concurrent-data.agent-stm
  (:use [concurrent-data.agents :only (data-file
                                        sum-items
                                        force-val
                                        div-vec
                                        accum-sums)]
        [concurrent-data.utils])
  (import [java.lang Thread]))

#_
(import '[java.lang Thread])

(defn get-chunk
  ([data-ref]
   (dosync
     (when-let [[s & ss] (seq @data-ref)]
       (ref-set data-ref ss)
       s))))

(defn dump-items
  ([items]
   (let [names (doall (map :NAME items))
         output (str ">>> " (count items) " => "
                     (interpose \space names)
                     \newline)]
     (print output)
     (.flush *out*))))

(defn update-totals
  ([totals fields coll-ref counter-ref]
   (if-let [items (get-chunk coll-ref)]
     (do
       (send *agent* update-totals fields coll-ref counter-ref)
       (sum-items totals fields items))
     (do 
       (dosync (commute counter-ref inc))
       totals))))

(defn block-to-done
  [counter agent-count]
  (loop []
    (when-not (= agent-count @counter)
      (Thread/sleep 500)
      (recur))))

(defn get-results
  ([agents fields]
   (->> agents
     (map force-val)
     (reduce accum-sums (mapv (constantly 0) fields))
     (div-vec))))

(defn main
  ([data-file] (main data-file [:P035001 :HU100] 5 5))
  ([data-file fields agent-count chunk-count]
   (let [mzero (mapv (constantly 0) fields)
         agents (map agent (take agent-count (repeat mzero)))
         data (with-header (lazy-read-csv data-file))
         data-ref (ref (doall (partition-all chunk-count data)))
         finished (ref 0)]
     (dorun
       (map #(send % update-totals fields data-ref finished) agents))
     (block-to-done finished (count agents))
     (get-results agents fields))))

