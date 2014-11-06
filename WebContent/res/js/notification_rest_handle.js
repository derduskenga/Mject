function fetchDonationRequest(type,donation_request_id){
	var newDataRequest = $.ajax({
		type: "POST",
		url: "../noti/",
		timeout: 30000, // timeout after 30 seconds
		dataType: "json", //JSON
		data: {tag:'donation_request', type:type,request_id: donation_request_id}
	});
	
	newDataRequest.done(function(data){
		if(data["status"] == "OK"){
			var arr = data['requests'];
						
			for(var i = 0;i<arr.length;i++){
				var request_summary = arr[i]["donation_request_summary"];
				var request_amount = arr[i]["donation_request_amount"];
				var request_details = arr[i]["donation_request_details"];
				var currency_symbol = arr[i]["currency_symbol"];
				var request_date = arr[i]["requested_on"];
				var request_category = arr[i]["programme_name"];
				var request_owner_full_name = arr[i]["post_owner_name"];
				var request_owner_entity_id = arr[i]["post_owner_entity_id"];
				var request_owner_pic_path = "";
				if(arr[i]["post_owner_pic_path"] == null){
						request_owner_pic_path = "<span class='fa fa-user fa-5x breadcrumb-state-greyed'></span>";
				}else{
						request_owner_pic_path = "../profilepic/" + arr[i]["post_owner_pic_path"];
						request_owner_pic_path = "<img alt='Profile Picture' src='" + request_owner_pic_path + "'>";
				}
				var donation_request_id = arr[i]["donation_request_id"];
				var funding_date = arr[i]["expected_funding_date"];
				var visibility_class_name = arr[i]["visibility_class_name"];
				var isActive = arr[i]["isActive"];
				var recommend_button_label = arr[i]['button_label'];
				var recommendation_count = arr[i]['recommendation_count'];
				var already_donated = arr[i]['already_donated_made'];
				var recommend_and_donate_visibility_class = arr[i]['recommend_and_donate_visibility_class'];
				var recommendation_title = "";
				
				if(isActive=='disabled'){
					
					recommendation_title = "You cannot recommend yourself";
				}else{
					
					recommendation_title = recommend_button_label ;
					
				}
				
				appendRequests(request_summary,request_amount,request_details,currency_symbol,
						request_date,request_category,request_owner_full_name,request_owner_entity_id,
						request_owner_pic_path,donation_request_id,funding_date,visibility_class_name,
						recommend_button_label,recommendation_count,already_donated,recommendation_title,isActive,
						recommend_and_donate_visibility_class);
				
				var cArr = arr[i]["comments"];
				var comment_size= cArr.length;
				
				for(var j=0;j<cArr.length; j++){
				
					var comment_id = cArr[j]['comment_id'];
					var comment_owner_names = cArr[j]['comment_owner_names'];
					var comment_date = cArr[j]['comment_date'];
					var comment_text = cArr[j]['comment_text'];
					var comment_owner_entity_id = cArr[j]['comment_owner_entity_id'];
					var comment_owner_profile_pic_path = "";
					if(cArr[j]['comment_owner_profile_pic_path'] == null){
						comment_owner_profile_pic_path = "<span class='fa fa-user fa-3x breadcrumb-state-greyed'></span>";
					}else{
						comment_owner_profile_pic_path = "../profilepic/" + cArr[j]['comment_owner_profile_pic_path'];
						comment_owner_profile_pic_path = "<img alt='Profile Picture' src='" + comment_owner_profile_pic_path + "'>";
					}
					
					appentDonationComments(donation_request_id,comment_id,comment_owner_names,comment_date,comment_text,comment_owner_entity_id,comment_owner_profile_pic_path);
					
				}
			}
		}else if(data["status"] == "NO"){
			if(data['redir']){
				window.location.reload();
			}else{
				$('#comment-and-post').html("<div class=''> " + data['message'] + " </div>");
			}
		}
	});
	
	newDataRequest.fail(function(jqXHR, exception){
		if (jqXHR.status === 0){
			$('#comment-and-post').html("<div class=''> Sorry, could not establishing a network connection. </div>");
		}else if (jqXHR.status == 404){
			$('#comment-and-post').html("<div class=''> Sorry, an error occured. </div>");
		}else if (jqXHR.status == 500){
			$('#comment-and-post').html("<div class=''> Sorry, an error occured. </div>");
		}else if (exception === 'parsererror'){
			$('#comment-and-post').html("<div class=''> Sorry, an error occured. </div>");
		}else if (exception === 'timeout'){
			$('#comment-and-post').html("<div class=''> Your request timed out. Please try again </div>");
		}else if (exception === 'abort'){
			$('#comment-and-post').html("<div class=''> Sorry, an error occured. </div>");
		}else{
			$('#comment-and-post').html("<div class=''> Sorry, an error occured. </div>");
		}
	});
}

function appendRequests(request_summary,request_amount,request_details,currency_symbol,
			request_date,request_category,request_owner_full_name,request_owner_entity_id,
			request_owner_pic_path,donation_request_id,funding_date,visibility_class_name,
			recommend_button_label,recommendation_count,already_donated,recommendation_title,isActive,
			recommend_and_donate_visibility_class){
	//assign the array of comments into a variable
	$('#comment-and-post').prepend("<div id='donation-post-" + donation_request_id + "' class='post'>" +
				"<div id = 'post-pic-row' class='col-xs-2 post-pic'> <div id = 'post-pic' class='post-pic'>" +
				"" + request_owner_pic_path + ""+
				"</div></div> <div='appeal-post' id='post-text'>" +
				"<p><a href='../user/?user=" + request_owner_entity_id + "' id='name_link-" +donation_request_id + "'>" + request_owner_full_name + " " + "</a>" + request_summary + ":  " + request_details	+ ""+
				"<br> <b>Category:</b> " + request_category +
				"<br><b>Expected funding date:</b> " + funding_date +  "<br><b>Amount requested:</b> " + request_amount + " " + currency_symbol + ""+
				" (" + request_date + ")</p>" + 
				"<b>Amount already funded:</b> " +  already_donated + " " + currency_symbol + "" +
				"<div class='row post-actions-row' id='post-actions-" + donation_request_id + "'>" +				
				"<button id='comment-action_link-" + donation_request_id  + "-" + request_owner_entity_id  +"' class='link'>Comment</button> " +
				"<button id='recommend-action_link-" + donation_request_id  + "-" + request_owner_entity_id +"' class='link' title = '" + recommendation_title + "' data-toggle='tooltip' data-placement='bottom'>" + recommend_button_label + "</button> <span id = 'badge-" + donation_request_id +"-" + request_owner_entity_id + "' class='badge badge-important'>" + recommendation_count + "</span>" +
				"<button id='msg-action_link-" + donation_request_id  + "-" + request_owner_entity_id  + "'class='link'>Message post owner</button> " +
				"<button id='donate-action_link-" + donation_request_id  + "-" + request_owner_entity_id  + "'class='link'>Donate to this cause</button> " +
				"</div><br> </div>");
	$("#msg-action_link-" + donation_request_id  + "-" + request_owner_entity_id).addClass(visibility_class_name);
		
	//$("#recommend-action_link-" + donation_request_id  + "-" + request_owner_entity_id).addClass(isActive);
	$("#recommend-action_link-" + donation_request_id  + "-" + request_owner_entity_id).prop(isActive, true);
	$("#recommend-action_link-" + donation_request_id  + "-" + request_owner_entity_id).addClass(recommend_and_donate_visibility_class);
	
	$("#donate-action_link-" + donation_request_id  + "-" + request_owner_entity_id).addClass(recommend_and_donate_visibility_class); 
	$("#donate-action_link-" + donation_request_id  + "-" + request_owner_entity_id).addClass(visibility_class_name); 
	
	$('#donation-post-' + donation_request_id).append("<div id='new-comment-" + donation_request_id + "' class='new-comment'></div>"); /*new comment input textbox will be appened here*/
	
	//append a "complete" label after the post if recommend_and_donate_visibility_class is hidden
	
	if(recommend_and_donate_visibility_class == "hidden"){
		$('#donation-post-' + donation_request_id).append("<div id='is-complete-" + donation_request_id + "' class='alert-info'> This request is complete and has beed closed !</div>");
	}
}


function appentDonationComments(donation_request_id,comment_id,comment_owner_names,comment_date,comment_text,comment_owner_entity_id,comment_owner_profile_pic_path){
	$('#donation-post-' + donation_request_id).append("<div class='comments post-text' id='donation-comment-" + comment_id + "'>" +
								"<div class='col-xs-2 status-pic'> " +
								"<div class='status-pic'>" +
									"" + comment_owner_profile_pic_path + "" +
								"</div>" +
								"</div>" +
								"<div class='comment row' id='comment-text'> <p>" +
								"<a href='../user/?user=" + comment_owner_entity_id + "' > " + comment_owner_names + "  </a>" + 
									" " + comment_text + "(" + comment_date + " )</p>" +
								"</div>"+
							"</div>");
}

function fetchSpecificOffers(type_url_offer,offer_id){
	var tag = type_url_offer;
	var newDataRequest = $.ajax({
		type: "POST",
		url: "../noti/",
		timeout: 30000, // timeout after 30 seconds
		dataType: "json", //JSON
		data: {tag:tag,type:type_url_offer,oi: offer_id}
	});
	
	newDataRequest.done(function(data){
			processJSONFeedback(data);
	});
		
	newDataRequest.fail(function(jqXHR, exception){
		if (jqXHR.status === 0){
			$('#offer-post').html("<div class=''> Sorry, could not establishing a network connection. </div>");
		}else if (jqXHR.status == 404){
			$('#offer-post').html("<div class=''> Sorry, an error occured. </div>");
		}else if (jqXHR.status == 500){
			$('#offer-post').html("<div class=''> Sorry, an error occured. </div>");
		}else if (exception === 'parsererror'){
			$('#offer-post').html("<div class=''> Sorry, an error occured. </div>");
		}else if (exception === 'timeout'){
			$('#offer-post').html("<div class=''> Your request timed out. Please try again </div>");
		}else if (exception === 'abort'){
			$('#offer-post').html("<div class=''> Sorry, an error occured. </div>");
		}else{
			$('#offer-post').html("<div class=''> Sorry, an error occured. </div>");
		}
	});
}

function fetchDynamicOffers(type_url_offer,offer_id,t){
	var tag = type_url_offer;
	var newDataRequest = $.ajax({
		type: "POST",
		url: "../noti/",
		timeout: 30000, // timeout after 30 seconds
		dataType: "json", //JSON
		data: {tag:tag,type:type_url_offer,oi:offer_id,t:t}
	});
	
	newDataRequest.done(function(data){
		processJSONFeedback(data);
	});
		
	newDataRequest.fail(function(jqXHR, exception){
		if (jqXHR.status === 0){
			$('#offer-post').html("<div class=''> Sorry, could not establishing a network connection. </div>");
		}else if (jqXHR.status == 404){
			$('#offer-post').html("<div class=''> Sorry, an error occured. </div>");
		}else if (jqXHR.status == 500){
			$('#offer-post').html("<div class=''> Sorry, an error occured. </div>");
		}else if (exception === 'parsererror'){
			$('#offer-post').html("<div class=''> Sorry, an error occured. </div>");
		}else if (exception === 'timeout'){
			$('#offer-post').html("<div class=''> Your request timed out. Please try again </div>");
		}else if (exception === 'abort'){
			$('#offer-post').html("<div class=''> Sorry, an error occured. </div>");
		}else{
			$('#offer-post').html("<div class=''> Sorry, an error occured. </div>");
		}
	});
}


function processJSONFeedback(data){
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
				
				var is_offer_complete = arrO[i]['is_offer_complete'];
				var apply_visibility_class = arrO[i]['apply_visibility_class'];
				var accept_visibility_class = arrO[i]['accept_visibility_class'];
				appendMoneyOfferPost(offer_entity_id,offer_id,visibility_class_name,
						     names,offer_amount,offer_details,
							programme_name,offer_owner_profile_pic,offer_summary,
							offer_date,offer_currency,offer_type,is_offer_complete,
							apply_visibility_class,accept_visibility_class);
				//////////////APPEND MONEY OFERPOST (function call)
				
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
					
					
					//////////////APPEND MONEY OFFER applications (function call)
					appendMoneyPostApplication(offer_id,offer_type,offer_application_id,profile_pic_path,offer_application_entity_id,applicant_names,
					offer_application_details,offer_application_date,application_visibility_class_name,offer_entity_id,
				    accept_visibility_class);
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
				
				var is_offer_complete = arrO[i]['is_offer_complete'];
				var apply_visibility_class = arrO[i]['apply_visibility_class'];
				var accept_visibility_class = arrO[i]['accept_visibility_class'];
				//call function to append to view
				appendServiceOffer(s_offer_name, s_offer_country,
						s_offer_state, s_offer_residence,
						s_working_hours, s_starting_date,
						s_offer_details, time_date_string,
						s_offer_id, s_entity_id,
						s_profile_pic_path, s_names,
						s_offer_type,visibility_class_name,
						is_offer_complete,apply_visibility_class,
						accept_visibility_class);
				
				
				
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
					
					//appent application for service offer
					appendServiceOfferApplication(s_offer_id,s_offer_type,s_offer_application_id,
								      s_offer_application_profile_pic_path,offer_application_id,
									s_offer_application_entity_id,s_applicant_names,
									s_offer_application_details,s_offer_application_date,
									s_entity_id,s_visibility_class_name,accept_visibility_class);
					
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
				var is_offer_complete = arrO[i]['is_offer_complete'];
				var apply_visibility_class = arrO[i]['apply_visibility_class'];
				var accept_visibility_class = arrO[i]['accept_visibility_class'];
				
				appendMaterialOffer(m_offer_category,m_offer_name,
						m_offer_units,m_offer_country,
						m_offer_state,m_offer_residence,m_offer_details,
						m_profile_pic_path,m_offer_id,
						m_time_date_string,m_offer_main_photo,
						m_offer_other_photo_1,m_offer_other_photo_2,
						m_names,m_entity_id,
						m_offer_type,m_visibility_class_name,is_offer_complete,
						apply_visibility_class,accept_visibility_class);
				
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
					
					//append applications for material offers 
					appendMaterialOfferApplication(m_offer_id,m_offer_type,m_offer_application_id,
								       m_offer_application_profile_pic_path,m_offer_application_entity_id,
									m_applicant_names,m_offer_application_details,
									m_offer_application_date,m_entity_id,
									m_visibility_class_name,accept_visibility_class);
									
				}
			}else{
				$('#offer-post').append("We were unable to fulfill your request. Please try again"); 
			}
		}
	}else if(data["status"] == "NO"){
		if(data['redir']){
			window.location.reload();
		}else{
			$('#offer-post').html("<div class=''> " + data['message'] + " </div>");
		}
	}
}

function appendMoneyOfferPost(offer_entity_id,offer_id,visibility_class_name,names,offer_amount,offer_details,
			      programme_name,offer_owner_profile_pic,offer_summary,offer_date,offer_currency,offer_type,is_offer_complete,
							apply_visibility_class,accept_visibility_class){
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
						"<a href='#' class='offer-link " + visibility_class_name + " " + apply_visibility_class + "' id='offer-apply-link-" + offer_id + "-" + offer_entity_id + "-" + offer_type +  "' title='Apply to this offer'>Apply &nbsp;</a>"+
						"<a href='#' class='offer-link " + visibility_class_name + "' id='offer-message-link-"+offer_id+"-" + offer_entity_id + "-" + offer_type + "' title ='Send a message to this offer owner'> Message " + generateValueArray(names) + "</a>"+
					"</div>"+
					"<div>" +
					"<br>");
		
		$('#offer-post-' + offer_id + '-' + offer_type).append(""+
						"<div class'col-xs-12 new-application' id='new-application-" + offer_id + "-" + offer_type + "'>"+
							
						"</div>"+
					"");
								
}

function appendMoneyPostApplication(offer_id,offer_type,offer_application_id,profile_pic_path,offer_application_entity_id,applicant_names,
					offer_application_details,offer_application_date,application_visibility_class_name,offer_entity_id,
				    accept_visibility_class){
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
					"<a href='' class='" + accept_visibility_class + " accept-link" + application_visibility_class_name + "' id='application-accept-link-" + offer_id + "-" + offer_application_id + "-" + offer_entity_id + "-" + offer_application_entity_id + "-" + offer_type + "' title='Accept this application'>Accept application</a>"+
				"</div>"+
			"</div>"+
		"</div>");
	
}

function appendServiceOfferApplication(s_offer_id,s_offer_type,s_offer_application_id,s_offer_application_profile_pic_path,offer_application_id,
					s_offer_application_entity_id,s_applicant_names,s_offer_application_details,s_offer_application_date,
					s_entity_id,s_visibility_class_name,accept_visibility_class){
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
	$('#application-accept-link-' + s_offer_id + "-" + s_offer_application_id + "-" + s_entity_id + "-" + s_offer_application_entity_id + '-' + s_offer_type).addClass(accept_visibility_class);
}

function appendMaterialOfferApplication(m_offer_id,m_offer_type,m_offer_application_id,m_offer_application_profile_pic_path,
					m_offer_application_entity_id,m_applicant_names,m_offer_application_details,
					m_offer_application_date,m_entity_id,m_visibility_class_name,accept_visibility_class){
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
	$('#application-accept-link-' + m_offer_id + "-" + m_offer_application_id + "-" + m_entity_id + "-" + m_offer_application_entity_id + '-' + m_offer_type).addClass(accept_visibility_class);
}

function appendApproveButton(offer_id,offer_type){
	$('#new-application-' + offer_id + '-' + offer_type).html("<br>"+
		
		"<div id='approve-" + offer_id + "-" + offer_type +"' class=' col-xs-12 approve-area-btn'>" +
			"<button id='btn-approve-offer-" + offer_id + "-" + offer_type + "' class='btn btn-success btn-lg btn-block'> Approve this offer</button>" +
		"<div>"
	);
}

function markAsApproved(offer_id,offer_type){
	$('#new-application-' + offer_id + '-' + offer_type).html("<br>"+
		"<div id='approve-" + offer_id + "-" + offer_type +"' class='col-xs-12 approve-area-btn'>" +
			"<button id='btn-approve-offer-" + offer_id + "-" + offer_type + "' class='btn btn-success btn-lg btn-block' disabled> Approved and points earned !</button>" +
		"<div>"
	);
}

function notificationViewNotFound(){
	$('#comment-and-post').html('<div class="">No offer or request found</div>');
}

function getUrlVars(){
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for(var i = 0; i < hashes.length; i++){
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
}