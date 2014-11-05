package com.harambesa.gServices;

import java.sql.*;
import com.harambesa.DBConnection.DBConnection;
import com.harambesa.gServices.Offer;
import com.harambesa.gServices.HarambesaUtils;
import com.harambesa.notification.Notifications;
import com.harambesa.Utility.Utilities;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import java.util.Random;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse; 
import javax.servlet.annotation.WebServlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.util.Random;
import java.util.Iterator;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import java.util.logging.Logger;
import java.io.InputStreamReader; 
import java.io.InputStream;
import java.io.StringWriter;
import javax.servlet.http.Part;

import java.lang.Thread;
import java.security.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.DateFormat;
import java.math.BigInteger;

//import file upload classes
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.DiskFileUpload;

import java.security.NoSuchAlgorithmException;

public class MaterialOffer extends HttpServlet {
	HttpServletRequest request=null; 
	HttpServletResponse response=null;
	PrintWriter out = null;	
	Logger log = Logger.getLogger(MaterialOffer.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException {
		this.request=req;
		this.response=res;
		out = res.getWriter();	
		doPost(req, res);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{  
		out = response.getWriter();
		this.request=request;
		this.response=response;
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		//get the tag for this request
		String tag=null;
		if(request.getParameter("tag") == null ){
		      log.severe("Tag is null");
		}else{
		
			try{
				saveMaterialOffer();
			}catch(FileUploadException fue){
				log.severe(getStackTrace(fue));
			}catch(Exception ex){
				
			}
		}
	}	
	public void saveMaterialOffer() throws FileUploadException{
		//check if user is logged in
		HttpSession sess=request.getSession();
		String id=null;
		String names = null;
		String profile_pic_path = null;
		try{
			id = (String)sess.getAttribute("entity_id");
			names = (String)sess.getAttribute("f_name") + " " + (String)sess.getAttribute("l_name");
			profile_pic_path = "../profilepic/" + (String)sess.getAttribute("profile_pic_path");
		}catch(Exception ex){
			
		}
		
		if(id != null){
			//since we set to process this request as a multipart request:
			// first check if the upload request coming in is a multipart request
			boolean isMultipart = FileUpload.isMultipartContent(request);
			//Create a handler for this request, and use it to parse the request. 
			//Upon parsing, all of the form items are available in a list.
			DiskFileUpload upload = new DiskFileUpload();	
			// parse this request by the handler
			// this gives us a list of items from the request
			List items = upload.parseRequest(request);
			 //Iterate over this list to access individual file items. 
			Iterator itr = items.iterator();						
			String m_category = null;
			String m_name = null;
			String m_units = null;
			String m_country = null;
			String m_state = null;
			String m_residence = null;
			File main_photo = null;
			File other_photo_one = null;
			File other_photo_two = null;
			String main_photo_path=null;
			String other_photo_one_path = null;
			String other_photo_two_path = null;
			String m_offer_details = null;
			
			while(itr.hasNext()) {
				FileItem item = (FileItem) itr.next();					
				// check if the current item is a form field or an uploaded file
				if(item.isFormField()) {
					// get the name of the field
					String fieldName = item.getFieldName();
					//log.info("FieldName: "+fieldName );
					if(fieldName.equals("material_category")){
						m_category = item.getString();}
					else if(fieldName.equals("material_name")){
						m_name = item.getString();}
					else if(fieldName.equals("material_units")){
						m_units = item.getString();}
					else if(fieldName.equals("material_countries")){
						m_country = item.getString();}
					else if(fieldName.equals("material_state")){
						m_state = item.getString();}
					else if(fieldName.equals("material_residence")){
						m_residence = item.getString();}
					else if(fieldName.equals("material_offer_details")){
						m_offer_details = item.getString();
					}
				}else{
					// the item must be an uploaded file save it to disk. Note that there
					// seems to be a bug in item.getName() as it returns the full path on
					// the client's machine for the uploaded file name, instead of the file
					// name only. To overcome that, I have used a workaround using
					// fullFile.getName().
					String fileFieldName = item.getFieldName();
					if(fileFieldName.equals("photo_main")){
						main_photo  = new File(item.getName());
						File savedFileMain = new File(getServletContext().getRealPath("/materialofferphotos"), renamePhoto(main_photo.getName()));
						log.info(getServletContext().getRealPath("/"));
						try{
							item.write(savedFileMain);
							main_photo_path=renamePhoto(main_photo.getName());
						}catch(Exception e){
							log.severe(getStackTrace(e));
						}
					}else if(fileFieldName.equals("other_photos_one")){
						other_photo_one  = new File(item.getName());
						File savedFileOtherOne = new File(getServletContext().getRealPath("/materialofferphotos"), renamePhoto(other_photo_one.getName()));
						log.info(getServletContext().getRealPath("/"));
						try{
							item.write(savedFileOtherOne);
							other_photo_one_path=renamePhoto(other_photo_one.getName());
						}catch(Exception e){
							log.severe(getStackTrace(e));
						}
					}else if(fileFieldName.equals("other_photos_two")){
						other_photo_two  = new File(item.getName());
						File savedFileOtherTwo = new File(getServletContext().getRealPath("/materialofferphotos"), renamePhoto(other_photo_two.getName()));
						log.info(getServletContext().getRealPath("/"));
						try{
							item.write(savedFileOtherTwo);
							other_photo_two_path=renamePhoto(other_photo_two.getName());
						}catch(Exception e){
							log.severe(getStackTrace(e));
						}
					}
				}
			}
			
			DBConnection db=new DBConnection(); 
			Connection con = db._getConnection();
			JSONObject obj=new JSONObject();
			String sql = "INSERT INTO material_offer (m_entity_id,m_offer_category,m_offer_name,m_offer_units,m_offer_country,m_offer_state,m_offer_residence,m_offer_main_photo,m_offer_other_photo_1,m_offer_other_photo_2,m_offer_details)" +
				      " VALUES(?,?,?,?,?,?,?,?,?,?,?)" +
				      " RETURNING m_offer_id,m_offer_main_photo,m_offer_other_photo_1,m_offer_other_photo_2";
			try{
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setInt(1,Integer.parseInt(id));
				ps.setString(2,m_category);
				ps.setString(3,m_name);
				ps.setInt(4,Integer.parseInt(m_units));
				ps.setString(5,m_country);
				ps.setString(6,m_state);
				ps.setString(7,m_residence);
				ps.setString(8,main_photo_path);
				ps.setString(9,other_photo_one_path);
				ps.setString(10,other_photo_two_path);
				ps.setString(11,m_offer_details);
				
				ResultSet rs = ps.executeQuery();
				Offer offer = new Offer(id);
				
				if(rs != null){
					if(rs.next());
					obj.put("success",1);
					obj.put("message","Offer has been registered successifully and will be seen by applicants.");
					obj.put("material_offer_id",rs.getInt(1));
					obj.put("entity_id",id);
					obj.put("names",names);
					obj.put("profile_pic_path",profile_pic_path);
					obj.put("time_details",offer.getFormattedTimeStamp("material_offer","m_offer_date",rs.getInt(1),"m_offer_id"));
					obj.put("main_photo_path","../materialofferphotos/" + rs.getString(2));
					obj.put("other_photo_one_path","../materialofferphotos/" + rs.getString(3));
					obj.put("other_photo_two_path","../materialofferphotos/" + rs.getString(4));
					obj.put("offer_type","material");
					
					String type = "m_offer";
					String notification_url = Utilities.NOTIFICATION_URL + "type=" + type + "&oi=" + rs.getString(1);
					String notification_msg = "<a href='../user/?user=" + id + "'>" + names + "</a> placed a material(" + m_name + ") offer";
					String notification_originator = id;
				
					Notifications noti = new Notifications(id);
					String [] recipients = noti.fetchNotificationRecipients();
					noti.saveNotification(notification_url,notification_msg,notification_originator,recipients);
				}else{
					obj.put("success",0);
					obj.put("message","An error ocured");
				}
			}catch(Exception ex){
				log.severe("Error is here: " + HarambesaUtils.getStackTrace(ex));
				System.out.println(ex.getMessage().toString());
				obj.put("success", 0);
				obj.put("message", "Offer failed. Something went wrong.");
				obj.put("error",ex.getMessage().toString());
			}
			out.println(obj);
			db.closeDB();
		}else{
			log.info("Trying to update profile without with no valid sess");
			JSONObject obj = new JSONObject();
			obj.put("success", 0);
			obj.put("message", "There was a problem posting your offer, Please try again");
			obj.put("redir", "../login");
			out.println(obj);
			
		}
	}
	
	public static String getStackTrace(final Throwable throwable) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}
	
	public String renamePhoto(String original){
		//just append a random string to the original name and return it
		// Create an instance of SimpleDateFormat used for formatting 
		// the string representation of date (month/day/year)
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		// Get the date today using Calendar object.
		java.util.Date today = Calendar.getInstance().getTime();        
		// Using DateFormat format method we can create a string 
		// representation of a date with the defined format.
		String reportDate = df.format(today);
		//String plaintext = "your text here";
		MessageDigest m =null;
		try{
			 m = MessageDigest.getInstance("MD5");
		}catch(NoSuchAlgorithmException nsae){
			log.severe("Algorithm Exception Error: " + HarambesaUtils.getStackTrace(nsae));
		}catch(Exception ex){
			log.severe("Algorithm Exception Error: " + HarambesaUtils.getStackTrace(ex));
		}
		m.reset();
		m.update(reportDate.getBytes());
		byte[] digest = m.digest();
		BigInteger bigInt = new BigInteger(1,digest);
		String hashtext = bigInt.toString(16);
		// Now we need to zero pad it if you actually want the full 32 chars.
		while(hashtext.length() < 32 ){
		  hashtext = "0"+hashtext;
		}
		
		return hashtext + original;
	}


}