(ns asr.expr.semnasr
  (:require [clojure.spec.alpha            :as    s         ]
            [clojure.spec.gen.alpha        :as    gen       ]
            [clojure.test.check.generators :as    tgen      ]))


;;  _     _                      _   _
;; (_)_ _| |_ ___ __ _ ___ _ _  | |_| |_ _  _ _ __  ___
;; | | ' \  _/ -_) _` / -_) '_| |  _|  _| || | '_ \/ -_)
;; |_|_||_\__\___\__, \___|_|    \__|\__|\_, | .__/\___|
;;               |___/                   |__/|_|

(s/def ::integer-ttype
  (s/spec              ; means "nestable" not "spliceable" in other "regex" specs
   (s/cat :head        #{'Integer}
          :kind        #{1 2 4 8} ;; i8, i16, i32, i64
          :dimensionss (s/+ :asr.specs/dimensions))))

#_(->> ::integer-ttype-semnasr
     s/gen
     gen/generate)
;; => (Integer
;;     1
;;     []
;;     [352256673885445370349486716111 1624332]
;;     [2]
;;     [86580034 64261]
;;     [8 458885090506053219617214830343524119916]
;;     [27173 1610173734]
;;     [48685 24306766029009794]
;;     [1150789934200849078335609340599 6]
;;     [8627550682561420745323677756919725945686847947214688 1361957]
;;     [12683960 712272279223862022492880049742732644816473428]
;;     [4060631819931453961107624898943598271095916602396458]
;;     [4 0]
;;     [25370]
;;     [2375688583602594078904527])


;;  _     _                                  _            _   _
;; (_)_ _| |_ ___ __ _ ___ _ _   ___ __ __ _| |__ _ _ _  | |_| |_ _  _ _ __  ___
;; | | ' \  _/ -_) _` / -_) '_| (_-</ _/ _` | / _` | '_| |  _|  _| || | '_ \/ -_)
;; |_|_||_\__\___\__, \___|_|   /__/\__\__,_|_\__,_|_|    \__|\__|\_, | .__/\___|
;;               |___/                                            |__/|_|

;; Often, we need a scalar that has no dimensionss [sic].

(s/def ::integer-scalar-ttype
  (s/spec
   (s/cat :head        #{'Integer}
          :kind        #{1 2 4 8}
          :dimensionss #{[]})))

#_
(-> ::integer-scalar-ttype-semnasr (s/exercise 4))
;; => ([(Integer 2 []) {:head Integer, :kind 2, :dimensionss []}]
;;     [(Integer 4 []) {:head Integer, :kind 4, :dimensionss []}]
;;     [(Integer 1 []) {:head Integer, :kind 1, :dimensionss []}]
;;     [(Integer 8 []) {:head Integer, :kind 8, :dimensionss []}])


;; Particular ones so we can match the "kind," i8 thru i64, by
;; hand. Written in this funny way for test-generation.

(do
  (s/def ::i8-scalar-ttype
    (s/spec ; nestable
     (s/cat :head        #{'Integer}
            :kind        #{1}
            :dimensionss #{[]})))

  (s/def ::i16-scalar-ttype
    (s/spec ; nestable
     (s/cat :head        #{'Integer}
            :kind        #{2}
            :dimensionss #{[]})))

  (s/def ::i32-scalar-ttype
    (s/spec ; nestable
     (s/cat :head        #{'Integer}
            :kind        #{4}
            :dimensionss #{[]})))

  (s/def ::i64-scalar-ttype
    (s/spec ; nestable
     (s/cat :head        #{'Integer}
            :kind        #{8}
            :dimensionss #{[]}))))

#_(-> ::i64-scalar-ttype (s/exercise 2))
;; => ([(Integer 8 []) {:head Integer, :kind 8, :dimensionss []}]
;;     [(Integer 8 []) {:head Integer, :kind 8, :dimensionss []}])

;; TEACHING NOTE: With nesting. Because every spec is wrapped in
;; s/spec, results are nested under s/alt.

#_(-> (s/alt ::i8-scalar-ttype
           ::i16-scalar-ttype
           ::i32-scalar-ttype
           ::i64-scalar-ttype)
    (s/exercise 2))
;; => ([[(Integer 2 [])]
;;      [:asr.core/i8-scalar-ttype
;;       {:head Integer, :kind 2, :dimensionss []}]]
;;     [[(Integer 8 [])]
;;      [:asr.core/i32-scalar-ttype
;;       {:head Integer, :kind 8, :dimensionss []}]])

;; TEACHING NOTE: Without nesting. Results are not nested under
;; s/or.

#_(-> (s/or :1 ::i8-scalar-ttype
          :2 ::i16-scalar-ttype
          :4 ::i32-scalar-ttype
          :8 ::i64-scalar-ttype)
    (s/exercise 2))
;; => ([(Integer 8 []) [:8 {:head Integer, :kind 8, :dimensionss []}]]
;;     [(Integer 1 []) [:1 {:head Integer, :kind 1, :dimensionss []}]])
