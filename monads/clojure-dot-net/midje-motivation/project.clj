(defproject midje-motivation "0.0.1-SNAPSHOT"
  :description "Cool new project to do things and stuff"
  :dependencies [[org.clojure/clojure    "1.5.1"]
                 [swiss-arrows           "0.6.0"]
                 ;; [swiss-arrows           "1.0.0"]
                 [org.clojure/algo.monads "0.1.4"]]
  :repositories {"local" ~(str (.toURI (java.io.File. "maven_repository")))}
  :profiles {:dev {:dependencies [[midje "1.5.0"]]}})
