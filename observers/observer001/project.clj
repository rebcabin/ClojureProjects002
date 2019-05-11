(defproject reactive-backplane "0.1.0-SNAPSHOT"
  :description "An observer that just prints out its inputs on the console"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure     "1.4.0"]
                 [ring-json-params        "0.1.3"]
                 [ring/ring-jetty-adapter "1.1.0"]
                 [clj-json                "0.5.1"] ;;; 0.5.2 and 0.5.3 verified fail
                 [compojure               "1.1.5"]
                 [org.slf4j/slf4j-log4j12           "1.7.5" ]
                 [com.netflix.rxjava/rxjava-clojure "0.7.0" ]
                 [org.webbitserver/webbit           "0.4.14"]
                 [org.clojure/data.json             "0.2.2" ]
                 ]
  :plugins [[lein-ring   "0.8.2"]
            [lein-pprint "1.1.1"]]
  :ring {:handler observer001.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}})
