(ns asr.grammar
  (:require [instaparse.core :as      insta  ]
            [asr.asr-snapshot :refer [all-asr]])
  )


;;              _ _
;;  __ _ ___ __| | |  __ _ _ _ __ _ _ __  _ __  __ _ _ _
;; / _` (_-</ _` | | / _` | '_/ _` | '  \| '  \/ _` | '_|
;; \__,_/__/\__,_|_| \__, |_| \__,_|_|_|_|_|_|_\__,_|_|
;;                   |___/

;;; Documentation for speclets and other nomenclature is in the
;;; docstring for "asdl-grammar."

(def asdl-grammar

  "We'll parse the ASDL spec for ASR into Clojure vectors and
  hashmaps, then generate clojure.specs for terms and forms from
  the vectors and hashmaps.

  The only thing less-than-obvious in the following grammar are
  angle brackets. They mean \"don't bother reporting this term.\"
  Cuts down on clutter in the output.

  The grammar says:

  1. An ASDL spec is a bunch of ___productions___ or ASDL-DEFs

  2. An ASDL-DEF, aka ___speclet___, is a triple of (a) ___term___
  or ASDL-TERM, (b) an equals sign, and (c) one or more ASDL-FORMs
  separated by vertical bar characters. The meaning of an ASDL-DEF
  is an _alternation_, a list of alternative ASDL-FORMs.

  There are 28 terms in the constant ASR snapshot we use for
  testing, 30 terms in a recent drop of live ASR. See
  all-terms-test in core_test.clj. Terms are left-hand sides of
  productions. They are things like array_index, ttype, expr, etc.

  3. There are three _groups_ of ASDL-FORM or speclet: a
  ___composite___, a ___symconst___, or a ___tuple___. These are
  not groups in the sense of abstract algebra; they're just
  collections of names. In a recent drop of ASR, there are

    a. 10 composites -- expr, stmt, ttype, symbol, etc.

    b. 6 tuples -- call_arg, do_loop_head, dimension, etc.

    c. 14 symconsts -- abi, cmpop, binop, presence, etc.

  4. A _composite_ is an ASDL-HEAD ___head___ followed by
  ASDL-ARGSs args (really formal parameters!). The \"symbol\" term
  of composites, for example has about 14 heads: Program, Module,
  Function, etc. The \"expr\" term of composites has about 86
  heads: IfExp, ComplexConstructor, NamedExpr, etc.

  Args (parameters) of a composite are identical in shape to a
  tuple.

  There are about 227 heads, about six of which are asr-tuples
  with gensymmed names. They are things like GetPointer,
  FileClose, TypeParameter, etc. See all-heads-test in
  core_test.clj.

  5. A _symconst_ is just a singleton identifier, reckoned as the
  _head_ of the symconst.

  6. A tuple is a comma-separated list (in round brackets) of
  pairs of ___type___ and ___variable___.

  A tuple is anonymous, but we gensym a _head_ for them, so that
  every kind of ASDL-FORM has a head, for convenience.

  7. Types can have _quantitative qualifiers_, aka
  _multiplicities_: STAR or QUES, meaning that the variable
  denotes \"zero or more\" and \"at least one\" instance of the
  type respectively. The default quantity is \"exactly once.\"

  The big picture to remember about _terms_ and _heads_ is that a
  _speclet_ looks like one of the following three:

  1. `term` `=` `composite-`*`head`*`-1` `args` `|`
                `composite-`*`head`*`-2` `args` `|`
                 ...

  2. `term` `=` `symconst-`*`head`* `|`
                `symconst-`*`head`* `|`
                 ...

  3. `term` `=` `tuple` (anonymous gensymmed *head*)

  A _term_ corresponds to one or more (a _bunch_ of) _heads_, but
  each head corresponds to exactly one term.

  A term has exactly one _speclet_. The speclet is the whole line,
  left-hand side (term) and right-hand side (bunch of _forms_, one
  per head).

  All forms in a speclet have the same kind: composite, symconst,
  or tuple. There won't be a mixture of composites and symconsts,
  for instance, on the right-hand side of any term.

  In addition to the 3 kinds, it turns out there are about 28
  terms and speclets, and 227 heads, forms, and clojure.specs when
  we're done. The number grows slowly as we add features to ASR.
  "

  "MODULE         = SPC* <'module'> SPC* IDENT LBRACE SPEC RBRACE
   SPEC           = (ASDL-DEF SPC*)*
(* **************************************************************** *)
  <SPC>           = <#'\\s*(--[^\\n]*)?'> (* eat comments quickly * *)
  <BAR>           = <SPC* '|' SPC*>
  <COMMA>         = <SPC* ',' SPC*>
   STAR           = <SPC* '*' SPC*>
   QUES           = <SPC* '?' SPC*>
  <EQ>            = <SPC* '=' SPC*>
  <LBRACE>        = <SPC* '{' SPC*>
  <RBRACE>        = <SPC* '}' SPC*>
  <LPAR>          = <SPC* '(' SPC*>
  <RPAR>          = <SPC* ')' SPC*>
  <IDENT>         = #'[A-Za-z_][A-Za-z0-9_\\.\\-]*'
(* **************************************************************** *)
   ASDL-DEF       = ASDL-TERM EQ ASDL-FORMS  (* aka 'speclet' ***** *)
   ASDL-TERM      = IDENT
   ASDL-FORMS     = ASDL-FORM (BAR ASDL-FORM)*
  <ASDL-FORM>     = ASDL-COMPOSITE
                  | ASDL-SYMCONST
                  | ASDL-TUPLE

   ASDL-COMPOSITE = ASDL-HEAD ASDL-ARGS
   ASDL-SYMCONST  = IDENT
   ASDL-TUPLE     = ASDL-ARGS

   ASDL-HEAD      = IDENT
   ASDL-ARGS      = LPAR ASDL-DECL (COMMA ASDL-DECL)* RPAR
                  | LPAR RPAR
   ASDL-DECL      = ASDL-TYPE SPC* ASDL-NYM
   ASDL-TYPE      = IDENT | IDENT STAR | IDENT QUES
   ASDL-NYM       = IDENT
  ")


;;              _ _
;;  __ _ ___ __| | |  _ __  __ _ _ _ ___ ___ _ _
;; / _` (_-</ _` | | | '_ \/ _` | '_(_-</ -_) '_|
;; \__,_/__/\__,_|_| | .__/\__,_|_| /__/\___|_|
;;                   |_|

(def asdl-parser (insta/parser asdl-grammar))


;;  __ _ ____ _ ___ _ __ _ _ ___ ___ ____ __  ___ __
;; / _` (_-< '_|___| '_ \ '_/ -_)___(_-< '_ \/ -_) _|
;; \__,_/__/_|     | .__/_| \___|   /__/ .__/\___\__|
;;                 |_|                 |_|

(def asr-pre-spec

  "Capture the parse of ASR.asdl into hiccup format
  **(**<https://github.com/weavejester/hiccup>**).** "

  (asdl-parser all-asr))
