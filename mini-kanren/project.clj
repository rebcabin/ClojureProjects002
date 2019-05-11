(defproject mini-kanren "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure    "1.9.0"]
                 [org.clojure/core.logic "0.8.11"]
                 [swiss-arrows           "0.6.0"] ;; doesn't work with 1.0.0 TODO
                 ]
  :main mini-kanren.core)
