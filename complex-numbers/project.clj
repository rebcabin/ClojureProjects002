(defproject complex-numbers "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure      "1.7.0"]
                 [com.cemerick/pomegranate "0.3.0"]
                 [simple-plotter           "0.1.2"]]
  :main ^:skip-aot complex-numbers.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
