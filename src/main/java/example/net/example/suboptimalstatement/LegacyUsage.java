package net.example.suboptimalstatement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/*
 * Wrong occurrences: 3;
 */
public class LegacyUsage {
    public void correctIterate() {
        String[] strs = { "A", "B", "C", "D" };
        for (String str : strs) {
            assert (str != null);
        }
        List<String> strList = Arrays.asList(strs);
        for (String str : strList) {
            assert (str != null);
        }

        Set<String> strSet = new HashSet<String>(strList);
        for (String str : strSet) {
            assert (str != null);
        }
    }

    public void wrongIterate() {
        String[] strs = { "E", "F", "G", "H" };
        for (int i = 0; i < strs.length; i++) {
            assert (strs[i] != null);
        }
        List<String> strList = Arrays.asList(strs);
        Iterator<String> it = strList.iterator();
        while (it.hasNext()) {
            assert (it.next() != null);
        }

        Set<String> strSet = new HashSet<String>(strList);
        Iterator<String> itr = strSet.iterator();
        while (itr.hasNext()) {
            assert (itr.next() != null);
        }
    }
}
