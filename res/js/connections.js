$(document).ready(function(e){
		if($('#load-all-connections').is(':visible')){
				showLoadingGif();	
				loadConnections();
		}
}); 

function showLoadingGif(){
	$('#load-all-connections').html('<div class="center-block"><img alt="Loading Connections..." class="center-block" src="../res/images/loading_site_green_small.gif"></div>');
}

function loadConnections(){					
			$('#connections-error').addClass("hidden");
			var newDataRequest = $.ajax({
					type: "POST",
					url: "../connections",
					timeout: 30000, // timeout after 30 seconds
					dataType: "json", //JSON
					data: { tag:'allrequests'}
			});	
			
			newDataRequest.done(function(data){
				if(data["success"]==0){
							if(data["redir"]){
								showSessExpired();
							}else{
								$('#connections-error').text(data["message"]).removeClass("hidden");
							}
				}else if(data["success"]==1){
							if(data['total']==0){
								$('#load-all-connections').html('<div class="bg-info text-center">You do not have any connection requests.</div>');
							}else{
								var friends = data["friends"];
								var i;	
								var connection_reqs="";
								var friendsLen = friends.length;
								for(i=0;i<friendsLen; i++){
									var friend = friends[i];		
									connection_reqs +='<div class="connection-request row" id="'+friend['id']+'" reqo_id="'+friend['reqo_id']+'"><div class="col-xs-4 requestor-profile-pic"><img class="img-responsive" src="../profilepic/'+friend['pic_path']+'"></div><div class="col-xs-8" reqo_id="'+friend['reqo_id']+'" reqid="'+friend['id']+'" id="'+friend['id']+'"><div class="row">'+friend['name']+'</div><div class="row"><small>'+friend['bio']+'</small></div><div class="row respond-connection-request-actions"><button href="#" type="button" class="accept-friend-request col-xs-4">Accept</button><button type="button" class="ignore-friend-request col-xs-4 col-xs-offset-1">Decline</button></div></div></div>';
								 if(i==(friendsLen-1)){}else connection_reqs+="<hr/>";
								}
								$('#load-all-connections').html(connection_reqs);
								loadConnectBtnCss();
								bindConnectionActionButtons();
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

function loadConnectBtnCss(){
	$("<link/>", {
		rel: "stylesheet",
		type: "text/css",
		href:"../res/css/buttonstyling.css"
	}).appendTo("head");
}

function bindConnectionActionButtons(){
		$('.accept-friend-request').click(function(e){
					completeConnectionRequest('accept-connection-request', $($(this).parent()).parent().attr('reqo_id'), $($(this).parent()).parent().attr('reqid'));
					$(this).parent().html('<div class="center-block"><img alt="Placing bid..." class="center-block" src="../res/images/loading_site_green_small.gif"></div>');
		 });
		
		$('.ignore-friend-request').click(function(e){
					completeConnectionRequest('ignore-connection-request', $($(this).parent()).parent().attr('reqo_id'), $($(this).parent()).parent().attr('reqid'));
					$(this).parent().html('<div class="center-block"><img alt="Placing bid..." class="center-block" src="../res/images/loading_site_green_small.gif"></div>');
		});
}

function completeConnectionRequest(req_type, user_id, reqw){
			if(req_type=='accept-connection-request')
					data={tag:'-con', reqtype:'accept-con', user:user_id, id:reqw};
			if(req_type=='ignore-connection-request')
					data={tag:'-con', reqtype:'ignore-req', user:user_id, id:reqw};
			var newDataRequest = $.ajax({
						type: "POST",
						url: "../userdata/",
 						timeout: 30000, // timeout after 30 seconds
						dataType: "json",
						data: data
			});	
			
			newDataRequest.done(function(data){
				if(data["success"]==0){
							if(data["redir"]){
								showSessExpired();
							}else{
								if(data['message'])
									alert(data['message']);
							}
				}else if(data["success"]==1){
						if(req_type=='accept-connection-request'){
							$('#'+reqw+' .respond-connection-request-actions').html('<div><span class="fa fa-check-square"></span> Connected </div>');
						}
						else if(req_type=='ignore-connection-request'){
							$('#'+reqw+' .respond-connection-request-actions').html('<div class="bg-warning col-xs-7 col-xs-offset-1" id="terminate-connection"> Connection request hidden </div>');
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
						alert('Requested JSON parse failed...');
					}else if (exception === 'timeout'){
						alert('Time out error.');
					}else if(exception === 'abort'){
						alert('Sorry, Request was aborted.');
					}else{
						alert('Sorry, an error occured.');
					}
			});
}