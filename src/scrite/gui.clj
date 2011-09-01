(ns scrite.gui
  (:require [scrite.recorders :as recorders])
  (:use seesaw.core
        [seesaw.chooser :only (choose-file)]
        [seesaw.mig :only (mig-panel)]))

(defn id [& args] args)

(defn make-recorder
  [& args]
  (apply recorders/get-sql-recorder args))

(defn show-main
  [&{:keys [on-start on-pause on-resume] :or {on-start id, on-pause id, on-resume id}}]
  (let [status (atom :init)
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
        set-started (fn []
                      (swap! status (fn [_] :started))
                      (config! status-display :text "Shooting"))
        set-stopped (fn []
                      (swap! status (fn [_] :stopped))
                      (config! status-display :text "Paused"))
        start-stop-btn (action :name "Start/Stop"
                               :handler (fn [e]
                                          (cond
                                            (= @status :init) (do (on-start e (make-recorder (config save-location-input :text)
                                                                                             (config file-format-input :text)))
                                                                (set-started))
                                            (= @status :started) (do (on-pause e) (set-stopped))
                                            (= @status :stopped) (do (on-resume e) (set-started)))))]
    (-> (frame :title "Scrite"
               :on-close :exit
               :content (mig-panel
                                    :constraints ["" "[right][300]" "[][]20[]"]
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
