package com.jcpa.cases;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.xml.crypto.Data;

import com.jcpa.util.ToolUtil;

public class LockTester extends Case {
	
	protected static boolean bInterrupt=false;//是否打断程序运行
	protected static boolean bRuning=false;//程序是否正在运行
	
    private final Map<String, Data> m = new HashMap<String, Data>();
    private final ReentrantReadWriteLock rwm = new ReentrantReadWriteLock();
    private final Lock rm = rwm.readLock();
    private final Lock wm = rwm.writeLock();

    public Data get(String key) {
        rm.lock();
        try { return m.get(key); }
        finally { rm.unlock(); }
    }
    public String[] allKeys() {
        rm.lock();
        try { return (String[]) m.keySet().toArray(); }
        finally { rm.unlock(); }
    }
    public Data put(String key, Data value) {
        wm.lock();
        try { return m.put(key, value); }
        finally { wm.unlock(); }
    }
    public void clear() {
        wm.lock();
        try { m.clear(); }
        finally { wm.unlock(); }
    }
    
    private final Map<String, Data> n = Collections.synchronizedMap(new HashMap<String, Data>());
    
	class ThreadWorker extends Thread {
		
		private int id;

		public ThreadWorker(int id) {
			this.id = id;
		}
		public int getThreadId(){return id;}
		public void run() {
			int r = LockTester.this.repeats * LockTester.this.iorate
					/ 100;
			int w = LockTester.this.repeats - r;

			LockTester.this.latch();

			long t1 = System.nanoTime();
			
			for (int i = 0; i < w && !LockTester.bInterrupt; i++) {
				m.put(String.valueOf(i), new Data() {
				});
			}
			
			long t2 = System.nanoTime();
			
			for (int i = 0; i < r && !LockTester.bInterrupt; i++) {
				m.containsKey(String.valueOf(i));
				m.containsValue("");
			}

			long t3 = System.nanoTime();
			
			for (int i = 0; i < w && !LockTester.bInterrupt; i++) {
				n.put(String.valueOf(i), new Data() {
				});
			}
			
			long t4 = System.nanoTime();
			
			for (int i = 0; i < r && !LockTester.bInterrupt; i++) {
				n.containsKey(String.valueOf(i));
				m.containsValue("");
			}

			long t5 = System.nanoTime();
			if(!LockTester.bInterrupt){
				double diff1 = t2 - t1;
				double diff2 = t3 - t2;
				double diff3 = t4 - t3;
				double diff4 = t5 - t4;
				LockTester.this.t1 += diff1;
				LockTester.this.t2 += diff2;
				LockTester.this.t3 += diff3;
				LockTester.this.t4 += diff4;
			}

			LockTester.this.cdl.countDown();
		}
	}

	private CountDownLatch cdl;

	private int threads;
	private int thrds;
	private int repeats;
	private int iorate;

	public long t1 = 0;
	public long t2 = 0;
	public long t3 = 0;
	public long t4 = 0;

	public LockTester() {}
	
	public void init(int threads, int repeats, int ioRate){
		this.threads = threads;
		this.repeats = repeats;
		thrds = threads;
		this.iorate = ioRate;

		cdl = new CountDownLatch(threads);

		for (int i = 0; i < threads; i++) {
			new ThreadWorker(i).start();
		}
	}

	public synchronized void latch() {
		try {
			this.threads--;
			if (this.threads == 0)
				this.notifyAll();
			else
				this.wait();
		} catch (Exception e) {
		}
	}

	public synchronized void start() {
		this.notifyAll();
	}

	public void waitRst() {
		try {
			cdl.await();
		} catch (InterruptedException e) {
		}

		if(!LockTester.bInterrupt){
			double time1 = this.t1/(thrds*repeats);
			double time2 = this.t2/(thrds*repeats);
			double time3 = this.t3/(thrds*repeats);
			double time4 = this.t4/(thrds*repeats);
			double io = 1.0*iorate/(100-iorate);
			result.add("Threads:"+thrds);
			result.add("Repeats per Thread:"+repeats);
			result.add("ReadWrite Rate:"+String.format("%.2f",ToolUtil.getRound(io)));
			//String str2 = "per thread, per repeat, need time: ";
			result.add("read for Lock: latency="+time1+"ns, throughput="+1.0*1000000000/time1+"op/s");
			result.add("write for Lock: latency="+time2+"ns, throughput="+1.0*1000000000/time2+"op/s");
			result.add("read for Collections.synchronizedMap(new HashMap<String, Data>()): latency="+time3+"ns, throughput="+1.0*1000000000/time3+"op/s");
			result.add("write for Collections.synchronizedMap(new HashMap<String, Data>()): latency="+time4+"ns, throughput="+1.0*1000000000/time4+"op/s");
		}
	}

	public static void main(String[] args) throws Exception {
		LockTester testlock = new LockTester();
//		testlock.init(100, 500, 20);
//		testlock.start();
//		testlock.waitRst();
		testlock.run(100, 500, 20);
		System.out.println(testlock.getResult());
	}
	@Override
	public void run(int threads, int repeat, int rwrate) throws Exception{
		if(bRuning){
			throw new Exception("Program LockTester Is Runing By Other,Please Wait...");
		}
		bInterrupt=false;bRuning=true;
		init(threads, repeat, rwrate);
		start();
		waitRst();
		bRuning=false;
		if(bInterrupt){//中途停止
			throw new Exception("Program Has Been Interrupted.");
		}
	}
	@Override
	public void stop() throws Exception{
		bInterrupt=true;
	}
}
