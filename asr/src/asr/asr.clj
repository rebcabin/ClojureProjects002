(ns asr.asr
  (:use [asr.utils]
        [clojure.set])

  ;;; TODO: Consider https://github.com/rebcabin/odin

  (:require
   [asr.grammar                   ]
   [asr.lpython                   ]
   [clojure.pprint :refer [pprint]]
   [asr.parsed     :refer [
                           shallow-map-from-speclet,
                           hashmap-from-speclet,
                           map-pair-from-speclet-map,

                           kind-from-form,
                           head-from-kind-form,
                           stuff-from-term-form,
                           ]]
   [clojure.walk   :as     walk    ]
   [clojure.zip    :as     zip    ]))


;;  ___         _                            _
;; | __|_ ___ _(_)_ _ ___ _ _  _ __  ___ _ _| |_
;; | _|| ' \ V / | '_/ _ \ ' \| '  \/ -_) ' \  _|
;; |___|_||_\_/|_|_| \___/_||_|_|_|_\___|_||_\__|


;;; User names MUST NOT USE GREEK.


;;; For flexibility, all "(eval-...)" functions return a function
;;; of an Environment. TODO: spec Environment. Such functions can
;;; be evaluated in any environment later.


;;; An Environment is a dictionary. An Environment-atom or
;;; PEnv (for pointer-to-environment) is a mutable object: an atom
;;; containing an Environment. It is needed for mutability.


(defn is-environment?
  "Ensure keys φ and π exist. Note it's not sufficient to check the
  value of π because it's nil for the global environment AND it's
  nil for an missing the key π because `(:π {:φ (atom 'foo)})` ~~>
  nil."
  [thing]
  (subset? #{:φ, :π} (set (keys thing))))


(defn clj-atom?
  [thing]
  (instance? clojure.lang.Atom thing))


(defn is-penv?
  [thing]
  (and (clj-atom? thing)
       (is-environment? @thing)))


(defn indirect-penvs
  [penv]
  (assert (is-penv? penv))
  (let [{:keys [φ π]} @penv]
    (cond
      (nil? π) @penv
      :else {:φ φ, :π (indirect-penvs π)})))


(defn is-global-penv?
  "The global environment is the only one with a nil parent. "
  [penv]
  (and (is-penv? penv)
       (nil? (:π @penv))))


(def ΓΠ
  "Unique, session-specific penv: Global Γ Perimeter Π; has a
  frame (:φ is-a dict) and a parent perimeter (:π is-a penv).
  TODO: spec."
  (atom {:φ {}, :π nil} :validator is-environment?))


(def Γsymtab-registry
  "Unique, session-specific, integer-indexed, global registry of
  symbol tables."
  (atom {} :validator map?))


(defn eval-bindings
  [bindings]
  (throw (AssertionError.
          "Forward-reference: incorrect eval-bindings function called")))


(defn new-penv
  "A new penv has a frame φ and an penv π."
  [bindings penv]
  (atom {:φ ((eval-bindings bindings) penv),
         :π penv}
        :validator is-environment?))


;;; I think we don't ever need to "delete" a penv, but we may need to clear one
;;; or more bindings.


(defn clear-bindings-penv!
  [penv]
  (assert (is-penv? penv))
  (swap! penv (fn [env]
                {:φ (apply dissoc (:φ @penv) (keys (:φ @penv)))
                 :π (:π @penv)})))


(defn clear-binding-penv!
  [penv
   key]
  (assert (is-penv? penv))
  (swap! penv (fn [env]
                {:φ (dissoc (:φ @penv) key)
                 :π (:π @penv)})))


(defn augment-bindings-penv!
  [bindings penv]
  (assert (is-penv? penv))
  (let [oenv (:π @penv)]
   (swap! penv (fn [env]
                 {:φ
                  (into (:φ env)
                        ((eval-bindings bindings) oenv))
                  :π oenv}))))


(defn lookup-penv
  [sym penv]
  (assert (or (nil? penv) (is-penv? penv)) "Invalid penv")
  (assert (instance? clojure.lang.Symbol sym) "Invalid symbol")
  (cond
    (nil? penv) nil
    true (let [r ((:φ @penv) (keyword sym))]
           (cond
             (nil? r) (recur sym (:π @penv))
             true r))))


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


(count asr.parsed/speclets)
;; => 28

(count speclets)
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


(count big-map-of-speclets-from-terms)
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
;;; :ASDL-TUPLEs are parameter-lists with gensymmed heads.
;;;
;;; :ASDL-COMPOSITEs have symbolic _heads_ and parameter-lists in
;;; round brackets.


;;; The group-by above produces a list with three elements:


(->> asr-groups count)
;; => 3


;;; The elements are the group keys:


(->> asr-groups (map first))
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


(->> (get-symconsts) count)
;; => 14


;;; List out the terms and forms alternation -- left-hand and
;;; right-hand sides -- of the fourteen symconst productions. Here
;;; are the terms (left-hand sides) of all ASR-SYMCONSTs:


(->> (get-symconsts) symbolize-terms)
;;  abi                 cmpop               access
;;  storage_type        intent              enumtype
;;  deftype             arraybound          logicalbinop
;;  cast_kind           arraystorage        integerboz
;;  presence            binop


;;; Go over the development of the extraction of heads so as to
;;; identify an abstraction for the data structure.


;;; Here are the forms of the first symconst, as an example:


(->> (get-symconsts) (map second) first)
;; => ({:ASDL-SYMCONST "Source"}
;;     {:ASDL-SYMCONST "LFortranModule"}
;;     {:ASDL-SYMCONST "GFortranModule"}
;;     {:ASDL-SYMCONST "BindC"}
;;     {:ASDL-SYMCONST "Interactive"}
;;     {:ASDL-SYMCONST "Intrinsic"})


;;; See that each form is a singleton with identical group-key.
;;; Check that for the example:


(let [forms         (map second (get-symconsts))
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


(let [forms         (map second (get-symconsts))
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
  [group-key group]
  (let [forms  (map second group)
        _      (map (partial check-counts 1) forms)
        _      (map (partial check-firsts group-key)
                    (map first forms))
        vals-  (map (partial map :ASDL-SYMCONST) forms)
        syms-  (map (partial map symbol) vals-)]
    syms-))


(->> (get-symconsts)
     (symbolize-symconst-heads :ASDL-SYMCONST))
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


(->> (get-tuples) count)
;; => 6


;; Here are the terms (left-hand sides) of all ASR-TUPLES.


(->> (get-tuples) symbolize-terms)
;;  call_arg            do_loop_head        alloc_arg
;;  attribute_arg       array_index         dimension


;;; Go over the development of the extraction of heads so as to
;;  identify an abstraction for the data structure.


;;; Here are the forms of the first tuple, as an example:


(let [forms  (map second (get-tuples))
      a-form (first forms)]
  a-form)
;; => ({:ASDL-TUPLE "asr-tuple12765",
;;      :ASDL-ARGS
;;      ({:ASDL-TYPE "expr",
;;        :MULTIPLICITY :asr.parsed/at-most-once,
;;        :ASDL-NYM "value"})})


;;; See that it's a singleton of a doubleton; check this; also
;;; check that the keyword is :ASDL-TUPLE:


(let [forms  (map second (get-tuples))
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


(let [forms  (map second (get-tuples))
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
  [group-key group]
  (let [forms  (map second group)
        _      ((partial check-counts 1) forms)
        _      (map (partial check-counts 2) forms)
        _      (map (partial check-first group-key)
                    (->> forms (map first) (map first)))
        vals-  (map (partial map :ASDL-TUPLE) forms)
        flats1 (map first vals-)
        syms-  (map symbol flats1)]
    syms-))


(->> (get-tuples) (symbolize-tuple-heads :ASDL-TUPLE))
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


(->> (get-composites) count)
;; => 10


;; Here are the terms (left-hand sides) of all ASR-COMPOSITEs.


(->> (get-composites) symbolize-terms)
;;  restriction_arg     type_stmt           case_stmt
;;  symbol              attribute           ttype
;;  stmt                tbind               expr
;;  unit


;;; For a little variation, let's analyze the second
;;; composite (this should always work because there should always
;;; be more than one composite). Note that asserts are not always
;;; thrown, here, but you can catch them by stepping with the
;;; debugger (TODO: reason unknown).


(map second (get-composites))


;;; Though the structure of a composite is more rich than the
;;; structures of the other groups (symconsts and tuples),
;;; extracting the heads is easier. Here is how to get the heads
;;; of one composite:


(let [forms  (map second (get-composites))
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
  [group-key group]
  (let [forms   (map second (get-composites))
        valss-  (map #(map :ASDL-COMPOSITE %) forms)
        headss- (map #(map :ASDL-HEAD %) valss-)
        symss-  (map #(map symbol %) headss-)]
    symss-))


(symbolize-composite-heads :ASDL-COMPOSITE (get-composites))
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


(let [ccs (get-composites)
      terms (->> ccs symbolize-terms)
      head-counts
      (->> (symbolize-composite-heads :ASDL-COMPOSITE ccs)
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


(->> big-map-of-speclets-from-terms
     first)
;; => [:asr.autospecs/abi
;;     ({:ASDL-SYMCONST "Source"}
;;      {:ASDL-SYMCONST "LFortranModule"}
;;      {:ASDL-SYMCONST "GFortranModule"}
;;      {:ASDL-SYMCONST "BindC"}
;;      {:ASDL-SYMCONST "Interactive"}
;;      {:ASDL-SYMCONST "Intrinsic"})]


(->> big-map-of-speclets-from-terms
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


(->> big-map-of-speclets-from-terms
     (map first))
;; 01  abi             call_arg        do_loop_head    restriction_arg
;; 02  cmpop           type_stmt       access          storage_type
;; 03  intent          case_stmt       enumtype        alloc_arg
;; 04  symbol          deftype         arraybound      attribute
;; 05  logicalbinop    ttype           cast_kind       stmt
;; 06  arraystorage    integerboz      tbind           presence
;; 07  expr            unit            binop           attribute_arg
;; 08  array_index     dimension


;;; For the 14 composites, fetch the heads out of the columnized
;;; data (the ___columns___):


(defn fetch-pair [key map]
  [key (key map)])


(->> big-map-of-speclets-from-terms
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


(->> big-map-of-speclets-from-terms
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


(->> big-map-of-speclets-from-terms
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

;; ;; spot-check with CIDER C-c C-e in buffer

;; (count big-list-of-stuff)
;; (first big-list-of-stuff)
;; (count asr.parsed/big-list-of-stuff)
;; (first asr.parsed/big-list-of-stuff)


;; (def symconst-stuffs
;;   (filter #(= (:kind %) :ASDL-SYMCONST) big-list-of-stuff))

;; ;; spot-check with CIDER C-c C-e in buffer

;; (count symconst-stuffs)
;; (first symconst-stuffs)
;; (count asr.parsed/symconst-stuffs)
;; (first asr.parsed/symconst-stuffs)


;; (def composite-stuffs
;;   (filter #(= (:kind %) :ASDL-COMPOSITE) big-list-of-stuff))

;; ;; spot-check with CIDER C-c C-e in buffer

;; (count composite-stuffs)
;; (first composite-stuffs)
;; (count asr.parsed/composite-stuffs)
;; (first asr.parsed/composite-stuffs)


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
;;   (filter #(= (:kind %) :ASDL-TUPLE) big-list-of-stuff))

;; ;; spot-check with CIDER C-c C-e in buffer

;; (count tuple-stuffs)
;; (first tuple-stuffs)


;;  _  _         _
;; | \| |___  __| |___
;; | .` / _ \/ _` / -_)
;; |_|\_\___/\__,_\___|


(defn eval-node
  "sketch"
  [node]
  (fn [penv]
    node))


(defn eval-nodes
  "sketch"
  [nodes]
  (fn [penv]
    (map (fn [node]
           ((eval-node node) penv)) nodes)))


;;  ___            _         _ _____     _    _
;; / __|_  _ _ __ | |__  ___| |_   _|_ _| |__| |___
;; \__ \ || | '  \| '_ \/ _ \ | | |/ _` | '_ \ / -_)
;; |___/\_, |_|_|_|_.__/\___/_| |_|\__,_|_.__/_\___|
;;      |__/


(defmulti eval-symbol first)            ; forward reference


(defn eval-bindings
  [bindings]
  (fn [penv]
    (loop [result {}
           remaining bindings]
      (if (seq remaining)               ; idiom for not empty
        (let [[k v] (first remaining)]
          (recur (into result {k ((eval-symbol v) penv)})
                 (rest remaining)))
        result))))


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
  (fn [penv]
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


(defmethod eval-symbol 'Program
  [[head
    symtab
    nym
    dependencies
    body
    :as program]]
  (fn [penv]
    (echo {:head         head          ; 'Program
           :symtab       ((eval-symbol symtab) penv)        ; 'SymbolTable
           :nym          nym           ; identifier
           :dependencies dependencies  ; identifier*
           :body         body          ; stmt*
           :penv         penv         ; Environment
           })))


(defmethod eval-symbol 'SymbolTable
  [[head
    integer-id
    bindings
    :as symbol-table]]
  (fn [penv]
    (let [np (new-penv bindings penv)
          ts {:head       head
              :integer-id integer-id   ; int
              :bindings   bindings     ; dict
              :penv       np          ; Environment
              }
          _ (keys @Γsymtab-registry)]  ; inspect in debugger
      (swap! Γsymtab-registry
             (fn [old] (into old {integer-id np})))
      (echo ts))))


(defmethod eval-symbol 'ForTest
  [[head
    datum]]
  (fn [penv]
    (if datum
      {:head  head,
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
    (echo {:head           head
           :symtab-id      parent-symtab-id
           :name           nym
           :dependencies   dependencies
           :intent         ((eval-node    intent)         penv)
           :symbolic-value ((eval-node    symbolic-value) penv)
           :value          ((eval-node    value)          penv)
           :storage        ((eval-node    storage)        penv)
           :type           ((eval-node    tipe)           penv)
           :abi            ((eval-node    abi)            penv)
           :access         ((eval-node    access)         penv)
           :presence       ((eval-node    presence)       penv)
           :value-attr     ((eval-node    value-attr)     penv)
           })))


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
    :as function]]
  (fn [penv]
    (echo {:head           head
           :symtab         ((eval-symbol symtab)          penv)
           :name           nym
           :dependencies   dependencies
           :args           ((eval-nodes   args)           penv)
           :body           ((eval-nodes   body)           penv)
           :return-var     ((eval-node    return-var)     penv)
           :abi            ((eval-node    abi)            penv)
           :access         ((eval-node    access)         penv)
           :deftype        ((eval-node    deftype)        penv)
           :bindc-name     bindc-name
           :elemental      ((eval-bool    elemental)      penv)
           :pure           ((eval-bool    pure)           penv)
           :module         ((eval-bool    module)         penv)
           :inline         ((eval-bool    inline)         penv)
           :static         ((eval-bool    static)         penv)
           :type-params    ((eval-nodes   type-params)    penv)
           :restrictions   ((eval-symbols restrictions)   penv)
           :is-restriction ((eval-bool    is-restriction) penv)
           })))


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
            (echo {:head           head
                   :symtab         symtab
                   :name           nym
                   :arguments      args
                   :dependencies   dependencies
                   :call-type      ((eval-node call-type) penv)
                   :subroutine     ((eval-symbol sub) penv)}))))))

;;                        _
;;  _ _ _  _ _ _    ___  | |_ ___ _ _ _ __
;; | '_| || | ' \  |___| |  _/ -_) '_| '  \
;; |_|  \_,_|_||_|        \__\___|_| |_|_|_|



(defmulti run-term :head)


(defmethod run-term 'Program
  [program]
  (let [penv (-> program :symtab :penv)]
    true))


;;  _   _      _ _
;; | | | |_ _ (_) |_
;; | |_| | ' \| |  _|
;;  \___/|_||_|_|\__|


(defn eval-unit
  [[head
    global-scope
    items
    :as translation-unit]]
  (assert (= 'TranslationUnit head)
          "head of a translation unit must be the symbol
          TranslationUnit")
  (fn [penv]
    (let [tu (echo
              {:head         head
               :global-scope ((eval-symbol global-scope) penv)
                                        ; TODO: eval-symbol-table?
               :items        ((eval-nodes items) penv)})
          main-prog (lookup-penv 'main_program (:penv (:global-scope tu)))])
    ))
