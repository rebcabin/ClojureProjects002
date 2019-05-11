(ns skew-zip.core)

(defn part
  "Take 'n' elements starting at index 'start' from vector 'v'."
  [v n start]
  {:pre [(<= n (count v))
         (>= n 0)
         (>= start 0)
         (<= start (- (count v) n))]}
    (subvec (vec v) start (+ start n)))

(defn replace-part
  "Starting at index 'start', replace elements in 'v-' with elements in
   'part-'. If 'v-' or 'part-' are not vectors, they will be converted
   to vectors."
  [v- part- start]
  #_{:pre [(<= (count part-) (count v-))
         (>= start 0)
         (<= start (- (count v-) (count part-)))]}
  (let [part (vec part-)
        v    (vec v-)
        n    (count part)
        c    (count v)
        m    (+ start n)]
    (map-indexed
     (fn [i x]
       (if (and (>= i start) (< i m))
         (part (- i start))
         (v i)))
     v)))

(defn swizzle-part
  "Starting at index 'start', shuffle 'n' elements inside vector 'v'."
  [v n start]
  (replace-part
   (vec v)
   (shuffle (part v n start))
   start
   ))

(defn swizzle
  "Randmonly shuffle elements in vector 'v' from left to right with a
   mixing length of 'n'."
  [v n]
  {:pre (<= n (count v))}
  (let [c (count v)]
    (reduce #(swizzle-part %1 n %2)
            v
           (if (= 0 (- c n))
             '(0)
             (range 0 (- c n))))))

;;; Zip-by maintains input queues, q1 and q2, corresponding to each
;;; input sequence, s1 and s2. The first element, x1, of s1 is
;;; considered. If the first element, x2, of seq-2 matches, x1 and x2
;;; are combined through the user-supplied binary function, fxy;
;;; appended to the output; and zip-by recurses. Otherwise, q2 is
;;; searched. If matches to x1 are found, the oldest, x2, is removed
;;; from q2; combined through fxy; appended to the output; and zip-by
;;; recurses. Otherwise, x1 is sequentially numbered and inserted into
;;; q1. The procedure is repeated for s2. If either queue overflows, the
;;; oldest element by sequence number is removed and the corresponding
;;; user-supplied overflow function is called. When either sequence is
;;; exhausted, the user-supplied corresponding residual function is
;;; called with the contents of the other sequence and zip-by
;;; terminates.

;;; As elements arrive, they are sequentially numbered and
;;; inserted into a chained hash set according to their keys. Each
;;; element in the hash set is a ring-buffer of elements.  Keys are
;;; determined by applying user-supplied key-selector functions. Each
;;; input sequence corresponds to an internal hash-set. If an incoming
;;; element would cause a queue to overflow, the oldest element (the one
;;; with the lowest sequence number) is evicted from the corresponding
;;; queue.

(defn zip-by
  "Combine matching elements from two sequences through
   binary-combiner-fn; matching is defined by passing elements of the
   sequences to corresponding key selector functions and comparing the
   results; oldest matches are picked first; internal queues are bounded
   by queue-depth (default: 1000); optional queue overflow functions are
   called with elements that are pushed out and optional residual
   functions are called with elements that remain in the other queue
   when one queue is exhausted."
  [seq-1              seq-2
   key-selector-fn-1  key-selector-fn-2
   binary-combiner-fn
   & {:keys [queue-depth
             queue-overflow-fn-1
             queue-overflow-fn-2
             queue-residual-fn-1
             queue-residual-fn-2]}]
  (let [q1     (ref #{})
        q2     (ref #{})
        count1 (ref 0)
        count2 (ref 0)
        d      (or queue-depth 1000)
        nop    (fn [& _])
        kf1    key-selector-fn-1
        kf2    key-selector-fn-2
        of1    (or queue-overflow-fn-1 nop)
        of2    (or queue-overflow-fn-2 nop)
        rs1    (or queue-residual-fn-1 nop)
        rs2    (or queue-residual-fn-2 nop)]
    )
  )
