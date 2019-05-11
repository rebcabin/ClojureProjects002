(ns side-files.core
  (:require side-files.side-file)
  #_(:gen-class))

(defn f [x] (+ x 1))

(defn -main
  "I can call functions like f defined in the core file, and I can call
  functions like g and h defined in the side-file."
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))
  (println (f 41))
  (println (g 42))
  (println (h 41))
  )
