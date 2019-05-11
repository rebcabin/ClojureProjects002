(defproject parallel-data "0.1.0"
  :description ""
  :url ""
  :dependencies [[org.clojure/clojure "1.5.0-alpha3"]
                 [speclj "2.1.2"]

                 ;; 04.03
                 [incanter "1.3.0"]

                 ;; 04.07
                 [nio "0.0.3"]
                 [org.apache.commons/commons-lang3 "3.1"]
                 [clj-time "0.4.4"]

                 ;; 04.08
                 [org.codehaus.jsr166-mirror/jsr166y "1.7.0"]

                 ;; 04.09
                 [org.clojure/data.csv "0.1.2"]

                 ;; 04.10
                 [calx "0.2.1"]

                 ;; 04.11
                 [criterium "0.3.0"]
                 ]
  :plugins [[speclj "2.1.2"]]
  :test-paths ["spec/"])
