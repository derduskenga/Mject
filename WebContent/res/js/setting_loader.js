 $(function(){//page ready loader i.e loads the page when ready
   $('#profile_loader').click(function(e){
        e.preventDefault();//prevents the defaul action loaded page 
        $(this).addClass('active').siblings().removeClass('active');
        $('#profile').show(500);//div to be loaded'id and the sho spee
        // $('.settings_page').load("include/demo.html");
        fetchProfileData();


   });

 });


 function fetchProfileData(){
 	var profileDiv=$('#profile').html();
 	var date_picker=$('#dateofbirth').datepicker({
							format: "dd/mm/yyyy",
							endDate: $('#dateofbirth').attr("max-date")
		 });
 	//load the loading gif
 	$('#profile').html('<div class="text-center block"><img src="../res/images/loading_site_green_small.gif" alt="Upload your pic here">Loading...</div>');
 	
 	//fetch data for the current user
 	var newDataRequest = $.ajax({
					type: "POST",
					url: "../allsettings",
					timeout: 30000, // timeout after 30 seconds
					dataType: "json" //JSON
					//data: { tag:'allrequests'}
			});	
			
			newDataRequest.done(function(data){
			 if(data['profile_redirect'] == true){

			 	location.href = "../complete-profile/index.jsp";

			 }else{
				if(data["success"]==0){
							if(data["redir"]){
								showSessExpired();
							}else{
								$('#profile').html('<div class="bg-danger text-center">'+data["message"]+'</div>');
							}
				}else if(data["success"]==1){

								
								$('#profile').html(profileDiv);
								$('#gender').val(data['gender']);
								$('#orgname1').val(data['org']);

								$('#countrys').val(data['cntry']);
								$('#dateofbirth').val(data['dob']);
								$('#dateofbirth').html(date_picker);
								$('#occupation').val(data['occ']);
								$('#address').val(data['addr']);
								$('#postalcode').val(data['p_code']);
								$('#city').val(data['cty']);
								$('#physical_address').val(data['street_addr']);
								$('#primaryemail').val(data['p_mail']);
								$('#profilepic').html("<img src='../profilepic/"+data['pic']+"' />");//loads the html contents dynamically
								$('#bio').val(data['bio_details']);
								$('#phone-country-code').val(data['mbl_no']);
								// $('#orgname').val(data['p_mail']);
								// $('#orgname').val(data['mbl_number']);
								// $('#orgname').val(data['street_addr'])

								insertProfile();


							
					}
				}
			});
					
			newDataRequest.fail(function(jqXHR, exception){
					if (jqXHR.status === 0){
						alert('Sorry, could not establishing a network connection.');
					}else if (jqXHR.status == 404){
						alert('Requested page not found. [404]');
					}else if (jqXHR.status == 500){
						alert('Internal Server Error [500].');
					}else if (exception === 'parsererror'){
						alert('Requested JSON parse failed. ....');
					}else if (exception === 'timeout'){
						alert('Time out error.');
					}else if (exception === 'abort'){
						alert('Sorry, Request was aborted.');
					}else{
						alert('Sorry, an error occured.');
					}
			});


 }

  function insertProfile(){

 	//alert('Insert profile method')            

	  $('#update_profile_form').bootstrapValidator({

					message: 'This field is not valid',
					submitHandler: function(validator, form, submitButton) {
						//preventDefault();
					var profile_form = $('#update_profile_form');
					var formData = new FormData(profile_form[0]);
					    $.ajax({
								url: '../editProfileServlet?tag=update_profile',
								type: 'POST',
								data: formData,
								async: false,	
								success: function (data) {
	 								if(data['success']==1){
											if(data['redir']){
												window.location.href=data['redir'];
											}
											else{
												window.location.href="../settings";
												alert('Profile update succesfull')
											}
									}else{
										if(data['redir'])
												window.location.href=data['redir'];
											else
												alert(data['message']);
									}
								},error: function(){
									alert("Sorry, an error occurred while updating your profile");
								},
								cache: false,
								contentType: false,
								processData: false
						});
					return false;
	        }, 
	        feedbackIcons: {
	            valid: 'fa fa-check',
	            invalid: 'fa fa-times',
	            validating: 'fa fa-refresh'
	        },
	        fields: {
				countrys: {
	                validators: {
	                    notEmpty: {
	                        message: 'Please select your country'
	                    }
	                }
	            },
	            orgname1:{
	            	validators:{
                        regexp: {
	                        regexp: /^[A-Za-z0-9]*[A-Za-z]{2,5}/,
	                        message: 'Only letters and numerals are accepted here'
	                    }
	            	},

	            },
	            primaryemail:{
	            	validators:{
	            		regexp:{
	            			regexp: /^[A-Za-z0-9._]*\@[A-Za-z]*\.[A-Za-z]{2,5}$/,
	            			message: 'Please enter only an email'
	            		}
	            	}
	            },
	            city:{
	            	validators:{
	            		regexp: {
	                        regexp: /^[A-Za-z0-9]*[A-Za-z]{2,5}/,
	                        message: 'Enter a valid city'
	                    }
	            	}
	            },
				phoneno: {
	                validators: {
	                    notEmpty: {
	                        message: 'Please enter your phone number'
	                    },
	                   /* stringLength: {
	                        min: 9,
	                        max: 20,
	                        message: 'Your phone number does not appear to be valid.'
	                    }, */
						regexp: {
	                        regexp: /^((\+\d{1,3}(-| )?\(?\d\)?(-| )?\d{1,3})|(\(?\d{2,3}\)?))(-| )?(\d{3,4})(-| )?(\d{4})(( x| ext)\d{1,5}){0,1}$/,
	                        message: 'Your phone number does not appear to be valid'
	                    }
	                }
	            },            
	            occupation: {
	                message: 'Please enter your occupation',
	                validators: {
	                    notEmpty: {
	                        message: 'Please enter your occupation'
	                     }
	                }
	            },
	            address: {
	                validators: {
	                    notEmpty: {
	                        message: 'Please enter your address'
	                    },
						digits: {
	                        message: 'Address can only contain digits'
	                    }
	                }
	            },
				postalcode: {
	                validators: {
	                    notEmpty: {
	                        message: 'Please enter your postal code'
	                    },
						digits: {
	                        message: 'Postal/Zip  code can only contain digits'
	                    }
	                }
	            },
				bio:{
					validators:{
						stringLength:{
							max: 500,
							message: 'You have exceeded 500 characters'
						}
					}
				},
				physical_address:{
					validators:{
						regexp:{
							regexp: /^[A-Za-z0-9]*[A-Za-z0-9]{2,5}/,
							message:'Enter a valid physical address'
						}
					}
				}
			  }
		});
   // alert(GENDER );
}


