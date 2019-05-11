
(ns getting-data.read-csv
  (:use [incanter.core]
        [incanter.io]))

(def data-file "data/small-sample.csv")
(def data-file-header "data/small-sample-header.csv")

(defn load-data
  "This loads a CSV file."
  [csv-file]
  (read-dataset csv-file))

(defn load-data-headers
  "This loads a CSV file with headers."
  [csv-file]
  (read-dataset csv-file :header true))

