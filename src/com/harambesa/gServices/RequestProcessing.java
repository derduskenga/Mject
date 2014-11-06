package com.harambesa.gServices;

import com.harambesa.security.PasswordHash;
import com.harambesa.mailing.Mail;
import java.sql.*;

import com.harambesa.DBConnection.DBConnection;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.security.MessageDigest;

import java.util.Random;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse; 
import javax.servlet.annotation.WebServlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.AddressException;
import java.security.MessageDigest;
import java.util.Random;
import java.util.Iterator;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import java.util.logging.Logger;
import java.io.InputStreamReader; 
import java.io.InputStream;
import java.io.StringWriter;
import javax.servlet.http.Part;

import java.lang.Thread;

//import file upload classes
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.DiskFileUpload;

import java.security.NoSuchAlgorithmException;

public class RequestProcessing extends HttpServlet {
  HttpServletRequest request=null; 
  HttpServletResponse response=null;
  PrintWriter out = null;	
  Logger log= null;
  
  public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException {
		request=req;
		response=res;
		log = Logger.getLogger(RequestProcessing.class.getName());
		out = res.getWriter();	
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");	
		doPost(req, res);
	}
	
  public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException{  
			out = res.getWriter();	
			request=req;
			response=res;
			log=Logger.getLogger(RequestProcessing.class.getName());
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");	
			//get the tag for this request
			 String tag=null;
			 if(request.getParameter("tag") == null ){
				log.severe("Tag is null");
				response.sendRedirect("error/");
			 }else{
					try{
						tag = request.getParameter("tag");
									processTag(tag.trim());
									log.info("Tag:"+tag);
						}catch(IOException ioe){
								log.severe(getStackTrace(ioe));
						}catch(SQLException sqle){
								log.severe(getStackTrace(sqle));
						}catch(FileUploadException fue){
								log.severe(getStackTrace(fue));
						}
			 }
			}
			//process tag and take the right action according to the specified tag
			private void processTag(String tag) throws FileUploadException, SQLException, IOException{
					if(tag.equals("sign_in")){
							String user_name=null, password=null;
							user_name=request.getParameter("username");
							password=request.getParameter("password");
							checkCredentials(user_name,password);
					}else if(tag.equals("register"))
							registerNewUser();
					 else if(tag.equals("all_countries"))
							getCountrys();
					else if(tag.equals("update_profile")){
							try{updateProfile();}catch(FileUploadException e) {}
					}else if(tag.equals("reset_pass"))
							resetMyPassword();
					else if(tag.equals("change_password"))
							changePassword();
					else
							response.sendRedirect("error/");	
		}
			
	private void changePassword(){			    	
				
				if(request.getParameter("token") == null || request.getParameter("id") == null
				|| request.getParameter("password") == null || request.getParameter("r_password") == null
				|| request.getParameter("token").trim().equals("") || request.getParameter("id").trim().equals("")
				|| request.getParameter("password").trim().equals("") || request.getParameter("r_password").trim().equals("")){
					//out.print("Some Parameters not set");
						giveErrorFeedBack("Sorry, we could not reset your password at this time");
						log.severe("Resetting pass. Some params not set or empty");
				}else{
				
				String token = request.getParameter("token").trim();
				String id=request.getParameter("id").trim();
				String password = request.getParameter("password").trim();
				String r_password = request.getParameter("r_password").trim();
				
				if(password.length()<8){
						giveErrorFeedBack("The password you entered is too short. Your new password must be at least 8 characters long");
						log.severe("pass less than 8 chars for reset id = "+id);
				}else if(!password.equals(r_password)){
						giveErrorFeedBack("Password and its confirm must be the same.");
						log.severe("pass and confirm not the same."+id);
				}else{
				String sql="SELECT (EXTRACT(EPOCH FROM INTERVAL '24 hours')-";
							sql+=" EXTRACT (EPOCH FROM ( LOCALTIMESTAMP - created_at))) as diff";
							sql+=",entity_id,status  FROM reset_password WHERE reset_password_id =? AND reset_password_token=?";
				DBConnection db = new DBConnection();
				Connection con = db._getConnection();
				try{
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setInt(1,Integer.parseInt(id));
				ps.setString(2,token);
				ResultSet rs= ps.executeQuery();
				if(rs.next()){
					Double diff=rs.getDouble(1);
					int entity_id=rs.getInt(2);
					boolean used = rs.getBoolean(3);
					if(diff>0.0){
						if(used==false){
						String hashedPassword =PasswordHash.createHash(password);
						String[] params = hashedPassword.split(":");
						String update_password = "UPDATE entitys SET entity_password ='"+params[0]+"',password_salt='"+params[1]+"'  WHERE entity_id="+entity_id+"; "
						+" UPDATE reset_password SET status='1' WHERE reset_password_id = "+id+" AND reset_password_token='"+token+"';";
						String rs1= db.executeQuery(update_password);
						if(rs1==null){
								giveSuccessFeedBack("You have successfully changed your password.");
								//Consider sending password changed mail.
						}else{
								log.severe("RS null reseting password for token id="+id);
						}
						}else{
							giveErrorFeedBack("You have already reset your password using this link.");
							log.severe("Password token already used.id"+id);
						}
					}else{
							log.severe("Password already expired for token id = "+id);
							giveErrorFeedBack("The link you have used for resetting your password has already expired.");
					}	
				}else{
						JSONObject obj=new JSONObject();
						obj.put("success", 0);
						obj.put("message", "Sorry, we could not find the page you requested.");
						out.print(obj);
				}
			}catch(Exception e){
						giveErrorFeedBack("Sorry, there was a problem resetting your password. Please try again.");
						log.severe(HarambesaUtils.getStackTrace(e));
				}finally{
					try{db.closeDB();}catch(Exception e1){}
				}
			}
		}
	}
			
	private void resetMyPassword(){
					log.info("At reset pass.");
					String email = request.getParameter("mailaddress");
					if(email == null || email.equals("")){
									giveErrorFeedBack("Sorry, the email you entered does not appear to be valid.");
									log.severe("Email entered does not appear to be valid ="+email);
					}else{
							if(isValidEmailAddress(email)){
									String sql = "SELECT first_name,entity_id from entitys where primary_email=?";
									DBConnection db = new DBConnection();
									Connection con = db._getConnection();
									try{
										PreparedStatement ps = con.prepareStatement(sql);
										ps.setString(1, email);
										ResultSet rs = ps.executeQuery();
										if(rs!=null){
												if(rs.next()){
														String f_name = rs.getString(1);
														int ent_id = rs.getInt(2);
														db.closeDB();
														if(sendPasswordResetMail(f_name, email, ent_id))
																giveSuccessFeedBack("We have reset your password, please check your email to set a new password.");
														else
																giveErrorFeedBack("Sorry we could not reset your password at this time. Please try again.");
												}else{
														giveErrorFeedBack("We could not find the email you entered.");
												}		
										}else{
												giveErrorFeedBack("Sorry, something went wrong. We could not reset your password at this time.");	
												log.severe("RS was null while trying to reset password for "+email);
										}	
									}catch(Exception e){
												giveErrorFeedBack("Sorry, we could not reset your password at this moment.");
												log.severe(HarambesaUtils.getStackTrace(e));
									}finally{
											try{
												db.closeDB();	
											}catch(Exception e){
														
											}
									}
							}else{
									giveErrorFeedBack("Sorry the email address you entered does not appear to be valid.");
									log.severe("Email entered does not appear to be valid ="+email);
							}					
					}
		}
		
		private boolean sendPasswordResetMail(final String name, final String email, final int ent_id){
							boolean result = false;
							String passwd_reset_hash = getRandomMDString();
							if(passwd_reset_hash == null){
									log.severe("passwd_reset_hash null while resetting password for user = "+email);
							}else{
									final String passwd = passwd_reset_hash.substring(2,22);
									DBConnection db = new DBConnection();
									Connection con = db._getConnection();
									try{					
									String sql  = "INSERT INTO reset_password(";
											   sql +=" entity_id, reset_password_token)";
											   sql+=" VALUES (?, ?)";
											   sql+=" RETURNING  reset_password_id";
									PreparedStatement ps = con.prepareStatement(sql);
									ps.setInt(1, ent_id);
									ps.setString(2, passwd);
									ResultSet rs =ps.executeQuery();
									if(rs!= null){
											if(rs.next()){
													final int token_id = 	 rs.getInt(1);
													log.info("Inside sending mail for reseting pass...");
													Thread t = new Thread() {
															public void run() {
																Mail mail=new Mail();
																mail.sendPasswordResetMail(email,name,passwd,token_id);
															}
														};
													t.start();
													db.closeDB();
													result = true;
											}else{
													log.severe("RS empty while inserting tokens for user="+ent_id);	
													giveErrorFeedBack("Sorry, we could not reset your password at this time.");
													db.closeDB();
											}
										}else{
													log.severe("RS null while inserting tokens for user="+ent_id);	
													giveErrorFeedBack("Sorry, we could not reset your password at this time.");
													db.closeDB();
										}
							}catch(Exception e){
									log.severe(getStackTrace(e));
							}finally{
								try{db.closeDB();}catch(Exception e){}							
							}
					}
					return result;
		}	
		
		public void updateProfile() throws FileUploadException{			
			//check if user is logged in
			HttpSession sess=request.getSession();
			String id=null;
			try{
				id = (String)sess.getAttribute("entity_id");
				log.info("id assigned.");
			}catch(Exception ex){
				log.severe("Error Assigning id on trying to check login --:"+HarambesaUtils.getStackTrace(ex));
			}
			if(id !=null){
			//since we set to process this request as a multipart request:
			// first check if the upload request coming in is a multipart request
			boolean isMultipart = FileUpload.isMultipartContent(request);
			//Create a handler for this request, and use it to parse the request. 
			//Upon parsing, all of the form items are available in a list.
			DiskFileUpload upload = new DiskFileUpload();	
			// parse this request by the handler
			// this gives us a list of items from the request
			List items = upload.parseRequest(request);
			 //Iterate over this list to access individual file items. 
			Iterator itr = items.iterator();
						
			String gender = null;
			String occupation =null;
			String country = null;
			String address = null;
			String postalcode = null;
			String phoneno = null;
			String bio = null;
			String dob = null;
			String org = null;
			String city = null;
			String profile_pic_path=null;
			String physical_street_address=null;
			String phone_country_code = null;
			File fullFile=null;
			
			while(itr.hasNext()) {
				
				FileItem item = (FileItem) itr.next();	
				// check if the current item is a form field or an uploaded file
				if(item.isFormField()) {	
				// get the name of the field
				String fieldName = item.getFieldName();
				// if it is name, we can set it in request to thank the user
				if(fieldName.equals("gender")){
					 gender = item.getString().trim();}
				else if(fieldName.equals("occupation")){
					 occupation = item.getString().trim();}
				else if(fieldName.equals("occupation")){
					      occupation = item.getString().trim();}
				else if(fieldName.equals("countrys")){
					 country = item.getString().trim();}
				else if(fieldName.equals("address")){
					 address = item.getString().trim();}
				else if(fieldName.equals("postalcode")){
					 postalcode = item.getString().trim();}
				else if(fieldName.equals("phoneno")){
					 phoneno = item.getString().trim();}
				else if(fieldName.equals("bio")){
					 bio = item.getString().trim();}
				else if(fieldName.equals("city")){
					 city = item.getString().trim();}
				else if(fieldName.equals("dateofbirth")){
					 dob = item.getString().trim();
				}
				else if(fieldName.equals("orgname")){
					 org = item.getString().trim();}
				else if(fieldName.equals("physical_address")){
					 physical_street_address = item.getString().trim();}
				else if(fieldName.equals("phone-country-code")){
					 phone_country_code = item.getString().trim();}
				} else {
					// the item must be an uploaded file save it to disk. Note that there
					// seems to be a bug in item.getName() as it returns the full path on
					// the client's machine for the uploaded file name, instead of the file
					// name only. To overcome that, I have used a workaround using
					// fullFile.getName().
					fullFile  = new File(item.getName());
					HttpSession session = request.getSession();
					String username = (String)request.getAttribute("entity_id");
					File savedFile = new File(getServletContext().getRealPath("/profilepic"), fullFile.getName());
					log.info(getServletContext().getRealPath("/"));
					try{
						item.write(savedFile);
						profile_pic_path=fullFile.getName();
					}catch(Exception e){
						log.severe(getStackTrace(e));
					}
				}
			}
			DBConnection db=new DBConnection();
			Connection con = db._getConnection();
			
			try{
			String sql=null;
			if(profile_pic_path==null){
				sql="UPDATE entitys SET gender=?, occupation=?, country=?, address=?, postal_code=?,mobile_number=?,physical_street_address=?, org_name=?, date_of_birth=?, details=?, city=? WHERE entity_id=?";
			}else{
				sql="UPDATE entitys SET gender=?, occupation=?, country=?, address=?, postal_code=?,mobile_number=?,physical_street_address=?, org_name=?, date_of_birth=?, details=?,city=?, profile_pic_path=? WHERE entity_id=?";
			}
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1,gender );
			ps.setString(2,occupation );
			ps.setString(3,country );
			ps.setString(4,address );
			ps.setString(5,postalcode);
			ps.setString(6,phone_country_code+phoneno );
			ps.setString(7,physical_street_address );
			ps.setString(8,org );
			ps.setDate(9,java.sql.Date.valueOf (formatDate(dob)));
			ps.setString(10,bio );
			ps.setString(11,city );
		
			if(profile_pic_path != null){
					ps.setString(12, profile_pic_path);
					ps.setInt(13, Integer.parseInt(id));
			}else{
					ps.setInt(12, Integer.parseInt(id));
			}
			JSONArray jArray=new JSONArray();
			JSONObject obj=new JSONObject();
			
			int stmt = ps.executeUpdate();
			log.info("Updating profile int:"+stmt);
			if(stmt != 1 ){
					obj.put("success", 0);
					obj.put("message", "There was a problem updating your profile, Please try again");
			}else if(stmt ==1 ){
					reloadCredentials(id);
					obj.put("success", 1);
					obj.put("message", "Your profile was successfully updated.");
			}
			jArray.add(obj);
				out.println(obj);
		 }catch(Exception e){
				log.severe(HarambesaUtils.getStackTrace(e));
		 }finally{
			try{db.closeDB();}catch(Exception e){}
		 }
		}else{
					log.info("Trying to update profile without with no valid sess");
					JSONArray jArray=new JSONArray();
					JSONObject obj=new JSONObject();
					obj.put("success", 0);
					obj.put("message", "There was a problem updating your profile, Please try again");
					obj.put("redir", "../login");
					jArray.add(obj);
					out.println(obj);
			}
		}
			
		private void checkCredentials(String username, String password){
			DBConnection db=new DBConnection();  
			try{
				JSONObject obj=new JSONObject();	
				if(db._getConnection()!=null){
								//now fetch data
								String fetch_cred = "SELECT entity_password, password_salt, is_active FROM entitys WHERE user_name='"+username+"'";
								ResultSet rs_c=db.readQuery(fetch_cred);
								if(rs_c != null){
										//check if the username exists
										if(!rs_c.next()){
											obj.put("success", 0);
											obj.put("message", "Sorry, We could not find the username/password supplied");
										}else{
										//now that there is a row, check if is is_active
										if(rs_c.getBoolean(3)){
											//is Active now check if the passwords match
											String hash=rs_c.getString(1);
											String salt=rs_c.getString(2);
											Boolean valid = PasswordHash.validatePassword(password, hash+":"+salt);	
											if(valid){
												//passwords match
												String sql="SELECT user_name,first_name, last_name, profile_pic_path, primary_email, entity_id, date_of_birth FROM entitys WHERE user_name='"+username+"'";
												ResultSet rs=db.readQuery(sql);
												if(rs!=null){
														if(!rs.next()){
															obj.put("success", 0);
															obj.put("message", "An error occurred while signing you in, Please try again");
														}else{
															//process this row now and set session variables
															String usrnm = rs.getString(1);
															String f_name = rs.getString(2);					
															String l_name = rs.getString(3);
															String profile_pic_path=rs.getString(4);
															String email=rs.getString(5);
															String entity_id=rs.getString(6);
															HttpSession session = request.getSession();
															session.setAttribute("username",usrnm);
															session.setAttribute("f_name", f_name);
															session.setAttribute("l_name",l_name);
															session.setAttribute("profile_pic_path", profile_pic_path);
															session.setAttribute("email", email);
															session.setAttribute("entity_id", entity_id);
															obj.put("success", 1);
															obj.put("message", "You have successfully logged into your harambesa account.");
															obj.put("redir", rs.getString(7)==null? "../complete-profile":"../home");
														}
												}else{
													//rs is null
													obj.put("success", 0);
													obj.put("message", "An error occurred while signing you in, Please try again.");
												}
											}else{
												//passwords do not match
												obj.put("success", 0);
												obj.put("message", "Sorry, We could not find the username/password supplied.");
											}
										}else{
											//user is not active and 
											obj.put("success", 0);
											obj.put("message", "You are yet to confirm your email, please do so in order to login.");
										}
								}
								}else{
										//no rs, an error occured
										obj.put("success", 0);
										obj.put("message", "An error occurred while signing you in, Please try again");
								}
				}else{
								//did not connect to db
								obj.put("success", 0);
								obj.put("message", "Sorry, We could not find the username/password supplied");
				}
					//output to client
					out.println(obj);
					db.closeDB();
					
			}catch(SQLException sqle){
				//log the error and output to the client
				log.severe(getStackTrace(sqle));
				JSONObject obj=new JSONObject();
				obj.put("success", 0);
				obj.put("message", "An error occurred while signing you in, Please try again");
				out.print(obj);
			}catch(NoSuchAlgorithmException nsae){
				//log the error and output to the client
				log.severe(getStackTrace(nsae));
				JSONObject obj=new JSONObject();
				obj.put("success", 0);
				obj.put("message", "An error occurred while signing you in, Please try again");
				out.print(obj);
				
			}catch(Exception e){
				//log the error, and output to the client
				log.severe(getStackTrace(e));
				JSONObject obj=new JSONObject();
				obj.put("success", 0);
				obj.put("message", "An error occurred while signing you in, Please try again");
				out.print(obj);
			}finally{
					try{db.closeDB();}catch(Exception e1){}
			}
		}
		
		private void reloadCredentials(String ent_id){
				DBConnection db = new DBConnection();
				try{
				String sql="SELECT user_name,first_name, last_name, profile_pic_path, primary_email, entity_id, date_of_birth FROM entitys WHERE entity_id='"+ent_id+"'";
				ResultSet rs=db.readQuery(sql);
				if(rs!=null){
						if(!rs.next()){
							log.severe("Error reloading sess variables after update rs empty");
						}else{
							//process this row now and set session variables
							String usrnm = rs.getString(1);
							String f_name = rs.getString(2);					
							String l_name = rs.getString(3);
							String profile_pic_path=rs.getString(4);
							String email=rs.getString(5);
							String entity_id=rs.getString(6);
							HttpSession session = request.getSession();
							session.setAttribute("username",usrnm);
							session.setAttribute("f_name", f_name);
							session.setAttribute("l_name",l_name);
							session.setAttribute("profile_pic_path", profile_pic_path);
							session.setAttribute("email", email);
							session.setAttribute("entity_id", entity_id);
						}
				}else{
					//rs is null
					log.severe("Error reloading sess variables after update rs null.");
				}
				}catch(Exception e){
					log.severe(HarambesaUtils.getStackTrace(e));
				}finally{
					try{db.closeDB();}catch(Exception e){}
				}
		}
		
		public void getCountrys(){        
			String sql = "SELECT iso, nicename,phonecode  FROM countrys_data";
			DBConnection db= new DBConnection();
			ResultSet rs = db.readQuery(sql);
			JSONArray jArray = new JSONArray();
			try{
					while(rs.next()){            
						JSONObject obj=new JSONObject();
						obj.put("id", rs.getString(1));
						obj.put("name", rs.getString(2));
						obj.put("p_code", rs.getString(3));
						jArray.add(obj);
					}
					out.print(jArray);
			}catch(SQLException ex){
				log.severe(getStackTrace(ex));
			}finally{
				try{db.closeDB();}catch(Exception e1){}
			}
		}
			
		public void registerNewUser(){
					//get all form data
			    	String fname = request.getParameter("fname");
					String lname = request.getParameter("lname");
					String username = request.getParameter("username");
					String country = request.getParameter("country");
					String password = request.getParameter("password");
					String r_pwd = request.getParameter("r_pwd");
					String email = request.getParameter("email");
					
				    String msg=checkMailUsername(email, username);
					
					if(!msg.equals("")){
							JSONObject obj=new JSONObject();
							obj.put("success", 0);
							obj.put("message", "Registration failed. "+msg);
							out.print(obj);
					 } else{								
							DBConnection db=new DBConnection(); 
							try{
								String hashedPassword =PasswordHash.createHash(password);
								String[] params = hashedPassword.split(":");

								Connection con= db._getConnection();
								String sql = "INSERT INTO entitys (entity_type, first_name, last_name, user_name, primary_email, entity_password, password_salt) VALUES ('1', ?, ?, ?, ?,'"+params[0]+"','"+params[1]+"') RETURNING entity_id";
								
								PreparedStatement preparedStatement = con.prepareStatement(sql);
								preparedStatement.setString(1, fname);
								preparedStatement.setString(2, lname);
								preparedStatement.setString(3, username);
								preparedStatement.setString(4, email);
								
								ResultSet rslt = preparedStatement.executeQuery();

								JSONObject obj=new JSONObject();
									if(rslt!=null){
										rslt.next();
										obj.put("success", 1);
										obj.put("message", "Registration Successful. Please Check Your Email For Confirmation");
										sendMailToUser(fname,email, rslt.getString(1));
									}
									else{
										obj.put("success", 0);
										obj.put("message", "Registration Failed. Something went wrong. We are working hard to fix this.");
										log.severe("Message: Registration Failed. Something went wrong. We are working hard to fix this.");
									}
									out.print(obj);
									db.closeDB();
						}catch(Exception ex){
							log.severe(getStackTrace(ex));
							JSONObject obj=new JSONObject();
							obj.put("success", 0);
							obj.put("message", "Registration Failed. Something went wrong. We are working hard to fix this.");
							out.print(obj);
						}finally{
								try{db.closeDB();}catch(Exception e1){}
						}
					}
			}
			
		  private String checkMailUsername(String email, String username){
					DBConnection db=new DBConnection();  
					String msg="";
					try{
				//check email , username
					String chckmail_sql="SELECT * FROM entitys where primary_email='"+email+"'";
					String chckusrnm_sql="Select * FROM entitys where user_name='"+username+"'";
					
					ResultSet rs= db.readQuery(chckmail_sql);
					if(rs.next()){
							msg="The Email you entered already exists, please use a different one.";
					}
					ResultSet rs2= db.readQuery(chckusrnm_sql);
					if(rs2.next()){
							msg +="The username you entered already exists, please choose a different one.";
					}
					}catch(SQLException ex){
							msg="A problem occurred while registering you. We are working hard to fix the problem.";	
					}finally{
						try{db.closeDB();}catch(Exception e1){}
					}
					return msg;
		  }
						
			//determine if the supplied email for registration is a valid one.
			private boolean isValidEmailAddress(String email) {
					boolean result = true;
					try {
						InternetAddress emailAddr = new InternetAddress(email);
						emailAddr.validate();
					} catch (AddressException ex) {
						result = false;
					}
					return result;
			}
		
			//this is for generating random tokens for password resetting and email confirmation		
			public String getRandomMDString(){								
				double random = Math.random() * 200000000 + 1;
				String str = String.valueOf(random);
				try{
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.reset(); 
				md.update(str.getBytes());
				byte[] digest = md.digest();
				StringBuffer sb = new StringBuffer();
				for (byte b : digest) {
					sb.append(String.format("%02x", b & 0xff));
				}
				return sb.toString();
				}
				catch(Exception e){
				log.severe("An error occured while trying to get a random string.");
				return null;
				}
			}	
			//get the full error stack trace and log it
			public static String getStackTrace(final Throwable throwable) {
					final StringWriter sw = new StringWriter();
					final PrintWriter pw = new PrintWriter(sw, true);
					throwable.printStackTrace(pw);
					return sw.getBuffer().toString();
			}
			
			private String formatDate(String date){
					String[] parts = date.split("/");
					log.info(parts[2]+"/"+parts[1]+"/"+parts[0]);
					return parts[2]+"-"+parts[1]+"-"+parts[0];
			}
			
			public void sendMailToUser(final String f_name,final String email, final String id){				    
					    //generate a random token and insert it and the user id
					   final String token = getRandomMDString();
					   final String token_id = updateTokenTable(id, token);
					    if(!(token_id.equals(""))){
						Thread t = new Thread() {
							public void run() {
								Mail mail=new Mail();
								mail.sendMail(f_name,email, token,token_id);
							}
						};
						t.start();
						}
						else{
							//log this error:
							log.severe("Could not send mail to user because error occured while retrieving/inserting token id. User Id is "+id);
						}
			}
			//send mail to admin
			public void sendMailToAdmin(final String f_name,final String email, final String id){				    
		    //generate a random token and insert it and the user id
		   final String token = getRandomMDString();
		   final String token_id = updateAdminTokenTable(id, token);
		    if(!(token_id.equals(""))){
			Thread t = new Thread() {
				public void run() {
					Mail mail=new Mail();
					mail.sendAdminMail(f_name,email, token,token_id);
				}
			};
			t.start();
			}
			else{
				//log this error:
				log.severe("Could not send mail to user because error occured while retrieving/inserting token id. User Id is "+id);
			 }
           }
			//update activation token table
			public String updateTokenTable(String id, String token){
			DBConnection db=new DBConnection(); 
			Connection con= db._getConnection();			
			try{
					String sql=" INSERT INTO activation_tokens( activation_token_entity_id, activation_token)";
								sql+=" VALUES (?,?) RETURNING activation_token_id";
					
					PreparedStatement preparedStatement = con.prepareStatement(sql);
					preparedStatement.setInt(1,Integer.parseInt(id));
					preparedStatement.setString(2, token);
					
					ResultSet rslt= preparedStatement.executeQuery();
					
					if(rslt !=null){
						db.closeDB();
						if(rslt.next())
							return rslt.getString(1);
						else
							return "";
					}else{
						log.severe("Error inserting activation tokens for id "+id+" and token:"+token);
						db.closeDB();
						return "";
					}		
			}catch(SQLException sqle){
					log.severe("Exception:"+getStackTrace(sqle));
					log.severe("Actual DB Error:"+db.getLastErrorMsg());
					return "";
			}finally{
					try{db.closeDB();}catch(Exception e1){}
			}
	}
//update the admin toke table
	public String updateAdminTokenTable(String id, String token){
        
        			DBConnection db=new DBConnection(); 
			Connection con= db._getConnection();			
			try{
					String sql=" INSERT INTO admin_account_tokens( activation_token_entity_id, activation_token)";
								sql+=" VALUES (?,?) RETURNING activation_token_id";
					
					PreparedStatement preparedStatement = con.prepareStatement(sql);
					preparedStatement.setInt(1,Integer.parseInt(id));
					preparedStatement.setString(2, token);
					
					ResultSet rslt= preparedStatement.executeQuery();
					
					if(rslt !=null){
						db.closeDB();
						if(rslt.next())
							return rslt.getString(1);
						else
							return "";
					}else{
						log.severe("Error inserting activation tokens for id "+id+" and token:"+token);
						db.closeDB();
						return "";
					}		
			}catch(SQLException sqle){
					log.severe("Exception:"+getStackTrace(sqle));
					log.severe("Actual DB Error:"+db.getLastErrorMsg());
					return "";
			}finally{
					try{db.closeDB();}catch(Exception e1){}
			}

	}
	
	private void giveSuccessFeedBack(String message){
				JSONObject obj=new JSONObject();
				obj.put("success", "1");
				obj.put("message", message);
				out.print(obj);
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