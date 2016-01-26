(ns clojira.rest-client
  "This namespace deals with REST configuration and requests"
  (:require [cheshire.core   :refer [generate-string]]
            [clj-http.client :as    rest-client]
            [clojira
             [file-util      :as    utils]
             [properties     :as    pp]]))

(def auth-type "?os_authType=basic")

(defn print-resp
  "Prints the json response"
  [response]
  (println (generate-string response {:pretty true})))

(defn get-body
  "Returns a decoded json body from a given json response"
  [response]
  (rest-client/json-decode (:body response)))

(defn print-resp-body
  "Pretty prints the HTTP response body."
  [response]
  (print-resp (get-body response)))

(defn rest-get
  "GET request on the Jira REST api."
  [rest-resource]
  (rest-client/get (str pp/jira-server-url rest-resource)
                   {:basic-auth [pp/jira-user-name pp/jira-user-passwd]
                    :accept :json}))

(defn rest-put
  "PUT request on the Jira REST api."
  [rest-resource payload]
  (rest-client/put (str pp/jira-server-url rest-resource)
                   {:body (rest-client/json-encode payload)
                    :debug? true
                    :debug-body true
                    :basic-auth [pp/jira-user-name pp/jira-user-passwd]
                    :accept :json
                    :content-type :json}))
