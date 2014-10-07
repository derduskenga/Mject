$(document).ready(function(){	
	$('#phone-country-code').attr('readonly', true);
	
	$(document).ajaxStart(function () {
		$('#updating-profile-gif').removeClass('hidden');
	}).ajaxStop(function () {
		$('#updating-profile-gif').addClass('hidden');
	});
	
	if($('#loading_country')){
			$('#loading_country').removeClass('hidden');
			$.post("../request_handler", {tag:"all_countries"}, function(data){
				var list = '<option p_code="" value="" >Select Country</option>';
				for(var i = 0; i<data.length; i++){
					list = list + '<option p_code="'+data[i]["p_code"]+'" value="'+data[i]["id"]+'"> '+ data[i]["name"] + '</option>';   
				}
			$("#countrys").html(list);
			$('#loading_country').addClass('hidden');
			},'json');
		}
		$('#dateofbirth').datepicker({
							format: "dd/mm/yyyy",
							endDate: $('#dateofbirth').attr("max-date")
		 });
		$('#countrys :nth-child(4)').prop('selected', true);
		$("#countrys").change(function(e){
					//$('#phone-country-code').val("ads");
					//alert($($(this).options.selectedIndex).attr('p_code'));
					//alert($("#dropDownMenuKategorie")[0].selectedIndex);
					//alert($(this).option[$(this).prop('selectedIndex')].attr('p_code'));
					$('#phone-country-code').val('+'+$('#countrys option:nth-child('+($(this).prop('selectedIndex')+1)+')').attr('p_code'));
		});
		
		$('#complete-profile-form').bootstrapValidator({
				message: 'This field is not valid',
				submitHandler: function(validator, form, submitButton) {
				var formData = new FormData($('form')[0]);
				    $.ajax({
							url: '../request_handler?tag=update_profile',
							type: 'POST',
							data: formData,
							async: false,
							success: function (data) {
 								if(data['success']==1){
										if(data['redir'])
											window.location.href=data['redir'];
										else
											window.location.href="../home";
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
		   dateofbirth: {
                validators: {
                    date: {
                        format: 'DD/MM/YYYY',
                        message: 'Please enter a valid date'
                    },
					notEmpty:{
						message: 'Please enter your date of birth'
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
			}
		  }
	});
	
	$('#bio').keyup(function(){
			if(this.value.length > 500){
				return false;
			}
			$("#remainingC").html("Remaining: " +(500 - this.value.length));
	});
});
