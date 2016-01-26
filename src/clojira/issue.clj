(ns clojira.issue
  "This namespace deals with functions for issues"
  (:require [clj-time.format          :as ctf]
            [clojira.other-components :as oc]))

(defrecord Issue [key project summary type version status description resolution assignee assignee-email dev-team estimated-time spent-time reporter reporter-email creation-date])

(defn extract-issues
  [json-issues]
  (:issues json-issues))

(defn get-key
  [issue]
  (:key issue))

(defn get-project
  [issue]
  (:key (:project (:fields issue))))

(defn get-summary
  [issue]
  (:summary (:fields issue)))

(defn get-issue-type-name
  [issue]
  (:name (:issuetype (:fields issue))))

(defn get-target-version
  [issue]
  (let  [fix-versions (:fixVersions (:fields issue))]
    (if (empty? fix-versions) nil (apply :name fix-versions))))

(defn get-status
  [issue]
  (let [json-status (:status (:fields issue))]
    (oc/parse-status json-status)))

(defn get-description
  [issue]
  (:description (:fields issue)))

(defn get-resolution
  [issue]
  (:name (:resolution (:fields issue))))

(defn get-assignee-display-name
  [issue]
  (:displayName (:assignee (:fields issue))))

(defn get-assignee-email
  [issue]
  (:emailAddress (:assignee (:fields issue))))

(defn get-dev-team
  [issue]
  (:value (:customfield_12500 (:fields issue))))

(defn get-estimated-time
  [issue]
  (:originalEstimate (:timetracking (:fields issue))))

(defn get-spent-time
  [issue]
  (:timeSpent (:timetracking (:fields issue))))

(defn get-reporter-display-name
  [issue]
  (:displayName (:reporter (:fields issue))))

(defn get-reporter-email
  [issue]
  (:emailAddress (:reporter (:fields issue))))

(defn get-creation-date
  [issue]
  (ctf/parse (ctf/formatters :date-time) (:created (:fields issue))))

(def select-values (juxt get-key
                         get-project
                         get-summary
                         get-issue-type-name
                         get-target-version
                         get-status
                         get-description
                         get-resolution
                         get-assignee-display-name
                         get-assignee-email
                         get-dev-team
                         get-estimated-time
                         get-spent-time
                         get-reporter-display-name
                         get-reporter-email
                         get-creation-date))

(defn parse
  "Transforms a raw json issue into a proper internal representation"
  [json-rep]
  (->> json-rep
       (select-values)
       (apply ->Issue)))
