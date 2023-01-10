(ns coins.core
  (:use clojure.pprint)
  ;; (:require [clojure.string :refer [join]])
  )

(defn- method-description [method]
  (join " "
        [(.getName method)
         (java.util.Arrays/toString (.getParameterTypes method))
         "->"
         (.getReturnType method)]))

;; TODO:
;; - show return types
;; - something else?
(defn jmethods
  "Return all public methods visible from 'clazz.'"
  [clazz]
  (->> (:methods (bean clazz))
       (sort-by #(.getName %))
       (map method-description)))

(pprint (jmethods java.util.Date))

(pprint (jmethods java.lang.reflect.Method))

(jmethods java.util.Date)
