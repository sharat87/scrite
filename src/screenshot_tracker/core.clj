(ns screenshot-tracker.core
  (:require [screenshot-tracker.recorders :as recorders]
            [screenshot-tracker.wm-utils :as wm-utils]
            [screenshot-tracker.gui :as gui])
  (:require [clj-time.core :as ctime]))

(def *filename-format* "shots/shot-{month}-{date}-{hour}-{minute}-{second}")

(defn format-str
  [template legend]
  (reduce (fn [template [key value]] (.replace template
                                               (str "{" (name key) "}")
                                               (str value)))
          template
          (seq legend)))

(defn get-filename-format-data
  []
  (let [now (ctime/now)]
    {:year    (ctime/year now)
     :month   (ctime/month now)
     :date    (ctime/day now)
     :day     (ctime/day-of-week now)
     :hour    (ctime/hour now)
     :minute  (ctime/minute now)
     :second  (ctime/sec now)}))

(defn construct-filename
  []
  (format-str *filename-format* (get-filename-format-data)))

(defn shoot-every
  "Save scrites every `time-gap` seconds, by calling the `save-fn` to save the scrite"
  [time-gap save-fn]
  (let [filename (construct-filename)]
    (save-fn
      {:title (wm-utils/get-active-window-title)
       :cmd (wm-utils/get-active-window-cmd)
       :img filename}
      (wm-utils/get-screenshot-data))
    (println "Saved shot " filename))
  (Thread/sleep (long (* time-gap 1000)))
  (recur time-gap save-fn))

(defn -main
  []
  (comment save-shot "shot.png")
  (comment save-shot (construct-filename))
  (comment println (get-active-window-title))
  (comment println (get-active-window-pid))
  (comment println (get-active-window-cmd))
  (comment (recorders/get-sql-recorder "scrite.db")
     {:title (wm-utils/get-active-window-title)
      :cmd (wm-utils/get-active-window-cmd)}
     (wm-utils/get-screenshot-data))
  (comment shoot-every 5 (recorders/get-sql-recorder "scrite.db"))
  (comment .start (Thread. (fn [] (shoot-every 5 (recorders/get-sql-recorder "scrite.db")))))
  (gui/show-main))
