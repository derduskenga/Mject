package com.harambesa.settings;
import com.harambesa.gServices.HarambesaUtils;
import com.harambesa.mailing.Mail;
import java.sql.*;

import com.harambesa.DBConnection.DBConnection;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.security.MessageDigest;

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
import javax.mail.internet.InternetAddress;
import javax.mail.internet.AddressException;
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

//import file upload classes
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.DiskFileUpload;

import java.security.NoSuchAlgorithmException;

public class profileEdit extends HttpServlet{

	  HttpServletRequest request=null; 
      HttpServletResponse response=null;
      PrintWriter out = null;	
      Logger log= null;
      String entity_id = null;

      public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException{

        //       request=req;
		      // response=res;
		      // log = Logger.getLogger(profileEdit.class.getName());
		      // out = res.getWriter();	
		      // response.setContentType("application/json");
		      // response.setCharacterEncoding("UTF-8");
        //   checkLogin();	
		      // postProfile();
          out = res.getWriter();  
          request=req;
          response=res;
          log=Logger.getLogger(profileEdit.class.getName());
          response.setContentType("application/json");
          response.setCharacterEncoding("UTF-8"); 
          //get the tag for this request
           String tag=null;
           if(request.getParameter("tag") == null ){
            log.severe("Tag is null");
            response.sendRedirect("error/");
           }else{
              try{
                tag = request.getParameter("tag");
                      processTag(tag.trim());
                      log.info("Tag:"+tag);
                }catch(IOException ioe){
                    log.severe(getStackTrace(ioe));
                }catch(SQLException sqle){
                    log.severe(getStackTrace(sqle));
                }catch(FileUploadException fue){
                    log.severe(getStackTrace(fue));
                }
           }
      }

      public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException{

   //    	    out = res.getWriter();	
			// request=req;
			// response=res;
			// log=Logger.getLogger(ProfileEdit.class.getName());
			// response.setContentType("application/json");
			// response.setCharacterEncoding("UTF-8");

			// String tagger = request.getParameter("tag");

			// if(tagger == "enter_profile"){
				
			// }
      }
      ///method to update current profile
	 // post profile method
  public void postProfile()throws FileUploadException{

            HttpSession sess=request.getSession();
            String id=null;

            try{
              id = (String)sess.getAttribute("entity_id");
              log.info("id assigned.");
            }catch(Exception ex){
              log.severe("Error Assigning id on trying to check login --:"+HarambesaUtils.getStackTrace(ex));
            }

            if(id !=null){
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

                String occupation =null;
                String country = null;
                String address = null;
                String postalcode = null;
                String phoneno = null;
                String bio = null;
                String org = null;
                String city = null;
                String profile_pic_path=null;
                String primary_email=null;
                String physical_street_address=null;
                File fullFile=null;

          while(itr.hasNext()){


                  FileItem item = (FileItem) itr.next();  
                  // check if the current item is a form field or an uploaded file

                    // check if the current item is a form field or an uploaded file
                  if(item.isFormField()) {  
                  // get the name of the field
                  String fieldName = item.getFieldName();
                  // if it is name, we can set it in request to thank the user
                 if(fieldName.equals("occupation")){
                     occupation = item.getString().trim();
                   }
                  else if(fieldName.equals("address")){
                     address = item.getString().trim();
                   }
                  else if(fieldName.equals("postalcode")){
                     postalcode = item.getString().trim();
                   }
                  else if(fieldName.equals("bio")){
                     bio = item.getString().trim();
                   }
                  else if(fieldName.equals("city")){
                     city = item.getString().trim();
                   }
                  else if(fieldName.equals("orgname1")){
                     org = item.getString().trim();
                   }
                  else if(fieldName.equals("physical_address")){
                     physical_street_address = item.getString().trim();
                   }else if(fieldName.equals("primaryemail")){
                          primary_email = item.getString().trim();
                    }
                  } else {
                    // the item must be an uploaded file save it to disk. Note that there
                    // seems to be a bug in item.getName() as it returns the full path on
                    // the client's machine for the uploaded file name, instead of the file
                    // name only. To overcome that, I have used a workaround using
                    // fullFile.getName().
                    fullFile  = new File(item.getName());
                    HttpSession session = request.getSession();
                    String username = (String)request.getAttribute("entity_id");
                    File savedFile = new File(getServletContext().getRealPath("/profilepic"), fullFile.getName());
                    log.info(getServletContext().getRealPath("/"));
                    try{
                      item.write(savedFile);
                      profile_pic_path=fullFile.getName();
                    }catch(Exception e){
                      log.severe(getStackTrace(e));
                    }
                  }
                }
            DBConnection db=new DBConnection();
            Connection con = db._getConnection();

            try{

                  
              String sql=null;
              if(profile_pic_path==null){
                sql="UPDATE entitys SET occupation=?, address=?, postal_code=?, physical_street_address=?, org_name=?, details=?, city=?, primary_email=? WHERE entity_id=?";
              }else{
                sql="UPDATE entitys SET occupation=?, address=?, postal_code=?, physical_street_address=?, org_name=?,  details=?,city=?, primary_email=?, profile_pic_path=? WHERE entity_id=?";
              }
              PreparedStatement ps = con.prepareStatement(sql);
             
              ps.setString(1,occupation );
              ps.setString(2,address );
              ps.setString(3,postalcode);
              ps.setString(4,physical_street_address );
              ps.setString(5,org );
              ps.setString(6,bio );
              ps.setString(7,city);
              ps.setString(8,primary_email);
            
              if(profile_pic_path != null){
                  ps.setString(9, profile_pic_path);
                  ps.setInt(10, Integer.parseInt(id));
              }else{
                  ps.setInt(9, Integer.parseInt(id));
              }
              JSONArray jArray=new JSONArray();
              JSONObject obj=new JSONObject();
              
              int stmt = ps.executeUpdate();
              log.info("Updating profile int:"+stmt);
              if(stmt != 1 ){
                  obj.put("success", 0);
                  obj.put("message", "There was a problem updating your profile, Please try again");
              }else if(stmt ==1 ){
                  // reloadCredentials(id);
                  obj.put("success", 1);
                  obj.put("message", "Your profile was successfully updated.");
              }
              jArray.add(obj);
                out.println(obj);

            }catch(Exception e){
                log.severe(HarambesaUtils.getStackTrace(e));
            }finally{
              try{db.closeDB();}catch(Exception e){}
            }

    }

}
private void processTag(String tag) throws FileUploadException, SQLException, IOException{
   if(tag.equals("update_profile")){
        try{
          postProfile();
        }catch(FileUploadException fe) {}
    }
    else{
        response.sendRedirect("error/");
        }  
}


   private void checkLogin(){
     HttpSession session = request.getSession();
     entity_id=(String)session.getAttribute("entity_id");
     if(entity_id == null){
      //this user is not logged in
      //now send a redirect
     sendRedir();
    }
  }
    public void sendRedir(){
    JSONObject obj  = new JSONObject();
    obj.put("success", 0);
    obj.put("redir", "login");
    obj.put("message", "Your session has expired, please login to continue");
    out.println(obj);
    out.close();
  }
    public static String getStackTrace(final Throwable throwable) {
      final StringWriter sw = new StringWriter();
      final PrintWriter pw = new PrintWriter(sw, true);
      throwable.printStackTrace(pw);
      return sw.getBuffer().toString();
  }
   
}