(ns mini-kanren.core-test
  (:refer-clojure :exclude [==])
  (:require [clojure.test      :as     test]
            ;; Normally, we would (:require [clojure.test
            ;; :refer :all ...]) except, the symbol "is"
            ;; is already define in the mini-kanren.core
            ;; ... probably. In any event, the compilation
            ;; fails when we try to import all the symbols
            ;; from clojure.test
            [mini-kanren.core  :refer :all ]
            [swiss-arrows.core :refer :all ])
  (:use [clojure.core.logic]))

;;; These are tests derived from "The Reasoned Schemer, Second
;;; Edition, by Daniel P. Friedman, William E. Byrd, Oleg
;;; Kiselyov, and Jason Hemann, MIT Press, ISBN 978-0-262-53551-9.
;;; When not specifically marked as from the 2d ed. (or 2E),
;;; frame, references pertain to the first edition, which doesn't
;;; seem to be easy to find nowadays.

;;;   ___ _              _             _
;;;  / __| |_  __ _ _ __| |_ ___ _ _  / |
;;; | (__| ' \/ _` | '_ \  _/ -_) '_| | |
;;;  \___|_||_\__,_| .__/\__\___|_|   |_|
;;;                |_|

;;; u# is a "goal that fails."
;;; s# is a "goal that succeeds."

;;; This is similar to #t and #f in scheme (a regular Lisp).

(test/deftest foo-basics
  (test/is (=  '(pea) (run* [q] (== q 'pea)))                              "p.4, 2d ed.")
  (test/is (=  '(pea) (run* [q] (== 'pea q)))                              "p.4, 2d ed.")
  ;; _0 is the name of a "fresh" logical variable.
  (test/is (=  '(_0)  (run* [q] (== 'pea 'pea)))                           "frame 1-19, p.6, 2d ed.")
  (test/is (=  '(pea) (run* [q] (fresh (x) (== q 'pea))))                  "frame 1-22, p.7, 2d ed.")
  (test/is (= '((_0)) (run* [q] (fresh (x) (== (cons x '()) q))))          "frame 1-25, p.7, 2d ed.")
  )

(test/deftest foo-test-01-1
  (test/is (= '()      (run* [q] u#))                                      "frame 1-10")
  (test/is (= '(true)  (run* [q] (== q true)))                             "frame 1-11")
  (test/is (= '(true)  (run* [q] (== true q)))                             "frame 1-11")
  (test/is (= '(true)  (run* [q] s# (== true q)))                          "frames 1-13, 14")
  (test/is (= '()      (run* [q] u# (== true q)))                          "frame 1-12")
  (test/is (= '(corn)  (run* [q] s# (== 'corn q)))                         "frames 1-15, 16")
  (test/is (= '()      (run* [q] u# (== 'corn q)))                         "frame 1-17")
  (test/is (= '(false) (run* [q] s# (== false q)))                         "frame 1-18")
  (test/is (= '()      (run* [q] (== false 'x)))                           "frame 1-19 (sort-of)")
  (test/is (= '(_0)    (run* [x] (let [x false] (== false x))))            "frame 1-21")
  (test/is (= '(_0)    (run* [x] (let [x true] (== true x))))              "frame 1-21 (alternate)")
  (test/is (= '()      (run* [x] (let [x false] (== true x))))             "frame 1-22")
  (test/is (= '(true)  (run* [q] (fresh [x] (== true x) (== true q))))     "frames 1-23-25")
  (test/is (= '(true)  (run* [q] (fresh [x] (== false x) (== true q))))    "frame 1-23 (alternate 1)")
  (test/is (= '(true)  (run* [q] (fresh [x] (== q x) (== true q))))        "frame 1-23 (alternate 2)")
  (test/is (= '(true)  (run* [q] (fresh [x] (== x true) (== true q))))     "frame 1-26")
  (test/is (= '(true)  (run* [q] (fresh [x] (== x true) (== q true))))     "frame 1-27")
  (test/is (= '(_0)    (run* [x] s#))                                      "frame 1-28")
  (test/is (= '(_0)    (run* [x]))                                         "no frame (unbound fresh)")
  (test/is (= '(_0)    (run* [x] (let [x false] (fresh [x] (== true x))))) "frame 1-29")
  (test/is (= '((_0 _1))
              (run* [r]
                (fresh [x y]
                  (== (lcons x (lcons y ())) r))))
           "frame 1-30")

  (test/is (= '((_0 _1))
              (run* [s]
                (fresh [t u]
                  (== (lcons t (lcons u ())) s))))
           "frame 1-31")

  (test/is (= '((_0 _1 _0))
              (run* [r]
                (fresh [x]
                  (let [y x]
                    (fresh [x]
                      (== (lcons y (lcons x (lcons y ()))) r))))))
           "frame 1-32")

  ;; Variables are reified "late:" in the order in which they appear
  ;; in "ouput-generating" forms when unifying against variables.
  ;; Here, y gets reified first, then x. They're not reified in the
  ;; order they appear in the 'fresh' declaration.

  (test/is (= '((_0 _1 _0))
              (run* [r]
                (fresh [x]
                   (let [y x]
                     (fresh [x]
                        (== (lcons x (lcons y (lcons x ()))) r))))))
           "frame 1-33")

  (test/is (= '((_0 _1 _0))
              (run* [r] (fresh [x]
                          (let [y x]
                            (fresh [x]
                              (-<>> () ; right-
                                    (lcons x) ; to-
                                    (lcons y) ; left =
                                    (lcons x) ; top-to-bottom
                                    (== r)))))))
           "frame 1-33 (alternate)")

  (test/is (= '()
              (run* [q]
                (== false q)
                (== true q)))
           "frame 1-34")

  (test/is (= '(false)
              (run* [q]
                (== false q)
                (== false q)))
           "frame 1-35")

  (test/is (= '(false)
              (run* [r]
                (== false r)
                (== r false)))
           "frame 1-35 (alternate)")

  (test/is (= '(true)
              (run* [q]
                (let [x q]
                  (== true x))))
           "frame 1-36")

  (test/is (= '(_0)
              (run* [r]
                (fresh [x]
                  (== x r))))
           "frame 1-37")

  (test/is (= '(true)
              (run* [q]
                (fresh [x]
                  (== true x)
                  (== x q))))
           "frame 1-38")

  (test/is (= '(true)
              (run* [q]
                (fresh [x]
                  (== x q)
                  (== true x))))
           "frame 1-39")

  (test/is (= '(true)
              (run* [q]
                (fresh [x]
                  (== x q)
                  (== true q))))
           "frame 1-39 (alternate)")

  (test/is (= '(false)
              (run* [q]
                (fresh [x]
                  (== (= x q) q))))
           "frame 1-40")

  (test/is (= '(true)
              (run* [q]
                    (fresh [x]
                           (== (= x x) q))))
           "frame 1-40 (alternate 1)")

  (test/is (= '(true)
              (run* [q]
                    (fresh [x]
                           (== (= q q) q))))
           "frame 1-40 (alternate 2)")

  (test/is (= '(false)
              (run* [q]
                (let [x q]
                  (fresh [q]
                    (== x (= x q))))))
           "frame 1-40 (alternate 3)")

  (test/is (= '(_0)
              (run* [q]
                (let [x q]
                  (fresh [q]
                    (== q (= x q))))))
           "frame 1-40 (alternate 4)")

  (test/is (= false
              (cond
               false true
               :else false))
           "frame 1-41")

  (test/is (= u#
              (cond
               false s#
               :else u#))
           "frame 1-43")

  (test/is (= '()
              (run* [_]
                (cond
                 false s#
                 :else u#)))
           "frame 1-43 (alternate)")

  (test/is (= '()
              (run* [_]
                    (conde
                     (u# s#)            ; never gets here
                     (s# u#))))         ; backtracks to here, then fails
           "frame 1-44")

  (test/is (= '(_0)
              (run* [_]
                    (conde
                     (u# _)          ; doesn't matter what you put here
                     (s# s#))))      ; backtracks to here, then succeeds
           "frame 1-45")

  (test/is (= '(_0)
              (run* [_]
                    (conde
                     (s# s#)         ; doesn't matter what you put here
                     (s# u#))))      ; backtracks to here, then succeeds
           "frame 1-46")

  (test/is (= '(olive oil)
              (run* [q] (conde
                         ((== 'olive q) s#)
                         ((== 'oil   q) s#)
                         (s#            u#))))
           "frame 1-47")

  (test/is (= '(olive)
              (run 1 [q] (conde
                          ((== 'olive q) s#)
                          ((== 'oil   q) s#)
                          (s#            u#))))
           "frame 1-49")

  (test/is (= '(olive _0 oil)
              (run* [x]
                (conde
                 ((== 'virgin x) u#)
                 ((== 'olive  x) s#)
                 (s# s#)
                 ((== 'oil    x) s#)
                 (s# u#))))
           "frame1-50")

  (test/is (= '(extra olive)
              (run 2 [x]
                (conde
                 ((== 'extra  x) s#)
                 ((== 'virgin x) u#)
                 ((== 'olive  x) s#)
                 (s# s#)
                 ((== 'oil    x) s#)
                 (s# u#))))
           "frame1-52")

  (test/is (= '((split pea))
              (run* [r]
                (fresh [x y]
                  (== 'split x)
                  (== 'pea   y)
                  (== (lcons x (lcons y ())) r))))
           "frame 1-53")

  (test/is (= '((split pea) (navy bean))
              (run* [r]
                (fresh [x y]
                  (conde
                   ((== 'split x) (== 'pea   y))
                   ((== 'navy  x) (== 'bean  y))
                   (s#            u#))
                  (== (lcons x (lcons y ())) r))))
           "frame 1-54")

  (test/is (= [[:split :pea] [:navy :bean]]
              (run* [r]
                (fresh [x y]
                  (conde
                   ((== :split x) (== :pea  y))
                   ((== :navy  x) (== :bean y))
                   (s#            u#))
                  (== [x y] r))))
           "frame 1-54 (variation)")

  (test/is (= [[:split :pea :soup] [:navy :bean :soup]]
              (run* [r]
                (fresh [x y]
                  (conde
                   ((== :split x) (== :pea  y))
                   ((== :navy  x) (== :bean y))
                   (s#            u#))
                  (== [x y :soup] r))))
           "frame 1-55")
  )

(defn teacup "with symbols" [x] (conde
                  ((== 'tea x) s#)
                  ((== 'cup x) s#)
                  (s#          u#)))

(defn teacup2 "with keywords" [x] (conde
                   ((== :tea x) s#)
                   ((== :cup x) s#)
                   (s#          u#)))

(test/deftest foo-test-01-2
  (test/is (= '(tea cup)
              (run* [x]
                (teacup x)))
           "frame 1-56")

  ;; This next one produces its values in the reverse order predicted by
  ;; the book and by common sense.  The top-level conde produces the
  ;; [false true] results (marked A) before producing the nested teacup2
  ;; results (marked B), no matter the order of A and B in the top-level
  ;; conde.  The wiki explains that clojure.logic's "conde" is really
  ;; the book's "condi", and the order of results is not predictable.
  ;; Clojure.logic does not offer an equivalent to the book's "conde".

  (test/is (= [[false true] [:tea true] [:cup true]]
              (run* [r]
                (fresh [x y]
                  (conde
                   ((teacup2 x)  (== true y) s#) ; B
                   ((== false x) (== true y) s#) ; A
                   (s#                       u#))
                  (== [x y] r))))
           "frame 1-57")

  (test/is (= [[false true] [:tea true] [:cup true]]
              (run* [r]
                (fresh [x y]
                  (conde
                   ((== false x) (== true y) s#) ; A
                   ((teacup2 x)  (== true y) s#) ; B
                   (s#                       u#))
                  (== [x y] r))))
           "frame 1-57 (variant 1)")

  ;; We can see this "condi-like" behavior in a simpler case that
  ;; elides the 'fresh':
  (test/is (= [:the-end :tea :cup]
              (run* [x] (conde
                         ((teacup2 x) s#)
                         ((== :the-end x) s#)
                         )))
           "frame 1-57 (variant 2)")

  (test/is (= [:the-end :tea :cup]
              (run* [x] (conde
                         ((== :the-end x) s#)
                         ((teacup2 x) s#)
                         )))
           "frame 1-57 (variant 3)")

  ;; But, if the last test is a fail, orders are preserved:
  (test/is (= [:tea :cup]
              (run* [x] (conde
                         ((teacup2 x) s#)
                         ((== :the-end x) u#)
                         )))
           "frame 1-57 (variant 4)")

  (test/is (= [:the-end]
              (run* [x] (conde
                         ((== :the-end x) s#)
                         ((teacup2 x) u#)
                         )))
           "frame 1-57 (variant 5)")

  (test/is (= '([_0 _1] [_0 _1])
              (run* [r]
                (fresh [x y z]
                  (conde
                   ((== x y) (fresh [x] (== z x)))
                   ((fresh [x] (== y x)) (== z x))
                   (s# u#))
                  (== [y z] r))))
           "frame 1-58")

  (test/is (= '([false _0] [_0 false])
              (run* [r]
                (fresh [x y z]
                  (conde
                   ((== x y) (fresh [x] (== z x)))
                   ((fresh [x] (== y x)) (== z x))
                   (s# u#))
                  (== [y z] r)
                  (== x false))))
           "frame 1-59")

  (test/is (= [false]
              (run* [q]
                (let [a (== true q)
                      b (== false q)]
                  b)))
           "frame 1-60")

  ;; The folloowing two expressions investigate stopping conditions
  ;; for conde.
  (test/is (= '(true false)
              (run* [q]
                    (conde
                     ((== true q) s#)
                     (s# (== false q)))))
           "frame 1-60 (augment 1)")

  ;; Last clause in a conde gets a default u#:
  (test/is (= '(true false)
              (run* [q]
                (conde
                 ((== true q) s#)
                 ((== false q) s#))))
           "frame 1-60 (augment 2)")

  (test/is (= [false]
              (run* [q]
                (let [a (== true q)     ; This never runs.
                      b (fresh [x]
                          (== x q)
                          (== false x))
                      c (conde
                         ((== true q) s#)
                         ((== 42 q)))   ; This never runs.
                      ]
                  b)))                  ; presumably because this is the only goal referenced
           "frame 1-61")
  )

;;;   ___ _              _             ___
;;;  / __| |_  __ _ _ __| |_ ___ _ _  |_  )
;;; | (__| ' \/ _` | '_ \  _/ -_) '_|  / /
;;;  \___|_||_\__,_| .__/\__\___|_|   /___|
;;;                |_|

(test/deftest foo-test-02-1

  (test/is (= 'c
              (let [x (fn [a] a)
                    y 'c]
                (x y)))
           "frame 2-1")

  ;; Regular lists, but not quoted when they contain logic variables,
  ;; work in goals:
  (test/is (= '((_0 _1))
              (run* [q]
                (fresh [y x]            ; order here does not matter
                  (== (list x y) q))))
           "frame 2-2")

  ;; Lcons can deliver the values of variables:
  (test/is (= '((_0 _1))
              (run* [q]
                (fresh [x y]
                       (== (<<-
                            (lcons x)
                            (lcons y) ())
                           q))))
           "frame 2-2 (variant 1)")

  ;; Vectors can get the values of variables out of a goal (though the
  ;; wiki mysteriously says that one should not use vectors
  ;; [https://github.com/clojure/core.logic/wiki/Differences-from-The-Reasoned-Schemer]):
  (test/is (= ['(_0 _1)]
              (run* [q]
                (fresh [x y]
                  (== [x y] q))))
           "frame 2-2 (variant 2)")

  (test/is (= ['(_0 _1)]
              (run* [q]
                (fresh [v w]
                  (let [x v, y w]
                    (== [x y] q)))))
           "frame 2-3")

  ;; "firsto" works on lists, lcons-lists, and vectors
  (test/is (= '(a)
              (run* [r]
                    (firsto
                     (<<- (lcons 'a)
                          (lcons 'c)
                          (lcons 'o)
                          (lcons 'r)
                          (lcons 'n) ())
                     r)))
           "frame 2-6")

  (test/is (= '(a)
              (run* [r]
                (firsto '(a c o r n) r)))
           "frame 2-6")

  (test/is (= '(a)
              (run* [r]
                (firsto ['a 'c 'o 'r 'n] r)))
           "frame 2-6")

  (test/is (= '(true)
              (run* [r]
                (firsto '(a c o r n) 'a)
                (== true r)))
           "frame 2-6 (variant)")

  ;; No solution to the next one; it fails:
  (test/is (= '()
              (run* [r]
                (firsto '(a c o r n) 'z)
                (== 'anything r)))
           "frame 2-6 (augment)")

  ;; You don't need to use lcons if you're doing internal associations
  (test/is (= '((pear pear _0))
              (run* [r x y]
                (firsto
                 (<<- (lcons r) (lcons y) ())
                 x)
                (== 'pear x)))
           "frame 2-6 (augment)")

  (test/is (= '((pear pear _0))
              (run* [r x y]
                (firsto
                 (list r y)
                 x)
                (== 'pear x)))
           "frame 2-6 (augment)")

  (test/is (= '((pear pear _0))
              (run* [r x y]
                (firsto
                 [r y]
                 x)
                (== 'pear x)))
           "frame 2-6 (augment)")

  ;; Back to regular lisp for a trice:
  (test/is (= '(grape a) (cons (first '(grape raisin pear))
                               (first '((a) (b) (c)))))
           "frame 2-10")

  (test/is (= '((grape a))
              (run* [r]
                (fresh [x y]
                  (firsto '(grape raisin pear) x)
                  (firsto '((a) (b) (c))       y)
                  ;; LCONS can do improper pairs; that's
                  ;; apparently its main reason for existence:
                  (== r (lcons x y)))))
           "frame 2-11")

  (test/is (= '(c)
              (run* [r]
                (fresh [v]
                  (resto '(a c o r n) v)
                  (firsto v r))))
           "frame 2-15")

  (test/is (= '(((raisin pear) a))
              (run* [r]
                (fresh [x y]
                  (resto '(grape raisin pear) x)
                  (firsto '((a) (b) (c))      y)
                  (== (lcons x y) r))))
           "frame 2-18")

  (test/is (= [true]
              (run* [q]
                (resto '(a c o r n) '(c o r n))
                (== true q)))
           "frame 2-19")

  ;; works with vectors, lists, lcons-trains, and llists:
  (test/is (= '(o)
              (run* [x]
                (resto '(c o r n)
                       [x 'r 'n])))
           "frame 2-20")

  (test/is (= '(o)
              (run* [x]
                (resto '(c o r n)
                  (list x 'r 'n))))
           "frame 2-20 (alternate 1)")

  (test/is (= '(o)
              (run* [x]
                (resto '(c o r n)
                   (<<- (lcons x)
                        (lcons 'r)
                        (lcons 'n)
                        ())
                   )))
           "frame 2-20 (alternate 2)")

  (test/is (= '(o)
              (run* [x]
                (resto '(c o r n)
                  (llist x 'r 'n ()))))
           "frame 2-20 (alternate 3)")

  (test/is (= '((a c o r n))
              (run* [l]
                (fresh [x]
                  (resto l '(c o r n))
                  (firsto l x)
                  (== 'a x))))
           "frame 2-21")

  (test/is (= '(((a b c) d e))
              (run* [l]
                (conso '(a b c)
                       '(d e)
                       l)))
           "frame 2-22")

  (test/is (= '(d)
              (run* [x]
                (conso
                 x
                 '(a b c)
                 '(d a b c))))
           "frame 2-23")

  (test/is (= '((e a d c))
              (run* [r]
                (fresh [x y z]
                  (== (list 'e 'a 'd x) r)
                  (conso y (list 'a z 'c) r))))
           "frame 2-24")

  (test/is (= '([(e a d c) d])
              (run* [r z]
                (fresh [x y]
                  (== (list 'e 'a 'd x) r)
                  (conso y (list 'a z 'c) r))))
           "frame 2-24 (variant)")

  (test/is (= '(d)
              (run* [x]
                (conso
                 x
                 ['a x 'c]
                 ['d 'a x 'c])))
           "frame 2-25")

  (test/is (= (run* [l]
                (fresh [x]
                  (== ['d 'a x 'c] l)
                  (conso x ['a x 'c] l)))
              '((d a d c)))
           "frame 2-26")

  (test/is (= (run* [l]
                (fresh [d x y w s]
                  (conso w ['a 'n 's] s)
                  (resto l s)
                  (firsto l x)
                  (== 'b x)
                  (resto l d)
                  (firsto d y)
                  (== 'e y)))
              '((b e a n s)))
           "frame 2-29")

  (test/is (= (run* [l]
                (fresh [d x y w s]
                  (conso w ['a 'n 's] s)
                  (conso 'b s l)        ; (firsto l x) (resto l s)
                  (firsto d y)
                  (resto l d)
                  (== 'e y)))
              '((b e a n s)))
           "frame 2-29 (variant)")

  (test/is (= '(_0)
              (run* [x]
                (emptyo ())))           ; emptyo is nullo
           "frame 2-31")

  ;; No solution:
  (test/is (= ()
              (run* [x]
                (emptyo
                 '(grape raisin pear))))
           "frame 2-30")

  ;; Solution reaches (== true q) because the empty list is emptyo:
  (test/is (= '(true)
              (run* [q]
                (emptyo ())
                (== true q)))
           "frame 2-33")

  ;; Solution equals the empty list:
  (test/is (= '(())
              (run* [x]
                (emptyo x)))
           "frame 2-34")

  (test/is (= 'plum 'plum)
           "frame 2-37")

  ;; No solution:
  (test/is (= (run* [q]
                (== 'pear 'plum)
                (== 'true q)) '())
           "frame 2-38")

  (test/is (= (run* [q]
                (pairo (lcons 'pear ()))
                (== 'true q))
              '(true))
           "frame 2-47")

  (test/is (= (run* [q]
                (firsto (lcons 'pear ()) q))
              '(pear))
           "frame 2-48")

  (test/is (= (run* [q]
                (resto (lcons 'pear ()) q))
              '(()))
           "frame 2-49")

  (test/is (= '(())
              (run* [q] (resto (lcons 'pear ()) q)))
           "frame 2-50")

  (test/is (= (run* [x] (pairo x))

              ;; The notation '(_0 . _1) ends up not being equal to the
              ;; result of (pairo x), even though they look exactly the
              ;; same, because . is part of Clojure's Java-interop
              ;; syntax.

              (run* [q]
                (fresh [x y]
                  (== (lcons x y) q))))
           "frame 2-??")

  ;; Here is a principle nice way of handling dot -- just boil things
  ;; down to their elements.
  (test/is (= (list (llist '_0 '_1))
              (run* [q] (pairo q)))
           "frame 2-??")

  ;; Here is a trick that exploits stringification. It's visually nice,
  ;; but it doesn't always work (as we shall see below)
  (test/is (= '("(_0 . _1)")
              (map str
                   (run* [q] (pairo q))))
           "frame 2-??")

  (test/is (= '("(_0 _1 . salad)")
              (map str (run* [r]
                             (fresh [x y]
                                    (== (lcons x (lcons y 'salad)) r)))))
           "frame 2-52")

  (test/is (= '(true)
              (run* [q]
                    (== q true)
                    (pairo (lcons q q))))
           "frame 2-54")

  (test/is (= '()  (run* [q]
                         (== q true)
                         (pairo '())))
           "frame 2-55")

  (test/is (= '()  (run* [q]
                         (== q true)
                         (pairo 'pair)))
           "frame 2-56")

  (test/is (= (map str (run* [q]
                             (pairo q)))
              '("(_0 . _1)"))
           "frame 2-57")

  (test/is (= '(_0)
             (run* [r]
                   (pairo (lcons r 'pear))))
           "frame 2-58")
)


;;;   ___ _              _             ____
;;;  / __| |_  __ _ _ __| |_ ___ _ _  |__ /
;;; | (__| ' \/ _` | '_ \  _/ -_) '_|  |_ \
;;;  \___|_||_\__,_| .__/\__\___|_|   |___/
;;;                |_|

(test/deftest foo-test-03-1
  (test/is (= '(true)
              (run* [q]
                    (== (llist 'a 'b)
                        (lcons 'a 'b))
                    (== q 'true)))
           "frame 3-??")

  (test/is (= '(true) (run* [q]
                            (listo ())
                            (== q 'true)))
           "frame 3-??")

  (test/is (= '([true _0])
              (run* [q x]
                    (listo '(a b x d))
                    (== q 'true)))
           "frame 3-7 (buggy)")

  (test/is (= '([true _0])
              (run* [q x]
                    (listo `(a b ~x d))
                    (== q 'true)))
           "frame 3-7 (buggy)")

  (test/is (= '([true _0])
              (run* [q x]
                    (listo (llist 'a 'b x 'd ()))
                    (== q 'true)))
           "frame 3-7 (augment 1)")

  ;; This next one is not a proper list
  (test/is (= '()
              (run* [q x]
                    (listo (llist 'a 'b x 'd))
                    (== q 'true)))
           "frame 3-7 (augment 2)")

  ;; But it is a pair
  (test/is (= '([true _0])
              (run* [q x]
                    (pairo (llist 'a 'b x 'd))
                    (== q 'true)))
           "frame 3-7 (augment 3)")

  (test/is (= '(())
              (run 1 [x]
                   (listo (llist 'a 'b 'c x))))
           "frame 3-11")

  ;; Don't run* the next one: it finds an infinite
  ;; number of solutions and does not terminate:
  (test/is (= '(() (_0) (_0 _1) (_0 _1 _2) (_0 _1 _2 _3))
              (run 5 [x] (listo (llist 'a 'b 'c x))))
           "frame 3-14")

  (test/is (= '(())
              (run 1 [l] (lolo l)))
           "frame 3-20")

  (test/is (= [true]
              (run* [q]
                    (fresh [x y]
                           (lolo (llist
                                  (llist 'a 'b ())
                                  (llist x 'c ())
                                  (llist 'd y ())
                                  ())))
                    (== q true)))
           "frame 3-21")

  ;; Emptyo always succeeds against a fresh var:
  (test/is (= '(())
              (run* [q] (emptyo q)))
           "frame 3-22")

  (test/is (= '(())
              (run 1 [x]
                   (lolo
                    (llist
                     '(a b)
                     '(c d)
                     x))))
           "frame 3-22")

  (test/is (=
            '(())
            (run 1 [x] (lolo
                        (llist
                         '(a b)
                         '(c d)
                         x))))
           "frame 3-23")

  ;; Clojure.logic's conde really produces interleaved results, so it
  ;; gets around to solutions that TRS's conde never finds. See
  ;; http://bit.ly/1a8QmPJ .

  (test/is
   (= '(()
        (())
        ((_0))
        (() ())
        ((_0 _1))
        (() (_0))
        ((_0) ())
        (() () ())
        ((_0 _1 _2)))
      (run 9 [x] (lolo (llist
                        '(a b)
                        '(c d)
                        x))))
   "frame 3-24")

  (test/is
   (= '(((a b) (c d) () () () ()))
      (run* [y]
            (fresh [x]
                   (== x '(() () () ()))
                   (== y (llist
                          '(a b)
                          '(c d)
                          x)))))
   "frame 3-25")

  ;; Checking an equivalence between conso and llist:
  (test/is
   (= (run 1 [q] (== q (llist 1 2)))
      (run 1 [q] (conso 1 2 q)))
   "frame 3-25 (augment 1)")

  (test/is
   (= '("(1 . 2)")
      (map str (run* [q]
                     (== q (llist 1 2))
                     (conso 1 2 q))))
   "frame 3-25 (augment 2)")

  (test/is
   (= '(true)
      (run* [q]
            (twinso '(tofu tofu))
            (== true q)))
   "frame 3-32")

  (test/is
   (= '(true)
      (run* [q]
            (twinso (list 'tofu 'tofu))
            (== true q)))
   "frame 3-32 (alternate 1)")

  (test/is
   (= '(true)
      (run* [q]
            (twinso (llist 'tofu 'tofu ()))
            (== true q)))
   "frame 3-32 (alternate 2)")

  (test/is
   (= '(true)
      (run* [q]
            (twinso ['tofu 'tofu])
            (== true q)))
   "frame 3-32 (alternate 3)")

  (test/is
   (= '(tofu)
      (run* [q]
            (twinso (list q 'tofu))))
   "frame 3-33")

  (test/is
   (= '(tofu)
      (run* [q]
            (twinso (list 'tofu q))))
   "frame 3-33 (alternate 1)")

  (test/is
   (= '(() ((_0 _0)))
      (run 2 [z] (loto (llist
                        '(g g)
                        z))))
   "frame 3-37")

  (test/is
   (= '(()
        ((_0 _0))
        ((_0 _0) (_1 _1))
        ((_0 _0) (_1 _1) (_2 _2))
        ((_0 _0) (_1 _1) (_2 _2) (_3 _3))
        )
      (run 5 [z] (loto (llist
                        '(g g)
                        z))))
   "frame 3-42")

  (test/is
   (= '((e (_0 _0) ())
        (e (_0 _0) ((_1 _1)))
        (e (_0 _0) ((_1 _1) (_2 _2)))
        (e (_0 _0) ((_1 _1) (_2 _2) (_3 _3)))
        (e (_0 _0) ((_1 _1) (_2 _2) (_3 _3) (_4 _4)))
        )
      (run 5 [r]
           (fresh [w x y z]
                  (loto (llist '(g g)
                               (list 'e w)
                               (list x y)
                               z))
                  (== r
                      (list w
                            (list x y)
                            z)))))
   "frame 3-45")

  (test/is
   (= '(((g g) (e e) (_0 _0))
        ((g g) (e e) (_0 _0) (_1 _1))
        ((g g) (e e) (_0 _0) (_1 _1) (_2 _2))
        ((g g) (e e) (_0 _0) (_1 _1) (_2 _2) (_3 _3))
        ((g g) (e e) (_0 _0) (_1 _1) (_2 _2) (_3 _3) (_4 _4))
        )
      (run 5 [out]
           (fresh [w x y z]
                  (== out
                      (llist
                       '(g g)
                       (list 'e w)
                       (list x y)
                       z))
                  (listofo twinso out))))
   "frame 3-49")

  (test/is
   (= '(true)
      (run* [q]
            (membero 'olive '(virgin olive oil))
            (== true q)))
   "frame 3-57")

  (test/is
   (= '(hummus)
      (run 1 [y]
           (membero y '(hummus with pita))))
   "frame 3-58")

  (test/is
   (= '(hummus with pita)
      (run 3 [y]
           (membero y '(hummus with pita))))
   "frame 3-58 (alternate 1))")

  ;; I ask for three solutions, but there is only one.
  (test/is
   (= '(pita)
      (run 3 [y]
           (membero y '(pita))))
   "frame 3-60")

  (test/is
   (= '(hummus with pita)
      (run* [y]
            (membero y '(hummus with pita))))
   "frame 3-62")

  (test/is
   (= '(e)
      (run* [x]
            (membero 'e (list 'pasta x 'fagioli))))
   "frame 3-66")

  (test/is
   (= '(_0)
      (run 1 [x]
           (membero 'e (list 'pasta 'e x 'fagioli))))
   "frame 3-69")

  (test/is
   (= '(e)
      (run 1 [x]
           (membero 'e (list 'pasta x 'e 'fagioli))))
   "frame 3-70")

  (test/is
   (= '((e _0) (_0 e))
      (run* [r]
            (fresh [x y]
                   (membero 'e (list 'pasta x 'fagioli y))
                   (== (list x y) r))))
   "frame 3-71")

  ;; Remember we can't test equality against things with dots.
  (test/is
   (= '("(tofu . _0)"
        "(_0 tofu . _1)"
        "(_0 _1 tofu . _2)"
        "(_0 _1 _2 tofu . _3)"
        "(_0 _1 _2 _3 tofu . _4)")
      (map str (run 5 [l] (membero 'tofu l))))
   "frame 3-76")

  (test/is
   (= '((tofu)
        (_0 tofu)
        (_0 _1 tofu)
        (_0 _1 _2 tofu)
        (_0 _1 _2 _3 tofu))
      (run 5 [l] (pmembero-3-80 'tofu l)))
   "frame 3-80")

  (test/is
   (= '(true)
      (run* [q]
            (pmembero-3-80 'tofu '(a b tofu d tofu))
            (== q true)))
   "frame 3-81")

  (test/is
   (= '(true true true)
      (run* [q]
            (pmembero-3-83 'tofu '(a b tofu d tofu))
            (== q true)))
   "frame 3-84")

  ;; This is principled but hard to read.
  (test/is
   (= (list
       (llist 'tofu ())
       (llist 'tofu '_0 '_1)
       (llist '_0 'tofu ())
       (llist '_0 'tofu '_1 '_2)
       (llist '_0 '_1 'tofu ())
       (llist '_0 '_1 'tofu '_2 '_3)
       (llist '_0 '_1 '_2 'tofu ())
       (llist '_0 '_1 '_2 'tofu '_3 '_4)
       (llist '_0 '_1 '_2 '_3 'tofu ())
       (llist '_0 '_1 '_2 '_3 'tofu '_4 '_5)
       (llist '_0 '_1 '_2 '_3 '_4 'tofu ())
       (llist '_0 '_1 '_2 '_3 '_4 'tofu '_5 '_6))
      (run 12 [l] (pmembero-3-86 'tofu l)))
   "frame 3-89")

  ;; This is more tricky to test since core.logic produces lazy
  ;; sequences that are difficult to materialize internally. For now,
  ;; we'll work around this by calling "vector" on every solution, which
  ;; introduces one level of meaningless brackets, but we'll live with
  ;; it for testing purposes.
  (test/is
   (= '("[(tofu)]"
        "[(tofu _0 . _1)]"
        "[(_0 tofu)]"
        "[(_0 tofu _1 . _2)]"
        "[(_0 _1 tofu)]"
        "[(_0 _1 tofu _2 . _3)]"
        "[(_0 _1 _2 tofu)]"
        "[(_0 _1 _2 tofu _3 . _4)]"
        "[(_0 _1 _2 _3 tofu)]"
        "[(_0 _1 _2 _3 tofu _4 . _5)]"
        "[(_0 _1 _2 _3 _4 tofu)]"
        "[(_0 _1 _2 _3 _4 tofu _5 . _6)]")
      (map (comp str vector) (run 12 [l] (pmembero-3-86 'tofu l))))
   "frame 3-89")

  (test/is
   (= '("[(tofu _0 . _1)]"
        "[(tofu)]"
        "[(_0 tofu _1 . _2)]"
        "[(_0 tofu)]"
        "[(_0 _1 tofu _2 . _3)]"
        "[(_0 _1 tofu)]"
        "[(_0 _1 _2 tofu _3 . _4)]"
        "[(_0 _1 _2 tofu)]"
        "[(_0 _1 _2 _3 tofu _4 . _5)]"
        "[(_0 _1 _2 _3 tofu)]"
        "[(_0 _1 _2 _3 _4 tofu _5 . _6)]"
        "[(_0 _1 _2 _3 _4 tofu)]")
      (map (comp str vector) (run 12 [l] (pmembero-3-93 'tofu l))))
   "frame 3-94")

  (test/is (= '(pasta)
              (first-value '(pasta e fagioli)))
           "frame 3.96")

  ;; Frame 3-100 -- Our conde doesn't have the same order
  ;; as the book's conde.
  (test/is
   (= '(pasta e fagioli)
      (run* [x] (memberrevo x '(pasta e fagioli))))
   "frame 3-100"))

;;;   ___ _              _             _ _
;;;  / __| |_  __ _ _ __| |_ ___ _ _  | | |
;;; | (__| ' \/ _` | '_ \  _/ -_) '_| |_  _|
;;;  \___|_||_\__,_| .__/\__\___|_|     |_|
;;;                |_|

(test/deftest foo-test-04-1

  (defn eq-car? [l x]
    (cond
     (empty? l)      false
     (= x (first l)) true
     true            false))

  (defn mem [x l]
    (cond
     (empty? l)      false
     (eq-car? l x)   l
     true            (mem x (rest l))))

  (test/is
   (= '(tofu d peas e)
      (mem 'tofu '(a b tofu d peas e)))
   "frame 4-1")

  (test/is
   (= false
      (mem 'tofu '(a b peas d peas e)))
   "frame 4-2")

  (test/is
   (= '((tofu d peas e))
      (run* [out] (== out (mem 'tofu '(a b tofu d peas e)))))
   "frame 4-3")

  (test/is
   (= '(peas e)
      (mem 'peas
           (mem 'tofu '(a b tofu d peas e ))))
   "frame 4-4")

  (test/is
   (= '((tofu d tofu e))
      (run 1 [out] (memo 'tofu '(a b tofu d tofu e) out)))
   "frame 4-7")

  (test/is
   (= '((tofu d tofu e))
      (run 1 [out]
           (fresh [x]
                  (memo 'tofu (list 'a 'b x 'd 'tofu 'e) out))))
   "frame 4-11")

  (test/is
   (= '(tofu)
      (run* [r]
            (memo r
                  '(a b tofu d tofu e)
                  '(tofu d tofu e))))
   "frame 4-12")

  (test/is
   (= '(true)
      (run* [q]
            (memo 'tofu '(tofu e) '(tofu e))
            (== q true)))
   "frame 4-13")

  (test/is
   (= '()
      (run* [q]
            (memo 'tofu '(tofu e) '(tofu))
            (== q true)))
   "frame 4-14")

  (test/is
   (= '(tofu)
      (run* [x]
            (memo 'tofu '(tofu e) (list x 'e))))
   "frame 4-15")

  (test/is
   (= '((tofu d tofu e) (tofu e))
      (run* [out]
            (fresh [x]
                   (memo 'tofu (list 'a 'b x 'd 'tofu 'e) out))))
   "frame 4-17")

  (test/is
   (= (list '_0
            '_0
            (llist 'tofu '_0)
            (llist '_0 'tofu '_1)
            (llist '_0 '_1 'tofu '_2)
            (llist '_0 '_1 '_2 'tofu '_3))
      (run 6 (z)
           (fresh [u]
                  (memo 'tofu (llist 'a 'b 'tofu 'd 'tofu 'e z) u))))
   "frame 4-18")

  (defn rember [x l]
    (cond
     (empty? l) ()
     (eq-car? l x) (rest l)
     true (cons (first l) (rember x (rest l)))))

  (test/is
   (= '(a b d peas e)
      (rember 'peas '(a b peas d peas e)))
   "frame 4-23")

  (test/is
   (= '((a b d peas e))
      (run 1 [out]
           (fresh [y] (rembero 'peas
                               (list 'a 'b y 'd 'peas 'e)
                               out))))
   "frame 4-30")

  (test/is
   (= '((a b d peas e)
        ((a b _0 d e) :- (!= (_0 peas))))
      (run 2 [out]
           (fresh [y] (rembero 'peas
                               (list 'a 'b y 'd 'peas 'e)
                               out))))
   "frame 4-30 (alternate)")

  ;; Looks like clojure.logic is doing something more sophisticated than
  ;; mini-Kanren does. Looking at the third inference, Clojure.logic
  ;; infers that (rembero y (a b y d z e) is (a b d _0 e) if 'y' is not
  ;; 'a' and 'y' is not 'b', meaning that the first two inferences have
  ;; been skipped. In the fourth inference, I think it's binding 'y' to
  ;; 'd', but it has an impossibility in its condition, namely (!= (_0
  ;; _0)). I'll leave this a mystery for now.
  (test/is
   (= '((b a d _0 e)
        (a b d _0 e)
        ((a b d _0 e) :- (!= (_1 b)) (!= (_1 a)))
        ((a b _0 d e) :-  (!= (_0 _0)) (!= (_0 b)) (!= (_0 d)) (!= (_0 a))))
      (run* [out]
            (fresh [y z]
                   (rembero y (list 'a 'b y 'd z 'e) out))))
   "frame 4-31")

  (test/is
   (= '((b a d _0 e)
        (a b d _0 e)
        (a b d _0 e)
        (a b d _0 e)
        (a b _0 d e)
        (a b e d _0)
        (a b _0 d _1 e))
      (run* [out]
            (fresh [y z]
                   (rembero2 y (list 'a 'b y 'd z 'e) out))))
   "frame 4-31 (alternate 1)")

  ;; The following exhibits the solutions for y and z.
  (test/is (= '(  [(b a  d _0    e)   a _0]
                  [(a b  d _0    e)   b _0]
                  [(a b  d _0    e)  _1 _0]
                  [(a b  d _0    e)   d _0]
                  [(a b _0  d    e)  _0 _0]
                  [(a b  e  d _0  )   e _0]
                  [(a b _0  d _1 e)  _0 _1])
              (run* [out y z]
                    (rembero2 y (list 'a 'b y 'd z 'e) out)))
           "frame 4-31 (alternate 2)")

  ;; This mystery is getting deeper; this result isn't anything like
  ;; the book's result.
  (test/is
   (= '([(d d) d d])
      (run* [r y z]
            (rembero y
                     (list y 'd z 'e)
                     (list y 'd   'e))
            (== (list y z) r)))
   "frame 4-49")

  ;; Resolve this mystery by implementing our own rembero, called
  ;; rembero2, in the file utils.clj, according to the guidelines in
  ;; Frame 4-24. TODO: examine the implementation of "rembero" in
  ;; clojure.core/logic.
  (test/is
   (= '(  [( d  d)  d  d]
          [( d  d)  d  d]
          [(_0 _0) _0 _0]
          [( e  e)  e  e]
          )
      (run* [r y z]
            (rembero2 y (list y 'd z 'e) (list y 'd 'e))
            (== (list y z) r)))
   "frame 4-49")

  (test/is
   (=
    (list (vector (llist 'b 'a 'd '_0 '_1)                  'a '_0 '_1)
          (vector (llist 'a 'b 'd '_0 '_1)                  'b '_0 '_1)
          (vector (llist 'a 'b 'd '_0 '_1)                 '_2 '_0 '_1)
          (vector (llist 'a 'b 'd '_0 '_1)                  'd '_0 '_1)
          (vector (llist 'a 'b '_0 'd '_1)                 '_0 '_0 '_1)
          (vector (list  'a 'b '_0 'd '_1)                 '_0 '_1 '())
          (vector (llist 'a 'b '_0 'd '_1 '_2)             '_0 '_1  (llist '_0 '_2))
          (vector (list  'a 'b '_0 'd '_1 '_2)             '_0 '_1  (list  '_2))
          (vector (llist 'a 'b '_0 'd '_1 '_2 '_3)         '_0 '_1  (llist '_2 '_0 '_3))
          (vector (list  'a 'b '_0 'd '_1 '_2 '_3)         '_0 '_1  (list  '_2 '_3))
          (vector (llist 'a 'b '_0 'd '_1 '_2 '_3 '_4)     '_0 '_1  (llist '_2 '_3 '_0 '_4))
          (vector (list  'a 'b '_0 'd '_1 '_2 '_3 '_4)     '_0 '_1  (list  '_2 '_3 '_4))
          (vector (llist 'a 'b '_0 'd '_1 '_2 '_3 '_4 '_5) '_0 '_1  (llist '_2 '_3 '_4 '_0 '_5))
          )
    (run 13 [out y z w]
         (rembero2 y (llist 'a 'b y 'd z w) out)))
   "frame 4-57")

  (defn surpriseo [s]
    (rembero2 s '(a b c) '(a b c)))

  (test/is
   (= '(d)
      (run* [q] (== q 'd) (surpriseo q)))
   "frame 4-69")

  (test/is
   (= '(_0)
      (run* [q] (surpriseo q)))
   "frame 4-70")

(test/is
   (= '(b)
    (run* [q] (== q 'b) (surpriseo q)))
   "frame 4-72")
  )

;;;   ___ _              _             ___
;;;  / __| |_  __ _ _ __| |_ ___ _ _  | __|
;;; | (__| ' \/ _` | '_ \  _/ -_) '_| |__ \
;;;  \___|_||_\__,_| .__/\__\___|_|   |___/
;;;                |_|

(test/deftest foo-test-05-1

  (test/is
   (= '((cake tastes yummy))
      (run* [x] (appendo
                 '(cake)
                 '(tastes yummy)
                 x)))
   "frame 5-10")

  (test/is
   (= '((cake with ice _0 tastes yummy))
      (run* [x]
            (fresh [y]
                   (appendo
                    (list 'cake 'with 'ice y)
                    '(tastes yummy)
                    x))))
   "frame 5-11")

  (test/is
   (= (list (llist 'cake 'with 'ice 'cream '_0))
      (run* [x]
            (fresh [y]
                   (appendo
                    (list 'cake 'with 'ice 'cream)
                    y
                    x))))
   "frame 5-12")

  (test/is
   (= '((cake with ice d t))
      (run 1 [x]
           (fresh [y]
                  (appendo
                   (llist 'cake 'with 'ice y)
                   '(d t)
                   x))))
   "frame 5-13")

  (test/is
   (= '(())
      (run 1 [y]
           (fresh [x]
                  (appendo
                   (llist 'cake 'with 'ice y)
                   '(d t)
                   x))))
   "frame 5-13")

  (test/is
   (= '((cake with ice d t)
        (cake with ice _0 d t)
        (cake with ice _0 _1 d t)
        (cake with ice _0 _1 _2 d t)
        (cake with ice _0 _1 _2 _3 d t))
      (run 5 [x]
           (fresh [y]
                  (appendo
                   (llist 'cake 'with 'ice y)
                   '(d t)
                   x))))
   "frame 5-16")

  (test/is
   (= '(()
        (_0)
        (_0 _1)
        (_0 _1 _2)
        (_0 _1 _2 _3))
      (run 5 [y]
           (fresh [x]
                  (appendo
                   (llist 'cake 'with 'ice y)
                   '(d t)
                   x))))
   "frame 5-17")

  (test/is
   (= '((cake with ice d t)
        (cake with ice _0 d t _0)
        (cake with ice _0 _1 d t _0 _1)
        (cake with ice _0 _1 _2 d t _0 _1 _2)
        (cake with ice _0 _1 _2 _3 d t _0 _1 _2 _3))
      (run 5 [x]
           (fresh [y]
                  (appendo
                   (llist 'cake 'with 'ice y)
                   (llist 'd 't y)
                   x))))
   "frame 5-20")

  (test/is
   (= '(()
        (cake)
        (cake with)
        (cake with ice)
        (cake with ice d)
        (cake with ice d t))
      (run* [x]
            (fresh [y]
                   (appendo x y '(cake with ice d t)))))
   "frame 5-23")
  )



;;;   ___ _              _              __
;;;  / __| |_  __ _ _ __| |_ ___ _ _   / /
;;; | (__| ' \/ _` | '_ \  _/ -_) '_| / _ \
;;;  \___|_||_\__,_| .__/\__\___|_|   \___/
;;;                |_|




;;;   ___ _              _             ____
;;;  / __| |_  __ _ _ __| |_ ___ _ _  |__  |
;;; | (__| ' \/ _` | '_ \  _/ -_) '_|   / /
;;;  \___|_||_\__,_| .__/\__\___|_|    /_/
;;;                |_|


(test/deftest foo-test-07-1

  (test/is
   (= '([0 0 0] [1 0 0] [0 1 1] [1 1 1])
      (run* [s x y]
            (bit-xoro x y 0)
            (conde
             ((== s 0))
             ((== s 1)))))
   "frame 7-6")
  )

;;;   ___ _              _             ___
;;;  / __| |_  __ _ _ __| |_ ___ _ _  ( _ )
;;; | (__| ' \/ _` | '_ \  _/ -_) '_| / _ \
;;;  \___|_||_\__,_| .__/\__\___|_|   \___/
;;;                |_|




;;;   ___ _              _             ___
;;;  / __| |_  __ _ _ __| |_ ___ _ _  / _ \
;;; | (__| ' \/ _` | '_ \  _/ -_) '_| \_, /
;;;  \___|_||_\__,_| .__/\__\___|_|    /_/
;;;                |_|




;;;   ___ _              _             _  __
;;;  / __| |_  __ _ _ __| |_ ___ _ _  / |/  \
;;; | (__| ' \/ _` | '_ \  _/ -_) '_| | | () |
;;;  \___|_||_\__,_| .__/\__\___|_|   |_|\__/
;;;                |_|




;;;   ___ _              _             _ _
;;;  / __| |_  __ _ _ __| |_ ___ _ _  / / |
;;; | (__| ' \/ _` | '_ \  _/ -_) '_| | | |
;;;  \___|_||_\__,_| .__/\__\___|_|   |_|_|
;;;                |_|




;;;   ___ _              _             _ ___
;;;  / __| |_  __ _ _ __| |_ ___ _ _  / |_  )
;;; | (__| ' \/ _` | '_ \  _/ -_) '_| | |/ /
;;;  \___|_||_\__,_| .__/\__\___|_|   |_/___|
;;;                |_|




;;;    _                          _ _
;;;   /_\  _ __ _ __  ___ _ _  __| (_)_ __
;;;  / _ \| '_ \ '_ \/ -_) ' \/ _` | \ \ /
;;; /_/ \_\ .__/ .__/\___|_||_\__,_|_/_\_\
;;;       |_|  |_|
