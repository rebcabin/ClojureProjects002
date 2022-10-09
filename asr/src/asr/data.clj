(ns asr.data)


;;                        __  _    ___ _ _  __   __   __
;;  _____ ___ __ _ _ ___ /  \/ |__|_  ) / |/  \ /  \ /  \
;; / -_) \ / '_ \ '_|___| () | |___/ /| | | () | () | () |
;; \___/_\_\ .__/_|      \__/|_|  /___|_|_|\__/ \__/ \__/
;;         |_|

(def expr-01-211000
  "Here is the Python that generates the ASR below, from
  <https://github.com/lcompilers/lpython/blob/84a073ce44a9a74213a4ac5648ee783bd38fc90f/tests/expr_01.py>.


```python id=f1020f8e-d252-4b90-b78a-e74a366b580e
def main0():
    x: i32
    x2: i64
    y: f32
    y2: f64
    x = (2+3)*5
    print(x)

main0()
```
  "
  '(TranslationUnit
    (SymbolTable
     1
     {:_lpython_main_program
      (Function
       (SymbolTable 4 {})
       _lpython_main_program
       []
       []
       [(SubroutineCall 1 main0 () [] ())] ()
       Source Public Implementation () .false. .false. .false. .false.),
      :main0
      (Function
       (SymbolTable
        2 {:x (Variable
               2 x Local () () Default
               (Integer 4 []) Source Public Required .false.),
           :x2 (Variable
                2 x2 Local () () Default
                (Integer 8 []) Source Public Required .false.),
           :y (Variable
               2 y Local () () Default
               (Real 4 []) Source Public Required .false.),
           :y2 (Variable
                2 y2 Local () () Default (Real 8 [])
                Source Public Required .false.)})
       main0 [] []
       [(= (Var 2 x)
           (IntegerBinOp
            (IntegerBinOp
             (IntegerConstant
              2 (Integer 4 []))
             Add
             (IntegerConstant
              3
              (Integer 4 []))
             (Integer 4 [])
             (IntegerConstant
              5 (Integer 4 [])))
            Mul
            (IntegerConstant
             5 (Integer 4 []))
            (Integer 4 [])
            (IntegerConstant
             25 (Integer 4 []))) ())
        (Print () [(Var 2 x)] () ())]
       () Source Public Implementation () .false. .false. .false. .false.),
      :main_program
      (Program
       (SymbolTable 3 {})
       main_program []
       [(SubroutineCall 1 _lpython_main_program () [] ())])}) []))
