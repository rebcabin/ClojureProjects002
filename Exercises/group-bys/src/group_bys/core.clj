(ns group-bys.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))

  (let [xs [{:acct "MSFT" :val 10 :pe 3}
            {:acct "GOOG" :val 15 :pe 3}
            {:acct "MSFT" :val 15 :pe 4}
            {:acct "YAH"  :val  8 :pe 1}]]
    (println (group-by :acct xs))))

(-main)