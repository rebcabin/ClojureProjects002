(ns basic-metronome.draw
  (:use [basic-metronome.setup :only [tick]])
  (:require [quil.core :as qc]))

(defmethod print-method clojure.lang.PersistentQueue
  [q, w]
  (print-method '<- w)
  (print-method (seq q) w)
  (print-method '-< w))

(defn- mx [dim ang]  (* 0.5 dim (Math/sin ang)))
(defn- tx [ang]      (mx (qc/width)  ang))
(defn- ty [ang]      (mx (qc/height) ang))

(defn- parm
  "Converts x, which must be in the same units of measure as \"lo\" and
  \"hi\", into a fractional distance from \"lo\" to \"hi\"."
  [x lo hi] (/ (- x lo) (- hi lo)))

(defn- lirp
  "Given a fractional distance from \"lo\" to \"hi\", produce a position
   scaled to the units of measure of \"lo\" and \"hi\"."
  [t lo hi] (+ lo (* t (- hi lo))))

(defn- half [x] (* 0.5 x))

(defn- doubel [x] (* 2.0 x))

(defn draw
  []
  (swap! tick inc)
  (qc/background 0 0 64)
  (qc/translate (half (qc/width)) (half (qc/height)))
  (let [theta (* 0.05 @tick)
        lo    (- @tick 20)
        hi    @tick
        _     (dorun (for [j (range lo hi)
                           t [(* 0.05 j)]]
                       (do
                         (qc/fill 0 (qc/map-range j lo hi 64 255) 0 )
                         ;(qc/fill (lirp (parm j lo hi) 64 255) 0 0)
                         (qc/ellipse (tx t) (ty (* 1.1 t)) 20 20))))
        ]
    (qc/fill 255 0 0)
    (qc/ellipse (tx (inc theta)) (ty (* 1.1 (inc theta))) 20 20)
    (qc/ellipse (tx theta) (ty (* 1.1 theta)) 20 20)))
