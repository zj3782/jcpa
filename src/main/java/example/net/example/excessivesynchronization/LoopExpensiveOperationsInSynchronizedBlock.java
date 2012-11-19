package net.example.excessivesynchronization;

import java.util.ArrayList;
import java.util.List;

/*
 * Wrong occurrences: 1;
 */
public class LoopExpensiveOperationsInSynchronizedBlock {
    List<String> strList = new ArrayList<String>();
    {

        strList.add("A");
        strList.add("B");
        strList.add("C");
    }
    
    public void excludeExpensiveOperation() {
        synchronized (strList) {
            strList.add("E");
        }
        excludecostMethod();
    }
    
    public void includeExpensiveOperation() {
        synchronized (strList) {
            strList.add("F");
            includecostMethod();
        }
    }
    
    public void excludecostMethod() {
        
    }
    
    public void includecostMethod() {
        
    }
}
