(ns blade-acousto.core-test
  (:require [clojure.test        :refer :all]
            [clatrix.core        :as clx    ]
            [clojure.core.matrix :as cmx    ]
            [blade-acousto.core  :refer :all]))

(deftest a-few-clatrix-tests
  (testing "A few clatrix tests")
  (is (let [m (clx/matrix (repeat 3 (range 4)))]
        (println m)
        (println (clx/solve (clx/rand 4 4) (clx/rand 4)))
        (let [A (clx/rand 10 14)
              B (clx/* A (clx/t A))          ; B is symmetric
              lu (clx/lu B)
              P (clx/rspectral 10) ; `respectral` makes positive definite matrices
              G (clx/cholesky P)]  ; so we can get their square root
          (and (= A (clx/t (clx/t A)))
               (= B (clx/* (:p lu) (:l lu) (:u lu)))
               (= P (clx/* (clx/t G) G))))))   )

(deftest a-few-matrix-tests-more
  (testing "A few matrix tests more")

;;                   _    _           _                  _
;;  _ __  ___ _ _ __(_)__| |_ ___ _ _| |_ _____ _____ __| |_ ___ _ _
;; | '_ \/ -_) '_(_-< (_-<  _/ -_) ' \  _|___\ V / -_) _|  _/ _ \ '_|
;; | .__/\___|_| /__/_/__/\__\___|_||_\__|    \_/\___\__|\__\___/_|
;; |_|
;;  _            _                   _        _   _
;; (_)_ __  _ __| |___ _ __  ___ _ _| |_ __ _| |_(_)___ _ _
;; | | '  \| '_ \ / -_) '  \/ -_) ' \  _/ _` |  _| / _ \ ' \
;; |_|_|_|_| .__/_\___|_|_|_\___|_||_\__\__,_|\__|_\___/_||_|
;;         |_|

  
  ;; With persistent-vector as the implementation, we can use 
  ;; clojure's =, sometimes clojure's ==, or always cmx's equal

  (cmx/set-current-implementation :persistent-vector)

  ;; Matrix mul returns integers for integer inputs:

  (is (= 14 (cmx/mmul (cmx/matrix [1 2 3])
                      (cmx/matrix [1 2 3]))))

  ;; and these are not = to doubles :
  
  (is (not= 14.0 (cmx/mmul (cmx/matrix [1 2 3])
                           (cmx/matrix [1 2 3]))))

  ;; but we still have contagion of doubles :
  
  (is (not= 14 (cmx/mmul (cmx/matrix [1.0 2 3])
                         (cmx/matrix [1   2 3]))))

  (is (= 14.0 (cmx/mmul (cmx/matrix [1.0 2 3])
                        (cmx/matrix [1   2 3]))))

  ;; We haven't overridden = :
  
  (is (= clojure.core$_EQ_ (type =)))
  
  ;; and Matrix mul returns a java.lang.Long :
  
  (is (= java.lang.Long
         (type (cmx/mmul (cmx/matrix [1 2 3])
                         (cmx/matrix [1 2 3])))))

  ;; We can sometimes use the fact that integers == doubles, at least
  ;; when scalars are the results :

  (is (== 14 14.0))

  (is (== 14 (cmx/mmul (cmx/matrix [1 2 3])
                       (cmx/matrix [1 2 3]))))

  ;; We can go back to = when we must compare non-scalars, but now
  ;; we must compare against doubles, even when all inputs are
  ;; integers:

  (is (= [[14.0]] (cmx/mmul (cmx/matrix [[1 2 3]])
                            (cmx/matrix [[1] [2] [3]]))))

  (is (not= [[14]] (cmx/mmul (cmx/matrix [[1 2 3]])
                             (cmx/matrix [[1] [2] [3]]))))

  ;; Now, == doesn't work at all :
  
  (is (thrown? ClassCastException
               (== [[14.0]] (cmx/mmul (cmx/matrix [[1 2 3]])
                                      (cmx/matrix [[1] [2] [3]])))))

  ;; We're ok with cmx/equals; it's always safe :

  (is (cmx/equals [[14.0]]
                  (cmx/mmul (cmx/matrix [[1 2 3]])
                            (cmx/matrix [[1] [2] [3]]))))

  (is (cmx/equals [[14]]
                  (cmx/mmul (cmx/matrix [[1 2 3]])
                            (cmx/matrix [[1] [2] [3]]))))

  (is (cmx/equals 14.0
                  (cmx/mmul (cmx/matrix [1 2 3])
                            (cmx/matrix [1 2 3]))))

  (is (cmx/equals 14
                  (cmx/mmul (cmx/matrix [1 2 3])
                            (cmx/matrix [1 2 3]))))

;;             _
;; __ _____ __| |_ ___ _ _ ___
;; \ V / -_) _|  _/ _ \ '_|_ /
;;  \_/\___\__|\__\___/_| /__|
;;  _            _                   _        _   _
;; (_)_ __  _ __| |___ _ __  ___ _ _| |_ __ _| |_(_)___ _ _
;; | | '  \| '_ \ / -_) '  \/ -_) ' \  _/ _` |  _| / _ \ ' \
;; |_|_|_|_| .__/_\___|_|_|_\___|_||_\__\__,_|\__|_\___/_||_|
;;         |_|

  ;; With the vectorz implementation, we cannot use clojure's =

  (cmx/set-current-implementation :vectorz)

  (is (not= 14 (cmx/mmul (cmx/matrix [1 2 3])
                         (cmx/matrix [1 2 3]))))
  
  ;; Because its types are weird; it's a mikera.vectorz.Scalar :

  (is (= mikera.vectorz.Scalar
         (type (cmx/mmul (cmx/matrix [1 2 3])
                         (cmx/matrix [1 2 3])))))

  ;; but his scalar test doesn't say so :

  (is (not (cmx/scalar? (cmx/mmul (cmx/matrix [1 2 3])
                                  (cmx/matrix [1 2 3])))))

  (is (not= [[14.0]] (cmx/mmul (cmx/matrix [[1 2 3]])
                               (cmx/matrix [[1] [2] [3]]))))

  ;; In any event, we're stil ok with cmx/equals; it's ALWAYS safe,
  ;; at least so far as I've tested

  (is (cmx/equals [[14.0]]
                  (cmx/mmul (cmx/matrix [[1 2 3]])
                            (cmx/matrix [[1] [2] [3]]))))

  (is (cmx/equals [[14]]
                  (cmx/mmul (cmx/matrix [[1 2 3]])
                            (cmx/matrix [[1] [2] [3]]))))

  (is (cmx/equals 14.0
                  (cmx/mmul (cmx/matrix [1 2 3])
                            (cmx/matrix [1 2 3]))))

  (is (cmx/equals 14
                  (cmx/mmul (cmx/matrix [1 2 3])
                            (cmx/matrix [1 2 3]))))

  )
