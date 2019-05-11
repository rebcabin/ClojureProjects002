(ns command-line-args.core
  (:use clojure.algo.monads))

(defn -main
  "I am the canonical ideal."
  [& args]
  (println "Hello, World!")
  (println args))

(defn parse-args [args]
  (into {} (map (fn [[k v]]
                  [(keyword (.replace k "--" "")) v])
                (partition 2 args))))

(defn keywordize [kvp]
  (let [[k v] kvp]
    [(keyword (.replace k "--" ""))
     v]))

;(println "boo")
;(println (keywordize ["a" 1 "b" 2]))

(def ^:dynamic v 1) ; v is global and dynamic

(defn f1 []
  (println "f1: v: " v))

(defn f2 []
  (println "f2: before let v: " v)
  (let [v 2]
    (println "f2: in let, v: " v)
    (f1))
  (println "f2: after let v: " v))

(defn f3 []
  (println "f3: before binding v: " v)
  (binding [v 3]
    (println "f3: in binding v: " v)
    (f1))
  (println "f3: after binding v: " v))

(defn f4 []
  (def v 4))

;; (println "(= v 1) ~~> " (= v 1))
;; (f2)
;; (f3)
;; (f4)
;; (println "(= v 4) ~~> " (= v 4))

(defn power [base & exponents]
  (reduce #(Math/pow %1 %2) base exponents))

(def plays [{:band "Burial",     :plays 979,  :loved 9}
            {:band "Eno",        :plays 2333, :loved 15}
            {:band "Bill Evans", :plays 979,  :loved 9}
            {:band "Magma",      :plays 2665, :loved 31}])

(def sort-by-loved-ratio (partial sort-by #(/ (:plays %) (:loved %))))

(defn columns [column-names]
  (fn [row]
    (vec (map row column-names))))

(defn keys-apply [f ks m]
  "Takes a function, a set of keys, and a map and applies the function 
   to the map on the given keys.  A new map of the results of the function 
   applied to the keyed entries is returned."
  (let [only (select-keys m ks)]
    (zipmap (keys only) (map f (vals only)))))

(defn manip-map [f ks m]
  "Takes a function, a set of keys, and a map and applies the function 
   to the map on the given keys.  A modified version of the original map
   is returned with the results of the function applied to each keyed entry."
  (conj m (keys-apply f ks m)))

;;; It appears to me that "halve!" is indeed a pure function if it
;;; only closes over immutable values, and I will test it as such.
(defn halve! [ks]
  (map (partial manip-map #(int (/ % 2)) ks)
       plays))

(defn slope-optional
  ^:harangue
  [& {:keys [p1 p2] :or {p1 [0 0] p2 [1 1]} }]
  (let [dy (- (p2 1) (p1 1))
        dx (- (p2 0) (p1 0))]
    (float (/ dy dx))))

(defn slope
  "Documentation fu."
  ^{:metadata "fu"}
  [& {:keys [p1 p2] :or {p1 [0 0] p2 [1 1]} }]
  {:pre [(vector? p1)
         (vector? p2)
         (= 2 (count p1))
         (= 2 (count p2))
         (not= (p1 0) (p2 0))],
   :post (float? %)}
  (let [dy (- (p2 1) (p1 1))
        dx (- (p2 0) (p1 0))]
    (float (/ dy dx)))  
  )

(defn fib [n]
  (if (< n 2)
    n
    (let [n1 (dec n)
          n2 (dec n1)
          f1 (fib n1)
          f2 (fib n2)]
      (+ f1 f2))))

(with-monad (writer-m [])
  (defn fib-trace [n]
    (if (< n 2)
      (m-result n)
      (domonad
       [n1 (m-result (dec n))
        n2 (m-result (dec n1))
        f1 (fib-trace n1)
        _  (write [n1 f1])
        f2 (fib-trace n2)
        _  (write [n2 f2])
        ]
       (+ f1 f2))))
  )

(with-monad identity-m
  (defn fib-ident [n]
    (if (< n 2)
      (m-result n)
      (domonad
       [n1 (m-result (dec n))
        n2 (m-result (dec n1))
        f1 (fib-ident n1)
        ;;         _  (write [n1 f1])
        f2 (fib-ident n2)
        ;;         _  (write [n2 f2])
        ]
       (+ f1 f2))))
  )

(with-monad maybe-m
  (defn fib-maybe [n]
    (if (< n 0)
      (m-result nil)
      (if (< n 2)
        (m-result n)
        (domonad
         [n1 (m-result (dec n))
          n2 (m-result (dec n1))
          f1 (fib-maybe n1)
          f2 (fib-maybe n2)
          ]
         (+ f1 f2)))))
)

;;; The lift abstraction is obvious.

(def maybe-in-sequence-m (maybe-t sequence-m))