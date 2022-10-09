(ns asr.specs
  (:require [clojure.spec.alpha            :as s   ]
            [clojure.spec.gen.alpha        :as gen ]
            [clojure.test.check.generators :as tgen]))

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
