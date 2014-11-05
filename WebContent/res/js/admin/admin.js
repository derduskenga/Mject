/**
*
*	Create a self invoking function to protect the global scope from populating with variables.
*	
*	Handles the settings page for email and password reset
*
*/
(function($){
	"use strict"
	// on document ready
	$(function(){
		// new users
		$('#newUsers').click(function(e){
			e.preventDefault();
			$("#errorArea").html(" ");
			$(this).addClass('active').siblings('li').removeClass('active');
			newUsers();
		});
		// registered users
		$('#registeredUsers').click(function(e){
			e.preventDefault();
			$("#errorArea").html(" ");
			$(this).addClass('active').siblings('li').removeClass('active');
			registeredUsersUsers();
		});
		// active users
		$('#activeUsers').click(function(e){
			e.preventDefault();
			$("#errorArea").html(" ");
			$(this).addClass('active').siblings('li').removeClass('active');
			currentActiveUsers();
		});
		// Request Donations
		$('#findUsers').click(function(e){
			e.preventDefault();
			$("#errorArea").html(" ");
			$(this).addClass('active').siblings('li').removeClass('active');
			findUsers();
		});

		
	});


/**
*
*	Load new users in the system
*
*
*/
function newUsers(){
	$(".app_users").empty();
	$("#selectMonth").load("includes/newUsers.jsp", function(){
		console.log(this);
		$('#duration').change(function(){
			var duration = $("#duration option:selected").val();
			$.ajax({
				url: 	'new-users',
				data: 	{'duration':duration,tag:'tag_new_users'},
				dataType: 	'json',
				type: 	'GET'
			})
			.done(function(result,responseText,xhr){
				$(".app_users").empty();
				var tableTop = "<div class='row'><div class='col-xs-12'><div class='table-responsive'>"+
				"<table class='table table-hover table-striped table-condensed'>"+
				"<thead>"+
				"<tr>"+
				"<td>First Name</td>"+
				"<td>Last Name</td>"+
				"<td>UserName</td>"+
				"<td>Email</td>"+
				"<td>Active</td>"+
				"<td>Date Enrolled</td>"+
				"</tr>"+
				"</thead>"+
				"<tbody id='dbusers'>";
				$(".app_users").append(tableTop);
				result.forEach(function(result){
					var bodyRow = "<tr>"+
					"<td>"+result.firstName+"</td>"+
					"<td>"+result.lastName+"</td>"+
					"<td>"+result.userName+"</td>"+
					"<td>"+result.email+"</td>"+
					"<td>"+result.active+"</td>"+
					"<td><span>"+result.dateEnroled+"</span></td>"+
					"</tr>"
					$("#dbusers").append(bodyRow); 
				});
				var tableBottom = "</tbody></table></div></div></div>";
				$(".app_users").append(tableBottom);

			})
			.fail(function(xhr){
				console.log("fail");
				console.log(xhr);
				errorDisplay(xhr,"An Error Occured, Contact your adminitor for assistance","danger");
				
			});
		});
	});
}



/**
*
*	Active users
*
*
*/
function currentActiveUsers(){
	$("#selectMonth").empty(); 
	$.ajax({
		url:"active-users",
		data:{tag:"tag_active_users"},
		dataType:"json",
		type:"GET"

	})
	.done(function(result,responseText,xhr){
		$(".app_users").html("<h4>Currently Logged in Users are:  <span class='label label-default'>"+xhr.responseText+"</span></h4>")
	})
	.fail(function(xhr){
		errorDisplay(xhr,"An Error Occured, Contact your adminitor for assistance","danger");
		
	});
	
}

/**
*	Show all the registered users
*
*
*
*/
function registeredUsersUsers(){
	$("#selectMonth").empty();
	$(".app_users").empty();
	$.ajax({
		url:"registered-users",
		data:{"tag":"tag_registered_users"},
		dataType:"json",
		type:"GET"
	})
	.done(function(result, responseText, xhr){
		// $(".app_users").empty();
		var tableTop = "<div class='row'><div class='col-xs-12'><div class='table-responsive'>"+
		"<table class='table table-hover table-striped table-condensed'>"+
		"<thead>"+
		"<tr>"+
		"<td>First Name</td>"+
		"<td>Last Name</td>"+
		"<td>UserName</td>"+
		"<td>Email</td>"+
		"<td>Active</td>"+
		"<td>Date Enrolled</td>"+
		"</tr>"+
		"</thead>"+
		"<tbody id='dbusers'>";
		$(".app_users").append(tableTop);
		result.forEach(function(result){
			var bodyRow = "<tr>"+
			"<td>"+result.firstName+"</td>"+
			"<td>"+result.lastName+"</td>"+
			"<td>"+result.userName+"</td>"+
			"<td>"+result.email+"</td>"+
			"<td>"+result.active+"</td>"+
			"<td><span>"+result.dateEnroled+"</span></td>"+
			"</tr>"
			$("#dbusers").append(bodyRow); 
		});
		var tableBottom = "</tbody></table></div></div></div>";
		$(".app_users").append(tableBottom);
	})
	.fail(function(xhr){
		errorDisplay(xhr,"An Error Occured, Contact your adminitor for assistance","danger");	
	});
}

/**
*
*	Find users in the system
*
*
*/
function findUsers(){
	$(".app_users").empty();
	$("#selectMonth").html(loadingImage());
	$("#selectMonth").load("includes/findUsers.jsp",function(){
		$("#selectMonth #findUser").keyup(function(e){
			$(".app_users").html(loadingImage());

			var searchValue = $(this).val(); 
			// the value is empty dont search
			if(!searchValue.trim()){				
				$(".app_users").empty();
				return; 
			}

			$.ajax({
				url:"search-users",
				data:{"tag":"tag_search_users","searchValue":searchValue},
				dataType:"json",
				type:"GET"
			})
			.done(function(result, responseText, xhr){
				if(result.length > 0){
					$(".app_users").empty();
					var tableTop = "<div class='row'><div class='col-xs-12'><div class='table-responsive'>"+
					"<table class='table table-hover table-striped table-condensed'>"+
					"<thead>"+
					"<tr>"+
					"<td>First Name</td>"+
					"<td>Last Name</td>"+
					"<td>UserName</td>"+
					"<td>Email</td>"+
					"<td>Active</td>"+
					"<td>Date Enrolled</td>"+
					"<td>Actions</td>"+
					"</tr>"+
					"</thead>"+
					"<tbody id='dbusers'>";
					$(".app_users").append(tableTop);
					result.forEach(function(result){
						var bodyRow = "<tr>"+
						"<td>"+result.firstName+"</td>"+
						"<td>"+result.lastName+"</td>"+
						"<td>"+result.userName+"</td>"+
						"<td>"+result.email+"</td>"+
						"<td>"+result.active+"</td>"+
						"<td><span>"+result.dateEnroled+"</span></td>"+
						"<td><a href ='../user/?user="+result.entityId+"' >More</a></td>"+
						"</tr>"
						$("#dbusers").append(bodyRow); 
					});
					var tableBottom = "</tbody></table></div></div></div>";
					$(".app_users").append(tableBottom);
				}else{
					$(".app_users ").html("<div class='text-center alert alert-info'>No Records Found</div>");
				}
			})
			.fail(function(xhr){
				errorDisplay(xhr,"An Error Occured, Contact your adminitor for assistance","danger");	
			});
		});	
	});	
}


/**
*
*
*	Display the error message
*
*/
function errorDisplay(xhr,message,bootstrap_class){
	$("#errorArea").html("<div class='alert alert-"+bootstrap_class+"'> "+message+"</div>");
}



})(jQuery)



/**
*
*
*	Display the error message
*
*/
function userFeedback(xhr){
	console.log(xhr); 
	var message = "";
	if(xhr.status == "404"){ 
		message = xhr.statusText;
		bootstrap_class = "danger";
	}else if(xhr.status == "500"){ 
		message = xhr.statusText;
		bootstrap_class = "danger";		
	}else if(xhr.status == "202"){ 
		var response = JSON.parse(xhr.responseText);
		bootstrap_class = "warning";
		message = response["message"];		
	}else if(xhr.status == "200"){ 
		if(xhr.responseText.trim()){  
			var response = JSON.parse(xhr.responseText.trim());
			message = response["message"];
			bootstrap_class = "success";
		}else{
			message = "Nothing to Display, if this is a mistake contact administrator";
			bootstrap_class = "info";	

		}
	}
	$("#errorArea").html("<div class='alert alert-"+bootstrap_class+"'> "+message+"</div>");
}

/**
*
*
*	Displays the loading image
*
*/
function loadingImage(){
	return "<p class='text-center'><img src='../res/images/loading_site_green_small.gif'> .... please wait as data loads</p>"	
}