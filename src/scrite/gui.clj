(ns scrite.gui
  (:require [scrite.recorders :as recorders])
  (:use seesaw.core
        [seesaw.chooser :only (choose-file)]
        [seesaw.mig :only (mig-panel)]))

(native!)

(defn id [& args] args)

(defn make-recorder
  [& args]
  (apply recorders/get-sql-recorder args))

(defn show-main
  [&{:keys [on-start on-stop] :or {on-start id, on-stop id}}]
  (let [running? (atom false)
        save-location-input (text ".")
        save-location-btn (action
                            :name "..."
                            :handler (fn [e]
                                       (choose-file
                                         :type :save
                                         :selection-mode :dirs-only
                                         :success-fn (fn [chooser file]
                                                       (config! save-location-input
                                                                :text (.getPath file))))))
        file-format-input (text "shot-{month}-{date}-{hour}-{minute}-{second}.png")
        status-display (label :text "Ready")
        start-shooting (fn [e]
                         (on-start e (make-recorder (config save-location-input :text)
                                                    (config file-format-input :text)))
                         (swap! running? not)
                         (config! status-display :text "Shooting"))
        stop-shooting (fn [e]
                        (on-stop e)
                        (swap! running? not)
                        (config! status-display :text "Paused"))
        start-stop-btn (action :name "Start/Stop"
                               :handler (fn [e]
                                          (if @running?
                                            (stop-shooting e)
                                            (start-shooting e))))]
    (-> (frame :title "Scrite"
               :on-close :exit
               :content (mig-panel
                          :constraints ["" "[right][420]" "[][]20[]"]
                          :items [["Enter save location"]
                                  [save-location-input "growx, split 2"]
                                  [save-location-btn "wrap"]
                                  ["Images saved as"]
                                  [file-format-input "growx, wrap"]
                                  [start-stop-btn "split 2"]
                                  [status-display]
                                  [(action :name "Help") "align right"]]))
      pack!
      show!)))
