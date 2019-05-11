(defproject webapp/project "1.0.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure/compojure "1.0.0"]
                 [ring                "1.1.8"]]
  :plugins      [[lein-ring           "0.8.5"]
                 [lein-beanstalk      "0.2.7"]]
  :ring {:handler webapp.core/routes})
