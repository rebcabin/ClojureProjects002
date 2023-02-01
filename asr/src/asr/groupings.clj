(ns asr.groupings
  (:require
    [clojure.zip :as zip]
    [asr.grammar :refer :all]
    [asr.lpython :refer :all]
    [asr.columnize :refer :all]
    [asr.parsed :refer [
                        shallow-map-from-speclet,
                        hashmap-from-speclet,
                        map-pair-from-speclet-map,

                        kind-from-form,
                        head-from-kind-form,
                        stuff-from-term-form,
                        ]])
  ;[:require [asr.asr :refer [big-map-of-speclets-from-terms]]]
  )


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
;;; namespaces, e.g. asr.parsed/speclets to prevent collisions
;;; with the snapshot:


(def speclets
  (vec (rest
         ((-> (zip/vector-zip asr-asdl-hiccup)
               zip/down zip/right zip/right) 0))))


#_(count speclets)
;; => 30


;;; The snapshot has fewer speclets:


#_(count asr.parsed/speclets)
;; => 28


;;; But the CODE in asr.parsed works on both the snapshot and on
;;; the live ASR:


(def big-map-of-speclets-from-terms
  (apply hash-map
         (mapcat identity ; flatten once
                 (map
                   (comp map-pair-from-speclet-map
                         hashmap-from-speclet)
                   speclets))))


;;; Inspect the count of speclets by pretty-printing to a
;;; comment (C-c C-f C-v C-c e):


#_(count big-map-of-speclets-from-terms)
;; => 30


;;; Obviously, terms and speclets are in 1-to-1 correspondence.


;;  _    _        _ _    _          __      _         __  __
;; | |__(_)__ _  | (_)__| |_   ___ / _|  __| |_ _  _ / _|/ _|
;; | '_ \ / _` | | | (_-<  _| / _ \  _| (_-<  _| || |  _|  _|
;; |_.__/_\__, | |_|_/__/\__| \___/_|   /__/\__|\_,_|_| |_|
;;        |___/


(def big-list-of-stuff
  (mapcat
    identity ; Flatten once.
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


;;; The group-by above produces a hashmap with three elements:


#_(->> asr-groups count)
;; => 3


;;; The elements are the group keys:


#_(->> asr-groups keys)
;; => (:ASDL-SYMCONST :ASDL-TUPLE :ASDL-COMPOSITE)
#_(->> asr-groups (map first))
;; => (:ASDL-SYMCONST :ASDL-TUPLE :ASDL-COMPOSITE)


;;; Here are support routines, same for all the groups


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
;;; string (via "name") and then to symbol to remove the quote
;;; marks:


(defn symbolize-terms
  [group]
  (->> group
       ;; the actual terms -- left-hand sides of productions
       keys
       ;; Convert to string without the namespace (always asr.autospecs).
       ;; TODO: check that!
       (map name)
       ;; Convert to symbol to rid the double quotes.
       (map symbol)))


#_(symbolize-terms asr-groups)
;; => (ASDL-SYMCONST ASDL-TUPLE ASDL-COMPOSITE)


(def flat-symconst-terms-set
  (->> asr-groups
       :ASDL-SYMCONST
       symbolize-terms
       set))


;; Here are the 14 symconst terms:


#_flat-symconst-terms-set
;;; => #{cmpop          arraystorage    deftype
;;      arraybound      storage_type    binop
;;      presence        integerboz      logicalbinop
;;      enumtype        abi             intent
;;      cast_kind       access


(def flat-composite-terms-set
  (->> asr-groups
       :ASDL-COMPOSITE
       symbolize-terms
       set))


;; Here are the 10 composite terms:


#_flat-composite-terms-set
;; => #{tbind           attribute       restriction_arg
;;      unit            symbol          case_stmt
;;      type_stmt       expr            ttype
;;      stmt}


(def flat-tuple-terms-set
  (->> asr-groups
       :ASDL-TUPLE
       symbolize-terms
       set))


;; Here are the 6 tuple terms:


#_flat-tuple-terms-set
;; => #{attribute_arg   alloc_arg       do_loop_head
;;      call_arg        array_index     dimension


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
;;; right-hand sides -- of the fourteen symconst productions.


;;; Here are the terms (left-hand sides) of all ASR-SYMCONSTs:


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


#_(let [forms (map second (get-symconsts))
        a-form (first forms)
        count-checked ((partial check-counts 1) a-form)
        key-checked ((partial check-firsts :ASDL-SYMCONST)
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


#_(let [forms (map second (get-symconsts))
        a-form (first forms)
        _ ((partial check-counts 1) a-form)
        _ ((partial check-firsts :ASDL-SYMCONST)
           (map first a-form))
        vals- (map :ASDL-SYMCONST a-form)
        syms- (map symbol vals-)]
    syms-)
;; => (Source LFortranModule GFortranModule BindC Interactive Intrinsic)


;;; Now do likewise for every ASDL-SYMCONST in the groups with one
;;; extra level of mapping (inspect intermediate results with the
;;; debugger):


(defn symbolize-symconst-heads
  "The ->> macro does not debug cleanly on this construction, so we
  back off to a 'let.'"
  [group]
  (let [forms (map second group)
        _ (map (partial check-counts 1) forms)
        _ (map (partial check-firsts :ASDL-SYMCONST)
               (map first forms))
        vals- (map (partial map :ASDL-SYMCONST) forms)
        syms- (map (partial map symbol) vals-)]
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
     (map count))
;; => (6 6 2 4 6 4 2 2 5 20 2 3 2 10)


#_(->> (get-symconsts)
       (symbolize-symconst-heads)
       (mapcat identity)
       count)
;; => 74


(def flat-symconst-heads-set
  (->> (get-symconsts)
       symbolize-symconst-heads
       (mapcat identity)
       set))


(defn symconst?
  "Used in eval-node."
  [head]
  (head flat-symconst-heads-set))


;;; Check for duplicates


#_(count flat-symconst-heads-set)
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


#_(let [forms (map second (get-tuples))
        a-form (first forms)]
    a-form)
;; => ({:ASDL-TUPLE "asr-tuple12765",
;;      :ASDL-ARGS
;;      ({:ASDL-TYPE "expr",
;;        :MULTIPLICITY :asr.parsed/at-most-once,
;;        :ASDL-NYM "value"})})


;;; See that it's a singleton of a doubleton; check this; also
;;; check that the keyword is :ASDL-TUPLE:


#_(let [forms (map second (get-tuples))
        a-form (first forms)
        _ ((partial check-count 1) a-form)
        _ ((partial check-counts 2) a-form)
        _ ((partial check-first :ASDL-TUPLE)
           (->> a-form first first))]
    a-form)
;; => ({:ASDL-TUPLE "asr-tuple12765",
;;      :ASDL-ARGS
;;      ({:ASDL-TYPE "expr",
;;        :MULTIPLICITY :asr.parsed/at-most-once,
;;        :ASDL-NYM "value"})})


;;; Now get the vals, check singleton, flatten and symbolize:


#_(let [forms (map second (get-tuples))
        a-form (first forms)
        _ ((partial check-count 1) a-form)
        _ ((partial check-counts 2) a-form)
        _ ((partial check-first :ASDL-TUPLE)
           (->> a-form first first))
        vals- (map :ASDL-TUPLE a-form)
        flat1 (first vals-)
        sym (symbol flat1)]
    sym)
;; => asr-tuple12765

;;; Note that the "head" is a gensymmed (made-up) symbol. See
;;; grammar.clj.


;;; As before with symconsts, add one level of "map" to get all
;;; the heads. Note "check-counts 1" instead of "check-count 1" to
;;; effect the extra level of mapping. Unlike symconsts and
;;; composites, there is only one level of list for each tuple
;;; head.


(defn symbolize-tuple-heads
  [group]
  (let [forms (map second group)
        _ ((partial check-counts 1) forms)
        _ (map (partial check-counts 2) forms)
        _ (map (partial check-first :ASDL-TUPLE)
               (->> forms (map first) (map first)))
        vals- (map (partial map :ASDL-TUPLE) forms)
        flats1 (map first vals-)
        syms- (map symbol flats1)]
    syms-))


(def flat-tuple-heads-set
  (->> (get-tuples)
       symbolize-tuple-heads
       set))


(defn tuple?
  "Used in eval-node."
  [head]
  (head flat-tuple-heads-set))


#_flat-tuple-heads-set
;; => #{asr-tuple10900
;;      asr-tuple10898
;;      asr-tuple10895
;;      asr-tuple10896
;;      asr-tuple10897
;;      asr-tuple10899}


;;   ______                                 _ __
;;  <  / _ \  _______  __ _  ___  ___  ___ (_) /____ ___
;;  / / // / / __/ _ \/  ' \/ _ \/ _ \(_-</ / __/ -_|_-<
;; /_/\___/  \__/\___/_/_/_/ .__/\___/___/_/\__/\__/___/
;;                        /_/


;;; Ten composites:


;;; Third doesn't work on maps, though First and Second do.


(defn flip [f] (fn [x y] (f y x)))


(def third (partial (flip nth) 2))


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


;;; For a little variaty, analyze the second composite (this
;;; should work on every drop of ASR because there should always
;;; be more than one composite). Note that asserts are not always
;;; thrown, here, but you can catch them by stepping with the
;;; debugger (TODO: reason unknown).


;;; Though the structure of a composite is more rich than the
;;; structures of the other groups (symconsts and tuples),
;;; extracting the heads is easier. Here is how to get the heads
;;; of one composite:


#_(let [forms (map second (get-composites))
        a-form (nth forms 2)
        _ (map (partial check-firsts :ASDL-COMPOSITE)
               a-form)
        vals- (map :ASDL-COMPOSITE a-form)
        heads- (map :ASDL-HEAD vals-)
        syms- (map symbol heads-)
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


(def flat-composite-heads-set
  (->> (get-composites)
       symbolize-composite-heads
       (mapcat identity)
       set))


(defn composite?
  "Used in eval-node."
  [head]
  (head flat-composite-heads-set))


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


;;; Count them all:


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


;;         _                 _           _
;;  __ ___| |_  _ _ __  _ _ (_)______ __| |
;; / _/ _ \ | || | '  \| ' \| |_ / -_) _` |
;; \__\___/_|\_,_|_|_|_|_||_|_/__\___\__,_|


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


(defn fetch-pair [key map]
    [key (key map)])


;;; Fourteen symbols (only coincidentally the same as the number
;;; of symconst speclets.


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


;;; 86 expressions:


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


;;; 44 statements:


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


;;; A random example of a tuple


#_(->> big-map-of-speclets-from-terms
       (fetch-pair :asr.autospecs/call_arg)
       columnize-term)
;; => {:group asr-tuple,
;;     :nym :asr.autospecs/call_arg,
;;     :head asr-tuple17871,
;;     :parmtypes (expr),
;;     :parmnyms (value),
;;     :parmmults (:asr.parsed/at-most-once)}
