(defproject hypertalk "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure    "1.9.0-alpha14"]
                 [org.clojure/test.check "0.9.0"]
                 [tupelo                 "0.9.14"]
                 [funcyard               "0.1.1-SNAPSHOT"]
                 [camel-snake-kebab      "0.4.0"]
                 [instaparse             "1.4.5"]
                 [com.rpl/specter        "0.13.2"]]
  :main ^:skip-aot hypertalk.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
