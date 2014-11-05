package com.harambesa.gServices; 

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse; 
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Logout extends HttpServlet{

	private static Logger logger = LogManager.getLogger();
	
	HttpServletRequest request=null; 
	HttpServletResponse response=null;
	PrintWriter out = null;	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException{
			this.request=request;
			this.response=response;
			this.out = response.getWriter();	
			doPost(this.request, this.response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
			this.request=request;
			this.response=response;
			this.out = response.getWriter();	
			logoutUser();
	}
	
	public void logoutUser() throws IOException{
			try{
			
			HttpSession session = request.getSession(false);
			System.out.println("User="+session.getAttribute("user"));
			if(session != null){
				session.invalidate();
			}
			response.sendRedirect("/mjet/login");
			
			}catch(NullPointerException ex){
					logger.error("Error: "+HarambesaUtils.getStackTrace(ex));
			}
	}
}