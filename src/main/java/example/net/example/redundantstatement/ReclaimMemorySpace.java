package net.example.redundantstatement;

import java.util.Scanner;

/*
 * Wrong occurrences: 1;
 */
public class ReclaimMemorySpace {
    private String field;

    // It is correct
    public ReclaimMemorySpace() {
        String field = null;
    }

    public String correctSetNull() {
        String question = "?";
        Scanner in = new Scanner(System.in);
        String what = in.nextLine();
        if (!(what == "Yes" || what == "No")) {
            question = null;
        } else {
            question = what + question;
        }
        return question;
    }

    public String wrongSetNull() {
        String question = "?";
        Scanner in = new Scanner(System.in);
        String what = in.nextLine() + question;
        question = null;
        return what;
    }
}
