(defproject clojira "0.1.0-SNAPSHOT"
  :description "Command line Jira administration tool writen in clojure"
  :url "http://clojira.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [clj-http            "3.9.0"]
                 [cheshire            "5.4.0"]
                 [doric               "0.9.0"]
                 [stencil             "0.5.0"]
                 [im.chit/cronj       "1.4.3"]
                 [com.draines/postal  "2.0.5" ]
                 [clj-time            "0.11.0"]]
  :profiles {:dev {:dependencies [[midje "1.6.3"]]}})
