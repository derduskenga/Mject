<%@ page import="com.harambesa.DBConnection.DBConnection"%>
<%@ page import="com.harambesa.gServices.HarambesaUtils"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.logging.Logger" %>

<%
		String message=null;
		if(request.getParameter("token") == null || request.getParameter("id") == null || 
					request.getParameter("token").equals("") || request.getParameter("id").equals("")){
								response.sendRedirect("../error/");
		}else{ 		
		String token = request.getParameter("token");
		String id = request.getParameter("id");
		if(token.equals("") || token==null || id.equals("") || id==null){
								response.sendRedirect("error/");
		}else{
			DBConnection db = new DBConnection();
			Connection con = db._getConnection();
			try{
					String check_token_sql="SELECT (EXTRACT(EPOCH FROM INTERVAL '24 hours')-EXTRACT (EPOCH FROM ( LOCALTIMESTAMP - created_at)))";
								check_token_sql+=" as diff ,entity_id,status ";
								check_token_sql+=" FROM reset_password WHERE reset_password_id =? AND reset_password_token=?";
					PreparedStatement ps = con.prepareStatement(check_token_sql);
					
					ps.setInt(1,Integer.parseInt(id));
					ps.setString(2,token);
					ResultSet rs = ps.executeQuery();					
					if(rs.next()){
						Double diff=rs.getDouble(1);
						Boolean status =rs.getBoolean(3);
						if(diff<0.0){

							message="Your Password reset period has expired. Please request for password reset again.";
						}else if(status == true){
							message="You have already reset your password using this link.";

						}
					}else{
							message="We could not find the page you requested.";
					}
			}catch(Exception e){
					System.out.println("Error: "+HarambesaUtils.getStackTrace(e));
					message="There was a problem loading this page, please try again later";
			}
		}
	}
%>
<!DOCTYPE html>
<html lang="en-us">
<head>
<meta charset="utf-8">

<title>HaraMbesa</title>		
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<link rel="stylesheet" type="text/css" media="screen" href="../res/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/css/styleX.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/css/connection_requests_styles.css">

<link rel="stylesheet" type="text/css" media="screen" href="../res/css/stylez.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/bootstrap-datepicker/css/datepicker.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/bootstrap-datepicker/css/datepicker3.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/bootstrap-datepicker/css/style.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/bootstrap-datepicker/css/datepicker.css"></link>
<link rel="stylesheet" type="text/css" media="screen" href="../res/select/select2.css"/>
<link rel="stylesheet" type="text/css" media="screen" href="../res/css/styleX.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/css/select2.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/bootstrap-dialog/css/bootstrap-dialog.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/spinner/bootstrap-spinner.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/css/spinner.css">

<script type="text/javascript" src="../res/js/jquery.js"></script>
<script type="text/javascript" src="../res/bootstrap/js/bootstrap.min.js"></script> 
<script type="text/javascript" src="../res/bootstrap-validator/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src="../res/js/landing.js" ></script>

</head>
<body>
	<div class="container-fluid">
		<header class="row">
			<!--<div class="col-md-2 col-md-offset-8 text-right" style="vertical-align:middle">need account?</div> -->
			<div class="col-md-4 col-md-offset-7 text-right">				
				<span> <a href="../logon.jsp"><button type="button" class="btn btn-danger"> SIGN IN</button> </a></span>
			</div>
		</header>		
		<div id="content" class="row">
			<div id="json_test"></div>
			<!-- ==========================================================================================-->
			<!--===========================================================================================-->
			<div id="content_right" class="col-lg-6 col-lg-offset-3">
				<div class="row">
					<div class="col-md-11 col-md-offset-1" id="password-reset-panel-">	
						<!--____________________________________________________________________________-->
				<%
					
					if(message==null){
				
				%>
								<form role="form" id="password-reenter-form" action="../process_request.jsp" class="form-horizontal">
										<fieldset>
										<legend>
										Please Enter your new password and its confirm below to reset your password
										
										</legend>
										<div id="form_panel" class="panel-body">
											<div id="password-change-error" class="col-lg-10 col-lg-offset-1 alert-danger hidden"></div>
											<div class="form-group">
												<label class="col-lg-4 control-label" for="password">Password:</label>
												<div class="col-lg-7">
													<input type="password" class="form-control" name="cpassword" id="cpassword">
												</div>
											</div>
											<div class="form-group">
												<label class="col-lg-4 control-label" for="r_password">Confirm Password:</label>
												<div class="col-lg-7">
													<input type="password" class="form-control" name="r_cpassword" id="r_cpassword">
												</div>
											</div>
										</div>	
										</fieldset>
										<div class="form-group ">
											<div class="col-lg-8 col-lg-offset-4">
												<button type="submit"  class="btn btn-primary custom-btn-submit " id="submit-reset-btn">Submit</button>				
												<a href="index.jsp" class="btn btn-success custom-btn-submit hidden" id="logon">Login</a>
											</div>
										</div>
								</form>
					<%   }else{    %>
					
							<div class="password-reset-error"><%=message%></div>
					
					<%    }    %>
						<!-- ___________________________________________________________________________-->	
					</div>					
				</div>
			</div><!--=========== content_right-->
			<!-- ==========================================================================================-->		
		</div> <!--/content-->
		<!-- footer========================== -->
		<div class="navbar-fixed-bottom">
			<%@ include file="../res/includes/footer.jsp" %>
		</div>
		<!-- =======================end footer========================= -->
	</div> <!-- container-fluid -->
</body>
</html> 
