<%@page import="javax.servlet.http.HttpSession"%>
<% if(request.getSession().getAttribute("entity_id")==null){
	response.sendRedirect("../login/");
}else{
%>
<!DOCTYPE html>
<html lang="en-us">
<head>
<meta charset="utf-8">

<title>HaraMbesa:Offer</title>		

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
<script type="text/javascript" src="../res/js/payment.js" ></script>

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
				<div id="payment" class="payment-modal-padding">
					<h4>Choose a payment channel.</h4>
					<div id="global-options-container">
						<div id="mpesa-visa-airtel-container" class="row">
							<div id="mpesa-option" class="col-sm-4">
								<div id="mpesa-img" class="image-pad"> 
									<img id="" alt="M-pesa" src="../res/images/mpesa_logo.png" style="border-width:0px;"> 
								</div>
								<div id="mpesa-btn"> 
									<button class="btn btn-primary" id="mpesa-option-btn">Pay with M-Pesa</button>
								</div>
							</div>
											  
							<div id="visa-option" class="col-sm-4">
								<div id="visa-img" class="image-pad-visa"> 
									<img id="" alt="Visa" src="../res/images/visa_logo.png" style="border-width:0px;"> 
								</div>
								<div id="visa-btn"> 
									<button class="btn btn-primary" id="visa-option-btn"> Pay with Visa</button>
								</div>
							</div>
							
							<div id="airtel-option" class="col-sm-4">
								<div id="airtel-img" class="image-pad-airtel"> 
									<img id="" alt="Airtel" src="../res/images/airtel_logo.jpg" style="border-width:0px;"> 
								</div>
								<div id="airtel-btn"> 
									<button class="btn btn-primary" id="airtel-option-btn"> Pay with Airtel</button>
								</div>
							</div>
							
						</div>
							
						<hr>
						
						<div id="mastercard-paypal" class="row">
							<div id="mastercard-option" class="col-sm-4">
								<div id="mastercard-img" class="image-pad-mastercard"> 
									<img id="" alt="Mastercard" src="../res/images/mastercard_logo.png" style="border-width:0px;"> 
								</div>
								<div id="mastercard-btn"> 
									<button class="btn btn-primary" id="mastercard-option-btn"> Pay with MasterCard</button>
								</div>
							</div>
													      
							<div id="paypal-option" class="col-sm-4">
								<div id="paypal-img"> 
									<img id="" alt="Paypal" src="../res/images/paypal_logo.gif" style="border-width:0px;"> 
								</div>
								<div id="paypal-btn"> 
									<button class="btn btn-primary" id="paypal-option-btn"> Pay with Paypal</button>
								</div>
							</div>
						</div>
					</div>
					
					<div id="card-submission-form" class="hidden teller">
						<form role="form" class="form-horizontal" id="debit-card-form">
							<div class="form-group">
								<label class="col-sm-4 control-label sm" for="">Enter card number:</label>
								
								<div class="col-sm-5">
									<input type="text" class="form-control input-sm" name="debit_card_number" id="debit-card-number">
								</div>
								
							</div>
							
							<div class="form-group">
								<label class="col-sm-4 control-label sm" for="">Amount:</label>
								
								<div class="col-sm-4">
									<input type="text" class="form-control input-sm" name="amount" id="amount">
								</div>
								
							</div>
							
							<div class="form-group">
								<label class="col-sm-4 control-label" for="">Card CVC:</label>
								
								<div class="col-sm-3">
									<input type="text" class="form-control input-sm" name="card_cvv" id="card-cvv">
								</div>
								
								<div id="what-is-this-cvv" class="col-sm-5 teller-2">
									<a href="#" id="what-is-cvv"><span class="" for="">What is this?</span></a>
								</div>
								
							</div>
							
							<div class="form-group">
								<label class="col-sm-4 control-label" for="">Card Expiry:(MM/YYYY)</label>
								
								<div class="col-sm-4">
									<input type="text" class="form-control input-sm" name="card_expiry" id="card-expiry">
								</div>
								
							</div>
							
							<div class="form-group">
								<label class="col-sm-4 control-label" for="currency">Select currency:</label>
								<div class="col-sm-4">
									<select class="form-control input-sm" id="payment-currency" name="payment_currency" role="menu">	
										
									</select> 
									
									<span id="payment_loading_currency" class="hidden">
										<img  src="../res/images/loading.gif" /> Loading Currency
									</span>
								</div> 
							</div>
							
							<div id="custom-payment-modal-footer" class="modal-footer">							
								<button type="button" class="btn btn-default" id="make-payment-cancel-btn">
									Cancel
								</button>
								
								<button type="submit" class="btn btn-primary" id="make-payment-submit-btn">
									Submit details <span class="glyphicon glyphicon-ok"></span>
								</button>
								<div id="card-type" class="hidden"> 
								
								</div>
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
							<a href="" class="pad-left">Donate</a><a href="" class="pad-left">Withdraw</a>
						</div>
					</div>
					<div class="social-points">
						<div id = 'points' class="row float-middle-ver">
							Social Points: 
						</div>
						<div class="row pad-top">
							<a href="" class="pad-left">Put On Sale</a><a href="" class="pad-left">Transfer</a>
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