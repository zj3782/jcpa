package com.jcpa.action;


import java.util.List;

import com.jcpa.beans.Benchmark;
import com.jcpa.cases.Case;
import com.jcpa.dao.sql.BenchmarkDaoImpl;
import com.jcpa.dao.sql.interfaces.BenchmarkDao;
import com.jcpa.util.ToolUtil;
import com.jcpa.util.json.Json;
import com.jcpa.util.json.JsonArrayNode;
import com.jcpa.util.json.JsonLeafNode;
import com.jcpa.util.json.JsonObjectNode;

public class BenchmarkAction extends Action{

	protected void _prepare() throws Exception{}

	protected void _cleanup() throws Exception{}
	
	/**
	 * list page case
	 * */
	public void page() throws Exception{
		int ONE_PAGE_COUNT=ToolUtil.strToPositiveInt(request.getParameter("rp"),25);;//一页pattern的个数 
		int page=ToolUtil.strToPositiveInt(request.getParameter("page"),1);//页码数
		
		try {
			BenchmarkDao dao=new BenchmarkDaoImpl();
			List<Benchmark> list = dao.list(page, ONE_PAGE_COUNT, "");
			long Count=dao.count("");
			JsonObjectNode j=new JsonObjectNode("");
			j.addChild(new JsonLeafNode("page",String.valueOf(page)));
			j.addChild(new JsonLeafNode("total",String.valueOf(Count)));
			JsonArrayNode rows=new JsonArrayNode("rows");

			for(Benchmark b:list){
				rows.addItem(b.getTBJsonNode());
			}
			j.addChild(rows);
			echo(j.toString());
		} catch (Exception e) {
			e.printStackTrace();
			error("Data Access Fail:"+e.getMessage());
		}
	}
	/**
	 * run case
	 * */
	public void run() throws Exception{
		String caseName;
		String caseNames[];
		int threadNum,repeatNum,RWRate;
		try {
			caseName = request.getParameter("case");
			caseNames=caseName.split(",");
			threadNum = Integer.parseInt(request.getParameter("thread"));
			repeatNum = Integer.parseInt(request.getParameter("repeat"));
			RWRate = Integer.parseInt(request.getParameter("rwrate"));
			if(RWRate<0 || RWRate>100)throw new Exception("rwrate error");
		} catch (Exception e) {
			error("Args Error:"+e.getMessage());
			return;
		}
		Json j = new Json();
		JsonObjectNode data = j.createData();
		JsonArrayNode results = new JsonArrayNode("result");
		data.addChild(results);
		String sResult;
		for(String cn:caseNames){
			try {
				Class<?> c  = Class.forName("com.jcpa.cases."+cn);
				Case ca=(Case) c.newInstance();
				ca.run(threadNum,repeatNum,RWRate);
				sResult=ca.getResult();
				JsonObjectNode result = new JsonObjectNode("");
				result.addChild(new JsonLeafNode("r",sResult));
				result.addChild(new JsonLeafNode("n",cn));
				results.addItem(result);
			} catch (Exception e) {
				error("Run Error:"+e.getMessage());
				return;
			}
			try{
				BenchmarkDao dao=new BenchmarkDaoImpl();
				dao.update(cn,sResult);
			}catch (Exception e) {}
		}
		echo(j.toString());
	}
	/**
	 * stop run
	 * */
	public void stop() throws Exception{
		try {
			String caseName = request.getParameter("case");
			String caseNames[]=caseName.split(",");
			for(String cn:caseNames){
				Class<?> c  = Class.forName("com.jcpa.cases."+cn);
				Case ca=(Case) c.newInstance();
				ca.stop();
			}
			success("Stopping...");
		} catch (Exception e) {
			error("Stop Error:"+e.getMessage());
		}
	}
	
	/**
	 * add case
	 * */
	public void add() throws Exception{
		try {
			String user=(String)session.getAttribute("user");
			if(user==null || user.equals("guest")){
				throw new Exception("You have no power to addcase,please login.");
			}
			
			Benchmark b=new Benchmark();
			String name=request.getParameter("name");
			b.setName(name);
			b.setDescript(request.getParameter("desp"));

			String fn=(String)application.getAttribute("Classes")+"/com/jcpa/cases/"+name+".class";
			if(!ToolUtil.ifFileExist(fn,false,"","UTF-8")){
				throw new Exception("Add Fail.Class com.jcpa.cases."+name+" not found.");
			}
			
			BenchmarkDao dao=new BenchmarkDaoImpl();
			dao.add(b);
			success("Add Success");
		} catch (Exception e) {
			error(e.getMessage());
		}
	}
}
