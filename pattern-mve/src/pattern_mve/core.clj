(ns pattern-mve.core
  (:require [clojure.spec              :as         s]
            [clojure.spec.gen          :as       gen])
  (:gen-class))

(defprotocol VirtualTimeT
  "A number with two distinguished values for plus and minus infinity. Minus
  infinity is less than any virtual time other than minus infinity. Plus
  infinity is greater than any virtual time other than plus infinity."
  (vt-lt [this-vt that-vt])
  (vt-le [this-vt that-vt])
  (vt-eq [this-vt that-vt]))

(defn -vt-compare-lt [this-vt that-vt]
  (case (:vt this-vt)
    :vt-negative-infinity
    (case (:vt that-vt)
      :vt-negative-infinity false
      #_otherwise true)

    :vt-positive-infinity
    false

    ;; otherwise: this-vt is a number.
    (case (:vt that-vt)
      :vt-positive-infinity true
      :vt-negative-infinity false
      #_otherwise (< (:vt this-vt) (:vt that-vt)))))

;;; The virtual-time record type implements VirtualTimeT protocol.

(defrecord virtual-time [vt]
  VirtualTimeT
  (vt-lt [this that] (-vt-compare-lt this that))
  (vt-eq [this that] (= this that))
  (vt-le [this that] (or (vt-eq this that) (vt-lt this that))))

;;; A couple of global variables.

(def vt-negative-infinity (virtual-time. :vt-negative-infinity))
(def vt-positive-infinity (virtual-time. :vt-positive-infinity))

;;; Generators for specs and tests.

(def vt-number-gen
  (gen/bind
   (gen/large-integer)
   (fn [vt] (gen/return (virtual-time. vt)))))

(def vt-negative-infinity-gen
  (gen/return (virtual-time. :vt-negative-infinity)))

(def vt-positive-infinity-gen
  (gen/return (virtual-time. :vt-positive-infinity)))

;;; Now we have enough to define a spec for virtual time. We could just say
;;; "#(instance? time_warp.core.virtual-time %)," but that's circular and
;;; therefore deficient; worse, it doesn't allow alternative implementations of
;;; the protocol. It's better to spec the required values.

(s/def ::virtual-time
  (s/with-gen
    (s/and
     (s/or
      :minus-infinity #(vt-eq % :vt-negative-infinity)
      :plus-infinity  #(vt-eq % :vt-positive-infinity)
      :number         #(number? (:vt %)))
     (s/conformer second))              ; strip off redundant conformer tag
    #(gen/frequency [[98 vt-number-gen]
                     [ 1 vt-negative-infinity-gen]
                     [ 1 vt-positive-infinity-gen]])))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
