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
			}else if(tag.equals("fetch_admins")){
				fetch_admins();
			}else if (tag.equals("create_dpt")) {
				createDepartment();
			}else if(tag.equals("set_admin_password")){
				 setAdminPassword();
			}else if(tag.equals("view_dpts")){
				 viewDepartments();
			}else{
				log.info("no such tag found");
			}

	}

	//public methods

	public void viewDepartments(){
		DBConnection db = new DBConnection();
		Connection conn = db._getConnection();

		try{

			String sql = "SELECT department_name,administrator,allocated ";
			       sql += "FROM departments JOIN admin_entitys ON admin_entitys.entity_id = departments.admin_id";
			log.info(sql);

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			//variable to count row
			int total = 0;

			//json object to store the rows
			JSONObject rows = new JSONObject();

			//Json array to to store the departments
			JSONArray jArray = new JSONArray();

			if (rs.next()) {

				do{

					JSONObject row = new JSONObject();

					row.put("department", rs.getString("department_name"));
					row.put("administrator", rs.getString("administrator"));

					jArray.add(row);

					total += 1;

				}while(rs.next());

                rows.put("success", 1);
                rows.put("total", total);
				rows.put("dpts",jArray);
				out.println(rows);
				
			}



		}catch(Exception ex){
			log.severe(HarambesaUtils.getStackTrace(ex));
		}
	}

	public void setAdminPassword(){

		DBConnection db = new DBConnection();
		Connection conn = db._getConnection();

		try{


			String password = request.getParameter("password1");
			String id = request.getParameter("admin_id").trim();

			log.info(password);
			log.info(id);

			String sql1 = "SELECT activation_token_entity_id FROM admin_account_tokens WHERE activation_token_id=?";
			log.info(sql1);

			PreparedStatement ps1 = conn.prepareStatement(sql1);
			ps1.setInt(1,Integer.parseInt(id));

			ResultSet rs1 = ps1.executeQuery();

			if(rs1.next()){

	            String hassPass = PasswordHash.createHash(password);
				String param[] = hassPass.split(":");

				String id1 = rs1.getString("activation_token_entity_id");

			String sq1 = "UPDATE admin_entitys SET entity_password=?,password_salt=? WHERE entity_id=?";
			log.info(sq1);
			PreparedStatement ps = conn.prepareStatement(sq1);

			ps.setString(1,param[0]);
			ps.setString(2,param[1]);
			ps.setInt(3,Integer.parseInt(id1));

			JSONObject obj = new JSONObject();
			int rs = ps.executeUpdate();

			if(rs == 1){
                 updateAdminTokensTable(id1);
                 obj.put("success",1);
                 out.println(obj);
			}else{
                 obj.put("success",0);
                 out.println(obj);
			}
		  }else{
		  	log.info("Nothing found");
		  }

		}catch(Exception ex){
			log.severe(HarambesaUtils.getStackTrace(ex));
		}
	}

	public void updateAdminTokensTable(String id){

		DBConnection db = new DBConnection();
		Connection conn = db._getConnection();

		try{
				String sql2 = "UPDATE admin_account_tokens SET status='TRUE' WHERE activation_token_entity_id=?";

				log.info(sql2);

				PreparedStatement ps2 = conn.prepareStatement(sql2);
				ps2.setInt(1,Integer.parseInt(id));

				int rs2 = ps2.executeUpdate();

				if (rs2 >= 1) {

				JSONObject obj = new JSONObject();
                 
				 obj.put("success",1);
                 out.println(obj);

				}else{
					log.info("Could not update admin account tokens table");
				}
			}catch(Exception ex){
				log.severe(HarambesaUtils.getStackTrace(ex));
			}finally{
				db.closeDB();
			}
	}

	public void adminLogin(){

		   String username = request.getParameter("username").trim();
		   String password = request.getParameter("password").trim();
		   String adminLevel = request.getParameter("adminLevel").trim();
           
           checkAdminCredentials(username,password,adminLevel);
	}

	//check admin credentials
	private void checkAdminCredentials(String username, String password, String adminLevel){
			DBConnection db=new DBConnection();
			Connection conn = db._getConnection();

			try{
				JSONObject obj=new JSONObject();	
				if(db._getConnection()!=null){
								//now fetch data
								String fetch_admin_cred = "SELECT entity_id,first_name,last_name,user_name,entity_password,password_salt,";
								       fetch_admin_cred +=" entity_level,enable_status,suspend_status FROM admin_entitys WHERE user_name='"+username+"'";
								ResultSet rs_c=db.readQuery(fetch_admin_cred);

								if(rs_c != null){
										//check if the username exists
										if(!rs_c.next()){
											obj.put("success", 0);
											obj.put("message", "Sorry, We could not find the username/password supplied");
										}else{
										//now that there is a row, check if is is_active

											Boolean enable_status = rs_c.getBoolean(8);
											Boolean suspend_status = rs_c.getBoolean(9);
										if(enable_status && ! suspend_status){
											//is Active now check if the passwords match
											String a_id = rs_c.getString(1);
											String f_name = rs_c.getString(2);
											String s_name = rs_c.getString(3);
											String u_name = rs_c.getString(4);
											String hash=rs_c.getString(5);
											String salt=rs_c.getString(6);
											String a_level = rs_c.getString(7); 
											

											Boolean valid = PasswordHash.validatePassword(password, hash+":"+salt);

											if(valid){//if passwords match i.e returns true

												HttpSession session = request.getSession();

												session.setAttribute("aId",a_id);
												session.setAttribute("fName", f_name);
												session.setAttribute("sName",s_name);
												session.setAttribute("uName", u_name);
												session.setAttribute("aLevel", a_level);
					
												obj.put("success", 1);
												obj.put("message", "Login Successful.");

												}else{
													//rs is null
													obj.put("success", 0);
													obj.put("message", "Wrong password provided.");
												}

											}else{
												//passwords do not match
												obj.put("success", 0);
												obj.put("message", "Your account has either been blocked or inactive.. Please contact the system administrator for assistance");
											}
										}
								}else{
								//did not connect to db
								obj.put("success", 0);
								obj.put("message", "Sorry, We could not find the username/password supplied");
				              }
					//output to client
					out.println(obj);
					db.closeDB();
				}
					
			}catch(SQLException sqle){
				//log the error and output to the client
				log.severe(HarambesaUtils.getStackTrace(sqle));
				JSONObject obj=new JSONObject();
				obj.put("success", 0);
				obj.put("message", "An error occurred while signing you in, Please try again");
				out.print(obj);
			}catch(Exception ex){
				//log the error, and output to the client
				log.severe(HarambesaUtils.getStackTrace(ex));
				JSONObject obj=new JSONObject();
				obj.put("success", 0);
				obj.put("message", "An error occurred while signing you in, Please try again");
				out.print(obj);
			}finally{
					try{
						db.closeDB();
					}catch(Exception e1){}
			}
		}

	public void create_admin(){

		    DBConnection db = new DBConnection();
			Connection conn = db._getConnection();

		try{

			String firstname = request.getParameter("fname").trim();
			String secondname = request.getParameter("sname").trim();
			String tmppassword = "admin@21century";
			String priemail = request.getParameter("pri_email").trim();
			String userlevel = request.getParameter("level").trim();

		   String newHashedPass = PasswordHash.createHash(tmppassword);

		   String param[] = newHashedPass.split(":");

			String create_admin_query = "INSERT INTO admin_entitys(first_name,last_name,user_name";
				   create_admin_query +=" ,primary_email,entity_password,password_salt,entity_level) VALUES(?,?,?,?,?,?,?)";
                   create_admin_query +=" RETURNING entity_id,user_name,primary_email";
                 
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
			ResultSet rs = ps.executeQuery();

			if(rs != null){

				if(rs.next()){
					String tokenId = rs.getString("entity_id");
					String userName = rs.getString("user_name");
					String primaryEmail = rs.getString("primary_email");

					RequestProcessing rp = new RequestProcessing();

					     rp.sendMailToAdmin(userName,primaryEmail,tokenId);


                    obj.put("success",1);
                    obj.put("tokenId",tokenId);
                    obj.put("userName",userName);
                    obj.put("primaryEmail",primaryEmail);
                    out.print(obj);
				}else if(!rs.next()){
					obj.put("success",0);
					out.println(obj);
				}else{

				}
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

    public void createDepartment(){
    	DBConnection db = new DBConnection();
    	Connection conn = db._getConnection();

    	try{
    	String col_dpt = "department_name";
    	String col_adminID = "admin_id";
    	String col_administrator = "administrator";
        String department = request.getParameter("department").trim();
        String adminId = request.getParameter("adminId").trim();
        String administrator = request.getParameter("first_name").trim()+" "+request.getParameter("second_name").trim();

    	
    		String sql = "INSERT INTO departments(department_name,admin_id,administrator) VALUES(?,?,?)";
    		log.info(sql);

    		PreparedStatement st = conn.prepareStatement(sql);

    		st.setString(1,department);
    		st.setInt(2,Integer.parseInt(adminId));
    		st.setString(3,administrator);

    		JSONObject obj = new JSONObject();
    		 int rs = st.executeUpdate();

    		 if(rs >= 1){
    		 	obj.put("success",1);
    		 	out.println(obj);
    		 }else{
    		 	obj.put("success",0);
    		 	out.println(obj);
    		 }

    	}catch(NullPointerException e){

          log.severe(HarambesaUtils.getStackTrace(e));

    	}catch(SQLException s){

             log.severe(HarambesaUtils.getStackTrace(s));
             
    	}catch(Exception ex){
               log.severe(HarambesaUtils.getStackTrace(ex));
               giveErrorFeedBack("Something went wrong while creating a new department");
    	}finally{
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


    public void fetch_admins(){
    	DBConnection db = new DBConnection();
    	Connection conn = db._getConnection();

    	try{

    		String sql = "SELECT entity_id,first_name,last_name FROM admin_entitys";
    		log.info(sql);

    		PreparedStatement st = conn.prepareStatement(sql);

    		ResultSet rs = st.executeQuery();

    		int total = 0;//variable to store the total number of raws

    		JSONObject admins1 = new JSONObject();//This will keep all the data

    		JSONArray jArray = new JSONArray();//To contain json objects each with single admin data

    		//statent to iterate through the result set and take count
    		if(rs.next()){
               //if there exists atleast one admin
    			do{
    				JSONObject admin = new JSONObject();//This will hold single row data

    				admin.put("adminId",rs.getString("entity_id"));
    				admin.put("first_name",rs.getString("first_name"));
    				admin.put("second_name",rs.getString("last_name"));

    				//Put the data inside the jArray object created earlier
    				jArray.add(admin);

    				//increase the tootal count by 1
    				total += 1;
    			}while(rs.next());

                    admins1.put("success",1);
                    admins1.put("total",total);
                    admins1.put("names",jArray);

    		}else{
			//if no names exist
			admins1.put("success",0);
			admins1.put("total",total);
    	 }
		
		out.println(admins1);
		out.close();


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

			      //create a json object to keep all data
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
			      	allAdmins.put("success",0);
			      	allAdmins.put("total", total);
			      	allAdmins.put("message", "There are no admins");

			      }
			      out.println(allAdmins);
			      out.close();

		}catch(SQLException sq){
           log.severe(HarambesaUtils.getStackTrace(sq));
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