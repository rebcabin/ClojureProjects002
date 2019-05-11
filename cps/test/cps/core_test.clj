(ns cps.core-test
  (:require [clojure.test :refer :all]
            [cps.core :refer :all]
            [clojure.algo.monads :refer :all]))

(deftest pyth-test
  (testing "pyth-cps"
    (is (= 61 (pyth-cps 5 6 identity)))))

(deftest fib-test
  (testing "fib-cps"
    (is (= 55 (fib-cps 10 identity)))
    (is (= 610 (fib-cps 15 identity)))
    (is (thrown? StackOverflowError (fib-cps 20 identity)))
    ))

(deftest mk-cps-test
  (is (= (facr 10000N) (fac0 10000N)))
  (is (= 50005000N (tri0 10000N))))

(deftest trampoline-test
  (is (= 0 (trampoline funa 42)))
  (is (= 0 (trampoline funa 100000)))
  (is (= 5 (+-cps 2 3 identity)))
  (is (= 5 (trampoline #(+-cps 2 3 identity))))
  (is (= 5 (trampoline #(+-cps 2 3 %) identity)))
  (is (= 5 (trampoline #(+-cps 2 3 (fn [a] (identity a))))))
  (is (= 5 (trampoline #(+-cps 2 3 (fn [a] (trampoline identity a))))))

  (is (= 0 (func 100)))
  (is (thrown? StackOverflowError (func 10000)))
  (is (= 0 (trampoline fund 10000)))

  (is (= 0 (fune 100 identity)))
  (is (thrown? StackOverflowError (fune 10000 identity)))

  (is (= 0 (trampoline funf 100 identity)))
  (is (= 0 (trampoline funf 10000 identity)))

  (is (= 720N (faca 6N identity)))
  (is (thrown? StackOverflowError (faca 10000N identity)))

  (is (= 720N (trampoline facb 6N 1N identity)))
  (is (= (facr 10000N) (trampoline facb 10000N 1N identity)))

  (is (= 720N (trampoline facc 6N identity)))
  (is (thrown? StackOverflowError (trampoline facc 10000N identity)))
  )

(deftest continuation-monad-test
  (is (= 85 (fn6 42 identity)))
  (is (= 21 (with-monad cont-m ((m-result 21) identity))))
  (is (= 85 (with-monad cont-m ((m-bind (m-result 42) (m-chain [mf-a mf-b mf-c])) identity))))
  )
