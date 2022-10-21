(ns asr.base-specs
  (:require [clojure.spec.alpha            :as s   ]
            [clojure.spec.gen.alpha        :as gen ]
            [clojure.test.check.generators :as tgen]))


;;  _    _         _   _  __ _
;; (_)__| |___ _ _| |_(_)/ _(_)___ _ _
;; | / _` / -_) ' \  _| |  _| / -_) '_|
;; |_\__,_\___|_||_\__|_|_| |_\___|_|

(println "CYCLE BREAKING identifier spec installed in asr.specs, defined in asr.base-specs.")

(let [alpha-re #"[a-zA-Z]" ;; The famous "let over lambda."
      alphameric-re #"[a-zA-Z0-9]*"]
  (def alpha?
    #(re-matches alpha-re %))
  (def alphameric?
    #(re-matches alphameric-re %))
  (defn identifier? [s]
    (and (alpha? (subs s 0 1))
         (alphameric? (subs s 1))))
  (def identifier-generator
    (tgen/let [c (gen/char-alpha)
               s (gen/string-alphanumeric)]
      (str c s)))
  (s/def :asr.specs/identifier ;; side effects the spec registry!
    (s/with-gen
      identifier?
      (fn [] identifier-generator))))

#_
(gen/sample (s/gen ::identifier))
;; => ("A" "t" "ua" "T" "t" "S3bu85" "xGi" "PJre" "RyHKK0QX4" "TIhjmK1e")