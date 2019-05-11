;;; https://www.zaiste.net/2014/02/web_applications_in_clojure_all_the_way_with_compojure_and_om/

(defproject acme "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure       "1.7.0-alpha5"]
                 [org.clojure/tools.reader  "0.8.4"]
                 [compojure                 "1.3.2"]
                 [org.clojure/clojurescript "0.0-2371"]
                 [org.clojure/core.async    "0.1.346.0.17112a-alpha"]
                 [cljs-http                 "0.1.21"]
                 [om                        "0.7.3"]
                 [ring/ring-core            "1.3.2"]
                 [ring/ring-json            "0.3.1"]
                 [ring/ring-defaults        "0.1.4"]]
  :plugins [[lein-ring      "0.9.7"] ;;; <~~~ version leak; should be "0.8.13"
            [lein-cljsbuild "1.0.3"] ;;;      according to the web site.
            [lein-pdo       "0.1.1"]]
  :aliases {"up" ["pdo" "cljsbuild" "auto" "dev," "ring" "server-headless"]}
  :cljsbuild {:builds [{:id           "dev"
                        :source-paths ["src/cljs"]
                        :compiler     {:output-to     "resources/public/js/app.js"
                                       :output-dir    "resources/public/js/out"
                                       :optimizations :none
                                       :source-map    true}}]}
  :ring {:handler acme.handler/app}
  :profiles
  :source-paths ["src/clj"]
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock            "0.3.0"]]}})
