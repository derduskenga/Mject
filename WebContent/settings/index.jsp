<%@page import="javax.servlet.http.HttpSession"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.util.Calendar" %>
<% if(request.getSession().getAttribute("entity_id")==null){

       response.sendRedirect("../login");
       
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
<title>HaraMbesa</title>		
	<%@ include file="../res/header_links.jsp" %> 

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


			<script type="text/javascript" src="../res/bootstrap/js/bootstrap.min.js"></script> 
			<script type="text/javascript" src="../res/bootstrap-validator/js/bootstrapValidator.min.js"></script>
			<script type="text/javascript" src="../res/jasny-bootstrap/js/jasny-bootstrap.min.js"></script>
			<script type="text/javascript" src="../res/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
			<script type="text/javascript"  src="../res/select/select2.js"></script>
			<script type="text/javascript" src="../res/js/log4javascript.js" ></script> 
			<script type="text/javascript" src="../res/js/landing.js" ></script> 
			<script type="text/javascript" src="../res/js/messages.js" ></script> 
		<!-- 	// <script type="text/javascript" src="../res/js/complete-profile.js" ></script> --> 
			<script type="text/javascript" src="../res/js/jquery.imgareaselect.js"></script>
			<script type="text/javascript" src="../res/js/jquery.awesome-cropper.js"></script>

	
	<script src="../res/js/setting_loader.js"></script>
 <!-- <script src="../res/js/change-profile.js"></script> --> 
 
	<link rel="stylesheet" href="settings.css"> 
</head>
<body>
	<div class="container-fluid">
		<%@ include file="../res/includes/header.jsp" %>
		<!--==New Request==-->
		<!--====-->
		<div id="content" class="row">
			<!--==content left============== -->
			<div id="main_content_left" class="col-xs-2 col-xs-offset-1">				
					<!-- <div class="row left-link profile-pic" id="profile-pic-section">
							
					</div> -->
					<ul class="nav nav-pills nav-stacked">
						<li class="active" id="profile_loader"><a href="#">Profile</a></li>
						<li id="passwordLoader"><a href="">Password Reset</a></li>
						<li id="emailNotificationsLoader"><a href="">Email Notifications</a></li>
					</ul>	
			</div><!--=============content left============-->
			<!--==content middle============== -->
			<div id="main_content_middle" class="col-xs-6">
				<div id="errorArea"></div>
				 <div class="settings_page">
                    <div class="text-center">
						<img src="../res/images/loading_site_green_small.gif">
						<span> .......... Please wait as data is being loaded</span>
					</div>
				 </div>
			</div>
			<!--=========== content middle-->
			<!--==content right============== -->
			<div id="main_content_right" class="col-xs-3">				 
			</div><!--=========== content right====-->
			<!--===========================================================================================-->
		</div> <!--/content-->
		<!-- footer========================== -->
		<%@ include file="../res/includes/footer.jsp" %>
		<!-- ===end footer========================= -->
	</div> <!-- container-fluid -->
</body>
</html>
<%
}
%>
