package com.jcpa.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jcpa.servlet.Controller;
import com.jcpa.util.json.Json;


/**
 * @author zhujie
 * */
public class Action {
	protected ServletContext application;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected PrintWriter out;
	protected HttpSession session;
	protected String MethodName;
	/**
	 * Action Entrance
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse
	 * */
	public void execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			application=Controller.application;
			request=req;
			response=resp; 
			out=resp.getWriter();
			session = request.getSession();
			
			//prepare before action
			_prepare();
			//invoke different method by url parameter
			MethodName=request.getParameter("method");
			if(MethodName!=null && !MethodName.equals("")){
				Class<?> c=this.getClass();
				Method method=c.getMethod(MethodName);
				method.invoke(this,new Object[]{});
			}
			//cleanup after action
			_cleanup();
		} catch (Exception e) {
			e.printStackTrace();
			error(e.toString());
		}
	}
	
	/**
	 * prepare before action
	 */
	protected void _prepare() throws Exception{}
	/**
	 * cleanup after action
	 */
	protected void _cleanup() throws Exception{}
	
	/**
	 * ajax return success info
	 * @param info String
	 */
	protected void success(String info) throws IOException
	{
		out.print(Json.success(info));
	}	
	/**
	 * ajax return error info
	 * @param info String
	 */
	protected void error(String info) throws IOException
	{
		out.print(Json.error(info));
	}
	/**
	 * ajax echo
	 * */
	protected void echo(String data) throws IOException{
		out.print(data);
	}
}
