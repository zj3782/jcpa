package net.example.unsafedataaccess;

import java.util.ArrayList;
import java.util.List;

/*
 * Wrong occurrences: 1;
 */
public class UnspecifyTargetTypeInArrayConversion {
    List<String> strList = new ArrayList<String>();
    {
        strList.add("A");
        strList.add("B");
        strList.add("C");
    }

    public String[] correctArrayConversion() {
        return strList.toArray(new String[0]);
    }

    public String[] wrongArrayConversion() {
        return (String[]) strList.toArray();
    }
}
