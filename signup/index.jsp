<!DOCTYPE html>
<html lang="en-us">
<head>
<meta charset="utf-8">

<title>HaraMbesa | Login</title>
<meta name="description" content="">
<meta name="author" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">

<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<link rel="stylesheet" type="text/css" media="screen" href="../res/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/jasny-bootstrap/css/jasny-bootstrap.min.css"></link>
<link rel="stylesheet" type="text/css" media="screen" href="../res/bootstrap-datepicker/css/datepicker.css"></link>
<link href="../res/select/select2.css" rel="stylesheet"/>
<link rel="stylesheet" type="text/css" media="screen" href="../res/css/styleX.css">


<script type="text/javascript" src="../res/js/jquery.js"></script>
<script type="text/javascript" src="../res/bootstrap/js/bootstrap.min.js"></script> 
<script type="text/javascript" src="../res/bootstrap-validator/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src="../res/jasny-bootstrap/js/jasny-bootstrap.min.js"></script>
<script type="text/javascript" src="../res/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
<script type="text/javascript"  src="../res/select/select2.js"></script>
<script type="text/javascript" src="../res/js/landing.js" ></script> 

</head>
<body>
	<div class="container-fluid">
		<header class="row">
			<!--<div class="col-md-2 col-md-offset-8 text-right" style="vertical-align:middle">need account?</div> -->
			<div class="col-md-4 col-md-offset-7 text-right">				
				<span> Already have an account? <a href="../login"><button type="button" class="btn btn-danger">Sign In</button> </a></span>
			</div>
		</header>			
		<div id="content" class="row">
			<div id="json_test"></div>
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
							<div class="panel panel-default">
								<div class="panel-heading">
									<h3 class="panel-title">Sign Up</h3>
								</div>
								<form role="form" id="reg-form" class="form-horizontal">
										<div id="form_panel" class="panel-body">
												<div id="sign-up-error" class="col-lg-10 col-lg-offset-1 alert-danger hidden"></div>
												<div class="form-group">
														<label class="col-lg-4 control-label" for="f_name">First Name:</label>
														<div class="col-lg-7">
															<input type="text" class="form-control" id="f_name" name="f_name">
														</div>
												</div>
												<div class="form-group">
														<label class="col-lg-4 control-label" for="l_name">Last Name:</label>
														<div class="col-lg-7">
															<input type="text" class="form-control" name="l_name" id="l_name">
														</div>
												</div>
												<div class="form-group">
													<label class="col-sm-4 control-label" for="username">Preferred Username:</label>
													<div class="col-sm-7">
														<input type="text" class="form-control" name="username" id="username">
														<span id="checking-username" class="hidden"> </span>
													</div>
													
												</div>
												<div class="form-group">
													<label class="col-sm-4 control-label" for="email">Primary Email:</label>
													<div class="col-sm-7">
														<input type="text" class="form-control" name="email" id="email">
														<span id="checking-email" class="hidden"></span>
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-4 control-label" for="password">Password:</label>
													<div class="col-sm-7">
														<input type="password" class="form-control" name="password" id="password">
													</div>
												
												</div>
												<div class="form-group">
													<label class="col-sm-4 control-label" for="r_password">Re-type Password:</label>
													<div class="col-sm-7">
														<input type="password" class="form-control" name="r_password" id="r_password">
													</div>
												</div>
												<div class="form-group has-feedback">
												<div class="col-sm-7 col-sm-offset-4">
													<div class="checkbox">
															<input type="checkbox" name="acceptTerms" data-bv-field="acceptTerms"></input>
															<i class="form-control-feedback" data-bv-field="acceptTerms"></i>
															 Accept the <a href="terms-conditions">terms and policies</a>
													</div>
												</div>
												</div>
										</div>
										<div class="panel-footer text-right">
											<button type="submit"  class="btn btn-primary custom-btn-submit" id="submit-btn">Submit</button>				
											<a href="../login" class="btn btn-success custom-btn-submit hidden" id="logon">Login</a>
										</div>
								</form>
						  </div>			
						<!-- ___________________________________________________________________________-->	
					</div>					
				</div>
				<!--======== include social -->
				<%@ include file="../res/includes/social.jsp" %>
				<!-- end Social -->
			</div><!--=========== content_right-->
			<!-- ==========================================================================================-->
		
		</div> <!--/content-->

		<!-- footer========================== -->
		<div class="navbar-fixed-bottom">
		<%@ include file="../res/includes/footer.jsp" %>	
		</div>
		<!-- end footer========================= -->
	</div> <!-- container-fluid -->

</body>
</html>