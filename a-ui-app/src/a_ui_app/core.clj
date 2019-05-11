;;; Borrowed from
;;; http://stackoverflow.com/questions/2792451/improving-my-first-clojure-program?rq=1

(ns a-ui-app.core
  (:import [java.awt    Color  Dimension  event.KeyListener])
  (:import [javax.swing JFrame JPanel                      ])
  (:gen-class))

(def x (ref 0))
(def y (ref 0))

(def panel
  (proxy [JPanel KeyListener] []
    (getPreferredSize [] (Dimension. 100 100))
    (keyPressed [e]
      (let [keyCode (.getKeyCode e)]
        (dosync
         (apply alter
           (case keyCode
             37 [x dec]
             38 [y dec]
             39 [x inc]
             40 [y inc])))
        ;(println keyCode)
        ))
    (keyReleased [e])
    (keyTyped [e])))

(def frame (JFrame. "Test"))

(defn draw-rectangle [p x y]
  (doto (.getGraphics p)  
    (.setColor (java.awt.Color/WHITE))
    (.fillRect 0 0 100 100)
    (.setColor (java.awt.Color/BLUE))
    (.fillRect (* 10 x) (* 10 y) 10 10)))

(defn -main
  "See http://stackoverflow.com/questions/2792451/improving-my-first-clojure-program?rq=1."
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))

  (doto panel
    (.setFocusable true)
    (.addKeyListener panel))

  (doto frame
    (.add panel)
    (.pack)
    (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
    (.setVisible true))

  (loop []
    (draw-rectangle panel @x @y)
    (Thread/sleep 10)
    (recur))
  )

