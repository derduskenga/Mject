/**
*
*	Create a self invoking function to protect the global scope from populating with variables.
*	
*	Handles the settings page for email and password reset
*
*/
(function($){
	// on document ready
	$(function(){
		// password reset
		$('#passwordLoader').click(function(e){
			e.preventDefault();
			$("#errorArea").html(" ");
			$(this).addClass('active').siblings('li').removeClass('active');
			passwordReset();
		});
		// email notifications
		$('#emailNotificationsLoader').click(function(e){
			e.preventDefault();
			$("#errorArea").empty();
			$(this).addClass('active').siblings('li').removeClass('active');
			emailNotifications();
		});

		
	});


// ************************************************ PASSWORD RESET JS **************************************
/**
*
*	Validates the password change form, posts to the server and return feedback to the browser
*
*
*/
	function passwordReset(){
		$('.settings_page').load('includes/password_reset_form.jsp',function(){				
			// if(statusTxt=="error")
		 	//      	console.log(xhr);
		 	//  		return;
			// validate form and submit if valid
			$('#passwordResetForm').bootstrapValidator({
				live: 'enabled',
	            message: 'This value is not valid',
	            feedbackIcons: {
		            valid: 'glyphicon glyphicon-ok',
		            invalid: 'glyphicon glyphicon-remove',
		            validating: 'glyphicon glyphicon-refresh'
		        },
		        submitHandler: function(validator, form, submitButton) {
				    // Use Ajax to submit form data
				    $.ajax({
				    	url:'../settings/reset-password',
				    	data: {
				    		oldPassword: $('#oldPassword').val(),
				    		password1: $('#password1').val(),
				    		password2: $('#password2').val(),
				    		tag: "reset_password"
				    	},
				    	dataType: 'json',
				    	type: 'POST'
				    })
				    .done(function(result,status,xhr){
				    	console.log(result);
				    	// if status == 202 the request was 
				    	// successful but it was not completed
				    	if(xhr.status == 202 || xhr.status == 500){
				    		var message = "<div class='alert alert-warning'>"
				    							+result.message+
				    					  "</div>";
				    		$('.settings_page #error').html(message);
				    		return;
				    	}
			    		$('.settings_page').empty();
			    		userFeedback(xhr);
			    	})
			    	.fail(function(result,status,xhr){
			    		console.log(xhr);
			    		console.log(result);
			    		console.log(status);
			    		$('.settings_page').html('success');
			    	});
				},
				fields:{
					oldPassword:{
						validators:{
							notEmpty:{
								message:'This field is required'
							},
							stringLength: {
		                        min: 8,
		                        message: 'Atleast 8 characters needed'
		                    }
						}
					},
					password1:{
						validators:{
							notEmpty:{
								message:'This field is required'
							},
							stringLength: {
		                        min: 8,
		                        message: 'Atleast 8 characters needed'
		                    }
						}
					},
					password2:{
						validators:{
							notEmpty:{
								message:'This field is required'
							},
							stringLength: {
		                        min: 8,
		                        message: 'Atleast 8 characters needed'
		                    },
		                    identical: {
		                        field: 'password1',
		                        message: 'This password should match the one above'
		                    } 
						}
					}
				}
			}).on('submit',function(e){
				e.preventDefault();
			}); // end of bootstrap validator for password reset form
		}); // end of password reset load
	}


//  ************************************* EMAIL NOTIFICATION JS *****************************
/**
*
*	Posts user prefences to the server and displays them to the browser
*
*
*/
	function emailNotifications(){
		// fetch setting from the database
		$.ajax({
			url:'../settings/notification-prefences',
			type:'get',
			dataType: 'json'
		})
		.done(function(data){
			// load the settings form
			$(".settings_page").load("includes/email_notifications.jsp", function(){
				$(".panel-heading").click(function(){
					var parent = this;
					$(this).siblings(".panel-body").slideToggle(500, function(){
						if($(this).is(":visible")){
							$(parent).find(".sign").html("<span class='glyphicon glyphicon-minus'></span>")
						}else{							
							$(parent).find(".sign").html("<span class='glyphicon glyphicon-plus'></span>")
						}
					});
				});
				// load data on the form
				for(key in data){
					var value = (data[key]=="t")?true:false
					$("#emailNotificationForm input[name='"+key+"'][value='"+value+"']").prop("checked",true);
					console.log(value);
				}



				$("#emailNotificationForm").submit(function(e){				
					e.preventDefault();
					var settingsOptions = $("#emailNotificationForm").serializeArray();
					$.ajax({
						url:"../settings/email-notification",
						data: {
							connectionRequest:$("input[name='connectionRequest']:checked").val(),
							connectionRequiresDonation:$("input[name='connectionRequiresDonation']:checked").val(), 
							connectionPlaceOffer:$("input[name='connectionPlaceOffer']:checked").val(),
							connectionSellsPoints:$("input[name='connectionSellsPoints']:checked").val(), 
							
							bidsOnMyPoints:$("input[name='bidsOnMyPoints']:checked").val(),
							buyerAcceptsBid:$("input[name='buyerAcceptsBid']:checked").val(),
							purchasesOnMyPoints:$("input[name='purchasesOnMyPoints']:checked").val(),
							receivedDonation:$("input[name='receivedDonation']:checked").val(), 
							
							ApplicationsOnMyOffer:$("input[name='ApplicationsOnMyOffer']:checked").val(),
							myOfferApplicationDenied:$("input[name='myOfferApplicationDenied']:checked").val(),
							myOfferApplicationAccepted:$("input[name='myOfferApplicationAccepted']:checked").val()
						},
						dataType: "json",
						type: "post"
					})
					.done(function(result, responseText,xhr){ 
						console.log(result);
						userFeedback(xhr);	
						console.log(xhr);						
					})
					.fail(function(xhr){
						userFeedback(xhr);	
						console.log(xhr);			
					}); 
				});
			});
			
		})
		.fail(function(xhr){
			userFeedback(xhr);

		});
		
	}

	/**
	*
	*
	*	Display the error message
	*
	*/
	function errorDisplay(xhr,message,bootstrap_class){
		$("#errorArea").html("<div class='alert alert-"+bootstrap_class+"'> "+message+"</div>");
	}


})(jQuery)