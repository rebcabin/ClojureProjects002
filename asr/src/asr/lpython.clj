(ns asr.lpython

  (:use [clojure.java.shell :only [sh     ] ]
        [asr.utils                          ])

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
  ["--show-asr"
   "--no-color"
   ;; Issues #1505 and #1420 https://github.com/lcompilers/lpython/issues/1505
   #_"--with-intrinsic-mods"])

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


(defn err-string
  [severity rsamp result]
  (clojure.string/join
   "\n"
   [(f-str "LPython produced compilation {severity}s for")
    (f-str "{(first rsamp)}")
    (:err result)]))


(defn get-sample-str
  [sample]
  (defn resolve-sample
    [sample]
    [(path/resolve
      dir
      sample)])
  (let [rsamp (resolve-sample sample)]
    (if (.exists (io/file (first rsamp)))
      ;; TODO: RACE THE OS TO SEE WHETHER THE FILE STILL EXISTS :)
      (let [result
            (apply sh (echo (cons executable
                             (concat includes
                                     options
                                     rsamp))))]
        (cond

          (and (= "" (:out result))
               (not (= "" (:err result))))
          (throw (java.lang.Error.
                  (err-string "error" rsamp result)))

          (not (= "" (:err result)))
          (print (err-string "warning" rsamp result)))

        (identity result))
      (throw (java.io.FileNotFoundException.
              (f-str "LPython input {rsamp} not found."))))))


(def clojurizer-1 #"([^\s^\{]+)\:") ; move colons to beginning of keywords
(def clojurizer-2 #"@")
(def clojurizer-2-replacement
  "/"
  #_"_AT_")

(defn post-process-asr
  "TODO: (nice-to-have) swiss arrows for this."
  [asr-sh-result]
  (let [one (clojure.string/replace
             asr-sh-result
             clojurizer-1
             ":$1")
        two (clojure.string/replace
             one
             clojurizer-2
             clojurizer-2-replacement)]
    two))


(defn get-sample-clj
  [sample]
  "DANGER! NEVER, EVER RUN THIS ON UNTRUSTED INPUTS."
  (-> sample
      get-sample-str
      :out
      post-process-asr
      read-string))
