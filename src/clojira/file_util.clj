(ns clojira.file-util
  "This namespace deals with file utility functions"
  (:require [clojure.java.io :as io]))

(defn write-to-file
  "Writes some content into a specific place"
  [dir filename content]
  (let [file (io/file dir filename)]
    (do
      (with-open [w (io/writer file)] (.write w content))
      (.getPath file))))
