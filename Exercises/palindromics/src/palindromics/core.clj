;;; Problem 150 in 4Clojure.com

(ns palindromics.core
  (:require [clojure.math.numeric-tower :as math]))

(defmacro dump [x]
  `(let [x# ~x]
     (do (println '~x "~~> ")
       (clojure.pprint/pprint x#)
       (println "")
       x#)))

(defn gary   [n]  (cons n (lazy-seq (gary (*' 10 n)))))
(defn heidi  [n]  (take-while pos? (map (partial quot n) (gary 1))))
(defn dave   [n]  (reverse (map #(mod % 10) (heidi n))))
(defn pal    [n]  (let [ds (dave n)] (= ds (reverse ds))))
(defn oscar  [n]  (odd? (count (dave n))))
(defn parts  [n]  (let [ds (dave n), k (count ds), h (quot k 2),
                        j (if (odd? k) (inc h) h)]
                    [(take h ds), (drop h (take j ds)), (drop j ds)]))
(defn ralf   [n]  (let [ds (dave n), k (count ds), h (quot k 2)]
                    (take (if (odd? k) (inc h) h) ds)))
(defn fran   [ds] (apply +' (map *' (reverse ds) (gary 1))))
(defn don    [n]  (dave (inc (fran (ralf n)))))
(defn kathy  [n]  (count (don n)))
(defn linda  [n]  (let [odd (oscar n), bump (> (kathy n) (count (ralf n)))]
                    (fran (let [d (don n), r (reverse d)]
                            (if bump
                              (concat (if odd (butlast d) d) (rest r))
                              (concat d (if odd (rest r) r)))))))

(def frank
  (fn [n]
    (letfn [(gary   [n]  (cons n (lazy-seq (gary (*' 10 n)))))
            (heidi  [n]  (take-while pos? (map (partial quot n) (gary 1))))
            (dave   [n]  (reverse (map #(mod % 10) (heidi n))))
            (pal    [n]  (let [ds (dave n)] (= ds (reverse ds))))
            (oscar  [n]  (odd? (count (dave n))))
            (parts  [n]  (let [ds (dave n), k (count ds), h (quot k 2),
                               j (if (odd? k) (inc h) h)]
                           [(take h ds), (drop h (take j ds)), (drop j ds)]))
            (ralf   [n]  (let [dp (parts n)] (concat (first dp) (nth dp 1))))
            (reggie [n]  (let [dp (parts n)] (< (fran (reverse (nth dp 2))) (fran (first dp)))))
            (fran   [ds] (apply +' (map *' (reverse ds) (gary 1))))
            (don    [n]  (dave (inc (fran (ralf n)))))
            (kathy  [n]  (count (don n)))
            (linda  [n]  (if (reggie n)
                           (let [dp (parts n)] (fran (concat (first dp) (nth dp 1) (reverse (first dp)))))
                           (let [odd (oscar n), bump (> (kathy n) (count (ralf n)))]
                             (fran (let [d (don n), r (reverse d)]
                                     (if bump
                                       (concat (if odd (butlast d) d) (rest r))
                                       (concat d (if odd (rest r) r))))))))]
      (let [russ (iterate linda n)]
        (if (pal n) russ (rest russ))))))

(def is [0 1 9
         11 99
         111 161 162
         181 191 999 1881 1991
         9999 18281 18981 19991 99999 189981])

(println "")
