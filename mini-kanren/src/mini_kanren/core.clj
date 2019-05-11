(ns mini-kanren.core
  (:refer-clojure :exclude [==])
  (:require [clojure.test :as test]
            [mini-kanren.utils])
  #_(:gen-class)
  (:use [clojure.core.logic]))

(defmacro pdump [x]
  `(let [x# ~x]
     (do (println "----------------")
         (clojure.pprint/pprint '~x)
         (println "~~>")
         (clojure.pprint/pprint x#)
         (println "----------------")
         x#)))

;;;  __  __      _
;;; |  \/  |__ _(_)_ _
;;; | |\/| / _` | | ' \
;;; |_|  |_\__,_|_|_||_|

(defn -main []
  (test/run-all-tests #"mini-kanren.core-test"))
