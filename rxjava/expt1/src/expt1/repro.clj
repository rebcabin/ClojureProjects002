(ns expt1.core
  (:require [expt1.k2                :as k2     ]
            clojure.pprint
            [rx.lang.clojure.interop :as rx]
            )
  (:import [rx
            Observable
            Observer
            subscriptions.Subscriptions
            subjects.Subject
            subjects.PublishSubject]))

(defmacro pdump [x]
  `(let [x# (try ~x (catch Exception e# (str e#)))]
     (do (println "----------------")
         (clojure.pprint/pprint '~x)
         (println "~~>")
         (clojure.pprint/pprint x#)
         x#)))

(defn- or-default [val default] (if val (first val) default))

(defn subscribe-collectors [obl & optional-wait-time]
  (let [wait-time (or-default optional-wait-time 1000)
        onNextCollector      (agent    [])
        onErrorCollector     (atom    nil)
        onCompletedCollector (promise    )]
    (let [collect-next      (rx/action [item] (send    onNextCollector      (fn [state] (conj state item))))
          collect-error     (rx/action [excp] (reset!  onErrorCollector     excp))
          collect-completed (rx/action [    ] (deliver onCompletedCollector true))
          report-collectors (fn [    ]
                              (pdump
                               {:onCompleted (deref onCompletedCollector wait-time false)
                                :onNext      (do (await-for wait-time onNextCollector)
                                                 @onNextCollector)
                                :onError     @onErrorCollector
                                }))]
      [(.subscribe obl collect-next collect-error collect-completed)
       report-collectors])))

(defn find-re [re obj]
  (pdump
   (filter
    #(re-find re (str %))
    (sort (map :name (:members (r/reflect obj :ancestors true)))))))

#_(find-re #"^onNext" (PublishSubject/create))
#_(find-re #"^onNext" (PublishSubject/create (rx/fn mySubscribe [obr])))

(let [obl1 (PublishSubject/create)]

  (.onNext obl1 41)

  (let [obl2 (-> obl1
                 (.map (rx/fn [x] (+ 100 x)))
                 (.filter (rx/fn* even?))
                 (.mapMany (rx/fn [obn]
                             (Observable/create (rx/fn [obr]
                                                  (.onNext obr obn)
                                                  (.onNext obr (* obn obn))
                                                  (.onCompleted obr)))))
                 )
        [subscription reporter] (subscribe-collectors obl2)]

    (.onNext obl1 42)
    (.onNext obl1 43)
    (.onNext obl1 44)

    (.unsubscribe subscription)

    (.onNext obl1 45)
    (.onNext obl1 46)

    (.onCompleted obl1)

    (reporter)))
