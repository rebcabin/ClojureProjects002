#! /bin/sh

find . -name '*.clj' | xargs etags --regex=@/Users/brianbeckman/Documents/ClojureProjects/clojure-etags.re
