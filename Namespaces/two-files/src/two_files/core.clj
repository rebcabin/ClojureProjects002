(ns two-files.core
  (:require [two-files.kfile :as k1]
            [two-files.k2    :as k2]
            )
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))

  (k1/foo)
  (k2/bar)
  (println "two-files.core/-main"))
