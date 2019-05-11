(defproject cleaning-data "0.1.0-SNAPSHOT"
  :description "Clojure Data Analysis Cookbook, Chapter 2: Cleaning Data"
  :url ""
  :dependencies [[org.clojure/clojure "1.5.0-RC2"]
                 [speclj "2.5.0"]

                 ;; 2.03
                 [clj-diff "1.0.0-SNAPSHOT"]

                 ;; 2.06
                 [clj-time "0.4.4"]

                 ;; 2.07
                 [org.clojure/data.csv "0.1.2"]

                 ;; 2.11
                 [parse-ez "0.3.4"]

                 ;; 2.12
                 ; [org.clojure/data.xml "0.0.6"]
                 [valip "0.2.0"]
                 ]
  :plugins [[speclj "2.1.2"]]
  :jvm-opts ["-Xmx768M"]
  :test-paths ["spec/"])
