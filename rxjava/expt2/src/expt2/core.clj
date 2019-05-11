(ns expt2.core
  (:require clojure.pprint)
  (:import [rx Observable subscriptions.Subscriptions]))

(defmacro pdump [x]
  `(let [x# ~x]
     (do (println "----------------")
         (clojure.pprint/pprint '~x)
         (println "~~>")
         (clojure.pprint/pprint x#)
         (println "----------------")
         x#)))

(defn- subscribe-collectors [obl]
  (let [;; Keep a sequence of all values sent:
        onNextCollector      (agent    [])
        ;; Only need one value if the observable errors out:
        onErrorCollector     (atom    nil)
        ;; Use a promise for 'completed' so we can wait for it on
        ;; another thread:
        onCompletedCollector (promise    )]
    (letfn [;; When observable sends a value, relay it to our agent"
            (collect-next      [item] (send onNextCollector (fn [state] (conj state item))))
            ;; If observable errors out, just set our exception;
            (collect-error     [excp] (reset!  onErrorCollector     excp))
            ;; When observable completes, deliver on the promise:
            (collect-completed [    ] (deliver onCompletedCollector true))
            ;; In all cases, report out the back end with this:
            (report-collectors [    ]
              (pdump
               {;; Wait at most 1 second for the promise to complete;
                ;; if it does not complete, then produce 'false'. We
                ;; must wait on the onCompleted BEFORE waiting on the
                ;; onNext because the agent's await-for in onNext only
                ;; waits for messages sent to the agent from THIS
                ;; thread, and our asynchronous observable may be
                ;; sending messages to the agent from another thread,
                ;; say, a future's thread. The agent's await-for will
                ;; return too quickly, allowing this onCompleted await
                ;; to return, losing some messages. This code depends
                ;; on order-of-evaluation assumptions in the map.
                :onCompleted (deref onCompletedCollector 1000 false)
                ;; Wait for everything that has been sent to the agent
                ;; to drain (presumably internal message queues):
                :onNext      (do (await-for 1000 onNextCollector)
                                 ;; Then produce the results:
                                 @onNextCollector)
                ;; If we ever saw an error, here it is:
                :onError     @onErrorCollector
                }))]
      ;; Recognize that the observable 'obl' may run on another thread:
      (-> obl
          (.subscribe collect-next collect-error collect-completed))
      ;; Therefore, produce results that wait, with timeouts, on both
      ;; the completion event and on the draining of the (presumed)
      ;; message queue to the agent.
      (report-collectors))))

(defn- customObservableBlocking []
  (Observable/create
    (fn [observer]                       ; This is the 'subscribe' method.
      ;; Send 25 strings to the observer's onNext:
      (doseq [x (range 25)]
        (-> observer (.onNext (str "SynchedValue_" x))))
      ; After sending all values, complete the sequence:
      (-> observer .onCompleted)
      ; return a NoOpSubsription since this blocks and thus
      ; can't be unsubscribed (disposed):
      (Subscriptions/empty))))

;;; The value of the following is the list of all 25 events:
(-> (customObservableBlocking)
    (subscribe-collectors))

(defn- customObservableNonBlocking []
  (Observable/create
    (fn [observer]                       ; This is the 'subscribe' method
      (let [f (future
                ;; On another thread, send 25 strings:
                (doseq [x (range 25)]
                  (-> observer (.onNext (str "AsynchValue_" x))))
                ; After sending all values, complete the sequence:
                (-> observer .onCompleted))]
        ; Return a disposable (unsubscribe) that cancels the future:
        (Subscriptions/create #(future-cancel f))))))

;;; For unknown reasons, the following does not produce all 25 events:
(-> (customObservableNonBlocking)
    (subscribe-collectors))

