(ns problem-153.core
  (:require [funcyard.core :refer :all]))

(def vss [#{#{\U} #{\s} #{\e \R \E} #{\P \L} #{\.}}
          #{#{:a :b :c :d :e}
            #{:a :b :c :d}
            #{:a :b :c}
            #{:a :b}
            #{:a}}
          #{#{[1 2 3] [4 5]}
            #{[1 2] [3 4 5]}
            #{[1] [2] 3 4 5}
            #{1 2 [3 4] [5]}}
          #{#{'a 'b}
            #{'c 'd 'e}
            #{'f 'g 'h 'i}
            #{''a ''c ''f}}
          #{#{'(:x :y :z) '(:x :y) '(:z) '()}
            #{#{:x :y :z} #{:x :y} #{:z} #{}}
            #{'[:x :y :z] [:x :y] [:z] [] {}}}
          #{#{(= "true") false}
            #{:yes :no}
            #{(class 1) 0}
            #{(symbol "true") 'false}
            #{(keyword "yes") ::no}
            #{(class '1) (int \0)}}
          #{#{distinct?}
            #{#(-> %) #(-> %)}
            #{#(-> %) #(-> %) #(-> %)}
            #{#(-> %) #(-> %) #(-> %)}}
          #{#{(#(-> *)) + (quote mapcat) #_ nil}
            #{'+ '* mapcat (comment mapcat)}
            #{(do) set contains? nil?}
            #{, , , #_, , empty?}}] )

(map #(apply distinct? (mapcat identity %)) vss)

(def shortest-path
  (letfn [(d [n] (* n 2))               ; double
          (h [n] (/ n 2))               ; halve
          (a [n] (+ n 2))               ; add
          (hop-count [n ts hops]
            (cond
              (ts n)      hops          ; end search
              :else
              (hop-count
               n
               (set (concat
                     ts
                     (map d ts)
                     (map a ts)
                     (map h ts)))
               (inc hops))))]
    (fn [s t] (hop-count t #{s} 1))))

(shortest-path 9 12)

;;               _    _             _ ___  ___
;;  _ __ _ _ ___| |__| |___ _ __   / | __|( _ )
;; | '_ \ '_/ _ \ '_ \ / -_) '  \  | |__ \/ _ \
;; | .__/_| \___/_.__/_\___|_|_|_| |_|___/\___/
;; |_|

(((fn [curried]
    (letfn [(decurried [& args]
              (reduce #(%1 %2) curried args))]
      decurried))
  (fn [a]
    (fn [b]
      (fn [c]
        (fn [d]
          (+ a b c d))))))
 1 2 3 4)

;;               _    _             ____ __
;;  _ __ _ _ ___| |__| |___ _ __   |__  /  \
;; | '_ \ '_/ _ \ '_ \ / -_) '  \    / / () |
;; | .__/_| \___/_.__/_\___|_|_|_|  /_/ \__/
;; |_|

(sort
 #(compare
   (clojure.string/upper-case %1)
   (clojure.string/upper-case %2))
 (clojure.string/split
  (clojure.string/replace
   "Have a nice day." #"[\.\!]" "")
  #"\s"))

(fn [s] (sort
         #(compare (clojure.string/upper-case %1)
                   (clojure.string/upper-case %2))
         (clojure.string/split
          (clojure.string/replace s #"[\.\!]" "") #"\s")))

(re-seq #"\w+" "Have a nice day.")

;;               _    _             ____ ____
;;  _ __ _ _ ___| |__| |___ _ __   |__  |__ /
;; | '_ \ '_/ _ \ '_ \ / -_) '  \    / / |_ \
;; | .__/_| \___/_.__/_\___|_|_|_|  /_/ |___/
;; |_|

(get-in [[:x :e :o]
         [:x :e :e]
         [:x :e :o]] [2 1])

(defn winner [board])

(def b1 [[:e :e :e]
         [:e :e :e]
         [:e :e :e]])

(def b2 [[:x :e :o]
         [:x :e :e]
         [:x :e :o]])

(def b3 [[:e :x :e]
         [:o :o :o]
         [:x :e :x]])

(def b4 [[:x :e :o]
         [:x :x :e]
         [:o :x :o]])

(def b5 [[:x :e :e]
         [:o :x :e]
         [:o :e :x]])

(def b6 [[:x :e :o]
         [:x :o :e]
         [:o :e :x]])

(def b7 [[:x :o :x]
         [:x :o :x]
         [:o :x :o]])

;;             _
;;  _ __  __ _(_)_ _ ___
;; | '_ \/ _` | | '_(_-<
;; | .__/\__,_|_|_| /__/
;; |_|

(defn for-pairs [binary-function lyst]
  (for [left lyst, right lyst]
    (binary-function left right)))

(for-pairs vector ['a 'b 'c])

;;               _    _             ____ ____
;;  _ __ _ _ ___| |__| |___ _ __   |__  |__  |
;; | '_ \ '_/ _ \ '_ \ / -_) '  \    / /  / /
;; | .__/_| \___/_.__/_\___|_|_|_|  /_/  /_/
;; |_|

(letfn [(str-permutations [l]
          (if (let [c (count l)] (or (= c 0) (= c 1))),
            (seq [l]),
            (let [splits (map #(split-at % l) (range (count l)))
                  pivots (map (comp first second) splits)
                  pres   (map first splits)
                  posts  (map (comp rest second) splits)
                  resids (map concat pres posts)
                  subps  (map str-permutations resids)
                  reasm  (mapcat #(map (partial cons %1) %2)
                                 pivots subps)]
              (map clojure.string/join reasm)
              )))
        (combinations [xs m]
          (cond
            (= m 0) (list ())
            (empty? (seq xs)) ()
            :else (let [x (first xs)
                        xs (rest xs)]
                    (concat
                     (map #(cons x %) (combinations xs (- m 1)))
                     (combinations xs m)))))]
  (set (filter
        (comp not nil?)
        (map
         (fn [[left right]]
           (cond
             (contains? (set (str-permutations left)) right) right
             (contains? (set (str-permutations right)) left) left
             :else nil))
         (combinations ["meat" "mat" "team" "mate" "eat"] 2)))))

;;  _        _ _
;; | |_ __ _| | |_  _
;; |  _/ _` | | | || |
;;  \__\__,_|_|_|\_, |
;;               |__/

(defn inc-from-nil [v]
  (if (nil? v), 1, (inc v)))

(defn find-winner [board]
  (letfn [(k-conj [m [k item]]
            (into m {k (conj (k m) item)})),
          (inc-from-nil [v]
            (if (nil? v), 1, (inc v))),
          (tally [items]
            (reduce
             (fn [talmap item]
               (into talmap
                     {item (inc-from-nil (talmap item))}))
             {} items)),
          (row-tally [coords] (tally (map first coords))),
          (col-tally [coords] (tally (map second coords))),
          (diagonal-tally [coords]
            (tally (map (partial apply -) coords))),
          (anti-diag-tally [coords]
            (tally (map (partial apply +) coords))),
          (tallies [coords]
            {:rt (row-tally coords)
             :ct (col-tally coords)
             :dt (diagonal-tally coords)
             :at (anti-diag-tally coords)}),
          (exists-a-3 [[k tally-maps]]
            {k (some
                identity
                (map (fn [[shape tally-map]]
                       (some (fn [[k v]] (= 3 v))
                             tally-map))
                     tally-maps))})
          (key-if-true [player-map]
            (let [player (first (keys player-map)),
                  won? (first (vals player-map))]
              (println player)
              (println won?)
              (when (and (not (= :e player)) won?)
                player)))]
    (let [cmap
          (reduce k-conj {:x [], :o [], :e []}
                  (for [r (range 3) c (range 3)]
                    (let [coords [r c]
                          k (get-in board coords)]
                      [k coords]))),
          board-tally-maps
          (into {} (map
                    (fn [[k coords]] {k (tallies coords)})
                    cmap))]
      (println board-tally-maps)
      (println (map exists-a-3 board-tally-maps))
      (first (drop-while
              nil?
              (map key-if-true
                   (map exists-a-3 board-tally-maps))))
      )))

(get-in b5 [2 2])

(fn [board]
  (let [;transpose (apply map list board)
        diag [(map #(get-in board %)
                   [[0 0] [1 1] [2 2]])]
        anti-diag [(map #(get-in board %)
                        [[0 2] [1 1] [2 0]])]]
    (some {[:x :x :x] :x, [:o :o :o] :o}
          (concat board
                  transpose
                  diag
                  anti-diag))))

(map find-winner (list b1 b2 b3 b4 b5 b6 b7))

;;               _    _             ____ __
;;  _ __ _ _ ___| |__| |___ _ __   |__  / /
;; | '_ \ '_/ _ \ '_ \ / -_) '  \    / / _ \
;; | .__/_| \___/_.__/_\___|_|_|_|  /_/\___/
;; |_|

(defn foo [a & xs]
  (if (> a 10), [a xs],
      (recur (inc a) nil)))
(foo 1)

(defn trampoline [f & args]
  (let [r (apply f args)]
    (if (fn? r), (recur r nil), r,)))

(letfn [(foo [x y] #(bar (conj x y) y))
        (bar [x y] (if (> (last x) 10), x,
                       #(foo x (+ 2 y))))]
  (trampoline foo [] 1))

(defn mainx []
  (println (map vec (combinations '(a b c d) 2)))
  (println (count (permutations '(a b c d))))
  (println (string-permutations "meat"))
)

;;   ___             _ ___ _
;;  / __|__ _ _ _ __| / __(_)_ __
;; | (__/ _` | '_/ _` \__ \ | '  \
;;  \___\__,_|_| \__,_|___/_|_|_|_|

(def pips {:A  1, :2  2, :3  3, :4 4, :5 5,
           :6  6, :7  7, :8  8, :9 9,
           :T 10, :J 11, :Q 12, :K 0})

(def suits #{:S :H :D :C})

(def sequential-mod? [pips])

