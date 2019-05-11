(ns expt1.core
  (:require clojure.pprint
            [clojure.reflect         :as r ]
            [rx.lang.clojure.interop :as rx])
  (:import  [rx subjects.PublishSubject Observer]))

(defmacro pdump [x]
  `(let [x# (try ~x (catch Exception e# (str e#)))]
     (do (println "----------------")
         (clojure.pprint/pprint '~x)
         (println "~~>")
         (clojure.pprint/pprint x#)
         x#)))

(defn find-re [re obj]
  (pdump
   (filter
    #(re-find re (str %))
    (sort (map :name (:members (r/reflect obj :ancestors true)))))))

(let [o2 (PublishSubject/create)
      obr (reify Observer
           (onNext      [this v] (println "[0] onNext"      v))
           (onError     [this e] (println "[0] onError"     e))
           (onCompleted [this  ] (println "[0] onCompleted"  )))]
  (find-re #"[Ss]ubscribe" o2)
  (pdump (.subscribe o2 (rx/action* println))))

(let [obl (rx.subjects.PublishSubject/create)
      obr1 (reify Observer
           (onNext      [this v] (println "[1] onNext"      v))
           (onError     [this e] (println "[1] onError"     e))
           (onCompleted [this  ] (println "[1] onCompleted"  )))
      obr2 (reify Observer
           (onNext      [this v] (println "[2] onNext"      v))
           (onError     [this e] (println "[2] onError"     e))
           (onCompleted [this  ] (println "[2] onCompleted" )))]
    (.subscribe   obl  obr1  )
    (.onNext      obl "one"  )
    (.onNext      obl "two"  )
    (.subscribe   obl  obr2  )
    (.onNext      obl "three")
    (.onCompleted obl        ))
