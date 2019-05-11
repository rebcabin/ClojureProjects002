(defproject blade-acousto "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure    "1.7.0"]
                 [complex                "0.1.5"]
                 [net.mikera/core.matrix "0.37.0"]
                 [net.mikera/vectorz-clj "0.33.0"]
                 [clatrix "0.5.0"]]
  :main ^:skip-aot blade-acousto.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
