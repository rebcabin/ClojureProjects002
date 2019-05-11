(require '[cljs.build.api :as b])

(b/watch "src"
  {:main 'async-tutl.core
   :output-to "out/async_tutl.js"
   :output-dir "out"})
