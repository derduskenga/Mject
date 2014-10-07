package com.harambesa.connections; 

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

public class Connections extends HttpServlet{

	HttpServletRequest request=null; 
	String entity_id = null;
	HttpServletResponse response=null;
	PrintWriter out = null;	
	Logger logger= null;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException{
			doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
			this.request=request;
			this.response=response;
			this.response.setContentType("application/json");
			this.response.setCharacterEncoding("UTF-8");
			out = this.response.getWriter();
			logger=Logger.getLogger(Connections.class.getName());
			checkLogin();
	}
	
	//check if the user is logged in otherwise redirect them.
	public void checkLogin() throws IOException{
			HttpSession session = request.getSession();
			entity_id=(String)session.getAttribute("entity_id");
			if(entity_id==null)
				sendRedir();
			else
				fetchConnections();	
	}
	
	public void fetchConnections(){
				DBConnection db = new DBConnection();
				Connection con = db._getConnection();
				try{
				
				String sql ="SELECT connection_id, requestor_entity_id, requestee_entity_id,";
							sql +=" request_date, is_accepted, connection_is_blocked, ignored, is_cancelled,";
							sql +=" is_terminated, profile_pic_path, first_name, last_name, details";
							sql +=" FROM connections";
							sql +=" LEFT OUTER JOIN entitys";
							sql +=" ON requestor_entity_id =entity_id";
							sql +=" WHERE requestee_entity_id ="+entity_id;
							sql +=" AND is_accepted=FALSE";
							sql +=" AND ignored=FALSE";
							sql +=" ORDER BY request_date DESC";
				logger.info(sql);
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				
				if(rs != null){
					//the query executed properly
					if(rs.next()){
						JSONObject friends = new JSONObject();
						JSONArray friendsArray = new JSONArray();
						int totalConnections = 0;
						do{
								JSONObject currentFriend = new JSONObject();
								currentFriend.put("id",rs.getInt(1));
								currentFriend.put("reqo_id",rs.getInt(2));
								currentFriend.put("reqe_id",rs.getInt(3));
								currentFriend.put("is_acc",rs.getBoolean(5));
								currentFriend.put("is_ign",rs.getBoolean(7));
								currentFriend.put("pic_path",rs.getString(10));
								currentFriend.put("name",rs.getString(11)+" "+rs.getString(12));
								currentFriend.put("bio",rs.getString(13));
								friendsArray.add(currentFriend);
								totalConnections++;
						}while(rs.next());			
								friends.put("friends", friendsArray);
								friends.put("total", totalConnections);
								friends.put("success",1);
								out.println(friends);
								out.close();
					}else{
								JSONObject friends = new JSONObject();
								friends.put("total",0);
								friends.put("success",1);
								out.println(friends);
								out.close();
					}
				}else{
						giveErrorFeedBack("Something went wrong when loading your connections. Refreshing this page may help.");
				}
			}catch(Exception e){
					logger.severe(HarambesaUtils.getStackTrace(e));
					giveErrorFeedBack("Something went wrong when loading your connections.");
			}
	}
	
	public void giveErrorFeedBack(String message){
				JSONObject friends = new JSONObject();
				friends.put("total",0);
				friends.put("success",0);
				friends.put("message",message);
				out.println(friends);
				out.close();
	}
	
	public void sendRedir(){
			//this user's session has expired send a login redirect
			JSONObject obj=new JSONObject();
 			obj.put("redir","../login");
 			obj.put("message", "Your session has expired. Please login to continue.");
 			out.println(obj);
	}
}