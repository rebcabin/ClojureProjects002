(use 'ring.adapter.jetty)
(require '[reactive-backplane :as web])
(run-jetty #'web/app {:port 8080})

