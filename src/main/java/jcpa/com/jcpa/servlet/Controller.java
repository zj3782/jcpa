package com.jcpa.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jcpa.action.Action;
import com.jcpa.util.ToolUtil;
import com.jcpa.util.HibernateUtil;

public class Controller extends HttpServlet{

	private static final long serialVersionUID = 7834506469080071882L;

	public static ServletContext application = null;
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		application=getServletContext();
		//使得servlet输出中文不会报错
		resp.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
		
		String ActionName=ToolUtil.regexGet(req.getRequestURI(), "(\\w+)\\.do");
		if(ActionName==null || ActionName.equals(""))return;
		ActionName=ToolUtil.strHeadUpper(ActionName);
		
		Class<?> c = null;
		Action act = null;
		try {
			c = Class.forName("com.jcpa.action."+ActionName+"Action");
		} catch (Exception e) {
			req.getRequestDispatcher("404.jsp").forward(req,resp);
			return;
		}
		
		try {
			act=(Action) c.newInstance();
			act.execute(req,resp);
		} catch (Exception e) {
			req.getRequestDispatcher("404.jsp").forward(req,resp);
		}finally{
			HibernateUtil.closeSession();//关闭数据库session
		}
		
	}
}
