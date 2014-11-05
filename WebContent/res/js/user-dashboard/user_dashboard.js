/**
*
*	SElf invoking function to stop flooding the global scope
*	also prevent jQuery $ crashing with any future js libraries
*
*
*/

(function($){
	"use strict"
	/**
	*
	*	On document load
	*
	*
	*/
	$(function(){

		// fetch all the requests belonging to a user
		$("#user_donation_request").click(function(e){
			e.preventDefault(); 
			donation_Offers_Requests("donation"); 
		});	

		// fetch all the offers belonging to a user
		$("#user_offer_request").click(function(e){
			e.preventDefault(); 
			donation_Offers_Requests("offer"); 
		});	

	});



})(jQuery)



/**
*
*	Donation requests
* 	(Global method)
*
*
*/
function donation_Offers_Requests(request_type,user){
	user = user || ""
	var tag = request_type == "donation"?"user_donation_request_tag":"user_offer_application_tag";
	var data={};
	console.log(user.trim());
	// if user is empty
	if(!user.trim()){
		data = {"tag":tag};
	}else if(user.trim()){			
		data = {"tag":tag,"user_id":user};
	}

	$.ajax({
		url: "donation-request",
		data: data,
		dataType: "json",
		type: "GET"
	})
	.done(function(result,responseText,xhr){
		if(result.length > 0){

			if(request_type == "donation"){	
				$("#user_timeline").load("includes/donation_display.jsp", function(){			
				 
					result.forEach(function(request){ 
						var complete = "";
						if(request.complete == "t"){
							complete = "Full Donation Made";
						}else if(request.complete == "f"){
							complete = "Donation Not Met";

						}
						var oneRequest = "<tr>"+
											"<td>"+request.request_summary+"</td>"+
											"<td>"+request.details+"</td>"+
											"<td>"+request.donation_request_amount+"</td>"+
											"<td>"+request.donation_already_made+"</td>"+
											"<td>"+request.donation_request_date+"</td>"+
											"<td>"+request.expected_funding_date+"</td>"+
											"<td>"+ complete+"</td>"+  
											"<td><a>Contribute</a></td>"+  
										"</tr>";
						$("#user_requests").append(oneRequest);
					});		

				});
			}else if(request_type=="offer"){
				$("#user_timeline").load("includes/offer_display.jsp", function(){			
				 
					result.forEach(function(request){ 
						var complete = "";
						if(request.complete == "t"){
							complete = "Full Donation Made";
						}else if(request.complete == "f"){
							complete = "Donation Not Met";

						}
						var oneRequest = "<tr>"+
											"<td>"+request.offer_summary+"</td>"+
											"<td>"+request.offer_details+"</td>"+
											"<td>"+request.offer_amount+"</td>"+ 
											"<td>"+request.offer_date+"</td>"+
											"<td>"+request.offer_expire_date+"</td>"+ 
											"<td><a>Apply</a></td>"+  
										"</tr>";
						$("#user_requests").append(oneRequest);
					});		

				});
			}
		}else{
			$("#user_timeline").html("<div class='text-center alert alert-info'>No Records Found</div>");
		}
	})
	.fail(function(xhr){
		userFeedback(xhr);
	});	
}