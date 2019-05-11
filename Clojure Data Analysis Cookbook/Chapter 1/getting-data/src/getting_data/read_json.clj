
(ns getting-data.read-json
  (:use [incanter.core]
        [clojure.data.json]))

(def data-file "data/small-sample.json")

(defn load-data
  "This loads data from a JSON file."
  [json-file]
  (to-dataset (read-json (slurp json-file))))

