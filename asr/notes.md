Use it to represent a function type, in callbacks like procedure(my_funtion_type). Probably Function should just reference this FunctionType as well.

Some details to figure out:

>    What should be part of FunctionType: probably arguments and
>    return value (as well as type_params and restrictions ?)

## Parameter Tuples

Parameter types must be part of the function signature. Argument
types only pertain to call expressions, not to function
signatures. Python literature often conflates "parameter" and
"argument." We must always be clear about the difference.

A function of `n > 1` fixed, positional, mandatory, non-defaulted
parameters may be modeled as a function of one parameter of tuple
type, or as a Curried function that returns Curried functions
until the parameters run out. The first is written

`fn : (S0, S1, ..., Sn_1) -> T`

the second is written

`fn : S0 -> S1 -> ... -> Sn_1 -> T`

where `Si` are the types of the parameters and `(S0, S1, ...)` is
the type of a tuple of parameters of given types.

The two representations above are isomorphic. I recommend the
tuple representation; it makes Python easy but Haskell requires a
second step. That's the right trade-off.

In modeling Haskell functions with parameter tuples, a
"restriction" must be recorded because Haskell automatically
Curries all function types and partially evaluates all function
invocations.

## Metadata (Restrictions):

Function types should include an extendible metadata attribute.
Clojure's design is State-of-the-Art:
https://clojure.org/reference/metadata.

In short, "metadata" is a dictionary of symbols to values. Some of
the symbols and values will be defined in ASR and well known.
Others will be language-specific or back-end-specific. All
well-known symbols must have default values, which pertain when
metadata is absent or partial. Implementations shall ignore
symbols and values it cannot parse or interpret.

## Optional, Defaulted, and Keyword parameters:

Python leads the way in specification of parameter lists with
non-positional, optional, defaulted, and keyword parameters (and
arguments). See (e.g.)
https://docs.python.org/3.11/reference/compound_stmts.html#function-definitions.
I believe that if we can handle all the Python cases, we can
handle the cases for any other front-end language of interest.


>    What about side-effect-free / deterministic, and other flags
>    like inline, static, abi, access, bindc, elemental, pure,
>    module ?

Why not make them members of the "metadata" with defaulted values

```
inline=false

static=false (does this just mean "non-linkable?" or does it have
implications about memory allocation?)

abi=false (not sure what this means)

bindc=false (not sure what this means)

elemental=false (not sure what this means)

pure=false (set true if ASR guarantees no side effects; also
implies the function can be auto-memoized)

module=false (is this about namespaces? or something deeper,
something else?)
```

>    When does a given function satisfy the type, say for a
>    callback: arguments must exactly match. Does "elemental" have
>    to match? What about other flags?

## Subtypes

An actual argument of type `TA` should match a parameter of type
`TB` if `TA` is a sub-type of `TB`. This is a big topic with
ramifications in ASRs assumed "numeric tower" (i.e., is "real" a
subtype of "complex?") if there is a numeric tower. Also, consider
"exact" versus "inexact," precision specifications, and, for
collection types, covariance (in output positions) and
contravariance (in input positions)

>    Name probably should not be part of it, so that FunctionType can be used for lambda functions as well (that do not have a name)

"Name" is not part of the type, proper, but it's helpful
(mandatory?) metadata.

"Name" can help with overload resolution. In practice, lambda
functions often get secret gensymmed names (perhaps just hashes)
so the compiler can coalesce and track them.

>    How to represent the name of the type ? I think FunctionType
>    would be a ttype, like Integer. Then we need to add a
>    "symbol", like Variable, that would represent the named
>    function type as a variable that you can reference in
>    procedure(my_function_type), or something like that.

Yes, this gets into the "Kind" space of type theory: a meta-type
system about types. Helps when reasoning about subtypes, Liskov
substitution, covariance, etc. Function types have non-commutative
algebraic structure, though not very rich. Function types are not
just atomic names.
