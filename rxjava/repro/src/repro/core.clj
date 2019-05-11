(ns repro.core
  (:require [clojure.data.json       :as cdjson ]
            clojure.string
            clojure.pprint
            [rx.lang.clojure.interop :as rx]
            )
  (:refer-clojure :exclude [distinct])
  (:import [rx Observable subscriptions.Subscriptions #_Subject])
  )

(defmacro pdump [x]
  `(let [x# ~x]
     (do (println "----------------")
         (clojure.pprint/pprint '~x)
         (println "~~>")
         (clojure.pprint/pprint x#)
         (println "----------------")
         x#)))

(defn- or-default [val default] (if val (first val) default))

(defn subscribe-collectors [obl & optional-wait-time]
  (let [wait-time (or-default optional-wait-time 1000)
        onNextCollector      (agent    [])
        onErrorCollector     (atom    nil)
        onCompletedCollector (promise    )]
    (let [collect-next      (rx/action [item] (send onNextCollector
                                                    (fn [state] (conj state item))))
          collect-error     (rx/action [excp] (reset!  onErrorCollector     excp))
          collect-completed (rx/action [    ] (deliver onCompletedCollector true))
          report-collectors (fn [    ]
                              (identity ;; pdump ;; for verbose output, use pdump.
                               {:onCompleted (deref onCompletedCollector wait-time false)
                                :onNext      (do (await-for wait-time onNextCollector)
                                                 @onNextCollector)
                                :onError     @onErrorCollector
                                }))]
      (-> obl
          (.subscribe collect-next collect-error collect-completed))
      (report-collectors))))

(-> (Observable/from [1 2 3 4 5 6])
    (.filter (rx/fn [n] (== 0 (mod n 2)))) ; passes only evens along
    (.take 2)                           ; keeps only the first two
    subscribe-collectors
    pdump
    )

(-> (Observable/from [1 2 3])
    (.take 2)
    (.mapMany                           ; convert each number to a vector
     (rx/fn* #(Observable/from (map (partial + %) [42 43 44]))))
    subscribe-collectors
    pdump
    )

(def string-explode seq)

(-> (Observable/from ["one" "two" "three"])
    (.mapMany (rx/fn* #(Observable/from (string-explode %))))
    subscribe-collectors
    pdump
    )

(defn from-seq [s] (Observable/from s))

(-> (from-seq ["one" "two" "three"])
    (.mapMany (rx/fn* (comp from-seq string-explode)))
    subscribe-collectors
    pdump
    )

(defn return [item] (from-seq [item]))

(-> (from-seq ["one" "two" "three"])
    (.mapMany (rx/fn* (comp from-seq string-explode)))
    (.mapMany (rx/fn* return))
    subscribe-collectors
    pdump
    )

(defn synchronous-observable [the-seq]
  "A custom Observable whose 'subscribe' method does not return until
   the observable completes, that is, a 'blocking' observable.

  returns Observable<String>"
  (Observable/create
   (rx/fn [observer]
     (doseq [x the-seq] (-> observer (.onNext x)))
     (-> observer .onCompleted)
     (Subscriptions/empty))))

(defn flip [f2] (fn [x y] (f2 y x)))

;;; Test the synchronous observable:

(-> (synchronous-observable (range 50)) ; produces 0, 1, 2, ..., 50
    (.map    (rx/fn* #(str "SynchronousValue_" %))) ; produces strings
    (.map    (rx/fn* (partial (flip clojure.string/split) #"_"))) ; splits at "_"
    (.map    (rx/fn [[a b]] [a (Integer/parseInt b)])) ; converts seconds
    (.filter (rx/fn [[a b]] (= 0 (mod b 7)))) ; keeps only multiples of 7
    subscribe-collectors
    pdump
    )

(defn asynchronous-observable [the-seq]
  "A custom Observable whose 'subscribe' method returns immediately and whose
   other actions -- namely, onNext, onCompleted, onError -- occur on another
   thread.

  returns Observable<String>"
  (Observable/create
   (rx/fn [observer]
     (let [f (future (doseq [x the-seq] (-> observer (.onNext x)))
                     ;; After sending all values, complete the sequence:
                     (-> observer .onCompleted))]
       ;; Return a subscription that cancels the future:
       (Subscriptions/create (rx/action [] (future-cancel f)))))))

(-> (asynchronous-observable (range 50))
    (.map    (rx/fn* #(str "AsynchronousValue_" %)))
    (.map    (rx/fn* (partial (flip clojure.string/split) #"_")))
    (.map    (rx/fn [[a b]] [a (Integer/parseInt b)]))
    (.filter (rx/fn [[a b]] (= 0 (mod b 7))))
    subscribe-collectors
    pdump
    )

(defn -main [])
