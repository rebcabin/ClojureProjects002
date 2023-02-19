(defproject asr "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :plugins [[lein-codox "0.10.8"]]
  :codox {:metadata {:doc/format :markdown}}
  :resource-paths ["resources/javacpp.jar"]
                                        ; :native-path "resources"
  :dependencies [[org.clojure/clojure              "1.11.1"]
                 [org.clojure/test.check           "1.1.1"]
                 [org.clojure/math.numeric-tower   "0.0.5"]
                 [instaparse                       "1.4.12"]
                 [camel-snake-kebab                "0.4.3"]
                 [swiss-arrows                     "1.0.0"] ; experimental
                 [pathetic                         "0.5.1"] ; experimental
                 [com.github.blasterai/clj-fstring "1.1.2"]
                 #_[org.bytedeco/javacpp           "1.5.7"]
                 #_[org.clojure/core.logic         "1.0.1"]
                 #_[org.clojure/algo.monads        "0.1.6"]
                 #_[org.bytedeco.javacpp-presets/openblas-platform "0.3.5-1.4.4"]]
  :test-selectors {:current (fn [metadata- & _]
                              (or
                               #_(= (:name metadata-)
                                  'eval-node-test-examples-slash-expr2-alert)
                               (= (:name metadata-)
                                  'eval-node-test-expr7)))
                   :dump-metadata (fn [metadata- & _]
                                    (prn metadata-)
                                    true)}
  :main ^:skip-aot asr.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
