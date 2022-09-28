(ns asr.core
  (:gen-class)
  (:require [clojure.spec.alpha            :as s]
            [clojure.pprint                :refer [pprint]]
            [instaparse.core               :as insta]
            [clojure.zip                   :as zip]
            [camel-snake-kebab.core        :as csk]
            [clojure.spec.gen.alpha        :as gen]
            [clojure.spec.test.alpha       :as stest]
            [clojure.test.check.generators :as tgen]
            ))

(println "+-------------------------------+")
(println "|                               |")
(println "|     Try this at the REPL:     |")
(println "|                               |")
(println "|     (-main)                   |")
(println "|     (s/exercise ::binop)      |")
(println "|     (s/describe ::binop)      |")
(println "|                               |")
(println "+-------------------------------+")

(defn echo [x]
  (pprint x) x)  ;; TODO: macro?


(def expr-01-211000
  "Here is the Python that generates the ASR below, from
  <https://github.com/lcompilers/lpython/blob/84a073ce44a9a74213a4ac5648ee783bd38fc90f/tests/expr_01.py>.

```python id=f1020f8e-d252-4b90-b78a-e74a366b580e
def main0():
    x: i32
    x2: i64
    y: f32
    y2: f64
    x = (2+3)*5
    print(x)

main0()
```
  "
  '(TranslationUnit
    (SymbolTable
     1
     {:_lpython_main_program
      (Function
       (SymbolTable 4 {})
       _lpython_main_program
       []
       []
       [(SubroutineCall 1 main0 () [] ())] ()
       Source Public Implementation () .false. .false. .false. .false.),
      :main0
      (Function
       (SymbolTable
        2 {:x (Variable
               2 x Local () () Default
               (Integer 4 []) Source Public Required .false.),
           :x2 (Variable
                2 x2 Local () () Default
                (Integer 8 []) Source Public Required .false.),
           :y (Variable
               2 y Local () () Default
               (Real 4 []) Source Public Required .false.),
           :y2 (Variable
                2 y2 Local () () Default (Real 8 [])
                Source Public Required .false.)})
       main0 [] []
       [(= (Var 2 x)
           (IntegerBinOp
            (IntegerBinOp
             (IntegerConstant
              2 (Integer 4 []))
             Add
             (IntegerConstant
              3
              (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant
              5 (Integer 4 [])))
            Mul
            (IntegerConstant
             5 (Integer 4 []))
            (Integer 4 [])
            (IntegerConstant
             25 (Integer 4 []))) ())
        (Print () [(Var 2 x)] () ())]
       () Source Public Implementation () .false. .false. .false. .false.),
      :main_program
      (Program
       (SymbolTable 3 {})
       main_program []
       [(SubroutineCall 1 _lpython_main_program () [] ())])}) []))


(defn nskw-kebab-from ;; TODO: macro?

  "### Kebab'bed Namespaced Keywords for Specs from Symbols in ASR

  Transform conventional names in ASR PascalCase to conventional
  namespaced keywords in kebab-case in clojure.spec. It works on
  symbols or on strings. We can write a spec for this function,
  too? Specs all the way down!
  "

  [sym-or-string]

  (keyword "asr.core" (name (csk/->kebab-case sym-or-string)))
  ;; Found by experiment that ->> doesn't work, here. Something
  ;; to do with macros.
  #_(->> sym csk/->kebab-case #(keyword "asr.core" %)) )

;;; Spec for nskw-kebab-from

(s/fdef nskw-kebab-from
  :args (s/alt :str string? :sym symbol?)
  :ret keyword?)

(stest/instrument `nskw-kebab-from)


(def all-asr

  "# A Grammar for ASDL Specs

  ## ASDL Spec for ASR

  Here is a big string that contains the ASR.asdl, the spec that
  any ASR instance must conform to. I had to escape the
  double-quote marks by hand.
  "

  "-- Abstract Semantic Representation (ASR) definition

-- The aim of ASR is to represent all semantics in a non-redundant way, and that
-- has all the semantic information available locally, so that the backend can
-- do a single pass over ASR and have all the information at hand to generate
-- code.
--
-- ASR is always semantically valid Fortran code. It is as far from the original
-- Fortran language code as possible (i.e. everything is explicitly figured out,
-- all semantic information gathered and readily available locally from each ASR
-- node), while ensuring no semantic information was lost (no lowering was
-- done), so one can still generate Fortran code from ASR that will be logically
-- equivalent to the original code.
--
-- ASR can be used to do Fortran level transformations (such as optimizations).

-- ASDL's builtin types are:
--   * identifier
--   * int (signed integers of infinite precision)
--   * string
-- We extend these by:
--   * bool (.true. / .false.)
--   * float (floating point number of infinite precision)
--   * symbol_table (scoped Symbol Table implementation)
--   * node (any ASR node)
--
-- Note: `symbol_table` contains `identifier` -> `symbol` mappings.

module ASR {

unit
    = TranslationUnit(symbol_table global_scope, node* items)

-- # Documentation for the symbol type

-- Each symbol has either `symtab` (local symbol table) or `parent_symtab`
-- (where this symbol is stored). One can get to parent_symtab via symtab, so
-- only one is present.

-- Each symbol has a `name` for easy lookup of the name of the symbol when only
-- having a pointer to it.

-- abi=Source means the symbol's implementation is included (full ASR),
-- otherwise it is external (interface ASR, such as procedure interface).

-- SubroutineCall/FunctionCall store the actual final resolved subroutine or
-- function (`name` member). They also store the original symbol
-- (`original_name`), which can be one of: null, GenericProcedure or
-- ExternalSymbol.

-- When a module is compiled, it is parsed into full ASR, an object file is
-- produced, the full ASR (abi=Source, \" body\" is non-empty) is transformed into
-- interface ASR (abi=LFortran, \"body\" is empty). Both interface and full ASR
-- is saved into the mod file.

-- When a module is used, it is first looked up in the symbol table (as either
-- full or interface ASR) and used if it is present. Otherwise a mod file is
-- found on the disk, loaded (as either full or interface ASR for LFortran's
-- mod file, depending on LFortran's compiler options; or for GFortran's mod
-- file the corresponding interface ASR is constructed with abi=GFortran) and
-- used. After the ASR is loaded, the symbols that are used are represented as
-- ExternalSymbols in the current scope of the symbol table.

-- ExternalSymbol represents symbols that cannot be looked up in the current
-- scoped symbol table. As an example, if a variable is defined in a module,
-- but used in a nested subroutine, that is not an external symbol
-- because it can be resolved in the current symbol table (nested subroutine)
-- by following the parents. However if a symbol is used from a different
-- module, then it is an external symbol, because usual symbol resolution by
-- going to the parents will not find the definition. The `module_name` member
-- is the name of the module the symbol is in, the `scope_names` is a list of
-- names if the symbol is in a nested symbol table. For example if it is a
-- local variable in a function `f` that is nested in function `g`, then
-- `scope_names=[g, f]`.

-- REPL: each cell is parsed into full ASR, compiled + executed, the full ASR
-- is transformed into interface ASR (abi=LFortran) and kept in the symbol
-- table. A new cell starts with an empty symbol table, whose parent symbol
-- table is the previous cell. That allows function / declaration shadowing.


symbol
    = Program(symbol_table symtab, identifier name, identifier* dependencies,
        stmt* body)
    | Module(symbol_table symtab, identifier name, identifier* dependencies,
        bool loaded_from_mod, bool intrinsic)
    | Function(symbol_table symtab, identifier name, expr* args,
        ttype* type_params, stmt* body, expr? return_var, abi abi,
        access access, deftype deftype, string? bindc_name, bool elemental,
        bool pure, bool module, bool inline)
    | GenericProcedure(symbol_table parent_symtab, identifier name,
        symbol* procs, access access)
    | CustomOperator(symbol_table parent_symtab, identifier name,
        symbol* procs, access access)
    | ExternalSymbol(symbol_table parent_symtab, identifier name,
        symbol external, identifier module_name, identifier* scope_names,
        identifier original_name, access access)
    | DerivedType(symbol_table symtab, identifier name, identifier* members,
        abi abi, access access, symbol? parent)
    | EnumType(symbol_table symtab, identifier name, identifier* members,
        abi abi, access access, ttype type, symbol? parent)
    | Variable(symbol_table parent_symtab, identifier name, intent intent,
        expr? symbolic_value, expr? value, storage_type storage, ttype type,
        abi abi, access access, presence presence, bool value_attr)
    | ClassType(symbol_table symtab, identifier name, abi abi, access access)
    | ClassProcedure(symbol_table parent_symtab, identifier name, identifier
        proc_name, symbol proc, abi abi)
    | AssociateBlock(symbol_table symtab, identifier name, stmt* body)
    | Block(symbol_table symtab, identifier name, stmt* body)

storage_type = Default | Save | Parameter | Allocatable
access = Public | Private
intent = Local | In | Out | InOut | ReturnVar | Unspecified
deftype = Implementation | Interface
presence = Required | Optional

-- # Documentation for the ABI type

-- External Yes: the symbol's implementation is not part of ASR, the
-- symbol is just an interface (e.g., subroutine/function interface, or variable
-- marked as external, not allocated by this ASR).

-- External No:  the symbol's implementation is part of ASR (e.g.,
-- subroutine/function body is included, variables must be allocated).

-- abi=Source: The symbol's implementation is included in ASR, the backend is
-- free to use any ABI it wants (it might also decide to inline or eliminate
-- the code in optimizations).

-- abi=LFortranModule/GFortranModule/BindC: the symbol's implementation is
-- stored as machine code in some object file that needs to be linked in. It
-- uses the specified ABI (one of LFortran module, GFortran module or C ABI).
-- An interface that uses `iso_c_binding` and `bind(c)` is represented using
-- abi=BindC.

-- abi=Interactive: the symbol's implementation has been provided by the
-- previous REPL execution (e.g., if LLVM backend is used for the interactive
-- mode, the previous execution generated machine code for this symbol's
-- implementation that was loaded into memory). Note: this option might be
-- converted/eliminated to just use LFortran ABI in the future.

-- abi=Intrinsic: the symbol's implementation is implicitly provided by the
-- language itself as an intrinsic function. That means the backend is free to
-- implement it in any way it wants. The function does not have a body, it is
-- just an interface.

abi                   -- External     ABI
    = Source          --   No         Unspecified
    | LFortranModule  --   Yes        LFortran
    | GFortranModule  --   Yes        GFortran
    | BindC           --   Yes        C
    | Interactive     --   Yes        Unspecified
    | Intrinsic       --   Yes        Unspecified


stmt
    = Allocate(alloc_arg* args, expr? stat, expr? errmsg, expr? source)
    | Assign(int label, identifier variable)
    | Assignment(expr target, expr value, stmt? overloaded)
    | Associate(expr target, expr value)
    | Cycle()
    -- deallocates if allocated otherwise throws a runtime error
    | ExplicitDeallocate(symbol* vars)
    -- deallocates if allocated otherwise does nothing
    | ImplicitDeallocate(symbol* vars)
    | DoConcurrentLoop(do_loop_head head, stmt* body)
    | DoLoop(do_loop_head head, stmt* body)
    | ErrorStop(expr? code)
    | Exit()
    | ForAllSingle(do_loop_head head, stmt assign_stmt)
        -- GoTo points to a GoToTarget with the corresponding target_id within
        -- the same procedure. We currently use `int` IDs to link GoTo with
        -- GoToTarget to avoid issues with serialization.
    | GoTo(int target_id)
        -- An empty statement, a target of zero or more GoTo statements
        -- the `id` is only unique within a procedure
    | GoToTarget(int id)
    | If(expr test, stmt* body, stmt* orelse)
    | IfArithmetic(expr test, int lt_label, int eq_label, int gt_label)
    | Print(expr? fmt, expr* values, expr? separator, expr? end)
    | FileOpen(int label, expr? newunit, expr? filename, expr? status)
    | FileClose(int label, expr? unit, expr? iostat, expr? iomsg, expr? err, expr? status)
    | FileRead(int label, expr? unit, expr? fmt, expr? iomsg, expr? iostat, expr? id, expr* values)
    | FileRewind(int label, expr? unit, expr? iostat, expr? err)
    | FileInquire(int label, expr? unit, expr? file, expr? iostat, expr? err,
              expr? exist, expr? opened, expr? number, expr? named,
              expr? name, expr? access, expr? sequential, expr? direct,
              expr? form, expr? formatted, expr? unformatted, expr? recl,
              expr? nextrec, expr? blank, expr? position, expr? action,
              expr? read, expr? write, expr? readwrite, expr? delim,
              expr? pad, expr? flen, expr? blocksize, expr? convert,
              expr? carriagecontrol, expr? iolength)
    | FileWrite(int label, expr? unit, expr? fmt, expr? iomsg, expr? iostat, expr? id, expr* values, expr? separator, expr? end)
    | Return()
    | Select(expr test, case_stmt* body, stmt* default)
    | Stop(expr? code)
    | Assert(expr test, expr? msg)
    | SubroutineCall(symbol name, symbol? original_name, call_arg* args, expr? dt)
    | Where(expr test, stmt* body, stmt* orelse)
    | WhileLoop(expr test, stmt* body)
    | Nullify(symbol* vars)
    | Flush(int label, expr unit, expr? err, expr? iomsg, expr? iostat)
    | ListAppend(expr a, expr ele)
    | AssociateBlockCall(symbol m)
    | CPtrToPointer(expr cptr, expr ptr, expr? shape)
    | BlockCall(int label, symbol m)
    | SetInsert(expr a, expr ele)
    | SetRemove(expr a, expr ele)
    | ListInsert(expr a, expr pos, expr ele)
    | ListRemove(expr a, expr ele)
    | ListClear(expr a)
    | DictInsert(expr a, expr key, expr value)


expr
    = IfExp(expr test, expr body, expr orelse, ttype type, expr? value)
        -- Such as: (x, y+z), (3.0, 2.0) generally not known at compile time
    | ComplexConstructor(expr re, expr im, ttype type, expr? value)
    | NamedExpr(expr target, expr value, ttype type)
    | FunctionCall(symbol name, symbol? original_name,
            call_arg* args, ttype type, expr? value, expr? dt)
    | DerivedTypeConstructor(symbol dt_sym, expr* args, ttype type, expr? value)
    | EnumTypeConstructor(symbol dt_sym, expr* args, ttype type, expr? value)
    | ImpliedDoLoop(expr* values, expr var, expr start, expr end,
                    expr? increment, ttype type, expr? value)
    | IntegerConstant(int n, ttype type)
    | IntegerBOZ(int v, integerboz intboz_type, ttype? type)
    | IntegerBitNot(expr arg, ttype type, expr? value)
    | IntegerUnaryMinus(expr arg, ttype type, expr? value)
    | IntegerCompare(expr left, cmpop op, expr right, ttype type, expr? value)
    | IntegerBinOp(expr left, binop op, expr right, ttype type, expr? value)
    | RealConstant(float r, ttype type)
    | RealUnaryMinus(expr arg, ttype type, expr? value)
    | RealCompare(expr left, cmpop op, expr right, ttype type, expr? value)
    | RealBinOp(expr left, binop op, expr right, ttype type, expr? value)
    | ComplexConstant(float re, float im, ttype type)
    | ComplexUnaryMinus(expr arg, ttype type, expr? value)
    | ComplexCompare(expr left, cmpop op, expr right, ttype type, expr? value)
    | ComplexBinOp(expr left, binop op, expr right, ttype type, expr? value)
    | LogicalConstant(bool value, ttype type)
    | LogicalNot(expr arg, ttype type, expr? value)
    | LogicalCompare(expr left, cmpop op, expr right, ttype type, expr? value)
    | LogicalBinOp(expr left, logicalbinop op, expr right, ttype type, expr? value)
    | TemplateBinOp(expr left, binop op, expr right, ttype type, expr? value)

    | ListConstant(expr* args, ttype type)
    | ListLen(expr arg, ttype type, expr? value)
    | ListConcat(expr left, expr right, ttype type, expr? value)

    | SetConstant(expr* elements, ttype type)
    | SetLen(expr arg, ttype type, expr? value)

    | TupleConstant(expr* elements, ttype type)
    | TupleLen(expr arg, ttype type, expr value)

    | StringConstant(string s, ttype type)
    | StringConcat(expr left, expr right, ttype type, expr? value)
    | StringRepeat(expr left, expr right, ttype type, expr? value)
    | StringLen(expr arg, ttype type, expr? value)
    | StringItem(expr arg, expr idx, ttype type, expr? value)
    | StringSection(expr arg, expr? start, expr? end, expr? step, ttype type, expr? value)
    | StringCompare(expr left, cmpop op, expr right, ttype type, expr? value)
    | StringOrd(expr arg, ttype type, expr? value)
    | StringChr(expr arg, ttype type, expr? value)

    | DictConstant(expr* keys, expr* values, ttype type)
    | DictLen(expr arg, ttype type, expr? value)

    | Var(symbol v)

    | ArrayConstant(expr* args, ttype type)
    | ArrayItem(expr v, array_index* args, ttype type, expr? value)
    | ArraySection(expr v, array_index* args, ttype type, expr? value)
    | ArraySize(expr v, expr? dim, ttype type, expr? value)
    | ArrayBound(expr v, expr? dim, ttype type, arraybound bound,
                 expr? value)
    | ArrayTranspose(expr matrix, ttype type, expr? value)
    | ArrayMatMul(expr matrix_a, expr matrix_b, ttype type, expr? value)
    | ArrayPack(expr array, expr mask, expr? vector, ttype type, expr? value)
    | ArrayReshape(expr array, expr shape, ttype type, expr? value)

    | BitCast(expr source, expr mold, expr? size, ttype type, expr? value)
    | DerivedRef(expr v, symbol m, ttype type, expr? value)
    | EnumRef(symbol v, symbol? m, string property, ttype type, expr? value)
    | OverloadedCompare(expr left, cmpop op, expr right, ttype type, expr? value, expr overloaded)
    | OverloadedBinOp(expr left, binop op, expr right, ttype type, expr? value, expr overloaded)
    | Cast(expr arg, cast_kind kind, ttype type, expr? value)
    | ComplexRe(expr arg, ttype type, expr? value)
    | ComplexIm(expr arg, ttype type, expr? value)
    | DictItem(expr a, expr key, expr? default, ttype type, expr? value)
    | CLoc(expr arg, ttype type, expr? value)
    | PointerToCPtr(expr arg, ttype type, expr? value)
    | GetPointer(expr arg, ttype type, expr? value)
    | ListItem(expr a, expr pos, ttype type, expr? value)
    | TupleItem(expr a, expr pos, ttype type, expr? value)
    | ListSection(expr a, array_index section, ttype type, expr? value)
    | ListPop(expr a, expr? index, ttype type, expr? value)
    | DictPop(expr a, expr key, ttype type, expr? value)
    | SetPop(expr a, ttype type, expr? value)
    | IntegerBitLen(expr a, ttype type, expr? value)


-- `len` in Character:
-- >=0 ... the length of the string, known at compile time
--  -1 ... character(*), i.e., inferred at runtime
--  -2 ... character(:), allocatable (possibly we might use -1 for that also)
--  -3 ... character(n+3), i.e., a runtime expression stored in `len_expr`

-- kind: The `kind` member selects the kind of a given type. We currently
-- support the following:
-- Integer kinds: 1 (i8), 2 (i16), 4 (i32), 8 (i64)
-- Real kinds: 4 (f32), 8 (f64)
-- Complex kinds: 4 (c32), 8 (c64)
-- Character kinds: 1 (utf8 string)
-- Logical kinds: 1, 2, 4: (boolean represented by 1, 2, 4 bytes; the default
--     kind is 4, just like the default integer kind, consistent with Python
--     and Fortran: in Python \"Booleans in Python are implemented as a subclass
--     of integers\", in Fortran the \"default logical kind has the same storage
--     size as the default integer\"; we currently use kind=4 as default
--     integer, so we also use kind=4 for the default logical.)

ttype
    = Integer(int kind, dimension* dims)
    | Real(int kind, dimension* dims)
    | Complex(int kind, dimension* dims)
    | Character(int kind, int len, expr? len_expr, dimension* dims)
    | Logical(int kind, dimension* dims)
    | Set(ttype type)
    | List(ttype type)
    | Tuple(ttype* type)
    | Derived(symbol derived_type, dimension* dims)
    | Enum(symbol enum_type, dimension *dims)
    | Class(symbol class_type, dimension* dims)
    | Dict(ttype key_type, ttype value_type)
    | Pointer(ttype type)
    | CPtr()
    | TypeParameter(identifier param, dimension* dims, restriction* rt)

restriction = Restriction(trait rt)

trait = SupportsZero | SupportsPlus | Divisible | Any

binop = Add | Sub | Mul | Div | Pow | BitAnd | BitOr | BitXor | BitLShift | BitRShift

logicalbinop = And | Or | Xor | NEqv | Eqv

cmpop = Eq | NotEq | Lt | LtE | Gt | GtE

integerboz = Binary | Hex | Octal

arraybound = LBound | UBound

cast_kind
    = RealToInteger
    | IntegerToReal
    | LogicalToReal
    | RealToReal
    | TemplateToReal
    | IntegerToInteger
    | RealToComplex
    | IntegerToComplex
    | IntegerToLogical
    | RealToLogical
    | CharacterToLogical
    | CharacterToInteger
    | CharacterToList
    | ComplexToLogical
    | ComplexToComplex
    | ComplexToReal
    | LogicalToInteger
    | RealToCharacter
    | IntegerToCharacter
    | LogicalToCharacter

dimension = (expr? start, expr? length)

alloc_arg = (symbol a, dimension* dims)

attribute = Attribute(identifier name, attribute_arg *args)

attribute_arg = (identifier arg)

call_arg = (expr? value)

tbind = Bind(string lang, string name)

array_index = (expr? left, expr? right, expr? step)

do_loop_head = (expr? v, expr? start, expr? end, expr? increment)

case_stmt = CaseStmt(expr* test, stmt* body) | CaseStmt_Range(expr? start, expr? end, stmt* body)

}")


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


(def asdl-parser (insta/parser asdl-grammar))


(def asr-pre-spec

  "Capture the parse of ASR.asdl into hiccup format
  **(**<https://github.com/weavejester/hiccup>**).** "

  (asdl-parser all-asr))


(def speclets
  "## Raw Hiccup for all Speclets

  Strip off the `module` info, leaving only ASDL-DEFs, i.e.,
  *speclets*.
  "
  (vec (rest
        ((-> (zip/vector-zip asr-pre-spec)
             zip/down zip/right zip/right) 0))))


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


;;  _            _                           __                   _     _
;; | |_  __ _ __| |_  _ __  __ _ _ __   ___ / _|  ____ __  ___ __| |___| |_ ___
;; | ' \/ _` (_-< ' \| '  \/ _` | '_ \ / _ \  _| (_-< '_ \/ -_) _| / -_)  _(_-<
;; |_||_\__,_/__/_||_|_|_|_\__,_| .__/ \___/_|   /__/ .__/\___\__|_\___|\__/__/
;;                              |_|                 |_|
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
  [(keyword "asr.core" (:ASDL-TERM speclet-map)) ;; no kebab'bing!
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
        kwh (keyword "asr.core" ghead)] ;; no kebab'bing
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


(def symconst-stuffs
  "# All Symconst Specs

  Clojure.specs for symconsts are easiest because they don't
  depend on other clojure.specs. There are about 72
  symconsts (more as ASR grows):

  ## Symconst Stuffs
  "
  (filter #(= (:kind %) :ASDL-SYMCONST) big-list-of-stuff))


(defn spec-from-symconst-stuff
  "## Symconst-Head-Specs

  This next code block REGISTERS the (about) 72 head-specs by
  `eval`'ing the `s/defs` written by `` `(s/def ...) ``. A spec is
  *registered* into a hidden Clojure Spec Registry by side-effect
  and is associated with the namespaced keyword produced by
  `nskw-kebab-from`. Once this next code block runs, we'll
  have (about) 72 head-specs magically registered and we can refer
  to them by namespaced kebab'bed keyword name. For example,
  `::implementation` will be registered and we can refer to it
  via `(s/spec ::implementation)`.

  All specs, head-specs and term-specs alike must be registered
  before being referred-to. Later, we'll break co-recursive cycles
  by registering defective specs then backpatching them. For
  example, the term-spec for `::symbol` refers to the term-spec
  for `::symbol-table`, which refers to the term-spec for
  `::symbol`. Clojure.spec can't tolerate that, but it can
  tolerate a defective term-spec for `::symbol-table` that we
  backpatch later.

  Construct and register all (approximately) 72 symconst
  head-specs:
  "
  [symconst-stuff]
  (let [symconst (-> symconst-stuff :form :ASDL-SYMCONST)
        nskw (nskw-kebab-from symconst)]
    `(s/def ~nskw #{(quote ~(symbol symconst))})))


(def symconst-stuffss-by-term
  "## Symconst-Term-Specs

  To check an instance or utterance of ASR like expr-221000, we'll
  need to check its sub-parts by term, not by head.

  ### Symconst Stuffss sic by Term

  First, partition the symconst specs by term. There are about 13
  terms categorizing the (approx) 72 symconst heads.
  "
  (partition-by :term symconst-stuffs))


(defn symconst-spec-for-term
  "### Symconst Spec for Term sic

  For each term, write a `set` containing its alternative heads,
  e.g., the term `binop` is one of the ten heads `Add`, `Sub`, and
  so on, to `BitRShift`.

  To unit-test `spec-for-term`, `eval` one of them and check it:
  "
  [stuffs-for-term]
  (let [term (-> stuffs-for-term first :term)
        ;; same for all! TODO: assert
        term-nskw (nskw-kebab-from (name term))
        ss1
        (->> stuffs-for-term
             (map (fn [stuff]
                    (let [head
                          (head-from-kind-form
                           (:kind stuff) (:form stuff))]
                      (-> head symbol)))))]
    `(s/def ~term-nskw (set (quote ~ss1)))))


;;; Experiment that failed.

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


(defn heads-for-composite
  "Produce a list of symbolic heads (like 'RealUnaryMinus and
  'ArraySection), from a term like :asr.core/expr. See
  all-heads-for-exprs-test in core_test.clj."
  [term]
  (->> big-map-of-speclets-from-terms
       term
       (map :ASDL-COMPOSITE)
       (map :ASDL-HEAD)
       (map symbol)
       set))

(defn dummy-generator-for-heads
  "A dummy generator for argument lists for heads which just
  inserts a list of random length of random identifiers. Not
  suitable long-term."
  [heads]
  (tgen/let [head (s/gen heads)
             rest (gen/list (s/gen ::identifier))]
    (cons head rest)))

(defn dummy-lpred
  "A predicate for dummy specs that checks simply that the
  instance is a list with an appropriate head and zero or
  more items of any type. Not suitable long-term."
  [heads]
  (s/and seq?
         (fn [lyst] (-> lyst count (>= 1)))
         (fn [lyst] (-> lyst first heads))))


;;                        __                                     _ _
;;  ____ __  ___ __ ___  / _|___ _ _   __ ___ _ __  _ __  ___ __(_) |_ ___ ___
;; (_-< '_ \/ -_) _(_-< |  _/ _ \ '_| / _/ _ \ '  \| '_ \/ _ (_-< |  _/ -_|_-<
;; /__/ .__/\___\__/__/ |_| \___/_|   \__\___/_|_|_| .__/\___/__/_|\__\___/__/
;;    |_|                                          |_|


;;; Try (s/exercise ::symbol) and (s/exercise ::expr in the REPL.

(let [heads (heads-for-composite ::symbol)]
  (s/def ::symbol
    (s/with-gen
      (dummy-lpred heads)
      (fn [] (dummy-generator-for-heads heads)))))

(let [heads (heads-for-composite ::expr)]
  (s/def ::expr
    (s/with-gen
      (dummy-lpred heads)
      (fn [] (dummy-generator-for-heads heads)))))

(let [heads (heads-for-composite ::stmt)]
  (s/def ::stmt
    (s/with-gen
      (dummy-lpred heads)
      (fn [] (dummy-generator-for-heads heads)))))


;;  __ _ _ _ __ _ ___
;; / _` | '_/ _` (_-<
;; \__,_|_| \__, /__/
;;          |___/


(defn spec-from-arg
  "### Spec Fragment from Arg, Args

  Convert multiplicities into clojure.spec equivalents.
  "
  [arg]
  (let [type (nskw-kebab-from (:ASDL-TYPE arg))
        nym (:ASDL-NYM arg)]
    (case (:MULTIPLICITY arg)
      ::once `(s/spec ~type)
      ::at-most-once `(s/? (s/spec ~type))
      ::zero-or-more `(s/* (s/spec ~type)))))


(defn spec-from-args [args]
  (let [nyms (->> args (map :ASDL-NYM)           #_echo)
        kyms (->> nyms (map (comp keyword name)) #_echo)
        specules (->> args (map spec-from-arg)   #_echo)
        riffle (-> (interleave kyms specules)    #_echo)]
    `(s/cat ~@riffle)))


(defn spec-from-head-and-args [head args]
  (let [nyms (->> args (map :ASDL-NYM)           #_echo)
        kyms (->> nyms (map (comp keyword name)) #_echo)
        specules (->> args (map spec-from-arg)   #_echo)
        riffle (-> (interleave kyms specules)    #_echo)
        lpred  #(= % head)
        headed (cons :head (cons lpred riffle))]
    `(s/cat ~@headed)))

;;  _             _
;; | |_ _  _ _ __| |___ ___
;; |  _| || | '_ \ / -_|_-<
;;  \__|\_,_| .__/_\___/__/
;;          |_|


(defn tuple-head-spec-from-stuff [tuple-stuff]
  #_(println "tuple-head-spec-from-stuff")
  (let [nskw (-> tuple-stuff :head name nskw-kebab-from #_echo)
        args (-> tuple-stuff :form :ASDL-ARGS           #_echo)]
    `(s/def ~nskw ~(spec-from-args args))))  ;; side effect!


(def tuple-stuffs
  "# Tuple Specs

  There are six tuple heads. Their names will change from
  run-to-run because the names are gensymmed.
  "
  (filter #(= (:kind %) :ASDL-TUPLE)
          big-list-of-stuff))


(def tuple-stuffss-by-term
  "## Tuple Term-Specs

  As before, we really need clojure.specs for the terms
  corresponding to the heads.

  ### Tuple Stuffss [sic] by Term (one extra level of lists)
  "
  (partition-by :term tuple-stuffs))


;; ### Register

;; To register the 6 term-specs, `eval` them.

(defn tuple-term-spec-from-stuffs [stuffs]
  #_(println "tuple-term-spec-from-stuffs")
  (let [term (-> stuffs first :term                      #_echo)
        nskw (-> term name nskw-kebab-from               #_echo)
        head (-> stuffs first :head name nskw-kebab-from #_echo)]
    `(s/def ~nskw (s/spec ~head))))


;;   __            __                   __
;;  / /___ _____  / /__   ______ ______/ /__
;; / __/ // / _ \/ / -_) / __/ // / __/ / -_)
;; \__/\_,_/ .__/_/\__/  \__/\_, /\__/_/\__/
;;        /_/               /___/
;;    __                __    _
;;   / /  _______ ___ _/ /__ (_)__  ___ _
;;  / _ \/ __/ -_) _ `/  '_// / _ \/ _ `/
;; /_.__/_/  \__/\_,_/_/\_\/_/_//_/\_, /
;;                                /___/


;;; Manual topological sort shows we must spec ::dimension before
;;; the others.

(defn do-one-tuple-spec-head-and-term!
  "Spec one tuple type, head-spec and term-spec, by term."
  [term]

  (->> tuple-stuffs
      (filter #(= term (-> % :term)))
      (map tuple-head-spec-from-stuff)
      (map eval)
      echo)

 (->> tuple-stuffss-by-term
      (filter #(= term (-> % first :term)))
      (map tuple-term-spec-from-stuffs)
      (map eval)
      echo))


;;                _         _     _        _    _
;;  ____  _ _ __ | |__  ___| |___| |_ __ _| |__| |___
;; (_-< || | '  \| '_ \/ _ \ |___|  _/ _` | '_ \ / -_)
;; /__/\_, |_|_|_|_.__/\___/_|    \__\__,_|_.__/_\___|
;;     |__/


;; # Hand-Written Term Spec for SymbolTable

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
   :symbols (s/map-of keyword?
                      (s/spec ::symbol))))


(defn spec-from-composite
  "# Backpatching Symbol

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


;;                       _        _
;;  ____ __  ___ __   __| |_ __ _| |_ ___
;; (_-< '_ \/ -_) _| (_-<  _/ _` |  _(_-<
;; /__/ .__/\___\__| /__/\__\__,_|\__/__/
;;    |_|


(defn only-asr-specs []
  (filter
   #(= (namespace %) "asr.core")
   (keys (s/registry))))


(defn check-registry
  "Print specs defined in the namespace 'asr.core.'"
  []
  (pprint (only-asr-specs)))


(defn count-asr-specs []
  (count (only-asr-specs)))


;;             _
;;  _ __  __ _(_)_ _
;; | '  \/ _` | | ' \
;; |_|_|_\__,_|_|_||_|


(defn -main
  "Please see the tests. Main doesn't do a whole lot ... yet."
  [& args]

  ;; Function specs for nskw-kebab-from.

  (s/fdef nskw-kebab-from
    :args (s/alt :str string? :sym symbol?)
    :ret keyword?)

  (stest/instrument `nskw-kebab-from)

  ;; Building up the spec registry by side-effect.

  (print "symconst head specs: ")

  (->> symconst-stuffs
       (map spec-from-symconst-stuff)
       (map eval)
       count
       echo)

  (print "symconst term specs: ")

  (->> symconst-stuffss-by-term
       (map symconst-spec-for-term)
       (map eval)
       count
       echo)

;;; We need a cycle-breaking spec for dimension to bootstrap the
;;; following constructions.

  (println "cycle-breaking with ::dimension")

  (do-one-tuple-spec-head-and-term! ::dimension)

  (println "tuple heads and terms are 1-to-1, unlike symconsts an composites.")
  (print "tuple head specs: ")

  (->> tuple-stuffs
       (map tuple-head-spec-from-stuff)
       (map eval)
       count
       echo)

  (print "tuple term specs: ")

  (->> tuple-stuffss-by-term
       (map tuple-term-spec-from-stuffs)
       #_echo
       (map eval)
       count
       echo)

  ;;  _ _               _         _
  ;; (_|_)____  _ _ __ | |__  ___| |
  ;;  _ _(_-< || | '  \| '_ \/ _ \ |
  ;; (_|_)__/\_, |_|_|_|_.__/\___/_|
  ;;         |__/

  (println "dummy spec for symbol: ")

  (let [heads (heads-for-composite ::symbol)]
    (->> (s/def ::symbol
           (s/with-gen
             (dummy-lpred heads)
             (fn [] (dummy-generator-for-heads heads))))
         echo))
  (pprint (s/describe ::symbol))

  ;;  _ _
  ;; (_|_)_____ ___ __ _ _
  ;;  _ _/ -_) \ / '_ \ '_|
  ;; (_|_)___/_\_\ .__/_|
  ;;             |_|

  (println "dummy spec for expr: ")

  (let [heads (heads-for-composite ::expr)]
    (->> (s/def ::expr
           (s/with-gen
             (dummy-lpred heads)
             (fn [] (dummy-generator-for-heads heads))))
         echo))
  (pprint (s/describe ::expr))

  ;;  _ _    _        _
  ;; (_|_)__| |_ _ __| |_
  ;;  _ _(_-<  _| '  \  _|
  ;; (_|_)__/\__|_|_|_\__|

  (println "dummy spec for stmt: ")

  (let [heads (heads-for-composite ::stmt)]
    (->> (s/def ::stmt
           (s/with-gen
             (dummy-lpred heads)
             (fn [] (dummy-generator-for-heads heads))))
         echo))
  (pprint (s/describe ::stmt))

  ;;  _ _ _   _
  ;; (_|_) |_| |_ _  _ _ __  ___
  ;;  _ _|  _|  _| || | '_ \/ -_)
  ;; (_|_)\__|\__|\_, | .__/\___|
  ;;              |__/|_|

  (println "dummy spec for ttupe: ")

  (let [heads (heads-for-composite ::ttype)]
    (->> (s/def ::ttype
           (s/with-gen
             (dummy-lpred heads)
             (fn [] (dummy-generator-for-heads heads))))
         echo))
  (pprint (s/describe ::ttype))

  ;; This isn't good enough. Let's write some head specs for it by
  ;; hand.

  (let [integer-bin-op-stuff
        (filter #(= (:head %) :asr.core/IntegerBinOp)
                big-list-of-stuff)]
    (-> (spec-from-composite
         (-> integer-bin-op-stuff
             first
             :form
             :ASDL-COMPOSITE
             echo))
        echo
        eval))

  ;;  _       _        _                   _
  ;; | |_ ___| |_ __ _| |  __ ___ _  _ _ _| |_
  ;; |  _/ _ \  _/ _` | | / _/ _ \ || | ' \  _|
  ;;  \__\___/\__\__,_|_| \__\___/\_,_|_||_\__|

  (print "total number of specs registered: ")
  (println (count-asr-specs))

  ;; (pprint (s/exercise ::identifier))
  ;; (pprint (s/exercise ::expr))
  ;; (pprint (s/exercise ::dimension))


  (println "Please see the tests. Main doesn't do a whole lot ... yet."))
