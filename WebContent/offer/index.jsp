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
<script type="text/javascript" src="../res/js/landing_offer.js"></script>
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
<script type="text/javascript" src="../res/js/email_invite.js" ></script>
<script type="text/javascript" src="../res/js/notification_rest_handle.js" ></script>
<script type="text/javascript" src="../res/js/messages.js" ></script>
<script type="text/javascript" src="../res/js/jquery.loadingdotdotdot.js"></script>   
</head>
<body>
	<div class="container-fluid">
		<%@ include file="../res/includes/header.jsp"%>
		<%@ include file="modal_include.jsp"%>
		<!--==New Request==-->
		<!-- Modal -->

		<!--====-->
		<div id="content" class="row">
			
			
			<!--==content left============== -->
			<div id="main_content_left" class="col-xs-2 col-xs-offset-1">
				<div class="row left-link profile-pic" id="profile-pic-section">
<%--  					<a href=''><img src=''></a>  --%>
				</div>
				
				<div id="invite-button" class="col-xs-2 col-xs-offset-1">
					<a href="#" class="btn btn-md btn-primary" data-toggle="modal" data-target="#invite-friends">Invite friends</a>
				</div>
				
			</div><!--=============content left============-->
			
			<!--==content middle============== -->
			<div id="main_content_middle" class="col-xs-6">
				<div id="req-filter" class="row filters" align="center">
					
					<a href="../home/" class="btn btn-default" role="button">Donation Requests</a>
					
					<%-- <button class="btn btn-default" data-toggle="modal" data-target="#post_request_form">
						New Donation Request
					</button> --%>
					
					<button class="btn btn-primary" id="new-offer-btn">
						New Offer
					</button> 
				</div>
				
				
				<div id="offer-options-holder" class="row col-xs-12 hidden alert-info" align="center">
					
					<div id="option-label" class="row col-xs-12">
						Please Choose you offer type
					</div>
					<br>
					
					
					<div id="" class="row col-xs-3 col-xs-offset-1" name="">						
						<label>
							<input type="radio" name="offer_option_radio" id="money-option">
							Offer money
						</label>
					</div>
					
					<div id="" class="row col-xs-3" name="">
						<label>
							<input type="radio" name="offer_option_radio" id="service-option">
							Offer service
						</label>
					</div>
					
					<div id="" class="row col-xs-3" name="">
						<label>
							<input type="radio" name="offer_option_radio" id="material-option">
							Offer material
						</label>
					</div>
					
					<br>
				</div>
				
				
				<div id="offer-loading-gif" class="row col-xs-8 col-xs-offset-2 hidden" align="center">
					<img src="../res/images/loading_messages.gif">Posting offer...</img>
				</div>
				
				<div id="m-form-holder" class="row col-xs-8 col-xs-offset-2 hidden">
					<%--This section holds a form for money offers  --%>
					<form class="form-horizontal" name="m-offer-form" id="m-offer-form" role="form"> 
						<fieldset>
							<legend>Create a new money offer</legend>
							<div class="form-group">
								<label for="input-amount-label" class="row col-xs-4">Amount:</label>
								<div class="row col-xs-8">
									<input type="text" class="form-control input-sm" id="offer-amount" name="offer_amount">
								</div>
							</div>
							
							<div class="form-group">
								<label class="row col-xs-4" for="currency">Currency:</label>
								<div class="row col-xs-8">
									<select class="form-control input-sm" id="donation_currency" name="donation_currency" role="menu">	
										
									</select>
									
									<span id="loading_currency" class="hidden">
										<img  src="../res/images/loading.gif" /> Loading Currency
									</span>
								</div> 
							</div>
							
							<div class="form-group">
								<label for="input-program-label" class="row col-xs-4">Category:</label>
								<div class="row col-xs-8">
									<select class="form-control input-sm" id="donation_category" name="donation_category" role="menu">
									
									</select>									
									<span id="loading_category" class="hidden">
									<img  src="../res/images/loading.gif" /> Loading categories
									</span>
								</div> 
								  
							</div>
							
							
							<div class="form-group">
								<label for="input-summery-label" class="row col-xs-4">Offer summary:</label>
								<div class="row col-xs-8">
									<input type="text" class="form-control input-sm" id="offer-summary" name="offer_summary">
								</div>
								
							</div>
							
							<div class="form-group">
								<label class="row col-xs-4" for="input-detail-label">Offer description:</label>
								<div class="row col-xs-8">
									<textarea class="form-control input-sm" rows="3" name="offer_details" id="offer-details"></textarea>
								</div>
							
							</div>
							
							<div class="form-group" align="center">
								<button class="btn btn-default" id="offer-post-cancel-btn">
									Cancel
								</button>
								<button class="btn btn-info" id="offer-post-reset-btn">
									Reset
								</button>
								<button type="submit" class="btn btn-primary custom-btn-submit" id="offer-post-submit-btn">
									Post offer
								</button>
							</div>
						</fieldset>
					</form>
				</div>
				
				<div id="s-form-holder" class="row col-xs-8 col-xs-offset-2 hidden">
					<form class="form-horizontal" id="s-offer-form" name="s_offer_form" role="form">
						<fieldset>
							<legend>Create a new service offer</legend>
							
							<div class="form-group">
								<label for="input-service-category-label" class="row col-xs-4">Select service:</label>
								<div class="row col-xs-8">
									<select class="form-control input-sm" id="service-category" name="service_category" role="menu">
									
									</select>
									<span id="loading_services" class="hidden">
										<img  src="../res/images/loading.gif" /> Loading services...
									</span>
								</div> 
								  
							</div>
							
							<div class="form-group">
								<label for="input-country-label" class="row col-xs-4">Location:</label>
								<div class="row col-xs-8">
									<select class="form-control input-sm" id="service-countries" name="service_countries" role="menu">
									
									</select>
									<span id="loading_countries" class="hidden">
										<img  src="../res/images/loading.gif"/> Loading countries...
									</span>
								</div> 
							</div>
							
							<div class="form-group">
								<label for="input-state-label" class="row col-xs-4"></label>
								<div class="row col-xs-8">
									<input type="text" placeholder="Enter state or province" class="form-control input-sm" id="state" name="state">
								</div>
							</div>
							
							<div class="form-group">
								<label for="input-residence-label" class="row col-xs-4"></label>
								<div class="row col-xs-8">
									<input type="text" placeholder="Enter town or residence" class="form-control input-sm" id="residence" name="residence">
								</div>
							</div>
							
							<div class="form-group">
								<label for="input-hrs-a-day-label" class="row col-xs-4">Service Hrs per day:</label>
								<div class="row col-xs-8">
									<input type="text" placeholder="Enter number of service hours each day" class="form-control input-sm" id="hours-a-day" name="hours_a_day">
								</div>
							</div>
							
							<div class="form-group">
								<label for="input-hrs-start-date-label" class="row col-xs-4">Starting date:</label>
								<div class="row col-xs-8">
									<input type="text" readOnly="true" placeholder="Start date (Click to select)" class="form-control input-sm" id="start-date" name="start_date">
								</div>
							</div>
							
							<div class="form-group">
								<label class="row col-xs-4" for="input-detail-label">Offer description:</label>
								<div class="row col-xs-8">
									<textarea class="form-control input-sm" rows="3" name="service_offer_details" id="service-offer-details" placeholder="Give your offer description"></textarea>
								</div>
							</div>
							
							<div class="form-group row col-xs-12" align="center">
								<button class="btn btn-default" id="service-offer-post-cancel-btn">
									Cancel
								</button>
								<button class="btn btn-info" id="service-offer-post-reset-btn">
									Reset
								</button>
								<button type="submit" class="btn btn-primary custom-btn-submit" id="service-offer-post-submit-btn">
									Post offer
								</button>
							</div>
							
							
							
						</fieldset>
					</form>
				</div>
				
				<div id="g-form-holder" class="row col-xs-8 col-xs-offset-2 hidden">
					<form class="form-horizontal" id="g-offer-form" name="g_offer_form" role="form" enctype="multipart/form-data">
						<fieldset>
							<legend>Create a new material offer</legend> 
							
							<div class="form-group">
								<label for="input-material-category-label" class="row col-xs-4">Select material category:</label>
								<div class="row col-xs-8">
									<select class="form-control input-sm" id="material-category" name="material_category" role="menu">
									
									</select>
									<span id="loading_material" class="hidden">
										<img  src="../res/images/loading.gif" /> Loading material...
									</span>
								</div> 
								  
							</div>
							
							
							<div class="form-group">
								<label for="input-item-name-label" class="row col-xs-4">Item name</label>
								<div class="row col-xs-8">
									<input type="text" placeholder="Enter item name e.g. trouser, shoes etc..." class="form-control input-sm" id="material-name" name="material_name">
								</div>
							</div>
							
							<div class="form-group">
								<label for="input-item-units-label" class="row col-xs-4">No. of items</label>
								<div class="row col-xs-8">
									<input type="text" placeholder="Enter number of items to offer" class="form-control input-sm" id="material-units" name="material_units">
								</div>
							</div>
							
							<div class="form-group">
								<label for="input-material-country-label" class="row col-xs-4">Location</label>
								<div class="row col-xs-8">
									<select class="form-control input-sm" id="material-countries" name="material_countries" role="menu">
									
									</select>
									<span id="loading_countries_materials" class="hidden">
										<img  src="../res/images/loading.gif"/> Loading countries...
									</span>
								</div> 
							</div>
							
							<div class="form-group">
								<label for="input-state-label" class="row col-xs-4"></label>
								<div class="row col-xs-8">
									<input type="text" placeholder="Enter state or province" class="form-control input-sm" id="material-state" name="material_state">
								</div>
							</div>
							
							<div class="form-group">
								<label for="input-material-residence-label" class="row col-xs-4"></label>
								<div class="row col-xs-8">
									<input type="text" placeholder="Enter town or residence" class="form-control input-sm" id="material-residence" name="material_residence">
								</div>
							</div>
							
							<div class="form-group" id="item-photos-main">
								<label for="input-files-label" class="row col-xs-4">Item photos</label>
								<div class="row col-xs-8">
									<div class="fileinput fileinput-new" data-provides="fileinput">
										
										<div class="fileinput-new thumbnail" style="width: 150px; height: 110px;">
											<img data-src="holder.js/100%x100%" alt="Main photo">
										</div>
										
										<div class="fileinput-preview fileinput-exists thumbnail" style="max-width: 200px; max-height: 150px;">
											
										</div>
										<div>
											<span class="btn btn-default btn-file"><span class="fileinput-new">Select photo</span><span class="fileinput-exists">Change</span><input type="file" id="photo-main" name="photo_main"></span>
											<a href="#" class="btn btn-default fileinput-exists" data-dismiss="fileinput">Remove</a>
										</div>
											  
									</div>
								</div>
							</div>
							
							<div class="form-group" id="item-photos-other-one">
								<label for="input-files-label" class="row col-xs-4"></label>
								<div class="row col-xs-8">
									<div class="fileinput fileinput-new" data-provides="fileinput">
										
										<div class="fileinput-new thumbnail" style="width: 150px; height: 110px;">
											<img data-src="holder.js/100%x100%" alt="add photo">
										</div>
										
										<div class="fileinput-preview fileinput-exists thumbnail" style="max-width: 200px; max-height: 150px;">
											
										</div>
										<div>
											<span class="btn btn-default btn-file"><span class="fileinput-new">Select photo</span><span class="fileinput-exists">Change</span><input type="file" name="other_photos_one" id="other-photos-one"></span>
											<a href="#" class="btn btn-default fileinput-exists" data-dismiss="fileinput">Remove</a>
										</div>
											  
									</div>
								</div>
							</div>
												    
							<div class="form-group" id="item-photos-other-two">
								<label for="input-files-label" class="row col-xs-4"></label>
								<div class="row col-xs-8">
									<div class="fileinput fileinput-new" data-provides="fileinput">
										
										<div class="fileinput-new thumbnail" style="width: 150px; height: 110px;">
											<img data-src="holder.js/100%x100%" alt="add photo">
										</div>
										
										<div class="fileinput-preview fileinput-exists thumbnail" style="max-width: 200px; max-height: 150px;">
											
										</div>
										<div>
											<span class="btn btn-default btn-file"><span class="fileinput-new">Select photo</span><span class="fileinput-exists">Change</span><input type="file" id="other-photos-two" name="other_photos_two"></span>
											<a href="#" class="btn btn-default fileinput-exists" data-dismiss="fileinput">Remove</a>
										</div>
											  
									</div>
								</div>
							</div>
									  
							<div class="form-group">
								<label class="row col-xs-4" for="input-detail-label">Offer description:</label>
								<div class="row col-xs-8">
									<textarea class="form-control input-sm" rows="3" name="material_offer_details" id="material-offer-details" placeholder="Give your offer description"></textarea>
								</div>
							</div>
							
							<div class="form-group row col-xs-12" align="center">
								<button class="btn btn-default" id="material-offer-post-cancel-btn"> 
									Cancel
								</button>
								<button class="btn btn-info" id="material-offer-post-reset-btn">
									Reset
								</button>
								<button type="submit" class="btn btn-primary custom-btn-submit" id="material-offer-post-submit-btn">
									Post offer
								</button>
							</div>
							
						</fieldset>
					</form>
				</div>
				
				<div id="offer-post" class="donation-request row col-xs-12">
				<%--posts will be appended here  --%>
				
				
				
				
				
				</div>
			
			<div><!--load more content here --!>
				</div>
			</div><!--=========== content middle-->
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
				<!--====online friends -->
				<div class="online-friends">
					<div class="row">
<%--  						online friends are loaded here  --%>
					</div>
				</div>
				<!--====end online friends -->
			</div><!--=========== content right====-->
			<div class="hidden" id="post-status"></div><!--===========================================================================================-->
			
<%--  			==========================================start invite Modal    --%>
			
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
			
<%--  			=======================================================================================end Invite modal  --%>
				
<%--  				 ==================================================start========================  --%>

				<div id="application-confirm-service-modal" class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
					<div class="modal-dialog modal-sm" id="confirm-modal-container">
						<div class="modal-content"> 
<%--  							header section    --%>
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal" aria-hidden="true" id="connect-button-close">
									&times;
								</button>
								<h4 class="modal-title" id="moadal-label">Confirm action</h4>
							</div>
<%--  							body section   --%>
							<div class="modal-body" id="">
								<div id="confirm-modal-body-container" class="col-sm-offset-1">
									<div id="confirm-wait-gif" class="hidden ">
										<img src="../res/images/loading_messages.gif">Please wait...</img>
									</div>
									<div id="service-confirm-area" class="hidden">
										    
									</div>
									
									<div id="service-confirm-area-message" class="">
										
									</div>
									
									<div id="confirm-message" class="">
										
									</div>
									
									<div id="payment-source-error-alert" class="">
										
									</div>
									
									<div id='offer-acceptpance-pay-options' class=' payment-options hidden'>
										<div class="radio" id="payment-wallet-container">
											<label>
												<input type="radio" name="acceptpance_payment_option_radios" id="acceptpance-harambesa-option" value="">
												From harambesa wallet
											</label>
										</div>
										
										<div class="radio" id="payment-aggregator-container">
											<label>
												<input type="radio" name="acceptpance_payment_option_radios" id="acceptpance-aggregator-option" value="">
												Others (Visa, Mastercard, M-PESA, Yu cash, Airtel money).
											</label>
										</div>
									</div>
								</div>
								
							</div>
<%--  							footer section  --%>
							<div id="application-confirm-modal-footer" class="modal-footer">
								<button type="button" class="btn btn-default" id="confirm-application-cancel-btn">I changed my mind</button>
								<button type="button" class="btn btn-primary" id="confirm-application-accept-btn">Accept</button>
							</div>
						</div>
					</div>
				</div>
<%--  		===================================end===================================================================  --%>
<%--  		============================================================================start of message post owner	  --%>
						<!--start message-->
								<div class="modal fade" id="message-modal" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
									<div class="modal-dialog">
										<div class="modal-content">
										<form role="form" class="form-horizontal" id="message-form-home">
												<div class="modal-header">
														<button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-times"></i></button>
														<h4 class="modal-title" id="myModalLabel">Send message</h4>
												</div>
												<div class="modal-body" id="message-modal-body-home">
													<div id="message-error-success">
														
													</div>
													<div id="new-message-display-area-home" class='hidden'>		
															
															<div class="form-group">
																	<div class="col-lg-12">
																				<div class="input-group">
																						<span class="input-group-addon">
																							<span class="">To:</span>
																						</span>
																						<input type="text" class="form-control input-sm" id="send-message-textbox-home" name="to" placeholder="Name">
																				</div>
																	</div>
															</div>
															
															<div class="form-group row" id="message-text-div">
																	<div class="col-lg-12">
																		<textarea type="textarea" class="form-control" rows="5" name="message" id="message-text-area-home" placeholder="Type your message here..."></textarea>
																	</div>
															</div>
													</div>
													
													<div id="dialog-id" class="hidden">
															
													</div>
												</div>
												<div class="modal-footer" id="send-message-footer-home">
<%--  													<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>  --%>
<%--  													<button type='button' class='btn btn-default' data-dismiss='modal'>Cancel</button>  --%>
<%--  													<button id="send-message-to-post-owner" type="submit" class="btn btn-primary">Send</button>  --%>
<%--  													<button id='send-message-to-post-owner' type='submit' class='btn btn-primary'>Send</button>  --%>
												</div>
											</form>
										</div>
									</div>
								</div>
			<!--end message -->
<%--  			======================================================================================== end of message post owner--%>
<%--  			======================================================================================== start of connect with messaged owner  --%>
			
				<div id="recommendation-action-connect" class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
					<div class="modal-dialog modal-sm">
						<div class="modal-content">
<%--  							header section    --%>
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal" aria-hidden="true" id="connect-button-close">
									&times;
								</button>
								<h4 class="modal-title" id="moadal-label">Send a connection request</h4>
							</div>
<%--  							body section   --%>
							<div class="modal-body" id="connect-action-body">
								
							</div>
<%--  							footer section  --%>
							<div id="connect-modal-footer" class="modal-footer">
<%--  								<button type="button" class="btn btn-default" data-dismiss="modal" id="modal-cancel-button">Cancel</button>  --%>
							</div>
						</div>
					</div>
				</div>
			
<%--  			======================================================================================== end of connect with messaged owner  --%>

<%--  START  --%>
 

<%--  END  --%>

		</div> <!--/content-->
		<!-- footer========================== -->
		
		<%@ include file="../res/includes/footer.jsp" %>
		
		<!-- end footer========================= -->
	</div> <!-- container-fluid -->
</body>
</html>
<%}%>