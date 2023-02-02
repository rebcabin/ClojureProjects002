(ns asr.lpython

  (:use [clojure.java.shell :only [sh     ] ])

  (:require [blaster.clj-fstring :refer [f-str]]
            [pathetic.core       :as    path   ]
            [clojure.java.io     :as    io     ]
            [clojure.string                    ]))


;;   __ _ _                       _
;;  / _(_) |___   _ __  __ _ _ __| |_
;; |  _| | / -_) | '  \/ _` | '  \  _|
;; |_| |_|_\___| |_|_|_\__, |_|_|_\__|
;;                     |___/


(def dir
  (str (System/getProperty "user.home")
       "/Documents/GitHub/lpython"))

#_dir
;; => "/Users/brian/Documents/GitHub/lpython"

(def asr-asdl-file-relative
  "src/libasr/ASR.asdl")

(def asr-asdl-file
  (path/resolve dir asr-asdl-file-relative))

(def asr-asdl
  (slurp asr-asdl-file))

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


#_(test-version)
;; => {:exit 0,
;;     :out
;;     "LPython version: 0.11.0-61-g2c5034a29\nPlatform: macOS ARM\nDefault target: arm64-apple-darwin22.2.0\n",
;;     :err ""}


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
  (let [rsamp (resolve-sample sample)]
    (if (.exists (io/file (first rsamp)))
      ;; RACE THE OS TO SEE WHETHER THE FILE STILL EXISTS :)
      (apply sh (cons executable
                      (concat includes
                              options
                              rsamp)))
      (throw (java.io.FileNotFoundException.
              (f-str "LPython input {rsamp} not found."))))))


(def clojurizer #"([^\s^\{]+)\:")


(defn post-process-asr
  [asr-sh-result]
  (clojure.string/replace asr-sh-result clojurizer ":$1"))


(defn get-sample-clj
  [sample]
  "DANGER! NEVER, EVER RUN ON UNTRUSTED INPUTS."
  (-> sample
      get-sample-str
      :out
      post-process-asr
      read-string))
