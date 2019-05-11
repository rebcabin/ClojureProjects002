(ns ex2.core
  (:use [clojure.data.zip.xml :only (attr text xml->)]
        [dk.ative.docjure.spreadsheet] )
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip]))
(def xml (xml/parse "myfile.xml"))
(def zippered (zip/xml-zip xml))
(let [wb (create-workbook "Price List"
                          [["Name"       "Price"]
                           ["Foo Widget" 100]
                           ["Bar Widget" 200]])
      sheet (select-sheet "Price List" wb)
      header-row (first (row-seq sheet))]
  (do
    (set-row-style!
      header-row
      (create-cell-style! wb
        {:background :yellow,
         :font       {:bold true}}))
    (save-workbook! "spreadsheet.xlsx" wb)))
