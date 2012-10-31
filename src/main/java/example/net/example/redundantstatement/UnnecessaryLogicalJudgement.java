package net.example.redundantstatement;

import java.util.Scanner;

/*
 * Wrong occurrences: 2;
 */
public class UnnecessaryLogicalJudgement {
    public String correctTestVariable() {
        String signal = null;
        Scanner in = new Scanner(System.in);
        String what = in.nextLine();
        if (what == "Yes" || what == "No") {
            signal = what;
        }
        return signal;
    }

    public String wrongTestVariable1() {
        String signal = null;
        Scanner in = new Scanner(System.in);
        String what = in.nextLine();
        if (signal == null) {
            if (what == "Yes" || what == "No") {
                signal = what;
            }
        }
        return signal;
    }

    public String wrongTestVariable2() {
        String signal = "Yes";
        Scanner in = new Scanner(System.in);
        String what = in.nextLine();
        if (signal != null) {
            if (what == "Yes" || what == "No") {
                signal = what;
            }
        }
        return signal;
    }
}
