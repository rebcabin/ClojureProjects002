(ns problem-58.core
  (:use clojure.pprint)
  (:gen-class))

(def k
  (fn c [& fns]
    (fn [& args]
      (let [argc (count args)
            arg  (first args)]
          (if (= argc 1)
            (if fns
              ((first fns) ((apply c (rest fns)) arg))
              arg)
            (if fns
              (let [lfns (butlast fns)
                    lfn  (last fns)
                    arg  (apply lfn args)]
                (if lfns
                  ((first lfns) ((apply c (rest lfns)) arg))
                  arg))
              args))))))

(def k2 (fn [& fs]
  (reduce (fn [f g]
            #(f (apply g %&)))
          fs)))

#_(#(identity %&) 2)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))
  (pprint ((k) [1 2 3 4 5]))
  (pprint ((k) 1 2 3 4 5))
  (pprint ((k rest reverse) [1 2 3 4]))
  (pprint ((k (partial + 3) second) [1 2 3 4]))
  (pprint ((k +) 3 5))
  (pprint ((k +) 3 5 7 9))
  (pprint ((k zero? #(mod % 8) +) 3 5 7 9))
  (pprint ((k #(.toUpperCase %) #(apply str %) take) 5 "hello world"))
  (println "Hello, World!"))
