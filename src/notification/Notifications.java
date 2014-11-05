package com.harambesa.notification;

import java.util.logging.Logger;
import java.sql.*;
import com.harambesa.gServices.HarambesaUtils;
import com.harambesa.DBConnection.DBConnection;
import com.harambesa.Utility.Utilities;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class Notifications{
	Logger log= null;
	public Notifications(){
		if(log==null){
			log = Logger.getLogger(Notifications.class.getName());
		}
	}
	public boolean saveNotification(String url, String msg, String recipient, String originator){
		boolean flag = false;
		DBConnection db = new DBConnection();
		Connection con = db._getConnection();
		String sql = "INSERT" +
				" INTO notifications(originator_entity_id,notification_message,notification_url)" +
				" VALUES(?,?,?)" +
				" RETURNING notification_id";
		try{
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setInt(1, Integer.parseInt(originator));
			st.setString(1, msg);
			st.setString(2, url);
			
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
		return flag;
	}
}