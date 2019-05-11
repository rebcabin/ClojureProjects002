(ns parallel.core
  (:gen-class))

(import '(jsr166y.forkjoin ParallelArray ParallelArrayWithBounds
                           ParallelArrayWithFilter
                           ParallelArrayWithMapping
                           Ops$Op Ops$BinaryOp Ops$Reducer Ops$Predicate Ops$BinaryPredicate
                           Ops$IntAndObjectPredicate Ops$IntAndObjectToObject))

(require '[clojure.core.reducers :as r])
(reduce + (r/filter even? (r/map inc [1 1 1 2])))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
