(ns green-eggs.core)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(def green-eggs-n-ham
  ["in the rain"
   "on a train"
   "in a box"
   "with a fox"
   "in a house"
   "with a mouse"
   "here or there"
   "anywhere"])

(defn i-do-not-like-them [s]
  (format "I would not eat them %s." s))

(defn try-them [s]
  (clojure.string/replace s #" not" ""))

(def sam-i-am-xform
  (comp
   (map i-do-not-like-them)
   (map try-them)))
