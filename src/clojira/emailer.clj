(ns clojira.emailer
  "This namespace deals with emailing functions"
  (:require [clojira
             [file-util      :as utils]
             [properties     :as pp   ]]
            [postal.core     :as pc   ]
            [clojure.java.io :as io   ]))

;; smtp connection string
(def smtp-conn {:host pp/smtp-host
                :user pp/email-address
                :pass pp/email-password
                :ssl  true})

(defn build-email
  "Builds an basic email"
  [recipients subject message]
  {:from    pp/email-address
   :to      recipients
   :subject subject
   :body    [{:type    "text/plain"
              :content message}]})

(defn build-attachments
  "Builds attachments from list of files"
  [files]
  (map (fn [f] {:type :attachment
               :content (io/file f)})
       files))

(defn add-attachments
  "Adds attached files into the email"
  [attached-files email]
  (let [attachments  (build-attachments attached-files)]
    (update-in email [:body] merge attachments)))

(defn post-email
  "Sends email with or without attachments"
  ([[recipients subject message]]
   (->> (build-email recipients subject message)
        (pc/send-message smtp-conn)))

  ([recipients subject message]
   (->> (build-email recipients subject message)
        (pc/send-message smtp-conn)))

  ([recipients subject message files]
   (->> (build-email recipients subject message)
        (add-attachments files)
        (pc/send-message smtp-conn))))
