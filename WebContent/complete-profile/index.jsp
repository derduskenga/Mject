<%@page import="javax.servlet.http.HttpSession"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.util.Calendar" %>


<% if(request.getSession().getAttribute("entity_id")==null){
	response.sendRedirect("../login/");
}else{
	
	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	Date date = new Date();
	String todaysdate = dateFormat.format(date);
	System.out.println(todaysdate+1);
	
	Calendar c = Calendar.getInstance(); 
	c.setTime(date); 
	c.add(Calendar.DATE, 1);
	Date dt = c.getTime();
	String nextdate = dateFormat.format(dt);
%>
<!DOCTYPE html>
<html lang="en-us">
<head>
<meta charset="utf-8">
<title>HaraMbesa | Complete Profile</title>
<meta name="description" content="">
<meta name="author" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">

<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<link rel="stylesheet" type="text/css" media="screen" href="../res/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/jasny-bootstrap/css/jasny-bootstrap.min.css"></link>
<link rel="stylesheet" type="text/css" media="screen" href="../res/bootstrap-datepicker/css/datepicker.css"></link>
<link rel="stylesheet" type="text/css" media="screen" href="../res/select/select2.css"/>
<link rel="stylesheet" type="text/css" media="screen" href="../res/css/styleX.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/css/select2.css">
<link rel="stylesheet" type="text/css" media="screen" src="../res/css/messages.css" >
<link rel="stylesheet" type="text/css" media="screen" href="../res/css/imgareaselect-default.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/css/jquery.awesome-cropper.css">

<script type="text/javascript" src="../res/js/jquery.js"></script>
<script type="text/javascript" src="../res/bootstrap/js/bootstrap.min.js"></script> 
<script type="text/javascript" src="../res/bootstrap-validator/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src="../res/jasny-bootstrap/js/jasny-bootstrap.min.js"></script>
<script type="text/javascript" src="../res/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
<script type="text/javascript"  src="../res/select/select2.js"></script>
<script type="text/javascript" src="../res/js/log4javascript.js" ></script> 
<script type="text/javascript" src="../res/js/landing.js" ></script> 
<script type="text/javascript" src="../res/js/messages.js" ></script> 
<script type="text/javascript" src="../res/js/complete-profile.js" ></script> 
<script type="text/javascript" src="../res/js/jquery.imgareaselect.js"></script>
<script type="text/javascript" src="../res/js/jquery.awesome-cropper.js"></script>
</head>
</head>
<body>
	<div class="container-fluid">
		<header class="row">
			<div class="col-md-2 col-md-offset-8 text-right" style="vertical-align:middle">need account?</div> 
			<div class="col-md-4 col-md-offset-7 text-right">				
				<span><a href="../home"><button type="button" class="btn btn-danger">Skip to Home >> </button> </a></span>
			</div>
		</header>

		<div id="content" class="row">
			<div id="json_test"></div>
			<!--===========================================================================================-->
			<!--===========================================================================================-->
			<div id="content_right" class="col-lg-6 col-lg-offset-3">
				<div class="row">
					<div class="col-md-11 col-md-offset-1" id="complete-profile-panel">	
						<!--____________________________________________________________________________-->	
							<form role="form" id="complete-profile-form" class="form-horizontal" enctype="multipart/form-data">
										<legend class="text-center">
											Please take a moment to complete your profile.
										</legend>

										<div id="form_panel" class="panel-body">
											<div class="text-small text-center profile-update-tip"><p>Fields marked * are mandatory</p></div>
											<div id="profile-update-error" class="col-lg-10 col-lg-offset-1 alert-danger hidden"></div>
											<div class="form-group">
												<label class="col-lg-4 control-label" for="profile-pic-upload">Profile Picture</label>
												<div class="fileinput fileinput-new col-lg-7" data-provides="fileinput">
														<div class="fileinput-new thumbnail" style="width: 200px; height: 150px;">
															<img data-src="holder.js/100%x100%" alt="">
														</div>
														<div class="fileinput-preview fileinput-exists thumbnail" style="max-width: 200px; max-height: 150px;"></div>
														<div>
																<span class="btn btn-default btn-file"><span class="fileinput-new">Select image</span><span class="fileinput-exists">Change</span><input type="file" name="profile_pic" id="profile_pic"></span>
																<a href="#" class="btn btn-default fileinput-exists" data-dismiss="fileinput">Remove</a>
														</div>
												</div>
											</div> 

											<div class="form-group">
												<label class="col-lg-4 control-label" for="gender">Gender *</label>
												<div class="col-lg-7">
													<select  class="form-control" id="gender" name="gender" role="menu">
														<option value="M">Male</option>
														<option value="F">Female</option>
													</select>
												</div>
											</div>
											<div class="form-group">
													<label class="col-sm-4 control-label" for="countrys">Country *</label>
													<div class="col-sm-7">
														<select class="form-control" id="countrys" name="countrys" role="menu">						
														</select>
														<span id="loading_country" class="hidden">
																<img  src="../res/images/loading.gif" /> Loading Countries
														</span>
													</div> 
											</div>
											<div class="form-group">
												<label class="col-lg-4 control-label" for="dob">Date Of Birth *</label>
												<div class="col-lg-7">
													 <input  type="text" placeholder="" max-date="<%=todaysdate%>" max-date2="<%=nextdate%>" class="form-control"  id="dateofbirth" name="dateofbirth">
												</div>
											</div>
											<div class="form-group">
												<label class="col-lg-4 control-label" for="phone-country-code">Phone Number *</label>
												<div class="col-xs-3">
													 <input  type="text" placeholder="--"  class="form-control"  id="phone-country-code" name="phone-country-code">
												</div>
												<div class="col-xs-4">
													 <input  type="text" placeholder=""  class="form-control"  id="phoneno" name="phoneno">
												</div>
											</div>
											<div class="form-group">
												<label class="col-lg-4 control-label" for="occupation">Occupation *</label>
												<div class="col-lg-7">
													<input type="text" class="form-control" name="occupation" id="occupation">
												</div>
											</div>
											<div class="form-group">
												<label class="col-lg-4 control-label" for="orgname">Organization </label>
												<div class="col-lg-7">
													<input type="text" class="form-control" name="orgname" id="orgname">
												</div>
											</div>
											<div class="form-group">
												<label class="col-lg-4 control-label" for="address">Address *</label>
												<div class="col-lg-7">
													<input type="textarea" class="form-control" name="address" id="address">
												</div>
											</div>
											<div class="form-group">
												<label class="col-lg-4 control-label" for="address">Postal/Zip Code *</label>
												<div class="col-lg-7">
													<input type="textarea" class="form-control"  name="postalcode" id="postalcode">
												</div>
											</div>
											<div class="form-group">
												<label class="col-lg-4 control-label" for="city">City *</label>
												<div class="col-lg-7">
													<input type="textarea" class="form-control"  name="city" id="city">
												</div>
											</div>
											<div class="form-group">
												<label class="col-lg-4 control-label" for="postalcode">Physical / Street Address </label>
												<div class="col-lg-7">
													<textarea type="text" class="form-control" rows="3" name="physical_address" id="physical_address"></textarea>
												</div>
											</div>
											<div class="form-group">
												<label class="col-lg-4 control-label" for="bio">Bio	</label>
												<div class="col-lg-7">
													<textarea type="textarea" maxlength="500" class="form-control" rows="6" name="bio" id="bio" placeholder="Write a brief summary about yourself in 500 characters or less."></textarea>
													<small id="remainingC">Remaining: 500</small>
												</div>
											</div>
											<div class="form-group">
												<div class="col-xs-4 col-xs-offset-4 col-lgs-offset-4">
														<button type="submit"  class="btn btn-primary custom-btn-submit " id="submit-pu-btn">Submit</button>	
												</div>
												<div class="col-xs-4">
														<span id="updating-profile-gif" class="hidden">
																<img  src="../res/images/loading.gif" /> Updating profile...
														</span>
												</div>
											</div>
								</form>
						<!-- ___________________________________________________________________________-->	
					</div>					
				</div>
			</div><!--=========== ontent_right-->
			<!-- ==========================================================================================-->
		</div> <!--/content-->
		<!-- footer========================== -->
		<div class="row navbar-fixed-bottom">
		<%@ include file="../res/includes/footer.jsp" %>
		</div>
		<!-- end footer========================= -->
	</div> <!-- container-fluid -->	  
</body>
</html>
<%}%>