(ns incremental-running-stats.core)

;;; Problem statement
;;;
;;; Write a program that can take double-precision floating-point data
;;; elements one at a time and can produce, on demand, cumulative
;;; statistics for the number seen so far (the count); the current mean
;;; value; the current variance and standard deviation. Be aware of the
;;; difference between the biased and unbiased estimates of variance and
;;; standard deviation. You may write either an object-oriented solution
;;; or a functional solution. An object-oriented solution should present
;;; methods on an object of a class for computing the statistics. A
;;; functional solution should thread a data structure as an argument so
;;; the function may be used in a fold or reduce.
;;;
;;; For extra credit, analyze potential numerical hazards in the naive
;;; computation of variance and standard deviation, find a better
;;; algorithm, and compare the naive to the better.

(defn add-datum
  "Contributes a datum to a block of running stats; ssr stands for \"sum
  of squared residuals,\" where a residual is the difference between the
  datum and the mean."
  [{:keys [s0 s1 s2 mean ssr naive-ssr var stddev naive-var]} datum]
  (let [new-s0    (inc s0)
        new-s1    (+ s1 datum)
        new-s2    (+ s2 (* datum datum))
        new-mean  (/ new-s1 new-s0)
        new-ssr   (+ ssr (* (- datum mean) (- datum new-mean)))
        new-var   (if (> new-s0 1) (/ new-ssr s0) new-ssr)
        new-naive (- s2 (* new-s0 (* new-mean new-mean)))
        ]
  {:s0        new-s0
   :s1        new-s1
   :s2        new-s2
   :mean      new-mean
   :ssr       new-ssr
   :naive-ssr new-naive
   :var       new-var
   :stddev    (java.lang.Math/sqrt new-var)
   :naive-var (if (> new-s0 1) (/ new-naive s0) new-naive)
   }))

(def zero-stats
  "Produces a zero of the stats monoid, suitable for reducing against a
collection of data."
  {:s0 0 :s1 0 :s2 0 :mean 0 :ssr 0 :naive-ssr 0 :var 0 :stddev 0 :naive-var 0})

(defn example []
  (reduce add-datum
          zero-stats
          (repeatedly 1000000 rand))
  )
