package net.example.unsafedataaccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * Wrong occurrences: 0;
 */
public class OperateUnsynchronizedCollectionRight {
    Set<String> unsafeSet = null;
    List<String> unsafeList = null;
    Map<String, String> unsafeMap = null;

    
    OperateUnsynchronizedCollectionRight() {
        unsafeSet = new HashSet<String>();
        unsafeList = new ArrayList<String>();
        unsafeMap = new HashMap<String, String>();
    }

    public void CorrectPutUnsafeSet() {
        synchronized (unsafeSet) {
        	unsafeSet.add("A");
        }

        synchronized (unsafeSet) {
        	unsafeSet.add("A");
        }

        synchronized (unsafeMap) {
        	unsafeMap.put("K", "V");
        }
    }

    public void CorrectPutUnsafeSet2() {
        synchronized (this) {
        	unsafeSet.add("A");
            unsafeList.add("A");
            unsafeMap.put("K", "V");
        }
    }
    public void CorrectPutUnsafeSet3() {
    	Lock l=new ReentrantLock();
    	l.lock();
    	try {
    		unsafeSet.add("A");
            unsafeList.add("A");
            unsafeMap.put("K", "V");
            otherfunc();
    	} finally {
    		l.unlock();
    	}
    }  
    
    public void otherfunc(){
    	
    }
}
