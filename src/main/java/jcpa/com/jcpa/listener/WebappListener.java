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
	 * webapp startup
	 * */
    public void contextInitialized(ServletContextEvent sce) {
    	commonInit(sce);
    	checkFolder(sce);
    	startHsqlServer(sce);
    }
    
    /**
     * webapp stop
     * */
	public void contextDestroyed(ServletContextEvent sce) { 
        stopHsqlServer(sce);
    }
    
	/**
	 * common init
	 * */
	private void commonInit(ServletContextEvent sce){
		application = sce.getServletContext();  
		String webroot=sce.getServletContext().getRealPath("/").replace('\\', '/');
		application.setAttribute("WebRoot",webroot);
		application.setAttribute("JcpaSource",webroot+"WEB-INF/jcpasource/");
		application.setAttribute("Ruleset",webroot+"WEB-INF/ruleset/");
		application.setAttribute("Classes",this.getClass().getResource("/").getPath().replace('\\', '/'));
	}
	/**
	 * check folder
	 * */
	private void checkFolder(ServletContextEvent sce){
		try {
			ToolUtil.ifFolderExist((String)application.getAttribute("JcpaSource"), true);
			ToolUtil.ifFolderExist((String)application.getAttribute("Ruleset"), true);
			ToolUtil.ifFolderExist((String)application.getAttribute("Ruleset")+"tmp/", true);
			ToolUtil.ifFileExist((String)application.getAttribute("Ruleset")+"desc.txt",true,"","UTF-8");
			ToolUtil.ifFileExist((String)application.getAttribute("WebRoot")+"WEB-INF/user.dat",true,"admin\tadmin","UTF-8");
		} catch (Exception e) {
			System.out.println("Error While checkFolder:"+e.getMessage());
			e.printStackTrace();
		}
	}
    /** 
     * start hsqldb 
     */ 
    private void startHsqlServer(ServletContextEvent sce) {
    	try {
			//db file path
			String dbPath = sce.getServletContext().getInitParameter("hsql.dbPath"); 
			dbPath = (String)application.getAttribute("WebRoot")+dbPath;
			if (!dbPath.endsWith("/")){
				dbPath = dbPath + "/"; 
			}
			// dbname
			String dbName = sce.getServletContext().getInitParameter("hsql.dbName"); 
			if (dbName==null || dbName.equals("")) { 
				System.out.println("[Error]:Can't get hsqldb.dbName from web.xml Context Param"); 
			    return; 
			}
			// db port
			int port = -1; 
			try { 
			    port = Integer.parseInt(sce.getServletContext().getInitParameter("hsql.port")); 
			} catch (Exception e) { 
				System.out.println("[Error]:error while getting hsqldb.port from web.xml Context Param");
			    e.printStackTrace(); 
			    return; 
			}
			// start db
			Server server = new Server();
			server.setDatabaseName(0, dbName); 
			server.setDatabasePath(0, dbPath + dbName); 
			if (port>0) server.setPort(port); 
			server.setSilent(true); 
			server.setTrace(true); 
			server.start();
			
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
     * stop hsqldb
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