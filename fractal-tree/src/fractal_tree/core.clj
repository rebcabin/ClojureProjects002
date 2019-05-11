(ns fractal-tree.core
  (:import [ javax.swing JFrame JPanel] )
  (:import [ java.awt Color Graphics Graphics2D])
  (:gen-class))

(defn draw-tree [g2d angle x y length branch-angle depth]
  (if (> depth 0)
    (let [new-x (+ x (* -1 length (Math/sin (Math/toRadians angle))))
          new-y (+ y (* -1 length (Math/cos (Math/toRadians angle))))
          new-length (fn [] (* length (+ 0.75 (rand 0.1))))
          new-angle (fn [op] (op angle (* branch-angle (+ 0.75 (rand)))))]
      (. g2d drawLine x y new-x new-y)
      (draw-tree g2d (new-angle +) new-x new-y (new-length) branch-angle (- depth 1))
      (draw-tree g2d (new-angle -) new-x new-y (new-length) branch-angle (- depth 1)))))

(defn render [g w h ]
  (doto g
    (.setColor (Color/BLACK))
    (.fillRect 0 0 w h)
    (.setColor (Color/GREEN)))
  (let [init-length ( / (min w h) 5),
        branch-angle (* 10 (/ w h)),
        max-depth 10]
    (draw-tree  g 0.0 (/ w 2) h init-length branch-angle max-depth)))

(defn create-panel []
  "Create a panel with a customised render"
  (proxy [JPanel] []
    (paintComponent [g]
      (proxy-super paintComponent g)
      (render g (. this getWidth) (. this getHeight)))))

(defn run []
  (let [frame (JFrame. "Fractal Tree")
        panel (create-panel)]
    (doto frame
      (.add panel)
      (.setSize 640 400)
      (.setVisible true))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))
  (run))
