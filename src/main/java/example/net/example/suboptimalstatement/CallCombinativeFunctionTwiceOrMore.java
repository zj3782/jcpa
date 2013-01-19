package net.example.suboptimalstatement;

/*
 * Wrong occurrences: 4;
 */
public class CallCombinativeFunctionTwiceOrMore {
	
	 void cominativeMethod(String s) {
		 
	 }
     void cominativeMethod(String[] s) {
    	 
     }
     void cominativeMethod(String[][] s) {

     }
     void cominativeMethod(String s1,String s2) {

     }
     void cominativeMethod(String[] s1,String[] s2) {

     }
     
     
    public void correctCallMethods() {
        cominativeMethod(new String[] { "A", "B" });
    }

    public void wrongCallMethods() {
        cominativeMethod("A");
        cominativeMethod("B");
    }
    
    public void correctCallMethods2() {
        cominativeMethod(new String[][] { {"A", "B"},{"C","D"}});
    }
    
    public void wrongCallMethods2() {
    	String c="",d="";
        cominativeMethod("A","B");
        cominativeMethod("C","D");
        cominativeMethod(c,d);
    }

    public void correctCallMethodsInIfElse(int a) {
    	if(a==1){
    		cominativeMethod("A","B");
    	}else if(a==2){
    		cominativeMethod("C","D");
    	}else{
    		String c="",d="";
    		cominativeMethod(c,d);
    	}
    }
    
    public void correctCallMethodsInCase(int a) {
    	switch(a){
    	case 1:
    		cominativeMethod("A","B");
    		break;
    	case 2:
    		cominativeMethod("C","D");
    		break;
    	default:
    		String c="",d="";
    		cominativeMethod(c,d);
    	}
    }
    
    public void wrongCallMethodsInCase(int a) {
    	switch(a){
    	case 1:
    		cominativeMethod("A","B");
    	case 2:
    		cominativeMethod("C","D");
    		break;
    	default:
    		String c="",d="";
    		cominativeMethod(c,d);
    	}
    }
}
