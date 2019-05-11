(ns midje_motivation.t-core
  (:use midje.sweet)
  (:use [midje_motivation.core]
        [swiss-arrows.core]
        [clojure.algo.monads]))

;;; To run this, add {:user {:plugins [[lein-midje "3.0.0"]]}} to your
;;; ~/.lein/profiles.clj. Then type "lein repl" at a command
;;; prompt. Then type "(use 'midje.repl)" and "autotest" in the
;;; repl. Every time you save either this file or the corresponding
;;; source file, all the tests will run again.

;;; The following is adapted from www.clojure

(facts "about half-double and inc-int"
       (fact (half-double 10)                     => [5 20]
             (inc-int      3)                     => [8 13])
       (fact (apply concat
                    (map inc-int
                         (half-double 8)))        => [9 14 21 26]))

(facts "about clojure.algo.monads"
       (fact "the identity monad binds like \"let\""
             (domonad identity-m
                      [a 1
                       b (inc a)]
                      (* a b))                         => 2)
       (fact "the maybe monad propagates silently"
             (domonad maybe-m
                      [a 1
                       b (inc a)]
                      (* a b))                         => 2)
       (fact "the maybe monad flows nils"
             (domonad maybe-m
                      [a nil
                       b (inc a)]
                      (* a b))                         => nil
             (domonad maybe-m
                      [a 1
                       b nil]
                      (* a b))                         => nil
             )
       (fact "the sequence monad stands in for \"for\""
             (domonad sequence-m
                      [a (range 5)
                       b (range a)]
                      (* a b))
             => (for [a (range 5), b (range a)] (* a b)))
       (fact "m-lift in the sequence monad is just map"
             (with-monad sequence-m
               ((m-lift 1 #(* % %)) '(1 2 3)))         => '(1 4 9)
             )

       (fact "m-seq generates chains"
             (with-monad sequence-m
               (m-seq (replicate 2 '(a b c)))
               => (for [x '(a b c), y '(a b c)] (list x y))
               )
             (with-monad maybe-m
               (m-seq '(1 2 nil 4 5)) => nil)
             )
       )


(facts "about `swiss-arrows"
       (fact "diamond wand pushes values into <> markers"
             (-<> 2 (* <> 5))                     => 10
             (-<> 2 (* <> 5) [1 2 <> 3 4])        => [1 2 10 3 4]
             (-<> 2 (* <> 5) (vector 1 2 <> 3 4)) => [1 2 10 3 4]
             (-<> 'foo {:a <> :b 'bar})           => {:a 'foo :b 'bar}
             (-<> :a
                  (map <> [{:a 1} {:a 2}])
                  (map (partial + 2) <>)
                  reverse)                        => [4 3]
             )
       (fact "diamond wand's push locale defaults to first position, like ->"
             (-<> 0 [1 2 3])                      => [0 1 2 3]
             (-<> 0 (list 1 2 3))                 => '(0 1 2 3)
             (-<> 2 (* 5))                        => 10
             )
       (fact "diamond wand does not push as expected into list literals"
             (-<> 0 '(1 2 3))                     =not=> '(0 1 2 3)
             (-<> 0 '(<> 1 2 3))                  =not=> '(0 1 2 3)
             )
       (fact "diamond spear pushes to last position by default"
             (-<>> ()
                   (cons 2)
                   (cons 3)
                   (cons 4)
                   reverse)                      => '(2 3 4))
       (fact "consing onto a vector treats it as a list"
             (-<>> [1 2 3] (cons 4))             => [4 1 2 3]
             (-<>  [1 2 3] (conj 4))             => [1 2 3 4]
             )
       (fact "conjing onto a list pushes the value to the front"
             (-<>> '(1 2 3) (cons 4))            => [4 1 2 3]
             (-<>  '(1 2 3) (conj 4))            => [4 1 2 3]
             (-<>  '(1 2 3) (conj 4))            => (-<>> '(1 2 3) (cons 4))
             )
       (fact "nil-shortcutting diamond wand and spear act like the maybe monad"
             (-?<> "abc"
                   (if (string? "adf") nil <>)
                   (str <> " + more"))           => nil

             (-?<> "abc"
                   (if (string? "adf") nil <>)
                   (str " + more"))              => nil

             (-?<>> "abc"
                    (if (string? "adf") nil <>)
                    (str <> " + more"))          => nil

             (-?<>> "abc"
                    (if (string? "adf") nil)
                    (str <> " + more"))          => nil
             )
       (fact "non-updating arrows nest and chain freely (they're 'nominal')"
             (-> {:foo "bar"}
                 (assoc :baz ["quux" "you"])
                 (assoc :far "boo")
                 )     => {:foo "bar" :baz ["quux" "you"] :far "boo"}
             (-> {:foo "bar"}
                 (assoc :baz ["quux" "you"])
                 (-!>    :baz second (str " got here")    pdump)
                 (-!<>   :baz second (str " got here")    pdump)
                 (-!<>   :baz second (str <> " got here") pdump)
                 (-!<>>  :baz second (str <> " got here") pdump)
                 (-!>>   :baz first  (str "got here ")    pdump)
                 (-!<>   :baz first  (str "got here " <>) pdump)
                 (-!<>>  :baz first  (str "got here ")    pdump)
                 (-!<>>  :baz first  (str "got here " <>) pdump)
                 (assoc :far "boo")
                 )     => {:foo "bar" :baz ["quux" "you"] :far "boo"}
             )
       (fact "back arrow is just ->> with arguments reversed"
             (<<-
              (let [x 'nonsense])
              (if-not x 'foo)
              (let [more 'blah] more)) => 'blah)

       (fact "furculae (branching arrows) and tegumina (nesting) arrows work together"
             (-< (+ 1 2)
                 (->> vector (repeat 3))
                 (->  (* 2)  list)
                 (list 4)
                 )             => '[([3] [3] [3]) (6) (3 4)]
             (-<:p (+ 1 2)
                 (->> vector (repeat 3))
                 (->  (* 2)  list)
                 (list 4)
                 )             => '[([3] [3] [3]) (6) (3 4)]
             (-<< (+ 1 2)
                  (list 2 1)
                  (list 5 7)
                  (list 9 4))  => '[(2 1 3) (5 7 3) (9 4 3)]
             (-<<:p (+ 1 2)
                  (list 2 1)
                  (list 5 7)
                  (list 9 4))  => '[(2 1 3) (5 7 3) (9 4 3)]
             (-<>< (+ 1 2)
                  (list 2 1 <>)
                  (list 5 <> 7)
                  (list <> 9 4))  => '[(2 1 3) (5 3 7) (3 9 4)]
             (-<><:p (+ 1 2)
                  (list 2 1 <>)
                  (list 5 <> 7)
                  (list <> 9 4))  => '((2 1 3) (5 3 7) (3 9 4))
             (-<>< (+ 1 2)
                  (list 2 1 <>)
                  (list 5 <> 7)
                  (list 9 4))  => '[(2 1 3) (5 3 7) (3 9 4)]
             (-<><:p (+ 1 2)
                  (list 2 1 <>)
                  (list 5 <> 7)
                  (list 9 4))  => '((2 1 3) (5 3 7) (3 9 4))
             (-<>>< (+ 1 2)
                  (list 2 1 <>)
                  (list 5 <> 7)
                  (list <> 9 4))  => '[(2 1 3) (5 3 7) (3 9 4)]
             (-<>><:p (+ 1 2)
                  (list 2 1 <>)
                  (list 5 <> 7)
                  (list <> 9 4))  => '((2 1 3) (5 3 7) (3 9 4))
             (-<>>< (+ 1 2)
                  (list 2 1)
                  (list 5 <> 7)
                  (list <> 9 4))  => '[(2 1 3) (5 3 7) (3 9 4)]
             (-<>><:p (+ 1 2)
                  (list 2 1)
                  (list 5 <> 7)
                  (list <> 9 4))  => '((2 1 3) (5 3 7) (3 9 4))
             )
       )
