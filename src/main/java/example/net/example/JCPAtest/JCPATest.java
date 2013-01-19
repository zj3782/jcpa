package net.example.JCPAtest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/*
 * Wrong occurrences: 5;
 */
public class JCPATest {
	List<String> strList=new ArrayList<String>();
	
	public void callExpensiveMethodTwice() {
		GenericEntity entity = new GenericEntity();
		entity.expensiveFunction("ab");
		entity.expensiveFunction("ab");
	}

	public void callCombinativeMethodTwice() {
		GenericEntity entity = new GenericEntity();
		entity.combinativeFunction("ab");
		entity.combinativeFunction("cd");
	}

	public void callExpensiveMethodInSynchronization() {
		GenericEntity entity = new GenericEntity();
		synchronized (strList) {
			strList.add("ABC");
			entity.expensiveFunction("ab");
		}
	}

	public void callExpensiveMethodInLockRight() {
		GenericEntity entity = new GenericEntity();
		ReentrantLock lock = new ReentrantLock();
		lock.lock();
		try {
			entity.expensiveFunction("ab");
		} finally {
			lock.unlock();
		}
	}
	
	public void callExpensiveMethodInLockWrong() {
		ReentrantLock lock = new ReentrantLock();
		lock.lock();
		try {
			strList.add("ABC");
		} finally {
			lock.unlock();
		}
	}

	public void callExpensiveMethodInRWLockRight() {
		GenericEntity entity = new GenericEntity();
		ReadWriteLock rwLock = new ReentrantReadWriteLock();
		rwLock.readLock().lock();
		try {
			entity.expensiveFunction("ab");
		} finally {
			rwLock.readLock().unlock();
		}
	}
	
	public void callExpensiveMethodInRWLockWrong() {
		ReadWriteLock rwLock = new ReentrantReadWriteLock();
		rwLock.readLock().lock();
		try {
			strList.add("ABC");
		} finally {
			rwLock.readLock().unlock();
		}
	}
}
