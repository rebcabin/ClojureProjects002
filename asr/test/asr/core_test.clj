(ns asr.core-test
  (:use [asr.core]
        [asr.asr]
        [asr.data]
        [asr.parsed]
        [asr.numbers]
        [asr.arithmetic]
        [asr.autospecs]
        [asr.expr.synnasr]
        [asr.expr.semnasr]
        [asr.expr.semsem]
        [asr.utils])

  (:require [clojure.math                  :as    math   ]
            [clojure.test                  :refer :all   ]
            [clojure.string                :as    string ]
            [clojure.spec.alpha            :as    s      ]
            [clojure.spec.gen.alpha        :as    gen    ]
            [clojure.test.check.generators :as    tgen   ]
            [clojure.test.check.properties :as    tprop  ]
            [asr.lpython                   :as    lpython]))


(def ONETEST           1)
(def NTESTS           50) ;; Smaller for routine touch-checks
(def RECURSION-LIMIT   4) ;; ditto
(def LONGTESTS      1000) ;; Bigger for inline stresses


;;        _   _ _
;;  _   _| |_(_) |___
;; | | | | __| | / __|
;; | |_| | |_| | \__ \
;;  \__,_|\__|_|_|___/

(deftest kebab-test
  (testing "kebab-case from asr.utils"
    (is (= (asr.utils/nskw-kebab-from 'TranslationUnit)
           :asr.autospecs/translation-unit))
    (is (= (asr.utils/nskw-kebab-from "TranslationUnit")
           :asr.autospecs/translation-unit))
    (is (thrown?
         Exception
         (asr.utils/nskw-kebab-from :should-fail)))))


;;           _           _
;; __      _| |__   ___ | | ___   ___ _ __   ___  ___
;; \ \ /\ / / '_ \ / _ \| |/ _ \ / __| '_ \ / _ \/ __|
;;  \ V  V /| | | | (_) | |  __/ \__ \ |_) |  __/ (__
;;   \_/\_/ |_| |_|\___/|_|\___| |___/ .__/ \___|\___|
;;                                   |_|

(deftest whole-spec-test
  (testing "whole examples pass trivial spec"
    (is (s/valid? list? asr.data/expr-01-211000))
    (is (s/valid? list? asr.data/test_vars_01))))


;;                      _      _
;;  ___ _ __   ___  ___| | ___| |_
;; / __| '_ \ / _ \/ __| |/ _ \ __|
;; \__ \ |_) |  __/ (__| |  __/ |_
;; |___/ .__/ \___|\___|_|\___|\__|
;;     |_|

(deftest shallow-map-from-speclet-test

  (testing "shallow map from speclet"

    (is (= (asr.parsed/shallow-map-from-speclet (speclets 3))
           {:ASDL-FORMS
            '([:ASDL-SYMCONST "Public"] [:ASDL-SYMCONST "Private"]),
            :ASDL-TERM "access"}))

    (is (= (asr.parsed/shallow-map-from-speclet (speclets 0))
           {:ASDL-FORMS
            '([:ASDL-COMPOSITE
               [:ASDL-HEAD "TranslationUnit"]
               [:ASDL-ARGS
                [:ASDL-DECL
                 [:ASDL-TYPE "symbol_table"]
                 [:ASDL-NYM "global_scope"]]
                [:ASDL-DECL
                 [:ASDL-TYPE "node" [:STAR]]
                 [:ASDL-NYM "items"]]]]),
            :ASDL-TERM "unit"}))

    (is (= (asr.parsed/shallow-map-from-speclet (speclets 22))
           {:ASDL-FORMS
            '([:ASDL-TUPLE
               [:ASDL-ARGS
                [:ASDL-DECL
                 [:ASDL-TYPE "identifier"]
                 [:ASDL-NYM "arg"]]]]),
            :ASDL-TERM "attribute_arg"}))))


(deftest hashmap-from-speclet-test
  (testing "hashmap from speclet"
    (is (= (hashmap-from-speclet (speclets 0))
           {:ASDL-TERM "unit",
            :ASDL-FORMS
            '({:ASDL-COMPOSITE
               {:ASDL-HEAD "TranslationUnit",
                :ASDL-ARGS
                ({:ASDL-TYPE "symbol_table",
                  :MULTIPLICITY :asr.parsed/once,
                  :ASDL-NYM "global_scope"}
                 {:ASDL-TYPE "node",
                  :MULTIPLICITY :asr.parsed/zero-or-more,
                  :ASDL-NYM "items"})}})}))

    (is (= (hashmap-from-speclet (speclets 3))
           {:ASDL-TERM "access",
            :ASDL-FORMS
            '({:ASDL-SYMCONST "Public"}
              {:ASDL-SYMCONST "Private"})}))))


;;        _ _               _
;;   __ _| | |   __ _ _   _| |_ ___  ___ _ __   ___  ___ ___
;;  / _` | | |  / _` | | | | __/ _ \/ __| '_ \ / _ \/ __/ __|
;; | (_| | | | | (_| | |_| | || (_) \__ \ |_) |  __/ (__\__ \
;;  \__,_|_|_|  \__,_|\__,_|\__\___/|___/ .__/ \___|\___|___/
;;                                      |_|

(deftest all-terms-test
  (testing "check all 28 terms"
    (is (= 28 (->> big-list-of-stuff (map :term) set count)))
    (is (= #{
             :asr.autospecs/abi
             :asr.autospecs/access
             :asr.autospecs/alloc_arg
             :asr.autospecs/array_index
             :asr.autospecs/arraybound
             :asr.autospecs/attribute
             :asr.autospecs/attribute_arg
             :asr.autospecs/binop
             :asr.autospecs/call_arg
             :asr.autospecs/case_stmt
             :asr.autospecs/cast_kind
             :asr.autospecs/cmpop
             :asr.autospecs/deftype
             :asr.autospecs/dimension
             :asr.autospecs/do_loop_head
             :asr.autospecs/expr
             :asr.autospecs/integerboz
             :asr.autospecs/intent
             :asr.autospecs/logicalbinop
             :asr.autospecs/presence
             :asr.autospecs/restriction
             :asr.autospecs/stmt
             :asr.autospecs/storage_type
             :asr.autospecs/symbol
             :asr.autospecs/tbind
             :asr.autospecs/trait
             :asr.autospecs/ttype
             :asr.autospecs/unit
             }
           (set (map :term big-list-of-stuff))))))


(deftest all-kinds-test
  (testing "check all 3 kinds"
    (is (= #{:ASDL-SYMCONST :ASDL-COMPOSITE :ASDL-TUPLE}
           (set (map :kind big-list-of-stuff))))))


(defn- not-asr-tuple [kw]
  (not (re-matches #"asr-tuple[0-9]+" (name kw))))


(deftest all-heads-test
  (testing "check all 227 heads, minus 6 asr-tuples"
    (is (= (- 227 6)
           (->> big-list-of-stuff
                (map :head)
                (filter not-asr-tuple)
                set
                count)))
    (is (= #{
             :asr.autospecs/Add
             :asr.autospecs/Allocatable
             :asr.autospecs/Allocate
             :asr.autospecs/And
             :asr.autospecs/Any
             :asr.autospecs/ArrayBound
             :asr.autospecs/ArrayConstant
             :asr.autospecs/ArrayItem
             :asr.autospecs/ArrayMatMul
             :asr.autospecs/ArrayPack
             :asr.autospecs/ArrayReshape
             :asr.autospecs/ArraySection
             :asr.autospecs/ArraySize
             :asr.autospecs/ArrayTranspose
             :asr.autospecs/Assert
             :asr.autospecs/Assign
             :asr.autospecs/Assignment
             :asr.autospecs/Associate
             :asr.autospecs/AssociateBlock
             :asr.autospecs/AssociateBlockCall
             :asr.autospecs/Attribute
             :asr.autospecs/Binary
             :asr.autospecs/Bind
             :asr.autospecs/BindC
             :asr.autospecs/BitAnd
             :asr.autospecs/BitCast
             :asr.autospecs/BitLShift
             :asr.autospecs/BitOr
             :asr.autospecs/BitRShift
             :asr.autospecs/BitXor
             :asr.autospecs/Block
             :asr.autospecs/BlockCall
             :asr.autospecs/CLoc
             :asr.autospecs/CPtr
             :asr.autospecs/CPtrToPointer
             :asr.autospecs/CaseStmt
             :asr.autospecs/CaseStmt_Range
             :asr.autospecs/Cast
             :asr.autospecs/Character
             :asr.autospecs/CharacterToInteger
             :asr.autospecs/CharacterToList
             :asr.autospecs/CharacterToLogical
             :asr.autospecs/Class
             :asr.autospecs/ClassProcedure
             :asr.autospecs/ClassType
             :asr.autospecs/Complex
             :asr.autospecs/ComplexBinOp
             :asr.autospecs/ComplexCompare
             :asr.autospecs/ComplexConstant
             :asr.autospecs/ComplexConstructor
             :asr.autospecs/ComplexIm
             :asr.autospecs/ComplexRe
             :asr.autospecs/ComplexToComplex
             :asr.autospecs/ComplexToLogical
             :asr.autospecs/ComplexToReal
             :asr.autospecs/ComplexUnaryMinus
             :asr.autospecs/CustomOperator
             :asr.autospecs/Cycle
             :asr.autospecs/Default
             :asr.autospecs/Derived
             :asr.autospecs/DerivedRef
             :asr.autospecs/DerivedType
             :asr.autospecs/DerivedTypeConstructor
             :asr.autospecs/Dict
             :asr.autospecs/DictConstant
             :asr.autospecs/DictInsert
             :asr.autospecs/DictItem
             :asr.autospecs/DictLen
             :asr.autospecs/DictPop
             :asr.autospecs/Div
             :asr.autospecs/Divisible
             :asr.autospecs/DoConcurrentLoop
             :asr.autospecs/DoLoop
             :asr.autospecs/Enum
             :asr.autospecs/EnumRef
             :asr.autospecs/EnumType
             :asr.autospecs/EnumTypeConstructor
             :asr.autospecs/Eq
             :asr.autospecs/Eqv
             :asr.autospecs/ErrorStop
             :asr.autospecs/Exit
             :asr.autospecs/ExplicitDeallocate
             :asr.autospecs/ExternalSymbol
             :asr.autospecs/FileClose
             :asr.autospecs/FileInquire
             :asr.autospecs/FileOpen
             :asr.autospecs/FileRead
             :asr.autospecs/FileRewind
             :asr.autospecs/FileWrite
             :asr.autospecs/Flush
             :asr.autospecs/ForAllSingle
             :asr.autospecs/Function
             :asr.autospecs/FunctionCall
             :asr.autospecs/GFortranModule
             :asr.autospecs/GenericProcedure
             :asr.autospecs/GetPointer
             :asr.autospecs/GoTo
             :asr.autospecs/GoToTarget
             :asr.autospecs/Gt
             :asr.autospecs/GtE
             :asr.autospecs/Hex
             :asr.autospecs/If
             :asr.autospecs/IfArithmetic
             :asr.autospecs/IfExp
             :asr.autospecs/Implementation
             :asr.autospecs/ImplicitDeallocate
             :asr.autospecs/ImpliedDoLoop
             :asr.autospecs/In
             :asr.autospecs/InOut
             :asr.autospecs/Integer
             :asr.autospecs/IntegerBOZ
             :asr.autospecs/IntegerBinOp
             :asr.autospecs/IntegerBitLen
             :asr.autospecs/IntegerBitNot
             :asr.autospecs/IntegerCompare
             :asr.autospecs/IntegerConstant
             :asr.autospecs/IntegerToCharacter
             :asr.autospecs/IntegerToComplex
             :asr.autospecs/IntegerToInteger
             :asr.autospecs/IntegerToLogical
             :asr.autospecs/IntegerToReal
             :asr.autospecs/IntegerUnaryMinus
             :asr.autospecs/Interactive
             :asr.autospecs/Interface
             :asr.autospecs/Intrinsic
             :asr.autospecs/LBound
             :asr.autospecs/LFortranModule
             :asr.autospecs/List
             :asr.autospecs/ListAppend
             :asr.autospecs/ListClear
             :asr.autospecs/ListConcat
             :asr.autospecs/ListConstant
             :asr.autospecs/ListInsert
             :asr.autospecs/ListItem
             :asr.autospecs/ListLen
             :asr.autospecs/ListPop
             :asr.autospecs/ListRemove
             :asr.autospecs/ListSection
             :asr.autospecs/Local
             :asr.autospecs/Logical
             :asr.autospecs/LogicalBinOp
             :asr.autospecs/LogicalCompare
             :asr.autospecs/LogicalConstant
             :asr.autospecs/LogicalNot
             :asr.autospecs/LogicalToCharacter
             :asr.autospecs/LogicalToInteger
             :asr.autospecs/LogicalToReal
             :asr.autospecs/Lt
             :asr.autospecs/LtE
             :asr.autospecs/Module
             :asr.autospecs/Mul
             :asr.autospecs/NEqv
             :asr.autospecs/NamedExpr
             :asr.autospecs/NotEq
             :asr.autospecs/Nullify
             :asr.autospecs/Octal
             :asr.autospecs/Optional
             :asr.autospecs/Or
             :asr.autospecs/Out
             :asr.autospecs/OverloadedBinOp
             :asr.autospecs/OverloadedCompare
             :asr.autospecs/Parameter
             :asr.autospecs/Pointer
             :asr.autospecs/PointerToCPtr
             :asr.autospecs/Pow
             :asr.autospecs/Print
             :asr.autospecs/Private
             :asr.autospecs/Program
             :asr.autospecs/Public
             :asr.autospecs/Real
             :asr.autospecs/RealBinOp
             :asr.autospecs/RealCompare
             :asr.autospecs/RealConstant
             :asr.autospecs/RealToCharacter
             :asr.autospecs/RealToComplex
             :asr.autospecs/RealToInteger
             :asr.autospecs/RealToLogical
             :asr.autospecs/RealToReal
             :asr.autospecs/RealUnaryMinus
             :asr.autospecs/Required
             :asr.autospecs/Restriction
             :asr.autospecs/Return
             :asr.autospecs/ReturnVar
             :asr.autospecs/Save
             :asr.autospecs/Select
             :asr.autospecs/Set
             :asr.autospecs/SetConstant
             :asr.autospecs/SetInsert
             :asr.autospecs/SetLen
             :asr.autospecs/SetPop
             :asr.autospecs/SetRemove
             :asr.autospecs/Source
             :asr.autospecs/Stop
             :asr.autospecs/StringChr
             :asr.autospecs/StringCompare
             :asr.autospecs/StringConcat
             :asr.autospecs/StringConstant
             :asr.autospecs/StringItem
             :asr.autospecs/StringLen
             :asr.autospecs/StringOrd
             :asr.autospecs/StringRepeat
             :asr.autospecs/StringSection
             :asr.autospecs/Sub
             :asr.autospecs/SubroutineCall
             :asr.autospecs/SupportsPlus
             :asr.autospecs/SupportsZero
             :asr.autospecs/TemplateBinOp
             :asr.autospecs/TemplateToReal
             :asr.autospecs/TranslationUnit
             :asr.autospecs/Tuple
             :asr.autospecs/TupleConstant
             :asr.autospecs/TupleItem
             :asr.autospecs/TupleLen
             :asr.autospecs/TypeParameter
             :asr.autospecs/UBound
             :asr.autospecs/Unspecified
             :asr.autospecs/Var
             :asr.autospecs/Variable
             :asr.autospecs/Where
             :asr.autospecs/WhileLoop
             :asr.autospecs/Xor
             }
           (set
            (filter
             not-asr-tuple
             (map :head big-list-of-stuff)))))))


(deftest install-all-symconst-specs-test
  (testing "install all 72 symconst specs"
    (is (= 72 (->> symconst-stuffs set count)))
    (is (= #{
             :asr.autospecs/add
             :asr.autospecs/allocatable
             :asr.autospecs/and
             :asr.autospecs/any
             :asr.autospecs/binary
             :asr.autospecs/bind-c
             :asr.autospecs/bit-and
             :asr.autospecs/bit-l-shift
             :asr.autospecs/bit-or
             :asr.autospecs/bit-r-shift
             :asr.autospecs/bit-xor
             :asr.autospecs/character-to-integer
             :asr.autospecs/character-to-list
             :asr.autospecs/character-to-logical
             :asr.autospecs/complex-to-complex
             :asr.autospecs/complex-to-logical
             :asr.autospecs/complex-to-real
             :asr.autospecs/default
             :asr.autospecs/div
             :asr.autospecs/divisible
             :asr.autospecs/eq
             :asr.autospecs/eqv
             :asr.autospecs/g-fortran-module
             :asr.autospecs/gt
             :asr.autospecs/gt-e
             :asr.autospecs/hex
             :asr.autospecs/implementation
             :asr.autospecs/in
             :asr.autospecs/in-out
             :asr.autospecs/integer-to-character
             :asr.autospecs/integer-to-complex
             :asr.autospecs/integer-to-integer
             :asr.autospecs/integer-to-logical
             :asr.autospecs/integer-to-real
             :asr.autospecs/interactive
             :asr.autospecs/interface
             :asr.autospecs/intrinsic
             :asr.autospecs/l-bound
             :asr.autospecs/l-fortran-module
             :asr.autospecs/local
             :asr.autospecs/logical-to-character
             :asr.autospecs/logical-to-integer
             :asr.autospecs/logical-to-real
             :asr.autospecs/lt
             :asr.autospecs/lt-e
             :asr.autospecs/mul
             :asr.autospecs/n-eqv
             :asr.autospecs/not-eq
             :asr.autospecs/octal
             :asr.autospecs/optional
             :asr.autospecs/or
             :asr.autospecs/out
             :asr.autospecs/parameter
             :asr.autospecs/pow
             :asr.autospecs/private
             :asr.autospecs/public
             :asr.autospecs/real-to-character
             :asr.autospecs/real-to-complex
             :asr.autospecs/real-to-integer
             :asr.autospecs/real-to-logical
             :asr.autospecs/real-to-real
             :asr.autospecs/required
             :asr.autospecs/return-var
             :asr.autospecs/save
             :asr.autospecs/source
             :asr.autospecs/sub
             :asr.autospecs/supports-plus
             :asr.autospecs/supports-zero
             :asr.autospecs/template-to-real
             :asr.autospecs/u-bound
             :asr.autospecs/unspecified
             :asr.autospecs/xor
             }
           (->> symconst-stuffs
                (map asr.autospecs/spec-from-symconst-stuff)
                (map eval)
                set)))))


(deftest install-symconst-stuffss-by-term-test
  (testing "installing symconst stuffss by term"
    (is (= #{:asr.autospecs/deftype
             :asr.autospecs/arraybound
             :asr.autospecs/storage-type
             :asr.autospecs/binop
             :asr.autospecs/presence
             :asr.autospecs/integerboz
             :asr.autospecs/logicalbinop
             :asr.autospecs/trait
             :asr.autospecs/abi
             :asr.autospecs/cast-kind
             :asr.autospecs/intent
             :asr.autospecs/access
             :asr.autospecs/cmpop}
           (->> symconst-stuffss-by-term
                (map symconst-spec-for-term)
                (map eval)
                set)))))


(deftest all-heads-for-symbols-test
  (testing "all 13 heads for symbols"
    (is (= '#{Block               Function            GenericProcedure
              ExternalSymbol      CustomOperator      ClassProcedure
              ClassType           Module              EnumType
              DerivedType         AssociateBlock      Variable
              Program}
           (asr.autospecs/heads-for-composite :asr.autospecs/symbol)))))


(deftest all-heads-for-stmts-test
  (testing "all 42 heads for composite stmts"
    (is (= '#{ListClear           Print               ExplicitDeallocate
              SetRemove           SetInsert           Select
              SubroutineCall      Where               FileRewind
              FileInquire         CPtrToPointer       Assert
              ListAppend          Stop                ListInsert

              ForAllSingle        ImplicitDeallocate  Allocate
              GoTo                AssociateBlockCall  BlockCall
              Nullify             Exit                Cycle
              Assignment          Assign              If
              Flush               WhileLoop           ListRemove

              FileOpen            Associate           FileRead
              DictInsert          DoLoop              FileWrite
              GoToTarget          Return              ErrorStop
              DoConcurrentLoop    FileClose           IfArithmetic}
           (heads-for-composite :asr.autospecs/stmt)))))


(deftest all-heads-for-exprs-test
  (testing "all 73 heads for expr composite"
    (is (= '#{ComplexConstructor  ListConstant        Var
              IntegerBinOp        IntegerUnaryMinus   ComplexUnaryMinus
              RealCompare         IntegerBitNot       IntegerBitLen
              IfExp               ImpliedDoLoop       RealConstant
              NamedExpr           ArrayTranspose      DictConstant

              EnumRef             StringSection       FunctionCall
              PointerToCPtr       ListLen             DictLen
              DerivedRef          StringCompare       StringLen
              StringConstant      StringOrd           IntegerCompare
              ListItem            TupleConstant       LogicalCompare

              ListPop             TupleLen            OverloadedBinOp
              ArrayMatMul         RealBinOp           SetConstant
              IntegerBOZ          OverloadedCompare   BitCast
              EnumTypeConstructor ArrayReshape        LogicalConstant
              LogicalBinOp        Cast                ListConcat

              SetPop              TupleItem           StringChr
              GetPointer          CLoc                TemplateBinOp
              ArraySize           ArrayItem           ComplexIm
              DictPop             DictItem            ArrayPack
              ComplexConstant     ArrayConstant       LogicalNot

              DerivedTypeConstructor  RealUnaryMinus  IntegerConstant
              StringRepeat            ArraySection    ListSection
              StringItem              StringConcat    SetLen
              ComplexCompare          ArrayBound      ComplexRe
              ComplexBinOp}
           (heads-for-composite :asr.autospecs/expr)))))


;;  _     _         _ _     _       _     _
;; | |__ (_) __ _  | (_)___| |_    | |__ (_) __ _   _ __ ___   __ _ _ __
;; | '_ \| |/ _` | | | / __| __|   | '_ \| |/ _` | | '_ ` _ \ / _` | '_ \
;; | |_) | | (_| | | | \__ \ |_ _  | |_) | | (_| | | | | | | | (_| | |_) |
;; |_.__/|_|\__, | |_|_|___/\__( ) |_.__/|_|\__, | |_| |_| |_|\__,_| .__/
;;          |___/              |/           |___/                  |_|

(deftest count-of-big-list-of-stuff
  (testing "count of big list of stuff"
    (is (= 227 (count big-list-of-stuff)))))


(deftest count-of-big-map-of-speclets
  (testing "count of big map of speclets"
    (is (= 28 (count big-map-of-speclets-from-terms)))))


(deftest count-of-composite-exprs
  (testing "count of composite exprs"
    (is (= 73
           (->> big-map-of-speclets-from-terms
                :asr.autospecs/expr
                (map :ASDL-COMPOSITE)
                count)))))


;;  ___ _________    ____ ___ _   _    ___  ____
;; |_ _|___ /___ \  | __ )_ _| \ | |  / _ \|  _ \
;;  | |  |_ \ __) | |  _ \| ||  \| | | | | | |_) |
;;  | | ___) / __/  | |_) | || |\  | | |_| |  __/
;; |___|____/_____| |____/___|_| \_|  \___/|_|


;;  ___     _                    ___ _      ___
;; |_ _|_ _| |_ ___ __ _ ___ _ _| _ |_)_ _ / _ \ _ __
;;  | || ' \  _/ -_) _` / -_) '_| _ \ | ' \ (_) | '_ \
;; |___|_||_\__\___\__, \___|_| |___/_|_||_\___/| .__/
;;                 |___/                        |_|

(let [test-vector '(IntegerBinOp
                    (IntegerBinOp
                     (IntegerConstant
                      2 (Integer 4 []))
                     Add
                     (IntegerConstant
                      3
                      (Integer 4 []))
                     (Integer 4 [])
                     (IntegerConstant
                      5 (Integer 4 [])))
                    Mul
                    (IntegerConstant
                     5 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant
                     25 (Integer 4 [])))]

  (deftest IntegerBinop-conformance-test
    (testing "IntegerBinop conforms to dummy expr spec and
              to evolving integer-bin-op spec."
      (is (= test-vector
             (s/conform :asr.autospecs/expr test-vector)))
      (is (s/valid? :asr.autospecs/integer-bin-op test-vector)))
    (testing "specs may not generate"
      (is (not (nil? (gen/generate (s/gen :asr.autospecs/expr)))))
      (is (thrown?
           Exception
           (gen/generate (s/gen :asr.autospecs/integer-bin-op)))))))


(let [integer-bin-op-stuff
      '({:head :asr.autospecs/IntegerBinOp,
         :term :asr.autospecs/expr,
         :kind :ASDL-COMPOSITE,
         :form
         {:ASDL-COMPOSITE
          {:ASDL-HEAD "IntegerBinOp",
           :ASDL-ARGS
           ({:ASDL-TYPE "expr", :MULTIPLICITY :asr.parsed/once,
             :ASDL-NYM "left"}
            {:ASDL-TYPE "binop", :MULTIPLICITY :asr.parsed/once,
             :ASDL-NYM "op"}
            {:ASDL-TYPE "expr", :MULTIPLICITY :asr.parsed/once,
             :ASDL-NYM "right"}
            {:ASDL-TYPE "ttype", :MULTIPLICITY :asr.parsed/once,
             :ASDL-NYM "type"}
            {:ASDL-TYPE "expr",
             :MULTIPLICITY :asr.parsed/at-most-once,
             :ASDL-NYM "value"})}}})]

  (deftest IntegerBinop-stuff-test
    (testing "stuff for IntegerBinOp has expected data"
      (is (= integer-bin-op-stuff
             (filter #(= (:head %) :asr.autospecs/IntegerBinOp)
                     big-list-of-stuff))))))


;;  _     _                      _   _
;; (_)_ _| |_ ___ __ _ ___ _ _  | |_| |_ _  _ _ __  ___
;; | | ' \  _/ -_) _` / -_) '_| |  _|  _| || | '_ \/ -_)
;; |_|_||_\__\___\__, \___|_|    \__|\__|\_, | .__/\___|
;;               |___/                   |__/|_|

(deftest integer-ttype-conformance
  (testing "Integer ttype conformance"
    (is (s/valid? :asr.expr.semnasr/integer-ttype
                  '(Integer 4 [])                ))
    (is (s/valid? :asr.expr.semnasr/integer-ttype
                  '(Integer 1 [1 2])             ))
    (is (s/valid? :asr.expr.semnasr/integer-ttype
                  '(Integer 8 [] [1 2] [] [3 4]) ))
    (is (not (s/valid? :asr.expr.semnasr/integer-ttype  ;; NOT case!
                  '(Integer 4)                   )))
    (is (s/valid? :asr.expr.semnasr/integer-ttype
                  '(Integer 2 ())                ))
    (is (s/valid? :asr.expr.semnasr/integer-ttype
                  '(Integer 2 (1 2))             ))
    (is (s/valid? :asr.expr.semnasr/integer-ttype
                  '(Integer 2 () (1 2) () (3 4)) ))
    (is (every?
         (partial s/valid? :asr.expr.semnasr/integer-ttype)
         (for [_ (range NTESTS)]
           (gen/generate (s/gen :asr.expr.semnasr/integer-ttype)))))
    ))


;;  _ _______                  _            _
;; (_)__ /_  )  __ ___ _ _  __| |_ __ _ _ _| |_
;; | ||_ \/ /  / _/ _ \ ' \(_-<  _/ _` | ' \  _|
;; |_|___/___| \__\___/_||_/__/\__\__,_|_||_\__|

(deftest i32-constant-conformance
  (testing "i32-constant conformance:"
    (testing "list"
      (is (s/valid? :asr.expr.semnasr/i32-constant
                    '(IntegerConstant 5 (Integer 4 [])))))
    (testing "vector"
      (is (s/valid? :asr.expr.semnasr/i32-constant
                    '[IntegerConstant 5 (Integer 4 [])])))))


(deftest i32-constant-non-conformance
  (testing "i32-constant NON-conformance:"
    (testing "wrong \"kind\", i.e., integer size"
      (is (not (s/valid? :asr.expr.semnasr/i32-constant
                         '(IntegerConstant 5 (Integer 8 []))))))
    (testing "wrong type of value"
      (is (not (s/valid? :asr.expr.semnasr/i32-constant
                         '(IntegerConstant 5.0 (Integer 4 []))))))
    (testing "wrong tag"
      (is (not (s/valid? :asr.expr.semnasr/i32-constant
                         '(foobarConstant 5 (Integer 4 []))))))
    (testing "wrong ttype"
      (is (not (s/valid? :asr.expr.semnasr/i32-constant
                         '(IntegerConstant 5 (Float 4 []))))))
    (testing "missing ttype"
      (is (not (s/valid? :asr.expr.semnasr/i32-constant
                         '(IntegerConstant 5)))))
    (testing "wrong structure"
      (is (not (s/valid? :asr.expr.semnasr/i32-constant
                         43)))
      (is (not (s/valid? :asr.expr.semnasr/i32-constant
                         '((IntegerConstant 5.0 (Integer 8 [])))))))))


;;  _ _______   _    _
;; (_)__ /_  ) | |__(_)_ _    ___ _ __   ___ ___ _ __  _ _  __ _ ____ _
;; | ||_ \/ /  | '_ \ | ' \  / _ \ '_ \ (_-</ -_) '  \| ' \/ _` (_-< '_|
;; |_|___/___| |_.__/_|_||_| \___/ .__/ /__/\___|_|_|_|_||_\__,_/__/_|
;;                               |_|

(deftest i32-bin-op-conformance
  (testing "conformance to structural integer bin-op specs (not correct arithmetic):"
    (testing "base case, base-answer"
      (is (let [test-vector '[IntegerBinOp
                             [IntegerConstant 2 (Integer 4 [])]  ; left
                             Add                                 ; binop
                             [IntegerConstant 3 (Integer 4 [])]  ; right
                             (Integer 4 [])                      ; answer-ttype
                             [IntegerConstant 5 (Integer 4 [])]]] ; answer
           (s/valid? :asr.expr.semnasr/i32-bin-op test-vector))))

    (testing "Base case, no-answer"
      (is (let [test-vector '[IntegerBinOp
                             [IntegerConstant 2 (Integer 4 [])] ; left
                             Add                                ; binop
                             [IntegerConstant 3 (Integer 4 [])] ; right
                             (Integer 4 []) ]]                  ; answer-ttype
           (s/valid? :asr.expr.semnasr/i32-bin-op test-vector))))

    (testing "Recurse left, no answers"
      (is (let [test-vector '[IntegerBinOp
                             [IntegerBinOp                      ; recuse:
                              [IntegerConstant 2 (Integer 4 [])] ; left
                              Add                                ; binop
                              [IntegerConstant 3 (Integer 4 [])] ; right
                              (Integer 4 [])]                    ; ttype
                             Add                                 ; binop
                             [IntegerConstant 3 (Integer 4 [])]  ; right
                             (Integer 4 [])] ]                   ; ttype
           (s/valid? :asr.expr.semnasr/i32-bin-op test-vector))))

    (testing "Base case, doubly recursive-answer"
      (is (let [test-vector '[IntegerBinOp
                             [IntegerConstant 2 (Integer 4 [])] ; left
                             Add                                ; binop
                             [IntegerConstant 3 (Integer 4 [])] ; right
                             (Integer 4 [])                     ; answer-ttype
                             [IntegerBinOp                      ; recurse answer
                              [IntegerBinOp                     ; recurse again
                               [IntegerConstant 2 (Integer 4 [])] ; left
                               Add                                ; binop
                               [IntegerConstant 3 (Integer 4 [])] ; right
                               (Integer 4 [])]                    ; ttype
                              Add                                 ; binop
                              [IntegerConstant 3 (Integer 4 [])]  ; right
                              (Integer 4 []) ]]]                  ; ttype
           (s/valid? :asr.expr.semnasr/i32-bin-op test-vector))))

    (testing "Recursive left, base-answer"
      (is (let [test-vector '[IntegerBinOp
                             [IntegerBinOp                      ; recur-left
                              [IntegerConstant 2 (Integer 4 [])] ;   left
                              Add                                ;   binop
                              [IntegerConstant 3 (Integer 4 [])] ;   right
                              (Integer 4 []) ;   answer-ttype
                              [IntegerConstant 5 (Integer 4 [])]] ;   answer
                             Mul                                  ; binop
                             [IntegerConstant 5 (Integer 4 [])]   ; right
                             (Integer 4 [])                       ; answer-ttype
                             [IntegerConstant 25 (Integer 4 [])]]] ; answer
           (s/valid? :asr.expr.semnasr/i32-bin-op test-vector))))

    (testing "Recursive right, base-answer"
      (is (let [test-vector '[IntegerBinOp
                             [IntegerConstant 2 (Integer 4 [])] ; left
                             Add                                ; binop
                             [IntegerBinOp                      ; recur-right
                              [IntegerConstant 3 (Integer 4 [])] ;   left
                              Add                                ;   binop
                              [IntegerConstant 4 (Integer 4 [])] ;   right
                              (Integer 4 []) ;   answer-ttype
                              [IntegerConstant 42 (Integer 4 [])]] ;   answer
                             (Integer 4 [])                       ; answer-ttype
                             [IntegerConstant 42 (Integer 4 [])]]] ; answer
           (s/valid? :asr.expr.semnasr/i32-bin-op test-vector))))

    (testing "Recursive right, recursive answer"
      (is (let [test-vector '[IntegerBinOp
                             [IntegerConstant 2 (Integer 4 [])]
                             Add
                             [IntegerBinOp ; recursive right       ; recur-right
                              [IntegerConstant 3 (Integer 4 [])]
                              Add
                              [IntegerConstant 4 (Integer 4 [])]
                              (Integer 4 [])]
                             (Integer 4 []) ; answer type
                             [IntegerBinOp  ; recurse answer
                              [IntegerConstant 3 (Integer 4 [])]
                              Mul
                              [IntegerConstant 4 (Integer 4 [])]
                              (Integer 4 [])
                              [IntegerConstant 25 (Integer 4 [])]]]]
           (s/valid? :asr.expr.semnasr/i32-bin-op test-vector))))

    (testing "base, recursive answer"
      (is (let [test-vector '[IntegerBinOp
                             [IntegerConstant 2 (Integer 4 [])] ; base
                             Add
                             [IntegerConstant 4 (Integer 4 [])] ; base
                             (Integer 4 [])                     ; answer ttype
                             [IntegerBinOp ; recursive answer
                              [IntegerConstant 3 (Integer 4 [])]
                              Mul
                              [IntegerConstant 4 (Integer 4 [])]
                              (Integer 4 [])
                              [IntegerConstant 25 (Integer 4 [])]]]]
           (s/valid? :asr.expr.semnasr/i32-bin-op test-vector))))

    (testing "big-honkin' case"
      (is (s/valid?
          :asr.expr.semnasr/i32-bin-op
          '[IntegerBinOp
            [IntegerBinOp
             [IntegerBinOp
              [IntegerBinOp
               [IntegerBinOp
                [IntegerConstant 544338735 (Integer 4 [])]
                BitXor
                [IntegerConstant -1100782011 (Integer 4 [])]
                (Integer 4 [])]
               Mul
               [IntegerConstant -971625237 (Integer 4 [])]
               (Integer 4 [])
               [IntegerConstant 1980305294 (Integer 4 [])]]
              Sub
              [IntegerConstant 1776882703 (Integer 4 [])]
              (Integer 4 [])]
             BitLShift
             [IntegerBinOp
              [IntegerConstant 1019633051 (Integer 4 [])]
              BitRShift
              [IntegerBinOp
               [IntegerConstant -1661136848 (Integer 4 [])]
               BitXor
               [IntegerBinOp
                [IntegerConstant -390475637 (Integer 4 [])]
                BitOr
                [IntegerConstant -627011181 (Integer 4 [])]
                (Integer 4 [])
                [IntegerConstant 591935352 (Integer 4 [])]]
               (Integer 4 [])
               [IntegerBinOp
                [IntegerConstant -903848770 (Integer 4 [])]
                BitRShift
                [IntegerConstant -256687998 (Integer 4 [])]
                (Integer 4 [])]]
              (Integer 4 [])
              [IntegerBinOp
               [IntegerBinOp
                [IntegerConstant -147005213 (Integer 4 [])]
                BitXor
                [IntegerConstant -2030399113 (Integer 4 [])]
                (Integer 4 [])]
               BitAnd
               [IntegerConstant -1249059602 (Integer 4 [])]
               (Integer 4 [])]]
             (Integer 4 [])
             [IntegerConstant -1164337677 (Integer 4 [])]]
            Add
            [IntegerConstant 1917318437 (Integer 4 [])]
            (Integer 4 [])])))

    (testing "Stress. Guaranteed by clojure.spec, but ya' never know."
      (is (every?
          (partial s/valid? :asr.expr.semnasr/i32-bin-op)
          (binding [s/*recursion-limit* RECURSION-LIMIT]
            (for [_ (range NTESTS)]
              (-> :asr.expr.semnasr/i32-bin-op
                  s/gen
                  gen/generate))))))))


;;  _     _                                                 _
;; (_)_ _| |_ ___ __ _ ___ _ _   __ _ ___ _ _  ___ _ _ __ _| |_ ___ _ _ ___
;; | | ' \  _/ -_) _` / -_) '_| / _` / -_) ' \/ -_) '_/ _` |  _/ _ \ '_(_-<
;; |_|_||_\__\___\__, \___|_|   \__, \___|_||_\___|_| \__,_|\__\___/_| /__/
;;               |___/          |___/

(deftest integer-generators
  (testing "::i8, ::i8nz, ::i16, etc."
    (is (not-any? zero?
                  (gen/sample (s/gen :asr.numbers/i8nz) NTESTS)))
    (is (not-any? zero?
                  (gen/sample (s/gen :asr.numbers/i16nz) NTESTS)))
    (is (not-any? zero?
                  (gen/sample (s/gen :asr.numbers/i32nz) NTESTS)))
    (is (not-any? zero?
                  (gen/sample (s/gen :asr.numbers/i64nz) NTESTS)))
    (is (not-any? #(> % Byte/MAX_VALUE)
                  (gen/sample (s/gen :asr.numbers/i8) NTESTS)))
    (is (not-any? #(> % Short/MAX_VALUE)
                  (gen/sample (s/gen :asr.numbers/i16) NTESTS)))
    (is (not-any? #(> % Integer/MAX_VALUE)
                  (gen/sample (s/gen :asr.numbers/i32) NTESTS)))
    (is (not-any? #(> % Long/MAX_VALUE)
                  (gen/sample (s/gen :asr.numbers/i64) NTESTS)))
    (is (not-any? #(< % Byte/MIN_VALUE)
                  (gen/sample (s/gen :asr.numbers/i8) NTESTS)))
    (is (not-any? #(< % Short/MIN_VALUE)
                  (gen/sample (s/gen :asr.numbers/i16) NTESTS)))
    (is (not-any? #(< % Integer/MIN_VALUE)
                  (gen/sample (s/gen :asr.numbers/i32) NTESTS)))
    (is (not-any? #(< % Long/MIN_VALUE)
                  (gen/sample (s/gen :asr.numbers/i64) NTESTS)))))


;;  _     _                                          _   _
;; (_)_ _| |_ ___ __ _ ___ _ _   _____ ____ ___ _ __| |_(_)___ _ _  ___
;; | | ' \  _/ -_) _` / -_) '_| / -_) \ / _/ -_) '_ \  _| / _ \ ' \(_-<
;; |_|_||_\__\___\__, \___|_|   \___/_\_\__\___| .__/\__|_\___/_||_/__/
;;               |___/                         |_|

(deftest integer-exceptions
  (testing "small integer overflow exceptions, negative and
  positive; Need different exceptions for the various types:"
    (testing "Long/MIN - 1"
      (is "long overflow"
          (try (long (dec Long/MIN_VALUE))
               (catch ArithmeticException e
                 (-> e ex-message)))))
    (testing "Integer/MIN - 1"
      (is "integer overflow"
          (try (int (dec Integer/MIN_VALUE))
               (catch ArithmeticException e
                 (-> e ex-message)))))
    (testing "Short/MIN - 1"
      (is "Value out of range for short: -32769"
          (try (short (dec Short/MIN_VALUE))
               (catch IllegalArgumentException e
                 (->> e ex-message)))))
    (testing "Byte/MIN - 1"
      (is "Value of of range for byte: -129"
          (try (byte (dec Byte/MIN_VALUE))
               (catch IllegalArgumentException e
                 (-> e ex-message)))))
    (testing "Long/MAX + 1"
      (is "long overflow"
          (try (long (inc Long/MAX_VALUE))
               (catch ArithmeticException e
                 (-> e ex-message)))))
    (testing "Integer/MAX + 1"
      (is "integer overflow"
          (try (int (inc Integer/MAX_VALUE))
               (catch ArithmeticException e
                 (-> e ex-message)))))
    (testing "Short/MAX + 1"
      (is "Value out of range for short: 32768"
          (try (short (inc Short/MAX_VALUE))
               (catch IllegalArgumentException e
                 (->> e ex-message)))))
    (testing "Byte/MAX + 1"
      (is "Value of of range for byte: 128"
          (try (byte (inc Byte/MAX_VALUE))
               (catch IllegalArgumentException e
                 (-> e ex-message)))))
    (is (thrown?
         ArithmeticException
         (inc Long/MAX_VALUE)))))


;;  _     _                      _
;; (_)_ _| |_ ___ __ _ ___ _ _  | |_ _  _ _ __  ___ ___
;; | | ' \  _/ -_) _` / -_) '_| |  _| || | '_ \/ -_|_-<
;; |_|_||_\__\___\__, \___|_|    \__|\_, | .__/\___/__/
;;               |___/               |__/|_|

(deftest integer-types
  (testing "integer types, clojure and java")
  (is clojure.lang.BigInt
      (type (+' 1 Long/MAX_VALUE)))
  ;; long and short forms for the java.language types
  (is java.lang.Byte    (type Byte/MAX_VALUE))
  (is java.lang.Short   (type Short/MAX_VALUE))
  (is java.lang.Integer (type Integer/MAX_VALUE))
  (is java.lang.Long    (type Long/MAX_VALUE)))


;;  _ _______    _    _
;; (_)__ /_  )__| |__(_)_ _ ___ ___ _ __ ___ ___ ___ _ __  ___ ___ _ __
;; | ||_ \/ /___| '_ \ | ' \___/ _ \ '_ \___(_-</ -_) '  \(_-</ -_) '  \
;; |_|___/___|  |_.__/_|_||_|  \___/ .__/   /__/\___|_|_|_/__/\___|_|_|_|
;;                                 |_|

(deftest i32-bin-op-leaf-semsem-conformance
  (testing "conformance to :asr.core/i32-bin-op-leaf"
    (is (s/valid? :asr.expr.semsem/i32-bin-op-leaf
                  '(IntegerBinOp
                    (IntegerConstant -1 (Integer 4 []))
                    BitOr
                    (IntegerConstant 0 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant -1 (Integer 4 [])))))
    (is (s/valid? :asr.expr.semsem/i32-bin-op-leaf
                  '(IntegerBinOp
                    (IntegerConstant -1 (Integer 4 []))
                    BitLShift
                    (IntegerConstant 0 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant -1 (Integer 4 [])))))
    (is (s/valid? :asr.expr.semsem/i32-bin-op-leaf
                  '(IntegerBinOp
                    (IntegerConstant 1 (Integer 4 []))
                    BitRShift
                    (IntegerConstant 0 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 1 (Integer 4 [])))))
    (is (s/valid? :asr.expr.semsem/i32-bin-op-leaf
                  '(IntegerBinOp
                    (IntegerConstant -4 (Integer 4 []))
                    BitAnd
                    (IntegerConstant -4 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant -4 (Integer 4 [])))))
    (is (s/valid? :asr.expr.semsem/i32-bin-op-leaf
                  '(IntegerBinOp
                    (IntegerConstant 0 (Integer 4 []))
                    Add
                    (IntegerConstant 0 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 0 (Integer 4 [])))))
    (is (s/valid? :asr.expr.semsem/i32-bin-op-leaf
                  '(IntegerBinOp
                    (IntegerConstant 5 (Integer 4 []))
                    Add
                    (IntegerConstant -1 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 4 (Integer 4 [])))))
    (is (s/valid? :asr.expr.semsem/i32-bin-op-leaf
                  '(IntegerBinOp
                    (IntegerConstant 61 (Integer 4 []))
                    BitRShift
                    (IntegerConstant -4 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 0 (Integer 4 [])))))
    (is (s/valid? :asr.expr.semsem/i32-bin-op-leaf
                  '(IntegerBinOp
                    (IntegerConstant 13 (Integer 4 []))
                    BitOr
                    (IntegerConstant -1 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant -1 (Integer 4 [])))))
    (is (s/valid? :asr.expr.semsem/i32-bin-op-leaf
                  '(IntegerBinOp
                    (IntegerConstant 0 (Integer 4 []))
                    Div
                    (IntegerConstant 246 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 0 (Integer 4 [])))))
    (is (s/valid? :asr.expr.semsem/i32-bin-op-leaf
                  '(IntegerBinOp
                    (IntegerConstant 14 (Integer 4 []))
                    Mul
                    (IntegerConstant -54 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant -756 (Integer 4 [])))))
    (is (s/valid? :asr.expr.semsem/i32-bin-op-leaf
                  '(IntegerBinOp
                    (IntegerConstant -82 (Integer 4 []))
                    Pow
                    (IntegerConstant -25 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 0 (Integer 4 [])))))
    (is (s/valid? :asr.expr.semsem/i32-bin-op-leaf
                  '(IntegerBinOp
                    (IntegerConstant -43 (Integer 4 []))
                    Pow
                    (IntegerConstant -1002 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 0 (Integer 4 [])))))
    (is (s/valid? :asr.expr.semsem/i32-bin-op-leaf
                  '(IntegerBinOp
                    (IntegerConstant 2316 (Integer 4 []))
                    BitXor
                    (IntegerConstant -3 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant -2319 (Integer 4 [])))))
    (is (s/valid? :asr.expr.semsem/i32-bin-op-leaf
                  '(IntegerBinOp
                    (IntegerConstant 622 (Integer 4 []))
                    Sub
                    (IntegerConstant 34 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 588 (Integer 4 [])))))
    (is (s/valid? :asr.expr.semsem/i32-bin-op-leaf
                  '(IntegerBinOp
                    (IntegerConstant 17 (Integer 4 []))
                    BitRShift
                    (IntegerConstant -2403 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 0 (Integer 4 [])))))
    (is (s/valid? :asr.expr.semsem/i32-bin-op-leaf
                  '(IntegerBinOp
                    (IntegerConstant 3207 (Integer 4 []))
                    Sub
                    (IntegerConstant -1108 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 4315 (Integer 4 [])))))
    (is (s/valid? :asr.expr.semsem/i32-bin-op-leaf
                  '(IntegerBinOp
                    (IntegerConstant -1 (Integer 4 []))
                    Add
                    (IntegerConstant 284 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 283 (Integer 4 [])))))
    (is (s/valid? :asr.expr.semsem/i32-bin-op-leaf
                  '(IntegerBinOp
                    (IntegerConstant -657 (Integer 4 []))
                    BitOr
                    (IntegerConstant -356 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant -1 (Integer 4 [])))))
    (is (s/valid? :asr.expr.semsem/i32-bin-op-leaf
                  '(IntegerBinOp
                    (IntegerConstant -131974 (Integer 4 []))
                    Pow
                    (IntegerConstant 630 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 0 (Integer 4 [])))))
    (testing "TODO: a nil value conforms, but we want to eliminate
                   nils in the generator"
      (is (s/valid? :asr.expr.semsem/i32-bin-op-leaf
                   '(IntegerBinOp
                     (IntegerConstant -41056462 (Integer 4 []))
                     Pow
                     (IntegerConstant -266578627 (Integer 4 []))
                     (Integer 4 [])
                     (IntegerConstant nil (Integer 4 []))))))))


(deftest i32-bin-op-leaf-semsem-non-conformance
  (testing "non-conformance of :asr.expr.semsem/i32-bin-op-leaf:"
    (testing "wrong structure"
      (is (not (s/valid? :asr.expr.semsem/i32-bin-op-leaf
                         'foo))))
    (testing "wrong keywords"
      (is (not (s/valid? :asr.expr.semsem/i32-bin-op-leaf
                         '(bag
                           (foo 42 bar)
                           Pow
                           (baz 6 bar)
                           (boo 4 far)
                           (qux 32 pgh))))))
    (testing "wrong value"
      (is (not (s/valid? :asr.expr.semsem/i32-bin-op-leaf
                         '(IntegerBinOp
                           (IntegerConstant -131974 (Integer 4 []))
                           Pow
                           (IntegerConstant 630 (Integer 4 []))
                           (Integer 4 [])
                           (IntegerConstant 43 (Integer 4 [])))))))
    ;; Check that a nested expr is not valid. It will fail due to
    ;; structural (syntactical?) constraints on the head, not due
    ;; to types and kinds (semnasr) on the preimage or
    ;; arithmetic (sem-sem) constraints on the values.
    (testing "nested structure (not leaf)"
      (is (not (s/valid? :asr.expr.semsem/i32-bin-op-leaf
                         '(IntegerBinOp
                           (IntegerBinOp
                            (IntegerConstant -657 (Integer 4 []))
                            BitOr
                            (IntegerConstant -356 (Integer 4 []))
                            (Integer 4 [])
                            (IntegerConstant -1 (Integer 4 [])))
                           Pow
                           (IntegerConstant 630 (Integer 4 []))
                           (Integer 4 [])
                           (IntegerConstant 0 (Integer 4 [])))))))))


(deftest i32-bin-op-leaf-semsem-gen-pluggable-test
  (testing "all non-nils are valid"
    (is (every?
         (partial s/valid? :asr.expr.semsem/i32-bin-op-leaf)
         (filter (comp not nil?)
                 (gen/sample
                  (s/gen :asr.expr.semsem/i32-bin-op-leaf)
                  NTESTS))))))


;;       _                     _    _       _ _______
;;  _ __| |_  _ __ _ __ _ __ _| |__| |___  (_)__ /_  )
;; | '_ \ | || / _` / _` / _` | '_ \ / -_) | ||_ \/ /
;; | .__/_|\_,_\__, \__, \__,_|_.__/_\___| |_|___/___|
;; |_|         |___/|___/
;;           _ _   _              _   _
;;  __ _ _ _(_) |_| |_  _ __  ___| |_(_)__
;; / _` | '_| |  _| ' \| '  \/ -_)  _| / _|
;; \__,_|_| |_|\__|_||_|_|_|_\___|\__|_\__|

(deftest maybe-fast-unchecked-i32-exp-test
  (testing "fast unchecked exp i32:"
    (testing "unchecked spinning on random-ish values"
      (is (= 1387939935  (maybe-fast-unchecked-i32-exp -481 211)))
      (is (= -1387939935 (maybe-fast-unchecked-i32-exp  481 211))))
    (testing "converging to 0 on pos or neg powers of 2"
      (is (zero? (maybe-fast-unchecked-i32-exp   32   499)))
      (is (zero? (maybe-fast-unchecked-i32-exp  -32   499)))
      (is (zero? (maybe-fast-unchecked-i32-exp   32    -1))))
    (testing "underflow to nil"
      (is (nil?  (maybe-fast-unchecked-i32-exp   32  -499)))
      (is (nil?  (maybe-fast-unchecked-i32-exp  -32  -499)))
      (is (nil?  (maybe-fast-unchecked-i32-exp 1234 -2345))))
    (testing "1 to negative powers = 1"
      (is (= 1   (maybe-fast-unchecked-i32-exp    1  -499))))
    (testing "0^0 == 1"
      (is (= 1   (maybe-fast-unchecked-i32-exp    0     0))))
    (testing "exception on 0 to a negative power"
      (is (nil?  (maybe-fast-unchecked-i32-exp    0    -1))))))


;;                  _                    _            _ _______
;;  _ __  __ _ _  _| |__  ___  __ ____ _| |_  _ ___  (_)__ /_  )
;; | '  \/ _` | || | '_ \/ -_) \ V / _` | | || / -_) | ||_ \/ /
;; |_|_|_\__,_|\_, |_.__/\___|  \_/\__,_|_|\_,_\___| |_|___/___|
;;             |__/

;; Co-cursively calls spec ::i32-bin-op.

(deftest maybe-value-i32-semsem-test
  (testing "various nil-punning returns:"
    (testing "good value i32bop"
      (is (zero?
           (fetch-value-i32-bin-op-semsem
            '(IntegerBinOp
              (IntegerConstant -131974 (Integer 4 []))
              Pow
              (IntegerConstant 630 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))))))
    (testing "good value i32con"
      (is (= -131974
             (fetch-value-i32-bin-op-semsem
              '(IntegerConstant -131974 (Integer 4 []))))))
    (testing "wrong \"kind\""
      (is (nil?
           (fetch-value-i32-bin-op-semsem
            '(IntegerConstant -131974 (Integer 8 []))))))
    (testing "seriously bad structure"
      (is (nil?
           (fetch-value-i32-bin-op-semsem
            nil))))
    (testing "nil value"
      (is (nil?
           (fetch-value-i32-bin-op-semsem
            '(IntegerBinOp
              (IntegerConstant  -131974 (Integer 4 []))
              Pow
              (IntegerConstant 630 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant nil (Integer 4 [])))))))
    (testing "slightly bad structure (wrong head)"
      (is (nil?
           (fetch-value-i32-bin-op-semsem
            '(IntegerBinOp
              (IntegerFOOBAR  -131974 (Integer 4 []))
              Pow
              (IntegerConstant 630 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 434343 (Integer 4 [])))))))
    (testing "wrong value"
      (is (nil?
           (fetch-value-i32-bin-op-semsem
            '(IntegerBinOp
              (IntegerConstant -131974 (Integer 4 []))
              Pow
              (IntegerConstant 630 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 434343 (Integer 4 [])))))))
    (testing "bad operator"
      (is (nil?
           (fetch-value-i32-bin-op-semsem
            '(IntegerBinOp
              (IntegerConstant -131974 (Integer 4 []))
              FOOBAR
              (IntegerConstant 630 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 434343 (Integer 4 [])))))))
    (testing "zero to a negative power"
      (is (nil?
           (fetch-value-i32-bin-op-semsem
            '(IntegerBinOp
              (IntegerConstant 0 (Integer 4 []))
              Pow
              (IntegerConstant -1 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))))))
    (testing "divide by zero"
      (is (nil?
           (fetch-value-i32-bin-op-semsem
            '(IntegerBinOp
              (IntegerConstant -131974 (Integer 4 []))
              Div
              (IntegerConstant 0 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))))))
    (testing "missing output clause"
      (is (nil?
           (fetch-value-i32-bin-op-semsem
            '(IntegerBinOp
              (IntegerConstant -131974 (Integer 4 []))
              Pow
              (IntegerConstant -630 (Integer 4 []))
              (Integer 4 []))))))
    (testing "underflow"
      (is (nil?
           (fetch-value-i32-bin-op-semsem
            '(IntegerBinOp
              (IntegerConstant -131974 (Integer 4 []))
              Pow
              (IntegerConstant -630 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))))))))


;;  _           __                   _
;; | |___ __ _ / _|  __ ___ _  _ _ _| |_
;; | / -_) _` |  _| / _/ _ \ || | ' \  _|
;; |_\___\__,_|_|   \__\___/\_,_|_||_\__|

(deftest i32-bin-op-semsem-leaf-count-test
  (testing "i32 bin op semsem leaf count"
    (is (= 12 (i32-bin-op-semsem-leaf-count
               '(IntegerBinOp
                 (IntegerBinOp
                  (IntegerBinOp
                   (IntegerConstant 40212 (Integer 4 []))
                   Div
                   (IntegerConstant -2 (Integer 4 []))
                   (Integer 4 [])
                   (IntegerConstant -20106 (Integer 4 [])))
                  Mul
                  (IntegerBinOp
                   (IntegerBinOp
                    (IntegerConstant -1399 (Integer 4 []))
                    BitXor
                    (IntegerConstant -288 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 1129 (Integer 4 [])))
                   BitRShift
                   (IntegerBinOp
                    (IntegerBinOp
                     (IntegerConstant -23465 (Integer 4 []))
                     Mul
                     (IntegerConstant -2841072 (Integer 4 []))
                     (Integer 4 [])
                     (IntegerConstant -2053722256 (Integer 4 [])))
                    Add
                    (IntegerBinOp
                     (IntegerConstant -1426 (Integer 4 []))
                     BitRShift
                     (IntegerConstant 1256806 (Integer 4 []))
                     (Integer 4 [])
                     (IntegerConstant -1 (Integer 4 [])))
                    (Integer 4 [])
                    (IntegerConstant -2053722257 (Integer 4 [])))
                   (Integer 4 [])
                   (IntegerConstant 0 (Integer 4 [])))
                  (Integer 4 [])
                  (IntegerConstant 0 (Integer 4 [])))
                 Mul
                 (IntegerBinOp
                  (IntegerBinOp
                   (IntegerConstant 7113 (Integer 4 []))
                   BitAnd
                   (IntegerConstant -407199570 (Integer 4 []))
                   (Integer 4 [])
                   (IntegerConstant 136 (Integer 4 [])))
                  BitAnd
                  (IntegerBinOp
                   (IntegerConstant -3 (Integer 4 []))
                   BitLShift
                   (IntegerConstant 852 (Integer 4 []))
                   (Integer 4 [])
                   (IntegerConstant -3145728 (Integer 4 [])))
                  (Integer 4 [])
                  (IntegerConstant 0 (Integer 4 [])))
                 (Integer 4 [])
                 (IntegerConstant 0 (Integer 4 []))))))))


;;                         _         _ _______   _    _
;;  __ ___ _ __  _ __ _  _| |_ ___  (_)__ /_  ) | |__(_)_ _  ___ _ __
;; / _/ _ \ '  \| '_ \ || |  _/ -_) | ||_ \/ /  | '_ \ | ' \/ _ \ '_ \
;; \__\___/_|_|_| .__/\_,_|\__\___| |_|___/___| |_.__/_|_||_\___/ .__/
;;              |_|                                             |_|

(let [compute (partial compute-i32-bin-op-value
                       asr-i32-unchecked-binop->clojure-op)]
  (deftest unit-compute-i32-binop
    (is (= -3145728 ;; kinda pi-ish :)
           (compute '(IntegerBinOp
                      (IntegerConstant -3 (Integer 4 []))
                      BitLShift
                      (IntegerConstant 852 (Integer 4 []))
                      (Integer 4 [])
                      (IntegerConstant -3145728 (Integer 4 []))))))))


(let [foo (-> (s/gen :asr.expr.semsem/i32-bin-op)
              (gen/sample NTESTS))
      compute (partial compute-i32-bin-op-value
                       asr-i32-unchecked-binop->clojure-op)]
  (deftest compute-vs-fetch-i32-bin-op
    (is (every?
         identity
         (->> foo
              (map (juxt fetch-value-i32-bin-op-semsem
                         compute))
              (map #(apply = %)))))))


;;                      _
;;  ___ __ _ _ __  _ __| |___
;; (_-</ _` | '  \| '_ \ / -_)
;; /__/\__,_|_|_|_| .__/_\___|
;;                |_|

(let [foo (-> (s/gen :asr.expr.semsem/i32-bin-op)
              (gen/sample NTESTS))]
 (deftest sample-100-test
   (testing "sample of 100 i32 bin ops:"
     (testing "leaf counts"
       (is (every? #(>= % 2)
                   (map i32-bin-op-semsem-leaf-count foo))))
     (testing "validity"
       (is (every? identity
                   (map #(s/valid? :asr.expr.semsem/i32-bin-op %) foo))))
     (testing "fetched value not nil"
       (is (not-any? nil?
                     (map fetch-value-i32-bin-op-semsem foo)))))))


;;  ______   ____  __ ____   ___  _
;; / ___\ \ / /  \/  | __ ) / _ \| |
;; \___ \\ V /| |\/| |  _ \| | | | |
;;  ___) || | | |  | | |_) | |_| | |___
;; |____/ |_| |_|  |_|____/ \___/|_____|

(deftest expr-01-211000-is-not-a-symbol
  (is (not (s/valid? :asr.autospecs/symbol expr-01-211000))))

;;                _         _   _        _    _
;;  ____  _ _ __ | |__  ___| | | |_ __ _| |__| |___
;; (_-< || | '  \| '_ \/ _ \ | |  _/ _` | '_ \ / -_)
;; /__/\_, |_|_|_|_.__/\___/_|  \__\__,_|_.__/_\___|
;;     |__/

(deftest two-example-symbol-tables
  (is (s/valid?
       :asr.specs/symbol-table
       '(SymbolTable
         2 {:x (Variable
                2 x Local () () Default
                (Integer 4 []) Source Public Required .false.),
            :x2 (Variable
                 2 x2 Local () () Default
                 (Integer 8 []) Source Public Required .false.),
            :y (Variable
                2 y Local () () Default
                (Real 4 []) Source Public Required .false.),
            :y2 (Variable
                 2 y2 Local () () Default (Real 8 [])
                 Source Public Required .false.)})))

  (is (s/valid?
       :asr.specs/symbol-table
       '(SymbolTable
         1
         {:_lpython_main_program
          (Function
           (SymbolTable 4 {})
           _lpython_main_program
           []
           []
           [(SubroutineCall 1 main0 () [] ())] ()
           Source Public Implementation () .false. .false. .false. .false.),
          :main0
          (Function
           (SymbolTable
            2 {:x (Variable
                   2 x Local () () Default
                   (Integer 4 []) Source Public Required .false.),
               :x2 (Variable
                    2 x2 Local () () Default
                    (Integer 8 []) Source Public Required .false.),
               :y (Variable
                   2 y Local () () Default
                   (Real 4 []) Source Public Required .false.),
               :y2 (Variable
                    2 y2 Local () () Default (Real 8 [])
                    Source Public Required .false.)})
           main0 [] []
           [(= (Var 2 x)
               (IntegerBinOp
                (IntegerBinOp
                 (IntegerConstant
                  2 (Integer 4 []))
                 Add
                 (IntegerConstant
                  3
                  (Integer 4 []))
                 (Integer 4 [])
                 (IntegerConstant
                  5 (Integer 4 [])))
                Mul
                (IntegerConstant
                 5 (Integer 4 []))
                (Integer 4 [])
                (IntegerConstant
                 25 (Integer 4 []))) ())
            (Print () [(Var 2 x)] () ())]
           () Source Public Implementation () .false. .false. .false. .false.),
          :main_program
          (Program
           (SymbolTable 3 {})
           main_program []
           [(SubroutineCall 1 _lpython_main_program () [] ())])}))))


;;               _      _    _
;; __ ____ _ _ _(_)__ _| |__| |___
;; \ V / _` | '_| / _` | '_ \ / -_)
;;  \_/\__,_|_| |_\__,_|_.__/_\___|

(deftest sample-asr-variable
  (is (s/valid?
       :asr.specs/variable
       '(Variable        ; head
         2               ; parent-symtab
         x               ; nym
         Local           ; intent
         ()              ; symbolic-value
         ()              ; value
         Default         ; storage-type
         (Integer 4 [])  ; type
         Source          ; abi
         Public          ; access
         Required        ; presence
         .false.         ; value-attr
         )))
  (is  (s/valid?
        :asr.specs/variable
        '(Variable        ; head
          2               ; parent-symtab
          x               ; nym
          Local           ; intent
          (IntegerConstant
           5 (Integer 4 [])) ; symbolic-value
          (SetConstant)      ; value
          Default         ; storage-type
          (Integer 4 [])  ; type
          Source          ; abi
          Public          ; access
          Required        ; presence
          .false.         ; value-attr
          ))))


;;  _              _
;; | |__  ___  ___| |
;; | '_ \/ _ \/ _ \ |
;; |_.__/\___/\___/_|

(deftest bool-spec-test
  (is (s/valid? :asr.specs/bool '.false.))
  (is (s/valid? :asr.specs/bool '.true.))
  (is (s/valid? :asr.specs/bool false))
  (is (s/valid? :asr.specs/bool true)))


;;  _             _       _   _
;; | |_ _  _ _ __| |___  | |_| |_ _  _ _ __  ___
;; |  _| || | '_ \ / -_) |  _|  _| || | '_ \/ -_)
;;  \__|\_,_| .__/_\___|  \__|\__|\_, | .__/\___|
;;          |_|                   |__/|_|

(deftest tuple-ttype-test
  (is (s/valid? :asr.specs/tuple-ttype
                '(Tuple [])))
  (is (s/valid? :asr.specs/tuple-ttype
                '(Tuple [(Character 1 []) (Integer 4 [])]))))



;;  ___  ___ _ __ ___
;; / __|/ __| '_ ` _ \
;; \__ \ (__| | | | | |
;; |___/\___|_| |_| |_|

;; Deprecated 9 Jan 2023.

(deftest ->scm-test
  (is (= (->scm 'foo) 'foo))
  (is (= (->scm 9) 9))
  (is (= (->scm 2.71828) 2.71828)))


;;     _    ____  ____    ___       _                           _
;;    / \  / ___||  _ \  |_ _|_ __ | |_ ___ _ __ _ __  _ __ ___| |_ ___ _ __
;;   / _ \ \___ \| |_) |  | || '_ \| __/ _ \ '__| '_ \| '__/ _ \ __/ _ \ '__|
;;  / ___ \ ___) |  _ <   | || | | | ||  __/ |  | |_) | | |  __/ ||  __/ |
;; /_/   \_\____/|_| \_\ |___|_| |_|\__\___|_|  | .__/|_|  \___|\__\___|_|
;;                                              |_|

(deftest global-environment-exists-test
  (is ΓΠ))


(deftest eval-bindings-test
  "A SymbolTable is just a dict; to be usable, it must be a frame π in an
Environment."
  (let [foo {:a '(ForTest), :b '(ForTest), :c '(ForTest)}]
    (is (= {:a {:head 'ForTest},
            :b {:head 'ForTest},
            :c {:head 'ForTest}}
           ((eval-bindings foo) ΓΠ)))))


(deftest eval-new-env-test
  (let [foo {:a '(ForTest), :b '(ForTest), :c '(ForTest)}]
    (is (= {:φ {:a {:head 'ForTest},
                :b {:head 'ForTest},
                :c {:head 'ForTest}},
            :π ΓΠ}
           (new-env foo ΓΠ)))))


(deftest eval-program-symbol-test
  (let [foo ((eval-symbol
              '(Program
                (SymbolTable 3 {})
                main_program
                []
                [(SubroutineCall 1 _lpython_main_program () [] ())]))
             ΓΠ)]
    (is (= {:head         'Program,
            :symtab       '(SymbolTable 3 {}),
            :nym          'main_program,
            :dependencies [],
            :body         ['(SubroutineCall 1 _lpython_main_program () [] ())],
            :env          ΓΠ}
           foo))))


(def mp {:main_program
         '(Program
           (SymbolTable 3 {})
           main_program
           []
           [(SubroutineCall 1 _lpython_main_program () [] ())])})


((eval-symbol (:main_program mp)) ΓΠ)


(def expr2-pp
  '(TranslationUnit
    (SymbolTable
     1
     {:_lpython_main_program
      (Function
       (SymbolTable 4 {})
       _lpython_main_program
       [main0]
       []
       [(SubroutineCall 1 main0 () [] ())]
       ()
       Source
       Public
       Implementation
       ()
       .false.
       .false.
       .false.
       .false.
       .false.
       []
       []
       .fals:e.),
      main0
      (Function
       (SymbolTable
        2
        {:x
         (Variable
          2
          x
          []
          Local
          ()
          ()
          Default
          (Integer 4 [])
          Source
          Public
          Required
          .false.)})
       main0
       []
       []
       [(=
         (Var 2 x)
         (IntegerBinOp
          (IntegerBinOp
           (IntegerConstant 2 (Integer 4 []))
           Add
           (IntegerConstant 3 (Integer 4 []))
           (Integer 4 [])
           (IntegerConstant 5 (Integer 4 [])))
          Mul
          (IntegerConstant 5 (Integer 4 []))
          (Integer 4 [])
          (IntegerConstant 25 (Integer 4 [])))
         ())
        (Print () [(Var 2 x)] () ())]
       ()
       Source
       Public
       Implementation
       ()
       .false.
       .false.
       .false.
       .false.
       .false.
       []
       []
       .fals:e.),
      main_program
      (Program
       (SymbolTable 3 {})
       main_program
       []
       [(SubroutineCall 1 _lpython_main_program () [] ())])})
    []))
(def expr2-lpy "examples/expr2.py")
(def expr2-clj
  (echo (lpython/get-sample-clj expr2-lpy)))

(deftest lpython-asr-test
  (is (= expr2-pp expr2-clj)))


;;; ((eval-unit expr2-pp) ΓΠ)
