(defproject rummy "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure    "1.9.0-alpha15"]
                 [org.clojure/test.check "0.9.0"]
                 [tupelo                 "0.9.14"]
                 [funcyard               "0.1.1-SNAPSHOT"]
                 [proto-repl             "0.3.1"]]
  :main ^:skip-aot rummy.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
