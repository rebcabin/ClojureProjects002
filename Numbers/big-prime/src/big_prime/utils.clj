(ns big-prime.utils
  (:require clojure.string
            clojure.pprint))

(defmacro pdump [x]
  `(let [x# (try ~x (catch Exception e#
                      (str "pdump caught exception: " (.getMessage e#))))]
     (do (println "----------------")
         (clojure.pprint/pprint '~x)
         (println "~~>")
         (clojure.pprint/pprint x#)
         (println "----------------")
         x#)))

(defn list-defs []
  (keys (ns-publics 'big-prime.core)))

(defn flip [f] (fn [x y] (f y x)))

(defn plucker [n] (partial (flip nth) n))

(defn my-mapcat
  [f coll]
  (lazy-seq
   (if (not-empty coll)
     (concat
      (f (first coll))
      (my-mapcat f (rest coll))))))