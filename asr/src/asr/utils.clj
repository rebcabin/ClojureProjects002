(ns asr.utils
  (:require [clojure.spec.alpha      :as    s       ]
            [clojure.spec.test.alpha :as    stest   ]
            [camel-snake-kebab.core  :as    csk     ]
            [clojure.pprint          :refer [pprint]]))


;;         _
;;  ___ __| |_  ___
;; / -_) _| ' \/ _ \
;; \___\__|_||_\___/


;; TODO: macro?
(defn echo
  "Print and return argument. Convenient for debugging -> call
  chains."
  [x]
  (pprint x) x)


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
                    #_default    "asr.core")]

    (keyword namespace
             (name (csk/->kebab-case sym-or-string))))

  ;; Found by experiment that ->> doesn't work, here. Something
  ;; to do with macros.
  #_(->> sym csk/->kebab-case #(keyword "asr.core" %)) )


;;; Experimental Function Spec for nskw-kebab-from. This is an
;;; ansatz for future function specs.

(s/fdef nskw-kebab-from
  :args (s/alt :str string? :sym symbol?)
  :ret keyword?)

(stest/instrument `nskw-kebab-from)
