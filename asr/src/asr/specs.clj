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

;;; No generator, yet. Just a validator.

(s/def ::symbol-table
  (s/cat :head #{'SymbolTable}
         :uid  pos?
         :dict (s/map-of keyword? :asr.autospecs/symbol
                         :conform-keys true #_"disallow duplicates")))


;;  _   _
;; | |_| |_ _  _ _ __  ___
;; |  _|  _| || | '_ \/ -_)
;;  \__|\__|\_, | .__/\___|
;;          |__/|_|

(s/def ::integer-kind    #{1 2 4 8})
(s/def ::real-kind       #{4 8})
(s/def ::complex-kind    #{4 8})
(s/def ::character-kind  #{1})
(s/def ::logical-kind    #{1 4 8})

(s/def ::integer-ttype
  (s/cat
   :head #{'Integer}
   :kind ::integer-kind
   :dims ::dimensions))

(gen/sample (s/gen ::integer-ttype))
;; => ((Integer 2 [25])
;;     (Integer 1 [])
;;     (Integer 4 [1])
;;     (Integer 1 [853])
;;     (Integer 8 [0])
;;     (Integer 8 [])
;;     (Integer 8 [])
;;     (Integer 1 [5])
;;     (Integer 2 [108])
;;     (Integer 8 []))

(s/def ::real-ttype
  (s/cat
   :head #{'Real}
   :kind ::real-kind
   :dims ::dimensions))

(s/def ::complex-ttype
  (s/cat
   :head #{'Complex}
   :kind ::complex-kind
   :dims ::dimensions))

(s/def ::character-ttype
  (s/cat
   :head #{'Character}
   :kind ::character-kind
   :dims ::dimensions))

(s/def ::logical-ttype
  (s/cat
   :head #{'Logical}
   :kind ::logical-kind
   :dims ::dimensions))

;;; Defective ansatz; backpatch later to break recursive cycle

(s/def ::ttype
  (s/or :integer   ::integer-ttype
        :real      ::real-ttype
        :complex   ::complex-ttype
        :character ::character-ttype
        :logical   ::logical-ttype))

(gen/sample (s/gen ::ttype))
;; => ((Character 1 [])
;;     (Logical 1 [1 29])
;;     (Logical 4 [52355])
;;     (Integer 1 [1])
;;     (Logical 1 [0])
;;     (Complex 8 [0 12])
;;     (Integer 2 [0])
;;     (Integer 2 [])
;;     (Complex 4 [])
;;     (Complex 8 []))

(s/def ::set-ttype
  (s/cat
   :head  #{'Set}
   :ttype ::ttype))

(s/def ::list-ttype
  (s/cat
   :head  #{'List}
   :ttype ::ttype))

(s/def ::tuple-ttype
  (s/cat
   :head  #{'Tuple}
   :ttype (s/* ::ttype)))

(gen/sample (s/gen ::tuple-ttype))
;; => ((Tuple)
;;     (Tuple (List (Character 1 [1])))
;;     (Tuple (Set (Integer 8 [])) (Complex 4 [0 7]))
;;     (Tuple)
;;     (Tuple
;;      (Tuple (Real 8 []))
;;      (List (Character 1 []))
;;      (Character 1 [7 1047388359946])
;;      (Integer 2 [1 60608278]))
;;     (Tuple
;;      (List (Logical 8 []))
;;      (List (List (Character 1 [17144])))
;;      (Complex 4 [])
;;      (Character 1 [2]))
;;     (Tuple (Character 1 [81532]) (List (Real 8 [])) (Real 8 [6 745]))
;;     (Tuple
;;      (Tuple
;;       (Complex 8 [])
;;       (Real 8 [1])
;;       (Complex 8 [])
;;       (Complex 8 [0])
;;       (List (Integer 8 [219382722219]))
;;       (Character 1 [25880128])
;;       (Complex 8 []))
;;      (Real 4 [])
;;      (Character 1 [13]))
;;     (Tuple
;;      (Logical 8 [])
;;      (Tuple
;;       (Real 4 [4 4])
;;       (Tuple
;;        (Logical 1 [20 431243])
;;        (Set (Integer 8 []))
;;        (Complex 4 [11 1])
;;        (Tuple))
;;       (Integer 2 [32])
;;       (Character 1 [16])
;;       (Set (Real 4 []))
;;       (Integer 8 [2517628 1])
;;       (Character 1 [3 56768332])
;;       (Real 8 []))
;;      (Set (List (Complex 4 [838788129960 0]))))
;;     (Tuple
;;      (Real 4 [168469188 23])
;;      (Real 8 [])
;;      (Set (Set (Complex 4 [3424])))
;;      (Logical 4 [13969 1])
;;      (Character 1 [1])
;;      (Real 4 [6 482188844534185])
;;      (Logical 8 [115])))
;; => ((Tuple)
;;     (Tuple)
;;     (Tuple (Character 1 [18]))
;;     (Tuple (Character 1 []) (Integer 2 []) (Real 4 []))
;;     (Tuple)
;;     (Tuple (Real 8 [366308]))
;;     (Tuple (Real 4 []))
;;     (Tuple (Real 4 [55]) (Complex 4 [1]))
;;     (Tuple (Integer 8 []) (Character 1 [2]) (Logical 4 [1156383710447]))
;;     (Tuple
;;      (Character 1 [])
;;      (Complex 4 [1])
;;      (Complex 8 [897 0])
;;      (Logical 1 [339410184938])
;;      (Real 4 [5])
;;      (Complex 8 [1 58331])
;;      (Logical 8 [])
;;      (Logical 8 [16890081 241])
;;      (Logical 1 [263663 1324679404525])))


(s/def ::ttype
  (s/or :integer   ::integer-ttype
        :real      ::real-ttype
        :complex   ::complex-ttype
        :character ::character-ttype
        :logical   ::logical-ttype
        :set       ::set-ttype
        :list      ::list-ttype
        :tuple     ::tuple-ttype
        ))


(gen/sample (s/gen ::ttype))
;; => ((Integer 1 [])
;;     (Set (Set (Logical 4 [0])))
;;     (Logical 1 [])
;;     (Set (Set (Real 8 [0 3025])))
;;     (Set (Complex 4 [1 576454]))
;;     (Real 8 [5])
;;     (Set (Integer 2 [516997 15]))
;;     (Complex 4 [14])
;;     (Integer 8 [66 288322])
;;     (Logical 1 [2 9]))
;; => ((Complex 4 [1999])
;;     (List (Set (Tuple (List (Real 4 [0])))))
;;     (Real 4 [2])
;;     (Real 4 [0 461312])
;;     (List
;;      (Tuple (Real 8 [452385 0]) (Complex 8 []) (Real 8 [1]) (Character 1 [])))
;;     (Logical 1 [])
;;     (Complex 4 [1])
;;     (Tuple
;;      (Integer 8 [13778708900817910 335])
;;      (Real 8 [3])
;;      (List (Integer 8 []))
;;      (Tuple
;;       (Character 1 [])
;;       (Tuple
;;        (Real 4 [])
;;        (Tuple
;;         (Complex 4 [564183751926])
;;         (Set (Logical 1 [4]))
;;         (List (Set (Real 4 [])))
;;         (Character 1 [])
;;         (Character 1 [24224899141586803 8798]))
;;        (Real 4 [])
;;        (Logical 8 [])
;;        (Logical 1 [28 238560])
;;        (Integer 2 [15 459231440654]))
;;       (Character 1 [1 0])
;;       (Real 4 [28760941 1047131])
;;       (Set
;;        (Tuple
;;         (Integer 1 [])
;;         (List (Character 1 [1266388707 3]))
;;         (Integer 4 [71233937802715]))))
;;      (Set (Logical 1 [])))
;;     (List (Integer 1 []))
;;     (Logical 1 [2 23]))


;;     _         _            _   _
;;  __| |___ _ _(_)_ _____ __| | | |_ _  _ _ __  ___
;; / _` / -_) '_| \ V / -_) _` | |  _| || | '_ \/ -_)
;; \__,_\___|_| |_|\_/\___\__,_|  \__|\_, | .__/\___|
;;                                    |__/|_|

;;; NOTE: this is NOT derived-ttype [sic, two t's], rather
;;; derived-type is a HEAD, DerivedType, kebabulated, of the TERM
;;; symbol. However, derived-ttype uses derived-type.

(s/def ::derived-type
  (s/cat
   :head         #{'DerivedType}
   :symbol-table #{'placeholder-symbol-table}
   :nym          ::identifier
   :members      (s/* ::identifier)
   :abi          :asr.autospecs/abi
   :access       :asr.autospecs/access
   :parent       (s/or :derived-type ::identifier
                       :class-type   ::identifier)
   ))

(s/exercise ::derived-type 1)
;; => ([(DerivedType placeholder-symbol-table C Source Private D)
;;      {:head DerivedType,
;;       :symbol-table placeholder-symbol-table,
;;       :nym C,
;;       :abi Source,
;;       :access Private,
;;       :parent [:derived-type D]}])


(s/def ::derived-ttype
  (s/cat
   :head         #{'Derived}
   :derived-type ::identifier
   :dims         ::dimensions))

(s/exercise ::derived-ttype 1)
;; => ([(Derived c [957])
;;      {:head Derived, :derived-type c, :dims [[:bigint 957]]}])


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
  (s/with-gen
    (s/cat
     :head           #{'Variable}
     :parent-symtab  pos?
     :nym            ::identifier ;symbol?            ; ::identifier
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
        (tgen/fmap inc tgen/nat)                     ; parent-symtab
        (tgen/fmap symbol (s/gen ::identifier))      ; nym
        (s/gen :asr.autospecs/intent)                ; intent
        (tgen/one-of [(tgen/return ())               ; symbolic-value
                      (s/gen :asr.autospecs/expr)])
        (tgen/one-of [(tgen/return ())               ; value
                      (s/gen :asr.autospecs/expr)])
        (s/gen :asr.autospecs/storage-type)
        (s/gen :asr.autospecs/ttype)
        (s/gen :asr.autospecs/abi)
        (s/gen :asr.autospecs/access)
        (s/gen :asr.autospecs/presence)
        (tgen/one-of [(tgen/return '.true.')         ; value-attr
                      (tgen/return '.false.)])
        )))))

(gen/sample (s/gen :asr.specs/dimensions))
;; => ([0] [354 0] [1] [] [] [5 1] [23089 4] [] [38 41229205] [])

(gen/sample (s/gen :asr.autospecs/ttype))
;; => ((Pointer)
;;     (Tuple)
;;     (Class j5 B6)
;;     (Integer)
;;     (Set B)
;;     (Enum Y W88e95)
;;     (Real E8t4d8 R)
;;     (List GsYGINHf YDF jky0ixn1 qk J SMJ1)
;;     (Character lv7w)
;;     (CPtr XXi2 f a7bh2ld0z d335Nq9 pDy tsI0xD2))

(gen/generate (s/gen ::variable))
;; => (Variable
;;     23
;;     B0dQK0KM3nLH2AhI
;;     Local                            ; intent
;;     ()                               ; symbolic-value
;;     ()                               ; value
;;     Parameter                        ; storage-type
;;     (Tuple aDt00G2S46GwSmLVu73Q6Ap)  ; ttype
;;     LFortranModule                   ; abi
;;     Public                           ; access
;;     Optional                         ; presence
;;     .false.)                         ; value-attr
;; => (Variable
;;     22
;;     kIhur6JLELa2T6S34VPY
;;     Unspecified
;;     ()
;;     ()
;;     Save
;;     (TypeParameter
;;      BXyWy
;;      Ag4i7VSz1
;;      M2E544sGj
;;      iqtO2k481I9LYlAsKjB0S2z5YmV6D8)
;;     GFortranModule
;;     Private
;;     Optional
;;     .false.)


;; (gen/generate (s/gen ::variable))
;; (gen/generate (s/gen #(and (int? %) (> % 0))))
;; (gen/generate (s/gen symbol?))
;; (s/exercise ::identifier)
;; (gen/generate (s/gen :asr.autospecs/expr))
