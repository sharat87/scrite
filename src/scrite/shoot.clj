(ns scrite.shoot
  (:require [scrite.recorders :as recorders]
            [scrite.wm :as wm]
            [scrite.gui :as gui])
  (:import java.awt.MouseInfo))

(defn shoot-every
  "Save scrites every `time-gap` seconds, by calling the `save-fn` to save the scrite"
  [time-gap save-fn keep-shooting?]
  (when @keep-shooting?
    (let [active-window (wm/active-window-details)
          mouse-pos (. (MouseInfo/getPointerInfo) getLocation)]
      (save-fn
        (assoc active-window
               :mouse-x (.x mouse-pos)
               :mouse-y (.y mouse-pos))
        (wm/get-screenshot-data)))
    (Thread/sleep (long (* time-gap 1000)))
    (recur time-gap save-fn keep-shooting?)))

(defn start-shooting
  []
  (let [keep-shooting? (atom true)
        start-shooter (fn [recorder]
                        (.start (Thread.
                                  (fn [] (shoot-every
                                           5
                                           recorder
                                           keep-shooting?)))))]
    (gui/show-main
      :on-start (fn [e recorder]
                  ; FIXME: The same instance of the keep-shooting? atom is being use over and over again
                  (swap! keep-shooting? (fn [_] true))
                  (start-shooter recorder))
      :on-stop (fn [e]
                 (swap! keep-shooting? not)))))
