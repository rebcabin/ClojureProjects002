(ns rummy.core
  (:require [clojure.string :as str]
            [clojure.spec   :as s]
            [funcyard.core  :as funcs]
            [clojure.data   :as data])
  (:gen-class))

;;  ___     _               _         _   _
;; |_ _|_ _| |_ _ _ ___  __| |_  _ __| |_(_)___ _ _
;;  | || ' \  _| '_/ _ \/ _` | || / _|  _| / _ \ ' \
;; |___|_||_\__|_| \___/\__,_|\_,_\__|\__|_\___/_||_|

;;; This file is written in anti-literate style: with literate documentation
;;; inside the code.

;;; TODO: Carl Kelso suggested an alternative implementation using bit-vectors.
;;; This should be much faster and could result is significant simplifications.

;;; TODO: After several cigar sessions, this feels really overcomplicated. I
;;; don't see easy ways to simplify it, but simplification must come to top of
;;; mind in some future passes.

;;  ___       __ _      _ _   _
;; |   \ ___ / _(_)_ _ (_) |_(_)___ _ _  ___
;; | |) / -_)  _| | ' \| |  _| / _ \ ' \(_-<
;; |___/\___|_| |_|_||_|_|\__|_\___/_||_/__/

;;; DEFINITIONS

;;; The following definitions pertain. It is surprising that a game as basic as
;;; this rummy should require so many careful definitions. However, such
;;; precision makes the testing and writing of code easier and better.
;;;
;;; suit : A keyword, one of [:S, :H, :C, :D]. Suits are implicitly ordered in
;;; major-minor red-black order common in contract bridge and other card games.
;;;
;;; pip : A keyword, one of #{:A, :2, :3, ..., :9, :T, :J, :Q, :K}, Where :A
;;; means "Ace," :T means "Ten,", :J means "Jack," :Q means "Queen," and :K
;;; means "King;" :2 is sometimes called "deuce" and :3 sometimes called "trey."
;;; Pips are implicitly ordered from Ace to King or deuce to Ace. Both orderings
;;; are allowed. The first ordering ranks Ace lowest in a suit, the second ranks
;;; Ace highest in a suit.
;;;
;;; rank : A number between 0 and 12, mapped to pips by the following
;;; correspondence: [:A :2 :3 :4 :5 :6 :7 :8 :9 :T :J :Q :K]
;;;                 [ 0  1  2  3  4  5  6  7  8  9 10 11 12]
;;; The reason for the apparent off-by-one mapping it to facilitate arithmetic
;;; mod 13 on ranks.
;;;
;;; card : A hash-map with a :suit key, one of #{:S, :H, :D, :C}, and a :pip
;;; key, one of #{:A, :2, :3, ..., :9, :T, :J, :Q, :K}
;;;
;;; card-string : A two-character string like "SA", "D9", and "CK". We provide
;;; functions for round-tripping amongst cards and card strings. Card strings
;;; obey a lexicographic ordering: SA, S2, ..., SQ, SK, HA, ..., HK, CA, ...,
;;; CK, DA, ..., DQ, DK, with suit's being the primary ordering key and pip's
;;; being the secondary ordering key.
;;;
;;; card-number : Honoring the lexicographic ordering of card strings, cards are
;;; mapped to the numbers from 0 to 51 to facilitate arithmetic mod 52.
;;;
;;; deck : A standard collection of fifty-two cards in order.
;;;
;;; pack : A shuffled deck.
;;;
;;; hand : A collection of seven cards drawn at random from a pack.
;;;
;;; deal : a partition of a pack into two hands: "yours" and "theirs," and
;;; a "stock."
;;;
;;; pile : or "discard pile;" a sequence of zero or more cards that generally
;;; grows during a game. In a game amongst humans, the humans must remember the
;;; pile. The human with the most accurate and complete memory has an advantage.
;;; In a game against computers, the computer, of course, has a perfect memory
;;; and therefore an unfair advantage over any imperfect.
;;;
;;; you : one of the two opposing players in a game, usually the one in which a
;;; person (you) makes choices.
;;;
;;; they : one of the two opposing players in a game; generally, a computer
;;; opponent to you.
;;;
;;; turn, play : A transformation in which a player draws a single card from the
;;; top of the stock or pile, optionally melds and/or augments a meld (yours or
;;; theirs) on the table, and then repletes his or her hand from the stock up to
;;; seven cards.
;;;
;;; draw : n. A selection of one or more cards from a pack, stock, or pile. v. To
;;; select one or more cards from a pack, stock, or pile.
;;;
;;; player : one of the two parties (you or they) participating in a match.
;;;
;;; board : A (virtual) playing surface containing the stock, pile, and face-up
;;; melds already played.
;;;
;;; play, choice : A draw of a card from either the stock or pile followed by an
;;; optional meld or optional augmenting of an existing meld, already face-up on
;;; the board.
;;;
;;; meld : n. A collection of three or more cards that is either a run or a set.
;;; A meld belongs to one or the other player. v. To lay down (face up) a meld
;;; during one's turn; entails an increase of the player's points equal to the
;;; number of cards melded (three or more).
;;;
;;; set : (Not to be confused with a mathematical set): A collection of three or
;;; more cards of equal pips (or ranks) and different suits.
;;;
;;; pair : A collection of exactly two cards of equal pips (or ranks) and
;;; different suits. We often say "exact pair" to reduce ambiguity: a pair might
;;; also be (part of) a triplet or quadruplet. Pairs are useful for computing
;;; the value of a holding.
;;;
;;; holding : A player's hand and all the melds face-up on the board. Holdings
;;; are useful for computing the value of a draw and for deciding whether to
;;; draw from the stock (all face down and unknown) or from the pile (all face
;;; up, with the top card available for drawing). All face-up melds are
;;; considered in a holding because a player may augment either his/her own
;;; melds or the opponent's (their) melds.
;;;
;;; multiplet : A set or pair.
;;;
;;; run, straight : A sequence of three or more cards of equal suits and
;;; sequentially adjacent pips (or ranks). Examples: [SA, S2, S3]; [CQ, CK,
;;; CA], [D8, D9, DT, DJ].
;;;
;;; run-like : A sequence of two or more cards of equal suits and sequentially
;;; adjacent pips (or ranks). (Generally, run-likes are useful for computing the
;;; probable value of a draw).
;;;
;;; pre-run : A run-like of length exactly two. Pre-runs are analogous to pairs
;;; and are useful for computing the value of a holding.
;;;
;;; inside straight : A run or straight missing one interior card (Generally,
;;; hoping for a draw that completes an inside straight is a play of low
;;; probability, so the value of an inside straight is relatively low).
;;;
;;; value : A measure of the probable points outcome of a play given all the
;;; cards, that is, in a player's hand or face-up on the board in melds. Also a
;;; measure of the probability that a holding will produce points from a draw
;;; from either the pile or the stock.
;;;
;;; augment : n. A card that augments an existing set or run already face-up on
;;; the board. v. To add one or more cards to a set or run to make a new set or
;;; run; entails an increase of the player's points equal to the number of cards
;;; in the augment.
;;;
;;; match, game : A sequence of turns, beginning with a random choice of you or
;;; they and in which each player begins with zero points, that continues until
;;; either you or they accumulates ten or more points.
;;;
;;; point : A player accumulates one point for every card played to a meld or to
;;; augmenting meld that has already been played.
;;;
;;; score : The current total of all a player's points.
;;;
;;; stock : A shuffled collection of thirty-eight or fewer cards after two hands
;;; have been drawn and zero or more turns have occurred. Because the stock is
;;; in random order, it usually does not matter whether it's an ordered sequence
;;; (vector or list) or whether it's unordered (a mathematical set). If the
;;; stock is depleted during a turn, the pile is picked up, reshuffled, and
;;; becomes the new stock.

;;; Generally, functions whose names end with question mark should return true
;;; or false. Other Boolean-like functions may return truthy or falsey. TODO: We
;;; are not scrupulous about this distinction.

;;; Generally, functions whose names are nouns denote pure functions. Functions
;;; whose names are verbs perform side effects. TODO: We are not scrupulous
;;; about this rule.

;;; TODO: We are not careful (yet) about which functions are private and which
;;; are public. It does not matter much because this is an app and not a
;;; library, but if we later split the namespaces it will become a matter of
;;; hygiene.

;;   __                               _
;;  / _|_  _ _ _  __ _  _ __ _ _ _ __| |
;; |  _| || | ' \/ _| || / _` | '_/ _` |
;; |_|  \_,_|_||_\__|\_, \__,_|_| \__,_|
;;                   |__/

;;; FUNCYARD

;;; TODO: Candidates for funcyard.

;;; TODO: Make running-stats into transducers.

(defn running-mean
  ([]
   {:mean 0, :count 0})
  ([{:keys [mean count]} new-datum]
   (let [new-count (inc count)]
     {:mean  (+ (/ new-datum new-count) (* mean (/ count new-count)))
      :count new-count})))

(defn running-stats
  ([]
   {:mean 0, :count 0, :ssr 0, :variance 0, :std-dev 0})
  ([{:keys [ssr mean count variance std-dev] :as ostats} new-datum]
   (let [nrmean   (running-mean ostats new-datum),
         nssr     (+ ssr (* (- new-datum (:mean ostats))
                            (- new-datum (:mean nrmean)))),
         ncount   (:count nrmean),
         nvar     (if (> ncount 1), (/ nssr (dec ncount)), 0.0)
         nstd-dev (Math/sqrt nvar)]

     {:ssr      (double nssr),
      :mean     (double (:mean nrmean)),
      :count    (:count nrmean),
      :variance (double nvar),
      :std-dev  nstd-dev})))

(defn seive-numeric [hashmap]
  "Keep only numeric fields in the given hashmap."
  (into {} (filter (comp number? second) hashmap)))

(defn statsmap-numeric [hashmap]
  "Convert the given hashmap into a statsmap: where each numeric field has the
  form needed for running stats."
  (into {}
        (map (fn [[k v]]
               [k (running-stats
                   (running-stats)
                   v)])
             (seive-numeric hashmap))))

(defn accum-numeric [statsmap hashmap]
  (into {}
        (map (fn [[k v]]
               [k (running-stats
                   (k statsmap)
                   (k hashmap))])
             (seive-numeric hashmap))))

(defn mapvals [fun a-map]
  (let [ks (keys a-map), vs (vals a-map)]
    (apply (partial assoc {})
           (interleave ks (map fun vs)))))

(defn inc-at [m k]
  (assoc m k (inc (k m))))

(defn inc-at-when [m k b]
  (if b, (inc-at m k), m))

(defn conj-at [m k i]
  (assoc m k (conj (k m) i)))

(defn conj-at-when [m k i b]
  (if b, (conj-at m k i), m))

(defn add-at [m k v]
  (assoc m k (+ (k m) v)))

(defn but-nth [n coll]
  "From https://goo.gl/StEFX3."
  (keep-indexed #(if (not= %1 n) %2) coll))

(defn adjacency-sequence? [integers]
  {:pre [(seq integers), (every? integer? integers)]}
  (if (= 1 (count integers))
    true
    (every? (partial = 1) (funcs/pairwise - integers))))

(defn jump-suffix [integers]
  "Splits a sequence of integers at the first element where the pairwise
  difference is not unity and returns the second part of the split (TODO:
  generalize). Alternatively, computes the discrete derivative of a sequence of
  integers and splits the inputs at the first value of the derivative that is
  not unity, returning the second element of the split."
  {:pre [(<= 2 (count integers)), (every? integer? integers)]}
  (->> integers
       (funcs/pairwise -)
       (split-with (partial = 1))
       ;; The following composition returns truthy when the second split has at
       ;; least one member, falsey otherwise. When falsey, there is no jump.
       (second)))

(defn integrate-sequence
  ([nums]
   {:pre [(every? integer? nums) (every? (complement neg?) nums)]}
   (integrate-sequence (first nums) (rest nums)))
  ([n0 nums]
   {:pre [(every? integer? nums) (every? (complement neg?) nums) number? n0]}
   (reductions + n0 nums)))

(defn adjacency-sequence-with-interior-gap? [integers]
  "Finds the width of the first interior gap in a sequence, or nil if there is
  no gap."
  (->> integers jump-suffix (first)))

(defn adjacency-sequence-with-one-interior-gap? [integers]
  "Finds the width of the first interior gap in a sequence, checking that it is
  the only gap, or nil if there is no gap."
  (let [[first-gap & more] (jump-suffix integers)]
    (when (every? (partial = 1) more)
      first-gap)))

;;; TODO: The following functions supporting modular arithmetic are restricted
;;; to the natural numbers including zero. That's probably too restrictive, and
;;; we should think about it when promoting these to the funcyard.

(defn clock-like [num modulus]
  {:pre [(and (integer? num)     (not (neg? num))
              (integer? modulus) (not (neg? modulus)))]}
  "Replaces zero with the modulus, supporting clock-like integers in the ring
  modulo the modulus."
  (if (= 0 num) modulus num))

(defn unary-mod [func]
  (fn [num modulus]
    {:pre [(and (integer? num)     (not (neg? num))
                (integer? modulus) (not (neg? modulus)))]}
    (mod (func num) modulus)))

(def inc-mod (unary-mod inc))

(def dec-mod (unary-mod dec))

(defn binary-mod [func]
  (fn [num1 num2 modulus]
    {:pre [(and (integer? num1)    (not (neg? num1))
                (integer? num2)    (not (neg? num2))
                (integer? modulus) (not (neg? modulus)))]}
    (mod (func num1 num2) modulus)))

(def diff-mod (binary-mod -))

(def add-mod  (binary-mod +))

;; TODO: use http://funcool.github.io/cats/latest/ or
;; https://github.com/uncomplicate/fluokitten instead of this mapf.

(defn mapf [f tree]
  (if (or (vector? tree) (seq? tree))
    (if (seq tree)
      (cons (mapf f (first tree)) (mapf f (rest tree)))
      ())
    (f tree)))

;;   ___             _
;;  / __|__ _ _ _ __| |___
;; | (__/ _` | '_/ _` (_-<
;;  \___\__,_|_| \__,_/__/

;;; CARDS

(def +nsuits+  4)
(def +npips+  13)
(def +nhand+   7)
(def +ndeck+  (* +nsuits+ +npips+))

(def suits [:S :H :C :D])
(def pips  [:A :2 :3 :4 :5 :6 :7 :8 :9 :T :J :Q :K])
(def deck  (vec (flatten (for [s suits] (for [p pips] {:suit s :pip p})))))

;;; TODO: Abstract string and hash-map representations into a protocol.

(def spades   (for [p pips] {:suit :S :pip p}))
(def hearts   (for [p pips] {:suit :H :pip p}))
(def clubs    (for [p pips] {:suit :C :pip p}))
(def diamonds (for [p pips] {:suit :D :pip p}))

(def pip-map  (apply hash-map (interleave pips  (range +npips+))))
(def suit-map (apply hash-map (interleave suits (range +nsuits+))))
(def card-map (apply hash-map (interleave deck  (range +ndeck+))))

(defn pip-nums  [cards] (map (comp pip-map  :pip)  cards))
(defn suit-nums [cards] (map (comp suit-map :suit) cards))

(defn make-card [suit pip] {:suit suit :pip pip})

(defn pip-diff [card1 card2]
  (diff-mod (pip-map (:pip card1)) (pip-map (:pip card2)) +npips+))

(defn inc-pip [pip]
  (get pips (inc-mod (pip-map pip) +npips+)))

(defn add-pip [pip num]
  {:pre [(pip-map pip) (integer? num)]}
  (get pips (add-mod (pip-map pip) num +npips+)))

(defn dec-pip [pip]
  (get pips (dec-mod (pip-map pip) +npips+)))

(defn next-card-cyclic [card]
  (get deck (inc-mod (card-map card) +ndeck+)))

(defn previous-card-cyclic [card]
  (get deck (dec-mod (card-map card) +ndeck+)))

(defn next-in-suit-cyclic [card]
  (let [{:keys [suit pip]} card]
    (make-card suit (inc-pip pip))))

(defn previous-in-suit-cyclic [card]
  (let [{:keys [suit pip]} card]
    (make-card suit (dec-pip pip))))

;;  ___                              _        _   _
;; | _ \___ _ __ _ _ ___ ___ ___ _ _| |_ __ _| |_(_)___ _ _  ___
;; |   / -_) '_ \ '_/ -_|_-</ -_) ' \  _/ _` |  _| / _ \ ' \(_-<
;; |_|_\___| .__/_| \___/__/\___|_||_\__\__,_|\__|_\___/_||_/__/
;;         |_|

;;; REPRESENTATIONS

;; TODO: Because of https://goo.gl/aYf7BS, it might be better to refactor the
;; base representation of cards from hash-maps into records.

(defn card-string->card [s]
  "Make card hash-maps like {:suit :D, :pip :9} from strings like \"D9\"."
  {:pre [(= 2 (count s)), (= (class s) java.lang.String)]}
  (apply hash-map (interleave
                   [:suit :pip]
                   (map (comp keyword str) (seq s)))))

(defn card->card-string [c]
  "Make card strings like \"D9\" from card hash-maps like {:suit :D, :pip :9}."
  {:pre [(= (class c) clojure.lang.PersistentArrayMap)
         (= (set (keys c))  #{:suit :pip})]}
  (let [suit (second (str (:suit c)))
        pip  (second (str (:pip  c)))]
    (str/join [suit pip])))

(defn permissive-card->card-string [c]
  (if (and (= (class c) clojure.lang.PersistentArrayMap)
           (= (set (keys c)) #{:suit :pip})),
    (card->card-string c), c))

(defn permissive-card->card-symbol [c]
  (if (and (= (class c) clojure.lang.PersistentArrayMap)
           (= (set (keys c)) #{:suit :pip})),
    (symbol (card->card-string c)), c))

;;; The following set up handy symbols like CA = {:suit :C, :pip :A}, D7, SK.
;;; See https://goo.gl/Oy4Uxc.

(defn bind-card-fn [card]
  `(def ~(symbol (card->card-string card)) ~card))

(defmacro bind-cards [] `(do ~@(map bind-card-fn deck)))

(bind-cards)

;;; After all these symbols have been defined, we might implement
;;; card-string->card as (comp var-get resolve symbol card-string).

;;  ___      _   _
;; | _ \__ _| |_| |_ ___ _ _ _ _  ___
;; |  _/ _` |  _|  _/ -_) '_| ' \(_-<
;; |_| \__,_|\__|\__\___|_| |_||_/__/

;;; PATTERNS

(defn- no-suits-equal? [cards]
  (not (funcs/has-duplicates (map :suit cards))))

(defn- multiplet? [cards]
  "A 'multiplet' is 2, 3, or 4 cards of the same pip value and different suits."
  (and (<= 2 (count cards))
       (apply = (map :pip cards))
       ;; The following check eliminates duplicated cards
       (no-suits-equal? cards)))

(defn is-set? [cards]
  "Collection of 3 or more cards of equal pip values and different suits is a
  \"set.\" The check for different suits is redundant when a candidate set is
  drawn from a single deck, but this routine will handle more general cases
  where the well contains more than one deck."
  (and (>= (count cards) 3) (multiplet? cards)))

(defn is-exact-pair? [cards]
  "An exact pair is 2 cards of the same pip values that is not also a triplet or
  quadruplet, etc. It is a precursor of a set and useful for computing the
  probable value of the next draw from the stock or the (discard) pile."
  (and (= (count cards) 2) (multiplet? cards)))

(defn is-pre-set? [cards]
  "A pre-set is a multiplet of length 2 or 3. It's the homologue of a pre-run."
  (let [c (count cards)]
    (and (or (= c 2) (= c 3))
         multiplet? cards)))

(defn find-cards-that-complete-a-multiplet [cards]
  "Find a set of cards that will complete or augment a multiplet."
  {:pre [(multiplet? cards)]}
  (let [remaining-suits (clojure.set/difference
                         (set suits)
                         (set (map :suit cards))),
        the-pip (:pip (first cards))]
    (map make-card remaining-suits (repeat the-pip))))

(defn- run-like? [cards]
  "A run-like is 2 or more cards of the same suit in a sequence. Ace may be the
  first card in a run-like that continues with 2, 3, ... Ace may be the last
  card in a run-like that begins with ..., Q, K. Run-likes may not cycle through
  Ace, meaning ..., K, A, 2, ... is not a run-like."
  (when (<= 2 (count cards))
    (apply = (map :suit cards))
    (let [sorted-cards (sort-by card-map cards)]
      (if (and (= :A (:pip (first sorted-cards)))
               (= :K (:pip (last  sorted-cards))))
        (adjacency-sequence? (pip-nums (drop 1 sorted-cards)))
        (adjacency-sequence? (pip-nums         sorted-cards))))))

(defn find-cards-that-complete-a-run-like [cards]
  {:pre [(run-like? cards)]}
  (let [the-suit     (:suit (first cards)),
        sorted-cards (let [sorted- (sort-by card-map cards)]
                       (if (and (= :A (:pip (first sorted-)))
                                (= :K (:pip (last  sorted-))))
                         (funcs/cycle-left sorted-)
                         sorted-))
        top-pip      (:pip (last sorted-cards)),
        bottom-pip   (:pip (first sorted-cards))]
    (cond
      (= bottom-pip :A) [(make-card the-suit (inc-pip top-pip))]
      (= top-pip    :A) [(make-card the-suit (dec-pip bottom-pip))]
      true              [(make-card the-suit (dec-pip bottom-pip))
                         (make-card the-suit (inc-pip top-pip))])))

(defn inside-straight? [cards]
  "Finds the first run of length 2 or greater missing a single card."
  (when (and (<= 2 (count cards))
             (apply = (map :suit cards)))
    (let [nums (->> cards (sort-by pip-map) (pip-nums))]
      (or (->> nums
               (adjacency-sequence-with-one-interior-gap?)
               (= 2))
          ;; The following accounts for high aces.
          (->> (map #(clock-like % +npips+) nums)
               sort
               (adjacency-sequence-with-one-interior-gap?)
               (= 2))))))

(defn split-inside-straight [cards]
  {:pre [(inside-straight? cards)]}
  (let [f (first cards),
        suit (:suit f),
        prior (previous-in-suit-cyclic f),
        offset (pip-map (:pip prior))]
    (->> (cons prior cards)
         (funcs/pairwise #(assoc %1 :delta (pip-diff %1 %2)))
         (split-with #(= 1 (:delta %)))
         (funcs/map-down-one #(dissoc % :delta)))))


(defn find-card-that-completes-an-inside-straight [cards]
  (let [split (split-inside-straight cards)
        bottom (last (first split))]
    (next-in-suit-cyclic bottom)))

;; is-run? --- In the current implementation, A is mapped to zero, so it's
;; always at the bottom of a sort when present. For the special case of Q K A,
;; it suffices to check that A is at the bottom of a sort, that K is at the top,
;; and that the rest of the cards, if any, in the candidate run are a sequence.

(defn is-run? [cards]
  "A run is 3 or more cards of the same suit and of adjacent and sequential pip
  values, with special consideration for the Ace. A run may begin with Ace as
  the low card, in which case it is A 2 3, or a run may end with Ace as the high
  card, in which case it is Q K A. An Ace may not be an interior card of a run."
  (and (> (count cards) 2) (run-like? cards)))

(defn is-pre-run? [cards]
  "A pre-run is exactly two cards in a sequence. It's the homologue of an exact
  pair."
  (and (= (count cards) 2) (run-like? cards)))

(defn hand-stock-split [cards]
  (->> cards
       shuffle
       (split-at +nhand+)))

(defn deal []
  (let [[first-hand  pre-stock] (hand-stock-split deck),
        [second-hand stock    ] (hand-stock-split pre-stock),
        pile                    []]
    [first-hand second-hand stock pile]))

(defn- suit-clump [hand]
  (->> hand
       (sort-by card-map)
       (partition-by :suit)))

(defn- suit-filtered [pred hand]
  (->> hand
       suit-clump
       (filter pred)))

(defn runs             [hand] (suit-filtered is-run?          hand))
(defn run-likes        [hand] (suit-filtered run-like?        hand))
(defn pre-runs         [hand] (suit-filtered is-pre-run?      hand))
(defn inside-straights [hand] (suit-filtered inside-straight? hand))

(defn- pip-clump [hand]
  (->> hand
       (sort-by card-map)
       (sort-by :pip)
       (partition-by :pip)))

(defn- pip-filtered [pred hand]
  (->> hand
       pip-clump
       (filter pred)))

(defn sets         [hand] (pip-filtered is-set?        hand))
(defn exact-pairs  [hand] (pip-filtered is-exact-pair? hand))
(defn pre-sets     [hand] (pip-filtered is-pre-set?    hand))
(defn multiplets   [hand] (pip-filtered multiplet?     hand))

;;; Consider a hand of seven cards, and all the other cards split into the
;;; opponent's hand of seven cards and a stock of thirty-eight cards. Given two
;;; hands, the population of stocks is the number of different ways of picking
;;; thirty-eight cards from forty-five, which are the remaining cards after
;;; picking one hand. This number is Binomial[45, 7] = 45! / 7! / 38! =
;;; 45,379,620, which is the same as the number of opponent's hands ---
;;; Binomial[45, 38] = 45! / 38! / 7!.

(def +nstocks+ (reduce * (range 39 46)))

;;; Imagine a two-card exact pair, say SA and HA. What are the chances that the
;;; next card on the stock will fill out the exact pair and make it a set? There
;;; are two such cards, CA and DA. There are 45 cards concealed in the
;;; opponent's hand and in the stock. Any one of them may be CA. The chances
;;; that CA is in the opponent's hand are 7/45 and the chances that CA is in the
;;; stock are 38/45. These are disjoint events (the card may not be in both), so
;;; their total probability adds up to one. The conditional probability that the
;;; top card of the stock is CA given that CA is in the stock is 1/38. Therefore
;;; (by Bayes), the total probability that the top card of the stock is CA is
;;; 1/38 * 38/45 = 1/45. We conclude that the split of the 45 remaining cards
;;; into the stock and into the opponent's hand does not matter. Likewise, the
;;; chances that the top card of the stock is DA is 1/45. The chances that the
;;; top card of the stock is either CA or DA is 2/45 = 0.04444... This is the
;;; conditional probability that the top card of the stock completes a set given
;;; two cards of the set.

;;  ___ _           _      _   _
;; / __(_)_ __ _  _| |__ _| |_(_)___ _ _  ___
;; \__ \ | '  \ || | / _` |  _| / _ \ ' \(_-<
;; |___/_|_|_|_\_,_|_\__,_|\__|_\___/_||_/__/

;;; SIMULATIONS

;;; Check that a hand has an exact pair. If so, draw the top card of the stock
;;; and increase the set-count if it completes the set.

(defn completes-set [exact-pair card]
  (is-set? (conj exact-pair card)))

(defn completing-a-set [n-hands]
  {:pre [(> n-hands 0)]}
  (loop [i 0,
         score {:n-hands-with-exact-pairs   0,
                :n-completed-sets           0,
                :n-multiplets               0,
                :total-hands                0,
                ; :exact-pairs             [],
                ; :completed-sets          [],
                :cond-prob-completed      0.0,
                :cond-prob-an-exact-pair  0.0}]
    (if (>= i n-hands)
      (-> score
          (add-at :cond-prob-completed
                  (/ (:n-completed-sets score)
                     (:n-hands-with-exact-pairs score)))
          (add-at :cond-prob-an-exact-pair
                  (/ (:n-hands-with-exact-pairs score)
                     (:total-hands score))))
      (let [[hand _ stock _] (deal)
            exact-pair       (first (exact-pairs hand))
            multiplet        (first (multiplets hand))
            draw             (first stock)
            triple           (conj exact-pair draw)
            completed?       (is-set? triple)]
        (recur (inc i),
               (-> score
                   (inc-at       :total-hands)
                   (inc-at-when  :n-multiplets             multiplet)
                   (inc-at-when  :n-hands-with-exact-pairs exact-pair)
                   (inc-at-when  :n-completed-sets         completed?)
                   #_(conj-at-when :exact-pairs p p)
                   #_(conj-at-when :completed-sets triple completed?)))))))



;;; A simulation of 1,000,000 hands produces the following:
;;;
;;;     {:n-hands-with-exact-pairs 737714,
;;;      :n-completed-sets 32772,
;;;      :n-multiplets 788764,
;;;      :total-hands 1000000,
;;;      :cond-prob-completed 0.04442371976131672,
;;;      :cond-prob-an-exact-pair 0.737714}
;;;
;;; The measured cond-prob of a completed set is 0.04442, close to the
;;; theoretical value of 0.04444... Back-of-the-envelope statistics predict that
;;; the measured number of completed sets should be within one sigma of the
;;; theoretical number about 68.2 percent of the time. One sigma is about the
;;; square root of 737,714. The theoretical number is (* 0.04444... 737714) or
;;; 32787.2888... The square root of 737,714 is about 859, so our measured
;;; number is plausible.
;;;
;;; Because square root of 1,000,000 is 1,000, back-of-the-envelope statistics
;;; predict that the cond-prob of a hand with at least one exact pair will be
;;; 737,714 +/- 1,000. What is the theoretical number?
;;;
;;; We want the number of ways of drawing 7 cards from 52 such that there is at
;;; least one exact pair, i.e., collection of exactly two with identical pips?
;;;
;;; It's easy to count how many hands have no multiplets, that is, collections
;;; with no repeated pips: 13 choices for the first pip, then 12, and so on down
;;; to 7, multiplied by four suit choices for each card, i.e., 4^7, and divided
;;; by the number of ways of ordering such a hand, namely by 7!. That number is
;;; 4^7 * Binomial[13, 7] = 28,114,944. The fraction of hands that have any
;;; multiplet must be 1 - (4^7 Binomial[13, 7])/Binomial[52, 7] = 508,027 /
;;; 643,195, which is about 0.789,849, easily within one sigma (0.001,000) of
;;; the empirical number, 0.788,764.
;;;
;;; The correct argument for the number of hands with at least one exact pair
;;; appears here: https://goo.gl/f0821U. The answer is 0.738,817, a little more
;;; than one sigma away from the empirical number 0.737,714, and therefore
;;; entirely plausible. Looks like we have our math right so far.

;;    _             _         _
;;   /_\  _ _  __ _| |_  _ __(_)___
;;  / _ \| ' \/ _` | | || (_-< (_-<
;; /_/ \_\_||_\__,_|_|\_, /__/_/__/
;;                    |__/

;;; ANALYSIS

;;; What is the average number of multiplets in a hand? If we are dealt a hand
;;; with fewer than this number, we might request a re-deal (rules of Royal
;;; Rummy from King games permit one re-deal at a certain price).

(defn- average-number-selected [n-hands selector]
  (+ 0.0 (/ (reduce + (map (comp count selector first)
                           (repeatedly n-hands deal)))
            n-hands)))

(defn average-number-of-multiplets [n-hands]
  (average-number-selected n-hands multiplets))

;;; A simulation of 1,000,000 hands takes a few seconds and yields 1.0762, which
;;; is probably good to four significant figures by head-math square-root
;;; statistics.

;;; What is the distribution of lengths of run-likes?

(defn- tally-of-selecteds-lengths [n-hands selector]
  (let [sample (->> (repeatedly n-hands deal)
                    (map (comp selector first))
                    (filter #(not (empty? %)))) ;; I know this is frowned upon.
        tally (->> sample
                   (map #(map count %))
                   flatten
                   funcs/tally)]
    tally))

(defn tally-of-run-like-lengths [n-hands]
  (tally-of-selecteds-lengths n-hands run-likes))

;;; A simulation of 1,000,000 hands, also taking less than a minute, yields the
;;; following distribution:
;;;
;;;     {2 223001, 3 29378, 4 3010, 5 224, 6 15}
;;;
;;; In about 22 percent of deals, we should expect a pre-run of length 2. These,
;;; and all other run-likes can overlap with multiplets, of course. A run-like
;;; that overlaps a multiplet gives a player multiple possibilities for
;;; completing a meld and increases the value of the combination.
;;;
;;; The tally above is consistent with the average number of run-likes in a
;;; hand, revealed by the following to be around 0.2567.

(defn average-number-of-run-likes [n-hands]
  (average-number-selected n-hands run-likes))

;;; Remarkably, we get a run of 6 dealt in 15 cases out of a million. A
;;; simulation ten million produced the following statistics in less than five
;;; minutes.
;;;
;;;     {2 2238093, 3 295148, 4 30113, 5 2220, 6 96, 7 2}
;;;
;;; with 9.6 runs of length six per million (and 0.2 runs of length 7 per
;;; million, to boot.
;;;
;;; A simulation of 1 billion hands (on two cores) takes about 8 hours and
;;; yields the following:
;;;
;;;     rummy.core> (time (doall (pmap (fn [_] (tally-of-run-like-lengths
;;;                                              500000000)) (range 2))))
;;;     "Elapsed time: 2.7645468274186E7 msecs"
;;;
;;;     ({2 111903420, 3 14752718, 4 1503675, 5 111142, 6 5145, 7 132}
;;;      {2 111891431, 3 14761718, 5 111188, 4 1501218, 6 5325, 7 102})
;;;
;;; We can aggregate it like this (map (fn [[k1 v1] [k2 v2]] [[k1 k2] (+ v1 v2)])
;;;
;;; ([[2 2] 223794851]
;;;  [[3 3] 29514436]
;;;  [[4 5] 1614863]
;;;  [[5 4] 1612360]
;;;  [[6 6] 10470]
;;;  [[7 7] 234])
;;;
;;; giving nicer statistics.
;;;
;;; A tally of 1,000,000 multiplets reveals the following distribution:
;;;
;;;     {2 997906, 3 75542, 4 1667}
;;;
;;; This adds up to more than a million because lots of hands have more than one
;;; multiplet. How many?

(defn- tally-of-selecteds [n-hands selector]
  (let [tally (->> (repeatedly n-hands deal)
                   (map (comp selector first))
                   (map count)
                   funcs/tally)]
    {:tally tally, :sum (reduce + (vals tally))}))

;;; A simulation of a million tallies up like this:
;;;
;;;     {:tally {3 19177, 1 523083, 0 210145, 2 247595}, :sum 1000000}
;;;
;;; The inner product of the tally tells us how many multiplets there were all
;;; together:
;;;
;;;     (reduce +
;;;       (map (partial apply *)
;;;            (into [] {3 19177, 1 523083, 0 210145, 2 247595})))
;;;
;;; ~~> 1,075,804
;;;
;;; That's a 7.6 percent overage, about what we saw above.
;;;

;;  _                                         _
;; | |   __ _ ____  _   __ _ __ _ _ __  ___  | |_ _ _ ___ ___
;; | |__/ _` |_ / || | / _` / _` | '  \/ -_) |  _| '_/ -_) -_)
;; |____\__,_/__|\_, | \__, \__,_|_|_|_\___|  \__|_| \___\___|
;;               |__/  |___/

;;; LAZY GAME TREE

;;; See https://www.amazon.com/Land-Lisp-Learn-Program-Game/dp/1593272812.

(defn initial-game-state []
  (let [[yours theirs stock pile] (deal)]
    {:player       :you   ;; (random-sample [:you :they])
     :stock        stock
     :your-hand    yours
     :their-hand   theirs
     :pile         pile
     :your-melds   []
     :your-points   0
     :your-plays   []
     :their-melds  []
     :their-plays  []
     :their-points  0
     :next-states  ()}))


(defn displayable-cards [cards]
  (mapf permissive-card->card-symbol cards))

(defn displayable-state [game-state]
  (mapvals displayable-cards game-state))

(defn classified-hand [hand]
  {:sets                (sets             hand)
   :pre-sets            (pre-sets         hand)
   :exact-pairs         (exact-pairs      hand)
   :multiplets          (multiplets       hand)

   :runs                (runs             hand)
   :pre-runs            (pre-runs         hand)
   :run-likes           (run-likes        hand)
   :inside-straights    (inside-straights hand)

   :set-completers      (map find-cards-that-complete-a-multiplet
                             (multiplets hand))
   :run-completers      (map find-cards-that-complete-a-run-like
                             (run-likes hand))
   :straight-completers (map find-card-that-completes-an-inside-straight
                             (inside-straights hand))

   :hand                (sort-by card-map hand)})


(defn score-missing-card [card theirs stock pile]
  (let [s (count stock)]
    (cond
      (= card (first pile))               1,
      (some (partial = card) (rest pile)) 0,
      (= 0 s)                             0,
      true                                (/ (double 1) s))))

(defn score-missing-cards [hand theirs stock pile]
  (reduce (fn [total card]
            (+ total (score-missing-card card theirs stock pile))),
          0, hand))

(defn score-completers [key classified-hand theirs stock pile]
  (-> (flatten (key classified-hand))
      (score-missing-cards theirs stock pile)))

(defn score-set-completers [classified-hand theirs stock pile]
  (score-completers :set-completers classified-hand theirs stock pile))

(defn score-run-completers [classified-hand theirs stock pile]
  (score-completers :run-completers classified-hand theirs stock pile))

(defn score-straight-completers [classified-hand theirs stock pile]
  (score-completers :insight-straight-completers classified-hand theirs stock pile))

(defn scored-hand [hand theirs stock pile]
  (let [ch  (classified-hand hand),
        sc  (score-set-completers      ch theirs stock pile),
        rc  (score-run-completers      ch theirs stock pile),
        ic  (score-straight-completers ch theirs stock pile),
        ns  (count (:sets              ch)),
        nr  (count (:runs              ch)),
        ni  (count (:inside-straights  ch)),
        npr (count (:pre-runs          ch)),
        nps (count (:pre-sets          ch)),
        nm  (+ ns nr),
        sm  (+ (* 1.0 (+ ns nr))),
        tc  (+ sc rc ic sm)] ; inaccurate heuristic because events aren't independent

    (assoc ch
           :score-set-completers       sc,
           :score-run-completers       rc,
           :score-straight-completers  ic,
           :number-of-meldables        nm,
           :number-of-sets             ns,
           :number-of-runs             nr,
           :number-of-inside-straights ni,
           :number-of-pre-runs         npr,
           :number-of-pre-sets         nps,
           :score-meldables            sm,
           :total-score                tc,)))


(defn displayable-hand [hand]
  (into (sorted-map) (mapvals displayable-cards hand)))

;;  ___          _           _   _             _        _    _ _
;; | __|_ ____ _| |_  _ __ _| |_(_)_ _  __ _  | |_  ___| |__| (_)_ _  __ _ ___
;; | _|\ V / _` | | || / _` |  _| | ' \/ _` | | ' \/ _ \ / _` | | ' \/ _` (_-<
;; |___|\_/\__,_|_|\_,_\__,_|\__|_|_||_\__, | |_||_\___/_\__,_|_|_||_\__, /__/
;;                                     |___/                         |___/

;;; EVALUATING HOLDINGS

(defn augments-multiplet? [a-multiplet card]
  {:pre [(multiplet? a-multiplet)]}
  (is-set? (conj a-multiplet card)))

(defn augments-run? [a-runnish card]
  {:pre [(or (run-like? a-runnish) (inside-straight? a-runnish))]}
  (is-run? (conj a-runnish card)))

(defn completing-cards [precursor]
  {:pre [(or (multiplet?       precursor)
             (run-like?        precursor)
             (inside-straight? precursor))]}
  (cond
    (multiplet? precursor)
    (find-cards-that-complete-a-multiplet precursor)

    (run-like? precursor)
    (find-cards-that-complete-a-run-like precursor)
   ,
    (inside-straight? precursor)
    [(find-card-that-completes-an-inside-straight precursor)]))

(defn the-hand [game-state]
  (if (= (:player game-state) :you)
    (:your-hand  game-state)
    (:their-hand game-state)))

(defn evaluate-card [game-state card]
  (let [hand             (the-hand game-state)
        multis           (multiplets hand)
        runlikes         (run-likes hand)
        inside-straights (inside-straights hand)]
    (lazy-seq nil)))

(defn hand-statistics [n-hands]
  {:pre [(> n-hands 0)]}
  (let [one (deal),
        two (statsmap-numeric (apply scored-hand one))]
    (reduce accum-numeric two (repeatedly (dec n-hands)
                                          #(apply scored-hand (deal))))))
;;  ___ _
;; | _ \ |__ _ _  _ ___
;; |  _/ / _` | || (_-<
;; |_| |_\__,_|\_, /__/
;;             |__/

;;; PLAYS

(defn other-player [player]
  (if (= player :you) :they :you))

(defn draw-from-stock [stock pile] "~~> [card new-stock new-pile"
  {:pre [(or (seq stock) (or (seq pile)))]}
  (if (seq stock)
    [  (first stock) (rest stock) pile]
    (let [stock (seq (shuffle pile)), pile  ()]
      [(first stock) (rest stock) pile])))

(defn smallest-play [game-state card]
  {:pre [(some #(= card %) (the-hand game-state))]}
  (let [[new-card new-stock new-pile] (draw-from-stock
                                       (:stock game-state)
                                       (:pile  game-state)),
        hand (the-hand game-state)
        ;; Don't care whether the new card goes at the beginning or at the end
        ;; of the new hand, that is, whether "hand" is a vector or seq; "conj"
        ;; is unconditionally cool here.
        new-hand (conj (remove #(= card %) hand) new-card)]
    (assoc
     (if (= (:player game-state) :you)
       (assoc game-state
              :your-hand  new-hand
              ;; Make sure play collections are vectors because we want new
              ;; plays to conj at the ends. See https://goo.gl/gDHzu4.
              :your-plays  (vec (conj (:your-plays  game-state) :smallest-play)))
       (assoc game-state
              :their-hand new-hand
              :their-plays (vec (conj (:their-plays game-state) :smallest-play))))
     :player (other-player (:player game-state))
     :stock  new-stock
     ;; Make sure "card" goes to the top of the pile
     :pile   (cons card new-pile))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
