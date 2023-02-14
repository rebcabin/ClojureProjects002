(ns asr.utils
  (:require [clojure.spec.alpha      :as    s       ]
            [clojure.spec.test.alpha :as    stest   ]
            [camel-snake-kebab.core  :as    csk     ]
            [clojure.pprint          :refer [pprint]]))


;;         _
;;  ___ __| |_  ___
;; / -_) _| ' \/ _ \
;; \___\__|_||_\___/


(defmacro nkecho
  "just the value, please"
  [x]
  `(let [x# ~x]
     (do (print "~~>" (with-out-str (clojure.pprint/pprint x#)))
         x#)))


(defmacro plnecho
  "preimage and value"
  [x]
  `(let [x# ~x]
     (do (println '~x "~~>" x#)
         x#)))


;;; and pretty-printing version


(defmacro echo
  "big, pretty-printed block"
  [x]
  `(let [x# ~x]
     (do (println
          "=.=.=.=.=.=.=.=.=.=.=.=.=.=.=.=.=.=.=.=.=.=.=.=.=.=.=.=.=")
         ;; (println
         ;;  " | | | | | | | | | | | | | | | | | | | | | | | | | | | | ")
         (clojure.pprint/pprint '~x)
         (println "~~~~~~~>")
         (clojure.pprint/pprint x#)
         ;; (println
         ;;  " | | | | | | | | | | | | | | | | | | | | | | | | | | | | ")
         (println
          "='='='='='='='='='='='='='='='='='='='='='='='='='='='='=")
         x#)))


;;; Example:


(doseq [t (list
           () [] {} #{} "")
        f (list
           ;; list? vector? map? set? coll? seq? empty?
           ;; sequential?
           ;; string?
           )]
  (prn ((plnecho f) (plnecho t))))
;; ------------.-----------------------------------------------
;;             |   ()        []        {}        #{}       ""
;; ------------|-----------------------------------------------
;; coll?       |  true      true      true      true      false
;; seq?        |  true      false     false     false     false
;; sequential? |  true      true      false     false     false
;; ------------|-----------------------------------------------
;; empty?      |  true      true      true      true      true
;; ------------'-----------------------------------------------
;; list?       |  true      false     false     false     false
;; vector?     |  false     true      false     false     false
;; map?        |  false     false     true      false     false
;; set?        |  false     false     false     true      false
;; string?     |  false     false     false     false     true
;; ------------|-----------------------------------------------


;;          _               _       _          _         __
;;  _ _  __| |____ __ _____| |_____| |__  __ _| |__ ___ / _|_ _ ___ _ __
;; | ' \(_-< / /\ V  V /___| / / -_) '_ \/ _` | '_ \___|  _| '_/ _ \ '  \
;; |_||_/__/_\_\ \_/\_/    |_\_\___|_.__/\__,_|_.__/   |_| |_| \___/_|_|_|


(defn nskw-kebab-from ;; TODO: macro?

  "### Kebab'bed Namespaced Keywords for Specs from Symbols in ASR

  Transform conventional names in ASR PascalCase to conventional
  namespaced keywords in kebab-case in clojure.spec. It works on
  symbols or on strings. We can write a spec for this function,
  too? Specs all the way down!
  "

  [sym-or-string]

  (let [namespace (case (str sym-or-string)
                    "identifier" "asr.specs"
                    #_default    "asr.autospecs")]

    (keyword namespace
             (name (csk/->kebab-case sym-or-string)))))


;;; Experimental Function Spec for nskw-kebab-from. This is an
;;; ansatz for future function specs.

(s/fdef nskw-kebab-from
  :args (s/alt :str string? :sym symbol?)
  :ret keyword?)

(stest/instrument `nskw-kebab-from)
