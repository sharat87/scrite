(ns scrite.wm.windows
  (:import com.sun.jna.Native
           com.sun.jna.platform.win32.User32)
  (:use scrite.wm))

(defn- active-window
  []
  (.GetForegroundWindow User32/INSTANCE))

(defn- active-window-title
  []
  (let [title (char-array 1024)]
    (.GetWindowText User32/INSTANCE (active-window) title 1024)
    (Native/toString title)))

(defn- active-window-class
  []
  (let [class-name (char-array 1024)]
    (.GetClassName User32/INSTANCE (active-window) class-name 1024)
    (Native/toString class-name)))

(defmethod active-window-details :windows
  []
  {:title (active-window-title)
   :class (active-window-class)})
