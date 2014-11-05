package com.harambesa.gServices; 

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


public class FriendList extends HttpServlet{

	HttpServletRequest request=null; 
	String sender_id=null;
	HttpServletResponse response=null;
	PrintWriter out = null;	
	Logger log= null;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException{
		
			this.request=request;
			this.response=response;
			out = response.getWriter();	
			doPost(this.request, this.response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
			out = response.getWriter();	;	
			if(this.request == null)
					this.request=request;
			if(this.response==null)
					this.response=response;
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			log=Logger.getLogger(FriendList.class.getName());
			
			checkLogin();
	}
	
	public void checkLogin() throws IOException{
			HttpSession session = request.getSession();
			String tag=request.getParameter("tag");
			sender_id=(String)session.getAttribute("entity_id");
			if(sender_id==null)
				sendRedir();
			else
				fetchFriends();	
	}
	
	public void sendRedir(){
			//this user's session has expired send a login redirect
			JSONObject obj=new JSONObject();
 			obj.put("redir","../login");
 			obj.put("message", "Your session has expired. Please login to continue.");
 			out.println(obj);
	}
	
	public void fetchFriends(){
				String [] q = getRequestQ();
				int size = q.length;
				DBConnection db=new DBConnection(); 
				Connection con= db._getConnection();
				if(size<1){
					JSONObject obj = new JSONObject();
					out.println(obj);
				}else{
					//this part provides an opportunity for optimization as 
					//filtering can be based on a couple of things which include number 
					//of points and connection status
					try{
					String frensSQL =null;
					PreparedStatement preparedStatement = null;
						if(size==1){
									//user has only entered one name or a part of it
									frensSQL = "SELECT entity_id, first_name, last_name, profile_pic_path FROM entitys";
									frensSQL+=" WHERE LOWER(first_name) LIKE LOWER(?)";
									frensSQL+=" AND entity_id !="+sender_id;
									frensSQL+=" AND is_active = TRUE";
									frensSQL+=" AND gender !=''";
									frensSQL+=" OR LOWER(last_name) LIKE LOWER(?)";
									frensSQL+=" AND entity_id !="+sender_id;
									frensSQL+=" AND is_active = TRUE";
									frensSQL+=" AND gender !=''";
									log.info("FriendSQL="+frensSQL);
									preparedStatement = con.prepareStatement(frensSQL);
									preparedStatement.setString(1, q[0]+"%");
									preparedStatement.setString(2, q[0]+"%");
						}else if(size>1){
							//now that the user seems to have entered more than one 
							//names to search for. handle the first two and ignore the rest
								frensSQL = "SELECT entity_id, first_name, last_name, profile_pic_path FROM entitys";
								frensSQL+=" WHERE LOWER(first_name) LIKE LOWER(?) AND LOWER(last_name) LIKE LOWER(?)";
								frensSQL+=" AND entity_id !="+sender_id;
								frensSQL+=" AND is_active = TRUE";
								frensSQL+=" AND gender != ''";
								frensSQL+=" OR LOWER(first_name) LIKE LOWER(?) and LOWER(last_name) LIKE LOWER(?)"; 
								frensSQL+=" AND entity_id !="+sender_id;
								frensSQL+=" AND is_active = TRUE";
								frensSQL+=" AND gender !=''";

								preparedStatement = con.prepareStatement(frensSQL);
								preparedStatement.setString(1, q[0]+"%");
								preparedStatement.setString(2,  q[1]+"%");
								preparedStatement.setString(3,  q[1]+"%");
								preparedStatement.setString(4,  q[0]+"%");
									
						}
						
						ResultSet rs= preparedStatement.executeQuery();
							if(rs!=null){
									if(!rs.next()){
										//there are no matches for the current searched name
										//now return an empty array 
										JSONObject output = new JSONObject(); 
										JSONArray jArray = new JSONArray();
										output.put("total",0);
										output.put("friends",jArray);
										out.println(output);
									}else{
										JSONObject names = new JSONObject();
										JSONArray jArray = new JSONArray();
										int total=0;
										do{
												JSONObject obj=new JSONObject();
												obj.put("id",rs.getString(1));
												obj.put("name", rs.getString(2)+" "+rs.getString(3));
												obj.put("profile_pic",rs.getString(4));
												jArray.add(obj);
												names.put("friends",jArray);
												total++;
										}while(rs.next());
										names.put("total",total);
										out.println(names);
									}
							}
							}catch(SQLException sqle){
								log.severe(HarambesaUtils.getStackTrace(sqle));
							}finally{
									db.closeDB();
							}
				}
	}
	
	private String [] getRequestQ(){
			String qParam= this.request.getParameter("q").trim();
			String [] nameTokens = qParam.split(" ");
			return nameTokens;
	}
}
