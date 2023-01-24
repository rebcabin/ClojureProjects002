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


;;; Hand-written specs for things that we don't want autospcc'ed.


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
  (s/coll-of (s/or :nat-int nat-int?,
                   :bigint :asr.numbers/bignat)
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

;;; A set acts like a function that tests membership.


(s/def ::integer-kind    #{1 2 4 8})
(s/def ::real-kind       #{4 8})
(s/def ::complex-kind    #{4 8})
(s/def ::character-kind  #{1})
(s/def ::logical-kind    #{1 4 8})

(s/def ::integer-ttype
  (s/cat
   :head #{'Integer}
   :grup ::integer-kind
   :dims ::dimensions))

#_
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
   :grup ::real-kind
   :dims ::dimensions))


(s/def ::complex-ttype
  (s/cat
   :head #{'Complex}
   :grup ::complex-kind
   :dims ::dimensions))


(s/def ::character-ttype
  (s/cat
   :head #{'Character}
   :grup ::character-kind
   :dims ::dimensions))


(s/def ::logical-ttype
  (s/cat
   :head #{'Logical}
   :grup ::logical-kind
   :dims ::dimensions))


;;; Defective ansatz; backpatch later to break recursive cycle.
;;; But, at this point, we need something defined for ttype so
;;; we can define the set, list, and tuple ttypes.

(s/def ::ttype
  (s/or :integer   ::integer-ttype
        :real      ::real-ttype
        :complex   ::complex-ttype
        :character ::character-ttype
        :logical   ::logical-ttype))

;; So far, only! It gets bigger

#_
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
  (s/with-gen
    (s/cat
     :head  #{'Tuple}
     :type (s/or :empty     empty?
                 :vector-of (s/* ::ttype)))
    (fn []
      (tgen/fmap
       list*
       (tgen/tuple
        (tgen/return 'Tuple)
        (tgen/vector (s/gen ::ttype)))))))

#_
(gen/sample (s/gen ::tuple-ttype))
;; => ((Tuple [])
;;     (Tuple [])
;;     (Tuple [(Real 8 []) (Complex 4 [207992 130290])])
;;     (Tuple [(Logical 4 [1213 0]) (Logical 4 [22])])
;;     (Tuple
;;      [(Integer 4 [1 0])
;;       (Logical 1 [939])
;;       (Real 8 [])
;;       (Logical 1 [1 77])])
;;     (Tuple [(Logical 1 [0]) (Real 8 []) (Character 1 [1 2])])
;;     (Tuple [])
;;     (Tuple [(Real 8 [0]) (Logical 8 []) (Real 8 [63 1])])
;;     (Tuple
;;      [(Logical 1 [12 358])
;;       (Real 4 [1])
;;       (Complex 4 [40262711142239])
;;       (Complex 4 [])
;;       (Logical 8 [])
;;       (Integer 4 [5174422140])
;;       (Complex 4 [])
;;       (Complex 4 [3109799523510])])
;;     (Tuple
;;      [(Integer 8 [64979707082 191214])
;;       (Logical 8 [0])
;;       (Complex 4 [])
;;       (Integer 4 [])
;;       (Real 8 [])
;;       (Logical 1 [104723020110])
;;       (Character 1 [10])]))

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

#_
(s/exercise ::ttype)
;; => ([(Logical 4 [35 8])
;;      [:logical
;;       {:head Logical, :grup 4, :dims [[:bigint 35] [:bigint 8]]}]]
;;     [(Character 1 [0 1])
;;      [:character
;;       {:head Character,
;;        :grup 1,
;;        :dims [[:nat-int 0] [:nat-int 1]]}]]
;;     [(Real 8 [1 83])
;;      [:real
;;       {:head Real, :grup 8, :dims [[:nat-int 1] [:bigint 83]]}]]
;;     [(Set (Logical 1 [0 1605884]))
;;      [:set
;;       {:head Set,
;;        :ttype
;;        [:logical
;;         {:head Logical,
;;          :grup 1,
;;          :dims [[:bigint 0] [:bigint 1605884]]}]}]]
;;     [(Set (Real 4 [2]))
;;      [:set
;;       {:head Set,
;;        :ttype [:real {:head Real, :grup 4, :dims [[:nat-int 2]]}]}]]
;;     [(Character 1 [2 0])
;;      [:character
;;       {:head Character,
;;        :grup 1,
;;        :dims [[:nat-int 2] [:nat-int 0]]}]]
;;     [(Integer 4 [2361])
;;      [:integer {:head Integer, :grup 4, :dims [[:bigint 2361]]}]]
;;     [(Tuple
;;       [(List (Set (Integer 1 [])))
;;        (Character 1 [1 14447796685686])])
;;      [:tuple
;;       {:head Tuple,
;;        :type
;;        [:vector-of
;;         [[:list
;;           {:head List,
;;            :ttype
;;            [:set
;;             {:head Set,
;;              :ttype
;;              [:integer {:head Integer, :grup 1, :dims []}]}]}]
;;          [:character
;;           {:head Character,
;;            :grup 1,
;;            :dims [[:nat-int 1] [:bigint 14447796685686]]}]]]}]]
;;     [(Tuple
;;       [(Integer 1 [19])
;;        (Logical 1 [])
;;        (Tuple
;;         [(Set (List (Complex 4 [24])))
;;          (Character 1 [114 1])
;;          (Character 1 [16180267365 6])
;;          (Character 1 [2 42959100])
;;          (Real 4 [])])
;;        (List (Real 4 [3]))
;;        (Set (Real 8 []))
;;        (Set (Complex 8 [23 2072723744791]))])
;;      [:tuple
;;       {:head Tuple,
;;        :type
;;        [:vector-of
;;         [[:integer {:head Integer, :grup 1, :dims [[:bigint 19]]}]
;;          [:logical {:head Logical, :grup 1, :dims []}]
;;          [:tuple
;;           {:head Tuple,
;;            :type
;;            [:vector-of
;;             [[:set
;;               {:head Set,
;;                :ttype
;;                [:list
;;                 {:head List,
;;                  :ttype
;;                  [:complex
;;                   {:head Complex,
;;                    :grup 4,
;;                    :dims [[:nat-int 24]]}]}]}]
;;              [:character
;;               {:head Character,
;;                :grup 1,
;;                :dims [[:nat-int 114] [:nat-int 1]]}]
;;              [:character
;;               {:head Character,
;;                :grup 1,
;;                :dims [[:bigint 16180267365] [:nat-int 6]]}]
;;              [:character
;;               {:head Character,
;;                :grup 1,
;;                :dims [[:nat-int 2] [:bigint 42959100]]}]
;;              [:real {:head Real, :grup 4, :dims []}]]]}]
;;          [:list
;;           {:head List,
;;            :ttype
;;            [:real {:head Real, :grup 4, :dims [[:nat-int 3]]}]}]
;;          [:set
;;           {:head Set,
;;            :ttype [:real {:head Real, :grup 8, :dims []}]}]
;;          [:set
;;           {:head Set,
;;            :ttype
;;            [:complex
;;             {:head Complex,
;;              :grup 8,
;;              :dims [[:nat-int 23] [:bigint 2072723744791]]}]}]]]}]]
;;     [(Complex 8 []) [:complex {:head Complex, :grup 8, :dims []}]])


;;                                     __   __
;;  _______  __ _  ___  ___  ___ ___ _/ /  / /__ ___
;; / __/ _ \/  ' \/ _ \/ _ \(_-</ _ `/ _ \/ / -_|_-<
;; \__/\___/_/_/_/ .__/\___/___/\_,_/_.__/_/\__/___/
;;              /_/

;;; private, reusable, composable mini-specs:


(s/def ::-members
  (s/or :vector-of (s/* ::identifier)
        :empty     empty?))


;; Don't check a symbol against empty first; order matters!
;; NOTE: An identifier will check against :derived-type first
;; and never make it to class-type.


(s/def ::-parent
  (s/or :parent (s/or :derived-type ::identifier
                      :class-type   ::identifier)
        :empty empty?))


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
  (s/with-gen
    (s/cat
     :head         #{'DerivedType}
     :symbol-table #{'symbol-table-placeholder}
     :nym          ::identifier
     :members      ::-members
     :abi          :asr.autospecs/abi
     :access       :asr.autospecs/access
     :parent       ::-parent)
    (fn []
      (tgen/fmap
       list*
       (tgen/tuple
        (tgen/return 'DerivedType)
        (tgen/return 'symbol-table-placeholder) ; FIXME
        (s/gen ::identifier)                    ; nym
        (tgen/vector (s/gen ::identifier))      ; members
        (s/gen :asr.autospecs/abi)              ; abi
        (s/gen :asr.autospecs/access)           ; access
        (tgen/one-of [(tgen/return ())
                      (s/gen ::identifier)]) ; parent
        )))))


#_(map second (s/exercise ::derived-type 4))
;; => ({:head DerivedType,
;;      :symbol-table symbol-table-placeholder,
;;      :nym i,
;;      :members [:vector-of []],
;;      :abi BindC,
;;      :access Public,
;;      :parent [:parent [:derived-type G]]}
;;     {:head DerivedType,
;;      :symbol-table symbol-table-placeholder,
;;      :nym s4,
;;      :members [:vector-of [EL]],
;;      :abi Interactive,
;;      :access Public,
;;      :parent [:empty ()]}
;;     {:head DerivedType,
;;      :symbol-table symbol-table-placeholder,
;;      :nym e,
;;      :members [:vector-of []],
;;      :abi Interactive,
;;      :access Public,
;;      :parent [:empty ()]}
;;     {:head DerivedType,
;;      :symbol-table symbol-table-placeholder,
;;      :nym T90,
;;      :members [:vector-of [P7Q6]],
;;      :abi Interactive,
;;      :access Public,
;;      :parent [:parent [:derived-type a]]})


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


#_(s/exercise ::derived-ttype 1)
;; =>  [(Derived
;;       DerivedType
;;       symbol-table-placeholder
;;       kY
;;       []
;;       Interactive
;;       Public
;;       A7
;;       [974])
;;      {:head Derived,
;;       :derived-type
;;       {:head DerivedType,
;;        :symbol-table symbol-table-placeholder,
;;        :nym kY,
;;        :members [:vector-of []],
;;        :abi Interactive,
;;        :access Public,
;;        :parent [:parent [:derived-type A7]]},
;;       :dims [[:bigint 974]]}])


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
  (s/with-gen
    (s/cat
     :head         #{'EnumType}
     :symbol-table #{'symbol-table-placeholder}
     :nym          ::identifier
     :members      ::-members
     :abi          :asr.autospecs/abi
     :access       :asr.autospecs/access
     :type         ::ttype
     :parent       ::-parent
     )
    (fn []
      (tgen/fmap
       list*
       (tgen/tuple
        (tgen/return 'EnumType)
        (tgen/return 'symbol-table-placeholder) ; FIXME
        (s/gen ::identifier)                    ; nym
        (tgen/vector (s/gen ::identifier))      ; members
        (s/gen :asr.autospecs/abi)              ; abi
        (s/gen :asr.autospecs/access)           ; access
        (s/gen ::ttype)
        (tgen/one-of [(tgen/return ())
                      (s/gen ::identifier)]) ; parent
        )))))


#_(map second (s/exercise ::derived-type 4))
;; => ({:head DerivedType,
;;      :symbol-table symbol-table-placeholder,
;;      :nym U,
;;      :members [:vector-of []],
;;      :abi Interactive,
;;      :access Public,
;;      :parent [:parent [:derived-type y]]}
;;     {:head DerivedType,
;;      :symbol-table symbol-table-placeholder,
;;      :nym q,
;;      :members [:vector-of [S]],
;;      :abi LFortranModule,
;;      :access Public,
;;      :parent [:parent [:derived-type u5]]}
;;     {:head DerivedType,
;;      :symbol-table symbol-table-placeholder,
;;      :nym F7,
;;      :members [:vector-of [TTI D]],
;;      :abi GFortranModule,
;;      :access Public,
;;      :parent [:parent [:derived-type bY]]}
;;     {:head DerivedType,
;;      :symbol-table symbol-table-placeholder,
;;      :nym keIQ,
;;      :members [:vector-of [nk]],
;;      :abi Source,
;;      :access Private,
;;      :parent [:empty ()]})


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


#_(s/exercise ::enum-ttype 1)
;; => ([(Enum
;;       EnumType
;;       symbol-table-placeholder
;;       h
;;       []
;;       Intrinsic
;;       Private
;;       (Derived
;;        DerivedType
;;        symbol-table-placeholder
;;        b
;;        []
;;        LFortranModule
;;        Public
;;        ()
;;        [1 0])
;;       L
;;       [1])
;;      {:head Enum,
;;       :enum-type
;;       {:head EnumType,
;;        :symbol-table symbol-table-placeholder,
;;        :nym h,
;;        :members [:vector-of []],
;;        :abi Intrinsic,
;;        :access Private,
;;        :type
;;        [:derived
;;         {:head Derived,
;;          :derived-type
;;          {:head DerivedType,
;;           :symbol-table symbol-table-placeholder,
;;           :nym b,
;;           :members [:vector-of []],
;;           :abi LFortranModule,
;;           :access Public,
;;           :parent [:empty ()]},
;;          :dims [[:nat-int 1] [:nat-int 0]]}],
;;        :parent [:parent [:derived-type L]]},
;;       :dims [[:bigint 1]]}])


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

#_
(s/exercise ::pointer-ttype 1)


;;            __        __  __
;;  _______  / /_____  / /_/ /___ _____  ___
;; / __/ _ \/ __/ __/ / __/ __/ // / _ \/ -_)
;; \__/ .__/\__/_/    \__/\__/\_, / .__/\__/
;;   /_/                     /___/_/


(s/def ::cptr-ttype
  (s/cat
   :head #{'CPtr}))


;;                __      _     __  _
;;   _______ ___ / /_____(_)___/ /_(_)__  ___
;;  / __/ -_|_-</ __/ __/ / __/ __/ / _ \/ _ \
;; /_/  \__/___/\__/_/ /_/\__/\__/_/\___/_//_/


(s/def ::restriction
  (s/cat
   :head #{'Restrction}
   :rt   :asr.autospecs/trait))


;;   __                                               __
;;  / /___ _____  ___   ___  ___ ________ ___ _  ___ / /____ ____
;; / __/ // / _ \/ -_) / _ \/ _ `/ __/ _ `/  ' \/ -_) __/ -_) __/
;; \__/\_, / .__/\__/ / .__/\_,_/_/  \_,_/_/_/_/\__/\__/\__/_/
;;    /___/_/        /_/
;;   __  __
;;  / /_/ /___ _____  ___
;; / __/ __/ // / _ \/ -_)
;; \__/\__/\_, / .__/\__/
;;        /___/_/


(s/def ::type-parameter-ttype
  (s/cat
   :head #{'TypeParameter}
   :dims ::dimensions
   :rt   (s/* ::restriction)
   ))


(s/exercise ::type-parameter-ttype 4)
;; => ([(TypeParameter [0])
;;      {:head TypeParameter, :dims [[:nat-int 0]]}]
;;     [(TypeParameter [] Restrction SupportsPlus)
;;      {:head TypeParameter,
;;       :dims [],
;;       :rt [{:head Restrction, :rt SupportsPlus}]}]
;;     [(TypeParameter [1 1] Restrction Divisible)
;;      {:head TypeParameter,
;;       :dims [[:nat-int 1] [:nat-int 1]],
;;       :rt [{:head Restrction, :rt Divisible}]}]
;;     [(TypeParameter [2 0])
;;      {:head TypeParameter, :dims [[:nat-int 2] [:nat-int 0]]}])


;;  _   _                               _      __
;; | |_| |_ _  _ _ __  ___   _ _ ___ __| |___ / _|
;; |  _|  _| || | '_ \/ -_) | '_/ -_) _` / -_)  _|
;;  \__|\__|\_, | .__/\___| |_| \___\__,_\___|_|
;;          |__/|_|

;;; This is everything, up to but not including symbol_table. This
;;; still has the placeholder for symbol_table.


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

#_(s/exercise :asr.autospecs/ttype)
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

;;; Here's the new, fixed-up stuff; we still have symbol-table
;;; placeholder:

#_
(s/exercise ::ttype 2)
;; => ([(Dict
;;       (Tuple [])
;;       (Dict
;;        (Dict
;;         (Integer 2 [])
;;         (Derived
;;          DerivedType
;;          symbol-table-placeholder
;;          G
;;          []
;;          Source
;;          Private
;;          a
;;          [5 0]))
;;        (Class ClassType s Interactive Public [])))
;;      [:dict
;;       {:head Dict,
;;        :key-type [:tuple {:head Tuple, :type [:empty []]}],
;;        :value-type
;;        [:dict
;;         {:head Dict,
;;          :key-type
;;          [:dict
;;           {:head Dict,
;;            :key-type [:integer {:head Integer, :grup 2, :dims []}],
;;            :value-type
;;            [:derived
;;             {:head Derived,
;;              :derived-type
;;              {:head DerivedType,
;;               :symbol-table symbol-table-placeholder,
;;               :nym G,
;;               :members [:vector-of []],
;;               :abi Source,
;;               :access Private,
;;               :parent [:parent [:derived-type a]]},
;;              :dims [[:bigint 5] [:nat-int 0]]}]}],
;;          :value-type
;;          [:class
;;           {:head Class,
;;            :class-type
;;            {:head ClassType,
;;             :nym s,
;;             :abi Interactive,
;;             :access Public},
;;            :dims []}]}]}]]
;;     [(Tuple []) [:tuple {:head Tuple, :type [:empty []]}]])
;; => ([(Tuple []) [:tuple {:head Tuple, :type [:empty []]}]]
;;     [(List (CPtr))
;;      [:list {:head List, :ttype [:cptr {:head CPtr}]}]])


;;                _         _ _                _      _    _
;;  ____  _ _ __ | |__  ___| (_) __ ____ _ _ _(_)__ _| |__| |___
;; (_-< || | '  \| '_ \/ _ \ |_  \ V / _` | '_| / _` | '_ \ / -_)
;; /__/\_, |_|_|_|_.__/\___/_(_)  \_/\__,_|_| |_\__,_|_.__/_\___|
;;     |__/

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
     :symbolic-value (s/or :expr  :asr.autospecs/expr
                           :empty empty?)
     :value          (s/or :expr  :asr.autospecs/expr
                           :empty empty?)
     :storage-type   :asr.autospecs/storage-type
     :type           :asr.autospecs/ttype
     :abi            :asr.autospecs/abi
     :access         :asr.autospecs/access
     :presence       :asr.autospecs/presence
     :value-attr     :asr.specs/bool
     )
    (fn []
      (tgen/fmap
       list*
       (tgen/tuple
        (tgen/return 'Variable)
        (tgen/fmap inc tgen/nat)                     ; parent-symtab
        (s/gen ::identifier)                         ; nym
        (s/gen :asr.autospecs/intent)                ; intent

        (tgen/vector (s/gen :asr.autospecs/expr))  ;;; FIXME ; symbolic-value
        (tgen/vector (s/gen :asr.autospecs/expr))  ;;; FIXME ; value

        (s/gen :asr.autospecs/storage-type)          ; storage-type

        (s/gen ::ttype)                              ;; don't auto!

        (s/gen :asr.autospecs/abi)                   ; abi
        (s/gen :asr.autospecs/access)                ; access
        (s/gen :asr.autospecs/presence)              ; presence
        (tgen/one-of [(tgen/return '.true.)          ; value-attr
                      (tgen/return '.false.)])
        )))))


;; FIXME: Cannot exercise ::variable too deeply lest stack
;; overflow. However, we can iterate over it.


#_(for [_ (range 4)]
 (s/exercise ::variable 1))
;; => (([(Variable
;;        1
;;        x
;;        In
;;        []
;;        []
;;        Allocatable
;;        (Tuple [])
;;        BindC
;;        Public
;;        Optional
;;        .false.)
;;       {:presence Optional,
;;        :value [:empty []],
;;        :type (Tuple []),
;;        :head Variable,
;;        :abi BindC,
;;        :intent In,
;;        :access Public,
;;        :nym x,
;;        :parent-symtab 1,
;;        :storage-type Allocatable,
;;        :symbolic-value [:empty []],
;;        :value-attr [:asr-bool .false.]}])
;;     ([(Variable
;;        1
;;        H
;;        Out
;;        []
;;        []
;;        Default
;;        (Dict
;;         (Set (TypeParameter [0]))
;;         (Dict (CPtr) (TypeParameter [])))
;;        LFortranModule
;;        Private
;;        Required
;;        .true.)
;;       {:presence Required,
;;        :value [:empty []],
;;        :type
;;        (Dict
;;         (Set (TypeParameter [0]))
;;         (Dict (CPtr) (TypeParameter []))),
;;        :head Variable,
;;        :abi LFortranModule,
;;        :intent Out,
;;        :access Private,
;;        :nym H,
;;        :parent-symtab 1,
;;        :storage-type Default,
;;        :symbolic-value [:empty []],
;;        :value-attr [:asr-bool .true.]}])
;;     ([(Variable
;;        1
;;        P
;;        Unspecified
;;        []
;;        []
;;        Save
;;        (Character 1 [0])
;;        Intrinsic
;;        Private
;;        Required
;;        .false.)
;;       {:presence Required,
;;        :value [:empty []],
;;        :type (Character 1 [0]),
;;        :head Variable,
;;        :abi Intrinsic,
;;        :intent Unspecified,
;;        :access Private,
;;        :nym P,
;;        :parent-symtab 1,
;;        :storage-type Save,
;;        :symbolic-value [:empty []],
;;        :value-attr [:asr-bool .false.]}])
;;     ([(Variable
;;        1
;;        r
;;        Out
;;        []
;;        []
;;        Parameter
;;        (Pointer (Integer 2 [0]))
;;        Intrinsic
;;        Private
;;        Optional
;;        .true.)
;;       {:presence Optional,
;;        :value [:empty []],
;;        :type (Pointer (Integer 2 [0])),
;;        :head Variable,
;;        :abi Intrinsic,
;;        :intent Out,
;;        :access Private,
;;        :nym r,
;;        :parent-symtab 1,
;;        :storage-type Parameter,
;;        :symbolic-value [:empty []],
;;        :value-attr [:asr-bool .true.]}]))
