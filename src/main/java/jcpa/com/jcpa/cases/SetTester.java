package com.jcpa.cases;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import com.jcpa.util.ToolUtil;

public class SetTester extends Case{
	private CountDownLatch cdl;
	private int threads;
	private int thrds;
	private int repeats;
	private int iorate;
	public long t1 = 0;
	public long t2 = 0;
	public long t3 = 0;
	public long t4 = 0;
	
	protected static boolean bInterrupt=false;//interrupt run or not
	protected static boolean bRuning=false;//is running
	
	static Set<String> s1 = Collections.synchronizedSet(new HashSet<String>());
	static Set<String> s2 = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
	
	class ThreadWorker extends Thread {
		private int id;
		public ThreadWorker(int id) {
			this.id = id;
		}
		public int getThreadId(){return id;}
		@Override
		public void run() {
			int r = SetTester.this.repeats * SetTester.this.iorate/100;
			int w = SetTester.this.repeats - r;
			SetTester.this.latch();
			long t1 = System.nanoTime();
			for (int i = 0; i < w && !SetTester.bInterrupt; i++) {
				s1.add(String.valueOf(i));
			}
			long t2 = System.nanoTime();
	
			for (int i = 0; i < r && !SetTester.bInterrupt; i++) {
				s1.contains(String.valueOf(i));
			}
			long t3 = System.nanoTime();

			for (int i = 0; i < w && !SetTester.bInterrupt; i++) {
				s2.add(String.valueOf(i));
			}
			long t4 = System.nanoTime();
			
			for (int i = 0; i < r && !SetTester.bInterrupt; i++) {
				s2.contains(String.valueOf(i));
			}
			long t5 = System.nanoTime();
			
			if(!SetTester.bInterrupt){
				double diff1 = t2 - t1;
				double diff2 = t3 - t2;
				double diff3 = t4 - t3;
				double diff4 = t5 - t4;
				SetTester.this.t1 += diff1;
				SetTester.this.t2 += diff2;
				SetTester.this.t3 += diff3;
				SetTester.this.t4 += diff4;
			}
			SetTester.this.cdl.countDown();
		}
	}

	public void init(int threads, int repeats, int ioRate) {
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
		try{
			cdl.await();
		}catch (InterruptedException e) {}

		if(!SetTester.bInterrupt){
			long time1 = (long)( this.t1/(thrds*repeats) );
			long time2 = (long)( this.t2/(thrds*repeats) );
			long time3 = (long)( this.t3/(thrds*repeats) );
			long time4 = (long)( this.t4/(thrds*repeats) );
			long throug1 = (long)( 1.0*1000000000/time1 );
			long throug2 = (long)( 1.0*1000000000/time2 );
			long throug3 = (long)( 1.0*1000000000/time3 );
			long throug4 = (long)( 1.0*1000000000/time4 );
			double io = 1.0*iorate/(100-iorate);
			result.add("Threads:"+thrds);
			result.add("Repeats per Thread:"+repeats);
			result.add("ReadWrite Rate:"+String.format("%.2f",ToolUtil.getRound(io)));
			result.add("read for Collections.synchronizedSet(new HashSet<String>()): latency="+time1+"ns, throughput="+throug1+"op/s");
			result.add("write for Collections.synchronizedSet(new HashSet<String>()): latency="+time2+"ns, throughput="+throug2+"op/s");
			result.add("read for Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>()): latency="+time3+"ns, throughput="+throug3+"op/s");
			result.add("write for Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>()): latency="+time4+"ns, throughput="+throug4+"op/s");
		}
	}

	public void run(int threads,int repeat,int rwrate) throws Exception{
		if(bRuning){
			throw new Exception("Program SetTester Is Runing By Other,Please Wait...");
		}
		bInterrupt=false;bRuning=true;
		init(threads, repeat, rwrate);
		start();
		waitRst();
		bRuning=false;
		if(bInterrupt){//interrupt
			throw new Exception("Program Has Been Interrupted.");
		}
	}
	public void stop()throws Exception{
		bInterrupt=true;
	}
}
