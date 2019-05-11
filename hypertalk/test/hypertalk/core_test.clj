(ns hypertalk.core-test
  (:require [clojure.spec      :as s]
            [clojure.test      :refer :all]
            [hypertalk.core    :refer :all]
            ))

(deftest put-command-test
  (testing "parsing a put command"
    (is (= [:COMMAND
            [:PUT
             [:EXPR [:INUM "42"]]
             [:CONTAINER [:SIMPLE_CONTAINER [:VARIABLE "foobar"]]]]]
           (hypertalk-parser "put 42 into foobar")))))

(deftest get-command-test
  (testing "parsing a get command"
    (is (= [:COMMAND
            [:GET
             [:EXPR [:CONTAINER [:SIMPLE_CONTAINER [:VARIABLE "foobar"]]]]]]
           (hypertalk-parser "get foobar")))))

(deftest compilation-of-put-command
  (testing "compilation of a put command"
    (is (= 72 (interpret-hypertalk-string "put 72 into foobar")))
    (is (= 42 (interpret-hypertalk-string "put 42 into foobar")))
    (is (= 43 (interpret-hypertalk-string "put 43 into foobar")))
    (is (= 43 @(@symtab "foobar")))))

(deftest compilation-of-get-command
  (testing "compilation of a get command"
    (is (= 43 (interpret-hypertalk-string "get foobar"))
        (=  0 (interpret-hypertalk-string "get bazrat")))))

(deftest macroized-lispy-hypertalk
  (testing "beautiful macroized stuff"
    (is (= 1729 (interpret-hypertalk-ast
                 (put 1729 into ramanujan))))
    (is (= 1729 (interpret-hypertalk-ast
                 (get ramanujan))))))
