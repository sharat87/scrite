(ns screenshot-tracker.gui
  (:use seesaw.core
        [seesaw.chooser :only (choose-file)]))

(defn show-main
  []
  (let [save-location-input (text :size [350 :by 30] :text ".")
        save-location-btn (action
                            :name "..."
                            :handler (fn [e]
                                       (choose-file
                                         :type :save
                                         :selection-mode :dirs-only
                                         :success-fn (fn [chooser file]
                                                       (config! save-location-input
                                                                :text (.getPath file))))))]
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
                                    :items [(button :text "Start/Resume")
                                            [:fill-h 10]
                                            (button :text "Stop/Pause")])]))
      pack!
      show!)))
