package com.harambesa.settings;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException; 

import java.io.IOException;
import java.io.PrintWriter; 

import java.util.logging.Logger; 
import java.util.Enumeration;
import java.util.List;
import java.util.LinkedList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import org.json.simple.JSONObject;
import org.json.simple.JSONArray; 

import com.harambesa.DBConnection.DBConnection; 
import com.harambesa.gServices.HarambesaUtils;


			 
public class EmailNoficationsServlet extends HttpServlet { 
    PrintWriter out = null;	
    Logger log= null;
    JSONObject jsonData = null;
    Connection conn = null;

  	public void init() throws ServletException {
        log = Logger.getLogger(EmailNoficationsServlet.class.getName());
        conn = new DBConnection()._getConnection(); 
        jsonData = new JSONObject();
    } 

    public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException,IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");	
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("entity_id");
		out = response.getWriter();

		try{
			ResultSet results = entryExists(conn, id, "harambesa_settings", "entity_id");
			if(results!=null){  
				jsonData.put("connectionRequest",results.getString("connection_request"));	
				jsonData.put("connectionRequiresDonation",results.getString("connection_requires_donation"));	
				jsonData.put("connectionPlaceOffer",results.getString("connection_place_offer"));	
				jsonData.put("connectionSellsPoints",results.getString("connection_sells_points"));	
				jsonData.put("bidsOnMyPoints",results.getString("bids_on_my_points"));	
				jsonData.put("purchasesOnMyPoints",results.getString("purchases_on_my_points"));	
				jsonData.put("buyerAcceptsBid",results.getString("buyer_accepts_bid"));
				jsonData.put("receivedDonation",results.getString("received_donation"));	
				jsonData.put("ApplicationsOnMyOffer",results.getString("applications_on_my_offer"));	
				jsonData.put("myOfferApplicationDenied",results.getString("my_offer_application_denied"));	
				jsonData.put("myOfferApplicationAccepted",results.getString("my_offer_application_accepted")); 			
				
			}else{
				log.info("No record exists");
			}
			out.println(jsonData);
		}catch(SQLException sql){
			log.info(sql.getMessage());
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException,IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");	
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("entity_id");
		out = response.getWriter(); 
		String connectionRequest = request.getParameter("connectionRequest");
		String connectionRequiresDonation = request.getParameter("connectionRequiresDonation"); 
		String connectionPlaceOffer = request.getParameter("connectionPlaceOffer");
		String connectionSellsPoints = request.getParameter("connectionSellsPoints");
		String bidsOnMyPoints = request.getParameter("bidsOnMyPoints");
		String buyerAcceptsBid = request.getParameter("buyerAcceptsBid");
		String purchasesOnMyPoints = request.getParameter("purchasesOnMyPoints");  
		String receivedDonation = request.getParameter("receivedDonation"); 
		String applicationsOnMyOffer = request.getParameter("applicationsOnMyOffer"); 
		String myOfferApplicationDenied = request.getParameter("myOfferApplicationDenied"); 
		String myOfferApplicationAccepted = request.getParameter("myOfferApplicationAccepted"); 

		try{
			ResultSet results = entryExists(conn,id,"harambesa_settings","entity_id");
 // ******************** CREATE A NEW RECORD ************** 
			if( results==null){				 
				// sql 
				String sql ="insert into harambesa_settings("+
					"entity_id,connection_request,connection_requires_donation,connection_place_offer,"+
					"connection_sells_points,bids_on_my_points,buyer_accepts_bid,purchases_on_my_points, "+
					"received_donation,applications_on_my_offer,my_offer_application_denied,my_offer_application_accepted)"+
					"values(?,?,?,?,?,?,?,?,?,?,?,?)";
				// create prepared statement
				PreparedStatement pstmt = conn.prepareStatement(sql);
				// pass values 
				pstmt.setInt(1,Integer.parseInt(id));
				pstmt.setBoolean(2,Boolean.parseBoolean(connectionRequest));
				pstmt.setBoolean(3,Boolean.parseBoolean(connectionRequiresDonation));
				pstmt.setBoolean(4,Boolean.parseBoolean(connectionPlaceOffer));
				pstmt.setBoolean(5,Boolean.parseBoolean(connectionSellsPoints));
				pstmt.setBoolean(6,Boolean.parseBoolean(bidsOnMyPoints));
				pstmt.setBoolean(7,Boolean.parseBoolean(buyerAcceptsBid)); 
				pstmt.setBoolean(8,Boolean.parseBoolean(purchasesOnMyPoints));
				pstmt.setBoolean(9,Boolean.parseBoolean(receivedDonation));
				pstmt.setBoolean(10,Boolean.parseBoolean(applicationsOnMyOffer));
				pstmt.setBoolean(11,Boolean.parseBoolean(myOfferApplicationDenied)); 
				pstmt.setBoolean(12,Boolean.parseBoolean(myOfferApplicationAccepted)); 
				int rowAffected = pstmt.executeUpdate();
				log.info("rows affected"+rowAffected);
				if(rowAffected>0){
					jsonData.put("message", "success settings updated successfully"); 
				}else{
					jsonData.put("message", "Settings was not created");	
					response.setStatus(404);			
				}
			}else{
	// *************************************** UPDATE EXISTING RECORD **********************************
							 
				// sql 
				String sql ="UPDATE harambesa_settings SET "+
					"connection_request = ?,connection_requires_donation = ?,"+
					"connection_place_offer = ?, connection_sells_points = ?, bids_on_my_points = ?,"+
					"buyer_accepts_bid = ?, purchases_on_my_points = ?, received_donation = ?, "+
					"applications_on_my_offer = ?, my_offer_application_denied = ?, my_offer_application_accepted = ?"+
					"WHERE entity_id = ?";
				// create prepared statement
				PreparedStatement pstmt = conn.prepareStatement(sql);
				// pass values  
				pstmt.setBoolean(1,Boolean.parseBoolean(connectionRequest));
				pstmt.setBoolean(2,Boolean.parseBoolean(connectionRequiresDonation));
				pstmt.setBoolean(3,Boolean.parseBoolean(connectionPlaceOffer));
				pstmt.setBoolean(4,Boolean.parseBoolean(connectionSellsPoints));
				pstmt.setBoolean(5,Boolean.parseBoolean(bidsOnMyPoints));
				pstmt.setBoolean(6,Boolean.parseBoolean(buyerAcceptsBid)); 
				pstmt.setBoolean(7,Boolean.parseBoolean(purchasesOnMyPoints));
				pstmt.setBoolean(8,Boolean.parseBoolean(receivedDonation));
				pstmt.setBoolean(9,Boolean.parseBoolean(applicationsOnMyOffer));
				pstmt.setBoolean(10,Boolean.parseBoolean(myOfferApplicationDenied)); 
				pstmt.setBoolean(11,Boolean.parseBoolean(myOfferApplicationAccepted));  
				pstmt.setInt(12,Integer.parseInt(id)); 
				int rowAffected = pstmt.executeUpdate();
				log.info("rows affected"+rowAffected);
				if(rowAffected>0){
					jsonData.put("message", "success settings updated successfully"); 
				}else{
					jsonData.put("message", "Settings was not created");	
					response.setStatus(404);			
				}
			}
			results.close(); 
		}catch(SQLException sql){
			log.info("post rong");
			log.severe(sql.getMessage());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An internal server error occured, Please contact your administrator for assistance");
		}

		
		out.print(jsonData);
	} 

	/**
	*
	*	Check if entry exists in the database
	*
	*	@param connection 	the database connection object
	*	@param id 			the value to use to look up entry
	*	@param table		the table to lookup the entry in 
	*	@param column		the table column to lookup the value in
	*
	*
	*	@return ResultSet	Returns a resultset with cursor at the first item
	*/
	public ResultSet entryExists(Connection conn, String id, String table, String column) throws SQLException{
		ResultSet results=null;
		// sql string
		String sql = "select * from "+table+" where "+column+"=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1,Integer.parseInt(id));
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()){ 
			results=rs;
		}else{
			log.info("No record exists");
		}

		return results;
	}

	/**
	*
	*	change character choices from user to boolean values for db
	*
	*	@param string 	takes a string value
	*	@return boolean 	returns the boolen equivalent
	*/
	public boolean returnBoolean(String choice){
		if(choice.equals("f")){
			return true;
		}else{
			return false;
		}
	}

	/**
	*	Returns
	*
	*	@param Connection connection object
	*	@param id 	row to fetch
	*	@param column	the column to fetch
	*
	*/
	public boolean isEmailSentToUser(Connection conn, String id, String column) throws SQLException{
		boolean flag = true;
		
		String sql = "SELECT " + column + " FROM harambesa_settings WHERE entity_id=?";
		
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1,Integer.parseInt(id));
			ResultSet rs = st.executeQuery();
			if(rs.next()){
				flag = false;
			}
		return flag;
	}
}