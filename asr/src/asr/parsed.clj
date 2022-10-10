(ns asr.parsed
  (:use     [asr.utils])
  (:require [asr.grammar  :refer [asr-pre-spec]])
  (:require [clojure.zip  :as    zip]))


;;                  _     _
;;  ____ __  ___ __| |___| |_ ___
;; (_-< '_ \/ -_) _| / -_)  _(_-<
;; /__/ .__/\___\__|_\___|\__/__/
;;    |_|

(def speclets
  "## Raw Hiccup for all Speclets

  Strip off the `module` info, leaving only ASDL-DEFs, i.e.,
  *speclets*.
  "
  (vec (rest
        ((-> (zip/vector-zip asr-pre-spec)
             zip/down zip/right zip/right) 0))))


;;     _         _ _                                 __
;;  __| |_  __ _| | |_____ __ __  _ __  __ _ _ __   / _|_ _ ___ _ __
;; (_-< ' \/ _` | | / _ \ V  V / | '  \/ _` | '_ \ |  _| '_/ _ \ '  \
;; /__/_||_\__,_|_|_\___/\_/\_/  |_|_|_\__,_| .__/ |_| |_| \___/_|_|_|
;;                                          |_|
;;                  _     _
;;  ____ __  ___ __| |___| |_
;; (_-< '_ \/ -_) _| / -_)  _|
;; /__/ .__/\___\__|_\___|\__|
;;    |_|

(defn shallow-map-from-speclet
  "Convert an ASDL-DEF into a map from :ASDL-TERM to the name of the
  term of the speclet and from :ASDL-FORMS into a list of
  alternative forms, still in hiccup format awaiting deeper
  conversion."
  [speclet]
  (let [[signal asdl-term asdl-forms] speclet
        _ (assert (= signal :ASDL-DEF)) ;; TODO: replace with s/fspec
        [signal & forms] asdl-forms     ;; listify forms
        _ (assert (= signal :ASDL-FORMS))
        renested [asdl-term [:ASDL-FORMS forms]]
        denested (mapcat identity renested)]
    (apply hash-map denested)))


;;     _        _
;;  __| |___ __| |___ _ __  __ _ _ __
;; / _` / -_) _| |___| '  \/ _` | '_ \
;; \__,_\___\__|_|   |_|_|_\__,_| .__/
;;                              |_|

(defn decl-map
  "Convert [:ASDL-DECL [:ASDL-TYPE ...] [:ASDL-NYM ...]] into
  {:ASDL-TYPE ..., :MULTIPLICITY ..., :ASDL-NYM ...}.
  Used to convert ASDL-COMPOSITES and ASDL-TUPLES.
  TODO: Rewrite argument validation with s/fdef."
  [decl-hiccup]
  (let [_ (assert (= (decl-hiccup 0) :ASDL-DECL))
        [signal type-nym & opt] (decl-hiccup 1)
        _ (assert (= signal :ASDL-TYPE))
        [signal decl-nym] (decl-hiccup 2)
        _ (assert (= signal :ASDL-NYM))]
    {:ASDL-TYPE type-nym
     :MULTIPLICITY (case opt
                     (([:STAR])) ::zero-or-more
                     (([:QUES])) ::at-most-once
                     ::once) ;; default
     :ASDL-NYM decl-nym}))


;;              _ _      __
;;  __ _ ___ __| | |___ / _|___ _ _ _ __
;; / _` (_-</ _` | |___|  _/ _ \ '_| '  \
;; \__,_/__/\__,_|_|   |_| \___/_| |_|_|_|


(defmulti asdl-form first)

(defmethod asdl-form :ASDL-SYMCONST [form]
  (apply hash-map form))

(defmethod asdl-form :ASDL-COMPOSITE [form]
  (let [[_ head args-pre] form
        [_ & decls] args-pre] ;; & means listify
    {:ASDL-COMPOSITE {:ASDL-HEAD (second head)
                      :ASDL-ARGS (map decl-map decls)}}))

(defmethod asdl-form :ASDL-TUPLE [form]
  (let [[_ args-pre] form
        [_ & decls] args-pre] ;; & means listify
    {:ASDL-TUPLE (name (gensym "asr-tuple"))
     :ASDL-ARGS (map decl-map decls)}))


;;  _    _                               __                   _     _
;; | |__(_)__ _   _ __  __ _ _ __   ___ / _|  ____ __  ___ __| |___| |_ ___
;; | '_ \ / _` | | '  \/ _` | '_ \ / _ \  _| (_-< '_ \/ -_) _| / -_)  _(_-<
;; |_.__/_\__, | |_|_|_\__,_| .__/ \___/_|   /__/ .__/\___\__|_\___|\__/__/
;;        |___/             |_|                 |_|
;;   __                 _
;;  / _|_ _ ___ _ __   | |_ ___ _ _ _ __  ___
;; |  _| '_/ _ \ '  \  |  _/ -_) '_| '  \(_-<
;; |_| |_| \___/_|_|_|  \__\___|_| |_|_|_/__/

(defn hashmap-from-speclet
  "## Hashmap from Speclet, Itself

  A speclet is, roughly, ASDL-TERM '=' ASDL-FORM*

  One entire speclet to a hashmap:
  "
  [speclet]
  (let [pre (shallow-map-from-speclet speclet)
        term (:ASDL-TERM pre)
        forms (map asdl-form (:ASDL-FORMS pre))]
    {:ASDL-TERM term
     :ASDL-FORMS forms}))


(defn map-pair-from-speclet-map [speclet-map]
  [(keyword "asr.autospecs" (:ASDL-TERM speclet-map)) ;; no kebab'bing!
   (:ASDL-FORMS speclet-map)])


(def big-map-of-speclets-from-terms
  "# Big Map of Speclets From Terms

  Example: term `::symbol` maps to `::Function`, `::Program`,
  `::Module`, and more.

  Except for `SymbolTable`, which is not written, terms are in
  `snake_case`.

  Have fun clicking around the large output of this cell. There
  are about 28 terms with about 227 heads.
  "
  ;; TODO: Make some nice swiss arrows to do all this.
  (apply hash-map
         (mapcat identity  ;; flatten one level
                 (map
                  (comp map-pair-from-speclet-map
                        hashmap-from-speclet)
                  speclets))))


;;  _    _        _ _    _          __      _         __  __
;; | |__(_)__ _  | (_)__| |_   ___ / _|  __| |_ _  _ / _|/ _|
;; | '_ \ / _` | | | (_-<  _| / _ \  _| (_-<  _| || |  _|  _|
;; |_.__/_\__, | |_|_/__/\__| \___/_|   /__/\__|\_,_|_| |_|
;;        |___/

(defn kind-from-form [form]
  (-> form first first))


(defn head-from-kind-form [kind form]
  (case kind
    :ASDL-COMPOSITE (-> form first second :ASDL-HEAD)
    :ASDL-SYMCONST (-> form first second) ;; symconst itself
    :ASDL-TUPLE (-> form first second)))


(defn stuff-from-term-form [term form]
  (let [kind (-> form kind-from-form)
        ghead (head-from-kind-form kind form)
        kwh (keyword "asr.autospecs" ghead)] ;; no kebab'bing
    {:head kwh,:term term,:kind kind,:form,form}))


(def big-list-of-stuff
  "# Big List of Stuff

  A ***stuff*** is a map of `:head`, `:term`, `:kind`, and `:form`
  for the approximately 227 heads & forms of ASR. A stuff is all
  we need for making clojure.specs from terms, heads, & forms. The
  stuff keywords `:head`, `:term`, `:kind`, and `:form` need not
  be namespaced.

  This big list of stuff is like a big, flat, denormalized
  database table.

  ## Generalized Heads

  For composites, the \"head\" is obvious because it's a symbol
  followed by an args tuple.

  For symconsts, the \"heads\" are just the symbolic constants
  themselves.

  For all tuples, the head is gensymmed. Example: head
  `::asr-tuple3805` (kebab-case; exception to the rule for heads),
  corresponds to term `::alloc_arg`, in snake case, like all
  terms.

  ### Don't Kebab Too Early

  Heads, except for gensymmed heads for tuples, are in PascalCase;
  don't kebab them. We'll kebab the derived namespaced keywords
  for naming clojure.specs.

  ## Helper: Stuff from Term & Form

  Here we go again, illiterates! Gotta show you how we're doing it
  before showing you what we're doing!

  The intention is to partially evaluate `stuff-from-term-form` on
  a term, then map the result over all the forms for that term.
  "
  (mapcat
   identity  ; Flatten once.
   (map (fn [speclet]
          (let [[term forms] speclet]
            (map
             (partial
              stuff-from-term-form term)
             forms)))
        big-map-of-speclets-from-terms)))


;;                               _        _         __  __
;;  ____  _ _ __  __ ___ _ _  __| |_   __| |_ _  _ / _|/ _|___
;; (_-< || | '  \/ _/ _ \ ' \(_-<  _| (_-<  _| || |  _|  _(_-<
;; /__/\_, |_|_|_\__\___/_||_/__/\__| /__/\__|\_,_|_| |_| /__/
;;     |__/

(def symconst-stuffs
  "# All Symconst Specs

  Clojure.specs for symconsts are easiest because they don't
  depend on other clojure.specs. There are about 72
  symconsts (more as ASR grows):

  ## Symconst Stuffs
  "
  (filter #(= (:kind %) :ASDL-SYMCONST) big-list-of-stuff))


;;                               _        _         __  __         _
;;  ____  _ _ __  __ ___ _ _  __| |_   __| |_ _  _ / _|/ _|______ | |__ _  _
;; (_-< || | '  \/ _/ _ \ ' \(_-<  _| (_-<  _| || |  _|  _(_-<_-< | '_ \ || |
;; /__/\_, |_|_|_\__\___/_||_/__/\__| /__/\__|\_,_|_| |_| /__/__/ |_.__/\_, |
;;     |__/                                                             |__/
;;  _
;; | |_ ___ _ _ _ __
;; |  _/ -_) '_| '  \
;;  \__\___|_| |_|_|_|

(def symconst-stuffss-by-term
  "## Symconst-Term-Specs

  To check an instance or utterance of ASR like expr-221000, we'll
  need to check its sub-parts by term, not by head.

  ### Symconst Stuffss sic by Term

  First, partition the symconst specs by term. There are about 13
  terms categorizing the (approx) 72 symconst heads.
  "
  (partition-by :term symconst-stuffs))
