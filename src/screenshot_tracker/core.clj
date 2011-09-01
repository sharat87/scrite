(ns screenshot-tracker.core
  (:require [screenshot-tracker.recorders :as recorders]
            [screenshot-tracker.wm-utils :as wm-utils]
            [screenshot-tracker.gui :as gui]))

(defn shoot-every
  "Save scrites every `time-gap` seconds, by calling the `save-fn` to save the scrite"
  [channel time-gap save-fn]
  (if (@channel :shoot?)
    (save-fn
      {:title (wm-utils/get-active-window-title)}
      (wm-utils/get-screenshot-data)))
  (Thread/sleep (long (* time-gap 1000)))
  (recur channel time-gap save-fn))

(defn start-shooting
  []
  (let [channel (atom {:shoot? nil})]
    (gui/show-main
      :on-start (fn [e]
                  (if (nil? (@channel :shoot?))
                    (.start (Thread.
                              (fn [] (shoot-every
                                       channel
                                       5
                                       (recorders/get-sql-recorder
                                         "scrite.db"
                                         "shots/shot-{month}-{date}-{hour}-{minute}-{second}"))))))
                  (swap! channel assoc :shoot? true))
      :on-stop (fn [e]
                 (swap! channel assoc :shoot? false)))))

(defn -main
  []
  (start-shooting))
