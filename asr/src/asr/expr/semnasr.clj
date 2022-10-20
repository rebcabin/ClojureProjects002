(ns asr.expr.semnasr
  (:require [clojure.spec.alpha            :as    s         ]
            [clojure.pprint                :refer [pprint]  ]
            [clojure.spec.gen.alpha        :as    gen       ]
            [clojure.spec.test.alpha       :as    stest     ]
            [clojure.test.check.generators :as    tgen      ]
            [clojure.math.numeric-tower    :refer [expt]    ]
            [clojure.set                   :as    set       ]))


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
