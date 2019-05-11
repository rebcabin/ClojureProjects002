(ns nrepl-bug.core
  (:require [clojure.zip             :as zip    ]
            clojure.string
            clojure.pprint
            [rx.lang.clojure.interop :as rx     ]
            )
  (:use     [clojail.core            :only [sandbox]]
            [clojail.testers         :only [blacklist-symbols
                                            blacklist-objects
                                            secure-tester
                                            ]])
  (:import [rx
            Observable
            Observer
            subscriptions.Subscriptions
            subjects.Subject
            subjects.PublishSubject])
  )

(defmacro catchless-pdump
  "Monitoring and debugging macro with semantics of 'identity', for use
   where 'catch' is not allowed."
  [x]
  `(let [x#  ~x]
     (do (println "----------------")
         (clojure.pprint/pprint '~x)
         (println "~~>")
         (clojure.pprint/pprint x#)
         x#)))

;;;   ___                  _       ___  _
;;;  / __|___ _ _  ___ _ _(_)__   / _ \| |__ ___ ___ _ ___ _____ _ _
;;; | (_ / -_) ' \/ -_) '_| / _| | (_) | '_ (_-</ -_) '_\ V / -_) '_|
;;;  \__/\___|_||_\___|_| |_\__|  \___/|_.__/__/\___|_|  \_/\___|_|
;;;

(defn- or-default
  "Fetch first optional value from function arguments preceded by &."
  [val default] (if val (first val) default))

(defn subscribe-collectors
  "Subscribe asynchronous collectors to any observable; produce a map
   containing a :subscription object for .unsubscribing and a :reporter
   function for retrieving values from the collectors. Default wait-time
   is 1 second until timeout."
  [obl & optional-wait-time]
  (let [wait-time (or-default optional-wait-time 1000)
        onNextCollector      (agent [])  ; seq of all values sent
        onErrorCollector     (atom  nil) ; only need one value
        onCompletedCollector (promise)]  ; can wait on another thread
    (let [collect-next
          (rx/action [item] (send onNextCollector
                                  (fn [state] (conj state item))))
          collect-error
          (rx/action [excp] (reset! onErrorCollector excp))

          collect-completed
          (rx/action [    ] (deliver onCompletedCollector true))

          report-collectors
          (fn []
            (identity ;; catchless-pdump ;; for verbose output, use
                      ;; catchless-pdump.
             { ;; Wait on onCompleted BEFORE waiting on onNext because
               ;; the onNext-agent's await-for can return too quickly,
               ;; before messages from other threads are drained from
               ;; its queue.  This code depends on order-of-evaluation
               ;; assumptions in the map.

              :onCompleted (deref onCompletedCollector wait-time false)

              :onNext      (do (await-for wait-time onNextCollector)
                               ;; Produce results even if timed out
                               @onNextCollector)

              :onError     @onErrorCollector
              }))]
      {:subscription
       (.subscribe obl collect-next collect-error collect-completed)

       :reporter
       report-collectors})))

(defn report
  "Given subscribed collectors, produce results obtained so far."
  [subscribed-collectors]
  (catchless-pdump ((:reporter subscribed-collectors))))

;;;   __
;;;  / _|_ _ ___ _ __ ___ ___ ___ __ _
;;; |  _| '_/ _ \ '  \___(_-</ -_) _` |
;;; |_| |_| \___/_|_|_|  /__/\___\__, |
;;;                                 |_|

(defn from-seq
  "Wrap rxjava's 'Observable/from' in a Clojure fn so it can be
  composed."
  [s]
  (Observable/from s))

(def string-explode
  "Explode a string into characters (purposeful alias of seq)."
  seq)

;;;          _
;;;  _ _ ___| |_ _  _ _ _ _ _
;;; | '_/ -_)  _| || | '_| ' \
;;; |_| \___|\__|\_,_|_| |_||_|

;;; This helps with for "distinct-until-changed"

;;; "Return" lifts a value into a collection of length 1 so that the
;;; collection can be composed with others via the standard query
;;; operators.

(defn return
  "Put item into an observable for composability via mapMany."
  [item]
  (from-seq [item]))

;;;     _ _    _   _         _
;;;  __| (_)__| |_(_)_ _  __| |_
;;; / _` | (_-<  _| | ' \/ _|  _|
;;; \__,_|_/__/\__|_|_||_\__|\__|
;;;      _   _     _   _ _  ___ _                          _
;;;     | | | |_ _| |_(_) |/ __| |_  __ _ _ _  __ _ ___ __| |
;;;     | |_| | ' \  _| | | (__| ' \/ _` | ' \/ _` / -_) _` |
;;;      \___/|_||_\__|_|_|\___|_||_\__,_|_||_\__, \___\__,_|
;;;                                           |___/

;;; DistinctUntilChanged collapses runs of the same value in a sequence into
;;; single instances of each value. [a a a x x x a a a] becomes [a x a].

(defn distinct-until-changed
  "Produces an observable that collapses repeated values from its
  predecessor into a single value."
  [obl]
  (let [last-container (ref [])]
    (-> obl
        (.mapMany (rx/fn [obn]
                    (dosync
                     (let [l (last @last-container)]
                       (if (and l (= obn l))
                         (Observable/empty)
                         (do
                           (ref-set last-container [obn])
                           (return obn))))))))))

;;; Must actually execute this once lest I get other errors. Not sure why.

(->  (from-seq ["onnnnne" "tttwo" "thhrrrrree"])
     (.mapMany (rx/fn* (comp from-seq string-explode)))
     distinct-until-changed
     subscribe-collectors
     report)

;;; In a real application, we would do many more unit tests.

;;;  ___               _          _
;;; | _ \___ _ __  ___| |_ ___ __| |
;;; |   / -_) '  \/ _ \  _/ -_) _` |
;;; |_|_\___|_|_|_\___/\__\___\__,_|
;;;    ___               _
;;;   / _ \ _  _ ___ _ _(_)___ ___
;;;  | (_) | || / -_) '_| / -_|_-<
;;;   \__\_\\_,_\___|_| |_\___/__/


;;; Be sure to set a .java.policy file in the appropriate directory
;;; (HOME if you are running this as an ordinary user; in a configured
;;; directory on a server).  Here is a very liberal policy file (you
;;; don't want this in Production!)
;;;
;;; grant {
;;;   permission java.security.AllPermission;
;;; };
;;;

;;; Symbols must be fully qualified (no implicit namespaces) in the
;;; sandbox.

(catchless-pdump
 (let [sb (sandbox secure-tester)]
   (sb '(-> (nrepl-bug.core/from-seq ["onnnnne" "tttwo" "thhrrrrree"])
            (.mapMany
             (rx.lang.clojure.interop/fn*
              (comp nrepl-bug.core/from-seq
                    nrepl-bug.core/string-explode)))
            nrepl-bug.core/distinct-until-changed
            nrepl-bug.core/subscribe-collectors
            nrepl-bug.core/report))))

