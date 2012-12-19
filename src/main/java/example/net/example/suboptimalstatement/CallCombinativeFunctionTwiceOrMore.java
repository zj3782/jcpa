package net.example.suboptimalstatement;

/*
 * Wrong occurrences: 2;
 */
public class CallCombinativeFunctionTwiceOrMore {
	
	 void cominativeMethod(String s) {
		 
	 }
     void cominativeMethod(String[] s) {
    	 
     }
     void cominativeMethod(String[][] s) {

     }

    public void correctCallMethods() {
        cominativeMethod(new String[] { "A", "B" });
    }

    public void wrongCallMethods() {
        cominativeMethod("A");
        cominativeMethod("B");
    }

   
    void cominativeMethod2(String s1,String s2) {

    }
    void cominativeMethod2(String[] s1,String[] s2) {

    }
    
    public void correctCallMethods2() {
        cominativeMethod(new String[][] { {"A", "B"},{"C","D"}});
    }
    
    
    
    public void wrongCallMethods2() {
    	String c="",d="";
        cominativeMethod2("A","B");
        cominativeMethod2("C","D");
        cominativeMethod2(c,d);
    }

   
}
