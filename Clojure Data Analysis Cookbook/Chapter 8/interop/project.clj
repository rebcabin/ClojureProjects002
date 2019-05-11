(defproject interop "0.1.0-SNAPSHOT"
  :description ""
:dependencies [[org.clojure/clojure "1.4.0"]
               [local.repo/REngine "1.7.0"]
               [local.repo/Rserve "1.7.0"]
               [incanter "1.4.1"]
               ; [local.repo/JLink "9.0"]
                 [speclj "2.3.1"]
                 ; [local.repo/clojuratica "2.0"]
                 ]
  :source-paths ["src" "Clojuratica/src/clj"]
  :resource-paths ["src/main/resource"
                   "/Applications/Mathematica.app/SystemFiles/Links/JLink"]
  :plugins [[speclj "2.3.1"]]
  :test-paths ["spec"])
