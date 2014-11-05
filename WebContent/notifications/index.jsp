<%@page import="javax.servlet.http.HttpSession"%>
<% if(request.getSession().getAttribute("entity_id")==null){
	response.sendRedirect("../login/");
}else{
%>
<!DOCTYPE html>
<html lang="en-us">
<head>
<meta charset="utf-8">

<title>HaraMbesa:Notifications</title>		

<meta name="description" content="">
<meta name="author" content="">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<link rel="stylesheet" type="text/css" media="screen" href="../res/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/jasny-bootstrap/css/jasny-bootstrap.min.css"></link>
<link rel="stylesheet" type="text/css" media="screen" href="../res/css/stylez.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/css/styleX.css">

<link rel="stylesheet" type="text/css" media="screen" href="../res/bootstrap-datepicker/css/datepicker.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/bootstrap-datepicker/css/datepicker3.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/bootstrap-datepicker/css/style.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/bootstrap-datepicker/css/datepicker.css"></link>
<link rel="stylesheet" type="text/css" media="screen" href="../res/select/select2.css"/>
<link rel="stylesheet" type="text/css" media="screen" href="../res/css/select2.css">

<script type="text/javascript" src="../res/js/jquery.js"></script>
<script type="text/javascript" src="../res/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
<%--  <script type="text/javascript" src="../res/js/landing_home.js"></script>  --%>
<script type="text/javascript" src="../res/js/landing_home.js"></script>
<script type="text/javascript" src="../res/js/global.js"></script>
<script type="text/javascript" src="../res/bootstrap/js/bootstrap.min.js"></script> 
<script type="text/javascript" src="../res/bootstrap-validator/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src="../res/jasny-bootstrap/js/jasny-bootstrap.min.js"></script>
<script type="text/javascript" src="../res/js/post.messaging.js"></script>
<script type="text/javascript"  src="../res/select/select2.js"></script>
<script type="text/javascript" src="../res/js/log4javascript.js" ></script> 
<script type="text/javascript" src="../res/js/import.js" ></script>
<script type="text/javascript" src="../res/js/payment.js" ></script>
<script type="text/javascript" src="../res/js/email_invite.js" ></script>
<script type="text/javascript" src="../res/js/search.js" ></script>
<script type="text/javascript" src="../res/tipsy/js/jquery.tipsy.js"></script>
<script type="text/javascript" src="../res/js/notification_rest_handle.js"></script>


<script type="text/javascript" src="../res/js/messages.js" ></script>
<script type="text/javascript" src="../res/js/jquery.loadingdotdotdot.js"></script>   
</head>

<body>
	<div class="container-fluid">
		<%@ include file="../res/includes/header.jsp" %>
		<!--==New Request==-->

		<!--====-->
		<div id="content" class="row">
			<!--==content left============== -->
			<div id="main_content_left" class="col-xs-2 col-xs-offset-1">
				<div class="left-link profile-pic" id="profile-pic-section">

				</div>
				
				<div id="invite-button" class="col-xs-2">
					<a href="#" class="btn btn-md btn-primary" data-toggle="modal" data-target="#invite-friends">Invite friends</a>
				</div>
			</div><!--=============content left============-->
			
			
			<!--==content middle============== -->
			<div id="main_content_middle" class="col-xs-6">
				<div id="notifications">
					
				</div>
			</div>
			<!--=========== content middle-->
			
			
			
			<!--==content right============== -->
			<div id="main_content_right" class="col-xs-3">
				<div class="account-summary">
					<div class="harambesa-balance">
						<div id = "balance" class="row">
							HaraMbesa Balance: 
						</div>
						<div class="row pad-top">
							<a href="../deposits" class="pad-left">Deposit</a> &nbsp;|&nbsp;<a href="../withdraw" class="pad-left">Withdraw</a>
						</div>
					</div>
					<div class="social-points">
						<div id = 'points' class="row float-middle-ver">
							Social Points: 
						</div>
						<div class="row pad-top">
							<a href="../charity-market" class="pad-left">Put On Sale</a>
						</div>
					</div>
				</div>
			</div><!--=========== content right====-->
			<!--===========================================================================================-->
		</div> <!--/content-->
		<!-- footer========================== -->
		
		
		<div class="modal fade" id="invite-friends" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<form name="typed_email_invite" id="typed_email_invite">
								   
								<div class="modal-header"> <%-- start of header --%>
									<button type="button" class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></button>
									<h4 class="modal-title" id="myModalLabel">Select contact source</h4>
								</div> <%-- end of modal header --%>
								  
								    <div class="modal-body"> <%-- start of body section --%> 
									    
									    <div id="contact_source_options" class="row">
										    
										    <div id="google_option" class="col-xs-12">
											    <a href="https://accounts.google.com/o/oauth2/auth?client_id=717736851971-to50m7t8dqd96nb1f8spss316hbadhk6.apps.googleusercontent.com&redirect_uri=http://localhost:8080/mjet/invite/validate.jsp&scope=https://www.google.com/m8/feeds/&response_type=code" onclick="window.open('https://accounts.google.com/o/oauth2/auth?client_id=717736851971-to50m7t8dqd96nb1f8spss316hbadhk6.apps.googleusercontent.com&redirect_uri=http://localhost:8080/mjet/invite/validate.jsp&scope=https://www.google.com/m8/feeds/&response_type=code', 'newwindow', 'width=800, height=550'); return false;"> <img src="../res/images/gmail.jpg">Use Gmail contacts</a><br><br>
										    </div>
										    
										    <div id="or-option" class="col-xs-12">
											    OR <br><br>    
										    </div>
										    
										    <div id="" class="form-group">
											    <div class="col-lg-12">
												    <div id="user_input_option" class="input-group">
													    <span class="input-group-addon">
														    <span class="">
															    Enter Email:
														    </span>
													    </span>
													    <input class ="form-control input-sm" type="text" name="email" id="email" placeholder="Email address to invite">
												    </div>
											    </div>
											    
										    </div>
										  
									    </div>
								      
									    <div id="typed_mail_response_message" class="col-lg-12">
										    
									    </div>
						
								    </div> <%-- end of modal body --%>
								  
								    <div class="modal-footer"> <%-- start of footer section --%>
									    <div id="" class="form-group">
										    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
										    <button type="submit" class="btn btn-primary" id="send_mail_typed_mail">Invite</button>
									    </div>
								    </div> <%-- end of footer section --%>
							    
							</form>
						      
						</div>
					</div>
				  </div>
		
		
		<%@ include file="../res/includes/footer.jsp" %>
		
		<!-- end footer========================= -->
	</div> <!-- container-fluid -->
	
	
	

  </body>
</html>
<%}%>