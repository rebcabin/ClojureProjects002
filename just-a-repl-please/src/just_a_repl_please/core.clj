(ns just-a-repl-please.core)

(defmacro pdump
  "Monitoring and debugging macro with semantics of 'identity'."
  [x]
  `(let [x# (try ~x (catch Exception e# (str e#)))]
     (do
       (println "================================================================")
       (clojure.pprint/pprint '~x)
       (println " ~~> ")
       (clojure.pprint/pprint x#)
       x#)))

(defn gb [f xs]
  (reduce (fn [m i] (into m
                          (let [fi (f i)]
                            [[fi (-> fi m (or []) (conj i))]])
                          )) {} xs))

(map d [{} #{} [] ()])

(= [:map :set :vector :list] (map distinguish [{} #{} [] ()]))

(map count [{} #{} [] ()])

(defn d [c] (let [k (keyword (gensym))
                  v1 (gensym)
                  v2 (gensym)
                  p1 [k v1]
                  p2 [k v2]
                  t (conj c p1 p2)
                  f (first t)
                  l (last t)]
              (cond
                (= (count t) (+ 1 (count c))) :map
                (= t (conj c p2 p1)) :set
                (= p2 f) :list
                (= p2 l) :vector)))

(pdump (d {:a 1, :b 2}))
(pdump (d (range (rand-int 20))))
(pdump (d [1 2 3 4 5 6]))
(pdump (d #{10 (rand-int 5)}))
(pdump (= [:map :set :vector :list] (map d [{} #{} [] ()])))

(fn [c] (let [k (keyword (gensym))
              v1 (gensym)
              v2 (gensym)
              p1 [k v1]
              p2 [k v2]
              t (conj c p1 p2)
              f (first t)
              l (last t)]
          (cond
            (= (count t) (+ 1 (count c))) :map
            (= t (conj c p2 p1)) :set
            (= p2 f) :list
            (= p2 l) :vector)))
