(ns asr-ast.core
  (:gen-class)
  (:require [clojure.spec.alpha            :as s]
            [clojure.pprint                :refer [pprint]]
            [instaparse.core               :as insta]
            [clojure.zip                   :as zip]
            [camel-snake-kebab.core        :as csk]
            [clojure.spec.gen.alpha        :as gen]
            [clojure.spec.test.alpha       :as stest]
            [clojure.test.check.generators :as tgen]
            [clojure.math.numeric-tower    :refer [expt]]
            [clojure.inspector             :refer [inspect-tree]]
            ))


(defn echo [x]
  (pprint x) x)

;;    _   ___ ___     __      _   ___ _____
;;   /_\ / __| _ \  __\ \    /_\ / __|_   _|
;;  / _ \\__ \   / |___> >  / _ \\__ \ | |
;; /_/ \_\___/_|_\    /_/  /_/ \_\___/ |_|


;; ### `asr-s-exp`: Top-Level Dispatch

;; Let's call `s-exp`s the top-level productions in [this
;; specification of
;; ASR](https://github.com/lcompilers/libasr/blob/main/src/libasr/ASR.asdl).
;; We'll want a function, `asr-s-exp`, that can parse any such
;; `s-exp`.

;; To get started, we're concerned with two `s-exp`s:
;; - expr
;; - ttype

;; Of the many alternatives for `expr` and `ttype`, we're
;; concerned with the following to get started:

;; <!-- #raw -->
;; expr = (IntegerBinOp ...)
;; | (IntegerConstant ...)

;; ttype = (Integer ...)
;; <!-- #endraw -->

;; Luckily, these alternatives can be distinguished by their
;; heads, retrieved via Clojure's `first` built-in:

(defn asr-expr  [node] ,,,)
(defn asr-ttype [node] ,,,)

(defn asr-s-exp
  [node]
  (case (-> node first #_echo)
    IntegerBinOp    (-> node asr-expr)
    IntegerConstant (-> node asr-expr)
    Integer         (-> node asr-ttype)
    #_"Add more here."))


;; ### `asr-expr`: Sub-Case

;; This is just a mid-level dispatcher. It exists to mimic the ASR
;; spec. Automatically generated parsers will certainly have this
;; structure.

(defn asr-expr-integer-bin-op   [node] ,,,)
(defn asr-expr-integer-constant [node] ,,,)

(defn asr-expr
  [node]
  (case (-> node first)
    IntegerBinOp    (-> node asr-expr-integer-bin-op)
    IntegerConstant (-> node asr-expr-integer-constant)
    #_"Add more here."))


;; ### `asr-ttype`: Sub-Case

;; another obvious mid-level dispatcher.

(defn asr-ttype-integer [node] ,,,)

(defn asr-ttype
  [node]
  (case (-> node first)
    Integer (-> node asr-ttype-integer)
    #_"Add more here."))

;; TODO: Generate the parsers from the ASDL grammar for AST, to
;; future-proof the parsing.

;; TODO: the transformation to AST is hard-coded. We will want
;; other transformations. Abstract over the transformations,
;; making them pluggable at the leaf-level of the parser.


;; ### Translations for Informal Kinds

;; Because `kind` is informally specified in ASR (there is no
;; production for `kind` on the left-hand side of a specification
;; in ASDL), we'll write the translation without a
;; recursive-descent parser for the `kind` node.

(def asr->ast-kind-map
  {4 "i32", 1 "i8", 2 "i16", 8 "i64"})


;; ### Translation of `ttype-integer`

(defn asr-ttype-integer
  "Example: (Integer 4 []) ~~~> \"i32\""
  [node]
  (let [[signum, kind, & dims]
        node]                           ; destructuring
    (assert (= signum 'Integer))
    (assert (not (empty? dims)))
    (assert (#{1 2 4 8} kind))
    (-> kind asr->ast-kind-map)
    ;; TODO: dimensions
    ))


;; ### Translation of `expr-integer-constant`

(defn asr-expr-integer-constant
  "Example: (IntegerConstant 42 (Integer 4 [])) ~~~>
    (ConstantInt 42 \"i32\")"
  [node]
  (let [[signum, value, ttype] node]    ; destructuring
    (assert (= signum 'IntegerConstant))
    (assert (int? value))
    (list 'ConstantInt
          value
          (-> ttype asr-ttype))))


;; ### Translation of `binop`

;; Note that ASR `Mul` maps to AST `Mult`

(def asr->ast-binop-map
  {'Add 'Add,
   'Mul, 'Mult,
   'Sub 'Sub,
   'Div 'Div,
   #_"Add more here."})

(defn asr-binop
  [node]
  (-> node asr->ast-binop-map))


;; ### Translation of `expr-integer-bin-op`

(defn asr-expr-integer-bin-op
  "Example: (IntegerBinOp
     (IntegerConstant 2 (Integer 4 []))  ; left
     Add                                 ; binop
     (IntegerConstant 3 (Integer 4 []))  ; right
     (Integer 4 [])                      ; ttype
     (IntegerConstant 5 (Integer 4 []))) ; value?
     ~~~>
     (with-meta (BinOp (ConstantInt 2 \"i32\")
                   Add (ConstantInt 3 \"i32\")))
                {:result-type \"i32\",
                 :value (ConstantInt 5 \"i32\")}"
  [node]
  (let [,[signum, left, binop, right, ttype, & value?] node
        ,result-ttype {:result-ttype (-> ttype asr-ttype)}]
    ;; TODO: consider a signum hashmap.
    (assert (= signum 'IntegerBinOp))
    (with-meta (list 'BinOp
                     (-> left  asr-expr)
                     (-> binop asr-binop)
                     (-> right asr-expr))
      (if value?
        (merge result-ttype
               {:value (-> value? first asr-expr)})
        result-ttype))))


;;    _   ___ _____     __      _   ___ ___
;;   /_\ / __|_   _|  __\ \    /_\ / __| _ \
;;  / _ \\__ \ | |   |___> >  / _ \\__ \   /
;; /_/ \_\___/ |_|      /_/  /_/ \_\___/_|_\

;; ## Grandma's Way, Again

;; If you understood ASR $\rightarrow$ AST, then the following
;; needs little explanation. Every production in the AST grammar
;; corresponds to a function with a name in kebab-case according
;; to [our naming convention](#naming-convention). To get started,
;; it's limited to our motivating example and recursive extensions
;; of it. [The normative, ruling referece grammar is
;; here](https://github.com/lcompilers/lpython/blob/fabf0ec00353acaf25af14c4b76371eae46f5847/grammar/Python.asdl).


;; According to [our naming convention](#naming-convention), AST's
;; `BinOp` becomes `bin-op`, whereas ASR's `binop` stays `binop`.


;; ### AST Binop Map

(require '[clojure.set :as set])
(def ast->asr-bin-op-map
  (-> (set/map-invert asr->ast-binop-map) #_echo))


;; ### AST Type Map

(def ast->asr-ttype-map
  {"i32" '(Integer 4 []), "i8"  '(Integer 1 []),
   "i16" '(Integer 2 []), "i64" '(Integer 8 [])})


;; ### AST BinOp

(defn ast-bin-op
  [node]
  (-> node ast->asr-bin-op-map))


;; ### AST Signum Map

(def ast->asr-signum-map
  {'ConstantInt 'IntegerConstant,
   'BinOp       'IntegerBinOp
   #_"Add more here."})


;; ### AST Expr: BinOp

(defn ast-expr [node] ,,,)

(defn ast-expr-bin-op
  [node]
  (let [,[signum, left, bin-op, right] node
        ,{:keys [result-ttype value]} (-> node meta #_echo)
        ,prefix
        (list
         (-> signum       ast->asr-signum-map)
         (-> left         ast-expr)
         (-> bin-op       ast-bin-op)
         (-> right        ast-expr)
         (-> result-ttype ast->asr-ttype-map))]
    (-> (if value
          (concat prefix [(-> value ast-expr)])
          prefix)
        #_echo)))


;; ### AST ConstantInt

(defn ast-expr-constant-int
  [node]
  (let [[signum, value, ast-type] node]
    (assert (int? value))
    (list
     (-> signum ast->asr-signum-map)
     value
     (-> ast-type ast->asr-ttype-map))))


;; ### AST Expr

(defn ast-expr
  [node]
  (case (-> node first)
    BinOp       (-> node ast-expr-bin-op)
    ConstantInt (-> node ast-expr-constant-int)
    #_"Add more cases here"))


;; ### AST S-Exp

(defn ast-s-exp
  [node]
  (case (-> node first)
    BinOp       (-> node ast-expr)
    ConstantInt (-> node ast-expr)
    #_"Add more cases here"))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  (println (= 42 (* 6 7))))
