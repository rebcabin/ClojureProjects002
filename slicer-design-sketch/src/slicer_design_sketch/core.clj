(ns slicer-design-sketch.core
  (:require [clojure.test :as test])
  (:gen-class))

(defmacro pdump [x]
  `(let [x# ~x]
     (do (println "----------------")
         (clojure.pprint/pprint '~x)
         (println "~~>")
         (clojure.pprint/pprint x#)
         (println "----------------")
         x#)))

;;;  __  __                           ___         _   _          
;;; |  \/  |__ _ _ __ _ __  ___ _ _  / __| ___ __| |_(_)___ _ _  
;;; | |\/| / _` | '_ \ '_ \/ -_) '_| \__ \/ -_) _|  _| / _ \ ' \ 
;;; |_|  |_\__,_| .__/ .__/\___|_|   |___/\___\__|\__|_\___/_||_|
;;;             |_|  |_|                                         

;;; These are dummies or mocks for Mohit's mapper
(defn mp-to-channels      [mp]
  (cond
   (= mp 1) [1000 1100 1600]
   :else (throw (Exception. (str "unknown marketplace: " mp)))))

(defn gl-to-pls           [gl])
(defn legal-entity-to-mps [le])

;;;  ___                 _            _     _             
;;; | _ \___ ___ _  _ __| |___ ___ __| |_ _(_)_ _____ _ _ 
;;; |  _(_-</ -_) || / _` / _ \___/ _` | '_| \ V / -_) '_|
;;; |_| /__/\___|\_,_\__,_\___/   \__,_|_| |_|\_/\___|_|  
;;;  ___         _   _          
;;; / __| ___ __| |_(_)___ _ _  
;;; \__ \/ -_) _|  _| / _ \ ' \ 
;;; |___/\___\__|\__|_\___/_||_|
                            
(def prime-pseudo-driver-table
  (pdump (map (partial zipmap [:marketplace :prime :amount])
              [[1 :p1 100]
               [1 :p2 200]
               [1 :p3 0]
               ])))
 
(def revenue-pseudo-driver-table
  (pdump (map (partial zipmap [:marketplace :channel-type :gl :prime :amount])
              [[1 1000 23 :p1 50]
               [1 1000 23 :p1 100]
               [1 1000 23 :p2 75]])))

(defn pseudo-driver-to-pseudo-driver-type [pseudo-driver]
  (cond
   ;; TODO: ensure that prime pseudo drivers have :marketplace
   (= nil (pseudo-driver :gl)) :prime-pseudo-driver
   :else                       :revenue-pseudo-driver
   ;; TODO: error-checking (this is only a sketch)
   ))

;; TODO: Move these to unit tests
(pdump (pseudo-driver-to-pseudo-driver-type (first prime-pseudo-driver-table)))
(pdump (pseudo-driver-to-pseudo-driver-type (first revenue-pseudo-driver-table)))

(defmulti pseudo-driver-to-proto-drivers pseudo-driver-to-pseudo-driver-type)

(defmethod pseudo-driver-to-proto-drivers :prime-pseudo-driver [pseudo-driver]
  (let [channels (mp-to-channels (pseudo-driver :marketplace))]
    (map (fn [channel]
           (-> pseudo-driver
               (dissoc :marketplace)
               (assoc :channel channel)))
         channels)))

(pdump (pseudo-driver-to-proto-drivers (first prime-pseudo-driver-table)))

(defmethod pseudo-driver-to-proto-drivers :revenue-pseudo-driver [pseudo-driver]
  (let [channels (mp-to-channels (pseudo-driver :marketplace))]
    (map (fn [channel]
           (-> pseudo-driver
               (dissoc :marketplace)
               (assoc :channel channel)))
         channels)))

(defn proto-driver-to-driver [])

#_
(def proto-driver-table
  (pdump (map (partial zipmap [:chan :pl :driver-name :prime :amount])
              [[1000 23 :subscription-rev :p1 79]
               [1000 23 :subscription-rev :p2  0]
               [1000 23 :subscription-rev :p3 29]
               ])))

;;;  ___      _               ___         _   _          
;;; |   \ _ _(_)_ _____ _ _  / __| ___ __| |_(_)___ _ _  
;;; | |) | '_| \ V / -_) '_| \__ \/ -_) _|  _| / _ \ ' \ 
;;; |___/|_| |_|\_/\___|_|   |___/\___\__|\__|_\___/_||_|

(def driver-table
  (pdump (map (partial zipmap [:chan :pl :driver-name :asin :prime :amount])
              [[1000 23 :prod-cogs "B00012345" :p1 5]
               [1000 23 :prod-cogs "B00012346" :p2 15]
               [1000 23 :prod-cogs "B00012347" :p3 10]
               [1100 24 :prod-rev  "B00012348" :p1 19]
               ])))

(def driver-mapping
  "Maps accounts to drivers."
  (pdump (map (partial zipmap [:chan :pl :account :driver-name :homo?])
              [[1000 23 :prod-cogs :prod-cogs true]
               [1000 23 :inv-val   :prod-cogs false]])))

(defn driver [driver-name chan pl]
  (let [ripped-table
        (filter (fn [line] (and (= driver-name (:driver-name line))
                               (= chan        (:chan        line))
                               (= pl          (:pl          line))))
                driver-table)
        ]
    ripped-table))

(def pnls
  (pdump (map (partial zipmap [:chan :pl :account :amount])
              [[1000 23 :prod-cogs 2500]
               [1600 23 :inv-val   5000]
               ])))

;;;  _  _                    _ _         _   _          
;;; | \| |___ _ _ _ __  __ _| (_)_____ _| |_(_)___ _ _  
;;; | .` / _ \ '_| '  \/ _` | | |_ / _` |  _| / _ \ ' \ 
;;; |_|\_\___/_| |_|_|_\__,_|_|_/__\__,_|\__|_\___/_||_|

(defn normalize [amaps]
  (let [total (apply + (map :amount amaps))]
    (map
     (fn [amap] (assoc amap :amount (/ (:amount amap) total)))
     amaps)))

(defn driver-spec [driver-name pnl]
  (normalize (driver driver-name (:chan pnl) (:pl pnl))))

(pdump (driver-spec :prod-cogs (first pnls)))

;;;    _   _ _              _   _          
;;;   /_\ | | |___  __ __ _| |_(_)___ _ _  
;;;  / _ \| | / _ \/ _/ _` |  _| / _ \ ' \ 
;;; /_/ \_\_|_\___/\__\__,_|\__|_\___/_||_|

(defn allocate [pnl driver-spec]
  (map (fn [line]
         (assoc line :amount (* (:amount pnl) (:amount line))))
       driver-spec))

(pdump (allocate (first pnls)
                 (driver-spec :prod-cogs (first pnls))))

(defn -main
  "Just run all tests."
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))

  (test/run-all-tests #"slicer-design-sketch.core-test"))
