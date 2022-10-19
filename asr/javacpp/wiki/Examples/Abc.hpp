#include <string>

class Abc {
public:
    void testMethod(int a) {
        printf("Abc.testMethod: +_+_+_+_+_+_+ %d +_+_+_+_+_+_+\n", a);
    }

    int testFunction(int a, int b) {
        int c = a * b;
        printf("Abc.testFunction: /-/-/-/-/-/-/ %d /-/-/-/-/-/-/\n", c);
        return c;
    }

    int testStrings(const char * s) {
        int c = strlen(s);
        return c;
    }

    const char * testStringEcho (const char * s) {
        return s;
    }

    // jacacpp doesn't like this:
    // int testStringMutation (const char * s) {
    //     for (int i=0; s[i]; i++) {
    //         s[i] = toupper[s[i]];
    //     }
    //     return strlen(s);
    // }
};
