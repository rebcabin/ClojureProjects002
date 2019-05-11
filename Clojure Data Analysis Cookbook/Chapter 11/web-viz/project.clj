(defproject web-viz "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [ring/ring-core "1.1.8"]
                 [ring/ring-jetty-adapter "1.1.8"]
                 [compojure "1.1.3"]
                 [org.clojure/data.json "0.2.1"]
                 [org.clojure/data.csv "0.1.2"]
                 [hiccup "1.0.2"]
                 [nz.ac.waikato.cms.weka/weka-dev "3.7.7"]
                 [speclj "2.5.0"]]
  :plugins [[lein-ring "0.8.3"]
            [lein-cljsbuild "0.3.0"]
            [speclj "2.5.0"]]
  :test-paths ["spec"]
  :cljsbuild {:crossovers [web-viz.x-over],
              :builds
              [{:source-paths ["src-cljs"],
                :crossover-path "xover-cljs",
                :compiler
                {:pretty-print true,
                 :output-to "resources/js/script.js",
                 :optimizations :whitespace}}]}
  :ring {:handler web-viz.web/app}
  ; :ring {:handler web-viz.tmp-web/app}
            )

