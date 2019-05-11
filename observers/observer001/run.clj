(use 'ring.adapter.jetty)
(require '[observer001 :as web])
(run-jetty #'web/app {:port 8080})

