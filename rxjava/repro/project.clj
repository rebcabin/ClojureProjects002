(defproject repro "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure               "1.5.1"]
                 [org.clojure/data.json             "0.2.2"]
                 [com.netflix.rxjava/rxjava-clojure "0.12.0"]
                 ]
  :repositories {"local" ~(str (.toURI (java.io.File. "maven_repository")))}
  :main repro.core)
