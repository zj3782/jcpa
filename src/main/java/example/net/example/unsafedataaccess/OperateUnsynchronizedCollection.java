package net.example.unsafedataaccess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Wrong occurrences: 2;
 */
public class OperateUnsynchronizedCollection {
    Set<String> unsafeSet = null;
    List<String> unsafeList = null;
    Map<String, String> unsafeMap = null;

    Set<String> safeSet = null;
    List<String> safeList = null;
    Map<String, String> safeMap = null;

    OperateUnsynchronizedCollection() {
        unsafeSet = new HashSet<String>();
        safeSet = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
        unsafeList = new ArrayList<String>();
        safeList = Collections.synchronizedList(new ArrayList<String>());
        unsafeMap = new HashMap<String, String>();
        safeMap = Collections.synchronizedMap(new HashMap<String, String>());
    }

    public void correctPutSafeSet() {
        safeSet.add("A");
        safeList.add("A");
        safeMap.put("K", "V");
    }

    public void wrongPutUnsafeSet() {
        unsafeSet.add("A");
        unsafeList.add("A");
        unsafeMap.put("K", "V");
    }
}
