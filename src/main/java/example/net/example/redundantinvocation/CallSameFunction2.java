package net.example.redundantinvocation;
/*
 * Wrong occurrences: 1;
 */

public class CallSameFunction2 {
    String correctGetRed() {
        MethodFactory mf = MethodFactory.getInstance();
         String color1 = mf.getColor(true);
         String color2 = mf.getColor(false);
         return color1 + color2;
    }
    
    String wrongGetRed() {
    	String color3 = MethodFactory.getInstance().getColor(true);
    	String color4 = MethodFactory.getInstance().getColor(false);
    	return color3 + color4;
    }
}

