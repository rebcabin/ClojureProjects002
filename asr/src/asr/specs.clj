(ns asr.specs
  (:use [asr.utils]
        [asr.data]
        [asr.numbers]
        [asr.base-specs]  ; defines :asr.specs/identifier
        [asr.autospecs])
  (:require [clojure.spec.alpha            :as s   ]
            [clojure.zip                   :as z   ]
            [clojure.spec.gen.alpha        :as gen ]
            [clojure.test.check.generators :as tgen]))


;;  _    _         _   _  __ _
;; (_)__| |___ _ _| |_(_)/ _(_)___ _ _
;; | / _` / -_) ' \  _| |  _| / -_) '_|
;; |_\__,_\___|_||_\__|_|_| |_\___|_|

;;; To break a cycle of imports, :asr.specs/identifier is
;;; specified in asr.base-specs. The definition of that spec
;;; writes into this namespace, :asr.specs, from another
;;; namespace, :asr.base-specs.


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

;;; Filling this out as we go along

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


;;                   __        ___      __        _             __
;;   ___ __ ____ _  / /  ___  / (_) ___/ /__ ____(_)  _____ ___/ /
;;  (_-</ // /  ' \/ _ \/ _ \/ /   / _  / -_) __/ / |/ / -_) _  /
;; /___/\_, /_/_/_/_.__/\___/_(_)  \_,_/\__/_/ /_/|___/\__/\_,_/
;;     /___/
;;   __
;;  / /___ _____  ___
;; / __/ // / _ \/ -_)
;; \__/\_, / .__/\__/
;;    /___/_/

;;; NOTE: this is NOT derived-ttype [sic, two t's], rather, this
;;; derived-type is a symbol, specifically, the HEAD, DerivedType,
;;; kebabulated, of the TERM, symbol. However, derived-ttype uses
;;; derived-type.

;;; The question: whether sometimes "symbol_table" means a literal
;;; symbol table and sometimes means a numerical id of or
;;; reference to a symbol table. In the case of Variable, the type
;;; "symbol_table" definitely means "numerical ID of a symbol
;;; table that's reified somewhere else." The example in data.clj
;;; shows that to be the case. Perhaps we can conclude that
;;; whenever the _name_ of a variable of type symbol_table
;;; is "parent_symtab," then the value of the variable is actually
;;; of type positive uint. TODO: this isn't great practice. The
;;; type of a variable, not the name of a variable, should typify
;;; the value of the variable.
;;; https://github.com/lcompilers/lpython/issues/1224

(s/def ::derived-type
  (s/cat
   :head         #{'DerivedType}
   :symbol-table #{'symbol-table-placeholder}
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


;;      __        _             __  __  __
;;  ___/ /__ ____(_)  _____ ___/ / / /_/ /___ _____  ___
;; / _  / -_) __/ / |/ / -_) _  / / __/ __/ // / _ \/ -_)
;; \_,_/\__/_/ /_/|___/\__/\_,_/  \__/\__/\_, / .__/\__/
;;                                       /___/_/

(s/def ::derived-ttype
  (s/cat
   :head         #{'Derived}
   :derived-type ::derived-type
   :dims         ::dimensions))

(s/exercise ::derived-ttype 1)
;; => ([(Derived c [957])
;;      {:head Derived, :derived-type c, :dims [[:bigint 957]]}])


;;                   __        ___                         __
;;   ___ __ ____ _  / /  ___  / (_) ___ ___  __ ____ _    / /___ _____  ___
;;  (_-</ // /  ' \/ _ \/ _ \/ /   / -_) _ \/ // /  ' \  / __/ // / _ \/ -_)
;; /___/\_, /_/_/_/_.__/\___/_(_)  \__/_//_/\_,_/_/_/_/  \__/\_, / .__/\__/
;;     /___/                                                /___/_/

;;; Again, NOT enum ttype; we'll get there in a minute.

;; EnumType(
;;   symbol_table symtab,
;;   identifier   name,
;;   identifier*  members,
;;   abi          abi,
;;   access       access,
;;   ttype        type,
;;   symbol?      parent)

(s/def ::enum-type
  (s/cat
   :head         #{'EnumType}
   :symbol-table #{'symbol-table-placeholder}
   :nym          ::identifier
   :members      (s/* ::identifier)
   :abi          :asr.autospecs/abi
   :access       :asr.autospecs/access
   :type         ::ttype
   :parent       (s/or :derived-type ::identifier
                       :class-type   ::identifier)
   ))


;;                         __  __
;;  ___ ___  __ ____ _    / /_/ /___ _____  ___
;; / -_) _ \/ // /  ' \  / __/ __/ // / _ \/ -_)
;; \__/_//_/\_,_/_/_/_/  \__/\__/\_, / .__/\__/
;;                              /___/_/

(s/def ::enum-ttype
  (s/cat
   :head       #{'Enum}
   :enum-type  ::enum-type
   :dims       ::dimensions))

(s/exercise ::enum-ttype 1)
;; => ([(Enum b []) {:head Enum, :enum-type b, :dims []}])


;;                   __        ___       __               __
;;   ___ __ ____ _  / /  ___  / (_) ____/ /__ ____ ___   / /___ _____  ___
;;  (_-</ // /  ' \/ _ \/ _ \/ /   / __/ / _ `(_-<(_-<  / __/ // / _ \/ -_)
;; /___/\_, /_/_/_/_.__/\___/_(_)  \__/_/\_,_/___/___/  \__/\_, / .__/\__/
;;     /___/                                               /___/_/

;;; ClassType(
;;;   symbol_table symtab,
;;;   identifier name,
;;;   abi abi,
;;;   access access)

(s/def ::class-type
  (s/cat
   :head    #{'ClassType}
   :nym     ::identifier
   :abi     :asr.autospecs/abi
   :access  :asr.autospecs/access
   ))


;;       __               __  __
;;  ____/ /__ ____ ___   / /_/ /___ _____  ___
;; / __/ / _ `(_-<(_-<  / __/ __/ // / _ \/ -_)
;; \__/_/\_,_/___/___/  \__/\__/\_, / .__/\__/
;;                             /___/_/

(s/def ::class-ttype
  (s/cat
   :head       #{'Class}
   :class-type ::class-type
   :dims       ::dimensions))


;;      ___     __    __  __
;;  ___/ (_)___/ /_  / /_/ /___ _____  ___
;; / _  / / __/ __/ / __/ __/ // / _ \/ -_)
;; \_,_/_/\__/\__/  \__/\__/\_, / .__/\__/
;;                         /___/_/

(s/def ::dict-ttype
  (s/cat
   :head       #{'Dict}
   :key-type   ::ttype
   :value-type ::ttype))


;;               _      __            __  __
;;    ___  ___  (_)__  / /____ ____  / /_/ /___ _____  ___
;;   / _ \/ _ \/ / _ \/ __/ -_) __/ / __/ __/ // / _ \/ -_)
;;  / .__/\___/_/_//_/\__/\__/_/    \__/\__/\_, / .__/\__/
;; /_/                                     /___/_/


(s/def ::pointer-ttype
  (s/cat
   :head  #{'Pointer}
   :type  ::ttype))


;;            __        __  __
;;  _______  / /_____  / /_/ /___ _____  ___
;; / __/ _ \/ __/ __/ / __/ __/ // / _ \/ -_)
;; \__/ .__/\__/_/    \__/\__/\_, / .__/\__/
;;   /_/                     /___/_/

(s/def ::cptr-ttype
  (s/cat
   :head #{'CPtr}))


;;   __                                               __
;;  / /___ _____  ___   ___  ___ ________ ___ _  ___ / /____ ____
;; / __/ // / _ \/ -_) / _ \/ _ `/ __/ _ `/  ' \/ -_) __/ -_) __/
;; \__/\_, / .__/\__/ / .__/\_,_/_/  \_,_/_/_/_/\__/\__/\__/_/
;;    /___/_/        /_/

(s/def ::restriction
  (s/cat
   :head #{'Restrction}
   :rt   :asr.autospecs/trait))

(s/def ::type-parameter-ttype
  (s/cat
   :head #{'TypeParameter}
   :dims ::dimensions
   :rt   (s/* ::restriction)
   ))
;;  _   _                               _      __
;; | |_| |_ _  _ _ __  ___   _ _ ___ __| |___ / _|
;; |  _|  _| || | '_ \/ -_) | '_/ -_) _` / -_)  _|
;;  \__|\__|\_, | .__/\___| |_| \___\__,_\___|_|
;;          |__/|_|

;;; Filling this out as we go along

(s/def ::ttype
  (s/or :integer        ::integer-ttype
        :real           ::real-ttype
        :complex        ::complex-ttype
        :character      ::character-ttype
        :logical        ::logical-ttype
        :set            ::set-ttype
        :list           ::list-ttype
        :tuple          ::tuple-ttype
        :derived        ::derived-ttype
        :enum           ::enum-ttype
        :class          ::class-ttype
        :dict           ::dict-ttype
        :pointer        ::pointer-ttype
        :cptr           ::cptr-ttype
        :type-parameter ::type-parameter-ttype
        ))

;;; Here's the old, inadequate stuff:

(s/exercise :asr.autospecs/ttype)
;; => ([(List) (List)]
;;     [(List) (List)]
;;     [(Set) (Set)]
;;     [(Tuple u) (Tuple u)]
;;     [(Derived F35 x96m T) (Derived F35 x96m T)]
;;     [(CPtr GCTvV1) (CPtr GCTvV1)]
;;     [(Class) (Class)]
;;     [(Real Ir RK7XtLH j4 AG MA k7ZZyKU8 Phr)
;;      (Real Ir RK7XtLH j4 AG MA k7ZZyKU8 Phr)]
;;     [(Complex KFr H11qG Hk5Kn77 P4n25cl7u Ju)
;;      (Complex KFr H11qG Hk5Kn77 P4n25cl7u Ju)]
;;     [(Real ml9oHevc2) (Real ml9oHevc2)])

;;; Here's the new, fixed-up stuff:

(s/exercise ::ttype)
;; => ([(Dict (Integer 2 []) (Class ClassType t Source Private []))
;;      [:dict
;;       {:head Dict,
;;        :key-type [:integer {:head Integer, :kind 2, :dims []}],
;;        :value-type
;;        [:class
;;         {:head Class,
;;          :class-type
;;          {:head ClassType, :nym t, :abi Source, :access Private},
;;          :dims []}]}]]
;;     [(Set (Pointer (TypeParameter [1 0])))
;;      [:set
;;       {:head Set,
;;        :ttype
;;        [:pointer
;;         {:head Pointer,
;;          :type
;;          [:type-parameter
;;           {:head TypeParameter,
;;            :dims [[:nat-int 1] [:nat-int 0]]}]}]}]]
;;     [(Set (Character 1 []))
;;      [:set
;;       {:head Set,
;;        :ttype [:character {:head Character, :kind 1, :dims []}]}]]
;;     [(Real 8 []) [:real {:head Real, :kind 8, :dims []}]]
;;     [(Tuple
;;       (Derived
;;        DerivedType
;;        symbol-table-placeholder
;;        y99Jg
;;        eDC
;;        L3
;;        Dn
;;        Intrinsic
;;        Private
;;        ANdva
;;        [2675])
;;       (List
;;        (Set
;;         (Tuple
;;          (Enum
;;           EnumType
;;           symbol-table-placeholder
;;           so
;;           G1tlT
;;           bKOn9
;;           GS8
;;           BindC
;;           Private
;;           (Dict
;;            (Derived
;;             DerivedType
;;             symbol-table-placeholder
;;             o
;;             GFortranModule
;;             Private
;;             n5g5f
;;             [2 1])
;;            (Real 8 [0]))
;;           Q
;;           [1 3724932396])
;;          (Real 4 [])
;;          (Integer 1 []))))
;;       (List (Class ClassType TG GFortranModule Private [4 6]))
;;       (Logical 8 [1 0]))
;;      [:tuple
;;       {:head Tuple,
;;        :ttype
;;        [[:derived
;;          {:head Derived,
;;           :derived-type
;;           {:head DerivedType,
;;            :symbol-table symbol-table-placeholder,
;;            :nym y99Jg,
;;            :members [eDC L3 Dn],
;;            :abi Intrinsic,
;;            :access Private,
;;            :parent [:derived-type ANdva]},
;;           :dims [[:bigint 2675]]}]
;;         [:list
;;          {:head List,
;;           :ttype
;;           [:set
;;            {:head Set,
;;             :ttype
;;             [:tuple
;;              {:head Tuple,
;;               :ttype
;;               [[:enum
;;                 {:head Enum,
;;                  :enum-type
;;                  {:head EnumType,
;;                   :symbol-table symbol-table-placeholder,
;;                   :nym so,
;;                   :members [G1tlT bKOn9 GS8],
;;                   :abi BindC,
;;                   :access Private,
;;                   :type
;;                   [:dict
;;                    {:head Dict,
;;                     :key-type
;;                     [:derived
;;                      {:head Derived,
;;                       :derived-type
;;                       {:head DerivedType,
;;                        :symbol-table symbol-table-placeholder,
;;                        :nym o,
;;                        :abi GFortranModule,
;;                        :access Private,
;;                        :parent [:derived-type n5g5f]},
;;                       :dims [[:nat-int 2] [:nat-int 1]]}],
;;                     :value-type
;;                     [:real
;;                      {:head Real, :kind 8, :dims [[:nat-int 0]]}]}],
;;                   :parent [:derived-type Q]},
;;                  :dims [[:nat-int 1] [:bigint 3724932396]]}]
;;                [:real {:head Real, :kind 4, :dims []}]
;;                [:integer {:head Integer, :kind 1, :dims []}]]}]}]}]
;;         [:list
;;          {:head List,
;;           :ttype
;;           [:class
;;            {:head Class,
;;             :class-type
;;             {:head ClassType,
;;              :nym TG,
;;              :abi GFortranModule,
;;              :access Private},
;;             :dims [[:nat-int 4] [:nat-int 6]]}]}]
;;         [:logical
;;          {:head Logical,
;;           :kind 8,
;;           :dims [[:nat-int 1] [:nat-int 0]]}]]}]]
;;     [(Pointer (Complex 8 []))
;;      [:pointer
;;       {:head Pointer,
;;        :type [:complex {:head Complex, :kind 8, :dims []}]}]]
;;     [(TypeParameter
;;       [0 2052028268]
;;       Restrction
;;       SupportsZero
;;       Restrction
;;       SupportsPlus
;;       Restrction
;;       Any
;;       Restrction
;;       SupportsPlus)
;;      [:type-parameter
;;       {:head TypeParameter,
;;        :dims [[:nat-int 0] [:bigint 2052028268]],
;;        :rt
;;        [{:head Restrction, :rt SupportsZero}
;;         {:head Restrction, :rt SupportsPlus}
;;         {:head Restrction, :rt Any}
;;         {:head Restrction, :rt SupportsPlus}]}]]
;;     [(Pointer
;;       (Enum
;;        EnumType
;;        symbol-table-placeholder
;;        t0
;;        W
;;        Interactive
;;        Private
;;        (TypeParameter [])
;;        YP
;;        [13634627160747788 12409532]))
;;      [:pointer
;;       {:head Pointer,
;;        :type
;;        [:enum
;;         {:head Enum,
;;          :enum-type
;;          {:head EnumType,
;;           :symbol-table symbol-table-placeholder,
;;           :nym t0,
;;           :members [W],
;;           :abi Interactive,
;;           :access Private,
;;           :type [:type-parameter {:head TypeParameter, :dims []}],
;;           :parent [:derived-type YP]},
;;          :dims [[:bigint 13634627160747788] [:bigint 12409532]]}]}]]
;;     [(Logical 1 [234785814526])
;;      [:logical
;;       {:head Logical, :kind 1, :dims [[:bigint 234785814526]]}]]
;;     [(CPtr) [:cptr {:head CPtr}]])

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
