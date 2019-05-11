(ns tunnel.core-test
  (:use clojure.test
        ring.mock.request
        tunnel.core))

(defn my-req [n] (str "{\"x\": \"" n "\"}"))

(defn test-echo [n]
  (let [req (my-req n)
        res (app (request :post "/" req))]
    (is (= (:status res)) 200)
    (is (= (:body   res)) req)))

(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) "Hello World"))))

  (testing "main post"
    (let [res (app (request :post "/" "{\"x\":\"42\"}"))]
      (is (= (:stauts res 200)))
      (is (= (:body res) "{\"x\":\"42\"}")))
    (doall (map test-echo (range 43 50))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= (:status response) 404)))))
