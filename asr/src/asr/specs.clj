(ns asr.specs
  (:use [asr.utils]
        [asr.data]
        [asr.numbers]
        [asr.base-specs]
        [asr.autospecs])
  (:require [clojure.spec.alpha            :as s   ]
            [clojure.zip                   :as z   ]
            [clojure.spec.gen.alpha        :as gen ]
            [clojure.test.check.generators :as tgen]))


;;     _ _                   _
;;  __| (_)_ __  ___ _ _  __(_)___ _ _  ___
;; / _` | | '  \/ -_) ' \(_-< / _ \ ' \(_-<
;; \__,_|_|_|_|_\___|_||_/__/_\___/_||_/__/

(s/def ::dimensions
  (s/coll-of (s/or :nat-int nat-int?, :bigint :asr.numbers/bignat)
             :min-count 0,
             :max-count 2,
             :into []))

#_
(-> ::dimensions s/exercise)
;; => ([[0] [[:bigint 0]]]
;;     [[1] [[:nat-int 1]]]
;;     [[150] [[:bigint 150]]]
;;     [[5786] [[:bigint 5786]]]
;;     [[1 7] [[:nat-int 1] [:nat-int 7]]]
;;     [[314370383 1] [[:bigint 314370383] [:nat-int 1]]]
;;     [[11] [[:nat-int 11]]]
;;     [[] []]
;;     [[2671503976487097 5] [[:bigint 2671503976487097] [:nat-int 5]]])
;;     [[6694 3] [[:bigint 6694] [:nat-int 3]]])


;;                _         _   _        _    _
;;  ____  _ _ __ | |__  ___| | | |_ __ _| |__| |___
;; (_-< || | '  \| '_ \/ _ \ | |  _/ _` | '_ \ / -_)
;; /__/\_, |_|_|_|_.__/\___/_|  \__\__,_|_.__/_\___|
;;     |__/

(s/def ::symbol-table
  (s/cat :head #{'SymbolTable}
         :uid  pos?
         :dict (s/map-of keyword? :asr.autospecs/symbol
                         :conform-keys true #_"disallow duplicates")))


;;               _      _    _
;; __ ____ _ _ _(_)__ _| |__| |___
;; \ V / _` | '_| / _` | '_ \ / -_)
;;  \_/\__,_|_| |_\__,_|_.__/_\___|

;; Variable(
;;   symbol_table parent_symtab,   -- actually a uid
;;   identifier   name,
;;   intent       intent,
;;   expr?        symbolic_value,
;;   expr?        value,
;;   storage_type storage,
;;   ttype        type,
;;   abi          abi,
;;   access       access,
;;   presence     presence,
;;   bool         value_attr,

(s/def ::variable
  (s/cat :head           #{'Variable}
         :parent-symtab  pos?
         :nym            symbol?  ; ::identifier
         :intent         :asr.autospecs/intent
         :symbolic-value (s/spec (s/? :asr.autospecs/expr))
         :value          (s/spec (s/? :asr.autospecs/expr))
         :storage-type   :asr.autospecs/storage-type
         :type           :asr.autospecs/ttype
         :abi            :asr.autospecs/abi
         :access         :asr.autospecs/access
         :presencs       :asr.autospecs/presence
         :value-attr     :asr.autospecs/bool))

(s/def ::variable
  (s/cat :head           #{'Variable}
         :parent-symtab  #(and (int? %) (> % 0))  ; pos? won't generate
         :nym            symbol?  ; ::identifier
         :intent         :asr.autospecs/intent
         :symbolic-value (s/spec (s/? :asr.autospecs/expr))
         :value          (s/spec (s/? :asr.autospecs/expr))
         :storage-type   :asr.autospecs/storage-type
         :type           :asr.autospecs/ttype
         :abi            :asr.autospecs/abi
         :access         :asr.autospecs/access
         :presencs       :asr.autospecs/presence
         :value-attr     :asr.autospecs/bool))

;; (gen/generate (s/gen ::variable))
;; (gen/generate (s/gen #(and (int? %) (> % 0))))
;; (gen/generate (s/gen symbol?))
;; (s/exercise ::identifier)
;; (gen/generate (s/gen :asr.autospecs/expr))
