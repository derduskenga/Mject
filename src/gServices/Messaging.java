package com.harambesa.gServices; 

import com.harambesa.DBConnection.DBConnection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse; 
import javax.servlet.http.HttpSession;

import java.sql.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.sql.SQLException;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import com.harambesa.gServices.HarambesaUtils;

public class Messaging extends HttpServlet{

	private static Logger logger = Logger.getLogger(Messaging.class.getName());
	
	HttpServletRequest request=null; 
	HttpServletResponse response=null;
	PrintWriter out = null;	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException{
			logger.info("In class messaging method get");
			this.request=request;
			this.response=response;
			this.out = response.getWriter();	
			doPost(this.request, this.response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
			logger.info("In class messaging...at the post method");
			this.request=request;
			this.response=response;
			this.out = response.getWriter();	
			processMessageRequest();
	}
	
	public void processMessageRequest() throws IOException{
			try{
			HttpSession session = request.getSession();			String message=request.getParameter("msg");
			String entity_id=(String)session.getAttribute("entity_id");
			String receipient_id = request.getParameter("to"); 
			String tag=this.request.getParameter("tag");
			
			if(tag==null || tag.trim().equals("")){
				String error = "Tag could not be identified."+tag;
				logger.severe(error);
				giveErrorFeedBack("Sorry, something went wrong while processing your request.");
			}else{
						if(tag.equals("save")){
											if(entity_id==null)
												sendRedir();
											else if(message==null || message.trim().equals(""))
												giveErrorFeedBack("Sorry, blank messages are not allowed.");
											else if(receipient_id==null || receipient_id.trim().equals(""))
												giveErrorFeedBack("Error sending message: receipient could not be identified.");
											else{
												if(request.getParameter("fetch") != null)
													saveMessageReturning(message, entity_id, receipient_id);
												else
													saveMessage(message, entity_id, receipient_id);
											
											}
													
						}else if(tag.equals("retrieve_spec")){
								String id =(String) request.getParameter("id");
								String id2 =(String) request.getParameter("id2");
								if(id!=null && id2!=null){
										fetchSpecMessages(entity_id, id, id2);
								}else{
								//the id is not set
								giveErrorFeedBack("Sorry, you do not have any messages to view.");
								logger.severe("Messages requested with id null by user id = "+ entity_id );
								}
								
						}else if(tag.equals("retrieve_all")){
								logger.info("id-fetch all messages:"+entity_id);
								fetchAllMessages(entity_id);
						}
				}
			}catch(NullPointerException ex){
					logger.severe("Error: "+HarambesaUtils.getStackTrace(ex));
					giveErrorFeedBack("Sorry, something went wrong while processing your request.");
			}
	}
	
	public void sendRedir(){
			JSONObject obj  = new JSONObject();
			obj.put("success", 0);
			obj.put("redir", "login");
			obj.put("message", "Your session has expired, please login to continue");
			out.println(obj);
	}
	
	public void giveErrorFeedBack(String message){
				JSONObject obj=new JSONObject();
				obj.put("success", 0);
				obj.put("message", message);
				out.print(obj);
	}
	
	public void giveErrorFeedBack(String message, int errorCode){
				JSONObject obj=new JSONObject();
				obj.put("success", 0);
				obj.put("error",errorCode);
				obj.put("message", message);
				out.print(obj);
	}
	
	public void saveMessage(String message, String sender_id, String receipient_id){
				DBConnection db = new DBConnection();
				Connection con = db._getConnection();
				try{
					String [] ids = receipient_id.split(",");
					int len = ids.length;
					String sql="INSERT INTO messages (sender_entity_id, receipient_entity_id, message_txt) VALUES";
								sql+=" (?, ?, ?)"; 
							if(len >1)
								for(int i=1; i<len; i++)
										sql+=",(?, ?, ?)"; 
					System.out.println("SQL:"+sql);
					PreparedStatement preparedStatement = con.prepareStatement(sql);
					
					for(int i = 0; i<len*3; i +=3){
							preparedStatement.setInt(i+1, Integer.parseInt(sender_id));
							preparedStatement.setInt(i+2, Integer.parseInt(ids[(i/3)]));
							preparedStatement.setString(i+3,message);
					}

					int rs=preparedStatement.executeUpdate();	
					logger.info("Result:"+rs);
					if(rs>=1){
								JSONObject obj=new JSONObject();
								obj.put("success", 1);
								obj.put("message", "Your message has been sent.");
								out.print(obj);
					}else{
								JSONObject obj=new JSONObject();
								obj.put("success", 0);
								obj.put("message","Sorry, something went wrong while sending your message. Please try again.");
								out.print(obj);
					}
				}catch(Exception e){
								logger.severe(HarambesaUtils.getStackTrace(e));
								JSONObject obj=new JSONObject();
								obj.put("success", 0);
								obj.put("message", "Sorry, something went wrong while sending your message. Please try again.");
								out.print(obj);
				}finally{
								db.closeDB();
				}
	  }
	  
	  public void fetchAllMessages(String entity_id){
				
				if(entity_id==null){
					logger.severe("Error missing entity_id, not logged in  trying to access messages");
					giveErrorFeedBack("You must first log in to continue",1);
				}else{
				DBConnection db= new DBConnection();
				String sql="SELECT r,";
						    sql+=" message_id, s, message_txt,";
							sql+=" time_sent, status,first_name,last_name, profile_pic_path,";
							sql+=" to_char(time_sent, 'Dy FMDD Mon YYYY HH:MM AM') as date_received, ";
							sql+=" EXTRACT(DAY FROM(now()-time_sent)) as days,";
							sql+=" EXTRACT(YEAR FROM(now()-time_sent)) as yr";
							sql+=" FROM";
							sql+=" (SELECT DISTINCT";
							sql+=" ON (sender_entity_id)sender_entity_id, *";
							sql+=" FROM";
							sql+=" (SELECT * FROM"; 
							sql+=" ((SELECT"; 
							sql+=" DISTINCT"; 
							sql+=" ON (messages.sender_entity_id) messages.sender_entity_id,";
							sql+=" messages.message_id, messages.receipient_entity_id, messages.message_txt,";
							sql+=" messages.time_sent, messages.status, entitys.first_name, entitys.last_name,";
							sql+=" entitys.profile_pic_path, messages.receipient_entity_id as r, messages.sender_entity_id as s";
							sql+=" FROM messages";
							sql+=" RIGHT OUTER JOIN entitys";
							sql+=" ON entitys.entity_id=messages.sender_entity_id";
							sql+=" WHERE (sender_entity_id='"+entity_id+"' OR receipient_entity_id='"+entity_id+"')";
							sql+=" AND (sender_entity_id!='"+entity_id+"') ";
							sql+=" ORDER BY messages.sender_entity_id, time_sent DESC)";
							sql+=" UNION ALL";
							sql+=" (SELECT ";
							sql+=" DISTINCT ";
							sql+=" ON (messages.receipient_entity_id) messages.receipient_entity_id,";
							sql+=" messages.message_id, messages.sender_entity_id, messages.message_txt,";
							sql+=" messages.time_sent, messages.status, entitys.first_name, entitys.last_name, ";
							sql+=" entitys.profile_pic_path, messages.receipient_entity_id as r, ";
							sql+=" messages.sender_entity_id as s";
							sql+=" FROM messages ";
							sql+=" RIGHT OUTER JOIN entitys ";
							sql+=" ON entitys.entity_id=messages.receipient_entity_id";
							sql+=" WHERE (sender_entity_id='"+entity_id+"' OR receipient_entity_id='"+entity_id+"') ";
							sql+=" AND (receipient_entity_id!='"+entity_id+"')";
							sql+=" ORDER BY messages.receipient_entity_id, time_sent DESC))";
							sql+=" p";
							sql+=" ORDER BY time_sent DESC)";
							sql+=" k ORDER BY sender_entity_id, time_sent DESC)";
							sql+=" l ORDER BY time_sent DESC";
							
							ResultSet rs= db.readQuery(sql);
							//declare a jobject which will be used in outputting the response to the user.
							JSONObject obj = new JSONObject();
							if(rs != null){  
							try{ 
								if(!rs.next()){
								//this user has no messages
								obj.put("success","1");
								obj.put("total","0");
								obj.put("message","You do not have any messages to view");
								out.println(obj);
								db.closeDB();
								}else{
									Date dNow = new Date( );
									SimpleDateFormat ft = new SimpleDateFormat ("E");
									obj.put("t",ft.format(dNow));
									int total_messages=0;
									obj.put("success","1");
									JSONObject mess_array= new JSONObject();
									JSONArray jArrayAll=new JSONArray();
								do{
									JSONObject messages = new JSONObject();
									messages.put("message", rs.getString(4));
									messages.put("time_sent",rs.getString(5));
									messages.put("profile_pic",rs.getString(9));
									messages.put("f_name",rs.getString(7));
									messages.put("l_name", rs.getString(8));
									messages.put("date",rs.getString(10));
									messages.put("days",rs.getString(11));
									messages.put("yr", rs.getString(12));
									messages.put("sender_id",rs.getString(1));
									messages.put("receipient_id",rs.getString(3));
									jArrayAll.add(messages);
									total_messages++;
								}while(rs.next());
									obj.put("total", total_messages);
									obj.put("id",entity_id);
									obj.put("messages", jArrayAll);
									out.println(obj);
									db.closeDB();
								}
								}catch(SQLException ex){
										logger.severe(HarambesaUtils.getStackTrace(ex));
										obj.put("success","0");
										obj.put("message","We could not load messages, please try again.");
										out.print(obj);
										db.closeDB();
								}
						}else{
								//could not fetch rs, something definitely went wrong
								//now, log the error and output the right message to the user.
								logger.severe("An error occured, could not fetch messages for user id "+entity_id );
								obj.put("success","0");
								obj.put("message","We could not load messages, please try again.");
								out.print(obj);
								db.closeDB();
						}
				}
	  }
	  
	  public void fetchSpecMessages(String entity_id, String id, String id2){
				
				if(entity_id==null){
					logger.severe("Error missing entity_id, not logged in  trying to access messages for id="+ id==null? null: id);
					giveErrorFeedBack("You must first log in to continue",1);
				}else if(id==null){
					logger.severe("sender id  missing for user id= "+entity_id);
					giveErrorFeedBack("You must first log in to continue",2);
				}else if(id2==null){
					logger.severe("sender id  missing for user id= "+entity_id);
					giveErrorFeedBack("Sorry, could not fetch messages",3);
				}
				else{
				if(id.equals(entity_id))
					entity_id=id2;
				DBConnection db= new DBConnection();
				String sql="SELECT messages.message_id, messages.sender_entity_id,"; 
							sql+=" messages.receipient_entity_id, messages.message_txt,";
							sql+="to_char(messages.time_sent, 'Dy dd Mon yyyy HH12:MI AM') as date_received, ";
							sql+=" EXTRACT(DAY FROM(now()-messages.time_sent)) as days,";
							sql+=" EXTRACT(YEAR FROM(now()-messages.time_sent)) as yr,";
							sql+=" messages.time_sent, messages.status, entitys.profile_pic_path, entitys.entity_id";
							sql+=" FROM messages";
							sql+=" LEFT OUTER JOIN entitys ON (messages.sender_entity_id= entitys.entity_id)";
							sql+=" WHERE (messages.receipient_entity_id = '"+entity_id+"' AND messages.sender_entity_id = '"+id+"') ";
							sql+=" OR (messages.receipient_entity_id = '"+id+"' AND messages.sender_entity_id = '"+entity_id+"') ORDER BY time_sent ASC";
				String pic_sql = "SELECT profile_pic_path from entitys WHERE entity_id='"+entity_id+"'";
				ResultSet rs= db.readQuery(sql);
				//declare a jobject which will be used in outputting the response to the user.
			    JSONObject obj = new JSONObject();
					if(rs != null){
						try{ 
						if(!rs.next()){
							//this user has no messages
							logger.severe("user="+entity_id+" accessing messages for sender="+id+" but no messages found");
							obj.put("success","1");
							obj.put("total","0");
							obj.put("message","You have no messages to view.");
							out.println(obj);
							db.closeDB();
						}else{
							String profile_pic=null;	
							ResultSet rs2= db.readQuery(pic_sql);
							if(rs2!=null && rs2.next())
									profile_pic=rs2.getString(1);
							logger.info(rs2.getString(1));
							logger.info(profile_pic);
							Date dNow = new Date( );
							SimpleDateFormat ft = new SimpleDateFormat ("E");
							int total_messages=0;
							obj.put("success","1");
							obj.put("t",ft.format(dNow));
							JSONObject mess_array= new JSONObject();
							JSONArray jArrayAll=new JSONArray();
							JSONArray jArrayPics= new JSONArray();
							JSONObject picsProf= new JSONObject();
							String pic_path=null;
						do{
							 JSONObject messages = new JSONObject();
							 messages.put("sent_by", rs.getString(2));
							 messages.put("msg", rs.getString(4));
							 messages.put("date",rs.getString(5));
							 logger.info("Time:"+rs.getString(5));
							 messages.put("days",rs.getString(6));
							 messages.put("year",rs.getString(7));
							 messages.put("status", rs.getString(9));
							 if(rs.getString(11)!=null && rs.getString(11).equals(id))
									pic_path=rs.getString(10);
							 jArrayAll.add(messages);
							 total_messages++;
						}while(rs.next());
							JSONObject objPics= new JSONObject();							
							obj.put("pic_path", pic_path);
							obj.put("profile_pic", profile_pic);
							obj.put("total", total_messages);
							obj.put("messages", jArrayAll);
							out.println(obj);
							db.closeDB();
						}
						}catch(SQLException ex){
								logger.severe(HarambesaUtils.getStackTrace(ex));
								obj.put("success","0");
								obj.put("message","We could not load messages, please try again.");
								out.print(obj);
								db.closeDB();
						}
			     }else{
						//could not fetch rs, something definitely went wrong
						//now, log the error and output the right message to the user.
						logger.severe("An error occured, could not fetch messages for user id "+entity_id );
						obj.put("success","0");
						obj.put("message","We could not load messages, please try again.");
						out.print(obj);
						db.closeDB();
			     }
		}
	}

	public void saveMessageReturning(String message, String sender_id, String receipient_id){
				
				DBConnection db = new DBConnection();
				Connection con = db._getConnection();
				try{
					String [] ids = receipient_id.split(",");
					int len = ids.length;
					String sql="INSERT INTO messages (sender_entity_id, receipient_entity_id, message_txt) VALUES";
								sql+=" (?, ?, ?)"; 
							if(len >1)
								for(int i=1; i<len; i++)
										sql+=",(?, ?, ?)"; 
							sql+=" RETURNING ";
							sql+="	messages.message_id, messages.sender_entity_id,"; 
							sql+=" messages.receipient_entity_id, messages.message_txt,";
							sql+=" to_char(messages.time_sent, 'Dy dd Mon yyyy HH12:MI AM') as date_received, ";
							sql+=" EXTRACT(DAY FROM(now()-messages.time_sent)) as days,";
							sql+=" EXTRACT(YEAR FROM(now()-messages.time_sent)) as yr,";
							sql+=" messages.time_sent, messages.status";
							
					System.out.println("SQL:"+sql);
					PreparedStatement preparedStatement = con.prepareStatement(sql);
					
					for(int i = 0; i<len*3; i +=3){
							preparedStatement.setInt(i+1, Integer.parseInt(sender_id));
							preparedStatement.setInt(i+2, Integer.parseInt(ids[(i/3)]));
							preparedStatement.setString(i+3,message);
					}

					ResultSet rs=preparedStatement.executeQuery();	
					logger.info("Result:"+rs);
					if(rs != null){
								if(rs.next()){
										JSONObject obj=new JSONObject();
										obj.put("success", 1);
										obj.put("msg_id", rs.getString(1));
										obj.put("sent_by",rs.getString(2));
										obj.put("received_by", rs.getString(3));
										obj.put("msg", rs.getString(4));
										obj.put("date", rs.getString(5));
										obj.put("days", rs.getString(6));
										Date dNow = new Date();
										SimpleDateFormat ft = new SimpleDateFormat ("E");
										obj.put("t",ft.format(dNow));
										out.print(obj);
										out.close();
								}
					}else{
								JSONObject obj=new JSONObject();
								obj.put("success", 0);
								obj.put("message","Sorry, something went wrong while sending your message. Please try again.");
								out.print(obj);
								out.close();
					}
				}catch(Exception e){
								logger.severe(HarambesaUtils.getStackTrace(e));
								JSONObject obj=new JSONObject();
								obj.put("success", 0);
								obj.put("message", "Sorry, something went wrong while sending your message. Please try again.");
								out.print(obj);
				}finally{
								db.closeDB();
				}
		}
}