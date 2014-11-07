$(document).ready(function() {
    var harambesa_bal_Global = "";
    var harambesa_social_points_Global = "";   
    var user_id = "";
     $.post("../mapping/mapping.jsp",{tag:"global_dresser"}, function(data){      
		harambesa_bal_Global = data['harambesa_balance'];
		harambesa_social_points_Global = data['harambesa_points']; 
		user_id = data['entity_id'];
		$('#balance').html("Harambesa Balance: " + harambesa_bal_Global);
		$('#points').html("Harambesa Points:" + harambesa_social_points_Global);
		
		if($('#id-mpesa-reference').length != 0){
			$('#id-mpesa-reference').html(user_id);
		}
		
		if($('#id-airtel-reference').length != 0){
			$('#id-airtel-reference').html(user_id);
		}
		
     },'json');  
     
});