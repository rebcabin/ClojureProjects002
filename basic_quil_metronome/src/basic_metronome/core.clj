(ns basic-metronome.core
  (:use [basic-metronome.setup :only [HEIGHT WIDTH]])
  (:require [basic-metronome.draw :as dynamic-draw]
            [basic-metronome.setup :as dynamic-setup]
            [quil.core :as qc]))

(defn on-close-sketch []
  ;;(stop)
  )

(defn run-sketch []
  (qc/defsketch the-sketch
    :title "Hello Metronome"
    :setup dynamic-setup/setup
    :draw dynamic-draw/draw
    :on-close on-close-sketch
    :size [WIDTH HEIGHT]))

(defn stop-sketch [] (qc/sketch-stop the-sketch))
(defn restart-sketch [] (qc/sketch-start the-sketch))
(defn close-sketch [] (qc/sketch-close the-sketch))

;;(run-sketch)
;;(qc/sketch-stop the-sketch)
;;(qc/sketch-start the-sketch)
;;(qc/sketch-close the-sketch)
