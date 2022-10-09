(ns asr.specs
  (:use [asr.utils])
  (:require [asr.autospecs                 :refer [spec-from-head-and-args]])
  (:require [clojure.spec.alpha            :as s                           ]
            [clojure.spec.gen.alpha        :as gen                         ]
            [clojure.test.check.generators :as tgen                        ]))


;;  _    _         _   _  __ _
;; (_)__| |___ _ _| |_(_)/ _(_)___ _ _
;; | / _` / -_) ' \  _| |  _| / -_) '_|
;; |_\__,_\___|_||_\__|_|_| |_\___|_|

;;; TEACHING NOTE: Experiment that failed.

"## Spec for *identifier*

We can't use just `symbol?` because it generates namespaced
symbols, and they aren't useful for testing LPython. We'll need a
custom
generator (<https://clojure.org/guides/spec#_custom_generators>).

The following attempt has performance problems and will be
discarded. We save it as a lesson in this kind of dead end.
"

#_(def identifier-re #"[a-zA-Z_][a-zA-Z0-9_]*")

#_(s/def ::identifier
  (s/with-gen
    symbol?
    (fn []
      (gen/such-that
       #(re-matches
         identifier-re
         (name %))
       (gen/symbol)))))

;;; Better solution that, sadly but harmlessly, lacks underscores
;;; because gen/char-alpha doesn't generate underscores. TODO: fix
;;; this.

(let [alpha-re #"[a-zA-Z]"  ;; The famous "let over lambda."
      alphameric-re #"[a-zA-Z0-9]*"]
  (def alpha?
    #(re-matches alpha-re %))
  (def alphameric?
    #(re-matches alphameric-re %))
  (defn identifier? [s]
    (and (alpha? (subs s 0 1))
         (alphameric? (subs s 1))))
  (def identifier-generator
    (tgen/let [c (gen/char-alpha)
               s (gen/string-alphanumeric)]
      (str c s)))
  (s/def ::identifier  ;; side effects the spec registry!
    (s/with-gen
      identifier?
      (fn [] identifier-generator))))


;;                _         _     _        _    _
;;  ____  _ _ __ | |__  ___| |___| |_ __ _| |__| |___
;; (_-< || | '  \| '_ \/ _ \ |___|  _/ _` | '_ \ / -_)
;; /__/\_, |_|_|_|_.__/\___/_|    \__\__,_|_.__/_\___|
;;     |__/

;; # unblocking: Hand-Written Term Spec for SymbolTable

;; ASDL doesn't offer an easy way to specify maps, but
;; Clojure.spec does. ASR.asdl doesn't have a spec for
;; `SymbolTable`, so we write one by hand and upgrade it as we go
;; along.

;; We cannot define this spec until we define the others
;; because `(s/spec ::symbol)` doesn't exist yet. We'll backpatch
;; it later

(s/def ::symbol-table
  (s/cat
   :head #(= % 'SymbolTable)
   :unique-id int?
   :symbols (s/map-of keyword? (s/spec :asr.core/symbol))))


(defn spec-from-composite
  "# Back-patching Symbol

  TODO

  # First Composite Spec: `TranslationUnit`

  Write specs as data lists and `eval` them later. Turns out it's
  necessary to do that, and it's a beneficial accident lest we
  clutter up the namespace of specs.

  Composites and tuples have lists of type-var pairs, that is, of
  args. We've already handled arg lists in `spec-from-args` above.

  Specs for all tuples' heads and terms have already been
  registered.

  Specs for all symconsts' heads and terms have already been
  registered.
  "
  [composite]
  (let [head (-> composite :ASDL-HEAD symbol echo)
        nskw (-> head nskw-kebab-from        echo)
        args (-> composite :ASDL-ARGS        #_echo)]
    `(s/def ~nskw ~(spec-from-head-and-args head args))))
