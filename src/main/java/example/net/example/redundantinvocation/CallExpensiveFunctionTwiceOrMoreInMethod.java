package net.example.redundantinvocation;

/*
 * Wrong occurrences: 6;
 */
public class CallExpensiveFunctionTwiceOrMoreInMethod {

     public long correctCallFunctions() {
        long part1 = factorial(4);
        long part2 = factorial(5);
        return part1 + part2;
    }

    public long wrongCallFunctions() {
        long part1 = factorial(6);
        long part2 = factorial(6);
        return part1 + part2;
    }

    public long wrongCallManyFunctions() {
    	int a=1,b=1;
        long part1 = factorial(6,7);
        long part2 = factorial(6,7);
        long part3 = factorial(a,b);
        long part4 = factorial(a,b);
        long part5 = factorial(a,b);
        return part1 + part2+part3+part4+part5;
    }	
	
	public void correctCallFunctionsInIfElse(){
		int a=12;
		if(a==1){
			if(a==1){
				costMethod();
			}
			costMethod();
		}else if(a==2){
			factorial(1);
		}else if(a==3){
			factorial(1);
		}else{
			costMethod();
		}
	}
	
	public void wrongCallFunctionsInIfElse(){
		int a=12;
		if(a==1){
			if(a==1){
				factorial(1);
			}
		}else if(a==2){
			factorial(1);
			factorial(1);
		}
	}
	
	public void correctCallFunctionsInIfElse2(){
		int a=12;
		if(a==1){
			if(a-1==1){
				costMethod();
			}
		}else{
			if(a==2){
				costMethod();
			}else{
				if(a==3){
					costMethod();
				}else{
					costMethod();
				}
			}
		}
	}
	
	public void correctCallFunctionsInIfElse3(){
		int a=12;
		if(a==1){
			if(a==1){
				costMethod();
			}
		}else if(a==2){
			if(a==1){
				a++;
				costMethod();
			}else{
				costMethod();
			}
		}else if(a==3){
			for(int i=0;i<10;i++){
				costMethod();
			}
		}else{
			costMethod();
		}
	}
	
	public void correctCallFunctionInCase(int i){
		switch(i){
		case 1:
			i++;
			costMethod();
			break;
		case 2:
			i+=3;
			costMethod();
			break;
		default:
			i++;
			break;
		}
	}
	public void wrongCallFunctionInCase(int i){
		switch(i){
		case 1:
			costMethod();
			for(int j=0;j<10;j++){
				i++;
				if(j==2)break;
			}
		case 2:
			i+=3;
			costMethod();
			break;
		default:
			i++;
			break;
		}
	}
	
	private long factorial(long n) {
        if (n == 1) {
            return 1;
        } else {
            return n * factorial(n - 1);
        }
    }
	public long factorial(int a,int b){
    	return a+b;
    }
	public void costMethod(){}
}
