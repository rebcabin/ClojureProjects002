(ns arrow-mve.core
  (:gen-class))

(defn extract-one [m]
  (-> m :a))

(defn- extract-two [m]
  (-> m :a))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
