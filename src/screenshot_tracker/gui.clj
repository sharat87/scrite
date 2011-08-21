(ns screenshot-tracker.gui
  (:import [javax.swing SwingUtilities JFrame JPanel JLabel JTextField
            BorderFactory Box BoxLayout]
           [java.awt GridBagLayout GridBagConstraints BorderLayout Dimension]))

(defn get-main-pane
  []
  (let [main-pane (JPanel.)
        location-box (JPanel.)]


    (comment doto location-box
      (.setLayout (BoxLayout. location-box BoxLayout/X_AXIS))
      (.add (doto (JLabel. "Location to save screenshots")
              (.setBorder (BorderFactory/createEmptyBorder 5 5 5 5))))
      (.add (JTextField. 20))
      (.setMaximumSize (Dimension. 500 70)))

    (doto main-pane
      (.setLayout (GridBagLayout.))
      ;(.setLayout (BoxLayout. main-pane BoxLayout/Y_AXIS))
      (.add location-box))

    (let [label (JLabel. "Save screenshots in")
          gc (let [constraints GridBagConstraints.]
               (set! (constraints fill) GridBagConstraints/HORIZONTAL)
               (set! (constraints gridx) 0)
               (set! (constraints gridy) 0)
               constraints)]
      (.add main-pane label gc))))

(defn -main
  []
  (let [frame (JFrame.)
        main-pane (get-main-pane)]
    (doto frame
      (.setTitle "Screenshot takey")
      (.setSize 600 400)
      ;(.setContentPane main-pane)
      (.add main-pane BorderLayout/CENTER)
      (.setLocationRelativeTo nil)
      (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
      (.setVisible true))))

(SwingUtilities/invokeLater -main)
