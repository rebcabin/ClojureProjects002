(ns uberjar-sample.core
  (:gen-class))

(defn -main [& args]
  (println "Welcome to my project! These are your args: " args))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
