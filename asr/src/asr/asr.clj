(ns asr.asr
  (:use [asr.utils]
        [clojure.set])
  (:require
   [asr.grammar                   ]
   [asr.lpython                   ]
   [clojure.pprint :refer [pprint]]
   [asr.parsed     :refer [shallow-map-from-speclet]]
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
  "Classic recursion; TODO: consider tail-recursion with loop and
  recur."
  [sym penv]
  (assert (or (nil? penv) (is-penv? penv)) "Invalid penv")
  (assert (instance? clojure.lang.Symbol sym) "Invalid symbol")
  (cond
    (nil? penv) nil
    true (let [r ((:φ @penv) (keyword sym))]
           (cond
             (nil? r) (lookup-penv sym (:π @penv))
             true r))))


;;  ____  _____    _    ____       _    ____  ____
;; |  _ \| ____|  / \  |  _ \     / \  / ___||  _ \
;; | |_) |  _|   / _ \ | | | |   / _ \ \___ \| |_) |
;; |  _ <| |___ / ___ \| |_| |  / ___ \ ___) |  _ <
;; |_| \_\_____/_/   \_\____/  /_/   \_\____/|_| \_\


;;; Parse the current ASR.ASDL, not the snapshot we have
;;; hard-coded.


(def asr-asdl-hiccup
  (asr.grammar/asdl-parser asr.lpython/asr-asdl))


;;; Some of the following gadgets collide with "parsed.clj," which
;;; works on the snapshot data in "asr_snapshot.clj".


(def speclets
  (vec (rest
        ((-> (zip/vector-zip asr-asdl-hiccup)
             zip/down zip/right zip/right) 0))))





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


;;  ___            _         _
;; / __|_  _ _ __ | |__  ___| |
;; \__ \ || | '  \| '_ \/ _ \ |
;; |___/\_, |_|_|_|_.__/\___/_|
;;      |__/


;;; docstrings apparently not allowed in defmethod


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
           :env          @penv         ; Environment
           })))

(defmethod eval-symbol 'SymbolTable
  [[head
    integer-id
    bindings
    :as symbol-table]]
  (fn [penv]
    (echo {:head       head
           :integer-id integer-id                  ; int
           :bindings   bindings                    ; dict
           :env        @(new-penv bindings penv)}) ; Environment
    ))

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


(defmethod eval-symbol 'SubroutineCall
  [[head
    symbol-table
    nym
    arguments
    dependencies
    call-type
    :as subroutine-call]]
  (fn [penv]
    (let [symtab ((eval-symbol symbol-table) penv)
          args   (map (fn [arg] ((eval-node arg) penv)) arguments)
          sub    (symtab nym)]
      (if (not sub)
        (throw (Exception. (str "Subroutine " nym " not found")))
        (do (echo {:head           head
                   :symtab         symtab
                   :name           nym
                   :arguments      args
                   :dependencies   dependencies
                   :call-type      ((eval-node call-type) penv)
                   :subroutine     ((eval-symbol sub) penv)})
            #_(run-subroutine sub args penv))))))


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
          "head of a translation unit must be the symbol TranslationUnit")
  (fn [penv]
    (echo {:head         head
           :global-scope ((eval-symbol global-scope) penv)
                                        ; TODO: eval-symbol-table?
           :items        ((eval-nodes items) penv)})))



;;  ____  _   _ ____  ____  _____ _   _ ____  _____ ____
;; / ___|| | | / ___||  _ \| ____| \ | |  _ \| ____|  _ \
;; \___ \| | | \___ \| |_) |  _| |  \| | | | |  _| | | | |
;;  ___) | |_| |___) |  __/| |___| |\  | |_| | |___| |_| |
;; |____/ \___/|____/|_|   |_____|_| \_|____/|_____|____/
;;  ____  _______     _______ _     ___  ____  __  __ _____ _   _ _____
;; |  _ \| ____\ \   / / ____| |   / _ \|  _ \|  \/  | ____| \ | |_   _|
;; | | | |  _|  \ \ / /|  _| | |  | | | | |_) | |\/| |  _| |  \| | | |
;; | |_| | |___  \ V / | |___| |__| |_| |  __/| |  | | |___| |\  | | |
;; |____/|_____|  \_/  |_____|_____\___/|_|   |_|  |_|_____|_| \_| |_|


;;              _ _                        _        _
;;  __ _ ___ __| | |  ____ _  __ _ _ __ __| |_  ___| |_
;; / _` (_-</ _` | | (_-< ' \/ _` | '_ (_-< ' \/ _ \  _|
;; \__,_/__/\__,_|_| /__/_||_\__,_| .__/__/_||_\___/\__|
;;                                |_|


;;; An immutable scrape from lcompilers web site, to have stable
;;; inputs for development. TODO: dynamically scrape this.
