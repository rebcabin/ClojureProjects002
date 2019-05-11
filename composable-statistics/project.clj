(defproject composable-statistics "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[beckon "0.1.1"]
                 [cheshire "5.7.0"]
                 [clj-time "0.11.0"]
               ; [com.cemerick/pomegranate "0.3.0"]
                 [com.cemerick/pomegranate "0.3.1"]
                 [com.taoensso/timbre "4.8.0"]
                 [compliment "0.3.2"]
                 [fipp "0.6.4"]
                 [incanter "1.5.7"]
                 [incanter/jfreechart "1.0.13-no-gnujaxp"]
                 [mvxcvi/puget "1.0.0"]
                 [net.cgrand/parsley "0.9.3" :exclusions [org.clojure/clojure]]
                 [net.cgrand/sjacket "0.1.1" :exclusions [org.clojure/clojure
                                                          net.cgrand.parsley]]
                 [org.clojure/clojure "1.8.0"]
               ; [org.clojure/core.async "0.2.395"]
                 [org.clojure/core.async "0.3.443"]
                 [org.clojure/java.classpath "0.2.3"]
                 [org.clojure/data.codec "0.1.0"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/tools.nrepl "0.2.12"]
                 [org.zeromq/cljzmq "0.1.4" :exclusions [org.zeromq/jzmq]]
                 [org.zeromq/jeromq "0.3.4"] ; "0.3.5" (modern) fails on zmq/bind.
                 [pandect "0.5.4"]
                 [spyscope "0.1.5"]
                 ;; Added by bbeckman
                 [com.github.casmi/casmi "0.4.0"]
                 [org.clojure/java.data "0.1.1"]
                 [com.amazonaws/aws-java-sdk "1.10.75"
                  ;; The following were tried and don't work
                  ;; "1.11.149"
                  ;; "1.11.74"
                  ;; "1.11.100"
                  ;; "1.11.148"
                  ;; "1.10.77"
                  ]
                 [org.apache.httpcomponents/httpclient "4.5.2"]
                 [uncomplicate/clojurecl "0.7.1"]
                 [uncomplicate/neanderthal "0.13.0"]
                 [manifold "0.1.6"]
                 [net.mikera/core.matrix "0.60.3"]
                 [net.mikera/vectorz-clj "0.47.0"]
                 [org.clojure/algo.generic "0.1.2"]
                 [quil "2.6.0"]])
