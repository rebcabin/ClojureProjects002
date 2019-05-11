(ns demeters-law.core-test
  (:require [clojure.test :refer :all]
            [clojure.test.check :as tc]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [demeters-law.core :refer :all]))

;;  _  _              _            _ _   _              _____       _
;; | || |__ _ _ _  __| |_ __ ___ _(_) |_| |_ ___ _ _   |_   _|__ __| |_
;; | __ / _` | ' \/ _` \ V  V / '_| |  _|  _/ -_) ' \    | |/ -_|_-<  _|
;; |_||_\__,_|_||_\__,_|\_/\_/|_| |_|\__|\__\___|_||_|   |_|\___/__/\__|

(def handwritten-shipments
  [{:warehouse 42, :value 10, :item 'foo}
   {:warehouse 77, :value 30, :item 'baz}
   {:warehouse 42, :value 20, :item 'bar}
   {:warehouse 77, :value 40, :item 'qux}
   {:warehouse 77, :value 50, :item 'boo}
   {:warehouse 42, :value 25, :item 'zab}
   {:warehouse 77, :value 60, :item 'far}])

(deftest handwritten-top-2-test
  (testing "Handwritten shipments"
    (is (=
         '(({:warehouse 42, :value 25, :item zab}
            {:warehouse 42, :value 20, :item bar})
           ({:warehouse 77, :value 60, :item far}
            {:warehouse 77, :value 50, :item boo}))
         (get-tops 2 handwritten-shipments)
         ))))

;;  ___                       _            _                     _
;; | _ \_ _ ___ _ __  ___ _ _| |_ _  _ ___| |__  __ _ ___ ___ __| |
;; |  _/ '_/ _ \ '_ \/ -_) '_|  _| || |___| '_ \/ _` (_-</ -_) _` |
;; |_| |_| \___/ .__/\___|_|  \__|\_, |   |_.__/\__,_/__/\___\__,_|
;;             |_|                |__/
;;  _____       _
;; |_   _|__ __| |_
;;   | |/ -_|_-<  _|
;;   |_|\___/__/\__|

;;; Definitely read https://github.com/clojure/test.check/blob/master/doc/intro.md

(def value-gen
  (gen/fmap (fn [i] [:value (Math/abs i)])
            gen/large-integer))

(def nym-gen
  (gen/fmap (fn [s] [:item s])
            gen/symbol))

(def warehouse-gen
  "Choose randomly from a finite set of warehouse numbers"
  (gen/fmap (fn [wi] [:warehouse wi])
            (gen/elements #{42 77 198 56})))

(def shipment-gen-000
  (gen/fmap #(into {} %)
            (gen/tuple nym-gen value-gen warehouse-gen)))

;; A more concise and erudite generator:

(def shipment-gen (gen/hash-map :warehouse
                                (gen/elements #{42 77 198 56})
                                :value
                                (gen/large-integer*
                                 {:min 1 :max 1000})
                                :item gen/string-alphanumeric))

;; Try this in the REPL:

;; demeters-law.core-test> (gen/sample shipment-gen)
;; ({:item w, :value 0, :warehouse 198}
;;  {:item +, :value 1, :warehouse 56}
;;  {:item _, :value 1, :warehouse 42}
;;  {:item R, :value 1, :warehouse 77}
;;  {:item M!4y, :value 0, :warehouse 198}
;;  {:item +?, :value 0, :warehouse 77}
;;  {:item B.08, :value 11, :warehouse 198}
;;  {:item CP!5If, :value 0, :warehouse 198}
;;  {:item !!_!Ad, :value 5, :warehouse 198}
;;  {:item C, :value 7, :warehouse 42})

(def shipments-gen
  (gen/vector shipment-gen))

(defspec at-most-two
  100
  (prop/for-all
   [shipments shipments-gen]
   (->> shipments
        (#(do (clojure.pprint/pprint {:shipments %}) %))
        (get-tops 2)
        (map count)
        (#(do (clojure.pprint/pprint {:counts %}) %))
        (every? (partial >= 2)))))
