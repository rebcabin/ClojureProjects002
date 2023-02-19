(ns stack-machine.stack
  (:use    asr.utils)
  (:require [clojure.math.numeric-tower :as math])
  (:import java.util.concurrent.Executors))


;;; TODO: Consider making the stack machine transactional.


(def eval-stack (atom [] :validator vector?))


(defn push-call-args [args]
  (doseq [arg args]
    (swap! eval-stack conj arg))
  (echo @eval-stack))


(defn pop-arg-stack
  "TODO: RACE!"
  []
  (let [top (peek @eval-stack)]
    (swap! eval-stack pop)
    top))


(defn run-external
  [nym]
  (case nym
    'pow/__lpython_overloaded_0__pow
    (let [victim (pop-arg-stack)
          expont (pop-arg-stack)
          result (math/expt victim expont)]
      (swap! eval-stack conj result))))


(defn pop-result
  []
  (pop-arg-stack))
