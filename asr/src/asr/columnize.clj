(ns asr.columnize)

;;         _                 _
;;  __ ___| |_  _ _ __  _ _ (_)______
;; / _/ _ \ | || | '  \| ' \| |_ / -_)
;; \__\___/_|\_,_|_|_|_|_||_|_/__\___|


;;; To make it easier to fetch data, columnize these terms:


(defn columnize-symconst [term]
  (let [nym (->> term first)
        enumdicts (->> term second)
        enumvals  (->> enumdicts (map vals)
                       (mapcat identity) (map symbol))]
    (assert (every? #(= 1 (count %)) enumdicts))
    (assert (every? #(= :ASDL-SYMCONST (-> % keys first)) enumdicts))
    {:group 'asr-enum, :nym nym, :vals enumvals}))


#_(->> big-map-of-speclets-from-terms
     first)
;; => [:asr.autospecs/abi
;;     ({:ASDL-SYMCONST "Source"}
;;      {:ASDL-SYMCONST "LFortranModule"}
;;      {:ASDL-SYMCONST "GFortranModule"}
;;      {:ASDL-SYMCONST "BindC"}
;;      {:ASDL-SYMCONST "Interactive"}
;;      {:ASDL-SYMCONST "Intrinsic"})]


#_(->> big-map-of-speclets-from-terms
     first
     columnize-symconst)
;; => {:group asr-enum,
;;     :nym :asr.autospecs/abi,
;;     :vals
;;     (Source      LFortranModule      GFortranModule      BindC
;;      Interactive Intrinsic)}


(defn columnize-tuple [term]
  (let [nym (->> term first)
        stuff (->> term second first)
        head (->> stuff :ASDL-TUPLE symbol)
        params (->> stuff :ASDL-ARGS)
        parmtypes (->> params (map :ASDL-TYPE) (map symbol))
        parmnyms (->> params (map :ASDL-NYM) (map symbol))
        parmmults (->> params (map :MULTIPLICITY))]
    (assert (= 1 (count (->> term second))))
    {:group 'asr-tuple, :nym nym, :head head, :parmtypes parmtypes,
     :parmnyms parmnyms, :parmmults parmmults}))


(defn columnize-composite [term]
  (let [nym (->> term first)
        stuff (->> term second)
        compos (->> stuff (map :ASDL-COMPOSITE))
        heads (->> compos (map :ASDL-HEAD) (map symbol))
        paramss (->> compos (map :ASDL-ARGS))
        params-nyms (->> paramss
                         (map #(map :ASDL-NYM %))
                         (map #(map symbol %)))
        params-types (->> paramss
                          (map #(map :ASDL-TYPE %))
                          (map #(map symbol %)))
        params-mults (->> paramss
                          (map #(map :MULTIPLICITY %)))]
    {:group 'asr-composite, :nym nym, :heads heads,
     :params-types params-types
     :params-nyms params-nyms
     :params-mults params-mults}))


(defn columnize-term
  [term]
  (case (-> term second first keys first)
    :ASDL-SYMCONST  (columnize-symconst term)
    :ASDL-TUPLE     (columnize-tuple term)
    :ASDL-COMPOSITE (columnize-composite term)))


