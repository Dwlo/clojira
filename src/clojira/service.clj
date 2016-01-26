(ns clojira.service
  "This namespace deals with all high functions to manipulate Jira content"
  (:require [clojira
             [board            :as bd]
             [cron             :as cr]
             [data-provider    :as comm]
             [display-utils    :as pr]
             [email-utils      :as mlu]
             [emailer          :as ml]
             [issue            :as tr]
             [other-components :as oc]
             [screen           :as scr]]
            [clojure.walk      :as wlk]))

;; Utils
(defn aggregate-by
  "Aggregates a data collection using an aggregator"
  [aggregator col]
  (->> col
       (group-by aggregator)
       (vals)))

;; Status
(defn get-status
  "Returns a status based on its id or name"
  [id-or-name]
  (->> id-or-name
       (comm/get-status)
       (comm/preprocess-body)
       (oc/parse-status)))

;; Query
(defn get-query
  "Returns a JQL query based on the query id"
  [query-id]
  (->> query-id
       (comm/get-query)
       (comm/preprocess-body)
       (oc/parse-query)))

;; Issue
(defn get-issue
  "Returns an issue based on the issue key"
  [key]
  (-> key
      comm/get-issue
      comm/preprocess-body
      tr/parse))

(defn print-issue
  "Given an issue key, prints the matching issue in a tabular mode"
  [key]
  (scr/display pr/default_headers [(get-issue key)]))

(defn print-issues
  "Prints a list of issues in a tabular mode"
  [issues]
  (scr/display pr/default_headers issues))

;; Issue Jql query
(defn search
  "List all the issues matching the query"
  [query]
  (->> (comm/search query)
       (comm/preprocess-body)
       (tr/extract-issues)
       (map tr/parse)))

(defn full-search
  "List all the issues matching the query but this time with timetracking data fetched"
  [query]
  (->> query
       (search)
       (map #(merge %1 (get-issue (:key %1))))))

;; Board
(defn get-board-configuration
  "Returns a board configuration based on its id"
  [board-id]
  (->> board-id
       (comm/get-board-configuration)
       (comm/preprocess-body)
       (bd/parse-board)))

(defn list-issues-in-board-column
  "Lists issues in a given board's column"
  [board-id column-index]
  (let [board        (get-board-configuration board-id)
        board-query  (->> board
                          :query-id
                          get-query
                          :query)
        col          (-> board
                         :columns
                         (nth column-index))
        col-name     (:name     col)
        col-statuses (:statuses col)
        col-query    (str "status in (" (clojure.string/join "," col-statuses) ") AND " board-query)]
    (full-search col-query)))

(defn print-issues-in-board-column
  "Prints a board's column placed at a given index"
  [board-id column-index]
  (print-issues (list-issues-in-board-column board-id column-index)))

(defn inspect-board
  "A board viewer"
  [board-id]
  (let [board     (get-board-configuration board-id)
        columns   (:columns board)
        msg       (str "+== Board " (:name board) " ==+\n"
                       "Choose a column for details\n"
                       (clojure.string/join "\n"
                                            (for [index (range (count columns))]
                                              (str index "-" (:name (nth columns index)))))
                       "\n"(count columns) "-to quit\n")]

    (do (println msg)
        (flush)
        (print-issues-in-board-column board-id (read-string (read-line))))))

;; Email notifications
;;   -- As you go emails
(defn send-live-email
  [recipents subject template placeholders results dry-run?]
  (let [generate-email #(mlu/generate-inline-email recipents subject template placeholders %)
        send-email     (if dry-run? scr/print-email ml/post-email)]
    (->> results
         (aggregate-by recipents)
         (map (comp send-email generate-email)))))

;;   -- predefined emails
(defn send-predefined-email
  "Sends a predefined email"
  [recipient subject predefined-template issues dry-run?]
  (let [email        (mlu/generate-file-email recipient subject predefined-template {} issues)
        send-email   (if dry-run? scr/print-email ml/post-email)]
    (send-email email)))

;; Crons
(defn run-crons
  [cron-defs]
  (->> cron-defs
       (cr/define-scheduler)
       (cr/start-scheduler)))
