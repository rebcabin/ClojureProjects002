(ns asr.grammar
  (:require [instaparse.core :as insta])
  (:use     [asr.asr]))


;;              _ _
;;  __ _ ___ __| | |  __ _ _ _ __ _ _ __  _ __  __ _ _ _
;; / _` (_-</ _` | | / _` | '_/ _` | '  \| '  \/ _` | '_|
;; \__,_/__/\__,_|_| \__, |_| \__,_|_|_|_|_|_|_\__,_|_|
;;                   |___/

;;; Documentation for speclets and other nomenclature is in the
;;; docstring for "asdl-grammar."

(def asdl-grammar

  "## Instaparse Grammar for ASDL

  We'll parse the ASDL spec for ASR into Clojure vectors and
  hashmaps, then generate clojure.specs for terms and forms from
  the vectors and hashmaps.

  The only thing less-than-obvious in the following grammar are
  angle brackets. They mean \"don't bother reporting this term.\"
  Cuts down on clutter in the output.

  The grammar says:

  1. An ASDL spec is a bunch of ***productions*** or ASDL-DEFs

  2. An ASDL-DEF, aka ***speclet***, is a triple of (a) ***term***
  or ASDL-TERM, (b) an equals sign, and (c) one or more ASDL-FORMs
  separated by vertical bar characters. The meaning of an ASDL-DEF
  is an *alternation*, a list of alternative ASDL-FORMs.

  There are about 28 terms. See all-terms-test in core_test.clj.
  They are things like ::array_index, ::ttype, ::expr, etc.

  3. There are three *kinds* of ASDL-FORM or speclet: a
  ***composite***, a ***symconst***, or a ***tuple***.

  4. A *composite* is a ASDL-HEAD ***head*** followed by
  ASDL-ARGSs args. Args are identical in shape to a tuple.

  There are about 227 heads, about six of which are asr-tuples
  with gensymmed names. They are things like ::GetPointer,
  ::FileClose, ::TypeParameter, etc. See all-heads-test in
  core_test.clj.

  5. A *symconst* is just an identifier, reckoned as the *head* of
  the symconst.

  6. A tuple is a comma-separated list (in round brackets) of
  pairs of ***type*** and ***variable***.

  A tuple is anonymous, but we gensym a *head* for them, so that
  every kind of ASDL-FORM has a head, for convenience.

  7. Types can have *quantitative qualifiers*, aka
  *multiplicities*: STAR or QUES, meaning that the variable
  denotes \"zero or more\" and \"at least one\" instance of the
  type respectively. The default quantity is \"exactly once.\"

  The big picture to remember about *terms* and *heads* is that a
  *speclet* looks like one of the following three:

  1. `term` `=` `composite-`*`head`*`-1` `args` `|`
  `composite-`*`head`*`-2` `args` `|` ...

  2. `term` `=` `symconst-`*`head`* `|` `symconst-`*`head`* `|`
  ...

  3. `term` `=` `tuple` (anonymous gensymmed *head*)

  A *term* corresponds to one or more (a *bunch* of) *heads*, but
  each head corresponds to exactly one term.

  A term has exactly one *speclet*. The speclet is the whole line,
  left-hand side (term) and right-hand side (bunch of *forms*, one
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
