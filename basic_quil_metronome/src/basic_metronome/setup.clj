(ns basic-metronome.setup
  (:require [quil.core :as qc]))

(def WIDTH 800)
(def HEIGHT 600)
(def tick (atom 0))

(defn setup
  []
  (qc/fill 255 0 0))
