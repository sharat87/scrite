(ns screenshot-tracker.recorders
  (:import [java.sql DriverManager]
           [java.io File]
           [javax.imageio ImageIO])
  (:require [clj-time.core :as ctime]))

(defn format-str
  [template legend]
  (reduce (fn [template [key value]] (.replace template
                                               (str "{" (name key) "}")
                                               (str value)))
          template
          (seq legend)))

(defn construct-filename
  [filename-format]
  (let [now (ctime/now)
        format-data {:year    (ctime/year now)
                     :month   (ctime/month now)
                     :date    (ctime/day now)
                     :day     (ctime/day-of-week now)
                     :hour    (ctime/hour now)
                     :minute  (ctime/minute now)
                     :second  (ctime/sec now)}]
    (format-str filename-format format-data)))

(defn get-sql-recorder
  [db-path filename-format]
  (Class/forName "org.sqlite.JDBC")
  (let [db-con (DriverManager/getConnection "jdbc:sqlite:scrite.db")
        stmt (.prepareStatement
               db-con
               "INSERT INTO shots (title, img) VALUES (?, ?);")]
    (fn [item image-data]
      (let [filename (str (construct-filename filename-format) ".png")]
        (doto stmt
          (.setString 1 (:title item))
          (.setString 2 (:img item))
          .execute)
        (ImageIO/write image-data "png" (File. filename))
        (println "Saved shot " filename)))))
