(defproject asr "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :plugins [[lein-codox "0.10.8"]]
  :codox {:metadata {:doc/format :markdown}}
  :dependencies [[org.clojure/clojure            "1.11.1"]
                 [org.clojure/core.logic         "1.0.1"]
                 [org.clojure/test.check         "1.1.1"]
                 [org.clojure/math.numeric-tower "0.0.5"]
                 [org.clojure/algo.monads        "0.1.6"]
                 [instaparse                     "1.4.12"]
                 [swiss-arrows                   "0.6.0"] ; experimental
                 [camel-snake-kebab              "0.4.3"]
                 [org.bytedeco/javacpp           "1.5.7"]
                 [org.bytedeco.javacpp-presets/openblas-platform "0.2.19-1.3"]]
  :main ^:skip-aot asr.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
