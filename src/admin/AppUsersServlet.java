package com.harambesa.admin;  

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
 
import org.json.simple.JSONObject;
import org.json.simple.JSONArray; 

import java.io.IOException;
import java.io.PrintWriter;

import java.util.logging.Logger; 
import java.util.Calendar;
import java.util.Date;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import java.text.SimpleDateFormat;

import com.harambesa.security.PasswordHash;
import com.harambesa.DBConnection.DBConnection;
import com.harambesa.settings.settingsUtil;

import java.util.HashMap;
import java.util.ArrayList;

public class AppUsersServlet extends HttpServlet { 
	Logger logger = null;
	Connection conn = null;
	PrintWriter out = null;

	public void init() throws ServletException{
		logger = Logger.getLogger(AppUsersServlet.class.getName());
		conn = new DBConnection()._getConnection();

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException,IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
    	String duration = request.getParameter("duration");
    	String tag = request.getParameter("tag");
    	String searchValue = request.getParameter("searchValue");

    	out = response.getWriter();
    	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

    	try{
	    	// new users
	    	if(tag.equals("tag_new_users")){		    	
	    		ResultSet rs = newUsers(duration);
	    		if(rs!=null){
	    			// output the current row of data
	    			// then go to next row
	    			JSONArray userArray = new JSONArray();
	    			do{
					JSONObject jsonData = new JSONObject();
	    				logger.info(rs.getString("first_name")+":"+rs.getString("last_name")); 
	    				jsonData.put("firstName",rs.getString("first_name"));
	    				jsonData.put("lastName",rs.getString("last_name"));
	    				jsonData.put("userName",rs.getString("user_name"));
	    				jsonData.put("email",rs.getString("primary_email"));
	    				if(rs.getString("is_active").equals("t")){
	    					jsonData.put("active","True");
	    				}else if(rs.getString("is_active").equals("f")){
	    					jsonData.put("active","False");
	    				}  

	    				jsonData.put("dateEnroled",formatter.format(rs.getTimestamp("date_enroled")));
	    				userArray.add(jsonData);
	    			}while(rs.next());  
	    			out.println(userArray);
	    		}

		    	 
	    	}else if(tag.equals("tag_active_users")){
	    		String totalSessionsActive = (request.getServletContext().getAttribute("totalSessionsActive")).toString();
	    		out.println(""+totalSessionsActive);

	    	}else if(tag.equals("tag_registered_users")){
	    		String sql = "select * from entitys where is_active= 'true' ";
	    		Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);  
				if(rs.next()){ 
					JSONArray userArray = new JSONArray();
	    			while(rs.next()){
						JSONObject jsonData = new JSONObject();
	    				logger.info(rs.getString("first_name")+":"+rs.getString("last_name")); 
	    				jsonData.put("firstName",rs.getString("first_name"));
	    				jsonData.put("lastName",rs.getString("last_name"));
	    				jsonData.put("userName",rs.getString("user_name"));
	    				jsonData.put("email",rs.getString("primary_email"));
	    				if(rs.getString("is_active").equals("t")){
	    					jsonData.put("active","True");
	    				}else if(rs.getString("is_active").equals("f")){
	    					jsonData.put("active","False");
	    				}  

	    				jsonData.put("dateEnroled",formatter.format(rs.getTimestamp("date_enroled")));
	    				userArray.add(jsonData);
	    			}  
	    			out.println(userArray);
				}else{ 
					logger.info(settingsUtil.getLoggerLine(Thread.currentThread(),this,"No record exists"));
					
				} 

	    	}else if(tag.equals("tag_search_users") && !searchValue.trim().isEmpty()){
				JSONArray userArray = new JSONArray();
					
				logger.info(settingsUtil.getLoggerLine(Thread.currentThread(),this,searchValue));

	    		// use search value to retrieve records with 
	    		// the given value in either first name,lastname, username
	    		String sqlSearch = "SELECT * FROM entitys "+
	    					" WHERE first_name ilike ? "+
	    					" OR last_name ilike ? "+
	    					" OR user_name ilike ? ";
	    		PreparedStatement pstmt = conn.prepareStatement(sqlSearch);
	    		pstmt.setString(1,"%"+searchValue.toLowerCase()+"%");
	    		pstmt.setString(2,"%"+searchValue.toLowerCase()+"%");
	    		pstmt.setString(3,"%"+searchValue.toLowerCase()+"%");

	    		ResultSet rs = pstmt.executeQuery();

	    		if(rs.next()){ 
	    			while(rs.next()){
						JSONObject jsonData = new JSONObject();
	    				logger.info(rs.getString("first_name")+":"+rs.getString("last_name")); 
	    				jsonData.put("entityId",rs.getString("entity_id"));
	    				jsonData.put("firstName",rs.getString("first_name"));
	    				jsonData.put("lastName",rs.getString("last_name"));
	    				jsonData.put("userName",rs.getString("user_name"));
	    				jsonData.put("email",rs.getString("primary_email"));
	    				if(rs.getString("is_active").equals("t")){
	    					jsonData.put("active","True");
	    				}else if(rs.getString("is_active").equals("f")){
	    					jsonData.put("active","False");
	    				}  

	    				jsonData.put("dateEnroled",formatter.format(rs.getTimestamp("date_enroled")));
	    				userArray.add(jsonData);
	    			}  
				}else{  
					logger.info(settingsUtil.getLoggerLine(Thread.currentThread(),this,"No record exists"));

				} 

	    		// search using email

	    		out.println(userArray);


	    	}
	    }catch(SQLException sqlE){
    		logger.severe(sqlE.getMessage());
    	}

    	


    }

    /**
    *
    *	Get people added between today and month specified
    *
    *	@param 	the period in months to look back upto
    *
    *
    *	@return		resultset object containing people in created period specified,with cursor at first row
    *
    */
    public ResultSet newUsers(String period) throws SQLException{
    	ResultSet rSet = null;
    	// get calendar instance
    	Calendar calenderToday = Calendar.getInstance();
    	// get todays date in milliseconds
    	long todayMills = calenderToday.getTimeInMillis();
    	// deduct/substract the period eg 2 month for todays month
    	period = "-"+period;
    	calenderToday.add(calenderToday.MONTH, Integer.parseInt(period) );
    	// calender has been reset back by month provide in the period variable
    	// get this new date in milliseconds
    	long prediodB4TodayInMills = calenderToday.getTimeInMillis();
    	// make this new date a sql timestamp
    	Timestamp perdiodB4TodayTimestamp = new Timestamp(prediodB4TodayInMills);
    	// get today as sql timestamp
    	Timestamp todayTimeStamp = new Timestamp(todayMills);


    	String sql = "select * from entitys where date_enroled "+
    	"between '"+perdiodB4TodayTimestamp+"' and '"+todayTimeStamp+"' "+
    	"order by date_enroled DESC";

    	PreparedStatement pstmt = conn.prepareStatement(sql);
    	ResultSet rs = pstmt.executeQuery();

    	if(rs.next()){
    		rSet = rs;
    		logger.info("success");
    	}else{
    		rSet = null;
    		logger.info("no data was found");
    	} 
    	return rSet;

    }
    

    public void destroy(){
    	try{
    		conn.close();
    	}catch(SQLException sqlE){
    		logger.info("Connection object does not exit on servlet destruction");
    	}
    	
    }


}