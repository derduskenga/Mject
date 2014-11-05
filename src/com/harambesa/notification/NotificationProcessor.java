package com.harambesa.notification;

import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse; 
import javax.servlet.http.HttpSession;

import com.harambesa.gServices.HarambesaUtils;
import com.harambesa.gServices.Offer;
import com.harambesa.DBConnection.DBConnection;
import com.harambesa.Utility.Utilities;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.io.PrintWriter;

public class NotificationProcessor extends HttpServlet{
	//fields
	HttpServletRequest request=null; 
	HttpServletResponse response=null;
	PrintWriter out = null;
	Logger log = Logger.getLogger(NotificationProcessor.class.getName());
	//String user_entity_id
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException {
		this.request=req;
		this.response=res;
		out = res.getWriter();
		doPost(req, res);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{  
		String u_id = (String)request.getSession().getAttribute("entity_id");
		out = response.getWriter();
		this.request=request;
		this.response=response;
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		JSONObject responseJObject = new JSONObject();
		
		String tag = request.getParameter("tag");
		if(tag == null ){
		      log.severe("Tag is null");
		      responseJObject.put("status","NO");
		      responseJObject.put("message","Missing parameters !");
		      out.println(responseJObject);
		}else{
			if(tag.equals("donation_request") || tag.equals("donation_request_comment")){
				getDonationRequest(request.getParameter("request_id"),u_id);
			}else if(tag.equals("s_offer") || tag.equals("m_offer") || tag.equals("offer")){
				log.info("tag is " + tag);
				String table_name = getTableName(tag.trim());
				String offer_id = request.getParameter("oi");
				log.info("offer:" + offer_id + " table name:" + table_name);
				if(table_name.equals("offers")){ //String id,String table_name, String entity_id
					fetchOfferPosts(offer_id,table_name,u_id);
				}else if(table_name.equals("service_offer")){
					fetchServiceOfferPosts(offer_id,table_name,u_id);
				}else if(table_name.equals("material_offer")){
					fetchMaterialOfferPosts(offer_id,table_name,u_id);
				}else{
					responseJObject.put("status","NO");
					responseJObject.put("message","Your request could not be fulfilled");
					out.println(responseJObject);
					out.close();
				}
			}else if(tag.equals("close_money_offer") || tag.equals("close_s_or_m_offer") || tag.equals("money_acceptance") || tag.equals("service_material_acceptance")){
				try{
					String table_name = request.getParameter("t");
					String offer_id = request.getParameter("oi");
					if(table_name.equals("offers")){
						fetchOfferPosts(offer_id,table_name,u_id);
					}else if(table_name.equals("service_offer")){
						fetchServiceOfferPosts(offer_id,table_name,u_id);
					}else if(table_name.equals("material_offer")){
						fetchMaterialOfferPosts(offer_id,table_name,u_id);
					}else{
						responseJObject.put("status","NO");
						responseJObject.put("message","Your request could not be fulfilled");
						out.println(responseJObject);
						out.close();
					}
				}catch(Exception ex){
					log.severe(HarambesaUtils.getStackTrace(ex));
					responseJObject.put("status","NO");
					responseJObject.put("message","Your request could not be fulfilled");
					out.println(responseJObject);
					out.close();
				}
			}else if(tag.equals("offer_application")){
				try{
					String table_name = request.getParameter("t");
					String offer_id = request.getParameter("oi");
					if(table_name.equals("offers")){
						fetchOfferPosts(offer_id,table_name,u_id);
					}else if(table_name.equals("service_offer")){
						fetchServiceOfferPosts(offer_id,table_name,u_id);
					}else if(table_name.equals("material_offer")){
						fetchMaterialOfferPosts(offer_id,table_name,u_id);
					}else{
						responseJObject.put("status","NO");
						responseJObject.put("message","Your request could not be fulfilled");
						out.println(responseJObject);
						out.close();
					}
				}catch(Exception ex){
					log.severe(HarambesaUtils.getStackTrace(ex));
					responseJObject.put("status","NO");
					responseJObject.put("message","Your request could not be fulfilled");
					out.println(responseJObject);
					out.close();
				}
			}else{
				responseJObject.put("status","NO");
				responseJObject.put("message","Your request could not be fulfilled");
				out.println(responseJObject);
				out.close();
			}
		}
	}	
	public void getDonationRequest(String id,String user_entity_id){
		JSONObject outerJson = new JSONObject();
		JSONArray outerJarray = new JSONArray();
		JSONObject jException = new JSONObject();
		if(isSessionActive()){
			DBConnection db=new DBConnection(); 
			Connection con= db._getConnection(); 
			String sql = "SELECT donation_request_id,donation_requests.entity_id," + //1 2
					" expected_funding_date,donation_request_amount," +//3 4
					" request_summary,donation_requests.details," + //5 6
					" donation_already_made,programme_name," + //7 8
					" currency_symbol,first_name," + //9 10
					" last_name,profile_pic_path," + //11 12
					" complete" + //13
					" FROM donation_requests" +
					" LEFT JOIN programmes" +
					" ON donation_requests.programme_id=programmes.programme_id" +
					" LEFT JOIN currency" +
					" ON donation_requests.currency_id=currency.currency_id" +
					" LEFT JOIN entitys" +
					" ON donation_requests.entity_id=entitys.entity_id" +
					" WHERE donation_request_id=?";
			
			try{
				String visibility_class_name = "";
				String isActive = "";
				String recommend_and_donate_visibility_class = "no";
				String is_complete = "no";
				
				PreparedStatement st = con.prepareStatement(sql); 
				int dri = Integer.parseInt(id);
				st.setInt(1,dri);
				ResultSet rs = st.executeQuery();
				if(rs != null){
					if(rs.next()){
						do{	
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("donation_request_id",rs.getString(1));
							jsonObj.put("post_owner_entity_id",rs.getString(2));
							jsonObj.put("post_owner_name",rs.getString(10) + " " + rs.getString(11));
							jsonObj.put("post_owner_pic_path",rs.getString(12));
							jsonObj.put("donation_request_date","no date");
							jsonObj.put("donation_request_amount",rs.getString(4));
							jsonObj.put("expected_funding_date", rs.getString(3));
							jsonObj.put("donation_request_summary",rs.getString(5));
							jsonObj.put("donation_request_details",rs.getString(6));
							jsonObj.put("programme_name",rs.getString(8));
							jsonObj.put("currency_symbol",rs.getString(9));
							jsonObj.put("already_donated_made",rs.getString(7));
							jsonObj.put("requested_on",new Offer(rs.getString(2)).getFormattedTimeStamp("donation_requests","donation_request_date",Integer.parseInt(rs.getString(1)),"donation_request_id"));
						
							if(user_entity_id.equals(rs.getString(2))){
								visibility_class_name = "hidden";
								isActive = "disabled";
							
							}else{
								visibility_class_name = "no";
								isActive = "no";
							}
							
							if(rs.getString(13).equalsIgnoreCase("t")){
								recommend_and_donate_visibility_class = "hidden";
								is_complete = "yes";
							}
							
							jsonObj.put("is_complete",is_complete);
							jsonObj.put("recommend_and_donate_visibility_class",recommend_and_donate_visibility_class);
							jsonObj.put("visibility_class_name",visibility_class_name);
							jsonObj.put("isActive",isActive);
							jsonObj.put("recommendation_count",fetchRecommendationCount(Integer.parseInt(rs.getString(1))));
							jsonObj.put("button_label",determineRecommendationLabel(rs.getString(1),user_entity_id));
							jsonObj.put("comments",fetchPostComments(Integer.parseInt(rs.getString(1))));
							
							outerJarray.add(jsonObj);
						}while(rs.next());
						
						outerJson.put("requests",outerJarray);
						outerJson.put("status","OK");
						outerJson.put("message","Load more requests...");  
						out.println(outerJson);
						out.close();
					
					}else{
						jException.put("status","NO");
						jException.put("message","No record was found");
						out.println(jException);
						out.close();
					}
				}else{
					jException.put("status","NO");
					jException.put("message","An error occured");
					out.println(jException);
					out.close();
				}				
			}catch(SQLException sqle){
				log.severe("Error: " + HarambesaUtils.getStackTrace(sqle));
				jException.put("status","NO");
				jException.put("message","An error occured");
				out.println(jException);
				out.close();
			}catch(Exception ex){
				log.severe("Error: " + HarambesaUtils.getStackTrace(ex));
				jException.put("status","NO");
				jException.put("message","An error occured");
				out.println(jException);
				out.close();
			}finally{
				db.closeDB();
			}
		}else{
			jException.put("status","NO");
			jException.put("redir","../login");
			jException.put("message","Session expired");
			out.println(jException);
			out.close();
		}
	}
	
	
	public JSONArray fetchPostComments(int postId){     
		DBConnection db = new DBConnection();
		Connection con = db._getConnection();
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
			" AND donation_request_comments.donation_request_id=?" +
			" ORDER BY donation_request_comments.donation_requests_comment_id ASC";
		try{
			PreparedStatement st = con.prepareStatement(commentQuery);
			st.setInt(1,postId);
			ResultSet cRs = st.executeQuery();					
			if(!cRs.next()){}
			else{
			
				do{
					JSONObject jObjCommentInner= new JSONObject();
					jObjCommentInner.put("comment_id",cRs.getString(1));
					jObjCommentInner.put("comment_owner_entity_id",cRs.getString(2));
					jObjCommentInner.put("comment_owner_names",cRs.getString(3) + " " + cRs.getString(4));
					jObjCommentInner.put("comment_owner_profile_pic_path",cRs.getString(5));
					jObjCommentInner.put("comment_text",cRs.getString(6));
					jObjCommentInner.put("comment_date",new Offer(cRs.getString(2)).getFormattedTimeStamp("donation_request_comments","donation_request_comment_date",Integer.parseInt(cRs.getString(1)),"donation_requests_comment_id"));
					jArray.add(jObjCommentInner);
				
				}while(cRs.next());
			} 
		}catch(SQLException sqle){
			log.severe(HarambesaUtils.getStackTrace(sqle));
		}catch(Exception ex){
			log.severe(HarambesaUtils.getStackTrace(ex));
		}finally{
			db.closeDB();
		}
		return jArray;
	}
	
	public String determineRecommendationLabel(String donation_request_id, String entity_id){
		DBConnection db = new DBConnection();
		Connection con = db._getConnection();
		String sql = "SELECT" +
				" entity_id, donation_request_id" +
				" FROM recommendations" +
				" WHERE entity_id=?" +
				" AND donation_request_id=?";
		String label = "Recommend";

		try{
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1,Integer.parseInt(entity_id));
			st.setInt(2,Integer.parseInt(donation_request_id));
			ResultSet rs = st.executeQuery();
			if(rs.next()){
				label = "Unrecommend";
			}
		}catch(SQLException sqle){
			log.severe(HarambesaUtils.getStackTrace(sqle));
		}catch(Exception ex){
			log.severe(HarambesaUtils.getStackTrace(ex));
		}finally{
			db.closeDB();
		}
		return label;
	}


	public double fetchRecommendationCount(int donation_request_id){
		JSONObject obj = new JSONObject();
		DBConnection db = new DBConnection();
		Connection con = db._getConnection();
		double count = 0.0;

		String sql = "SELECT" +
				" COUNT(donation_request_id)" +
				" FROM recommendations" +
				" WHERE donation_request_id=?";
		try{
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1,donation_request_id);
			ResultSet rs = st.executeQuery();
			if(rs.next()){
				count = Double.parseDouble(rs.getString(1));
			}
		}catch(SQLException sqle){
			log.severe("sqle error: count is" + count +" " + HarambesaUtils.getStackTrace(sqle));
		}catch(Exception ex){
			log.severe("General exception: count is: " + count + " " + HarambesaUtils.getStackTrace(ex));
		}finally{
			db.closeDB();
		}
		return count;
	}
	
	
	public boolean isSessionActive(){
		boolean flag = false;
		try{
			HttpSession sess=request.getSession();
			if((String)sess.getAttribute("entity_id") != null){
				flag = true;
			}
		}catch(Exception ex){
			log.severe("Error " + HarambesaUtils.getStackTrace(ex));
		}
		return flag;
	}
	

	public void fetchOfferPosts(String id, String table_name, String entity_id){
		if(isSessionActive()){
			DBConnection db = new DBConnection();
			Connection con = db._getConnection();
			
			JSONArray outerJsonArray = new JSONArray();
			JSONObject outerJsonObject = new JSONObject();
			String sql = "SELECT" +
					" offers.offer_id,offers.entity_id,offers.programme_id," +
					" offers.offer_date,offers.currency_id,offers.offer_amount," + 
					" offers.offer_summary,offers.offer_details," + 
					" entitys.entity_id,entitys.first_name," +
					" entitys.last_name,entitys.profile_pic_path, programmes.programme_id," + 
					" programmes.programme_name,currency.currency_id,"+ 
					" currency.currency_name," + //[16]
					" offers.accepted_application_id,offers.accepted" + //17 18
					" FROM offers,entitys,currency,programmes" +
					" WHERE offers.offer_id=?" +
					" AND offers.entity_id=entitys.entity_id" +
					" AND offers.programme_id=programmes.programme_id" +
					" AND offers.currency_id=currency.currency_id";
			//log.info("Query looged here: " + sql);
			
			try{
				String visibility_class_name = "";
				String apply_visibility_class = "no";
				String accept_visibility_class = "no";
				String is_offer_complete = "no";
				
				PreparedStatement st = con.prepareStatement(sql);
				st.setInt(1,Integer.parseInt(id));
				ResultSet rs = st.executeQuery();
				
				if(!rs.next()){
					outerJsonObject.put("status","NO");
					outerJsonObject.put("message","Offer does not exist or it has been removed");
					out.println(outerJsonObject);
					out.close();
				}else{
					do{
						//log.info("Catching something here offer_id: " + rs.getString(1));
						JSONObject obj = new JSONObject();
						
						obj.put("offer_id",rs.getString(1));
						obj.put("offer_entity_id",rs.getString(2));
						obj.put("programme_id",rs.getString(3));
						obj.put("offer_date",rs.getString(4));
						obj.put("currency_id",rs.getString(5));
						obj.put("offer_amount",rs.getString(6));
						obj.put("offer_summary",rs.getString(7));
						obj.put("offer_details",rs.getString(8));
						obj.put("names",rs.getString(10) + " " + rs.getString(11));
						obj.put("profile_pic_path",rs.getString(12));
						obj.put("programme_name",rs.getString(14));
						obj.put("currency_name",rs.getString(16));
						
						if(entity_id.equals(rs.getString(2))){
							visibility_class_name = "hidden";
							//visibility_class_name = "no";
						}else{
							visibility_class_name = "no";
						}
						
						if(rs.getString(18).equalsIgnoreCase("t")){
							apply_visibility_class = "hidden";
							accept_visibility_class = "hidden";
							is_offer_complete = "yes";
						}
						
						log.info("offer is d: " + rs.getString(1) + " table name is " + " entity_id is: " + rs.getString(2));
						
						obj.put("is_offer_complete",is_offer_complete);
						obj.put("apply_visibility_class",apply_visibility_class);
						obj.put("accept_visibility_class",accept_visibility_class);
						obj.put("visibility_class_name",visibility_class_name);
						obj.put("time_date_string",new Offer(entity_id).getFormattedTimeStamp("offers","offer_date",Integer.parseInt(rs.getString(1)),"offer_id"));
						obj.put("applications",new Offer(entity_id).fetchOfferApplications(rs.getString(1),table_name,rs.getString(2)));
						obj.put("offer_type","money");
						outerJsonArray.add(obj);
						
					}while(rs.next());
					outerJsonObject.put("status","OK");
					outerJsonObject.put("offers",outerJsonArray);
					outerJsonObject.put("message","Offer found");				
					out.println(outerJsonObject);
					out.close();
				}
			}catch(SQLException sqle){
				log.severe("Error is here: " + HarambesaUtils.getStackTrace(sqle));
			}catch(Exception ex){
				log.severe("Error is here: " + HarambesaUtils.getStackTrace(ex));
			}finally{
				db.closeDB();
			}
		}else{
			JSONObject jException = new JSONObject();	
			jException.put("status","NO");
			jException.put("redir","../login");
			jException.put("message","Session expired");
			out.println(jException);
			out.close();
		}
	} 
	public void fetchServiceOfferPosts(String id,String table_name, String entity_id){
		if(isSessionActive()){
			DBConnection db = new DBConnection();
			Connection con = db._getConnection();
			JSONArray outerJsonArray = new JSONArray();
			JSONObject outerJsonObject = new JSONObject();
			String sql = "SELECT"+
				" service_offer.s_offer_id,service_offer.s_entity_id," +
				" service_offer.s_offer_name,service_offer.s_offer_country," +
				" service_offer.s_offer_state,service_offer.s_offer_residence," +
				" service_offer.s_working_hours,service_offer.s_starting_date,"+  
				" service_offer.s_offer_details,entitys.first_name," +
				" entitys.last_name,entitys.profile_pic_path," +
				" service_offer.accepted_application_id,service_offer.status," +// 13 14
				" service_offer.approved" + // 15 16
				" FROM service_offer,entitys" +
				" WHERE service_offer.s_offer_id=?" +
				" AND service_offer.s_entity_id=entitys.entity_id";
				//" AND offer_applications.table_name=?::offer_table" +
				//" AND service_offer.accepted_application_id=offer_applications.offer_application_id";
			try{
				String visibility_class_name = "";
				
				String apply_visibility_class = "no";
				String accept_visibility_class = "no";
				String is_offer_complete = "no";
				
				PreparedStatement st = con.prepareStatement(sql);
				st.setInt(1,Integer.parseInt(id));
				//String tb = "service_offer";
				//st.setString(2,Offer.TableType.service_offer.toString());
				ResultSet rs = st.executeQuery();
				if(!rs.next()){
					outerJsonObject.put("status","NO");
					outerJsonObject.put("message","Offer does not exist or it has been removed");
					out.println(outerJsonObject);
					out.close();
				}else{
					do{
						JSONObject obj = new JSONObject(); 
						obj.put("s_offer_id",rs.getString(1));
						obj.put("s_entity_id",rs.getString(2));
						obj.put("s_offer_name",rs.getString(3));
						obj.put("s_offer_country",rs.getString(4));
						obj.put("s_offer_state",rs.getString(5));
						obj.put("s_offer_residence",rs.getString(6));
						obj.put("s_working_hours",rs.getString(7));
						obj.put("s_starting_date",rs.getString(8)); 
						obj.put("s_offer_details",rs.getString(9));
						obj.put("s_names",rs.getString(10) + " " + rs.getString(11));
						obj.put("s_profile_pic_path",rs.getString(12));
						
						if(entity_id.equals(rs.getString(2))){
							visibility_class_name = "hidden";
							//visibility_class_name = "no";
						}else{
							visibility_class_name = "no";
						}
						
						if(rs.getString(14).equals("accepted")){
							 apply_visibility_class = "hidden";
							 accept_visibility_class = "hidden";
							 //obj.put("accepted_entity",rs.getString(16));
						}
						
						if(rs.getString(15).equalsIgnoreCase("t")){
							is_offer_complete = "yes";
						}
						
						
						obj.put("is_offer_complete",is_offer_complete);
						obj.put("apply_visibility_class",apply_visibility_class);
						obj.put("accept_visibility_class",accept_visibility_class);
						obj.put("visibility_class_name",visibility_class_name);
						obj.put("time_date_string",new Offer(entity_id).getFormattedTimeStamp("service_offer","s_offer_date",Integer.parseInt(rs.getString(1)),"s_offer_id"));
						obj.put("applications", new Offer(entity_id).fetchOfferApplications(rs.getString(1),table_name,rs.getString(2)));
						obj.put("offer_type","service");
						
						
						
						
						
						outerJsonArray.add(obj);
					}while(rs.next());
					outerJsonObject.put("offers",outerJsonArray);
					outerJsonObject.put("status","OK");
					outerJsonObject.put("message","Service offer found");
					out.println(outerJsonObject);
					out.close();
				}
			}catch(SQLException sqle){
				//System.out.print("SQLException error" + sqle.getMessage().toString());
				log.severe("Error is here: " + HarambesaUtils.getStackTrace(sqle));
			}catch(Exception ex){
				// System.out.print("Blanket exception error" + ex.getMessage().toString());
				log.severe("Error is here: " + HarambesaUtils.getStackTrace(ex));
			}finally{
				db.closeDB();
			}
		}else{
			JSONObject jException = new JSONObject();	
			jException.put("status","NO");
			jException.put("redir","../login");
			jException.put("message","Session expired");
			out.println(jException);
			out.close();
		}
	}
	
	public void fetchMaterialOfferPosts(String id,String table_name, String entity_id){
		if(isSessionActive()){
			DBConnection db = new DBConnection();
			Connection con = db._getConnection();
			JSONArray outerJsonArray = new JSONArray();
			JSONObject outerJsonObject = new JSONObject();
			String sql = "SELECT" +
				" material_offer.m_offer_id,material_offer.m_entity_id," +
				" material_offer.m_offer_category,material_offer.m_offer_name," +
				" material_offer.m_offer_units,material_offer.m_offer_country," +
				" material_offer.m_offer_state,material_offer.m_offer_residence," + 
				" material_offer.m_offer_main_photo,material_offer.m_offer_other_photo_1," +
				" material_offer.m_offer_other_photo_2,material_offer.m_offer_details," +
				" entitys.first_name,entitys.last_name," +
				" entitys.profile_pic_path," + //15
				" material_offer.accepted_application_id,material_offer.status," + //16 17
				" material_offer.approved" + //18 19
				" FROM material_offer,entitys" +
				" WHERE material_offer.m_offer_id=?" +
				//" AND offer_applications.table_name=?::offer_table" +
				" AND material_offer.m_entity_id=entitys.entity_id";
				//" AND material_offer.accepted_application_id=offer_applications.offer_application_id";
				
			try{
				String visibility_class_name = "";
				
				String apply_visibility_class = "no";
				String accept_visibility_class = "no";
				String is_offer_complete = "no";
				
				PreparedStatement st = con.prepareStatement(sql);
				st.setInt(1,Integer.parseInt(id));
				//st.setString(2,Offer.TableType.material_offer.toString());
				
				//st.setString(2,"material_offer");
				
				ResultSet rs = st.executeQuery();
				if(!rs.next()){
					outerJsonObject.put("status","NO");
					outerJsonObject.put("message","Offer does not exist or it has been removed");
					out.println(outerJsonObject);
					out.close();
				}else{
					do{
						JSONObject obj = new JSONObject();
						
						obj.put("m_offer_id",rs.getString(1));
						obj.put("m_entity_id",rs.getString(2));
						obj.put("m_offer_category",rs.getString(3));
						obj.put("m_offer_name",rs.getString(4));
						obj.put("m_offer_units",rs.getString(5));
						obj.put("m_offer_country",rs.getString(6));
						obj.put("m_offer_state",rs.getString(7));
						obj.put("m_offer_residence",rs.getString(8)); 
						obj.put("m_offer_main_photo","../materialofferphotos/" +rs.getString(9)); 
						obj.put("m_offer_other_photo_1","../materialofferphotos/"+rs.getString(10)); 
						obj.put("m_offer_other_photo_2","../materialofferphotos/"+rs.getString(11)); 
						obj.put("m_offer_details",rs.getString(12));
						obj.put("m_names",rs.getString(13) + " " + rs.getString(14));
						obj.put("m_profile_pic_path",rs.getString(15));
						
						if(entity_id.equals(rs.getString(2))){
							visibility_class_name = "hidden";
						}else{
							visibility_class_name = "no";
						}
						
						if(rs.getString(17).equals("accepted")){
							 apply_visibility_class = "hidden";
							 accept_visibility_class = "hidden";
							 //obj.put("accepted_entity",rs.getString(19));
						}
						log.info("approved is " + rs.getString(18));
						if(rs.getString(18).equalsIgnoreCase("t")){
							is_offer_complete = "yes";
						}
						
						obj.put("is_offer_complete",is_offer_complete);
						obj.put("apply_visibility_class",apply_visibility_class);
						obj.put("accept_visibility_class",accept_visibility_class);
						
						obj.put("visibility_class_name",visibility_class_name);
						obj.put("time_date_string",new Offer(entity_id).getFormattedTimeStamp("material_offer","m_offer_date",Integer.parseInt(rs.getString(1)),"m_offer_id"));
						obj.put("applications",new Offer(entity_id).fetchOfferApplications(rs.getString(1),table_name,rs.getString(2)));
						obj.put("offer_type","material");
						outerJsonArray.add(obj);
						
					}while(rs.next());
					outerJsonObject.put("offers",outerJsonArray);
					outerJsonObject.put("status","OK");
					outerJsonObject.put("message","Service offer found");
					out.println(outerJsonObject);
					out.close();
				}
			}catch(SQLException sqle){
				log.severe("Error is here: " + HarambesaUtils.getStackTrace(sqle));
			}catch(Exception ex){
				log.severe("Error is here: " + HarambesaUtils.getStackTrace(ex));
			}finally{
				db.closeDB();
			}
		}else{
			JSONObject jException = new JSONObject();	
			jException.put("status","NO");
			jException.put("redir","../login");
			jException.put("message","Session expired");
			out.println(jException);
			out.close();
		}
	}
	
	public String getTableName(String tag){
		String table_name = "no table";
		if(tag.equals("s_offer")){
			 table_name = "service_offer";
		}else if(tag.equals("m_offer")){
			table_name = "material_offer";
		}else if(tag.equals("offer")){
			table_name = "offers";
		}
		return table_name;
	}
	
	
	
}