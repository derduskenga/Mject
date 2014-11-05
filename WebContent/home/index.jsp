<%@page import="javax.servlet.http.HttpSession"%>
<% if(request.getSession().getAttribute("entity_id")==null){
	response.sendRedirect("../login/");
}else{
%>
<!DOCTYPE html>
<html lang="en-us">
<head>
<meta charset="utf-8">

<title>HaraMbesa | Home</title>		

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
<link rel="stylesheet" type="text/css" href="../res/tipsy/css/main.css" />


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
<script type="text/javascript" src="../res/js/notification_rest_handle.js" ></script>
<script type="text/javascript" src="../res/tipsy/js/jquery.tipsy.js"></script>


<script type="text/javascript" src="../res/js/messages.js" ></script>
<script type="text/javascript" src="../res/js/jquery.loadingdotdotdot.js"></script>  

<script type="text/javascript">
	//$(function() {
		//$('#').tipsy();
	//});
</script>

</head>

<body>
	<div class="container-fluid">
		<%@ include file="../res/includes/header.jsp"%>
		<!--==New Request==-->
		<!-- Modal -->

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
				<div id="req-filter" class="row filters" align="center">
					
					<button class="btn btn-primary" id="donation-request-button">
						New Donation Request
					</button>
					
					<a href="../offer/" class="btn btn-default" role="button">Offers</a>					
					
				</div>
				<div id="donation-request-holder" class="col-xs-10 hidden">
					<form class="form-horizontal" name="donation_request_form" id="donation-request-form">
						<fieldset>
							<legend>New donation request</legend>
							
							<div class="alert alert-danger hidden" id="post_result">

							</div> 

							<div class="form-group">
								<label class="col-xs-4 control-label" for="donation-category">Category:</label>
								<div class="col-xs-6">
									<select class="form-control input-sm" id="donation_category"  name="donation_category" role="menu">	
										      
									</select>

									<span id="loading_category" class="hidden">
									<img  src="../res/images/loading.gif" /> Loading categories
									</span>
								</div> 
							</div>
							
							<div class="form-group">
								<label class="col-xs-4 control-label" for="f_date">Funding Date:</label>
								<div class="col-xs-6">
									<input type="text" placeholder="Click to select date" readonly='true' class="form-control input-sm" id="f_date" name="f_date">
								</div>
							</div>
							
							<div class="form-group">
								<label class="col-xs-4 control-label" for="currency">Currency:</label>
								<div class="col-xs-6">
									<select class="form-control input-sm" id="donation_currency" name="donation_currency" role="menu">	

									</select>

									<span id="loading_currency" class="hidden">
										<img  src="../res/images/loading.gif" /> Loading Currency
									</span>
								</div> 
							</div> 
									
							<div class="form-group">
								<label class="col-xs-4 control-label" for="doation_amount">Request Amount:</label>
								<div class="col-xs-6">
									<input type="text" class="form-control input-sm" name="donation_amount" id="donation_amount">
								<%--  								<span id="checking-username" class="hidden"> </span>  --%>
								</div>		
							</div>
								    
							<div class="form-group">
								<label class="col-xs-4 control-label" for="summary">Request Summary:</label>
								<div class="col-xs-6">
									<input type="text" class="form-control input-sm" name="summary" id="summary">
								</div>
							</div>
									
								
							<div class="form-group">
								<label class="col-xs-4 control-label" for="details">Details:</label>
								<div class="col-xs-6">
									<textarea class="form-control input-sm" rows="3" name="details" id="details"></textarea>
								</div>		
							</div>
								
							<div class="form-group" align="center">
								<div class="row">
									<button class="btn btn-default" id="cancel-donation-request">
										Cancel
									</button>
									
								
									
									<button type="submit" class="btn btn-primary" id="offer-post-sub">
										Post request
									</button>
								</div>		
							</div>
						</fieldset>	
					</form>  
				 </div>
				<div id="comment-and-post" class="donation-request row col-xs-12">
				
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
			
			<div class="hidden" id="post-status"></div>
<%--  				============================================================================start of message post owner	  --%>
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
			<%-- ======================================================================================== start donate dialog  --%>

								<div class="modal fade" id="donate-modal" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
									<div class="modal-dialog">
										<div class="modal-content">
										<form role="form" class="form-horizontal" id="donate-form">
												<div class="modal-header">
													<button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-times"></i></button>
													<h4 class="modal-title" id="myModalLabel">Donate to cause</h4>
												</div>
												
												<div class="modal-body" id="donate-modal-body">
													  <div id='breadcrumb-status' class='row col-sm-offset-1 hidden'>  <span class='breadcrumb-state' id='status-1'>1. Enter amount</span>  <span id='status-2' class= "breadcrumb-state-greyed">2. Payment options</span> <span id='status-3' class="breadcrumb-state-greyed">3. Finish payment</span> </div>
														<br>
													<div id="message-error">
														
													</div>
													
													<div id="pay-by-harambesa-option" class='row col-sm-offset-1'>
														
													</div>
													
													<div id='donor-donee' class='hidden'> 
														
													</div>
													
													<div id="beneficiary-name" class="hidden">
													
													</div>
													
													<div id="dustbin" class='hidden'>
														
													</div>
													
													<div id="donation-source-type" class="hidden">
													      
													</div>
													
<%--  													not hidden  --%>
													<div id="requested-amount" class="hidden">
														
													</div>
													
<%--  													not hidden  --%>
													<div class="hidden" id ="request-id">
													
													</div>
													
													<div id="final-feedback-message" class='row col-sm-offset-1 hidden'>
														 
													</div>
													
													<div id='donation-options' class='payment-options row col-sm-offset-1 hidden'>
													
														<div class="radio">
															<label>
																<input type="radio" name="payment_option_radios" id="harambesa-option" value="">
																From harambesa wallet
															</label>
														</div>
														
														<div class="radio">
															<label>
																<input type="radio" name="payment_option_radios" id="aggregator-option" value="">
																Others (Visa, Mastercard, M-PESA,Airtel money).
															</label>
														</div>
													
													</div>
													<br> 
													<div id="aggregator-option-area" class="hidden row col-sm-offset-1">
														<img src="../res/images/kenga.PNG">
													</div>
													
													<div id = "radio-buttons-error" class='row col-sm-offset-2'>
														
													</div>
													
													<div id="new-donation-display-area" class="hidden">
														
														<div id="info-area" class="info-alert-padding">
															
														
														</div>
												
														<div class="form-group">
															
															
															<label class="col-lg-4 control-label" for="currency_donate">Currency:</label>
															
															<div class="col-sm-7">
																<select class="form-control" id="donation_currency_donate" name="donation_currency_donate" role="menu">	

																</select>

																<span id="loading_currency_donate" class="hidden">
																	<img  src="../res/images/loading.gif" /> Loading Currency
																</span>
															</div> 
														</div> 
															
														<div class="form-group">
															<label class="col-sm-4 control-label" for="donation_amount_donate">Your donation Amount:</label>
															
														      <div class="col-sm-7">
																<input type="text" class="form-control" name="donation_amount_donate" id="donation_amount_donate">
															
															</div>		
														</div>
														
														<div class='form-group'>
															<div class="col-sm-7 col-sm-offset-4 checkbox">
																<label>
																	<input name='confirm_terms_conditions' id='confirm-terms-conditions' type="checkbox" value=""> Confirm donation <a href='#'>Terms and conditions</a>
																</label>
															</div>
														</div>
													</div>
													
													<div id="dialog-id" class="hidden">
															
													</div>
												</div>
												<div class="modal-footer" id="donate-footer">
													
<%--  													<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>  --%>
<%--  													<button type='button' class='btn btn-default' data-dismiss='modal'>Cancel</button>  --%>
<%--  													<button id="send-message-to-post-owner" type="submit" class="btn btn-primary">Send</button>  --%>
<%--  													<button id='send-message-to-post-owner' type='submit' class='btn btn-primary'>Send</button>  --%>
												</div>
											</form>
										</div>
									</div>
								</div>
<%--  			========================================================================================end donate dialog  --%>
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




<%--  			=======================================================================================start invite Modal  --%>
			
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
				<div id="empty-donation-request-comment" class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
<%--  							header section    --%>
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal" aria-hidden="true" id="empty-request-comment-button-close">
									&times;
								</button>
								<h4 class="modal-title" id="moadal-label">Response</h4>
							</div>
<%--  							body section   --%>
							<div class="modal-body" id="empty-request-comment-action-body">
								
							</div>
<%--  							footer section  --%>
							<div id="connect-modal-footer" class="modal-footer">
								<button type="button" class="btn btn-default" data-dismiss="modal" id="modal-cancel-button">Cancel</button>
							</div>
						</div>
					</div>
				</div>
				
				
				
		</div> <!--/content-->
		<!-- footer========================== -->
		
		<%@ include file="../res/includes/footer.jsp" %>
		
		<!-- end footer========================= -->
	</div> <!-- container-fluid -->
</body>
</html>
<%}%>