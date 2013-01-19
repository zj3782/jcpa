package net.example.suboptimalstatement;

/*
 * Wrong occurrences: 1;
 */
public class SuboptimalLogicalExpression {
    public boolean correctExpression() {
        boolean l = true;
        boolean r = false;
        boolean m = true;
        return (!(l | r | m));
    }
    
    public boolean correctExpression2() {
        boolean l = true;
        boolean r = false;
        return (!l & !r);
    }

    public boolean wrongExpression() {
        boolean l = true;
        boolean r = false;
        boolean m = true;
        return (!l & !r & !m);
    }
}
