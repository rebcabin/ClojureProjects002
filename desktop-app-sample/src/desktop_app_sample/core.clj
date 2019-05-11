(ns desktop-app-sample.core
  (:import
   (java.awt       BorderLayout)
   (java.awt.event ActionListener)
   (javax.swing    JButton      JFrame  JLabel
                   JOptionPane  JPanel  JTextField))
  (:gen-class))

(defn message
  "gets the message to display based on the current text in text-field"
  [text-field]
  (str "Hello, " (.getText text-field) "!"))

(let [name-field   (JTextField. "World" 10)
      greet-button (JButton. "Greet")
      panel        (JPanel.)
      frame (proxy
                [JFrame ActionListener]
                []
              (actionPerformed [e]
                (JOptionPane/showMessageDialog
                 nil (message name-field))))
      ]
  (doto panel
    (.add (JLabel. "Name:"))
    (.add name-field))
  (doto frame
    (.add panel BorderLayout/CENTER)
    (.add greet-button BorderLayout/SOUTH)
    (.pack)
    (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
    (.setVisible true))
  (.addActionListener greet-button frame)
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ;;(println "Hello, World!")
  )
