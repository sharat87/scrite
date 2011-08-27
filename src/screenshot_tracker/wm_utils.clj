(ns screenshot-tracker.wm-utils
  (:require [clojure.java.shell :as shell]))

(defn get-active-window-id
  []
  (->
    (shell/sh "xprop" "-root" "_NET_ACTIVE_WINDOW")
    :out
    (.split " ")
    reverse
    peek))

; Ofcourse, this fn is not tested with anything other than :title :)
(defn get-active-window-prop
  [prop]
  (if (= prop :id)
    (get-active-window-id)
    (let [prop-value (->
                       (shell/sh "xprop" "-id" (get-active-window-id) prop)
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
                     2))))

(defn get-active-window-pid
  []
  (get-active-window-prop "_NET_WM_PID"))

(defn get-active-window-cmd
  []
  (->
    (shell/sh "ps" "-o" "cmd" (get-active-window-pid))
    :out
    (.split "\n")
    reverse
    peek))
