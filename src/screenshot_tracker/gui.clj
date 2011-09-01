(ns screenshot-tracker.gui
  (:use seesaw.core
        [seesaw.chooser :only (choose-file)]))

(defn id [& args] args)

(defn show-main
  [&{:keys [on-start on-stop] :or {on-start id, on-stop id}}]
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
                                    :items [(action :name "Start/Resume"
                                                    :handler #(on-start %))
                                            [:fill-h 10]
                                            (action :name "Stop/Pause"
                                                    :handler #(on-stop %))])]))
      pack!
      show!)))
