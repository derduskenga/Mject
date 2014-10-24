package com.harambesa.admin;
import com.harambesa.DBConnection.DBConnection;
import com.harambesa.mailing.Mail;
import com.harambesa.gServices.HarambesaUtils;
import com.harambesa.security.PasswordHash;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;

import java.util.logging.Logger;
import java.util.List;
import java.util.Random;
import java.util.Iterator;
import java.sql.ResultSet;
import java.sql.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;


import java.io.PrintWriter;
import java.io.IOException;
import java.io.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.DiskFileUpload;


public class admin extends HttpServlet{

	HttpServletRequest request = null;
	HttpServletResponse response = null;
	Logger log = null;
	PrintWriter out = null;

	public void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException{
             
		        request=request;
				response=response;
				log = Logger.getLogger(admin.class.getName());
				out = response.getWriter();	
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				doPost(request,response);

	}
	public void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException{
			this.request=request;
			this.response=response;
			this.response.setContentType("application/json");
			this.response.setCharacterEncoding("UTF-8");
			out = this.response.getWriter();
			log =Logger.getLogger(admin.class.getName());

			String tag = request.getParameter("tag");

			if(tag.equals("log_in")){
				  adminLogin();
			}else if(tag.equals("create_admin")){
                  create_admin();
			}else if (tag.equals("view_admins")) {
				   view_admins();
			}else if(tag.equals("suspend")){
				suspendAdmin();
			}else if(tag.equals("disable_admin")){
				disAbleAdmin();
			}
			else{
				log.info("no such tag found");
			}

	}

	//public methods

	public void adminLogin(){

           DBConnection db = new DBConnection();
		   Connection conn = db._getConnection();

		try{
	

		   String username = request.getParameter("username").trim();
		   String password = request.getParameter("password").trim();
		   String adminLevel = request.getParameter("adminLevel");



           
           String fetch_admin_details_query = "SELECT * FROM admin_entitys WHERE user_name=?,entity_password=? AND entity_level=?";
           log.info(fetch_admin_details_query);
           PreparedStatement p = conn.prepareStatement(fetch_admin_details_query);

           p.setString(1,username);
           p.setString(2,password);
           p.setInt(3,Integer.parseInt(adminLevel));

           JSONObject object = new JSONObject();
           ResultSet statement = p.executeQuery();

           log.info("Authenticating adminstrator"+statement);

           if(statement != null){
		       
		       if(!statement.next()){
		       	  object.put("success",0);
		       	  object.put("message","Wrong Username or password");
		       	  out.print(object);
		       	  db.closeDB();
		       }else if(statement.next()){
		       	  object.put("success",1);
		       	  object.put("message","Log in Succesfull");
		       	  out.print(object);
		       	  db.closeDB();
		       }
           }



		}catch(SQLException ex){
			log.severe(HarambesaUtils.getStackTrace(ex));
			db.closeDB();
		}
		catch(Exception ex){
                  log.severe(HarambesaUtils.getStackTrace(ex));

				  giveErrorFeedBack("Something went wrong while loging you in.");
				  db.closeDB();
	  }
	}
	public void create_admin(){

		    DBConnection db = new DBConnection();
			Connection conn = db._getConnection();

		try{

			String firstname = request.getParameter("fname").trim();
			String secondname = request.getParameter("sname").trim();
			String tmppassword = request.getParameter("tmppassword").trim();
			String priemail = request.getParameter("pri_email").trim();
			String userlevel = request.getParameter("level").trim();

		   String newHashedPass = PasswordHash.createHash(tmppassword);

		   String param[] = newHashedPass.split(":");

			String create_admin_query = "INSERT INTO admin_entitys(first_name,last_name,user_name,"+
				"primary_email,entity_password,password_salt,entity_level) VALUES(?,?,?,?,?,?,?)";
			log.info(create_admin_query);
			PreparedStatement ps = conn.prepareStatement(create_admin_query);

			ps.setString(1,firstname);
			ps.setString(2,secondname);
			ps.setString(3,priemail);
			ps.setString(4,priemail);
			ps.setString(5,param[0]);
			ps.setString(6,param[1]);
			ps.setInt(7,Integer.parseInt(userlevel));


           JSONObject obj=new JSONObject();
			int rs = ps.executeUpdate();

				if(rs >= 1){
                    obj.put("success",1);
                    out.print(obj);
				}else if(rs < 1){
					obj.put("success",0);
				}else{

				}

		}catch(SQLException ex){
			log.severe(HarambesaUtils.getStackTrace(ex));
			db.closeDB();
		}
		catch(Exception ex){
			log.severe(HarambesaUtils.getStackTrace(ex));
			giveErrorFeedBack("Something went wrong while creating a new admin");
			db.closeDB();
		}

	}


    public void disAbleAdmin(){
    	DBConnection db = new DBConnection();
    	Connection conn = db._getConnection();

    	String id = request.getParameter("admin").trim();

    	try{

    	String sql = "UPDATE admin_entitys SET enable_status='FALSE' where entity_id=?";
    	log.info(sql);
    	PreparedStatement st = conn.prepareStatement(sql);
    	st.setInt(1,Integer.parseInt(id));

    	int rs = st.executeUpdate();

    	JSONObject obj = new JSONObject();

    	if(rs > 0 ){
            obj.put("success",1);
    	}else{
    		obj.put("success",0);
    	}
    
          out.println(obj);

    	}catch(Exception ex){
             log.severe(HarambesaUtils.getStackTrace(ex));
    	}finally{
    		db.closeDB();
    	}
    }

	public void suspendAdmin(){
         
         DBConnection db = new DBConnection();
         Connection conn = db._getConnection();

         String id = request.getParameter("admin_id").trim();
         try{

         	String sql = "UPDATE admin_entitys SET suspend_status='TRUE' WHERE entity_id=?";
         	log.info(sql);
         	PreparedStatement ssql = conn.prepareStatement(sql);
         	ssql.setInt(1,Integer.parseInt(id));

         	int rs = ssql.executeUpdate();

           JSONObject obj = new JSONObject();
         	if(rs > 0){
               obj.put("success",1);
         	}else{
               obj.put("success",0);
         	}
         	out.println(obj);


         }catch(Exception ex){
         	log.severe(HarambesaUtils.getStackTrace(ex));
         }finally{
         	db.closeDB();
         }
	}


	public void view_admins(){
		DBConnection db = new DBConnection();
		Connection conn = db._getConnection();

		try{

			String sql = "SELECT entity_id,first_name,last_name,user_name,profile_pic_path,"+
			             "primary_email,mobile_number,date_enroled,entity_level FROM admin_entitys";

			      log.info(sql);
			      PreparedStatement psql = conn.prepareStatement(sql);

			      
			      ResultSet rs = psql.executeQuery();
			      //create a variable total that will be used to count the total number of raws
			      int total = 0;

			      //create a json objrect to keep all datars
			      JSONObject allAdmins = new JSONObject();
			      //create a json array to contain json objects each with a single admin's data
			      JSONArray jArray = new JSONArray();
			      //now iterate through the rs 
			      if(rs.next()){
			      	//there exist at least one admin
			      	do{
			      		JSONObject admin = new JSONObject();

			      		admin.put("e_id", rs.getString("entity_id"));
			      		admin.put("f_name", rs.getString("first_name"));
			      		admin.put("l_name", rs.getString("last_name"));
			      		admin.put("u_name", rs.getString("user_name"));
			      		admin.put("profile_pic", rs.getString("profile_pic_path"));
			      		admin.put("p_email", rs.getString("primary_email"));
			      		admin.put("m_number", rs.getString("mobile_number"));
			      		admin.put("date_enrolled", rs.getString("date_enroled"));
			      		admin.put("a_level", rs.getString("entity_level"));

			      		//put the object  inside the jArray object
			      		jArray.add(admin);

			      		//increment total by 1
			      		total = total+1;

			      	}while(rs.next());
			      	allAdmins.put("success",1);
			      	allAdmins.put("total",total);
			      	allAdmins.put("admins",jArray);
			      }else{
			      	//not a  single admin aexists in db
			      	//so return appropriate message
			      	allAdmins.put("success",1);
			      	allAdmins.put("total", total);
			      	allAdmins.put("message", "There are no admins");

			      }
			      out.println(allAdmins);
			      out.close();

		}catch(SQLException sq){

		}
	}
  	private void giveErrorFeedBack(String message){
			JSONObject obj=new JSONObject();
			obj.put("success", 0);
			obj.put("message", message);
			out.print(obj);
			out.close();
   }

}