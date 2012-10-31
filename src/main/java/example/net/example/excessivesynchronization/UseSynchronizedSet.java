package net.example.excessivesynchronization;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Wrong occurrences: 1;
 */
public class UseSynchronizedSet {

    public Set<String> correctGetSafeSet() {
        return Collections
            .newSetFromMap(new ConcurrentHashMap<String, Boolean>());
    }

    public Set<String> wrongGetSafeSet() {
        return Collections.synchronizedSet(new HashSet<String>());
    }
}
