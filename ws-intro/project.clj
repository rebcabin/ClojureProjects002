(defproject ws-intro "0.1.0-SNAPSHOT"
  :main ws-intro.core
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repl-options {:init-ns ws-intro.core}
  :dependencies [[org.clojure/clojure               "1.5.1" ]
                 [org.slf4j/slf4j-log4j12           "1.7.5" ]
                 [com.netflix.rxjava/rxjava-clojure "0.7.0" ]
                 [org.webbitserver/webbit           "0.4.14"]
                 [org.clojure/data.json             "0.2.2" ]
                 ])
