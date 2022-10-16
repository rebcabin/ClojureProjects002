(ns asr.core-test
  (:use [asr.core]
        [asr.utils]
        [asr.data]
        [asr.parsed]
        [asr.autospecs])

  (:require [clojure.math                  :as    math   ]
            [clojure.test                  :refer :all   ]
            [clojure.string                :as    string ]
            [clojure.spec.alpha            :as    s      ]
            [clojure.spec.gen.alpha        :as    gen    ]
            [clojure.test.check.generators :as    tgen   ]
            [clojure.test.check.properties :as    tprop  ]))


(def ONETEST           1)
(def NTESTS           50) ;; Smaller for routine touch-checks
(def RECURSION-LIMIT   4) ;; ditto
(def LONGTESTS      1000) ;; Bigger for inline stresses


;;  _____       _     ___                   _               _        _
;; |_   _|__ __| |_  | __|_ ___ __  ___ _ _(_)_ __  ___ _ _| |_ __ _| |
;;   | |/ -_|_-<  _| | _|\ \ / '_ \/ -_) '_| | '  \/ -_) ' \  _/ _` | |
;;   |_|\___/__/\__| |___/_\_\ .__/\___|_| |_|_|_|_\___|_||_\__\__,_|_|
;;                           |_|
;;  ___           _  _   _   ___ ___
;; / __|_  _ _ _ | \| | /_\ / __| _ \
;; \__ \ || | ' \| .` |/ _ \\__ \   /
;; |___/\_, |_||_|_|\_/_/ \_\___/_|_\
;;      |__/

(deftest kebab-test
  (testing "kebab-case from asr.utils"
    (is (= (asr.utils/nskw-kebab-from 'TranslationUnit)
           :asr.autospecs/translation-unit))
    (is (= (asr.utils/nskw-kebab-from "TranslationUnit")
           :asr.autospecs/translation-unit))
    (is (thrown?
         Exception
         (asr.utils/nskw-kebab-from :should-fail)))))


(deftest whole-spec-test
  (testing "whole example passes trivial spec"
    (is (s/valid? list? asr.data/expr-01-211000))))


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


;;                               _     _
;;  ____  _ _ __  __ ___ _ _  __| |_  | |_ ___ _ _ _ __  ___
;; (_-< || | '  \/ _/ _ \ ' \(_-<  _| |  _/ -_) '_| '  \(_-<
;; /__/\_, |_|_|_\__\___/_||_/__/\__|  \__\___|_| |_|_|_/__/
;;     |__/

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

      (is (s/valid? :asr.autospecs/integer-bin-op test-vector)))))


(let [integer-bin-op-stuff
      '({:head :asr.autospecs/IntegerBinOp,
         :term :asr.autospecs/expr,
         :kind :ASDL-COMPOSITE,
         :form
         {:ASDL-COMPOSITE
          {:ASDL-HEAD "IntegerBinOp",
           :ASDL-ARGS
           ({:ASDL-TYPE "expr", :MULTIPLICITY :asr.parsed/once, :ASDL-NYM "left"}
            {:ASDL-TYPE "binop", :MULTIPLICITY :asr.parsed/once, :ASDL-NYM "op"}
            {:ASDL-TYPE "expr", :MULTIPLICITY :asr.parsed/once, :ASDL-NYM "right"}
            {:ASDL-TYPE "ttype", :MULTIPLICITY :asr.parsed/once, :ASDL-NYM "type"}
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

(deftest integer-ttype-semnasr-conformance
  (testing "Integer ttype conformance"
    (is (s/valid? :asr.core/integer-ttype-semnasr
                  '(Integer 4 [])                ))
    (is (s/valid? :asr.core/integer-ttype-semnasr
                  '(Integer 1 [1 2])             ))
    (is (s/valid? :asr.core/integer-ttype-semnasr
                  '(Integer 8 [] [1 2] [] [3 4]) ))
    (is (not (s/valid? :asr.core/integer-ttype-semnasr  ;; NOT case!
                  '(Integer 4)                   )))
    (is (s/valid? :asr.core/integer-ttype-semnasr
                  '(Integer 2 ())                ))
    (is (s/valid? :asr.core/integer-ttype-semnasr
                  '(Integer 2 (1 2))             ))
    (is (s/valid? :asr.core/integer-ttype-semnasr
                  '(Integer 2 () (1 2) () (3 4)) ))
    (is (every?
         (partial s/valid? :asr.core/integer-ttype-semnasr)
         (for [_ (range NTESTS)]
           (gen/generate (s/gen :asr.core/integer-ttype-semnasr)))))
    ))


;;  _ _______                  _            _
;; (_)__ /_  )  __ ___ _ _  __| |_ __ _ _ _| |_   ___ ___ _ __  _ _  __ _ ____ _
;; | ||_ \/ /  / _/ _ \ ' \(_-<  _/ _` | ' \  _| (_-</ -_) '  \| ' \/ _` (_-< '_|
;; |_|___/___| \__\___/_||_/__/\__\__,_|_||_\__| /__/\___|_|_|_|_||_\__,_/__/_|

(deftest i32-constant-semnasr-conformance
  (testing "i32-constant-semnasr conformance:"
    (testing "list"
      (is (s/valid? :asr.core/i32-constant-semnasr
                    '(IntegerConstant 5 (Integer 4 [])))))
    (testing "vector"
      (is (s/valid? :asr.core/i32-constant-semnasr
                    '[IntegerConstant 5 (Integer 4 [])])))))


(deftest i32-constant-semnasr-non-conformance
  (testing "i32-constant-semnasr NON-conformance:"
    (testing "wrong \"kind\", i.e., integer size"
      (is (not (s/valid? :asr.core/i32-constant-semnasr
                         '(IntegerConstant 5 (Integer 8 []))))))
    (testing "wrong type of value"
      (is (not (s/valid? :asr.core/i32-constant-semnasr
                         '(IntegerConstant 5.0 (Integer 4 []))))))
    (testing "wrong tag"
      (is (not (s/valid? :asr.core/i32-constant-semnasr
                         '(foobarConstant 5 (Integer 4 []))))))
    (testing "wrong ttype"
      (is (not (s/valid? :asr.core/i32-constant-semnasr
                         '(IntegerConstant 5 (Float 4 []))))))
    (testing "missing ttype"
      (is (not (s/valid? :asr.core/i32-constant-semnasr
                         '(IntegerConstant 5)))))
    (testing "wrong structure"
      (is (not (s/valid? :asr.core/i32-constant-semnasr
                         43)))
      (is (not (s/valid? :asr.core/i32-constant-semnasr
                         '((IntegerConstant 5.0 (Integer 8 [])))))))))


;;  _ _______   _    _
;; (_)__ /_  ) | |__(_)_ _    ___ _ __   ___ ___ _ __  _ _  __ _ ____ _
;; | ||_ \/ /  | '_ \ | ' \  / _ \ '_ \ (_-</ -_) '  \| ' \/ _` (_-< '_|
;; |_|___/___| |_.__/_|_||_| \___/ .__/ /__/\___|_|_|_|_||_\__,_/__/_|
;;                               |_|

(deftest i32-bin-op-semnasr-conformance
  ;; Base case, base-answer
  (is (let [test-vector '[IntegerBinOp
                          [IntegerConstant 2 (Integer 4 [])]   ; left
                          Add                                  ; binop
                          [IntegerConstant 3 (Integer 4 [])]   ; right
                          (Integer 4 [])                       ; answer-ttype
                          [IntegerConstant 5 (Integer 4 [])]]] ; answer
        (s/valid? :asr.core/i32-bin-op-semnasr test-vector)))

  ;; Base case, no-answer
  (is (let [test-vector '[IntegerBinOp
                          [IntegerConstant 2 (Integer 4 [])] ; left
                          Add                                ; binop
                          [IntegerConstant 3 (Integer 4 [])] ; right
                          (Integer 4 []) ]]                  ; answer-ttype
        (s/valid? :asr.core/i32-bin-op-semnasr test-vector)))

  ;; Recurse left, no answers
  (is (let [test-vector '[IntegerBinOp
                          [IntegerBinOp                        ; recuse:
                           [IntegerConstant 2 (Integer 4 [])]  ; left
                           Add                                 ; binop
                           [IntegerConstant 3 (Integer 4 [])]  ; right
                           (Integer 4 [])]                     ; ttype
                          Add                                ; binop
                          [IntegerConstant 3 (Integer 4 [])] ; right
                          (Integer 4 [])] ]                  ; ttype
        (s/valid? :asr.core/i32-bin-op-semnasr test-vector)))

  ;; Base case, doubly recursive-answer
  (is (let [test-vector '[IntegerBinOp
                          [IntegerConstant 2 (Integer 4 [])] ; left
                          Add                                ; binop
                          [IntegerConstant 3 (Integer 4 [])] ; right
                          (Integer 4 [])                     ; answer-ttype
                          [IntegerBinOp                       ; recurse answer
                           [IntegerBinOp                       ; recurse again
                            [IntegerConstant 2 (Integer 4 [])] ; left
                            Add                                ; binop
                            [IntegerConstant 3 (Integer 4 [])] ; right
                            (Integer 4 [])]                    ; ttype
                           Add                                ; binop
                           [IntegerConstant 3 (Integer 4 [])] ; right
                           (Integer 4 []) ]]]                 ; ttype
        (s/valid? :asr.core/i32-bin-op-semnasr test-vector)))

  ;; Recursive left, base-answer
  (is (let [test-vector '[IntegerBinOp
                          [IntegerBinOp                         ; recur-left
                           [IntegerConstant 2 (Integer 4 [])]   ;   left
                           Add                                  ;   binop
                           [IntegerConstant 3 (Integer 4 [])]   ;   right
                           (Integer 4 [])                       ;   answer-ttype
                           [IntegerConstant 5 (Integer 4 [])]]  ;   answer
                          Mul                                   ; binop
                          [IntegerConstant 5 (Integer 4 [])]    ; right
                          (Integer 4 [])                        ; answer-ttype
                          [IntegerConstant 25 (Integer 4 [])]]] ; answer
        (s/valid? :asr.core/i32-bin-op-semnasr test-vector)))

  ;; Recursive right, base-answer
  (is (let [test-vector '[IntegerBinOp
                          [IntegerConstant 2 (Integer 4 [])]    ; left
                          Add                                   ; binop
                          [IntegerBinOp                         ; recur-right
                           [IntegerConstant 3 (Integer 4 [])]   ;   left
                           Add                                  ;   binop
                           [IntegerConstant 4 (Integer 4 [])]   ;   right
                           (Integer 4 [])                       ;   answer-ttype
                           [IntegerConstant 42 (Integer 4 [])]] ;   answer
                          (Integer 4 [])                        ; answer-ttype
                          [IntegerConstant 42 (Integer 4 [])]]] ; answer
        (s/valid? :asr.core/i32-bin-op-semnasr test-vector)))

  ;; Recursive right, recursive answer
  (is (let [test-vector '[IntegerBinOp
                          [IntegerConstant 2 (Integer 4 [])]
                          Add
                          [IntegerBinOp ; recursive right       ; recur-right
                           [IntegerConstant 3 (Integer 4 [])]
                           Add
                           [IntegerConstant 4 (Integer 4 [])]
                           (Integer 4 [])]
                          (Integer 4 [])                        ; answer type
                          [IntegerBinOp                         ; recurse answer
                           [IntegerConstant 3 (Integer 4 [])]
                           Mul
                           [IntegerConstant 4 (Integer 4 [])]
                           (Integer 4 [])
                           [IntegerConstant 25 (Integer 4 [])]]]]
        (s/valid? :asr.core/i32-bin-op-semnasr test-vector)))

  ;; base, recursive answer
  (is (let [test-vector '[IntegerBinOp
                          [IntegerConstant 2 (Integer 4 [])] ; base
                          Add
                          [IntegerConstant 4 (Integer 4 [])] ; base
                          (Integer 4 [])                     ; answer ttype
                          [IntegerBinOp                      ; recursive answer
                           [IntegerConstant 3 (Integer 4 [])]
                           Mul
                           [IntegerConstant 4 (Integer 4 [])]
                           (Integer 4 [])
                           [IntegerConstant 25 (Integer 4 [])]]]]
        (s/valid? :asr.core/i32-bin-op-semnasr test-vector)))

  ;; big-honkin' case
  (is (s/valid?
       :asr.core/i32-bin-op-semnasr
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
         (Integer 4 [])]))

  ;; Stress. Guaranteed by clojure.spec, but ya' never know.
  (is (every?
       (partial s/valid? :asr.core/i32-bin-op-semnasr)
       (binding [s/*recursion-limit* RECURSION-LIMIT]
         (for [_ (range NTESTS)]
           (-> :asr.core/i32-bin-op-semnasr
               s/gen
               gen/generate))))))


;;  _     _                                                 _
;; (_)_ _| |_ ___ __ _ ___ _ _   __ _ ___ _ _  ___ _ _ __ _| |_ ___ _ _ ___
;; | | ' \  _/ -_) _` / -_) '_| / _` / -_) ' \/ -_) '_/ _` |  _/ _ \ '_(_-<
;; |_|_||_\__\___\__, \___|_|   \__, \___|_||_\___|_| \__,_|\__\___/_| /__/
;;               |___/          |___/

(deftest integer-generators
  (testing "::i8, ::i8nz, ::i16, etc."
    (is (not-any? zero?
                  (gen/sample (s/gen :asr.core/i8nz) NTESTS)))
    (is (not-any? zero?
                  (gen/sample (s/gen :asr.core/i16nz) NTESTS)))
    (is (not-any? zero?
                  (gen/sample (s/gen :asr.core/i32nz) NTESTS)))
    (is (not-any? zero?
                  (gen/sample (s/gen :asr.core/i64nz) NTESTS)))
    (is (not-any? #(> % Byte/MAX_VALUE)
                  (gen/sample (s/gen :asr.core/i8) NTESTS)))
    (is (not-any? #(> % Short/MAX_VALUE)
                  (gen/sample (s/gen :asr.core/i16) NTESTS)))
    (is (not-any? #(> % Integer/MAX_VALUE)
                  (gen/sample (s/gen :asr.core/i32) NTESTS)))
    (is (not-any? #(> % Long/MAX_VALUE)
                  (gen/sample (s/gen :asr.core/i64) NTESTS)))
    (is (not-any? #(< % Byte/MIN_VALUE)
                  (gen/sample (s/gen :asr.core/i8) NTESTS)))
    (is (not-any? #(< % Short/MIN_VALUE)
                  (gen/sample (s/gen :asr.core/i16) NTESTS)))
    (is (not-any? #(< % Integer/MIN_VALUE)
                  (gen/sample (s/gen :asr.core/i32) NTESTS)))
    (is (not-any? #(< % Long/MIN_VALUE)
                  (gen/sample (s/gen :asr.core/i64) NTESTS)))))


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


(deftest binop-no-div
  (testing "that ::binop-no-div never generates 'Div")
  (is (not-any?
       #{'Div}
       (s/exercise :asr.core/binop-no-div NTESTS))))


;;  _ _______   _    _
;; (_)__ /_  ) | |__(_)_ _    ___ _ __   ___ ___ _ __  _ _  __ _ ____ _
;; | ||_ \/ /  | '_ \ | ' \  / _ \ '_ \ (_-</ -_) '  \| ' \/ _` (_-< '_|
;; |_|___/___| |_.__/_|_||_| \___/ .__/ /__/\___|_|_|_|_||_\__,_/__/_|
;;                               |_|
;;                                 _ _     _
;;  _ _  ___   ______ _ _ ___   __| (_)_ _(_)___ ___ _ _
;; | ' \/ _ \ |_ / -_) '_/ _ \ / _` | \ V / (_-</ _ \ '_|
;; |_||_\___/ /__\___|_| \___/ \__,_|_|\_/|_/__/\___/_|

(def honker-2
  '(IntegerBinOp
   (IntegerBinOp
    (IntegerConstant -1 (Integer 4 []))
    Div
    (IntegerBinOp
     (IntegerConstant 0 (Integer 4 []))
     Div
     (IntegerConstant -1 (Integer 4 []))
     (Integer 4 []))
    (Integer 4 []))
   BitAnd
   (IntegerBinOp
    (IntegerBinOp
     (IntegerConstant 0 (Integer 4 []))
     Pow
     (IntegerBinOp
      (IntegerConstant -1 (Integer 4 []))
      Mul
      (IntegerConstant -1 (Integer 4 []))
      (Integer 4 []))
     (Integer 4 [])
     (IntegerBinOp
      (IntegerBinOp
       (IntegerConstant 0 (Integer 4 []))
       Div
       (IntegerConstant -1 (Integer 4 []))
       (Integer 4 []))
      Div
      (IntegerConstant -1 (Integer 4 []))
      (Integer 4 [])))
    BitOr
    (IntegerConstant -1 (Integer 4 []))
    (Integer 4 [])
    (IntegerBinOp
     (IntegerBinOp
      (IntegerBinOp
       (IntegerConstant 0 (Integer 4 []))
       Div
       (IntegerConstant 0 (Integer 4 []))  ;;; BAD
       (Integer 4 [])
       (IntegerConstant 0 (Integer 4 [])))
      BitRShift
      (IntegerConstant 0 (Integer 4 []))
      (Integer 4 [])
      (IntegerConstant -1 (Integer 4 [])))
     Div
     (IntegerBinOp
      (IntegerConstant 0 (Integer 4 []))
      Div
      (IntegerConstant -1 (Integer 4 []))
      (Integer 4 []))
     (Integer 4 [])))
   (Integer 4 [])
   (IntegerBinOp
    (IntegerConstant 0 (Integer 4 []))
    BitAnd
    (IntegerConstant -1 (Integer 4 []))
    (Integer 4 []))))


(def honker-3
  '(IntegerBinOp
    (IntegerConstant 0 (Integer 4 []))
    Div
    (IntegerConstant 0 (Integer 4 []))   ;;; BAD
    (Integer 4 [])
    (IntegerConstant 0 (Integer 4 []))))


(deftest no-zero-divisors
  (testing "lack of zero divisors"
    (is (not (s/valid? :asr.core/i32-bin-op-semnasr-no-zero-divisor
                       honker-2)))
    (is (not (s/valid? :asr.core/i32-bin-op-semnasr-no-zero-divisor
                       honker-3)))))


;;  _ _______    _    _
;; (_)__ /_  )__| |__(_)_ _ ___ ___ _ __ ___ ___ ___ _ __  ___ ___ _ __
;; | ||_ \/ /___| '_ \ | ' \___/ _ \ '_ \___(_-</ -_) '  \(_-</ -_) '  \
;; |_|___/___|  |_.__/_|_||_|  \___/ .__/   /__/\___|_|_|_/__/\___|_|_|_|
;;                                 |_|

(deftest i32-bin-op-leaf-semsem-conformance
  (testing "conformance to :asr.core/i32-bin-op-leaf-semsem"
    (is (s/valid? :asr.core/i32-bin-op-leaf-semsem
                  '(IntegerBinOp
                    (IntegerConstant -1 (Integer 4 []))
                    BitOr
                    (IntegerConstant 0 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant -1 (Integer 4 [])))))
    (is (s/valid? :asr.core/i32-bin-op-leaf-semsem
                  '(IntegerBinOp
                    (IntegerConstant -1 (Integer 4 []))
                    BitLShift
                    (IntegerConstant 0 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant -1 (Integer 4 [])))))
    (is (s/valid? :asr.core/i32-bin-op-leaf-semsem
                  '(IntegerBinOp
                    (IntegerConstant 1 (Integer 4 []))
                    BitRShift
                    (IntegerConstant 0 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 1 (Integer 4 [])))))
    (is (s/valid? :asr.core/i32-bin-op-leaf-semsem
                  '(IntegerBinOp
                    (IntegerConstant -4 (Integer 4 []))
                    BitAnd
                    (IntegerConstant -4 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant -4 (Integer 4 [])))))
    (is (s/valid? :asr.core/i32-bin-op-leaf-semsem
                  '(IntegerBinOp
                    (IntegerConstant 0 (Integer 4 []))
                    Add
                    (IntegerConstant 0 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 0 (Integer 4 [])))))
    (is (s/valid? :asr.core/i32-bin-op-leaf-semsem
                  '(IntegerBinOp
                    (IntegerConstant 5 (Integer 4 []))
                    Add
                    (IntegerConstant -1 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 4 (Integer 4 [])))))
    (is (s/valid? :asr.core/i32-bin-op-leaf-semsem
                  '(IntegerBinOp
                    (IntegerConstant 61 (Integer 4 []))
                    BitRShift
                    (IntegerConstant -4 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 0 (Integer 4 [])))))
    (is (s/valid? :asr.core/i32-bin-op-leaf-semsem
                  '(IntegerBinOp
                    (IntegerConstant 13 (Integer 4 []))
                    BitOr
                    (IntegerConstant -1 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant -1 (Integer 4 [])))))
    (is (s/valid? :asr.core/i32-bin-op-leaf-semsem
                  '(IntegerBinOp
                    (IntegerConstant 0 (Integer 4 []))
                    Div
                    (IntegerConstant 246 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 0 (Integer 4 [])))))
    (is (s/valid? :asr.core/i32-bin-op-leaf-semsem
                  '(IntegerBinOp
                    (IntegerConstant 14 (Integer 4 []))
                    Mul
                    (IntegerConstant -54 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant -756 (Integer 4 [])))))
    (is (s/valid? :asr.core/i32-bin-op-leaf-semsem
                  '(IntegerBinOp
                    (IntegerConstant -82 (Integer 4 []))
                    Pow
                    (IntegerConstant -25 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 0 (Integer 4 [])))))
    (is (s/valid? :asr.core/i32-bin-op-leaf-semsem
                  '(IntegerBinOp
                    (IntegerConstant -43 (Integer 4 []))
                    Pow
                    (IntegerConstant -1002 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 0 (Integer 4 [])))))
    (is (s/valid? :asr.core/i32-bin-op-leaf-semsem
                  '(IntegerBinOp
                    (IntegerConstant 2316 (Integer 4 []))
                    BitXor
                    (IntegerConstant -3 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant -2319 (Integer 4 [])))))
    (is (s/valid? :asr.core/i32-bin-op-leaf-semsem
                  '(IntegerBinOp
                    (IntegerConstant 622 (Integer 4 []))
                    Sub
                    (IntegerConstant 34 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 588 (Integer 4 [])))))
    (is (s/valid? :asr.core/i32-bin-op-leaf-semsem
                  '(IntegerBinOp
                    (IntegerConstant 17 (Integer 4 []))
                    BitRShift
                    (IntegerConstant -2403 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 0 (Integer 4 [])))))
    (is (s/valid? :asr.core/i32-bin-op-leaf-semsem
                  '(IntegerBinOp
                    (IntegerConstant 3207 (Integer 4 []))
                    Sub
                    (IntegerConstant -1108 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 4315 (Integer 4 [])))))
    (is (s/valid? :asr.core/i32-bin-op-leaf-semsem
                  '(IntegerBinOp
                    (IntegerConstant -1 (Integer 4 []))
                    Add
                    (IntegerConstant 284 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 283 (Integer 4 [])))))
    (is (s/valid? :asr.core/i32-bin-op-leaf-semsem
                  '(IntegerBinOp
                    (IntegerConstant -657 (Integer 4 []))
                    BitOr
                    (IntegerConstant -356 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant -1 (Integer 4 [])))))
    (is (s/valid? :asr.core/i32-bin-op-leaf-semsem
                  '(IntegerBinOp
                    (IntegerConstant -131974 (Integer 4 []))
                    Pow
                    (IntegerConstant 630 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 0 (Integer 4 [])))))
    (testing "TODO: a nil value conforms, but we want to eliminate
                   nils in the generator"
      (is (s/valid? :asr.core/i32-bin-op-leaf-semsem
                   '(IntegerBinOp
                     (IntegerConstant -41056462 (Integer 4 []))
                     Pow
                     (IntegerConstant -266578627 (Integer 4 []))
                     (Integer 4 [])
                     (IntegerConstant nil (Integer 4 []))))))))


(deftest i32-bin-op-leaf-semsem-non-conformance
  (testing "non-conformance of :asr.core/i32-bin-op-leaf-semsem:"
    (testing "wrong structure"
      (is (not (s/valid? :asr.core/i32-bin-op-leaf-semsem
                         'foo))))
    (testing "wrong keywords"
      (is (not (s/valid? :asr.core/i32-bin-op-leaf-semsem
                         '(bag
                           (foo 42 bar)
                           Pow
                           (baz 6 bar)
                           (boo 4 far)
                           (qux 32 pgh))))))
    (testing "wrong value"
      (is (not (s/valid? :asr.core/i32-bin-op-leaf-semsem
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
      (is (not (s/valid? :asr.core/i32-bin-op-leaf-semsem
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
         (partial s/valid? :asr.core/i32-bin-op-leaf-semsem)
         (filter (comp not nil?)
                 (gen/sample
                  (s/gen :asr.core/i32-bin-op-leaf-semsem)
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

;; Co-cursively calls spec ::i32-bin-op-semsem.

(deftest maybe-value-i32-semsem-test
  (testing "various returns in the maybe monad:"
    (testing "good value i32bop"
      (is (zero?
           (maybe-value-i32-semsem
            '(IntegerBinOp
              (IntegerConstant -131974 (Integer 4 []))
              Pow
              (IntegerConstant 630 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))))))
    (testing "good value i32con"
      (is (= -131974
             (maybe-value-i32-semsem
              '(IntegerConstant -131974 (Integer 4 []))))))
    (testing "wrong \"kind\""
      (is (nil?
           (maybe-value-i32-semsem
            '(IntegerConstant -131974 (Integer 8 []))))))
    (testing "seriously bad structure"
      (is (nil?
           (maybe-value-i32-semsem
            nil))))
    (testing "slightly bad structure"
      (is (nil?
           (maybe-value-i32-semsem
            '(IntegerBinOp
              (IntegerFOOBAR  -131974 (Integer 4 []))
              Pow
              (IntegerConstant 630 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 434343 (Integer 4 [])))))))
    (testing "wrong value"
      (is (nil?
           (maybe-value-i32-semsem
            '(IntegerBinOp
              (IntegerConstant -131974 (Integer 4 []))
              Pow
              (IntegerConstant 630 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 434343 (Integer 4 [])))))))
    (testing "bad operator"
      (is (nil?
           (maybe-value-i32-semsem
            '(IntegerBinOp
              (IntegerConstant -131974 (Integer 4 []))
              FOOBAR
              (IntegerConstant 630 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 434343 (Integer 4 [])))))))
    (testing "zero to a negative power"
      (is (nil?
           (maybe-value-i32-semsem
            '(IntegerBinOp
              (IntegerConstant 0 (Integer 4 []))
              Pow
              (IntegerConstant -1 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))))))
    (testing "divide by zero"
      (is (nil?
           (maybe-value-i32-semsem
            '(IntegerBinOp
              (IntegerConstant -131974 (Integer 4 []))
              Div
              (IntegerConstant 0 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))))))
    (testing "missing output clause"
      (is (nil?
           (maybe-value-i32-semsem
            '(IntegerBinOp
              (IntegerConstant -131974 (Integer 4 []))
              Pow
              (IntegerConstant -630 (Integer 4 []))
              (Integer 4 []))))))
    (testing "underflow"
      (is (nil?
           (maybe-value-i32-semsem
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


;;                      _       _  __   __
;;  ___ __ _ _ __  _ __| |___  / |/  \ /  \
;; (_-</ _` | '  \| '_ \ / -_) | | () | () |
;; /__/\__,_|_|_|_| .__/_\___| |_|\__/ \__/
;;                |_|

;;; result of (-> (s/gen :asr.core/i32-bin-op-semsem)
;;;               (gen/sample 100))

(def foo '((IntegerBinOp
             (IntegerBinOp
              (IntegerConstant -1 (Integer 4 []))
              BitRShift
              (IntegerConstant -1 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant -1 (Integer 4 [])))
             Mul
             (IntegerBinOp
              (IntegerBinOp
               (IntegerBinOp
                (IntegerConstant 0 (Integer 4 []))
                Mul
                (IntegerConstant -1 (Integer 4 []))
                (Integer 4 [])
                (IntegerConstant 0 (Integer 4 [])))
               BitOr
               (IntegerBinOp
                (IntegerConstant 0 (Integer 4 []))
                BitAnd
                (IntegerConstant -1 (Integer 4 []))
                (Integer 4 [])
                (IntegerConstant 0 (Integer 4 [])))
               (Integer 4 [])
               (IntegerConstant 0 (Integer 4 [])))
              Mul
              (IntegerBinOp
               (IntegerConstant -1 (Integer 4 []))
               BitXor
               (IntegerConstant 0 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -1 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 0 (Integer 4 []))
             BitRShift
             (IntegerConstant 0 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant 0 (Integer 4 []))
              Mul
              (IntegerConstant 0 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             Sub
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant -1 (Integer 4 []))
               Div
               (IntegerConstant -1 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 1 (Integer 4 [])))
              Sub
              (IntegerBinOp
               (IntegerBinOp
                (IntegerBinOp
                 (IntegerConstant 0 (Integer 4 []))
                 Div
                 (IntegerConstant 6 (Integer 4 []))
                 (Integer 4 [])
                 (IntegerConstant 0 (Integer 4 [])))
                Mul
                (IntegerBinOp
                 (IntegerBinOp
                  (IntegerConstant -1 (Integer 4 []))
                  BitXor
                  (IntegerConstant -1 (Integer 4 []))
                  (Integer 4 [])
                  (IntegerConstant 0 (Integer 4 [])))
                 BitXor
                 (IntegerBinOp
                  (IntegerConstant 0 (Integer 4 []))
                  Div
                  (IntegerConstant -1 (Integer 4 []))
                  (Integer 4 [])
                  (IntegerConstant 0 (Integer 4 [])))
                 (Integer 4 [])
                 (IntegerConstant 0 (Integer 4 [])))
                (Integer 4 [])
                (IntegerConstant 0 (Integer 4 [])))
               BitRShift
               (IntegerBinOp
                (IntegerBinOp
                 (IntegerConstant -1 (Integer 4 []))
                 BitRShift
                 (IntegerConstant 0 (Integer 4 []))
                 (Integer 4 [])
                 (IntegerConstant -1 (Integer 4 [])))
                Sub
                (IntegerBinOp
                 (IntegerConstant -1 (Integer 4 []))
                 BitOr
                 (IntegerConstant -2 (Integer 4 []))
                 (Integer 4 [])
                 (IntegerConstant -1 (Integer 4 [])))
                (Integer 4 [])
                (IntegerConstant 0 (Integer 4 [])))
               (Integer 4 [])
               (IntegerConstant 0 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant 1 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant -1 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant 0 (Integer 4 []))
               Mul
               (IntegerConstant 0 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 0 (Integer 4 [])))
              BitXor
              (IntegerBinOp
               (IntegerConstant -1 (Integer 4 []))
               BitAnd
               (IntegerConstant -1 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -1 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant -1 (Integer 4 [])))
             BitXor
             (IntegerBinOp
              (IntegerBinOp
               (IntegerBinOp
                (IntegerConstant -4 (Integer 4 []))
                Sub
                (IntegerConstant 3 (Integer 4 []))
                (Integer 4 [])
                (IntegerConstant -7 (Integer 4 [])))
               Mul
               (IntegerBinOp
                (IntegerBinOp
                 (IntegerConstant 2 (Integer 4 []))
                 Pow
                 (IntegerConstant -3 (Integer 4 []))
                 (Integer 4 [])
                 (IntegerConstant 0 (Integer 4 [])))
                Mul
                (IntegerBinOp
                 (IntegerConstant -1 (Integer 4 []))
                 BitLShift
                 (IntegerConstant -1 (Integer 4 []))
                 (Integer 4 [])
                 (IntegerConstant 0 (Integer 4 [])))
                (Integer 4 [])
                (IntegerConstant 0 (Integer 4 [])))
               (Integer 4 [])
               (IntegerConstant 0 (Integer 4 [])))
              BitOr
              (IntegerBinOp
               (IntegerBinOp
                (IntegerConstant -1 (Integer 4 []))
                BitRShift
                (IntegerConstant -7 (Integer 4 []))
                (Integer 4 [])
                (IntegerConstant -1 (Integer 4 [])))
               BitLShift
               (IntegerBinOp
                (IntegerBinOp
                 (IntegerConstant -4 (Integer 4 []))
                 BitAnd
                 (IntegerConstant 4 (Integer 4 []))
                 (Integer 4 [])
                 (IntegerConstant 4 (Integer 4 [])))
                Sub
                (IntegerBinOp
                 (IntegerBinOp
                  (IntegerBinOp
                   (IntegerConstant -2 (Integer 4 []))
                   BitRShift
                   (IntegerConstant -1 (Integer 4 []))
                   (Integer 4 [])
                   (IntegerConstant -1 (Integer 4 [])))
                  Pow
                  (IntegerBinOp
                   (IntegerBinOp
                    (IntegerConstant -3 (Integer 4 []))
                    BitOr
                    (IntegerConstant -1 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant -1 (Integer 4 [])))
                   Sub
                   (IntegerBinOp
                    (IntegerConstant 1 (Integer 4 []))
                    Add
                    (IntegerConstant -2 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant -1 (Integer 4 [])))
                   (Integer 4 [])
                   (IntegerConstant 0 (Integer 4 [])))
                  (Integer 4 [])
                  (IntegerConstant 1 (Integer 4 [])))
                 Div
                 (IntegerBinOp
                  (IntegerConstant 2 (Integer 4 []))
                  BitLShift
                  (IntegerConstant 0 (Integer 4 []))
                  (Integer 4 [])
                  (IntegerConstant 2 (Integer 4 [])))
                 (Integer 4 [])
                 (IntegerConstant 0 (Integer 4 [])))
                (Integer 4 [])
                (IntegerConstant 4 (Integer 4 [])))
               (Integer 4 [])
               (IntegerConstant -16 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant -16 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 15 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 3 (Integer 4 []))
             BitOr
             (IntegerConstant 1 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 3 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant 1 (Integer 4 []))
              Sub
              (IntegerConstant -1 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 2 (Integer 4 [])))
             Add
             (IntegerBinOp
              (IntegerConstant -1 (Integer 4 []))
              BitLShift
              (IntegerConstant -1 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 2 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 1 (Integer 4 []))
             BitAnd
             (IntegerConstant -6 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant -1 (Integer 4 []))
              Mul
              (IntegerConstant -1 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 1 (Integer 4 [])))
             Div
             (IntegerBinOp
              (IntegerConstant 3 (Integer 4 []))
              Add
              (IntegerConstant -14 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant -11 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant -4 (Integer 4 []))
              Mul
              (IntegerConstant -1 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 4 (Integer 4 [])))
             Div
             (IntegerBinOp
              (IntegerConstant -2 (Integer 4 []))
              BitRShift
              (IntegerConstant -1 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant -1 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant -4 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant -60 (Integer 4 []))
             Pow
             (IntegerConstant 2 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 3600 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant 0 (Integer 4 []))
               BitLShift
               (IntegerConstant -182 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 0 (Integer 4 [])))
              Div
              (IntegerBinOp
               (IntegerConstant 64 (Integer 4 []))
               BitRShift
               (IntegerConstant 2 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 16 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             BitOr
             (IntegerBinOp
              (IntegerConstant -1 (Integer 4 []))
              BitAnd
              (IntegerConstant -7 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant -7 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant -7 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 936 (Integer 4 []))
             BitOr
             (IntegerConstant -1 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant -1 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant 161 (Integer 4 []))
              Sub
              (IntegerConstant 12 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 149 (Integer 4 [])))
             BitRShift
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant -1 (Integer 4 []))
               BitRShift
               (IntegerConstant 10 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -1 (Integer 4 [])))
              Add
              (IntegerBinOp
               (IntegerConstant -90 (Integer 4 []))
               Mul
               (IntegerConstant -15 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 1350 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant 1349 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 4 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 2565 (Integer 4 []))
             BitOr
             (IntegerConstant 31 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 2591 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 4 (Integer 4 []))
             Mul
             (IntegerConstant 2 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 8 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant -24 (Integer 4 []))
             BitRShift
             (IntegerConstant 0 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant -24 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant -2 (Integer 4 []))
              Div
              (IntegerConstant 9427 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             Mul
             (IntegerBinOp
              (IntegerBinOp
               (IntegerBinOp
                (IntegerConstant -4 (Integer 4 []))
                BitRShift
                (IntegerConstant 0 (Integer 4 []))
                (Integer 4 [])
                (IntegerConstant -4 (Integer 4 [])))
               Sub
               (IntegerBinOp
                (IntegerBinOp
                 (IntegerConstant -50 (Integer 4 []))
                 BitOr
                 (IntegerConstant 83 (Integer 4 []))
                 (Integer 4 [])
                 (IntegerConstant -33 (Integer 4 [])))
                BitXor
                (IntegerBinOp
                 (IntegerBinOp
                  (IntegerBinOp
                   (IntegerConstant -16 (Integer 4 []))
                   BitLShift
                   (IntegerConstant -59 (Integer 4 []))
                   (Integer 4 [])
                   (IntegerConstant -512 (Integer 4 [])))
                  Pow
                  (IntegerBinOp
                   (IntegerBinOp
                    (IntegerConstant -168 (Integer 4 []))
                    BitXor
                    (IntegerConstant -2 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 166 (Integer 4 [])))
                   BitXor
                   (IntegerBinOp
                    (IntegerConstant 964 (Integer 4 []))
                    Mul
                    (IntegerConstant 7321 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 7057444 (Integer 4 [])))
                   (Integer 4 [])
                   (IntegerConstant 7057538 (Integer 4 [])))
                  (Integer 4 [])
                  (IntegerConstant 0 (Integer 4 [])))
                 Pow
                 (IntegerBinOp
                  (IntegerBinOp
                   (IntegerConstant -941 (Integer 4 []))
                   Sub
                   (IntegerConstant 0 (Integer 4 []))
                   (Integer 4 [])
                   (IntegerConstant -941 (Integer 4 [])))
                  Mul
                  (IntegerBinOp
                   (IntegerBinOp
                    (IntegerConstant -31 (Integer 4 []))
                    BitAnd
                    (IntegerConstant 1341 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 1313 (Integer 4 [])))
                   BitXor
                   (IntegerBinOp
                    (IntegerBinOp
                     (IntegerConstant 4179 (Integer 4 []))
                     BitOr
                     (IntegerConstant -2 (Integer 4 []))
                     (Integer 4 [])
                     (IntegerConstant -1 (Integer 4 [])))
                    BitXor
                    (IntegerBinOp
                     (IntegerConstant 2 (Integer 4 []))
                     BitRShift
                     (IntegerConstant 1648 (Integer 4 []))
                     (Integer 4 [])
                     (IntegerConstant 0 (Integer 4 [])))
                    (Integer 4 [])
                    (IntegerConstant -1 (Integer 4 [])))
                   (Integer 4 [])
                   (IntegerConstant -1314 (Integer 4 [])))
                  (Integer 4 [])
                  (IntegerConstant 1236474 (Integer 4 [])))
                 (Integer 4 [])
                 (IntegerConstant 0 (Integer 4 [])))
                (Integer 4 [])
                (IntegerConstant -33 (Integer 4 [])))
               (Integer 4 [])
               (IntegerConstant 29 (Integer 4 [])))
              BitAnd
              (IntegerBinOp
               (IntegerConstant 195 (Integer 4 []))
               BitLShift
               (IntegerConstant 0 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 195 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant 1 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant 524 (Integer 4 []))
              BitLShift
              (IntegerConstant 11 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 1073152 (Integer 4 [])))
             BitAnd
             (IntegerBinOp
              (IntegerConstant 10 (Integer 4 []))
              BitAnd
              (IntegerConstant 47452 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 8 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant -726 (Integer 4 []))
             Mul
             (IntegerConstant 24 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant -17424 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 123291 (Integer 4 []))
             BitLShift
             (IntegerConstant 86 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 1723858944 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant -1 (Integer 4 []))
              BitRShift
              (IntegerConstant 0 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant -1 (Integer 4 [])))
             Div
             (IntegerBinOp
              (IntegerConstant 1 (Integer 4 []))
              BitOr
              (IntegerConstant 120959 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 120959 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant 61 (Integer 4 []))
              BitOr
              (IntegerConstant -42 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant -1 (Integer 4 [])))
             Pow
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant -16 (Integer 4 []))
               Div
               (IntegerConstant 18981 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 0 (Integer 4 [])))
              BitRShift
              (IntegerBinOp
               (IntegerConstant 1527 (Integer 4 []))
               BitRShift
               (IntegerConstant 68920 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 0 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 1 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant 222886 (Integer 4 []))
               Sub
               (IntegerConstant 349 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 222537 (Integer 4 [])))
              BitLShift
              (IntegerBinOp
               (IntegerConstant -1053 (Integer 4 []))
               BitXor
               (IntegerConstant -2337 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 3388 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             Pow
             (IntegerBinOp
              (IntegerBinOp
               (IntegerBinOp
                (IntegerConstant -28132 (Integer 4 []))
                BitXor
                (IntegerConstant -16786 (Integer 4 []))
                (Integer 4 [])
                (IntegerConstant 11378 (Integer 4 [])))
               BitRShift
               (IntegerBinOp
                (IntegerBinOp
                 (IntegerBinOp
                  (IntegerConstant -4 (Integer 4 []))
                  BitAnd
                  (IntegerConstant -11420 (Integer 4 []))
                  (Integer 4 [])
                  (IntegerConstant -11420 (Integer 4 [])))
                 BitXor
                 (IntegerBinOp
                  (IntegerConstant -1 (Integer 4 []))
                  BitLShift
                  (IntegerConstant -3 (Integer 4 []))
                  (Integer 4 [])
                  (IntegerConstant 0 (Integer 4 [])))
                 (Integer 4 [])
                 (IntegerConstant -11420 (Integer 4 [])))
                Div
                (IntegerBinOp
                 (IntegerBinOp
                  (IntegerConstant -71214 (Integer 4 []))
                  Mul
                  (IntegerConstant 0 (Integer 4 []))
                  (Integer 4 [])
                  (IntegerConstant 0 (Integer 4 [])))
                 BitOr
                 (IntegerBinOp
                  (IntegerBinOp
                   (IntegerConstant 4468 (Integer 4 []))
                   Sub
                   (IntegerConstant -3 (Integer 4 []))
                   (Integer 4 [])
                   (IntegerConstant 4471 (Integer 4 [])))
                  Sub
                  (IntegerBinOp
                   (IntegerBinOp
                    (IntegerConstant -45 (Integer 4 []))
                    Mul
                    (IntegerConstant 1576 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant -70920 (Integer 4 [])))
                   BitXor
                   (IntegerBinOp
                    (IntegerBinOp
                     (IntegerConstant -3 (Integer 4 []))
                     BitOr
                     (IntegerConstant 0 (Integer 4 []))
                     (Integer 4 [])
                     (IntegerConstant -3 (Integer 4 [])))
                    BitAnd
                    (IntegerBinOp
                     (IntegerConstant -61 (Integer 4 []))
                     Add
                     (IntegerConstant 0 (Integer 4 []))
                     (Integer 4 [])
                     (IntegerConstant -61 (Integer 4 [])))
                    (Integer 4 [])
                    (IntegerConstant -63 (Integer 4 [])))
                   (Integer 4 [])
                   (IntegerConstant 70969 (Integer 4 [])))
                  (Integer 4 [])
                  (IntegerConstant -66498 (Integer 4 [])))
                 (Integer 4 [])
                 (IntegerConstant -66498 (Integer 4 [])))
                (Integer 4 [])
                (IntegerConstant 0 (Integer 4 [])))
               (Integer 4 [])
               (IntegerConstant 11378 (Integer 4 [])))
              BitXor
              (IntegerBinOp
               (IntegerConstant -268 (Integer 4 []))
               Div
               (IntegerConstant -5 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 53 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant 11335 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant -5117 (Integer 4 []))
              BitRShift
              (IntegerConstant -129238 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant -1 (Integer 4 [])))
             Sub
             (IntegerBinOp
              (IntegerConstant 14 (Integer 4 []))
              Pow
              (IntegerConstant 381 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant -1 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 0 (Integer 4 []))
             BitOr
             (IntegerConstant 941 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 941 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 134 (Integer 4 []))
             BitXor
             (IntegerConstant 1025248 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 1025126 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 868297 (Integer 4 []))
             Add
             (IntegerConstant -3 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 868294 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant -71 (Integer 4 []))
              Add
              (IntegerConstant 11508546 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 11508475 (Integer 4 [])))
             BitLShift
             (IntegerBinOp
              (IntegerConstant 797 (Integer 4 []))
              BitOr
              (IntegerConstant 317411 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 317439 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant -94 (Integer 4 []))
             Add
             (IntegerConstant -2846607 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant -2846701 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant 48609562 (Integer 4 []))
              BitOr
              (IntegerConstant 41937487 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 50330463 (Integer 4 [])))
             Sub
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant 537771 (Integer 4 []))
               BitRShift
               (IntegerConstant 194149710 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 32 (Integer 4 [])))
              Add
              (IntegerBinOp
               (IntegerConstant -268205325 (Integer 4 []))
               Mul
               (IntegerConstant -4 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 1072821300 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant 1072821332 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant -1022490869 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 2585 (Integer 4 []))
             BitAnd
             (IntegerConstant 102 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant -3117 (Integer 4 []))
               BitAnd
               (IntegerConstant -9 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -3117 (Integer 4 [])))
              Add
              (IntegerBinOp
               (IntegerBinOp
                (IntegerConstant -3 (Integer 4 []))
                Div
                (IntegerConstant -21160378 (Integer 4 []))
                (Integer 4 [])
                (IntegerConstant 0 (Integer 4 [])))
               BitAnd
               (IntegerBinOp
                (IntegerConstant 144342 (Integer 4 []))
                BitLShift
                (IntegerConstant 1222 (Integer 4 []))
                (Integer 4 [])
                (IntegerConstant 9237888 (Integer 4 [])))
               (Integer 4 [])
               (IntegerConstant 0 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant -3117 (Integer 4 [])))
             Pow
             (IntegerBinOp
              (IntegerConstant -972344 (Integer 4 []))
              BitAnd
              (IntegerConstant -188996 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant -980600 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant 407013 (Integer 4 []))
              Div
              (IntegerConstant 6075767 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             BitAnd
             (IntegerBinOp
              (IntegerConstant 694733403 (Integer 4 []))
              Sub
              (IntegerConstant 543 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 694732860 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 8530587 (Integer 4 []))
             Sub
             (IntegerConstant 203102368 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant -194571781 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 86778942 (Integer 4 []))
             Pow
             (IntegerConstant 39923 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant 7 (Integer 4 []))
               Mul
               (IntegerConstant 1181174602 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -321712378 (Integer 4 [])))
              Add
              (IntegerBinOp
               (IntegerBinOp
                (IntegerBinOp
                 (IntegerConstant 39525 (Integer 4 []))
                 BitOr
                 (IntegerConstant -1015174625 (Integer 4 []))
                 (Integer 4 [])
                 (IntegerConstant -1015170433 (Integer 4 [])))
                BitRShift
                (IntegerBinOp
                 (IntegerBinOp
                  (IntegerConstant -117 (Integer 4 []))
                  Mul
                  (IntegerConstant 283672 (Integer 4 []))
                  (Integer 4 [])
                  (IntegerConstant -33189624 (Integer 4 [])))
                 BitOr
                 (IntegerBinOp
                  (IntegerBinOp
                   (IntegerBinOp
                    (IntegerConstant -64550 (Integer 4 []))
                    BitAnd
                    (IntegerConstant -150585 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant -195646 (Integer 4 [])))
                   Add
                   (IntegerBinOp
                    (IntegerBinOp
                     (IntegerConstant -1 (Integer 4 []))
                     BitAnd
                     (IntegerConstant -523 (Integer 4 []))
                     (Integer 4 [])
                     (IntegerConstant -523 (Integer 4 [])))
                    Sub
                    (IntegerBinOp
                     (IntegerConstant -375549776 (Integer 4 []))
                     Mul
                     (IntegerConstant 64143703 (Integer 4 []))
                     (Integer 4 [])
                     (IntegerConstant -285156400 (Integer 4 [])))
                    (Integer 4 [])
                    (IntegerConstant 285155877 (Integer 4 [])))
                   (Integer 4 [])
                   (IntegerConstant 284960231 (Integer 4 [])))
                  Pow
                  (IntegerBinOp
                   (IntegerConstant 6848092 (Integer 4 []))
                   Sub
                   (IntegerConstant -660275 (Integer 4 []))
                   (Integer 4 [])
                   (IntegerConstant 7508367 (Integer 4 [])))
                  (Integer 4 [])
                  (IntegerConstant 1171000919 (Integer 4 [])))
                 (Integer 4 [])
                 (IntegerConstant -3301537 (Integer 4 [])))
                (Integer 4 [])
                (IntegerConstant -1 (Integer 4 [])))
               Mul
               (IntegerBinOp
                (IntegerConstant 0 (Integer 4 []))
                Sub
                (IntegerConstant -61469 (Integer 4 []))
                (Integer 4 [])
                (IntegerConstant 61469 (Integer 4 [])))
               (Integer 4 [])
               (IntegerConstant -61469 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant -321773847 (Integer 4 [])))
             Sub
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant -1328508642 (Integer 4 []))
               BitLShift
               (IntegerConstant -549 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -268435456 (Integer 4 [])))
              BitAnd
              (IntegerBinOp
               (IntegerConstant 259 (Integer 4 []))
               BitLShift
               (IntegerConstant -3 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 0 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant -321773847 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 1369 (Integer 4 []))
             Mul
             (IntegerConstant 9397 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 12864493 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant 475770 (Integer 4 []))
               BitOr
               (IntegerConstant 7 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 475775 (Integer 4 [])))
              BitXor
              (IntegerBinOp
               (IntegerConstant -7334 (Integer 4 []))
               BitAnd
               (IntegerConstant -3275 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -7408 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant -482961 (Integer 4 [])))
             Div
             (IntegerBinOp
              (IntegerConstant 893865 (Integer 4 []))
              BitAnd
              (IntegerConstant 118987 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 98441 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant -4 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant -23 (Integer 4 []))
             BitOr
             (IntegerConstant 3 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant -21 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant -1511226560 (Integer 4 []))
             Pow
             (IntegerConstant 15 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 1765537 (Integer 4 []))
             BitRShift
             (IntegerConstant 92021886 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant -348863 (Integer 4 []))
             Pow
             (IntegerConstant -1 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant -63 (Integer 4 []))
              BitRShift
              (IntegerConstant 16 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant -1 (Integer 4 [])))
             Sub
             (IntegerBinOp
              (IntegerConstant -1897713420 (Integer 4 []))
              Add
              (IntegerConstant -1178921985 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 1218331891 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant -1218331892 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant -2464 (Integer 4 []))
               BitRShift
               (IntegerConstant 2051634386 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -1 (Integer 4 [])))
              BitLShift
              (IntegerBinOp
               (IntegerConstant 11 (Integer 4 []))
               Mul
               (IntegerConstant -222 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -2442 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             BitAnd
             (IntegerBinOp
              (IntegerBinOp
               (IntegerBinOp
                (IntegerConstant -2355 (Integer 4 []))
                BitAnd
                (IntegerConstant 1548281831 (Integer 4 []))
                (Integer 4 [])
                (IntegerConstant 1548281541 (Integer 4 [])))
               BitLShift
               (IntegerBinOp
                (IntegerConstant -1399 (Integer 4 []))
                BitOr
                (IntegerConstant -297217172 (Integer 4 []))
                (Integer 4 [])
                (IntegerConstant -1043 (Integer 4 [])))
               (Integer 4 [])
               (IntegerConstant 0 (Integer 4 [])))
              Mul
              (IntegerBinOp
               (IntegerConstant 1684384503 (Integer 4 []))
               BitRShift
               (IntegerConstant 1932090 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 0 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant -917319909 (Integer 4 []))
             BitAnd
             (IntegerConstant -9428 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant -917320952 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 20125 (Integer 4 []))
             BitRShift
             (IntegerConstant -1980215081 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 1315691093 (Integer 4 []))
             Sub
             (IntegerConstant -800572 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 1316491665 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 1175478516 (Integer 4 []))
             Add
             (IntegerConstant -1336597199 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant -161118683 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant -1708570193 (Integer 4 []))
             Pow
             (IntegerConstant 0 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 1 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant -9052268 (Integer 4 []))
              BitRShift
              (IntegerConstant -28 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant -1 (Integer 4 [])))
             BitRShift
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant 26433030 (Integer 4 []))
               BitOr
               (IntegerConstant -102 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -98 (Integer 4 [])))
              Add
              (IntegerBinOp
               (IntegerBinOp
                (IntegerConstant 1 (Integer 4 []))
                Pow
                (IntegerConstant 885 (Integer 4 []))
                (Integer 4 [])
                (IntegerConstant 1 (Integer 4 [])))
               BitOr
               (IntegerBinOp
                (IntegerBinOp
                 (IntegerConstant 1705820023 (Integer 4 []))
                 Mul
                 (IntegerConstant 1154532663 (Integer 4 []))
                 (Integer 4 [])
                 (IntegerConstant -198986351 (Integer 4 [])))
                BitOr
                (IntegerBinOp
                 (IntegerBinOp
                  (IntegerConstant -119807161 (Integer 4 []))
                  Pow
                  (IntegerConstant 7 (Integer 4 []))
                  (Integer 4 [])
                  (IntegerConstant -1055423561 (Integer 4 [])))
                 Sub
                 (IntegerBinOp
                  (IntegerConstant -2556739 (Integer 4 []))
                  Mul
                  (IntegerConstant 1090036273 (Integer 4 []))
                  (Integer 4 [])
                  (IntegerConstant 1603271213 (Integer 4 [])))
                 (Integer 4 [])
                 (IntegerConstant 1636272522 (Integer 4 [])))
                (Integer 4 [])
                (IntegerConstant -173556325 (Integer 4 [])))
               (Integer 4 [])
               (IntegerConstant -173556325 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant -173556423 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant -1 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant -960277 (Integer 4 []))
              Pow
              (IntegerConstant -327799 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             BitOr
             (IntegerBinOp
              (IntegerConstant -15 (Integer 4 []))
              Div
              (IntegerConstant 894982665 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant -31594 (Integer 4 []))
             BitAnd
             (IntegerConstant -1466288854 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant -1466301438 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 634 (Integer 4 []))
             BitRShift
             (IntegerConstant -66037 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant 22861 (Integer 4 []))
               BitXor
               (IntegerConstant 1488112562 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 1488098047 (Integer 4 [])))
              BitRShift
              (IntegerBinOp
               (IntegerConstant 1010168 (Integer 4 []))
               Mul
               (IntegerConstant 1 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 1010168 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             Pow
             (IntegerBinOp
              (IntegerConstant 2901 (Integer 4 []))
              Sub
              (IntegerConstant -498472241 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 498475142 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant 1339148436 (Integer 4 []))
              BitAnd
              (IntegerConstant 1503154 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 1099920 (Integer 4 [])))
             BitXor
             (IntegerBinOp
              (IntegerConstant 0 (Integer 4 []))
              Add
              (IntegerConstant 2103982159 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 2103982159 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 2105077983 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant -1941587 (Integer 4 []))
             BitXor
             (IntegerConstant -256 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 1941677 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant -1 (Integer 4 []))
              BitAnd
              (IntegerConstant -1157060237 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant -1157060237 (Integer 4 [])))
             BitLShift
             (IntegerBinOp
              (IntegerBinOp
               (IntegerBinOp
                (IntegerConstant 1812082097 (Integer 4 []))
                BitLShift
                (IntegerConstant -1082700969 (Integer 4 []))
                (Integer 4 [])
                (IntegerConstant -662700032 (Integer 4 [])))
               Mul
               (IntegerBinOp
                (IntegerBinOp
                 (IntegerBinOp
                  (IntegerConstant 1328 (Integer 4 []))
                  Div
                  (IntegerConstant -4 (Integer 4 []))
                  (Integer 4 [])
                  (IntegerConstant -332 (Integer 4 [])))
                 Mul
                 (IntegerBinOp
                  (IntegerConstant 26461463 (Integer 4 []))
                  BitLShift
                  (IntegerConstant -1345192584 (Integer 4 []))
                  (Integer 4 [])
                  (IntegerConstant 0 (Integer 4 [])))
                 (Integer 4 [])
                 (IntegerConstant 0 (Integer 4 [])))
                Add
                (IntegerBinOp
                 (IntegerConstant 1464 (Integer 4 []))
                 Add
                 (IntegerConstant 2091767528 (Integer 4 []))
                 (Integer 4 [])
                 (IntegerConstant 2091768992 (Integer 4 [])))
                (Integer 4 [])
                (IntegerConstant 2091768992 (Integer 4 [])))
               (Integer 4 [])
               (IntegerConstant 1342177280 (Integer 4 [])))
              BitXor
              (IntegerBinOp
               (IntegerConstant -1853121548 (Integer 4 []))
               BitXor
               (IntegerConstant -1518733221 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 888300463 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant 1693606831 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant 45 (Integer 4 []))
              Pow
              (IntegerConstant -2865 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             BitXor
             (IntegerBinOp
              (IntegerConstant 1479862343 (Integer 4 []))
              Pow
              (IntegerConstant 3 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant -324358633 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant -324358633 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant -2 (Integer 4 []))
              Div
              (IntegerConstant 1349767838 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             BitOr
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant 67604 (Integer 4 []))
               BitXor
               (IntegerConstant -7516 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -70992 (Integer 4 [])))
              BitXor
              (IntegerBinOp
               (IntegerBinOp
                (IntegerBinOp
                 (IntegerConstant -1436655394 (Integer 4 []))
                 Div
                 (IntegerConstant -1893721661 (Integer 4 []))
                 (Integer 4 [])
                 (IntegerConstant 0 (Integer 4 [])))
                Add
                (IntegerBinOp
                 (IntegerConstant -1771217527 (Integer 4 []))
                 BitAnd
                 (IntegerConstant -1979775451 (Integer 4 []))
                 (Integer 4 [])
                 (IntegerConstant -2140339199 (Integer 4 [])))
                (Integer 4 [])
                (IntegerConstant -2140339199 (Integer 4 [])))
               BitOr
               (IntegerBinOp
                (IntegerBinOp
                 (IntegerConstant -1263321463 (Integer 4 []))
                 Pow
                 (IntegerConstant -1660956018 (Integer 4 []))
                 (Integer 4 [])
                 (IntegerConstant 0 (Integer 4 [])))
                Add
                (IntegerBinOp
                 (IntegerConstant 1627457443 (Integer 4 []))
                 BitLShift
                 (IntegerConstant 1163734342 (Integer 4 []))
                 (Integer 4 [])
                 (IntegerConstant 1078061248 (Integer 4 [])))
                (Integer 4 [])
                (IntegerConstant 1078061248 (Integer 4 [])))
               (Integer 4 [])
               (IntegerConstant -1066537791 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant 1066600049 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 1066600049 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 1549808298 (Integer 4 []))
             Div
             (IntegerConstant 2037271740 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant -1391551772 (Integer 4 []))
             Div
             (IntegerConstant 2177 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant -639206 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant 12 (Integer 4 []))
              BitAnd
              (IntegerConstant -1561740 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 4 (Integer 4 [])))
             BitAnd
             (IntegerBinOp
              (IntegerConstant -105400863 (Integer 4 []))
              BitXor
              (IntegerConstant -120096 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 105488129 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant 28 (Integer 4 []))
               BitAnd
               (IntegerConstant -1144815517 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 0 (Integer 4 [])))
              Sub
              (IntegerBinOp
               (IntegerConstant -1216753504 (Integer 4 []))
               Add
               (IntegerConstant 975540881 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -241212623 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant 241212623 (Integer 4 [])))
             BitRShift
             (IntegerBinOp
              (IntegerConstant -175978 (Integer 4 []))
              Div
              (IntegerConstant 1486577020 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 241212623 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant -11926 (Integer 4 []))
             BitOr
             (IntegerConstant -1833890007 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant -8341 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 1661993186 (Integer 4 []))
             BitAnd
             (IntegerConstant 1687755554 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 1611661346 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 1345151947 (Integer 4 []))
             BitAnd
             (IntegerConstant 1665532194 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 1074004226 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant -1867831060 (Integer 4 []))
               BitAnd
               (IntegerConstant 15394941 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 11151468 (Integer 4 [])))
              Mul
              (IntegerBinOp
               (IntegerConstant 32708 (Integer 4 []))
               Mul
               (IntegerConstant 256929606 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -1597445224 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant -1400691680 (Integer 4 [])))
             Div
             (IntegerBinOp
              (IntegerConstant 3751 (Integer 4 []))
              BitOr
              (IntegerConstant 1961223015 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 1961226215 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant -111 (Integer 4 []))
              Pow
              (IntegerConstant 20249 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 1175851281 (Integer 4 [])))
             BitRShift
             (IntegerBinOp
              (IntegerConstant -651620583 (Integer 4 []))
              Sub
              (IntegerConstant 20 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant -651620603 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 36745352 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant 2087255540 (Integer 4 []))
              BitRShift
              (IntegerConstant 1481702442 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             Mul
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant -48435066 (Integer 4 []))
               BitOr
               (IntegerConstant -463 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -329 (Integer 4 [])))
              BitOr
              (IntegerBinOp
               (IntegerBinOp
                (IntegerConstant 415177 (Integer 4 []))
                BitRShift
                (IntegerConstant -1 (Integer 4 []))
                (Integer 4 [])
                (IntegerConstant 0 (Integer 4 [])))
               BitAnd
               (IntegerBinOp
                (IntegerBinOp
                 (IntegerConstant -15830259 (Integer 4 []))
                 Add
                 (IntegerConstant 1434819963 (Integer 4 []))
                 (Integer 4 [])
                 (IntegerConstant 1418989704 (Integer 4 [])))
                Sub
                (IntegerBinOp
                 (IntegerBinOp
                  (IntegerConstant -1895448581 (Integer 4 []))
                  BitAnd
                  (IntegerConstant -2023181388 (Integer 4 []))
                  (Integer 4 [])
                  (IntegerConstant -2029997136 (Integer 4 [])))
                 Add
                 (IntegerBinOp
                  (IntegerBinOp
                   (IntegerConstant -501 (Integer 4 []))
                   Sub
                   (IntegerConstant -1323869491 (Integer 4 []))
                   (Integer 4 [])
                   (IntegerConstant 1323868990 (Integer 4 [])))
                  Sub
                  (IntegerBinOp
                   (IntegerBinOp
                    (IntegerConstant -1152365138 (Integer 4 []))
                    BitOr
                    (IntegerConstant -1768315642 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant -1076236882 (Integer 4 [])))
                   BitOr
                   (IntegerBinOp
                    (IntegerConstant 32819887 (Integer 4 []))
                    Mul
                    (IntegerConstant 24246540 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 1457305396 (Integer 4 [])))
                   (Integer 4 [])
                   (IntegerConstant -2228290 (Integer 4 [])))
                  (Integer 4 [])
                  (IntegerConstant 1326097280 (Integer 4 [])))
                 (Integer 4 [])
                 (IntegerConstant -703899856 (Integer 4 [])))
                (Integer 4 [])
                (IntegerConstant 2122889560 (Integer 4 [])))
               (Integer 4 [])
               (IntegerConstant 0 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant -329 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant 1660758163 (Integer 4 []))
              BitRShift
              (IntegerConstant 1246648350 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 1 (Integer 4 [])))
             Add
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant -106234109 (Integer 4 []))
               Mul
               (IntegerConstant -1825931366 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -1797397298 (Integer 4 [])))
              BitXor
              (IntegerBinOp
               (IntegerConstant -308937 (Integer 4 []))
               Mul
               (IntegerConstant -2122144920 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -492715176 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant 1987848086 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 1987848087 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant 1440300581 (Integer 4 []))
              Sub
              (IntegerConstant 1975277196 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant -534976615 (Integer 4 [])))
             Pow
             (IntegerBinOp
              (IntegerBinOp
               (IntegerBinOp
                (IntegerConstant 91232052 (Integer 4 []))
                Sub
                (IntegerConstant -126425 (Integer 4 []))
                (Integer 4 [])
                (IntegerConstant 91358477 (Integer 4 [])))
               Add
               (IntegerBinOp
                (IntegerBinOp
                 (IntegerBinOp
                  (IntegerConstant -3507 (Integer 4 []))
                  BitRShift
                  (IntegerConstant -1668352580 (Integer 4 []))
                  (Integer 4 [])
                  (IntegerConstant -1 (Integer 4 [])))
                 BitOr
                 (IntegerBinOp
                  (IntegerBinOp
                   (IntegerBinOp
                    (IntegerConstant -1417228438 (Integer 4 []))
                    Sub
                    (IntegerConstant 6 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant -1417228444 (Integer 4 [])))
                   Div
                   (IntegerBinOp
                    (IntegerConstant -1362296541 (Integer 4 []))
                    Add
                    (IntegerConstant 1814115714 (Integer 4 []))
                    (Integer 4 [])
                    (IntegerConstant 451819173 (Integer 4 [])))
                   (Integer 4 [])
                   (IntegerConstant -3 (Integer 4 [])))
                  BitAnd
                  (IntegerBinOp
                   (IntegerConstant -37 (Integer 4 []))
                   Add
                   (IntegerConstant -1508376319 (Integer 4 []))
                   (Integer 4 [])
                   (IntegerConstant -1508376356 (Integer 4 [])))
                  (Integer 4 [])
                  (IntegerConstant -1508376356 (Integer 4 [])))
                 (Integer 4 [])
                 (IntegerConstant -1 (Integer 4 [])))
                BitLShift
                (IntegerBinOp
                 (IntegerBinOp
                  (IntegerConstant -1797755228 (Integer 4 []))
                  Mul
                  (IntegerConstant -7804 (Integer 4 []))
                  (Integer 4 [])
                  (IntegerConstant -1976356720 (Integer 4 [])))
                 Mul
                 (IntegerBinOp
                  (IntegerConstant -188858692 (Integer 4 []))
                  Mul
                  (IntegerConstant 1 (Integer 4 []))
                  (Integer 4 [])
                  (IntegerConstant -188858692 (Integer 4 [])))
                 (Integer 4 [])
                 (IntegerConstant 775588288 (Integer 4 [])))
                (Integer 4 [])
                (IntegerConstant -1 (Integer 4 [])))
               (Integer 4 [])
               (IntegerConstant 91358476 (Integer 4 [])))
              BitXor
              (IntegerBinOp
               (IntegerConstant -1741990973 (Integer 4 []))
               BitAnd
               (IntegerConstant -1 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -1741990973 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant -1655089457 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant 1623782135 (Integer 4 []))
              BitAnd
              (IntegerConstant -892 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 1623781508 (Integer 4 [])))
             BitLShift
             (IntegerBinOp
              (IntegerConstant 540878 (Integer 4 []))
              Pow
              (IntegerConstant 1968862947 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 1623781508 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant 1171965 (Integer 4 []))
               BitXor
               (IntegerConstant -969065 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -2042006 (Integer 4 [])))
              BitAnd
              (IntegerBinOp
               (IntegerConstant 15712 (Integer 4 []))
               Pow
               (IntegerConstant -5 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 0 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             BitXor
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant -1 (Integer 4 []))
               Mul
               (IntegerConstant -3198 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 3198 (Integer 4 [])))
              Pow
              (IntegerBinOp
               (IntegerConstant 1084842353 (Integer 4 []))
               BitXor
               (IntegerConstant 307 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 1084842050 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant -215418633 (Integer 4 []))
             BitXor
             (IntegerConstant -1142137960 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 1220846447 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant -95 (Integer 4 []))
              BitOr
              (IntegerConstant 1 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant -95 (Integer 4 [])))
             BitOr
             (IntegerBinOp
              (IntegerConstant -774070 (Integer 4 []))
              Add
              (IntegerConstant -16408 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant -790478 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant -77 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant 1746489379 (Integer 4 []))
              Pow
              (IntegerConstant 731527 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 863927403 (Integer 4 [])))
             Mul
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant 93 (Integer 4 []))
               BitAnd
               (IntegerConstant -1080094198 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 8 (Integer 4 [])))
              Mul
              (IntegerBinOp
               (IntegerConstant -2122727044 (Integer 4 []))
               BitLShift
               (IntegerConstant 1400143037 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 0 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant 1404800247 (Integer 4 []))
               BitOr
               (IntegerConstant -2038262184 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -675569921 (Integer 4 [])))
              BitLShift
              (IntegerBinOp
               (IntegerConstant 13616493 (Integer 4 []))
               Mul
               (IntegerConstant 1198950712 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -1722252072 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant -16777216 (Integer 4 [])))
             Sub
             (IntegerBinOp
              (IntegerConstant 1314667220 (Integer 4 []))
              BitLShift
              (IntegerConstant 2347 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant -16777216 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 1167040020 (Integer 4 []))
             Sub
             (IntegerConstant -140 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 1167040160 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant 1118482127 (Integer 4 []))
              BitAnd
              (IntegerConstant 199 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 199 (Integer 4 [])))
             BitLShift
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant 1560513782 (Integer 4 []))
               BitRShift
               (IntegerConstant -1841519222 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 1523939 (Integer 4 [])))
              BitOr
              (IntegerBinOp
               (IntegerConstant -1242146893 (Integer 4 []))
               Mul
               (IntegerConstant -22 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 1557427870 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant 1557624575 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 1641314468 (Integer 4 []))
             Sub
             (IntegerConstant -3626 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 1641318094 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 2 (Integer 4 []))
             BitLShift
             (IntegerConstant 543927969 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant -1262086145 (Integer 4 []))
              BitOr
              (IntegerConstant -1748089221 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant -1211213825 (Integer 4 [])))
             BitXor
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant -1379407705 (Integer 4 []))
               BitAnd
               (IntegerConstant 58401442 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 21176482 (Integer 4 [])))
              Sub
              (IntegerBinOp
               (IntegerConstant -40065 (Integer 4 []))
               Add
               (IntegerConstant 1562286473 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 1562246408 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant -1541069926 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 334197861 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant 10 (Integer 4 []))
               BitOr
               (IntegerConstant -1453648092 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -1453648082 (Integer 4 [])))
              Div
              (IntegerBinOp
               (IntegerConstant -1226705815 (Integer 4 []))
               BitAnd
               (IntegerConstant 1701955466 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 610386952 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant -2 (Integer 4 [])))
             Pow
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant 2058341435 (Integer 4 []))
               Sub
               (IntegerConstant 793985074 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 1264356361 (Integer 4 [])))
              Sub
              (IntegerBinOp
               (IntegerConstant 0 (Integer 4 []))
               BitXor
               (IntegerConstant 1949 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 1949 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant 1264354412 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant 7 (Integer 4 []))
               BitAnd
               (IntegerConstant 1119878050 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 2 (Integer 4 [])))
              BitLShift
              (IntegerBinOp
               (IntegerConstant 1918609211 (Integer 4 []))
               BitAnd
               (IntegerConstant 1471913660 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 1377535544 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             BitAnd
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant -2012896083 (Integer 4 []))
               BitXor
               (IntegerConstant 329074952 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -1684476507 (Integer 4 [])))
              BitOr
              (IntegerBinOp
               (IntegerConstant 2443030 (Integer 4 []))
               BitLShift
               (IntegerConstant 15253 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -490733568 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant -69669467 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant -1519260986 (Integer 4 []))
             Sub
             (IntegerConstant -1797869041 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 278608055 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant -1295707354 (Integer 4 []))
             Mul
             (IntegerConstant -257353895 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 525472310 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 1260560111 (Integer 4 []))
             Sub
             (IntegerConstant 2200 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 1260557911 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant -1238319807 (Integer 4 []))
              Mul
              (IntegerConstant -454 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant -443523398 (Integer 4 [])))
             Div
             (IntegerBinOp
              (IntegerConstant 1803056036 (Integer 4 []))
              BitLShift
              (IntegerConstant 1198516238 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 485031936 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant -89619 (Integer 4 []))
             BitOr
             (IntegerConstant -26159505 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant -67601 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 1471734236 (Integer 4 []))
             BitOr
             (IntegerConstant -1888349444 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant -537201668 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant -1791192700 (Integer 4 []))
              Mul
              (IntegerConstant -849 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 304179516 (Integer 4 [])))
             Mul
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant -2045985789 (Integer 4 []))
               Sub
               (IntegerConstant 0 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -2045985789 (Integer 4 [])))
              Add
              (IntegerBinOp
               (IntegerBinOp
                (IntegerConstant -66317911 (Integer 4 []))
                Mul
                (IntegerConstant -16 (Integer 4 []))
                (Integer 4 [])
                (IntegerConstant 1061086576 (Integer 4 [])))
               Mul
               (IntegerBinOp
                (IntegerConstant -100210880 (Integer 4 []))
                Sub
                (IntegerConstant 1744456937 (Integer 4 []))
                (Integer 4 [])
                (IntegerConstant -1844667817 (Integer 4 [])))
               (Integer 4 [])
               (IntegerConstant -173713136 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant 2075268371 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 366263156 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant -1360194116 (Integer 4 []))
              Mul
              (IntegerConstant 1897827120 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 256452416 (Integer 4 [])))
             Sub
             (IntegerBinOp
              (IntegerConstant -60280056 (Integer 4 []))
              BitRShift
              (IntegerConstant 7 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant -470938 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 256923354 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant 1958564935 (Integer 4 []))
              Pow
              (IntegerConstant -1280395811 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             Div
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant -17861024 (Integer 4 []))
               BitRShift
               (IntegerConstant 2115300944 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -273 (Integer 4 [])))
              Mul
              (IntegerBinOp
               (IntegerConstant 163831 (Integer 4 []))
               BitAnd
               (IntegerConstant 11121 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 11121 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant -3036033 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant -1155471318 (Integer 4 []))
             Add
             (IntegerConstant -1640830131 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 1498665847 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant 130073 (Integer 4 []))
             BitOr
             (IntegerConstant -15 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant -7 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant -402 (Integer 4 []))
              Add
              (IntegerConstant 1413085516 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 1413085114 (Integer 4 [])))
             Sub
             (IntegerBinOp
              (IntegerBinOp
               (IntegerBinOp
                (IntegerConstant -4 (Integer 4 []))
                BitRShift
                (IntegerConstant 1314379300 (Integer 4 []))
                (Integer 4 [])
                (IntegerConstant -1 (Integer 4 [])))
               BitRShift
               (IntegerBinOp
                (IntegerBinOp
                 (IntegerBinOp
                  (IntegerConstant -1251 (Integer 4 []))
                  BitAnd
                  (IntegerConstant -1230670063 (Integer 4 []))
                  (Integer 4 [])
                  (IntegerConstant -1230671087 (Integer 4 [])))
                 BitRShift
                 (IntegerBinOp
                  (IntegerConstant 1938921160 (Integer 4 []))
                  BitXor
                  (IntegerConstant 15 (Integer 4 []))
                  (Integer 4 [])
                  (IntegerConstant 1938921159 (Integer 4 [])))
                 (Integer 4 [])
                 (IntegerConstant -9614618 (Integer 4 [])))
                Pow
                (IntegerBinOp
                 (IntegerConstant -1281284811 (Integer 4 []))
                 BitLShift
                 (IntegerConstant -2008462079 (Integer 4 []))
                 (Integer 4 [])
                 (IntegerConstant 1732397674 (Integer 4 [])))
                (Integer 4 [])
                (IntegerConstant 0 (Integer 4 [])))
               (Integer 4 [])
               (IntegerConstant -1 (Integer 4 [])))
              BitRShift
              (IntegerBinOp
               (IntegerBinOp
                (IntegerConstant -1744007319 (Integer 4 []))
                BitRShift
                (IntegerConstant 1824749914 (Integer 4 []))
                (Integer 4 [])
                (IntegerConstant -26 (Integer 4 [])))
               Add
               (IntegerBinOp
                (IntegerConstant -1796663213 (Integer 4 []))
                BitOr
                (IntegerConstant -81598 (Integer 4 []))
                (Integer 4 [])
                (IntegerConstant -9901 (Integer 4 [])))
               (Integer 4 [])
               (IntegerConstant -9927 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant -1 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 1413085115 (Integer 4 [])))
            (IntegerBinOp
             (IntegerConstant -2088358006 (Integer 4 []))
             BitLShift
             (IntegerConstant 1777312987 (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant 1342177280 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant -13630 (Integer 4 []))
              BitOr
              (IntegerConstant 1116 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant -12578 (Integer 4 [])))
             Div
             (IntegerBinOp
              (IntegerBinOp
               (IntegerBinOp
                (IntegerConstant -1005546915 (Integer 4 []))
                BitOr
                (IntegerConstant -3412 (Integer 4 []))
                (Integer 4 [])
                (IntegerConstant -3331 (Integer 4 [])))
               Div
               (IntegerBinOp
                (IntegerBinOp
                 (IntegerConstant -535 (Integer 4 []))
                 BitAnd
                 (IntegerConstant -1821882865 (Integer 4 []))
                 (Integer 4 [])
                 (IntegerConstant -1821883383 (Integer 4 [])))
                Div
                (IntegerBinOp
                 (IntegerConstant 1255958494 (Integer 4 []))
                 Add
                 (IntegerConstant -233 (Integer 4 []))
                 (Integer 4 [])
                 (IntegerConstant 1255958261 (Integer 4 [])))
                (Integer 4 [])
                (IntegerConstant -1 (Integer 4 [])))
               (Integer 4 [])
               (IntegerConstant 3331 (Integer 4 [])))
              Sub
              (IntegerBinOp
               (IntegerConstant 1 (Integer 4 []))
               Mul
               (IntegerConstant 1673535203 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 1673535203 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant -1673531872 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerConstant -274848697 (Integer 4 []))
              Pow
              (IntegerConstant -2147069198 (Integer 4 []))
              (Integer 4 [])
              (IntegerConstant 0 (Integer 4 [])))
             Div
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant -60388204 (Integer 4 []))
               BitXor
               (IntegerConstant 1193783339 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -1153352513 (Integer 4 [])))
              BitOr
              (IntegerBinOp
               (IntegerBinOp
                (IntegerConstant 15003526 (Integer 4 []))
                Mul
                (IntegerConstant -1363638881 (Integer 4 []))
                (Integer 4 [])
                (IntegerConstant -1158585798 (Integer 4 [])))
               Div
               (IntegerBinOp
                (IntegerBinOp
                 (IntegerBinOp
                  (IntegerConstant 1556751086 (Integer 4 []))
                  BitLShift
                  (IntegerConstant 1650912130 (Integer 4 []))
                  (Integer 4 [])
                  (IntegerConstant 1932037048 (Integer 4 [])))
                 Sub
                 (IntegerBinOp
                  (IntegerConstant 1627827830 (Integer 4 []))
                  Sub
                  (IntegerConstant -1 (Integer 4 []))
                  (Integer 4 [])
                  (IntegerConstant 1627827831 (Integer 4 [])))
                 (Integer 4 [])
                 (IntegerConstant 304209217 (Integer 4 [])))
                Add
                (IntegerBinOp
                 (IntegerConstant -251803 (Integer 4 []))
                 BitAnd
                 (IntegerConstant 50 (Integer 4 []))
                 (Integer 4 [])
                 (IntegerConstant 32 (Integer 4 [])))
                (Integer 4 [])
                (IntegerConstant 304209249 (Integer 4 [])))
               (Integer 4 [])
               (IntegerConstant -3 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant -1 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant 0 (Integer 4 [])))
            (IntegerBinOp
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant -52 (Integer 4 []))
               BitRShift
               (IntegerConstant 2065784890 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -1 (Integer 4 [])))
              BitAnd
              (IntegerBinOp
               (IntegerConstant -13 (Integer 4 []))
               BitOr
               (IntegerConstant 1478364542 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant -1 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant -1 (Integer 4 [])))
             Pow
             (IntegerBinOp
              (IntegerBinOp
               (IntegerConstant 24562262 (Integer 4 []))
               BitLShift
               (IntegerConstant -1253270 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 0 (Integer 4 [])))
              BitOr
              (IntegerBinOp
               (IntegerConstant 1901688711 (Integer 4 []))
               BitOr
               (IntegerConstant 1 (Integer 4 []))
               (Integer 4 [])
               (IntegerConstant 1901688711 (Integer 4 [])))
              (Integer 4 [])
              (IntegerConstant 1901688711 (Integer 4 [])))
             (Integer 4 [])
             (IntegerConstant -1 (Integer 4 [])))))


(deftest sample-100-test
  (testing "sample of 100 i32 bin ops:"
    (testing "leaf counts"
      (is (every? #(>= % 2)
                  (map i32-bin-op-semsem-leaf-count foo))))
    (testing "validity"
      (is (every? identity
                  (map #(s/valid? :asr.core/i32-bin-op-semsem %) foo))))
    (testing "value"
      (is (not-any? nil?
                    (map maybe-value-i32-semsem foo))))))
