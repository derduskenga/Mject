<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="com.harambesa.DBConnection.DBConnection"%>
<%@page import="com.harambesa.DBConnection.GlobalDresser"%>
<%@page import="com.harambesa.gServices.Donation"%>
<%@page import="com.harambesa.gServices.Offer"%>
<%@page import="java.sql.*"%>
<%@page import="javax.servlet.http.HttpSession"%>
<%@page import= "com.harambesa.mailing.Mail"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.*"%>
<%@page import = "com.harambesa.gServices.HarambesaUtils"%>
<%@page import = "com.harambesa.notification.Notifications"%> 
<%@page import = "com.harambesa.Utility.Utilities"%>

<%
  if(request.getSession().getAttribute("entity_id")==null){

	  JSONObject ob = new JSONObject();
	  response.sendRedirect("../login/");
	  ob.put("success",0);
	  ob.put("redir","../login");
	  out.print(ob);
  
  }else if(request.getParameter("tag")==null){
  
	  //page accesed directly and this is bad request
	  out.print("Bad request");
  }else{
  
	  String tag = request.getParameter("tag");
	  //String tag = "email_typed";
	  if(tag.equals("email_typed")){
		//output jsaveson data, basically email response
		JSONObject json = new JSONObject();		
		String entity_id = (String)request.getSession().getAttribute("entity_id"); 
		GlobalDresser gd = new GlobalDresser(entity_id);
		final String senderNames =gd.getNames();
		final String email = request.getParameter("email");
		Thread t = new Thread(){ 
		      public void run(){
				Mail mail = new Mail();
				mail.sendInvitationMail(senderNames,email);
		      }};  
		t.start();
		
		json.put("success",1);
		json.put("message","Invitation has been sent to this contact");
		out.print(json);

	  }else if(tag.equals("email_imported")){
	  
		  JSONArray jArray = new JSONArray();
		  
		 
		  String entity_id = (String)request.getSession().getAttribute("entity_id");
		  
		  GlobalDresser gd = new GlobalDresser(entity_id);
		  final String senderNames =gd.getNames();
		  
		  String str = request.getParameter("emailsString");
		  
		  String []emailName = str.split("\\,");
		  JSONObject jobj = new JSONObject();
		  for(int i=0; i<emailName.length; i++){
			
			String [] nameEmailSeperate =  emailName[i].split("\\|");
			final String receiverEmail = nameEmailSeperate[0];
			final String receiverName = nameEmailSeperate[1];
			
			Thread t = new Thread(){ 
				public void run(){
					Mail mail = new Mail();
					mail.sendInvitationMail(receiverName,senderNames,receiverEmail);
			}};
			t.start();
		  }
		  jobj.put("success",1);
		  jobj.put("message","Invitation has been sent to your contacts");
		  
		  jArray.add(jobj);
		  
		  out.print(jArray);
	
	}else if(tag.equals("currency")){	    
	    DBConnection db = new DBConnection();	    
	    String query = "SELECT currency_id,currency_name,currency_symbol FROM currency";
	    ResultSet rs = db.readQuery(query);
	    JSONArray jArray = new JSONArray();	    
	    while(rs.next()){       
		
		JSONObject obj=new JSONObject();
		obj.put("id", rs.getString(1));
		obj.put("name", rs.getString(2));
		obj.put("symbol", rs.getString(3));
		jArray.add(obj);
	  
	  }
	    out.print(jArray);
	    db.closeDB();

	  }else if(tag.equals("categories")){
	    DBConnection db = new DBConnection();	    
	    String query = "SELECT programme_id,programme_name FROM programmes";
	    ResultSet rs = db.readQuery(query);
	    JSONArray jArray = new JSONArray();
	    
	    while(rs.next()){            
		JSONObject obj=new JSONObject();
		obj.put("id", rs.getString(1));
		obj.put("name", rs.getString(2));
		jArray.add(obj);
	  
	  }
	    out.print(jArray);
	    db.closeDB();
	  
	  }else if(tag.equals("post_request")){
		response.setContentType("application/json");
		String category = request.getParameter("category");
		String f_date= request.getParameter("f_date");
		String currency= request.getParameter("currency");
		double amount = Double.parseDouble(request.getParameter("amount"));
		String summary = request.getParameter("summary");
		String details = request.getParameter("details");
		String entity_id = (String)request.getSession().getAttribute("entity_id");
		//String entity_id = "1";//will be removed prior to intergration.
		
		DBConnection db =  new DBConnection();
		
		
		String query  = "INSERT INTO donation_requests (entity_id,programme_id,expected_funding_date,currency_id,donation_request_amount,request_summary,details,donation_already_made) VALUES('" + entity_id + "','" + category + "','" + f_date + "','" + currency + "','" + amount + "','" + summary + "','" + details + "','0')";
		System.out.println(query);
		ResultSet result = db.executeQuery(query,"t");//t is just a string used to overload
		JSONArray jArray = new JSONArray();
		JSONObject obj=new JSONObject();
		
		if(result.next()){
		    //data inserted successifully
		    HttpSession _session = request.getSession();
		    
		    java.util.Date now = new java.util.Date();
		    
		    String post_date = now.toString();
		
		  
		    String f_name = (String)_session.getAttribute("f_name");
		    String l_name = (String)_session.getAttribute("l_name");
		    String profile_pic_path = (String)_session.getAttribute("profile_pic_path");
		    obj.put("success", 1);
		    obj.put("message", "You have made a post");
		    obj.put("full_name",f_name + " " + l_name);
		    obj.put("profile_pic_path", profile_pic_path);
		    obj.put("entity_id",entity_id);
		    obj.put("post_date",returnedTimeStamp("donation_requests","donation_request_date",result.getInt(1),"donation_request_id"));
		    obj.put("record_id",result.getInt(1));
		    
		    
		    System.out.print(result.getInt(1));
		    
		}else{
		  
		  //something went wrong
		    obj.put("success", 0);
		    //obj.put("message", "This is quite embrassing !. An error occured. Please try again");
		    obj.put("message","This is quite embarassing. An error occured. Please try again");
		
		}
		
		//jArray.add(obj);
		out.print(obj);
		db.closeDB();
		
	  
	  }else if(tag.equals("request_posts")){
		//retrieve donation request posts
		
		DBConnection db =  new DBConnection();
		JSONObject outerJSONObject = new JSONObject();
		HttpSession p_session = request.getSession();
		String user_entity_id = (String)p_session.getAttribute("entity_id");
		
		//String user_entity_id = "1";
		
		
		JSONArray outerJSONArray = new JSONArray();
		JSONArray innerJSONArray = new JSONArray();
		boolean complete =false;
	      
		String post_query = "SELECT donation_requests.donation_request_id,donation_requests.entity_id,entitys.first_name,entitys.last_name," +
		    " entitys.profile_pic_path,TO_CHAR(donation_requests.donation_request_date,'YYYY') AS y,donation_requests.expected_funding_date," +
		    " donation_requests.donation_request_amount,donation_requests.request_summary,donation_requests.details," +
		    " programmes.programme_name,currency.currency_symbol," + 
		    " TO_CHAR(donation_requests.donation_request_date,'Month') AS m," +
		    " TO_CHAR(donation_requests.donation_request_date,'DD') AS d," +
		    " TO_CHAR(donation_requests.donation_request_date,'HH12:MI AM') AS t" +
		    " FROM donation_requests,entitys,programmes," +
		    " currency WHERE donation_requests.complete=FALSE" +
		    " AND entitys.entity_id=donation_requests.entity_id " +
		    " AND programmes.programme_id=donation_requests.programme_id" +
		    " AND currency.currency_id=donation_requests.currency_id" +
		    " ORDER BY donation_requests.donation_request_id ASC";	    
	      
	      try{
	      
			String visibility_class_name = "";
			String isActive = "";
			ResultSet postRs = db.readQuery(post_query);
			//out.print(db.getLastErrorMsg());
			if(!postRs.next()){
				//do nothing
			}else{

				do{
					//out.println(fetchPostComments(Integer.parseInt(postRs.getString(1))));
					JSONObject innerJSONObject = new JSONObject();
					JSONArray arr = new JSONArray();
					innerJSONObject.put("donation_request_id",postRs.getString(1));
					innerJSONObject.put("post_owner_entity_id",postRs.getString(2));
					innerJSONObject.put("post_owner_name",postRs.getString(3) + " " + postRs.getString(4));
					innerJSONObject.put("post_owner_pic_path",postRs.getString(5));
					innerJSONObject.put("donation_request_date",postRs.getString(6));
					innerJSONObject.put("donation_request_amount",postRs.getString(8));
					innerJSONObject.put("expected_funding_date", postRs.getString(7));
					innerJSONObject.put("donation_request_summary",postRs.getString(9));
					innerJSONObject.put("donation_request_details",postRs.getString(10));
					innerJSONObject.put("programme_name",postRs.getString(11));
					innerJSONObject.put("currency_symbol",postRs.getString(12));
					
					GlobalDresser global_dresser = new GlobalDresser(user_entity_id);
					String already_donated = global_dresser.getDonationAlreadyMade(postRs.getString(1));
					innerJSONObject.put("already_donated",already_donated);
					String t = postRs.getString(15);
					String d = postRs.getString(14);
					String m = postRs.getString(13);
					String y = postRs.getString(6);
					
					innerJSONObject.put("requested_on",returnedTimeStamp("donation_requests","donation_request_date",Integer.parseInt(postRs.getString(1)),"donation_request_id"));
					
					if(user_entity_id.equals(postRs.getString(2))){
						visibility_class_name = "hidden";
						isActive = "disabled";
					
					}else{
						visibility_class_name = "no";
						isActive = "no";
					}
					
					innerJSONObject.put("visibility_class_name",visibility_class_name);
					innerJSONObject.put("isActive",isActive);
					
					innerJSONObject.put("recommendation_count",fetchRecommendationCount(Integer.parseInt(postRs.getString(1))));
					innerJSONObject.put("button_label",determineRecommendationLabel(Integer.parseInt(postRs.getString(1)),Integer.parseInt(user_entity_id)));
					
					
					
					innerJSONObject.put("comments",fetchPostComments(Integer.parseInt(postRs.getString(1))));
					outerJSONArray.add(innerJSONObject);
					
				}while(postRs.next());
				
					outerJSONObject.put("requests",outerJSONArray);
					outerJSONObject.put("status","OK");
					outerJSONObject.put("message","Load more requests...");     
					 response.setContentType("application/json");
					out.println(outerJSONObject);  
			}
	      
		}catch(SQLException sqle){
		
		
		}finally{
			db.closeDB();
		} 
	      
	
	  }else if(tag.equals("global_dresser")){
	      
	      HttpSession _session = request.getSession();
	      String f_name = (String)_session.getAttribute("f_name");
	      String l_name = (String)_session.getAttribute("l_name");
	      String profile_pic_path = (String)_session.getAttribute("profile_pic_path");
	      String entity_id = (String)_session.getAttribute("entity_id");
	      GlobalDresser globalDresser = new GlobalDresser(entity_id);
	      
	      JSONObject obj = new JSONObject();
	      
	      obj.put("full_names", f_name + " " + l_name);
	      obj.put("profile_pic_path",profile_pic_path);
	      obj.put("entity_id",entity_id);
	      obj.put("harambesa_balance",globalDresser.getHarambesaBalance());
	      obj.put("harambesa_points",globalDresser.getHarambesaPoints());
	      //object.put("entity_id",entity_id);
	      response.setContentType("application/json");
	      out.print(obj);
	  }else if(tag.equals("post_new_comment")){      
	      String donation_request_id = request.getParameter("donation_request_id");
	      String entity_id = request.getParameter("entity_id");
	      String comment_text = request.getParameter("comment_text").trim();
	JSONObject obj = new JSONObject();
	if(!comment_text.equals("")){

	
		DBConnection db = new DBConnection();
		String query  = "INSERT INTO donation_request_comments (donation_request_id,donation_request_comment_text,comment_owner_entity_id) VALUES('" + donation_request_id + "','" + comment_text + "','"  + entity_id + "')";	
		Notifications noti = new Notifications((String)request.getSession().getAttribute("entity_id"));
		String []recipients  = noti.fetchDonationRequestOwner(donation_request_id);
		
		String type = "donation_request_comment";
		String notification_url = Utilities.NOTIFICATION_URL + "home/?type=" + type + "&dri=" + donation_request_id;
		String notification_msg = "<a href='../user/?user=" + (String)request.getSession().getAttribute("entity_id") + "'>" + new GlobalDresser((String)request.getSession().getAttribute("entity_id")).getNames() +"</a> commented on your donation request";
		String notification_originator = (String)request.getSession().getAttribute("entity_id");
		noti.saveNotification(notification_url, notification_msg, notification_originator,recipients);
		
		try{
			ResultSet result_ = db.executeQuery(query,"t");

			if(result_.next()){
			
				int record_id = result_.getInt(1);
				obj.put("success",1);
				obj.put("comment_id",record_id);
				obj.put("message","Comment has been post");
				obj.put("coment_time",returnedTimeStamp("donation_request_comments","donation_request_comment_date",record_id,"donation_requests_comment_id"));				
			}else{
				obj.put("success",0);
				obj.put("message","An error occured. Your comment will not be seen");
			} 
		
		}catch(SQLException sqle){
			System.out.print("error 11");
		
		}catch(NullPointerException npe){
		
			System.out.print("Error 222");
		}finally{
			db.closeDB();
		}
	}else{
		obj.put("success",0);
		obj.put("message","An error occured. Your comment will not be seen");
	}
	      //db.closeDB();
	      out.print(obj);
	  
	  }else if(tag.equals("recommend")){
		String donation_request_id = request.getParameter("donation_request_id");
		//String recommendation_owner = request.getParameter("recommendation_owner");
		String recommendation_owner = (String)request.getSession().getAttribute("entity_id");
		String post_owner_entity_id = request.getParameter("recommendation_owner");
		JSONObject obj = new JSONObject();
		if(!isRecommended(recommendation_owner,donation_request_id)){
			DBConnection db = new DBConnection();
			try{
				String query = "INSERT INTO recommendations" + 
						" (donation_request_id,entity_id,recommended_post_owner_entity_id)" +
						" VALUES('" + donation_request_id + "','" + recommendation_owner + "','" + post_owner_entity_id + "')";
				String Queryfeedback = db.executeQuery(query);    
				
				
				if(Queryfeedback == null){
					
					obj.put("success",1);
					obj.put("message","Reccomendation count updated");
				
				}else{
					obj.put("success",0);
					obj.put("message","Error");
				}
			}catch(Exception ex){
				obj.put("success",0);
				obj.put("message",ex.getMessage().toString());
			}finally{
				db.closeDB();
			}
		}else{
			obj.put("success",0);
			obj.put("message","You have already recommended this donation request");
		}	
		
		out.print(obj);
	
	}else if(tag.equals("unrecommend")){
	
		String donation_request_id = request.getParameter("donation_request_id");
		String recommendation_owner = request.getParameter("recommendation_owner");
		String entity_id = (String)request.getSession().getAttribute("entity_id"); 
		JSONObject obj = new JSONObject();
		
		if(isRecommended(entity_id,donation_request_id)){
			boolean unrecommend_flag = unrecommend(Integer.parseInt(donation_request_id),Integer.parseInt(entity_id));			
			if(unrecommend_flag){				
				obj.put("success",1);
				obj.put("message","Unrecommend");			
			}else{
				obj.put("success",0);
				obj.put("message","Unrecommend failed");
			}
		}else{
			obj.put("success",0);
			obj.put("message","You have not recommended this donation request. Therefore, you can't unrecommend it");
		}
		out.print(obj);

	}else if(tag.equals("isConnected")){
	    
	    String sender = request.getParameter("sender");
	    String receiver = request.getParameter("receiver");
	    JSONObject obj = new JSONObject();
	    //String sender = "3";
	    //String receiver = "2";
	    
	    if(sender.equals(receiver)){
		obj.put("success",2);
		obj.put("message","This post belongs to you, no need of messaging yourself");
	    }else{
		GlobalDresser gd = new GlobalDresser(sender);
		boolean isConnected = gd.isConnected(Integer.parseInt(receiver));
	      
		if(isConnected){
		      
		      obj.put("success",1);
		      obj.put("message","You are connected");
		      
		  }else{
		      obj.put("success",0);
		      obj.put("message","You are not connected with the request owner. You need to send connection request");
		  }
	    }
	  
	      
	      out.print(obj);
	
	}else if(tag.equals("connect")){
		String requestor = request.getParameter("sender");
		String requestee = request.getParameter("receiver");
		JSONObject obj = new JSONObject();
		GlobalDresser gb = new GlobalDresser(requestor);
		
		if(gb.isRequestExist(Integer.parseInt(requestee))){
			// a request has been sent but has not been accepted
			obj.put("success",0);
			obj.put("message","You have already sent a connection request but it has not been accepted");  
		}else{
			obj = gb.saveConnectionRequest(requestee);
		}
		out.print(obj);
	
	}else if(tag.equals("get_names")){
		
		String id = request.getParameter("id");
		//String id = "1";
		GlobalDresser gd = new GlobalDresser(id);
		
		String names = gd.getNames();
		
		JSONObject obj = new JSONObject();
		
		if(!names.equals("")){
			obj.put("success",1);
			obj.put("full_names",names);
			obj.put("message","Names have been fetched");
		
		}else{
			obj.put("success",0);
			obj.put("message","Names were not fetched");
		}
		
		out.print(obj);
	
	}else if(tag.equals("save")){
		String from = request.getParameter("from");
		String to = request.getParameter("to");
		String msg = request.getParameter("msg");
		out.print(saveMessage(msg,from,to));
	}else if(tag.equals("requestedamount")){
		out.print(fetchRequestedAmount(request.getParameter("id")));
		//out.print(fetchRequestedAmount("1"));
	}else if(tag.equals("finalise_donation_by_account")){
		String donor = request.getParameter("donor");
		String donee = request.getParameter("donee");
		String amount_requested = request.getParameter("amount_requested");
		String amount_donated = request.getParameter("amount_donated");
		String currency_id = request.getParameter("currency");
		String source_type = request.getParameter("source_type");
		String request_id = request.getParameter("request_id");
		
		Donation donation = new Donation(amount_donated,currency_id,donor,amount_requested);
		out.print(donation.makeDonation(donor,donee,source_type,request_id));
		
	
	}else if(tag.equals("fetch_recommenders")){
		JSONObject jObj = new JSONObject();
		if((String)request.getSession().getAttribute("entity_id") == null){
			jObj.put("success",0);
			jObj.put("redir","../login");
			jObj.put("message","You are not logged in");
		}else{
			String entity_id = (String)request.getSession().getAttribute("entity_id");
			String donation_request_id = request.getParameter("donation_request_id");
			jObj = getRecommenders(donation_request_id);
		}
		
		out.print(jObj);
	}
  }
	      
	%>
	
	<%!
		public JSONObject getRecommenders(String postId){
			DBConnection db = new DBConnection();
			JSONObject obj = new JSONObject();
			JSONArray arrO = new JSONArray();
			String sql = "SELECT" +
					" entitys.entity_id,entitys.first_name,entitys.last_name" +
					" FROM entitys" +
					" RIGHT JOIN recommendations" +
					" ON entitys.entity_id=recommendations.entity_id" +
					" WHERE recommendations.donation_request_id='" + postId + "'" +
					" ORDER BY recommendations.recommendation_id ASC";
			try{
				ResultSet rs = db.readQuery(sql);
				if(!rs.next()){
					obj.put("success",1);
					obj.put("ok",0);
					obj.put("message","No recommendations yet");
				}else{
					obj.put("success",1);
					obj.put("ok",1);
					do{
						JSONObject innerObj = new JSONObject();
						innerObj.put("id",rs.getString(1));
						innerObj.put("name",rs.getString(2) + " " + rs.getString(3));
						arrO.add(innerObj);
						
					}while(rs.next());	
					obj.put("recommendations",arrO);
				}
			}catch(SQLException sqle){
				obj.put("success",0);
				obj.put("message",sqle.getMessage().toString());
			}catch(Exception ex){
				obj.put("success",0);
				obj.put("message",ex.getMessage().toString());
			}finally{
				db.closeDB();
			}
			return obj;
		}
	%>
	
	<%!
		public String getDayOfWeek(int to_char_extract){
			String day_of_week = "";
			
			if(to_char_extract == 1){
				day_of_week = "Sun";
			}else if(to_char_extract == 2){
				day_of_week = "Mon";
			}else if(to_char_extract ==3){
				day_of_week = "Tue";
			}else if(to_char_extract ==4){
				day_of_week = "Wed";
			}else if(to_char_extract == 5){
				day_of_week = "Thur";
			}else if(to_char_extract == 6){
				day_of_week = "Fri";
			}else if(to_char_extract == 7){
				day_of_week = "Sat";
			}
			return day_of_week;
		}
	%>
	
	<%!
		public String getTimeLabel(double elapsedSec, String y, String m, String day_of_month, String t, String day_of_week ){
			String label = "";
			if(elapsedSec < 10){
				label = "a few sec";
			}else if(elapsedSec>10 && elapsedSec<59){
				label = String.valueOf((int)Math.ceil(elapsedSec)) + " sec";
			}else if(elapsedSec>59 && elapsedSec<3600){
				label = String.valueOf((int)Math.ceil(elapsedSec/60)) + " min";
			}else if(elapsedSec/3600>1 && elapsedSec/3600<24){
				label = String.valueOf((int)Math.ceil(elapsedSec/3600)) + " hrs";
			}else if(elapsedSec/3600>24 && elapsedSec/3600<48){
				label = "Yesterday at " + t;
			}else if(elapsedSec/3600>48 && elapsedSec/86400<7){
				label = "On " + day_of_week + " at " + t;
			}else if(elapsedSec/86400>7 && elapsedSec/86400 < 30){
				label = day_of_month + " " + m + " at " + t;
			}else if(elapsedSec/86400>30 && elapsedSec/86400<365){
				label  = day_of_month + " " +m; 
			}else{
				label = m + " " + day_of_month + ", " + y;
			}
			return label;
		}
	%>
	
	<%!
		public String returnedTimeStamp(String table_name, String column_name, int record_id, String index_column_name){
			String timeStr = "";
			DBConnection db = new DBConnection();
			String query = "SELECT TO_CHAR("+column_name+",'YYYY') AS y," +
					" TO_CHAR("+column_name+", 'Month') AS m," +
					" TO_CHAR("+column_name+", 'DD') AS d," +				
					" TO_CHAR("+column_name+", 'HH12:MI AM') AS t," + 
					" EXTRACT(EPOCH FROM now()-" + column_name + ") AS elapsedSec," +
					" TO_CHAR("+column_name+", 'D') AS day_of_week" + 
					" FROM "+table_name+" WHERE "+index_column_name+"='" + record_id + "'";
			
			ResultSet rs = db.readQuery(query);		    
			try{
			
				if(rs.next()){
					String y = rs.getString(1);
					String m = rs.getString(2);
					String d = rs.getString(3);
					String t = rs.getString(4);
					double elapsedSec = rs.getDouble(5);
					String day_of_week = getDayOfWeek(rs.getInt(6));
					
					timeStr = getTimeLabel(elapsedSec, y, m, d, t, day_of_week);
					
					//timeStr = d + " " + m + ", " + y + " at " + t + " elapsedSec " + elapsedSec + " On " + day_of_week;

				}else{
					timeStr = "Failed to fetch time";
				}		    
			}catch(SQLException sqle){
					//out.print(sqle.getMessage().toString());
					return "SQLException exception " + sqle.getMessage().toString();
			}catch(NullPointerException npe){
				//out.print(npe.getMessage().toString());
				return "NullPointerException " + npe.getMessage().toString();
			}catch(Exception e){
				//out.print(e.getMessage().toString());
				return "general exception " + e.getMessage().toString();
			}finally{
				db.closeDB();
			}
			return timeStr;
		}
	%>
	
	<%!

	    public String fetchCurrencySymbol(String currency_id){
	    
		    DBConnection db = new DBConnection();
		    boolean complete = false;
		    String query = "SELECT currency_symbol FROM currency WHERE currency_id='" + currency_id +"'";
		    ResultSet rs = db.readQuery(query); 
		    String currency_symbol ="";
		    try{
			    if(rs.next()){
				    currency_symbol = rs.getString("currency_symbol");
			    }
		    }catch(SQLException sqle){

			    System.out.print(sqle.getMessage().toString());

		    }catch(NullPointerException npe){

			    System.out.print(npe.getMessage().toString());

		    }

		    db.closeDB();
		    return currency_symbol;
	    
	    }
%>

<%!
public JSONObject fetchRequestedAmount(String entity_id){

	DBConnection db = new DBConnection();
	boolean complete = false;
	String query = "SELECT currency_id, donation_request_amount FROM donation_requests WHERE entity_id='" + entity_id +"' AND complete='" + complete + "'";
	ResultSet rs = db.readQuery(query);
	
	JSONObject obj = new JSONObject();
	
	      
	try{
	    if(rs.next()){
		obj.put("success",1);
		obj.put("currency_id",rs.getString(1));
		obj.put("amount",rs.getString(2));
		obj.put("currency_symbol",fetchCurrencySymbol(rs.getString(1)));
	    }
	}catch(SQLException sqle){
	    
	    System.out.print(sqle.getMessage().toString());
	    obj.put("success",0);
	    obj.put("message","Error occured");
	
	}catch(NullPointerException npe){
	    
	    System.out.print(npe.getMessage().toString());
	    obj.put("success",2);
	    obj.put("message","No amount found");
	
	
	}
	
	db.closeDB();
	return obj;
}

%>

<%!

public JSONObject saveMessage(String message, String sender_id, String receipient_id){
		DBConnection db = new DBConnection();
		String sql="INSERT INTO messages (sender_entity_id, receipient_entity_id, message_txt) VALUES ('"+sender_id+"', '"+receipient_id+"', '"+message+"')";
		String rs= db.executeQuery(sql);			
		JSONObject obj=new JSONObject();
		
		if(rs==null){
			obj.put("success", 1);
			obj.put("message", "Message Sent");
			//out.print(obj);
		}else{
			
			obj.put("success", 0);
			obj.put("message","Sorry, An error occurred while sending your message.");
			//out.print(obj);
		}
		db.closeDB();		
		return obj;
}

%>

<%!
    public boolean unrecommend(int donation_request_id, int entity_id){
	
	DBConnection db = new DBConnection();	
	String query = "DELETE" +
			" FROM recommendations" +
			" WHERE donation_request_id='" + donation_request_id + "'" +
			" AND entity_id='" + entity_id + "'";
	
	String result = db.executeQuery(query);
	boolean flag = false;
	
	try{
	    if(result == null){
		flag = true;
	    }
	
	}catch(Exception e){
	    
	    System.out.print(e.getMessage().toString());
	}finally{
		db.closeDB();
	}
	
	return flag;
    }


%>

<%!
	public double fetchRecommendationCount(int donation_request_id){
		JSONObject obj = new JSONObject();
		DBConnection db = new DBConnection();
		double count = 0.0;

		String query = "SELECT" +
				" COUNT(donation_request_id)" +
				" FROM recommendations" +
				" WHERE donation_request_id='" + donation_request_id + "'";
		try{
			ResultSet result = db.readQuery(query);
			if(result.next()){
				count = Double.parseDouble(result.getString("count"));
			}

		}catch(SQLException sqle){
			System.out.print(sqle.getMessage().toString());
		}catch(NullPointerException npe){
			System.out.print("No records");
		}finally{
			db.closeDB();
		}
		return count;
	}

%>

<%!
    public String getTimestampRecordComment(int comment_id){
	    String timeStr = "time error";
	    DBConnection db = new DBConnection();
	    String query = "SELECT TO_CHAR(donation_request_comment_date,'YYYY') AS y," +
			    " TO_CHAR(donation_request_comment_date, 'Month') AS m," +
			    " TO_CHAR(donation_request_comment_date, 'DD') AS d," +				
			    " TO_CHAR(donation_request_comment_date, 'HH12:MI AM') AS t" + 
			    " FROM donation_request_comments WHERE donation_requests_comment_id='" + comment_id + "'";
	   
	    ResultSet rs = db.readQuery(query);
	    
	    try{
	    
		    if(rs.next()){
			    String y = rs.getString(1);
			    String m = rs.getString(2);
			    String d = rs.getString(3);
			    
			    String t = rs.getString(4);
			    
			    timeStr = "On " + d + " " + m + ", " + y + " at " + t;

		}
	    
	    }catch(SQLException sqle){
		    System.out.print(sqle.getMessage().toString());
	    }catch(NullPointerException npe){
		  System.out.print(npe.getMessage().toString());
	   }catch(Exception e){
		System.out.print(e.getMessage().toString());
	   }finally{
		  db.closeDB();
	   }
	    return timeStr;
    }
%>

<%!
	public String getTimestampRecordRequest(int donation_id){
		    String timeStr = "time error";
		    DBConnection db = new DBConnection();
		    String query = "SELECT TO_CHAR(donation_request_date,'YYYY') AS y," +
				    " TO_CHAR(donation_request_date, 'Month') AS m," +
				    " TO_CHAR(donation_request_date, 'DD') AS d," +				
				    " TO_CHAR(donation_request_date, 'HH12:MI AM') AS t" + 
				    " FROM donation_requests WHERE donation_request_id='" + donation_id + "'";
		  
		    ResultSet rs = db.readQuery(query);
		    
		    try{
		    
			    if(rs.next()){
				    String y = rs.getString(1);
				    String m = rs.getString(2);
				    String d = rs.getString(3);
				    
				    String t = rs.getString(4);
				    
				    timeStr = "On " + d + " " + m + ", " + y + " at " + t;

			}
		    
		    }catch(SQLException sqle){
			    System.out.print(sqle.getMessage().toString());
		    }catch(NullPointerException npe){
			  System.out.print(npe.getMessage().toString());
		  }catch(Exception e){
			System.out.print(e.getMessage().toString());
		  }finally{
			  db.closeDB();
		  }
		    return timeStr;
    }

%>

<%!
    public String determineRecommendationLabel(int donation_request_id, int entity_id){
	//returns Reccomendation button label: Reccomend or Unrecommend
	DBConnection db = new DBConnection();
	String query = "SELECT" +
			" entity_id, donation_request_id" +
			" FROM recommendations" +
			" WHERE entity_id='" + entity_id +"'" +
			" AND donation_request_id='" + donation_request_id + "'";
	ResultSet rs = db.readQuery(query);
	String label = "Recommend";
	
	try{
		if(rs.next()){
			label = "Unrecommend";
		}
	}catch(SQLException sqle){
		
		System.out.print(sqle.getMessage().toString());
	
	}catch(NullPointerException npe){
		
		System.out.print(npe.getMessage().toString());
		label= "Recommend";
	
	}
	
	db.closeDB();
	return label;
      
    }

%>

<%!
  public JSONArray fetchPostComments(int postId){     
      DBConnection db = new DBConnection();
      JSONArray jArray = new JSONArray();
      
      String commentQuery = "SELECT donation_request_comments.donation_requests_comment_id," +
              " donation_request_comments.comment_owner_entity_id," +
              " entitys.first_name," +
              " entitys.last_name," +
              " entitys.profile_pic_path," +
              " donation_request_comments.donation_request_comment_text," +
              " donation_request_comments.donation_request_comment_date" +
              " FROM donation_request_comments,entitys" +
	      " WHERE donation_request_comments.comment_owner_entity_id= entitys.entity_id" +
	      " AND donation_request_comments.donation_request_id='" + postId + "'" +
	      " ORDER BY donation_request_comments.donation_requests_comment_id ASC";
      
      ResultSet cRs = db.readQuery(commentQuery);
      
      try{
        if(!cRs.next()){
        //no data
        
        }else{
        
		do{
			JSONObject jObjCommentInner= new JSONObject();
			jObjCommentInner.put("comment_id",cRs.getString(1));
			jObjCommentInner.put("comment_owner_entity_id",cRs.getString(2));
			jObjCommentInner.put("comment_owner_names",cRs.getString(3) + " " + cRs.getString(4));
			jObjCommentInner.put("comment_owner_profile_pic_path",cRs.getString(5));
			jObjCommentInner.put("comment_text",cRs.getString(6));
			jObjCommentInner.put("comment_date",returnedTimeStamp("donation_request_comments","donation_request_comment_date",Integer.parseInt(cRs.getString(1)),"donation_requests_comment_id"));
			jArray.add(jObjCommentInner);
		
		}while(cRs.next());
        } 
    }catch(SQLException sqle){
        System.out.println(sqle.getMessage().toString());
    }finally{
	    db.closeDB();
    }   
      
      
      return jArray;
  }


%>

<%!

	public boolean isRecommended(String entity_id, String donation_request_id){
		DBConnection db = new DBConnection();
		boolean flag = false;
		
		try{
			String sql =  "SELECT" +
					" donation_request_id,entity_id" +
					" FROM recommendations" +
					" WHERE donation_request_id='" + donation_request_id + "'" +
					" AND entity_id='" + entity_id + "'";
			ResultSet rs = db.readQuery(sql); 
			if(rs.next()){
				flag = true;
			}
		}catch(Exception ex){
			//logger.error("error that led to rollback is: " + ex.getMessage().toString());
		}finally{
			db.closeDB();
		}	
		return flag;
	}
%>