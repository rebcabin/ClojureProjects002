(ns die-harder.core
  (:require [clojure.pprint]
            [taoensso.timbre :as timbre :exclude [assert]]))

(timbre/refer-timbre) ; Provides useful Timbre aliases in this ns

(defmacro pdump
  "Monitoring and debugging macro with semantics of 'identity'."
  [x]
  `(let [x# (try ~x (catch Exception e# (str e#)))]
     (do (println "----------------")
         (clojure.pprint/pprint '~x)
         (println "~~>")
         (clojure.pprint/pprint x#)
         x#)))

(defnp except
  "Throws an Exception with the given string message."
  [s]
   (-> s Exception. throw))

(defn- or-default
  "Fetch first optional value from function arguments preceded by &."
  [val default] (if val (first val) default))

(defnp ^:private
  assert
  "Throws an exception if the given Boolean is false."
  [bool & msg-]
  (when-not bool (except (or-default msg- ""))))

(defnp ^:private
  assert-not
  "Throws an exception if the given Boolean is true."
  [bool & msg-]
  (when bool (except (or-default msg- ""))))

(defnp get-jug
  "Retrieves the i-th jug state from a vector of jugs, checking
invariants along the way. "
  [jugs i]
  (let [mj (jugs i)]
    (assert (== i (:id mj))
            (str "data corrupted: " mj " should have id " i "."))
    (let [c (:capacity mj)
          a (:amount   mj)]
      (assert-not (< a 0) (str "amount negative: " mj "."))
      (assert-not (> a c) (str "amount greater than capacity: " mj ".")))
    mj))


(defnp make-jugs
  "Makes a vector of jug states given a vector of integer
capacities. Each JUG STATE is a map of an id, integer capacity, and
integer amount, which must be non-negative and less than or equal to
capacity: {:id 1, :capacity 5, :amount 0}"
  [capacities]
  (->>
   capacities
   (map-indexed (fn [i c] {:id i :capacity c :amount 0}))
   vec))

(defnp fill-jug
  "Fills the i-th jug state in a vector of jug states to capacity,
irrespective of current amount."
  [jugs i]
  (let [mj (get-jug jugs i)]
    (assoc jugs i
           (->> (:capacity mj)
                (assoc mj :amount)))))

(defnp spill-jug
  "Spills the i-th jug state of a vector of jug states, reducing its
current amount to 0."
  [jugs i]
  (let [mj (get-jug jugs i)]
    (assoc jugs i
           (->> 0
                (assoc mj :amount)))))

(defnp pour-from
  "Pours a quantity into the i-th jug state in a vector of jug states
from another, distinct j-th jug state. The quantity may fill the i-th
jug or empty the j-th jug, or both."
  [jugs i j]
  (assert-not (== i j)
              (str "cannot pour from a jug into itself: " i "."))
  (let [jug-i            (get-jug   jugs i)
        jug-j            (get-jug   jugs j)
        jug-i-amount     (:amount   jug-i )
        jug-j-amount     (:amount   jug-j )
        jug-i-capacity   (:capacity jug-i )
        available-source jug-j-amount
        available-space  (- jug-i-capacity jug-i-amount)
        amount-to-pour   (min available-space available-source)]
    (-> jugs
        (assoc i (->> (+ jug-i-amount amount-to-pour)
                      (assoc jug-i :amount)))
        (assoc j (->> (- jug-j-amount amount-to-pour)
                      (assoc jug-j :amount))))))

(defnp range-excluding
  "Produces a sequence of the integers from 0 through n-1, excluding i."
  [n i]
  (->> (range n)
       (filter #(not= i %))))

(defnp gen-fill
  "Generates a fill instruction for jug i."
  [i]   `(fill-jug  ~i))

(defnp gen-spill
  "Generates a spill instruction for jug i."
  [i]   `(spill-jug ~i))

(defnp gen-pours
  "Generates all legal pour instructions into jug i from other jugs in a
vector of jug states of length n."
  [n i] (map (fn [j] `(pour-from ~i ~j))
             (range-excluding n i)))

(defnp all-moves
  "Generates a squences of all moves, excluding repeats of the last
instruction given."
  [jugs last-move]
  (let [n   (count jugs)
        all (range n   )]
    (filter
     #(not= % last-move)
     (concat (map gen-fill  all)
             (map gen-spill all)
             (mapcat #(gen-pours n %) all)
             ))))

(defnp detect-win
  "Determines whether a vector of jug states satisfies the required
target amount in-toto."
  [jugs target]
  (== target
      (apply + (map :amount jugs))))

(defnp execute-move [jugs move]
  (eval `(-> ~jugs ~move)))

(defnp filter-trivial-moves
  "Given \"states,\" a vector of jugs states; and \"moves,\" a sequence
of instructions to fill, spill, or pour, filters trivial moves out. A
move is trivial when the source of a pour is empty, when spilling an
empty jug, or filling a full jug."
  [state moves]
  (filter
   (fn [move]
     (let [instruction (first move)]
       (condp = instruction
         'die-harder.core/pour-from
         (let [source-j   (nth move 2)
               source-amt (:amount (state source-j))]
           (not (== 0 source-amt)))

         'die-harder.core/fill-jug
         (let [i (nth move 1)
               a (:amount   (state i))
               c (:capacity (state i))]
           (not (== a c)))

         'die-harder.core/spill-jug
         (let [i (nth move 1)
               a (:amount   (state i))]
           (not (== 0 a)))
         )))
   moves))

(defnp try-moves
  [states moves target seen iters max-iters]
  (if (or (not moves) (> iters max-iters)) nil
      (let [trials
            (->> moves
                 (map (fn [move] {:states (execute-move (:states states) move)
                                 :trace  (conj (:trace states) move)}))
                 (filter #(not (contains? seen (:states %)))))
            wins (filter #(detect-win (:states %) target) trials)
            ]
        (if (not (empty? wins)) wins
            (let [new-seen    (reduce conj seen (map :states trials))
                  last-moves  (map #(-> % :trace peek) trials)
                  k           (count trials)
                  ii          (inc iters)
                  just-states (map :states trials)
                  new-movess  (map all-moves just-states last-moves)
                  ]
              (lazy-seq
               (mapcat try-moves
                       trials
                       new-movess
                       (repeat k target)
                       (repeat k new-seen)
                       (repeat k ii)
                       (repeat k max-iters))))))))

(defnp try-non-trivial-moves
  [states moves target seen iters max-iters]
  (if (or (not moves) (> iters max-iters)) nil ; {:moves moves :iters iters}
      (let [trials
            (->> moves
                 (map (fn [move] {:states (execute-move (:states states) move)
                                 :trace  (conj (:trace states) move)}))
                 (filter #(not (contains? seen (:states %)))))
            wins (filter #(detect-win (:states %) target) trials)
            ]
        (if (not (empty? wins)) wins
            (let [new-seen    (reduce conj seen (map :states trials))
                  last-moves  (map #(-> % :trace peek) trials)
                  k           (count trials)
                  ii          (inc iters)
                  just-states (map :states trials)
                  new-movess  (map all-moves just-states last-moves)
                  non-trivial-movess
                              (map filter-trivial-moves just-states new-movess)
                  ]
              ;; (if (not (= (map count new-movess) (map count non-trivial-movess)))
              ;;   (println {:saved (- (apply + (map count new-movess))
              ;;                       (apply + (map count non-trivial-movess)))}))
              (lazy-seq
               (mapcat try-non-trivial-moves
                       trials
                       non-trivial-movess
                       (repeat k target)
                       (repeat k new-seen)
                       (repeat k ii)
                       (repeat k max-iters))))))))

(defnp divides? [a b] (== 0 (mod b a)))

(defn integer-power [a b]
  (assert (and (pos? a) (not (neg? b))))
  (letfn [(helper [a b n]
            (if (zero? b) n (recur a (dec b) (* n a))))]
    (helper a b 1)))

(defn gcd
  ([a b]
     (assert (and (not= 0 a) (not= 0 b)))
     (if (> a b)
       (recur b a)
       (if (divides? a b)
         a
         (recur (rem b a) a))))
  ([a b & more] (reduce gcd a (cons b more))))

(defnp play-game [capacities target & strategy-]
  (if (or (> target (apply + capacities))
          (not (divides? (apply gcd capacities) target))) '()
      (let [strategy (or-default strategy- try-moves)]
        (strategy
         {:states (make-jugs capacities), :trace []}
         ['(die-harder.core/fill-jug 0)]
         target
         #{}, 1, 10
         ))))

;;; Mutable-Ref variation (discouraged, but may be necessary due to perf)

(defn make-jug-refs  [capacities]
  (->>
   capacities
   (map-indexed (fn [i c] {:id i :capacity c :amount 0}))
   (map ref)
   vec))

(defn get-jug-ref  [jugs i]
  (let [mj (jugs i)]
    (assert (== i (:id  @mj))
            (str "data corrupted: " @mj " should have id " i "."))
    mj))

(defn fill-jug-ref  [jugs i]
  (let [mj (get-jug-ref jugs i)]
    (dosync (ref-set mj (assoc @mj :amount (:capacity @mj))))))

(defn set-jug-ref-attribute  [jugs i attr new-value]
  (let [mj (get-jug-ref jugs i)]
    (dosync (ref-set mj (assoc @mj attr new-value))))  )

(defn spill-jug-ref  [jugs i]
  (set-jug-ref-attribute   jugs i :amount 0))

(defn get-jug-ref-attribute  [jugs i attr]
  (-> jugs (get-jug-ref i) deref attr))

(defn pour-from-ref  [jugs i other]
  (dosync
   (let [this             (get-jug-ref jugs i                  )
         that             (get-jug-ref jugs other              )
         this-amount      (:amount   @this                     )
         that-amount      (:amount   @that                     )
         this-capacity    (:capacity @this                     )
         available-source that-amount
         available-space  (- this-capacity this-amount         )
         amount-to-pour   (min available-space available-source)]
     (ref-set this (assoc @this :amount (+ this-amount amount-to-pour)))
     (ref-set that (assoc @that :amount (- that-amount amount-to-pour))))))
