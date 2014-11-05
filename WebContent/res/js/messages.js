$.profile_pics={};
$.profile_pics.me="";
$(document).ready(function(){
		var message_icon_listener=$("#common_messages_icon");
		$(message_icon_listener).click(function(){
				$("#e7").select2('val',""); 
				$("#message").val("");
		});
		
		$('#all-messages-link, #new-message-icon').click(function(){
				alternateMessageView(this.id);
		});
		
		$('#message-send-btn-all').click(function(){
			var id=$('#header-name').attr('u_id');
		});
		
		$('#message-form-all').bootstrapValidator({
			message: 'This value is not valid',
			// live: 'disabled',
			submitHandler: function(validator, form, submitButton) {			
 		    var to= $('#header-name').attr('u_id');
 			var message=$("#message_all").val();
            $.post("../messages",
				{
					 tag:'save',msg:message, to:to , fetch:"new_msg"
				}, function(result) {
				if(result['success']==1){
					var msg_date=formatThisDate(result['t'], result['days'], result['date']);
					//alert(result['msg'];
					//$('#all-messages-display-area').append('<div class="row"><div class="col-xs-8 bubble2">'+ message+'</div><div class="col-xs-2"><img class="img-responsive msg-profile-pic-right" src="../profilepic/'+$.profile_pics.me+'"></div></div>');
					$('#all-messages-display-area').append('<div class="row"><div class="col-xs-8 bubble2"><div class="row">'+ result['msg']+'</div><div class="row"><h5><small>'+msg_date+'</small></h5></div></div><div class="col-xs-2"><img class="img-responsive msg-profile-pic-right" src="../profilepic/'+$.profile_pics.me+'"></div></div>');
					applyMessageCss();
					clearAllMsgTextBox();
				}else{
					alert(result['message']);
				}
            }, 'json');
        },
		feedbackIcons: {
            valid: '',
            invalid: 'fa fa-times',
            validating: ''
		  },
		fields: {
            message_all: {
                validators: {
					notEmpty: {
                        message: 'Please enter your message'
                    }
                }
            }	
		}
	 }); 
	
		
	$('#e7').select2({
			placeholder: "Type to search",
			multiple:true,
			minimumInputLength: 1,
			ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
					url: "../myfriends",
					dataType: 'json',
					data: function (term, page) {
						return {
							q: term, // search term
							page_limit: 4,
						};
			    },
				results: function (data, page) { // parse the results into the format expected by Select2.
					// since we are using custom formatting functions we do not need to alter remote JSON data
					if(data.redir != null){
									BootstrapDialog.show({
											title: 'Session Expired',
											type: BootstrapDialog.TYPE_DEFAULT,
											message: 'Sorry, your session has expired. Please login to continue.',
											buttons: [{ 
												label: 'OK',
												cssClass: 'btn-default', 
												autospin: false,
												action: function(dialogRef){    
													dialogRef.close();
													window.location.href=data['redir'];	
												}
											}]
							});		
					}
					return {results: data.friends};
				}
			},
			formatResult: friendFormatResult, // see function outside this document.ready func
			formatSelection: friendFormatSelection, // see function outside this document.ready func
			dropdownCssClass: "bigdrop", // apply css that makes the dropdown taller
			escapeMarkup: function (m) { return m; } // we do not want to escape markup since we are displaying html in results
			});
	
	//post mail_chck if exists
	 $('#forgot-p-form').submit(function(event) {
		event.preventDefault();
		var r_email=$('#r_email').val();
		$.post('process_request.jsp', {tag:"password_tokens_request", r_email:r_email}, function(result) {
			if(result['success']==0){
				$('#r_p_text').addClass('text-danger').html('<h4>'+result['message']+'<h4> <hr>');
			}
			else{
				$('#pass-reset').addClass('pswd-exist-success-msg').html('<h4>'+result['message']+'<h4>');
				$('#submit-r-btn').addClass('hidden');
				//$('#logon').removeClass('hidden');
			}
		}, 'json') 
	 }); 
	 
	$('#message-form').bootstrapValidator({
		message: 'This value is not valid',
       // live: 'disabled',
        submitHandler: function(validator, form, submitButton) {			
 		    var to=$("#e7").val();
 			var message=$("#message").val();
            $.post("../messages",
				{
					 tag:"save",msg:message, to:to	  
				}, function(result) {
				if(result['success']==1)
					alert(result['message']);
				else{
					alert(result['message']);
				}
            }, 'json');
        },
		feedbackIcons: {
            valid: '',
            invalid: 'fa fa-times',
            validating: ''
		  },
		fields: {
		   to: {
                validators: {
					notEmpty: {
                        message: 'Please enter name of receipient'
                    }
                }
            },
            message: {
                validators: {
					notEmpty: {
                        message: 'Please enter your message'
                    }
                }
            }	
		}
	 }); 
});

function friendFormatResult(friend) {
		var markup = "<table class='friend-result'><tr>";
		markup += "<td class='profile-pic-on-search'><img src='../profilepic/" + friend.profile_pic + "'/></td>";
		markup += "<td class='friend-name'><div class='movie-title'>" + friend.name + "</div>";
		markup += "</td></tr></table>";
		return markup;
}

function friendFormatSelection(friend) {
		return friend.name;
} 
 
function alternateMessageView(id){
		if(id==null){
			
		}else if(id=="new-message-icon"){
				$('#messaging-footer-new-msg').removeClass('hidden');
				$('#new-message-display-area').removeClass('hidden');
				$('#all-messages-display-area').addClass('hidden');
				$('#messaging-footer-all').addClass('hidden');
		}else if(id=="all-messages-link"){
				$('#messaging-footer-new-msg').addClass('hidden');
				$('#new-message-display-area').addClass('hidden')
				$('#all-messages-display-area').removeClass('hidden');
				$('#messaging-footer-all').addClass('hidden');
				showCurrentMessageView();
		}
}

function clearAllMsgTextBox(){
			$('#message_all').val("");
}

function showCurrentMessageView(type){
			var message_color_bg;
			$('#new-message-display-area,#all-messages-display-area').addClass('hidden');
			$('#all-messages-display-area').removeClass('hidden');
			
			$.post("../messages",{tag:"retrieve_all"}, function(data){
				if(data['success']==1){
					if(data['total']>0){
					var user_id="";
					if(data['messages'][0]['sender_id']==data['id'])
						user_id=data['messages'][0]['receipient_id'];
					else
						user_id=data['messages'][0]['sender_id'];
					var full_name=data['messages'][0]['f_name']+' '+data['messages'][0]['l_name'];
					
					var msg_date=formatThisDate(data['t'], data['messages'][0]['days'], data['messages'][0]['date']);
					
					$('#all-messages-display-area').html('<div class="row all-message" name="'+full_name+'"   id="'+user_id+'" id2="'+data['messages'][0]['receipient_id']+'"><div class="col-xs-2"><img class="img-responsive" src="../profilepic/'+data['messages'][0]['profile_pic']+'" id="img'+data['messages'][0]['sender_id']+'"></div><div class="message col-xs-10"><div class="row"><strong>'+full_name+'</strong></div><div class="row">'+data['messages'][0]['message']+'</div><div class="message-date row"><h5><small>'+msg_date+'</small></h5></div></div></div>');
					for(var i=1; i<data['messages'].length; i++){
					var user_id_="";	
					if(data['messages'][i]['sender_id']==data['id'])
						user_id_=data['messages'][i]['receipient_id'];
					else
						user_id_=data['messages'][i]['sender_id'];
					var full_name=data['messages'][i]['f_name']+' '+data['messages'][i]['l_name'];
					var msg_date_=formatThisDate(data['t'], data['messages'][i]['days'], data['messages'][i]['date']);
					
					$('#all-messages-display-area').append('<div class="row all-message" name="'+full_name+'"   id="'+user_id_+'" id2="'+data['messages'][i]['receipient_id']+'"><div class="col-xs-2"><img class="img-responsive" src="../profilepic/'+data['messages'][i]['profile_pic']+'" id="img'+user_id_+'"></div><div class="message col-xs-10"><div class="row"><strong>'+full_name+'</strong></div><div class="row">'+data['messages'][i]['message']+'</div><div class="message-date row"><h5><small>'+msg_date_+'</small></h5></div></div></div>');
					}
						$('.all-message').css({
							"border-top" : "solid #E5E5E5 1px" ,
							"padding-top" : "10px",
							"padding-bottom" : "10px"
						});
						
						$('.all-message.first').css({
							"margin-top": "0px"
						});
					
					$('.all-message').hover(
						function(){
						message_color_bg = $( this ).css( "background-color");
						$(this).css({"background-color" : "#E5E5E5"});
						},function(){
						$(this).css({"background-color" : message_color_bg});
						}
					);
					
					$('.all-message').click(
						function(){
							var id=$(this).attr('id');
							var id2=$(this).attr('id2');
							var name=$(this).attr('name');
							changeHeader(name,id);
							var prof_pic_path=$('#img'+id).attr('src');
							fetchMessageThread(id, id2, prof_pic_path);
						});
					}else{
							$('#all-messages-display-area').html('<div class="center-block">You do not have any messages to view.</div>');
					}
					}else if(data['success']=='0'){
					$('#all-messages-display-area').html(data['message']);
					$('#messaging-footer-all').addClass('hidden');
					}
					},'json');
	}
	
	function formatThisDate(today, days, date){
				var date_tokens=date.split(' ');
				if(days==0){
					if(today==date_tokens[0])
						return date_tokens[4]+ " "+date_tokens[5];
					else
						return "Yesterday "+date_tokens[4]+ " "+date_tokens[5];
				}
				else if(days<6){
					return date_tokens[0]+", "+date_tokens[4]+ " "+date_tokens[5];
				} 
				else{
					return date_tokens[1]+" "+date_tokens[2]+", "+date_tokens[4] + " "+date_tokens[5];
				}
	}
	function changeHeader(name,id){
				$('#header-name').html(" > "+name);
				$('#header-name').attr('u_id',id);
	}
	
	function fetchMessageThread(id, id2, prof_pic_path){
			$.post('../messages',{tag:"retrieve_spec", id: id, id2: id2},function(data){
					if(data['success']=="1"){
							var messages = data['messages'];
							var html="";
							var pic_path="";
							if(data['pic_path']!=null){
								pic_path=data['pic_path'];
							}
									$.profile_pics.me=data['profile_pic'];
								//	alert(data['profile_pic']);
							for(var k=0; k<messages.length; k++ ){
								    var msg_date=formatThisDate(data['t'], messages[k]['days'], messages[k]['date']);
									if(messages[k]['sent_by']==id){
										html +='<div class="row"><div class="col-xs-2"><img class="img-responsive msg-profile-pic-left" src="../profilepic/'+pic_path+'"></div><div class="col-xs-8 bubble">'+ messages[k]['msg']+'<div class="row"><h5><small>'+msg_date+'</small></h5></div></div></div>';}
									else{
										html += '<div class="row"><div class="col-xs-8 bubble2"><div class="row">'+ messages[k]['msg']+'</div><div class="row"><h5><small>'+msg_date+'</small></h5></div></div><div class="col-xs-2"><img class="img-responsive msg-profile-pic-right" src="../profilepic/'+$.profile_pics.me+'"></div></div>';}
							}
							$('#all-messages-display-area').html(html);
							$('#messaging-footer-all').removeClass('hidden');
							applyMessageCss();
					}else if(data['success']=="0"){
							$('#all-messages-display-area').html(data['message']);
							$('#messaging-footer-all').addClass('hidden');
					}else if(data['success']==null){
							$('#all-messages-display-area').html("Sorry, an error occured while loading messages");
							$('#messaging-footer-all').addClass('hidden');
					}
			}, 'json');
	}
	
	function applyMessageCss(){
				$('.bubble').css({
					"background-color":" #F2F2F2",
					"border-radius": "5px",
					"box-shadow": "0 0 6px #B2B2B2",
					"display": "inline-block",
					"padding": "10px 18px",
					"position": "relative",
					"vertical-align": "top",
					"float": "left",
					"margin": "5px 0px 5px 0px",  
					"margin-right": "16.66666667%",
					"border-color": "#cdecb0"
					});
					$('.bubble2').css({
						"background-color": "#dfeecf",
						"border-radius": "5px",
						"box-shadow": "0 0 6px #B2B2B2",
						"display": "inline-block",
						"padding": "10px 18px",
						"position": "relative",
						"vertical-align": "top", 
						"margin": "5px 0px 5px 0px", 
						"margin-left": "16.66666667%",
						"border-color": "#cdecb0"
					});
					$('head').append('<style>.bubble:before {background-color: #F2F2F2;	content: "";display: block;	height: 16px;position: absolute;				top: 11px;transform:             rotate( 29deg ) skew( -35deg );	-moz-transform:    rotate( 29deg ) skew( -35deg );-ms-transform:     rotate( 29deg ) skew( -35deg ); 	-o-transform:      rotate( 29deg ) skew( -35deg );		-webkit-transform: rotate( 29deg ) skew( -35deg ); 	width:  20px;	box-shadow: -2px 2px 2px 0 rgba( 178, 178, 178, .4 );	left: -9px;}</style>');
					
					$('head').append('<style>.bubble2:before {float:right;background-color: #dfeecf; content: "";display: block;height: 16px; position:relative;left: 26px;top: 9px;transform:             rotate( 205deg ) skew( -35deg );-moz-transform:    rotate( 205deg ) skew( -35deg );-ms-transform:     rotate( 205deg ) skew( -35deg ); -o-transform:      rotate( 205deg ) skew( -35deg ); -webkit-transform: rotate( 205deg ) skew( -35deg );    width:  20px; box-shadow: -2px 2px 2px 0 rgba( 178, 178, 178, .4 );}</style>');
					
					$('.bubble').css({
						"background-color": "#F2F2F2",
						"border-radius": "5px",
						"box-shadow": "0 0 6px #B2B2B2",
						"display": "inline-block",
						"padding": "10px 18px",
						"position": "relative",
						"vertical-align": "top",
						"margin": "10px 10px",
						"border-color": "#cdecb0",
						});
				
					  $('.msg-profile-pic-left').css({
					    "margin": "10px 0px 10px 0px"
						});
					  $('.msg-profile-pic-right').css({
					    "margin": "10px 0px 10px 10px"
						});
					  $('.bubble2').css({
							"background-color": "#dfeecf",
							"border-radius": "5px",
							"box-shadow": "0 0 6px #B2B2B2",
							"display": "inline-block",
							/*"padding": "10px 18px", */
							"position": "relative",
							"vertical-align": "top",
							"margin-top": "10px",
							"margin-bottom": "10px",
							"border-color": "#cdecb0"
		}); 
}