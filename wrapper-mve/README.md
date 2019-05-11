I've reduced my bigger problem to an artificial MVE (minimal viable example)
using file-io for illustration. My question concerns a certain wrapper macro
that I explain below; it does not concern better ways to use the file-io APIs;
I'm just using file-io to illustrate the macro problem in a small and easy
context. The wrapper macro tactic in my real problem is harder to show and
explain, but this MVE captures the gist of the problem.

Consider the following protocol:

```
(defprotocol Dumper
  (dump [this]))
```

and an implementation over `java.io.File`


    (extend-type java.io.File
  Dumper
  (dump [file]
    (with-open [rdr (io/reader file)]
      (doseq [line (line-seq rdr)]
        (println line)))))
```

where we have done a `(:use [clojure.java.io :as io])` to get the `reader`
function. I can use this as follows:

```
(defn -main
  [& args]
  (dump (io/file "resources/a_file.txt")))
```

>     Hello from a text file.

Now, I want to create another implementation of the protocol, this time over
`java.lang.String`. This implemetation simply wraps the string, treating it as a
path string, creating a `clojure.java.io/file`, then calling into the other
implementation of the protocol:

```
(extend-type java.lang.String
  Dumper
  (dump [path-str] (-> path-str, io/file dump)))
```

and call it like this:

```
(defn -main
  [& args]
  (dump (io/file "resources/a_file.txt"))
  (dump          "resources/a_file.txt"))
```

In my real problem, I have many functions in the protocol, and one
implementation just wraps the other in the manner shown. Notice that, in the
wrapper implemetation, the method name, `dump`, is replicated. Let's eliminate
that replication with a macro (it's worth doing when the real protocol has many
methods):

```
(defmacro wrap-path-string [method]
  `(~method [path-str] (-> path-str, io/file ~method)))

(extend-type java.lang.String
  Dumper
  (wrap-path-string dump))
```

Oops, the compiler doesn't like it:

>     Exception in thread "main" java.lang.UnsupportedOperationException: 
>       nth not supported on this type: Symbol, compiling:(wrapper_mve/core.clj:18:1)
>     at clojure.lang.Compiler.analyze(Compiler.java:6688)
>     at clojure.lang.Compiler.analyze(Compiler.java:6625)
>     at clojure.lang.Compiler$MapExpr.parse(Compiler.java:3072)

I tried macroexpanding and macroexpand-1'ing the macro calls (in CIDER,
difficult to replicate here), and it looks ok. I'm at a loss how to debug
deeper, but perhaps someone here can spot the problem.

Again, I know this MVE has better solutions with the file-io APIs, but I really
want to debug the macro, not find ways to avoid using it, because I need the
wrapper-macro tactic in my real problem.


# wrapper-mve

FIXME: description

## Installation

Download from http://example.com/FIXME.

## Usage

FIXME: explanation

    $ java -jar wrapper-mve-0.1.0-standalone.jar [args]

## Options

FIXME: listing of options this app accepts.

## Examples

...

### Bugs

...

### Any Other Sections
### That You Think
### Might be Useful

## License

Copyright © 2016 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
