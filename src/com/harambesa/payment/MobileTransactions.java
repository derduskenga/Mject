package com.harambesa.payment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileWriter;
import java.io.FileReader;
import java.util.Map;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.util.logging.Logger;
import java.sql.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import com.harambesa.gServices.HarambesaUtils;
import com.harambesa.DBConnection.DBConnection;
import com.harambesa.DBConnection.GlobalDresser;
import com.harambesa.Utility.Utilities;
//import com.harambesa.payment.DebitCardTransaction;

public class MobileTransactions{
	//transaction_reference is the user identifier
	//mobile_reference is m-pesa transaction code
	String transaction_reference,mobile_reference,mobile;
	double amount;
	Logger log= null;
	public MobileTransactions(){
		//this.entity_id = entity_id;
		if(log==null){
			log = Logger.getLogger(MobileTransactions.class.getName());
		}

	}
	
	//setters
	/*public void setMobile(String mobile){
		this.mobile = mobile;
	}
	public void setAmount(double amount){
		this.amount = amount;
	}
	public void setTransactionReference(String ref){
		this.transaction_reference = ref;
	}
	public void setMobileReference(String mobi_ref){
		this.mobile_reference = mobi_ref;
	}
	
	//getters
	public String getMobile(){
		return this.mobile;
	}
	public double getAmount(){
		return this.amount;
	}
	public String getTransactionReference(){
		return this.transaction_reference;
	}
	public String getMobileReference(){
		return this.mobile_reference;
	}
	
	public void testApi(String api_key){
		log.info("signature is: " + api_key);
	}*/
	
	public void tempStore(String transaction_code,String transaction_date,double transaction_amount,String transaction_name,String transaction_paybill,String transaction_mobile,int entity_id){
		DBConnection db = new DBConnection();
		Connection con = db._getConnection();
		String sql = "INSERT INTO mobile_money_temp (transaction_code,transaction_date,transaction_amount,transaction_name,transaction_paybill,transaction_mobile,entity_id)" +
			      " VALUES (?,?,?,?,?,?,?)" +
			      " RETURNING mobile_money_temp_id";
		try{
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, transaction_code);
			preparedStatement.setString(2, transaction_date);
			preparedStatement.setDouble(3, transaction_amount);
			preparedStatement.setString(4, transaction_name);
			preparedStatement.setString(5, transaction_paybill);
			preparedStatement.setString(6, transaction_mobile);
			preparedStatement.setInt(7, entity_id);
			
			ResultSet rs= preparedStatement.executeQuery();
			if(rs != null){
				if(rs.next());
			}
		}catch(SQLException sqle){
			log.severe("mobile money transaction failed to record sqle " + HarambesaUtils.getStackTrace(sqle));
		}catch(Exception ex){
			log.severe("mobile money transaction failed to record Blanket Exception " + HarambesaUtils.getStackTrace(ex));
		}finally{
			db.closeDB();
		}
		
	}
	public JSONObject checkMobiMoneyTransaction (double amount, String type, String entity_id_check,String phone){
		log.info("in checkMobiMoneyTransaction amount: " + amount);
		JSONObject json = new JSONObject();
		DBConnection db = new DBConnection();
		phone = standardPhone(phone);
		//Connection con = db._getConnection();
		String sql = "SELECT" +
			     " t_id,t_amount, entity_id" + 
			     " FROM transactions" +
			     " WHERE entity_id='" + entity_id_check + "'" +
			     " AND mobile_phone='" + phone + "'" +
			     " AND t_type='deposit'" +
			     " AND t_amount=" + amount;     
		try{
			ResultSet rs = db.readQuery(sql);
			if(rs.next()){
				json.put("success",1);
				json.put("message","Your payment was success. Please check your balance");
				
			}else{
				json.put("success",0);
				json.put("message","No record shows that you have paid. If you have not received your M-pesa message, please be patient");
			}
		}catch(SQLException sqle){
			json.put("success",0);
			json.put("message","An error occured. Please try again");
			log.severe("sqle Exception on checkMobiMoneyTransaction " + HarambesaUtils.getStackTrace(sqle));
		}catch (Exception ex){
			json.put("success",0);
			json.put("message","An error occured. Please try again");
			log.severe("General Exception on checkMobiMoneyTransaction " + HarambesaUtils.getStackTrace(ex));
		}
		return json;
	}	
	public void updateTempStore(String transaction_reference){
		JSONObject json = new JSONObject();
		DBConnection db = new DBConnection();
		Connection con = db._getConnection();
		String sql = "UPDATE mobile_money_temp SET is_acknowledge=? WHERE transaction_code=? AND is_acknowledge=?";
		try{
			PreparedStatement st = con.prepareStatement(sql);
			st.setBoolean(1,true);
			st.setString(2,transaction_reference);
			st.setBoolean(3,false);
			int rs = st.executeUpdate();
			if(rs>0){
				log.info("record updated");
			}else{
				log.info("No match found");
			}
		}catch(SQLException sqle){
			log.severe("sqle update error  " + HarambesaUtils.getStackTrace(sqle));
		}catch(Exception ex){
			log.severe("Blanket update error  " + HarambesaUtils.getStackTrace(ex));
		}finally{
			db.closeDB();
		}
	}
	public String standardPhone(String original){
		String finalStr = "254" + original.substring(1);
		return finalStr;
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