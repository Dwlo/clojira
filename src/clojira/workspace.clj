(ns clojira.workspace
  "This namespace should be used for user personal usage"
  (:require [clj-time.local     :as    tl]
            [clojira
             [display-utils     :as    pr]
             [email-utils       :as    emu]
             [emailer           :as    em]
             [org-mode-exporter :as    exp]
             [properties        :as    pp]
             [screen            :as    scr]
             [service           :refer :all]]
            [stencil.core       :as    stc]))

(def custom-query "status = Done")

;; Search
(defn custom-search
  "Returns issues matching the custom query"
  []
  (full-search custom-query))

;; Printing
(defn print-custom-search
  "Prints issues matching the defined query"
  []
  (let [results (custom-search)]
    (scr/display "Issues from custom query"
                 pr/default_headers
                 results)))

;; Export
(defn export-custom-search
  "Exports the results of a custom search to an org-mode formated file"
  []
  (exp/export "Issues from custom query"
              pr/default_headers
              (custom-search)))

;; Email
(defn email-custom-search
  []
  (let [results (search custom-query)]
   (send-predefined-email pp/user-email "Sample mail" "custom-letter" results true)))

;; Crons
(defn run-custom-cron
  []
  (let [cron-defs [{:id "custom-cron"
                    :handler (fn [t opts] (println (print-custom-search)))
                    :pre-hook  (fn [t opts] (println "+== Cron \"custom-cron\"  started ==+ "))
                    :post-hook (fn [t opts] (println "+== Cron \"custom-cron\" finished ==+ "))
                    :schedule "/60 * * * * * *"
                    :opts {}}]]
    (run-crons cron-defs)))
