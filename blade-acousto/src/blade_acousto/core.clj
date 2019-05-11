(ns blade-acousto.core
  (:require [clatrix.core        :as clx]
            [clojure.core.matrix :as cmx]
            [complex.core        :as cpx])
  (:gen-class))

(defn line-circle-intersections []
  "Find intersection of a circle at the origin of radius r with a line
  connecting tail to head."
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  #_(cmx/set-current-implementation :vectorz)
  (cmx/set-current-implementation :persistent-vector)

  (println (cmx/mmul (cmx/matrix [1 2 3])
                     (cmx/matrix [[1] [2] [3]]))) ; vec times colvec => [14]
  (println (cmx/mmul (cmx/matrix [[1 2 3]])
                     (cmx/matrix [1 2 3]))) ; rowvec times vec => [14]
  (println (cmx/mmul (cmx/matrix [[1 2 3]
                                  [4 5 6]
                                  [7 8 9]]) (cmx/matrix [[1] [2] [3]]))) ; matrix times colvec => [[14] [32] [50]]
  (println (cmx/mmul (cmx/matrix [[1 2 3]
                                  [4 5 6]
                                  [7 8 9]]) (cmx/matrix [1 2 3]))) ; matrix times vec => [14 32 50]
 
  (println (try (cmx/mmul (cmx/matrix [1 2 3])
                          (cmx/matrix [[1 2 3]]))
                (catch Exception e (str "caught exception: " (.getMessage e))))) ; vec as rowvec times rowvec => RuntimeException Mismatched vec sizes ...
  (println (try (cmx/mmul (cmx/matrix [[1] [2] [3]])
                          (cmx/matrix [1 2 3]))
                (catch Exception e (str "caught exception: " (.getMessage e))))) ; colvec times vec as colvec  => RuntimeException Mismatched vec sizes ...
  (let [M (cmx/matrix [[1 2] [3 4]])
        V (cmx/matrix [1 2])]
    (println M)
    (println (cmx/to-nested-vectors M)))
  (println (cpx/stringify (cpx/complex 1 2)))
  (println "Hello, World!"))
