(ns rummy.core-test
  (:require [clojure.test                    :refer :all ]
            [rummy.core                      :refer :all ]
            [clojure.test.check.clojure-test :refer :all ]
            [clojure.test.check              :as    qc   ]
            [clojure.test.check.generators   :as    gen  ]
            [clojure.test.check.properties   :as    prop ]
            [tupelo.core                     :refer [spy]]
            [funcyard.core                   :as    funcs]))

(defspec check-that-test-dot-check-works-properly
  100 ;; the number of iterations for test.check to test
  (prop/for-all [v (gen/not-empty (gen/vector gen/int))]
                (= (apply min v)
                   (first (sort v)))))

(deftest test-got-funcyard
  (testing "Picking up new funcyard."
    (is (not (funcs/has-duplicates #{})))))

(deftest test-counts
  (testing "counts of suits, pips, and cards."
    (is (= +nsuits+ (count suits)))
    (is (= +npips+  (count pips)))
    (is (= +ndeck+  (count deck)))))

(deftest test-representations
  (testing "card representations"
    (is (= '("SA" "S2" "S3" "S4" "S5" "S6" "S7" "S8" "S9" "ST" "SJ" "SQ" "SK"
             "HA" "H2" "H3" "H4" "H5" "H6" "H7" "H8" "H9" "HT" "HJ" "HQ" "HK"
             "CA" "C2" "C3" "C4" "C5" "C6" "C7" "C8" "C9" "CT" "CJ" "CQ" "CK"
             "DA" "D2" "D3" "D4" "D5" "D6" "D7" "D8" "D9" "DT" "DJ" "DQ" "DK")
           (map card->card-string deck)))
    (is (= (list
            SA S2 S3 S4 S5 S6 S7 S8 S9 ST SJ SQ SK
            HA H2 H3 H4 H5 H6 H7 H8 H9 HT HJ HQ HK
            CA C2 C3 C4 C5 C6 C7 C8 C9 CT CJ CQ CK
            DA D2 D3 D4 D5 D6 D7 D8 D9 DT DJ DQ DK)
           deck))))

(deftest test-pip-arithmetic
  (testing "incrementing and decrementing pips"
    (is (= :2 (inc-pip :A)))
    (is (= :A (inc-pip :K)))
    (is (= :K (inc-pip :Q)))
    (is (= :3 (inc-pip :2)))
    (is (= :2 (dec-pip :3)))
    (is (= :A (dec-pip :2)))
    (is (= :K (dec-pip :A)))
    (is (= :3 (dec-pip :4)))))

(deftest test-pip-diff
  (testing "pip-diff"
    (is (= 12 (pip-diff SA S2)))
    (is (=  1 (pip-diff S2 SA)))))

(deftest test-cyclic-card-fetching
  (testing "cyclic card fetching"
    (is (= SA (previous-card-cyclic    S2)))
    (is (= DK (previous-card-cyclic    SA)))
    (is (= SA (next-card-cyclic        DK)))
    (is (= S2 (next-card-cyclic        SA)))

    (is (= S2 (previous-in-suit-cyclic S3)))
    (is (= SK (previous-in-suit-cyclic SA)))
    (is (= S2 (next-in-suit-cyclic     SA)))
    (is (= SA (next-in-suit-cyclic     SK)))
    ))

(deftest test-multiplets
  (testing "multiplets"
    (is (not (#'rummy.core/multiplet? [])))
    (is (not (#'rummy.core/multiplet? [CA])))
    (is (not (#'rummy.core/multiplet? [CA C3])))
    (is (not (#'rummy.core/multiplet? [CA CA])))
    (is (not (#'rummy.core/multiplet? [CA H3 C3])))
    (is (not (#'rummy.core/multiplet? [CA HA SA DA DA])))
    (is (#'rummy.core/multiplet? [CA HA]))
    (is (#'rummy.core/multiplet? [CT HT]))
    (is (#'rummy.core/multiplet? [HQ CQ]))
    (is (#'rummy.core/multiplet? [H2 C2]))
    (is (#'rummy.core/multiplet? [H3 C3 D3]))
    (is (#'rummy.core/multiplet? [S4 H4 C4 D4]))))

;; In addition to the hand-written tests above, let's do a statistical test with
;; quick-check.

(defmacro spy-cards [cards]
  `(do (spy (displayable-cards ~cards)) ~cards))

(defn gen-multiplet [min-len max-len]
  (gen/let [len   (gen/choose min-len max-len)
            suits (gen/shuffle suits)
            pip-n (gen/choose 0 (dec +npips+))]
    (let [pip (get pips pip-n)]
      (identity #_spy-cards
       (for [c (range len)]
         (make-card (get suits c) pip))))))

(defspec check-multiplets 100
 (prop/for-all [multi (gen-multiplet 2 4)]
               (#'rummy.core/multiplet? multi)))

(def gen-non-multiplet
  (gen/let [m (gen-multiplet 2 4)]
    (identity #_spy-cards
              (cons (next-card-cyclic (first m)) (rest m)))))

(defspec check-non-muliplets 100
  (prop/for-all [multi gen-non-multiplet]
                (not (#'rummy.core/multiplet? multi))))

(def a-set     [C2 H2 S2])
(def a-non-set [H2 H3 S2])

(deftest test-sets
  (testing "collection of > 2 cards of equal pip-value and different suits is a
  set."
    (is (is-set? a-set))
    (is (is-set? [CA DA HA SA]))
    (is (is-set? [DA HA SA]))
    (is (not (is-set? a-non-set)))
    (is (not (is-set? deck)))
    (is (not (is-set? [])))
    (is (not (is-set? [CA D7 H4])))
    (is (not (is-set? [CA DA])))
    (is (not (is-set? [CA CA SA])))))

(defspec check-sets 100
  (prop/for-all [a-set (gen-multiplet 3 4)]
                (is-set? a-set)))

(defspec check-exact-pairs 100
  (prop/for-all [a (gen-multiplet 2 2)]
                (is-exact-pair? a)))

(deftest test-augments-multiplet?
  (testing "whether cards augment a multiplet"
    (is (augments-multiplet? [CA DA] SA))
    (is (augments-multiplet? [CA DA HA] SA))
    (is (not (augments-multiplet? [CA DA] CA)))
    (is (thrown? java.lang.AssertionError
                 (augments-multiplet? [] DA)))
    (is (thrown? java.lang.AssertionError
                 (augments-multiplet? [CA] DA)))))

;; Because we brought quick-check into the game later, we will add statistical
;; tests but not remove any hand-written tests that existed beforehand.

(def gen-multiplet-augmentation
  (gen/let [m (gen-multiplet 2 4)]
    [m (find-cards-that-complete-a-multiplet m)]))

(defspec check-multiplet-augmentation
  (prop/for-all [[multi cs] gen-multiplet-augmentation]
                (every? is-set? (map #(conj multi %) cs))))

(def a-run [C2 C3 C4])

(deftest test-run
  (testing "sequential collection of cards of same suit is a run."
    (is (is-run? a-run))
    (is (is-run? spades))
    (is (is-run? hearts))
    (is (is-run? clubs))
    (is (is-run? diamonds))
    (is (is-run? [CA C2 C3]))
    (is (is-run? [CA C2 C3 C4]))
    (is (is-run? [CA C2 C3 C4 C5]))
    (is (is-run? [CQ CK CA]))
    (is (is-run? [CJ CQ CK CA]))
    (is (is-run? [CT CJ CQ CK CA]))
    (is (not (is-run? [CK CA C2])))
    (is (not (is-run? [CA C2])))
    (is (not (is-run? [])))
    (is (not (is-run? [CA])))
    (is (not (is-run? deck)))))

(deftest test-pre-run
  (testing "sequential collection of cards is a two-card precursor of a run"
    (is (is-pre-run? [CA C2]))
    (is (is-pre-run? [C2 CA]))
    (is (is-pre-run? [CA CK]))
    (is (not (is-pre-run? [])))
    (is (not (is-pre-run? [CA C2 C3])))
    (is (not (is-pre-run? spades)))))

(defn gen-run-like [min-len max-len]
  (gen/let [len            (gen/choose (dec min-len) (dec max-len)),
            the-suit       (gen/one-of (map gen/return suits))
            bottom-pip-num (gen/choose 0 (- +npips+ len))]
    (reductions (fn [card _] (next-in-suit-cyclic card))
                (make-card the-suit (get pips bottom-pip-num))
                (range len))))

(defspec check-pre-run
  (prop/for-all [rl (gen-run-like 2 2)]
                (is-pre-run? rl)))

(deftest test-run-like
  (testing "run-likes"
    (is (not (#'rummy.core/run-like? [])))
    (is (not (#'rummy.core/run-like? [CA])))
    (is (not (#'rummy.core/run-like? [CA C3])))
    (is (not (#'rummy.core/run-like? [CA CA])))
    (is (#'rummy.core/run-like? [CA C2]))
    (is (#'rummy.core/run-like? [C3 C2]))
    (is (#'rummy.core/run-like? [C3 C2 C4]))
    (is (#'rummy.core/run-like? [C3 C2 C4 C5 C6 C7 C8]))
    (is (#'rummy.core/run-like? [C3 C2 CA]))
    (is (#'rummy.core/run-like? [CK CQ CA]))
    (is (#'rummy.core/run-like? [CK CQ CJ CA]))))

(defspec check-run-likes
  (prop/for-all [rl (gen-run-like 2 +nhand+)]
                #'rummy.core/run-like? rl))

(deftest test-augments-run
  (testing "whether a card completes or augments a run"
    (is (augments-run? [CA C2] C3))
    (is (augments-run? [CA C3] C2))
    (is (augments-run? [CA CK] CQ))
    (is (augments-run? [CK CQ] CA))
    (is (augments-run? [CA CQ] CK))
    (is (augments-run? [CQ CK] CJ))
    (is (augments-run? [CJ CQ CK] CT))
    (is (augments-run? [CJ CQ CK] CA))
    (is (not (augments-run? [CA C2] CK)))
    (is (not (augments-run? [CA C2] CK)))
    (is (not (augments-run? [CK CA] C2)))
    (is (thrown? java.lang.AssertionError
                 (augments-run? [CK C2] CA)))
    (is (thrown? java.lang.AssertionError
                 (augments-run? [CA] C2)))
    ))

(def gen-run-like-augmentation
  (gen/let [m (gen-run-like 2 (dec +nhand+))]
    [m (find-cards-that-complete-a-run-like m)]))

(defspec check-run-like-augmentation-1
  (prop/for-all [[multi cs] gen-run-like-augmentation]
                (every? is-run? (map #(conj multi %) cs))))

(defspec check-run-like-augmentation-2
  (prop/for-all [[multi cs] gen-run-like-augmentation]
                (let [shuffleds
                      (identity #_spy-cards
                       (map shuffle (map #(conj multi %) cs)))]
                  (every? is-run? shuffleds))))

(def gen-inside-straight
  (gen/let [m (gen-run-like 3 +nhand+),
            i (gen/choose 1 (dec (dec (count m))))]
    (let [result (but-nth i m)]
      #_(spy {:i i,
              :m (displayable-cards m),
              :r (displayable-cards result)})
      result)))

(defspec check-completing-inside-straight
  (prop/for-all
   [inside-straight gen-inside-straight]
   #_(spy (displayable-cards inside-straight))
   (let [c (find-card-that-completes-an-inside-straight inside-straight)]
        (is-run? (conj inside-straight c)))
   ))

(deftest find-completing-cards
  (testing "the finding of cards that complete multiplets and runs"
    (is (= #{SA HA} (set (completing-cards  [CA DA]))))
    (is (= #{SA HA} (set (completing-cards #{CA DA}))))
    (is (= #{DA}    (set (completing-cards  [CA SA HA]))))
    (is (= #{DA}    (set (completing-cards #{CA SA HA}))))
    (is (= #{}      (set (completing-cards  [CA SA HA DA]))))
    (is (= #{}      (set (completing-cards #{CA SA HA DA}))))
    (is (= #{S2}    (set (completing-cards  [SA S3]))))
    (is (not (= #{SA HA} (completing-cards #{CA DA}))))
    (is (thrown? java.lang.AssertionError
                 (completing-cards [CA])))
    (is (thrown? java.lang.AssertionError
                 (completing-cards [CA CA])))
    (is (= #{D2 D5} (set (completing-cards [D3 D4]))))
    ))

(deftest test-sorting
  (testing "sorting a hand or a meld"
    (is (= [CA C2 C3]
           (sort-by (comp pip-map :pip)
                    [C3 CA C2])))
    (is (= deck (sort-by card-map (shuffle deck))))))

(deftest test-gaps
  (testing "finding first gaps in sequences"
    (is (= 2 (adjacency-sequence-with-interior-gap? [0 1 2 4])))
    (is (not (adjacency-sequence-with-interior-gap? [4 5 6])))))

(deftest test-inside-straights
  (testing "finding the first inside straights"
    (is (inside-straight?      [CA C2 C4]))
    (is (inside-straight?      [CA C3]))
    (is (inside-straight?      [CA CQ]))
    (is (inside-straight?      [CA C2 C4 C5]))
    (is (inside-straight?      [CA C3 C4 C5]))
    (is (not (inside-straight? [CA C2 C3])))
    (is (not (inside-straight? [CA C2])))))

(deftest test-split-inside-straights
  (testing "the splitting of inside straights"
    (is (= [[SA]    [S3]   ] (split-inside-straight [SA S3])))
    (is (= [[SA]    [S3 S4]] (split-inside-straight [SA S3 S4])))
    (is (= [[SA S2] [S4]   ] (split-inside-straight [SA S2 S4])))
    (is (= [[S2 S3] [S5]   ] (split-inside-straight [S2 S3 S5])))
    (is (= [[S2]    [S4 S5]] (split-inside-straight [S2 S4 S5])))
    (is (= [[SJ]    [SK SA]] (split-inside-straight [SJ SK SA])))
    (is (= [[ST SJ] [SK SA]] (split-inside-straight [ST SJ SK SA])))
    (is (= [[ST SJ] [SK]   ] (split-inside-straight [ST SJ SK])))
    (is (= [[SQ]    [SA]   ] (split-inside-straight [SQ SA])))

    (is (= [[SA] [S3 S4 S5 S6 S7 S8] ] (split-inside-straight [SA S3 S4 S5 S6 S7 S8])))
    (is (= [[SA S2] [S4 S5 S6 S7 S8] ] (split-inside-straight [SA S2 S4 S5 S6 S7 S8])))
    (is (= [[SA S2 S3] [S5 S6 S7 S8] ] (split-inside-straight [SA S2 S3 S5 S6 S7 S8])))
    (is (= [[SA S2 S3 S4] [S6 S7 S8] ] (split-inside-straight [SA S2 S3 S4 S6 S7 S8])))
    (is (= [[SA S2 S3 S4 S5] [S7 S8] ] (split-inside-straight [SA S2 S3 S4 S5 S7 S8])))
    (is (= [[SA S2 S3 S4 S5 S6] [S8] ] (split-inside-straight [SA S2 S3 S4 S5 S6 S8])))

    (is (thrown? java.lang.AssertionError
                 (split-inside-straight [SA C3])))

    (is (thrown? java.lang.AssertionError
                 (split-inside-straight [SK SA S3])))
    (is (thrown? java.lang.AssertionError
                 (split-inside-straight [SK SA S3])))
    (is (thrown? java.lang.AssertionError
                 (split-inside-straight [SA S3 S5])))
    (is (thrown? java.lang.AssertionError
                 (split-inside-straight [SA S2 S5])))
    (is (thrown? java.lang.AssertionError
                 (split-inside-straight [SA S4 S5])))
    ))
