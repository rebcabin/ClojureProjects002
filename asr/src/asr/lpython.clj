(ns asr.lpython

  (:use [clojure.java.shell :only [sh     ] ]
        [clojure.string     :only [replace] ] )

  (:require [pathetic.core  :as   path ]))


;;   __ _ _                       _
;;  / _(_) |___   _ __  __ _ _ __| |_
;; |  _| | / -_) | '  \/ _` | '  \  _|
;; |_| |_|_\___| |_|_|_\__, |_|_|_\__|
;;                     |___/


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


;;                  _ _
;;  _ _ ___ __ _ __| (_)_ _  __ _
;; | '_/ -_) _` / _` | | ' \/ _` |
;; |_| \___\__,_\__,_|_|_||_\__, |
;;                          |___/


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


(defn post-process-asr
  [asr-sh-result]
  (replace asr-sh-result #"([^s^\{]+):" ":$1"))


(defn get-sample-clj
  [sample]
  "DANGER! NEVER, EVER RUN ON UNTRUSTED INPUTS."
  (-> sample
      get-sample-str
      :out
      post-process-asr
      read-string))
