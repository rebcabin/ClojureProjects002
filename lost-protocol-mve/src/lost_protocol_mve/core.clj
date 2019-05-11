(ns lost-protocol-mve.core)

(defprotocol SayFortyTwo
  (say-it [this]))

(defrecord sayer [datum]
  SayFortyTwo
  (say-it [this]
    (if (= 42 (:datum this))
      (do (println "forty-two!")
          true)
      (do (println "oh noez, something is wrong!")
          false))))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
