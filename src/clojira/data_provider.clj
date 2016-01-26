(ns clojira.data-provider
  "This namespace deals with REST communication functions with Jira server"
  (:require [cheshire.core  :as chc]
            [clojira
             [display-utils :as pr]
             [rest-client   :refer :all]]
            [clojure
             [string        :refer [blank?]]
             [walk          :as wlk]]))

(def rest-search-api          "/rest/api/latest/search?jql=")
(def rest-issue-api           "/rest/api/latest/issue/")
(def rest-dashboard-api       "/rest/api/latest/dashboard")
(def rest-board-api           "/rest/agile/latest/board")
(def rest-board-configure-api "/rest/agile/latest/board/{boardId}/configuration")
(def rest-status-api          "/rest/api/latest/status/")
(def rest-query-api           "/rest/api/latest/filter/")

(defn preprocess-body
  "Extracts the body part from the REST response"
  [response]
  (wlk/keywordize-keys (chc/parse-string (:body response))))

(defn get-issue
  "Returns a json issue having the given id"
  [key]
  (rest-get (str rest-issue-api key)))

(defn search
  "Lists issues matching the given JQL query"
  [query]
  (rest-get (str rest-search-api query)))

(defn list-boards
  "List all the boards"
  []
  (rest-get rest-board-api))

(defn get-board-configuration
  "Returns a board configuration"
  [board-id]
  (let [request (clojure.string/replace rest-board-configure-api "{boardId}" board-id)]
    (rest-get request)))

(defn get-status
  "Returns a status json representation"
  [id-or-name]
  (rest-get (str rest-status-api id-or-name)))

(defn get-query
  "Returns a query json representation"
  [query-id]
  (rest-get (str rest-query-api query-id)))
