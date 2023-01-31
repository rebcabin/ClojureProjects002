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

  "Parse the ASDL spec for ASR into Clojure vectors and hashmaps,
  then generate clojure.specs for terms and forms from the vectors
  and hashmaps. Also promote these collections into forms
  convenient for interpreters (see asr.clj).

  The only things less-than-obvious in the following grammar are
  angle brackets. They mean \"don't bother reporting this term.\"
  Cuts down on clutter in the output.

  The grammar says:

  1. An ASDL spec is a bunch of ___productions___, aka _speclets_
  or ASDL-DEFs. For example,

    symbol
      = Program(symbol_table symtab, identifier name, ..., stmt* body)
      | Module(symbol_table symtab, identifier name, ...)
    ...
    storage_type = Default | Save | Parameter | Allocatable
    abi                 -- External     ABI
      = Source          --   No         Unspecified
      | LFortranModule  --   Yes        LFortran
    ...
    stmt
      = Allocate(alloc_arg* args, expr? stat, ...)
      | Assign(int label, identifier variable)
    ...
    expr
      = IfExp(expr test, expr body, expr orelse, ...)
      | ComplexConstructor(expr re, expr im, ttype type, expr? value)
    ...

  is an ASDL spec consisting of productions (speclets) for
  \"symbol,\" \"storage_type,\" \"abi,\" \"stmt,\" and \"expr.\".

  2. An ASDL-DEF, aka _production_ or ___speclet___, is a triple
  of

    (a) ___term___ or ASDL-TERM

    (b) an equals sign

    (c) one or more (a _bunch_ of) ASDL-FORMs, ___forms___, for
  short, aka _alternatives_.

  For example,

          production,        (grammar lingo)
            speclet          (ASR lingo)
     __________^___________
    |                      |
      term        forms      (one or more [bunch of])
     __^__      _____^_____
    |     |    |           |
     symbol  =  Program(...)
             |  Module(...)
             |  Function(...)
                ...

  The left-hand side of (the equals sign in) a speclet is a
  _term_. The right-hand side of (the equals sign in) a speclet is
  an _alternation of forms_, a \"term of venery\" as one might say
  \"a murder of crows,\" \"a parliament of owls,\" or a
  \"lamentation of swans.\"

  There are 28 speclets in the constant ASR snapshot for testing,
  and 30 speclets in a recent drop of live ASR. See all-terms-test
  in core_test.clj.

  3. There are three ___groups___ of speclets: ___composites___,
  ___symconsts___, and ___tuples___. These are not algebraic
  groups; they're just collections (see [this _stackoverflow_ post
  for the difference between a _collection_ and a _sequence_ in
  Clojure](https://stackoverflow.com/questions/19850730/whats-the-difference-between-a-sequence-and-a-collection-in-clojure).

  In a recent (Jan 2023) drop of ASR, there are

    (a) 10 composite speclets -- expr, stmt, ttype, symbol, etc.

    (b) 6 tuple speclets -- call_arg, do_loop_head, dimension, etc.

    (c) 14 symconst speclets -- abi, cmpop, binop, presence, etc.

  4. A _composite speclet_ is an alternation of _composite forms_.
  Each composite form is an ASDL-HEAD or ___head___ followed by
  ASDL-ARGSs, really FORMAL PARAMETERS! [See ASR Issue 1446]
  (https://github.com/lcompilers/lpython/issues/1446).

  Example of one of the composite forms of the composite speclet
  \"symbol:\"

                          composite form
     ____________________________^____________________________
    |                                                         |
                     (same shape as a tuple form)
     head             parameters, parameter list
     __^__  ________________________^_________________________
    |     ||                                                  |
    Program(symbol_table s, ident nym, ident* deps, stmt* body)

  The \"symbol\" composite speclet has about 14 composite forms
  with heads Program, Module, Function, etc. The \"expr\"
  composite has about 86 heads: IfExp, ComplexConstructor,
  NamedExpr, etc. The \"stmt\" composite has about 74 heads.

  Parameters (ASDL-ARGS) of a composite form are identical in
  shape to a _tuple form_, e.g.,

    ...(symbol_table s, ident nym, ident* deps, stmt* body)

  5. The _symconst_ group contains symconst speclets like

                symconst speclet
     __________________^___________________
    |                                      |
     term           symconst forms
     _^_    _______________^_______________
    |   |  |                               |
    cmpop = Eq | NotEq | Lt | LtE | Gt | GtE

  where the _symconst forms_ are singleton identifiers, without
  parameter lists in round brackets. These forms are the _heads_
  of the symconst forms -- the symconst speclet above has _term_
  cmpop and its forms have _heads_ Eq, NotEq, etc.

  6. A tuple is a singleton form: a comma-separated list (in round
  brackets) of pairs of ___type___ and ___variable___, like this
  one:

                       tuple speclet
     ________________________^________________________
    |                                                 |
        term                   tuple form
     ____^____     _________________^_________________
    |         |   |                                   |
    array_index = (expr? left, expr? right, expr? step)

  identical in shape to the parameter list of a composite term.

  A tuple form is anonymous, but we gensym a _head_ for it,
  something like asr_234789, so that symconst, composite, and
  tuple forms all have heads.

  There are about 248 heads across all the groups, about six of
  which are heads of tuple forms with gensymmed heads. The tuple
  speclets are things like array_index, attribute_arg, alloc_arg,
  etc. See all-heads-test in core_test.clj.

  7. _parameter lists_ in composite forms and _tuple forms_ have
  the same shapes: pairs of ___types___ and identifiers. A _type_
  can have a _quantitative qualifier_, aka _multiplicity_: STAR or
  QUES, meaning that the variable denotes \"zero or more\" and
  \"at least one\" instance of the type, respectively. For
  instance, in the parameter list of the following composite form,

    Program(symbol_table s, ident nym, ident* deps, stmt* body)

  \"deps\" and \"body\" have STAR multiplicities.

  The default multiplicity, with no STAR or QUES, is \"exactly
  once.\"

  Identifiers are not spec'ced in ASDL. For test generation, an
  identifier spec appears in base_specs.clj. We may extend that to
  a superset of source-language identifiers like those of Fortran
  or Python in the future.

  ## TL;DR SUMMARY:

  The big picture to remember about _terms_ and _heads_ is that a
  _speclet_ looks like one of the following three:

    (a) `term` `=` `composite-`*`head`*`-1` `args`  ;; forms
               `|` `composite-`*`head`*`-2` `args`
               `|` ...

    (b) `term` `=` `symconst-`*`head`*              ;; forms
               `|` `symconst-`*`head`*
               `|`  ...

    (c) `term` `=` `tuple`  ;; one form, anonymous gensymmed head

  A _term_ corresponds to a _bunch_ (one or more) _forms_ with
  _heads_, but each head corresponds to exactly one term.

  In the evaluator in asr.clj, though not in the grammar below,
  each alternative of a term is called a ___node___. For example,
  \"Program,\" \"Module,\" and \"Function\" are all nodes of the
  term \"Symbol.\"

  A term has exactly one _speclet_. The speclet is the term and
  the whole alternation of forms:, left-hand side (term) and
  right-hand side (alternation of forms), one form per head.

  All forms in a speclet are of the same group: composite,
  symconst, or tuple. There won't be a mixture of composite forms
  and symconst forms, for instance, in the right-hand side of
  any speclet.

  The following mnemonic slogans may be helpful:

  :ASDL-SYMCONSTs are heads without parameter lists.

  :ASDL-TUPLEs are parameter-lists with anonymous, gensymmed
  heads.

  :ASDL-COMPOSITEs have symbolic heads and parameter-lists.

  In addition to the 3 groups, it turns out there are 28 terms and
  speclets, and 227 heads, forms, and clojure.specs in the testing
  snapshot of ASR. The numbers grow as features are added to ASR.
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
