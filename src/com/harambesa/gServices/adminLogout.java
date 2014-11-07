package com.harambesa.gServices; 

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse; 
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.logging.Logger;

public class adminLogout extends HttpServlet{

	private static Logger log = null;
	
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
			response.sendRedirect("/mjet/ADMIN/");
			
			}catch(NullPointerException ex){
				    log = Logger.getLogger(adminLogout.class.getName());
					log.severe(HarambesaUtils.getStackTrace(ex));
			}
	}
}