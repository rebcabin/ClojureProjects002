(ns mini-kanren.core
  (:use clojure.core.logic)
  (:refer-clojure :exclude [==]))

;;; borrowed from
;;; https://github.com/candera/reasoned-schemer

(defn set=
  "Returns true if a and b have the same elements, regardless of order"
  [a b]
  (= (set a) (set b)))

(defn pair?
  "Returns true if x is a pair-like thing. The slightly awkward
  definition arises out of the mismatch between Scheme and Clojure."
  [x]
  (or (lcons? x) (and (coll? x) (seq x))))

(defn pairo
  "Succeeds if p is a pair-like thing."
  [p]
  (fresh [a d]
    (== (lcons a d) p)))

(defn listo
  "Succeeds if l is a proper list."
  [l]
  (conde
   ((emptyo l) s#)
   ((pairo l) (fresh [d]
                     (resto l d)
                     (listo d)))
   ((s# u#))))

(defn lolo
  "Succeeds if l is a list-of-lists."
  [l]
  (conde
   ((emptyo l) s#)
   ((fresh [a]
           (firsto l a)
           (listo a)) (fresh [d]
                             (resto l d)
                             (lolo d)))
   (s# u#)))

(defn twinso
  "Succeeds if l is a list of two identical items."
  [l]
  (fresh [x y]
         (conso x y l)
         (conso x () y)
         ))

;;; Frame 3-36
(defn twinso
  "Succeeds if l is a list of two identical items."
  [l]
  (fresh [x]
         (== (list x x) l)
         ))

;;; Frame 3-37
(defn loto
  "Succeeds if l is a list of twins."
  [l]
  (conde
   ((emptyo l) s#)
   ((fresh [a]
            (firsto l a)
            (twinso a)) (fresh [d]
                               (resto l d)
                               (loto d)))
   (s# u#)))

;;; Frame 3-48
(defn listofo
  "Succeeds if predo, applied to every element of l, succeeds."
  [predo l]
  (conde
   ((emptyo l) s#)
   ((fresh [a]
           (firsto l a)
           (predo a)) (fresh [d]
                             (resto l d)
                             (listofo predo d)))
   (s# u#)))

;;; Frame 3-50
(defn loto
  "Succeeds if l is a list of twins."
  [l]
  (listofo twinso l))

;;; Frame 3-54
(defn eq-caro
  "Succeeds if the car of l is x. Identical to 'firsto.'"
  [l x]
  (firsto l x))

;;; Already defined in the core
#_(defn membero
  "Succeeds if x is a member of list l."
  [x l]
  (conde
   ((emptyo l) u#) ; line unnecessary
   ((eq-caro l x) s#)
   (s# (fresh [d]
              (resto l d)
              (membero x d)))))

(defn pmembero-3-80
  "Succeeds if x is a member of proper list l (frame 3-80)."
  [x l]
  (conde
   ((emptyo l) u#)
   ((eq-caro l x) (resto l ()))
   (s# (fresh [d]
              (resto l d)
              (pmembero-3-80 x d)))))

(defn pmembero-3-83
  "Succeeds if x is a member of proper list l, and produces all
solutions"
  [x l]
  (conde
   ((emptyo l) u#)
   ((eq-caro l x) (resto l ()))
   ((eq-caro l x)  s#)
   (s# (fresh [d]
              (resto l d)
              (pmembero-3-83 x d)))))

(defn pmembero-3-86
  "Succeeds if x is a member of proper list l, and produces all
unique solutions"
  [x l]
  (conde
   ((emptyo l) u#)
   ((eq-caro l x) (resto l ()))
   ((eq-caro l x) (fresh [a d]
                         (resto l (llist a d))))
   (s# (fresh [d]
              (resto l d)
              (pmembero-3-86 x d)))))

(defn pmembero-3-93
  "Succeeds if x is a member of proper list l, and produces all
unique solutions"
  [x l]
  (conde
   ((emptyo l) u#)
   ((eq-caro l x) (fresh [a d]
                         (resto l (llist a d))))
   ((eq-caro l x) (resto l ()))
   (s# (fresh [d]
              (resto l d)
              (pmembero-3-93 x d)))))

(defn first-value
  "Produces a list containing the first value of the given list, l."
  [l]
  (run 1 [y] (membero y l))
  )

(defn memberrevo
  "Succeeds if x is a member of proper list l, and produces all unique
solutions in reverse order (however, this does not work in clojure since
we don't have an order-preserving 'conde'."
  [x l]
  (conde
   ((emptyo l) u#)
   (s# (fresh [d]
              (resto l d)
              (memberrevo x d)))
   (s# (eq-caro l x))
   ))

;;; Frame 4-7
(defn memo
  "Succeeds when out equals the sublist of l whose first (car) is x."
  [x l out]
  (conde
   ((emptyo l)    u#)
   ((eq-caro l x) (== l out))
   (s#            (fresh [d]
                         (resto l d)
                         (memo x d out)))))

;;; Frame 4-24
(defn rembero2
  "Succeeds when 'out' equals 'l' with the first occurrence of 'x'
removed."
  [x l out]
  (conde
   ((emptyo l) (== () out))
   ((eq-caro l x) (resto l out))
   (s# (fresh [a d res]
              (resto l d)
              (rembero2 x d res)
              (firsto l a)
              (conso a res out)
              ))))

;;; Frame 7-5
(defn bit-nando [x y r]
  (conde
   ((== 0 x) (== 0 y) (== 1 r))
   ((== 1 x) (== 0 y) (== 1 r))
   ((== 0 x) (== 1 y) (== 1 r))
   ((== 1 x) (== 1 y) (== 0 r))
   (s# u#)
   ))

(defn bit-xoro [x y r]
  (fresh [s t u]
         (bit-nando x y s)
         (bit-nando x s t)
         (bit-nando s y u)
         (bit-nando t u r))
         )
