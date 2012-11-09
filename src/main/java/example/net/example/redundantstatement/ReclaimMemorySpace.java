package net.example.redundantstatement;

import java.util.Scanner;

/*
 * Wrong occurrences: 1;
 */
public class ReclaimMemorySpace {
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
        Scanner ini = new Scanner(System.in);
        String whati = ini.nextLine() + question;
        question = null;
        return whati;
    }
}
