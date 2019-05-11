(ns green-eggs.core-test
  (:require [clojure.test :refer :all]
            [clojure.core.async :as async]
            [green-eggs.core :refer :all]))

(deftest into-testing
  (is (= ["I would eat them in the rain."
          "I would eat them on a train."
          "I would eat them in a box."
          "I would eat them with a fox."
          "I would eat them in a house."
          "I would eat them with a mouse."
          "I would eat them here or there."
          "I would eat them anywhere."]
         (into [] sam-i-am-xform green-eggs-n-ham)))
  (is (= clojure.lang.PersistentVector
         (class (into [] sam-i-am-xform green-eggs-n-ham)))))


