(require
  '[cljs.build.api :as b]
  '[cljs.repl :as repl]
  '[cljs.repl.browser :as browser])

(b/build "src"
  {:main 'async-tutl.core
   :output-to "out/async_tutl.js"
   :output-dir "out"
   :verbose true})

(repl/repl (browser/repl-env)
  :output-dir "out")
