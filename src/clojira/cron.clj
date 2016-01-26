(ns clojira.cron
  (:require [cronj.core :as cr]))

(defn define-scheduler
  [cron-defs]
  (cr/cronj :entries cron-defs))

(defn start-scheduler
  [scheduler]
  (cr/start! scheduler))

(defn stop-scheduler
  [scheduler]
  (cr/stop! scheduler))
