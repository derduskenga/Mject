$(document).ready(function() {
	
  //commom global variables which include harambesa balance, social points, profile pic path, full names of logged in user, user entity_id
    var harambesa_bal_Global = "";
    var harambesa_social_points_Global = "";
   
    var profile_pic_path_Global="";
    var user_full_names_Global = "";
    var user_entity_id_Global = "";
    
    
    
     $.post("../mapping/mapping.jsp", {tag:"global_dresser"}, function(data){
      
	harambesa_bal_Global = data['harambesa_balance'];

	harambesa_social_points_Global = data['harambesa_points'];
	
	profile_pic_path_Global = data['profile_pic_path'];
	
	user_full_names_Global = data['full_names'];
	
	user_entity_id_Global = data['entity_id'];
	
	$('#balance').html("Harambesa Balance: " + harambesa_bal_Global);
	$('#points').html("Harambesa Points:" + harambesa_social_points_Global);
	
     },'json');
  
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
	      var request_date = arr[i]["donation_request_date"];
	      var request_category = arr[i]["programme_name"];
	      var request_owner_full_name = arr[i]["post_owner_name"];
	      var request_owner_entity_id = arr[i]["post_owner_entity_id"];
	      var request_owner_pic_path = arr[i]["post_owner_pic_path"];
	      var donation_request_id = arr[i]["donation_request_id"];
	      var funding_date = arr[i]["expected_funding_date"];
	      var visibility_class_name = arr[i]["visibility_class_name"];
	      var isActive = arr[i]["isActive"];
	      var recommend_button_label = arr[i]['button_label'];
	      var recommendation_count = arr[i]['recommendation_count'];
	      
	      
	      //assign the array of comments into a variable
	      $('#comment-and-post').prepend("<div id='donation-post-" + donation_request_id + "' class='post'>" +
				"<div id = 'post-pic-row' class='col-xs-2 post-pic'> <div id = 'post-pic' class='post-pic'>" +
				"<img alt='Profile Picture' src='" + request_owner_pic_path + "'>" +
				"</div></div> <div='appeal-post' id='post-text'>" +
				"<p><a href='#' id='name_link-" +donation_request_id + "'>" + request_owner_full_name + " " + "</a>" + request_summary + ":  " + request_details	+ ""+
				"<br> <b>Category:</b> " + request_category +
				"<br><b>Expected funding date:</b> " + funding_date +  "<br><b>Amount requested:</b> " + request_amount + " " + currency_symbol + ""+
				"(requested on " + request_date + ")</p>" +
				"<div class='row post-actions-row' id='post-actions-" + donation_request_id + "'>" +				
				"<button id='comment-action_link-" + donation_request_id  + "-" + request_owner_entity_id  +"'class='link'>Comment</button> " +
				"<button id='recommend-action_link-" + donation_request_id  + "-" + request_owner_entity_id +"'class='link'>" + recommend_button_label + "</button> <span id = 'badge-" + donation_request_id +"-" + request_owner_entity_id + "' class='badge badge-important'>" + recommendation_count + "</span>" +
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
		  var comment_owner_profile_pic_path = cArr[j]['comment_owner_profile_pic_path'];
		  
		  $('#donation-post-' + donation_request_id).append("<div class='comments post-text' id='donation-comment-" + comment_id + "'>" +
									"<div class='col-xs-2 status-pic'> " +
									    "<div class='status-pic'>" +
										"<img alt='Profile Picture' src='" + comment_owner_profile_pic_path + "'>" +
									    "</div>" +
									"</div>" +
									"<div class='comment row' id='comment-text'> <p>" +
									    "<a href='' > " + comment_owner_names + "  </a>" + comment_text + "(On " + comment_date + " )</p>" +
									"</div>"+
								    "</div>");
	      }

	    }
  
	}else{
	  
	  $('#comment-and-post').append(data['message'])
	  $('#post-status').html('FALSE');
	    
	}
	
    },'json');

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
	    $('#new-comment-' + postId).html("<div id = 'comment_input_text-'"  + postId +  "> " +
						  "<div class='col-xs-2 status-pic' id='pic'>"+
						      "<div class='status-pic'>" +
							  "<img alt='Profile ure' src='" + profile_pic_path_Global + "'>" +
						      "</div> " +
						    "</div> " +
						    "<div class='row' id='comment-text-'"  + postId + ">" +
							  "<textarea class='comment-text-area' rows='2' cols='55' name='details' id='comment-details-" + postId + "' placeholder='Type your comment here and enter to post...'></textarea>" +
						     "</div>" +
					      "</div>");
	  }else if(type=='recommend'){
	    var recommendation_label = $("#recommend-action_link-" + postId + "-" + owner_entity_id).text();
	      
	      if(recommendation_label == "Recommend"){
		  //do a post to recommend
		//put a loading stuff
		//alert(recommendation_label);
		$("#recommend-action_link-" + postId + "-" + owner_entity_id).html("<img  src='../res/images/loading.gif' /> Recommending...");
		
		$.post("../mapping/mapping.jsp", {tag:"recommend",donation_request_id:postId,recommendation_owner:user_entity_id_Global}, function(data){ 
		  
		    if(data['success']==1){
			//recommendation has been post, get the current count and increase it by one
			//look for the specific recommend that produced the action then update the count, the append it later
			var current_recommendation_count = $("#badge-" + postId +"-" + owner_entity_id).text();
			$("#badge-" + postId +"-" + owner_entity_id).html(parseInt(current_recommendation_count)+1);
			
			$("#recommend-action_link-" + postId + "-" + owner_entity_id).html("Unrecommend");
			  
		      }else{
			    //loading gif will continue loading
			
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

		      }else{
			//loading gif will continue loading

		      }

		  },'json');
	       

	      }
   
	  }else if(type=='msg'){
	    //check if the logged person is connected to whoever s/he is Messaging
	    //alert(owner_entity_id);
		
		$.post("../mapping/mapping.jsp", {tag:"isConnected",sender:user_entity_id_Global,receiver:owner_entity_id}, function(data){
		      var names = $('#name_link-'+ postId).text();
		      
		      if(data['success'] == 1){
			
			//the two are connected and a message can be sent
			$('#send-message-footer').html("<button type='button' class='btn btn-default' data-dismiss='modal'>Cancel</button>" +
			"<button id='send-message-" + user_entity_id_Global + "-" + owner_entity_id + "' type='submit' class='btn btn-primary'>Send</button>");
			
			$('#message-modal').modal('show');
			$('#message-error').html("");
			
			$('#new-message-display-area').removeClass('hidden');
			
			$('#send-message-textbox').val(names);
		
			$('#dialog-id').html(user_entity_id_Global + "-" + owner_entity_id);
			
		      }else if(data['success'] == 2){
			
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
	      //var names = $('#name_link-'+ postId).text() donnee or post owner id
	      //user_full_names_Global donors full names
	      
	      $('#donate-modal').modal('show');
	      
	      $('#donate-footer').html("");
	      
	      
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
	      
    });
    
    $('#comment-and-post').keypress('.comment-text-area', function (e) {
	  if (e.keyCode == 13) {                //check its ID and extract the post id
		var elementId = e.target.id;
		var resultArray = elementId.split('-');
		e.preventDefault();
		var postId = resultArray[2];		//get text from the text-area
		var comment_string = $('#comment-details-'+postId).val();//alert(postId + "  " + comment_string);
     
     
		//post the comment to database with tag (donation_request_comment)
     
		$.post("../mapping/mapping.jsp", {tag:"post_new_comment",donation_request_id:postId,entity_id:user_entity_id_Global,comment_text:comment_string}, function(data){
		    $('#new-comment-' + postId).html('');
		    
 		    $('#new-comment-' + postId).html("<img  src='../res/images/loading.gif' /> Posting...");
		   
		    if(data['success']==1){
			//append this text to others after storing it to database
			$('#donation-post-'+postId).append("<div class='comments post-text' id='donation-comment-'>" +
							      "<div class='col-xs-2 status-pic'>" +
								  "<div class='status-pic'>" +
								      "<img alt='Profile Picture' src='" + profile_pic_path_Global + "'>" + 
								  "</div>" +
							      "</div>" +
							      "<div class='comment row' id='comment-text'>" +
								  "<p><a href='' >" + user_full_names_Global + "</a>" + " " + comment_string + "(On today )</p>" +
							      "</div>" +
							  "</div>");
			//After posting remove the contents of the new-comment element
			
			 $('#new-comment-' + postId).html('');
			
		      
		    }else{
		      
			  //do not remove the loading thing
		      
		    }
		  
		},'json');
	
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
    
  
    $('#loading_country').removeClass('hidden');
    $('#loading_category').removeClass('hidden');
    $('#loading_currency').removeClass('hidden');

    $('#loading_currency_donate').removeClass('hidden');    
    $.post("process_request.jsp", {tag:"countrys"}, function(data){
		
        var list = '<option value="" >Select Country</option>';
        for(var i = 0; i< data.length; i++){
            list = list + '<option value="'+data[i]["id"]+'"> '+ data[i]["name"] + '</option>';
            
        }
      
      $("#countrys").html(list);
	   $('#loading_country').addClass('hidden');
    },'json');
    
   
//     ===============================start Donate validation=================================
	      $('#donate-form').bootstrapValidator({
		message: 'This value is not valid',
		
		submitHandler: function(validator, form, submitButton) {
				var currency_of_donation=$("#donation_currency_donate").val();
				var amount_donated=$("#message-text-area").val();
				var donee = "";
				 
				
				var idArray = $('#').text().split('-');
				
				var sen = idArray[0];
				var rec = idArray[1]; 
				//alert('send-message-' + user_entity_id_Global + '-' + rec);
				
				$('#send-message-' + user_entity_id_Global + '-' + rec).html("Sending...");
		 
				$.post('../mapping/mapping.jsp',{tag:"save",to:rec,msg:message,from:user_entity_id_Global}, function(result) {
					
				
		    }, 'JSON');
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
				message: 'Amount field is empty'
			    }
			}
		    }
		  
		}
    });
    
//     =============================end donation validation======================================
    
    
    
    //.............send Message form validation...............................................
    
    
    
        $('#message-form').bootstrapValidator({
		message: 'This value is not valid',
		
		submitHandler: function(validator, form, submitButton) {
				var to=$("#send-message-textbox").val();
				var message=$("#message-text-area").val();
				
				var idArray = $('#dialog-id').text().split('-');
				
				var sen = idArray[0];
				var rec = idArray[1]; 
				//alert('send-message-' + user_entity_id_Global + '-' + rec);
				
				$('#send-message-' + user_entity_id_Global + '-' + rec).html("Sending...");
		 
				$.post('../mapping/mapping.jsp',{tag:"save",to:rec,msg:message,from:user_entity_id_Global}, function(result) {
					
					if(result['success'] == 1){
					  
						  $('#new-message-display-area').addClass('hidden');
						      
						  $('#message-error').html("<div class='alert alert-success'>" +  result['message'] + "</div>");
						  
						  $("#message-text-area").val("");
						     
						      
						  $('#send-message-footer').html("<button type='button' class='btn btn-default' data-dismiss='modal'>OK</button>");
					      
					  
					}else{
					  
						$('#send-message-' + user_entity_id_Global + '-' + rec).html("Send");
						
						$('#message-error').html("<div class='alert alert-danger'>"  + result['message'] + "</div>"); 

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
    
    
    
    //.............end of Message send validation........................................
    
    
    
    
    
    //................................request post processing..................................
    
          $('#donation-request-form').bootstrapValidator({
        message: 'This value is not valid',
        
        submitHandler: function(validator, form, submitButton) {
 			var category=$("#donation_category").val();
 			var f_date=$("#f_date").val();
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
					
					$('#post_result').html(result['message']);
					$('#post_result').removeClass('hidden');
					
				  }else if(result['success'] == '1'){
					
				      //it was a success
					$('#post_request_form').modal('hide');
					var full_name = result['full_name'];
					var profile_pic_path = result['profile_pic_path'];
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
									    "<img alt='Profile Picture' src='" + profile_pic_path + "'>" +
									    "</div></div> <div='appeal-post' id='post-text'>" +
									    "<p><a href=''>" + full_name + " " + "</a>" + summary + ":  " + details	+ ""+
									    "<br> <b>Category:</b> " + next(arrCat,category) +
									    "<br><b>Expected funding date:</b> " + f_date +  "<br><b>Amount requested:</b> " + amount + " " + next(arrCur,currency) + ""+
									    "(requested on " + time_of_post + ")</p>" +
									    "<div class='row post-actions-row' id='post-actions-" + returned_donation_request_id + "'>" +				
									    "<button id='comment-action_link-" + returned_donation_request_id  + "-" + entity_id  + "' class='link'>Comment</button> " +
									    "<button id='recommend-action_link-" + returned_donation_request_id  + "-" + entity_id  + "' class='link'>Recommend</button> " +
									    "<button id='msg-action_link-" + returned_donation_request_id  + "-" + entity_id  + "' class='link'>Message post owner</button> " +
									    "<button id='donate-action_link-" + returned_donation_request_id  + "-" + entity_id  + "' class='link'>Donate to this cause</button> " +
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
                    stringLength: {
                        min: 9,
                        max: 12,
                        message: 'Date must be 10 or more characters long'
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
                    notEmpty: {
                        message: 'Please enter request amount'
                    },
		    regexp: {
                        regexp: /([1-9][0-9]*)|0/,
                        message: 'Amount can only be made of numbers'
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
	    //obj[data[i]["id"]] = data[i]["name"];
            arrCur.push({key:data[i]["id"],value:data[i]["name"]});
        }
      
	      $("#donation_currency").html(list);
	      $("#donation_currency_donate").html(list);
	      $('#loading_currency').addClass('hidden');
	      $('#loading_currency_donate').addClass('hidden');
	},'json');
    
    //.........................currency end...................................
       
       
       $('#countrys').on('change', function() {
		$('#loading_city').removeClass('hidden');
        var country = this.value;
        $.post("process_request.jsp", {tag:"cities", country:country}, function(data){
            
            var list = '<option value="" >Select City</option>';
            var no_of_cities = data.length;
            for(var i = 0; i < no_of_cities; i++){
                list = list + '<option value="'+data[i]["id"]+'"> '+ data[i]["name"] + '</option>';
                
            }
	      
	      $("#cities").html(list);
	      
	      $('#loading_city').addClass('hidden');
	   
        },'json');

    });
		 
	 
	$('#typed_email_invite').bootstrapValidator({
		message: 'This email does not appear to be valid',
        
	    submitHandler: function(validator, form, submitButton) {
			var email = $('#email').val();
			
			      $.post("../mapping/mapping.jsp",
				{
					tag:"email_typed", email:email,
					  
				}, function(result) {
				
				if(result['success']=='1'){
				 $("#typed_mail_response_message").html("get");
				}else if(result['success'] == '0'){
					
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
		    notEmpty: {
                        message: 'Please enter a valid email'
                    },
                   emailAddress: {
                        message: 'This email address does not appear to be valid'
                    }
                }
            }	
	}
   });

    $('#f_date').datepicker({
	format: "dd/mm/yyyy"
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
