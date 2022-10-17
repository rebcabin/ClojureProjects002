import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;

@Platform(include = "Abc.hpp", link = "jniAbc")
// "link" tells javacpp which original library should be linked
// (if not specified, "Abc" will be used)
public class Abc extends Pointer {
    static {
        //Loader.loadLibrary("jnifoo");  //  Name of wrapper library
        Loader.load();  // Use if JNI wrapper named as class, i.e. "jniAbc"
    }

    public Abc() {
        allocate();  // Wrap allocator
    }

    public native void allocate();  // Define allocator

    public native void testMethod(int a);  // Method we want to use

    public static void main(String[] args) {
        Abc abc = new Abc();
        abc.testMethod(123);
    }
}
