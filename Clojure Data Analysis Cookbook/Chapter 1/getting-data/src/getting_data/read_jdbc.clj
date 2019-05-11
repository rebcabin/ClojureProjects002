
(ns getting-data.read-jdbc
  (:use [clojure.java.jdbc :exclude [resultset-seq]]
        [incanter.core]))

(defn load-table-data
  "This loads the data from a database table."
  [db table-name]
  (with-connection db
    (with-query-results rs [(str "SELECT * FROM " table-name ";")]
      (to-dataset (doall rs)))))

