<div id="payment" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
<%--  header section    --%>
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true" id="make-payment-button-close">
					&times;
				</button>
				<h4 class="modal-title breadcrumb-state" id="moadal-label">Make payment</h4>
			</div>
<%--  							body section   --%>
			<div class="modal-body payment-modal-padding" id="make-payment">
				<div id='breadcrumb-status' class='row new-comment'> 
					<span class='breadcrumb-state' id='status-p-1'>1. Select option</span>  <span id='status-p-2' class= "breadcrumb-label breadcrumb-state-greyed">2. Supply transaction details</span> <span id='status-p-3' class="breadcrumb-label breadcrumb-state-greyed">3. Complete </span> 
				</div>
				
				<div id="param-area" class="hidden">
					
				</div>
				
				<div id="selected-option" class="hidden">
				
				</div>
				
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
							
							<div class="col-sm-4">
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
							<button type="button" class="btn btn-default" data-dismiss="modal" id="make-payment-submit-btn">
								Cancel
							</button>
							
							<button type="submit" class="btn btn-primary" id="make-payment-submit-btn">
								Submit details <span class="glyphicon glyphicon-ok"></span>
							</button>
						</div>
						
					</form>
				</div>
			</div>
			<%-- footer section  --%>
			<div id="make-payment-modal-footer" class="modal-footer">
			
			</div>
		</div>
	</div>
</div> 