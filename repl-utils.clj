;;; TODO -- this is unfinished -- was turning into a rat-hole on Sat
;;; 23 Mar at 9:30 PM.  Has not been updated since the old
;;; clojure-contrib days.

;;; Scraped from 
;;; http://www.learningclojure.com/

;;; 
;;; LOAD ME WITH
;;; (load-file "/Users/rebcabin/Documents/ClojureProjects/repl-utils.clj")

;;; This file conditions a repl in various ways that I do all the time.

;; Sometimes I like to ask which public functions a namespace provides.
(defn- ns-publics-list [ns] (#(list (ns-name %) (map first (ns-publics %))) ns))
;; And occasionally which functions it pulls in (with refer or use)
(defn- ns-refers-list  [ns] (#(list (ns-name %) (map first (ns-refers %))) ns))


;;; It drives me up the wall that it's (doc re-pattern) but (find-doc
;;; "re-pattern"). Can use macros so that (fd re-pattern) (fd
;;; "re-pattern") and (fd 're-pattern) all mean the same thing

(defn- stringify [x]
;;  (println "stringify given" (str x))
  (let [s  (cond (string? x) x
                 (symbol? x) (str x)
                 (and (list? x) (= (first x) 'quote)) (str (second x))
                 :else (str x)) ]
;;    (println (str "translating to: \"" s "\""))
    s))

;; Nice pretty-printed versions of these functions, accepting strings, symbols or quoted symbol
(defmacro list-publics     
  ([]
     `(clojure.pprint/pprint (ns-publics-list *ns*)))
  ([symbol-or-string]
     `(clojure.pprint/pprint
       (ns-publics-list (find-ns (symbol (stringify '~symbol-or-string)))))))

(defmacro list-refers
  ([]
     `(clojure.pprint/pprint (ns-refers-list *ns*)))
  ([symbol-or-string]
     `(clojure.pprint/pprint
       (ns-refers-list (find-ns (symbol (stringify '~symbol-or-string)))))))

;;; List all the namespaces

(defn list-all-ns [] (clojure.pprint/pprint (map ns-name (all-ns))))

;;; List all public functions in all namespaces!

(defn list-publics-all-ns []
  (clojure.pprint/pprint
   (map #(list (ns-name %) (map first (ns-publics %))) (all-ns))))

;;; Def-let, for debugging lets. try
;; (def-let [r (range 10)
;;       make-map   (fn [f] (zipmap r (map f r)))
;;       fns        (map make-map (list  #(* % % %) #(* 5 % %) #(* 10 %)))
;;       merge-fns  (fn[f] (apply merge-with f fns))]
;;   (map merge-fns (list min max))) 

(defmacro def-let
  "like let, but binds the expressions globally."
  [bindings & more]
  (let [let-expr (macroexpand `(let ~bindings))
        names-values (partition 2 (second let-expr))
        defs   (map #(cons 'def %) names-values)]
    (concat (list 'do) defs more)))

;;; debugging macro   try: (* 2 (dbg (* 3 4)))

(defmacro dbg [x]
  `(let [x# ~x]
     (do (println '~x "~~>" x#)
         x#))) 

;;; and pretty-printing version

(defmacro ppdbg [x]
  `(let [x# ~x]
     (do (println "----------------")
         (clojure.pprint/pprint '~x)
         (println "~~>")
         (clojure.pprint/pprint x#)
         (println "----------------")
         x#))) 

;;; Sometimes it's nice to check the classpath

(defn- get-classpath []
   (sort (map (memfn getPath) 
              (seq (.getURLs (java.lang.ClassLoader/getSystemClassLoader))))))

(defn print-classpath []
  (clojure.pprint/pprint (get-classpath)))

;;; You always need to know the current directory

(defn get-current-directory []
  (. (java.io.File. ".") getCanonicalPath))

(defn pwd [] (get-current-directory))
