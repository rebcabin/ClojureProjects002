
(ns cleaning-data.lazy-read
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

#_
(require '[clojure.data.csv :as csv]
         '[clojure.java.io :as io])

(defn lazy-read-bad-1
  "This one returns the result before it's read from the file."
  ([csv-file]
   (with-open [in-file (io/reader csv-file)]
     (csv/read-csv in-file))))

(defn lazy-read-bad-2
  "This one forces all the lines to be read, but the whole file is read into
  memory."
  ([csv-file]
   (with-open [in-file (io/reader csv-file)]
     (doall
       (csv/read-csv in-file)))))

(defn lazy-read-ok
  "This one's OK. It handles everything lazy, but the lazy read is emeshed with
  the logic.

  This contrived example pulls the age out of each file.

  Note that you still need to force everything to be read. Here I'm using
  frequencies to do this."
  ([csv-file]
   (with-open [in-file (io/reader csv-file)]
     (frequencies (map #(nth % 2) (csv/read-csv in-file))))))

(defn lazy-read-csv
  "This one has lazily reads from the file and generates the CSV data.

  It will leak if the sequence is not fully consumed."
  ([csv-file]
   (let [in-file (io/reader csv-file)
         csv-seq (csv/read-csv in-file)
         lazy (fn lazy [wrapped]
                (lazy-seq
                  (if-let [s (seq wrapped)]
                    (cons (first s) (lazy (rest s)))
                    (.close in-file))))]
     (lazy csv-seq))))

