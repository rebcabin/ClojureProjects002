(defn table [syze]
  (map
   (fn [i] {:asin i
           :vc (mod (* i 37) (/ syze 100))})
   (range syze)))

(defn sample [vc syze]
  (filter
   (fn [row] (== vc (row :vc)))
   (table syze)))

(table 1000)
(sample 3 1000)
