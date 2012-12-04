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
}
