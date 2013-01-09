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

    public void correctCallMethods2(int a) {
    	if(a==1){
    		cominativeMethod2("A","B");
    	}else if(a==2){
    		cominativeMethod2("C","D");
    	}else{
    		String c="",d="";
    		cominativeMethod2(c,d);
    	}
    }
    public void correctCallMethods3(int a) {
    	switch(a){
    	case 1:
    		cominativeMethod2("A","B");
    		break;
    	case 2:
    		cominativeMethod2("C","D");
    		break;
    	default:
    		String c="",d="";
    		cominativeMethod2(c,d);
    	}
    }
    public void wrongCallMethods3(int a) {
    	switch(a){
    	case 1:
    		cominativeMethod2("A","B");
    	case 2:
    		cominativeMethod2("C","D");
    		break;
    	default:
    		String c="",d="";
    		cominativeMethod2(c,d);
    	}
    }
}
