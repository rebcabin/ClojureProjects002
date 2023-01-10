(ns asr.lpython

  (:use [clojure.java.shell :only [sh] ])

  (:require [pathetic.core  :as   path ]))

(def dir
  "/Users/brian/Documents/GitHub/lpython")

(def executable-relative
  "src/bin/lpython")

(def executable
  (path/resolve dir executable-relative))

(def ltypes-relative
  "src/runtime/ltypes/ltypes.py")

(def includes
  [(str "-I" (path/resolve dir ltypes-relative))])

(def options
  ["--show-asr" "--no-color"])

(def test-options
  ["--version"])

(defn test-version
  []
  (apply sh (cons executable
                  test-options)))

(defn get-sample-str
  [sample]
  (defn resolve-sample
    [sample]
    [(path/resolve
      dir
      sample)])
  (apply sh (cons executable
                  (concat includes
                          options
                          (resolve-sample sample)))))

(defn get-sample-clj
  [sample]
  "DANGER! NEVER, EVER RUN ON UNTRUSTED INPUTS."
  (-> sample
      get-sample-str
      :out
      read-string))
