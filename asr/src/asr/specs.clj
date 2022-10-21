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

(defn- f1 [it]
  (if (empty? it) () (first it)))

(s/def ::temporary
  (s/with-gen
    (s/cat
     :head           #{'Variable}
     :parent-symtab  pos?
     :nym            symbol?            ; ::identifier
     :intent         :asr.autospecs/intent
     :symbolic-value (s/or :empty empty?
                           :expr  :asr.autospecs/expr)
     :value          (s/or :empty empty?
                           :expr  :asr.autospecs/expr)
     :storage-type   :asr.autospecs/storage-type
     :type           :asr.autospecs/ttype
     :abi            :asr.autospecs/abi
     :access         :asr.autospecs/access
     :presencs       :asr.autospecs/presence
     :value-attr     :asr.autospecs/bool
     )
    (fn []
      (tgen/fmap
       list*
       (tgen/tuple
        (tgen/return 'Variable)
        (tgen/fmap inc tgen/nat)
        (tgen/fmap symbol (s/gen ::identifier))
        (s/gen :asr.autospecs/intent)
        (tgen/one-of [(tgen/return ())
                      (s/gen :asr.autospecs/expr)])
        (tgen/one-of [(tgen/return ())
                      (s/gen :asr.autospecs/expr)])
        (s/gen :asr.autospecs/storage-type)
        (s/gen :asr.autospecs/ttype)
        (s/gen :asr.autospecs/abi)
        (s/gen :asr.autospecs/access)
        (s/gen :asr.autospecs/presence)
        (tgen/one-of [(tgen/return '.true.')
                      (tgen/return '.false.)])
        )))))

(gen/generate (s/gen ::temporary))
;; => (Variable 6 T9b1mV2065BfVvq1ks9v3T2B Unspecified [])
;; => (Variable
;;     13
;;     FHtS1LkB1vig5mO2XNRW49PS2t7u4
;;     Out
;;     [(StringChr
;;       "p348Pe"
;;       "F05T7pmxt9y7lj2c7IY2GiXROS6Rbx4"
;;       "hV3"
;;       "Whz174"
;;       "LElz79O"
;;       "j6G2AU9EO7Z1oPrMKMEzIcjAR730"
;;       "Bkf0LF7ziLNNB5Yu"
;;       "C722F0sgJx9Zhay"
;;       "y42o2YRDnU74fyLvcfn3UJ3H5y11"
;;       "giX"
;;       "XL3SIST8q7jR1Bo00vPlG3lu8OwQ"
;;       "xpC0vOW3r9wC4dD7"
;;       "UdaB9GKeEzaCRNL6nxggWL9J8729R0"
;;       "a4E32S2laJ"
;;       "is7laASca2Btq3mso6EVH68E9pD"
;;       "w13liZuuN1eX0u3PWsH5oQ"
;;       "kYD64")])
;; => (Variable 24 VA4JWqe55ED146LOhIN9JKn InOut [])
;; => (Variable 8 lHSY3 InOut)


;; (gen/generate (s/gen ::variable))
;; (gen/generate (s/gen #(and (int? %) (> % 0))))
;; (gen/generate (s/gen symbol?))
;; (s/exercise ::identifier)
;; (gen/generate (s/gen :asr.autospecs/expr))
