(ns scrite.wm
  (:import [java.awt Rectangle Toolkit Robot])
  (:require [clojure.java.shell :as shell])
  (:use [clojure.string :only (trim-newline split split-lines)]))

(def current-os (System/getProperty "os.name"))

(defmulti active-window-details
  "Get details about the active window."
  (constantly current-os))

(defmulti get-screenshot-data
  "Return screenshot image data."
  (constantly current-os))
