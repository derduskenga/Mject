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

<!DOCTYPE html>
<html lang="en-us">
<head>
<meta charset="utf-8">
<meta name="description" content="">
<meta name="author" content="">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<link rel="stylesheet" type="text/css" media="screen" href="../res/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/css/styleX.css">

<script type="text/javascript" src="../res/js/jquery.js"></script>
<script type="text/javascript" src="../res/bootstrap/js/bootstrap.min.js"></script> 
<script type="text/javascript" src="../res/bootstrap-validator/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src="../res/js/res.js" ></script> 
<title>HaraMbesa</title>		

</head>
<body>
	<div class="container-fluid">
		<%@ include file="../res/includes/header.jsp" %>
		<!--==New Request==-->

		<!--====-->
		<div id="content" class="row">
			<!--==content left============== -->
			<div id="main_content_left" class="col-xs-2 col-xs-offset-1">
			
			
					<div class="row left-link profile-pic">
					<a href=""><img src="../res/images/makau.jpg" alt="Create Tithing"></a>
				</div>
				<div class="row left-link">
					<a href=""><img src="../res/images/create.png" alt="Create Tithing"><imglinktxt>Create Tithing</imglinktxt></a>
				</div>
				<div class="row left-link">
					<a href=""><img src="../res/images/create.png" alt="Create Merry-Go-Rich"><imglinktxt>Create Merry-Go-Rich</imglinktxt></a>
				</div>
				
	
			</div><!--=============content left============-->
			<!--==content middle============== -->
		<div id="main_content_middle" class="col-xs-6">
					

<%

	DBConnection db=new DBConnection();
	
	String query="SELECT entitys.entity_id,entitys.first_name,entitys.last_name,entitys.profile_pic_path,connections.connection_id,connections.requestor_entity_id,connections.requestee_entity_id,connections.is_accepted FROM connections,entitys WHERE connections.is_accepted = FALSE AND connections.requestee_entity_id=1 AND entitys.entity_id=connections.requestor_entity_id ORDER BY connections.connection_id";
	
	ResultSet rs= db.readQuery(query);
	//System.out.println(db.getLastErrorMsg());
	JSONArray jArray=new JSONArray();
	try{
			
				while(rs.next()){            
					JSONObject obj=new JSONObject();
					obj.put("connection_id", rs.getString(5));
					obj.put("requestor_id", rs.getString(6));
					obj.put("requestor_fname", rs.getString(2));
					obj.put("requestor_lname", rs.getString(3));
					obj.put("connection_status", rs.getString(8));
					obj.put("prof_pic_path", rs.getString(4));
					obj.put("requestee_id", rs.getString(7));

				
					
					jArray.add(obj);
				}
				//out.print(jArray);
				db.closeDB();
				}
				catch(SQLException ex){
					System.out.println(ex.toString());
				}
		%>	
			
				
				<% for(int i=0; i<jArray.size(); i++){  
					//get the ith json object
					JSONObject jo=(JSONObject) jArray.get(i) ;
				%>
				<div id="connection_reqiest"class="row connection_reqiest"><!--row connection_reqiest-->
						<div class="connect-pic col-xs-2">
						<img alt="connect-pics" src="<%= jo.get("prof_pic_path") %>">
						</div>
						<%
					
						%>
						<div class="col-xs-4 details  person-details"><!--col-xs-2 post-pic-->
									<a class ="connector-name" href="#"><%= jo.get("requestor_fname") %> <%= jo.get("requestor_lname") %></a>
									
						</div><!--col-xs-2 post-pic-->
							
							<div class="btn-group"><!--btn-group-->
									<a id="ConnectBtn" class ="connector-name" href="exec_connection_request.jsp?ro=<%= jo.get("requestor_id") %>&re=<%= jo.get("requestee_id") %>">
									<button class="btn btn-primary connect-button col-md-3">Connect</button>
									</a>
									<a id="<%= jo.get("requestor_id") %>" class ="connector-name" href="../home/">
									<button class="btn btn-primary connect-button col-md-3">Message</button>
									</a>
									<a id="<%= jo.get("requestor_id") %>"  class ="connector-name" href="../home/">
									<button class="btn btn-primary disconect-button col-md-3" >Ignore</button>
									</a>
						</div><!--btn-group-->
				</div><!--row connection_reqiest-->
							
				<% } %>
			


				</div><!--main_content_middle-->
			<!--=========== content middle-->
			<!--==content right============== -->
			<div id="main_content_right" class="col-xs-3">
			Contect Right
			
			
			</div><!--=========== content right====-->
			<!--===========================================================================================-->
		</div> <!--/content-->
		<!-- footer========================== -->
		
		<%@ include file="../res/includes/footer.jsp" %>
		
		<!-- end footer========================= -->
	</div> <!-- container-fluid -->
</body>
</html>