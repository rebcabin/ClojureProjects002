(defproject d-mining "0.1.0-SNAPSHOT"
  :description ""
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [nz.ac.waikato.cms.weka/weka-dev "3.7.7"]
                 [nz.ac.waikato.cms.weka/LibSVM "1.0.5"]
                 [speclj "2.5.0"]
                 [incanter "1.4.1"]
                 [org.clojure/data.json "0.2.1"]
                 [org.clojure/data.csv "0.1.2"]
                 [org.codehaus.jsr166-mirror/jsr166y "1.7.0"]]
  :plugins [[speclj "2.5.0"]]
  :test-paths ["spec"]
  ; :main d-mining.ga-runner
  ; :main d-mining.hierarchy-viz
            )
