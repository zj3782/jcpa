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
 * Action基类，其他action都继承自本类
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
	 * Action的入口函数，在control中通过调用此方法进行相应的动作
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
			
			//执行动作之前的准备工作
			_prepare();
			//根据method参数调用不同的方法
			MethodName=request.getParameter("method");
			if(MethodName!=null && !MethodName.equals("")){
				Class<?> c=this.getClass();
				Method method=c.getMethod(MethodName);
				method.invoke(this,new Object[]{});
			}
			//执行完动作之后的清理工作
			_cleanup();
		} catch (Exception e) {
			e.printStackTrace();
			echo(e.getMessage());
		}
	}
	
	/**
	 * 执行动作之前的准备工作
	 */
	protected void _prepare() throws Exception{}
	/**
	 * 执行完动作之后的清理工作
	 */
	protected void _cleanup() throws Exception{}
	
	/**
	 * ajax返回正确消息
	 * @param info String 返回的信息
	 */
	protected void success(String info) throws IOException
	{
		out.print(Json.success(info));
	}	
	/**
	 * ajax返回错误消息
	 * @param info String 返回的信息
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
