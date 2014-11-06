package com.harambesa.settings; 

import com.harambesa.DBConnection.DBConnection;
import com.harambesa.gServices.HarambesaUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse; 
import javax.servlet.http.HttpSession;

import java.util.logging.Logger;
import java.sql.ResultSet;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.sql.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.*;
//my servelet class
public class Settings extends HttpServlet {
	HttpServletRequest request=null; 
	String entity_id = null;
	HttpServletResponse response=null;
	PrintWriter out = null;
	Logger logger = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException{
			doPost(request, response);//cal the doPost method
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

			this.request=request;
			this.response=response;
			this.response.setContentType("application/json");
			this.response.setCharacterEncoding("UTF-8");
			out = this.response.getWriter();
			logger=Logger.getLogger(Settings.class.getName());//attach this logger instance to this servlet
			checkLogin();
			processTag();
			getProfileInfo((String)request.getSession().getAttribute("entity_id"));


	}

//write a method to process all tags 
public void processTag(){
	//step1. get tag from request
	//step2. if tag is null return success 0 and appropriate message to the user
	//step3. if tag matches any of your predetermined tags, take appropriate action
	//as in call the rigth method
	//if tag does not match any of your predetermined tags, return the appropriate message to the user.

}

//metthod to fetch the loged in user's profile
  public void getProfileInfo(String entity_id){

	DBConnection db = new DBConnection();//instatiates the database connection object
	Connection con = db._getConnection();

	try{
		
		
        
     if(isDOBExist(entity_id)){ 
         

		String profile_fetch_query = "SELECT * FROM entitys WHERE entity_id=?";
		logger.info(profile_fetch_query);//checks if query has error
		PreparedStatement ps = con.prepareStatement(profile_fetch_query);//prepares our query for execution
		ps.setInt(1,Integer.parseInt(entity_id));
		ResultSet rs = ps.executeQuery();//executes our prepared query and store our results in rs variable
         

		if(rs != null){
             if(rs.next()){
             	//String dob_val = rs.getString("date_of_birth");

              	//create a new json object
              	JSONObject prof_details = new JSONObject();
              	//put the user details in the json object
              	prof_details.put("success",1);
              	prof_details.put("f_nm",rs.getString("first_name"));
              	prof_details.put("l_nm",rs.getString("last_name"));
              	prof_details.put("u_nm",rs.getString("user_name"));
              	prof_details.put("gender",rs.getString("gender"));
              	prof_details.put("dob",rs.getString("date_of_birth"));
              	prof_details.put("pic",rs.getString("profile_pic_path"));
              	prof_details.put("occ",rs.getString("occupation"));
              	prof_details.put("cntry",rs.getString("country"));
              	prof_details.put("addr",rs.getString("address"));
              	prof_details.put("p_code",rs.getString("postal_code"));
              	prof_details.put("cty",rs.getString("city"));
              	prof_details.put("p_mail",rs.getString("primary_email"));
              	prof_details.put("mbl_no",rs.getString("mobile_number"));
              	prof_details.put("street_addr",rs.getString("physical_street_address"));
              	prof_details.put("org",rs.getString("org_name"));
              	logger.info(rs.getString("profile_pic_path"));
              	prof_details.put("bio_details",rs.getString("details"));

            	out.println(prof_details);
            	out.close();
            }

		}
	  }else{
			JSONObject obj = new JSONObject();
			obj.put("profile_redirect", true);
			out.println(obj);
			  // response.sendRedirect(request.getContextPath()+"/complete-profile");
		}

	}
	catch(Exception e){
                    logger.severe(HarambesaUtils.getStackTrace(e));

					giveErrorFeedBack("Something went wrong when loading your Profile.");
	}
	finally{
		try{db.closeDB();}catch(Exception e){}
	}
  }


public boolean isDOBExist(String entity_id)throws SQLException{
	DBConnection db = new DBConnection();//instatiates the database connection object
	Connection con = db._getConnection();
	boolean flag = false;
	String sql = "SELECT date_of_birth FROM entitys WHERE entity_id=?";
	logger.info(sql);
	try{
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1,Integer.parseInt(entity_id));
			ResultSet rs = st.executeQuery();
			if(rs != null){
				if(rs.next()){
					if(rs.getString(1).trim().length()>0){
						flag=true;
					}
				}
			}
	}catch(SQLException sqlex){

        sqlex.printStackTrace(); 
	}catch(Exception ex){
		ex.printStackTrace();
	}finally{
		con.close();
	}
	return flag;	

}



  private void checkLogin(){
  	HttpSession session = request.getSession();
	entity_id=(String)session.getAttribute("entity_id");
	if(entity_id == null){
		//this user is not logged in
		//now send a redirect
		sendRedir();
	}
  }

  public void sendRedir(){
		JSONObject obj  = new JSONObject();
		obj.put("success", 0);
		obj.put("redir", "login");
		obj.put("message", "Your session has expired, please login to continue");
		out.println(obj);
		out.close();
  }

  	private void giveErrorFeedBack(String message){
				JSONObject obj=new JSONObject();
				obj.put("success", 0);
				obj.put("message", message);
				out.print(obj);
				out.close();
	}
}