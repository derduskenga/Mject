<%@page import="javax.servlet.http.HttpSession"%>
<%@page import="org.apache.logging.log4j.LogManager"%>
<%@page import="org.apache.logging.log4j.Logger"%>
<%@page import="com.harambesa.gServices.HarambesaUtils"%>
<%@page import="javax.servlet.http.HttpServletRequest"%>
<%@page import="java.io.IOException"%>

<%
	HttpServletResponse httpResponse = (HttpServletResponse) response;
	httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
	httpResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0
	httpResponse.setDateHeader("Expires", 0); // Proxies.
	final Logger logger = LogManager.getLogger();
	killSession(request, logger);
%>
<%!
	public void killSession(HttpServletRequest request, Logger logger){
		try{
			HttpSession session = request.getSession(false);
			if(session != null){
				session.invalidate();
			}
		}catch(NullPointerException ex){
				System.out.println("Error: "+HarambesaUtils.getStackTrace(ex));
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

</head>
<body>
	<div class="container-fluid">
		<header class="row">
			<!--<div class="col-md-2 col-md-offset-8 text-right" style="vertical-align:middle">need account?</div> -->
			<div class="col-md-4 col-md-offset-7 text-right">				
				<span> Need Account? <a href="../signup"><button type="button" class="btn btn-danger">Sign Up</button></a></span>
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
									<h3 class="panel-title">Sign In</h3>
								</div>
								<form role="form" method="POST" action="" id="login-form">
										<div class="panel-body">		
											<div id="sign-in-error" class="alert-danger hidden"></div>
											<div class="form-group">
												<label for="username">Username</label>
												<div class="input-group">
													<input type="text" class="form-control" id="j_username" name="j_username" required>
													<span class="input-group-addon">
														<i class="fa fa-user fa-fw"></i>
													</span>
												</div>
											</div>										
											<div class="form-group">
												<label for="exampleInputPassword1">Password</label>
												<div class="input-group">
													<input type="password" class="form-control" id="j_password" name="j_password" required>
													<span class="input-group-addon">
														<span class="glyphicon glyphicon-lock"></span>
													</span>
												</div>
											</div>
											<div class="form-group">
												
												<a href="javascript:alternatePanel('forgot_panel');">Forgot Password ? </a>
												
											</div>	
										</div>
								
										<div class="panel-footer text-right">
											
											<button type="submit" class="btn btn-primary custom-btn-submit">Sign In</button>
											
										</div>
								</form>
						  </div>
						<!-- ___________________________________________________________________________-->	
						<!-- ____________________________forgot Password----------------------->
						<div class="hidden" id="forgot_panel">
							<div class="panel panel-default" id="forgot_panel-default">
								<div class="panel-heading">
									<h3 class="panel-title">Password Reset</h3>
								</div>
								<form role="form" id="forgot-p-form" class="form-horizontal">
									<div id="pass-reset">
										<div class="text-center col-lg-10 col-lg-offset-1" id="r_p_text">
											<h4>
											Please enter your email below to reset password
											</h4>
											<hr>
										</div>		
										<div id="form_panel" class="panel-body">
												<div id="password-reset-error" class="col-lg-10 col-lg-offset-1 alert-danger hidden"></div>
												<div id="password-reset-success" class="col-lg-10 col-lg-offset-1 alert-success hidden"></div>
												<div class="form-group">
													<label class="col-sm-4 control-label" for="email">Email:</label>
													<div class="col-sm-7">
														<input type="text" class="form-control" name="r_email" id="r_email">
													</div>
												</div>	
										</div>
									</div>
									<div class="panel-footer text-right" id="submit-btn-panel">
										<button type="submit"  class="btn btn-primary custom-btn-submit" id="submit-r-btn">Submit</button>	
										<a href="../login" class="btn btn-success custom-btn-submit hidden" id="logon">Login</a>
									</div>	
								</form>
						  </div>			
						<!-- ___________________________________________________________________________-->	
						<div id="click-logon" class="text-right">
								Click <a href="javascript:alternatePanel('logon_panel');">here</a> to login
						</div>
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