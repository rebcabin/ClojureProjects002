# Basic Quil Metronome

A Clojure Quil project template that serves as a visual Hello World.

## Usage

Run me via nREPL, using the *run-sketch* function in core.  When you look at the *clojure.main* window, you should see a red circle oscillating from side to side.

## Why?

Here's why:
* This is a basic template I use to get started with Clojure Quil projects.
* I am using separate namespaces for the draw and setup code, which allows `C-c C-k` evaluation of the *setup* and *draw* buffers without re-evaluating the sketch creation code (in *core*).
* The *setup* and *draw* code of a typical Processing sketch is broken out into separate namespaces, which kinda makes sense, when you think about it...

## License

Copyright © 2013 Pas de Chocolat, LLC

Distributed under the Eclipse Public License, the same as Clojure.
