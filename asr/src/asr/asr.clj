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


(def composite-stuffs
  (filter #(= (:grup %) :ASDL-COMPOSITE) big-list-of-stuff))


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


(defmulti eval-symbol first)            ; forward reference


(defmulti eval-expr first)


(defmulti eval-stmt first)


(defmethod eval-stmt 'SubroutineCall
  [node]
  (quote node))


(defmethod eval-stmt 'Assignment
  [node]
  (quote node))


(defmethod eval-stmt 'Print
  [node]
  (quote node))


(defmulti eval-tuple first)


(defmulti eval-ttype first)


(defmethod eval-ttype 'Integer
  [node]
  (quote node))


(defmulti eval-unit first)


(defn eval-node
  "sketch"
  [node]
  (fn [penv]

    (cond       ; order matters ...
      ;;------------------------------------------------
      (symbol? node)                    ; then
      (cond
        (node asr.groupings/flat-symconst-heads-set)
        node                            ; TODO

        (node asr.groupings/flat-tuple-terms-set)
        (assert false "Not Yet Implemented: asr-tuples") ; TODO

        :else
        node
        )
      ;;------------------------------------------------
      (and (coll? node)
           (empty? node))               ; then
      node
      ;;------------------------------------------------
      (list? node)                      ; then
      (let [head (first node)
            com (head asr.groupings/flat-composite-heads-set)
            con (head asr.groupings/flat-symconst-heads-set)
            tup (head asr.groupings/flat-tuple-terms-set)
            smt (= head 'SymbolTable)]
        (cond

          smt
          ((eval-symbol node) penv) ; SymbolTable is an unspec'ced symbol

          com
          (let [com- (term-from-head-sym com)]
            (case com-
              unit   ((eval-unit   node) penv)
              symbol ((eval-symbol node) penv)
              expr   ((eval-expr   node) penv)
              stmt   ((eval-stmt   node) penv)
              ttype  ((eval-ttype  node) penv)
              (assert
               false
               (f-str
                "Not Yet Implemented: composite case {com-}"))
              ))

          con
          (assert false (f-str "Shouldn't have the symconst {con} here."))

          tup
          ((eval-tuple tup) penv)

          :else
          (assert false (f-str "unclassified head {head} here"))
          ))
      ;; (let [stuff ((first node) big-symdict-by-head)]
      ;;   ((eval-stuff stuff) penv))
      ;;------------------------------------------------
      :else
      (identity node)
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
  (let [nu-bindings ((eval-bindings bindings) penv)]
   (atom {:φ nu-bindings, :π penv}
         :validator is-environment?)))


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


(defn dump-global-keys
  "TODO: can race"
  []
  (let [glob @ΓΣ]
    (nkecho
     (map
      (fn [s] {s (keys (:φ @(glob s)))})
      (keys glob)))))


(defn dump-penv-keys
  [penv]
  (let [env @penv]
    (echo (keys (:φ env)))))


(defmethod eval-symbol 'SymbolTable
  [[head
    integer-id
    bindings
    :as symbol-table]]
  (fn [penv]
    (let [np (new-penv bindings penv)
          ts {:head       head
              :term       (term-from-head-sym head)
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
      (dump-global-keys)
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


(defmethod eval-unit 'TranslationUnit
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
              :global-scope ((eval-node  global-scope) penv)
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


; @ΓΣ


;;; The "global scope" or "global symbol registry" ΓΣ is really
;;; one below the unique global environment ΓΠ. ΓΣ contains
;;; user-defined symbols. ΓΠ contains built-ins.
