$(document).ready(function() {	
$('#typed_email_invite').bootstrapValidator({
		message: 'This email does not appear to be valid',
        
	    submitHandler: function(validator, form, submitButton){
			var email = $("#email").val();
			$('#send_mail_typed_mail').text("Inviting...");
			      $.post("../mapping/mapping.jsp",
				{
					tag:"email_typed", email:email
					  
				}, function(result) {
				
				if(result['success']==1){
					$('#send_mail_typed_mail').text("Invite");
					$("#typed_mail_response_message").html("<br><br><div class='alert alert-success'>" + result['message'] + "</div>");
					$("#email").val("");
				}else{
					$("#typed_mail_response_message").html("<div class='alert alert-danger'>" + result['message'] + "</div>");  
				}
				
		}, 'JSON');
	    },
	    feedbackIcons: {
	      valid: 'fa fa-check',
	      invalid: 'fa fa-times',
	      validating: 'fa fa-refresh'
		  },
	    fields: {
	      email: {
                validators: {
		    notEmpty:{
                        message: 'Please enter a valid email'
                    },
                   emailAddress: {
                        message: 'This email address does not appear to be valid'
                    }
                }
            }	
	}
   });

});