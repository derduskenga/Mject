$(document).ready(function(){ 
	var profile_pic_path_Global = "";
	var user_full_names_Global = "";
	var user_entity_id_Global = "";      
     $.post("../mapping/mapping.jsp",{tag:"global_dresser"}, function(data){      
		profile_pic_path_Global = data['profile_pic_path'];		
		user_full_names_Global = data['full_names'];
		user_entity_id_Global = data['entity_id'];
		if(profile_pic_path_Global == null){			  
			
			$('#profile-pic-section').html("<span class='fa fa-user fa-5x breadcrumb-state-greyed'></span>");	
		}else{
			$('#profile-pic-section').html("<a href='#'><img src='" + "../profilepic/" + profile_pic_path_Global + "'></a>"); 	  
		}
     },'json');  
	
	$('#loading_category').removeClass('hidden');
	$('#loading_currency').removeClass('hidden');
	$('#loading_currency_donate').removeClass('hidden'); 
	
	    
	//................................categories start.............................
	var arrCat = [];
	$.post("../mapping/mapping.jsp", {tag:"categories"}, function(data){		
		var list = '<option value="" >Select category</option>';
		for(var i = 0; i< data.length; i++){
			list = list + '<option value="'+data[i]["id"]+'"> '+ data[i]["name"] + '</option>';
			arrCat.push({key:data[i]["id"],value:data[i]["name"]});
		}
      
		$("#donation_category").html(list);
		$('#loading_category').addClass('hidden');
      },'json');
    
	//.........................categories end.................................    
       
	//..........................currency start.............................
	
	var arrCur = []; 
	
	$.post("../mapping/mapping.jsp", {tag:"currency"}, function(data){
		var list = '<option value="" >Select currency</option>';
		for(var i = 0; i< data.length; i++){             
			list = list + '<option value="'+data[i]["id"]+'"> '+ data[i]["name"] + '</option>';
			//obj[data[i]["id"]] = data[i]["name"];
			arrCur.push({key:data[i]["id"],value:data[i]["name"]});
		}
			
		$("#donation_currency").html(list);
		$("#donation_currency_donate").html(list);
		$('#loading_currency').addClass('hidden');
		$('#loading_currency_donate').addClass('hidden');
	},'json');
    
    //.........................currency end...................................
	
	$('#new-offer-btn').click(function(eventSource){  
		$('#offer-options-holder').removeClass('hidden');
		
	});
	
	$('#money-option').click(function(){
		$('#m-form-holder').removeClass('hidden'); 
		$('#g-form-holder').addClass('hidden');
		$('#s-form-holder').addClass('hidden'); 
	});
	
	$('#service-option').click(function(){
		$('#s-form-holder').removeClass('hidden'); 
		$('#m-form-holder').addClass('hidden'); 
		$('#g-form-holder').addClass('hidden');
	});
	$('#material-option').click(function(){
		$('#g-form-holder').removeClass('hidden'); 
		$('#m-form-holder').addClass('hidden');
		$('#s-form-holder').addClass('hidden'); 
	});
	//cancel buttons
	$('#offer-post-cancel-btn').click(function(e){
		e.preventDefault();
		$('#m-form-holder').addClass('hidden');  		
		$('#m-offer-form').data('bootstrapValidator').resetForm();
		$('#m-offer-form').get(0).reset();
	});
	
	$('#service-offer-post-cancel-btn').click(function(e){
		e.preventDefault();
		$('#s-form-holder').addClass('hidden');  		
		$('#s-offer-form').data('bootstrapValidator').resetForm();
		$('#s-offer-form').get(0).reset();
	});
	
	$('#material-offer-post-cancel-btn').click(function(e){
		e.preventDefault();
		$('#g-form-holder').addClass('hidden');
		$('#g-offer-form').data('bootstrapValidator').resetForm();
		$('#g-offer-form').get(0).reset();
	});
	
	//reset buttons
	$('#offer-post-reset-btn').click(function(e){
		e.preventDefault();
		$('#m-offer-form').get(0).reset();
		$('#m-offer-form').data('bootstrapValidator').resetForm();
		
	});	
	$('#service-offer-post-reset-btn').click(function(e){
		e.preventDefault();
		$('#s-offer-form').get(0).reset();
		$('#s-offer-form').data('bootstrapValidator').resetForm();
		
	});
	
	$('#material-offer-post-reset-btn').click(function(e){
		e.preventDefault();
		$('#g-offer-form').get(0).reset();
		$('#g-offer-form').data('bootstrapValidator').resetForm();
		
	});
	
	
	$('#m-offer-form').bootstrapValidator({
		message: 'This value is not valid',		
		submitHandler: function(validator, form, submitButton) {
				var offered_amount=$("#offer-amount").val();
				var offered_currency=$("#donation_currency").val();
				var offer_category=$('#donation_category').val();
				var offer_summary=$('#offer-summary').val();
				var offer_details=$('#offer-details').val(); 			
				
				$.post("../mapping/offer_mapping.jsp",{tag:"save_offer",offered_amount:offered_amount,offered_currency:offered_currency,
					      offered_category:offer_category,offer_details:offer_details,offer_summary:offer_summary}, function(data){
					$('#offer-loading-gif').removeClass('hidden');					
					if(data['success'] == 1){					  
					      //pre-append the post on offer-post div
						$('#offer-loading-gif').addClass('hidden');						  
						$('#m-form-holder').addClass('hidden');
						$('#m-offer-form').data('bootstrapValidator').resetForm();
						$('#m-offer-form').get(0).reset();
						var offer_post_id = data['offer_id'];
						var profile_pic_path = data['profile_pic_path'];
						var names = data['names'];
						var message = data['message'];
						var time_details = data['time_details'];
						var offer_type = data['offer_type'];
						  
						$('#offer-post').prepend(""+
									"<div class='row col-xs-12 post' id='offer-post-"+offer_post_id+"'>" +
										"<div class='row col-xs-12 offer-post-and-pic-name'>" +
											"<span class='row col-xs-2 post-pic' id='offer-post-pic-"+offer_post_id+"'>"+
												"<img class='post-pic' src='"+profile_pic_path+"'></img>"+
											"</span>"+
											"<span id='offer-name' class='offer-name-link'>" +
												"<a href='#' class='offer-name-link-"+offer_post_id+"'> "+names+"</a>"+
											"</span>"+
											"<span id='offer-post-time' class='breadcrumb-state-greyed'>"+
												"" + time_details +
											"</span>"+
										"<div>"+
										"<div id='' class='row col-xs-12'>" +
											"<p class='teller'>" + names + " " + getTypeString(offer_type) + "</p>"+
										"</div>"+
										"<div id='post-offer-details-"+offer_post_id+"' class='row col-xs-12'>"+
											"<p><b>"+offer_summary+": </b> "+offer_details+"</p>"+
											"<p><b>Offer category: </b>"+next(arrCat,offer_category)+"</p>"+
											"<p><b>Offer amount: </b>"+offered_amount+" "+ next(arrCur,offered_currency)+"</p>"+
										"</div>"+
										"<div class='row col-xs-12 offer-post-actions' id='offer-post-actions-"+offer_post_id+"'>"+
											//"<a href='#' class='offer-link' id='offer-apply-link-"+offer_post_id+"'>Apply &nbsp;</a>"+
											//"<a href='#' class='offer-link' id='offer-message-link-"+offer_post_id+"'> Message post owner</a>"+
										"</div>"+
									 "<div>" +
									 "");
						  
					}else{  
						  alert("failed");
					}
					    
				},'json');
				
		},
		feedbackIcons: {
		    valid: 'fa fa-check',
		    invalid: 'fa fa-times',
		    validating: 'fa fa-refresh'
		},
		fields: {
		    offer_amount: {
			validators: {
			    notEmpty: {
				message: 'Amount field is empty'
			    },
			    numeric: {
				   message: 'Amount can only be made of numbers'
			    }
			}
		    },            
		    donation_currency: {
			validators: {
			    notEmpty: {
				message: 'Select currency'
			    }
			}
		    },
		    donation_category:{
			validators:{
			    notEmpty:{
				message:'Select offer category'
			    }
			}
		    },
		    offer_summary:{
			validators:{
			    notEmpty:{
				message:'Offer summary field is empty'
			    }
			}
		    },
		    offer_details:{
			validators:{
			    notEmpty:{
				message:'Offer details field is empty'
			    }
			}
		    }  
		}
	});	
	$.post("../mapping/offer_mapping.jsp", {tag:"pull_offer_posts"}, function(data){
		if(data['status'] == 'OK'){
			var arrO = data['offers'];
			for(var i = 0;i<arrO.length;i++){
				 
				if(arrO[i]['offer_type'] == "money"){
					var offer_entity_id = arrO[i]['offer_entity_id'];
					var offer_id = arrO[i]['offer_id'];
					var visibility_class_name = arrO[i]['visibility_class_name'];
					var names = arrO[i]['names'];
					var offer_amount =arrO[i]['offer_amount'];
					var offer_details =arrO[i]['offer_details'];
					var programme_name =arrO[i]['programme_name'];
					//var offer_owner_profile_pic = "../profilepic/" +arrO[i]['profile_pic_path'];
					var offer_owner_profile_pic = "";
					if(arrO[i]['profile_pic_path'] == null){
						offer_owner_profile_pic = "<span class='fa fa-user fa-5x breadcrumb-state-greyed'></span>";
					}else{
						offer_owner_profile_pic = "../profilepic/" +arrO[i]['profile_pic_path'];
						offer_owner_profile_pic = "<img alt='Profile Picture' src='" + offer_owner_profile_pic + "'>";
					}
					var offer_summary = arrO[i]['offer_summary'];
					var offer_date = arrO[i]['time_date_string'];
					var offer_currency = arrO[i]['currency_name'];
					var offer_type = arrO[i]['offer_type'];
					$('#offer-post').prepend(""+
								"<div class='row col-xs-12 offer-post' id='offer-post-"+offer_id + "-" + offer_type +"'>" +
								      "<div class='row col-xs-12 offer-post-and-pic-name'>" +
									      "<span class='row col-xs-2 post-pic' id='offer-post-pic-" + offer_id + "-" + offer_type + "'>"+
										      "" + offer_owner_profile_pic + ""+
									      "</span>"+
									      "<span id='offer-name' class='offer-name-link'>" +
										      "<a href='../user/?user=" + offer_entity_id + "' class='offer-name-link-" + offer_id + "'> "+names+"</a>"+
									      "</span>"+
									      "<span id='offer-post-time' class='breadcrumb-state-greyed'>"+
										      "" + offer_date +
									      "</span>"+
								      "<div>"+
								      "<div id='post-offer-details-" + offer_id + "-" + offer_type +  "' class='row col-xs-12'>"+
									      "<p><b>"+offer_summary+": </b> "+offer_details+"</p>"+
									      "<p><b>Offer category: </b>"+programme_name+"</p>"+
									      "<p><b>Offer amount: </b>"+offer_amount+" "+ offer_currency +"</p>"+
								      "</div>"+
								      "<div class='row col-xs-12 offer-post-actions' id='offer-post-actions-" + offer_id + "-" + offer_type + "'>"+
									      "<a href='#' class='offer-link " + visibility_class_name + "' id='offer-apply-link-" + offer_id + "-" + offer_entity_id + "-" + offer_type +  "' title='Apply to this offer'>Apply &nbsp;</a>"+
									      "<a href='#' class='offer-link " + visibility_class_name + "' id='offer-message-link-"+offer_id+"-" + offer_entity_id + "-" + offer_type + "' title ='Send a message to this offer owner'> Message " + generateValueArray(names) + "</a>"+
								      "</div>"+
								"<div>" +
								"<br>");
					
					$('#offer-post-' + offer_id + '-' + offer_type).append(""+
									"<div class'col-xs-12 new-application' id='new-application-" + offer_id + "-" + offer_type + "'>"+
										
									"</div>"+
								      "");
					
					var aArr = arrO[i]['applications'];
					
					for(var j=0;j<aArr.length;j++){
						var offer_application_id = aArr[j]['offer_application_id'];
						var offer_application_entity_id = aArr[j]['offer_application_entity_id'];
						var offer_application_details = aArr[j]['offer_application_details'];
						var applicant_names = aArr[j]['names'];
						var profile_pic_path = "../profilepic/" +aArr[j]['profile_pic_path'];
						var profile_pic_path = "";
						if(aArr[j]['profile_pic_path'] == null){
							profile_pic_path = "<span class='fa fa-user fa-3x breadcrumb-state-greyed'></span>";
						}else{
							profile_pic_path = "../profilepic/" +aArr[j]['profile_pic_path'];
							profile_pic_path = "<img alt='Profile Picture' src='" + profile_pic_path + "'>";
						}
						var offer_application_date = aArr[j]['offer_application_date'];
						var application_visibility_class_name = aArr[j]['visibility_class_name'];
						
						$('#offer-post-' + offer_id + '-' + offer_type).append(""+ 
										      "<div class='comments post-text row col-xs-12' id='offer-application-" + offer_application_id + "'>" +
											      "<div class='row col-xs-2 status-pic'> " +
												      "<div class='status-pic'>" +
													      "" + profile_pic_path + "" +
												      "</div>" +
											      "</div>" +
											      "<div class='comments row col-xs-10' id='application-text-" + offer_application_id + "'> <p>" +
												      "<a href='../user/?user=" + offer_application_entity_id + "' > " + applicant_names + "  </a>" + offer_application_details + "(" + offer_application_date + " )</p>" +
											      "</div>"+
											      "<div class='accept-action row col-xs-12' id='accept-action-" + offer_application_id+ "'>"+
												      "<div class='row col-xs-4 col-xs-offset-8'>"+
													      "<a href='' class='accept-link " + application_visibility_class_name + "' id='application-accept-link-" + offer_id + "-" + offer_application_id + "-" + offer_entity_id + "-" + offer_application_entity_id + "-" + offer_type + "' title='Accept this application'>Accept application</a>"+
												      "</div>"+
											      "</div>"+
										      "</div>");
						//$('#application-accept-link-' + offer_id + "-" + offer_application_id + "-" + offer_entity_id + "-" + offer_application_entity_id + '-' + offer_type).addClass(application_visibility_class_name);
					    
					}
				}else if(arrO[i]['offer_type'] == "service"){
					var s_offer_id = arrO[i]['s_offer_id'];
					var s_starting_date = arrO[i]['s_starting_date'];
					var s_names = arrO[i]['s_names'];
					var visibility_class_name = arrO[i]['visibility_class_name'];
					var s_offer_name = arrO[i]['s_offer_name'];
					var s_offer_country = arrO[i]['s_offer_country'];
					var s_offer_type = arrO[i]['offer_type'];
					var s_offer_state = arrO[i]['s_offer_state'];
					var s_offer_residence = arrO[i]['s_offer_residence'];
					//var s_profile_pic_path = "../profilepic/" + arrO[i]['s_profile_pic_path'];
					var s_profile_pic_path = "";
					if(arrO[i]['s_profile_pic_path'] == null){
						 s_profile_pic_path = "<span class='fa fa-user fa-5x breadcrumb-state-greyed'></span>";
					}else{
						 s_profile_pic_path = "../profilepic/" + arrO[i]['s_profile_pic_path'];
						 s_profile_pic_path = "<img alt='Profile Picture' src='" + s_profile_pic_path + "'>";
					}
					var s_working_hours = arrO[i]['s_working_hours'];
					var time_date_string = arrO[i]['time_date_string'];
					var s_entity_id = arrO[i]['s_entity_id'];
					var s_offer_details = arrO[i]['s_offer_details'];
					
					//call function to append to view
					appendServiceOffer(s_offer_name, s_offer_country,
							    s_offer_state, s_offer_residence,
							    s_working_hours, s_starting_date,
							    s_offer_details, time_date_string,
							    s_offer_id, s_entity_id,
							    s_profile_pic_path, s_names,
							    s_offer_type,visibility_class_name);
					
					
					
					var aArr = arrO[i]['applications'];
					
					for(var j=0;j<aArr.length;j++){
						var s_offer_application_id  = aArr[j]['offer_application_id'];
						var s_offer_application_entity_id = aArr[j]['offer_application_entity_id']; 
						var s_offer_application_details = aArr[j]['offer_application_details'];
						var s_applicant_names = aArr[j]['names'];
						//var s_offer_application_profile_pic_path = "../profilepic/" +aArr[j]['profile_pic_path'];
						var s_offer_application_profile_pic_path = "";
						if(aArr[j]['profile_pic_path'] == null){
							s_offer_application_profile_pic_path = "<span class='fa fa-user fa-3x breadcrumb-state-greyed'></span>";
						}else{
							s_offer_application_profile_pic_path = "../profilepic/" +aArr[j]['profile_pic_path'];
							s_offer_application_profile_pic_path = "<img alt='Profile Picture' src='" + s_offer_application_profile_pic_path + "'>";
						}
						var s_offer_application_date = aArr[j]['offer_application_date'];
						var s_visibility_class_name  = aArr[j]['visibility_class_name'];
						
						//call function to append the
						$('#offer-post-' + s_offer_id + '-' + s_offer_type).append(""+ 
										      "<div class='comments post-text row col-xs-12' id='offer-application-" + s_offer_application_id + "'>" +
											      "<div class='row col-xs-2 status-pic'> " +
												      "<div class='status-pic'>" +
													      "" + s_offer_application_profile_pic_path + "" +
												      "</div>" +
											      "</div>" +
											      "<div class='comments row col-xs-10' id='application-text-" + offer_application_id + "'> <p>" +
												      "<a href='../user/?user=" + s_offer_application_entity_id + "' > " + s_applicant_names + "  </a>" + s_offer_application_details + "(" + s_offer_application_date + " )</p>" +
											      "</div>"+
											      "<div class='accept-action row col-xs-12' id='accept-action-" + s_offer_application_id+ "'>"+
												      "<div class='row col-xs-4 col-xs-offset-8'>"+
													      "<a href='' class='accept-link' id='application-accept-link-" + s_offer_id + "-" + s_offer_application_id + "-" + s_entity_id + "-" + s_offer_application_entity_id + "-" + s_offer_type + "' title='Accept this application'>Accept application</a>"+
												      "</div>"+
											      "</div>"+
										      "</div>");
						$('#application-accept-link-' + s_offer_id + "-" + s_offer_application_id + "-" + s_entity_id + "-" + s_offer_application_entity_id + '-' + s_offer_type).addClass(s_visibility_class_name);	
					
					}
					
				}else if(arrO[i]['offer_type'] == "material"){
					var m_offer_residence = arrO[i]['m_offer_residence'];
					var m_offer_category = arrO[i]['m_offer_category']; 
					var m_offer_country = arrO[i]['m_offer_country'];
					var m_visibility_class_name = arrO[i]['visibility_class_name'];
					
					var m_profile_pic_path = "";
					
					if(arrO[i]['m_profile_pic_path'] == null){
						m_profile_pic_path = "<span class='fa fa-user fa-5x breadcrumb-state-greyed'></span>";
					}else{
						m_profile_pic_path = "../profilepic/" + arrO[i]['m_profile_pic_path'];
						m_profile_pic_path =  "<img alt='Profile Picture' src='" + m_profile_pic_path + "'>";
					}
					var m_offer_type = arrO[i]['offer_type'];
					var m_offer_units = arrO[i]['m_offer_units'];
					var m_offer_name = arrO[i]['m_offer_name'];
					var m_offer_id = arrO[i]['m_offer_id'];
					var m_offer_other_photo_1 = arrO[i]['m_offer_other_photo_1'];
					var m_offer_other_photo_2 = arrO[i]['m_offer_other_photo_2'];
					var m_offer_main_photo = arrO[i]['m_offer_main_photo'];
					var m_time_date_string = arrO[i]['time_date_string'];
					var m_offer_details = arrO[i]['m_offer_details'];
					var m_offer_state = arrO[i]['m_offer_state'];
					var m_entity_id = arrO[i]['m_entity_id'];
					var m_names = arrO[i]['m_names'];
					
					appendMaterialOffer(m_offer_category,m_offer_name,
							    m_offer_units,m_offer_country,
							    m_offer_state,m_offer_residence,m_offer_details,
							    m_profile_pic_path,m_offer_id,
							    m_time_date_string,m_offer_main_photo,
							    m_offer_other_photo_1,m_offer_other_photo_2,
							    m_names,m_entity_id,
							    m_offer_type,m_visibility_class_name);
					
					var aArr = arrO[i]['applications'];
					
					for(var j=0;j<aArr.length;j++){
						var m_offer_application_id  = aArr[j]['offer_application_id'];
						var m_offer_application_entity_id = aArr[j]['offer_application_entity_id']; 
						var m_offer_application_details = aArr[j]['offer_application_details'];
						var m_applicant_names = aArr[j]['names'];
						//var m_offer_application_profile_pic_path = "../profilepic/" +aArr[j]['profile_pic_path'];
						var m_offer_application_profile_pic_path = "";
						if(aArr[j]['profile_pic_path'] == null){
							m_offer_application_profile_pic_path = "<span class='fa fa-user fa-5x breadcrumb-state-greyed'></span>";
						}else{
							m_offer_application_profile_pic_path = "../profilepic/" +aArr[j]['profile_pic_path'];
							
							m_offer_application_profile_pic_path = "<img alt='Profile Picture' src='" + m_offer_application_profile_pic_path + "'>";
						}
						var m_offer_application_date = aArr[j]['offer_application_date'];
						var m_visibility_class_name  = aArr[j]['visibility_class_name'];
						//WE ARE HERE WILL PROCEED=============================================
						
						$('#offer-post-' + m_offer_id + '-' + m_offer_type).append(""+ 
										      "<div class='comments post-text row col-xs-12' id='offer-application-" + m_offer_application_id + "'>" +
											      "<div class='row col-xs-2 status-pic'> " +
												      "<div class='status-pic'>" +
													      "" + m_offer_application_profile_pic_path + "" +
												      "</div>" +
											      "</div>" +
											      "<div class='comments row col-xs-10' id='application-text-" + m_offer_application_id + "'> <p>" +
												      "<a href='../user/?user=" + m_offer_application_entity_id + "' > " + m_applicant_names + "  </a>" + m_offer_application_details + "(On " + m_offer_application_date + " )</p>" +
											      "</div>"+
											      "<div class='accept-action row col-xs-12' id='accept-action-" + m_offer_application_id+ "'>"+
												      "<div class='row col-xs-4 col-xs-offset-8'>"+
													      "<a href='' class='accept-link' id='application-accept-link-" + m_offer_id + "-" + m_offer_application_id + "-" + m_entity_id + "-" + m_offer_application_entity_id + "-" + m_offer_type + "' title='Accept this application'>Accept application</a>"+
												      "</div>"+									
											      "</div>"+
										      "</div>");
						$('#application-accept-link-' + m_offer_id + "-" + m_offer_application_id + "-" + m_entity_id + "-" + m_offer_application_entity_id + '-' + m_offer_type).addClass(m_visibility_class_name);	
						
						
						
					}
				}

				  
				  
			}
		}else{
			$('#offer-post').append("No offers available at the moment");  
		}
	});
	
	$('#offer-post').on('click','.offer-link',function(e){
		var eventSourceElement = e.target.id;
		e.preventDefault();
		//alert(eventSourceElement);
		var constituents = eventSourceElement.split('-');
		var type = constituents[1];
		var offer_id = constituents[3];
		var offer_owner_entity_id = constituents[4];
		var offer_type = constituents[5];
		//alert(offer_owner_entity_id);
		if(type=="apply"){
			//place a textarea for typing i the new application div.
			$('#new-application-'+offer_id + '-' + offer_type).html("<br><br>"+
							"<div id ='apply_input_text-"  + offer_id +"-" + offer_type +  "'>" +
								"<div class='col-xs-2 status-pic' id='pic'>"+
									"<div class='status-pic'>" +
										"<img alt='Profile ure' src='"+ profile_pic_path_Global + "'>" +
									"</div>" +
								"</div> " +
								"<div class='row' id='comment-text-"+ offer_id + "-" + offer_type +  "'>" +
									"<textarea class='application-text-area row col-xs-9' rows='2' name='details' id='application-details-" + offer_id + "-" + offer_type + "' placeholder='Type your application here and enter to apply...'></textarea>" +
								"</div>" +
							"</div><br>");
		}else if(type=="message"){
			var owner_entity_id = offer_owner_entity_id;
			//var owner_entity_id = 10;
			var names = $(".offer-name-link-" + offer_id).text();
			$.post("../mapping/mapping.jsp", {tag:"isConnected",sender:user_entity_id_Global,receiver:owner_entity_id}, function(data){			      
			      if(data['success'] == 1){
				
					//the two are connected and a message can be sent
					$('#send-message-footer-home').html("<button type='button' class='btn btn-default' data-dismiss='modal'>Cancel</button>" +
					"<button id='send-message-" + user_entity_id_Global + "-" + owner_entity_id + "' type='submit' class='btn btn-primary'>Send</button>");
					$('#message-modal').modal('show');
					$('#message-error').html("");
					
					$('#new-message-display-area-home').removeClass('hidden');
					
					$('#send-message-textbox-home').val(names);
				
					$('#dialog-id').html(user_entity_id_Global + "-" + owner_entity_id);
				
			      }else if(data['success'] == 2){
				
					alert("error");
					//self messaging
				
			      }else{
				    //send a connection request
				    
				    $('#recommendation-action-connect').modal('show');
				    $('#connect-action-body').html("<p>You are not connected with post owner. Please send a connection request to <a href='#'>" + names + "</a></p>");
				    $('#connect-modal-footer').html("<button id='connect-" + user_entity_id_Global + "-" + owner_entity_id + "' type='button' class='connect-button btn btn-primary'> Send request</button>");

			      }
			  
			},'json');
			
			
		}else{
			  //do nothing
		}
	});
	
	$('#recommendation-action-connect').on('click','.connect-button', function(event){
		var eventSourceId = event.target.id;
		//alert(eventSourceId);
		var eventSourceElementsArray = eventSourceId.split('-');
		var sender = user_entity_id_Global;//logged in user; the message sender
		var receiver = eventSourceElementsArray[1];
		
		$("#" + eventSourceId).html("<img  src='../res/images/loading.gif' /> Connecting...");
		
		$.post("../mapping/mapping.jsp", {tag:"connect",sender:sender,receiver:receiver}, function(data){
	      
			if(data['success'] == 0){
				//request already exist
				$('#connect-action-body').html("<p style='color:blue'>" + data['message'] + "</p>");
				$('#connect-modal-footer').html("<button type='button' class='create-connection btn btn-default' id='connect-ok-button'>OK</button>");
			}else if(data['success'] == 1){
				//request sent successfully
				$('#connect-action-body').html("<p style='color:green'>" + data['message'] + "</p>");
				$('#connect-modal-footer').html("<button type='button' class='create-connection btn btn-default' data-dismiss='modal' id='connect-ok-button'>OK</button>");
				
			}else{//data[] == 2
				//an error occured and request failed
				$('#connect-action-body').html("<p style='color:red'>" + data['message'] + "</p>");
				$('#connect-modal-footer').html("<button type='button' class='create-connection btn btn-default' data-dismiss='modal' id='connect-ok-button'>OK</button>");
			  
			}
		
		},'json');  
	});
	$('#recommendation-action-connect').on('click','.create-connection',function(e){
		  
		  $('#connect-action-body').html("");
		  $('#connect-modal-footer').html("");
		  $('#recommendation-action-connect').modal('hide');
		  
	});

	$('#offer-post').keypress('.application-text-area',function(e){
		if (e.keyCode == 13){
			//alert("working");
			e.preventDefault();
			var elementId = e.target.id;
			//alert(elementId);
			var resultArray = elementId.split('-');
			var offer_id = resultArray[2];
			var offer_t = resultArray[3];
			var application_string = $('#application-details-' + offer_id + '-' + offer_t).val();
			var table_name = getTableName(offer_t);
			//post it to db and then append it
			$.post("../mapping/offer_mapping.jsp", {tag:"save_offer_application",offer_id:offer_id,application_text:application_string,table_name:table_name}, function(data){
			  $('#new-application-'+ offer_id).html("<img  src='../res/images/loading.gif' /> Posting...");
			  if(data['success'] == 1){
					//append the application_string as an application
					//alert("was successiful");
					$('#offer-post-'+offer_id + '-' + offer_t).append(""+
							"<div class='comments post-text row col-xs-12' id='offer-application-" + offer_id + "-" + offer_t +  "'>" +
								"<div class='row col-xs-2 status-pic'>" +
								    "<div class='status-pic'>" +
									"<img alt='Profile Picture' src='" + profile_pic_path_Global + "'>" + 
								    "</div>" +
								"</div>" +
								"<div class='comment row col-xs-10' id='application-text'>" +
								    "<p><a href='' >" + user_full_names_Global + "</a>" + " " + application_string + "(On "+data['time_details']+")</p>" +
								"</div>" +
							"</div>");
					$('#new-application-'+ offer_id + '-' + offer_t).html("");
					
					
				}else{
					//do nothing
				}
			});
			
			
		}
	});
	
	$('#application-confirm-service-modal').on('hidden.bs.modal', function () {
		restoreModalState();
		//alert("working");
	});
	
	$('#offer-post').on('click','.accept-link', function(e){
		var elementId = e.target.id;
		e.preventDefault();
		//alert(elementId);
		
		var elemenArray = elementId.split('-');
		
		var offer_id = elemenArray[3];
		var application_id = elemenArray[4];
		var offer_owner_entity_id = elemenArray[5];
		var applicant_entity_id = elemenArray[6];
		var offer_type = elemenArray[7];
		var table_name = getTableName(offer_type);
		
		/*3-offer_id,4-application_id,5-offer_owner_entity_id,6-applicant_entity_id,7-offer_type*/
		 $('#offer-acceptpance-pay-options').addClass("hidden");
		if(offer_type == "money"){
			//let users confirm their actions before processing their request.
			//tag will be accept_service_application  
			$('#confirm-modal-body-container').removeClass('col-sm-offset-1');
			$('#application-confirm-service-modal').removeClass('bs-example-modal-sm');
			$('#confirm-modal-container').removeClass('modal-sm');
			$('#service-confirm-area-message').html("");
			$('#application-confirm-service-modal').modal('show'); 
			$('#confirm-application-cancel-btn').text("I changed my mind");
			//Hide the Accept button
			$('#confirm-message').html("Are you sure you want to accept this application?");
			$('#confirm-application-accept-btn').removeClass("hidden");
			$('#service-confirm-area').html(offer_id + '-' + application_id + '-' + offer_owner_entity_id + '-' + applicant_entity_id + '-' + offer_type + '-' +table_name);
			  
		}else if(offer_type == "service"){
			//let users confirm their actions before processing their request.
			//tag will be accept_service_application
			$('#confirm-modal-body-container').removeClass('col-sm-offset-1');
			$('#application-confirm-service-modal').addClass('bs-example-modal-sm');
			$('#confirm-modal-container').addClass('modal-sm');
			$('#service-confirm-area-message').html("");
			$('#application-confirm-service-modal').modal('show');
			$('#confirm-application-cancel-btn').text("I changed my mind");
			//Hide the Accept button
			$('#confirm-message').html("Are you sure you want to accept this application?");
			$('#confirm-application-accept-btn').removeClass("hidden");
			$('#service-confirm-area').html(offer_id + '-' + application_id + '-' + offer_owner_entity_id + '-' + applicant_entity_id + '-' + offer_type + '-' +table_name);
			
			
			
		}else if(offer_type == "material"){
			//let users confirm their actions before processing their request.
			//tag will be accept_material_application
			$('#confirm-modal-body-container').removeClass('col-sm-offset-1');
			$('#application-confirm-service-modal').addClass('bs-example-modal-sm');
			$('#confirm-modal-container').addClass('modal-sm');
			$('#service-confirm-area-message').html("");
			$('#application-confirm-service-modal').modal('show');
			$('#confirm-application-cancel-btn').text("I changed my mind");
			//Hide the Accept button
			$('#confirm-message').html("Are you sure you want to accept this application?");
			$('#confirm-application-accept-btn').removeClass("hidden");
			$('#service-confirm-area').html(offer_id + '-' + application_id + '-' + offer_owner_entity_id + '-' + applicant_entity_id + '-' + offer_type + '-' +table_name);
		}
		
		
	});
	
	$('#confirm-application-accept-btn').click(function(e){
		var details = $('#service-confirm-area').text();
		var dA = details.split('-');		
		var offer_id = dA[0];
		var application_id = dA[1];
		var offer_owner_entity_id = dA[2];
		var applicant_entity_id = dA[3];
		var offer_type = dA[4];
		var table_name = dA[5];
		//alert(details);
		if(offer_type=="money"){
			  $('#confirm-message').html("Select source of funds");
			  $('#offer-acceptpance-pay-options').removeClass("hidden");
			  //$('#confirm-application-accept-btn').text("Next");
			  //$('#confirm-application-accept-btn').append("<span class='glyphicon glyphicon-arrow-right'>");
			  $('#confirm-application-accept-btn').addClass("hidden");
			  $('#application-confirm-modal-footer').append("<button id='next-on-confirm-money-application' type='button' class='btn btn-primary accept-btn-to-next'>Next <span class='glyphicon glyphicon-arrow-right'></span></button>");
		}else{
			$('#confirm-wait-gif').removeClass("hidden");
			$('#offer-acceptpance-pay-options').addClass("hidden");
			//post to server and wait for feedback
			$.post("../mapping/offer_mapping.jsp",{tag:"save_application_acceptance",offer_id:offer_id,application_id:application_id,offer_owner_entity_id:offer_owner_entity_id,table_name:table_name,applicant_entity_id:applicant_entity_id},function(data){
				
				  if(data['success']==1){
					  //Let the user know that application was successiful and hide the whole offer posta and its thread
					  $('#confirm-wait-gif').addClass("hidden");
					  $('#service-confirm-area-message').html("<div class='alert alert-success'>" +  data['message'] + "</div>");
					  $('#confirm-application-cancel-btn').text("OK");
					  //Hide the Accept button
					  $('#confirm-application-accept-btn').addClass("hidden");
					  $('#confirm-message').html("");
					  $('#offer-post-' + offer_id + '-' + offer_type).addClass("hidden");
					  
					  
					  
				}else{
					//let the user know that something went wrong; the pst should be visible
					  $('#confirm-wait-gif').addClass("hidden");
					  $('#service-confirm-area-message').html("<div class='alert alert-danger'>" +  data['message'] + "</div>");
					  $('#confirm-application-cancel-btn').text("OK");
					  //Hide the Accept button
					  $('#confirm-application-accept-btn').addClass("hidden");
					  $('#confirm-message').html("");
					  
				}
				
			},'json');
		}	
		
	});
	
	$('#confirm-modal-container').on('click','.accept-btn-to-next',function(e){
		if($('#acceptpance-harambesa-option').is(':checked')){
			//$('#service-confirm-area').removeClass("hidden");
			$('#payment-aggregator-container').addClass("hidden");
			$('#confirm-message').html("You have chose to accept this application using funds in your harambesa account");
			//prepend a back button to the footer
			$('#application-confirm-modal-footer').prepend("<button id='application-accept-back-btn' type='button' class='back-button-first btn btn-danger'><span class='glyphicon glyphicon-arrow-left'></span> Back</button>");
			$('#next-on-confirm-money-application').addClass("hidden");
			//add a new button
			 $('#application-confirm-modal-footer').append("<button id='finish-money-application-acceptpance-btn' type='button' class='finish-money-application-acceptpance btn btn-primary'> Finish </button>");
		}else if($('#acceptpance-aggregator-option').is(':checked')){
			//Fetch params 
			//append them on payment modal div
			 var params = $('#service-confirm-area').text().trim();
			 $('#param-area').html(params);
			 $('#application-confirm-service-modal').modal('hide');
			 window.location.replace("../deposits/");
			// $('#payment').modal('show');
		}else{
			//nothing is selected so show an alert
			$('#payment-source-error-alert').html("<div class='alert alert-danger'>You have not selected any option</div>");
		}
	});
	
	
	
	$('#confirm-application-cancel-btn').click(function(e){  
		$('#application-confirm-service-modal').modal('hide');
	});
	
	$('#acceptpance-aggregator-option').click(function(e){
		$('#payment-source-error-alert').html("");
	});
	
	$('#acceptpance-harambesa-option').click(function(e){
		 $('#payment-source-error-alert').html("");
	});
	
	$('#confirm-modal-container').on('click','.back-button-first',function(e){
		  //show the aggregator option if it is hidden or the wallet option if it the one that is hidden
		  //remove the back button
		  $('#application-accept-back-btn').remove();
		  $('#payment-aggregator-container').removeClass("hidden");
		  $('#next-on-confirm-money-application').removeClass("hidden");
		  $('#finish-money-application-acceptpance-btn').remove();
		  $('#service-confirm-area-message').html("");
		  $('#confirm-message').html("Select source of funds");
	});
	
	$('#application-confirm-modal-footer').on('click','.finish-money-application-acceptpance',function(e){
		//offer_id + '-' + application_id + '-' + offer_owner_entity_id + '-' + applicant_entity_id + '-' + offer_type + '-' + table_name
		var ingridients = $('#service-confirm-area').text().trim();
		var ingrArr = ingridients.split('-'); 
		
		var post_offer_id = ingrArr[0];
		var application_id = ingrArr[1];
		var offer_owner_id = ingrArr[2];
		var applicant_id = ingrArr[3];
		var offer_type = ingrArr[4];
		var table_name = ingrArr[5]; 
		
		finishMoneyApplicationByWallet(post_offer_id,application_id,offer_owner_id,applicant_id,offer_type,table_name);
		
		
	});
	
	$(function (){ $("#state").popover({
		html: 'true',
		placement: 'top',
		content: 'This is the state or province where you wish to ofer your service'})
		.blur(function () {
		$(this).popover('hide');
		});
	});

	$(function (){ $("#service-offer-details").popover({
		html: 'true',
		placement: 'top',
		content: 'Give more details to your offer so that applicants get easier time when applying'})
		.blur(function () {
		$(this).popover('hide');
		});
	});
	
	$('#message-modal').on('hidden.bs.modal', function () {
		$('#message-form-home').data('bootstrapValidator').resetForm();
		 $('#message-error-success').html("");
	});
    
	
	populateCountries();
	populateServices();
	populateItems();
	validateServiceForm();
	validateMaterialForm();
	validateMessageForm(user_entity_id_Global);
	
	$('#start-date').datepicker({
		format: "dd/mm/yyyy",
		startDate: '1d',
		autoclose:true
	});
});

//................................................
  //FUNCTIONS
//.............................................

	function finishMoneyApplicationByWallet(offer_id,application_id,offer_owner_entity_id,applicant_entity_id,offer_type,table_name){
		$('#confirm-message').html("");
		$.post("../mapping/offer_mapping.jsp",{tag:"accept_money_application",offer_id:offer_id,application_id:application_id,offer_owner_entity_id:offer_owner_entity_id,applicant_entity_id:applicant_entity_id,offer_type:offer_type,table_name:table_name},function(data){  
			if(data['success'] == 1){
				$('#service-confirm-area-message').html("<div class='alert alert-success'>" +  data['message'] + "</div>");
			}else{
				if(data['redir'] != null){
					//Reload; user taken to login page
					window.location.reload();
				}else{
					//Error occured
					$('#service-confirm-area-message').html("<div class='alert alert-warning'>" +  data['message'] + "</div>");
				}
			}
		},'json');
	}

      var next = function(db, key){
	  for (var i = 0; i < db.length; i++) {
	    if (db[i].key === key) {
	      return db[i] && db[i].value;
	    }
	  }
	};
	
	function generateValueArray(str){
		//var user_values = str;
		var user_values_array = str.split(" ");
		return user_values_array[0];
	}
	
	function populateCountries(){
		$('#loading_countries').removeClass('hidden');
		$('#loading_countries_materials').removeClass('hidden');
		
		$.post("../mapping/offer_mapping.jsp",{tag:"offer_countries"}, function(data){  
			var list = '<option value="" >Select country</option>';
			for(var i = 0; i< data.length; i++){
			    list = list + '<option value="'+data[i]["name"]+'"> '+ data[i]["name"] + '</option>';
			}
			$('#material-countries').html(list);
			$('#service-countries').html(list);
			$('#loading_countries').addClass('hidden');
			$('#loading_countries_materials').addClass('hidden');
			
		},'json');
	}
	
	function populateServices(){
		$('#loading_services').removeClass('hidden');
		$.post("../mapping/offer_mapping.jsp",{tag:"offer_services"}, function(data){  
			var list = '<option value="" >Select service</option>';
			for(var i = 0; i< data.length; i++){
			    list = list + '<option value="'+data[i]["name"]+'"> '+ data[i]["name"] + '</option>';
			}
			$('#service-category').html(list);
			
			$('#loading_services').addClass('hidden');
			
		},'json');
	}
	
	function populateItems(){
		$('#loading_material').removeClass('hidden');
		$.post("../mapping/offer_mapping.jsp",{tag:"offer_items"}, function(data){  
			var list = '<option value="" >Select item</option>';
			for(var i = 0; i< data.length; i++){
			    list = list + '<option value="'+data[i]["name"]+'"> '+ data[i]["name"] + '</option>';
			}
			$('#material-category').html(list);
			
			$('#loading_material').addClass('hidden');
			
		},'json');
		
	}	
	function validateServiceForm(){
			$('#s-offer-form').bootstrapValidator({
			message: 'This value is not valid',		
			submitHandler: function(validator, form, submitButton) {
					var service_name = $('#service-category').val();
					var service_countries=$('#service-countries').val();
					var state=$('#state').val();
					var residence=$('#residence').val();
					var hours_a_day=$('#hours-a-day').val();
					var start_date = $('#start-date').val();
					var service_offer_details=$('#service-offer-details').val();  
					$.post("../mapping/offer_mapping.jsp",{tag:"save_service_offer",service_name:service_name,service_countries:service_countries,
										state:state,residence:residence,hours_a_day:hours_a_day,start_date:start_date,
										service_offer_details:service_offer_details}, function(data){
						if(data['success']==1){
							var time_details = data['time_details'];
							var service_offer_id = data['offer_id'];
							var entity_id = data['entity_id'];
							var profile_pic_path = "../profilepic/" + data['profile_pic_path'];
							var names = data['names'];
							var offer_type = data['offer_type'];
							appendServiceOffer(service_name,service_countries,state,residence,hours_a_day,start_date,
									service_offer_details,time_details,service_offer_id,entity_id,profile_pic_path,names,offer_type,"hidden");
						}else{
							///check if it has a redirect
							if(data['redir'] != null){
								//reload the page
								window.location.reload();
							}else{
								//do another thing
							}
						}
					},'json');
					
			},
			feedbackIcons: {
			    valid: 'fa fa-check',
			    invalid: 'fa fa-times',
			    validating: 'fa fa-refresh'
			},
			fields: {
			    service_category: {
				validators: {
				    notEmpty: {
					message: 'Service has not been selected'
				    }
				}
			    },            
			    service_countries: {
				validators: {
				    notEmpty: {
					message: 'Select country'
				    }
				}
			    },
			    state:{
				validators:{
				    notEmpty:{
					message:'state/province is empty'
				    }
				}
			    },
			    residence:{
				validators:{
				    notEmpty:{
					message:'Residence field is empty'
				    }
				}
			    },
			    hours_a_day:{
				validators:{
				    notEmpty:{
					message:'Working hours per day is required'
				    },
				    digits: {
					message: 'A number required'
				    }
				}
			    },
			    start_date:{
			      validators:{
				notEmpty:{
				  message:'Starting date is empty'
				},
				date:{
					format:'DD/MM/YYYY',
					message:'Your date does not seem to be ok'
				}
			      }
			    },
			    service_offer_details:{
			      validators:{
				notEmpty:'Offer description cannot be empty'
			      }
			    }
			}
		});
	}
	
	function validateMaterialForm(){
			$('#g-offer-form').bootstrapValidator({
			message: 'This value is not valid',		
			submitHandler: function(validator, form, submitButton) {
				var material_category = $('#material-category').val();
				var item_name = $('#material-name').val();
				var no_of_items = $('#material-units').val();
				var country = $('#material-countries').val();
				var state = $('#material-state').val();
				var residence = $('#material-residence').val();
				var details = $('#material-offer-details').val();
				
				var formObject = document.getElementById("g-offer-form");
				var formData = new FormData(formObject);
				$.ajax({
					url: '../material_offer?tag=save_material_offer',
					type: 'POST',
					data: formData,
					async: false,
					success: function (data) {
						if(data['success'] == 1){
							//it was a success: append the post
							  var profile_pic_path = data['profile_pic_path'];
							  var material_offer_id = data['material_offer_id'];
							  var time_details = data['time_details'];
							  var main_photo_path = data['main_photo_path'];
							  var other_photo_one_path = data['other_photo_one_path'];
							  var other_photo_two_path = data['other_photo_two_path'];
							  var names = data['names'];
							  var entity_id = data['entity_id'];
							  var offer_type = data['offer_type'];
							  var m_visibility_class = "hidden";
							  
							  appendMaterialOffer(material_category,item_name,no_of_items,country,state,residence,details,
							  profile_pic_path,material_offer_id,time_details,main_photo_path,other_photo_one_path,other_photo_two_path,
							  names,entity_id,offer_type,m_visibility_class);
						}else{
							    //check if it has a redirect
							    if(data['redir'] != null){
								    //reload the page
								    window.location.reload();
							    }else{
								    //do another thing
							    }
						}
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
			    material_category: {
				validators: {
				    notEmpty: {
					message: 'Material category has not been selected'
				    }
				}
			    },            
			    material_name: {
				validators: {
				    notEmpty: {
					message: 'Material name field cannot be empty'
				    }
				}
			    },
			    material_units:{
				validators:{
				    notEmpty:{
					message:'Number of items cannot be empty'
				    },
				    digits: {
					  message: 'A number is required'
				    }
				}
			    },
			    material_countries:{
				validators:{
				    notEmpty:{
					message:'Select country' 
				    }
				}
			    },
			    material_state:{
				validators:{
				    notEmpty:{
					message:'State/province field is empty'
				    }
				}
			    },
			    material_residence:{
			      validators:{
				notEmpty:{
				  message:'Residence is empty'
				}
			      }
			    },
			    material_offer_details:{
			      validators:{
				notEmpty:{
				  message:'Offer description cannot be empty'
				}
			      }
			    },
			    photo_main:{
			      validators:{
				notEmpty:{
				  message:'Main photo has not been collected'
				}
			      }
			    },
			    other_photos_one:{
			      validators:{
				notEmpty:{
				  message:'Photo has not been selected'
				}
			      }
			    },
			    other_photos_two:{
			      validators:{
				notEmpty:{
				  message:'Photo has not been selected'
				}
			      }
			    }
			}
		});
	}
	function changeDateFormat(p_date){
		var new_date_array = p_date.split('/');
		var year = new_date_array[2];
		var month = new_date_array[1];
		var day = new_date_array[0];
		var new_format = year+ "/" +month + "/" + day;
		
		return new_format;
	}
	function appendMaterialOffer(material_category,item_name,no_of_items,country,state,residence,details,profile_pic_path,
		material_offer_id,time_details,main_photo_path,other_photo_one_path,other_photo_two_path,
		names,entity_id,offer_type,class_name){
			$('#offer-post').prepend(""+
				"<div class='row col-xs-12 post' id='offer-post-"+material_offer_id+"-" + offer_type +  "'>" +
					"<div class='row col-xs-12 offer-post-and-pic-name'>" +
						"<span class='row col-xs-2 post-pic' id='offer-post-pic-"+material_offer_id+"'>"+
							"" + profile_pic_path + ""+
						"</span>"+
						"<span id='offer-name' class='offer-name-link'>" +
							"<a href='../user/?user=" + entity_id + "' class='offer-name-link-" + material_offer_id + "'> "+names+"</a>"+
						"</span>"+
						"<span id='offer-post-time' class='breadcrumb-state-greyed'>"+
							"" + time_details +
						"</span>"+
					"<div>"+
					
					"<div id='' class='row col-xs-12'>" +
						"<p class='teller'>" + names + " " + getTypeString(offer_type) + "</p>"+
					"</div>"+
					
					"<div id='post-offer-details-"+material_offer_id+"' class='row col-xs-12'>"+
						"<p><b>Material category: </b> "+material_category+"</p>"+
						"<p><b>Item name: </b> "+item_name+"</p>"+
						"<p><b>Number of items: </b> "+no_of_items+"</p>"+
						"<p><b>Location: </b> " + residence + ", " + state + ", " + country + "</p>" +
						"<p><b>Offer details: </b>" + details + "</p>" +
					"</div>"+
					"<div class='row row col-xs-12'>"+
						"<p> <b>Item Photos</b></p>"+
					"</div>"+
					
					"<div class='row'>" +
						"<span class='col-xs-4' id='offer-post-pic-"+material_offer_id+"'>"+
							"<img class='post-pic pad' style='width:200px; height:220px;' src='"+main_photo_path+"'></img>"+
						"</span>"+
						"<span class='col-xs-4' id='offer-post-pic-"+material_offer_id+"'>"+
							"<img style='width:200px; height:220px;' class='post-pic pad' src='"+other_photo_one_path+"'></img>"+
						"</span>"+
						"<span class='col-xs-4' id='offer-post-pic-"+material_offer_id+"'>"+
							"<img class='post-pic pad' style='width:200px; height:220px;' src='"+other_photo_two_path+"'></img>"+
						"</span>"+
					"</div>" +
					"<div class='row col-xs-12 offer-post-actions' id='offer-post-actions-"+material_offer_id+"-" + offer_type +  "'>" +
						"<a href='#' class='offer-link " + class_name + "' id='offer-apply-link-"+material_offer_id+"-" + entity_id + "-" + offer_type + "' title='Apply to this offer'>Apply &nbsp;</a>"+
						"<a href='#' class='offer-link " + class_name + "' id='offer-message-link-"+material_offer_id+"-" + entity_id + "-" + offer_type + "' title='Send a message to this offer owner'> Message " + generateValueArray(names) + "</a>"+
					"</div>"+
				  "<div>" +
				  "");
			
				  $('#offer-post-' + material_offer_id + '-' + offer_type).append(""+
					  "<div class'col-xs-12 new-application' id='new-application-" + material_offer_id + "-" + offer_type + "'>"+
						    
					  "</div>"+
				  "");
			$('#g-form-holder').addClass('hidden');
			$('#g-offer-form').data('bootstrapValidator').resetForm();
			$('#g-offer-form').get(0).reset();
						
	}
	
	function appendServiceOffer(service_name,service_countries,state,residence,hours_a_day,start_date,
				service_offer_details,time_details,service_offer_id,entity_id,profile_pic_path,names,offer_type,visibility_class_name){
					$('#offer-post').prepend(""+
						"<div class='row col-xs-12 post' id='offer-post-"+service_offer_id+"-" + offer_type + "'>" +
							"<div class='row col-xs-12 offer-post-and-pic-name'>" +
								"<span class='row col-xs-2 post-pic' id='offer-post-pic-"+service_offer_id+"'>"+
									"" + profile_pic_path + ""+
								"</span>"+
								"<span id='offer-name' class='offer-name-link'>" +
									"<a href='../user/?user=" + entity_id + "' class='offer-name-link-" + service_offer_id + "'> "+names+"</a>"+
								"</span>"+
								"<span id='offer-post-time' class='breadcrumb-state-greyed'>"+
									"" + time_details +
								"</span>"+
							"<div>"+
							"<div id='' class='row col-xs-12'>" +
								"<p class='teller'>" + names + " " + getTypeString(offer_type) + "</p>"+
							"</div>"+
							"<div id='post-offer-details-"+service_offer_id+"' class='row col-xs-12'>"+
								"<p><b>Service name: </b> "+service_name+"</p>"+
								"<p><b>Number of hours per day: </b> " + hours_a_day + "</p>"+
								"<p><b>Start day: </b> " + start_date + "</p>"+
								"<p><b>Location: </b> " + residence + ", " + state + ", " + service_countries + "</p>" +
								"<p><b>Offer details: </b>" + service_offer_details + "</p>" +
							"</div>"+
							"<div class='row col-xs-12 offer-post-actions' id='offer-post-actions-"+service_offer_id+"-" + offer_type +  "'>" +
								"<a href='#' class='offer-link " + visibility_class_name + "' id='offer-apply-link-"+service_offer_id+"-" + entity_id + "-" + offer_type +  "' title='Apply to this offer'>Apply &nbsp;</a>"+
								"<a href='#' class='offer-link " + visibility_class_name + "' id='offer-message-link-"+service_offer_id+"-" + entity_id + "-" + offer_type + "' title='Send message to this offer owner'> Message " + generateValueArray(names) + "</a>"+
							"</div>"+
						"<div>" +
						  "");
					
					$('#offer-post-' + service_offer_id + '-' + offer_type).append(""+
						"<div class'col-xs-12 new-application' id='new-application-" + service_offer_id + "-" + offer_type + "'>"+
							 
						"</div>"+
					"");
					$('#s-form-holder').addClass('hidden');
					$('#s-offer-form').data('bootstrapValidator').resetForm();
					$('#s-offer-form').get(0).reset();
	}
	
	function getTypeString(offer_type){
		var str = null;
		if(offer_type == "money"){
			str = "is offering money for needy applicants";
		}else if(offer_type == "service"){
			str = "is offering a service for needy applicants";
		}else if(offer_type == "material"){
			str = "is offering material items for needy applicants";
		}		
		return str;
	}
	
	function getTableName(offer_t){
		if(offer_t == "money"){
			return "offers";
		}else if(offer_t == "service"){
			return "service_offer";
		}else if(offer_t == "material"){
			return "material_offer";
		}
	}
	
 	function restoreModalState(){
		  $('#next-on-confirm-money-application').remove();
		  $('#finish-money-application-acceptpance-btn').remove();
		  $('#application-accept-back-btn').remove();
		  $('#payment-wallet-container').removeClass('hidden');
		  $('#payment-aggregator-container').removeClass('hidden');
 		
 	}
 	
 	function validateMessageForm(user_entity_id_Global){
		$('#message-form-home').bootstrapValidator({
			message: 'This value is not valid',
			
			submitHandler: function(validator, form, submitButton) {
					var to=$("#send-message-textbox-home").val();
					var message=$("#message-text-area-home").val();
					
					var idArray = $('#dialog-id').text().split('-');
					
					var sen = idArray[0];
					var rec = idArray[1]; 
					//alert('send-message-' + user_entity_id_Global + '-' + rec);
					
					$('#send-message-' + user_entity_id_Global + '-' + rec).html("Sending...");
			
					$.post('../mapping/mapping.jsp',{tag:"save",to:rec,msg:message,from:sen}, function(result) {
						
						if(result['success'] == 1){
						  
							  $('#new-message-display-area-home').addClass('hidden');
							      
							  $('#message-error-success').html("<div class='alert alert-success'>" +  result['message'] + "</div>");
							  
							  $("#message-text-area-home").val("");
							    
							      
							  $('#send-message-footer-home').html("<button type='button' id='ok-for-message-sent' class='btn btn-default' data-dismiss='modal'>OK</button>");
						      
						  
						}else{
						  
							$('#send-message-' + user_entity_id_Global + '-' + rec).html("Send");
							
							$('#message-error-success').html("<div class='alert alert-danger'>"  + result['message'] + "</div>"); 

						}
			    }, 'JSON');
			},
			feedbackIcons: {
			    valid: 'fa fa-check',
			    invalid: 'fa fa-times',
			    validating: 'fa fa-refresh'
			},
			fields: {
			    to: {
				validators: {
				    notEmpty: {
					message: 'Recipient name is empty'
				    }
				}
			    },            
			    message: {
				validators: {
				    notEmpty: {
					message: 'Message is required'
				    }
				}
			    }
			  
			}
	    });
      
		  
}