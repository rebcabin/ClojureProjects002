(ns demeters-law.core)

;; Principle of "least knowledge." Don't know why my BR
;; named the problem this, but it's not important.
;;
;; The story is as follows: The interview question "Given a
;; list of shipments in random order, each of which has an
;; item, a value, and a warehouse number, report the n
;; top-value items from each warehouse. The interviewee
;; produced a good answer in almost 150 lines of Java,
;; building a class for the shipment records, a hash-map of
;; "ArrayLists" by warehouse, a "Comparable" implementation,
;; sorting the ArrayLists, and index loops top pick. We
;; remarked that this could be done in a functional
;; one-liner. It then became an exercise in showing off
;; test.check.

(defn get-tops [n shipments]
  (->> shipments
       (group-by :warehouse)
       (map #(->> % second
                  (sort-by :value)
                  reverse
                  (take n)))))

