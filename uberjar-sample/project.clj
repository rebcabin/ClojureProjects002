(defproject uberjar-sample "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [clj-http "0.6.4"]]
  :profiles {:dev {:dependencies [[midje "1.3.1"]]}}
  :test-selectors {:default (complement :integration)
                   :integration :integration
                   :all (fn [_] true)}
  :main uberjar-sample.core
)
