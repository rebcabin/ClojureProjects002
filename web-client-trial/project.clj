(defproject web-client-trial "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  ;; "lein run" knows to find the -main defn in this namespace
  :main web-client-trial.core
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.0"]
                 [clj-json            "0.5.1"]
                 [clj-http            "0.6.5"]
                 ])
