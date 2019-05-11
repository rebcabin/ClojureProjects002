(ns observer001.test.handler
  (:use clojure.test
        ring.mock.request
        [clj-json.core :as json]
        observer001.handler))

(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/"))]
      (is (= (:status response) 200))
      (is (= (json/parse-string (:body response)) {"hello" "json-get"}))))
  
  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= (:status response) 404)))))