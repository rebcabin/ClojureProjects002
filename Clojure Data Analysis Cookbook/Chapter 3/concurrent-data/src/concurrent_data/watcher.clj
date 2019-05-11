
(ns concurrent-data.watcher
  (:use concurrent-data.utils
        [concurrent-data.validator
         :only (int-rows try-read-string coerce-row)])
  (:import [java.lang Thread]))

#_
(do
  (require '[clojure.java.io :as io]
           '[clojure.data.csv :as csv])
  (import '[java.lang Thread])
  )

(defn read-row
  [rows caster sink done]
  (if-let [[item & items] (seq rows)]
    (do
      (send caster coerce-row item sink)
      (send *agent* read-row caster sink done)
      items)
    (do
      (dosync (commute done (constantly true)))
      '())))

(defn watch-caster
  [counter watch-key watch-agent old-state new-state]
  (when-not (nil? new-state)
    (dosync (commute counter inc))))

(defn wait-for-it
  [sleep-for ref-var]
  (loop []
    (when-not @ref-var
      (Thread/sleep sleep-for)
      (recur))))

(defn watch-processing
  [input-file]
  (let [reader (agent (seque
                        (with-header
                          (lazy-read-csv
                            input-file))))
        caster (agent nil)
        sink (agent [])
        counter (ref 0)
        done (ref false)]
    (add-watch caster :counter
               (partial watch-caster counter))
    (send reader read-row caster sink done)
    (wait-for-it 250 done)
    {:results @sink
     :count-watcher @counter}))

