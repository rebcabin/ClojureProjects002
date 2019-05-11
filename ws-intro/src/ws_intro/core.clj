(ns ws-intro.core
  (:require [clojure.data.json        :as json]
            [clojure.string           :as s   ]
            [clojure.pprint           :as pp  ]
            [ws-intro.ring-buffer     :as rb  ]
            )
  (:import  [org.webbitserver         WebServer WebServers WebSocketHandler]
            [org.webbitserver.handler StaticFileHandler                    ]
            [rx                       Observable Observer Subscription     ]
            [rx.subscriptions         Subscriptions                        ]
            [rx.util                  AtomicObservableSubscription         ]
            ))

;;; ================================================================

;;; This is a reminder that we can put type annotations on wrapper
;;; functions to get a sliver of performance.

(defn ^Observable observable
  "Create an observable from the given handler. When subscribed to, (handler observer)
  is called at which point, handler can start emitting values, etc."
  [handler]
  (Observable/create handler))

(defn getMock [] (json/read-str (slurp "traffic.json")))

;;; This observable represents data produced by real-time streams on
;;; the server. Its observers are proxies for the client that
;;; internally send messages to the client via websockets.

(def ^:private domains
  [
   "vendorinfoportal.amazon.com"
   "w.amazon.com"
   "cr.amazon.com"
   "external"
   "vendormaster.amazon.com"
   "permissions.amazon.com"
   "devcentral.amazon.com"
   "build.amazon.com"
   ])

(defn cheapGaussian [mean sigma]
  (let [sqrt12 3.46410161514
        n      4.0
        r2     (* 2 sqrt12) ; sqrt(n * 12)
        c      (fn [] (- (rand) 0.5))
        r      (* r2 (+ (c) (c) (c) (c))) ; n calls of c
        ] 
    (+ mean (/ (* r sigma) n))
    ))

(defn randrate [] (str
                     (/
                      (Math/round (* 10000 (rand)))
                      1000.0)))

(defn- randomizedDomainHits []
  (map (fn [domain]
         {"domain"     domain,
          "hitsPerSec" (randrate),
          "url"        domain,
          "timestamp"  (System/currentTimeMillis)
          }) domains)
  )

(def ^:private fromTos
  [{:from ["vendorinfoportal.amazon.com"], :tos ["vendorinfoportal.amazon.com"
                                                 "w.amazon.com"
                                                 "external"
                                                 "vendormaster.amazon.com"]}
   {:from ["w.amazon.com"], :tos ["w.amazon.com"
                                  "external"
                                  "vendorinfoportal.amazon.com"]}
   {:from ["vendormaster.amazon.com"], :tos ["vendormaster.amazon.com"
                                             "w.amazon.com"
                                             "external"
                                             "permissions.amazon.com"
                                             "devcentral.amazon.com"
                                             ]}]
  )

(defn- randomizedReferralMatrix []
  (for [fromTo fromTos
        from   (:from fromTo)
        to     (:tos  fromTo)]
    {"from" from, "to" to, "refsPerSec" (randrate)}
    ))

(defn randomizedMock [mock]
  {"hitRateAggs"
   (randomizedDomainHits)
   "referralMatrix"
   (randomizedReferralMatrix)
   }
  )

(defn mockObservable [mock]
  (observable
   (fn [observer]
     (let [f (future
               (doseq [i (range 100)]
                 (-> observer (.onNext (randomizedMock mock)))
                 (Thread/sleep 1000)
                 )
               )
           ]
       (Subscriptions/create #(future-cancel f))))))

;;; ================================================================

(defn on-message [conn json-message]
  (println "WEBSOCKET MESSAGE" conn json-message)
  (let [msg (->
             json-message
             json/read-json
             (get-in [:data :message]))]
    (.send conn (json/write-str
                 {:type "upcased"
                  :message (s/upper-case msg)}))
    ))

(def buffer-length       5)
(def domain-hit-epochs   (atom {}))

(defn do-accum-times! [rates]
  (doseq [rate rates]
    (let [url  (rate "url")
          buf  (@domain-hit-epochs url)
          buf2 (conj (or buf (rb/ring-buffer buffer-length))
                     (rate "timestamp"))
          ]
      (swap! domain-hit-epochs #(into % {url buf2})))))

(defn hit-rates-from-epochs [epoch-maps]
  (map (fn [[url buf]]
         (let [duration (- (last buf) (first buf))
               len      (count buf)
               rate     (if (= 0 duration)
                          (double 0)
                          (/ (double len) (double duration) 1000.0))
               ]
           {url rate})
         )
       epoch-maps)
  )

(defn disconnectedCountsTest []
  (-> (mockObservable (getMock))
      (.map (fn [item]
              (do-accum-times! (item "hitRateAggs"))
              @domain-hit-epochs))
      (.subscribe pp/pprint)))

(defn -main []
  "Thanks to blog.jayfields.com and amalloy/ring-buffer AT github"
  (let [server (WebServers/createWebServer 8080)]
    (doto server
      (.add "/websocket"
            (proxy [WebSocketHandler] []

              (onOpen    [conn]
                (println "WEBSOCKET OPENED"  conn)
                (-> (mockObservable (getMock))
                    (.map (fn [item]
                            (do-accum-times! (item "hitRateAggs"))
                            (hit-rates-from-epochs @domain-hit-epochs)))
                    (.subscribe
                     (fn [datum]
                       (-> conn
                           (.send (json/write-str
                                   {:type "hitdata"
                                    :message (str (vec datum))}))))
                     )))

              (onClose   [c  ]
                (println "WEBSOCKET CLOSED"  c)
                )

              (onMessage [c j]
                                        ;(println "WEBSOCKET MESSAGE" c j)
                (on-message c j)
                )
              ))
      (.add (StaticFileHandler. "."))
      (.start))))

