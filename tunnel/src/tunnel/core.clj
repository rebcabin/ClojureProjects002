(ns tunnel.core
  (:use compojure.core)
  (:require [compojure.handler       :as   handler ]
            [compojure.route         :as   route   ]
            [clojure.pprint          :as   pp      ]
            [clojure.data.json       :as   json    ]
            [clj-http.client         :as   client  ]
            [rx.lang.clojure.interop :as   rx      ])
  (:use     [clojail.core            :only [sandbox]]
            [clojail.testers         :only [blacklist-symbols
                                            blacklist-objects
                                            secure-tester
                                            ]])
  (:import [rx
            Observable
            Observer
            subscriptions.Subscriptions
            subjects.Subject
            subjects.PublishSubject]))

(defn- java-io-ByteArrayInputStream-to-string [jiobais]
  (with-open [r jiobais]
    (loop [b (.read r) v []]
      (if (= b -1)
        (apply str v)
        (recur (.read r) (conj v (char b)))))))

(let [rsub (PublishSubject/create)
      ssub (-> rsub
               (.map     (rx/fn [j] (let [x (read-string (j :x))]
                                      {:x (str (+ 100 x))})))
               (.filter  (rx/fn [j] (even? (read-string (j :x)))))
               (.mapMany
                (rx/fn [j]
                  (let [x (read-string (j :x))]
                    (Observable/create
                     (rx/fn [obr]
                       (.onNext obr {:x (str x)})
                       (.onNext obr {:x (str (* x x))})
                       (.onCompleted obr)))))))
      ]
  (.subscribe rsub (rx/action [obn] (pp/pprint (str "Observed!: " obn))))
  (.subscribe ssub (rx/action [obn] (pp/pprint (str "Transformed!: " obn))))
  (defroutes app-routes
    (GET "/"  [] "Hello World")
    (POST "/" {payload :body}
          (let [payload-json (java-io-ByteArrayInputStream-to-string payload)
                payload-clj  (json/read-str payload-json :key-fn keyword)]
            (pp/pprint (str "Request!: " payload-clj))
            (.onNext rsub payload-clj)
            payload-json                ; http response body is this
            ))
    (route/resources "/")
    (route/not-found "Not Found")))

(def app
  (handler/site app-routes))
