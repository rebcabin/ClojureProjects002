(ns asr.sandbox)

;; It's not easy to put libjniAbc.dylib on the java.library.path,
;; so we just put it in the same directory as project.clj.
(. System getProperty "java.library.path")

;; We can find all the javacpp methods (even autocomplete can find
;; them.
(org.bytedeco.javacpp.BoolPointer.)
;; => #object[org.bytedeco.javacpp.BoolPointer 0x3d6bf863 "org.bytedeco.javacpp.BoolPointer[address=0x0,position=0,limit=0,capacity=0,deallocator=null]"]

;; Because Abc has been added to javacpp.jar, and because
;; javacpp.jar is in :resource-paths of project.clj, we don't need
;; to import the class name if we're just using it. Here are two
;; ways to use it. (I found it difficult to use Abc without putting
;; it in the javacpp.jar).
(. (Abc.) testMethod 42)
(.testMethod (Abc.) 42)

;; But we can't refer to the class directly by name without
;; importing it.
(import 'Abc)
Abc

;; We can create instances.
(def abc (Abc.))
;; => #object[Abc 0x66d83fb4 "Abc[address=0x600001814490,position=0,limit=1,capacity=1,deallocator=org.bytedeco.javacpp.Pointer$NativeDeallocator[ownerAddress=0x600001814490,deallocatorAddress=0x103b05054]]"]

;; We can call methods on the instances and pass args.
(.testMethod abc 42)
;; => nil
(.testFunction abc 42 42)
;; => 1764
(.testStrings abc "hello")
;; => 5
(.testStringEcho abc "world!")
;; => "world!"

;;; DOESN'T WORK:

;; Port of https://github.com/bytedeco/javacpp-presets/tree/master/openblas
;; Requires openblas dependency:
;; [org.bytedeco.javacpp-presets/openblas-platform "0.3.5-1.4.4"]

#_
(ns ExampleDGELSrowmajor
  (:import [org.bytedeco.javacpp openblas]))

#_
(defn print-matrix-rowmajor [desc m n mat ldm]
  (println "\n " desc)
  (doseq [i (range m)]
    (doseq [j (range n)]
      (print (format " %6.2f" (aget mat (+ j (* i ldm))))))
    (println)))

#_
(defn open-blas-unit-test
  []
  (let [a (double-array [1, 1, 1, 2, 3, 4, 3, 5, 2, 4, 2, 5, 5, 4, 3])
        b (double-array [-10, -3, 12, 14, 14, 12, 16, 16, 18, 16])
        m 5
        n 3
        nrhs 2
        lda 3
        ldb 2]
    (print-matrix-rowmajor "Entry Matrix A" m n a lda)
    (print-matrix-rowmajor "Right Hand Side b" n nrhs b ldb)
    (println "LAPACKE_dgels (row-major, high-level) Example Program Results")
    (let [info (openblas/LAPACKE_dgels openblas/LAPACK_ROW_MAJOR (byte \N) m n
                                       nrhs a lda b ldb)]
      (print-matrix-rowmajor "Solution" n nrhs b ldb))))

#_
(open-blas-unit-test)
