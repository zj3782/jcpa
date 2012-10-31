package net.example.redundantinvocation;

public class MethodFactory {
    public final static MethodFactory inst = new MethodFactory();

    public static MethodFactory getInstance() {
        return inst;
    }
    
    public  String getColor(boolean red) {
        if (red) {
            return "RED";
        } else {
            return "Gray";
        }
    }
}
