$(document).ready(function() {
  //commom global variables which include harambesa balance, social points, profile pic path, full names of logged in user, user entity_id
//      .........................................................start email invitation 
	//setTimeout(executeQuery, 5000);
	//postComment();
	var profile_pic_path_Global="";
	var user_full_names_Global = "";
	var user_entity_id_Global = "";    
    
     $.post("../mapping/mapping.jsp",{tag:"global_dresser"}, function(data){      
		profile_pic_path_Global = data['profile_pic_path'];
		user_full_names_Global = data['full_names'];
		user_entity_id_Global = data['entity_id'];
		if(profile_pic_path_Global == null){			  
			
			$('#profile-pic-section').html("<span class='fa fa-user fa-5x breadcrumb-state-greyed'></span>");
			profile_pic_path_Global = "<span class='fa fa-user fa-3x breadcrumb-state-greyed'></span>";
		}else{
			$('#profile-pic-section').html("<a href='#'><img src='" + "../profilepic/" + profile_pic_path_Global + "'></a>"); 
			profile_pic_path_Global = "../profilepic/" + data['profile_pic_path'];
			profile_pic_path_Global = "<img alt='Profile Picture' src='" + profile_pic_path_Global + "'>";
		}
     },'json');  
     
	
     //postComment(user_entity_id_Global,profile_pic_path_Global,user_full_names_Global);
//      .........................................................end email invitation
  
      //retrieve donation requests and its comments
    //===============================================start donation request post retrieval====================================

    $.post("../mapping/mapping.jsp", {tag:"request_posts"}, function(data){
		
	if(data['status']=='OK'){
	  
	   // $('#comment-and-post').append(data['requests']);
	  var arr = data['requests'];
	 // var cArr = [];
	  // $('#comment-and-post').append(arr.length);
	  
	  for(var i = 0;i<arr.length;i++){
	
	       //$('#comment-and-post').append(arr[i]["donation_request_summary"] + "<br>");
	       
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
	      var already_donated = arr[i]['already_donated'];
	      
	      var recommendation_title = "";
	      
	      if(isActive=='disabled'){
		
		      recommendation_title = "You cannot recommend yourself";
	      }else{
		
		     recommendation_title = recommend_button_label ;
		
	      }
	      
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
	      
	       $("#donate-action_link-" + donation_request_id  + "-" + request_owner_entity_id).addClass(visibility_class_name); 
	     
	       var cArr = arr[i]["comments"];
	      
	      var comment_size= cArr.length;
	      $('#donation-post-' + donation_request_id).append("<div id='new-comment-" + donation_request_id + "' class='new-comment'></div>"); /*new comment input textbox will be appened here*/
	     
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
		  $('#donation-post-' + donation_request_id).append("<div class='comments post-text' id='donation-comment-" + comment_id + "'>" +
									"<div class='col-xs-2 status-pic'> " +
									    "<div class='status-pic'>" +
										"" + comment_owner_profile_pic_path + "" +
									    "</div>" +
									"</div>" +
									"<div class='comment row' id='comment-text'> <p>" +
									    "<a href='../user/?user=" + comment_owner_entity_id + "' > " + comment_owner_names + "  </a>" + comment_text + "(" + comment_date + " )</p>" +
									"</div>"+
								    "</div>");
	      }

	    }
  
	}else{
	  
		$('#comment-and-post').append(data['message']);
		$('#post-status').html('FALSE');
	    
	}
	
    },'json');

	$('#comment-and-post').on('mouseover', '.link', function (e) {
		var elementId = e.target.id;
		var strResult = elementId.split('-');
		var type = strResult[0];
		var postId = strResult[2];
		var owner_entity_id= strResult[3];
		if(type == 'recommend'){
			$.post("../mapping/mapping.jsp", {tag:"fetch_recommenders",donation_request_id:postId,recommendation_owner:owner_entity_id}, function(data){ 
			
				if(data['success']==1){
					if(data['ok'] == 1){
						//Fetch names
						//alert(data['ok']);
						var rArr = data['recommendations'];
						//recommend-action_link-" + donation_request_id  + "-" + request_owner_entity_id
						var recommend_string = "";
						var recommend_count = rArr.length;
						var start_string = "";
						var count = 0;
						for(var i = 0; i < rArr.length ; i++){
							if(rArr[i]['id'] == owner_entity_id){
								count = count + 1;
							}
						}
						
						if(count > 0){
							start_string = "You";
						}
						
						for(var i = 0; i < rArr.length ; i++){
							recommend_string = recommend_string + " " + rArr[i]['name'] + ",";
						}
						var overal_tip = "";
						if(start_string == "You"){
							overal_tip = start_string + "," +  recommend_string + " recommends this"; 
						}else{
							overal_tip = recommend_string + " recommends this"; 
						}
						$("#recommend-action_link-" + postId  + "-" + owner_entity_id).attr("title", overal_tip);
						
						$("#recommend-action_link-" + postId  + "-" + owner_entity_id).tipsy();						
					}else{
						//No recommenders available
					}
				}else{	
					if(data['redir'] != null){
						window.location.replace(data['redir']);
					}else{
						
					}
				}
			
			},'json');
		}
	});
    
    $('#comment-and-post').on('click', '.link', function (e) {
	  var elementId = e.target.id;
	    /*examine the ID to check its type (Recommend,Comment,Donate or msg) and fetch its asscociated comment ID*/
	  //alert(elementId);
	 
	  var strResult = elementId.split('-');
	  var type = strResult[0];
	  var postId = strResult[2];
	  var owner_entity_id= strResult[3];
	  //var names = strResult[4];	  
	  //alert(owner_entity_id);////used for esting
	  //alert('type is ' + type + " and post ID " + postId);
	  if(type == 'comment'){ 
	      //add new textbox (via html) to the new-comment div with id postID
	    $('#new-comment-' + postId).html("<div class='row' id = 'comment_input_text-'"  + postId +  "> " +
						  "<div class='col-xs-2 status-pic' id='pic'>"+
						      "<div class=''>" +
							  ""+profile_pic_path_Global + "" +
						      "</div> " +
						    "</div> " +
						    "<div class='col-xs-10' id='comment-text-'"  + postId + ">" +
							  "<textarea class='comment-text-area' rows='1' cols='59' name='details' id='comment-details-" + postId + "' placeholder='Type your comment here...'></textarea>" +
						     "</div>" +						    
					      "</div>" +
					       "<div class='row comment-cancel-post-btn' align='right' id='post-cancel-btns'" + postId + ">" +
							"<button class='btn-default comment-post-cancel-action' id='comment-cancel-btn-" + postId + "'>Cancel</button>" + 
							"<button id='comment-post-btn-" + postId + "' class='btn-primary comment-post-cancel-action'>Post</button>" + 
						 "</div>");
	  }else if(type=='recommend'){
	    var recommendation_label = $("#recommend-action_link-" + postId + "-" + owner_entity_id).text();
	      if(recommendation_label == "Recommend"){
		  //do a post to recommend
		//put a loading stuff
		//alert(recommendation_label);
		$("#recommend-action_link-" + postId + "-" + owner_entity_id).html("<img  src='../res/images/loading.gif' /> Recommending...");
		
		$.post("../mapping/mapping.jsp", {tag:"recommend",donation_request_id:postId,recommendation_owner:owner_entity_id}, function(data){ 
		  
		    if(data['success']==1){
			//recommendation has been post, get the current count and increase it by one
			//look for the specific recommend that produced the action then update the count, the append it later
			var current_recommendation_count = $("#badge-" + postId +"-" + owner_entity_id).text();
			$("#badge-" + postId +"-" + owner_entity_id).html(parseInt(current_recommendation_count)+1);
			
			$("#recommend-action_link-" + postId + "-" + owner_entity_id).html("Unrecommend");
			$("#recommend-action_link-" + postId + "-" + owner_entity_id).attr("title", "Unrecommend");
			  
		      }else{
				$("#recommend-action_link-" + postId + "-" + owner_entity_id).html("Recommend");
				
				$('#empty-request-comment-action-body').html("");
				$('#empty-request-comment-action-body').html(data['message']);
				$('#empty-donation-request-comment').modal('show');
				
			
		      }
		  
		},'json');
		
	     }else{
		$("#recommend-action_link-" + postId + "-" + owner_entity_id).html("<img  src='../res/images/loading.gif' /> Unrecommending...");
	       //do a post to unrecomend i.e delete a recommendation post where postId is pos and entity_id is 
	       
		  $.post("../mapping/mapping.jsp", {tag:"unrecommend",donation_request_id:postId,recommendation_owner:user_entity_id_Global}, function(data){ 

		      if(data['success']==1){
			  //recommendation has been post, get the current count and increase it by one
			  //look for the specific recommend that produced the action then update the count, the append it later
			  var current_recommendation_count = $("#badge-" + postId +"-" + owner_entity_id).text();
			  $("#badge-" + postId +"-" + owner_entity_id).html(parseInt(current_recommendation_count)-1);

			  $("#recommend-action_link-" + postId + "-" + owner_entity_id).html("Recommend");
			  $("#recommend-action_link-" + postId + "-" + owner_entity_id).attr("title","Recommend");

		      }else{
				$("#recommend-action_link-" + postId + "-" + owner_entity_id).html("unrecomend");
				$('#empty-request-comment-action-body').html("");
				$('#empty-request-comment-action-body').html(data['message']);
				$('#empty-donation-request-comment').modal('show');

		      }

		  },'json');
	       

	      }
   
	  }else if(type=='msg'){    
		$.post("../mapping/mapping.jsp", {tag:"isConnected",sender:user_entity_id_Global,receiver:owner_entity_id}, function(data){
		      var names = $('#name_link-'+ postId).text();
		      
		      if(data['success'] == 1){
			
			//the two are connected and a message can be sent
				$('#send-message-footer-home').html("<button type='button' class='btn btn-default' data-dismiss='modal'>Cancel</button>" +
				"<button id='send-message-" + user_entity_id_Global + "-" + owner_entity_id + "' type='submit' class='btn btn-primary'>Send</button>");
				
				$('#message-modal').modal('show');
				$('#message-error').html("");
				$('#message-error-success').html("");
				
				$('#new-message-display-area-home').removeClass('hidden');
				
				$('#send-message-textbox-home').val(names);
			
				$('#dialog-id').html(user_entity_id_Global + "-" + owner_entity_id);
			
		      }else if(data['success'] == 2){
				alert("error");
		      }else{
			  //send a connection request
			    
			    $('#recommendation-action-connect').modal('show');
			    $('#connect-action-body').append("<p>You are not connected with post owner. Please send a connection request to <a href='#'>" + names + "</a></p>");
			    $('#connect-modal-footer').append("<button id='connect-" + user_entity_id_Global + "-" + owner_entity_id + "' type='button' class='connect-button btn btn-primary'> Send request</button>");

		      }
		  
		},'json');
	    
	    
	  }else if(type=='donate'){
	    
	     // alert("working donate");
	     // alert(e.target.id);
	      //donor === user_entity_id_Global
	      //donee is post_owner === owner_entity_id
	      var name = $('#name_link-'+ postId).text();// donnee or post owner id
	      //user_full_names_Global donors full names
	      $('#beneficiary-name').html(name);
	      $('#donate-modal').modal('show');
	      
	      $('#donate-footer').html("");
	      $('dustbin').html("");
	      $('#requested-amount').html("");
	      $('#request-id').html(postId);
	      $('#donation-source-type').html("");
	      
	      $('#new-donation-display-area').removeClass('hidden');
	      $('#breadcrumb-status').removeClass('hidden');
	      
	      $('#pay-by-harambesa-option').addClass('hidden');
	      $('#final-feedback-message').html("");
	      
	      $('#donor-donee').html(user_entity_id_Global + "-" + owner_entity_id);
	      $('#donate-footer').html("<button type='button' class='btn btn-default' data-dismiss='modal'>Cancel</button>" +
				      "<button id='donate-submit-button' type='submit' class='btn btn-primary'>Next <span class='glyphicon glyphicon-arrow-right'></span></button>");
	      $('#info-area').html("");
	    
	      requestedAmount(owner_entity_id,name);
	  }
    });
    
    $('#recommendation-action-connect').on('click','.connect-button', function(event){
	  
	  var eventSourceId = event.target.id;
	  var eventSourceElementsArray = eventSourceId.split('-');
	  var sender = eventSourceElementsArray[1];//logged in user; the message sender
	  var receiver = eventSourceElementsArray[2];//the owner of the message
	  
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
	      
	     // alert("boot reset working");
	      
    }); 
    
	$('#comment-and-post').on('click','.comment-post-cancel-action',function(e){
		//comment-cancel-btn'" + postId
		//13
		var elementId = e.target.id;
		var resultArray = elementId.split('-');
		e.preventDefault();
		var postId = resultArray[3];//get text from the text-area
		var action_type= resultArray[1];
		//alert(user_entity_id_Global);
		if(action_type == "post"){
			
			var comment_string = $('#comment-details-'+postId).val().trim();//alert(postId + "  " + comment_string);

			if(comment_string != ""){
				$.post("../mapping/mapping.jsp", {tag:"post_new_comment",donation_request_id:postId,entity_id:user_entity_id_Global,comment_text:comment_string}, function(data){ 
				// $('#new-comment-' + postId).html('');
				
				$('#new-comment-' + postId).html("<img  src='../res/images/loading.gif' /> Posting...");
				
				if(data['success']==1){
					//append this text to others after storing it to database
				
				//alert("working");
					$('#donation-post-' + postId).append("<div class='comments post-text' id='donation-comment-'>" +
									"<div class='col-xs-2 status-pic'>" +
										"<div class='status-pic'>" +
										""+ profile_pic_path_Global + "" + 
										"</div>" +
									"</div>" +
									"<div class='comment row' id='comment-text'>" +
										"<p><a href='' >" + user_full_names_Global + "</a>" + " " + comment_string + "("+data['coment_time']+")</p>" +
									"</div>" +
									"</div>");
					//After posting remove the contents of the new-comment element
					
					$('#new-comment-' + postId).html('');
					
				
				}else{
					//do not remove the loading thing
					$('#new-comment-' + postId).html('');
					$('#empty-request-comment-action-body').html("");
					$('#empty-request-comment-action-body').html(data['message']);
					$('#empty-donation-request-comment').modal('show');
				
				}
				
				},'json');
			}else{
				////post seems to be empty
				$('#empty-request-comment-action-body').html("");
				$('#empty-request-comment-action-body').html("This comment appears to be blank. Please write something to comment to this post");
				$('#empty-donation-request-comment').modal('show');
			}
		}else{
			$('#new-comment-' + postId).html('');
		}
	});

	    $('.comment-text-area').each(function(){
		var pad = parseInt($(this).css('padding-top'));

		$(this).height(1);
		var contentHeight = this.scrollHeight;
		if (!$.browser.mozilla) 
		    contentHeight -= pad * 2;
		if (contentHeight > $(this).height()) 
		    $(this).height(contentHeight);
	    });
   //==================================end of donation request posts=============================
  
  
   // $('#button_comment').click()
  
  //$("#cities").prop('disabled', true);
    
  
   // $('#loading_country').removeClass('hidden');
    $('#loading_category').removeClass('hidden');
    $('#loading_currency').removeClass('hidden');

    $('#loading_currency_donate').removeClass('hidden');    

    
   
     //===============================start Donate validation=================================
	      $('#donate-form').bootstrapValidator({
		message: 'This value is not valid',
		
		submitHandler: function(validator, form, submitButton) {
				
			var currency_of_donation=$("#donation_currency_donate").val();
			var amount_donated=$("#donation_amount_donate").val();
			var donor_donee = $('#donor-donee').text();
			var idArray = donor_donee.split('-');				
			
			var donor = idArray[0];
			var donee = idArray[1]; 
			
			$('#dustbin').html(donor + "-" + donee + "-" + currency_of_donation + "-" + amount_donated);			
			
			$('#status-1').removeClass('breadcrumb-state');
			$('#status-1').addClass('breadcrumb-state-greyed');

			$('#status-2').addClass('breadcrumb-state');
			$('#status-2').removeClass('breadcrumb-state-greyed');
			
			$('#new-donation-display-area').addClass('hidden');
			
			$('#donation-options').removeClass('hidden');
			
			$('#donate-footer').prepend("<button id='donation-back' type='button' class='back-for-options btn btn-danger'><span class='glyphicon glyphicon-arrow-left'></span>Back</button>");
			
			$('#donate-submit-button').addClass('hidden');		
			
			$('#donate-footer').append("<button id='donation-options-next' type='button' class='next-for-options btn btn-primary'><span class='glyphicon glyphicon-arrow-right'></span>Next</button>");
			
		},
		feedbackIcons: {
		    valid: 'fa fa-check',
		    invalid: 'fa fa-times',
		    validating: 'fa fa-refresh'
		},
		fields: {
			donation_currency_donate: {
			    validators: {
				notEmpty: {
				    message: 'Please select currency'
				}
			    }
			},            
			donation_amount_donate: {
			    validators: {
				    notEmpty: {
					    message: 'Please enter request amount'
				    },
				    numeric: {
					    message: 'Amount can only be made of numbers'
				    }
			    }
		      
			},
			
			confirm_terms_conditions:{
			
				 validators: {
					notEmpty: {
						message: 'Please confirm that you want to donate'
					}
			    }
		      }
		  }
		
	      });
    
//     =============================end donation validation======================================
    
    
    
    //.............send Message form validation...............................................
    
    //..........................................donation-options-next button click action.....start
	  
	 $('#aggregator-option').click(function(){
	   
		clearAlert();	
		
	});
	 
	 $('#harambesa-option').click(function(){
	   
		clearAlert();
		//wallet 
		$('#aggregator-option-area').addClass('hidden');
	});
	 
	 $('#donate-footer').on('click','.next-for-options',function(){
		
		if($('#harambesa-option').is(':checked')){ 
			$('#donation-source-type').html("wallet");
			$('#donation-options').addClass('hidden');
			$('#status-2').addClass('breadcrumb-state-greyed');
			$('#status-2').removeClass('breadcrumb-state');
			$('#status-3').addClass('breadcrumb-state');
			$('#status-3').removeClass('breadcrumb-state-greyed');
			
			$('#donation-back').addClass('back-for-pay');
			$('#donation-back').removeClass('back-for-options');
			
			$('#donation-options-next').text("Complete donation");
			$('#donation-options-next').addClass('payout');

			$('#donation-options-next').removeClass('next-for-options');
			
			//0-donor;donee-1;currency_of_donation-2;amount_donated-3
			$('#pay-by-harambesa-option').html("<div id='success-info-harambesa-option' class='alert alert-info'>" +
				"You are about to complete your donation using your harambesa account<br>" +
				"Beneficiary : " + $('#beneficiary-name').text() +
				"<br>Amount : " + generateValueArray()[3] + " " + next(arrCur,generateValueArray()[2]) +
			"</div>");
			
			$('#pay-by-harambesa-option').removeClass('hidden'); 
			
			//add this class to defferentiate
			
			$('#donation-options-next').addClass('complete-donation-harambesa-option');
			
			
			
			
			
		}else if($('#aggregator-option').is(':checked')){
			
			$('#donation-source-type').html("aggregator");
			
			//$('#aggregator-option-area').removeClass('hidden');
			window.location.replace("../deposits/");
			
		}else{
		  
			$('#radio-buttons-error').html("<div class='alert alert-danger'>You have not selected any option</div>");
		}
	   
	   
	});
    //..........................................donation-options-next button click action.....end
	      
	      
	 $('#donate-footer').on('click','.complete-donation-harambesa-option',function(){
	   
		//alert("working");
		$('#donation-options-next').addClass('hidden');
		
		$('#final-feedback-message').removeClass('hidden');
		
		$('#final-feedback-message').Loadingdotdotdot({
			"speed": 400,
			"maxDots": 4,
			"word": "Finalising donation"
		});
		
		//0-donor;donee-1;currency_of_donation-2;amount_donated-3
		var source_type = $('#donation-source-type').text();
		var amount_requested = $('#requested-amount').text();
		var request_id = $('#request-id').text();
		
		$.post("../mapping/mapping.jsp", {tag:"finalise_donation_by_account",donor:generateValueArray()[0],donee:generateValueArray()[1],
						  currency:generateValueArray()[2],amount_donated:generateValueArray()[3],source_type:source_type,
						  amount_requested:amount_requested,request_id:request_id}, function(data){
			if(data['account']==1){
				//account has enough funds
				if(data['success'] == 1){
					//donation successiful, so remove the loading thing
				  
					//check if donation is Complete; if it is true, we are done, otherwise, we repost a new post, and remove the mother post
					$('#final-feedback-message').html("<div class='alert alert-success'>Thank you for donating !</div>");
					
					$('#pay-by-harambesa-option').addClass('hidden');
					$('#donate-footer').html("<button id='donation-ok' type='button' class='final-donation-ok btn btn-default'>OK</button>");
			  
				}else{
				  
					//an error occured
					$('#final-feedback-message').html("<div id='feedback-inner-alert-section' class='alert alert-danger'>" + data['message'] + "</div>");
					$('#pay-by-harambesa-option').addClass('hidden');
				  
				}	  
			  
			}else{
				//account has no enough funds
				$('#final-feedback-message').html("<div id='feedback-inner-alert-section' class='alert alert-danger'>You do not have enough funds for donation. Please load your account or go back to select another option</div>");
				$('#pay-by-harambesa-option').addClass('hidden'); 
				
				$('#donate-footer').append("<button id='load-account-on-donate' type='button' class='loading-account-from-donate btn btn-primary'>Load account</button>");
			}    
		},'json'); 
	  });
	 
	  $('#donate-footer').on('click','.final-donation-ok',function(){
		  $('#donate-modal').modal('hide');
		  window.location.reload();
		  $('#donation_amount_donate').val("");
	  });
    
	  $('#donate-footer').on('click','.back-for-options',function(){
		  $('#status-1').addClass('breadcrumb-state');
		  $('#status-1').removeClass('breadcrumb-state-greyed');
		  $('#status-2').removeClass('breadcrumb-state');
		  $('#status-2').addClass('breadcrumb-state-greyed');
		  $('#new-donation-display-area').removeClass('hidden');
		  $('#donation-options').addClass('hidden');
		  $('#donate-submit-button').removeClass('hidden');
		  $('#donation-back').remove();
		  $('#donation-options-next').remove();
		  //activate the submit button
		  $('#donation_amount_donate').val("");
		  
		  $('#aggregator-option-area').addClass('hidden');
		  
		  
	  });
	  
	  $('#donate-footer').on('click','.back-for-pay',function(){
		  $('#donation-options').removeClass('hidden');

		  $('#status-2').removeClass('breadcrumb-state-greyed');
		  $('#status-2').addClass('breadcrumb-state');
		  $('#status-3').removeClass('breadcrumb-state');
		  $('#status-3'). addClass('breadcrumb-state-greyed');
		  $('#donation-back').removeClass ('back-for-pay');
		  $('#donation-back').addClass('back-for-options');
		  $('#donation-options-next').text("Next");
		  $('#donation-options-next').removeClass('payout');
		  $('#donation-options-next').addClass ('next-for-options');
		  $('#donation-options-next').removeClass('hidden');
		  $('#pay-by-harambesa-option').html("");
		  $('#pay-by-harambesa-option').addClass('hidden'); 
		  $('#donation-options-next').removeClass('complete-donation-harambesa-option');
		  
		  $('#load-account-on-donate').remove();
		  
		  $('#final-feedback-message').html("");
	  });
	  
	  $('#donate-footer').on('click','.loading-account-from-donate',function(){
	    
		  alert("Waiting for aggregator");
	    
	  });
	  
	  
	  $('#donate-modal').on('hidden.bs.modal', function () {
		  window.location.reload(true);
	  });
	  
	   $('#invite-friends').on('hidden.bs.modal', function () {
		  $('#typed_mail_response_message').html("");
		//  $('#email').val("");
		  
	  });
	  
	  
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
		 
				$.post('../mapping/mapping.jsp',{tag:"save",to:rec,msg:message,from:user_entity_id_Global}, function(result) {
					
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
    
    
	$('#message-modal').on('hidden.bs.modal', function () {
		$('#message-form-home').data('bootstrapValidator').resetForm();
	});
    
    //.............end of Message send validation........................................
    
    
    
	$('#donation-request-button').click(function(e){
		//alert(user_entity_id_Global);
		$('#donation-request-holder').removeClass("hidden");
	});

	
	$('#cancel-donation-request').click(function(e){
		$('#donation-request-holder').addClass("hidden");
		cleanRequestForm("donation-request-form"); 
	});
	
	
    
    //................................request post processing..................................
    
     $('#donation-request-form').bootstrapValidator({
	  message: 'This value is not valid',
        
	  submitHandler: function(validator, form, submitButton) {
 			var category=$("#donation_category").val();
 			var f_date= $("#f_date").val();
			    f_date = changeDateFormat(f_date);
 			var currency=$("#donation_currency").val();
 			var amount=$("#donation_amount").val();
 			var summary=$("#summary").val();
 			var details=$("#details").val();
			
           $.post('../mapping/mapping.jsp',
				{
					tag:"post_request", category: category, f_date:f_date,currency: currency,
					amount:amount,summary:summary, details:details
					  
				}, function(result) {
				//alert(result);
				
				  if(result['success'] == '0'){
					if(result['redir'] != null){
						window.location.reload();
					}else{
						$('#post_result').html(result['message']);
						$('#post_result').removeClass('hidden');
					}					
				  }else if(result['success'] == '1'){
					$('#donation-request-holder').addClass("hidden");
					var full_name = result['full_name'];
					//var profile_pic_path = "../profilepic/" + result['profile_pic_path'];
					var profile_pic_path = "";
					if(result['profile_pic_path'] == null){
						profile_pic_path = "<span class='fa fa-user fa-5x breadcrumb-state-greyed'></span>";
					}else{
						profile_pic_path = "../profilepic/" +result['profile_pic_path'];
						profile_pic_path = "<img alt='Profile Picture' src='" + profile_pic_path + "'>";
					}
					var time_of_post = result['post_date'];
					var entity_id = result['entity_id'];
					var returned_donation_request_id = result['record_id'];
					
					if($('#post-status').text()=='FALSE'){
					  
					    $('#post-status').html('TRUE');
					    $('#post-status').addClass('hidden');
					    
					    $('#comment-and-post').html('');
					    
					    
					}
					
					    $('#comment-and-post').prepend("<div id='donation-post-" + returned_donation_request_id + "' class='post'>" +
									    "<div id = 'post-pic-row' class='col-xs-2 post-pic'> <div id = 'post-pic' class='post-pic'>" +
									    "" + profile_pic_path + "" +
									    "</div></div> <div='appeal-post' id='post-text'>" +
									    "<p><a href='../user/?user=" + entity_id + "'>" + full_name + " " + "</a>" + summary + ":  " + details	+ ""+
									    "<br> <b>Category:</b> " + next(arrCat,category) +
									    "<br><b>Expected funding date:</b> " + f_date +  "<br>" +									     
									    "<b>Amount requested:</b> " + amount + " " + next(arrCur,currency) + "<br>"+
									    "<b>Amount already funded:</b> 0 " + next(arrCur,currency) + "" + 
									    " (requested on " + time_of_post + ")</p>" +
									    "<div class='row post-actions-row' id='post-actions-" + returned_donation_request_id + "'>" +				
									    "<button id='comment-action_link-" + returned_donation_request_id  + "-" + entity_id  + "' class='link' title='Leave a comment'>Comment</button> " +
									    "<button id='recommend-action_link-" + returned_donation_request_id  + "-" + entity_id  + "' class='link' disabled title='You cannot recommend yourself'>Recommend</button> <span id = 'badge-" + returned_donation_request_id +"-" + entity_id + "' class='badge badge-important'>0</span>" +
									    "<button id='msg-action_link-" + returned_donation_request_id  + "-" + entity_id  + "' class='link hidden'>Message post owner</button> " +
									    "<button id='donate-action_link-" + returned_donation_request_id  + "-" + entity_id  + "' class='link hidden'>Donate to this cause</button> " +
									    "</div><br> </div>");
					    
					    //append a new-comment element as well
					     $('#donation-post-' + returned_donation_request_id).append("<div id='new-comment-" + returned_donation_request_id + "' class='new-comment'></div>"); /*new comment input textbox will be appened here*/

				  }else{
				    
				    
				  }
				
            }, 'JSON');
        },
        feedbackIcons: {
            valid: 'fa fa-check',
            invalid: 'fa fa-times',
            validating: 'fa fa-refresh'
        },
        fields: {
            donation_category: {
                validators: {
                    notEmpty: {
                        message: 'Please select country'
                    }
                }
            },            
            f_date: {
                validators: {
                    notEmpty: {
                        message: 'Date is required'
                    },
                    date:{
			 format: "DD/MM/YYYY",
			 message: 'Date is invalid'
                    }
                }
            },
            donation_currency: {
                validators: {
                    notEmpty: {
                        message: 'Please select currency'
                    }
                }
            },
            donation_amount: {
                validators: {
                    numeric: {
                        message: 'Put a valid amount'
                    },
		    notEmpty:{
		      message:'Amount required'
		    }
                }
            },
            summary: {
                validators: {
                    notEmpty: {
                        message: 'Enter your request summary'
                    },
		    stringLength: {
                        min: 5,
                        max: 30,
                        message: 'Your summary cannot be more than 30 characters'
                    },
                }
            },
            details: {
                validators: {
                    notEmpty: {
                        message: 'Please enter your request details'
                    }
                   
                }
            },
           
        }
    });
    
    //.........................end of request post procesing...................................
    
    
    
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
			arrCur.push({key:data[i]["id"],value:data[i]["name"]});
		}
			      
			$("#donation_currency").html(list);
			$("#donation_currency_donate").html(list);
			$('#loading_currency').addClass('hidden');
			$('#loading_currency_donate').addClass('hidden');
	},'json');
    
    //.........................currency end...................................

	$('#f_date').datepicker({
		format: "dd/mm/yyyy",
		startDate: '1d',
		autoclose:true
	});
  
});


//================functions
	

var next = function(db, key) {
  for (var i = 0; i < db.length; i++) {
    if (db[i].key === key) {
      return db[i] && db[i].value;
    }
  }
};

function requestedAmount(id,name){
	
	var amount="";
	var symbol="";
	
	$.post("../mapping/mapping.jsp", {tag:"requestedamount",id:id}, function(data){
	
		  if(data['success'] == 1){			  
			  $('#info-area').html("<div class='alert alert-info alert-dismissable'> <button type='button' class='close' data-dismiss='alert' aria-hidden='true'>&times;</button>" +
			  "Request amount : " +data['amount'] + " " + data['currency_symbol'] + " <br> Beneficiary : " + name + "</div>");			  
			  $('#requested-amount').html(data['amount']);			    
		  }	      
	  },'json');
}

function clearAlert(){
	$('#radio-buttons-error').html(""); 
}

function generateValueArray(){
	var user_values = $('#dustbin').text();
	var user_values_array = user_values.split('-');
	return user_values_array;
}

function changeDateFormat(_date){
	//format: "dd/mm/yyyy"
	
	var new_date_array = _date.split('/');
	var year = new_date_array[2];
	var month = new_date_array[1];
	var day = new_date_array[0];
	var new_format = year+ "/" +month + "/" + day;
	
	return new_format;
}

function cleanRequestForm(formId){
	    $('#' + formId).get(0).reset();
	    $('#' + formId).data('bootstrapValidator').resetForm();
	
}

function executeQuery() {
	$.ajax({
		url: '../mapping/mapping.jsp?tag=update_home_time',
		success: function(data) {
			
		}
	});
	setTimeout(executeQuery, 5000); // you could choose not to continue on failure
}

function postComment(user_entity_id_Global,profile_pic_path_Global,user_full_names_Global){

}