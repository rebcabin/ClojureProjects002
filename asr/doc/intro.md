# Introduction to asr

First of all, see README.md at the root of this repository. That's where the
beef is, not here.

Second, see codox-generated collection of docstrings in
`target/default/doc/index.html`. This is a handy list of functions and
variables.

Third, because codox can't collect information about specs, here is a handy
list of the specs currently in all namespaces:

```
autospecs.clj:50:    `(s/def ~nskw #{(quote ~(symbol symconst))})))
autospecs.clj:80:    `(s/def ~term-nskw (set (quote ~ss1)))))
autospecs.clj:129:  (s/def ::symbol
autospecs.clj:136:  (s/def ::expr
autospecs.clj:143:  (s/def ::stmt
autospecs.clj:150:  (->> (s/def ::ttype
autospecs.clj:207:    `(s/def ~nskw ~(spec-from-args args))))  ;; side effect!
autospecs.clj:238:    `(s/def ~nskw (s/spec ~head))))
autospecs.clj:294:    `(s/def ~nskw ~(spec-from-head-and-args head args))))
autospecs.clj:380:  (s/def ::int   int?)
autospecs.clj:381:  (s/def ::float float?)
autospecs.clj:382:  (s/def ::bool  (s/or :clj-bool boolean?
core.clj:48:(s/def ::bignat
core.clj:70:(s/def ::dimensions
core.clj:96:(s/def ::integer-ttype-semnasr
core.clj:131:(s/def ::integer-scalar-ttype-semnasr
core.clj:148:  (s/def ::i8-scalar-ttype-semnasr
core.clj:154:  (s/def ::i16-scalar-ttype-semnasr
core.clj:160:  (s/def ::i32-scalar-ttype-semnasr
core.clj:166:  (s/def ::i64-scalar-ttype-semnasr
core.clj:226:    (s/def ::i8  (s/spec  si8 :gen  gi8)) ; s/spec means
core.clj:227:    (s/def ::i16 (s/spec si16 :gen gi16)) ; "nestable"
core.clj:228:    (s/def ::i32 (s/spec si32 :gen gi32))
core.clj:229:    (s/def ::i64 (s/spec si64 :gen gi64))))
core.clj:264:    (s/def ::i8nz  (s/spec  si8nz :gen  gi8nz)) ; s/spec means
core.clj:265:    (s/def ::i16nz (s/spec si16nz :gen gi16nz)) ; "nestable"
core.clj:266:    (s/def ::i32nz (s/spec si32nz :gen gi32nz))
core.clj:267:    (s/def ::i64nz (s/spec si64nz :gen gi64nz))))
core.clj:286:  (s/def ::i8-constant-semnasr
core.clj:292:  (s/def ::i16-constant-semnasr
core.clj:298:  (s/def ::i32-constant-semnasr
core.clj:304:  (s/def ::i64-constant-semnasr
core.clj:311:(s/def ::integer-constant-semnasr
core.clj:333:  (s/def ::i8-non-zero-constant-semnasr
core.clj:339:  (s/def ::i16-non-zero-constant-semnasr
core.clj:345:  (s/def ::i32-non-zero-constant-semnasr
core.clj:351:  (s/def ::i64-non-zero-constant-semnasr
core.clj:358:(s/def ::integer-non-zero-constant-semnasr
core.clj:445:(s/def ::i32-bin-op-semnasr
core.clj:615:;; (s/def ::nil-producing-spec
core.clj:624:;; (s/def ::nil-rejecting-spec
core.clj:706:(s/def ::i32-bin-op-leaf-semsem
core.clj:816:(s/def ::i32-bin-op-semsem
core.clj:940:    (s/def ::i32-bin-op-semsem
specs.clj:26:  (s/def :asr.specs/identifier
```

TODO: write [great documentation](http://jacobian.org/writing/what-to-write/)
