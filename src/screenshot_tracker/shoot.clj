(ns screenshot-tracker.shoot
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
  (let [channel (atom {:shoot? true})
        start-shooter (fn [recorder] (.start (Thread.
                                       (fn [] (shoot-every
                                                channel
                                                5
                                                recorder)))))]
    (gui/show-main
      :on-start (fn [e recorder]
                  (start-shooter recorder))
      :on-pause (fn [e]
                  (swap! channel assoc :shoot? false))
      :on-resume (fn [e]
                   (swap! channel assoc :shoot? true)))))
