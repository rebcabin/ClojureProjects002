Dear friends -- I apologize in advance for the length of this note. I spent considerable time making it shorter, and this was as small as I could get it.

I have a mystery and would be grateful for your help. This mystery comes from the behavior of an rxjava `observer` I wrote in Clojure over a couple of straightforward `observable`s cribbed from online samples.

One observable synchronously sends messages to the `onNext` handlers of its observers, and my supposedly principled observer behaves as expected.

The other observable asynchronously does the same, on another thread, via a Clojure `future`. The exact same observer does not capture all events posted to its `onNext`; it just seems to lose a random number of messages at the tail.

There is an intentional race in the following between the expiration of a wait for the `promise`d `onCompleted` and the expiration of a wait for all events sent to an `agent` collector. If the `promise` wins, I expect to see `false` for `onCompleted` and a possibly short queue in the `agent`. If the `agent` wins, I expect to see `true` for `onCompleted` and all messages from the `agent`'s queue. The one result I DO NOT expect is `true` for `onCompleted` AND a short queue from the `agent`. But, Murphy doesn't sleep, and that's exactly what I see. I don't know whether garbage-collection is at fault, or some internal queuing to Clojure's STM, or my stupidity, or something else altogether.

I present the source in the order of its self-contained form, here, so that it can be run directly via `lein repl`.  There are three cermonials to get out of the way: first, the leiningen project file, `project.clj`, which declares dependency on the `0.9.0` version of Netflix's rxjava:

    (defproject expt2 "0.1.0-SNAPSHOT"
      :description "FIXME: write description"
      :url "http://example.com/FIXME"
      :license {:name "Eclipse Public License"
                :url "http://www.eclipse.org/legal/epl-v10.html"}
      :dependencies [[org.clojure/clojure               "1.5.1"]
                     [com.netflix.rxjava/rxjava-clojure "0.9.0"]]
      :main expt2.core)

Now, the namespace and a Clojure requirement and the Java imports:

    (ns expt2.core
      (:require clojure.pprint)
      (:refer-clojure :exclude [distinct])
      (:import [rx Observable subscriptions.Subscriptions]))

Finally, a macro for output to the console:

    (defmacro pdump [x]
      `(let [x# ~x]
         (do (println "----------------")
             (clojure.pprint/pprint '~x)
             (println "~~>")
             (clojure.pprint/pprint x#)
             (println "----------------")
             x#)))

Finally, to my observer. I use an `agent` to collect the messages sent by any observable's `onNext`. I use an `atom` to collect a potential `onError`. I use a `promise` for the `onCompleted` so that consumers external to the observer can wait on it.

    (defn- subscribe-collectors [obl]
      (let [;; Keep a sequence of all values sent:
            onNextCollector      (agent [])
            ;; Only need one value if the observable errors out:
            onErrorCollector     (atom nil)
            ;; Use a promise for 'completed' so we can wait for it on
            ;; another thread:
            onCompletedCollector (promise)]
        (letfn [;; When observable sends a value, relay it to our agent"
                (collect-next      [item] (send onNextCollector (fn [state] (conj state item))))
                ;; If observable errors out, just set our exception;
                (collect-error     [excp] (reset!  onErrorCollector     excp))
                ;; When observable completes, deliver on the promise:
                (collect-completed [    ] (deliver onCompletedCollector true))
                ;; In all cases, report out the back end with this:
                (report-collectors [    ]
                  (pdump
                   ;; Wait for everything that has been sent to the agent
                   ;; to drain (presumably internal message queues):
                   {:onNext      (do (await-for 1000 onNextCollector)
                                     ;; Then produce the results:
                                     @onNextCollector)
                    ;; If we ever saw an error, here it is:
                    :onError     @onErrorCollector
                    ;; Wait at most 1 second for the promise to complete;
                    ;; if it does not complete, then produce 'false'.
                    ;; I expect if this times out before the agent
                    ;; times out to see an 'onCompleted' of 'false'.
                    :onCompleted (deref onCompletedCollector 1000 false)
                    }))]
          ;; Recognize that the observable 'obl' may run on another thread:
          (-> obl
              (.subscribe collect-next collect-error collect-completed))
          ;; Therefore, produce results that wait, with timeouts, on both
          ;; the completion event and on the draining of the (presumed)
          ;; message queue to the agent.
          (report-collectors))))

Now, here is a synchronous observable. It pumps 25 messages down the `onNext` throats of its observers, then calls their `onCompleted`s.  

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

We subscribe our observer to this observable:

    ;;; The value of the following is the list of all 25 events:
    (-> (customObservableBlocking)
        (subscribe-collectors))

It works as expected, and we see the following results on the console

    {:onNext (do (await-for 1000 onNextCollector) @onNextCollector),
     :onError @onErrorCollector,
     :onCompleted (deref onCompletedCollector 1000 false)}
    ~~>
    {:onNext
     ["SynchedValue_0"
      "SynchedValue_1"
      "SynchedValue_2"
      "SynchedValue_3"
      "SynchedValue_4"
      "SynchedValue_5"
      "SynchedValue_6"
      "SynchedValue_7"
      "SynchedValue_8"
      "SynchedValue_9"
      "SynchedValue_10"
      "SynchedValue_11"
      "SynchedValue_12"
      "SynchedValue_13"
      "SynchedValue_14"
      "SynchedValue_15"
      "SynchedValue_16"
      "SynchedValue_17"
      "SynchedValue_18"
      "SynchedValue_19"
      "SynchedValue_20"
      "SynchedValue_21"
      "SynchedValue_22"
      "SynchedValue_23"
      "SynchedValue_24"],
     :onError nil,
     :onCompleted true}
    ----------------

Here is an asynchronous observable that does exactly the same thing, only on a `future`'s thread:

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

But, surprise, here is what we see on the console: `true` for `onCompleted`, implying that the `promise` DID NOT TIME-OUT; but only some of the asynch messages.  The actual number of messages we see varies from run to run, implying that there is some concurrency phenomenon at play. Clues appreciated.

    ----------------
    {:onNext (do (await-for 1000 onNextCollector) @onNextCollector),
     :onError @onErrorCollector,
     :onCompleted (deref onCompletedCollector 1000 false)}
    ~~>
    {:onNext
     ["AsynchValue_0"
      "AsynchValue_1"
      "AsynchValue_2"
      "AsynchValue_3"
      "AsynchValue_4"
      "AsynchValue_5"
      "AsynchValue_6"],
     :onError nil,
     :onCompleted true}
    ----------------



