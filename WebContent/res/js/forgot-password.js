$(document).ready(function() {
	
$('#password-reenter-form').bootstrapValidator({
		message: 'This value is not valid',
        
        submitHandler: function(validator, form, submitButton) {
			   
 		    var password=$("#password").val();
 			var r_password=$("#r_password").val();
			alert(password)
           /* $.post('process_request.jsp',
				{
					tag:"register", fname: f_name, lname:l_name,email: email,
					username:username,city:city, country:country, password:password,r_pwd:r_password
					  
				}, function(result) {
				alert(result);
				if(result['success'] == '1'){
					$('#form_panel').html(result['message']);
					$('#logon').removeClass('hidden');
					$('#submit-btn').addClass('hidden');
					$('#sign-up-error').addClass('hidden');
				}else if(result['success'] == '0'){
					$('#sign-up-error').html(result['message']);
					$('#sign-up-error').removeClass('hidden');
				}
				
            }, 'JSON');*/
        },
		feedbackIcons: {
            valid: 'fa fa-check',
            invalid: 'fa fa-times',
            validating: 'fa fa-refresh'
		  },
		fields: {
		    password: {
                validators: {
                    stringLength: {
                        min: 8,
                        message: 'Password must be at least 8 characters long'
                    },
                    identical: {
                        field: 'r_password',
                        message: 'Password and its confirm are not the same'
                    }
                }
            },
            r_password: {
                validators: {
                    identical: {
                        field: 'password',
                        message: 'The password and its confirm are not the same'
                    }
                }
            }	
		}
   });

});