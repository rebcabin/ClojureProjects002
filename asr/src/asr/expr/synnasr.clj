(ns asr.expr.synnasr
  (:use [asr.parsed]
        [asr.autospecs])

  (:require [clojure.spec.alpha            :as    s       ]
            [clojure.spec.gen.alpha        :as    gen     ]
            [clojure.spec.test.alpha       :as    stest   ]
            [clojure.test.check.generators :as    tgen    ]
            [clojure.set                   :as    set     ]

            [asr.utils                     :refer [echo]  ]))


;;  _     _                      _    _
;; (_)_ _| |_ ___ __ _ ___ _ _  | |__(_)_ _    ___ _ __
;; | | ' \  _/ -_) _` / -_) '_| | '_ \ | ' \  / _ \ '_ \
;; |_|_||_\__\___\__, \___|_|   |_.__/_|_||_| \___/ .__/
;;               |___/                            |_|
;;            _
;;  __ _ _  _| |_ ___ ____ __  ___ __
;; / _` | || |  _/ _ (_-< '_ \/ -_) _|
;; \__,_|\_,_|\__\___/__/ .__/\___\__|
;;                      |_|

(println "Experimental hand-aided autospec for IntegerBinOp ~~~> integer-bin-op")
(println "Note! This code in namespace asr.expr.synnasr.integer-bin-op, \n writes a spec into another namespace, namely 'autospecs'!")

(let [integer-bin-op-stuff ;; SynNASR
      (filter #(= (:head %) :asr.autospecs/IntegerBinOp)
              big-list-of-stuff)]
  (-> (asr.autospecs/spec-from-composite
       (-> integer-bin-op-stuff
           first
           :form
           :ASDL-COMPOSITE))
      #_echo
      eval
      ))
;; => :asr.autospecs/integer-bin-op


#_
(-> :asr.autospecs/expr
    s/exercise)


#_
(s/describe :asr.autospecs/integer-bin-op)
;; => (cat
;;     :head
;;     #function[asr.autospecs/spec-from-head-and-args/lpred--2572]
;;     :left
;;     (spec :asr.autospecs/expr)
;;     :op
;;     (spec :asr.autospecs/binop)
;;     :right
;;     (spec :asr.autospecs/expr)
;;     :type
;;     (spec :asr.autospecs/ttype)
;;     :value
;;     (? (spec :asr.autospecs/expr)))
