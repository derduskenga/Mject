<%@page import="javax.servlet.http.HttpSession"%>
<% if(request.getSession().getAttribute("entity_id")==null){
	response.sendRedirect("../login/");
}else{
%>
<!DOCTYPE html>
<html lang="en-us">
<head>
<meta charset="utf-8">

<title>HaraMbesa:Payment</title>		

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

<script type="text/javascript" src="../res/js/global.js"></script>
<script type="text/javascript" src="../res/bootstrap/js/bootstrap.min.js"></script> 
<script type="text/javascript" src="../res/bootstrap-validator/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src="../res/jasny-bootstrap/js/jasny-bootstrap.min.js"></script>
<script type="text/javascript" src="../res/js/post.messaging.js"></script>
<script type="text/javascript"  src="../res/select/select2.js"></script>
<script type="text/javascript" src="../res/js/log4javascript.js" ></script> 
<script type="text/javascript" src="../res/js/landing.js" ></script> 
<script type="text/javascript" src="../res/js/import.js" ></script>
<script type="text/javascript" src="../res/js/withdraw.js" ></script>

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
	
			</div><!--=============content left============-->
			
			
			<!--==content middle============== -->
			<div id="main_content_middle" class="col-xs-6">
				<div id="withdraw" class="withdraw-modal-padding">
					
					<div id="withdraw-global-options-container">
						<div id="withdraw-mpesa-visa-airtel-container" class="row">
							<div id="withdraw-mpesa-option" class="col-sm-4">
								<div id="withdraw-mpesa-img" class="image-pad"> 
									<img id="" alt="M-pesa" src="../res/images/mpesa_logo.png" style="border-width:0px;"> 
								</div>
								<div id="withdraw-mpesa-btn"> 
									<button class="btn btn-primary" id="withdraw-mpesa-option-btn">Withdraw to M-Pesa</button>
								</div>
							</div>
							<div id="withdraw-airtel-option" class="col-sm-4">
								<div id="withdraw-airtel-img" class="image-pad-airtel"> 
									<img id="" alt="Airtel" src="../res/images/airtel_logo.jpg" style="border-width:0px;"> 
								</div>
								<div id="withdraw-airtel-btn"> 
									<button class="btn btn-primary" id="withdraw-airtel-option-btn"> Withdraw to Airtel</button>
								</div>

							</div>				  
							<div id="withdraw-bank-option" class="col-sm-4">
								<div id="withdraw-bank-img" class="image-pad-visa"> 
									<img id="" alt="Visa" src="../res/images/mpesa_logo.png" style="border-width:0px;"> 
								</div>
								<div id="withdraw-bank-btn"> 
									<button class="btn btn-primary" id="withdraw-bank-option-btn"> Withdraw to bank</button>
								</div>
							</div>
						</div>
							
						<hr>
						
						<div id="withdraw-selected-option" class="hidden">
						
						</div>
					</div>
					
					
					<div id="withdraw-mpesa-submission-form" class="hidden teller">
						<form role="form" class="form-horizontal" id="withdraw-mpesa-form">
							<div class="form-group">
								<label class="col-sm-4 control-label sm" for="">Enter amount:</label>
								
								<div class="col-sm-4">
									<input type="text" class="form-control input-sm" name="withdraw_mpesa_amount" id="withdraw-mpesa-amount">
								</div>
								
							</div>
							
							<div class="form-group">
								<label class="col-sm-4 control-label sm" for="">Mobile number:</label>
								
								<div class="col-sm-4">
									<input type="text" class="form-control input-sm" name="mobile_no" id="mobile-no">
								</div>
							</div>
							
							<div id="custom-withdraw-modal-footer-mpesa" class="modal-footer">							
								<button type="button" class="btn btn-default" id="make-withdraw-cancel-btn">
									Cancel
								</button>
								
								<button type="submit" class="btn btn-primary" id="make-withdraw-submit-btn">
									Submit request <span class="glyphicon glyphicon-ok"></span>
								</button>
								<div id="withdraw-mode" class="hidden"> 
								
								</div>
							</div>
						</form>
					</div>
					
					<div id="withdraw-bank-submission-form" class="hidden teller">
						<form id="withdraw-bank-form" name="" role="form" class="form-horizontal">
							<div class="form-group">
								<label class="col-sm-4 control-label sm" for="">Enter amount:</label>
								
								<div class="col-sm-2">
									<input type="text" class="form-control input-sm" name="withdraw_bank_amount" id="withdraw-bank-amount">
								</div>
								
							</div>
							
							<div class="form-group">
								<label class="col-sm-4 control-label sm" for="currency">Select currency:</label>
								<div class="col-sm-4">
									<select class="form-control input-sm" id="withdraw-currency" name="withdraw_currency" role="menu">	
										
									</select>
									<span id="loading-currency-withdraw" class="hidden">
										<img  src="../res/images/loading.gif" /> Loading currency
									</span>
								</div> 
							</div>
							
							<div class="form-group">
								<label class="col-sm-4 control-label sm" for="">Bank name:</label>
								
								<div class="col-sm-4">
									<input type="text" class="form-control input-sm" name="withdraw_bank_name" id="withdraw-bank-name">
								</div>
								
							</div>
							
							<div class="form-group">
								<label class="col-sm-4 control-label sm" for="">Bank branch:</label>
								
								<div class="col-sm-4">
									<input type="text" class="form-control input-sm" name="withdraw_bank_branch" id="withdraw-bank-branch">
								</div>
								
							</div>
							
							<div class="form-group">
								<label class="col-sm-4 control-label sm" for="">Account number/IBAN:</label>
								
								<div class="col-sm-4">
									<input type="text" class="form-control input-sm" name="withdraw_account_number" id="withdraw-account-number">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-4 control-label sm" for="">Full account names:</label>
								
								<div class="col-sm-4">
									<input type="text" class="form-control input-sm" name="withdraw_account_name" id="withdraw-account-name">
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-sm-4 control-label sm" for="">Bank swift code:</label>
								
								<div class="col-sm-3">
									<input type="text" class="form-control input-sm" name="withdraw_bank_swift_code" id="withdraw-bank-swift-code">
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-sm-4 control-label sm" for="">Bank physical address:</label>
								
								<div class="col-sm-4">
									<input type="text" class="form-control input-sm" name="withdraw_bank_physical_address" id="withdraw-bank-physical-address">
								</div>
							</div>
							
							<div id="custom-withdraw-modal-footer-bank" class="modal-footer">							
								<button type="button" class="btn btn-default" id="make-withdraw-cancel-btn-bank">
									Cancel
								</button>
								
								<button type="submit" class="btn btn-primary" id="make-withdraw-submit-btn-bank">
									Submit request <span class="glyphicon glyphicon-ok"></span>
								</button>
							</div>
						</form>
					</div>
					
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
		
		<%@ include file="../res/includes/footer.jsp" %>
		
		<!-- end footer========================= -->
	</div> <!-- container-fluid -->
	
	
	

  </body>
</html>
<%}%>