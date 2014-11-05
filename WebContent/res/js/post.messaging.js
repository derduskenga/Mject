 //messaging js this is
 //var logger=log4javascript.getDefaultLogger();
 
 $(document).ready(function(){
	 
	 
	 
});

function alternateMessageView(type){
			var message_color_bg;
			$('#new-message-display-area,#all-messages-display-area').addClass('hidden');
			$('#all-messages-display-area').removeClass('hidden');
			
			$.post("../messages",{tag:"retrieve_all"}, function(data){
				if(data['success']==1){
					$('#all-messages-display-area').html('<div class="row all-message first"  id="'+data['messages'][0]['sender_id']+'"><div class="col-xs-2"><img class="img-responsive" src="../ProfilePic/2014-04-08-181733.jpg" id="img'+data['messages'][0]['sender_id']+'"></div><div class="message col-xs-10">'+data['messages'][0]['message']+'</div></div>');
					for(var i=1; i<data['messages'].length; i++){
						$('#all-messages-display-area').append('<div class="row all-message"  id="'+data['messages'][i]['sender_id']+'"><div class="col-xs-2"><img class="img-responsive"src="../ProfilePic/2014-04-08-181733.jpg"></div><div class="message col-xs-10">'+data['messages'][i]['message']+'</div></div>');
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
							var prof_pic_path=$('#img'+id).attr('src');
							fetchMessageThread(id, prof_pic_path);
						});
					}else if(data['success']=='0'){
					$('#all-messages-display-area').html(data['message']);
					}
					},'json');
	}
	
	function fetchMessageThread(id, prof_pic_path){
			$.post('../messages',{tag:"retrieve_spec", id: id},function(data){
					if(data['success']=="1"){
							var messages = data['messages'];
							var html="";
							for(var k=0; k<messages.length; k++ ){
								
									html +='<div><img src="'+prof_pic_path+'">'+ messages[k]['msg']+'</div>';	
							}
							$('#all-messages-display-area').html(html);
					}else if(data['success']=="0"){
							alert("got 0");
					}else if(data['success']==null){
							alert("something went awry.");
					}
			}, 'json');
	}
	