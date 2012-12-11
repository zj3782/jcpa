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
	 * 执行动作之前的准备工作
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
			p.setPatternType(request.getParameter("type"));
			p.setJavaClass(request.getParameter("javaclass"));
			p.setExpression(request.getParameter("expression"));
			p.setAux(request.getParameter("aux"));
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
			p.setPatternType(request.getParameter("type"));
			p.setJavaClass(request.getParameter("javaclass"));
			p.setExpression(request.getParameter("expression"));
			p.setAux(request.getParameter("aux"));
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
	 * 获得rulesets
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
		//描述信息
		data.addChild(getRulesetDescJsonArr());
		echo(j.toString());
	}
	/**
	 * 删除某个ruleset
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
	 * 生成patterns的xml文件
	 * */
	public void addruleset() throws Exception{
		String shortxmlfn,xmlfn,pluginfn,type,cond,desc;
		try {
			//生成的文件名
			shortxmlfn = request.getParameter("fn");
			if(shortxmlfn==null || shortxmlfn.equals("")){
				error("Please Input FileName!");
				return;
			}
			if(!shortxmlfn.endsWith(".xml")){
				shortxmlfn+=".xml";
			}
			xmlfn = (String)application.getAttribute("Ruleset")+shortxmlfn;
			pluginfn=xmlfn+".plugin";
			//patterns条件
			type = request.getParameter("type");
			if(type.equals("all")){//all patterns
				cond="";
			}else{//符合条件的pattern
				cond = request.getParameter("cond");
			}
			//ruleset的description
			desc=request.getParameter("desc");
			if(desc==null || desc.equals(""))desc="jcpa pmd rules";
			desc+=" -- generate by "+(String)session.getAttribute("user")+" at the time of "+ToolUtil.getTimeString();
			
			OutputStream out = new FileOutputStream(xmlfn);
			OutputStreamWriter fw = new OutputStreamWriter(out, "UTF-8");
			OutputStream pout = new FileOutputStream(pluginfn);
			OutputStreamWriter pfw = new OutputStreamWriter(pout, "UTF-8");
			String buff="",buff2="",tmp="";
			//写入xml头部
			buff="<?xml version=\"1.0\"?> <ruleset name=\"jcpa pmd rules\" xmlns=\"http://pmd.sourceforge.net/ruleset/2.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://pmd.sourceforge.net/ruleset/2.0.0 http://pmd.sourceforge.net/ruleset_2_0_0.xsd\" xsi:noNamespaceSchemaLocation=\"http://pmd.sourceforge.net/ruleset_2_0_0.xsd\">";
			fw.write(buff);pfw.write(buff);
			buff="<description>"+desc+"</description>";
			fw.write(buff);pfw.write(buff);
			//分页取出数据写入xml
			final int ONE_PAGE_COUNT = 100;
			PatternDao dao = new PatternDaoImpl();
			long Count = dao.count(cond);//数据总条数
			long pages = ToolUtil.getSplitCount(Count, ONE_PAGE_COUNT);//总页数
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
						buff2=buff;
					}else{
						buff2=buff;
						buff+="class=\"net.sourceforge.pmd.lang.rule.XPathRule\">";
						buff2+="class=\"net.sourceforge.pmd.rules.XPathRule\">";
						tmp="<properties><property name=\"xpath\"><value><![CDATA["
								+ AuxUtil.ExpIntegrate(p.getExpression(),p.getAux())
								+ "]]></value></property></properties>";
						buff+=tmp;
						buff2+=tmp;
					}
					fw.write(buff);pfw.write(buff2);
					buff="<description>" + p.getWarning() + "</description>";
					fw.write(buff);pfw.write(buff);
					buff="<priority>"+p.getPriority()+"</priority>";
					fw.write(buff);pfw.write(buff);
					buff="<example><![CDATA[" + p.getExample() + "]]></example>";
					fw.write(buff);pfw.write(buff);
					buff="</rule>";
					fw.write(buff);pfw.write(buff);
				}
			}
			//写入xml尾部
			buff="</ruleset>";
			fw.write(buff);pfw.write(buff);
			fw.flush();pfw.flush();
			fw.close();pfw.close();
		} catch (Exception e) {
			error("xml file bulid error:"+e.getMessage());
			return;
		}
		success("ok");
		addRulesetDesc(shortxmlfn,desc);
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
			String plugin=request.getParameter("plugin");
			String txt;
			//是否当作导入plugin使用
			if(plugin!=null && plugin.equals("yes")){
				if(ToolUtil.ifFileExist(fn+".plugin",false,"", "UTF-8")){//对应的plugin文件存在
						txt=ToolUtil.getFileConetent(fn+".plugin","UTF-8");
				}else{//对于的plugin文件不存在，取出来进行替换（如果ruleset文件是上传的，其对应的plugin文件是不存在的）
						txt=ToolUtil.getFileConetent(fn,"UTF-8");
						txt=txt.replaceAll("class=\"net.sourceforge.pmd.lang.rule.XPathRule\"","class=\"net.sourceforge.pmd.rules.XPathRule\"");
				}
				if(file.endsWith(".xml"))file=file.substring(0,file.length()-4);
				file+=".plugin.xml";
			}else{
				txt=ToolUtil.getFileConetent(fn,"UTF-8");
			}
			
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
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void upRuleset() throws Exception{
		 /** 
         * form中的enctype必须是multipart/... 
         * 组件提供方法检测form表单的enctype属性 
         * 在isMultipartContent方法中同时检测了是否是post提交 
         * 如果不是post提交则返回false 
         */  
        if(ServletFileUpload.isMultipartContent(request)) {  //description
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
            
            if(items!=null){
            	for(FileItem item:items){
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
                        addRulesetDesc(fileName,desc);
                    } 
            	}
            }
            
            echo(j.toString());
        } else {  
            error("enctype error!");  
        }  
	}
	/*列出javaClass列表*/
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
	 * 添加ruleset描述信息
	 * */
	private void addRulesetDesc(String filename,String desc){
		try {
			desc=desc.replaceAll("\r\n","\t");
			desc=desc.replaceAll("\n","\t");
			String fn=(String)application.getAttribute("Ruleset")+"desc.txt";
			//读取
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
			//新添加的记录写在后面
			txt+=filename+DESC_SPLIT+desc+"\r\n";
			//写入文件
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
	 * 删除ruleset描述信息
	 * */
	private void delRulesetDesc(String filename){
		try {
			String fn = (String) application.getAttribute("Ruleset")+ "desc.txt";
			//从文件读取
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
			//写入文件
			OutputStream out = new FileOutputStream(fn);
			OutputStreamWriter fw = new OutputStreamWriter(out, "UTF-8");
			fw.write(txt);
			fw.flush();
			fw.close();
		} catch (Exception e) {}
	}
	/**
	 * 获取ruleset的描述
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
