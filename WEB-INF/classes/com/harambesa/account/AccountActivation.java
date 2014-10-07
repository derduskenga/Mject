package com.harambesa.account; 

import com.harambesa.DBConnection.DBConnection;
import com.harambesa.gServices.HarambesaUtils;

import java.sql.*;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.logging.Logger;
import java.lang.NumberFormatException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse; 

public class AccountActivation extends HttpServlet{

		HttpServletRequest request=null; 
		HttpServletResponse response=null;
		PrintWriter out = null;	
		Logger log= null;
  
		public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException {
			
				this.request=req;
				this.response=res;
				doPost(req, res);
		}
	
		public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{  
					out = response.getWriter();	
					this.request=request;
					this.response=response;
					log=Logger.getLogger(AccountActivation.class.getName());
					response.setContentType("text/html; charset=UTF-8");
					checkActivationTokens();
		}
		
		public void checkActivationTokens(){
					String id  = request.getParameter("id");
					String token = request.getParameter("token");
					HttpSession sess=request.getSession();
					try{
					int tkn_id=Integer.parseInt(id);
					DBConnection db=new DBConnection(); 
					Connection con= db._getConnection();			
					String sql = "SELECT activation_tokens.activation_token_id, activation_tokens.activation_token_entity_id, activation_tokens.activation_token, ";
								sql+=" activation_tokens.created_at, activation_tokens.status, entitys.is_active";
								sql+=" FROM activation_tokens ";
								sql+=" INNER JOIN entitys ";
								sql+=" ON activation_token_entity_id=entity_id";
								sql+=" WHERE activation_token_id = ? ";
								sql+=" AND activation_token= ?";

					PreparedStatement preparedStatement = con.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
					preparedStatement.setInt(1,tkn_id);
					preparedStatement.setString(2, token);
					
					
					ResultSet rslt= preparedStatement.executeQuery();
					if(rslt!=null){
						if(rslt.next()){
							//that row exists, now check if that user is already activated
							if(rslt.getBoolean(6)){
							//user already active 
								sess.setAttribute("conf_title","Registration Confirmation!" );
								sess.setAttribute("conf_msg","Account Already Active. Click <a href='../login'>Here</a> to login");
								db.closeDB();
							}else{
							//user not active. now, set status to active and token status to true
							//meaning it has already been used
								rslt.updateBoolean(5, true);
								rslt.updateRow();
										String sq = "UPDATE entitys  SET is_active='1' WHERE entity_id=?";
										PreparedStatement prs = con.prepareStatement(sq);
										log.info(Integer.toString(rslt.getInt(2)));
										prs.setInt(1,rslt.getInt(2));
										prs.executeUpdate();
										
								sess.setAttribute("conf_title","Registration Successful!" );
								sess.setAttribute("conf_msg","Confirmation Successfull. Click <a href='../login'>Here</a> to login");
								db.closeDB();
							}
							response.sendRedirect("../account-activation");
						}else{
							//that user token combination does not exist
							response.sendRedirect("../error");
						}
					}else{
						//here, no rs was returned. Something must have gone wrong
						log.severe(db.getLastErrorMsg());
					}
					}catch(NumberFormatException ex){
						log.severe("Error number format "+id);			
						try{response.sendRedirect("../error");}catch(IOException ioe){log.severe("Error redirecting account activation to error page");}
					}catch(IOException  ioex){
						log.severe(HarambesaUtils.getStackTrace(ioex));			
						try{response.sendRedirect("../error");}catch(IOException ioe){log.severe("Error redirecting account activation to error page");}
					}catch(SQLException  sqle){
						log.severe(HarambesaUtils.getStackTrace(sqle));			
						try{response.sendRedirect("../error");}catch(IOException ioe){log.severe("Error redirecting account activation to error page");}
					}
		}
}
