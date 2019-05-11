
(ns getting-data.read-html-table
  (:require [clojure.string :as string]
            [net.cgrand.enlive-html :as html])
  (:use [incanter.core])
  (:import [java.net URL]))

(defn to-keyword
  "This takes a string and returns a normalized keyword."
  [input]
  (-> input
    string/lower-case
    (string/replace \space \-)
    keyword))

(defn load-data
  "This loads the data from a table at a URL."
  [url]
  (let [html (html/html-resource (URL. url))
        table (html/select html [:table#data])
        headers (->>
                  (html/select table [:tr :th])
                  (map html/text)
                  (map to-keyword)
                  vec)
        rows (->> (html/select table [:tr])
               (map #(html/select % [:td]))
               (map #(map html/text %))
               (filter seq))]
    (dataset headers rows)))

