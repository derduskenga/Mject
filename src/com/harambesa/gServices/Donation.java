package com.harambesa.gServices; 

import java.sql.*;
import com.harambesa.DBConnection.DBConnection;
import com.harambesa.DBConnection.GlobalDresser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.logging.Logger;
import com.harambesa.notification.Notifications;
import com.harambesa.gServices.HarambesaUtils;
import com.harambesa.gServices.Offer;
import com.harambesa.Utility.Utilities;

public class Donation{
	//fields
	Logger log= null;
	private String amount;
	private String currency;
	private String entity_id;
	private String amount_requested; 
	private int balanceCheck;
	private boolean isDonationComplete;
	//getters and setters
	
	public void setIsDonationComplete(boolean donation_status){
		this.isDonationComplete = donation_status;
	}
	
	public boolean getIsDonationComplete(){
		return isDonationComplete;
	}
	
	public void setBalanceCheck(int accountValue){
		this.balanceCheck = accountValue;
	}
	
	public int getBalanceCheck(){
		return balanceCheck;
	
	}
	//parameterised constructor 
	public Donation(String amount,String currency,String donor, String amount_requested){
		//logger.debug("In class Donation");
		this.amount = amount;
		this.currency = currency;
		this.entity_id = donor;
		this.amount_requested = amount_requested;
		if(log==null){
			log = Logger.getLogger(Donation.class.getName());
		}
	}
	public JSONObject makeDonation(String donor, String donee,String source_type,String donation_request_id){
		//register 2 transactions, donor to have donated and donee to have received
		JSONObject jObj = new JSONObject();
		
		if(recordTransaction(donor,donee,source_type,donation_request_id)){
			//All was successifull
			jObj.put("success",1);
			jObj.put("message","You successifully made a donation.");
			jObj.put("account",getBalanceCheck());
			jObj.put("donation_status",getIsDonationComplete());
			jObj.put("donation_request_id",donation_request_id);
			
			String type = "make_donation";
			String notification_url = Utilities.NOTIFICATION_URL + "home/?type=" + type + "&dri=" + donation_request_id;
			String notification_msg = "<a href='../user/?user=" + donor + "'>" + new GlobalDresser(donor).getNames() +" made a donation on your request</a>";
			String notification_originator = donee;

			Notifications noti = new Notifications(donor);
			String [] recipients = {donee};
			noti.saveNotification(notification_url,notification_msg,notification_originator,recipients);
			
			
		
		}else{
			//all transactions failed
			jObj.put("success",0);
			jObj.put("message","Something went wrong");
			jObj.put("account",getBalanceCheck());
			jObj.put("donation_status",getIsDonationComplete());
			jObj.put("donation_request_id",donation_request_id);
			jObj.put("request_owner_entity_id",donee);
		}
		return jObj;
	}
	
	public boolean recordTransaction(String donor, String donee,String source_type,String donation_request_id){
		
		DBConnection db = new DBConnection();
		Connection dbCon = db._getConnection();
		boolean transactionFlag = false;
		
		
		if(getAccountBalance() >= Double.parseDouble(amount)){ 
			setBalanceCheck(1);
			
			if(Double.parseDouble(amount) >= Double.parseDouble(amount_requested)-getDonationAlreadyMade(donation_request_id)){
				setIsDonationComplete(true);
			}else{
				setIsDonationComplete(false);
			}
			
			String queryRecordDonation = "INSERT INTO transactions (t_type,t_amount, entity_id, currency,donation_source,transaction_root) VALUES('donation','"+ amount +"','"+ donor +"','"+ currency +"','" + source_type + "','donation request')";
			String queryRecordDonationReceive = "INSERT INTO transactions (t_type,t_amount, entity_id, currency,donation_source,transaction_root) VALUES('donation_receive','"+ amount +"','"+ donee +"','"+ currency +"','" + source_type + "','Donation request')";
			//set a query to update donation request status
			String queryUpdateDonationRequestStatus = "UPDATE donation_requests SET complete ='" + getIsDonationComplete() + "' WHERE donation_request_id='" + donation_request_id + "'";			
			//insert a donation points earning operation
			String queryDonationPoints = "INSERT INTO points_transactions (points_transactions_entity_id,points_transactions_type,number_of_points) VALUES('" + donor + "','donation','" + calculatePoints(amount,currency) + "')";
			//update the already donated VALUES
			Double update = getDonationAlreadyMade(donation_request_id) + Double.parseDouble(amount);			
			String queryUpdateAlreadyDonated = "UPDATE donation_requests SET donation_already_made='" + update + "' WHERE donation_request_id='" + donation_request_id + "'";
			
			try{
			
				dbCon.setAutoCommit(false);
				String resultRecordDonation = db.executeQuery(queryRecordDonation);
				String resultRecordDonationReceive = db.executeQuery(queryRecordDonationReceive);
				String resultUpdateDonationRequestStatus = db.executeUpdate(queryUpdateDonationRequestStatus);
				String resultQueryDonationPoints = db.executeQuery(queryDonationPoints);
				String resultQueryUpdateAlreadyDonated = db.executeUpdate(queryUpdateAlreadyDonated);
					
					//commit the connection
					//logger.debug("Results of the five queries of the transaction resultRecordDonation: " + resultRecordDonation + " and resultRecordDonationReceive: " + resultRecordDonationReceive);
					log.severe("Results of the five queries of the transaction resultRecordDonation: " + resultRecordDonation + " and resultRecordDonationReceive: " + resultRecordDonationReceive);
					dbCon.commit();
					transactionFlag = true;

			}catch(SQLException sqle){
				
				//logger.error("error that led to rollback is: " + sqle.getMessage().toString());
				try{
					dbCon.rollback();
				
				}catch(SQLException sqexc){
					System.out.print(sqexc.getMessage().toString());
					//logger.error(sqexc.getMessage().toString());
					log.severe("URL encoding exception authorize " + HarambesaUtils.getStackTrace(sqexc));
				}
			
			}catch(Exception e){
				//logger.error("Blanket Exception" + e.getMessage().toString());
				System.out.print("Blanket Exception" + e.getMessage().toString());
				log.severe("Error blanket " + HarambesaUtils.getStackTrace(e));
			}
			
			try{
			
				if(dbCon!=null){
					db.closeDB();
					dbCon.setAutoCommit(true);
				}
			
			}catch(SQLException sqle){
				System.out.print(sqle.getMessage().toString());
				//logger.error(sqle.getMessage().toString());
				log.severe("close Connection error" + HarambesaUtils.getStackTrace(sqle));
			
			}
			
			
		}else {
			setBalanceCheck(0);
			//setIsDonationComplete(false);
		
		}
		
		return transactionFlag;
	}
	
	
	public double getAccountBalance(){
		  GlobalDresser globaldresser= new GlobalDresser(entity_id);
		  return globaldresser.getHarambesaBalance();
	
	}
	
	
	public int calculatePoints(String amount, String currency){
		
		double newAmount = Double.parseDouble(amount);
		if(currency.equals("1")){
			return (int) Math.round(newAmount);
		}else{
			double newVal = getConvetionRate(currency) * newAmount;
			int points = (int)newAmount;
			
			return points;
		
		}
	}
	
	public double getDonationAlreadyMade(String donation_request_id){
	
		GlobalDresser globaldresser = new GlobalDresser(entity_id);
		String totalDonations = globaldresser.getDonationAlreadyMade(donation_request_id);
		return Double.parseDouble(totalDonations);
	
	}
	/*returns convertion rate: from dollars to shillings*/
	public double getConvetionRate(String currency){
	    return 83;
	
	}
	
	public boolean recordDonation(String donor){
	
		DBConnection db = new DBConnection();
		String query = "INSERT INTO transactions (t_type,t_amount, entity_id, currency) VALUES('donation','"+ amount +"','"+ donor +"','"+ currency +"')";
		boolean recordDonationFlag = false;
		
		String result = db.executeQuery(query);
		
		if(result == null){
			
			recordDonationFlag = true;
		
		}
		
		db.closeDB();
		return recordDonationFlag;
	
	}
	
	public boolean recordDonationReceive(String donee){
	
		DBConnection db = new DBConnection();
		String query = "INSERT INTO transactions (t_type,t_amount, entity_id, currency) VALUES('donation_receive','"+ amount +"','"+ donee +"','"+ currency +"')";
		boolean recordDonationReceiveFlag = false;
		
		String result = db.executeQuery(query);
		
		if(result == null){
			
			recordDonationReceiveFlag = true;
		
		}
		
		db.closeDB();
		
		return recordDonationReceiveFlag;
	}
      

}