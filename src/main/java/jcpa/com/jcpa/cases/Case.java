package com.jcpa.cases;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class Case {
	protected List<String> result= new LinkedList<String>();
	/**
	 * 运行
	 * @return 
	 * */
	public abstract void run(int threads,int repeat,int rwrate) throws Exception;
	/**
	 * 停止
	 * */
	public abstract void stop()throws Exception;
	/**
	 * 获取运行结果
	 * */
	public String getResult(){
		String str="";
		Iterator<String> it=result.iterator();
		while(it.hasNext()){
			str+=it.next()+"\r\n";
		}
		return str;
	}
}
