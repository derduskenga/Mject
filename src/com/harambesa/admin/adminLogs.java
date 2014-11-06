package com.harambesa.admin;

import com.harambesa.DBConnection.DBConnection;
import com.harambesa.mailing.Mail;
import com.harambesa.gServices.HarambesaUtils;
import com.harambesa.security.PasswordHash;
import com.harambesa.gServices.RequestProcessing;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;

import java.util.logging.Logger;
import java.util.List;
import java.util.Date;
import java.util.Random;
import java.util.Iterator;
import java.sql.ResultSet;
import java.sql.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import java.io.PrintWriter;
import java.io.IOException;
import java.io.*;

public class adminLogs extends HttpServlet{

        	HttpServletRequest request = null;
			HttpServletResponse response = null;
			Logger log = null;
			PrintWriter out = null;
			String fname = null;
			String sname = null;
			String adminId = null;
			String login = null;
			String logout = null;
			String uname = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

				request=request;
				response=response;
				log = Logger.getLogger(adminLogs.class.getName());
				out = response.getWriter();	
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				doPost(request,response);

	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
             
            this.request=request;
			this.response=response;
			this.response.setContentType("application/json");
			this.response.setCharacterEncoding("UTF-8");
			out = this.response.getWriter();
			log =Logger.getLogger(adminLogs.class.getName());

			String tag = request.getParameter("tag");

			if(tag.equals("update_logs")){
				  recordLogs(request,response);
			}else if(tag.equals("get_admin_logs")){
				  viewLogs();
			}else{
                log.info("Taq not found");
			}
	}

	public void recordLogs(HttpServletRequest request, HttpServletResponse response){
		//create a new session if its already not created
		HttpSession session = request.getSession();

				//get the session's time of creation
		Date createTime = new Date(session.getCreationTime());

		//get the time the page was accesed last
		Date lastAccessTime = new Date(session.getLastAccessedTime());

		 adminId = (String)session.getAttribute("aId");
		 fname = (String)session.getAttribute("fName");
		 sname = (String)session.getAttribute("sName");
		 uname = (String)session.getAttribute("uName");
		 String login = (String)createTime.toString();
		 String logout = (String)lastAccessTime.toString();

	 	DBConnection db = new DBConnection();
		Connection conn = db._getConnection();

    	try{


			String sql = "INSERT INTO";
			       sql +=" admin_logs(first_name,admin_id,login_time,logout_time,second_name,user_name)";
			       sql +=" VALUES(?,?,?,?,?,?)";
			log.info(sql);

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, fname);
			ps.setInt(2,Integer.parseInt(adminId));
			ps.setString(3, login);
			ps.setString(4, logout);
			ps.setString(5, sname);
			ps.setString(6, uname);
  
            JSONObject obj = new JSONObject();
			ResultSet rst = ps.executeQuery();

			if(rst != null){
                obj.put("Succes", 1);
                obj.put("message", "Logs updated");
                out.println(obj);
  			}



		}catch(SQLException ex){
			log.severe(HarambesaUtils.getStackTrace(ex));
		}catch(Exception ex){

            log.severe(HarambesaUtils.getStackTrace(ex));

		}finally{
           db.closeDB();
		}
	}

public void viewLogs(){
	String search_key  = request.getParameter("search_log_key").toString();

	DBConnection db = new DBConnection();
	Connection conn = db._getConnection();

	//jsonbject to keep the rows
	JSONObject admins = new JSONObject();

	//Json array to keep the columns
	JSONArray jArray = new JSONArray();

	//counter to keep count of the total number of rows got
	int total = 0;

	try{

		String sql = "SELECT * FROM admin_logs WHERE";
		       sql += " first_name ilike'%search_key%'";
		       sql += " OR";
		       sql += " second_name ilike'%search_key%'";
		       sql += " OR";
		       sql += " user_name ilike'search_key'";

		      log.info(sql);

		   PreparedStatement ps = conn.prepareStatement(sql);

		    ResultSet rs = ps.executeQuery();

		    if(rs.next()){

		     do{
		    	//Json object to store individual columns
		    	JSONObject obj = new JSONObject();
		    	obj.put("fName", rs.getString("first_name"));
		    	obj.put("sName", rs.getString("second_name"));
		    	obj.put("uName", rs.getString("user_name"));
		    	obj.put("logIn", rs.getString("login_time"));
		    	obj.put("logOut", rs.getString("logout_time"));

		    //push obj to the jArray
		    	jArray.add(obj);

		    //increment counter by 1 then move next
		    	total += 1;

		     }while(rs.next());
		      admins.put("Succes",1);
		      admins.put("total",total);
		      admins.put("logs",jArray);
		      out.println(admins);

		    }



	}catch(SQLException ex){
		log.severe(HarambesaUtils.getStackTrace(ex));
	}catch(Exception ex){
		log.severe(HarambesaUtils.getStackTrace(ex));
	}
}

}