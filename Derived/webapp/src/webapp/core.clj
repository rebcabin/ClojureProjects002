(ns webapp.core
  (:use
    [compojure.core  :only (GET HEAD defroutes)]
    [compojure.route :only (resources)]
    ))

(defroutes routes
  (resources "/")
  (HEAD "/" [] "")
  (GET "*" 
       {:keys [uri]}
       (format "<html>
                URL requested: %s
                <p>
                  <a href=\"/wright_pond.jpg\">
                    Image served by compojure.route/resources
                  </a>
                </p>
                </html>"
               uri)
       ))