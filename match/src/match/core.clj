(ns match.core
  (:use [clojure.core.match :only (match)])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  (dorun (map println args))
  )
