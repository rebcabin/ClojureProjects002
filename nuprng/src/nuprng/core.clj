(ns nuprng.core
  (:use clojure.pprint)
  (:require [clojure.math.numeric-tower :as mathEx]))

;;; Problem statement
;;;
;;; Generate random music on piano white keys by sampling a given
;;; distribution of notes. Outcomes are the N=7 characters from 'A' to
;;; 'G' with proportions P = (37, 0, 17, 5, 12, 11, 44). Generate
;;; characters randomly and statistically in those proportions. Show
;;; that your solution has the given statistics. You may use a uniformly
;;; distributed floating-point, [0, 1) pseudo-random number generator
;;; such as Unix "rand." Your randoms need not be cryptographically
;;; strong.
;;;
;;; Your code should handle the general case where the outcomes are
;;; arbitrary types, the number of outcomes is N and the given
;;; proportions are large integers. The above is just one concrete
;;; instance of the general problem.
;;;
;;; Characterize the space and time complexity of your solution in
;;; "big-O" terms. If you divide your solution into preprocessing and
;;; production phases, you need only chracterize the complexity of the
;;; production phase and may assume unbounded resources for
;;; preprocessing.
;;;
;;; There are several solutions to this problem with a variety of
;;; "big-O" complexities.

(def outcome
  "Get the outcome from a outcome-frequency pair."
  first)

(def frequency
  "Get the frequency from an outcome-frequency pair."
  second)

(defn total
  "The sum of frequencies in a distribution. A distribution is a sequence of
  pairs of outcomes and frequencies."
  [frqs] (apply + (map frequency frqs)))

(defn rand-int2
  "Produce a random integer from 0 (inclusive) to mac (exclusive). Clojure has a
built-in \"rand-int\", but it is not allowed in this program as a pedogogical
precondition \"... You may use a uniformly distributed floating-point, [0, 1)
pseudo-random number generator such as Unix 'rand.'"
  [mac] (->> (rand)
             (* mac)
             int))

(defn sample-run-length-array
  "Produce n samples of the vector of outcome-frequency pairs by
sampling an auxiliary array of outcomes that contains numbers of copies
of each outcome proportional to the frequencies in the given
distribution; this solution is O(S)-space, where S is the sum of all the
frequencies, and O(1)-time."
  [n frqs-]
  (let [aux (vec (mapcat (fn [[outcome frequency]]
                           (repeat frequency outcome))
                         frqs-))
        s   (count aux)]
    (repeatedly n (fn [] (aux (rand-int2 s))))))

(defn sample-logarithmically
  "Produce n samples of the outcome-frequency vector by binary
searching the cumulative distribution function; this is O(N)-space,
O(log N)-time. "
  [n frqs-]
  (let [frqs (filter (comp pos? frequency) frqs-)
        cdf  (vec (reductions (fn [results [outcome freq]]
                                [outcome
                                 (+ (frequency results) freq)])
                              (first frqs)
                              (rest frqs)))
        sum  (total frqs)
        len  (count frqs)
        mid  (fn [lo hi] (int (/ (+ lo hi) 2)))]
    (map (fn [target]
           (loop [l 0, h (dec len)]
             (let [i (mid l h), v (frequency (cdf i)), o (outcome (cdf i))]
               (if (>= l h) o
                   (cond

                    (>= target v)
                    (let [nx (cdf (inc i)), nv (frequency nx), no (outcome   nx)]
                      (if (< target nv) no
                          (recur (inc i) h)))

                    (< target v)
                    (recur l i))))))
         (repeatedly n (fn [] (rand-int2 sum))))))

;;; Here is a very short solution that linearly searches the cumulative
;;; probability distribution implied by the given probability
;;; distribution function.

(defn sample-linearly
  "Produce n samples of the outcome-frequency vector by linear
searching; this is O(N)-space, O(N)-time. "
  [n frqs-]
  (let [frqs (filter (comp pos? frequency) frqs-), sum  (total frqs)]
    (map (fn [target]
           (loop [candidates frqs
                  so-far (frequency (first candidates))]
             (if (< target so-far)
               (outcome (first candidates))
               ;; The reason that this is guaranteed never to run off
               ;; the end of the input is a little subtle and worth some
               ;; thought to prove to yourself. The fact that we don't
               ;; check is not a matter of sloppy coding, it's a matter
               ;; of a provable fact.
               (recur (rest candidates)
                      ;; An alternative implementation is to subtract
                      ;; the current frequency from the target.
                      (+ so-far (frequency (second candidates)))))))
         (repeatedly n (fn [] (rand-int2 sum))))))


;;; The following presents an O(N)-space, O(1)-time solution using
;;; Walker's "Method of Aliases."  I know of no better solution. It
;;; requires significant preprocessing, which redistributes the counts
;;; such that each new bin contains no more than two outcomes; the total
;;; number of outcomes in each new bin is the same for all bins; each
;;; new bin contains at least one count of its original "home"
;;; outcome. To redistribute the counts, they must be proportionally
;;; increased so that the new total is disvisible both by N, so that the
;;; new bins will all contain the same per-bin totals; and by S, the sum
;;; of the original counts, so that that the new total count is a
;;; multiple of the old total count and proportions are preserved
;;; exactly. The first step in redistribution is to multiply each
;;; original frequency by lcm(N,S)/S. Then sort the bins, fill the
;;; shortest from the tallest (this is always possible), remove the
;;; newly filled bin from the process, and repeat until all bins are
;;; filled to equal heights with no more than two colors.

(defn N
  "The number of outcomes in a distribution."
  [frqs] (count frqs))

(defn S
  "The total of the frequencies in a distribution."
  [frqs] (total frqs))

(defn L
  "A new total of augmented frequencies after augmentation."
  [frqs] (mathEx/lcm (N frqs) (S frqs)))

(defn H
  "The amount in each bin after augmentation and redistribution."
  [frqs] (/ (L frqs) (N frqs)))

(defn augmentation-factor
  "The factor by which to increase each frequency prior to
redistribution."
  [frqs] (/ (L frqs) (S frqs)))

(defn augmented
  "The augmented frequencies after augmentation and prior to
redistribution."
  [frqs] (map #(vector
                (outcome %)
                (* (frequency %) (augmentation-factor frqs)))
              frqs))

;;; Preprocessing

(defn fill-shortest
  "Given a target height, a hashmap of :filled and :remaining bins, and
a vector of outcome-frequency pairs, produce a new hashmap of :filled
and :remaining bins. The :filled item of that hashmap should be a vector
of hashmaps, each specifying :home and :other, the two outcomes in the
bin after redistribution."
  [target filled frqs]
  (let [sorted   (sort-by frequency frqs)
        tallest  (last    sorted)
        shortest (first   sorted)
        deficit  (- target (frequency shortest))
        to-do    (drop 1 sorted)]
    {:filled
     (conj filled {:home shortest, :other [(outcome tallest) deficit]})
     :remaining
     (if (empty? to-do)
       to-do
       (conj (drop-last to-do)
             [(outcome tallest) (- (frequency tallest) deficit)]))}))

(defn redistribute
  "Given a target height for each bin and a vector of beginning
outcome-frequency pairs, produce a vector of redistributed frequencies,
where each new bin is a hash map of the new :home outcome-frequency pair
and the new frequency of the :other outcome-frequency pair. "
  [target frqs]                     ; TODO: precondition frqs not empty?
  (loop [result (fill-shortest target [] frqs)]
    (if (empty? (:remaining result))
      (:filled result)
      (recur (fill-shortest target
                            (:filled result)
                            (:remaining result))))))

(defn sample-
  "Helper function that finds either the :home outcome or the :other
outcome from a sequence of redistributed frequencies."
  [target redistributed]
  (let [bucket (rand-nth redistributed)
        height (rand-int target)]
    (if (< height (frequency (:home bucket)))
      (outcome (:home bucket))
      (outcome (:other bucket))
      )))

(defn sample-walker
  "Takes n random samples from a hashmap of outcomes and frequencies;
preprocessing aside, this is O(N)-space, O(1)-time."
  [n outcome-freqency]
  (let [redistributed (redistribute
                       (H outcome-freqency)
                       (augmented outcome-freqency))
        ansatz (first redistributed)
        target (+ (frequency (:home  ansatz))
                  (frequency (:other ansatz)))]
    (map (fn [_] (sample- target redistributed))
         (range n))))

;;; The following is no substitute for unit tests, but it produces a
;;; nice printed result of an experiment.

(def loaded-die [[:A 37] [:B 0] [:C 17] [:D 5] [:E 12] [:F 11] [:G 44]])
(def loaded-die
  (map (fn [c] [((comp keyword str) c)
               (rand-int 5200)])
       (seq "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz")))

(defmacro mytime [sampler-fn]
  `(let [start# (. java.lang.System (clojure.core/nanoTime))
         result# (frequencies (~sampler-fn (* 20 (S loaded-die)) loaded-die))
         end# (. java.lang.System (clojure.core/nanoTime))]
     {(keyword '~sampler-fn) (take 7 (sort-by frequency > result#))
      :time (str "Elapsed time: " (/ (- end# start#) 1000000.0) " msec.")}))

(defn -main [] (pprint
                (let [redis (augmented loaded-die)
                      h     (H loaded-die)
                      n     (N loaded-die)
                      s     (S loaded-die)
                      l     (L loaded-die)]
                  #_{ "original distribution" loaded-die
                   ,"original count"  n
                   ,"original total"  s
                   ,"gcd count total" (mathEx/gcd n s)
                   ,"lcm count total" l
                   ,"target height:"  h
                   ,"target total:"   (* h n)
                   ,"augmented heights" redis
                   ,"total augmented heights" (total redis)
                   ,"tallest and shortest" (fill-shortest h [] redis)
                   ,"redistributed" (redistribute h redis)
                   ,"sample-walker" (mytime sample-walker)
                   ,"sample-linearly" (mytime sample-linearly)
                   ,"sample-logarithmically" (mytime sample-logarithmically)
                   ,"sample-run-length-array" (mytime sample-run-length-array)
                   }
                  [(mytime sample-walker)
                   (mytime sample-linearly)
                   (mytime sample-logarithmically)
                    (mytime sample-run-length-array)])))
