package net.example.redundantinvocation;

/*
 * Wrong occurrences: 2;
 */
public class CallExpensiveFunctionTwiceOrMoreInMethod {

    private long factorial(long n) {
        if (n == 1) {
            return 1;
        } else {
            return n * factorial(n - 1);
        }
    }

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

    public long wrongCallFunctions2() {
    	int a=1,b=1;
        long part1 = factorial(6,7);
        long part2 = factorial(6,7);
        long part3 = factorial(a,b);
        long part4 = factorial(a,b);
        long part5 = factorial(a,b);
        return part1 + part2+part3+part4+part5;
    }
    
    public long factorial(int a,int b){
    	return a+b;
    }
}
