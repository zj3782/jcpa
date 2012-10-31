package net.example.suboptimalstatement;

/*
 * Wrong occurrences: 2;
 */
public class SuboptimalLogicalExpression {
    public boolean correctExpression() {
        boolean l = true;
        boolean r = false;
        boolean m = true;
        return (!(l | r | m));
    }

    public boolean wrongExpression() {
        boolean l = true;
        boolean r = false;
        boolean m = true;
        return (!l & !r & !m);
    }
}
