(ns asr.asr
  (:use [asr.utils]
        [asr.environment]
        [clojure.set])

  ;;; TODO: Consider https://github.com/rebcabin/odin

  (:require
   [blaster.clj-fstring :refer [f-str] ]
   [asr.grammar                        ]
   [asr.lpython                        ]
   [clojure.pprint      :refer [pprint]]
   [asr.parsed          :refer [
                                shallow-map-from-speclet,
                                hashmap-from-speclet,
                                map-pair-from-speclet-map,

                                kind-from-form,
                                head-from-kind-form,
                                stuff-from-term-form,
                               ]]
   [clojure.walk        :as     walk   ]
   [clojure.zip         :as     zip    ]
   [clojure.spec.alpha  :as s]))


;;  _     _____     _______      _    ____  ____
;; | |   |_ _\ \   / / ____|    / \  / ___||  _ \
;; | |    | | \ \ / /|  _|     / _ \ \___ \| |_) |
;; | |___ | |  \ V / | |___   / ___ \ ___) |  _ <
;; |_____|___|  \_/  |_____| /_/   \_\____/|_| \_\


;;; See grammar.clj for deeper documentation on terminology, here.


;;                  _     _
;;  ____ __  ___ __| |___| |_ ___
;; (_-< '_ \/ -_) _| / -_)  _(_-<
;; /__/ .__/\___\__|_\___|\__/__/
;;    |_|


;;; Parse the current, live ASR.ASDL, not the snapshot hard-coded
;;; in "asr_snapshot.clj".


(def asr-asdl-hiccup
  (asr.grammar/asdl-parser asr.lpython/asr-asdl))


;;; Some of the following gadgets collide with "parsed.clj," which
;;; works on the snapshot data in "asr_snapshot.clj". Use explicit
;;; namespaces, e.g. asr.parsed/speclets:


(def speclets
  (vec (rest
        ((-> (zip/vector-zip asr-asdl-hiccup)
             zip/down zip/right zip/right) 0))))


;;; The snapshot has fewer speclets:


#_(count asr.parsed/speclets)
;; => 28

#_(count speclets)
;; => 30


;;; But the CODE in asr.parsed works on both the snapshot and on
;;; the live ASR:


(def big-map-of-speclets-from-terms
  (apply hash-map
         (mapcat identity ;; flatten one level
                 (map
                  (comp map-pair-from-speclet-map
                        hashmap-from-speclet)
                  speclets))))


;;; Inspect the count of speclets by pretty-printing to a
;;; comment (C-c C-f C-v C-c e):


#_(count big-map-of-speclets-from-terms)
;; => 30


;;    _   ___ ___
;;   /_\ / __| _ \  __ _ _ _ ___ _  _ _ __ ___
;;  / _ \\__ \   / / _` | '_/ _ \ || | '_ (_-<
;; /_/ \_\___/_|_\ \__, |_| \___/\_,_| .__/__/
;;                 |___/             |_|


;; There are three groups of size approximately 14, 6, 10; the
;; first group of 14 or so contains SYMCONSTs in subgroups by
;; term, e.g.,

;; {:group asr-enum,
;;  :nym :asr.autospecs/cmpop, :vals (Eq NotEq Lt LtE Gt GtE)}.

;; The second group of 6 or so contains tuples, e.g.,

;; {:group asr-tuple,
;;  :nym :asr.autospecs/array_index,
;;  :head asr-tuple14316,
;;  :parmtypes (expr expr expr),
;;  :parmnyms (left right step),
;;  :parmmults
;;  (:asr.parsed/at-most-once
;;   :asr.parsed/at-most-once
;;   :asr.parsed/at-most-once)}

;; The third group of 10 or so composite terms like expr and stmt.
;; An example, "symbol" has this structure (abbreviated to a
;; page's length):

;; {:group asr-composite,
;;  :nym :asr.autospecs/symbol,
;;  :heads
;;  (Program   Module  Function
;;   ...
;;   AssociateBlock  Block),
;;  :params-types
;;  ((symbol_table identifier identifier stmt)
;;   (symbol_table identifier identifier bool bool)
;;   (symbol_table    identifier      identifier      expr
;;    stmt            expr            abi             access
;;    deftype         string          bool            bool
;;    bool            bool            bool            ttype
;;    symbol          bool            bool            bool)
;;   ...
;;   (symbol_table identifier stmt)
;;   (symbol_table identifier stmt)),
;;  :params-nyms
;;  ((symtab name dependencies body)
;;   (symtab name dependencies loaded_from_mod intrinsic)
;;   (symtab          name            dependencies    args
;;    body            return_var      abi             access
;;    deftype         bindc_name      elemental       pure
;;    module          inline          static          type_params
;;    restrictions    is_restriction  deterministic   side_effect_free)
;;   ...
;;   (symtab name body)
;;   (symtab name body)),
;;  :params-mults
;;  ((:asr.parsed/once                :asr.parsed/once
;;    :asr.parsed/zero-or-more        :asr.parsed/zero-or-more)
;;   (:asr.parsed/once                :asr.parsed/once
;;    :asr.parsed/zero-or-more        :asr.parsed/once
;;    :asr.parsed/once)
;;   (:asr.parsed/once                :asr.parsed/once
;;    :asr.parsed/zero-or-more        :asr.parsed/zero-or-more
;;    :asr.parsed/zero-or-more        :asr.parsed/at-most-once
;;    :asr.parsed/once                :asr.parsed/once
;;    :asr.parsed/once                :asr.parsed/at-most-once
;;    :asr.parsed/once                :asr.parsed/once
;;    :asr.parsed/once                :asr.parsed/once
;;    :asr.parsed/once                :asr.parsed/zero-or-more
;;    :asr.parsed/zero-or-more        :asr.parsed/once
;;    :asr.parsed/once                :asr.parsed/once)
;;   ...
;;   (:asr.parsed/once :asr.parsed/once :asr.parsed/zero-or-more)
;;   (:asr.parsed/once :asr.parsed/once :asr.parsed/zero-or-more))}


(def asr-groups
  "Exploit the fact that all forms in a speclet (term + alternative
  forms) have the same group key, :ASDL-SYMCONST, :ASDL-TUPLE,
  or :ASDL-COMPOSITE. See grammar.clj."
  (group-by
   (fn [speclet]   ; e.g.,
     (->> speclet  ; [:asr.autospecs/abi ({:ASDL-SYMCONST "Source"} ...)]
          second   ; ({:ASDL-SYMCONST "Source"} ...)
          first    ; {:ASDL-SYMCONST "Source"}
          keys     ; (:ASDL-SYMCONST)
          first)   ; :ASDL-SYMCONST
     )
   big-map-of-speclets-from-terms))


;;; There are three groups of
;;; speclets: :ASDL-SYMCONST, :ASDL-TUPLE, and :ASDL-COMPOSITE.
;;;
;;; :ASDL-SYMCONSTs are heads without parameter lists.
;;;
;;; :ASDL-TUPLE s are parameter-lists with gensymmed heads.
;;;
;;; :ASDL-COMPOSITE s have symbolic _heads_ and parameter-lists in
;;; round brackets.


;;; The group-by above produces a list with three elements:


#_(->> asr-groups count)
;; => 3


;;; The elements are the group keys:


#_(->> asr-groups (map first))
;; => (:ASDL-SYMCONST :ASDL-TUPLE :ASDL-COMPOSITE)


;;; some support routines, same for all the groups


(defn check-first
  "Check that the first element of sigil is the given keyword."
  [keyword sigil]
  (assert (= keyword (first sigil)))
  sigil)


(defn check-firsts
  [keyword sigils]
  (map #(check-first keyword %) sigils))


(defn check-count
  [count- sigil]
  (assert (= count- (count sigil)))
  sigil)


(defn check-counts
  [count- sigils]
  (map #(check-count count- %) sigils))


;;; For readability, strip the asr.autospecs namespace; convert to
;;; string (via "name") and then to symbol:


(defn symbolize-terms
  [group]
  (->> group
       ;; the actual terms -- left-hand sides of productions
       (map first)
       ;; Convert to string without the namespace (always asr.autospecs).
       ;; TODO: check that!
       (map name)
       ;; Convert to symbol to rid the double quotes.
       (map symbol)))


;;   _______                                    __
;;  <  / / /  ___ __ ____ _  _______  ___  ___ / /____
;;  / /_  _/ (_-</ // /  ' \/ __/ _ \/ _ \(_-</ __(_-<
;; /_/ /_/  /___/\_, /_/_/_/\__/\___/_//_/___/\__/___/
;;              /___/


;;; Fourteen symconsts:


;;; Get all the symconsts and check their count:


(defn get-symconsts
  "Symconsts are the first group of productions in ASR."
  []
  (->> asr-groups
       ;; The first group is the group of symconsts:
       first
       ;; The first of the group must be ASR-SYMCONST:
       (check-first :ASDL-SYMCONST)
       ;; the actual term keyword in namespace asr.autospecs
       second))


#_(->> (get-symconsts) count)
;; => 14


;;; List out the terms and forms alternation -- left-hand and
;;; right-hand sides -- of the fourteen symconst productions. Here
;;; are the terms (left-hand sides) of all ASR-SYMCONSTs:


#_(->> (get-symconsts) symbolize-terms)
;;  abi                 cmpop               access
;;  storage_type        intent              enumtype
;;  deftype             arraybound          logicalbinop
;;  cast_kind           arraystorage        integerboz
;;  presence            binop


;;; Go over the development of the extraction of heads so as to
;;; identify an abstraction for the data structure.


;;; Here are the forms of the first symconst, as an example:


#_(->> (get-symconsts) (map second) first)
;; => ({:ASDL-SYMCONST "Source"}
;;     {:ASDL-SYMCONST "LFortranModule"}
;;     {:ASDL-SYMCONST "GFortranModule"}
;;     {:ASDL-SYMCONST "BindC"}
;;     {:ASDL-SYMCONST "Interactive"}
;;     {:ASDL-SYMCONST "Intrinsic"})


;;; See that each form is a singleton with identical group-key.
;;; Check that for the example:


#_(let [forms         (map second (get-symconsts))
      a-form        (first forms)
      count-checked ((partial check-counts 1) a-form)
      key-checked   ((partial check-firsts :ASDL-SYMCONST)
                     (map first count-checked))
      ]
  key-checked
  )
;; => ([:ASDL-SYMCONST "Source"]
;;     [:ASDL-SYMCONST "LFortranModule"]
;;     [:ASDL-SYMCONST "GFortranModule"]
;;     [:ASDL-SYMCONST "BindC"]
;;     [:ASDL-SYMCONST "Interactive"]
;;     [:ASDL-SYMCONST "Intrinsic"])


;;; Note it's turned into a vector of pairs. Ditch the results and
;;; fetch the vals and symbolize them to rid the double quotes:


#_(let [forms         (map second (get-symconsts))
      a-form        (first forms)
      _             ((partial check-counts 1) a-form)
      _             ((partial check-firsts :ASDL-SYMCONST)
                     (map first a-form))
      vals-         (map :ASDL-SYMCONST a-form)
      syms-         (map symbol vals-)]
  syms-)
;; => (Source LFortranModule GFortranModule BindC Interactive Intrinsic)


;;; Now do likewise for every ASDL-SYMCONST in the groups with one
;;; extra level of mapping (use the debugger to see the
;;; intermediate values):


(defn symbolize-symconst-heads
  "The ->> macro does not debug cleanly on this construction, so we
  back off to a 'let.'"
  [group]
  (let [forms  (map second group)
        _      (map (partial check-counts 1) forms)
        _      (map (partial check-firsts :ASDL-SYMCONST)
                    (map first forms))
        vals-  (map (partial map :ASDL-SYMCONST) forms)
        syms-  (map (partial map symbol) vals-)]
    syms-))


#_(->> (get-symconsts)
     (symbolize-symconst-heads))
;; => ((Source          LFortranModule      GFortranModule
;;      BindC           Interactive         Intrinsic)
;;     (Eq NotEq Lt LtE Gt GtE)
;;     (Public Private)
;;     (Default Save Parameter Allocatable)
;;     (Local In Out InOut ReturnVar Unspecified)
;;     (IntegerConsecutiveFromZero   IntegerUnique
;;      IntegerNotUnique             NonInteger)
;;     (Implementation Interface)
;;     (LBound UBound)
;;     (And Or Xor NEqv Eqv)
;;     (RealToInteger           IntegerToReal         LogicalToReal
;;      RealToReal              IntegerToInteger      RealToComplex
;;      IntegerToComplex        IntegerToLogical      RealToLogical
;;      CharacterToLogical      CharacterToInteger    CharacterToList
;;      ComplexToLogical        ComplexToComplex      ComplexToReal
;;      ComplexToInteger        LogicalToInteger      RealToCharacter
;;      IntegerToCharacter      LogicalToCharacter)
;;     (RowMajor ColMajor)
;;     (Binary Hex Octal)
;;     (Required Optional)
;;     (Add Sub Mul Div Pow BitAnd BitOr BitXor BitLShift BitRShift))


;;; Count them all


#_(->> (get-symconsts)
     (symbolize-symconst-heads)
     (mapcat identity)
     count)
;; => 74


;;   ____   __            __
;;  / __/  / /___ _____  / /__ ___
;; / _ \  / __/ // / _ \/ / -_|_-<
;; \___/  \__/\_,_/ .__/_/\__/___/
;;               /_/


;;; six tuples


(defn get-tuples
  "Tuples are the second group of productions in ASR."
  []
  (->> asr-groups
       ;; The second group is the group of symconsts:
       second
       ;; The first of the group must be ASR-SYMCONST:
       (check-first :ASDL-TUPLE)
       ;; the actual term keyword in namespace asr.autospecs
       second))


#_(->> (get-tuples) count)
;; => 6


;; Here are the terms (left-hand sides) of all ASR-TUPLES.


#_(->> (get-tuples) symbolize-terms)
;;  call_arg            do_loop_head        alloc_arg
;;  attribute_arg       array_index         dimension


;;; Go over the development of the extraction of heads so as to
;;  identify an abstraction for the data structure.


;;; Here are the forms of the first tuple, as an example:


#_(let [forms  (map second (get-tuples))
      a-form (first forms)]
  a-form)
;; => ({:ASDL-TUPLE "asr-tuple12765",
;;      :ASDL-ARGS
;;      ({:ASDL-TYPE "expr",
;;        :MULTIPLICITY :asr.parsed/at-most-once,
;;        :ASDL-NYM "value"})})


;;; See that it's a singleton of a doubleton; check this; also
;;; check that the keyword is :ASDL-TUPLE:


#_(let [forms  (map second (get-tuples))
      a-form (first forms)
      _      ((partial check-count 1) a-form)
      _      ((partial check-counts 2) a-form)
      _      ((partial check-first :ASDL-TUPLE)
              (->> a-form first first))]
  a-form)
;; => ({:ASDL-TUPLE "asr-tuple12765",
;;      :ASDL-ARGS
;;      ({:ASDL-TYPE "expr",
;;        :MULTIPLICITY :asr.parsed/at-most-once,
;;        :ASDL-NYM "value"})})


;;; Now get the vals, check singleton, flatten and symbolize:


#_(let [forms  (map second (get-tuples))
      a-form (first forms)
      _      ((partial check-count 1) a-form)
      _      ((partial check-counts 2) a-form)
      _      ((partial check-first :ASDL-TUPLE)
              (->> a-form first first))
      vals-  (map :ASDL-TUPLE a-form)
      flat1  (first vals-)
      sym    (symbol flat1)]
  sym)
;; => asr-tuple12765

;;; Note that the "head" is a gensymmed (made-up) symbol. See
;;; grammar.clj.


;;; As before with symconsts, add one level of "map" to get all
;;; the heads. Note "check-counts 1" instead of "check-count 1" to
;;; effect the extra level of mapping. Unlike symconsts and
;;; composites, there is only one level of list for all the tuple
;;; heads.


(defn symbolize-tuple-heads
  [group]
  (let [forms  (map second group)
        _      ((partial check-counts 1) forms)
        _      (map (partial check-counts 2) forms)
        _      (map (partial check-first :ASDL-TUPLE)
                    (->> forms (map first) (map first)))
        vals-  (map (partial map :ASDL-TUPLE) forms)
        flats1 (map first vals-)
        syms-  (map symbol flats1)]
    syms-))


#_(->> (get-tuples) (symbolize-tuple-heads))
;; => (asr-tuple12765
;;     asr-tuple12766
;;     asr-tuple12767
;;     asr-tuple12768
;;     asr-tuple12769
;;     asr-tuple12770)


;;   ______                                 _ __
;;  <  / _ \  _______  __ _  ___  ___  ___ (_) /____ ___
;;  / / // / / __/ _ \/  ' \/ _ \/ _ \(_-</ / __/ -_|_-<
;; /_/\___/  \__/\___/_/_/_/ .__/\___/___/_/\__/\__/___/
;;                        /_/


;;; Ten composites:


;;; Third doesn't work on maps, though First and Second do.


(defn flip [f] (fn [x y] (f y x)))


(def  third  (partial (flip nth) 2))


;;; Convert asr-groups to a vec so "third" will work on it:


(defn get-composites
  "Composites are the third group of productions in ASR."
  []
  (->> asr-groups
       vec
       third
       (check-first :ASDL-COMPOSITE)
       second))


#_(->> (get-composites) count)
;; => 10


;; Here are the terms (left-hand sides) of all ASR-COMPOSITE s.


#_(->> (get-composites) symbolize-terms)
;;  restriction_arg     type_stmt           case_stmt
;;  symbol              attribute           ttype
;;  stmt                tbind               expr
;;  unit


;;; For a little variation, let's analyze the second
;;; composite (this should always work because there should always
;;; be more than one composite). Note that asserts are not always
;;; thrown, here, but you can catch them by stepping with the
;;; debugger (TODO: reason unknown).


;;; Though the structure of a composite is more rich than the
;;; structures of the other groups (symconsts and tuples),
;;; extracting the heads is easier. Here is how to get the heads
;;; of one composite:


#_(let [forms  (map second (get-composites))
      a-form (nth forms 2)
      _      (map (partial check-firsts :ASDL-COMPOSITE)
                  a-form)
      vals-  (map :ASDL-COMPOSITE a-form)
      heads- (map :ASDL-HEAD vals-)
      syms-  (map symbol heads-)
      ]
  syms-)
;; => (CaseStmt CaseStmt_Range)


;;; As before insert one more level of mapping to get them all:


(defn symbolize-composite-heads
  [_]
  (let [forms   (map second (get-composites))
        valss-  (map #(map :ASDL-COMPOSITE %) forms)
        headss- (map #(map :ASDL-HEAD %) valss-)
        symss-  (map #(map symbol %) headss-)]
    symss-))


#_(symbolize-composite-heads (get-composites))
;; => ((RestrictionArg)
;;     (TypeStmt)
;;     (CaseStmt CaseStmt_Range)
;;     (Program             Module              Function
;;      GenericProcedure    CustomOperator      ExternalSymbol
;;      StructType          EnumType            UnionType
;;      Variable            ClassType           ClassProcedure
;;      AssociateBlock      Block)
;;     (Attribute)
;;     (Integer             Real                Complex
;;      Character           Logical             Set
;;      List                Tuple               Struct
;;      Enum                Union               Class
;;      Dict                Pointer             Const
;;      CPtr                TypeParameter)
;;     (Allocate            Assign              Assignment
;;      Associate           Cycle               ExplicitDeallocate
;;      ImplicitDeallocate  DoConcurrentLoop    DoLoop
;;      ErrorStop           Exit                ForAllSingle
;;      GoTo                GoToTarget          If
;;      IfArithmetic        Print               FileOpen
;;      FileClose           FileRead            FileBackspace
;;      FileRewind          FileInquire         FileWrite
;;      Return              Select              Stop
;;      Assert              SubroutineCall      Where
;;      WhileLoop           Nullify             Flush
;;      ListAppend          AssociateBlockCall  SelectType
;;      CPtrToPointer       BlockCall           SetInsert
;;      SetRemove           ListInsert          ListRemove
;;      ListClear           DictInsert)
;;     (Bind)
;;     (IfExp               ComplexConstructor  NamedExpr
;;      FunctionCall        StructTypeConstructor
;;      EnumTypeConstructor UnionTypeConstructor
;;      ImpliedDoLoop       IntegerConstant     IntegerBOZ
;;      IntegerBitNot       IntegerUnaryMinus   IntegerCompare
;;      IntegerBinOp        RealConstant        RealUnaryMinus
;;      RealCompare         RealBinOp           ComplexConstant
;;      ComplexUnaryMinus   ComplexCompare      ComplexBinOp
;;      LogicalConstant     LogicalNot          LogicalCompare
;;      LogicalBinOp        TemplateBinOp       ListConstant
;;      ListLen             ListConcat          ListCompare
;;      SetConstant         SetLen              TupleConstant
;;      TupleLen            TupleCompare        StringConstant
;;      StringConcat        StringRepeat        StringLen
;;      StringItem          StringSection       StringCompare
;;      StringOrd           StringChr           DictConstant
;;      DictLen             Var                 ArrayConstant
;;      ArrayItem           ArraySection        ArraySize
;;      ArrayBound          ArrayTranspose      ArrayMatMul
;;      ArrayPack           ArrayReshape        ArrayMaxloc
;;      BitCast             StructInstanceMember
;;      StructStaticMember  EnumMember          UnionRef
;;      EnumName            EnumValue           OverloadedCompare
;;      OverloadedBinOp     Cast                ComplexRe
;;      ComplexIm           DictItem            CLoc
;;      PointerToCPtr;      GetPointer          ListItem
;;      TupleItem           ListSection         ListPop
;;      DictPop             SetPop              IntegerBitLen
;;      Ichar               Iachar              SizeOfType
;;      PointerNullConstant PointerAssociated)
;;     (TranslationUnit))


;;; It's interesting to count them all:


#_(let [ccs (get-composites)
      terms (->> ccs symbolize-terms)
      head-counts
      (->> (symbolize-composite-heads ccs)
           (map count))]
  (interleave terms head-counts))
;; => (restriction_arg  1
;;     type_stmt        1
;;     case_stmt        2
;;     symbol          14
;;     attribute        1
;;     ttype           17
;;     stmt            44
;;     tbind            1
;;     expr            86
;;     unit             1)


;;         _                 _
;;  __ ___| |_  _ _ __  _ _ (_)______
;; / _/ _ \ | || | '  \| ' \| |_ / -_)
;; \__\___/_|\_,_|_|_|_|_||_|_/__\___|


;;; To make it easier to fetch data, columnize these terms:


(defn columnize-symconst [term]
  (let [nym (->> term first)
        enumdicts (->> term second)
        enumvals  (->> enumdicts (map vals)
                       (mapcat identity) (map symbol))]
    (assert (every? #(= 1 (count %)) enumdicts))
    (assert (every? #(= :ASDL-SYMCONST (-> % keys first)) enumdicts))
    {:group 'asr-enum, :nym nym, :vals enumvals}))


#_(->> big-map-of-speclets-from-terms
     first)
;; => [:asr.autospecs/abi
;;     ({:ASDL-SYMCONST "Source"}
;;      {:ASDL-SYMCONST "LFortranModule"}
;;      {:ASDL-SYMCONST "GFortranModule"}
;;      {:ASDL-SYMCONST "BindC"}
;;      {:ASDL-SYMCONST "Interactive"}
;;      {:ASDL-SYMCONST "Intrinsic"})]


#_(->> big-map-of-speclets-from-terms
     first
     columnize-symconst)
;; => {:group asr-enum,
;;     :nym :asr.autospecs/abi,
;;     :vals
;;     (Source      LFortranModule      GFortranModule      BindC
;;      Interactive Intrinsic)}


(defn columnize-tuple [term]
  (let [nym (->> term first)
        stuff (->> term second first)
        head (->> stuff :ASDL-TUPLE symbol)
        params (->> stuff :ASDL-ARGS)
        parmtypes (->> params (map :ASDL-TYPE) (map symbol))
        parmnyms (->> params (map :ASDL-NYM) (map symbol))
        parmmults (->> params (map :MULTIPLICITY))]
    (assert (= 1 (count (->> term second))))
    {:group 'asr-tuple, :nym nym, :head head, :parmtypes parmtypes,
     :parmnyms parmnyms, :parmmults parmmults}))


(defn columnize-composite [term]
  (let [nym (->> term first)
        stuff (->> term second)
        compos (->> stuff (map :ASDL-COMPOSITE))
        heads (->> compos (map :ASDL-HEAD) (map symbol))
        paramss (->> compos (map :ASDL-ARGS))
        params-nyms (->> paramss
                         (map #(map :ASDL-NYM %))
                         (map #(map symbol %)))
        params-types (->> paramss
                          (map #(map :ASDL-TYPE %))
                          (map #(map symbol %)))
        params-mults (->> paramss
                          (map #(map :MULTIPLICITY %)))]
    {:group 'asr-composite, :nym nym, :heads heads,
     :params-types params-types
     :params-nyms params-nyms
     :params-mults params-mults}))


(defn columnize-term
  [term]
  (case (-> term second first keys first)
    :ASDL-SYMCONST  (columnize-symconst term)
    :ASDL-TUPLE     (columnize-tuple term)
    :ASDL-COMPOSITE (columnize-composite term)))


;;; Here are all 30 terms:


#_(->> big-map-of-speclets-from-terms
     (map first))
;; 01  abi             call_arg        do_loop_head    restriction_arg
;; 02  cmpop           type_stmt       access          storage_type
;; 03  intent          case_stmt       enumtype        alloc_arg
;; 04  symbol          deftype         arraybound      attribute
;; 05  logicalbinop    ttype           cast_kind       stmt
;; 06  arraystorage    integerboz      tbind           presence
;; 07  expr            unit            binop           attribute_arg
;; 08  array_index     dimension


;;; For the 14 symconsts, fetch the heads out of the columnized
;;; data (the ___columns___):


#_(defn fetch-pair [key map]
  [key (key map)])


#_(->> big-map-of-speclets-from-terms
     (fetch-pair :asr.autospecs/symbol)
     columnize-term
     :heads
     count)
;; => 14
;;  Program             Module              Function
;;  GenericProcedure    CustomOperator      ExternalSymbol
;;  StructType          EnumType            UnionType
;;  Variable            ClassType           ClassProcedure
;;  AssociateBlock      Block


#_(->> big-map-of-speclets-from-terms
     (fetch-pair :asr.autospecs/expr)
     columnize-term
     :heads
     count)
;; => 86
;; 01 IfExp                   ComplexConstructor      NamedExpr
;; 02 FunctionCall            StructTypeConstructor   EnumTypeConstructor
;; 03 UnionTypeConstructor    ImpliedDoLoop           IntegerConstant
;; 04 IntegerBOZ              IntegerBitNot           IntegerUnaryMinus
;; 05 IntegerCompare          IntegerBinOp            RealConstant
;; 06 RealUnaryMinus          RealCompare             RealBinOp
;; 07 ComplexConstant         ComplexUnaryMinus       ComplexCompare
;; 08 ComplexBinOp            LogicalConstant         LogicalNot
;; 09 LogicalCompare          LogicalBinOp            TemplateBinOp
;; 10 ListConstant            ListLen                 ListConcat
;; 11 ListCompare             SetConstant             SetLen
;; 12 TupleConstant           TupleLen                TupleCompare
;; 13 StringConstant          StringConcat            StringRepeat
;; 14 StringLen               StringItem              StringSection
;; 15 StringCompare           StringOrd               StringChr
;; 16 DictConstant            DictLen                 Var
;; 17 ArrayConstant           ArrayItem               ArraySection
;; 18 ArraySize               ArrayBound              ArrayTranspose
;; 19 ArrayMatMul             ArrayPack               ArrayReshape
;; 20 ArrayMaxloc             BitCast                 StructInstanceMember
;; 21 StructStaticMember      EnumMember              UnionRef
;; 22 EnumName                EnumValue               OverloadedCompare
;; 23 OverloadedBinOp         Cast                    ComplexRe
;; 24 ComplexIm               DictItem                CLoc
;; 25 PointerToCPtr           GetPointer              ListItem
;; 26 TupleItem               ListSection             ListPop
;; 27 DictPop                 SetPop                  IntegerBitLen
;; 28 Ichar                   Iachar                  SizeOfType
;; 29 PointerNullConstant     PointerAssociated)


#_(->> big-map-of-speclets-from-terms
     (fetch-pair :asr.autospecs/stmt)
     columnize-term
     :heads
     count)
;; => 44
;; 01 Allocate            Assign              Assignment          Associate
;; 02 Cycle               ExplicitDeallocate  ImplicitDeallocate  DoConcurrentLoop
;; 03 DoLoop              ErrorStop           Exit                ForAllSingle
;; 04 GoTo                GoToTarget          If                  IfArithmetic
;; 05 Print               FileOpen            FileClose           FileRead
;; 06 FileBackspace       FileRewind          FileInquire         FileWrite
;; 07 Return              Select              Stop                Assert
;; 08 SubroutineCall      Where               WhileLoop           Nullify
;; 09 Flush               ListAppend          AssociateBlockCall  SelectType
;; 10 CPtrToPointer       BlockCall           SetInsert           SetRemove
;; 11 ListInsert          ListRemove          ListClear           DictInsert


#_(->> big-map-of-speclets-from-terms
     (fetch-pair :asr.autospecs/call_arg)
     columnize-term)


;;  _    _        _ _    _          __      _         __  __
;; | |__(_)__ _  | (_)__| |_   ___ / _|  __| |_ _  _ / _|/ _|
;; | '_ \ / _` | | | (_-<  _| / _ \  _| (_-<  _| || |  _|  _|
;; |_.__/_\__, | |_|_/__/\__| \___/_|   /__/\__|\_,_|_| |_|
;;        |___/


(def big-list-of-stuff
  (mapcat
   identity                             ; Flatten once.
   (map (fn [speclet]
          (let [[term forms] speclet]
            (map
             (partial
              stuff-from-term-form term)
             forms)))
        big-map-of-speclets-from-terms)))


(def lookup-stuff-by-head
  (into {} (map (fn [stuff]
                  [(symbol (name (:head stuff)))
                   stuff])
                big-list-of-stuff)))


('TranslationUnit lookup-stuff-by-head)
;; => {:head :asr.autospecs/TranslationUnit,
;;     :term :asr.autospecs/unit,
;;     :grup :ASDL-COMPOSITE,
;;     :form
;;     {:ASDL-COMPOSITE
;;      {:ASDL-HEAD "TranslationUnit",
;;       :ASDL-ARGS
;;       ({:ASDL-TYPE "symbol_table",
;;         :MULTIPLICITY :asr.parsed/once,
;;         :ASDL-NYM "global_scope"}
;;        {:ASDL-TYPE "node",
;;         :MULTIPLICITY :asr.parsed/zero-or-more,
;;         :ASDL-NYM "items"})}}}


;;; Spot-check with CIDER C-c C-e in buffer:


#_(count big-list-of-stuff)
;; => 248


#_(first big-list-of-stuff)
;; => {:head :asr.autospecs/Source,
;;     :term :asr.autospecs/abi,
;;     :grup :ASDL-SYMCONST,
;;     :form {:ASDL-SYMCONST "Source"}}


;;; Compare against the testing snapshot:


#_(count asr.parsed/big-list-of-stuff)
;; => 227


#_(first asr.parsed/big-list-of-stuff)
;; => {:head :asr.autospecs/Source,
;;     :term :asr.autospecs/abi,
;;     :grup :ASDL-SYMCONST,
;;     :form {:ASDL-SYMCONST "Source"}}


(def tuple-stuffs
  (filter #(= (:grup %) :ASDL-TUPLE) big-list-of-stuff))


#_tuple-stuffs
;; => ({:head :asr.autospecs/asr-tuple10800,
;;      :term :asr.autospecs/call_arg,
;;      :form ...
;;     {:head :asr.autospecs/asr-tuple10801,
;;      :term :asr.autospecs/do_loop_head,
;;      :form ...
;;     {:head :asr.autospecs/asr-tuple10802,
;;      :term :asr.autospecs/alloc_arg,
;;      :form ...
;;     {:head :asr.autospecs/asr-tuple10803,
;;      :term :asr.autospecs/attribute_arg,
;;      :form ...
;;     {:head :asr.autospecs/asr-tuple10804,
;;      :term :asr.autospecs/array_index,
;;      :form ...
;;     {:head :asr.autospecs/asr-tuple10805,
;;      :term :asr.autospecs/dimension,
;;      :form ...


(def symconst-stuffs
  (filter #(= (:grup %) :ASDL-SYMCONST) big-list-of-stuff))


;;; Spot-check with CIDER C-c C-e in buffer.


;; (count symconst-stuffs)
;; => 74


;; (first symconst-stuffs)
;; => {:head :asr.autospecs/Source,
;;     :term :asr.autospecs/abi,
;;     :grup :ASDL-SYMCONST,
;;     :form {:ASDL-SYMCONST "Source"}}


;; (count asr.parsed/symconst-stuffs)
;; => 72


;; (first asr.parsed/symconst-stuffs)
;; => {:head :asr.autospecs/Source,
;;     :term :asr.autospecs/abi,
;;     :grup :ASDL-SYMCONST,
;;     :form {:ASDL-SYMCONST "Source"}}


(def composite-stuffs
  (filter #(= (:grup %) :ASDL-COMPOSITE) big-list-of-stuff))

;;; Spot-check with CIDER C-c C-e in buffer


;;; These are promoted to ../../tests/asr/core_test.clj
#_(count composite-stuffs)
;; => 168
#_(let [ccs (get-composites)
      head-counts
      (->> ccs (symbolize-composite-heads)
           (map count))]
  (apply + head-counts))
;; => 168


#_(first composite-stuffs)
;; => {:head :asr.autospecs/RestrictionArg,
;;     :term :asr.autospecs/restriction_arg,
;;     :grup :ASDL-COMPOSITE,
;;     :form
;;     {:ASDL-COMPOSITE
;;      {:ASDL-HEAD "RestrictionArg",
;;       :ASDL-ARGS
;;       ({:ASDL-TYPE "identifier",
;;         :MULTIPLICITY :asr.parsed/once,
;;         :ASDL-NYM "restriction_name"}
;;        {:ASDL-TYPE "symbol",
;;         :MULTIPLICITY :asr.parsed/once,
;;         :ASDL-NYM "restriction_func"})}}}


#_(count asr.parsed/composite-stuffs)
;; => 149


#_(first asr.parsed/composite-stuffs)
;; => {:head :asr.autospecs/CaseStmt,
;;     :term :asr.autospecs/case_stmt,
;;     :grup :ASDL-COMPOSITE,
;;     :form
;;     {:ASDL-COMPOSITE
;;      {:ASDL-HEAD "CaseStmt",
;;       :ASDL-ARGS
;;       ({:ASDL-TYPE "expr",
;;         :MULTIPLICITY :asr.parsed/zero-or-more,
;;         :ASDL-NYM "test"}
;;        {:ASDL-TYPE "stmt",
;;         :MULTIPLICITY :asr.parsed/zero-or-more,
;;         :ASDL-NYM "body"})}}}


;; (def raw-composite-heads
;;   (->> composite-stuffs
;;        (map :head)
;;        (map name)  ; strip namespace
;;        set
;;        ))
;; ;; (count raw-composite-heads)


;; (def raw-snapshot-composite-heads
;;   (->> asr.parsed/composite-stuffs
;;        (map :head)
;;        (map name)
;;        set
;;        ))
;; ;; (count raw-snapshot-composite-heads)


;; (defn get-names [specs]
;;   (->> specs
;;        asr.autospecs/heads-for-composite
;;        (map name)
;;        set
;;        ))


;;; These are the left-hand sides (terms) of all speclets in a
;;; current version of ASR.asdl, NOT the snapshot

;;; 001 unit
;;; 002 symbol
;;; 003 storage_type
;;; 004 access
;;; 005 intent
;;; 006 deftype
;;; 007 presence
;;; 008 abi
;;; 009 stmt
;;; 010 expr
;;; 011 ttype
;;; 012 restriction_arg
;;; 013 binop
;;; 014 logicalbinop
;;; 015 cmpop
;;; 016 integerboz
;;; 017 arraybound
;;; 018 arryastorage
;;; 019 cast_kind
;;; 020 dimension
;;; 021 alloc_arg
;;; 022 attribute
;;; 023 attribute_arg
;;; 024 call_arg
;;; 025 tbind
;;; 026 array_index
;;; 027 do_loop_head
;;; 028 case_stmt
;;; 029 type_stmt
;;; 030 enumtype

;;; Let's see what the reader finds; it should find 30 "terms."
;;; The count of speclets equals the number of terms.

;; (count speclets)

;;; The snapshot has 28 terms. This number does not change as
;;; ASR.asdl is updated.

;; (count asr.parsed/speclets)

;; ;;; The following un-commented sets have autospecs:

;; (get-names :asr.autospecs/unit)
;; (get-names :asr.autospecs/symbol)
;; #_(get-names :asr.autospecs/storage_type)
;; #_(get-names :asr.autospecs/access)
;; #_(get-names :asr.autospecs/intent)
;; #_(get-names :asr.autospecs/deftype)
;; #_(get-names :asr.autospecs/presence)
;; #_(get-names :asr.autospecs/abi)
;; (get-names :asr.autospecs/stmt)
;; (get-names :asr.autospecs/expr)
;; (get-names :asr.autospecs/ttype)
;; (get-names :asr.autospecs/restriction_arg)
;; #_(get-names :asr.autospecs/binop)
;; #_(get-names :asr.autospecs/logicalbinop)
;; #_(get-names :asr.autospecs/cmpop)
;; #_(get-names :asr.autospecs/integerboz)
;; #_(get-names :asr.autospecs/arraybound)
;; (get-names :asr.autospecs/arryastorage)
;; #_(get-names :asr.autospecs/cast_kind)
;; #_(get-names :asr.autospecs/dimension)
;; #_(get-names :asr.autospecs/alloc_arg)
;; (get-names :asr.autospecs/attribute)
;; #_(get-names :asr.autospecs/attribute_arg)
;; #_(get-names :asr.autospecs/call_arg)
;; (get-names :asr.autospecs/tbind)
;; #_(get-names :asr.autospecs/array_index)
;; #_(get-names :asr.autospecs/do_loop_head)
;; (get-names :asr.autospecs/case_stmt)
;; (get-names :asr.autospecs/type_stmt)
;; (get-names :asr.autospecs/enumtype)


;; (def cooked-composite-heads
;;   (let [exprs     (get-names :asr.autospecs/expr)
;;         stmts     (get-names :asr.autospecs/stmt)
;;         ttypes    (get-names :asr.autospecs/ttype)
;;         symbols   (get-names :asr.autospecs/symbol)
;;         ctexprs   (count exprs)
;;         ctstmts   (count stmts)
;;         ctttypes  (count ttypes)
;;         ctsymbols (count symbols)
;;         sum (+ ctexprs ctstmts ctttypes ctsymbols)
;;         all (clojure.set/union exprs stmts ttypes symbols)]
;;     {:ctexprs ctexprs, :ctstmts ctstmts,
;;      :ctttypes ctttypes, :ctsymbols ctsymbols,
;;      :sum sum,
;;      :subset?
;;      (clojure.set/subset?
;;       all
;;       raw-composite-heads),
;;      :raw-all-difference
;;      (clojure.set/difference
;;       raw-composite-heads
;;       all),
;;      :all-raw-difference
;;      (clojure.set/difference
;;       all,
;;       raw-composite-heads)}))

;; (count raw-composite-heads)
;; cooked-composite-heads
;; (count cooked-composite-heads)

;; (take 5 raw-composite-heads)

;; (take 5 cooked-composite-heads)

;; (clojure.set/subset? cooked-composite-heads raw-composite-heads)

;; (count (clojure.set/difference raw-composite-heads cooked-composite-heads))

;; (filter #(= (:head %) :asr.autospecs/TypeStmt) composite-stuffs)

;; (def tuple-stuffs
;;   (filter #(= (:grup %) :ASDL-TUPLE) big-list-of-stuff))

;; ;; spot-check with CIDER C-c C-e in buffer

;; (count tuple-stuffs)
;; (first tuple-stuffs)


;;  ___ _        ___               _ _    _     _           _  _             _
;; | _ |_)__ _  / __|_  _ _ __  __| (_)__| |_  | |__ _  _  | || |___ __ _ __| |
;; | _ \ / _` | \__ \ || | '  \/ _` | / _|  _| | '_ \ || | | __ / -_) _` / _` |
;; |___/_\__, | |___/\_, |_|_|_\__,_|_\__|\__| |_.__/\_, | |_||_\___\__,_\__,_|
;;       |___/       |__/                            |__/


(def big-symdict-by-head
  (let [heads (map :head big-list-of-stuff)
        syms  (map (comp symbol name) heads)
        pairs (partition 2 (interleave syms big-list-of-stuff))
        pvecs (map vec pairs)
        big-dict (into {} pvecs)]
    big-dict))


#_(count big-symdict-by-head)
;; => 248


;;  _  _         _
;; | \| |___  __| |___
;; | .` / _ \/ _` / -_)
;; |_|\_\___/\__,_\___|


;;; Every alternative of a term is a _node_ (see grammar.clj).


;;; TODO: consider moving penv parameter to the front of every
;;; eval function, as with `self` in OOP.


(defn eval-stuff
  [stuff]
  (fn [penv]
    stuff))


(defn eval-node
  "sketch"
  [node]
  (fn [penv]

    (cond  ; order matters ...
      ;;------------------------------------------------
      (symbol? node)  ; then
      (let [stuff (node big-symdict-by-head)]
        (or stuff node))
      ;;------------------------------------------------
      (and (coll? node)
           (empty? node))  ; then
      node
      ;;------------------------------------------------
      (list? node)  ; then
      (let [stuff ((first node) big-symdict-by-head)]
        ((eval-stuff stuff) penv))
      ;;------------------------------------------------
      :else
      node
      ;;------------------------------------------------
      )))


(defn eval-nodes
  "sketch"
  [nodes]
  (fn [penv]
    (map (fn [node]
           ((eval-node node) penv))
         nodes)))


;;  ___            _         _ _____     _    _
;; / __|_  _ _ __ | |__  ___| |_   _|_ _| |__| |___
;; \__ \ || | '  \| '_ \/ _ \ | | |/ _` | '_ \ / -_)
;; |___/\_, |_|_|_|_.__/\___/_| |_|\__,_|_.__/_\___|
;;      |__/


;;; Symbol-table is not spec'ced in ASR.asdl.


;;; We have a design that is explicitly recursive. Try a design
;;; suitable for clojure.walk.


;;               ____
;;   __ _  __ __/ / / ____  _/|
;;  /  ' \/ // / / / /___/ > _<
;; /_/_/_/\_,_/_/_/        |/


;;                 __
;;  ___ _  _____ _/ / ____  _/|
;; / -_) |/ / _ `/ / /___/ > _<
;; \__/|___/\_,_/_/        |/


(defmulti eval-symbol first)            ; forward reference


(defn eval-bindings
  "Return a function of a penv, in which all bindings are evaluated.
  Supports lexical environments and closures."
  [bindings]
  (fn [penv]
    (loop [result {}
           remaining bindings]
      (if (seq remaining)               ; idiom for not empty
        (let [[k v] (first remaining)]
          (recur (into result {k ((eval-symbol v) penv)})
                 (rest remaining)))
        result))))


(defn new-penv
  "A new penv has a frame φ and a penv π. Bindings are looked up in
  the old penv and bound in the new penv. A frequent case for this is
  binding actual arguments to function parameters."
  [bindings penv]
  (atom {:φ ((eval-bindings bindings) penv),
         :π penv}
        :validator is-environment?))


(defn augment-bindings-penv!
  [bindings penv]
  (assert (is-penv? penv))
  (let [oenv (:π @penv)]
   (swap! penv (fn [env]
                 {:φ
                  (into (:φ env)
                        ((eval-bindings bindings) oenv))
                  :π oenv}))))


;;                             _
;;  ____  _ _ __ _ __  ___ _ _| |_
;; (_-< || | '_ \ '_ \/ _ \ '_|  _|
;; /__/\_,_| .__/ .__/\___/_|  \__|
;;         |_|  |_|


(defn eval-symbols
  "sketch"
  [symbols]
  (fn [penv]
    (map (fn [sym] ((eval-symbol sym) penv))
         symbols)))


(defn eval-bool
  [bool]
  (fn [_]
    (case bool
      .true.  true
      .false. false)))


;;               _                       _         _
;;  _____ ____ _| |  ___   ____  _ _ __ | |__  ___| |
;; / -_) V / _` | | |___| (_-< || | '  \| '_ \/ _ \ |
;; \___|\_/\__,_|_|       /__/\_, |_|_|_|_.__/\___/_|
;;                            |__/

;;; That's probably not a great name for this thing.


;;; docstrings are apparently not allowed in defmethod. For
;;; convenience, treat SymbolTable as if it were an ASR symbol.
;;; SymbolTable is not actually specified in ASR.


(defn term-from-head-sym
  "Summarize from 'big-symdict-by-head."
  [sym]
  (cond
    (= sym 'SymbolTable) 'symbol   ; no spec for this in ASDL
    (= sym 'ForTest)     'testing  ; ditto
    :else (symbol ; strip quotes
           (name ; strip namespace
            (:term ; fetch
             (sym big-symdict-by-head))))))


(defmethod eval-symbol 'Program
  [[head
    symtab
    nym
    dependencies
    body
    :as program]]
  (fn [penv]
    {:head         head          ; 'Program
     :term         (term-from-head-sym head)
     :symtab       ((eval-symbol symtab) penv)        ; 'SymbolTable
     :nym          nym           ; identifier
     :dependencies dependencies  ; identifier*
     :body         body          ; stmt*
     :penv         penv          ; Environment
     }))


;;; The problem to solve today 26 Jan 2023 is recursive lookup in
;;; penvs versus explicit lookup by symbol-table-id number.


(defmethod eval-symbol 'SymbolTable
  [[head
    integer-id
    bindings
    :as symbol-table]]
  (fn [penv]
    (let [np (new-penv bindings penv)
          ts {:head       head
              :term       (term-from-head-sym head)
              :integer-id integer-id   ; int
              :bindings   bindings     ; dict
              :penv       np           ; Environment
              }
          _ (identity (keys @ΓΣ))]     ; inspect in debugger
      (swap! ΓΣ (fn [old] (into old {integer-id np})))
      ts)))


(defmethod eval-symbol 'ForTest
  [[head
    datum]]
  (fn [penv]
    (if datum
      {:head  head,
       :term  (term-from-head-sym head)
       :datum ((eval-node datum) penv)}
      {:head head})))


(defmethod eval-symbol 'Variable
  [[head
    parent-symtab-id
    nym                          ; "name" shadows Clojure build-in.
    dependencies
    intent
    symbolic-value
    value
    storage
    tipe                         ; "type" shadows Clojure built-in.
    abi
    access
    presence
    value-attr      ; bool (.true., .false.)
    :as variable]]
  (fn [penv]
    {:head           head
     :term           (term-from-head-sym head)
     :symtab-id      parent-symtab-id
     :name           nym
     :dependencies   dependencies
     :intent         ((eval-node intent)         penv)
     :symbolic-value ((eval-node symbolic-value) penv)
     :value          ((eval-node value)          penv)
     :storage        ((eval-node storage)        penv)
     :type           ((eval-node tipe)           penv)
     :abi            ((eval-node abi)            penv)
     :access         ((eval-node access)         penv)
     :presence       ((eval-node presence)       penv)
     :value-attr     ((eval-node value-attr)     penv)
     }))


(defmethod eval-symbol 'Function
  [[head                       ; 'Function
    symtab                     ; SymbolTable
    nym                        ; identifier
    dependencies               ; identifier*
    args                       ; expr* !!! TODO !!! params ??? !!!
    body                       ; stmt*
    return-var                 ; expr?
    abi                        ; abi
    access                     ; access
    deftype                    ; deftype
    bindc-name                 ; string?
    elemental                  ; bool (.true., .false.)
    pure                       ; bool (.true., .false.)
    module                     ; bool (.true., .false.)
    inline                     ; bool (.true., .false.)
    static                     ; bool (.true., .false.)
    type-params                ; ttype*
    restrictions               ; symbol*
    is-restriction             ; bool (.true., .false.)
    deterministic               ; bool (.true., .false.)
    side-effect-free           ; bool (.true., .false.)
    :as function]]
  (fn [penv]
    {:head             head
     :term             (term-from-head-sym head)
     :symtab           ((eval-symbol symtab)            penv)
     :name             nym
     :dependencies     dependencies
     :args             ((eval-nodes   args)             penv)  ; TODO: params!
     :body             ((eval-nodes   body)             penv)
     :return-var       ((eval-node    return-var)       penv)
     :abi              ((eval-node    abi)              penv)
     :access           ((eval-node    access)           penv)
     :deftype          ((eval-node    deftype)          penv)
     :bindc-name       bindc-name
     :elemental        ((eval-bool    elemental)        penv)
     :pure             ((eval-bool    pure)             penv)
     :module           ((eval-bool    module)           penv)
     :inline           ((eval-bool    inline)           penv)
     :static           ((eval-bool    static)           penv)
     :type-params      ((eval-nodes   type-params)      penv)
     :restrictions     ((eval-symbols restrictions)     penv)
     :is-restriction   ((eval-bool    is-restriction)   penv)
     :deterministic    ((eval-bool    deterministic)    penv)
     :side-effect-free ((eval-bool    side-effect-free) penv)
     }))


;;; The following was automatically written by chatGPT:


(defmethod eval-symbol 'SubroutineCall
  [[head
    symtab
    nym
    arguments
    dependencies
    call-type
    :as subroutine-call]]
  (fn [penv]
    (let [symtable  ((eval-symbol symtab) penv)
          args      (map (fn [arg] ((eval-node arg) penv)) arguments)
          sub       (get symtable nym)]
      (if (not sub)
        (throw (Exception. (str "Error: Subroutine " nym " not found")))
        (do (println "Calling subroutine: " nym " with args: " args)
            #_(run-subroutine subroutine args penv)
            {:head           head
             :term           (term-from-head-sym head)
             :symtab         symtab
             :name           nym
             :arguments      args
             :dependencies   dependencies
             :call-type      ((eval-node call-type) penv)
             :subroutine     ((eval-symbol sub) penv)})))))


(defmethod eval-symbol 'TranslationUnit
  [[head
    global-scope
    items
    :as translation-unit]]
  (assert (= 'TranslationUnit head)
          "head of a translation unit must be the symbol
          TranslationUnit")
  (fn [penv]
    #_(assert (s/valid? :asr.autospecs/TranslationUnit translation-unit))
    (let [tu {:head         head
              :term         (term-from-head-sym head)
              ;; We know the global-scope is a symbol-table.
              :global-scope ((eval-symbol global-scope) penv)
              :items        ((eval-nodes items) penv)}
          main-prog (lookup-penv 'main_program (:penv (:global-scope tu)))]
      tu
      ;; (when main-prog
      ;;   (run-program main-prog))
      )))


;;                                     _        _
;;  _ _ _  _ _ _    ___   _____ ____ _| |___ __| |
;; | '_| || | ' \  |___| / -_) V / _` | / -_) _` |
;; |_|  \_,_|_||_|       \___|\_/\__,_|_\___\__,_|



(defn run-program
  [e]
  (echo e)
  (echo @ΓΣ)
  (echo (keys (:φ @(get @ΓΣ 1))))
  (let [code (:body e)]
    (echo code))
  )


@ΓΣ


;;; The "global scope" or "global symbol registry" ΓΣ is really
;;; one below the unique global environment ΓΠ. ΓΣ contains
;;; user-defined symbols. ΓΠ contains built-ins.

;; {4
;;  #<Atom@427a99ab:
;;    {:φ {}, :π #<Atom@110d2d14: {:φ {}, :π nil}>}>,
;;  2
;;  #<Atom@7d9983df:
;;    {:φ
;;     {:x
;;      {:presence
;;       {:head :asr.autospecs/Required,
;;        :term :asr.autospecs/presence,
;;        :grup :ASDL-SYMCONST,
;;        :form {:ASDL-SYMCONST "Required"}},
;;       :name x,
;;       :value (),
;;       :type
;;       {:head :asr.autospecs/Integer,
;;        :term :asr.autospecs/ttype,
;;        :grup :ASDL-COMPOSITE,
;;        :form
;;        {:ASDL-COMPOSITE
;;         {:ASDL-HEAD "Integer",
;;          :ASDL-ARGS
;;          ({:ASDL-TYPE "int",
;;            :MULTIPLICITY :asr.parsed/once,
;;            :ASDL-NYM "kind"}
;;           {:ASDL-TYPE "dimension",
;;            :MULTIPLICITY :asr.parsed/zero-or-more,
;;            :ASDL-NYM "dims"})}}},
;;       :head Variable,
;;       :symtab-id 2,
;;       :abi
;;       {:head :asr.autospecs/Source,
;;        :term :asr.autospecs/abi,
;;        :grup :ASDL-SYMCONST,
;;        :form {:ASDL-SYMCONST "Source"}},
;;       :intent
;;       {:head :asr.autospecs/Local,
;;        :term :asr.autospecs/intent,
;;        :grup :ASDL-SYMCONST,
;;        :form {:ASDL-SYMCONST "Local"}},
;;       :storage
;;       {:head :asr.autospecs/Default,
;;        :term :asr.autospecs/storage_type,
;;        :grup :ASDL-SYMCONST,
;;        :form {:ASDL-SYMCONST "Default"}},
;;       :access
;;       {:head :asr.autospecs/Public,
;;        :term :asr.autospecs/access,
;;        :grup :ASDL-SYMCONST,
;;        :form {:ASDL-SYMCONST "Public"}},
;;       :dependencies [],
;;       :symbolic-value (),
;;       :value-attr .false.}},
;;     :π #<Atom@110d2d14: {:φ {}, :π nil}>}>,
;;  3
;;  #<Atom@2ba69af7:
;;    {:φ {}, :π #<Atom@110d2d14: {:φ {}, :π nil}>}>,
;;  1
;;  #<Atom@4b5084d8:
;;    {:φ
;;     {:_lpython_main_program
;;      {:args (),
;;       :deftype
;;       {:head :asr.autospecs/Implementation,
;;        :term :asr.autospecs/deftype,
;;        :grup :ASDL-SYMCONST,
;;        :form {:ASDL-SYMCONST "Implementation"}},
;;       :restrictions (),
;;       :type-params (),
;;       :symtab
;;       {:head SymbolTable,
;;        :integer-id 4,
;;        :bindings {},
;;        :penv
;;        #<Atom@427a99ab:
;;          {:φ {}, :π #<Atom@110d2d14: {:φ {}, :π nil}>}>},
;;       :is-restriction false,
;;       :bindc-name (),
;;       :name _lpython_main_program,
;;       :static false,
;;       :return-var (),
;;       :module false,
;;       :head Function,
;;       :abi
;;       {:head :asr.autospecs/Source,
;;        :term :asr.autospecs/abi,
;;        :grup :ASDL-SYMCONST,
;;        :form {:ASDL-SYMCONST "Source"}},
;;       :access
;;       {:head :asr.autospecs/Public,
;;        :term :asr.autospecs/access,
;;        :grup :ASDL-SYMCONST,
;;        :form {:ASDL-SYMCONST "Public"}},
;;       :pure false,
;;       :body
;;       ({:head :asr.autospecs/SubroutineCall,
;;         :term :asr.autospecs/stmt,
;;         :grup :ASDL-COMPOSITE,
;;         :form
;;         {:ASDL-COMPOSITE
;;          {:ASDL-HEAD "SubroutineCall",
;;           :ASDL-ARGS
;;           ({:ASDL-TYPE "symbol",
;;             :MULTIPLICITY :asr.parsed/once,
;;             :ASDL-NYM "name"}
;;            {:ASDL-TYPE "symbol",
;;             :MULTIPLICITY :asr.parsed/at-most-once,
;;             :ASDL-NYM "original_name"}
;;            {:ASDL-TYPE "call_arg",
;;             :MULTIPLICITY :asr.parsed/zero-or-more,
;;             :ASDL-NYM "args"}
;;            {:ASDL-TYPE "expr",
;;             :MULTIPLICITY :asr.parsed/at-most-once,
;;             :ASDL-NYM "dt"})}}}),
;;       :dependencies [main0],
;;       :inline false,
;;       :elemental false},
;;      :main0
;;      {:args (),
;;       :deftype
;;       {:head :asr.autospecs/Implementation,
;;        :term :asr.autospecs/deftype,
;;        :grup :ASDL-SYMCONST,
;;        :form {:ASDL-SYMCONST "Implementation"}},
;;       :restrictions (),
;;       :type-params (),
;;       :symtab
;;       {:head SymbolTable,
;;        :integer-id 2,
;;        :bindings
;;        {:x
;;         (Variable
;;          2
;;          x
;;          []
;;          Local
;;          ()
;;          ()
;;          Default
;;          (Integer 4 [])
;;          Source
;;          Public
;;          Required
;;          .false.)},
;;        :penv
;;        #<Atom@7d9983df:
;;          {:φ
;;           {:x
;;            {:presence
;;             {:head :asr.autospecs/Required,
;;              :term :asr.autospecs/presence,
;;              :grup :ASDL-SYMCONST,
;;              :form {:ASDL-SYMCONST "Required"}},
;;             :name x,
;;             :value (),
;;             :type
;;             {:head :asr.autospecs/Integer,
;;              :term :asr.autospecs/ttype,
;;              :grup :ASDL-COMPOSITE,
;;              :form
;;              {:ASDL-COMPOSITE
;;               {:ASDL-HEAD "Integer",
;;                :ASDL-ARGS
;;                ({:ASDL-TYPE "int",
;;                  :MULTIPLICITY :asr.parsed/once,
;;                  :ASDL-NYM "kind"}
;;                 {:ASDL-TYPE "dimension",
;;                  :MULTIPLICITY :asr.parsed/zero-or-more,
;;                  :ASDL-NYM "dims"})}}},
;;             :head Variable,
;;             :symtab-id 2,
;;             :abi
;;             {:head :asr.autospecs/Source,
;;              :term :asr.autospecs/abi,
;;              :grup :ASDL-SYMCONST,
;;              :form {:ASDL-SYMCONST "Source"}},
;;             :intent
;;             {:head :asr.autospecs/Local,
;;              :term :asr.autospecs/intent,
;;              :grup :ASDL-SYMCONST,
;;              :form {:ASDL-SYMCONST "Local"}},
;;             :storage
;;             {:head :asr.autospecs/Default,
;;              :term :asr.autospecs/storage_type,
;;              :grup :ASDL-SYMCONST,
;;              :form {:ASDL-SYMCONST "Default"}},
;;             :access
;;             {:head :asr.autospecs/Public,
;;              :term :asr.autospecs/access,
;;              :grup :ASDL-SYMCONST,
;;              :form {:ASDL-SYMCONST "Public"}},
;;             :dependencies [],
;;             :symbolic-value (),
;;             :value-attr .false.}},
;;           :π #<Atom@110d2d14: {:φ {}, :π nil}>}>},
;;       :is-restriction false,
;;       :bindc-name (),
;;       :name main0,
;;       :static false,
;;       :return-var (),
;;       :module false,
;;       :head Function,
;;       :abi
;;       {:head :asr.autospecs/Source,
;;        :term :asr.autospecs/abi,
;;        :grup :ASDL-SYMCONST,
;;        :form {:ASDL-SYMCONST "Source"}},
;;       :access
;;       {:head :asr.autospecs/Public,
;;        :term :asr.autospecs/access,
;;        :grup :ASDL-SYMCONST,
;;        :form {:ASDL-SYMCONST "Public"}},
;;       :pure false,
;;       :body
;;       (nil
;;        {:head :asr.autospecs/Print,
;;         :term :asr.autospecs/stmt,
;;         :grup :ASDL-COMPOSITE,
;;         :form
;;         {:ASDL-COMPOSITE
;;          {:ASDL-HEAD "Print",
;;           :ASDL-ARGS
;;           ({:ASDL-TYPE "expr",
;;             :MULTIPLICITY :asr.parsed/at-most-once,
;;             :ASDL-NYM "fmt"}
;;            {:ASDL-TYPE "expr",
;;             :MULTIPLICITY :asr.parsed/zero-or-more,
;;             :ASDL-NYM "values"}
;;            {:ASDL-TYPE "expr",
;;             :MULTIPLICITY :asr.parsed/at-most-once,
;;             :ASDL-NYM "separator"}
;;            {:ASDL-TYPE "expr",
;;             :MULTIPLICITY :asr.parsed/at-most-once,
;;             :ASDL-NYM "end"})}}}),
;;       :dependencies [],
;;       :inline false,
;;       :elemental false},
;;      :main_program
;;      {:head Program,
;;       :symtab
;;       {:head SymbolTable,
;;        :integer-id 3,
;;        :bindings {},
;;        :penv
;;        #<Atom@2ba69af7:
;;          {:φ {}, :π #<Atom@110d2d14: {:φ {}, :π nil}>}>},
;;       :nym main_program,
;;       :dependencies [],
;;       :body [(SubroutineCall 1 _lpython_main_program () [] ())],
;;       :penv #<Atom@110d2d14: {:φ {}, :π nil}>}},
;;     :π #<Atom@110d2d14: {:φ {}, :π nil}>}>}
