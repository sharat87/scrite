(ns scrite.wm)

(def current-os
  (let [os-name (System/getProperty "os.name")]
    (cond
      (= os-name "Linux") :linux
      (.startsWith os-name "Windows") :windows)))

(defmulti active-window-details
  "Get details about the active window. Returns a hash-map with keys:

    :title The window title
    :class The window's class"
  (constantly current-os))
