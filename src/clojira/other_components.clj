(ns clojira.other-components
  "This namespace deals with Jira components")

;; Status record
(defrecord Status [id name description])

(def select-values
  (juxt :id :name :description))

(defn parse-status
  "Creates a status record from a json status"
  [json-rep]
  (->> json-rep
       (select-values)
       (apply ->Status)))

;; Query record
(defrecord Query [id name description query])

(def select-query-values
  (juxt :id :name :description :jql))

(defn parse-query
  "Creates a query record from a json query"
  [json-query]
  (->> json-query
       (select-query-values)
       (apply ->Query)))
