# asr

Test generator for ASR (https://github.com/lcompilers/lpython/wiki/ASR-Design)

## Installation

Prerequisite: [Leiningen and Clojure](https://leiningen.org/)

```
cd -=|[some convenient directory]|=-
git clone https://github.com/rebcabin/ClojureProjects002
```

## Usage

```
cd ClojureProjects002/asr
lein test
```

## Specs Defined by this Program

```clojure
01 :asr.core/abi
02 :asr.core/access
03 :asr.core/alloc_arg
04 :asr.core/array_index
05 :asr.core/arraybound
06 :asr.core/attribute
07 :asr.core/attribute_arg
08 :asr.core/binop
09 :asr.core/call_arg
10 :asr.core/case_stmt
11 :asr.core/cast_kind
12 :asr.core/cmpop
13 :asr.core/deftype
14 :asr.core/dimension
15 :asr.core/do_loop_head
16 :asr.core/expr
17 :asr.core/integerboz
18 :asr.core/intent
19 :asr.core/logicalbinop
20 :asr.core/presence
21 :asr.core/restriction
22 :asr.core/stmt
23 :asr.core/storage_type
24 :asr.core/symbol
25 :asr.core/tbind
26 :asr.core/trait
27 :asr.core/ttype
28 :asr.core/unit
```

```clojure
001 :asr.core/Add
002 :asr.core/Allocatable
003 :asr.core/Allocate
004 :asr.core/And
005 :asr.core/Any
006 :asr.core/ArrayBound
007 :asr.core/ArrayConstant
008 :asr.core/ArrayItem
009 :asr.core/ArrayMatMul
010 :asr.core/ArrayPack
011 :asr.core/ArrayReshape
012 :asr.core/ArraySection
013 :asr.core/ArraySize
014 :asr.core/ArrayTranspose
015 :asr.core/Assert
016 :asr.core/Assign
017 :asr.core/Assignment
018 :asr.core/Associate
019 :asr.core/AssociateBlock
020 :asr.core/AssociateBlockCall
021 :asr.core/Attribute
022 :asr.core/Binary
023 :asr.core/Bind
024 :asr.core/BindC
025 :asr.core/BitAnd
026 :asr.core/BitCast
027 :asr.core/BitLShift
028 :asr.core/BitOr
029 :asr.core/BitRShift
030 :asr.core/BitXor
031 :asr.core/Block
032 :asr.core/BlockCall
033 :asr.core/CLoc
034 :asr.core/CPtr
035 :asr.core/CPtrToPointer
036 :asr.core/CaseStmt
037 :asr.core/CaseStmt_Range
038 :asr.core/Cast
039 :asr.core/Character
040 :asr.core/CharacterToInteger
041 :asr.core/CharacterToList
042 :asr.core/CharacterToLogical
043 :asr.core/Class
044 :asr.core/ClassProcedure
045 :asr.core/ClassType
046 :asr.core/Complex
047 :asr.core/ComplexBinOp
048 :asr.core/ComplexCompare
049 :asr.core/ComplexConstant
050 :asr.core/ComplexConstructor
051 :asr.core/ComplexIm
052 :asr.core/ComplexRe
053 :asr.core/ComplexToComplex
054 :asr.core/ComplexToLogical
055 :asr.core/ComplexToReal
056 :asr.core/ComplexUnaryMinus
057 :asr.core/CustomOperator
058 :asr.core/Cycle
059 :asr.core/Default
060 :asr.core/Derived
061 :asr.core/DerivedRef
062 :asr.core/DerivedType
063 :asr.core/DerivedTypeConstructor
064 :asr.core/Dict
065 :asr.core/DictConstant
066 :asr.core/DictInsert
067 :asr.core/DictItem
068 :asr.core/DictLen
069 :asr.core/DictPop
070 :asr.core/Div
071 :asr.core/Divisible
072 :asr.core/DoConcurrentLoop
073 :asr.core/DoLoop
074 :asr.core/Enum
075 :asr.core/EnumRef
076 :asr.core/EnumType
077 :asr.core/EnumTypeConstructor
078 :asr.core/Eq
079 :asr.core/Eqv
080 :asr.core/ErrorStop
081 :asr.core/Exit
082 :asr.core/ExplicitDeallocate
083 :asr.core/ExternalSymbol
084 :asr.core/FileClose
085 :asr.core/FileInquire
086 :asr.core/FileOpen
087 :asr.core/FileRead
088 :asr.core/FileRewind
089 :asr.core/FileWrite
090 :asr.core/Flush
091 :asr.core/ForAllSingle
092 :asr.core/Function
093 :asr.core/FunctionCall
094 :asr.core/GFortranModule
095 :asr.core/GenericProcedure
096 :asr.core/GetPointer
097 :asr.core/GoTo
098 :asr.core/GoToTarget
099 :asr.core/Gt
100 :asr.core/GtE
101 :asr.core/Hex
102 :asr.core/If
103 :asr.core/IfArithmetic
104 :asr.core/IfExp
105 :asr.core/Implementation
106 :asr.core/ImplicitDeallocate
107 :asr.core/ImpliedDoLoop
108 :asr.core/In
109 :asr.core/InOut
110 :asr.core/Integer
111 :asr.core/IntegerBOZ
112 :asr.core/IntegerBinOp
113 :asr.core/IntegerBitLen
114 :asr.core/IntegerBitNot
115 :asr.core/IntegerCompare
116 :asr.core/IntegerConstant
117 :asr.core/IntegerToCharacter
118 :asr.core/IntegerToComplex
119 :asr.core/IntegerToInteger
120 :asr.core/IntegerToLogical
121 :asr.core/IntegerToReal
122 :asr.core/IntegerUnaryMinus
123 :asr.core/Interactive
124 :asr.core/Interface
125 :asr.core/Intrinsic
126 :asr.core/LBound
127 :asr.core/LFortranModule
128 :asr.core/List
129 :asr.core/ListAppend
130 :asr.core/ListClear
131 :asr.core/ListConcat
132 :asr.core/ListConstant
133 :asr.core/ListInsert
134 :asr.core/ListItem
135 :asr.core/ListLen
136 :asr.core/ListPop
137 :asr.core/ListRemove
138 :asr.core/ListSection
139 :asr.core/Local
140 :asr.core/Logical
141 :asr.core/LogicalBinOp
142 :asr.core/LogicalCompare
143 :asr.core/LogicalConstant
144 :asr.core/LogicalNot
145 :asr.core/LogicalToCharacter
146 :asr.core/LogicalToInteger
147 :asr.core/LogicalToReal
148 :asr.core/Lt
149 :asr.core/LtE
150 :asr.core/Module
151 :asr.core/Mul
152 :asr.core/NEqv
153 :asr.core/NamedExpr
154 :asr.core/NotEq
155 :asr.core/Nullify
156 :asr.core/Octal
157 :asr.core/Optional
158 :asr.core/Or
159 :asr.core/Out
160 :asr.core/OverloadedBinOp
161 :asr.core/OverloadedCompare
162 :asr.core/Parameter
163 :asr.core/Pointer
164 :asr.core/PointerToCPtr
165 :asr.core/Pow
166 :asr.core/Print
167 :asr.core/Private
168 :asr.core/Program
169 :asr.core/Public
170 :asr.core/Real
171 :asr.core/RealBinOp
172 :asr.core/RealCompare
173 :asr.core/RealConstant
174 :asr.core/RealToCharacter
175 :asr.core/RealToComplex
176 :asr.core/RealToInteger
177 :asr.core/RealToLogical
178 :asr.core/RealToReal
179 :asr.core/RealUnaryMinus
180 :asr.core/Required
181 :asr.core/Restriction
182 :asr.core/Return
183 :asr.core/ReturnVar
184 :asr.core/Save
185 :asr.core/Select
186 :asr.core/Set
187 :asr.core/SetConstant
188 :asr.core/SetInsert
189 :asr.core/SetLen
190 :asr.core/SetPop
191 :asr.core/SetRemove
192 :asr.core/Source
193 :asr.core/Stop
194 :asr.core/StringChr
195 :asr.core/StringCompare
196 :asr.core/StringConcat
197 :asr.core/StringConstant
198 :asr.core/StringItem
199 :asr.core/StringLen
200 :asr.core/StringOrd
201 :asr.core/StringRepeat
202 :asr.core/StringSection
203 :asr.core/Sub
204 :asr.core/SubroutineCall
205 :asr.core/SupportsPlus
206 :asr.core/SupportsZero
207 :asr.core/TemplateBinOp
208 :asr.core/TemplateToReal
209 :asr.core/TranslationUnit
210 :asr.core/Tuple
211 :asr.core/TupleConstant
212 :asr.core/TupleItem
213 :asr.core/TupleLen
214 :asr.core/TypeParameter
215 :asr.core/UBound
216 :asr.core/Unspecified
217 :asr.core/Var
218 :asr.core/Variable
219 :asr.core/Where
220 :asr.core/WhileLoop
221 :asr.core/Xor
```

```clojure
01 :asr.core/bignat
02 :asr.core/binop-no-div
03 :asr.core/bool
04 :asr.core/dimensions
05 :asr.core/expr
06 :asr.core/float
07 :asr.core/i16
08 :asr.core/i16-constant-semnasr
09 :asr.core/i16-non-zero-constant-semnasr
10 :asr.core/i16-scalar-ttype-semnasr
11 :asr.core/i16nz
12 :asr.core/i32
13 :asr.core/i32-bin-op-semnasr
14 :asr.core/i32-bin-op-semnasr-no-zero-divisor
15 :asr.core/i32-bin-op-semnasr-static-arithmetic
16 :asr.core/i32-constant-semnasr
17 :asr.core/i32-non-zero-constant-semnasr
18 :asr.core/i32-scalar-ttype-semnasr
19 :asr.core/i32nz
20 :asr.core/i64
21 :asr.core/i64-constant-semnasr
22 :asr.core/i64-non-zero-constant-semnasr
23 :asr.core/i64-scalar-ttype-semnasr
24 :asr.core/i64nz
25 :asr.core/i8
26 :asr.core/i8-constant-semnasr
27 :asr.core/i8-non-zero-constant-semnasr
28 :asr.core/i8-scalar-ttype-semnasr
29 :asr.core/i8nz
30 :asr.core/identifier
31 :asr.core/int
32 :asr.core/integer-bin-op-mixed-kind-base-semnasr
33 :asr.core/integer-constant-semnasr
34 :asr.core/integer-non-zero-constant-semnasr
35 :asr.core/integer-scalar-ttype-semnasr
36 :asr.core/integer-ttype-semnasr
37 :asr.core/stmt
38 :asr.core/symbol
39 :asr.core/symbol-table
40 :asr.core/ttype
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
