(ns web-client-trial.core
  (:use clojure.pprint)
  (:require [clj-http.client :as client]
            [clj-json.core   :as json  ]
            ))

(def ^:private site "http://localhost:3000")

(defn- getelems []
  (-> (str site "/elems")
      client/get
      :body
      json/parse-string))

(defn- getelem-light [key]
  (-> (str site "/elems/" key)
      client/get
      :body))

(defn- getelem [key]
  (-> (str site "/elems/" key)
      client/get
      :body
      json/parse-string))

(defn- putelem [key data]
  (let [b (json/generate-string {:data data})]
    (pprint b)
    (-> (str site "/elems/" key)
        (client/put
         {:body b,
          :content-type :json
          })
        :body
        json/parse-string)))

(defn- justget []
  (->  site  client/get))

(defn- justdelete []
  (->  (str site "/elems")
       client/delete))

(defn- justput [nym]
  (-> site
      (client/put
       {:body (json/generate-string {:name nym})
        :content-type :json
        })
      :body
      json/parse-string))

(defn- postbad []
  (-> (str site "/messages")
      (client/post
       {:body (json/generate-string {:data "test"})
        :content-type :json
        })
      :body
      json/parse-string))

(defn -main
  [& args]
  (println "justget "       (justget))
  (println "justput "       (justput "jacker"))
  (println "getelems"       (getelems))
  (println "getelem-light"  (getelem-light 1))  ; null
  (println "getelem 1"      (getelem 1))        ; nil
  (println "putelem \"k\""  (putelem "k" {:tag 42}))
  (println "putelem \"q\""  (putelem "q" {:tag 37}))
  (println "getelems "      (getelems))
  (println "postbad"        (postbad))
  (println "putelem \"http\"" 
             (putelem "http" {:observer "some-document"}))
  #_(justdelete)
  )
(-main)
;;; The paramstest endpoint is broken. It's using an
;;; invalid form of a route description.
#_(-> (str site "/paramstest")
    (client/post
      {:body (json/generate-string {:data "params"})
       :content-type :json
       })
    :body
    json/parse-string)


    
    
    
    