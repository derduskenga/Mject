package com.harambesa.user;  

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;

import java.util.logging.Logger;

import java.io.PrintWriter;
import java.io.IOException;

import java.util.HashMap;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

 
import org.json.simple.JSONObject;
import org.json.simple.JSONValue; 

import com.harambesa.DBConnection.DBConnection; 
import com.harambesa.settings.settingsUtil;

public class UserDashboard extends HttpServlet{
	Logger logger = null;
	PrintWriter out = null;
	Connection conn = null;

	public void init() throws ServletException{ 
		logger=Logger.getLogger(UserDashboard.class.getName()); 
	}


	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException,IOException{
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");	 
		out = response.getWriter();
		HttpSession session = request.getSession();
		conn =new DBConnection()._getConnection();
		String currently_logged_user = (String)session.getAttribute("entity_id");
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String donationRequestsJson =null;
		String tag = request.getParameter("tag").trim();
		String user_id = "";

		if(request.getParameter("user_id")!=null){
			user_id = request.getParameter("user_id");
		}
		 
		logger.info(settingsUtil.getLoggerLine(Thread.currentThread(),this,"user_id"+user_id));

		try{ 

			// Fetch details of another user
			if(tag.equals("user_donation_request_tag") && !user_id.trim().isEmpty()){
				ArrayList<HashMap> donationRequests = settingsUtil.entryExistsMap(conn, user_id, "entity_id", "donation_requests");
				// add user request to json object 
				donationRequestsJson = JSONValue.toJSONString(donationRequests);

			}
			// Fetch details of another user
			else if(tag.equals("user_offer_application_tag") && !user_id.trim().isEmpty()){
				ArrayList<HashMap> donationOffers = settingsUtil.entryExistsMap(conn, user_id, "entity_id", "offers");
				// add user request to json object 
				donationRequestsJson = JSONValue.toJSONString(donationOffers); 
			} 
			// fetch currently logged in user
			else if(tag.equals("user_donation_request_tag")){ 
				// get all request by user id
				ArrayList<HashMap> donationRequests = settingsUtil.entryExistsMap(conn, currently_logged_user, "entity_id", "donation_requests");
				// String[] conditions = {"name","mash"};
				// ArrayList<HashMap> m = settingsUtil.filter(conn, "donation_requests",conditions);

				// add user request to json object 
				donationRequestsJson = JSONValue.toJSONString(donationRequests);
				// output to browser 
				// System.out.println(donationRequests);
				// System.out.println(donationRequestsJson);

			}  
			// fetch currently logged in user
			else if(tag.equals("user_offer_application_tag")){ 
				// get all request by user id
				ArrayList<HashMap> donationOffers = settingsUtil.entryExistsMap(conn, currently_logged_user, "entity_id", "offers");
				// String[] conditions = {"name","mash"};
				// ArrayList<HashMap> m = settingsUtil.filter(conn, "donation_requests",conditions);

				// add user request to json object 
				donationRequestsJson = JSONValue.toJSONString(donationOffers);
				// output to browser 
				// System.out.println(donationRequests);
				// System.out.println(donationRequestsJson);

			}
			 
		}finally{
			try{ 

				if(conn!=null){
					conn.close();
				}

			}catch(SQLException e){
				e.printStackTrace();
			}
		}

		out.print(donationRequestsJson);  
	}
}