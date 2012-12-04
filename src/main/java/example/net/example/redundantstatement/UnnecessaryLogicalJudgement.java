package net.example.redundantstatement;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

/*
 * Wrong occurrences: 2;
 */
public class UnnecessaryLogicalJudgement {
    public InputStream correctTestVariable() {
        Properties prop = new Properties();
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(
            "resources/messages.properties");
        if (is != null) {
            try {
                prop.load(is);
            } catch (IOException e) {
            }
        }
        return is;
    }

    public String correctTestConstant() {
        String signal = null;
        Scanner in = new Scanner(System.in);
        String what = in.nextLine();
        if (what == "Yes" || what == "No") {
            signal = what;
        }
        return signal;
    }

    public String wrongTestConstant1() {
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

    public String wrongTestConstant2() {
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
