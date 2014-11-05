package com.harambesa.settings;  

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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import java.util.Date;


import com.harambesa.security.PasswordHash;
import com.harambesa.DBConnection.DBConnection; 
import com.harambesa.settings.settingsUtil;


public class PasswordManagement extends HttpServlet { 
    PrintWriter out = null;	
    Logger log= null;
    JSONObject jsonData = null;
    Connection conn = null;

  	public void init() throws ServletException {
        log = Logger.getLogger(PasswordManagement.class.getName());
        jsonData = new JSONObject();
        conn = new DBConnection()._getConnection();
		log.info(settingsUtil.getLoggerLine(Thread.currentThread(),this,"Password management started"));

    } 

	public void doPost(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException,IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");	 
    	HttpSession session = request.getSession();  
    	String entity_id = (String) session.getAttribute("entity_id");
		out = response.getWriter();	 
			
		// get user inputs
		
		String tag = request.getParameter("tag");
		String referer = request.getParameter("referer");


		
		try{
			// if its a redirect 
			if(tag.equals("re_login")){ 
				String login_username = (String)request.getParameter("LoginUsername");
				String login_passord = (String)request.getParameter("LoginPassword"); 
				// check of login was successfull
				if(login(conn, request, login_username, login_passord)){
					log.info(settingsUtil.getLoggerLine(Thread.currentThread(),this,"successfull login"));
					
					log.info(settingsUtil.getLoggerLine(Thread.currentThread(),this,referer));;
					jsonData.put("success", 1);
					jsonData.put("redir", referer);
				}else{					
					//rs is null 
					log.info(settingsUtil.getLoggerLine(Thread.currentThread(),this,"failed login"));
					jsonData.put("success", 0);
					jsonData.put("message", "An error occurred while signing you in, Please try again.");
				}
			
			}else if(tag.equals("reset_password")){ //if this is a password change request
				String old_password = request.getParameter("oldPassword");
				String password_1 = request.getParameter("password1");
				String password_2 = request.getParameter("password2"); 
				// check if password 1 and password two are equal, and meet the required length
				if(validateNewPassword(password_1, password_2)){ 
					log.info(settingsUtil.getLoggerLine(Thread.currentThread(),this,"has a re login"));

					
					// confirm old password matches database password
					if(comparePassword(conn, old_password, entity_id)){

						// update the password
						if(updatePassword(conn, entity_id, password_1)){
							jsonData.put("message", "Password Updated Succeffuly."); 
						}else{
							jsonData.put("message", "Password Update not Succeful."); 						
						}

					}else {
						jsonData.put("message", "Old password did not match.");
						response.setStatus(202);
					}


				}else{ 
					jsonData.put("message", "Ensure the new and repeated passwords are similar.");
					response.setStatus(202);
				}
			}
		}finally{
			// try{
			// 	if(conn!=null){
			// 		// conn.close();
			// 	}	
			// }
			// catch(SQLException e){
			// 	e.printStackTrace();

			// }
		}

		
		out.print(jsonData);


		
	}

	/**
	*
	* 	Reset compares database password and the old password provided.
	*
	*	@param oldpassword the password user believes to be there passwors
	*	@param connection the database connection object to user 
	*	@param user id of the user to login
	*	
	*	@return true if passwords is the same as one in the database
	*/
	public Boolean comparePassword(Connection conn, String oldpassword, String entity_id){ 
		Boolean status = false; 
		PreparedStatement pstmt = null;
		ResultSet rs = null;

			try{
				// sql statement
				String sql = "select password_salt,entity_password from entitys where entity_id = ?";
			
				//create statement
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1,Integer.parseInt(entity_id));
				rs = pstmt.executeQuery();
	
				// if user with that id exists
				if(rs.next()){				
					// get hashedPassword and the hash
					String hashedPassword = rs.getString("entity_password");
					String salt = rs.getString("password_salt"); 
			 
					// compare the hashedPassword and oldpassword provided by user
					Boolean valid = PasswordHash.validatePassword(oldpassword, hashedPassword+":"+salt);		 
					
					// passwords match return true
					if(valid){ 
						status = true;
					}else{
						status = false;
					}
				}	
			}catch(SQLException sqlE){
				log.severe(settingsUtil.getLoggerLine(Thread.currentThread(),this,"SQL Error"+sqlE.getMessage()));
			}catch(NoSuchAlgorithmException nae){
				log.severe("Password validation error"+nae.getMessage());
			}catch(InvalidKeySpecException ikse){
				log.severe("Password validation error"+ikse.getMessage());
			}finally{
				try{	

					if(pstmt!=null){
						pstmt.close();
					}

					if(rs!=null){
						rs.close();
					}	
				}
				catch(SQLException e){
					e.printStackTrace();
				}
			}
		return status;
	}  

	/**
	*
	*	Updates the password to the one provided
	*
	*	@param connection the connection
	*	@param entity_id	the user id
	*	@param password 	the password to update to	
	*
	*
	*/
	public Boolean updatePassword(Connection conn, String entity_id, String password) {
		Boolean status = false; 
		PreparedStatement stmt = null;
		try{
			// create a hashed password
			String newHashedPassword =PasswordHash.createHash(password);
			// split the hashed passed where there is a full colon
			String[] params = newHashedPassword.split(":"); 
			// create new sql to update password
			String sql = "UPDATE entitys SET entity_password = ?, password_salt = ? WHERE entity_id = ?";
			// create a statement
			stmt = conn.prepareStatement(sql); 
			stmt.setString(1,params[0]);
			stmt.setString(2,params[1]);
			stmt.setInt(3,Integer.parseInt(entity_id));
			int rowsAffected = stmt.executeUpdate();
			if(rowsAffected>0){
				// msg = "Password Updated";
				log.info("line 167 : Password Updated");
				status = true;
			}else{
				// msg = "Something Went Wrong, Password Not Updated";
				log.info("line 171 : Password Not Updated");
				status = false;
			} 
			
		}catch(SQLException sqlE){
			log.severe("SQL Error"+sqlE.getMessage());
		}catch(NoSuchAlgorithmException nae){
			log.severe("Password validation error"+nae.getMessage());
		}catch(InvalidKeySpecException ikse){
			log.severe("Password validation error"+ikse.getMessage());
		}finally{
			try{	

				if(stmt!=null){
					stmt.close();
				}	 
			}
			catch(SQLException e){
				e.printStackTrace();

			}
		}
		return status;
	}

	/**
	*
	*	Compare two new passwords provided
	*	
	*	@param password takes the passwords to equate
	*
	*	@return it returns true if passwords are equal
	*/
	private Boolean validateNewPassword(String password_1, String password_2){
		if(password_1.trim().equals(password_2.trim()) && (password_1.trim().length()>8))
			return true;
		else
			return false;

	} 

	/**
	*
	*	Login user into the system
	*	@param username the username to use to login user
	*	@param password the password to user to login the user
	*	
	* 	@return Boolean if login succeeds return true
	*/
	public Boolean login(Connection conn,  HttpServletRequest request, String username, String password){
		Boolean status = false;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try{
			// ensure username exists
			rs = settingsUtil.entryExists(conn, username, "entitys", "user_name");
			if(rs != null){
				String id = rs.getString("entity_id");
				// from the id got, 
				// check password of this id to the one provided
				//compare password with database password
				if(comparePassword(conn, password, id)){ 
					log.info(settingsUtil.getLoggerLine(Thread.currentThread(),this,"password matched"));
					// set the session variable for this user				
					HttpSession session = request.getSession(); 
					session.setAttribute("username",rs.getString("user_name"));
					session.setAttribute("f_name", rs.getString("first_name"));
					session.setAttribute("l_name",rs.getString("last_name"));
					session.setAttribute("profile_pic_path", rs.getString("profile_pic_path"));
					session.setAttribute("email", rs.getString("primary_email"));
					session.setAttribute("entity_id", rs.getString("entity_id")); 
					status = true;

					String sql = "update entitys set last_login=now() where entity_id="+id+"";
					pstmt = conn.prepareStatement(sql);
					Integer rowsAffected = pstmt.executeUpdate();
					if(rowsAffected>0){
						log.info(settingsUtil.getLoggerLine(Thread.currentThread(),this,"last_login update was a success"));
					}else{
						log.info(settingsUtil.getLoggerLine(Thread.currentThread(),this,"last_login update failed"));
					}
				}else{
					log.info(settingsUtil.getLoggerLine(Thread.currentThread(),this,"password did not match"));	
					status = false;		
				}
			}else{			
				status = false; 
				log.info(settingsUtil.getLoggerLine(Thread.currentThread(),this,"Username doest not exist"));
				log.info(settingsUtil.getLoggerLine(Thread.currentThread(),this,"resultset returned null"));
			}

			
		}catch(SQLException sqlE){
			log.severe(settingsUtil.getLoggerLine(Thread.currentThread(),this,"SQL Error"+sqlE.getMessage()));
		}finally{
			try{	

				if(pstmt!=null){
					pstmt.close();
				}	

				if(rs!=null){
					rs.close();
				}
			}
			catch(SQLException e){
				e.printStackTrace();

			}
		}
		// verify passwords match for that user
		return status;
	} 
}