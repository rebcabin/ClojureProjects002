(ns quil2.core
  (:use quil.core)
  (:gen-class))

(defn setup []
  (smooth)                              ; anti-aliasing
  (frame-rate 10)
  (background 200))                     ; grey

(defn draw []
  (stroke (random 255))                 ; random grey
  (stroke-weight (random 10))
  (fill (random 255))
  (let [diam (random 100)
        x    (random (width))
        y    (random (height))]
    (ellipse x y diam diam)))

(defsketch example
  :title "Oh so many grey circles"
  :setup setup
  :draw  draw
  :size  [323 200])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))
  (println "Hello, World!"))
