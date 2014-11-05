<%@ page import="com.harambesa.DBConnection.DBConnection"%>
<%@ page import="com.harambesa.gServices.HarambesaUtils"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.logging.Logger" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@ page import="org.json.simple.JSONArray" %>

<%
  String message = null;
  Logger log = null;
  PrintWriter print = null;

  	String token = request.getParameter("token");
  	String id = request.getParameter("id");

	if(request.getParameter("token") == null || request.getParameter("id") == null || request.getParameter("token").equals("") || request.getParameter("id").equals("")){

	           response.sendRedirect("../error/");

	}else if(token.equals("") || token==null || id.equals("") || id==null){

								response.sendRedirect("error/");

	}else{

			DBConnection db = new DBConnection();
			Connection con = db._getConnection();
			try{
					String check_token_sql="SELECT (EXTRACT(EPOCH FROM INTERVAL '24 hours')-EXTRACT (EPOCH FROM ( LOCALTIMESTAMP - created_at))) as diff ,activation_token_entity_id,status FROM admin_account_tokens WHERE activation_token_id=? AND activation_token=?";
					PreparedStatement ps = con.prepareStatement(check_token_sql);
					
					ps.setInt(1,Integer.parseInt(id));
					ps.setString(2,token);
					ResultSet rs = ps.executeQuery();

					JSONObject obj = new JSONObject();

					if(rs.next()){
						Double diff=rs.getDouble(1);
						Boolean status =rs.getBoolean(3);

						if(diff<0.0){
							message="Your Password reset period has expired. Please request for password reset again.";
							response.sendRedirect("../password/expired/");
						}else if(status == true){
							message="You have already reset your password using this link.";
							response.sendRedirect("../password/already-set/");
						}
					}else{
							message="We could not find the page you requested.";
							response.sendRedirect("../error/");
					}
			}catch(Exception e){
					System.out.println("Error: "+HarambesaUtils.getStackTrace(e));
					message="There was a problem loading this page, please try again later";
			} 
	}
%>

<!DOCTYPE html>
<html lang="en-us">
<head>
<meta charset="utf-8">

<title>HaraMbesa | Login</title>
<meta name="description" content="">
<meta name="author" content="">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<link rel="stylesheet" type="text/css" media="screen" href="../res/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/jasny-bootstrap/css/jasny-bootstrap.min.css"></link>
<link rel="stylesheet" type="text/css" media="screen" href="../res/bootstrap-datepicker/css/datepicker.css"></link>
<link rel="stylesheet" type="text/css" media="screen" href="../res/select/select2.css"/>
<link rel="stylesheet" type="text/css" media="screen" href="../res/css/styleX.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/css/select2.css">

<script type="text/javascript" src="../res/js/jquery.js"></script>
<script type="text/javascript" src="../res/bootstrap/js/bootstrap.min.js"></script> 
<script type="text/javascript" src="../res/bootstrap-validator/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src="../res/jasny-bootstrap/js/jasny-bootstrap.min.js"></script>
<script type="text/javascript" src="../res/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
<script type="text/javascript"  src="../res/select/select2.js"></script>
<script type="text/javascript" src="../res/js/log4javascript.js" ></script> 
<script type="text/javascript" src="../res/js/landing.js" ></script>
<script type="text/javascript" src="../ADMIN/includes/js/admin_login.js"></script>


</head>
<body>
	<div class="container-fluid">
		<header class="row">
			<!--<div class="col-md-2 col-md-offset-8 text-right" style="vertical-align:middle">need account?</div> -->
			<div class="col-md-4 col-md-offset-7 text-right">				
				<span> <h3>Password set up</h3> </span>
			</div>
		</header>
		<div id="content" class="row">
			<!-- ==========================================================================================-->
			<div id="content_left" class="col-md-5 col-md-offset-1">
				<div class="row">
					<div class="col-md-12">
						<img src="../res/images/logo.png" class="img-responsive" alt="logo">
					</div>
				</div>
				<div class="row ">
				</div> <!--footer -->
			</div><!-- /content_left-->
			<!-- ==========================================================================================-->
			<div id="content_right" class="col-md-5">
				<div class="row">
					<div class="col-md-11 col-md-offset-1">							
						<!-- ___________________________________________________________________________-->
							<div class="panel panel-default" id="logon_panel">
								<div class="panel-heading">
									<h3 class="panel-title">Set Password</h3>
								</div>
								<form role="form" method="" action="" id="set_password_form" enctype="multipart/form-data">
										<div class="panel-body">		
											<div id="sign-in-error" class="alert-danger hidden"></div>
											<div class="form-group">
												<label for="username">Password</label>
												<div class="input-group">
													<input type="password" class="form-control" id="s_password" name="s_password" required>
													<span class="input-group-addon">
														<span class="glyphicon glyphicon-lock"></span>
													</span>
												</div>
											</div>										
											<div class="form-group">
												<label for="exampleInputPassword1">Confirm Password</label>
												<div class="input-group">
													<input type="password" class="form-control" id="c_s_password" name="c_s_password" required>
													<span class="input-group-addon">
														<span class="glyphicon glyphicon-lock"></span>
													</span>
												</div>
											</div>
											<div class="form-group">
												<div class="input-group">
											     <input id="admin_entity_id" type="text" value="<%=request.getParameter("id")%>" hidden>
											 </div>
											</div>
										</div>
								
										<div class="panel-footer text-right">
											
											<button type="submit" class="btn btn-primary custom-btn-submit">Create</button>
											
										</div>
								</form>
						  </div>
						_____________________________________________________________	
					
					</div>
				</div>
			</div>
				<!--include social -->
				<%@ include file="../res/includes/social.jsp" %>	
				<!--end social-->
			</div><!-- /content_right-->
			<!-- ===========================================================================alternatePanel===============-->
		</div> <!--/content-->
		<!--footer-->
		<div class="navbar-fixed-bottom">
		<%@ include file="../res/includes/footer.jsp" %>
		</div>
		<!--end footer-->
	</div> <!-- container-fluid -->
</body>
</html>