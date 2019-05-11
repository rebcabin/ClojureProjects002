
(ns concurrent-data.watch-debug
  (:use concurrent-data.utils
        [concurrent-data.validator
         :only (int-rows try-read-string data-file
                         watch-caster)]
        concurrent-data.watcher))

#_
(use 'concurrent-data.utils
     'concurrent-data.watcher
     '[concurrent-data.validator
       :only (int-rows try-read-string data-file)])

(defn debug-watch
  [watch-key watch-agent old-state new-state]
  (let [output (str watch-key
                    ": "
                    (pr-str old-state)
                    " => "
                    (pr-str new-state)
                    \newline)]
    (print output)))

(defn watch-debugging
  [input-file]
  (let [reader (agent
                 (seque
                   (with-header
                     (lazy-read-csv
                       input-file))))
        caster (agent nil)
        sink (agent [])
        counter (ref 0)
        done (ref false)]
    (add-watch caster :counter
               (partial watch-caster counter))
    (add-watch caster :debug debug-watch)
    (send reader read-row caster sink done)
    (wait-for-it 250 done)
    {:results @sink
     :count-watcher @counter}))

