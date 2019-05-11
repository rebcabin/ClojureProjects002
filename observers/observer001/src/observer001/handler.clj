(ns observer001.handler
  (:use     compojure.core)
  (:use     clojure.pprint)
  ;; pprint requires a following (println <something>) to flush its buffer.
  (:use     ring.middleware.json-params)
  (:require [compojure.handler        :as handler]
            [compojure.route          :as route  ]
            [clj-json.core            :as json   ]
            [observer001.elem         :as elem   ]
            [clojure.data.json        :as cdjson ]
            [clojure.string           :as s      ]
            [clojure.pprint           :as pp     ]
            [observer001.ring-buffer  :as rb     ]
            )
  (:import  [org.webbitserver         WebServer WebServers WebSocketHandler]
            [org.webbitserver.handler StaticFileHandler                    ]
            [rx                       Observable Observer Subscription     ]
            [rx.subscriptions         Subscriptions                        ]
            [rx.util                  AtomicObservableSubscription         ]
            ))

;;; ================================================================
;;; Mocks for hit rate
;;; ================================================================



;;; ================================================================
;;; compojure REST web server
;;; ================================================================

(defn json-response [data & [status]]
  {:status  (or status 200)
   :headers {"Content-Type" "application/json"}
   :body    (json/generate-string data)})

(defroutes app-routes
  
  (GET "/" []
       (println "GET /")
       (let [r (json-response {"hello" "json-get"})]
         (pprint {:json-response r})
         (println "")                   ; Flush pprint
         r ))

  (GET "/elems" []
       (println "GET /elems")
       (let [e (elem/list)
             j (json-response e)]
         (pprint {:elem-list e, :json-response j})
         (println "")
         j ))

  (GET "/elems/:key" [key]
       (println (str "GET /elems/" key))
       ;; Clojure nil gets jsonized as {..., :body null},
       ;; which is invalid json
       (let [e (elem/get key)
             j (json-response e)]
         (pprint {:key key, :elem-list e,
                  :json-response j})
         (println "")
         j ))
  
  (DELETE "/elems" []
          (println "DELETE /elems")
          (elem/clear)
          (pprint {:elem-list (elem/list)})
          (println ""))

  (PUT "/elems/:key" [key data]
       (println (str "PUT /elems/" key))
       (let [e (elem/put key data)
             j (json-response e)]
         (pprint {:key key, :data data,
                  :elem-put e, :json-response j})
         (println "")
         j ))

  (PUT "/" [name]
       (println (str "PUT /" name))
       (newline)
       (json-response {"hello" name}))

  (POST "/messages" [data]
        (println (str "POST /messages" data))
        (let [j (json-response {"stuff" data})]
          (pprint j)
          (println "")
          j ))
  
  ;; The following form throws an exception. I recall 
  ;; seeing this in reviewed code and I must re-examine.
  #_(POST "/paramstest" data
        (println "POST /paramstest" data)
        (let [j (json-response {"stuff" data})]
          (pprint j)
          (println "")
          j))

  (route/not-found "Not Found"))

(def app
  (do
    (let [server (WebServers/createWebServer 8080)]
      (doto server
        (.add "/websocket"
              (proxy [WebSocketHandler] []

                (onOpen    [conn]
                  (println "WEBSOCKET OPENED"  conn)
                  (-> conn
                      (.send (cdjson/write-str {:foo "bar"}))))

                (onClose   [c  ]
                  (println "WEBSOCKET CLOSED"  c)
                  )

                (onMessage [c j]
                  (println "WEBSOCKET MESSAGE" c j)
                  )
                ))
        (.add (StaticFileHandler. "."))
        (.start)))
    (-> app-routes wrap-json-params))

  ;;  (handler/site app-routes) ; <---===/// absolutely does not work

  )
