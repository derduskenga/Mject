<%@ page import = "java.sql.Connection"%>
<%@ page import = "java.sql.DriverManager"%>
<%@ page import = "java.sql.SQLException"%>
<%@ page import = "java.sql.ResultSet"%>
<%@ page import = "java.sql.Statement"%>
<%@ page import = "com.harambesa.DBConnection.DBConnection"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import = "java.sql.Statement"%>
<%@ page import=" org.json.simple.JSONObject"%>
<%@ page import=" org.json.simple.JSONArray"%>


<%
	String re = request.getParameter("re");
	String ro = request.getParameter("ro");

	//System.out.println(req);
	DBConnection db=new DBConnection();
	String sesid=session.getId();
	String updsql="UPDATE connections SET is_accepted = 'true' WHERE requestor_entity_id='"+ro+"' AND requestee_entity_id='"+re+"' ";
	System.out.println(updsql);
	System.out.println(updsql);

	//out.println(db.getLastErrorMsg());
	db.executeUpdate(updsql);
	db.closeDB();
  // New location to be redirected
   String site = new String("http://localhost:8080/mjet/connection_requests/index.jsp");
   response.setStatus(response.SC_MOVED_TEMPORARILY);
   response.setHeader("Location", site); 
	
%>
