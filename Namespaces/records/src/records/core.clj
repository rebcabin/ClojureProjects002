(ns records.core)

(defn hello []  "hello")

(defn- secret [] "secret")

(defprotocol my-sequence
  (add [seqq item]))

(defrecord my-vector [coll]
  my-sequence
  (add [_ item] (conj coll item)))

