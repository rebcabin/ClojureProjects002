(defproject composable-statistics "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure           "1.10.0"]
                 [org.clojure/core.async        "0.4.490"]
                 [zprint                        "0.4.15"]
                 [net.mikera/core.matrix        "0.62.0"]
                 [net.mikera/vectorz-clj        "0.48.0"]
                 [org.clojure/algo.generic      "0.1.2"]
                 [uncomplicate/neanderthal      "0.22.1"]
                 [org.tensorflow/tensorflow     "1.13.1"]
                 [quil                          "3.0.0"]
                 [metasoarous/oz                "1.6.0-alpha2"]
                 ]
  :main ^:skip-aot composable-statistics.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
