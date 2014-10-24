$(document).ready(function(){

      adminLogin();

    
});

function adminLogin(){

	$('#admin_login_form').bootstrapValidator({
             
       message : 'Please enter your login credentials',
       submitHandler : function(validator, form, submitButton){

       	var username = $('#a_username').val();
       	var password = $('#a_password').val();
       	var adminLevel = $("#a_level option:selected").text();

           $.ajax({
                url:"../adminlogin",
                type:"post",
                dataType:"json", 
                data:{
                	tag:"log_in",username:username,password:password,adminLevel:adminLevel
                }
        	})
           .done(function(data){

            	if(data['success'] == 1){
            		alert(data['message']);
            	}else if(data['success'] == 0){
            		alert(data['message']);
            	}
            }).
            fail( function(){
            	alert("Sorry, an error occured while login you in. Please try again after sometime");
            });
           return false;

       },fields: {
        		a_username: {
                validators: {
                    notEmpty: {
                        message: 'Please enter your username'
                    }
                }
	            },a_password : {
	            	validators:{
	            		notEmpty: {
	            			message : 'Please Enter toy passwords'
	            		}
	            	}
	            },a_level : {
	            	validators:{
	            		notEmpty:{
	            			message:'Please select your admin level'
	            		}
	            	}
	            }

	        }



	});




}

