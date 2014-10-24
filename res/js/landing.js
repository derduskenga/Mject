$(document).ready(function() {
		$('.single option').click(function() {
			// only affects options contained within the same optgroup
			// and doesn't include this
			$(this).siblings().prop('selected', false);
		});
		
		//	$('#new-message-icon').tooltip();	
	 
	$('#login-form').bootstrapValidator({
		message: 'This value is not valid',
        live: 'disabled',
        submitHandler: function(validator, form, submitButton) {			
 		    var username=$("#j_username").val(); // get value of the input element with id of j_username and assigning to username variable
 			var password=$("#j_password").val();
			//alert("Password : " + cpassword + "\nPassword2 : " + r_cpassword)
            $.get("../request_handler",
				{
					tag:"sign_in", username: username, password: password				  
				}, function(result) {
				if(result['success']==1){
					window.location.href=result['redir'];
				}
				else{
					$('#sign-in-error').removeClass('hidden').html(result['message']);
				}
            }, 'json');
        },
		feedbackIcons: {
            valid: '',
            invalid: 'fa fa-times',
            validating: ''
		  },
		fields: {
		    j_username: {
                validators: {
					notEmpty: {
                        message: 'Please enter a valid username'
                    }
                }
            },
            j_password: {
                validators: {
					notEmpty: {
                        message: 'Please enter a valid password'
                    }
                }
            }	
		}
   });
	
//password reset form
		 $('#forgot-p-form').bootstrapValidator({
				message: 'Please input a valid value',
				live: 'disabled',
				submitHandler: function(validator, form, submitButton) {			
					var email=$("#r_email").val();
					$('#password-reset-error').addClass('hidden').html("");
					$('#password-reset-success').addClass('hidden').html("");
					$.post("../request_handler",
						{
							tag:"reset_pass", mailaddress: email	  
						}, function(result) {
								if(result['success']==1){
									$('#password-reset-error').addClass('hidden').html("");
									$('#forgot_panel-default').html("<div class='text-center'><h4>"+result['message']+"</h4></div>");
								}else{
									$('#password-reset-error').removeClass('hidden').html(result['message']);
								}
					}, 'json');
				},
				feedbackIcons: {
					valid: '',
					invalid: 'fa fa-times',
					validating: ''
				},
				fields: {
					j_username: {
						validators: {
							notEmpty: {
								message: 'Please enter a valid username'
							}
						}
					},
					j_password: {
						validators: {
							notEmpty: {
								message: 'Please enter a valid password'
							}
						}
					}	
				}
		});
		 
//password reset form
		 
$('#password-reenter-form').bootstrapValidator({
		message: 'This value is not valid',
        submitHandler: function(validator, form, submitButton) {
			var id = getUrlVars()["id"];
			var token = getUrlVars()["token"];
 		    var cpassword=$("#cpassword").val();
 			var r_cpassword=$("#r_cpassword").val();
			//alert("Password : " + cpassword + "\nPassword2 : " + r_cpassword)
            $.post("../request_handler",
				{
					tag:"change_password", id:id, token:token , password:cpassword,r_password:r_cpassword		  
				}, function(result) {
				//alert(result['success'] + "\nMessage : " + result['message']);
				if(result['success'] == '1'){
					$('#password-reset-panel-').html("<h4>You have successfully changed your password. Click <a href='../login'> here </a> to login.</h4>");
				}else if(result['success'] == '0'){
					$('#password-change-error').removeClass('hidden').html(result['message']);	
				}
            }, 'JSON');
        },
		feedbackIcons: {
            valid: 'fa fa-check',
            invalid: 'fa fa-times',
            validating: 'fa fa-refresh'
		  },
		fields: {
		    cpassword: {
                validators: {
					notEmpty: {
                        message: 'The password is required and can\'t be empty'
                    },
                    stringLength: {
                        min: 8,
                        message: 'Password must be at least 8 characters long'
                    },
                    identical: {
                        field: 'r_cpassword',
                        message: 'Password and its confirm are not the same'
                    }
                }
            },
            r_cpassword: {
                validators: {
					notEmpty: {
                        message: 'The confirm password is required and must be same as password'
                    },
                    identical: {
                        field: 'cpassword',
                        message: 'The password and its confirm are not the same'
                    }
                }
            }	
		}
   });


    $('#reg-form').bootstrapValidator({
        message: 'This value is not valid',
        submitHandler: function(validator, form, submitButton) {
 			var f_name=$("#f_name").val();
 			var l_name=$("#l_name").val();
 			var email=$("#email").val();
 			var username=$("#username").val();
 			var password=$("#password").val();
 			var r_password=$("#r_password").val();
			
           $.post('../request_handler',
				{
					tag:"register", fname: f_name, lname:l_name,email: email,
					username:username, password:password,r_pwd:r_password
				}, function(result) {
				//alert(result);
				if(result['success'] == '1'){
					$('#form_panel').html(result['message']);
					$('#logon').removeClass('hidden');
					$('#submit-btn').addClass('hidden');
					$('#sign-up-error').addClass('hidden');
				}else if(result['success'] == '0'){
					$('#sign-up-error').html(result['message']);
					$('#sign-up-error').removeClass('hidden');
				}
				
            }, 'JSON');
        },
        feedbackIcons: {
            valid: 'fa fa-check',
            invalid: 'fa fa-times',
            validating: 'fa fa-refresh'
        },
        fields: {
            f_name: {
                message: 'First name is not valid',
                validators: {
                    notEmpty: {
                        message: 'First name is required and can\'t be empty'
                    },
                    stringLength: {
                        min: 2,
                        max: 30,
                        message: 'First name must be 2 or more characters long'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z]+$/,
                        message: 'Name can only consist of alphabetical characters'
                    }
                }
            },            
            l_name: {
                message: 'Last name is not valid',
                validators: {
                    notEmpty: {
                        message: 'Last name is required and can\'t be empty'
                    },
                    stringLength: {
                        min: 2,
                        max: 30,
                        message: 'Last name must be 2 or more characters long'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z]+$/,
                        message: 'Name can only consist of alphabetical characters'
                    }
                }
            },
            username: {
                message: 'The username is not valid',
                validators: {
                    notEmpty: {
                        message: 'The username is required and can\'t be empty'
                    },
                    stringLength: {
                        min: 3,
                        max: 30,
                        message: 'The username must be more than 2 and less than 30 characters long'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9]+$/,
                        message: 'The username can only consist of alpha-numeric characters'
                    },
                }
            },
            acceptTerms: {
                validators: {
                    notEmpty: {
                        message: 'You have to accept the terms and policies'
                    }
                }
            },
            email: {
                validators: {
                    notEmpty: {
                        message: 'The email address is required and can\'t be empty'
                    },
                    emailAddress: {
                        message: 'The input is not a valid email address'
                    }
                }
            },
            password: {
                validators: {
                    notEmpty: {
                        message: 'The password is required and can\'t be empty'
                    },
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
                    notEmpty: {
                        message: 'The confirm password is required and must be same as password'
                    },
                    identical: {
                        field: 'password',
                        message: 'The password and its confirm are not the same'
                    }
                }
            },
        }
    });
});
//================functions
	
	function liveProcessIndicator(type, elementId, message){
		var $element = $('#'+elementId);
		
		switch(type){
			case 'loading':
				$element.removeClass().addClass('text-info').html('<i class="fa fa-refresh fa-spin"></i> ' + message);
			break;
			
			case 'success':
				$element.removeClass().addClass('text-success').html('<i class="fa fa-check"></i> ' + message);
			break;
			
			case 'error':
				$element.removeClass().addClass('text-danger').html('<i class="fa fa-times"></i> ' + message);
			break;
		}	
	}
	//==========================================
	function alternatePanel(id){
		$('#logon_panel,#forgot_panel').addClass('hidden');
		$('#'+id).removeClass('hidden');
	 }

	function getUrlVars() {
		var map = {};
		var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
			map[key] = value;
		});
		return map;
	 }