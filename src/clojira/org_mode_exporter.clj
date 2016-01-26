(ns clojira.org-mode-exporter
  "This namespace deals with functions to allow exports in Emacs org-mode format"
  (:require [clojira
             [display-utils :as pr]
             [file-util     :as fu]
             [properties    :as pp]]))

(def org-mode-metadata "#+STARTUP: indent")
(def single-bullet "*")
(def double-bullets (str single-bullet single-bullet))


(defn- gen-filename
  "Generates a filename"
  [title]
  (str (clojure.string/replace title " " "_") "-" (pr/current-time) ".org" ))

(defn export
  [title table-headers table-data]
  (->> (str "\n" single-bullet " org-mode export\n"
            (pr/build-rich-tabular-model title table-headers table-data))
       (fu/write-to-file pp/export-output-dir (gen-filename title))))
