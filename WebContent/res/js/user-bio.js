$(document).ready(function(){
			var user = getUrlVars()["user"]; 

			if(user==undefined || user==null || !user.trim()){
				showNoUser();
			}else if(Math.floor(user) == user && $.isNumeric(user)){
				fetchUserData(user);
			}else
				showNoUser();
}); 

function showNoUser(){
	$('#user-bio').html('<div class="bg-info text-center">No user found</div>');
}

function fetchUserData(user){
					var newDataRequest = $.ajax({
						type: "POST",
						url: "../userdata/",
 						timeout: 30000, // timeout after 30 seconds
						dataType: "json", //JSON
						data: {tag:'fetchuserdata', user: user}

					});	
					newDataRequest.done(function(data){
						if(data["success"]==0){
									if(data["redir"]){
										showSessExpired();
									}else
										$('#user-bio').html('<div class="bg-warning text-center">'+data['message']+'</div>');
						}else if(data["success"]==1){
										if(!data["id"])
											$('#user-bio').html('<div class="bg-info text-center">No user found</div>');
										else
											displayUserData(data);
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
							alert('Requested JSON parse failed.');
						}else if (exception === 'timeout'){
							alert('Time out error.');
						}else if (exception === 'abort'){
							alert('Sorry, Request was aborted.');
						}else{
							alert('Sorry, an error occured.');
						}
					});
}

function displayUserData(data){
				var html = '<div class="row user-bio-data"><div class="col-xs-4"><div class="row"><img class="img-responsive" src="../profilepic/'+data['prof_pic']+'"></div><div class="row user-connect-action" id="'+data['id']+'" reqw="'+data['reqw']+'">';
				if(data['owner']){
					
				}else if(data['req']==0)
					html+='<button class="btn-primary conn-action" id="send-connection-request">Send Connection Request</button>';
				else if(data['req']== 1){
					if(data['acc']==1 && data['ter']==false){
							html+='<button class="btn-default conn-action" id="terminate-connection"><span class="fa fa-check-square"></span> Connected </button>';
					}else if(data['acc']==1 && data['ter']==true){
							html+='<button class="btn-primary conn-action" id="send-connection-request">Send Connection Request</button>';
					}else if(data['reqo']== 1 )
							html+='<div class="connection-info"><span class="fa fa-check-square"></span> Connection Request Sent</div>';
					else if(data['reqo'] == 0){
							html+='<button class="btn-primary conn-action" id="accept-connection-request">Accept Connection Request</button><button class="btn-primary conn-action" id="ignore-connection-request">Ignore Connection Request</button>';
					}
				}
				html+='</div></div><div class="col-xs-8"><div class="user-data row"><div class="col-xs-4 user-bio-elem">Name:</div><div class="col-xs-4 user-bio-text">'+data['name']+'</div></div><div class="user-data row"><div class="col-xs-4 user-bio-elem">Country:</div><div class="col-xs-4 user-bio-text">'+data['country']+'</div></div><div class="user-data row"><div class="col-xs-4 user-bio-elem">Bio:</div><div class="col-xs-4 user-bio-text">'+data['bio']+'</div></div><div class="user-data row"><div class="col-xs-4 user-bio-elem"></div><div class="col-xs-4 user-bio-text"></div></div></div></div>';
				$('#user-bio').html(html);
				bindConnectionActions();
		
}
function bindConnectionActions(){
				applyUserBioCss();
				bindConnectBtnActions();
				listenToConnectionBtnHover()
}

function applyUserBioCss(){
		$('.user-connect-action').css({
				"margin-top":"10px"					
		});
		
		$('.connection-info').css({
				"margin-top":"10px",
				"margin-bottom":"10px",
				"background-color":""
		});
		
		$('#ignore-connection-request').css({
				"margin-top":"5px"
		});
}
function bindConnectBtnActions(){
		$('.conn-action').click(function(e){
				takeConnectionAction($(this));
		});
}

function listenToConnectionBtnHover(){
		$('#terminate-connection').hover(
						function(){
						text = $( this ).html();
						$(this).text("Disconnect");
						},function(){
						$('.conn-action').html(text);
						}
			);
}

function takeConnectionAction(btn){
					var id=$(btn).attr('id');
					var reqw = $(btn).parent().attr('reqw');
					var data = {};
					if(id=='send-connection-request')
							data={tag:'-con', reqtype:'send-con', user:$(btn).parent().attr('id'), id:reqw}; 
					else if(id=='accept-connection-request')
							data={tag:'-con', reqtype:'accept-con', user:$(btn).parent().attr('id'), id:reqw};
					/*else if(id=='cancel-connection-request')
							data={tag:'-con', reqtype:'cancel-req', user:$(btn).parent().attr('id'), id:reqw}; */
					else if(id=='ignore-connection-request')
							data={tag:'-con', reqtype:'ignore-con', user:$(btn).parent().attr('id'), id:reqw};
					else if(id=='terminate-connection')
							data={tag:'-con', reqtype:'terminate-con', user:$(btn).parent().attr('id'), id:reqw};
					var newDataRequest = $.ajax({
						type: "POST",
						url: "../userdata/",
 						timeout: 30000, // timeout after 30 seconds
						dataType: "json", //JSON
						data: data
					});	
					newDataRequest.done(function(data){
							if(data["success"]==0){
										if(data["redir"]){
											showSessExpired();
										}else{
												$('.user-connect-action').append('<div class="bg-warning text-center" id="con-req-error">'+data['message']+'</div>');
												applyConErrorCss();
												setTimeout(slideUpError, 5000);
										}
							}else if(data["success"]==1){
										if(id=='send-connection-request'){
												$('.user-connect-action').html('<div class="connection-info"><span class="fa fa-check-square"></span> Connection Request Sent</div>');
										}else if(id=='accept-connection-request'){
												$('.user-connect-action').html('<button class="btn-default conn-action" id="terminate-connection"><span class="fa fa-check-square"></span> Connected </button>');
										}else if(id=='terminate-connection'){
												$('.user-connect-action').html('<button class="btn-primary conn-action" id="send-connection-request">Send Connection Request</button>');
												displayConnectionFeedback("You have successfully disconnected with this user.");
										}
										bindConnectionActions();
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
							alert('Requested JSON parse failed.');
						}else if (exception === 'timeout'){
							alert('Time out error.');
						}else if (exception === 'abort'){
							alert('Sorry, Request was aborted.');
						}else{
							alert('Sorry, an error occured.');
						}
					});
}

function displayConnectionFeedback(message){
		$('.user-connect-action').append('<div class="bg-info text-center" id="con-req-error">'+message+'</div>');
		
		$('#con-req-error').css({
				"margin-top":"10px",
				"margin-bottom":"10px"
		});
		setTimeout(slideUpError, 5000);
		
}

function slideUpError(){
		$('#con-req-error').slideUp("normal", function() { $('#con-req-error').remove();});
}

function applyConErrorCss(){
		$('#con-req-error').css({
				"margin-top":"10px",
				"margin-bottom":"10px"
		});
}

function displayUserDataError(message){
		alert("Error="+message);

}


// ******************************************** Donation and offers  *************************
(function($){
	"use strict"

	$(function(){		
		var user = getUrlVars()["user"]; 
		// on donation request click
		$("#user_donation_requests").click(function(e){
			e.preventDefault();
			$("#user_timeline").html(loadingImage());
			donation_Offers_Requests("donation",user); //global methods in the window object
			console.log(window);
		});	
		// on offer request click
		$("#user_offer_contribution").click(function(e){
			e.preventDefault();
			$("#user_timeline").html(loadingImage());
			donation_Offers_Requests("offer",user);
		});
	
	});
	

// ******************************************** Offers  *************************
})(jQuery)
