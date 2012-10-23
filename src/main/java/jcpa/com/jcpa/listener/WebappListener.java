package com.jcpa.listener;

import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.Statement; 

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent; 
import javax.servlet.ServletContextListener; 

import org.hsqldb.Server; 

import com.jcpa.util.ToolUtil;
 
public class WebappListener implements ServletContextListener { 
	private ServletContext application = null;
	/**
	 * webapp启动
	 * */
    public void contextInitialized(ServletContextEvent sce) {
    	commonInit(sce);
    	checkFolder(sce);
    	startHsqlServer(sce);
    }
    
    /**
     * webapp停止
     * */
	public void contextDestroyed(ServletContextEvent sce) { 
        stopHsqlServer(sce);
    }
    
	/**
	 * 公共初始化
	 * */
	private void commonInit(ServletContextEvent sce){
		application = sce.getServletContext();  
		String webroot=sce.getServletContext().getRealPath("/").replace('\\', '/');
		application.setAttribute("WebRoot",webroot);
		application.setAttribute("JcpaSource",webroot+"WEB-INF/jcpasource/");
		application.setAttribute("Ruleset",webroot+"WEB-INF/ruleset/");
	}
	/**
	 * 检查目录
	 * */
	private void checkFolder(ServletContextEvent sce){
		try {
			ToolUtil.ifFolderExist((String)application.getAttribute("JcpaSource"), true);
			ToolUtil.ifFolderExist((String)application.getAttribute("Ruleset"), true);
			ToolUtil.ifFolderExist((String)application.getAttribute("Ruleset")+"tmp/", true);
		} catch (Exception e) {
			System.out.println("Error While checkFolder:"+e.getMessage());
			e.printStackTrace();
		}
	}
    /** 
     * 启动hsql数据库 
     */ 
    private void startHsqlServer(ServletContextEvent sce) {
    	try {
			// 获得数据库文件访问路径 
			String dbPath = sce.getServletContext().getInitParameter("hsql.dbPath"); 
			dbPath = (String)application.getAttribute("WebRoot")+dbPath;
			if (!dbPath.endsWith("/")){
				dbPath = dbPath + "/"; 
			}
			// 数据库文件名 
			String dbName = sce.getServletContext().getInitParameter("hsql.dbName"); 
			if (dbName==null || dbName.equals("")) { 
				System.out.println("[Error]:Can't get hsqldb.dbName from web.xml Context Param"); 
			    return; 
			}
			// 数据库访问端口 
			int port = -1; 
			try { 
			    port = Integer.parseInt(sce.getServletContext().getInitParameter("hsql.port")); 
			} catch (Exception e) { 
				System.out.println("[Error]:error while getting hsqldb.port from web.xml Context Param");
			    e.printStackTrace(); 
			    return; 
			}
			// 启动数据库 
			Server server = new Server();
			server.setDatabaseName(0, dbName); 
			server.setDatabasePath(0, dbPath + dbName); 
			if (port>0) server.setPort(port); 
			server.setSilent(true); 
			server.setTrace(true); 
			server.start();
			// 等待Server启动 
			try { 
			    Thread.sleep(800); 
			} catch (InterruptedException e) { 
			    e.printStackTrace(); 
			}
		} catch (Exception e) {
			System.out.println("Error While startHsqlServer:"+e.getMessage());
			e.printStackTrace();
		} 
    }
    /**
     * 停止hsql服务器
     * */
    private void stopHsqlServer(ServletContextEvent sce){
        try { 
        	Connection conn = null; 
        	String port=sce.getServletContext().getInitParameter("hsql.port");
        	String dbName=sce.getServletContext().getInitParameter("hsql.dbName");
        	String user=sce.getServletContext().getInitParameter("hsql.user");
        	String password=sce.getServletContext().getInitParameter("hsql.password");
            Class.forName("org.hsqldb.jdbcDriver");
            conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost:"+port+"/"+dbName,user,password); 
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("SHUTDOWN;"); 
        } catch (Exception e){
        	System.out.println("Error While stopHsqlServer:"+e.getMessage());
        	e.printStackTrace();
        }
    }
} 