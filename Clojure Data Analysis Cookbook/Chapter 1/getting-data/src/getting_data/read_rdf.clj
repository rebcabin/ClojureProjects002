
(ns getting-data.read-rdf
  (:use [incanter.core]
        [edu.ucdenver.ccp.kr.kb]
        [edu.ucdenver.ccp.kr.rdf]
        [edu.ucdenver.ccp.kr.sparql]
        [edu.ucdenver.ccp.kr.sesame.kb]
        [clojure.set :only (rename-keys)]
        [clojure.tools.logging :only [info error]])
  (:import [java.io File]))

(defn kb-memstore
  "This creates a Sesame triple store in memory."
  []
  (kb :sesame-mem))

(defn init-kb
  "This creates an in-memory knowledge base and initializes it with a default
  set of namespaces."
  [kb-store]
  (register-namespaces
    kb-store
    '(("geographis" "http://telegraphis.net/ontology/geography/geography#")
         ("code" "http://telegraphis.net/ontology/measurement/code#")
         ("money" "http://telegraphis.net/ontology/money/money#")
         ("owl" "http://www.w3.org/2002/07/owl#")
         ("rdf" "http://www.w3.org/1999/02/22-rdf-syntax-ns#")
         ("xsd" "http://www.w3.org/2001/XMLSchema#")
         ("currency" "http://telegraphis.net/data/currencies/")
         ("dbpedia" "http://dbpedia.org/resource/")
         ("dbpedia-ont" "http://dbpedia.org/ontology/")
         ("dbpedia-prop" "http://dbpedia.org/property/")
         ("err" "http://ericrochester.com/"))))

(defn rekey
  "This just flips the arguments for clojure.set/rename-keys to make it more
  convenient."
  ([k-map map]
   (rename-keys (select-keys map (keys k-map)) k-map)))

(defn header-keyword
  "This converts a query symbol to a keyword."
  [header-symbol]
  (keyword (.replace (name header-symbol) \_ \-)))

(defn fix-headers
  "This changes all the keys in the map to make them valid header keywords."
  [coll]
  (into {} (map (fn [[k v]] [(header-keyword k) v]) coll)))

(defn load-data
  "This loads data from a file and returns a dataset with the columns given."
  [k rdf-file q]
  (load-rdf-file k rdf-file)
  (to-dataset (map fix-headers (query k q))))

