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
