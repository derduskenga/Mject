package com.harambesa.notification;

import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import com.harambesa.gServices.HarambesaUtils;
import com.harambesa.DBConnection.DBConnection;
import com.harambesa.Utility.Utilities;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import com.harambesa.gServices.Offer;

public class Notifications{
	Logger log= null;
	String entity_id;
	boolean hasConnections;
	String[]arr;
	public Notifications(String entity_id){
		this.entity_id = entity_id;
		if(log==null){
			log = Logger.getLogger(Notifications.class.getName());
		}
	} 	
	public boolean saveNotification(String url, String msg, String originator, String []recipients){
		boolean flag = false;
		
		if(recipients != null || recipients.length != 0){
			log.info("array not null");
			DBConnection db = new DBConnection();
			Connection con = db._getConnection();
			String sql = "INSERT" +
					" INTO notifications(originator_entity_id,notification_message,notification_url,notification_recipients)" +
					" VALUES(?,?,?,?)" +
					" RETURNING notification_id";
			
			try{
				Array aArray = con.createArrayOf("varchar", recipients);
				PreparedStatement st = con.prepareStatement(sql);
				
				st.setInt(1, Integer.parseInt(originator));
				st.setString(2, msg);
				st.setString(3, url);
				st.setArray(4,aArray);
				
				ResultSet rs= st.executeQuery();
				
				if(rs != null){
					if(rs.next());
						flag = true;
						log.info("Record was inserted");
				}else{
					log.severe("Error is here");
				}
			}catch(SQLException sqle){
				log.severe("Error is here: SQLException " + HarambesaUtils.getStackTrace(sqle));
			}catch(Exception ex){
				log.severe("Error is here: Exception " + HarambesaUtils.getStackTrace(ex));
			}finally{
				db.closeDB();
			}
		}else{
			log.info("array null or empty");
		}		
		return flag;
	}
	
	public String[] fetchNotificationRecipients(){
		DBConnection db = new DBConnection();
		Connection con = db._getConnection();
		List<String> connections_array_list = new ArrayList<String>();
		String [] returnedArray = null;
		String sql = "SELECT" +
				" requestor_entity_id,requestee_entity_id" +
				" FROM connections" +
				" WHERE requestor_entity_id=?"+
				" OR requestee_entity_id=?" +
				" AND is_accepted='TRUE'"+
				" AND Connection_is_blocked='FALSE'";		
		try{
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1,Integer.parseInt(entity_id));
			st.setInt(2,Integer.parseInt(entity_id));
			//ResultSet rs = db.readQuery(sql);
			ResultSet rs = st.executeQuery();
			if(!rs.next()){
				
			}else{
				do{
					log.info("entity_id is  is " + entity_id);
					
					if(rs.getString(1).equals(entity_id)){
						connections_array_list.add(rs.getString(2));
 					}else{
 						connections_array_list.add(rs.getString(1));
 					}
				}while(rs.next());
				
				String [] list_to_array = new String[connections_array_list.size()];
				returnedArray = connections_array_list.toArray(list_to_array);
			}
		}catch(SQLException sqle){
			log.severe("Error: " + HarambesaUtils.getStackTrace(sqle));
		}catch(Exception ex){
			log.severe("Error: " + HarambesaUtils.getStackTrace(ex));
		}finally{
			db.closeDB();
		}
		return returnedArray;
	}
	
	public String[] fetchDonationRequestOwner(String donationRequestId){
		DBConnection db = new DBConnection();
		Connection con = db._getConnection();
		String [] returnedArray = null;
		String column = null;
		List<String> connections_array_list = new ArrayList<String>();
		
		String sql = "SELECT"+
				" entity_id" +
				" FROM donation_requests" +
				" WHERE donation_request_id=?";
		try{
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1,Integer.parseInt(donationRequestId));
			
			ResultSet rs = st.executeQuery();
			if(!rs.next()){
				
			}else{
				do{
 						connections_array_list.add(rs.getString(1));
				}while(rs.next());
				
				String [] list_to_array = new String[connections_array_list.size()];
				returnedArray = connections_array_list.toArray(list_to_array);
			}
		}catch(SQLException sqle){
			log.severe("Error: " + HarambesaUtils.getStackTrace(sqle));
		}catch(Exception ex){
			log.severe("Error: " + HarambesaUtils.getStackTrace(ex));
		}finally{
			db.closeDB();
		}
		return returnedArray;
	}
	public String[] fetchOfferOwnerAsRecipient(String offer_id, String table_name){
		DBConnection db = new DBConnection();
		Connection con = db._getConnection();
		String [] returnedArray = null;
		String column = null;
		List<String> connections_array_list = new ArrayList<String>();
		if(table_name.equals("offers")){
			column = "offer_id";
		}else if(table_name.equals("material_offer")){
			column = "m_offer_id";
		}else if(table_name.equals("service_offer")){
			column = "s_offer_id";
		}
		
		String sql = "SELECT" +
			" entity_id" +
			" FROM " + table_name + "" +
			" WHERE " + column + "=?";
		try{
			PreparedStatement st = con.prepareStatement(sql);
			

			st.setInt(1,Integer.parseInt(offer_id));
			
			ResultSet rs =  st.executeQuery();
			
			if(rs.next()){
				do{
					connections_array_list.add(rs.getString(1));
				}while(rs.next());
				
				String [] list_to_array = new String[connections_array_list.size()];
				returnedArray = connections_array_list.toArray(list_to_array);
			}
		}catch(SQLException sqle){
			log.severe("Error: " + HarambesaUtils.getStackTrace(sqle));
		}catch(Exception ex){
			log.severe("Error: " + HarambesaUtils.getStackTrace(ex));
		}finally{
			db.closeDB();
		}
		return returnedArray; 
	}
	
	public String [] fetchOfferApplicants(String table_name, String offer_id){
		DBConnection db = new DBConnection();
		Connection con = db._getConnection();
		String [] returnedArray = null;
		String column = null;
		List<String> connections_array_list = new ArrayList<String>();
		
		String sql = "SELECT DISTINCT" +
			" entity_id" +
			" FROM offer_applications" +
			" WHERE table_name=?::offer_table" +
			" AND offer_id=?";
		try{
			PreparedStatement st = con.prepareStatement(sql);			
			if(table_name.equals("offers")){
				st.setString(1,Offer.TableType.offers.toString());
			}else if(table_name.equals("service_offer")){
				st.setString(1,Offer.TableType.service_offer.toString());
			}else if(table_name.equals("material_offer")){
				st.setString(1,Offer.TableType.material_offer.toString());
			}
			
			st.setString(1,table_name);
			
			st.setInt(2,Integer.parseInt(offer_id));
			
			ResultSet rs =  st.executeQuery();
			if(rs.next()){
				do{
					connections_array_list.add(rs.getString(1));
				}while(rs.next());
				
				String [] list_to_array = new String[connections_array_list.size()];
				returnedArray = connections_array_list.toArray(list_to_array);
			}
		}catch(SQLException sqle){
			log.severe("Error: " + HarambesaUtils.getStackTrace(sqle));
		}catch(Exception ex){
			log.severe("Error: " + HarambesaUtils.getStackTrace(ex));
		}finally{
			db.closeDB();
		}
		return returnedArray;
	}
	
	public String getColumn(String table_name){
		String column = null;
		if(table_name.equals("offers")){
			column = "offer_id";
		}else if(table_name.equals("material_offer")){
			column = "m_offer_id";
		}else if(table_name.equals("service_offer")){
			column = "s_offer_id";
		}
		return column;
	}	
	
}

 