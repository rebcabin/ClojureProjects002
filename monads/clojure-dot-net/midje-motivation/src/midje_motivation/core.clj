(ns midje_motivation.core
(:require [clojure.zip             :as zip    ]
          clojure.string
          clojure.pprint
          [clojure.reflect         :as r      ]
          [swiss-arrows.core       :refer :all]
          [clojure.algo.monads     :refer :all]
          )
  )

(defmacro pdump
  "Monitoring and debugging macro with semantics of 'identity'."
  [x]
  `(let [x# (try ~x (catch Exception e# (str e#)))]
     (do (println "----------------")
         (clojure.pprint/pprint '~x)
         (println "~~>")
         (clojure.pprint/pprint x#)
         x#)))

(def ^:private
  numbers-members
  (pdump
   (into #{}
         (map (comp #(% 1) first)
              (sort-by
               :name
               (filter
                :exception-types
                (:members (r/reflect clojure.lang.Numbers :ancestors true))))))))


(defn half-double [n]
  [(/ n 2) (* n 2)])

(defn inc-int [n]
  [(+ n 5) (+ n 10)])
