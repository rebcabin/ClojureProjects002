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

    // BOILERPLATE
    public native void allocate();  // Define allocator

    // SUBJECT METHODS --- things that matter to our app
    /**************************************************************************/
    /**/
    /**/    public native void testMethod(int a);  // Method we want to use
    /**/
    /**/    public native int  testFunction(int a, int b);
    /**/
    /**/    public native int  testStrings(String s);
    /**/
    /**/    // javacpp Doesn't like mutable C strings
    /**/    // public native int  testStringMutation(String s);
    /**/
    /**/    public native String testStringEcho(String s);
    /**/
    /**************************************************************************/

    // BOILERPLATE
    public static void main(String[] args) {
        Abc abc = new Abc();
        abc.testMethod(123);
    }
}
