
;;;; This is Knuth's Algorithm S from *The Art of Computer Programming*, vol.
;;;; 2.

(ns cleaning-data.sampling)

(defn sample-percent
  "This samples n percent of the input. n should be between 0 and 1.

  The actual size of the output is an estimate of the size of the input."
  ([k coll]
   (filter (fn [_] (<= (rand) k)) coll)))

(defn rand-replace
  "This removes a random key in the map and inserts a new
  key-value."
  ([m [k v]]
   (assoc
     (dissoc m (rand-nth (keys m)))
     k v)))

(defn range-from
  [x] (map (partial + x) (range)))

(defn sample-amount
  "This samples x items from the input.

  This first takes the entire x items and adds them to the sample.
  From that point on, the change that each item has the chance of
  replacing an item from the set is x/K, where K is the number of
  items seen so far. "
  [k coll]
  (->> coll
    (drop k)
    (map vector (range-from (inc k)))
    (filter #(<= (rand) (/ k (first %))))
    (reduce rand-replace (into {} (map vector (range k) (take k coll))))
    (sort-by first)
    (map second)))

