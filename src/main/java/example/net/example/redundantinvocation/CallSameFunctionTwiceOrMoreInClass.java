package net.example.redundantinvocation;

/*
 * Wrong occurrences: 1;
 */
public class CallSameFunctionTwiceOrMoreInClass {

    MethodFactory mf = MethodFactory.getInstance();

    String correctGetRed() {
        return mf.getColor(true);
    }

    String correctGetGray() {
        return mf.getColor(false);
    }
    
    String wrongGetRed() {
        return MethodFactory.getInstance().getColor(true);
    }

    String WrongGetGray() {
        return MethodFactory.getInstance().getColor(false);
    }


}
