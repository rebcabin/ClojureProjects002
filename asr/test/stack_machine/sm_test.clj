(ns stack-machine.sm-test
  (:use      [stack-machine.stack]
             [clojure.test       ]))

(deftest test-test-itself
  (testing "tests in the namespace 'stack machine.'"
    (is (== 1 1.0))))
