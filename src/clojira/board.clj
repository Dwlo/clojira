(ns clojira.board
  "This namespace deals with board functions"
  (:require [clojure.walk             :as wlk]
            [cheshire.core            :as chc]
            [clojira.other-components :as oc ]
            [clj-time.format          :as ctf]))

;; Columns
(defrecord Column [name statuses])

(defn get-column-statuses
  "Returns a list of status ids"
  [json-column]
  (->> json-column
       :statuses
       (map :id)))

(def select-column-values
  (juxt :name
        get-column-statuses))

(defn parse-column
  "Creates a column record from jira json like representation"
  [json-column]
  (->> json-column
       (select-column-values)
       (apply ->Column)))

;; Board
(defrecord Board  [id name query-id columns])

(defn get-board-columns
  "Returns boards columns definition"
  [json-board]
  (->> json-board
      :columnConfig
      :columns
      (map parse-column)))

(def select-board-values
  (juxt :id
        :name
        (comp :id :filter)
        get-board-columns))

(defn parse-board
  "Creates a board record from jira json like representation"
  [json-rep]
  (->> json-rep
       (select-board-values)
       (apply ->Board)))
