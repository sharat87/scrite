(ns scrite.shoot
  (:require [scrite.recorders :as recorders]
            [scrite.wm-utils :as wm-utils]
            [scrite.gui :as gui])
  (:import java.awt.MouseInfo))

(defn shoot-every
  "Save scrites every `time-gap` seconds, by calling the `save-fn` to save the scrite"
  [time-gap save-fn keep-shooting?]
  (when @keep-shooting?
    (let [mouse-pos (. (MouseInfo/getPointerInfo) getLocation)]
      (save-fn
        {:title (wm-utils/get-active-window-title)
         :class (wm-utils/get-active-window-class)
         :mouse-x (.x mouse-pos)
         :mouse-y (.y mouse-pos)}
        (wm-utils/get-screenshot-data)))
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
                  (start-shooter recorder))
      :on-stop (fn [e]
                 (swap! keep-shooting? not)))))
