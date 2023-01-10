(ns asr.core
  (:gen-class)

  (:use [asr.utils] ; TODO: winnow
        [asr.numbers]
        [asr.specs]
        [asr.parsed]
        [asr.autospecs]
        [asr.arithmetic]
        [asr.expr.semnasr]
        [asr.expr.semsem]
        [swiss.arrows])

  (:require [clojure.spec.alpha            :as    s       ]
            [clojure.pprint                :refer [pprint]]
            [clojure.spec.gen.alpha        :as    gen     ]
            [clojure.spec.test.alpha       :as    stest   ]
            [clojure.test.check.generators :as    tgen    ]
            [clojure.set                   :as    set     ]
            [pathetic.core                 :as    path    ]
            [asr.lpython                   :as    lpython]))


;; Code above is production code


;;  ___                   _               _        _      _         __  __
;; | __|_ ___ __  ___ _ _(_)_ __  ___ _ _| |_ __ _| |  __| |_ _  _ / _|/ _|
;; | _|\ \ / '_ \/ -_) '_| | '  \/ -_) ' \  _/ _` | | (_-<  _| || |  _|  _|
;; |___/_\_\ .__/\___|_| |_|_|_|_\___|_||_\__\__,_|_| /__/\__|\_,_|_| |_|
;;         |_|


;;  _       _        _                   _
;; | |_ ___| |_ __ _| |  __ ___ _  _ _ _| |_
;; |  _/ _ \  _/ _` | | / _/ _ \ || | ' \  _|
;;  \__\___/\__\__,_|_| \__\___/\_,_|_||_\__|

(defn only-asr-specs
  "Filter non-ASR specs from the keys of the spec registry."
  [asr-namespace-string]
  []
  (filter
   #(= (namespace %) asr-namespace-string)
   (keys (s/registry))))


(defn check-registry
  "Print specs defined in the various namespaces. Call this at the
  REPL (replacing missing codox for specs)."
  []
  (pprint {"core specs"         (only-asr-specs "asr.core")
           "parsed specs"       (only-asr-specs "asr.parsed")
           "hand-written specs" (only-asr-specs "asr.specs")
           "automatic specs"    (only-asr-specs "asr.autospecs")}))


(defn count-asr-core-specs
  "Count the asr.core specs. Call this at the REPL."
  []
  (count (only-asr-specs "asr.core")))


(println "total number of experimental specs registered in the core namespace: ")
(println (count-asr-core-specs))


(println "Please see the tests. Main doesn't do a whole lot ... yet.")


(import java.io.File)


(defn file-example [outfile-prefix, spec, n]
  (let [outputs_dir (File. "outputs")
        fout (File/createTempFile
              outfile-prefix
              ".txt"
              outputs_dir)]
    (-<> spec
         s/gen
         (gen/sample <> n)
         pprint
         with-out-str
         (spit fout <>))))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]

  (let [outputs (File. "outputs")]
    (->> (.listFiles outputs)
         (map #(.toString %))))

  (dotimes [_ 3]
    (file-example
     "ASR_IntegerBinOp_",
     :asr.expr.semsem/i32-bin-op,
     4))

  (dotimes [_ 3]
    (file-example
     "ASR_Variable_",
     :asr.specs/variable,
     1))

  )
