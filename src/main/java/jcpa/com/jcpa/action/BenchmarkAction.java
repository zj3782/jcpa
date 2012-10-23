package com.jcpa.action;


import java.util.Iterator;
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
	/**
	 * 执行动作之前的准备工作
	 */
	protected void _prepare() throws Exception{}
	/**
	 * 执行完动作之后的清理工作
	 */
	protected void _cleanup() throws Exception{}
	
	/**
	 * 列出第p页的case
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
			Iterator<Benchmark> it = list.iterator();
			while(it.hasNext()){
				rows.addItem(it.next().getTBJsonNode());
			}
			j.addChild(rows);
			echo(j.toString());
		} catch (Exception e) {
			e.printStackTrace();
			error("Data Access Fail:"+e.getMessage());
		}
	}
	/**
	 * 运行
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
	 * 
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
}
