(ns t9.core
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

(def cmap
  {\a {:code "2"     :key 2}
   \b {:code "22"    :key 2}
   \c {:code "222"   :key 2}
   \d {:code "3"     :key 3}
   \e {:code "33"    :key 3}
   \f {:code "333"   :key 3}
   \g {:code "4"     :key 4}
   \h {:code "44"    :key 4}
   \i {:code "444"   :key 4}
   \j {:code "5"     :key 5}
   \k {:code "55"    :key 5}
   \l {:code "555"   :key 5}
   \m {:code "6"     :key 6}
   \n {:code "66"    :key 6}
   \o {:code "666"   :key 6}
   \p {:code "7"     :key 7}
   \q {:code "77"    :key 7}
   \r {:code "777"   :key 7}
   \s {:code "7777"  :key 7}
   \t {:code "8"     :key 8}
   \u {:code "88"    :key 8}
   \v {:code "888"   :key 8}
   \w {:code "9"     :key 9}
   \x {:code "99"    :key 9}
   \y {:code "999"   :key 9}
   \z {:code "9999"  :key 9}
   \space {:code "0" :key 0}
   })

(defn keys-from-line [l]
  (let [c1 (:code (cmap (first l)))
        d1 (rest l)
        b1 (butlast l)
        ]
    (apply str
           (cons c1
                 (mapcat
                  (fn [pre now]
                    (let [char-now (cmap now)
                          char-pre (cmap pre)]
                      (if (= (:key char-now) (:key char-pre))
                        (list \space (:code char-now))
                        (list (:code char-now))
                        )
                      ))
                  b1
                  d1)))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))
  (let [input (slurp
               "/Users/rebcabin/Downloads/C-large-practice.in"
               ;"input.txt"
               )
        _     (spit "input.txt" input)
        lines (cstr/split-lines input)
        answs (map-indexed
               (fn [i l]
                 (str "Case #" (inc i) ": "
                      (keys-from-line l)
                      "\n")
                 )
               (rest lines)
               )
        ]
    (with-open [w (cjio/writer "output.txt")]
      (doseq [line answs] (.write w line)))
    ))
