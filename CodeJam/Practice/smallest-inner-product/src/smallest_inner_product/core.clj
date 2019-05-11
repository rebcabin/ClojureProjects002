(ns smallest-inner-product.core
  (:require clojure.set)
  (:use [clojure.java.io :as cjio])
  (:use [clojure.string  :as cstr :only [split split-lines]])
  (:use [clojure.pprint  :as pp   :only [pprint]])
  (:gen-class))

(defmacro dbg [x]
  `(let [x# ~x]
     (do (println '~x "~~>" x#)
         x#))) 

;;; and pretty-printing version

(defmacro ppdbg [x]
  `(let [x# ~x]
     (do (println "----------------")
         (clojure.pprint/pprint '~x)
         (println "~~>")
         (clojure.pprint/pprint x#)
         (println "----------------")
         x#))) 

(defn inner [v1 v2] (apply + (map * v1 v2)))

(defn smallest-inner [v1 v2] (inner (sort > v1) (sort < v2)))

(defn str-of-components->vec [s]
  (let [strs (cstr/split s #"\s")
        nums (map read-string strs)]
    (vec nums))
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))
  (let [input (slurp
               ;"/Users/rebcabin/Downloads/A-large-practice (2).in"
               "input.txt"
               )
        _     (spit "input.txt" input)
        lines (cstr/split-lines input)
        ;; The (-> l vec) incantation just forces the lazy partition.
        parts (map (fn [l] (-> l vec)) (partition 3 (rest lines)))
        vects (map (fn [part]
                     {:dim (part 0),
                      :v1 (str-of-components->vec (part 1)),
                      :v2 (str-of-components->vec (part 2))
                      })
                   parts)
        answs (map-indexed
               (fn [i l]
                 (str "Case #" (inc i) ": "
                      (smallest-inner (:v1 l) (:v2 l))
                      "\n")
                 )
               vects
               )
        ]
    (with-open [w (cjio/writer "output.txt")]
      (doseq [line answs] (.write w line)))
    ))
