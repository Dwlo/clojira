(ns clojira.display-utils
  "This namespace deals with utility functions for data display"
  (:require [doric.core         :as dc]
            [cheshire.core      :as cc]
            [clojure.walk       :as cw]
            [clj-time
             [local             :as tl]
             [format            :as tf]]
            [clojira.properties :as pp]))

;; Date
(def default-formatter   (tf/formatters :mysql))
(def date-time-formatter (tf/formatters :basic-date-time-no-ms))

(defn format-date [date]
  (tf/unparse default-formatter date))

(defn current-time
  ([]
   (tf/unparse date-time-formatter (tl/local-now)))
  ([formatter]
   (tf/unparse formatter (tl/local-now))))

;; Table headers
(def default_headers [:key :project :type :version {:name :status :width 15 :ellipsis true :format :name} {:name :summary :width 75 :ellipsis true} :assignee :reporter :estimated-time {:name :creation-date :format format-date}])

;; Tabular preprocessing
(defn build-tabular-model
  "Returns tabular model data display"
  [headers rows]
  (->> rows
       (map #(into {} %))
       (dc/table headers)))

(defn build-rich-tabular-model
  "Returns tabular model data having title and footer display"
  [title headers rows]
  (str "\n" title " (" (count rows) ") \n"
       (build-tabular-model headers rows)
       "\n+++ " pp/robot  " / Last update: " (current-time default-formatter) " +++"))
