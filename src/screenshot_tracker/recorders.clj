(ns screenshot-tracker.recorders
  (:import [java.sql DriverManager]
           [java.io File]
           [javax.imageio ImageIO]))

(defn get-sql-recorder
  [db-path]
  (Class/forName "org.sqlite.JDBC")
  (let [db-con (DriverManager/getConnection "jdbc:sqlite:scrite.db")
        stmt (.prepareStatement
               db-con
               "INSERT INTO shots (title, cmd, img) VALUES (?, ?, ?);")]
    (fn [item image-data]
      (doto stmt
        (.setString 1 (:title item))
        (.setString 2 (:cmd item))
        (.setString 3 (:img item))
        .execute)
      (ImageIO/write image-data "png" (File. (str (:img item) ".png"))))))
