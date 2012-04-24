(ns scrite.wm.linux
  (:import [java.awt Rectangle Toolkit Robot])
  (:require [clojure.java.shell :as shell])
  (:use [clojure.string :only (trim-newline split split-lines)])
  (:use scrite.wm))

(defn to-hex
  [n]
  (format "0x%08x" n))

; Code stolen from sikuli (clojurified).
(defmethod active-window-details :linux
  []
  (let [window-id (-> (shell/sh "xdotool" "getactivewindow")
                    :out
                    trim-newline
                    Integer/parseInt
                    to-hex)
        all-window-lines (-> (shell/sh "wmctrl" "-lpGx")
                           :out
                           split-lines)
        all-windows (map #(zipmap
                            [:id :desk :pid :x :y :w :h :class :host :title]
                            (split #"\s+" 10 %))
                         all-window-lines)]
    (first (filter #(= window-id (:id %)) all-windows))))

(defmethod get-screenshot-data :linux
  []
  (let [screen-rect (Rectangle. (.getScreenSize (Toolkit/getDefaultToolkit)))
        capture (.createScreenCapture (Robot.) screen-rect)]
    capture))

(defn get-active-window-id
  []
  (-> (shell/sh "xprop" "-root" "_NET_ACTIVE_WINDOW")
    :out
    (.split " ")
    reverse
    peek))

(defn get-active-window-prop
  [prop]
  (if (= prop :id)
    (get-active-window-id)
    (let [prop-value (-> (shell/sh "xprop" "-id" (get-active-window-id) prop)
                       :out
                       (.split " = " 2)
                       (nth 1)
                       .trim)]
      prop-value)))

(defn get-active-window-title
  []
  (let [window-title (get-active-window-prop "WM_NAME")]
    (.substring window-title
                1
                (- (.length window-title)
                   1))))

(defn get-active-window-class
  []
  (let [window-class (get-active-window-prop "WM_CLASS")]
    window-class))
