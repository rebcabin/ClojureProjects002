(ns command-line-args.core-test
  (:require clojure.algo.monads)
  (:use clojure.test
        clojure.pprint
        clojure.java.io
        command-line-args.core))

(deftest pair-of-values
  (let [args ["--server"      "localhost"
              "--port"        "8080"
              "--environment" "production"]]
    (is (= {:server "localhost"
            :port "8080"
            :environment "production"}
           (parse-args args)))))

(deftest a-test
  (testing "The test test."
    (is (= 1 1))))

(deftest b-test
  (testing "keywordize [\"a\"] = [:a nil] -- missing value defaults to nil."
    (is (= (keywordize ["a"])         [:a nil]))))

(deftest c-001-test
  (testing "(keywordize [\"a\" 1 \"b\" 2]) -- extra key-value pairs ignored."
    (is (= (keywordize ["a" 1 "b" 2]) [:a 1]))))

(deftest c-002-test
  (testing "(keywordize [\"a\" 3 2]) -- extra values ignored."
    (is (= (keywordize ["a" 3 2])     [:a 3]))))

;; UDIAGNOSED PROBLEM: If I uncomment the following and let it fail,
;; then the nrepl test framework seems to get confused. It continues
;; to report failures, one more for each iteration of the test via C-c
;; C-,. Quitting and restarting the nrepl fixes it -- C-x C-b, switch
;; to nrepl buffer, d on every line beginning with "*nrepl", x, y, y,
;; y, M-x nrepl-jack-in -- but this is really draconian. I do not know
;; if this is a bug with nrepl or with my tests. If I can prove it's a
;; bug in my tests, then "fixtures" might be a good way to fix it.
;; TODO. -- More evidence: the nrepl testing infrastructure tends to
;; go haywire frequently. I haven't decided whether to try upgrading
;; it or just to forget it since leiningen works well.

(deftest d-test
  (testing "dynamic binding and let shadowing"
    (do (f4)
        (is (= v 4)))))

(deftest e-001-test
  (testing "count on vectors"
    (is (count [19 "yellow" true]) 3)))

(deftest e-002-test
  (testing "reverse a vector"
    (is (= (reverse [2 4 7]) [7 4 2]))))

(deftest e-003-test
  (testing "map over multiple collections of differing lengths"
    (is (= (map +
                [2 4 7]
                [5 6]
                [1 2 3 4])
           [8 12]))))

(deftest f-001-test
  (is (= 1 1))
  (is (= 2 2)))

(defn parting
  "returns a String containing a parting salutation, an anti-greeting
  if you will, and I am making this documentation extra long so that I
  can test C-c M-q for formatting the documentation."
  ; we don't have an attr-map
  [name] ; these are the params
  ; we don't have a prepost-map
  (str "Goodbye, " name)
  )

(deftest f-test
  (testing "test the parting function"
    (is (= (parting "Mark")
           "Goodbye, Mark"))))

(deftest g-test
  (testing "testing multiary power function"
    (is (= (power 2 3 4)
           4096.0)))) ; definitely NOT 4096 the integer


(deftest purity-test-001
  (testing "Testing purity of the book's \"halve!\" function if  \"plays\" is immutable."
    (is (= (map :plays plays) '(979 2333, 979, 2665)))
    (is (= (map :plays (halve! [:plays])) '(489 1166 489 1332)))
    (is (= (map :plays plays) '(979 2333, 979, 2665))))
  )

(deftest optional-and-named-arguments-test-001
  (testing "Optional and named arguments."
    (is (= -6.0 (slope-optional :p1 [4 15] :p2 [3 21])))
    (is (= 0.5 (slope-optional :p2 [2 1])))
    (is (= 1.0 (slope-optional)))
    ))

(deftest preconditions-and-postconditions-001
  (testing "Preconditions and postconditions in the slope function."
    (is (= -6.0 (slope :p1 [4 15] :p2 [3 21])))
    (is (= -6.0 (slope :p2 [3 21] :p1 [4 15])))
    (is (= 1.0 (slope)))
    (is (= 0.0 (slope :p1 [0 0] :p2 [1234 0])))
    (is (thrown? AssertionError (slope :p1 [0 0] :p2 [0 1234])))
    (is (thrown? AssertionError (slope :p1 [0 0 0] :p2 [1234 4567])))
    (is (thrown? AssertionError (slope :p1 {0 0} :p2 [1234 4567])))
    ))

(deftest vectors-are-functions-of-their-indices-001
  (testing "Vectors are functions of their indices."
    (-> 1.0 (= ([1.0] 0)) is)
    (is (thrown? java.lang.IndexOutOfBoundsException ([1.0] 1)))
    (is (thrown? java.lang.IndexOutOfBoundsException ([] 0)))
    (is (thrown? java.lang.IndexOutOfBoundsException ([] -1)))
    (is (thrown? java.lang.IndexOutOfBoundsException ([1.0] -1)))
    ))

(deftest maps-are-functions-of-their-keys-001
  (testing "Maps are functions of their keys."
    (->   1 (= ({:a 1}       :a)) is)
    (->   2 (= ({:a 1, :b 2} :b)) is)
    (-> nil (= ({:a 1, :b 2} :c)) is)
    (-> 'df (= ({} :a 'df      )) is)
    ))

(deftest keys-are-functions-that-look-up-values-in-maps-001
  (testing "Keys are functions that look up values in maps."
    (->   1 (= (:a {:a 1}))           is)
    (->   1 (= (:a {:a 1, :b 2}    )) is)
    (-> nil (= (:c {:a 1, :b 2}    )) is)
    (-> nil (= (:c {}              )) is)
    (-> 'df (= (:c {:a 1, :b 2} 'df)) is)
    (-> 'df (= (:c {}           'df)) is)
    ))

(deftest sets-are-functions-that-test-membership
  (testing "Sets are functions that test membership."
    (is (= \a ((set "aeiou") \a)))
    (is (= nil ((set "aeiou")\b)))
))

(deftest clojure-java-io-test-001
  (clojure.pprint/pprint (. (java.io.File. ".") getCanonicalPath))
  (testing "Clojure java io."
    (is (= ["\"a\"", "'b", ":c", "\\d"]
           (with-open [rdr (reader "./test/command_line_args/data/foo.txt")]
             (vec (line-seq rdr))))
        )))
;;; via
;;; http://www.learningclojure.com/2009/09/nested-def-me-name-firstname-john.html

(def he
  {:name
   {:firstname "John"
    :middlename "Lawrence"
    :surname "Aspden"}
   :address 
   {:street "Catherine Street"
    :town {:name "Cambridge"
           :county "Cambridgeshire"
           :country{
                    :name "England"
                    :history "Faded Imperial Power"
                    :role-in-world "Beating Australia at Cricket"}}}})

(deftest nested-access-test-001
  (testing "Various nested access methods (via John Aspden)."
    (is (= (:name he) (get he :name)))
    (is (= (get-in he [:name :middlename]) (-> he :name :middlename)))
    (is (= (get-in he [:name :middlename]) (reduce get he [:name :middlename])))
    (is (= (let [ks [:address :street]]
             (get-in
              (update-in he ks #(str "33 " %))
              ks)
             ) "33 Catherine Street"))
    (is (= (let [ks [:name :initials]]
             (get-in
              (assoc-in he ks "JLA")
              ks))
           "JLA"))
    ))

(deftest writer-monad-test
  (testing "The writer monad (via onclojure.com)."
    (is (= (fib-trace 5)
           [5 [[1 1] [0 0] [2 1] [1 1] [3 2] [1 1] [0 0] [2 1] [4 3] [1 1] [0 0] [2 1] [1 1] [3 2]]]))
    ))

(deftest identity-monad-test
  (testing "The identity monad (via onclojure.com)."
    (is (= (fib-ident 5) 5))))

(deftest maybe-monad-test
  (testing "The maybe monad."
    (is (= nil (fib-maybe -100)))))

(deftest monad-transformer-test
  (testing "Monad transformers with maybe and sequence (via onclojure.com)."
    (let [msq (clojure.algo.monads/maybe-t clojure.algo.monads/sequence-m)]
      (is (= (vec (clojure.algo.monads/domonad
                   msq
                   [x [1 2 nil 4]
                    y [10 nil 30 40]]
                   (+ x y)
                   ))
             [11 nil 31 41, 12 nil 32 42, nil, 14 nil 34 44]
             )))
    ))