(ns two-files-lib.core
  (:require [two-files-lib.kfile :as kfile]))

(defn foo
  "I don't do a whole lot."
  [x]
  (kfile/foo)
  (println x "-- Hello, World!"))

