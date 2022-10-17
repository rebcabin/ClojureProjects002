rm Abc.class
rm jniAbc.cpp
rm jnijavacpp.cpp
rm libjniAbc.dylib


javac -cp javacpp.jar Abc.java


# The following produces a clang error, which we fix in a second.
java  -jar javacpp.jar -d . -o jniAbc -Xcompiler -L. Abc.java


clang++ \
    -I/opt/homebrew/Cellar/openjdk/19/libexec/openjdk.jdk/Contents/Home/include/darwin \
    -I/opt/homebrew/Cellar/openjdk/19/libexec/openjdk.jdk/Contents/Home/include \
    jniAbc.cpp \
    jnijavacpp.cpp \
    -O3 -L. -arch arm64 -Wl,-rpath,@loader_path/. \
    -Wall -fPIC -pthread -dynamiclib -undefined dynamic_lookup \
    -o libjniAbc.dylib


java -cp javacpp.jar:. --add-opens=java.base/java.lang=ALL-UNNAMED Abc.java
