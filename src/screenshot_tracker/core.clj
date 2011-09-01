(ns screenshot-tracker.core
  (:use [screenshot-tracker.shoot :only (start-shooting)]))

(defn -main
  []
  (start-shooting))
