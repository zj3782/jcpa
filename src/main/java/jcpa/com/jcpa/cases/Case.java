package com.jcpa.cases;

import java.util.LinkedList;
import java.util.List;

public abstract class Case {
	protected List<String> result= new LinkedList<String>();
	/**
	 * run
	 * @return 
	 * */
	public abstract void run(int threads,int repeat,int rwrate) throws Exception;
	/**
	 * stop
	 * */
	public abstract void stop()throws Exception;
	/**
	 * get run result
	 * */
	public String getResult(){
		String str="";
		for(String s:result){
			str+=s+"\r\n";
		}
		return str;
	}
}
