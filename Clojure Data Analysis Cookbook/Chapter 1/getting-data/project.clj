(defproject getting-data "0.1.0-SNAPSHOT"
  :description "Recipes for Clj data analysis cookbook, chapter 1."
  :url ""
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [speclj "2.5.0"]

                 ; For 1.1. read csv
                 [incanter/incanter-core "1.4.1"]
                 [incanter/incanter-io "1.4.1"]

                 ; For 1.2, read JSON
                 ; [incanter/incanter-core "1.3.0"]
                 [org.clojure/data.json "0.2.1"]

                 ; For 1.3, read XML
                 ; [incanter/incanter-core "1.3.0"]

                 ; For 1.4, read XLS
                 ; [incanter/incanter-core "1.3.0"]
                 [incanter/incanter-excel "1.4.1"]

                 ; For 1.4, read JDBC
                 ; [incanter/incanter-core "1.3.0"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [org.xerial/sqlite-jdbc "3.7.2"]

                 ; For 1.6, scraping HTML tables
                 ; [incanter/incanter-core "1.3.0"]
                 [enlive "1.0.1"]

                 ; For 1.7, scraping HTML text
                 ; [incanter/incanter-core "1.3.0"]
                 ; [enlive "1.0.1"]

                 ; For 1.8, read RDF
                 ; [incanter/incanter-core "1.3.0"]
                 [edu.ucdenver.ccp/kr-sesame-core "1.4.5"]
                 [org.clojure/tools.logging "0.2.4"]
                 [org.slf4j/slf4j-simple "1.7.2"]

                 ; For 1.9, read SPARQL
                 ; [incanter/incanter-core "1.3.0"]
                 ; [edu.ucdenver.ccp/kr-sesame-core "1.4.2"]
                 ; [org.clojure/tools.logging "0.2.3"]
                 ; [org.slf4j/slf4j-simple "1.6.6"]

                 ; For 1.10, aggregating semantic web data
                 ; [incanter/incanter-core "1.3.0"]
                 ; [edu.ucdenver.ccp/kr-sesame-core "1.4.2"]
                 ; [org.clojure/tools.logging "0.2.3"]
                 ; [org.slf4j/slf4j-simple "1.6.6"]

                 ; For 1.11, aggregating multiple sources
                 ; [incanter/incanter-core "1.3.0"]
                 ; [enlive "1.0.1"]
                 ; [edu.ucdenver.ccp/kr-sesame-core "1.4.2"]
                 ; [org.clojure/tools.logging "0.2.3"]
                 ; [org.slf4j/slf4j-simple "1.6.6"]
                 [clj-time "0.4.4"]

                 ; For 1.12, download concurrently
                 [http.async.client "0.5.0"]

                 ]
  :plugins [[speclj "2.1.2"]]
  :test-paths ["spec"])

