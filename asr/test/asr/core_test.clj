(ns asr.core-test
  (:use [asr.core]

        [asr.utils]
        [asr.data]
        [asr.parsed]
        [asr.autospecs]
        [asr.specs])
  (:require [clojure.test                  :refer :all]
            [clojure.spec.alpha            :as s      ]
            [clojure.spec.gen.alpha        :as gen    ]
            [clojure.test.check.generators :as tgen   ]
            [clojure.test.check.properties :as tprop  ]))


;; Failure of the NSPECS number check will remind you to write
;; tests for your new specs!

(def NSPECS          136) ;; Adjust to the number of specs in core.clj.
(def NTESTS           50) ;; Bigger for more stress, smaller for more speed
(def RECURSION-LIMIT   4) ;; ditto


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
  (testing "kebab-case"
    (is (= (nskw-kebab-from 'TranslationUnit)
           :asr.core/translation-unit))
    (is (= (nskw-kebab-from "TranslationUnit")
           :asr.core/translation-unit))
    (is (thrown?
         Exception
         (nskw-kebab-from :should-fail)))))


(deftest whole-spec-test
  (testing "whole example passes trivial spec"
    (is (s/valid? list? expr-01-211000))))


(deftest shallow-map-from-speclet-test

  (testing "shallow map from speclet"

    (is (= (shallow-map-from-speclet (speclets 3))
           {:ASDL-FORMS
            '([:ASDL-SYMCONST "Public"] [:ASDL-SYMCONST "Private"]),
            :ASDL-TERM "access"}))

    (is (= (shallow-map-from-speclet (speclets 0))
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

    (is (= (shallow-map-from-speclet (speclets 22))
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
    (is (= #{:asr.core/array_index    :asr.core/cast_kind
             :asr.core/ttype          :asr.core/stmt
             :asr.core/expr           :asr.core/restriction
             :asr.core/binop          :asr.core/dimension
             :asr.core/abi            :asr.core/attribute_arg
             :asr.core/unit           :asr.core/presence
             :asr.core/cmpop          :asr.core/tbind
             :asr.core/attribute      :asr.core/arraybound
             :asr.core/deftype        :asr.core/alloc_arg
             :asr.core/call_arg       :asr.core/storage_type
             :asr.core/do_loop_head   :asr.core/symbol
             :asr.core/access         :asr.core/integerboz
             :asr.core/intent         :asr.core/case_stmt
             :asr.core/trait          :asr.core/logicalbinop}
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
    (is (= #{:asr.core/ArrayPack             :asr.core/WhileLoop
             :asr.core/Interactive           :asr.core/LogicalNot
             :asr.core/TemplateBinOp         :asr.core/Add
             :asr.core/ListSection           :asr.core/StringItem
             :asr.core/Hex                   :asr.core/DerivedType
             :asr.core/LFortranModule        :asr.core/RealUnaryMinus
             :asr.core/StringRepeat          :asr.core/SetInsert
             :asr.core/NEqv                  :asr.core/IntegerConstant
             :asr.core/FunctionCall          :asr.core/ArraySection
             :asr.core/BitAnd                :asr.core/CaseStmt_Range
             :asr.core/Parameter             :asr.core/IfArithmetic
             :asr.core/IntegerToCharacter    :asr.core/Associate
             :asr.core/Pow                   :asr.core/Allocatable
             :asr.core/FileOpen              :asr.core/LogicalToCharacter
             :asr.core/TranslationUnit       :asr.core/ComplexIm
             :asr.core/AssociateBlock        :asr.core/DerivedTypeConstructor
             :asr.core/FileRewind            :asr.core/ComplexConstant
             :asr.core/DictInsert            :asr.core/RealToReal
             :asr.core/InOut                 :asr.core/ComplexRe
             :asr.core/GFortranModule        :asr.core/Dict
             :asr.core/FileWrite             :asr.core/ComplexBinOp
             :asr.core/LogicalToReal         :asr.core/Character
             :asr.core/Public                :asr.core/IntegerToComplex
             :asr.core/Eqv                   :asr.core/RealToCharacter
             :asr.core/ArrayConstant         :asr.core/DoLoop
             :asr.core/ArrayTranspose        :asr.core/IntegerUnaryMinus
             :asr.core/ComplexUnaryMinus     :asr.core/Complex
             :asr.core/ComplexCompare        :asr.core/Class
             :asr.core/RealToComplex         :asr.core/And
             :asr.core/DoConcurrentLoop      :asr.core/ErrorStop
             :asr.core/Return                :asr.core/Xor
             :asr.core/ListClear             :asr.core/LBound
             :asr.core/In                    :asr.core/SetLen
             :asr.core/Print                 :asr.core/Attribute
             :asr.core/GoTo                  :asr.core/ImpliedDoLoop
             :asr.core/Source                :asr.core/Intrinsic
             :asr.core/Program               :asr.core/Implementation
             :asr.core/IntegerBOZ            :asr.core/Real
             :asr.core/SubroutineCall        :asr.core/Required
             :asr.core/IntegerBitLen         :asr.core/SupportsPlus
             :asr.core/LtE                   :asr.core/EnumRef
             :asr.core/ArrayMatMul           :asr.core/TupleLen
             :asr.core/ComplexToReal         :asr.core/StringSection
             :asr.core/ComplexConstructor    :asr.core/BitOr
             :asr.core/RealCompare           :asr.core/IntegerBitNot
             :asr.core/Sub                   :asr.core/ReturnVar
             :asr.core/LogicalCompare        :asr.core/IfExp
             :asr.core/Div                   :asr.core/Var
             :asr.core/FileInquire           :asr.core/CPtrToPointer
             :asr.core/Private               :asr.core/ListConstant
             :asr.core/Eq                    :asr.core/Lt
             :asr.core/RealToLogical         :asr.core/IntegerBinOp
             :asr.core/Enum                  :asr.core/SetRemove
             :asr.core/LogicalToInteger      :asr.core/CPtr
             :asr.core/Local                 :asr.core/ListInsert
             :asr.core/Mul                   :asr.core/PointerToCPtr
             :asr.core/Default               :asr.core/DictConstant
             :asr.core/Out                   :asr.core/RealConstant
             :asr.core/StringLen             :asr.core/ExplicitDeallocate
             :asr.core/ListAppend            :asr.core/NamedExpr
             :asr.core/BitLShift             :asr.core/Where
             :asr.core/Integer               :asr.core/CharacterToLogical
             :asr.core/BindC                 :asr.core/Unspecified
             :asr.core/Exit                  :asr.core/UBound
             :asr.core/TypeParameter         :asr.core/OverloadedBinOp
             :asr.core/ImplicitDeallocate    :asr.core/Select
             :asr.core/BlockCall             :asr.core/Block
             :asr.core/DerivedRef            :asr.core/Stop
             :asr.core/Assert                :asr.core/Optional
             :asr.core/SupportsZero          :asr.core/DictItem
             :asr.core/FileRead              :asr.core/ArrayItem
             :asr.core/IntegerToInteger      :asr.core/DictPop
             :asr.core/DictLen               :asr.core/BitRShift
             :asr.core/ListLen               :asr.core/GtE
             :asr.core/ComplexToComplex      :asr.core/Binary
             :asr.core/SetConstant           :asr.core/Bind
             :asr.core/StringConstant        :asr.core/StringOrd
             :asr.core/ComplexToLogical      :asr.core/Nullify
             :asr.core/OverloadedCompare     :asr.core/Tuple
             :asr.core/RealBinOp             :asr.core/ListPop
             :asr.core/Function              :asr.core/CLoc
             :asr.core/StringCompare         :asr.core/CaseStmt
             :asr.core/RealToInteger         :asr.core/Or
             :asr.core/ListItem              :asr.core/Save
             :asr.core/AssociateBlockCall    :asr.core/TupleConstant
             :asr.core/Gt                    :asr.core/IntegerToReal
             :asr.core/Allocate              :asr.core/GenericProcedure
             :asr.core/Cycle                 :asr.core/ForAllSingle
             :asr.core/ClassProcedure        :asr.core/IntegerCompare
             :asr.core/ExternalSymbol        :asr.core/Interface
             :asr.core/Any                   :asr.core/Octal
             :asr.core/Assign                :asr.core/FileClose
             :asr.core/Logical               :asr.core/Derived
             :asr.core/StringConcat          :asr.core/ListRemove
             :asr.core/List                  :asr.core/Flush
             :asr.core/ListConcat            :asr.core/ClassType
             :asr.core/Variable              :asr.core/CharacterToList
             :asr.core/Assignment            :asr.core/ArrayBound
             :asr.core/LogicalBinOp          :asr.core/CustomOperator
             :asr.core/IntegerToLogical      :asr.core/Divisible
             :asr.core/LogicalConstant       :asr.core/GetPointer
             :asr.core/CharacterToInteger    :asr.core/BitXor
             :asr.core/Set                   :asr.core/StringChr
             :asr.core/ArrayReshape          :asr.core/EnumTypeConstructor
             :asr.core/BitCast               :asr.core/Cast
             :asr.core/SetPop                :asr.core/TupleItem
             :asr.core/NotEq                 :asr.core/If
             :asr.core/Pointer               :asr.core/GoToTarget
             :asr.core/Module                :asr.core/ArraySize
             :asr.core/TemplateToReal        :asr.core/EnumType
             :asr.core/Restriction}
           (set
            (filter
             not-asr-tuple
             (map :head big-list-of-stuff)))))))


(deftest install-all-symconst-specs-test
  (testing "install all 72 symconst specs"
    (is (= 72 (->> symconst-stuffs set count)))
    (is (= (set '(:asr.core/implementation        :asr.core/interface
                  :asr.core/l-bound               :asr.core/u-bound
                  :asr.core/default               :asr.core/save
                  :asr.core/parameter             :asr.core/allocatable
                  :asr.core/add                   :asr.core/sub
                  :asr.core/mul                   :asr.core/div
                  :asr.core/pow                   :asr.core/bit-and
                  :asr.core/bit-or                :asr.core/bit-xor
                  :asr.core/bit-l-shift           :asr.core/bit-r-shift
                  :asr.core/required              :asr.core/optional
                  :asr.core/binary                :asr.core/hex
                  :asr.core/octal                 :asr.core/and
                  :asr.core/or                    :asr.core/xor
                  :asr.core/n-eqv                 :asr.core/eqv
                  :asr.core/supports-zero         :asr.core/supports-plus
                  :asr.core/divisible             :asr.core/any
                  :asr.core/source                :asr.core/l-fortran-module
                  :asr.core/g-fortran-module      :asr.core/bind-c
                  :asr.core/interactive           :asr.core/intrinsic
                  :asr.core/real-to-integer       :asr.core/integer-to-real
                  :asr.core/logical-to-real       :asr.core/real-to-real
                  :asr.core/template-to-real      :asr.core/integer-to-integer
                  :asr.core/real-to-complex       :asr.core/integer-to-complex
                  :asr.core/integer-to-logical    :asr.core/real-to-logical
                  :asr.core/character-to-logical  :asr.core/character-to-integer
                  :asr.core/character-to-list     :asr.core/complex-to-logical
                  :asr.core/complex-to-complex    :asr.core/complex-to-real
                  :asr.core/logical-to-integer    :asr.core/real-to-character
                  :asr.core/integer-to-character  :asr.core/logical-to-character
                  :asr.core/local                 :asr.core/in
                  :asr.core/out                   :asr.core/in-out
                  :asr.core/return-var            :asr.core/unspecified
                  :asr.core/public                :asr.core/private
                  :asr.core/eq                    :asr.core/not-eq
                  :asr.core/lt                    :asr.core/lt-e
                  :asr.core/gt                    :asr.core/gt-e))
           (->> symconst-stuffs
                (map spec-from-symconst-stuff)
                (map eval)
                set)))))


(deftest all-heads-for-symbols-test
  (testing "all 13 heads for symbols"
    (is (= '#{Block               Function            GenericProcedure
              ExternalSymbol      CustomOperator      ClassProcedure
              ClassType           Module              EnumType
              DerivedType         AssociateBlock      Variable
              Program}
           (heads-for-composite :asr.core/symbol)))))


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
           (heads-for-composite :asr.core/stmt)))))


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
           (heads-for-composite :asr.core/expr)))))


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
                :asr.core/expr
                (map :ASDL-COMPOSITE)
                count)))))


;;                       _        _
;;  ____ __  ___ __   __| |_ __ _| |_ ___
;; (_-< '_ \/ -_) _| (_-<  _/ _` |  _(_-<
;; /__/ .__/\___\__| /__/\__\__,_|\__/__/
;;    |_|

(deftest count-asr-specs-test
  (is (= NSPECS (count-asr-core-specs))))


;;                               _     _
;;  ____  _ _ __  __ ___ _ _  __| |_  | |_ ___ _ _ _ __  ___
;; (_-< || | '  \/ _/ _ \ ' \(_-<  _| |  _/ -_) '_| '  \(_-<
;; /__/\_, |_|_|_\__\___/_||_/__/\__|  \__\___|_| |_|_|_/__/
;;     |__/


(deftest install-symconst-stuffss-by-term-test
  (testing "installing symconst stuffss by term"
    (is (= (set '(:asr.core/deftype     ; why is this indented?
                     :asr.core/arraybound
                     :asr.core/storage-type
                     :asr.core/binop
                     :asr.core/presence
                     :asr.core/integerboz
                     :asr.core/logicalbinop
                     :asr.core/trait
                     :asr.core/abi
                     :asr.core/cast-kind
                     :asr.core/intent
                     :asr.core/access
                     :asr.core/cmpop))
           (->> symconst-stuffss-by-term
                (map symconst-spec-for-term)
                (map eval)
                set)))))


;;  ___     _                    ___ _
;; |_ _|_ _| |_ ___ __ _ ___ _ _| _ |_)_ _  ___ _ __
;;  | || ' \  _/ -_) _` / -_) '_| _ \ | ' \/ _ \ '_ \
;; |___|_||_\__\___\__, \___|_| |___/_|_||_\___/ .__/
;;                 |___/                       |_|


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
             (s/conform :asr.core/expr test-vector)))

      (is (s/valid? :asr.core/integer-bin-op test-vector)))))


(let [integer-bin-op-stuff
      '({:head :asr.core/IntegerBinOp,
         :term :asr.core/expr,
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
             (filter #(= (:head %) :asr.core/IntegerBinOp)
                     big-list-of-stuff))))))


(deftest integer-ttype-semnasr-conformance
  (testing "Integer ttype conformance"
    (is (s/valid? :asr.core/integer-ttype-semnasr
                  '(Integer 4 [])                ))
    (is (s/valid? :asr.core/integer-ttype-semnasr
                  '(Integer 4 [1 2])             ))
    (is (s/valid? :asr.core/integer-ttype-semnasr
                  '(Integer 4 [] [1 2] [] [3 4]) ))
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


(deftest integer-exceptions
  (testing "small integer exceptions; Need different exceptions
  for the various types"
    (is "long overflow"
        (try (long (dec Long/MIN_VALUE))
             (catch ArithmeticException e
               (-> e ex-message))))
    (is "integer overflow"
        (try (int (dec Integer/MIN_VALUE))
             (catch ArithmeticException e
               (-> e ex-message))))
    (is "Value out of range for short: -32769"
        (try (short (dec Short/MIN_VALUE))
             (catch IllegalArgumentException e
               (->> e ex-message))))
    (is "Value of of range for byte: -129"
        (try (byte (dec Byte/MIN_VALUE))
             (catch IllegalArgumentException e
               (-> e ex-message))))
    (is "long overflow"
        (try (long (inc Long/MAX_VALUE))
             (catch ArithmeticException e
               (-> e ex-message))))
    (is "integer overflow"
        (try (int (inc Integer/MAX_VALUE))
             (catch ArithmeticException e
               (-> e ex-message))))
    (is "Value out of range for short: 32768"
        (try (short (inc Short/MAX_VALUE))
             (catch IllegalArgumentException e
               (->> e ex-message))))
    (is "Value of of range for byte: 128"
        (try (byte (inc Byte/MAX_VALUE))
             (catch IllegalArgumentException e
               (-> e ex-message))))
    (is (thrown?
         ArithmeticException
         (inc Long/MAX_VALUE)))))


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
