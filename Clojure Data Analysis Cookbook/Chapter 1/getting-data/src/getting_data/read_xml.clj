
(ns getting-data.read-xml
  (:use [incanter.core]
        [clojure.xml]
        [clojure.zip :exclude [next replace remove]]))

(defn load-xml-data
  "This loads data from an XML file.

  The parameters are

  * the file name;
  * a zipper function to move to the first data element;
  * a zipper function to move to the next data element.
  "
  [xml-file first-data next-data]
  (let [data-map (fn [node]
                   [(:tag node) (first (:content node))])]
    (->>
      (parse xml-file)
      xml-zip
      first-data
      (iterate next-data)
      (take-while #(not (nil? %)))
      (map children)
      (map #(mapcat data-map %))
      (map #(apply array-map %))
      to-dataset)))

