package net.example.excessivesynchronization;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
 * Wrong occurrences: 1;
 * 
 * Correct:
 * Map<String, Data> m = Collections.synchronizedMap(new HashMap<String, Data>());
 */

public class SynchronizeCollectionWithLockVariable {
    private final Map<String, Data> m = new HashMap<String, Data>();
    private final ReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();

    public Data correctGet(String key) {
        r.lock();
        try {
        	DBManager db=new DBManager();
        	db.process();
            return m.get(key);
        } finally {
            r.unlock();
        }
    }
    
    public Data get(String key) {
        r.lock();
        try {
            return m.get(key);
        } finally {
            r.unlock();
        }
    }

    public String[] allKeys() {
        r.lock();
        try {
            return m.keySet().toArray(new String[0]);
        } finally {
            r.unlock();
        }
    }

    public Data put(String key, Data value) {
        w.lock();
        try {
            return m.put(key, value);
        } finally {
            w.unlock();
        }
    }

    public void clear() {
        w.lock();
        try {
            m.clear();
        } finally {
            w.unlock();
        }
    }

    class Data {
        String name;

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name
         *            the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

    }
    class DBManager{
    	public void process(){}
    }
}
