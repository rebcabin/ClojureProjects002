;;; From https://github.com/quil/quil

(ns quil2.core
  (:require [quil.core :as q]))

(defn setup []
  (q/frame-rate 10)
  (q/background 200))                     ; grey

(defn draw []
  (q/stroke (random 255))                 ; random grey
  (q/stroke-weight (q/random 10))
  (q/fill (q/random 255))
  (let [diam (q/random 100)
        x    (q/random (q/width))
        y    (q/random (q/height))]
    (q/ellipse x y diam diam)))

(q/defsketch example
  :title    "Oh so many grey circles"
  :settings #(q/smooth 2) ; anti-aliasing
  :setup    setup
  :draw     draw
  :size     [323 200])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))
  (println "Hello, World!"))
