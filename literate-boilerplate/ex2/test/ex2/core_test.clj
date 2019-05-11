(ns ex2.core-test
  (:use [clojure.data.zip.xml :only (attr text xml->)]
        [dk.ative.docjure.spreadsheet]
  )
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.test :refer :all]
            [ex2.core :refer :all]))
(deftest xml-zipper-test
  (testing "xml and zip on a trivial file."
    (are [a b] (= a b)
      (xml-> zippered :track :name text) '("Track one" "Track two")
      (xml-> zippered :track (attr :id)) '("t1" "t2"))))
(deftest docjure-test
  (testing "docjure read"
    (is (=

      (->> (load-workbook "spreadsheet.xlsx")
           (select-sheet "Price List")
           (select-columns {:A :name, :B :price}))

      [{:name "Name"      , :price "Price"}, ; don't forget header row
       {:name "Foo Widget", :price 100.0  },
       {:name "Bar Widget", :price 200.0  }]

      ))))
