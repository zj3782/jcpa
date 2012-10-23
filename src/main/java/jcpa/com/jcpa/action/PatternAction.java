package com.jcpa.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import com.jcpa.beans.Pattern;
import com.jcpa.dao.sql.PatternDaoImpl;
import com.jcpa.dao.sql.interfaces.PatternDao;
import com.jcpa.util.ToolUtil;
import com.jcpa.util.json.Json;
import com.jcpa.util.json.JsonArrayNode;
import com.jcpa.util.json.JsonLeafNode;
import com.jcpa.util.json.JsonObjectNode;

public class PatternAction extends Action{
	/**
	 * 执行动作之前的准备工作
	 */
	protected void _prepare() throws Exception{
		String user=(String)session.getAttribute("user");
		if(user==null || user.equals("guest"))throw new Exception("You have no power to access this page!");
	}
	/**
	 * 执行完动作之后的清理工作
	 */
	protected void _cleanup() throws Exception{}
	
	/**
	 * 添加pattern
	 * */
	public void add() throws Exception{		
		Pattern p=new Pattern();	
		try {
			p.setName(request.getParameter("name"));
			p.setExpression(request.getParameter("expression"));
			p.setWarning(request.getParameter("warning"));
			p.setCategory(request.getParameter("category"));
			p.setScope(request.getParameter("scope"));
			p.setExample(request.getParameter("example"));
			p.setPriority(Integer.parseInt(request.getParameter("priority")));
		} catch (Exception e) {//参数错误
			error("Pattern Add Fail:"+e.getMessage());
			return;
		}
		
		PatternDao dao=new PatternDaoImpl();
		boolean bResult=false;
		try{
			bResult=dao.add(p);
		}catch(Exception e){//产生异常
			error("Pattern Add Fail:"+e.getMessage());
			return;
		}
		if(bResult){
			Json j=new Json(1,"Pattern Add Success");
			j.setData(p.getTBJsonNode());
			echo(j.toString());
		}else{
			error("Pattern Add Fail");
		}
	}
	
	/**
	 * 删除pattern
	 * */
	public void delete() throws Exception{
		String ids=request.getParameter("ids");
		String[] idArr=ids.split(",");
		
		PatternDao dao=new PatternDaoImpl();
		int delCount=0;
		try{
			delCount=dao.delete(idArr);
		}catch(Exception e){
			error("Pattern Delete Fail:"+e.getMessage());
			return;
		}
		
		if(delCount>0){
			success(delCount+" Patterns Delete Success");
		}else{
			error("No Pattern Are Deleted.");
		}
	}
	
	/**
	 * 更新pattern
	 * */
	public void update() throws Exception{
		Pattern p=new Pattern();	
		try {
			p.setId(Integer.parseInt(request.getParameter("id")));
			p.setName(request.getParameter("name"));
			p.setExpression(request.getParameter("expression"));
			p.setWarning(request.getParameter("warning"));
			p.setCategory(request.getParameter("category"));
			p.setScope(request.getParameter("scope"));
			p.setExample(request.getParameter("example"));
			p.setPriority(Integer.parseInt(request.getParameter("priority")));
		} catch (Exception e) {//参数错误
			error("Pattern Update Fail:"+e.getMessage());
			return;
		}
		
		PatternDao dao=new PatternDaoImpl();
		boolean bResult=false;
		try{
			bResult=dao.update(p);
		}catch(Exception e){//产生异常
			error("Pattern Update Fail:"+e.getMessage());
			return;
		}
		if(bResult){
			Json j=new Json(1,"Pattern Update Success");
			j.setData(p.getTBJsonNode());
			echo(j.toString());
		}else{
			error("Pattern Update Fail");
		}
	}
	/**
	 * 获取指定id的pattern
	 * */
	public void get() throws Exception{
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			PatternDao dao = new PatternDaoImpl();
			Pattern p = dao.get(id);
			Json j = new Json(1, "ok");
			j.setData(p.getObjectNode("data"));
			echo(j.toString());
		} catch (Exception e) {
			error("Exception:"+e.getMessage());
		}
	}
	/**
	 * 列出第p页的pattern
	 * */
	public void page() throws Exception{
		int ONE_PAGE_COUNT=ToolUtil.strToPositiveInt(request.getParameter("rp"),25);;//一页pattern的个数 
		int page=ToolUtil.strToPositiveInt(request.getParameter("page"),1);//页码数
		
		try {
			PatternDao dao=new PatternDaoImpl();
			List<Pattern> list = dao.list(page, ONE_PAGE_COUNT, "");
			long Count=dao.count("");
			JsonObjectNode j=new JsonObjectNode("");
			j.addChild(new JsonLeafNode("page",String.valueOf(page)));
			j.addChild(new JsonLeafNode("total",String.valueOf(Count)));
			JsonArrayNode rows=new JsonArrayNode("rows");
			Iterator<Pattern> it = list.iterator();
			while(it.hasNext()){
				rows.addItem(it.next().getTBJsonNode());
			}
			
			j.addChild(rows);
			echo(j.toString());
		} catch (Exception e) {
			error("Data Access Fail:"+e.getMessage());
		}
	}
	/**
	 * 获得rulesets
	 * */
	public void rulesets() throws Exception{
		Json j = new Json(1);
		JsonObjectNode data = j.createData();
		JsonArrayNode rules=new JsonArrayNode("rules");
		data.addChild(rules);
		List<String> files=ToolUtil.getFileFromPath((String)application.getAttribute("Ruleset"),"xml");
		Iterator<String> it=files.iterator();
		while(it.hasNext()){
			rules.addItem(new JsonLeafNode("",it.next()));
		}
		echo(j.toString());
	}
	/**
	 * 删除某个ruleset
	 * */
	public void delruleset() throws Exception{
		try {
			String rule = request.getParameter("rule");
			File ruleFile = new File((String)application.getAttribute("Ruleset")+rule);
			if(ruleFile.delete()){
				success("delete success");
			}else{
				error("file delete fail");
			}
		} catch (Exception e) {
			error("file delete fail:"+e.getMessage());
		}
	}
	/**
	 * 生成patterns的xml文件
	 * */
	public void addruleset() throws Exception{
		try {
			//生成的文件名
			String fn = request.getParameter("fn");
			if(fn==null || fn.equals("")){
				error("Please Input FileName!");
				return;
			}
			fn = (String)application.getAttribute("Ruleset")+fn+".xml";
			String all = request.getParameter("all");
			String cond = request.getParameter("cond");
			if(all=="1")cond="";
			//写入xml头部
			OutputStream out = new FileOutputStream(fn);
			OutputStreamWriter fw = new OutputStreamWriter(out, "UTF-8");
			fw.write("<?xml version=\"1.0\"?> <ruleset name=\"jcpa pmd rules\" xmlns=\"http://pmd.sourceforge.net/ruleset/2.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://pmd.sourceforge.net/ruleset/2.0.0 http://pmd.sourceforge.net/ruleset_2_0_0.xsd\" xsi:noNamespaceSchemaLocation=\"http://pmd.sourceforge.net/ruleset_2_0_0.xsd\">");
			fw.write("<description>jcpa pmd ruleset</description>");
			//分页取出数据写入xml
			final int ONE_PAGE_COUNT = 100;
			PatternDao dao = new PatternDaoImpl();
			long Count = dao.count(cond);//数据总条数
			long pages = ToolUtil.getSplitCount(Count, ONE_PAGE_COUNT);//总页数
			for (int i = 0; i < pages; i++) {
				List<Pattern> list = dao.list(i, ONE_PAGE_COUNT, cond);
				Iterator<Pattern> it = list.iterator();
				while (it.hasNext()) {
					Pattern p = it.next();
					fw.write("<rule name=\""+ p.getName()
							+ "\" language=\"java\" since=\"5.0\" scope=\""+p.getScope()
							+"\" message=\"\" class=\"net.sourceforge.pmd.lang.rule.XPathRule\" externalInfoUrl=\"pattern.jsp?id="+p.getId()+"\">");
					fw.write("<description>" + p.getWarning()
							+ "</description>");
					fw.write("<priority>"+p.getPriority()+"</priority>");
					fw.write("<properties><property name=\"xpath\"><value><![CDATA["
							+ p.getExpression()
							+ "]]></value></property></properties>");
					fw.write("<example><![CDATA[" + p.getExample()
							+ "]]></example>");
					fw.write("</rule>");
				}
			}
			//写入xml尾部
			fw.write("</ruleset>");
			fw.flush();
			fw.close();
		} catch (Exception e) {
			error("xml file bulid error:"+e.getMessage());
			return;
		}
		success("ok");
	}
	/**
	 * 查看某个ruleset文件
	 * */
	public void viewRuleset() throws Exception{
		try {
			//文件名
			String fn = request.getParameter("file");
			fn = (String)application.getAttribute("Ruleset") + fn;
			//读取文件内容
			String txt=ToolUtil.getFileConetent(fn,"UTF-8");
			// 设置HTTP头：
			response.setContentType("text/xml");
			//写入内容
			out.write(txt);
			out.flush();
		} catch (Exception e) {
			echo(e.getMessage());
		}
	}
	/**
	 * 下载某个ruleset
	 * */
	public void downRuleset() throws Exception{
		try {
			//文件名
			String file = request.getParameter("file");
			String fn = (String)application.getAttribute("Ruleset") + file;
			//读取文件内容
			String txt=ToolUtil.getFileConetent(fn,"UTF-8");
			// 设置HTTP头：
			response.reset();
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition","attachment;"+ "filename=\""+file+"\"");
			//写入内容
			out.write(txt);
			out.flush();
		} catch (Exception e) {
			echo(e.getMessage());
		}
	}
	/**
	 * 上传ruleset
	 * */
	public void upRuleset() throws Exception{
		 /** 
         * form中的enctype必须是multipart/... 
         * 组件提供方法检测form表单的enctype属性 
         * 在isMultipartContent方法中同时检测了是否是post提交 
         * 如果不是post提交则返回false 
         */  
        if(ServletFileUpload.isMultipartContent(request)) {  
        	String RulePath=(String)application.getAttribute("Ruleset");
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setRepository(new File(RulePath+"tmp"));//临时文件目录  
            //内存最大占用  
            factory.setSizeThreshold(1024000);  
            ServletFileUpload sfu = new ServletFileUpload(factory);  
            //单个文件最大值byte  
            sfu.setFileSizeMax(102400000);
            //所有上传文件的总和最大值byte  
            sfu.setSizeMax(204800000);
            
            List<FileItem> items = null;  
            try {  
                items = sfu.parseRequest(request);  
            } catch (SizeLimitExceededException e) {  
                error("size limit exception!");
                return;
            } catch(Exception e) {
            	error("Exception:"+e.getMessage());
            	return;
            }

            Json j = new Json(1,"ok");
            JsonObjectNode data=j.createData();
            JsonArrayNode files=new JsonArrayNode("filename");
            data.addChild(files);
            
            Iterator<FileItem> iter = (items==null)?null:items.iterator();  
            while(iter != null && iter.hasNext()) {  
                FileItem item = (FileItem)iter.next();   
                //文件域  
                if(!item.isFormField()) {   
                    String fileName = item.getName();
                    int index=fileName.lastIndexOf("\\");if(index<0)index=0;
                    fileName=fileName.substring(index); 
                    if(!fileName.endsWith(".xml"))fileName+=".xml";
                    BufferedInputStream in = new BufferedInputStream(item.getInputStream());  
                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(RulePath+fileName)));  
                    Streams.copy(in, out, true);
                    files.addItem(new JsonLeafNode("",fileName));
                }  
            }
            
            echo(j.toString());
        } else {  
            error("enctype error!");  
        }  
	}
}
