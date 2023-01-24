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
  "Big vector. Strip off the `module` info, leaving only ASDL-DEFs,
  i.e., _speclets_.
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
  "Convert an `ASDL-DEF` into a map from `:ASDL-TERM` to the name of
  the term of the speclet and from `:ASDL-FORMS` into a list of
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
  "Convert hiccup `[:ASDL-DECL [:ASDL-TYPE ...] [:ASDL-NYM ...]]`
  into `{:ASDL-TYPE ..., :MULTIPLICITY ..., :ASDL-NYM ...}`. Used
  to convert `ASDL-COMPOSITES` and `ASDL-TUPLES`. TODO: Rewrite
  argument validation with s/fdef."
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


(defmulti asdl-form
  "Convert three `ASDL` kinds, `ASDL-SYMCONST`, `ASDL-COMPOSITE`,
  and `ASDL-TUPLE`, into Clojure hashmaps"
  first )

(defmethod asdl-form :ASDL-SYMCONST
  #_"Convert `ASDL-SYMCONST` into a Clojure hashmap."
  [form]
  (apply hash-map form))

(defmethod asdl-form :ASDL-COMPOSITE
  #_"Convert `ASDL-COMPOSITE` into a Clojure hashmap."
  [form]
  (let [[_ head args-pre] form
        [_ & decls] args-pre] ;; & means listify
    {:ASDL-COMPOSITE {:ASDL-HEAD (second head)
                      :ASDL-ARGS (map decl-map decls)}}))

(defmethod asdl-form :ASDL-TUPLE
  #_"Convert `ASDL-TUPLE` into a Clojure hashmap."
  [form]
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
  "Convert one entire speclet to a hashmap.

  A speclet is, roughly, `ASDL-TERM '=' ASDL-FORM*`
  "
  [speclet]
  (let [pre (shallow-map-from-speclet speclet)
        term (:ASDL-TERM pre)
        forms (map asdl-form (:ASDL-FORMS pre))]
    {:ASDL-TERM term
     :ASDL-FORMS forms}))


(defn map-pair-from-speclet-map
  "Convert a speclet hashmap `{:ASDL-TERM <term>, :ASDL-FORMS <forms>}`
  into an ordered pair `[:ASDL-TERM <term>, :ASDL-FORMS <forms>]` in
  preparation for the inverted index, `big-map-of-speclets-from-terms`.
  Prepend the namespace prefix `asr.autospecs` without kebab'bing."
  [speclet-map]
  [(keyword "asr.autospecs" (:ASDL-TERM speclet-map)) ;; no kebab'bing!
   (:ASDL-FORMS speclet-map)])


(def big-map-of-speclets-from-terms
  "Make a big, inverted index of about 227 heads from about 28
  terms.

  Example: term `::symbol` maps to `::Function`, `::Program`,
  `::Module`, and more.

  Except for `SymbolTable`, which is not written in ASDL, terms
  are in `snake_case`.
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

(defn kind-from-form
  "Get the `kind` from an ASDL form. The kind is one of
  `ASDL-COMPOSITE`, `ASDL-SYMCONST`, or `ASDL-TUPLE`."
  [form]
  (-> form first first))


(defn head-from-kind-form
  "Get the generalize head from a `kind` and a form."
  [kind form]
  (case kind
    :ASDL-COMPOSITE (-> form first second :ASDL-HEAD)
    :ASDL-SYMCONST (-> form first second) ;; symconst itself
    :ASDL-TUPLE (-> form first second)))


(defn stuff-from-term-form
  "Construct a _stuff_ from a `term` and a form. Prepend the
  namespace prefix `asr.autospecs`, without kebab'bing."
  [term form]
  (let [kind (-> form kind-from-form)
        ghead (head-from-kind-form kind form)
        kwh (keyword "asr.autospecs" ghead)] ;; no kebab'bing
    {:head kwh,:term term,:grup kind,:form,form}))


(def big-list-of-stuff
  "A ___stuff___ is a map of `:head`, `:term`, `:grup`, and `:form`
  for the approximately 227 heads & forms of ASR. A stuff is all
  we need for making clojure.specs from terms, heads, & forms. The
  stuff keywords `:head`, `:term`, `:grup`, and `:form` need not
  be namespaced.

  Example of a _stuff_:

      {:head :asr.autospecs/Source,
       :term :asr.autospecs/abi,
       :grup :ASDL-SYMCONST,
       :form {:ASDL-SYMCONST \"Source\"}}

  This big list of stuff is like a big, flat, denormalized
  database table.

  ## Kinds

  There are three kinds of stuffs: `ASDL-COMPOSITE`, which look
  like function declarations with a head and args in parentheses;
  `ASDL-SYMCONST`, which are symbolic constants, and `ASDL-TUPLE`,
  which look like headless arg lists. The kinds are distinguished
  by the `:grup` field.

  ## Terms

  ___Terms___ are the things to the left of an equals sign in the
  ASDL grammar (namespace `asr.asr`). For example, in the ASDL
  production

      abi                   -- External     ABI
        = Source          --   No         Unspecified
        | LFortranModule  --   Yes        LFortran
        | GFortranModule  --   Yes        GFortran
        | BindC           --   Yes        C
        | Interactive     --   Yes        Unspecified
        | Intrinsic       --   Yes        Unspecified

  `abi` is a term. `Source` is a generalized head, as we see next.

  ## Generalized Heads

  For composites, the \"head\" is obvious because it's a symbol
  followed by an `ASDL-ARGS` tuple in parentheses. Example:

  ASDL:

      case_stmt = CaseStmt(expr* test, stmt* body)

  stuff:

      {:head :asr.autospecs/CaseStmt,
       :term :asr.autospecs/case_stmt,
       :grup :ASDL-COMPOSITE,
       :form
       {:ASDL-COMPOSITE
        {:ASDL-HEAD \"CaseStmt\",
         :ASDL-ARGS
         ({:ASDL-TYPE \"expr\",
           :MULTIPLICITY :asr.parsed/zero-or-more,
           :ASDL-NYM \"test\"}
          {:ASDL-TYPE \"stmt\",
           :MULTIPLICITY :asr.parsed/zero-or-more,
           :ASDL-NYM \"body\"})}}}

  For symconsts, the \"heads\" are just the symbolic constants
  themselves. Example:

      {:head :asr.autospecs/Source,
       :term :asr.autospecs/abi,
       :grup :ASDL-SYMCONST,
       :form {:ASDL-SYMCONST \"Source\"}}

  For all tuples, the head is gensymmed. Example:

  ASDL:

      call_arg = (expr? value)

  stuff:

      {:head :asr.autospecs/asr-tuple2595,
       :term :asr.autospecs/call_arg,
       :grup :ASDL-TUPLE,
       :form
       {:ASDL-TUPLE \"asr-tuple2595\",
        :ASDL-ARGS
        ({:ASDL-TYPE \"expr\",
          :MULTIPLICITY :asr.parsed/at-most-once,
          :ASDL-NYM \"value\"})}}

  The head is `:asr.autospecs/asr-tuple2595` (kebab-case;
  exception to the rule for heads), corresponds to term
  `:asr.autospecs/call_arg`, in snake case, like all terms.
  Gensyms change every time the program runs.

  ## Don't Kebab Too Early

  Heads, except for gensymmed heads for tuples, are in PascalCase;
  don't kebab them. We'll kebab the derived namespaced keywords
  for naming clojure.specs in namespace `asr.autospecs`.

  ## How-to-Helper: Stuff from Term & Form

  Partially evaluate `stuff-from-term-form` on a term, then map
  the result over all the forms for that term.

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
  "Clojure.specs for symconsts are easiest because they don't
  depend on other clojure.specs. There are about 72
  symconsts (more as ASR grows):
  "
  (filter #(= (:grup %) :ASDL-SYMCONST) big-list-of-stuff))


;;                            _ _              _         __  __
;;  __ ___ _ __  _ __  ___ __(_) |_ ___ ___ __| |_ _  _ / _|/ _|___
;; / _/ _ \ '  \| '_ \/ _ (_-< |  _/ -_)___(_-<  _| || |  _|  _(_-<
;; \__\___/_|_|_| .__/\___/__/_|\__\___|   /__/\__|\_,_|_| |_| /__/
;;              |_|

(def composite-stuffs
  (filter #(= (:grup %) :ASDL-COMPOSITE) big-list-of-stuff))


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
  "To check an instance or utterance of ASR like
  `asr.data/expr-221000`, we'll need to check its sub-parts by
  term, not by head.

  Partition the symconst specs by term. There are about 13
  terms categorizing the (approx) 72 symconst heads.
  "
  (partition-by :term symconst-stuffs))
