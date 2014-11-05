$(document).ready( function(){
  $.post("../connectionrequests", {tag:"connectionrequests"}, function(data){	
	 var connection_reqiest="";
	 
	 for(var i=0; i<data["requests"].length; i++){
	    	//below is a variable that will be passes to 
		 connection_reqiest+=
	    	'<div id="request_'+data["requests"][i]["entity_id"]+'"class="row connection_reqiest">'
				+'<div class="connect-pic col-xs-2">'+'<img alt="connect-pics" src="'+data["requests"][i]["profile_pic_path"]+'">'+'</div>'
				+'<div class="col-xs-3 details  person-details">'+data["requests"][i]["first_name"]+"   "+data["requests"][i]["last_name"]+'</div>'
				+'<div class="btn-group">'
						+'<button id="con_button-'+data["requests"][i]["entity_id"]+'" class="btn btn-primary connect-button">Accept</button>' 
						+""+""+""
// 						+'<button id="mes_button-'+data["requests"][i]["entity_id"]+'" class="btn btn-primary connect-button">Message</button>'
// 						+""+""+""
						+'<button id="ign_button-'+data["requests"][i]["entity_id"]+'" class="btn btn-primary disconect-button" >Ignore</button>'
						//+data["requests"][i]["entity_id"]
					+'</div>'			
		+'</div>';
	}
	//end of for loop
	
	 $('#main_content_middle').html(connection_reqiest); 
	//when 
	 
	 $('.btn').click(function(e){
			//alert(e.target.id);
			
			
			var elementID = e.target.id;
			
			//getting the Requestor_id of the button clicked
			var btn_entity_id = elementID.split('-')[1];
			//alert(e.target.id);
			//alert(btn_entity_id);
			
			//getting the buttonType that was clicked
			var button = e.target.id;
 			var buttonType =  button.split('_')[0];
			//alert(buttonType);
			
			
			
// 			
			var divToHide = "request_"+btn_entity_id;
			//alert(divToHide);
			//var paro =$('#btn').parent(div);
			//alert(paro);
				$('#'+divToHide).addClass('hidden');
				
				if (buttonType=="con"){
					
					alert("posting to connect");
					
						$.post("../processconnectionrequests", {Requestor_id:btn_entity_id}, function(data){	
				  
				       },'json');


				}else if(buttonType=="ign") {
					alert("posting to ignore");
					
						$.post("../ignoreconnectionrequests", {Requestor_id:btn_entity_id}, function(data){	
				  
				       },'json');

					
				}
				
				$.post("../processconnectionrequestsss", {Requestor_id:btn_entity_id}, function(data){	
				  
				       },'json');


			
			
		});
     },'json');
   	   
  
	   
});