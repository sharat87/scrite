(ns scrite.core
  (:require [scrite.wm :as wm])
  (:use [scrite.shoot :only (start-shooting)]))

; Initialize OS-specific implementations of window manager functions.
(require (case wm/current-os
           "Linux" 'scrite.wm.linux))

(defn -main
  []
  (start-shooting))
