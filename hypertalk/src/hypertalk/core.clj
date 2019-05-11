(ns hypertalk.core
  (:require [clojure.spec      :as s]
            [instaparse.core   :as ip]
            [clojure.pprint    :as pp])
  (:gen-class))

;;; Reference: HyperTalk: The Language for the Rest of Us
;;; Kyle Wheeler, 2004
;;; http://www.memoryhole.net/~kyle/newwebsite/Curriculum_Vitae_files/hypertalk.pdf

(def hypertalk-grammar-1
  "(* The first line is the master non-terminal. *)
  COMMAND          = PUT | GET

  <SP>             = <#'\\s'>

  INUM             = #'[0-9]+'

  <IDENTIFIER>     = #'[A-Za-z_][A-Za-z0-9_\\.]*'
  VARIABLE         = IDENTIFIER

  CONTAINER        = SIMPLE_CONTAINER
  SIMPLE_CONTAINER = VARIABLE

  PUT     = <'put'> SP+ EXPR SP+ <'into'> SP+ CONTAINER
  GET     = <'get'> SP+ EXPR

  EXPR    = INUM | CONTAINER
  ")

(def hypertalk-parser (ip/parser hypertalk-grammar-1))

(def symtab (atom {}))

(defn hypertalk-intern [sym]
  (or (@symtab sym)
      ((swap! symtab into [[sym (atom 0)]]) ; in the new symtable...
       sym))) ; lookup this symbol-string

(defn compile-command [cmd]
  (condp = (first cmd)
    ;; TODO: use clojure.spec to validate the parse
    ;; TODO: use enlive to build map-trees intead of hiccup vector trees
    ;; TODO: use specter to navigate the structure
    :PUT (let [the-num (-> cmd second second second Integer/parseInt)
               the-sym (-> cmd (nth 2) second second second)
               the-atom (hypertalk-intern the-sym)]
           ;; The generated code is a function:
           (fn [] (reset! the-atom the-num)))
    :GET (let [the-sym (-> cmd second second second second second)
               the-atom (hypertalk-intern the-sym)]
           (fn [] @the-atom))
    (throw (Exception. (str "unknown command" (first cmd))))
    ))

(defn compile-hypertalk-ast [ast]
  (condp = (first ast)
    :COMMAND (compile-command (second ast))
    (throw (Exception. (str "unknown ast root" (first ast))))))

(defn compile-hypertalk-string [string]
  (-> string hypertalk-parser compile-hypertalk-ast))

(defn interpret-hypertalk-ast [ast]
  ((compile-hypertalk-ast ast)))

(defn interpret-hypertalk-string [str]
  ((compile-hypertalk-string str)))

(defmacro put [thing preposition container]
  `[:COMMAND
    [:PUT
     [:EXPR [:INUM (str ~thing)]]
     [:CONTAINER [:SIMPLE_CONTAINER [:VARIABLE (str '~container)]]]]])

(defmacro get [expr]
  `[:COMMAND
    [:GET
     [:EXPR [:CONTAINER [:SIMPLE_CONTAINER [:VARIABLE (str '~expr)]]]]]])
