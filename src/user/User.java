package com.harambesa.user; 

import com.harambesa.DBConnection.DBConnection;
import com.harambesa.DBConnection.GlobalDresser;
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

public class User extends HttpServlet{

		HttpServletRequest request=null; 
		String entity_id = null;
		HttpServletResponse response=null;
		PrintWriter out = null;	
		Logger logger= null;
	
		public void doGet(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException{
				this.request=request;
				this.response=response;
				out = response.getWriter();	
				doPost(this.request, this.response);
		}

		public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
				out = response.getWriter();	
				this.request=request;
				this.response=response;
				
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				
				logger=Logger.getLogger(User.class.getName());
				checkLogin();
		}
		
		public void checkLogin() throws IOException{
				HttpSession session = request.getSession();
				String tag=request.getParameter("tag");
				String user=request.getParameter("user");
				String entity_id=(String)session.getAttribute("entity_id");
				if(entity_id==null)
					if(tag==null)
						response.sendRedirect(request.getContextPath()+"/login");
					else
						sendRedir();
				else if(user==null || user.trim().equals(""))
						giveErrorFeedBack("Sorry, something went wrong.");
				else{
						this.entity_id = entity_id;
						processReq(tag.trim());
						logger.info("Tag at user.java = "+tag.trim());
				}	
		}
		
		public void processReq(String tag){
				if(tag.equals("")){
					logger.severe("Tag null at User.java class");
					giveErrorFeedBack("Sorry, Something went wrong.");
				}else if(tag.equals("fetchuserdata")){
					fetchUser();
					logger.severe("Tag ="+tag);
				}else if(tag.equals("-con")){
					connectionActions();
					logger.severe("Tag = "+tag);
				}else{
					logger.severe("Tag Null = "+tag);
					giveErrorFeedBack("Sorry, Something went wrong.");
				}
		}
	
		public void fetchUser(){
					logger.info("At method fetch user");
					DBConnection db=new DBConnection(); 
					Connection con= db._getConnection();
					try{
					String frensSQL =null;
					PreparedStatement preparedStatement = null;
									//user has only entered one name or a part of it
							frensSQL = "SELECT entity_id, first_name, last_name, profile_pic_path, details, sys_country_name FROM entitys";
							frensSQL+=" RIGHT JOIN countrys";
							frensSQL+=" ON countrys.sys_country_id = entitys.country";
							frensSQL+=" WHERE entity_id = ?";
							
							frensSQL = "SELECT entity_id, first_name, last_name, profile_pic_path, details, sys_country_name,";
							frensSQL+=" connection_id, requestor_entity_id, requestee_entity_id, is_accepted, is_terminated, connection_is_blocked, ignored";
							frensSQL+=" FROM entitys RIGHT JOIN countrys"; 
							frensSQL+=" ON countrys.sys_country_id = entitys.country"; 
							frensSQL+=" LEFT OUTER JOIN connections";
							frensSQL+=" ON ((requestor_entity_id = ? AND requestee_entity_id=?)";
							frensSQL+=" OR  (requestor_entity_id = ? AND requestee_entity_id=?))";
							frensSQL+=" WHERE entity_id=?";
							
							logger.info("Get User SQL = "+frensSQL);
							int user = Integer.parseInt(request.getParameter("user"));
							int ent_id= Integer.parseInt(entity_id);
							preparedStatement = con.prepareStatement(frensSQL);
							
							preparedStatement.setInt(1,user);
							preparedStatement.setInt(2,ent_id);
							preparedStatement.setInt(3,ent_id);
							preparedStatement.setInt(4,user);
							preparedStatement.setInt(5,user);
							
							ResultSet rs= preparedStatement.executeQuery();
							
							if(rs!=null){
									if(!rs.next()){
										giveSuccessFeedback("No user found.");
									}else{
										JSONObject obj=new JSONObject();
										obj.put("success",1);
										obj.put("id",rs.getString(1));
										obj.put("name", rs.getString(2)+" "+rs.getString(3));
										obj.put("prof_pic",rs.getString(4));
										obj.put("bio",rs.getString(5));
										obj.put("country",rs.getString(6));
										if(rs.getInt(7) > 0){
												obj.put("req",1);
												obj.put("reqw",rs.getInt(7));
											if(rs.getBoolean(10))
												obj.put("acc",1);
											else
												obj.put("acc",0);
											if(rs.getInt(8) == ent_id)
												obj.put("reqo",1);
											else
												obj.put("reqo",0);
											obj.put("ter",rs.getBoolean(11));
										}else{
												obj.put("req",0);
										}
										if(rs.getInt(1) == Integer.parseInt(entity_id))
												obj.put("owner",true);
										out.println(obj);
										out.close();
									}
							}
							}catch(SQLException sqle){
								logger.severe(HarambesaUtils.getStackTrace(sqle));
								giveErrorFeedBack("Something went wrong");
							}finally{
									try{db.closeDB();}catch(Exception e){};
							}
		}
		
		public void connectionActions(){
// 					if(id=='send-connection-request')
// 							data={tag:'send-con', id:$(btn).parent().attr('id')}; 
// 					else if(id=='accept-connection-request')
// 							data={tag:'accept-con', id:$(btn).parent().attr('id')};
// 					else if(id=="cancel-connection-request')
// 							data={tag:'cancel-req', id:$(btn).parent().attr('id')};
// 					else if(id=='ignore-connection-request')
// 							data={tag:'ignore-con', id:$(btn).parent().attr('id')};
// 					else if(id=='terminate-connection')
// 							data={tag:'terminate-con', id:$(btn).parent().attr('id')};
				logger.info("At method connection actions..");
				String reqtype = request.getParameter("reqtype");
				String id = request.getParameter("id");
				String user = request.getParameter("user");
				
				if(reqtype==null || reqtype.equals("") || reqtype==null || reqtype.equals("")){
						giveErrorFeedBack("Sorry, something went wrong.");
						logger.severe("Error identifying reqtype for connection actions user="+entity_id);
						return;
				}
				if(reqtype.equals("send-con")){
						GlobalDresser gd = new GlobalDresser(entity_id);
						Boolean connected = isConnected(Integer.parseInt(user));
						Boolean requested = isRequestExist (Integer.parseInt(user));
						if(connected == null){
								giveErrorFeedBack("Sorry, something went wrong.");
						}else if(connected == true){
								giveErrorFeedBack("You are already connected to this user.");
						}else if(requested == null){
								giveErrorFeedBack("Sorry, something went wrong.");
						}else if(requested){
								giveErrorFeedBack("You have already made a connection request to this user.");
						}else{
								out.println(gd.saveConnectionRequest(user));
								out.close();
						}
				}else if(reqtype.equals("accept-con")){
							DBConnection db = new DBConnection();
							Connection con = db._getConnection();
							try{
							String 	sql = "UPDATE connections";
										sql+=" SET is_accepted=TRUE";
										sql+=" WHERE connection_id=?";
										sql+=" AND requestor_entity_id=?";
										sql+=" AND requestee_entity_id=?";
										sql+=" AND is_accepted=FALSE";				
							logger.info("SQL:"+sql);
							PreparedStatement ps = con.prepareStatement(sql);
							ps.setInt(1,Integer.parseInt(id));
							ps.setInt(2,Integer.parseInt(user));
							ps.setInt(3,Integer.parseInt(entity_id));
							int accRs = ps.executeUpdate();
							if(accRs == 0){
									logger.severe("accRs equals null logged in user = "+entity_id+" and id="+id);
									giveErrorFeedBack("Connection request not found");
									return;
							}
							if(accRs>0){
								giveSuccessFeedback("You are now connected to this user.");
							}
						}catch(Exception e){
							logger.info("Error:"+HarambesaUtils.getStackTrace(e));
							giveErrorFeedBack("Sorry, something went wrong.");
						}finally{
							try{db.closeDB();}catch(Exception ex){}
						}
				}else if(reqtype.equals("ignore-req")){
							DBConnection db = new DBConnection();
							Connection con = db._getConnection();
							try{
							String 	sql = "UPDATE connections";
										sql+=" SET ignored=TRUE";
										sql+=" WHERE connection_id=?";
										sql+=" AND requestor_entity_id=?";
										sql+=" AND ignored=FALSE";	
							PreparedStatement ps = con.prepareStatement(sql);
							ps.setInt(1,Integer.parseInt(id));
							ps.setInt(2,Integer.parseInt(user));
							int accRs = ps.executeUpdate();
							if(accRs ==0){
									logger.severe("Ignore request equals null logged in user = "+entity_id+" and id="+id);
									giveErrorFeedBack("Sorry, something went wrong.");
									return;
							}
							if(accRs>0){
								giveSuccessFeedback("Connection request successfully Ignored.");
							}
						}catch(Exception e){
							logger.info("Error:"+HarambesaUtils.getStackTrace(e));
							giveErrorFeedBack("Sorry, something went wrong.");
						}finally{
							try{db.closeDB();}catch(Exception ex){}
						}
				}else if(reqtype.equals("cancel-req")){
							logger.info("At cancel request..");
							DBConnection db = new DBConnection();
							Connection con = db._getConnection();
							try{
							
							String 	sql = "UPDATE connections";
										sql+=" SET is_cancelled =TRUE";
										sql+=" WHERE connection_id=?";
										sql+=" AND requestor_entity_id=?";
										sql+=" AND requestee_entity_id=?";
										sql+=" AND is_cancelled=FALSE";	
							PreparedStatement ps = con.prepareStatement(sql);
							ps.setInt(1,Integer.parseInt(id));
							ps.setInt(2,Integer.parseInt(entity_id));
							ps.setInt(3,Integer.parseInt(user));
							int accRs = ps.executeUpdate();
							logger.info("RS INT: = "+accRs);
							if(accRs ==0){
									logger.severe("cancel request equals null logged in user = "+entity_id+" and id="+id);
									giveErrorFeedBack("Sorry, something went wrong.");
									return;
							}
							if(accRs>0){
								giveSuccessFeedback("Connection request successfully cancelled.");
							}
						}catch(Exception e){
							logger.info("Error:"+HarambesaUtils.getStackTrace(e));
							giveErrorFeedBack("Sorry, something went wrong.");
						}finally{
							try{db.closeDB();}catch(Exception ex){}
						}
				}else if(reqtype.equals("terminate-con")){
							
							DBConnection db = new DBConnection();
							Connection con = db._getConnection();
							
							try{
							
							String 	sql = "DELETE FROM connections";
										sql+=" WHERE connection_id=?";
										sql+=" AND ((requestor_entity_id=? AND requestee_entity_id=?)";
										sql+=" OR (requestor_entity_id=? AND requestee_entity_id=?))";
										sql+=" AND is_terminated = FALSE";
										
							PreparedStatement ps = con.prepareStatement(sql);
							
							ps.setInt(1,Integer.parseInt(id));
							ps.setInt(2,Integer.parseInt(user));
							ps.setInt(3,Integer.parseInt(entity_id));
							ps.setInt(4,Integer.parseInt(entity_id));
							ps.setInt(5,Integer.parseInt(user));
							
							int accRs = ps.executeUpdate();
							
							if(accRs == 0){
									logger.severe("Terminate connection request equals null logged in user = "+entity_id+" and id="+id);
									giveErrorFeedBack("Sorry, something went wrong.");
									return;
							}
							if(accRs>0){
								giveSuccessFeedback("Connection request successfully ignored.");
							}
						}catch(Exception e){
							logger.info("Error:"+HarambesaUtils.getStackTrace(e));
							giveErrorFeedBack("Sorry, something went wrong.");
						}finally{
							try{db.closeDB();}catch(Exception ex){}
						}
				}				
		}
		
		private boolean isConnected(int user){
					
					Boolean connected = null;
					String 	sql  ="SELECT connection_id";
								sql+=" FROM connections";
								sql+=" WHERE ((requestor_entity_id= ? AND requestee_entity_id = ?)";
								sql+=" OR (requestor_entity_id= ? AND requestee_entity_id = ?)) ";
								sql +=" AND is_accepted = TRUE"; 
								sql+=" AND connection_is_blocked= FALSE";
								sql+=" AND is_terminated = FALSE";
					DBConnection db = new DBConnection();
					Connection con = db._getConnection();
					try{
						PreparedStatement ps = con.prepareStatement(sql);
						ps.setInt(1, Integer.parseInt(entity_id));
						ps.setInt(2, user);
						ps.setInt(3, user);
						ps.setInt(4, Integer.parseInt(entity_id));
						
						ResultSet rs = ps.executeQuery();
						if(rs!=null){
								if(rs.next())
									connected = true;
								else
									connected = false;
						}
					}catch(Exception e){
						logger.severe("Error determining is connected ent_id = "+entity_id+" and user ="+user);
					}finally{
							try{db.closeDB();}catch(Exception e){}
					}
					return connected;
		}
		
		private boolean isRequestExist(int user){
					
					Boolean connected = null;
					
					String 	sql  ="SELECT connection_id";
								sql+=" FROM connections";
								sql+=" WHERE ((requestor_entity_id= ? AND requestee_entity_id = ?)";
								sql+=" OR (requestor_entity_id= ? AND requestee_entity_id = ?)) ";
								sql +=" AND is_accepted =FALSE"; 
								sql+=" AND connection_is_blocked= FALSE";
								sql+=" AND is_terminated = FALSE";
					
					DBConnection db = new DBConnection();
					Connection con = db._getConnection();
					try{
					PreparedStatement ps = con.prepareStatement(sql);
					ps.setInt(1, Integer.parseInt(entity_id));
					ps.setInt(2, user);
					ps.setInt(3, user);
					ps.setInt(4, Integer.parseInt(entity_id));
					
					ResultSet rs = ps.executeQuery();
					if(rs!=null){
							if(rs.next())
								connected = true;
							else
								connected = false;
					}
					}catch(Exception e){
						logger.severe("Error determining is connection req exist ent_id = "+entity_id+" and user ="+user);
					}finally{
							try{db.closeDB();}catch(Exception e){}
					}
					return connected;
		}
				
		public void sendRedir(){
					JSONObject obj  = new JSONObject();
					obj.put("success", 0);
					obj.put("redir", "login");
					obj.put("message", "Your session has expired, please login to continue");
					out.println(obj);
					out.close();
		}
		
		public void giveErrorFeedBack(String message){
					JSONObject obj=new JSONObject();
					obj.put("success", 0);
					obj.put("message", message);
					out.print(obj);
					out.close();
		}
			
		public void giveSuccessFeedback(String message){
					JSONObject obj=new JSONObject();
					obj.put("success", "1");
					obj.put("message", message);
					out.print(obj);
					out.close();
		}
		
}