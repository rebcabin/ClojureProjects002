(ns asr.environment
  (:use clojure.set)
  (:require [clojure.pprint :as pp]))


;;  ___         _                            _
;; | __|_ ___ _(_)_ _ ___ _ _  _ __  ___ _ _| |_
;; | _|| ' \ V / | '_/ _ \ ' \| '  \/ -_) ' \  _|
;; |___|_||_\_/|_|_| \___/_||_|_|_|_\___|_||_\__|


;;; User names MUST NOT USE GREEK.


;;; For flexibility, all "(eval-...)" functions return a function
;;; of an Environment. Such functions can be evaluated in any
;;; environment later. TODO: spec Environment. TODO: consider
;;; moving env to first position for partial evaluation /
;;; currying.


;;; An Environment is a dictionary φ and a pointer π to an
;;; enclosing penv (π is also a pun for Greek περι, peri-,
;;; meaning "belt," "enclosing," "around," as in "perimeter,"
;;; not "parameter"). A penv or Environment-atom is a mutable atom
;;; containing an Environment.


(defn is-environment?
  "Ensure keys φ and π exist. Note it's not sufficient to check the
  value of π because it's nil for the global environment AND it's
  nil for a missing key π because `(:π {:φ (atom 'foo)})` ~~>
  nil."
  [thing]
  (subset? #{:φ, :π} (set (keys thing))))


(defn clj-atom?
  "Thread-safe mutable container, not a primitive data object."
  [thing]
  (instance? clojure.lang.Atom thing))


(defn is-penv?
  "TODO: Consider clojure.spec for this."
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


(defn init-global-environments
  "Also called in TranslationUnit in asr.clj"
  []
  (def ΓΠ
      "Unique, session-specific penv: Global Γ Perimeter Π; has a
  frame (:φ is-a dict) and a parent perimeter (:π is-a penv).
  TODO: built-ins go here.
  TODO: spec."
    (atom {:φ {}, :π nil} :validator is-environment?))


  (def ΓΣ
    "Unique, session-specific, integer-indexed, global registry of
  symbol tables."
    (atom {} :validator map?)))


(init-global-environments)


;;; I think we don't ever need to "delete" a penv, but we may need
;;; to clear one or more bindings.


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


(let [f (partial format "%x")]
  (defn dump-penv-chain
    [penv]
    (cond
      (nil? penv) nil
      :else (cons
             [(f (hash penv)), (keys (:φ @penv))]
             (dump-penv-chain (:π @penv))
             ))))
