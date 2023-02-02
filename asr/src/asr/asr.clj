(ns asr.asr
  (:use [asr.utils]
        [asr.environment]
        [asr.groupings]
        [clojure.set])

  ;;; TODO: Consider https://github.com/rebcabin/odin

  (:require
   [blaster.clj-fstring :refer [f-str] ]
   [asr.grammar                        ]
   [asr.lpython                        ]
   [clojure.pprint      :refer [pprint]]
   [clojure.walk        :as     walk   ]
   [clojure.spec.alpha  :as s]))


;;  _     _____     _______      _    ____  ____
;; | |   |_ _\ \   / / ____|    / \  / ___||  _ \
;; | |    | | \ \ / /|  _|     / _ \ \___ \| |_) |
;; | |___ | |  \ V / | |___   / ___ \ ___) |  _ <
;; |_____|___|  \_/  |_____| /_/   \_\____/|_| \_\


(def tuple-stuffs
  (filter #(= (:grup %) :ASDL-TUPLE) big-list-of-stuff))


#_tuple-stuffs
;; => ({:head :asr.autospecs/asr-tuple10800,
;;      :term :asr.autospecs/call_arg, ...
;;     {:head :asr.autospecs/asr-tuple10801,
;;      :term :asr.autospecs/do_loop_head, ...
;;     {:head :asr.autospecs/asr-tuple10802,
;;      :term :asr.autospecs/alloc_arg, ...
;;     {:head :asr.autospecs/asr-tuple10803,
;;      :term :asr.autospecs/attribute_arg, ...
;;     {:head :asr.autospecs/asr-tuple10804,
;;      :term :asr.autospecs/array_index, ...
;;     {:head :asr.autospecs/asr-tuple10805,
;;      :term :asr.autospecs/dimension, ...


(def symconst-stuffs
  (filter #(= (:grup %) :ASDL-SYMCONST) big-list-of-stuff))


(def composite-stuffs
  (filter #(= (:grup %) :ASDL-COMPOSITE) big-list-of-stuff))


;;  ___ _        ___               _ _    _     _           _  _             _
;; | _ |_)__ _  / __|_  _ _ __  __| (_)__| |_  | |__ _  _  | || |___ __ _ __| |
;; | _ \ / _` | \__ \ || | '  \/ _` | / _|  _| | '_ \ || | | __ / -_) _` / _` |
;; |___/_\__, | |___/\_, |_|_|_\__,_|_\__|\__| |_.__/\_, | |_||_\___\__,_\__,_|
;;       |___/       |__/                            |__/


(def big-symdict-by-head
  (let [heads (map :head big-list-of-stuff)
        ;; Rid namespaces and quote marks:
        syms  (map (comp symbol name) heads)]
    (zipmap syms big-list-of-stuff)))


#_(count big-symdict-by-head)
;; => 248


;;  _  _         _
;; | \| |___  __| |___
;; | .` / _ \/ _` / -_)
;; |_|\_\___/\__,_\___|


;;; Every alternative of a term is a _node_ or _speclet_ or
;;; ASDL-DEF(see grammar.clj).


;; -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
;;  E V A L   N O D E   F W D   R E F
;; -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-


(defn eval-node [node]
  (assert false "This eval-node should never be called."))


;; -+-+-+-+-
;;  M I S C
;; -+-+-+-+-


(defn eval-bool
  [bool]
  (fn [_]
    (case bool
      .true.  true
      .false. false)))


;;; TODO: consider moving penv parameter to the front of every
;;; eval function, as with `self` in OOP.


(defn eval-identifier
  "unspec'ced"
  [ident]
  (fn [penv]
    {:head ident
     :term 'identifier}))


(defn dump-global-keys
  "TODO: can race"
  []
  (let [glob @ΓΣ]
    (nkecho
     (map
      (fn [s] {s (keys (:φ @(glob s)))})
      (keys glob)))))


(defn dump-global-chains
  []
  (let [glob @ΓΣ]
    (nkecho
     (map
      (fn [s] {s (asr.environment/dump-penv-chain (glob s))})
      (keys glob)))))


(defn dump-penv-keys
  [penv]
  (let [env @penv]
    (echo (keys (:φ env)))))


(defn eval-bindings
  "Return a function of a penv, in which all bindings are evaluated.
  Supports lexical environments and closures. Can't be in
  namespace 'asr.environment' due to circular reference."
  [bindings]
  (fn [penv]
    (loop [result {}
           remaining bindings]
      (if (seq remaining)               ; idiom for not empty
        (let [[k v] (first remaining)]
          (recur (into result {k ((eval-node v) penv)})
                 (rest remaining)))
        result))))


(defn new-penv
  "A new penv has a frame φ and a penv π. Bindings are looked up in
  the old penv and bound in the new penv. A frequent case for this
  is binding actual arguments to function parameters."
  [bindings penv]
  (let [nu-bindings ((eval-bindings bindings) penv)]
   (atom {:φ nu-bindings, :π penv}
         :validator is-environment?)))


(defn augment-bindings-penv!
  "Add-to or update bindings in penv."
  [bindings penv]
  (assert (is-penv? penv))
  (let [oenv (:π @penv)]
    (swap!
     penv
     (fn [env]
       {:φ
        (into (:φ env)
              ((eval-bindings bindings) oenv))
        :π oenv}))))


(defn term-from-head
  "Summarize from 'big-symdict-by-head."
  [sym]
  (case sym
    SymbolTable 'symbol   ;; not spec'ced, but used frequently in ASR
    ForTest     'testing  ;; also not spec'ced, and not used in ASR
    (symbol ; strip quotes
     (name ; strip namespace
      (:term ; fetch
       (sym big-symdict-by-head))))))


;; -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
;;  E V A L S   F O R   T U P L E S
;; -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-


#_flat-tuple-terms-set
;; => #{attribute_arg   alloc_arg       do_loop_head
;;      call_arg        array_index     dimension


(defn eval-tuple
  [tup]
  (fn [penv]
    (assert false "eval-tuple is Not Yet Implemented.")
    (echo tup)))


;; -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
;;  E V A L S   F O R   S Y M C O N S T S
;; -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-


#_flat-symconst-terms-set
;;; => #{cmpop          arraystorage    deftype
;;      arraybound      storage_type    binop
;;      presence        integerboz      logicalbinop
;;      enumtype        abi             intent
;;      cast_kind       access


;; An important one:
;; binop = Add | Sub | Mul | Div | Pow
;;       | BitAnd | BitOr | BitXor | BitLShift | BitRShift


;; All symconsts can be handled the same way, in eval-node,
;; as there is no extra evaluation to do. Breaking them out
;; here is definitely over-engineering.


;; -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
;;  E V A L S   F O R   C O M P O S I T E S
;; -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-


#_flat-composite-terms-set
;; => #{tbind           attribute       restriction_arg
;;      unit            symbol          case_stmt
;;      type_stmt       expr            ttype
;;      stmt}


(defmulti eval-tbind           first)
(defmulti eval-attribute       first)
(defmulti eval-restriction-arg first)
(defmulti eval-unit            first) ; singleton, but, OK, for uniformity
(defmulti eval-symbol          first)
(defmulti eval-case-stmt       first)
(defmulti eval-type-stmt       first)
(defmulti eval-expr            first)
(defmulti eval-ttype           first)
(defmulti eval-stmt            first)


;; -+-+-+-+-+-+-+-
;;  P L U R A L S
;; -+-+-+-+-+-+-+-


(defn eval-many
  "sketch"
  [nodes evaluator]
  (fn [penv]
    (map (fn [node]
           ((evaluator node) penv))
         nodes)))


(defn eval-identifiers
  [idents]
  (eval-many idents eval-identifier))


(defn eval-nodes
  [nodes]
  (eval-many nodes eval-node))


(defn eval-stmts
  [stmts]
  (eval-many stmts eval-stmt))


(defn eval-symbols
  [syms]
  (eval-many syms eval-symbol))


(defn eval-exprs
  [exprs]
  (eval-many exprs eval-expr))


;;                  _
;;   _____   ____ _| |           _____  ___ __  _ __
;;  / _ \ \ / / _` | |  _____   / _ \ \/ / '_ \| '__|
;; |  __/\ V / (_| | | |_____| |  __/>  <| |_) | |
;;  \___| \_/ \__,_|_|          \___/_/\_\ .__/|_|
;;                                       |_|


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


(defmethod eval-expr nil
  [_]
  (throw (Exception. "catch me in the debugger: xzyzy plugh")))


;; = IfExp(expr test, expr body, expr orelse, ttype type, expr? value)
;; -- Such as: (x, y+z), (3.0, 2.0) generally not known at compile time


(defmethod eval-expr 'IfExp
  [[head
    test-
    body
    orelse
    type-
    value]]
  (fn [penv]
    {:head  head
     :term  (term-from-head head)

     :test   ((eval-expr  test-) penv)
     :body   ((eval-expr  body)  penv)
     :orelse ((eval-expr orelse) penv)
     :type   ((eval-ttype type-) penv)
     ;; Use eval-node on question-marked items.
     :value  ((eval-node  value) penv)
     }    ))


;; | StringConcat(expr left, expr right, ttype type, expr? value)


(defmethod eval-expr 'StringConcat
  [[head
    left
    right
    type-
    value]]
  (fn [penv]
    {:head  head
     :term  (term-from-head head)

     :left  ((eval-expr  left)  penv)
     :right ((eval-expr  right) penv)
     :type  ((eval-ttype type-) penv)
     ;; Use eval-node on question-marked items.
     :value ((eval-node  value) penv)
     }))


;; | IntegerCompare(expr left, cmpop op, expr right,
;;                  ttype type, expr? value)


(defmethod eval-expr 'IntegerCompare
  [[head
    left
    cmpop
    right
    type-
    value]]
  (fn [penv]
    {:head  head
     :term  (term-from-head head)

     :left  ((eval-expr  left)  penv)
     :cmpop ((eval-node  cmpop) penv)
     :right ((eval-expr  right) penv)
     :type  ((eval-ttype type-) penv)
     ;; Use eval-node on question-marked items.
     :value ((eval-node  value) penv)
     }))


;; | LogicalCompare(expr left, cmpop op, expr right,
;;                  ttype type, expr? value)


(defmethod eval-expr 'LogicalCompare
  [[head
    left
    cmpop
    right
    type-
    value]]
  (fn [penv]
    {:head  head
     :term  (term-from-head head)

     :left  ((eval-expr  left)  penv)
     :cmpop ((eval-node  cmpop) penv)
     :right ((eval-expr  right) penv)
     :type  ((eval-ttype type-) penv)
     ;; Use eval-node on question-marked items.
     :value ((eval-node  value) penv)
     }))


;; | LogicalBinOp(expr left, logicalbinop op, expr right,
;;                ttype type, expr? value)


(defmethod eval-expr 'LogicalBinOp
  [[head
    left
    logicalbinop
    right
    type-
    value]]
  (fn [penv]
    {:head  head
     :term  (term-from-head head)

     :left         ((eval-expr  left)         penv)
     :logicalbinop ((eval-node  logicalbinop) penv)
     :right        ((eval-expr  right)        penv)
     :type         ((eval-ttype type-)        penv)
     ;; Use eval-node on question-marked items.
     :value        ((eval-node  value)        penv)
     }))


;; | IntegerBinOp(expr left, binop op, expr right, ttype type, expr? value)


(defmethod eval-expr 'IntegerBinOp
  [[head
    left
    op
    right
    type-
    value]]
  (fn [penv]
    {:head  head
     :term  (term-from-head head)

     :left  ((eval-expr  left)  penv)
     :op    ((eval-node  op)    penv)
     :right ((eval-expr  right) penv)
     :type  ((eval-ttype type-) penv)
     ;; Use eval-node on question-marked items.
     :value ((eval-node  value) penv)
     }))


;; | LogicalConstant(bool value, ttype type)


(defmethod eval-expr 'LogicalConstant
  [[head
    value
    type-]]
  (fn [penv]
    {:head head
     :term  (term-from-head     head)

     :value ((eval-bool  value) penv)
     :type  ((eval-ttype type-) penv)}))


;; | IntegerConstant(int n, ttype type)


(defmethod eval-expr 'IntegerConstant
  [[head
    n
    type-]]
  (fn [penv]
    {:head head
     :term (term-from-head     head)

     :n    ((eval-node  n)     penv)
     :type ((eval-ttype type-) penv)}))


;; | Var(symbol v)
;; https://github.com/lcompilers/lpython/issues/1478
;; should be
;; | Var(symtab-id id, symbol v)


(defmethod eval-expr 'Var
  [[head
    id
    v]]
  (fn [penv]
    {:head      head
     :term      (term-from-head head)

     :symtab-id id
     ;; TODO chain the environments!
     :v         (lookup-penv v (@ΓΣ id))})) ; a Variable!


;; | NamedExpr(expr target, expr value, ttype type)


(defmethod eval-expr 'NamedExpr
  [[head
    target  ; expr
    value   ; expr
    type-   ; ttype
    ]]
  (fn [penv]
    {:head   head
     :term   (term-from-head head)

     :target ((eval-expr  target) penv)
     :value  ((eval-expr  value)  penv)
     :type   ((eval-ttype type-)  penv)}))


;; | StringOrd(expr arg, ttype type, expr? value)


(defmethod eval-expr 'StringOrd
  [[head
    arg
    type-
    value]]
  (fn [penv]
    {:head   head
     :term   (term-from-head head)

     :arg    ((eval-expr  arg)    penv)
     :type   ((eval-ttype type-)  penv)
     ;; Use "eval-node" for question-mark items.
     :value  ((eval-node  value)  penv)}))


;; | StringConstant(string s, ttype type)


(defmethod eval-expr 'StringConstant
  [[head
    s
    type-]]
  (fn [penv]
    {:head   head
     :term   (term-from-head head)

     :s      ((eval-node  s)      penv)
     :type   ((eval-ttype type-)  penv)}))


;;                  _                               _           _
;;   _____   ____ _| |          ___ _   _ _ __ ___ | |__   ___ | |
;;  / _ \ \ / / _` | |  _____  / __| | | | '_ ` _ \| '_ \ / _ \| |
;; |  __/\ V / (_| | | |_____| \__ \ |_| | | | | | | |_) | (_) | |
;;  \___| \_/ \__,_|_|         |___/\__, |_| |_| |_|_.__/ \___/|_|
;;                                  |___/


;;; For convenience, treat SymbolTable as if it were an ASR
;;; symbol. SymbolTable is not actually specified in ASR.


;; = Program(symbol_table symtab, identifier name,
;;           identifier* dependencies, stmt* body)


(defmethod eval-symbol 'Program
  [[head
    symtab
    nym
    dependencies
    body
    :as program]]
  (fn [penv]
    {:head         head          ; 'Program
     :term         (term-from-head head)

     :symtab       ((eval-symbol symtab)       penv)   ; 'SymbolTable
     :nym          ((eval-node   nym)          penv)   ; identifier
     :dependencies ((eval-node   dependencies) penv)   ; identifier *
     :body         ((eval-stmts  body) penv)           ; stmt *
     :penv         penv
     }))


(defmethod eval-symbol 'SymbolTable
  [[head
    integer-id
    bindings
    :as symbol-table]]
  (fn [penv]
    (let [np (new-penv bindings penv)
          ts {:head       head
              :term       (term-from-head head)

              :integer-id integer-id    ; int
              :bindings   bindings      ; dict
              :penv       np            ; Environment
              }]
      (plnecho integer-id)
      (swap! ΓΣ
             (fn [old]
               (into
                old
                {integer-id np})))
      (dump-global-chains)
      ts)))


(defmethod eval-symbol 'ForTest
  [[head
    datum]]
  (fn [penv]
    (if datum
      {:head  head,
       :term  (term-from-head head)
       :datum ((eval-node datum) penv)}
      {:head head})))


;; | Variable(symbol_table parent_symtab, identifier name,
;;   identifier* dependencies, intent intent,
;;   expr? symbolic_value, expr? value,
;;   storage_type storage, ttype type,
;;   abi abi, access access, presence presence, bool value_attr)


(defmethod eval-symbol 'Variable
  [[head
    parent-symtab-id  ; an integer
    nym               ; "name" shadows Clojure build-in.
    dependencies      ; "identifier" is not spec'ced;
    intent
    symbolic-value
    value
    storage
    type-                         ; "type" shadows Clojure built-in.
    abi
    access
    presence
    value-attr        ; bool (.true., .false.)
    :as variable]]
  (fn [penv]
    {:head           head
     :term           (term-from-head head)

     :symtab-id      parent-symtab-id
     :name           nym
     :dependencies   dependencies
     :intent         ((eval-node intent)         penv)
     :symbolic-value ((eval-node symbolic-value) penv)
     :value          ((eval-node value)          penv)
     :storage        ((eval-node storage)        penv)
     :type           ((eval-node type-)          penv)
     :abi            ((eval-node abi)            penv)
     :access         ((eval-node access)         penv)
     :presence       ((eval-node presence)       penv)
     :value-attr     ((eval-bool value-attr)     penv)
     }))


;; | Function(symbol_table symtab, identifier name,
;;            identifier* dependencies, expr* args, stmt* body,
;;            expr? return_var, abi abi, access access, deftype deftype,
;;            string? bindc_name, bool elemental, bool pure, bool module,
;;            bool inline, bool static, ttype* type_params,
;;            symbol* restrictions, bool is_restriction,
;;            bool deterministic, bool side_effect_free)


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
    deterministic              ; bool (.true., .false.)
    side-effect-free           ; bool (.true., .false.)
    :as function]]
  (fn [penv]
    (println (f-str "Setting up Function {nym}"))
    {:head             head
     :term             (term-from-head head)
     :symtab           ((eval-symbol  symtab)           penv)
     :name             ((eval-node    nym)              penv)
     :dependencies     ((eval-nodes   dependencies)     penv)
     :args             ((eval-nodes   args)             penv)  ; TODO: params!
     ;; Replace "identity" with "echo" or "doall" to force faults
     ;; that depend on lazy evaluation (grep "lazy" and "laziness"
     ;; in this file for relevant commentary). Forcing evaluation
     ;; here is useful during development to expose missing
     ;; defmethods.
     :body             (identity
                        #_echo
                        ((eval-nodes  body)             penv))
     :return-var       ((eval-node    return-var)       penv)
     :abi              ((eval-node    abi)              penv)
     :access           ((eval-node    access)           penv)
     :deftype          ((eval-node    deftype)          penv)
     :bindc-name       ((eval-node    bindc-name)       penv)
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


;;                  _           _   _
;;   _____   ____ _| |         | |_| |_ _   _ _ __   ___
;;  / _ \ \ / / _` | |  _____  | __| __| | | | '_ \ / _ \
;; |  __/\ V / (_| | | |_____| | |_| |_| |_| | |_) |  __/
;;  \___| \_/ \__,_|_|          \__|\__|\__, | .__/ \___|
;;                                      |___/|_|


;;| Logical(int kind, dimension* dims)


(defn common-ttype
  [head kind dims]
  (fn [penv]
    {:head head
     :term (term-from-head head)

     :kind ((eval-node  kind) penv)
     :dims ((eval-nodes dims) penv)}))


(defmethod eval-ttype 'Logical
  [[head
    kind
    dims]]
  (common-ttype head kind dims))


;; = Integer(int kind, dimension* dims)


(defmethod eval-ttype 'Integer
  [[head  ;
    kind  ; Integer kinds: 1 (i8), 2 (i16), 4 (i32), 8 (i64)
    dims  ; dimension *
    ]]
  (fn [penv]
    {:head head
     :term (term-from-head head)

     :kind ((eval-node  kind) penv)
     :dims ((eval-nodes dims) penv)}))


;; | Character(int kind, int len, expr? len_expr, dimension* dims)


(defmethod eval-ttype 'Character
  [[head
    kind      ; int
    len       ; int
    len-expr  ; expr?
    dims      ; dimension *
    ]]
  (fn [penv]
    {:head head
     :term (term-from-head head)

     :kind     ((eval-node  kind)     penv)
     :len      ((eval-node  len)      penv)
     ;; Use "eval-node" for question-marked items.
     :len-expr ((eval-node  len-expr) penv)
     :dims     ((eval-nodes dims)     penv)}))


;;                  _               _             _
;;   _____   ____ _| |          ___| |_ _ __ ___ | |_
;;  / _ \ \ / / _` | |  _____  / __| __| '_ ` _ \| __|
;; |  __/\ V / (_| | | |_____| \__ \ |_| | | | | | |_
;;  \___| \_/ \__,_|_|         |___/\__|_| |_| |_|\__|


;; => 44
;; Allocate            Assign              Assignment          Associate
;; Cycle               ExplicitDeallocate  ImplicitDeallocate  DoConcurrentLoop
;; DoLoop              ErrorStop           Exit                ForAllSingle
;; GoTo                GoToTarget          If                  IfArithmetic
;; Print               FileOpen            FileClose           FileRead
;; FileBackspace       FileRewind          FileInquire         FileWrite
;; Return              Select              Stop                Assert
;; SubroutineCall      Where               WhileLoop           Nullify
;; Flush               ListAppend          AssociateBlockCall  SelectType
;; CPtrToPointer       BlockCall           SetInsert           SetRemove
;; ListInsert          ListRemove          ListClear           DictInsert


;; | ExplicitDeallocate(symbol* vars)
;; https://github.com/lcompilers/lpython/issues/1492
;; vars is not symbol*, but symref*. Until issue 1492
;; is resolved, I'll fake symref.


(defn eval-symrefs
  [symrefs]  ; something like [2 x, 2 y, ...]
  (assert (even? (count symrefs)))
  (fn [penv]
    (vec (partition 2 symrefs))))


(defmethod eval-stmt 'ExplicitDeallocate
  [[head
    vars]]
  (fn [penv]
    {:head head
     :term (term-from-head head)

     :vars ((eval-symrefs vars) penv)
     ;; Issue #1492 :vars ((eval-symbols vars) penv)
     }))


;; | WhileLoop(expr test, stmt* body)


(defmethod eval-stmt 'WhileLoop
  [[head
    test-
    body
    orelse]]
  (fn [penv]
    {:head head
     :term (term-from-head head)

     :test ((eval-expr  test-)  penv)
     :body ((eval-stmts body)   penv)
     }))


;; | If(expr test, stmt* body, stmt* orelse)


(defmethod eval-stmt 'If
  [[head
    test-
    body
    orelse]]
  (fn [penv]
    {:head   head
     :term   (term-from-head head)

     :test   ((eval-expr  test-)  penv)
     :body   ((eval-stmts body)   penv)
     :orelse ((eval-stmts orelse) penv)
     }))


;; | Assignment(expr target, expr value, stmt? overloaded)


(defmethod eval-stmt 'Assignment
  [[head
    target      ; expr: often, even usually a Variable
    value       ; expr
    overloaded  ; stmt ? -- absence is an empty list
    :as assignment
    ]]
  (fn [penv]
    {;; Every eval'ed speclet has :head and :term:
     :head       head
     :term       (term-from-head head)

     :target     ((eval-expr target)     penv)  ; TODO: a Var or a Variable?
     ;; The rest of this varies from speclet to speclet.
     :value      ((eval-expr value)      penv)
     ;; Use "eval-node" for ? multiplicities. Otherwise, use the
     ;; most specific "eval-<whatever>" you can read from the
     ;; ASR: "eval-expr" if you know it's an expr;
     ;; "eval-stmt" if you know it's a stmt, etc.
     :overloaded ((eval-node overloaded) penv)
     }))


;; Print(expr? fmt, expr* values, expr? separator, expr? end)


(defmethod eval-stmt 'Print
  [[head
    fmt        ; expr ?
    values     ; expr *
    separator  ; expr ?
    end        ; expr ?
    :as print]]
  (fn [penv]
    {:head      head
     :term      (term-from-head head)
     ;; Use "eval-node" for ? multiplicities.
     :fmt       ((eval-node  fmt)       penv)
     :values    ((eval-exprs values)    penv)
     :separator ((eval-node  separator) penv)
     :end       ((eval-node  end)       penv)
     }))


;; https://github.com/lcompilers/lpython/issues/1479
;; | SubroutineCall(symtab-id id, symbol name, symbol? original_name,
;;                  call_arg* args, expr? dt)

;; examples:

;; (SubroutineCall 1 main0                 () [] ())
;; (SubroutineCall 1 _lpython_main_program () [] ())


(defmethod eval-stmt 'SubroutineCall
  [[head                             ; 'SubroutineCall
    symtab-id                        ; integer
    nym                              ; e.g., _lpython_main_program
    original-name                    ; nil, GenericProcedure or ExternalSymbol
    arguments                        ;
    dt                               ; expr ?
    :as subroutine-call]]
  (fn [penv]
    (let [symtable  (@ΓΣ symtab-id)
          sub       (lookup-penv nym symtable)]
      ;; This works only because of laziness. ΓΣ is populated
      ;; before these lookups occur. Without laziness, the "sub"
      ;; would not be found at the right time and this would
      ;; throw (try "doall" to force the "body" evaluation
      ;; in "Function" to test that claim).
      (when (not sub)
        (throw (Exception. (f-str "Error: Subroutine {nym} not found"))))
      (println (f-str"Setting up subroutine call {nym} with {arguments}"))
      {:head           head
       :term           (term-from-head head)

       ;; TODO: Look up parameters, here?
       :symtab-id      symtab-id
       :nym            nym ;; Function and Variable have :name
       :original-name  ((eval-node  original-name) penv)
       :arguments      ((eval-nodes arguments)     penv)
       :dt             ((eval-node  dt)            penv)
       :subroutine     sub
       })))


;;                  _                           _
;;   _____   ____ _| |          _ __   ___   __| | ___
;;  / _ \ \ / / _` | |  _____  | '_ \ / _ \ / _` |/ _ \
;; |  __/\ V / (_| | | |_____| | | | | (_) | (_| |  __/
;;  \___| \_/ \__,_|_|         |_| |_|\___/ \__,_|\___|


;; TODO: get rid of this extra level of dispatching


(defmulti eval-composite
  (fn [head _node _penv]
    (term-from-head head)))


(defmethod eval-composite 'unit   [_ node penv] ((eval-unit   node) penv))
(defmethod eval-composite 'symbol [_ node penv] ((eval-symbol node) penv))
(defmethod eval-composite 'expr   [_ node penv] ((eval-expr   node) penv))
(defmethod eval-composite 'stmt   [_ node penv] ((eval-stmt   node) penv))
(defmethod eval-composite 'ttype  [_ node penv] ((eval-ttype  node) penv))


(defn eval-node
  "sketch"
  [node]
  (fn [penv]

    (cond       ; order matters ...
      ;;------------------------------------------------
      (symbol? node)         ; then
      ;; Handle all symconsts the same way:
      (cond
        (symconst? node)
        {:head node,
         :term (term-from-head node)}

        (tuple? node)
        (assert false "Not Yet Implemented: asr-tuples") ; TODO

        :else
        ((eval-identifier node) penv))
      ;;------------------------------------------------
      ;; e.g., empty calls like () or empty vectors like []
      ;; Without this test here, things get turned into "nil."
      (and (coll? node) (empty? node))  ; then
      node
      ;;------------------------------------------------
      ;; e.g., [(SubroutineCall ...) ...]
      (vector? node)
      (for [n node] ((eval-node n) penv))
      ;;------------------------------------------------
      ;; looks like a lisp-call with a head in the first slot
      (list? node)                      ; then
      (let [head (first node)]

        (cond

          (or (= head 'SymbolTable) (= head 'ForTest))
          ((eval-symbol node) penv) ; SymbolTable is an unspec'ced symbol

          (composite? head)
          (eval-composite head node penv)

          (symconst? head)
          (assert false (f-str "Shouldn't have the symconst {head} here."))

          (tuple? head)
          ((eval-tuple node) penv)

          :else
          (assert false (f-str "unclassified head {head} here"))
          ))
      ;;------------------------------------------------
      ;; strings, identifiers, etc.; otherwise unclassified nodes
      :else
      (identity node)
      ;;------------------------------------------------
      )))


;;                        _ _    _
;;  _ _ _  _ _ _    ___  | | |__(_)_ _  ___ _ __
;; | '_| || | ' \  |___| | | '_ \ | ' \/ _ \ '_ \
;; |_|  \_,_|_||_|       |_|_.__/_|_||_\___/ .__/
;;                                         |_|


(defmulti run-lbinop
  (fn [op _l _r] (:head op)))


;;                        _ _    _
;;  _ _ _  _ _ _    ___  (_) |__(_)_ _  ___ _ __
;; | '_| || | ' \  |___| | | '_ \ | ' \/ _ \ '_ \
;; |_|  \_,_|_||_|       |_|_.__/_|_||_\___/ .__/
;;                                         |_|


(defmulti run-ibinop
  (fn [op _l _r] (:head op)))


(defmethod run-ibinop 'Mul [_ l r] (* l r))
(defmethod run-ibinop 'Add [_ l r] (+ l r))


;;  _ _ _  _ _ _    ___   _____ ___ __ _ _
;; | '_| || | ' \  |___| / -_) \ / '_ \ '_|
;; |_|  \_,_|_||_|       \___/_\_\ .__/_|
;;                               |_|


(defmulti run-expr :head)


;; When 'value' is nil


(defmethod run-expr nil
  [_]
  nil)


;; "If there is a 'value' attribute, compare it to the result of
;; the abstract execution. The 'value' attribute is a compile-time
;; constant, meaning that the compiler could compute the answer."


(defmethod run-expr 'IntegerBinOp
  [e]
  (fn [penv]
    (let [r-left  ((run-expr (:left  e)) penv)
          r-right ((run-expr (:right e)) penv)
          result  (run-ibinop (:op e) r-left r-right)
          value   ((run-expr (:value e)) penv)]
      ;; (pprint (f-str "IntegerBinOp result: {result}"))
      (when value
        (assert (= value result)))
      result)))


(defmethod run-expr 'IntegerConstant
  [e]
  (fn [penv]
    (let [result (:n e)]
      ;; (pprint (f-str "IntegerConstant result: {result}"))
      result)))


;;                           _        _
;;  _ _ _  _ _ _    ___   __| |_ _ __| |_
;; | '_| || | ' \  |___| (_-<  _| '  \  _|
;; |_|  \_,_|_||_|       /__/\__|_|_|_\__|


(defmulti run-stmt :head)


(defmethod run-stmt 'SubroutineCall
  [s]
  (fn [penv]
    (let [nym  (:nym s)
          stid (:symtab-id s) ;; local variables

          ;; TODO: We've lost the nested chains of envrt's
          ;; wherein to look up free variables. We need to
          ;; restore that nesting.

          stab (@ΓΣ stid)

          ;; Look up parameters; evaluate actual args in penv;
          ;; zipmap them to parameters; eval bindings in a new
          ;; penv.

          body (identity (:body (:subroutine s)))
          ]
      ;; TODO:                         |newpenv|
      ;;                                ---v---
      (doall (for [s body] ((run-stmt s) penv)))
      )))


(defmethod run-stmt 'Assignment
  [s]
  (fn [penv]
    (let [target   (:target s)
          tgtnym   (:name (:v target))
          stid     (:symtab-id target)
          stab     (@ΓΣ stid)
          prelkup  (lookup-penv tgtnym stab)
          presumm  {:name (:name prelkup), :value (:value prelkup)}
          value    ((run-expr (:value s)) penv)
          post     (assoc prelkup :value value)
          augbdg   {(keyword tgtnym) post}
          augres   (augment-bindings-penv! augbdg stab)
          ustab    (@ΓΣ stid)
          postlkup (lookup-penv tgtnym ustab)
          postsumm {:name (:name postlkup), :value (:value postlkup)}]

      (pprint (f-str "running Assignment"))
      (pprint (f-str "   tgtnym:   {tgtnym}"))
      (pprint (f-str "   value:    {value}"))
      (pprint (f-str "   stid:     {stid}"))
      (pprint (f-str "   presumm:  {presumm}"))
      (pprint (f-str "   postsumm: {postsumm}"))
      )))


(defn print-expr
  [e]
  (let [nym      (:name (:v e))
        stid     (:symtab-id e)
        stidtest (:symtab-id (:v e))
        stab     (@ΓΣ stid)
        variable (lookup-penv nym stab)
        value    (:value variable)]
    (assert
     (= stid stidtest)
     (f-str "Var {nym} has symtab-id {stid} but
             Variable has symtab-id {stidtest}"))
    (pprint {(keyword nym) value})))


(defmethod run-stmt 'Print
  [s]
  (fn [penv]
    (let [values (:values s)]
      (pprint (f-str "running Print"))
      (dorun (map print-expr values)))))


;;  _ _ _  _ _ _    ___   _ __ _ _ ___  __ _ _ _ __ _ _ __
;; | '_| || | ' \  |___| | '_ \ '_/ _ \/ _` | '_/ _` | '  \
;; |_|  \_,_|_||_|       | .__/_| \___/\__, |_| \__,_|_|_|_|
;;                       |_|           |___/


(def SUCCESS 0)


(defn run-program
  [e]
  (fn [penv]
    (let [code (:body e)
          ev ((eval-node code) penv)]
      ;; could have expressions here, too
      (doall (for [s ev] ((run-stmt s) penv)))
      SUCCESS
      )))


;;                  _                       _ _
;;   _____   ____ _| |          _   _ _ __ (_) |_
;;  / _ \ \ / / _` | |  _____  | | | | '_ \| | __|
;; |  __/\ V / (_| | | |_____| | |_| | | | | | |_
;;  \___| \_/ \__,_|_|          \__,_|_| |_|_|\__|


;; -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
;;  e v a l   u n i t   ( c o m p o s i t e )
;; -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
;; unit is a composite like expr or stmt.


;; unit
;; = TranslationUnit(symbol_table global_scope, node* items)


(defmethod eval-unit 'TranslationUnit
  [[head
    global-scope
    items
    :as translation-unit]]
  (assert (= 'TranslationUnit head)
          (f-str "head of a translation unit must be the symbol
                  TranslationUnit, not {head}"))
  (init-global-environments)
  (fn [penv]
    #_(assert (s/valid? :asr.autospecs/TranslationUnit translation-unit))
    (let [tu {:head         head
              :term         (term-from-head head)
              :global-scope ((eval-symbol global-scope) penv)
              :items        ((eval-nodes items) penv)}
          main-prog (lookup-penv
                     'main_program
                     (:penv (:global-scope tu)))]
      tu
      (when main-prog
        (println "Statement heads in main program (observe laziness):")
        (doseq [s (:body main-prog)] (nkecho (:head s)))
        ((run-program main-prog) penv))
      )))
