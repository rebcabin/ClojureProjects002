(ns temp-2.core
  (:use clojure.algo.monads))

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
#_
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
          (combine (-> db-results
                       call-other-web-service))))
    (catch Exception e (.getMessage e))))

(defmonad if-not-error-m
  [m-result (fn [value] value)
   m-bind   (fn [value f]
              (if-not (:error value)
                (f value) 
                (m-result value)))
  ])

(defmacro with-em-result [expr]
  `(with-monad if-not-error-m (m-result ~expr)))
(defn- computation [] (with-em-result {}))
(defn- authorize [computation]
  (with-em-result
    (if (randomly-error) {:error "auth errored"}
        {:auth-token "John's credentials"})))
(defn- read-database [auth-token]
  (with-em-result
    (if (randomly-error) {:error "database errored"}
        {:name "John", :PO 421357})))
(defn- call-web-service [database-results]
  (with-em-result
    (if (randomly-error) {:error "ws1 errored"}
        [{:item "camera"}, {:item "shoes"}])))
(defn- filter-ws [web-service-call-results]
  (with-em-result
    (if (randomly-error) {:error "filter errored"}
        [{:item "camera"}])))
(defn- call-other-web-service [database-results]
  (with-em-result
    (if (randomly-error) {:error "ws2 errored"}
        [{:price 420.00M}])))
(defn- combine [other-ws-results-val]
  (fn [filtered-ws-results-val]
    (with-em-result
      (if (randomly-error)
        {:error "combine errored"}
        (concat filtered-ws-results-val
                other-ws-results-val)))))


(defn- computation []
  (with-em-result
    {:computation "computation"}))

(defn- authorize [computation]
  (with-em-result
    (merge computation
           (if (randomly-error)
             {:error "auth errored"}
             {:auth-token "John's credentials"}))))

(defn- read-database [auth-token]
  (with-em-result
    (merge auth-token
           (if (randomly-error)
             {:error "database errored"}
             {:name "John", :PO 421357}))))

(defn- call-web-service [database-results]
  (with-em-result
    (merge database-results
           (if (randomly-error)
             {:error "ws1 errored"}
             {:items [{:item "camera"}, {:item "shoes"}]}))))

(defn- filter-ws [web-service-call-results]
  (with-em-result
    (merge web-service-call-results
           (if (randomly-error)
             {:error "filter errored"}
             {:items [{:item "camera"}]}))))

(defn- call-other-web-service [database-results]
  (with-em-result
    (merge database-results
           (if (randomly-error)
             {:error "ws2 errored"}
             {:prices [{:price 420.00M}]}))))

(defn- combine [other-ws-results-val]
  (fn [filtered-ws-results-val]
    (merge other-ws-results-val filtered-ws-results-val
           (with-em-result
             (if (randomly-error)
               {:error "combine errored"}
               {})))))

#_(println
 (domonad if-not-error-m
          [a1 (computation)
           a2 (authorize a1)
           db (read-database a2)
           a4 (call-web-service db)
           a5 (filter-ws a4)
           a6 (call-other-web-service db)
           a7 ((combine a6) a5)
           ]
          a7
          ))

(defmacro =>> [in-monad & transforms]
  `(with-monad if-not-error-m
     ((m-chain [~@transforms]) ~in-monad)))

(println
 (let [db-results
       (=>> (computation)
            authorize
            read-database)]
   (=>> db-results
        call-web-service
        filter-ws
        (combine (=>> db-results
                      call-other-web-service)))))

