(ns clojira.screen
  "This namespace deals with screen printing"
  (:require [clojira.display-utils :as pr]))

(defn clear-screen
  "Clears the screen by scrolling down"
  []
  (println (reduce str (repeat 100 "\n"))))

(defn display
  "Displays a collection of data in a tabular mode"
  ([table-headers table-data]
   (println (pr/build-tabular-model table-headers table-data)))
  ([title table-headers table-data]
   (println (pr/build-rich-tabular-model title table-headers table-data))))

(defn print-email
  "Prints an email content on the screen"
  [[recipients subject msg]]
  (println "------------"             "\n"
           "Recipients: "  recipients "\n"
           "Subject:    "  subject    "\n"
           "Message:    "  msg        "\n"))
