(ns scrite.recorders
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
  [save-location filename-format]
  (Class/forName "org.sqlite.JDBC")
  (let [save-location (.getCanonicalPath (File. save-location))
        filename-format (if (.isAbsolute (File. filename-format))
                          filename-format
                          (.getCanonicalPath (File. filename-format)))
        con-str (str "jdbc:sqlite:" save-location File/separator "scrite.db")
        db-con (DriverManager/getConnection con-str)
        stmt (.prepareStatement
               db-con
               (str "INSERT INTO shots "
                    "(title, class, mouse_x, mouse_y, filename) "
                    "VALUES (?, ?, ?, ?, ?);"))]
    (fn [item image-data]
      (let [filename (str (construct-filename filename-format))]
        (doto stmt
          (.setString 1 (:title item))
          (.setString 2 (:class item))
          (.setInt 3 (:mouse-x item))
          (.setInt 4 (:mouse-y item))
          (.setString 5 filename)
          .execute)
        (ImageIO/write image-data "png" (File. filename))
        (println "Saved shot " filename)))))
