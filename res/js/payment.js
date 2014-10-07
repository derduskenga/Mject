$(document).ready(function(){ 
	  
	$('#payment_loading_currency').removeClass('hidden');
	loadCurrency();
	validateDebitCardForm();
	validateMpesaForm();
	finishMpesaTransaction();
	validateAirtelForm();
	finishAirtelTransaction();
	
	$('#mpesa-option-btn').click(function(){
		var type = "mpesa";
		setPaymentCommon(type);
		$('#selected-payment-method').html("<img id='' alt='mpesa' src='../res/images/mpesa_logo.png' style='border-width:0px;'> Option");
		//$('#make-payment-modal-footer').append("<button id='finish-mpesa-payment' type='button' class='btn-finish-mpesa-payment btn btn-primary'>Finish</button>");
		//
		
	});
	
	$('#visa-option-btn').click(function(){
		var type = "visa";
		setPaymentCommon(type);
		$('#selected-payment-method').html("<img id='' alt='Visa' src='../res/images/visa_logo.png' style='border-width:0px;'> Option");
	});
	
	$('#airtel-option-btn').click(function(){
		var type = "airtel";
		setPaymentCommon(type);
		$('#selected-payment-method').html("<img id='' alt='airtel' src='../res/images/airtel_logo.jpg' style='border-width:0px;'> Option");
	});
	
	$('#mastercard-option-btn').click(function(){
		var type = "mastercard";
		setPaymentCommon(type);
		$('#selected-payment-method').html("<img id='' alt='Visa' src='../res/images/mastercard_logo.png' style='border-width:0px;'> Option");
		
	});
	
	$('#paypal-option-btn').click(function(){
		var type = "paypal";
		setPaymentCommon(type);
		$('#selected-payment-method').html("<img id='' alt='Visa' src='../res/images/paypal_logo.gif' style='border-width:0px;'> Option");
	});
	
	$('#payment').on('hidden.bs.modal', function () {
		resetPaymentModalDefaults();
	});

	$('#what-is-cvv').click(function(e){
		e.preventDefault();
	});
	
	$(function (e){ $("#what-is-cvv").popover({
		html: 'true',
		placement: 'top',
		content: 'For MasterCard and Visa The CVC consists of three digits that you will find next to the signature field on the back of your credit card.'})
		.blur(function () {
		$(this).popover('hide');
		});
	});
	
	$('#card-expiry').datepicker({
		format: "mm/yyyy",
		viewMode: "months", 
		minViewMode: "months"
	});
	
	$('#payment').on('click','.back-button-to-options',function(e){
		  $('#card-submission-form').addClass('hidden');
		  $('#back-btn-to-options').remove();
		  $('#global-options-container').removeClass('hidden');
		  resetFormValidation("debit-card-form");
		  $('#selected-payment-method').html("");
		  $('#mpesa-submision').addClass('hidden');
		  $('#finish-mpesa-payment').remove();
		  $('#finish-airtel-payment').remove();
		   $('#airtel-submision').addClass('hidden');
		  $('#mpesa-code-error').addClass('hidden');
	});

	
	$('#make-payment-cancel-btn').click(function(e){
		 // alert("working");
		  resetFormValidation("debit-card-form");
	});

	
});

//functions
function finishMpesaTransaction(){
	$('#payment').on('click','.btn-finish-mpesa-payment',function(e){
		var card_type = $('#card-type').text();
		var amount = $('#mpesa-amount').text();
		var phone = $('#mpesa-phone').text();
		$('#finish-mpesa-payment').addClass("hidden");
		$('#finish-mpesa-loading-gif').removeClass("hidden");
			$.post('../mapping/payment.jsp',{tag:"check_mpesa_code",type:card_type,amount:amount,phone:phone}, function(result){
				if(result['success'] == 1){
					alert(result['message']);
					$('#finish-mpesa-payment').removeClass("hidden");
					$('#finish-mpesa-loading-gif').addClass("hidden");
				}else{
					if(result['redir'] != null){
						window.location.reload();
					}else{
						alert(result['message']);
						$('#finish-mpesa-loading-gif').addClass("hidden");
						$('#finish-mpesa-payment').removeClass("hidden");
					}
				}
			
	    }, 'JSON');
		
	  
	});
}

function finishAirtelTransaction(){
	$('#payment').on('click','.btn-finish-airtel-payment',function(e){
		var card_type = $('#card-type').text();
		var amount = $('#airtel-amount').text();
		var phone = $('#airtel-phone').text();
		
		$('#finish-airtel-payment').addClass('hidden');
		$('#finish-airtel-loading-gif').removeClass('hidden');
		
		$.post('../mapping/payment.jsp',{tag:"check_mpesa_code",type:card_type,amount:amount,phone:phone}, function(result){
			if(result['success'] == 1){
				$('#finish-airtel-payment').removeClass('hidden');
				$('#finish-airtel-loading-gif').addClass('hidden');
			}else{
				if(result['redir'] != null){
					window.location.reload();
				}else{
					
					alert(result['message']);
					$('#finish-airtel-payment').removeClass('hidden');
					$('#finish-airtel-loading-gif').addClass('hidden');
				}	
			}

		}, 'JSON');
	});
}
function validateAirtelForm(){
	
	$('#airtel-form').bootstrapValidator({
		message: 'This value is not valid',
		submitHandler: function(validator, form, submitButton){
			var amount = $('#airtel-transaction-amount').val();
			var phone = $('#airtel-phone').val();
			$('#airtel-phone').html(phone);
			$('#airtel-submision').removeClass('hidden');
			$('#airtel-amount').html('<strong>' + amount + "</strong>");
			$('#airtel-form-submision').addClass("hidden");
			resetFormValidation("airtel-form");
			$('#make-payment-modal-footer').removeClass('hidden');
			
			$('#make-payment-modal-footer').prepend("<button id='back-btn-to-options' type='button' align='left' class='back-button-to-options btn btn-danger'> <span class='glyphicon glyphicon-arrow-left'> </span> Back to payment options</button>");
			$('#make-payment-modal-footer').append("<button id='finish-airtel-payment' type='button' class='btn-finish-airtel-payment btn btn-primary'>Finish</button>");
			$('#make-payment-modal-footer').append("<span id='finish-airtel-loading-gif' class='hidden'><img src='../res/images/loading.gif' /> Completing...</span>");
		},
		feedbackIcons: {
		    valid: 'fa fa-check',
		    invalid: 'fa fa-times',
		    validating: 'fa fa-refresh'
		},
		fields: {
		    airtel_transaction_amount:{
			validators: {
			    notEmpty: {
				message: 'Amount cannot be empty'
			    },
			    numeric:{			      
			      message:'Only numbers required'
			    }
			}	
		    },
		    airtel_phone:{
			    validators:{
				    notEmpty:{
					    message:'Phone number required'
				},
				digits:{
					message:'Only numbers required'
				},
				stringLength:{
					max:10,
					min:10,
					message:'Your phone number seems not to be valid'
				}
			}
		}
		  
		}
    });
}

function validateMpesaForm(){
	
	$('#mpesa-form').bootstrapValidator({
		message: 'This value is not valid',
		submitHandler: function(validator, form, submitButton){
			var amount = $('#mpesa-transaction-amount').val();
			var phone = $('#mpesa-phone').val();
			$('#mpesa-submision').removeClass('hidden');
			$('#mpesa-amount').html('<strong>' + amount + "</strong>");
			$('#mpesa-phone').html(phone);
			$('#mpesa-form-submision').addClass("hidden");
			resetFormValidation("mpesa-form");
			$('#make-payment-modal-footer').removeClass('hidden');
			$('#make-payment-modal-footer').prepend("<button id='back-btn-to-options' type='button' align='left' class='back-button-to-options btn btn-danger'> <span class='glyphicon glyphicon-arrow-left'> </span> Back to payment options</button>");
			$('#make-payment-modal-footer').append("<button id='finish-mpesa-payment' type='button' class='btn-finish-mpesa-payment btn btn-primary'>Finish</button>");
			$('#make-payment-modal-footer').append("<span id='finish-mpesa-loading-gif' class='hidden'><img src='../res/images/loading.gif' /> Completing...</span>");
		},
		feedbackIcons: {
		    valid: 'fa fa-check',
		    invalid: 'fa fa-times',
		    validating: 'fa fa-refresh'
		},
		fields: {
		    mpesa_transaction_amount:{
			validators: {
			    notEmpty: {
				message: 'Amount cannot be empty'
			    },
			    numeric:{			     
			      message:'Only numbers required'
			    }
			}	
		    },
		    mpesa_phone:{
			    validators:{
				    notEmpty:{
					    message:'Phone number is required'
				},
				digits:{
					message:'Only numbers required'
				},
				stringLength:{
					min:10,
					max:10,
					message:'Your phone number seems not to be valid'
				}
			}
		}
		  
		}
    });
}

function validateDebitCardForm(){ 
	$('#debit-card-form').bootstrapValidator({
		message: 'This value is not valid',
		submitHandler: function(validator, form, submitButton){
				var card_number = $('#debit-card-number').val();
				var cvv = $('#card-cvv').val();
				var expiry = formatDate($('#card-expiry').val());
				var currency = $('#payment-currency').val();
				var card_type = $('#card-type').text();
				var amount = $('#amount').val();
				$('#custom-payment-modal-footer').addClass('hidden');
				$('#payment-loading-gif').removeClass('hidden');
				$('#gateway-payment-feedback-success').html("");
				$('#gateway-payment-feedback-fail').html("");
				$('#gateway-payment-feedback-success').removeAttr("style")
				$('#gateway-payment-feedback-fail').removeAttr("style");
				
				$.post('../mapping/payment.jsp',{tag:"authorize_c",card_number:card_number,cvv:cvv,expiry:expiry,currency:currency,amount:amount,card_type:card_type}, function(result){
					if(result['success'] == 1){
						//was successful  
						$('#custom-payment-modal-footer').removeClass('hidden');
						$('#payment-loading-gif').addClass('hidden');
						$('#gateway-payment-feedback-success').html("<div id='inner-feedback-div' class='alert alert-success inner-payment-div' role='alert'>" +
											    "<strong></strong> " + result['message'] +
										    "</div>");
						$("#gateway-payment-feedback-success").delay(5000).fadeOut();
						resetFormValidation("debit-card-form");
						
					}else{
						if(result['redir'] = null){
							//was not successful
							$('#custom-payment-modal-footer').removeClass('hidden');
							$('#payment-loading-gif').addClass('hidden'); 
							
							$('#gateway-payment-feedback-fail').html("<div id='inner-feedback-div' class='alert alert-danger inner-payment-div' role='alert'>" +
												"<strong>Error: </strong> " + result['message'] +
											"</div>");
							$("#gateway-payment-feedback-fail").delay(5000).fadeOut();
						}else{
							window.location.reload();
						}
					}
				//alert(result);
		    }, 'JSON');
		},
		feedbackIcons: {
		    valid: 'fa fa-check',
		    invalid: 'fa fa-times',
		    validating: 'fa fa-refresh'
		},
		fields: {
		    debit_card_number:{
			validators: {
			    notEmpty: {
				message: 'Card number cannot be empty'
			    },
			    digits:{
				message: 'Only numbers required'
			    },
			    stringLength:{
			      max:16,
			      min:16,
			      message:'Card number is exactly 16 digits'
			    }
			}
		    },            
		    card_cvv: {
			validators: {
			    notEmpty: {
				message: 'CVC is required'
			    },
			    digits:{
			      message:'Only numbers required'
			    },
			    stringLength:{
			      max:3,
			      min:3,
			      message:'Card code is exactly 3 digits'
			    }
			}
		    },
		    amount:{
		      validators:{
			notEmpty:{
			  message:'Amount is required'
			},
			numeric:{
			  message:'Numbers only'
			}
		      }
		    },
		    payment_currency:{
		      validators:{
			notEmpty:{
			  message:'Currency is not selected'
			}
		      }
		    },   
		    card_expiry:{
		      validators:{
			notEmpty:{
			  message:'Expiry date required'
			}
		      }
		    }
		}
    });
}



function setPaymentCommon(type){
	$('#global-options-container').addClass('hidden');
	$('#status-p-1').removeClass('breadcrumb-state');
	$('#status-p-1').addClass('breadcrumb-state-greyed');
	$('#status-p-2').addClass('breadcrumb-state');	
	$('#status-p-2').removeClass('breadcrumb-state-greyed');
	
	if(type == "visa" || type == "mastercard"){
		$('#card-submission-form').removeClass('hidden');
		$('#custom-payment-modal-footer').prepend("<button id='back-btn-to-options' type='button' class='back-button-to-options btn btn-danger'> <span class='glyphicon glyphicon-arrow-left'> </span> Back to payment options</button>");
	}else if(type == "mpesa"){
		$('#mpesa-form-submision').removeClass("hidden");
	}else if(type == "airtel"){
		$('#airtel-form-submision').removeClass("hidden");
	}else{
		$('#make-payment-modal-footer').removeClass('hidden');
		$('#make-payment-modal-footer').prepend("<button id='back-btn-to-options' type='button' align='left' class='back-button-to-options btn btn-danger'> <span class='glyphicon glyphicon-arrow-left'> </span> Back to payment options</button>");
	}
	
	$('#card-type').html(type);
}

function resetPaymentModalDefaults(){
	  $('#global-options-container').removeClass('hidden');
	  //reset labels
	  $('.breadcrumb-label').addClass('breadcrumb-state-greyed');
	  $('.breadcrumb-label').removeClass('breadcrumb-state');
	  
	  $('#status-p-1').removeClass('breadcrumb-state-greyed');
	  $('#status-p-1').addClass('breadcrumb-state');
	  
	  $('#card-submission-form').addClass('hidden');
	  $('#back-btn-to-options').remove();
	  resetFormValidation("debit-card-form");
}

function resetFormValidation(formId){
	$('#' + formId).data('bootstrapValidator').resetForm();
	$('#' + formId).get(0).reset();
}


function loadCurrency(){
	$.post("../mapping/mapping.jsp", {tag:"currency",cer:"second"}, function(data){
		var list = '<option value="" >Select currency</option>';
		for(var i = 0; i< data.length; i++){             
			list = list + '<option value="'+data[i]["symbol"]+'"> '+ data[i]["name"] + '</option>';
		}
			
		$("#payment-currency").html(list);
		$('#payment_loading_currency').addClass('hidden');
	},'json');
}

function formatDate(inputDate){
	var arrDate = inputDate.split("/");
	var month = arrDate[0];
	var yr = arrDate[1];
	return month + yr;
}