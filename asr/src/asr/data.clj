(ns asr.data)


;;  _          _                      __  _
;; | |_ ___ __| |_ __ ____ _ _ _ ___ /  \/ |
;; |  _/ -_|_-<  _|\ V / _` | '_(_-<| () | |
;;  \__\___/__/\__|_\_/\__,_|_| /__/_\__/|_|
;;               |___|            |___|


(def test_vars_01
  " from test_vars_01b import test_name_other_module

    def test_name():
    print(__name__)
    assert __name__ == \"__main__\"

    test_name()
    test_name_other_module()
  "
  '(TranslationUnit
    (SymbolTable
     1
     {:_lpython_main_program
      (Function
       (SymbolTable 7 {})
       _lpython_main_program
       [test_name test_name_other_module]
       []
       [(SubroutineCall 1 test_name () [] ())
        (SubroutineCall 1 test_name_other_module () [] ())]
       ()
       Source
       Public
       Implementation
       ()
       .false.
       .false.
       .false.
       .false.
       .false.
       []
       []
       .false.),
      :main_program
      (Program
       (SymbolTable 6 {})
       main_program
       []
       [(SubroutineCall 1 _lpython_main_program () [] ())]),
      :test_name
      (Function
       (SymbolTable
        5
        {:__name__
         (Variable
          5
          __name__
          []
          Local
          (StringConstant "__main__" (Character 1 8 () []))
          (StringConstant "__main__" (Character 1 8 () []))
          Default
          (Character 1 8 () [])
          Source
          Public
          Required
          .false.)})
       test_name
       []
       []
       [(Print () [(Var 5 __name__)] () ())
        (Assert
         (StringCompare
          (Var 5 __name__)
          Eq
          (StringConstant "__main__" (Character 1 8 () []))
          (Logical 4 [])
          (LogicalConstant .true. (Logical 4 [])))
         ())]
       ()
       Source
       Public
       Implementation
       ()
       .false.
       .false.
       .false.
       .false.
       .false.
       []
       []
       .false.),
      :test_name_other_module
      (ExternalSymbol
       1
       test_name_other_module
       3
       test_name_other_module
       test_vars_01b
       []
       test_name_other_module
       Public),
      :test_vars_01b
      (Module
       (SymbolTable
        3
        {:test_name_other_module
         (Function
          (SymbolTable
           4
           {:__name__
            (Variable
             4
             __name__
             []
             Local
             (StringConstant "__non_main__" (Character 1 12 () []))
             (StringConstant "__non_main__" (Character 1 12 () []))
             Default
             (Character 1 12 () [])
             Source
             Public
             Required
             .false.)})
          test_name_other_module
          []
          []
          [(Print () [(Var 4 __name__)] () ())
           (Assert
            (StringCompare
             (Var 4 __name__)
             NotEq
             (StringConstant "__main__" (Character 1 8 () []))
             (Logical 4 [])
             (LogicalConstant .true. (Logical 4 [])))
            ())]
          ()
          Source
          Public
          Implementation
          ()
          .false.
          .false.
          .false.
          .false.
          .false.
          []
          []
          .false.)})
       test_vars_01b
       []
       .false.
       .false.)})
    []))


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
