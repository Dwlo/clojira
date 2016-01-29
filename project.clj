(defproject clojira "0.1.0-SNAPSHOT"
  :description "Command line Jira administration tool writen in clojure"
  :url "http://clojira.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http            "1.0.1"]
                 [cheshire            "5.4.0"]
                 [doric               "0.9.0"]
                 [stencil             "0.5.0"]
                 [im.chit/cronj       "1.4.3"]
                 [com.draines/postal  "1.11.3" ]
                 [clj-time            "0.11.0"]]
  :profiles {:dev {:dependencies [[midje "1.6.3"]]}})
