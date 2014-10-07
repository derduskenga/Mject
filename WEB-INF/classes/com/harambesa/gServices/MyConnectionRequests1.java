package com.harambesa.gServices; 

import com.harambesa.DBConnection.DBConnection;
import java.io.StringWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse; 
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.PrintWriter;
import java.io.IOException;

// Extend HttpServlet class
public class MyConnectionRequests extends HttpServlet {
		
			HttpServletRequest request=null; 
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
					
					out = response.getWriter();
					this.request=request;
					this.response=response;
					getAllHarambesas();
					 String tag=null;
						if(request.getParameter("tag") == null ){
							log.severe("Tag is null at connection requests");
							response.sendRedirect("../error/");
						}else{	
							tag = request.getParameter("tag");
							log=Logger.getLogger(MyConnectionRequests.class.getName());
							processTag(tag);
							log.info("Tag at connection requests:"+tag);
						
							}
	 
			}
			
		public void processTag(String tag){
			if(tag.equals("getconnectionrequests")){
				
				getConnections();	
			}
			else if(tag.equals("acceptconnectionrequest")){
			acceptconnectionrequest();
			}else if(tag.equals("ignoreconnectionrequest")){
			ignoreconnectionrequest();
			}
		}// end of processTag()

		public void getConnections() {
			HttpSession session = request.getSession();
			String user_id=(String)session.getAttribute("entity_id");
				
			DBConnection db = new DBConnection();
			
			String sql="SELECT entitys.entity_id,entitys.first_name,entitys.last_name,entitys.profile_pic_path,connections.connection_id,connections.requestor_entity_id,connections.requestee_entity_id,connections.is_accepted FROM connections,entitys WHERE connections.is_accepted = FALSE AND connections.requestee_entity_id= '" +user_id+"' AND entitys.entity_id=connections.requestor_entity_id AND connections.ignored =FALSE ";
			
			ResultSet rs= db.readQuery(sql);		
			
			log.info(sql);
			try{
				JSONArray jArray = new JSONArray();
				while(rs.next()){            
					JSONObject obj=new JSONObject();
					obj.put("entity_id", rs.getString(1));
					obj.put("first_name", rs.getString(2));
					obj.put("last_name", rs.getString(3));
					obj.put("profile_pic_path", rs.getString(4));
					obj.put("connection_id", rs.getString(5));
					obj.put("requestor_entity_id", rs.getString(6));
					obj.put("requestee_entity_id", rs.getString(7));
					obj.put("is_accepted", rs.getString(8));
					jArray.add(obj);
				}		
				JSONObject obj=new JSONObject();
				obj.put("requests",jArray);
				out.print(obj);
				db.closeDB();
				}
				catch(SQLException ex){
					System.out.println(ex.toString());
				}
		}//end of getConnections
		
		public void acceptconnectionrequest()
		{
		//System.out.println("I am almost ready");
			HttpSession session = request.getSession();
			String user_id=(String)session.getAttribute("entity_id");
			String requestor =(String)request.getParameter("Requestor_id");

			DBConnection db = new DBConnection();

			String updsql="UPDATE connections SET is_accepted = 'true' WHERE requestor_entity_id=' "+requestor+"' AND requestee_entity_id='"+user_id+"' ";
			db.executeUpdate(updsql);
			
			
			db.closeDB();
		}//end of  acceptconnectionrequest()
		
		public void ignoreconnectionrequest(){
		
			HttpSession session = request.getSession();
			String user_id=(String)session.getAttribute("entity_id");
			String requestor =(String)request.getParameter("Requestor_id");
					
			DBConnection db = new DBConnection();
					
			String updsql="UPDATE connections SET ignored= 'true' WHERE requestor_entity_id=' "+requestor+"' AND requestee_entity_id='"+user_id+"'";
			db.executeUpdate(updsql);
		}//end of ignoreconnectionrequest()
		
		public static String getStackTrace(final Throwable throwable) {
					final StringWriter sw = new StringWriter();
					final PrintWriter pw = new PrintWriter(sw, true);
					throwable.printStackTrace(pw);
					return sw.getBuffer().toString();
		}//end of getStackTrace()
		
		public void getAllHarambesas(){
		System.out.println("Nimwaget wote");
		}
}//end of class  
  
