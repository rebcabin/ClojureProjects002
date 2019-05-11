(ns maybe.core
  (:use clojure.algo.monads))

(defmacro pdump [x]
  `(let [x# (try ~x (catch Exception e#
                      (str "pdump caught exception: " (.getMessage e#))))]
     (do (println "----------------")
         (clojure.pprint/pprint '~x)
         (println "~~>")
         (clojure.pprint/pprint x#)
         (println "----------------")
         x#)))

(defn maybe-t-2
  [m]
  (monad [m-result (with-monad m m-result)
          m-bind   (with-monad m
                     (fn [mv f]
                       (m-bind mv
                               (fn [x]
                                 (if (nil? x)
                                   (m-result nil)
                                   (f x))))))
          m-zero   (with-monad m m-zero)
          m-plus   (with-monad m m-plus)
          ]))

(defmonad if-not-error-m
  [m-result (fn [value] value)
   m-bind   (fn [value f]
              (if-not (:error value)
                (f value) 
                value))
   m-zero   {:error "unspecified error"}
   m-plus   (fn [& mvs]
              (first (drop-while :error mvs)))
   
   ])


