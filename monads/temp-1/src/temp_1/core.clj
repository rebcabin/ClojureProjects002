(ns temp-1.core
  (:use clojure.algo.monads)
  (:gen-class))

(defn- randomly-error [] (< (rand) 0.10))
(defn- computation [] {})
(defn- authorize [computation]
  (if (randomly-error) (throw (Exception. "auth errored"))
                       {:auth-token "John's credentials"}))
(defn- read-database [auth-token]
  (if (randomly-error) (throw (Exception. "database errored"))
                       {:name "John", :PO 421357}))
(defn- call-web-service [database-results]
  (if (randomly-error) (throw (Exception. "ws1 errored"))
                       [{:item "camera"}, {:item "shoes"}]))
(defn- filter-ws [web-service-call-results]
  (if (randomly-error) (throw (Exception. "filter errored"))
                       [{:item "camera"}]))
(defn- call-other-web-service [database-results]
  (if (randomly-error) (throw (Exception. "ws2 errored"))
                       [{:price 420.00M}]))
(defn- combine [filtered-web-service-results
               other-web-service-call-results]
  (if (randomly-error) (throw (Exception. "combine errored"))
      (concat filtered-web-service-results
              other-web-service-call-results)))

(defn- combine-2 [filtered-web-service-results
                  other-web-service-call-results]
  (if (randomly-error) (throw (Exception. "combine errored"))
      (concat filtered-web-service-results
              other-web-service-call-results)))

(defmonad if-not-error-m
  [m-result (fn [value] value)
   m-bind   (fn [value f]
              (if-not (:error value)
                (f value) 
                value))
  ])

(defn -main [& args]
  (alter-var-root #'*read-eval* (constantly false))
  (println
   (try
     (let [db-results
           (-> (computation)
               authorize
               read-database
               )]
       (-> db-results
           call-web-service
           filter-ws
           (combine (call-other-web-service db-results))))
     (catch Exception e (.getMessage e))))
  (println
   (try
     (let [db-results
           (-> (computation)
               authorize
               read-database
               )]
       (-> db-results
           call-web-service
           filter-ws
           (combine (call-other-web-service db-results))))
     (catch Exception e (.getMessage e))))  )


