(ns milkshakes.core
  (:require clojure.set)
  (:use [clojure.java.io :as cjio])
  (:use [clojure.string  :as cstr :only [split split-lines]])
  (:use [clojure.pprint  :as pp   :only [pprint]])
  (:gen-class))

(defn get-current-directory []
  (. (java.io.File. ".") getCanonicalPath))

(defn do-per-line [f]
  (with-open [rdr (cjio/reader "input.txt")]
    (doseq [line (line-seq rdr)]
      (f line))))

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

(defn parse-cases [acc ls]
  (let [flavors          (read-string (first ls))
        c                (read-string (fnext ls))
        raw-likes        (take c (drop 2 ls))
        nested-likes     (map #(cstr/split % #"\s")     raw-likes)
        nested-int-likes (map #(map read-string %)      nested-likes)
        likes            (map #(partition 2 (drop 1 %)) nested-int-likes)
        rems             (drop (+ 2 c) ls)
        ]
    (let [ans (conj acc {:flavors flavors :all-likes (vec likes) :customers c})]
      (if (not= '() rems)
        (recur ans rems)
        ans))))

(defn parse-lines [ls]
  (let [ncases (read-string (first ls))]
    (dbg ncases)
    (parse-cases [] (rest ls))
    ))

(defn case->soln [a]
  (let [N      (:flavors a)
        C      (:customers a)
        ms     (int-array N C)
        us     (int-array N C)
        as     [ms us]
        likes  (:all-likes a)
        ;; Each "like" is a list of a flavor index and a
        ;; malted-or-not. Make the leaves into vectors. Sort by count
        ;; to get the most restrictive ones to the front, because they
        ;; eliminate possibilities quickly.
        mikes  (group-by count (map #(map vec %) likes))
        ;; t0     (try (doseq [mike mikes]
        ;;               (doseq [[f m?] mike]
        ;;                 (dbg [f m?]))
        ;;               )
        ;;             (catch Exception e (.getMessage e)))
        _      (doseq [mike mikes]
                 (let [arity (mike 0)
                       elements (mike 1)
                       ]
                   )
                 (dbg mike)
                 )
        ]
    (ppdbg  {:mikes mikes
             :c C
             :m (vec ms)
             :u (vec us)})
    ))

(defn -main
  "Basic husk for programming problems."
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))
  (println (get-current-directory))

  (let [input (slurp
               ;"/Users/rebcabin/Downloads/B-small-practice.in"
               "input.txt"
               )
        _     (spit "input.txt" input)
        lines (cstr/split-lines input)
        answs (map-indexed
               (fn [i l]
                 (str "Case #" (inc i) ": "
                      (identity l)
                      "\n")
                 )
               (let [parsed 
                     (parse-lines lines)]
                 (map case->soln parsed))
               )
        ]
    (with-open [w (cjio/writer "output.txt")]
      (doseq [line answs] (.write w line)))
    ))

