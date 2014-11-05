$(document).ready(function(){ 

	$('#loading-currency-withdraw').removeClass("hidden");
	
	$.post("../mapping/mapping.jsp", {tag:"currency"}, function(data){		
		var list = '<option value="" >Select currency</option>';
		for(var i = 0; i< data.length; i++){ 
							
			list = list + '<option value="'+data[i]["symbol"]+'"> '+ data[i]["name"] + '</option>';
			//arrCur.push({key:data[i]["id"],value:data[i]["name"]});
		}
			      
			$("#withdraw-currency").html(list);
			$('#loading-currency-withdraw').addClass('hidden');
	},'json');
  
	$('#withdraw-mpesa-option-btn').click(function(e){
		var type = "mpesa";
		handleOptions(type);
	});
	$('#withdraw-airtel-option-btn').click(function(e){
		var type = "airtel";    
		handleOptions(type);
	});	
	$('#withdraw-bank-option-btn').click(function(e){
		var type = "bank";
		handleOptions(type);
	});
	validateMpesaWithdrawal(); 
	validateBankWithdrawal();
	
	$('#withdraw').on('click','.back-button-to-options-withdraw', function(e){
		//var id = e.target.id;
		$('#back-btn-to-options-withdraw').remove();
		if($('#withdraw-bank-submission-form').is(':hidden')){
		//
		}else{
			$('#withdraw-bank-submission-form').addClass("hidden");
		}
		
		if($('#withdraw-mpesa-submission-form').is(':hidden')){
		  //
		}else{
			$('#withdraw-mpesa-submission-form').addClass('hidden');
		}
		
		$('#withdraw-global-options-container').removeClass("hidden");
	});
	
	$('#make-withdraw-cancel-btn').click(function(e){
		resetForm("withdraw-mpesa-form");
	});
	
	$('#make-withdraw-cancel-btn-bank').click(function(e){
		resetForm("withdraw-bank-form");
	});
	
	
});

//functions
function resetForm(formId){
	//$('#' + formId).data('bootstrapValidator').resetForm();
	$('#' + formId).get(0).reset();
}
function handleOptions(type){
	$('#withdraw-selected-option').html(type);
	$('#withdraw-global-options-container').addClass("hidden");	
	if(type == "mpesa" || type == "airtel"){
		$('#withdraw-mpesa-submission-form').removeClass("hidden");
		$('#custom-withdraw-modal-footer-mpesa').prepend("<button id='back-btn-to-options-withdraw' type='button' class='back-button-to-options-withdraw btn btn-danger'> <span class='glyphicon glyphicon-arrow-left'> </span> Back to withdraw options</button>");
	}else{
		 //type is bank
		$('#withdraw-bank-submission-form').removeClass("hidden");
		$('#custom-withdraw-modal-footer-bank').prepend("<button id='back-btn-to-options-withdraw' type='button' class='back-button-to-options-withdraw btn btn-danger'> <span class='glyphicon glyphicon-arrow-left'> </span> Back to withdraw options</button>");
	}
}

function validateBankWithdrawal(){
	$('#withdraw-bank-form').bootstrapValidator({
		message: 'This value is not valid',
		submitHandler: function(validator, form, submitButton){
			var withdraw_amount = $('#withdraw-bank-amount').val();
			var bank_name = $('#withdraw-bank-name').val();
			var bank_branch = $('#withdraw-bank-branch').val();
			var account_number = $('#withdraw-account-number').val();
			var full_account_names = $('#withdraw-account-name').val();
			var swift_code = $('#withdraw-bank-swift-code').val();
			var physical_address = $('#withdraw-bank-physical-address').val();
			var currency = $('#withdraw-currency').val();
			if(physical_address == ""){
				physical_address = "none";
			}
			$.post("../mapping/payment.jsp",{tag:"withdraw_from_bank",currency:currency,withdraw_amount:withdraw_amount,bank_name:bank_name,bank_branch:bank_branch,account_number:account_number,full_account_names:full_account_names,swift_code:swift_code,physical_address:physical_address}, function(data){  
				if(data["success"] == 1){
					 alert(data["message"]);
				}else{
					alert(data["message"]);
				}
			},'json');
		},
		feedbackIcons: {
		    valid: 'fa fa-check',
		    invalid: 'fa fa-times',
		    validating: 'fa fa-refresh'
		},
		fields: {
		    withdraw_bank_amount:{
			validators: {
			    notEmpty: {
				message: 'Amount cannot be empty'
			    },
			    numeric:{
			      message:'Only numbers required'
			    }
			}	
		    },
		    withdraw_bank_name:{
		      validators:{
			notEmpty:{
			  message:'Bank name required'
			}
		      }
		    },
		    withdraw_currency:{
		     validators:{
		       notEmpty:{
			 message:'Please select currency'
		      }
		    } 
		    },
		    withdraw_bank_branch:{
		      validators:{
			notEmpty:{
			  message:'Bank branch required'
			}
		      }
		    },
		    withdraw_account_number:{
		      validators:{
			notEmpty:{
			  message:'Account number required'
			}
		      }
		    },
		    withdraw_account_name:{
		      validators:{
			notEmpty:{
			  message:'Account names required'
			}
		      }
		    },
		    withdraw_bank_swift_code:{
		      validators:{
			notEmpty:{
			  message:'Swift code required'
			}
		      }
		    }  
		}
    });	
}
function validateMpesaWithdrawal(){
	$('#withdraw-mpesa-form').bootstrapValidator({
		message: 'This value is not valid',
		submitHandler: function(validator, form, submitButton){
			var amount = $('#withdraw-mpesa-amount').val();
			var mobile = $('#mobile-no').val();
			$.post("../mapping/payment.jsp",{tag:"send_money",mobile:mobile,amount:amount}, function(data){  
				
			},'json');
		},
		feedbackIcons: {
		    valid: 'fa fa-check',
		    invalid: 'fa fa-times',
		    validating: 'fa fa-refresh'
		},
		fields: {
		    withdraw_mpesa_amount:{
			validators: {
			    notEmpty: {
				message: 'Amount cannot be empty'
			    },
			    numeric:{
			      message:'Only numbers required'
			    }
			}	
		    },
		    mobile_no:{
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
			  message:'10 digits for a phone number required'
			}
		      }
		    }
		  
		}
    });	
}