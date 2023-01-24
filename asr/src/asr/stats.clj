(ns asr.stats
  (:use
   [asr.numbers]
   [asr.expr.semsem])

  (:require [asr.utils                :refer [echo] ]
            [clojure.spec.alpha       :as    s      ]
            [clojure.spec.gen.alpha   :as    gen    ]
            [clojure.string           :as    string ]
            [clojure.math             :as    math   ]))


(def sample [[140 190 1 8]
             5
             [140 150 1]
             [150 160 0]
             [160 170 7]
             [170 180 6]
             [180 190 2]])


(def challenge [[0 50 1 10]
                5
                [0 10 1]
                [10 20 3]
                [20 30 6]
                [30 40 4]
                [40 50 2]])


(defn chart-out
  [[axes records & [[fval sval] :as cols]]]
  (defn lpad
    "Add spaces to string `st` on the left if it's smaller than `size`
  chars."
    [st size]
    (let [st* (str st), len (count st*)]
      (if (<= len size)
        (string/join (concat (repeat (- size len) \space) [st*])))))
  (defn pad-val
    "Return a tuple of the length of `st` and an all-space string of
  equal length."
    [st]
    (let [st* (str st), len (count st*)]
      [len (string/join (repeat len \space))]))
  (let [cume (atom [])
        [min-x max-x, min-y max-y] axes
        [x-pad x-spc, y-pad y-spc] (mapcat pad-val [max-x max-y])
        col-margin    (- sval fval)
        col-values    (map last cols)]
    (doseq [idx (range max-y (dec min-y) -1)
            :let [row (map (fn [n] (if (>= n idx) "@" ".")) col-values)
                  lbl (lpad idx y-pad)]]
      (swap! cume
       #(conj %1
         (print-str
          (str lbl x-spc (string/join x-spc row))))))
    (swap! cume
     #(conj %1
       (print-str
        (as-> (range min-x (inc max-x) col-margin) st
          (map lpad st (repeat x-pad))
          (string/join \space st)
          (str y-spc st)))))
    @cume))


;;(chart-out challenge)
;; => ["10  .  .  .  .  ."
;;     " 9  .  .  .  .  ."
;;     " 8  .  .  .  .  ."
;;     " 7  .  .  .  .  ."
;;     " 6  .  .  @  .  ."
;;     " 5  .  .  @  .  ."
;;     " 4  .  .  @  @  ."
;;     " 3  .  @  @  @  ."
;;     " 2  .  @  @  @  @"
;;     " 1  @  @  @  @  @"
;;     "   0 10 20 30 40 50"]


(let [log10_ (math/log 10)]
  (defn log10   [x] (/ (math/log      x)  log10_))
  (defn log10+1 [x] (/ (math/log (inc x)) log10_)))


(defn sample-size-distribution [SAMPLE-SIZE]
  (let [foo (->> (-> (s/gen :asr.core/i32-bin-op-semsem)
                     (gen/sample SAMPLE-SIZE))
                 (map i32-bin-op-semsem-leaf-count)
                 (frequencies)
                 (sort-by first))
        food (apply hash-map (mapcat identity foo))
        n (first (first foo))
        x (first (last  foo))]
    (for [q (range n (inc x) 2)]
      [q (or (food q) 0)])))


(defn lo-cliff [x n]
  (int (* n (math/floor (/ x n)))))


(defn hi-cliff [x n]
  (int (* n (math/ceil (/ x n)))))


(def DX  10)
(def DY  10)


(defn scale-log-sum-row [[lo pairs]]
  [lo
   (->> (apply + (map second pairs))
        log10+1
        (* DY)
        int)])


(defn re-pair-x [pairs]
  (let [ns (->> pairs (sort-by first)             #_echo)
        os (->> ns (map first)                    #_echo)
        ps (-> (->> os (drop 1) vec)
               (conj (+ DX (first (last ns))))    #_echo)
        qs (->> ns (map second)                   #_echo)]
    (partition 3 (interleave os, ps, qs))))


#_
(binding [s/*recursion-limit* 4]
  (let [data (->  (sample-size-distribution 100)           #_echo)
        lx   (->> data (map first)  (apply min)            #_echo)
        hx   (->> data (map first)  (apply max)            #_echo)
        ly   (->> data (map second) (apply min) log10+1    #_echo)
        hy   (->> data (map second) (apply max) log10+1    #_echo)
        ilx  (->  lx (lo-cliff  DX)                        #_echo)
        ihx  (->  hx (hi-cliff  DX)                        #_echo)
        ily  (->  0                                        #_echo)
        ihy  (->  (* DY hy) (hi-cliff  DY)                 #_echo)
        axs  (->  (range ilx (inc ihx) DX)                 #_echo)
        gs   (->> data (group-by #(lo-cliff (first %) DX)) echo)
        hs   (->> gs (map scale-log-sum-row)               #_echo)
        is   (->  hs re-pair-x                             #_echo)
        cht  (->> is (concat (list [ilx ihx, ily ihy]
                                   (count gs)))            #_echo)]
    (chart-out cht)))
;; => ["20   .   .   .   .   .   .   .   .   .   .   .   ."
;;     "19   @   .   .   .   .   .   .   .   .   .   .   ."
;;     "18   @   .   .   .   .   .   .   .   .   .   .   ."
;;     "17   @   .   .   .   .   .   .   .   .   .   .   ."
;;     "16   @   .   .   .   .   .   .   .   .   .   .   ."
;;     "15   @   .   .   .   .   .   .   .   .   .   .   ."
;;     "14   @   .   .   .   .   .   .   .   .   .   .   ."
;;     "13   @   .   .   .   .   .   .   .   .   .   .   ."
;;     "12   @   .   .   .   .   .   .   .   .   .   .   ."
;;     "11   @   @   .   .   .   .   .   .   .   .   .   ."
;;     "10   @   @   .   .   .   .   .   .   .   .   .   ."
;;     " 9   @   @   .   .   .   .   .   .   .   .   .   ."
;;     " 8   @   @   .   .   .   .   .   .   .   .   .   ."
;;     " 7   @   @   .   .   .   .   .   .   .   .   .   ."
;;     " 6   @   @   @   .   .   .   .   .   .   .   .   ."
;;     " 5   @   @   @   .   .   .   .   .   .   .   .   ."
;;     " 4   @   @   @   .   .   .   .   .   .   .   .   ."
;;     " 3   @   @   @   @   .   @   .   .   .   .   .   @"
;;     " 2   @   @   @   @   .   @   .   .   .   .   .   @"
;;     " 1   @   @   @   @   .   @   .   .   .   .   .   @"
;;     " 0   @   @   @   @   @   @   @   @   @   @   @   @"
;;     "    0  10  20  30  40  50  60  70  80  90 100 110 120"]
