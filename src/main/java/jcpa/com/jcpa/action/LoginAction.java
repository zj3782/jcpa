package com.jcpa.action;

import java.io.BufferedReader;
import java.io.FileReader;


public class LoginAction extends Action{
	private String from;
	/**
	 * 执行动作之前的准备工作
	 */
	protected void _prepare() throws Exception{
		from=request.getParameter("from");
		if(from==null || from.equals(""))from="index.jsp";
	}
	/**
	 * 执行完动作之后的清理工作
	 */
	protected void _cleanup() throws Exception{}
	
	/**
	 * 登录
	 * */
	public void in()throws Exception{
		String user=request.getParameter("user");
		String password=request.getParameter("password");
		
		if(check(user,password)){
			session.setAttribute("user",user);
			response.sendRedirect(from);
		}else{
			request.setAttribute("user",user);
			request.setAttribute("error","Username and password does not match!");
			request.getRequestDispatcher("login.jsp").forward(request,response);
		}
	}
	/**
	 * 登出
	 * */
	public void out()throws Exception{
		session.setAttribute("user","guest");
		response.sendRedirect(from);
	}
	/**
	 * 检查用户名和密码是否正确
	 * */
	private boolean check(String user,String password){
		try {
			String fn=(String)application.getAttribute("WebRoot")+"WEB-INF/user.dat";
			String up=user+"\t"+password;
			BufferedReader br=new BufferedReader(new FileReader(fn));
			String r=br.readLine();
			while(r!=null){
				if(r.endsWith("\r\n"))up+="\r\n";
				else if(r.endsWith("\n"))up+="\n";
				if(r.equals(up))return true;
				r=br.readLine();
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
