package net.example.redundantinvocation;

/*
 * Wrong occurrences: 2;
 */
public class CallSameFunctionTwiceOrMoreInMethod {
    private long f(long n) {
        if (n == 1) {
            return 1;
        } else {
            return n * f(n - 1);
        }
    }

    public long correctCallFunctions() {
        long part1 = f(5);
        long part2 = f(4);
        return part1 + part2;
    }

    public long wrongCallFunctions() {
        long part1 = f(5);
        long part2 = f(5);
        return part1 + part2;
    }
}
