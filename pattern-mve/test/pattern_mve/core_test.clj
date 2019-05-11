(ns pattern-mve.core-test
  (:require [clojure.test                    :refer      :all]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.properties   :as         prop]
            [clojure.spec                    :as            s]
            [pattern-mve.core :refer :all]))
;;; 1.
(defspec minus-infinity-less-than-all-but-minus-infinity
  100
  (prop/for-all
   [vt (s/gen :pattern-mve.core/virtual-time)]
   (if (not= (:vt vt) :vt-negative-infinity)
     (vt-lt vt-negative-infinity vt)
     true)))
;;; 2.
(defspec plus-infinity-not-less-than-any
  100
  (prop/for-all
   [vt (s/gen :pattern-mve.core/virtual-time)]
   (not (vt-lt vt-positive-infinity vt))))

