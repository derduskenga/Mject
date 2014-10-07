<%@ page import = "java.sql.Connection"%>
<%@ page import = "java.sql.DriverManager"%>
<%@ page import = "java.sql.SQLException"%>
<%@ page import = "java.sql.Statement"%>
<%@ page import = "com.harambesa.DBConnection.DBConnection"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<html lang="en-us">
<head>
<meta charset="utf-8">

<title>HaraMbesa</title>

    <meta name="description" content="">
    <meta name="author" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

   
   
    <link rel="stylesheet" type="text/css" media="screen" href="../res/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" media="screen" href="../res/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" media="screen" href="../res/css/styleX.css">
    <link rel="stylesheet" type="text/css" media="screen" href="../res/bootstrap-datepicker/css/datepicker.css">
     <link rel="stylesheet" type="text/css" media="screen" href="../res/bootstrap-datepicker/css/datepicker3.css">
      <link rel="stylesheet" type="text/css" media="screen" href="../res/bootstrap-datepicker/css/style.css">
     <%--      <link rel="stylesheet" type="text/css" media="screen" href="../res/css/datepicker.css">  --%>

     <script type="text/javascript" src="../res/js/jquery.js"></script>
    <script type="text/javascript" src="../res/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
<%--      <script type="text/javascript" src="../res/js/datepicker.js"></script>  --%>
    <script type="text/javascript" src="../res/js/post_request_landing.js"></script>
    <script type="text/javascript" src="../res/bootstrap/js/bootstrap.min.js"></script> 
    <script type="text/javascript" src="../res/bootstrap-validator/js/bootstrapValidator.min.js"></script>
    <script type="text/javascript" src="../res/js/res.js"></script>
    <script type="text/javascript" src="../res/js/post.messaging.js"></script>
    
    <script type="text/javascript" src="../res/js/jquery.loadingdotdotdot.js"></script>
   
    
   
   
   <script type="text/javascript">

	/*$(document).ready(function(){

	    $("#send_mail_typed_mail").click(function(){

		var email = $("#email").val();
		
		$.post("../mapping/mapping.jsp",
		      {tag:"email_typed",
		      email:email},
		      
		      function(data){
		      $("#typed_mail_response_message").html(data);
		});

	    });

	});*/


    </script>

	
</head>

<body>
	<div class="container-fluid">
		<%@ include file="../res/includes/header.jsp" %>
		<!--==New Request==-->

		<!--====-->
		<div id="content" class="row">
			<!--==content left============== -->
			<div id="main_content_left" class="col-xs-2 col-xs-offset-1">
	
			</div><!--=============content left============-->
			
			
			<!--==content middle============== -->
			<div id="main_content_middle" class="col-xs-6">
			
				  <a href="#" class="btn btn-md btn-primary" data-toggle="modal" data-target="#basicModal">Invite friends</a>
				  
				  
			
			
			</div>
			<!--=========== content middle-->
			
			
			
			<!--==content right============== -->
			<div id="main_content_right" class="col-xs-3">
			</div><!--=========== content right====-->
			<!--===========================================================================================-->
		</div> <!--/content-->
		<!-- footer========================== -->
		
		<%@ include file="../res/includes/footer.jsp" %>
		
		<!-- end footer========================= -->
	</div> <!-- container-fluid -->
	
	
	
	<div class="modal fade" id="basicModal" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
	    <div class="modal-dialog">
		<div class="modal-content">
		    
		      <div class="modal-header"> <%-- start of header --%>
			  <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></button>
			  <h4 class="modal-title" id="myModalLabel">Select contact source</h4>
		      </div> <%-- end of modal header --%>
		      
		      <div class="modal-body"> <%-- start of body section --%>
			 
			 
			<div id="contact_source_options" class="row">
			
			<div id="google_option" class="col-xs-1 col-xs-offset-1"><a href="https://accounts.google.com/o/oauth2/auth?client_id=333266010332-lqm01tjkkfpm40p80le22t42bl5sdag0.apps.googleusercontent.com&redirect_uri=http://localhost:8080/mjet/connect/validate.jsp&scope=https://www.google.com/m8/feeds/&response_type=code" onclick="window.open('https://accounts.google.com/o/oauth2/auth?client_id=333266010332-lqm01tjkkfpm40p80le22t42bl5sdag0.apps.googleusercontent.com&redirect_uri=http://localhost:8080/mjet/connect/validate.jsp&scope=https://www.google.com/m8/feeds/&response_type=code', 'newwindow', 'width=800, height=550'); return false;"> <img src="../res/images/gmail.jpg">Gmail </a></div>
			
			<div id="or-option" class="col-xs-1 col-xs-offset-1"><br>OR</div>
			
			<form name="typed_email_invite" id="typed_email_invite">
			  <div id="user_input_option" class="col-xs-8 form-group"><br>Enter email: <input class ="" type="text" name="email" id="email" size="30"></div>
			  </hr>
			 <div align="right">
			    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			    <button type="submit" class="btn btn-primary" id="send_mail_typed_mail">Invite</button>
			  </div>
			</form>
			
			</div>
			
			  <div id="typed_mail_response_message"> </div>
  
		      </div> <%-- end of modal body --%>
		      
		      <div class="modal-footer"> <%-- start of footer section --%>
			  
		      </div> <%-- end of footer section --%>
		      
		</div>
	    </div>
	</div
      
	
	
	
	
</body>
</html>