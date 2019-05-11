(ns repro.core-test
  (:require [repro.core      :refer :all]
            [midje.sweet     :refer :all]))

(facts "about numbers"
       (fact "trivial"
         1 => 1) )

