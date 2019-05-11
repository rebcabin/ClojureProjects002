(ns reverse-words.core
  (:require clojure.set)
  (:use [clojure.java.io :as cjio])
  (:use [clojure.string  :as cstr :only [split split-lines]])
  (:use [clojure.pprint  :as pp   :only [pprint]])
  (:gen-class))

;;; This is speed-code: no attention to error-checking or testing.

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

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))

  (let [input (slurp
               "/Users/rebcabin/Downloads/B-large-practice.in"
               ;"input.txt"
               )
        lines (cstr/split-lines input)
        answs (map-indexed
               (fn [i l]
                 (str "Case #" (inc i) ": "
                      (cstr/join " " (reverse (cstr/split l #"\s")))
                      "\n"))
               (rest lines))
        ]
    (with-open [w (cjio/writer "output.txt")]
      (doseq [line answs] (.write w line)))
    )
  )

