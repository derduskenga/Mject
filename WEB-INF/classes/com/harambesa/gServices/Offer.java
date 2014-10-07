package com.harambesa.gServices;

import java.sql.*;
import com.harambesa.gServices.HarambesaUtils;
import com.harambesa.DBConnection.DBConnection;
import com.harambesa.DBConnection.GlobalDresser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;

import java.io.*;
public class Offer{
	//fields
	String entity_id;
	HttpSession session;
	Logger log= null;
	public Offer(String entity_id){
		this.entity_id=entity_id;
		//this.session = request.getSession();
		if(log==null){
			log = Logger.getLogger(Offer.class.getName());
		}

	}
	
	public JSONObject savefferPost(String offered_amount,String offered_currency,String offered_category,String offer_details,String offer_summary){
		DBConnection db = new DBConnection();
		Connection con = db._getConnection();
		JSONObject obj = new JSONObject();
		String query = "INSERT INTO offers (entity_id,programme_id,currency_id,offer_amount,offer_summary,offer_details)" +
				" VALUES(?,?,?,?,?,?)" +
				" RETURNING offer_id";
		try{
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, Integer.parseInt(entity_id));
			preparedStatement.setInt(2, Integer.parseInt(offered_category));
			preparedStatement.setInt(3, Integer.parseInt(offered_currency));
			preparedStatement.setInt(4, Integer.parseInt(offered_amount));
			preparedStatement.setString(5, offer_summary);
			preparedStatement.setString(6, offer_details);
			ResultSet rs= preparedStatement.executeQuery();
			
			if(rs != null){
				if(rs.next());
				obj.put("success",1);
				obj.put("message","Offer has been registered successifully and will be seen by applicants.");
				obj.put("offer_id",rs.getInt(1));
				obj.put("time_details",getFormattedTimeStamp("offers","offer_date",rs.getInt(1),"offer_id"));
			}else{
				obj.put("success",0);
				obj.put("message","An error ocured ");
			
			}
		}catch(Exception ex){
			//log.severe(getStackTrace(ex));
			System.out.print(ex.getMessage().toString());
			obj.put("success", 0);
			obj.put("message", "Offer failed. Something went wrong.");
			obj.put("error",ex.getMessage().toString() );
		}finally{
			db.closeDB();
		}		
		return obj;
	}
	
	public String getFormattedTimeStamp(String table_name, String column_name, int record_id, String index_column_name){
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
	
	public JSONObject pullAllOffers(){
		DBConnection db = new DBConnection();
		String sql = "SELECT offer_tracker_id,offer_table_id,tracked_offer_table_type" +
			      " FROM offer_tracker" +
			      " WHERE is_complete='FALSE'" +
			      " ORDER BY offer_tracker_id ASC";
		
		ResultSet rs = db.readQuery(sql);
		JSONArray outerArr = new JSONArray();
		JSONObject outerObj = new JSONObject();
		try{
			if(!rs.next()){
				//do nothing
			}else{
				do{
					String offer_tracker_id = rs.getString(1);
					String offer_id = rs.getString(2);
					String table_name = rs.getString(3);
					JSONObject fragmentObj = new JSONObject();
					//each time a fragment runs the new JSONObject is appended to the to the already created Object.
					if(table_name.equals("offers")){
						  fragmentObj = fetchOfferPosts(offer_id,table_name);
					}else if(table_name.equals("service_offer")){
						  fragmentObj = fetchServiceOfferPosts(offer_id,table_name);
					}else if(table_name.equals("material_offer")){
						  fragmentObj = fetchMaterialOfferPosts(offer_id,table_name);
					}
					outerArr.add(fragmentObj);
					log.info("logging loop");
				}while(rs.next());
				
				outerObj.put("offers",outerArr);
				outerObj.put("status","OK");
				outerObj.put("message","Load more offers...");
			}
				
		}catch(SQLException sqle){
			log.info("Error is here: " + HarambesaUtils.getStackTrace(sqle));
		}catch(Exception ex){
			log.severe("Error is here: " + HarambesaUtils.getStackTrace(ex));
		}finally{
			db.closeDB();
		}
		
		return outerObj;
	}
	
	public JSONObject fetchOfferPosts(String id,String table_name){
		DBConnection db = new DBConnection();
		JSONArray outerJsonArray = new JSONArray();
		JSONObject outerJsonObject = new JSONObject();
		String sql = "SELECT" +
				" offers.offer_id,offers.entity_id,offers.programme_id," +
				" offers.offer_date,offers.currency_id,offers.offer_amount," + 
				" offers.offer_summary,offers.offer_details," + 
				" entitys.entity_id,entitys.first_name," +
				" entitys.last_name,entitys.profile_pic_path, programmes.programme_id," + 
				" programmes.programme_name,currency.currency_id,"+ 
				" currency.currency_name" + 
				" FROM offers,entitys,currency,programmes" +
				" WHERE offers.offer_id='" + id + "'" +
				" AND offers.entity_id=entitys.entity_id" +
				" AND offers.programme_id=programmes.programme_id" +
				" AND offers.currency_id=currency.currency_id";
		 //log.info("Query looged here: " + sql);
		ResultSet rs = db.readQuery(sql);
		JSONObject obj = new JSONObject();
		try{
			String visibility_class_name = "";
			
			if(!rs.next()){
				//do nothing
			}else{
				do{
					//log.info("Catching something here offer_id: " + rs.getString(1));
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
					
					obj.put("visibility_class_name",visibility_class_name);
					obj.put("time_date_string",getFormattedTimeStamp("offers","offer_date",Integer.parseInt(rs.getString(1)),"offer_id"));
					obj.put("applications",fetchOfferApplications(rs.getString(1),table_name,rs.getString(2)));
					obj.put("offer_type","money");
					//outerJsonArray.add(obj);
					
				}while(rs.next());
				
			}
		}catch(SQLException sqle){
			  log.severe("Error is here: " + HarambesaUtils.getStackTrace(sqle));
		}catch(Exception ex){
			  log.severe("Error is here: " + HarambesaUtils.getStackTrace(ex));
		}finally{
			db.closeDB();
		}
		return obj;
	}
	
	public JSONObject fetchServiceOfferPosts(String id,String table_name){
		DBConnection db = new DBConnection();
		JSONArray outerJsonArray = new JSONArray();
		JSONObject outerJsonObject = new JSONObject();
		String sql = "SELECT"+
			      " service_offer.s_offer_id,service_offer.s_entity_id," +
			      " service_offer.s_offer_name,service_offer.s_offer_country," +
			      " service_offer.s_offer_state,service_offer.s_offer_residence," +
			      " service_offer.s_working_hours,service_offer.s_starting_date,"+  
			      " service_offer.s_offer_details,entitys.first_name," +
			      " entitys.last_name,entitys.profile_pic_path" +
			      " FROM service_offer,entitys" +
			      " WHERE service_offer.s_offer_id='" + id + "'" +
			      " AND service_offer.s_entity_id=entitys.entity_id";
		
		ResultSet rs = db.readQuery(sql);
		JSONObject obj = new JSONObject(); 
		try{
			String visibility_class_name = "";
			
			if(!rs.next()){
				//do nothing
			}else{
				do{
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
					obj.put("visibility_class_name",visibility_class_name);
					obj.put("time_date_string",getFormattedTimeStamp("service_offer","s_offer_date",Integer.parseInt(rs.getString(1)),"s_offer_id"));
					obj.put("applications",fetchOfferApplications(rs.getString(1),table_name,rs.getString(2)));
					obj.put("offer_type","service");
					
				}while(rs.next());
			
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
		return obj;
	}

	public JSONObject fetchMaterialOfferPosts(String id,String table_name){
		DBConnection db = new DBConnection();
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
			      " entitys.profile_pic_path" + 
			      " FROM material_offer,entitys" +
			      " WHERE material_offer.m_offer_id='" + id + "'" +
			      " AND material_offer.m_entity_id=entitys.entity_id";
		ResultSet rs = db.readQuery(sql);
		JSONObject obj = new JSONObject();
		try{
			String visibility_class_name = "";
			
			if(!rs.next()){
				//do nothing
			}else{
				do{
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
						//visibility_class_name = "no";
					}else{
						visibility_class_name = "no";
					}
					obj.put("visibility_class_name",visibility_class_name);
					obj.put("time_date_string",getFormattedTimeStamp("material_offer","m_offer_date",Integer.parseInt(rs.getString(1)),"m_offer_id"));
					obj.put("applications",fetchOfferApplications(rs.getString(1),table_name,rs.getString(2)));
					obj.put("offer_type","material");
					
				}while(rs.next());
			}
		}catch(SQLException sqle){
			 log.severe("Error is here: " + HarambesaUtils.getStackTrace(sqle));
		}catch(Exception ex){
			 log.severe("Error is here: " + HarambesaUtils.getStackTrace(ex));
		}finally{
			db.closeDB();
		}
		return obj;
	}
	
	public JSONArray fetchOfferApplications(String offer_id,String table_name, String offer_owner_entity_id){
		DBConnection db = new DBConnection();
		JSONArray jArray = new JSONArray();
		String sql = "SELECT" +
			      " offer_applications.offer_application_id,offer_applications.entity_id," + 
			      " offer_applications.offer_id,offer_applications.application_details," +
			      " entitys.first_name,entitys.last_name, entitys.profile_pic_path" + 
			      " FROM offer_applications" +
			      " RIGHT JOIN entitys" +
			      " ON offer_applications.entity_id=entitys.entity_id" + 
			      " WHERE offer_applications.table_name='" + table_name + "'" +
			      " AND offer_applications.offer_id='" + offer_id + "'" +
			      " ORDER BY offer_applications.offer_application_id ASC";
		
		ResultSet rs = db.readQuery(sql);
		//log.info("Query looged here: " + sql);
		
		try{
			String accept_link_visibility = "hidden";
			if(!rs.next()){
				//no data
			}else{
				do{
					JSONObject jObjectApp = new JSONObject();
					//log.info("catching something inside loop offer_application_id: " + rs.getString(1));
					jObjectApp.put("offer_application_id",rs.getString(1));
					jObjectApp.put("offer_application_entity_id",rs.getString(2));
					jObjectApp.put("offer_application_details",rs.getString(4));
					jObjectApp.put("names",rs.getString(5) + " " + rs.getString(6));
					jObjectApp.put("profile_pic_path",rs.getString(7));
					
					if(entity_id.equals(offer_owner_entity_id)){
						accept_link_visibility = "";
						//log(entity_id)
					}
					
					jObjectApp.put("visibility_class_name",accept_link_visibility);
					jObjectApp.put("offer_application_date",getFormattedTimeStamp("offer_applications","offer_application_date",Integer.parseInt(rs.getString(1)),"offer_application_id"));
					jArray.add(jObjectApp);	
					
				}while(rs.next());
			}
		}catch(SQLException sqle){
			System.out.println(sqle.getMessage().toString());
		}catch(Exception ex){
			System.out.println(ex.getMessage().toString());
		}finally{
			db.closeDB();
		}
		
		return jArray;
	}
	
	public JSONObject saveOfferApplication(String offer_id,String application_text, String table_name){
		DBConnection db = new DBConnection();
		Connection con = db._getConnection();
		JSONObject obj = new JSONObject();
		String sql = "INSERT INTO offer_applications (entity_id,offer_id,application_details,table_name)"+
			      " VALUES(?,?,?,?::offer_table)"+
			      " RETURNING offer_application_id";
		try{
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, Integer.parseInt(entity_id));
			st.setInt(2, Integer.parseInt(offer_id));
			st.setString(3,application_text);
			if(table_name.equals("offers")){
				st.setString(4,TableType.offers.toString());
			}else if(table_name.equals("service_offer")){
				st.setString(4,TableType.service_offer.toString());
			}else if(table_name.equals("material_offer")){
				st.setString(4,TableType.material_offer.toString());
			}
			ResultSet rs= st.executeQuery();
			if(rs != null){
				  if(rs.next());
				  obj.put("success",1);
				  obj.put("message","Your application has been saved and will be visible to the owner");
				  obj.put("offer_application_id",rs.getInt(1));
				  obj.put("time_details",getFormattedTimeStamp("offer_applications","offer_application_date",rs.getInt(1),"offer_application_id"));
				  
			}else{
				obj.put("success",0);
				obj.put("message","An error ocured"); 
			}
		}catch(Exception ex){
			System.out.print(ex.getMessage().toString());
			obj.put("success", 0);
			obj.put("message", "Offer application failed. Something went wrong.");
			obj.put("error",ex.getMessage().toString() );  
		}finally{
			  db.closeDB();
		}		
		return obj;  
	}
	
	public JSONObject saveApplicationAcceptance(String offer_id,String application_id,String offer_owner_entity_id,String applicant_entity_id,String table_name){
		DBConnection db = new DBConnection();
		Connection con = db._getConnection();
		JSONObject obj = new JSONObject();
		String table_col = null;
		
		if(table_name.equals("service_offer")){
			table_col = "s_offer_id";
		}else if(table_name.equals("material_offer")){
			table_col = "m_offer_id";
		}
		//update table of table_name set field accepted_entity_id to applicant_entity_id
		
		String sql = "UPDATE " + table_name + " SET accepted_application_id=?, status=?::service_material_status WHERE " + table_col + "=?";
		
		try{
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1,Integer.parseInt(application_id));
			st.setString(2,OfferStatus.accepted.toString());
			st.setInt(3,Integer.parseInt(offer_id));
			int rs = st.executeUpdate();
			
			if(rs > 0){
				obj.put("success",1);
				if(table_name.equals("service_offer")){
					obj.put("message","You have accepted this application. After delivering this service, the applicant will approve and you gain socila points");
				}else if(table_name.equals("material_offer")){
					obj.put("message","You have accepted this application. After delivering the materials you offered, the applicant will approve and you will ge social points");
				}
				
				
			}else{
				obj.put("success",0);
				obj.put("","An error occured. Please try again! Try again");
			}
			
		
		}catch(SQLException sqle){
			log.info("Checking sql error in saving offer acceptance; service and material: " + HarambesaUtils.getStackTrace(sqle));
			obj.put("success", 0);
			obj.put("message", "Error occured while accepting the application! Try again");
			obj.put("error",HarambesaUtils.getStackTrace(sqle));  
		}catch(Exception ex){
			log.info("Checking Blanket error in saving offer acceptance; service and material: " + HarambesaUtils.getStackTrace(ex));
			obj.put("success", 0);
			obj.put("message", "Error occured while accepting the application! Try again");
			obj.put("error",HarambesaUtils.getStackTrace(ex));  
		}finally{
			db.closeDB();
		}
		
		return obj;
	}
	
	public JSONArray getCountrys(){        
		String sql = "SELECT sys_country_id, sys_country_name FROM countrys";
		DBConnection db= new DBConnection();
		ResultSet rs = db.readQuery(sql);
		JSONArray jArray = new JSONArray();
		try{
			while(rs.next()){            
				JSONObject obj=new JSONObject();
				obj.put("id", rs.getString(1));
				obj.put("name", rs.getString(2));
				jArray.add(obj);
			}			
		}catch(SQLException ex){
			//log.severe(getStackTrace(ex));
			System.out.print(ex.getMessage().toString());
		}finally{
			db.closeDB();
		}
		return jArray;
	}	
	public JSONArray getServices(){        
		String sql = "SELECT service_id, service_name FROM services";
		DBConnection db= new DBConnection();
		ResultSet rs = db.readQuery(sql);
		JSONArray jArray = new JSONArray();
		try{
			while(rs.next()){            
				JSONObject obj=new JSONObject();
				obj.put("id", rs.getString(1));
				obj.put("name", rs.getString(2));
				jArray.add(obj);
			}			
		}catch(SQLException ex){
			//log.severe(getStackTrace(ex));
			System.out.print(ex.getMessage().toString());
		}finally{
			db.closeDB();
		}
		return jArray;
	}	
	public JSONArray getItems(){        
		String sql = "SELECT item_id, item_name FROM items";
		DBConnection db= new DBConnection();
		ResultSet rs = db.readQuery(sql);
		JSONArray jArray = new JSONArray();
		try{
			while(rs.next()){            
				JSONObject obj=new JSONObject();
				obj.put("id", rs.getString(1));
				obj.put("name", rs.getString(2));
				jArray.add(obj);
			}			
		}catch(SQLException ex){
			//log.severe(getStackTrace(ex));
			System.out.print(ex.getMessage().toString());
		}finally{
			db.closeDB();
		}
		return jArray;
	}
	
	public JSONObject saveServiceOffer(String service_name,String service_countries,String state,String residence,String hours_a_day,String start_date,String service_offer_details){
		DBConnection db = new DBConnection();
		Connection con = db._getConnection();
		JSONObject obj = new JSONObject();
		String sql = "INSERT INTO service_offer (s_entity_id,s_offer_name,s_offer_country,s_offer_state,s_offer_residence,s_working_hours,s_starting_date,s_offer_details)" +
			      " VALUES(?,?,?,?,?,?,?,?)"+
			      " RETURNING s_offer_id";
		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1,Integer.parseInt(entity_id));
			ps.setString(2,service_name);
			ps.setString(3,service_countries);
			ps.setString(4,state);
			ps.setString(5,residence);
			ps.setInt(6,Integer.parseInt(hours_a_day));
			ps.setDate(7,fetchDate(formatDate(start_date)));
			ps.setString(8,service_offer_details);
			ResultSet rs = ps.executeQuery();
			
			if(rs != null){
				if(rs.next());
				obj.put("success",1);
				obj.put("message","Offer has been registered successifully and will be seen by applicants.");
				obj.put("offer_id",rs.getInt(1));
				obj.put("time_details",getFormattedTimeStamp("service_offer","s_offer_date",rs.getInt(1),"s_offer_id"));
				obj.put("entity_id",entity_id);
			}else{
				obj.put("success",0);
				obj.put("message","An error ocured ");
			
			}
		}catch(Exception ex){
			log.severe(HarambesaUtils.getStackTrace(ex));
			System.out.println(ex.getMessage().toString());
			obj.put("success", 0);
			obj.put("message", "Offer failed. Something went wrong.");
			obj.put("error",ex.getMessage().toString() );
		}finally{
			db.closeDB();
		}		
		return obj;
	}
	
	public JSONObject saveAcceptedMoneyApplication(String offer_id,String application_id,String offer_owner_entity_id,String applicant_entity_id,String offer_type,String table_name){
		
		String el [] = getMoneyOfferElements(offer_id,entity_id);
		if(el == null){
			log.info("el is null");
		}else{
			log.info("The size of el is " + el.length + " at 3 is " + el[2] + "at 1 is " + el[0]);
		}
		String amount = el[2];
		String currency_id = el[1];
		String currency_symbol = el[5];
		String currency_name = el[3];
		
		double offer_amount = Double.parseDouble(amount);
		double walletBalance = new GlobalDresser(entity_id).getHarambesaBalance();
		
		JSONObject json = new JSONObject();
		
		if(walletBalance<offer_amount){
			json.put("success",0);
			json.put("message","You do not have enough funds in your account to accept this offer. Navigate back to use alternative source");
		}else{
			//There is enough funds to accept offer. Write a transaction to register the process.
			//1. Register the accepted application id
			//2. Record offer acceptance as donation_receive and donation_receive
			//3. Trigger to offer applications to update offer tracker to 'TRUE';
			DBConnection db = new DBConnection();
			Connection con = db._getConnection();
			//accepted id=application_id
			String sqlUpdateApplications = "UPDATE offers SET accepted_application_id=? WHERE offer_id=?";
			
			String sqlRecord= "INSERT INTO transactions(t_type,t_amount,entity_id,currency,transaction_root) VALUES(?::operations,?,?,?,?)";
			
			PreparedStatement stSqlUpdateApplications = null;
			PreparedStatement stSqlRecord = null;
			
			try{  
				   con.setAutoCommit(false);
				   stSqlUpdateApplications = con.prepareStatement(sqlUpdateApplications);
				   stSqlRecord = con.prepareStatement(sqlRecord);
				   
				   //set the values for update
				   stSqlUpdateApplications.setInt(1,Integer.parseInt(application_id));
				   stSqlUpdateApplications.setInt(2,Integer.parseInt(offer_id));
				   //execute the update
				   stSqlUpdateApplications.executeUpdate();
				   
				   //set values for on insert then to a batch
				   stSqlRecord.setString(1,Operations.donation.toString());
				   stSqlRecord.setDouble(2,Double.parseDouble(amount));
				   stSqlRecord.setInt(3,Integer.parseInt(offer_owner_entity_id));
				   stSqlRecord.setString(4,currency_name);
				   stSqlRecord.setString(5,"Offers");
				   
				   stSqlRecord.executeUpdate();
				   
				   stSqlRecord.setString(1,Operations.donation_receive.toString());
				   stSqlRecord.setDouble(2,Double.parseDouble(amount));
				   stSqlRecord.setInt(3,Integer.parseInt(applicant_entity_id));
				   stSqlRecord.setString(4,currency_name);
				   stSqlRecord.setString(5,"Offers");
				   
				   //stSqlRecord.addBatch();
				  //execute the batch
// 				   stSqlRecord.executeBatch(); 
				   stSqlRecord.executeUpdate();
 				   //commit queries to effect changes   
				   con.commit();
				   
				   json.put("success",1);
				   json.put("message","You have accepted the application. Thank you for offering on Harambesa platform");
				   
			}catch(SQLException sqle){
				log.severe("Error on execution of batch on acceptance sqle ex. error" + HarambesaUtils.getStackTrace(sqle));
				try{
					con.rollback();
				}catch(SQLException sq){
					 log.severe("Error on execution: child SQLException sqle" + HarambesaUtils.getStackTrace(sq));
				}catch(Exception e){
					 log.severe("Error on execution: child Exception ex" + HarambesaUtils.getStackTrace(e));
				
				}     
				json.put("success",0);
				json.put("message","An error occured. Please try again");
			}catch(Exception ex){
				  log.severe("Error on execution of batch on acceptance Blanket ex. error" + HarambesaUtils.getStackTrace(ex));
				  json.put("success",0);
				  json.put("message","An error occured. Please try again");
			}finally{
				db.closeDB();
				
				try{
					if (stSqlRecord != null) {
						stSqlRecord.close();
					}
					if (stSqlUpdateApplications != null) {
						stSqlUpdateApplications.close();
					}
				}catch(SQLException sq){
					 log.severe("Transaction connection failed to close" + HarambesaUtils.getStackTrace(sq));
				}
			}
			
		}
		return json;
		
	}
	
	public String[] getMoneyOfferElements(String offer_id,String entity_id){
		DBConnection db = new DBConnection();
		String elemArray[] = new String[6];
		String sql = "SELECT" +
			      " offers.offer_id," +
			      " offers.currency_id,offers.offer_amount," +
			      " currency.currency_name,offers.entity_id," +
			      " currency.currency_symbol" +
			      " FROM offers" +
			      " RIGHT JOIN currency" +
			      " ON offers.currency_id=currency.currency_id" +
			      " WHERE offers.offer_id='" + offer_id + "'" +
			      " AND offers.entity_id='" + entity_id + "'";
		//log.info(sql);
		try{
			ResultSet rs = db.readQuery(sql); 
		
			if(!rs.next()){
				//do nothing
			}else{
				
				elemArray[0] = rs.getString(1);//offer_id
				elemArray[1] = rs.getString(2);//currency_id
				elemArray[2] = rs.getString(3);//offer_amount
				elemArray[3] = rs.getString(4);//currency_name
				elemArray[4] = rs.getString(5);//entity_id
				elemArray[5] = rs.getString(6);//currency_symbol
				
				//log.info(elemArray[0] + " " + elemArray[1] + " " + elemArray[2] + " " + elemArray[3]);
				

			}
		}catch(SQLException sqle){
			log.severe("Error is here: sqle " + HarambesaUtils.getStackTrace(sqle));
		}catch(Exception ex){
			log.severe("Error is here: Blanket ex " + HarambesaUtils.getStackTrace(ex));
		}finally{
			db.closeDB();
		}
		return elemArray;
	}
	
	public java.sql.Date fetchDate(String theDate){
		  java.sql.Date date_ = java.sql.Date.valueOf(theDate);
		  return date_;
	}
	
	private String formatDate(String date){
		String[] parts = date.split("/");
		log.info(parts[2]+"/"+parts[1]+"/"+parts[0]);
		return parts[2]+"-"+parts[1]+"-"+parts[0];
	}	
	/*
	Java inner class
	*/
	public enum TableType{
		offers,
		service_offer,
		material_offer;
	}
	/*
	Java inner class
	*/
	public enum OfferStatus{
		pending,
		accepted,
		completed;
	}
	
	public enum Operations{
		withdraw,
		deposit,
		donation,
		purchase_points,
		donation_receive,
		sale_points;
	}

}