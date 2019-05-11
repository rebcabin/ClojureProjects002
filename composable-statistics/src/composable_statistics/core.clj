(ns composable-statistics.core)

(require '[clojure.core.matrix :as ccm])
(ccm/set-current-implementation :vectorz)
(require '[clojure.core.matrix.linear :as ccml])

(defn similarity-transform [A M]
  (ccm/mmul A M (ccm/transpose A)))

(defn solve-matrix
  "The 'solve' routine in clojure.core.matrix only works on Matrix times Vector.
  We need it to work on Matrix times Matrix. The equation to solve is

  Ann * Xnm = Bnm

  Think of the right-hand side matrix Bnm as a matrix of columns. Iterate over
  its transpose, treating each column as a row, then converting that row to a
  vector, to get the transpose of the solution X."
  [Ann Bnm]
  (ccm/transpose (mapv (partial ccml/solve Ann) (ccm/transpose Bnm))))

(defn kalman-update [{:keys [xn1 Pnn]} {:keys [zm1 Hmn Ann Qnn Rmm]}]
  (let [x'n1   (ccm/mmul Ann xn1)                    ; Predict state
        P'nn   (ccm/add
                Qnn (similarity-transform Ann Pnn))  ; Predict covariance
        Dmm    (ccm/add
                Rmm (similarity-transform Hmn P'nn)) ; Gain precursor
        DTmm   (ccm/transpose Dmm)                   ; Support for "solve"
        HP'Tmn (ccm/mmul Hmn (ccm/transpose P'nn))   ; Support for "solve"
        KTmn   (solve-matrix DTmm HP'Tmn)            ; Eqn 3 of http://vixra.org/abs/1606.0328
        Knm    (ccm/transpose KTmn)                  ; Kalman gain
        rm1    (ccm/sub zm1  (ccm/mmul Hmn x'n1))    ; innovation = predicted obn residual
        x''n1  (ccm/add x'n1 (ccm/mmul Knm rm1))     ; final corrected estimate
        n      (ccm/dimension-count xn1 0)
        Lnn    (ccm/sub (ccm/identity-matrix n)      ; Support for new covariance ...
                        (ccm/mmul Knm Hmn))          ; ...  exposed to catastrophic cancellation
        P''nn  (ccm/mmul Lnn P'nn)]                  ; New covariance
    {:xn1 x''n1, :Pnn P''nn}))

(defn Hmn-t [t]
  (ccm/matrix [[(* t t t) (* t t) t 1]]))

(def true-x
  (ccm/array [[-5] [-4] [9] [-3]]))

(require '[clojure.core.matrix.random :as ccmr])

(defn fake [n]
  (let [times   (range -2.0 2.0 (/ 2.0 n))
        Hmns    (mapv Hmn-t times)
        true-zs (mapv #(ccm/mmul % true-x) Hmns)
        zm1s    (mapv #(ccm/add
                        % (ccm/array
                           [[(ccmr/rand-gaussian)]]))
                      true-zs)]
    {:times times, :Hmns Hmns, :true-zs true-zs, :zm1s zm1s}))

(def test-data (fake 7))

;;; A state cluster is a vector of $\boldsymbol{x}$ and $\boldsymbol{P}$:

(def state-cluster-prior
  {:xn1 (ccm/array [[0.0] [0.0] [0.0] [0.0]])
   :Pnn (ccm/mul 1000.0 (ccm/identity-matrix 4))})

;;; An obn-cluster is a vector of $\boldsymbol{z}$, $\boldsymbol{H}$,
;;; $\boldsymbol{A}$, $\boldsymbol{Q}$, and $\boldsymbol{R}$. _Obn_ is short for
;;; _observation_.

(def obn-clusters
  (let [c (count (:times test-data))]
    (mapv (fn [zm1 Hmn Ann Qnn Rmm]
            {:zm1 zm1, :Hmn Hmn, :Ann Ann, :Qnn Qnn, :Rmm Rmm})
          (:zm1s test-data)
          (:Hmns test-data)
          (repeat c (ccm/identity-matrix 4))
          (repeat c (ccm/zero-matrix 4 4))
          (repeat c (ccm/identity-matrix 1))
          )))

(reduce kalman-update state-cluster-prior obn-clusters)
