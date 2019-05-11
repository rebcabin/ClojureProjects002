
(ns cleaning-data.rescaling)

;; rescale by a total of everything
(defn rescale-by-total
  "This resizes the values of a key (src) by the total for that key. The values
  are stored in the key dest."
  ([src dest] (fn [coll] (rescale-by-total src dest coll)))
  ([src dest coll]
   (let [total (reduce + (map src coll))
         update (fn [m]
                  (assoc m dest (/ (m src) total)))]
     (map update coll))))

;; rescale by the total of a group
(defn rescale-by-group
  "This resizes the values of a key (src) by the total of that key, after the
  data has been grouped by the key group. The results are stored in the key
  dest."
  ([src group dest] (fn [coll] (rescale-by-group src group dest coll)))
  ([src group dest coll]
   (mapcat (rescale-by-total src dest)
           (vals (group-by group (sort-by group coll))))))

;; rescale by another column
(defn rescale-by-key
  "This resizes the values of a key (src) by another key (by). The results are
  stored in the key dest."
  ([src by dest] (fn [coll] (rescale-by-key src by dest coll)))
  ([src by dest coll]
   (let [update (fn [m] (assoc m dest (/ (m src) (m by))))]
     (map update coll))))

