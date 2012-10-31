package net.example.suboptimalstatement;

/*
 * Wrong occurrences: 1;
 */
public class CallCombinativeFunctionTwiceOrMore {
    public void correctCallMethods() {
        cominativeMethod(new String[] { "A", "B" });
    }

    public void wrongCallMethods() {
        cominativeMethod("A");
        cominativeMethod("B");
    }

    void cominativeMethod(String s) {

    }

    void cominativeMethod(String[] s) {

    }
}
