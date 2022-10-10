# asr

Test generator for ASR (https://github.com/lcompilers/lpython/wiki/ASR-Design)

## Installation

Prerequisite: [Leiningen and Clojure](https://leiningen.org/)

```
cd -=|[some convenient directory]|=-
git clone https://github.com/rebcabin/ClojureProjects002
```

## Usage

Learn the code by reading, running, and writing tests.

```
cd ClojureProjects002/asr
lein test
```

## Development

[CIDER](https://docs.cider.mx/cider/index.html) is almost
essential. We make heavy use of its debugger (C-u C-M-x) and its
pretty-print-to-comment (C-c C-v C-f C-c e). In-place evaluation
(C-c e, C-M-x) is also priceless.

See also `echo` in `utils.clj` for standard "printf" debugging in
the REPL.

Many other things, like paredit and multiple-cursors, make
Clojure programming the most joyous experience in the industry.
Look for the video series "Emacs Rocks." There is no close
second, except maybe Python's pudb.

## Generating Documentation

See https://github.com/weavejester/codox.

```
lein codox
```

Codox doesn't handle specs. You'll have to read comments in
`autodocs.clj`, `spec.clj`, `core.clj`. Sorry about that. TODO:
figure out a work-around.

Look for generated docs in

```
target
`-- default
    `-- doc
        |-- asr.asr.html
        |-- asr.autospecs.html
        |-- asr.core.html
        |-- asr.data.html
        |-- asr.grammar.html
        |-- asr.parsed.html
        |-- asr.specs.html
        |-- asr.utils.html
        |-- index.html
        `-- intro.html
```

## Theory of Operation

The grammar for [ASR](https://github.com/lcompilers/libasr) is
written in ASDL.

The grammar for ASDL is written in
[instaparse](https://github.com/Engelberg/instaparse). Instaparse
gives us a parser for the ASR written in ASDL. From that, we get
lots of autospecs (see `autospecs.clj`) in
[`clojure.spec`](https://www.google.com/search?client=firefox-b-1-d&q=clojure.spec.alpha).
We write lots of those specs automatically in `autoparse.clj`.
We also write some specs by hand, committed ones in `specs.cls`
and experimental ones in `core.clj`.

Once we have specs, we generate test strings for ASR by various
hooks and crooks.

The development horizon is in `core.clj`. As stuff evolves from
experimental into production, it migrates into `specs.clj`

## Naming Conventions

ASR heads, like

```
IntegerBinOp(expr left, binop op, expr right, ttype type, expr? value)
```

are in PascalCase. The corresponding clojure.specs are in
kebab-case, as in

```clojure
(s/describe :asr.autospecs/integer-bin-op)
;; => (cat
;;     :head  #function[asr.autospecs/spec-from-head-and-args/lpred--2572]
;;     :left  (spec :asr.autospecs/expr)
;;     :op    (spec :asr.autospecs/binop)
;;     :right (spec :asr.autospecs/expr)
;;     :type  (spec :asr.autospecs/ttype)
;;     :value (? (spec :asr.autospecs/expr)))
```

Converting a name to kebab-case is ***kebabulating***. The
function for doing that is `nskw-kebab-from` in `utils.clj` *nskw*
means *namespaced keyword*. Clojure.spec requires spec names to be
namespaced keywords, as in `:asr.autospecs/integer-bin-op`

## Namespaces and DataFlow

```
+-----------------+
| asr.asr/all-asr |    +-----------------+
|                 |    | asr.grammar/    |
| grammar for ASR |    |   asdl-grammar  |
|    in ADSL      |    +--------.--------+
+--------.--------+             |
         |                      |
         v                      v
 ,-------'-------.      ,-------'-------.
(   asdl-parser   )<---(  insta/parser   )
 `-------.-------'      `---------------'
         |
         v
+--------'--------+     ,----------------.
| asr.grammar/    |    | clojure.spec     |
|    asr-pre-spec |    |        .alpha/   |
+--------.--------+    | clojure.spec.gen |
         |             |        .alpha/   |
         v             | clojure.test     |
 ,-------'-------.     |        .check    |
|  asr.parsed/    |    |    .generators/  |
|    speclets     |     `-------.--------'
 `-------.-------'              ^
         |                      |
         v              ,-------'-------.
+--------'--------+    |  asr.autospecs/ |
|big-list-of-stuff|--->|    namespace    |
+-----------------+    |  various tools  |
                        `---------------'
```

## Specs Defined by this Program

```clojure
01 :asr.autospecs/abi
02 :asr.autospecs/access
03 :asr.autospecs/alloc_arg
04 :asr.autospecs/array_index
05 :asr.autospecs/arraybound
06 :asr.autospecs/attribute
07 :asr.autospecs/attribute_arg
08 :asr.autospecs/binop
09 :asr.autospecs/call_arg
10 :asr.autospecs/case_stmt
11 :asr.autospecs/cast_kind
12 :asr.autospecs/cmpop
13 :asr.autospecs/deftype
14 :asr.autospecs/dimension
15 :asr.autospecs/do_loop_head
16 :asr.autospecs/expr
17 :asr.autospecs/integerboz
18 :asr.autospecs/intent
19 :asr.autospecs/logicalbinop
20 :asr.autospecs/presence
21 :asr.autospecs/restriction
22 :asr.autospecs/stmt
23 :asr.autospecs/storage_type
24 :asr.autospecs/symbol
25 :asr.autospecs/tbind
26 :asr.autospecs/trait
27 :asr.autospecs/ttype
28 :asr.autospecs/unit
```

```clojure
001 :asr.autospecs/Add
002 :asr.autospecs/Allocatable
003 :asr.autospecs/Allocate
004 :asr.autospecs/And
005 :asr.autospecs/Any
006 :asr.autospecs/ArrayBound
007 :asr.autospecs/ArrayConstant
008 :asr.autospecs/ArrayItem
009 :asr.autospecs/ArrayMatMul
010 :asr.autospecs/ArrayPack
011 :asr.autospecs/ArrayReshape
012 :asr.autospecs/ArraySection
013 :asr.autospecs/ArraySize
014 :asr.autospecs/ArrayTranspose
015 :asr.autospecs/Assert
016 :asr.autospecs/Assign
017 :asr.autospecs/Assignment
018 :asr.autospecs/Associate
019 :asr.autospecs/AssociateBlock
020 :asr.autospecs/AssociateBlockCall
021 :asr.autospecs/Attribute
022 :asr.autospecs/Binary
023 :asr.autospecs/Bind
024 :asr.autospecs/BindC
025 :asr.autospecs/BitAnd
026 :asr.autospecs/BitCast
027 :asr.autospecs/BitLShift
028 :asr.autospecs/BitOr
029 :asr.autospecs/BitRShift
030 :asr.autospecs/BitXor
031 :asr.autospecs/Block
032 :asr.autospecs/BlockCall
033 :asr.autospecs/CLoc
034 :asr.autospecs/CPtr
035 :asr.autospecs/CPtrToPointer
036 :asr.autospecs/CaseStmt
037 :asr.autospecs/CaseStmt_Range
038 :asr.autospecs/Cast
039 :asr.autospecs/Character
040 :asr.autospecs/CharacterToInteger
041 :asr.autospecs/CharacterToList
042 :asr.autospecs/CharacterToLogical
043 :asr.autospecs/Class
044 :asr.autospecs/ClassProcedure
045 :asr.autospecs/ClassType
046 :asr.autospecs/Complex
047 :asr.autospecs/ComplexBinOp
048 :asr.autospecs/ComplexCompare
049 :asr.autospecs/ComplexConstant
050 :asr.autospecs/ComplexConstructor
051 :asr.autospecs/ComplexIm
052 :asr.autospecs/ComplexRe
053 :asr.autospecs/ComplexToComplex
054 :asr.autospecs/ComplexToLogical
055 :asr.autospecs/ComplexToReal
056 :asr.autospecs/ComplexUnaryMinus
057 :asr.autospecs/CustomOperator
058 :asr.autospecs/Cycle
059 :asr.autospecs/Default
060 :asr.autospecs/Derived
061 :asr.autospecs/DerivedRef
062 :asr.autospecs/DerivedType
063 :asr.autospecs/DerivedTypeConstructor
064 :asr.autospecs/Dict
065 :asr.autospecs/DictConstant
066 :asr.autospecs/DictInsert
067 :asr.autospecs/DictItem
068 :asr.autospecs/DictLen
069 :asr.autospecs/DictPop
070 :asr.autospecs/Div
071 :asr.autospecs/Divisible
072 :asr.autospecs/DoConcurrentLoop
073 :asr.autospecs/DoLoop
074 :asr.autospecs/Enum
075 :asr.autospecs/EnumRef
076 :asr.autospecs/EnumType
077 :asr.autospecs/EnumTypeConstructor
078 :asr.autospecs/Eq
079 :asr.autospecs/Eqv
080 :asr.autospecs/ErrorStop
081 :asr.autospecs/Exit
082 :asr.autospecs/ExplicitDeallocate
083 :asr.autospecs/ExternalSymbol
084 :asr.autospecs/FileClose
085 :asr.autospecs/FileInquire
086 :asr.autospecs/FileOpen
087 :asr.autospecs/FileRead
088 :asr.autospecs/FileRewind
089 :asr.autospecs/FileWrite
090 :asr.autospecs/Flush
091 :asr.autospecs/ForAllSingle
092 :asr.autospecs/Function
093 :asr.autospecs/FunctionCall
094 :asr.autospecs/GFortranModule
095 :asr.autospecs/GenericProcedure
096 :asr.autospecs/GetPointer
097 :asr.autospecs/GoTo
098 :asr.autospecs/GoToTarget
099 :asr.autospecs/Gt
100 :asr.autospecs/GtE
101 :asr.autospecs/Hex
102 :asr.autospecs/If
103 :asr.autospecs/IfArithmetic
104 :asr.autospecs/IfExp
105 :asr.autospecs/Implementation
106 :asr.autospecs/ImplicitDeallocate
107 :asr.autospecs/ImpliedDoLoop
108 :asr.autospecs/In
109 :asr.autospecs/InOut
110 :asr.autospecs/Integer
111 :asr.autospecs/IntegerBOZ
112 :asr.autospecs/IntegerBinOp
113 :asr.autospecs/IntegerBitLen
114 :asr.autospecs/IntegerBitNot
115 :asr.autospecs/IntegerCompare
116 :asr.autospecs/IntegerConstant
117 :asr.autospecs/IntegerToCharacter
118 :asr.autospecs/IntegerToComplex
119 :asr.autospecs/IntegerToInteger
120 :asr.autospecs/IntegerToLogical
121 :asr.autospecs/IntegerToReal
122 :asr.autospecs/IntegerUnaryMinus
123 :asr.autospecs/Interactive
124 :asr.autospecs/Interface
125 :asr.autospecs/Intrinsic
126 :asr.autospecs/LBound
127 :asr.autospecs/LFortranModule
128 :asr.autospecs/List
129 :asr.autospecs/ListAppend
130 :asr.autospecs/ListClear
131 :asr.autospecs/ListConcat
132 :asr.autospecs/ListConstant
133 :asr.autospecs/ListInsert
134 :asr.autospecs/ListItem
135 :asr.autospecs/ListLen
136 :asr.autospecs/ListPop
137 :asr.autospecs/ListRemove
138 :asr.autospecs/ListSection
139 :asr.autospecs/Local
140 :asr.autospecs/Logical
141 :asr.autospecs/LogicalBinOp
142 :asr.autospecs/LogicalCompare
143 :asr.autospecs/LogicalConstant
144 :asr.autospecs/LogicalNot
145 :asr.autospecs/LogicalToCharacter
146 :asr.autospecs/LogicalToInteger
147 :asr.autospecs/LogicalToReal
148 :asr.autospecs/Lt
149 :asr.autospecs/LtE
150 :asr.autospecs/Module
151 :asr.autospecs/Mul
152 :asr.autospecs/NEqv
153 :asr.autospecs/NamedExpr
154 :asr.autospecs/NotEq
155 :asr.autospecs/Nullify
156 :asr.autospecs/Octal
157 :asr.autospecs/Optional
158 :asr.autospecs/Or
159 :asr.autospecs/Out
160 :asr.autospecs/OverloadedBinOp
161 :asr.autospecs/OverloadedCompare
162 :asr.autospecs/Parameter
163 :asr.autospecs/Pointer
164 :asr.autospecs/PointerToCPtr
165 :asr.autospecs/Pow
166 :asr.autospecs/Print
167 :asr.autospecs/Private
168 :asr.autospecs/Program
169 :asr.autospecs/Public
170 :asr.autospecs/Real
171 :asr.autospecs/RealBinOp
172 :asr.autospecs/RealCompare
173 :asr.autospecs/RealConstant
174 :asr.autospecs/RealToCharacter
175 :asr.autospecs/RealToComplex
176 :asr.autospecs/RealToInteger
177 :asr.autospecs/RealToLogical
178 :asr.autospecs/RealToReal
179 :asr.autospecs/RealUnaryMinus
180 :asr.autospecs/Required
181 :asr.autospecs/Restriction
182 :asr.autospecs/Return
183 :asr.autospecs/ReturnVar
184 :asr.autospecs/Save
185 :asr.autospecs/Select
186 :asr.autospecs/Set
187 :asr.autospecs/SetConstant
188 :asr.autospecs/SetInsert
189 :asr.autospecs/SetLen
190 :asr.autospecs/SetPop
191 :asr.autospecs/SetRemove
192 :asr.autospecs/Source
193 :asr.autospecs/Stop
194 :asr.autospecs/StringChr
195 :asr.autospecs/StringCompare
196 :asr.autospecs/StringConcat
197 :asr.autospecs/StringConstant
198 :asr.autospecs/StringItem
199 :asr.autospecs/StringLen
200 :asr.autospecs/StringOrd
201 :asr.autospecs/StringRepeat
202 :asr.autospecs/StringSection
203 :asr.autospecs/Sub
204 :asr.autospecs/SubroutineCall
205 :asr.autospecs/SupportsPlus
206 :asr.autospecs/SupportsZero
207 :asr.autospecs/TemplateBinOp
208 :asr.autospecs/TemplateToReal
209 :asr.autospecs/TranslationUnit
210 :asr.autospecs/Tuple
211 :asr.autospecs/TupleConstant
212 :asr.autospecs/TupleItem
213 :asr.autospecs/TupleLen
214 :asr.autospecs/TypeParameter
215 :asr.autospecs/UBound
216 :asr.autospecs/Unspecified
217 :asr.autospecs/Var
218 :asr.autospecs/Variable
219 :asr.autospecs/Where
220 :asr.autospecs/WhileLoop
221 :asr.autospecs/Xor
```

```clojure
01 :asr.autospecs/bignat
02 :asr.autospecs/binop-no-div
03 :asr.autospecs/bool
04 :asr.autospecs/dimensions
05 :asr.autospecs/expr
06 :asr.autospecs/float
07 :asr.autospecs/i16
08 :asr.autospecs/i16-constant-semnasr
09 :asr.autospecs/i16-non-zero-constant-semnasr
10 :asr.autospecs/i16-scalar-ttype-semnasr
11 :asr.autospecs/i16nz
12 :asr.autospecs/i32
13 :asr.autospecs/i32-bin-op-semnasr
14 :asr.autospecs/i32-bin-op-semnasr-no-zero-divisor
15 :asr.autospecs/i32-bin-op-semnasr-static-arithmetic
16 :asr.autospecs/i32-constant-semnasr
17 :asr.autospecs/i32-non-zero-constant-semnasr
18 :asr.autospecs/i32-scalar-ttype-semnasr
19 :asr.autospecs/i32nz
20 :asr.autospecs/i64
21 :asr.autospecs/i64-constant-semnasr
22 :asr.autospecs/i64-non-zero-constant-semnasr
23 :asr.autospecs/i64-scalar-ttype-semnasr
24 :asr.autospecs/i64nz
25 :asr.autospecs/i8
26 :asr.autospecs/i8-constant-semnasr
27 :asr.autospecs/i8-non-zero-constant-semnasr
28 :asr.autospecs/i8-scalar-ttype-semnasr
29 :asr.autospecs/i8nz
30 :asr.autospecs/identifier
31 :asr.autospecs/int
32 :asr.autospecs/integer-bin-op-mixed-kind-base-semnasr
33 :asr.autospecs/integer-constant-semnasr
34 :asr.autospecs/integer-non-zero-constant-semnasr
35 :asr.autospecs/integer-scalar-ttype-semnasr
36 :asr.autospecs/integer-ttype-semnasr
37 :asr.autospecs/stmt
38 :asr.autospecs/symbol
39 :asr.autospecs/symbol-table
40 :asr.autospecs/ttype
```

## Options

## Examples

## Bugs

## License

Copyright © 2022 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
