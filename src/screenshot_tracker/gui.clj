(ns screenshot-tracker.gui
  (:use seesaw.core
        [seesaw.chooser :only (choose-file)]))

(defn id [& args] args)

(defn show-main
  [&{:keys [on-start on-pause on-resume] :or {on-start id, on-pause id, on-resume id}}]
  (let [status (atom :init)
        save-location-input (text :size [350 :by 30] :text ".")
        save-location-btn (action
                            :name "..."
                            :handler (fn [e]
                                       (choose-file
                                         :type :save
                                         :selection-mode :dirs-only
                                         :success-fn (fn [chooser file]
                                                       (config! save-location-input
                                                                :text (.getPath file))))))
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
                                            (= @status :init) (do (on-start e) (set-started))
                                            (= @status :started) (do (on-pause e) (set-stopped))
                                            (= @status :stopped) (do (on-resume e) (set-started)))))]
    (-> (frame :title "Scrite"
               :on-close :exit
               :content (vertical-panel
                          :border 10
                          :items [(horizontal-panel
                                    :items ["Enter save location"
                                            [:fill-h 10]
                                            save-location-input
                                            [:fill-h 10]
                                            save-location-btn])
                                  [:fill-v 15]
                                  (horizontal-panel
                                    :items [status-display
                                            [:fill-h 10]
                                            start-stop-btn])]))
      pack!
      show!)))
