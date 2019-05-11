(defproject inc-dsets "0.1.0"
  :description ""
  :dependencies [[org.clojure/clojure "1.5.0-alpha3"]
                 [incanter "1.3.0"]
                 [org.clojure/data.csv "0.1.2"]
                 [org.clojure/data.json "0.2.1"]

                 ;; 06.11
                 [congomongo "0.3.3"]]
  :plugins [[speclj "2.3.1"]]
  :test-paths ["spec/"])
