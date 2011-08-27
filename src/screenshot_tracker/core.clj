(ns screenshot-tracker.core
  (:use screenshot-tracker.wm-utils)
  (:import [java.io File]
           [java.util TimerTask Timer]
           [java.awt Rectangle Toolkit Robot]
           [javax.imageio ImageIO])
  (:require [clj-time.core :as ctime]))

(def *filename-format* "shots/shot-{month}-{date}-{hour}-{minute}-{second}.png")

(defn save-shot
  [file-name]
  (let [screen-rect (Rectangle. (.getScreenSize (Toolkit/getDefaultToolkit)))
        capture (.createScreenCapture (Robot.) screen-rect)]
    (ImageIO/write capture "png" (File. file-name))))

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

(defn start-take-shots
  []
  (.scheduleAtFixedRate (Timer.)
                        (proxy [TimerTask] []
                          (run [] 
                            (println "saving a shot")
                            (save-shot (construct-filename))))
                        (long 0)
                        (long 5000))
  (println "scheduled to take shots"))

(defn -main
  []
  (comment save-shot "shot.png")
  (comment save-shot (construct-filename))
  (comment println (get-active-window-title))
  (comment println (get-active-window-pid))
  (comment println (get-active-window-cmd))
  (start-take-shots))
