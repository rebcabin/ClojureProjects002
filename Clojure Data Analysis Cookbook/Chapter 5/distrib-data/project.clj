(defproject distrib-data "0.1.0"
  :description ""
  :url ""
  :dependencies [[org.clojure/clojure "1.4.0"]

                 ;; 05.01
                 ; [cascalog "2.0.0-SNAPSHOT"]
                 [cascalog "1.10.0"]
                 [org.slf4j/slf4j-api "1.7.2"]

                 ;; 05.02
                 [incanter/incanter-core "1.4.0"]
                 [incanter/incanter-io "1.4.0"]
                 [incanter/incanter-mongodb "1.4.0"]
                 [congomongo "0.4.0"]
                 [org.mongodb/mongo-java-driver "2.10.1"]

                 ;; 05.05
                 [cascading/cascading-core "2.1.3"]
                 [cascading/cascading-hadoop "2.1.3"]

                 ;; 05.11
                 [avout "0.5.3"]
                 [commons-codec/commons-codec "1.6"]

                 [speclj "2.3.1"]
                 [org.clojure/data.csv "0.1.2"]
                 ]
  ; :plugins [[speclj "2.3.1"]]
  :repositories [["conjars.org" "http://conjars.org/repo"]]
  :profiles {:dev {:dependencies [[org.apache.hadoop/hadoop-core "1.1.1"]]}}
  :test-paths ["spec/"])
