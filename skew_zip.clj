;;; for light-table rather than for leiningen -- --

(hash-map :a 1 :b 2)
(into {} {:a 1, :b 2})
(conj {} [:a 1] [:b 2])
(vec '(1 2 3))
(vec (range 65 75))
(vector 1 2 3)
(apply vector [1 2 3])
(merge [1 2 3] [4 5 6])
([1 2 3] 0)
(assoc [1 2 3] 1 42)
(replace [:zeroth :first :second :third :fourth] [0 2 4 0])

(defn map-n-m
  [n m]
  (map
   #(hash-map :id % :data (mod % 10))
   (range n m)))

(map-n-m 65 75)

(map shuffle
     (partition 3 1 (range 65 75)))

(subvec (vec (range 65 75)) 1 4)

(try (throw (IllegalArgumentException. "blah"))
  (catch IllegalArgumentException e (str "got exception: " (.getMessage e))))

(for [start (range 0 (inc (- 5 3)))] start)

(defn parts [v n]
  {:pre [(<= n (count v))]}
  (let [c (count v)]
    (for [start (range 0 (inc (- c n)))]
      (subvec (vec v) start (+ start n)))))

(parts (range 0 5) 3)

(defn part [v n start]
  {:pre [(<= n (count v))
         (<= start (- (count v) n))]}
    (subvec (vec v) start (+ start n)))

(part (range 65 75) 3 7)

(defn replace-part [v- part- start]
  (let [part (vec part-)
        v    (vec v-)]
  {:pre [(<= (count part) (count v))
         (<= start (- (count v) (count part)))]}
  (let [n (count part)
        c (count v)
        m (+ start n)]
    (map-indexed
     (fn [i x]
       (if (and (>= i start) (< i m))
         (part (- i start))
         (v i)))
     v))))

(replace-part
 (range 65 75)
 ['a 'b 'c]
 0)

(defn swizzle-part [v n start]
  (replace-part
   (vec v)
   (shuffle (part v n start))
   start
   ))

(swizzle-part
 (range 65 75)
 3
 0)

(defn swizzle [v n]
  {:pre (<= n (count v))}
  (let [c (count v)]
    (reduce #(swizzle-part %1 n %2)
            v
           (if (= 0 (- c n))
             '(0)
             (range 0 (- c n))))))

(swizzle (range 65 75) 3)

(range 0 1)

(str (java.util.UUID/randomUUID))
(#(str (java.util.UUID/randomUUID)))
