(ns store-credit.core
  (:require clojure.set)
  (:use [clojure.java.io :as cjio])
  (:use [clojure.string  :as cstr :only [split split-lines]])
  (:use [clojure.pprint  :as pp   :only [pprint]])
  (:gen-class))

;;; This is speed-code: no attention to error-checking or testing.

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

(defn case-from-lines [triple]
  {:credit (read-string (first triple))
   :nitems (read-string (nth triple 1))
   :prices (map read-string (cstr/split (nth triple 2) #"\s"))}
  )

(defn soln-from-case [cs]
  (let [c    (:credit cs)
        ps   (:prices cs)
        ;; Map prices to indices as singleton-vectors.
        zs   (map-indexed (fn [i price] {price [(inc i)]}) (:prices cs))
        ;; Merge maps with same prices, conj-ing up vectors of
        ;; indices, to produce a map of price to all indices with that
        ;; price. The conditions of the problem imply that all vectors
        ;; will be of length one or two.
        ys   (reduce
              (fn [m z]
                (merge
                 m
                 (let [[p i] (first z) ; Turns map into vector.
                       f     (m p)]    ; Do we already have that price?
                   (if f
                     {p (conj f (i 0))} ; If so, conj-in the current index.
                     z                  ; Otherwise, just return the map.
                     ))))
              {} 
              zs)
        ;; Throw away entries where the price is exactly half of the
        ;; input price and the number of indices is less than two,
        ;; otherwise the item will match against itself.
        ws   (filter #(let [[price indices] %]
                        (not
                         (and (= price (/ c 2))
                              (= (count indices) 1)))) ys)
        ;; Put the stuff back into a map for the following fast-lookup
        ;; trick.
        vs   (into {} ws)
        fs   (filter
              ;; Keep only non-nil items out of the following sequence.
              identity
              ;; Lookup elements with exactly the price difference.
              (map #(vs (- c %)) ps))]
    fs))

(defn -main
  "Solve codeJam practice problem https://code.google.com/codejam/contest/351101/dashboard#s=p0."
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))

  (let [input (slurp
               ;"input.txt"
               "/Users/rebcabin/Downloads/A-large-practice.in"
               )
        lines (cstr/split-lines input)
        n (read-string (first lines))
        flat-cases (rest lines)
        cases (reduce
               (fn [cases triple] (conj cases (case-from-lines triple)))
               []
               (partition 3 flat-cases))
        solns (map soln-from-case cases)
        ;_     (ppdbg solns)
        ys    (map #(map set %) solns)
        ;_     (ppdbg ys)
        zs    (map #(apply clojure.set/union %) ys)
        ;_     (ppdbg zs)
        ready (map-indexed
               (fn [i [a b]]
                 (str "Case #" (inc i) ": " a " " b "\n"))
               (map #(-> % vec sort) zs))]
    (with-open [w (cjio/writer "output.txt")]
      (doseq [line ready] (.write w line)))
    )
  'done
  )

