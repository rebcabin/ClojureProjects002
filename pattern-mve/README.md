# pattern-mve

I am seeking critique of a certain "programming pattern" that's arisen several times in a project. I want testable types satisfying a protocol, but the pattern I developed "feels" heavyweight, as the example will show, but I don't know a smaller way to get what I want. The amount of code I needed in order to formalize and test my specs "feels" like too much. In particular, the introduction of a defrecord just to support the protocol doesn't "feel" minimal. The defrecord provides a constructor with positional args — of dubious utility, especially for large records — but otherwise acts like a hashmap. Perhaps there is a way to bypass the defrecord and directly use a hashmap?

Generally, I am suspicious of "programming patterns," because I believe that an apparent need for a programming pattern usually means one of two things:

1. The programming language doesn't directly support some reasonable need, and that's not usually the case with Clojure

2. Ignorance: I don't know an idiomatic way to do what I want.

There is a remote, third possibility: that "what I want" is stupid, ignorant, or otherwise unreasonable. 

Here is what I settled on: quadruples of protocol, defrecord, specs and tests to fully describe and test types in my application:

1. a protocol to declare functions that certain types must implement
2. at least one defrecord to implement the protocol
3. a spec to package checks and test generators
4. tests to, well, test them

For a small example (my application has some that are much bigger), consider a type that models "virtual times" as numbers-with-infinities. Informally, a "virtual time" is either a number or one of two distinguished values for plus and minus infinity. Minus infinity is less than any virtual time other than minus infinity. Plus infinity is greater than any virtual time other than plus infinity." We'll write a protocol, a defrecord, a spec, and a couple of tests for this type. The amount of code I (apparently) need to write to express this definition more formally (that is, machine-checkably) surprised me and led me to write this question.

In the actual code, the elements of my pattern come in that order --- protocol, defrecord, spec, and tests --- because of cascading dependencies. For human consumption, I'll "detangle" them and present the spec first:

```clojure
(s/def ::virtual-time
  (s/with-gen
    (s/and ; idiom for providing a "conformer" function below
     (s/or
      :minus-infinity #(vt-eq % :vt-negative-infinity) ; see the protocol for "vt-eq"
      :plus-infinity  #(vt-eq % :vt-positive-infinity)
      :number         #(number? (:vt %)))
     (s/conformer second))              ; strip off redundant conformer tag
    #(gen/frequency [[98 vt-number-gen] ; generate mostly numbers ...
                     [ 1 vt-negative-infinity-gen] ; ... with occasional infinities
                     [ 1 vt-positive-infinity-gen]])))
```

That should be self-explanatory given the following definitions:

```clojure
(def vt-number-gen
  (gen/bind
   (gen/large-integer)
   (fn [vt] (gen/return (virtual-time. vt))))) ; invoke constructor ... heavyweight?

(def vt-negative-infinity-gen
  (gen/return (virtual-time. :vt-negative-infinity)))

(def vt-positive-infinity-gen
  (gen/return (virtual-time. :vt-positive-infinity)))
```

The tests use the generators and a couple of global variables:

```clojure
(def vt-negative-infinity (virtual-time. :vt-negative-infinity))
(def vt-positive-infinity (virtual-time. :vt-positive-infinity))

(defspec minus-infinity-less-than-all-but-minus-infinity
  100
  (prop/for-all
   [vt (s/gen :pattern-mve.core/virtual-time)]
   (if (not= (:vt vt) :vt-negative-infinity)
     (vt-lt vt-negative-infinity vt) ; see the protocol for def of "vt-lt"
     true)))

(defspec plus-infinity-not-less-than-any
  100
  (prop/for-all
   [vt (s/gen :pattern-mve.core/virtual-time)]
   (not (vt-lt vt-positive-infinity vt))))
```

The protocol specifies the comparison operators "vt-lt" and "vt-le." A defrecord to implement it should now be obvious, given understanding of how they're used above:

```clojure
(defprotocol VirtualTimeT
  "A number with two distinguished values for plus and minus infinity. Minus
  infinity is less than any virtual time other than minus infinity. Plus
  infinity is greater than any virtual time other than plus infinity."
  (vt-lt [this-vt that-vt])
  (vt-le [this-vt that-vt])
  (vt-eq [this-vt that-vt]))

(defn -vt-compare-lt [this-vt that-vt]
  (case (:vt this-vt)
    :vt-negative-infinity
    (case (:vt that-vt)
      :vt-negative-infinity false
      #_otherwise true)

    :vt-positive-infinity
    false

    ;; otherwise: this-vt is a number.
    (case (:vt that-vt)
      :vt-positive-infinity true
      :vt-negative-infinity false
      #_otherwise (< (:vt this-vt) (:vt that-vt)))))

(defrecord virtual-time [vt]
  VirtualTimeT
  (vt-lt [this that] (-vt-compare-lt this that))
  (vt-eq [this that] (= this that))
  (vt-le [this that] (or (vt-eq this that) (vt-lt this that))))
```



