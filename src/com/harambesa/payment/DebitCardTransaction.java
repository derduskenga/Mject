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
import com.harambesa.gServices.HarambesaUtils;
import com.harambesa.DBConnection.DBConnection;
import com.harambesa.DBConnection.GlobalDresser;
import com.harambesa.Utility.Utilities;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class DebitCardTransaction{
	//fields
	String entity_id,country,address,postal,email,mobile,state,cardType,index,currency;
	long reference;
	double amount;
	
	Logger log= null;
	//class constructor 
	public DebitCardTransaction(String entity_id){
		this.entity_id = entity_id;
		//this.session = request.getSession();
		if(log==null){
			log = Logger.getLogger(DebitCardTransaction.class.getName());
		}

	}
	//setters
	public void setCurrency(String currency){
		this.currency = currency;
	}
	public void setAmount(double amount){
		this.amount = amount;
	}
	public void setReference(long reference){
		this.reference = reference;
	}
	public void setIndex(String index){
		this.index = index;
	}
	public void setCountry(String country){
		this.country = country;
	}
	public void setAddress(String address){
		this.address = address;
	}
	public void setPostal(String postal){
		this.postal = postal;
	}
	public void setEmail(String email){
		this.email = email;
	}
	public void setMobile(String mobile){
		this.mobile = mobile;
	}
	public void setState(String state){
		this.state = state;
	}
	public void setCardType(String cardType){
		this.cardType = cardType;
	}
	//getters
	public String getCurrency(){
		return this.currency;
	}
	public double getAmount(){
		return this.amount;
	}
	public long getReference(){
		return this.reference;
	}
	public String getCountry(){
		return this.country;
	}
	public String getIndex(){
		return this.index;
	}
	public String getAddress(){
		return this.address;
	}
	public String getPostal(){
		return this.postal;
	}
	public String getEmail(){
		return this.email;
	}
	public String getMobile(){
		return this.mobile;
	}
	public String getState(){
		return this.state;
	}
	public String getCardType(){
		return this.cardType;
	}
	
	public JSONObject authorizeCardTransaction(String holder_card_number,String card_expiry_date, String card_holder_name, String cvv, String currency,String amount,String card_type){
		log.info("Now in authorize card transaction");
		setCardType(card_type);
		setAmount(Double.parseDouble(amount));
		setCurrency(currency);
		
		fetchUserDetails();
		String res = "";
		String url =  Utilities.LIPISHA_AUTHORIZE_CARD_TRANSACTION_FUNCTION_URL;
		log.info("URL here " + Utilities.LIPISHA_AUTHORIZE_CARD_TRANSACTION_FUNCTION_URL);
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		HttpResponse response = null;
		JSONObject jsonExceptionReturns = new JSONObject();
		
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("api_key", Utilities.LIPISHA_API_KEY));
		urlParameters.add(new BasicNameValuePair("api_signature", Utilities.LIPISHA_API_SIGNATURE));
		urlParameters.add(new BasicNameValuePair("api_version", Utilities.LIPISHA_API_VERSION));
		urlParameters.add(new BasicNameValuePair("api_type", Utilities.LIPISHA_API_TYPE));
		urlParameters.add(new BasicNameValuePair("account_number", Utilities.HARAMBESA_ACCOUNT_NUMBER));
		urlParameters.add(new BasicNameValuePair("card_number", holder_card_number));
		urlParameters.add(new BasicNameValuePair("address1", getAddress()));
		urlParameters.add(new BasicNameValuePair("address2", getMobile()));
		urlParameters.add(new BasicNameValuePair("expiry", card_expiry_date));
		urlParameters.add(new BasicNameValuePair("name", card_holder_name));
		urlParameters.add(new BasicNameValuePair("country", getCountry()));
		urlParameters.add(new BasicNameValuePair("state", getState()));
		urlParameters.add(new BasicNameValuePair("zip", getPostal()));
		urlParameters.add(new BasicNameValuePair("security_code",cvv));
		urlParameters.add(new BasicNameValuePair("amount",amount));
		urlParameters.add(new BasicNameValuePair("currency", currency));
		
		//Url Encoding the POST parameters
		try{
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
		}catch(UnsupportedEncodingException uee){
			log.severe("URL encoding exception authorize " + HarambesaUtils.getStackTrace(uee));
		}
		
		// Making HTTP Request
		try{
			response = client.execute(post);
		}catch(IOException ioe){
			log.severe("IOException here authorize " + HarambesaUtils.getStackTrace(ioe));
			
		}catch(Exception ex){
			log.severe("General Exception on http Request authorize " + HarambesaUtils.getStackTrace(ex));
			jsonExceptionReturns.put("success",0);
			jsonExceptionReturns.put("message","Unknown error. Please try again");
			return jsonExceptionReturns;
		}
		StringBuffer result = null;
		try{
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));	
			result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		}catch(Exception ex){
			log.severe("General Exception on buffering authorize " + HarambesaUtils.getStackTrace(ex));
		}
		
		res = result.toString();
		log.info("gateway feedback authorize " + res);
		writeToFile(res);
		JSONObject jObject = readFromFile();
		return parseJson(jObject,"authorize");
	}
	
	public JSONObject completeCardTransaction(){
		JSONObject jsonExceptionReturns = new JSONObject();
		log.info("Now in complete card transaction ");
		String res = null;
		String url =  Utilities.LIPISHA_COMPLETE_CARD_TRANSACTION_FUNCTION_URL;
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		HttpResponse response = null;
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("api_key", Utilities.LIPISHA_API_KEY));
		urlParameters.add(new BasicNameValuePair("api_signature", Utilities.LIPISHA_API_SIGNATURE));
		urlParameters.add(new BasicNameValuePair("api_version", Utilities.LIPISHA_API_VERSION));
		urlParameters.add(new BasicNameValuePair("api_type", Utilities.LIPISHA_API_TYPE));
		urlParameters.add(new BasicNameValuePair("transaction_index", removeLeadingTrailing(getIndex())));
		urlParameters.add(new BasicNameValuePair("transaction_reference", Long.toString(getReference())));
		
		log.info("transaction_reference=============== " + removeLeadingTrailing(getIndex()));
		//Url Encoding the POST parameters
		try{
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
		}catch(UnsupportedEncodingException uee){
			log.severe("URL encoding exception complete card " + HarambesaUtils.getStackTrace(uee));
		}
		
		// Making HTTP Request
		try{
			response = client.execute(post);
		}catch(IOException ioe){
			log.severe("IOException here complete card " + HarambesaUtils.getStackTrace(ioe));
		}catch(Exception ex){
			log.severe("General Exception on http Request complete card  " + HarambesaUtils.getStackTrace(ex));
			jsonExceptionReturns.put("success",0);
			jsonExceptionReturns.put("message","Unknown error. Please try again");
			return jsonExceptionReturns;
		}
		StringBuffer result = null;
		try{
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));	
			 result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		}catch(Exception ex){
			log.severe("General Exception on buffering: complete card " + HarambesaUtils.getStackTrace(ex));
			jsonExceptionReturns.put("success",0);
			jsonExceptionReturns.put("message","Unknown error. Please try again");
			return jsonExceptionReturns;
		}
		
		res = result.toString();
		writeToFile(res);
		log.info("gateway feedback complete " + res);
		JSONObject jObject = readFromFile();
		return parseJson(jObject,"complete");
		
	}
	public JSONObject reverseAuthorizedFund(){
		JSONObject jsonExceptionReturns = new JSONObject();
		String res = null;
		String url =  Utilities.LIPISHA_REVERSE_CARD_AUTHORIZATION_FUNCTION_URL;
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		HttpResponse response = null;
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("api_key", Utilities.LIPISHA_API_KEY));
		urlParameters.add(new BasicNameValuePair("api_signature", Utilities.LIPISHA_API_SIGNATURE));
		urlParameters.add(new BasicNameValuePair("api_version", Utilities.LIPISHA_API_VERSION));
		urlParameters.add(new BasicNameValuePair("api_type", Utilities.LIPISHA_API_TYPE));
		urlParameters.add(new BasicNameValuePair("transaction_index", removeLeadingTrailing(getIndex())));
		urlParameters.add(new BasicNameValuePair("transaction_reference", Long.toString(getReference())));
		log.info("Now in reverse card transaction ");
		//Url Encoding the POST parameters
		try{
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
		}catch(UnsupportedEncodingException uee){
			log.severe("URL encoding exception reverse card transaction " + HarambesaUtils.getStackTrace(uee));
		}
		// Making HTTP Request
		try{
			response = client.execute(post);
		}catch(IOException ioe){
			log.severe("IOException here reverse card " + HarambesaUtils.getStackTrace(ioe));
		}catch(Exception ex){
			log.severe("General Exception on http Request reverse card  " + HarambesaUtils.getStackTrace(ex));
			jsonExceptionReturns.put("success",0);
			jsonExceptionReturns.put("message","Unknown error. Please try again");
			return jsonExceptionReturns;
		}
		StringBuffer result = null;
		try{
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));	
			 result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		}catch(Exception ex){
			log.severe("General Exception on buffering: complete card " + HarambesaUtils.getStackTrace(ex));
			jsonExceptionReturns.put("success",0);
			jsonExceptionReturns.put("message","Unknown error. Please try again");
		}
		
		res = result.toString();
		writeToFile(res);
		JSONObject jObject = readFromFile();
		return parseJson(jObject,"reverse");
	}
	
	public boolean debitAccount(){
		//JSONObject jsonExceptionReturns = new JSONObject();
		boolean flag = false;
		DBConnection db = new DBConnection();
		Connection con = db._getConnection();
		String sql = "INSERT INTO transactions (t_type,entity_id,t_amount,currency,transaction_root)" +
			      " VALUES (?::operations,?,?,?,?)" +
			      " RETURNING t_id";
			      
		try{
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, Operations.deposit.toString());
			preparedStatement.setInt(2, Integer.parseInt(entity_id));
			preparedStatement.setDouble(3, getAmount());
			preparedStatement.setString(4, getCurrency());
			preparedStatement.setString(5,getCardType());
			
			ResultSet rs= preparedStatement.executeQuery();
			
			if(rs != null){
				if(rs.next());
				flag = true;
			}
		}catch(SQLException sqle){
			log.severe("card transaction failed to record sqle " + HarambesaUtils.getStackTrace(sqle));
		}catch(Exception ex){
			log.severe("card transaction failed to record Blanket Exception " + HarambesaUtils.getStackTrace(ex));
			//jsonExceptionReturns.put("success",0);
			//jsonExceptionReturns.put("message","Unknown error. Please try again");
			//return jsonExceptionReturns;
		}finally{
			db.closeDB();
		}
		return flag; 
	}
	
	public JSONObject sendMoneyToMobile(String mobile, String amount){
		//compare amount withdrawable with his/her balance		
		log.info("amount inially = " + amount);
		JSONObject jsonExceptionReturns = new JSONObject();
		JSONObject lessFundsJson = new JSONObject();
		if(getWithdrawableFundsMobile(Double.parseDouble(amount)) <= new GlobalDresser(entity_id).getHarambesaBalance()){	
			String res = null;
			String url =  Utilities.LIPISHA_SEND_MONEY_FUNCTION_URL;
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			HttpResponse response = null;
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("api_key", Utilities.LIPISHA_API_KEY));
			urlParameters.add(new BasicNameValuePair("api_signature", Utilities.LIPISHA_API_SIGNATURE));
			urlParameters.add(new BasicNameValuePair("api_version", Utilities.LIPISHA_API_VERSION));
			urlParameters.add(new BasicNameValuePair("api_type", Utilities.LIPISHA_API_TYPE));
			urlParameters.add(new BasicNameValuePair("account_number", Utilities.HARAMBESA_ACCOUNT_NUMBER_PAYOUT));
			urlParameters.add(new BasicNameValuePair("mobile_number", mobile));
			//urlParameters.add(new BasicNameValuePair("amount", Double.toString(amount)));
			urlParameters.add(new BasicNameValuePair("amount",amount));
			log.info("Now in send money"); 
			//Url Encoding the POST parameters
			//log.info("amount later = " + Double.toString(amount));
			try{
				post.setEntity(new UrlEncodedFormEntity(urlParameters));
			}catch(UnsupportedEncodingException uee){
				log.severe("URL encoding exception send money " + HarambesaUtils.getStackTrace(uee));
			}
			// Making HTTP Request
			try{
				response = client.execute(post);
			}catch(IOException ioe){
				log.severe("IOException here send money " + HarambesaUtils.getStackTrace(ioe));
				jsonExceptionReturns.put("success",0);
				jsonExceptionReturns.put("message","Unknown error. Please try again");
				return jsonExceptionReturns;
			}catch(Exception ex){
				log.severe("General Exception on http Request send money  " + HarambesaUtils.getStackTrace(ex));
				jsonExceptionReturns.put("success",0);
				jsonExceptionReturns.put("message","Unknown error. Please try again");
				return jsonExceptionReturns;
			}
			StringBuffer result = null;
			try{
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));	
				result = new StringBuffer();
				String line = "";
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}
			}catch(Exception ex){
				log.severe("General Exception on buffering: send money " + HarambesaUtils.getStackTrace(ex));
			}
			
			JSONObject jObject = new JSONObject();
			try{
				res = result.toString();
				log.info("send money response is " + res);
				writeToFile(res);
				jObject = readFromFile();
			}catch(Exception ex){			
				log.severe("General Exception on reading from file: send money " + HarambesaUtils.getStackTrace(ex));	
			}
				
			//log.info("send money response after read from file " + jObject);
			return parseJson(jObject,"send_money");
		}else{
			lessFundsJson.put("success",0);
			lessFundsJson.put("message","You have insuficient funds");
			return lessFundsJson;
		}
	}
	public JSONObject parseJson(JSONObject jsonObj,String transaction_type){
		JSONObject jsonExceptionReturns = new JSONObject();
		long status_code = 12345678910L;
		String status_description = null;
		String status = null;		
		String transaction_index = null;
		long transaction_reference = 12345678910L;
		String invalid_parameters = null;
		String reason = null;
		JSONObject customObj = new JSONObject();
		
		  try{
			JSONObject statusObj = (JSONObject)jsonObj.get("status");
			JSONObject contentObj = (JSONObject)jsonObj.get("content");
			
			status = (String)statusObj.get("status");
			
			
			if(transaction_type.equals("authorize")){
				if(status.equals("SUCCESS")){
					//authorize successiful
					status_description = (String)statusObj.get("status_description");
					status = (String)statusObj.get("status");
					
					transaction_index = (String)contentObj.get("transaction_index"); 
					transaction_reference = (Long)contentObj.get("transaction_reference");
				      
					setReference(transaction_reference);
					setIndex(transaction_index);
					log.info("transaction_reference " + transaction_reference);
					return completeCardTransaction();
				}else{
					status_code = (Long)statusObj.get("status_code");
					if(status_code ==1000){
						customObj.put("success",0);
						customObj.put("message","Some of your data may have been lost. Please try again");
					}else if(status_code == 2000){
						customObj.put("success",0);
						customObj.put("message",(String)contentObj.get("invalid_parameters"));
					}else if(status_code == 3000){
						customObj.put("success",0);
						customObj.put("message","An error occured. Please contact Harambesa for assistance");
					}else if(status_code == 4000){
						customObj.put("success",0);
						customObj.put("message","Something went wrong. Our engineers are working to solve the problem. You can contact Harambesa if you wish");
					}else if(status_code == 5000){
						customObj.put("success",0);
						customObj.put("message",(String)contentObj.get("reason"));
					}
				}
				  
			}else if(transaction_type.equals("complete")){
				//transaction is complete
				if(status.equals("SUCCESS")){
					
					//record this in Harambesa database
					if(debitAccount()){
						customObj.put("success",1);
						customObj.put("message","Account has been loaded successiful. Please see your balance");
					}else{
						customObj.put("success",0);
						customObj.put("message","A severe error occured. Please contact Harambesa");
					}
				}else{
					status_code = (Long)statusObj.get("status_code");
					if(status_code ==1000){
						customObj.put("success",0);
						customObj.put("message","Some of your data may have been lost. Please try again");
						reverseAuthorizedFund();
					}else if(status_code == 2000){
						customObj.put("success",0);
						customObj.put("message",(String)contentObj.get("invalid_parameters"));
						reverseAuthorizedFund();
					}else if(status_code == 3000){
						customObj.put("success",0);
						customObj.put("message","An error occured. Please contact Harambesa for assistance");
						reverseAuthorizedFund();
					}else if(status_code == 4000){
						customObj.put("success",0);
						customObj.put("message","Something went wrong. Our engineers are working to solve the problem");
						reverseAuthorizedFund();
					}else if(status_code == 5000){
						customObj.put("success",0);
						customObj.put("message",(String)contentObj.get("reason"));
						reverseAuthorizedFund();
					}
				}

			}else if(transaction_type.equals("send_money")){
				//user tries to withdraw funds to his/her mobile phone
				if(status.equals("SUCCESS")){
					String mobile = (String)contentObj.get("mobile_number");
					String amount = (String)contentObj.get("amount");
					String reference = (String)contentObj.get("reference");
					
					setMobile(mobile);
					setAmount(Double.parseDouble(amount));
					
					if(recordSendMoneyTransaction(reference)){
						customObj.put("success",1);
						customObj.put("message","Your withdraw was successiful !");	
					}else{
						customObj.put("success",0);
						customObj.put("message","A severe error occured. Please contact Harambesa and explain your to withdraw");	
					}
				}else{
					status_code = (Long)statusObj.get("status_code");
					if(status_code ==1000){
						customObj.put("success",0);
						customObj.put("message","Some of your data may have been lost. Please try again");
						
					}else if(status_code == 2000){
						customObj.put("success",0);
						customObj.put("message",(String)contentObj.get("invalid_parameters"));
						
					}else if(status_code == 3000){
						customObj.put("success",0);
						customObj.put("message","An error occured. Please contact Harambesa for assistance");
						
					}else if(status_code == 4000){
						customObj.put("success",0);
						customObj.put("message","Something went wrong. Our engineers are working to solve the problem");
						
					}else if(status_code == 5000){
						customObj.put("success",0);
						customObj.put("message","An error occured. Also ensure that  check your account balance is sufficient");
						
					}
				}
			}
		  }catch(Exception ex){
			   customObj.put("success",0);
			   customObj.put("message","Unknown error occured");
			   log.severe("JSONException occured " + HarambesaUtils.getStackTrace(ex));
		  }
			
		  return customObj;
	}
	public boolean recordSendMoneyTransaction(String reference){
		//after funds have been sent successifully
		DBConnection db = new DBConnection();
		Connection con = db._getConnection();
		boolean flag = false;
		
		String sql = "INSERT INTO transactions (t_type,entity_id,t_amount,currency,transaction_root,external_transaction_code,mobile_phone)" +
			      " VALUES (?::operations,?,?,?,?,?,?)" +
			      " RETURNING t_id";
		try{
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, Operations.withdraw.toString());
			preparedStatement.setInt(2, Integer.parseInt(entity_id));
			preparedStatement.setDouble(3, getAmount());
			preparedStatement.setString(4, "KES");
			preparedStatement.setString(5,"withdraw via mobile");
			preparedStatement.setString(6,reference);
			preparedStatement.setString(7,getMobile());
			
			ResultSet rs= preparedStatement.executeQuery();
			if(rs != null){
				if(rs.next());
				flag = true;
			}
		}catch(SQLException sqle){
			log.severe("mobile money withdraw transaction failed to record sqle " + HarambesaUtils.getStackTrace(sqle));
		}catch(Exception ex){
			log.severe("mobile money withdraw transaction failed to record Blanket Exception " + HarambesaUtils.getStackTrace(ex));
		}finally{
			db.closeDB();
		}
		return flag;
	}
	
	public JSONObject saveBankWithDrawalRequest(String amount,String bank_name,String branch_name,String account_number,String account_names,String swift_code,String address,String currency){
		DBConnection db = new DBConnection();
		Connection con = db._getConnection();
		JSONObject json = new JSONObject();
		if(getWithdrawableFundsBank(Double.parseDouble(amount)) <= new GlobalDresser(entity_id).getHarambesaBalance()){
			String sql = "INSERT INTO bank_withdrawal_requests (requestor_entity_id,request_amount,request_bank_name,request_bank_branch,request_account_number,request_account_names,request_bank_swift_code,request_other_details,currency)" +
				    " VALUES(?,?,?,?,?,?,?,?,?)" + 
				    " RETURNING bank_withdrawal_requests_id";
			try{
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setInt(1,Integer.parseInt(entity_id));
				ps.setDouble(2,Double.parseDouble(amount));
				ps.setString(3,bank_name);
				ps.setString(4,branch_name);
				ps.setString(5,account_number);
				ps.setString(6,account_names);
				ps.setString(7,swift_code);
				ps.setString(8, address);
				ps.setString(9,currency);
				
				ResultSet rs = ps.executeQuery();
				
				if(rs != null){
					if(rs.next());
					json.put("success",1);
					json.put("message","You withdraw request has been placed for processing");
				}else{
					json.put("success",0);
					json.put("message","An error occured. Please try again");
				}
				
			}catch(SQLException sqle){
				log.severe("Error: " + HarambesaUtils.getStackTrace(sqle));
				json.put("success",0);
				json.put("message","An error occured. Please try again");
			}catch(Exception ex){
				log.severe("Error: " + HarambesaUtils.getStackTrace(ex));
				json.put("success",0);
				json.put("message","An error occured. Please try again");
				
			}finally{
				db.closeDB();
			}
		}else{
			json.put("success",0);
			json.put("message","You have insuficient funds.");
		}
		return json;
	}
	public void fetchUserDetails(){
		//fetching phone_number, address, country, state, zip		
		DBConnection db = new DBConnection();
		String sql = "SELECT" +
		      " country,address,postal_code," +
		      " primary_email,mobile_number," +
		      " sys_country_name,city" +
		      " FROM entitys" +
		      " RIGHT JOIN countrys" +
		      " ON entitys.country=countrys.sys_country_id" +
		      " WHERE entitys.entity_id='" + entity_id + "'";
		try{
			ResultSet rs = db.readQuery(sql);
			if(!rs.next()){
				//do nothing
			}else{
				setAddress(rs.getString(2));
				setPostal(rs.getString(3));
				setEmail(rs.getString(4));
				setMobile(rs.getString(5));
				setCountry(rs.getString(6).toUpperCase());
				setState(rs.getString(7));
				
				log.info(rs.getString(2)+","+rs.getString(3)+","+rs.getString(4)+","+rs.getString(5)+","+rs.getString(6).toUpperCase() + ","+rs.getString(7));
			}
		}catch(SQLException sqle){
			log.severe("Error is here: sqle " + HarambesaUtils.getStackTrace(sqle));
		}catch(Exception ex){
			log.severe("Error is here: Blanket ex " + HarambesaUtils.getStackTrace(ex));
		}finally{
			db.closeDB();
		}
	}
	public JSONObject readFromFile(){
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = new JSONObject();
		try {
			Object obj = parser.parse(new FileReader(Utilities.LIPISHA_FEED_FILE));
	
			jsonObject = (JSONObject) obj;
			//setAccessToken((String)jsonObject.get(Utilities.ACCESS_TOKEN));
			//setExpiry((Long)jsonObject.get(Utilities.EXPIRY));
			//setAccessType((String)jsonObject.get(Utilities.ACCESS_TYPE));	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	public void writeToFile(String jsonStr){
		try {
			FileWriter file = new FileWriter(Utilities.LIPISHA_FEED_FILE);
			file.write(jsonStr);
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String removeLeadingTrailing(String str){
		String loginToken = str;
		loginToken = loginToken.substring(1, loginToken.length() - 1);
		return loginToken;
	}
	
	public double getWithdrawableFundsBank(double requested){
		  //pass the figure in shillings
		  return requested + ((requested*Utilities.WITHDRAWAL_RATE)/100);
	}
	
	public double getWithdrawableFundsMobile(double requested){
		//for airtel and mpesa
		//pas figures in shillings
		return requested + ((requested*Utilities.WITHDRAWAL_RATE)/100) + Utilities.MPESA_CHARGES;
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