package com.jcpa.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import com.jcpa.beans.Pattern;
import com.jcpa.dao.sql.PatternDaoImpl;
import com.jcpa.dao.sql.interfaces.PatternDao;
import com.jcpa.util.AuxUtil;
import com.jcpa.util.ToolUtil;
import com.jcpa.util.json.Json;
import com.jcpa.util.json.JsonArrayNode;
import com.jcpa.util.json.JsonLeafNode;
import com.jcpa.util.json.JsonObjectNode;

public class PatternAction extends Action{
	private static Map<String,Integer> MethodsPriority = new HashMap<String,Integer>();
    static {
    	MethodsPriority.put("add",1);
    	MethodsPriority.put("delete",1);
    	MethodsPriority.put("update",1);
    	MethodsPriority.put("get",2);
    	MethodsPriority.put("page",2);
    	MethodsPriority.put("rulesets",2);
    	MethodsPriority.put("delruleset",1);
    	MethodsPriority.put("addruleset",1);
    	MethodsPriority.put("upRuleset",1);
    	MethodsPriority.put("viewRuleset",2);
    	MethodsPriority.put("downRuleset",2);
    	MethodsPriority.put("javaClasses",1);
    }
	/**
	 *prepare to before action
	 */
	protected void _prepare() throws Exception{
		String user=(String)session.getAttribute("user");
		
		if(user==null || user.equals("guest")){
			Integer priority=MethodsPriority.get(MethodName);
			if(priority!=null && priority<2){
				throw new Exception("You have no power to access this page!");
			}
		}
	}
	/**
	 *cleanup after action
	 */
	protected void _cleanup() throws Exception{}
	
	/**
	 * add pattern
	 * */
	public void add() throws Exception{		
		Pattern p=new Pattern();	
		try {
			p.setName(request.getParameter("name"));
			p.setPatternType(request.getParameter("type"));
			p.setJavaClass(request.getParameter("javaclass"));
			p.setExpression(request.getParameter("expression"));
			p.setAux(request.getParameter("aux"));
			p.setWarning(request.getParameter("warning"));
			p.setCategory(request.getParameter("category"));
			p.setScope(request.getParameter("scope"));
			p.setExample(request.getParameter("example"));
			p.setPriority(Integer.parseInt(request.getParameter("priority")));
		} catch (Exception e) {
			error("Pattern Add Fail:"+e.getMessage());
			return;
		}
		
		PatternDao dao=new PatternDaoImpl();
		boolean bResult=false;
		try{
			bResult=dao.add(p);
		}catch(Exception e){
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
	 * delete pattern
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
	 * update pattern
	 * */
	public void update() throws Exception{
		Pattern p=new Pattern();	
		try {
			p.setId(Integer.parseInt(request.getParameter("id")));
			p.setName(request.getParameter("name"));
			p.setPatternType(request.getParameter("type"));
			p.setJavaClass(request.getParameter("javaclass"));
			p.setExpression(request.getParameter("expression"));
			p.setAux(request.getParameter("aux"));
			p.setWarning(request.getParameter("warning"));
			p.setCategory(request.getParameter("category"));
			p.setScope(request.getParameter("scope"));
			p.setExample(request.getParameter("example"));
			p.setPriority(Integer.parseInt(request.getParameter("priority")));
		} catch (Exception e) {
			error("Pattern Update Fail:"+e.getMessage());
			return;
		}
		
		PatternDao dao=new PatternDaoImpl();
		boolean bResult=false;
		try{
			bResult=dao.update(p);
		}catch(Exception e){
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
	 * get pattern by id
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
	 * list page pattern
	 * */
	public void page() throws Exception{
		int ONE_PAGE_COUNT=ToolUtil.strToPositiveInt(request.getParameter("rp"),25);;//patterns one page 
		int page=ToolUtil.strToPositiveInt(request.getParameter("page"),1);//curpage
		
		try {
			PatternDao dao=new PatternDaoImpl();
			List<Pattern> list = dao.list(page, ONE_PAGE_COUNT, "","ID desc");
			long Count=dao.count("");
			JsonObjectNode j=new JsonObjectNode("");
			j.addChild(new JsonLeafNode("page",String.valueOf(page)));
			j.addChild(new JsonLeafNode("total",String.valueOf(Count)));
			JsonArrayNode rows=new JsonArrayNode("rows");
			for(Pattern p:list){
				rows.addItem(p.getTBJsonNode());
			}
			
			j.addChild(rows);
			echo(j.toString());
		} catch (Exception e) {
			error("Data Access Fail:"+e.getMessage());
		}
	}
	/**
	 * get rulesets file list
	 * */
	public void rulesets() throws Exception{
		Json j = new Json(1);
		JsonObjectNode data = j.createData();
		JsonArrayNode rules=new JsonArrayNode("rules");
		data.addChild(rules);
		List<String> files=ToolUtil.getFileFromPath((String)application.getAttribute("Ruleset"),"xml");
		for(String f:files){
			rules.addItem(new JsonLeafNode("",f));
		}
		
		data.addChild(getRulesetDescJsonArr());
		echo(j.toString());
	}
	/**
	 * delete ruleset
	 * */
	public void delruleset() throws Exception{
		try {
			String rule = request.getParameter("rule");
			String fn=(String)application.getAttribute("Ruleset")+rule;
			File ruleFile = new File(fn);
			if(ruleFile.delete()){
				success("delete success");
				delRulesetDesc(rule);
				File pRuleFile=new File(fn+".plugin");
				pRuleFile.delete();
			}else{
				error("file delete fail");
			}
		} catch (Exception e) {
			error("file delete fail:"+e.getMessage());
		}
	}
	/**
	 * gernate a ruleset xml file using some patterns
	 * */
	public void addruleset() throws Exception{
		String shortxmlfn,xmlfn,type,cond,desc;
		try {
			//filename gernated
			shortxmlfn = request.getParameter("fn");
			if(shortxmlfn==null || shortxmlfn.equals("")){
				error("Please Input FileName!");
				return;
			}
			if(!shortxmlfn.endsWith(".xml")){
				shortxmlfn+=".xml";
			}
			xmlfn = (String)application.getAttribute("Ruleset")+shortxmlfn;
			//Condition
			type = request.getParameter("type");
			if(type.equals("all")){//all patterns
				cond="";
			}else{//pattern meet the conditions
				cond = request.getParameter("cond");
			}
			//description of ruleset
			desc=request.getParameter("desc");
			if(desc==null || desc.equals(""))desc="jcpa pmd rules";
			desc+=" -- generate by "+(String)session.getAttribute("user")+" at the time of "+ToolUtil.getTimeString();
			
			OutputStream out = new FileOutputStream(xmlfn);
			OutputStreamWriter fw = new OutputStreamWriter(out, "UTF-8");
			String buff="",tmp="";
			//write xml header
			buff="<?xml version=\"1.0\"?> <ruleset name=\"jcpa pmd rules\" xmlns=\"http://pmd.sourceforge.net/ruleset/2.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://pmd.sourceforge.net/ruleset/2.0.0 http://pmd.sourceforge.net/ruleset_2_0_0.xsd\" xsi:noNamespaceSchemaLocation=\"http://pmd.sourceforge.net/ruleset_2_0_0.xsd\">";
			fw.write(buff);
			buff="<description>"+desc+"</description>";
			fw.write(buff);
			//read the patterns by page and write to xml
			final int ONE_PAGE_COUNT = 100;
			PatternDao dao = new PatternDaoImpl();
			long Count = dao.count(cond);//patterns meet condition count
			long pages = ToolUtil.getSplitCount(Count, ONE_PAGE_COUNT);//sum page
			for (int i = 0; i < pages; i++) {
				List<Pattern> list = dao.list(i, ONE_PAGE_COUNT, cond,"");
				for(Pattern p:list) {
					buff="<rule name=\""+ p.getName()
							+ "\" language=\"java\" since=\"5.0\" scope=\""+p.getScope()
							+"\" message=\"\" externalInfoUrl=\"pattern.jsp?id="+p.getId()+"\" ";
					
					if(p.getPatternType()!=null && p.getPatternType().equals("java")){
						buff+="class=\"com.jcpa.pattern.javaclass."+p.getJavaClass()+"\">";
						buff+="<properties><property name=\"aux\"><value><![CDATA["
								+ p.getAux()+ "]]></value></property></properties>";
					}else{
						buff+="class=\"net.sourceforge.pmd.lang.rule.XPathRule\">";
						tmp="<properties><property name=\"xpath\"><value><![CDATA["
								+ AuxUtil.ExpIntegrate(p.getExpression(),p.getAux())
								+ "]]></value></property></properties>";
						buff+=tmp;
					}
					fw.write(buff);
					buff="<description>" + p.getWarning() + "</description>";
					fw.write(buff);
					buff="<priority>"+p.getPriority()+"</priority>";
					fw.write(buff);
					buff="<example><![CDATA[" + p.getExample() + "]]></example>";
					fw.write(buff);
					buff="</rule>";
					fw.write(buff);
				}
			}
			//write xml footer
			buff="</ruleset>";
			fw.write(buff);
			fw.flush();
			fw.close();
		} catch (Exception e) {
			error("xml file bulid error:"+e.getMessage());
			return;
		}
		success("ok");
		addRulesetDesc(shortxmlfn,desc);
	}
	/**
	 * view ruleset file
	 * */
	public void viewRuleset() throws Exception{
		try {
			//filename
			String fn = request.getParameter("file");
			fn = (String)application.getAttribute("Ruleset") + fn;
			//read file content
			String txt=ToolUtil.getFileConetent(fn,"UTF-8");
			// http header
			response.setContentType("text/xml");
			//print content to screen
			out.write(txt);
			out.flush();
		} catch (Exception e) {
			echo(e.getMessage());
		}
	}
	/**
	 * download ruleset
	 * */
	public void downRuleset() throws Exception{
		try {
			//filename
			String file = request.getParameter("file");
			String fn = (String)application.getAttribute("Ruleset") + file;
			String txt=ToolUtil.getFileConetent(fn,"UTF-8");
			//http header
			response.reset();
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition","attachment;"+ "filename=\""+file+"\"");
			//write to stream
			out.write(txt);
			out.flush();
		} catch (Exception e) {
			echo(e.getMessage());
		}
	}
	/**
	 * upload ruleset
	 * */
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void upRuleset() throws Exception{
		 /** 
         * enctype of the form must be multipart/... 
         */  
        if(ServletFileUpload.isMultipartContent(request)) { 
        	//description
			String desc=request.getParameter("desc");
			if(desc==null || desc.equals("")){
				desc="jcpa pmd rules";
			}else{
				desc=URLDecoder.decode(desc);
			}
			desc+=" -- upload by "+(String)session.getAttribute("user")+" at the time of "+ToolUtil.getTimeString();
			
        	String RulePath=(String)application.getAttribute("Ruleset");
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setRepository(new File(RulePath+"tmp"));  
            
            factory.setSizeThreshold(1024000);  
            ServletFileUpload sfu = new ServletFileUpload(factory);  
            //max file size  
            sfu.setFileSizeMax(102400000); 
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
            
            if(items!=null){
            	for(FileItem item:items){
                    if(!item.isFormField()) {   
                        String fileName = item.getName();
                        int index=fileName.lastIndexOf("\\");if(index<0)index=0;
                        fileName=fileName.substring(index); 
                        if(!fileName.endsWith(".xml"))fileName+=".xml";
                        BufferedInputStream in = new BufferedInputStream(item.getInputStream());  
                        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(RulePath+fileName)));  
                        Streams.copy(in, out, true);
                        files.addItem(new JsonLeafNode("",fileName));
                        addRulesetDesc(fileName,desc);
                    } 
            	}
            }
            
            echo(j.toString());
        } else {  
            error("enctype error!");  
        }  
	}
	/*list all javaClass*/
	public void javaClasses() throws Exception{
		Json j = new Json(1);
		JsonObjectNode data = j.createData();
		JsonArrayNode classes=new JsonArrayNode("classes");
		data.addChild(classes);
		String path=(String)application.getAttribute("Classes")+"/com/jcpa/pattern/javaclass/";
		List<String> files=ToolUtil.getFileFromPath(path,"class");
		for(String f:files){
			if(!f.equals("JcpaAbstractJavaRule.class")){
				classes.addItem(new JsonLeafNode("",f.substring(0,f.length()-6)));
			}
		}
		echo(j.toString());
	}
	/*******************************************************************************************************************************/
	private final String DESC_SPLIT="#@SPLIT@#";
	/**
	 * add ruleset description to file
	 * */
	private void addRulesetDesc(String filename,String desc){
		try {
			desc=desc.replaceAll("\r\n","\t");
			desc=desc.replaceAll("\n","\t");
			String fn=(String)application.getAttribute("Ruleset")+"desc.txt";

			String txt="";
			BufferedReader bufr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fn)), "UTF-8"));
			String r = bufr.readLine(),f;int index;
			while (r != null) {
				f="";
				index=r.indexOf(DESC_SPLIT);
				if(index!=-1){
					f=r.substring(0,index);
					if(!f.equals(filename)){
						txt+=r+"\r\n";
					}
				}
				r = bufr.readLine();
			}
			bufr.close();
			//add new line
			txt+=filename+DESC_SPLIT+desc+"\r\n";
			//write to file
			OutputStream out = new FileOutputStream(fn);
			OutputStreamWriter fw = new OutputStreamWriter(out, "UTF-8");
			fw.write(txt);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * delete ruleset description in file
	 * */
	private void delRulesetDesc(String filename){
		try {
			String fn = (String) application.getAttribute("Ruleset")+ "desc.txt";
			
			BufferedReader bufr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fn)), "UTF-8"));
			int index;
			String r = bufr.readLine(),f,txt="";
			while (r != null) {
				f="";
				index=r.indexOf(DESC_SPLIT);
				if(index!=-1){
					f=r.substring(0,index);
					if(!f.equals(filename)){
						txt+=r+"\r\n";
					}
				}
				r = bufr.readLine();
			}
			bufr.close();
			
			OutputStream out = new FileOutputStream(fn);
			OutputStreamWriter fw = new OutputStreamWriter(out, "UTF-8");
			fw.write(txt);
			fw.flush();
			fw.close();
		} catch (Exception e) {}
	}
	/**
	 * get ruleset description
	 * */
	private JsonArrayNode getRulesetDescJsonArr(){
		JsonArrayNode arr = new JsonArrayNode("desc");
		try {
			String fn = (String) application.getAttribute("Ruleset")+ "desc.txt";
			BufferedReader bufr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fn)), "UTF-8"));
			int index;
			String r = bufr.readLine(),f,d;
			while (r != null) {
				f="";d="";
				index=r.indexOf(DESC_SPLIT);
				if(index!=-1){
					f=r.substring(0,index);
					d=r.substring(index+DESC_SPLIT.length());
					JsonObjectNode desc=new JsonObjectNode("");
					desc.addChild(new JsonLeafNode("f",f));
					desc.addChild(new JsonLeafNode("d",d));
					arr.addItem(desc);
				}
				r = bufr.readLine();
			}
			bufr.close();
		} catch (Exception e) {}
		return arr;
	}
}
