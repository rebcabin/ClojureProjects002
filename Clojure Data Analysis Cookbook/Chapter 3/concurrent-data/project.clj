(defproject concurrent-data "0.1.0-SNAPSHOT"
  :description "Clojure Data Analysis Cookbook, Chapter 3: Processing Data Concurrently"
  :url ""
  :dependencies [[org.clojure/clojure "1.5.0-RC2"]
                 [speclj "2.5.0"]

                 ;; Miscellaneous Libraries.
                 [org.clojure/data.json "0.2.1"]
                 [org.clojure/java.classpath "0.2.1"]

                 ;; 03.01
                 [org.clojure/data.csv "0.1.2"]

                 ;; 03.12
                 ; [org.clojure/data.csv "0.1.2"]
                 ]
  :plugins [[speclj "2.1.2"]]
  :test-paths ["spec/"])
