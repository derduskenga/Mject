package com.harambesa.DBConnection;

import java.sql.*;
import com.harambesa.DBConnection.DBConnection;
import com.harambesa.gServices.HarambesaUtils;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.logging.Logger;

public class GlobalDresser{
	//fields
	String entity_id;
	//HttpSession session;
	Logger log= null;
	public GlobalDresser(String entity_id){
		this.entity_id=entity_id;
		//this.session = request.getSession();
		if(log==null){
			log = Logger.getLogger(GlobalDresser.class.getName());
		}

	}

    //other methods
    //returns true if sender and receiver are connected
    public boolean isConnected(int receiver_id){
      DBConnection db = new DBConnection();
      boolean isConnected_flag = false;
      String query = "SELECT requestor_entity_id,requestee_entity_id" +
		      " FROM connections" +
		      " WHERE ((requestor_entity_id='" + entity_id + "' AND requestee_entity_id='" + receiver_id + "') OR (requestor_entity_id='" + receiver_id + "' AND requestee_entity_id='" + entity_id + "'))" +
		      " AND is_accepted=TRUE" +
		      " AND connection_is_blocked=FALSE";
     
      ResultSet isConnectedResultset = db.readQuery(query);
      
      try{
	  
	  if(isConnectedResultset.next()){
	    
	    isConnected_flag = true;
	  }
      
      }catch(SQLException sqle){
      
	  System.out.print(sqle.getMessage().toString());
	   log.severe("Error blanket " + HarambesaUtils.getStackTrace(sqle));
	   
	  
      }catch(NullPointerException npe){
      
	  System.out.print(npe.getMessage().toString());
      
      }finally{
	      db.closeDB();
      }
	
      return isConnected_flag;
    }
    
    
    public boolean isRequestExist(int receiver_id){
	    DBConnection db = new DBConnection();
	    boolean isRequestExist_flag = false;
	    String query = "SELECT requestor_entity_id,requestee_entity_id" +
			    " FROM connections" +
			    " WHERE ((requestor_entity_id='" + entity_id + "' AND requestee_entity_id='" + receiver_id + "') OR (requestor_entity_id='" + receiver_id + "' AND requestee_entity_id='" + entity_id + "'))" +
			    " AND is_accepted=FALSE" +
			    " AND connection_is_blocked=FALSE";
	    
	    ResultSet isConnectedResultset = db.readQuery(query);
	    
	    try{
		
		if(isConnectedResultset.next()){
		  
		  isRequestExist_flag = true;
		}
	    
	    }catch(SQLException sqle){
	    
		System.out.print(sqle.getMessage().toString());
		
	    }catch(NullPointerException npe){
	    
		System.out.print(npe.getMessage().toString());
	    
	    }finally{
		    db.closeDB();
	    }
	      
	      return isRequestExist_flag;
    }
    
    public JSONObject saveConnectionRequest(String receiver){
		DBConnection db = new DBConnection();
		Connection con = db._getConnection();
		JSONObject json = new JSONObject();
		String queryString = "INSERT INTO connections (requestor_entity_id,requestee_entity_id) VALUES (?,?)" +
				      " RETURNING connection_id";
														    
			try{  
				PreparedStatement st = con.prepareStatement(queryString);
				st.setInt(1,Integer.parseInt(entity_id));
				st.setInt(2,Integer.parseInt(receiver));
				ResultSet rs= st.executeQuery();
				if(rs != null){
					if(rs.next());
					json.put("success",1);
					json.put("message","Your request has been sent !");
				}else{
					json.put("success",2);
					json.put("message","An error ocured ");
				
				}
											      
			}catch(SQLException sqle){
				log.severe("Error sqle " + HarambesaUtils.getStackTrace(sqle));
				json.put("success",2);
				json.put("message","An error occured. Please try again");
			}catch(Exception ex){
				  log.severe("Error blanket " + HarambesaUtils.getStackTrace(ex));
				  json.put("success",2);
				  json.put("message","An error occured. Please try again");
			}finally{
				db.closeDB();
				
			}
		return json;
			
	}
    
    
    public double getHarambesaBalance (){
	double balance = ((getStartingBalance()+getTotalPointSales()+getTotalDonationReceive()+getTotalDeposits()) - (getTotalWithdrawals()+getTotalPointPurchase()+getTotalDonations()));
	return balance;
    
    }
    
    public int getHarambesaPoints(){
		double points = 0.0;
		try{
			points = (getTotalPointsStartingBalance() + getTotalPointsFromPurchase() + getTotalPointsFromDonations())-getTotalPointsFromSale();
			
		}catch(Exception ex){
			log.severe("Error blanket from GlobalDresser points balance " + HarambesaUtils.getStackTrace(ex));
		}
		int pointsC = (int)points;
		return pointsC;
    }
    
    public double getTotalPointsFromDonations(){    
		DBConnection db = new DBConnection();
		String query = "SELECT SUM(number_of_points) AS totalFromPurchase FROM points_transactions WHERE points_transactions_type='donation' AND points_transactions_entity_id='" + entity_id + "'";
		ResultSet cRs = db.readQuery(query);
		double pointsSaleTotal = 0.0;
		
		try{
		
		    if(cRs.next()){
			
			pointsSaleTotal = Double.parseDouble(cRs.getString("totalFromPurchase"));    
		    }
		    
		}catch(SQLException sqle){	
		    System.out.print("error occuring" + sqle.getMessage().toString());	
		}catch(NullPointerException npe){
		    
		  // System.out.print("No such transactions");
		
		}finally{
			db.closeDB();
		}
		
		
		
		return pointsSaleTotal;
    } 
    public double getTotalPointsFromSale(){   
		  
		DBConnection db = new DBConnection();
		String query = "SELECT SUM(number_of_points) AS totalFromPurchase FROM points_transactions WHERE points_transactions_type='sale_purchase' AND points_transactions_entity_id='" + entity_id + "'";
		ResultSet cRs = db.readQuery(query);
		double pointsSaleTotal = 0.0;
		
		try{
		
		    if(cRs.next()){
			pointsSaleTotal = Double.parseDouble(cRs.getString("totalFromPurchase"));
		    
		    }
		    
		}catch(SQLException sqle){	
		    System.out.print("error occuring" + sqle.getMessage().toString());	
		}catch(NullPointerException npe){
		    
		  // System.out.print("No such transactions");
		
		}
		
		db.closeDB();
		
		return pointsSaleTotal;
    }   
    public double getTotalPointsFromPurchase(){  
	    
	DBConnection db = new DBConnection();
	String query = "SELECT SUM(number_of_points) AS totalFromPurchase FROM points_transactions WHERE points_transactions_type='sale_purchase' AND buyer_entity_id='" + entity_id + "'";
	ResultSet cRs = db.readQuery(query);
	double pointsSaleTotal = 0.0;
	
	try{
	
	    if(cRs.next()){
			pointsSaleTotal = Double.parseDouble(cRs.getString("totalFromPurchase"));
			log.info("pointsSaleTotal " + pointsSaleTotal);	    
	      }
			
	   
	}catch(SQLException sqle){	
		  System.out.print("error occuring" + sqle.getMessage().toString());	
		  log.severe("Error blanket from get Total Points From Purchase " + HarambesaUtils.getStackTrace(sqle));
	}catch(NullPointerException npe){
	    
	    //System.out.print("No such transactions");
	
	}
	db.closeDB();
	log.info("RETURNed " + pointsSaleTotal);
	return pointsSaleTotal;   
	    
    }
    public double getTotalPointsStartingBalance(){
	    DBConnection db = new DBConnection();
	    String query = "SELECT starting_points_balance FROM  balances WHERE entity_id='" + entity_id + "'";
	    ResultSet cRs = db.readQuery(query);

	    double startingPointsBalance = 0.0;
	    
	    if(cRs!=null){
		    try{

			    if(cRs.next()){

				    startingPointsBalance = Integer.parseInt(cRs.getString("starting_points_balance"));

			    }

		    }catch(SQLException sqle){

			    System.out.print(sqle.getMessage().toString());

		    }catch(NullPointerException npe){

			    //System.out.print("No such transactions");

		    }
	}

	    db.closeDB();

	    return startingPointsBalance;
    }
    
    public double getTotalDonationReceive(){
    
	DBConnection db = new DBConnection();
	String query = "SELECT SUM(t_amount) AS totalDonationReceives FROM  transactions WHERE t_type='donation_receive' AND entity_id='" + entity_id + "'";
	ResultSet cRs = db.readQuery(query);
	double donation_receives = 0.0;
	
	try{
	
	    if(cRs.next()){
		
		donation_receives = Double.parseDouble(cRs.getString("totalDonationReceives"));
	    
	    }
	    
	}catch(SQLException sqle){
	
	    System.out.print(sqle.getMessage().toString());
	
	}catch(NullPointerException npe){
	    
	   // System.out.print("No such transactions");
	
	}
	
	db.closeDB();
	
	return donation_receives;
    
    
    }
   
    public double getTotalWithdrawals(){
	
	DBConnection db = new DBConnection();
	String query = "SELECT SUM(t_amount) AS totalWithdrawals FROM  transactions WHERE t_type='withdraw' AND entity_id='" + entity_id + "'";
	ResultSet cRs = db.readQuery(query);
	double withdrawals = 0.0;
	
	try{
	
	    if(cRs.next()){
		withdrawals = Double.parseDouble(cRs.getString("totalWithdrawals"));
	    
	    }
	    
	}catch(SQLException sqle){
	
	    System.out.print(sqle.getMessage().toString());
	
	}catch(NullPointerException npe){
	    
	    //System.out.print("No such transactions");
	
	}
	
	db.closeDB();
	
	return withdrawals;
	
    }
    
    public double getTotalDeposits(){
	
	DBConnection db = new DBConnection();
	String query = "SELECT SUM(t_amount) AS totalWithdrawals FROM  transactions WHERE t_type='deposit' AND entity_id='" + entity_id + "'";
	ResultSet cRs = db.readQuery(query);
	double deposits = 0.0;
	
	try{
	
	    if(cRs.next()){
		deposits = Double.parseDouble(cRs.getString("totalWithdrawals"));
	    
	    }
	    
	}catch(SQLException sqle){
	
	    System.out.print(sqle.getMessage().toString());
	
	}catch(NullPointerException npe){
	    
	   // System.out.print("No such transactions");
	
	}
	
	db.closeDB();
	
	return deposits;
    }
    
    public double getTotalPointPurchase(){
	DBConnection db = new DBConnection();
	String query = "SELECT SUM(t_amount) AS totalWithdrawals FROM  transactions WHERE t_type='purchase_points' AND entity_id='" + entity_id + "'";
	ResultSet cRs = db.readQuery(query);
	double pointsSaleTotal = 0.0;
	
	try{
	
	    if(cRs.next()){
		pointsSaleTotal = Double.parseDouble(cRs.getString("totalWithdrawals"));
	    
	    }
	    
	}catch(SQLException sqle){	
	    System.out.print("error occuring" + sqle.getMessage().toString());	
	}catch(NullPointerException npe){
	    
	  // System.out.print("No such transactions");
	
	}
	
	db.closeDB();
	
	return pointsSaleTotal;
    }
  
    public double getTotalPointSales(){
		DBConnection db = new DBConnection();
		String query = "SELECT SUM(t_amount) AS totalWithdrawals FROM  transactions WHERE t_type='sale_points' AND entity_id='" + entity_id + "'";
		ResultSet cRs = db.readQuery(query);
		double pointsSaleTotal = 0.0;
	
	try{
	
	    if(cRs.next()){
		pointsSaleTotal = Double.parseDouble(cRs.getString("totalWithdrawals"));
	    
	    }
	    
	}catch(SQLException sqle){
	
	    System.out.print(sqle.getMessage().toString());
	    log.severe("Error sqle on money balance" + HarambesaUtils.getStackTrace(sqle));
	
	}catch(NullPointerException npe){
	    
	    //System.out.print("No such transactions");
	
	}
	
	db.closeDB();
	 //log.info("money from pints sales" + pointsSaleTotal);
	return pointsSaleTotal;
    }

    public double getTotalDonations(){

      
	DBConnection db = new DBConnection();
	String query = "SELECT SUM(t_amount) AS totalWithdrawals FROM  transactions WHERE t_type='donation' AND entity_id='" + entity_id + "'";
	ResultSet cRs = db.readQuery(query);
	double donationTotal = 0.0;
	
	try{
	
	    if(cRs.next()){
		donationTotal = Double.parseDouble(cRs.getString("totalWithdrawals"));
	    
	    }
	    
	}catch(SQLException sqle){
	
	    System.out.print(sqle.getMessage().toString());
	
	}catch(NullPointerException npe){
	    
	    log.info("Erro at donation");
	    donationTotal = 0.0;
	
	}
	
	log.info("money i have donated is: " + donationTotal);
	db.closeDB();
	
	return donationTotal;


    }
    
    public double getStartingBalance(){
	
	  DBConnection db = new DBConnection();
	  String query = "SELECT starting_balance FROM  balances WHERE entity_id='" + entity_id + "'";
	  ResultSet cRs = db.readQuery(query);
	  
	  double startingBalance = 0.0;
	  
	  try{
	  
	      if(cRs.next()){
		  
			startingBalance = Double.parseDouble(cRs.getString("starting_balance"));
	      
	      }
	      
	  }catch(SQLException sqle){
	  
	      System.out.print(sqle.getMessage().toString());
	      log.severe("Error blanket from get starting_balance " + HarambesaUtils.getStackTrace(sqle));
	  
	  }catch(NullPointerException npe){
	      
	      //System.out.print("No such transactions");
	  
	  }
	  
	  db.closeDB();
	  
	  return startingBalance;
      
      }
       
    public String getDonationAlreadyMade(String donation_request_id){
	    DBConnection db = new DBConnection();
	    String query = "SELECT donation_already_made FROM donation_requests WHERE donation_request_id='" + donation_request_id + "'";
	    ResultSet rs = db.readQuery(query);
	    String totalDonation = "";
	    try{
		    if(rs.next()){
			    totalDonation = rs.getString("donation_already_made");
		    
		    }
	    }catch(SQLException sqle){
		    System.out.println(sqle.getMessage().toString());
	    }finally{
		  db.closeDB();
	    }
	      return totalDonation;
    }
    
    public String getNames(){
	  DBConnection db = new DBConnection();
	  String query = "SELECT first_name,last_name FROM  entitys WHERE entity_id='" + entity_id + "'";
	  ResultSet cRs = db.readQuery(query);
	  
	  String names = "";
	  
	  try{
	  
	      if(cRs.next()){
		  
		  names = cRs.getString(1) + " " + cRs.getString(2);
	      
	      }
	      
	  }catch(SQLException sqle){
	  
	      System.out.print(sqle.getMessage().toString());
	  
	  }catch(NullPointerException npe){
	      
	     // System.out.print("No user with such id");
	  
	  }
	  
	  db.closeDB();
	  
	  return names;    
    
      }
    
    }