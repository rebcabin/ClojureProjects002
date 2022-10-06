(ns asr-ast.core-test
  (:require [clojure.test :refer :all]
            [asr-ast.core :refer :all]))

;;; hand-written test vectors

(def asr-frag
  '(IntegerBinOp
    (IntegerConstant 2 (Integer 4 []))   ; expr  left
    Add                                  ; binop op
    (IntegerConstant 3 (Integer 4 []))   ; expr  right
    (Integer 4 [])                       ; ttype type
    (IntegerConstant 5 (Integer 4 []))))

(def ast-frag
  (with-meta
    '(BinOp
      (ConstantInt 2, "i32"),
      Add,
      (ConstantInt 3, "i32"))
    {:result-ttype "i32",
     :value '(ConstantInt 5 "i32")}))

;;; round-tripping

(def bigger-asr-frag
  '[IntegerBinOp
    [IntegerConstant 2 (Integer 4 [])]
    Add
    [IntegerBinOp ; recursive right
     [IntegerConstant 3 (Integer 4 [])]
     Add
     [IntegerConstant 5 (Integer 4 [])]
     (Integer 4 [])]
    (Integer 4 [])
    [IntegerBinOp
     [IntegerConstant 3 (Integer 4 [])]
     Mul
     [IntegerConstant 4 (Integer 4 [])]
     (Integer 4 [])
     [IntegerConstant 12 (Integer 4 [])]]])

(deftest asr<->ast-round-tripping
  (testing "asr<->ast round tripping"
    (is (= asr-frag
           (-> asr-frag
               asr-s-exp
               ast-s-exp)))
    (is (= bigger-asr-frag
           (-> bigger-asr-frag
               asr-s-exp
               ast-s-exp)))))

(deftest asr->ast-one-direction
  (testing "asr->ast one direction"
    (is (= ast-frag (-> asr-frag asr-s-exp)))
    ))
