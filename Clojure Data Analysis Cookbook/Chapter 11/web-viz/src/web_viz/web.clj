
(ns web-viz.web
  (:require [compojure.route :as route]
            [compojure.handler :as handler])
  (:use compojure.core
        ring.adapter.jetty
        [ring.middleware.content-type :only (wrap-content-type)]
        [ring.middleware.file :only (wrap-file)]
        [ring.middleware.file-info :only (wrap-file-info)]
        [ring.middleware.stacktrace :only (wrap-stacktrace)]
        [ring.util.response :only (redirect)]
        [hiccup core element page]
        [hiccup.middleware :only (wrap-base-url)]
        web-viz.core))

(defn chart-li [title url]
  [:li [:a {:href url} title]
   " (" [:a {:href (str url "/data.json")} "data"] ")"])

(defn index-page []
  (html5
    [:head
     [:title "Web Charts"]]
    [:body
     [:h1 "Web Charts"]
     [:ol
      #_ [:li [:a {:href "/ibm.json"} "IBM Data"]]
      [:li [:a {:href "/data/census-race.json"} "2010 Census Race Data"]]
      (chart-li "Scatter Chart" "/scatter")
      #_ (chart-li "Bar Chart" "/barchart")
      #_ (chart-li "Line Plot" "/lineplot")
      (chart-li "Histogram" "/histogram")
      #_ (chart-li "Box Plot" "/boxplot")
      #_ (chart-li "Scatter Line" "/scatterline")
      #_ (chart-li "Force-Directed Layout" "/force")
      #_ (chart-li "Interactive Force" "/int-force")]
     #_ (include-js "js/script.js")
     #_ (javascript-tag
       "webviz.core.hello('from ClojureScript!');")]))

(defroutes
  site-routes
  (GET "/" [] (index-page))
  (GET "/ibm.json" [] (ibm-json))

  (GET "/scatter" [] (scatter-charts))
  (GET "/scatter/data.json" []
       (redirect "/data/census-race.json"))

  (GET "/barchart" [] (bar-chart))
  (GET "/barchart/data.json" []
       (redirect "/data/chick-weight.json"))

  (GET "/lineplot" [] (line-plot))
  (GET "/lineplot/data.json" [] (ibm-json))

  (GET "/histogram" [] (hist-plot))
  (GET "/histogram/data.json" []
       (redirect "/data/abalone.json"))

  (GET "/boxplot" [] (box-plot))
  (GET "/boxplot/data.json" [] (ibm-json))

  (GET "/scatterline" [] (scatter-line-plot))
  (GET "/scatterline/data.json" [] (scatter-data))

  (GET "/force" [] (force-layout-plot))
  (GET "/force/data.json" []
       (redirect "/data/clusters.json"))

  (GET "/int-force" [] (interactive-force-plot))
  (GET "/int-force/data.json" []
       (redirect "/data/clusters.json"))

  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (-> (handler/site site-routes)
    (wrap-base-url)
    (wrap-file "resources")
    (wrap-file-info)
    (wrap-content-type)))

(defn -main
  []
  (run-jetty app {:port 3000}))

