(ns hesen-problems.core
  (:require [rx.lang.clojure.interop :as rx]
            [clojure.pprint])
  (:import  [rx
             Observable
             Observer
             subscriptions.Subscriptions
             subjects.Subject
             subjects.PublishSubject])
  (:gen-class))

(defmacro pdump
  "Monitoring and debugging macro with semantics of 'identity'."
  [x]
  `(let [x# (try ~x (catch Exception e# (str e#)))]
     (do (println "----------------")
         (clojure.pprint/pprint '~x)
         (println "~~>")
         (clojure.pprint/pprint x#)
         x#)))

(defn- or-default
  "Fetch first optional value from function arguments preceded by &."
  [val default] (if val (first val) default))

(defn report
  "Given subscribed collectors, produce results obtained so far."
  [subscribed-collectors]
  (pdump ((:reporter subscribed-collectors))))

(defn subscribe-collectors
  "Subscribe asynchronous collectors to any observable; produce a map
   containing a :subscription object for .unsubscribing and a :reporters
   function for retrieving values from the collectors. Default wait-time
   is 1 second until timeout."
  [obl & optional-wait-time]
  (let [wait-time (or-default optional-wait-time 1000)
        onNextCollector      (agent [])  ; seq of all values sent
        onErrorCollector     (atom  nil) ; only need one value
        onCompletedCollector (promise)]  ; can wait on another thread
    (let [collect-next
          (rx/action [item] (send onNextCollector
                                  (fn [state] (conj state item))))
          collect-error
          (rx/action [excp] (reset! onErrorCollector excp))

          collect-completed
          (rx/action [    ] (deliver onCompletedCollector true))

          report-collectors
          (fn []
            (identity ;; pdump ;; for verbose output, use pdump.
             { ;; Wait on onCompleted BEFORE waiting on onNext because
              ;; the onNext-agent's await-for can return too quickly,
              ;; before messages from other threads are drained from
              ;; its queue.  This code depends on order-of-evaluation
              ;; assumptions in the map.

              :onCompleted (deref onCompletedCollector wait-time false)

              :onNext      (do (await-for wait-time onNextCollector)
                               ;; Produce results even if timed out
                               @onNextCollector)

              :onError     @onErrorCollector
              }))]
      {:subscription
       (.subscribe obl collect-next collect-error collect-completed)

       :reporter
       report-collectors})))

(defn rev-apply
  [xs f]
  (let [{horiz :horizontal vert :vertical} (f (:vertical xs))]
    {:vertical vert,
     :accumulated-results (conj (:accumulated-results xs) horiz)}))

(defn chain-all
  [fn-seq horizontal-arg-seq initial-vertical]
  (reduce
   rev-apply
   initial-vertical
   (map partial fn-seq horizontal-arg-seq)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
