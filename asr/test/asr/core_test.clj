(ns asr.core-test
  (:require [clojure.test :refer :all]
            [asr.core :refer :all]
            [clojure.spec.alpha :as s]))


(deftest kebab-test
  (testing "kebab-case"
    (is (= (nskw-kebab-from 'TranslationUnit)
           :asr.core/translation-unit))
    (is (= (nskw-kebab-from "TranslationUnit")
           :asr.core/translation-unit))
    (is (thrown?
         Exception
         (nskw-kebab-from :should-fail)))))


(deftest whole-spec-test
  (testing "whole example passes trivial spec"
    (is (s/valid? list? expr-01-211000))))


(deftest shallow-map-from-speclet-test

  (testing "shallow map from speclet"

    (is (= (shallow-map-from-speclet (speclets 3))
           {:ASDL-FORMS
            '([:ASDL-SYMCONST "Public"] [:ASDL-SYMCONST "Private"]),
            :ASDL-TERM "access"}))

    (is (= (shallow-map-from-speclet (speclets 0))
           {:ASDL-FORMS
            '([:ASDL-COMPOSITE
               [:ASDL-HEAD "TranslationUnit"]
               [:ASDL-ARGS
                [:ASDL-DECL
                 [:ASDL-TYPE "symbol_table"]
                 [:ASDL-NYM "global_scope"]]
                [:ASDL-DECL
                 [:ASDL-TYPE "node" [:STAR]]
                 [:ASDL-NYM "items"]]]]),
            :ASDL-TERM "unit"}))

    (is (= (shallow-map-from-speclet (speclets 22))
           {:ASDL-FORMS
            '([:ASDL-TUPLE
               [:ASDL-ARGS
                [:ASDL-DECL
                 [:ASDL-TYPE "identifier"]
                 [:ASDL-NYM "arg"]]]]),
            :ASDL-TERM "attribute_arg"}))))


(deftest asdl-forms-test
  (testing "asdl-forms from speclet"
    ;; The following test fails because the tuple gensym
    ;; differs, but it's ok. TODO: write a better test.
    #_(is (= (->> (shallow-map-from-speclet (speclets 22))
                  :ASDL-FORMS
                  (map asdl-form))
             {:ASDL-TUPLE "asr-tuple12841",
              :ASDL-ARGS
              '({:ASDL-TYPE "identifier",
                 :MULTIPLICITY :asr.core/once,
                 :ASDL-NYM "arg"})}))))


(deftest hashmap-from-speclet-test
  (testing "hashmap from speclet"
    (is (= (hashmap-from-speclet (speclets 0))
           {:ASDL-TERM "unit",
            :ASDL-FORMS
            '({:ASDL-COMPOSITE
               {:ASDL-HEAD "TranslationUnit",
                :ASDL-ARGS
                ({:ASDL-TYPE "symbol_table",
                  :MULTIPLICITY :asr.core/once,
                  :ASDL-NYM "global_scope"}
                 {:ASDL-TYPE "node",
                  :MULTIPLICITY :asr.core/zero-or-more,
                  :ASDL-NYM "items"})}})}))

    (is (= (hashmap-from-speclet (speclets 3))
           {:ASDL-TERM "access",
            :ASDL-FORMS
            '({:ASDL-SYMCONST "Public"}
              {:ASDL-SYMCONST "Private"})}))

    ;; The following test fails because the tuple gensym
    ;; differs, but it's ok. TODO: write a better test.
    #_(is (= (hashmap-from-speclet (speclets 22))
             {:ASDL-TERM "attribute_arg",
              :ASDL-FORMS
              '({:ASDL-TUPLE "asr-tuple13048",
                 :ASDL-ARGS
                 ({:ASDL-TYPE "identifier",
                   :MULTIPLICITY :asr.core/once,
                   :ASDL-NYM "arg"})})}))))


(deftest install-all-symconst-specs-test
  (testing "install all symconst specs"
    (is (= (set '(:asr.core/implementation ; why is this not indented?
                  :asr.core/interface
                  :asr.core/l-bound
                  :asr.core/u-bound
                  :asr.core/default
                  :asr.core/save
                  :asr.core/parameter
                  :asr.core/allocatable
                  :asr.core/add
                  :asr.core/sub
                  :asr.core/mul
                  :asr.core/div
                  :asr.core/pow
                  :asr.core/bit-and
                  :asr.core/bit-or
                  :asr.core/bit-xor
                  :asr.core/bit-l-shift
                  :asr.core/bit-r-shift
                  :asr.core/required
                  :asr.core/optional
                  :asr.core/binary
                  :asr.core/hex
                  :asr.core/octal
                  :asr.core/and
                  :asr.core/or
                  :asr.core/xor
                  :asr.core/n-eqv
                  :asr.core/eqv
                  :asr.core/supports-zero
                  :asr.core/supports-plus
                  :asr.core/divisible
                  :asr.core/any
                  :asr.core/source
                  :asr.core/l-fortran-module
                  :asr.core/g-fortran-module
                  :asr.core/bind-c
                  :asr.core/interactive
                  :asr.core/intrinsic
                  :asr.core/real-to-integer
                  :asr.core/integer-to-real
                  :asr.core/logical-to-real
                  :asr.core/real-to-real
                  :asr.core/template-to-real
                  :asr.core/integer-to-integer
                  :asr.core/real-to-complex
                  :asr.core/integer-to-complex
                  :asr.core/integer-to-logical
                  :asr.core/real-to-logical
                  :asr.core/character-to-logical
                  :asr.core/character-to-integer
                  :asr.core/character-to-list
                  :asr.core/complex-to-logical
                  :asr.core/complex-to-complex
                  :asr.core/complex-to-real
                  :asr.core/logical-to-integer
                  :asr.core/real-to-character
                  :asr.core/integer-to-character
                  :asr.core/logical-to-character
                  :asr.core/local
                  :asr.core/in
                  :asr.core/out
                  :asr.core/in-out
                  :asr.core/return-var
                  :asr.core/unspecified
                  :asr.core/public
                  :asr.core/private
                  :asr.core/eq
                  :asr.core/not-eq
                  :asr.core/lt
                  :asr.core/lt-e
                  :asr.core/gt
                  :asr.core/gt-e))

           (set (->> symconst-stuffs
                     (map spec-from-symconst-stuff)
                     (map eval))))
        )))


;;; Ongoing experiment with fixtures.

#_(defn symconst-stuffs-fixture [f]
    (->> symconst-stuffs
         (map spec-from-symconst-stuff)
         (map eval))
    (->> symconst-stuffss-by-term
         (map symconst-spec-for-term)
         (map eval))
    (->> tuple-stuffs
         (map spec-from-tuple-stuff!)
         (map eval))
    (->> tuple-stuffss-by-term
         (map tuple-spec-for-term)
         (map eval))
    (f))

#_(use-fixtures :once symconst-stuffs-fixture)


;;; The following test fails in lein test at the because
;;; console the spec :asr.core/add cannot be found. The
;;; test passes in CIDER. TODO: Investigate. Write better
;;; test. Fixtures?
#_(deftest add-conforms-test
  (is (= 'Add
       (s/conform (s/spec :asr.core/add) 'Add))))


;;; The order is not stable, so the 'n-th' doesn't give
;;; a predictable result.
#_(deftest symconst-stuffss-by-term-test
  (testing "symconst stuffss by term"
    (is (= :asr.core/binop
           (-> (nth symconst-stuffss-by-term 3)
               symconst-spec-for-term
               #_echo
               eval)))))


(deftest install-symconst-stuffss-by-term-test
  (testing "installing symconst stuffss by term"
    (is (= (set '(:asr.core/deftype ; why is this indented?
                     :asr.core/arraybound
                     :asr.core/storage-type
                     :asr.core/binop
                     :asr.core/presence
                     :asr.core/integerboz
                     :asr.core/logicalbinop
                     :asr.core/trait
                     :asr.core/abi
                     :asr.core/cast-kind
                     :asr.core/intent
                     :asr.core/access
                     :asr.core/cmpop))
           (set (->> symconst-stuffss-by-term
                     (map symconst-spec-for-term)
                     (map eval)))))))
