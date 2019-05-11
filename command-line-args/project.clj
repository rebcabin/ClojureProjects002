(defproject command-line-args "0.1.0-SNAPSHOT"

  :description "FIXME: write description"

  :url "http://example.com/FIXME"

  ;; "lein run" knows to find the -main defn in this namespace
  :main command-line-args.core
  
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure         "1.5.0"]
                 [clj-http                    "0.6.4"]
                 [org.clojure/tools.namespace "0.2.2"]
                 [org.clojure/algo.monads     "0.1.4"]
                 ])
