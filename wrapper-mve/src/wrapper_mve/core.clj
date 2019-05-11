(ns wrapper-mve.core
  (:use [clojure.java.io :as io])
  (:gen-class))

(defprotocol Dumper
  (dump [this]))

(extend-type java.io.File
  Dumper
  (dump [file]
    (with-open [rdr (io/reader file)]
      (doseq [line (line-seq rdr)]
        (println line)))))

(defmacro wrap-path-string [method]
  `(~method [path-str] (-> path-str, io/file ~method)))

(extend-type java.lang.String
  Dumper
  (wrap-path-string dump)
  #_(dump [path-str] (-> path-str, io/file dump)))

(defn -main
  [& args]
  (dump (io/file "resources/a_file.txt"))
  (dump          "resources/a_file.txt"))
