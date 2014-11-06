package com.harambesa.socialmarket; 

import com.harambesa.DBConnection.DBConnection;
import com.harambesa.DBConnection.GlobalDresser;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse; 
import javax.servlet.http.HttpSession;

import java.sql.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.sql.SQLException;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import com.harambesa.gServices.HarambesaUtils;

import java.util.Enumeration;

public class SocialMarket extends HttpServlet{

	private enum points_operation{
		donation,
		sale_purchase;
	}
	
	private static Logger logger = Logger.getLogger(SocialMarket.class.getName());
	
	HttpServletRequest request=null; 
	HttpServletResponse response=null;
	String entity_id=null;
	PrintWriter out = null;	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException{
			this.request=request;
			this.response=response;
			this.out = response.getWriter();	
			doPost( request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
			this.request=request;
			this.response=response;
			this.out = response.getWriter();
			this.response.setContentType("application/json");
			this.response.setCharacterEncoding("UTF-8");
			processSocialMarketRequest();
	}
	
	public void putMyPointsOnSale(){
					HttpSession session = request.getSession();
					String points = request.getParameter("pnts");
					String price_per_point = request.getParameter("ppp");
					if(points != null && price_per_point != null){
					DBConnection db = new DBConnection();
					try{
						int pnts = Integer.parseInt(points);
						Double ppp = Double.parseDouble(price_per_point);
						int ent_id = Integer.parseInt((String)session.getAttribute("entity_id"));
						int available_points = checkPointsToSell();
						if(pnts>available_points){
								giveErrorFeedBack("Sorry, you cannot put "+pnts+" points on sale. You have "+available_points+" points available to put on sale.");
								return;
						}
						String sql = "INSERT INTO points_sales( ";
									sql+=" seller_entity_id, no_of_points_to_sell, price_per_point) ";
									sql+=" VALUES (?, ?, ?) RETURNING points_sale_id, seller_entity_id";
						Connection con = db._getConnection();					
						PreparedStatement preparedStatement = con.prepareStatement(sql);
						preparedStatement.setInt(1, ent_id);
						preparedStatement.setInt(2, pnts);
						preparedStatement.setDouble(3, ppp);
						ResultSet rs = preparedStatement.executeQuery();
						if(rs != null){
								if(rs.next()){
										giveSuccessFeedBack("Congratulations, you have successfully put "+pnts+" points on sale at a rate of Ksh. "+ppp+" per point.", rs.getInt(1), rs.getInt(2));
								}else{
										//the query executed well but no resultset was returned
										//a very unlikely scenario.  logging just in case and for the sake
										logger.severe("Saving points sales. But no resultset (sid) was returned");									
								}
						}else{
							giveErrorFeedBack("Sorry, something went wrong. We could not complete your request.");
						}
					}catch(Exception e){
						//there must have been an error converting ppp and points to Integer
						//Indicate this to the user.
						logger.severe(HarambesaUtils.getStackTrace(e));
						giveErrorFeedBack("Sorry, we could not complete your request. Please try again.");
					}finally{
						db.closeDB();
					}
					}else{
							logger.severe("Points & PPP not set.");
							giveErrorFeedBack("Sorry, your request does not appear to be valid.");
					}
	}
	
	private int checkPointsToSell(){
				int points =0;
				HttpSession session = request.getSession();
				GlobalDresser gd = new GlobalDresser((String)session.getAttribute("entity_id"));
				points=gd.getHarambesaPoints();
				logger.info("Points:"+points);
				DBConnection db = new DBConnection();
				
				try{
						int ent_id = Integer.parseInt((String)session.getAttribute("entity_id"));
						String	sql ="SELECT (points_sales.no_of_points_to_sell-points_sales.points_sold) as diff from points_sales";
									sql+=" WHERE seller_entity_id =? AND is_closed=FALSE";
									sql+=" ORDER BY seller_entity_id";
						Connection con = db._getConnection();					
						PreparedStatement preparedStatement = con.prepareStatement(sql);
						preparedStatement.setInt(1, ent_id);
						ResultSet rs = preparedStatement.executeQuery();
						if(rs != null){
								if(rs.next()){
										do{points -= rs.getInt(1);}while(rs.next());
								}else{
										
								}
						}else{
										points=-1;
						}
					}catch(Exception e){
						logger.severe(HarambesaUtils.getStackTrace(e));
						points=-2;
					}finally{
						db.closeDB();
					}
					return points;
	}
	
	public void processSocialMarketRequest() throws IOException{
			try{
				HttpSession session = request.getSession();
				entity_id=(String)session.getAttribute("entity_id");
				String tag= this.request.getParameter("tag");
				if(entity_id==null){
					//not logged in
					sendRedir();
				}else{
				if(tag==null || tag.trim().equals("")){
					String error = "Tag could not be identified."+tag;
					logger.severe(error);
					giveErrorFeedBack("Sorry, we could not process your request.");
				}else{
							if(tag.equals("fetchall")){
								fetchAllPointsOnSale();
							}else if(tag.equals("mypointsonsale")){
								fetchMyPointsOnSale(entity_id);
							}else if(tag.equals("sellmypoints")){
								putMyPointsOnSale();
							}else if(tag.equals("placebidonpoints")){
								placeMyBid();
							}else if(tag.equals("fetchnumberofbids")){
								getNumberOfBids();
							}else if(tag.equals("getallbidsforspecps")){
								getBidsForPointsSale();
							}else if(tag.equals("acceptspecbid")){
								acceptSpecBid();
							}else if(tag.equals("buypoints")){
								buyPoints();
							}else if(tag.equals("cancelmybid")){
								cancelMyBid();
							}else if(tag.equals("getmybalance")){
								getMyBalance();
							}else if(tag.equals("declinebid")){
								declinebid();
							}
				}
			}
			}catch(NullPointerException ex){
					logger.severe("Error: "+HarambesaUtils.getStackTrace(ex));
					giveErrorFeedBack("Sorry, something went wrong while processing your request.");
			}
	}
	
	private void getMyBalance(){
				GlobalDresser gd = new GlobalDresser(entity_id);
				Double harambesBal = gd.getHarambesaBalance();
				logger.info("User = "+entity_id+" Harambesa Bal = "+harambesBal);
				if(harambesBal != null){
						logger.info("At not null harambesa balance.");
						giveSuccessFeedBack(harambesBal);
				}else{
						giveErrorFeedBack("Sorry. Could not fetch your harambesa balance. Please try again.");
						logger.severe("Could not fetch balance for user = "+entity_id);
				}
				logger.info("At end of get harambesa balance");
	}
	
	public void cancelMyBid(){
				String bid = request.getParameter("bid");
				if(bid.equals("") || bid == null){
						giveErrorFeedBack("Sorry, your request could not be processed.");
				}else{
								DBConnection db = new DBConnection();
								Connection con = db._getConnection();
						try{
								String sql = "UPDATE points_sale_bids";
										   sql+=" SET deleted = TRUE WHERE points_sale_id =?"; 
										   sql+=" AND bidder_entity_id =?";
										   sql+=" AND bid_overidden = FALSE";
								logger.info(sql);
								PreparedStatement ps = con.prepareStatement(sql);
								ps.setInt(1, Integer.parseInt(bid));
								ps.setInt(2, Integer.parseInt(entity_id));
								int rs = ps.executeUpdate();
								if(rs >= 0){
											logger.info("Num rows affected"+rs);																						
											if(rs==1){
												giveSuccessFeedBack("You have successfully deleted your bid.");
											}else{
												giveSuccessFeedBack("Sorry, you do not have any bid yet for these points.");
											}
								}else{
											logger.severe("ERROR: RS is null while trying to delete bid for user = "+entity_id +" for bid  ="+bid);
											giveErrorFeedBack("Sorry, An error occured. This bid could not be deleted.");
								}
						}catch(NumberFormatException nfe){
											logger.severe("ERROR: NFE while trying to delete bid for user = "+entity_id +" for bid  ="+bid);
											logger.severe(HarambesaUtils.getStackTrace(nfe));
						}catch(SQLException sqle){
											logger.severe("ERROR: SQLE while trying to delete bid for user = "+entity_id +" for bid  ="+bid);
											logger.severe(HarambesaUtils.getStackTrace(sqle));
						}finally{
											db.closeDB();
						}
				}
	}
	
	public void buyPoints(){
			String psid = request.getParameter("psid");
			String commit = request.getParameter("commit");
			DBConnection db = new DBConnection();
			Connection con = db._getConnection();
			try{
				int ps = Integer.parseInt(psid);		
				String  sql1 = "SELECT points_sale_id, seller_entity_id, (points_sales.no_of_points_to_sell-points_sales.points_sold)";
							sql1 += " as no_of_points_to_sell, price_per_point,"; 
							sql1+=" is_closed, details, points_sale_currency_id";
							sql1+=" FROM points_sales WHERE points_sale_id = ?";
							
				String  sql2 = "INSERT INTO points_transactions(";
							sql2+=" points_transactions_date, points_transactions_entity_id,";
							sql2+=" number_of_points, points_transactions_type,  buyer_entity_id, points_sale_id)";
							sql2+=" VALUES (now(), ?, ?, CAST(? AS points_operation),?, ?)";
				if(commit ==null){
				PreparedStatement ps1 = con.prepareStatement(sql1);
				ps1.setInt(1, ps);
				ResultSet rs = ps1.executeQuery();
				if(rs != null){
						if(rs.next()){
							giveSuccessFeedBack("You are about to buy "+rs.getInt(3)+" points @ "+rs.getDouble(4)+" (KES) per point. Total:"+Math.round(((rs.getInt(3)*rs.getDouble(4)) * 100.0) / 100.0)+" (KES)", Math.round(((rs.getInt(3)*rs.getDouble(4)) * 100.0) / 100.0));
						}else{
							giveErrorFeedBack("Sorry, your request could not be completed.");
						}
				}else{
						logger.severe("error in sql1 buy points user id = "+entity_id);
						giveErrorFeedBack("Sorry, transaction could not be completed.");
				}
				}else{
					String  sql4 = "SELECT seller_entity_id,(points_sales.no_of_points_to_sell-points_sales.points_sold)"; 
								sql4+=" as no_of_points_to_sell FROM points_sales WHERE points_sale_id = ?";
								PreparedStatement ps2 = con.prepareStatement(sql4);
								ps2.setInt(1, ps);
								//
								ResultSet rs = ps2.executeQuery();
								if(rs != null){
									if(rs.next()){
										 int sellerId = rs.getInt(1);
										 int nop_to_sell = rs.getInt(2);
										 PreparedStatement ps3 = con.prepareStatement(sql2);
										 ps3.setInt(1, sellerId);
										 ps3.setInt(2, nop_to_sell);
										 ps3.setString(3, points_operation.sale_purchase.toString());
										 ps3.setInt(4, Integer.parseInt(entity_id));
										 ps3.setInt(5, ps);
											int numRows = ps3.executeUpdate();
											if(numRows>0){
												giveSuccessFeedBack("Transaction completed successfully.");
											}else{
												//probably roll back the previous insertion
												logger.info("Error completing transaction in points purchase for user = "+entity_id+". Consider rollback.");
												giveErrorFeedBack("Sorry, an error occured while completing transaction. Please try again.");
											}															
									}else{
										giveErrorFeedBack("Sorry, your request could not be completed.");
									}
							}else{
									logger.severe("error in sql4 buy points user id = "+entity_id);
									giveErrorFeedBack("Sorry, transaction could not be completed.");
							}
				}
		}catch(NumberFormatException nfe){
				logger.severe("Error converting string to number in buying points for user id = "+entity_id);
				giveErrorFeedBack("Sorry, this transaction could not be completed.");
				logger.info(HarambesaUtils.getStackTrace(nfe));
		}catch(SQLException sqle){
				logger.severe("An sql Exception occured while initiating buy points. User = "+entity_id);
				logger.severe("Error is: "+HarambesaUtils.getStackTrace(sqle));
				logger.severe("Last sql error msg is: "+db.getLastErrorMsg());
				giveErrorFeedBack("Sorry, this transaction could not be completed.");
		}finally{
				db.closeDB();
		}
	}
	public void fetchMyPointsOnSale(String entity_id){
			String 		sql = "SELECT points_sales.points_sale_id, points_sales.seller_entity_id, ";
							sql+=" points_sales.no_of_points_to_sell - points_sales.points_sold, points_sales.price_per_point, ";
							sql+=" points_sales.is_closed, ";
							sql+=" entitys.first_name, entitys.last_name, entitys.user_name, "; 
							sql+=" entitys.profile_pic_path, points_sales.points_sold";
							sql+=" FROM points_sales";
							sql+=" LEFT JOIN entitys";
							sql+=" ON entitys.entity_id = points_sales.seller_entity_id";
							sql+=" WHERE points_sales.seller_entity_id ="+entity_id;
							sql+=" AND (points_sales.points_sold < points_sales.no_of_points_to_sell)";
							sql+=" AND points_sales.is_closed=FALSE";
				
				DBConnection db = new DBConnection();
				try{
						ResultSet rs = db.readQuery(sql); 
						if(rs != null){
								if(rs.next()){
										JSONObject on_sale = new JSONObject();
										JSONArray jArray = new JSONArray();
										int total=0;
										do{
												JSONObject obj=new JSONObject();
												obj.put("psid",rs.getString(1));
												obj.put("sid",rs.getString(2));
												obj.put("nop",rs.getString(3));
												obj.put("ppp",rs.getString(4));
												obj.put("pic",rs.getString(9));
												obj.put("name", rs.getString(6)+" "+rs.getString(7));
												obj.put("sold", rs.getString(10));
												jArray.add(obj);
												on_sale.put("psale_details",jArray);
												total++;
										}while(rs.next());
										on_sale.put("total",total);
										on_sale.put("success",1);
										out.println(on_sale);								
							}else{
									JSONObject on_sale = new JSONObject();
									on_sale.put("total",0);
									on_sale.put("success",1);
									on_sale.put("message","You do not have any points on sale at the moment.");
									out.println(on_sale);								
							}
						}else{
								giveErrorFeedBack("Sorry, an error occured while processing your request.");
						}
					}catch(Exception ex){
						logger.severe(HarambesaUtils.getStackTrace(ex));
						giveErrorFeedBack("Sorry, an error occured while processing your request. We are working hard to fix this.");
					}finally{
						db.closeDB();
					}
	}
	
	public void getNumberOfPoints(int ent_id){
			//get the number of points for the current user for the current period
			//	if(ent_id){
			//			String sql = "";				
			//	}else{
			//	}
	}
	
	public void getNumberOfBids(){
					//enumerateParams();
					String [] sids = request.getParameterValues("sids[]");
					if(sids==null){
						giveErrorFeedBack("Sorry, could not load bids for your points on sale.");
						logger.info("Could not load bids for user id "+entity_id);
					}else{
						if(sids.length<1){
								//this user does not have any bids
								//giveNoDataFound();
								//very unlikely as no such req will be made when no bids available
								logger.severe("Length of sids is zero.");
						}else{
								logger.info("Length of sids greater than 0.");
								getBidCountForAllPointSale(sids);
						}					
					}
	}
	
	private void getBidCountForAllPointSale(String [] psids){
			String 	sql ="SELECT COUNT(*)";
						sql+=" FROM(";
						sql+=" SELECT DISTINCT ON (bidder_entity_id) bidder_entity_id";
						sql+=" FROM points_sale_bids";
						sql+=" WHERE points_sale_id = ?"; 
						sql+=" AND points_sale_bids.bid_overidden = FALSE";
						sql+=" AND points_sale_bids.is_declined = FALSE) p";
			int total = 0;
			JSONObject obj = new JSONObject();
			JSONArray jArray = new JSONArray();
			//make db connection
			DBConnection db = new DBConnection();
			//get the established connection
			Connection con = db._getConnection();
			try{
			//create a prepared statement
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			//get prepared statement parameters
						for(int i=0; i<psids.length; i++){
									preparedStatement.setInt(1, Integer.parseInt(psids[i]));	
									ResultSet rs = preparedStatement.executeQuery();
									JSONObject psidCountPair = new JSONObject();
							if(rs==null){
									//something went wrong and the sql did not execute properly
									psidCountPair.put("key",psids[i]);
									psidCountPair.put("val", 0);
							}else{
									//this sql executed properly
								if(rs.next()){
									//if it executed properly it must return at least a row, of course just a row
									//even if the count is zero. this means this is the only path of execution at this point.
									psidCountPair.put("key",psids[i]);
									psidCountPair.put("val", rs.getInt(1));
								}
							}
									jArray.add(psidCountPair);
						}
						obj.put("success",1);
						obj.put("bids",jArray);
						out.println(obj);
						out.close();
			}catch(NumberFormatException ex){
						logger.severe("Error converting number in fetching bid count for user "+entity_id);
						logger.severe("Error is: "+HarambesaUtils.getStackTrace(ex));
						giveErrorFeedBack("Error loading bid counts");
			}catch(SQLException sqle){
						logger.severe("An sql Exception occured while fetching bid count. User = "+entity_id);
						logger.severe("Error is: "+HarambesaUtils.getStackTrace(sqle));
						logger.severe("Last sql error msg is: "+db.getLastErrorMsg());
						giveErrorFeedBack("Error loading bid counts");
			}finally{
						db.closeDB();
			}
	}
	
	public void enumerateParams(){		
			logger.info("Enumerating param names....");
			Enumeration<String> parameterNames = request.getParameterNames();	 
			while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			logger.info(paramName);
			logger.info("\n");
	
			String[] paramValues = request.getParameterValues(paramName);
			for (int i = 0; i < paramValues.length; i++) {
				String paramValue = paramValues[i];
				logger.info("\t" + paramValue);
				logger.info("\n");
			}
	        }
	        out.close();
	}
	
	public void getBidsForPointsSale(){
					HttpSession session = request.getSession();
					String psid = request.getParameter("psid");
					psid = psid.trim();
					if(psid==null || psid.equals("")){
						giveErrorFeedBack("Sorry, your request could not be processed.");
					}else{
						DBConnection db = new DBConnection();
						Connection con = db._getConnection();
					try{
						String 	sql = "SELECT DISTINCT ON (bidder_entity_id) bidder_entity_id, points_sale_bid_id, points_sale_id, no_of_points_to_buy, ";
									sql+=" round (bid_price_per_point::numeric, 2) , accepted, first_name, last_name, profile_pic_path";
									sql+=" FROM points_sale_bids";
									sql+=" INNER JOIN entitys ON";
									sql+=" points_sale_bids.bidder_entity_id = entitys.entity_id";
									sql+=" WHERE points_sale_id = ?";
									sql+=" AND points_sale_bids.is_declined = FALSE";
									sql+=" AND points_sale_bids.bid_overidden = FALSE";
									sql+=" ORDER BY bidder_entity_id, points_sale_bid_id DESC";
						
						PreparedStatement ps = con.prepareStatement(sql);
						ps.setInt(1, Integer.parseInt(psid));
						ResultSet rs = ps.executeQuery();
						if(rs != null){
							if(rs.next()){
									JSONObject obj = new JSONObject();
									JSONArray jArray = new JSONArray();
									int total = 0;
									obj.put("psid",rs.getInt(3));
									do{
										total++;
										JSONObject bids = new JSONObject();
										bids.put("bid",rs.getInt(1));
										bids.put("psbid", rs.getInt(2));
										bids.put("nop", rs.getInt(4));
										bids.put("ppp",rs.getDouble(5));
										bids.put("acc", rs.getBoolean(6));
										bids.put("bnm", rs.getString(7)+" "+rs.getString(8));
										bids.put("pic_path", rs.getString(9));
										jArray.add(bids);
									}while(rs.next());
									obj.put("bids", jArray);
									obj.put("total", total);
									obj.put("success", 1);
									out.println(obj);
									out.close();
							}else{
									giveSuccessFeedBack("No bids on this points sale.");
							}
						}else{
								giveErrorFeedBack("Error loading bids");
								logger.severe("RS was null fetching for user id= "+entity_id);					
						}
						}catch(NumberFormatException nfe){
								logger.severe("Error converting string to number for user id = "+entity_id);
								giveErrorFeedBack("Error fetching bids.");
						}catch(SQLException sqle){
								logger.severe("An sql Exception occured while fetching bid count. User = "+entity_id);
								logger.severe("Error is: "+HarambesaUtils.getStackTrace(sqle));
								logger.severe("Last sql error msg is: "+db.getLastErrorMsg());
								giveErrorFeedBack("Error loading bid counts");
						}finally{
								db.closeDB();
						}
					}
	}
	public void declinebid(){
				try{
					HttpSession session = request.getSession();
					String bidid = request.getParameter("bidid");
					bidid = bidid.trim();
					if(bidid==null || bidid.equals("")){
						giveErrorFeedBack("Sorry, something went wrong.");
					}else{
						DBConnection db = new DBConnection();
						Connection con = db._getConnection();
					try{
						String 	sql = "UPDATE points_sale_bids SET is_declined = TRUE";
									sql+=" WHERE (points_sale_bid_id=? AND is_declined=FALSE AND points_sale_id = ";
									sql+=" (SELECT points_sale_id FROM points_sales WHERE seller_entity_id = ? ";
									sql+=" AND points_sale_id= (SELECT points_sale_id FROM points_sale_bids WHERE points_sale_bid_id = ?)))";
						logger.info("DECLINE BID SQL: "+sql);
						PreparedStatement ps = con.prepareStatement(sql);
						ps.setInt(1, Integer.parseInt(bidid));
						ps.setInt(2, Integer.parseInt(entity_id));
						ps.setInt(3, Integer.parseInt(bidid));
						int res = ps.executeUpdate();
						logger.info("ROWS AFFECTED:"+res);
							if(res > 0){
										giveSuccessFeedBack("You have successfully declined this bid.");
							}else{
										giveErrorFeedBack("No bid was found.");
							}
						}catch(NumberFormatException nfe){
								logger.severe("Error converting string to number for user id = "+entity_id);
								giveErrorFeedBack("Sorry, an error occured.");
						}catch(SQLException sqle){
								logger.severe("An sql Exception occured declining bid User = "+entity_id);
								logger.severe("Error is: "+HarambesaUtils.getStackTrace(sqle));
								logger.severe("Last sql error msg is: "+db.getLastErrorMsg());
								giveErrorFeedBack("Error loading bid counts");
						}finally{
								try{db.closeDB();}catch(Exception e){}
						}
					}
				}catch(Exception e){
					logger.severe("ERROR:"+HarambesaUtils.getStackTrace(e));
					giveErrorFeedBack("Sorry. Something went wrong. Please try again.");
				}
	}
	
	public void placeMyBid(){
					HttpSession session = request.getSession();
					String points = request.getParameter("pnts");
					String psid = request.getParameter("psid");
					String price_per_point = request.getParameter("ppp");
					try{
					int pnts = Integer.parseInt(points);
					Double ppp = Double.parseDouble(price_per_point);
					int ent_id = Integer.parseInt(entity_id);
					int sid =   Integer.parseInt(psid);
					if(points != null && price_per_point != null && psid != null){
					if(hasEnoughMoney(pnts*ppp)){
					DBConnection db = new DBConnection();
					try{
						String sql = "INSERT INTO points_sale_bids(points_sale_id, bidder_entity_id, no_of_points_to_buy, bid_price_per_point)";
					    sql+="VALUES (?, ?, ?, ?) RETURNING points_sale_bid_id";
						Connection con = db._getConnection();
						PreparedStatement preparedStatement = con.prepareStatement(sql);
						preparedStatement.setInt(1, sid);
						preparedStatement.setInt(2, ent_id);
						preparedStatement.setDouble(3, pnts);
						preparedStatement.setDouble(4, ppp);
						ResultSet rs = preparedStatement.executeQuery();
						if(rs != null){
								if(rs.next()){
										giveSuccessFeedBack("Congratulations, you have successfully bid "+pnts+" points at a rate of Ksh. "+ppp+" per point.");
								}else{
										//the query executed well but no resultset was returned
										//a very unlikely scenario.  logging just in case and for the sake
										logger.severe("Saving points sales. But no resultset (sid) was returned");									
								}
						}else{
							giveErrorFeedBack("Sorry, something went wrong. We could not complete your request.");
						}
					}catch(SQLException sqle){
							if(sqle.getMessage().startsWith("ERROR: You cannot bid for more than")){
								giveErrorFeedBack("You cannot bid for more points than those on sale. Please lower the number of points you are bidding for.");
							}else{
								giveErrorFeedBack("Sorry, we could not process your request. Please try again.");
								logger.severe("Code:"+sqle.getErrorCode());
								logger.severe("ERROR DETAIL:"+HarambesaUtils.getStackTrace(sqle));
							}
					}catch(Exception e){
					//there must have been an error converting ppp and points to Integer
					//Indicate this to the user.
							logger.severe(HarambesaUtils.getStackTrace(e));
							giveErrorFeedBack("Sorry, we could not process your request. Please try again.");
					}finally{
						try{db.closeDB();}catch(Exception e){}
					}
					}else{
							//this user does not have enough money in their account.
							//give a response to that effect.
							giveErrorFeedBack("You do not have enough money in your account to make this bid worth KES. "+ppp*pnts);
					}
					}else{
							logger.severe("Points & PPP not set.");
							giveErrorFeedBack("Sorry, your request does not appear to be valid.");
					}
				}catch(Exception e){
						logger.severe("Error converting points to int user "+entity_id+":  ERROR: "+HarambesaUtils.getStackTrace(e));
				}
	}
	
	public void fetchAllPointsOnSale(){
						String	sql="SELECT * FROM";
									sql+=" (SELECT points_sales.points_sale_id, points_sales.seller_entity_id,";
									sql+=" (points_sales.no_of_points_to_sell-points_sales.points_sold) as diff, ";
									sql+=" points_sales.price_per_point, points_sales.is_closed,";
									sql+=" entitys.first_name, entitys.last_name, entitys.user_name, ";
									sql+=" entitys.profile_pic_path";
									sql+=" FROM points_sales";
									sql+=" LEFT JOIN entitys";
									sql+=" ON entitys.entity_id = points_sales.seller_entity_id";
									sql+=" WHERE points_sales.seller_entity_id !="+entity_id;
									sql+=" AND (points_sales.points_sold < points_sales.no_of_points_to_sell)";
									sql+=" AND points_sales.is_closed=FALSE) p";
									sql+=" LEFT JOIN";
									sql+=" (";
									sql+=" SELECT DISTINCT ON (bidder_entity_id) bidder_entity_id, points_sale_bid_id, points_sale_id, no_of_points_to_buy";
									sql+=" as noptb,";
									sql+=" round (bid_price_per_point::numeric, 2) as bid_price_per_point, accepted, deleted";
									sql+=" FROM points_sale_bids";
									sql+=" INNER JOIN entitys ON";
									sql+=" points_sale_bids.bidder_entity_id = entitys.entity_id";
									sql+=" WHERE points_sale_bids.bidder_entity_id ="+entity_id;
									sql+=" AND points_sale_bids.deleted=FALSE";
									sql+=" ORDER BY bidder_entity_id, points_sale_bid_id DESC";
									sql+=" ) k";
									sql+=" ON p.points_sale_id = k.points_sale_id";
					DBConnection db = new DBConnection();
				try{
						ResultSet rs = db.readQuery(sql); 
						if(rs != null){
								if(rs.next()){
										JSONObject on_sale = new JSONObject();
										JSONArray jArray = new JSONArray();
										int total=0;
										do{
												JSONObject obj=new JSONObject();
												obj.put("psid",rs.getString(1));
												obj.put("sid",rs.getString(2));
												obj.put("nop",rs.getString(3));
												obj.put("ppp",rs.getString(4));
												obj.put("pic",rs.getString(9));
												obj.put("name", rs.getString(6)+" "+rs.getString(7));
												obj.put("bidder_entity_id", rs.getInt(10));
												boolean accepted = rs.getBoolean(15);
												logger.info("int b4:"+rs.getInt(10));
												
												if(rs.getInt(10)>0){
													obj.put("placed_bid",1);
													obj.put("b_acc",rs.getBoolean(15));
													obj.put("bnop",rs.getInt(13));
													obj.put("bppp", rs.getDouble(14));
													obj.put("psbid", rs.getInt(11));
												}else{
													obj.put("placed_bid",0);
												}
												
												jArray.add(obj);
												on_sale.put("psale_details",jArray);
												total++;
										}while(rs.next());
										on_sale.put("total",total);
										on_sale.put("success",1);
										out.println(on_sale);
										out.close();
							}else{
								//there are no stuff in the market
								giveSuccessFeedBack("There are no points on sale at the moment. Please come back later.");							
							}
						}else{
								logger.severe("RS is null while fetching all points on sale.");
								giveErrorFeedBack("Sorry, an error occured while processing your request.");
					}
				}catch(Exception ex){
						logger.severe("At logged..."+HarambesaUtils.getStackTrace(ex));
						giveErrorFeedBack("Sorry, an error occured while processing your request. We are working hard to fix this.");
				}finally{
						db.closeDB();
				}
	}
	
	public void acceptSpecBid(){
			logger.info("At accepting bid.");
			DBConnection db = new DBConnection();
			Connection con = db._getConnection();
			try{
			String bidtoaccept = request.getParameter("bid");
			String sql ="UPDATE points_sale_bids";
						sql+=" SET accepted = TRUE";
						sql+=" WHERE points_sale_bid_id=(";
						sql+=" SELECT points_sale_bid_id";
						sql+=" FROM points_sale_bids";
						sql+=" INNER JOIN points_sales ON";
						sql+=" points_sale_bids.points_sale_id = points_sales.points_sale_id";
						sql+=" WHERE points_sale_bid_id = ? AND points_sales.seller_entity_id = ?)";			
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(bidtoaccept));
			ps.setInt(2, Integer.parseInt(entity_id));
			
			int numRows = ps.executeUpdate();
			if(numRows == 1){
					giveSuccessFeedBack("You have successfully accepted bid.");
			}else{
					giveErrorFeedBack("Sorry, an error occured. Could not complete request");
			}
			}catch(NumberFormatException nfe){
					logger.severe("Error converting str to int in bid acceptance for user = "+entity_id);
					logger.severe(HarambesaUtils.getStackTrace(nfe));
					giveErrorFeedBack("Sorry, something went wrong.");
			}catch(SQLException sqle){
					logger.severe("SQL error occured in bid acceptance for user = "+entity_id);
					logger.severe(HarambesaUtils.getStackTrace(sqle));
					giveErrorFeedBack("Sorry, something went wrong.");
			}finally{
					db.closeDB();
			}
	}
	
	private boolean hasEnoughMoney(Double amt){
			GlobalDresser gd = new GlobalDresser(entity_id);
			Double harambesBal = gd.getHarambesaBalance();	
			if(harambesBal >= amt)
				return true;
			return false;
	}
	
	public void sendRedir(){
				JSONObject obj  = new JSONObject();
				obj.put("success", 0);
				obj.put("redir", "login");
				obj.put("message", "Your session has expired, please login to continue");
				out.println(obj);
				out.close();
	}
	
	public void giveErrorFeedBack(String message){
				JSONObject obj=new JSONObject();
				obj.put("success", 0);
				obj.put("message", message);
				out.print(obj);
				out.close();
	}
	
	private void giveSuccessFeedBack(Double harambesBal){
				JSONObject obj=new JSONObject();
				obj.put("success", 1);
				obj.put("balance",harambesBal);
				out.print(obj);
				out.close();
	}
	
	private void giveSuccessFeedBack(String message, long amt){
				JSONObject obj=new JSONObject();
				obj.put("success", 1);
				obj.put("message",message);
				obj.put("total",amt);
				out.print(obj);
				out.close();
	}
	
	public void giveSuccessFeedBack(String message){
				JSONObject obj=new JSONObject();
				obj.put("success", "1");
				obj.put("message", message);
				out.print(obj);
				out.close();
	}
	
	public void giveSuccessFeedBack(String message, int sid, int id){
				JSONObject obj=new JSONObject();
				obj.put("success", 1);
				obj.put("message", message);
				obj.put("sid",sid);
				obj.put("id", id);
				out.print(obj);
				out.close();
	}
	
	public void giveNoDataFound(String message){
				JSONObject obj=new JSONObject();
				obj.put("success", 1);
				obj.put("message", message);
				out.print(obj);
				try{out.close();}catch(Exception e){}
	}
	
	public void giveErrorFeedBack(String message, int errorCode){
				JSONObject obj=new JSONObject();
				obj.put("success", 0);
				obj.put("error",errorCode);
				obj.put("message", message);
				out.print(obj);
				out.close();
	}
}
