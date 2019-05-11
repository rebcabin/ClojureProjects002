(deftest get-data-helper-test
  (testing "get-data-helper"
    (is (= 1 1))))

(deftest get-data-helper-test
  (testing "merge-with"
    (is (= (merge-with + {:x 1 :y 2} {:x 3 :y 4}) {:x 4 :y 6})))
  (testing "get-data-helper"
    (is (= 1 1))))
