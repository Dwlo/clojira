(ns clojira.email-utils
  "This namespace deals with utility email functions"
  (:require [clojira.properties :as pp ]
            [stencil.core       :as stc]))

(defn mandatory-placeholders
  "Selects extra placeholders from issues to be added to the message template"
  [issue]
  (select-keys issue [:reporter :reporter-email :assignee :assignee-email]))

(defn generate-inline-email
  [recipients subject template optional-placeholders [first-issue & rest :as issues]]
  (let [placeholders        (merge optional-placeholders (mandatory-placeholders first-issue) {:issues issues})
        msg                 (stc/render-string template placeholders)
        resolved-recipients (if (keyword? recipients) (recipients first-issue) recipients)]
    [resolved-recipients subject msg]))

(defn generate-file-email
  [recipients subject file-template optional-placeholders [first-issue & rest :as issues]]
  (let [placeholders        (merge optional-placeholders (mandatory-placeholders first-issue) {:issues issues})
        msg                 (stc/render-file file-template placeholders)
        resolved-recipients (if (keyword? recipients) (recipients first-issue) recipients)]
     [resolved-recipients subject msg]))
