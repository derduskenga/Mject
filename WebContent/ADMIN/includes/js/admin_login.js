$(document).ready(function(){

      adminLogin();
      setAdminPassword();

    
});

function setAdminPassword(){
    
     $('#set_password_form').bootstrapValidator({
        
        submitHandler:function(validator,form, submitButton){
            
            var password1 = $('#s_password').val();
            var password2 = $('#c_s_password').val();
            var admin_id = $('#admin_entity_id').val();

            $.ajax({

              url:'../adminlogin',
              dataType:'json',
              type:'post',
              data:{
                tag:"set_admin_password", password1:password1, admin_id:admin_id
              }

            }).done(function(data){

              if (data["success"] == 1) {
                console.log("Password set");
              }else if (data["success"] == 0) {
                console.log("Pasword not set");
              }

            }).fail(function(data){

            });
            return false;

        },
          feedbackIcons: {
          valid: 'glyphicon glyphicon-ok',
          invalid: 'glyphicon glyphicon-remove',
          validating: 'glyphicon glyphicon-refresh'
        },fields:{
           s_password:{
             validators:{
                identical: {
                    field: 'c_s_password',
                    message: 'The password and its confirm are not the same'
                }
             }
           },c_s_password:{
               validators:{
                 identical: {
                        field: 's_password',
                        message: 'The password and its confirm are not the same'
                    }
               }
           }
        }
     });

}

function adminLogin(){

	$('#admin_login_form').bootstrapValidator({
             
       message : 'Please enter your login credentials',
       submitHandler : function(validator, form, submitButton){

       	var username = $('#a_username').val();
       	var password = $('#a_password').val();
       	var adminLevel = $("#a_level option:selected").text();

        console.log(adminLevel);

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
                  
                  url = "../ADMIN/proces_admin.jsp";

                 var message = (data['message']);
            		  $('#succes').append('<div class="alert alert-success alert-dismissible" role="alert">'+message+'</div>');
                  
                  setTimeout(function(){
                    $(location).attr("href", url)
                  },2000);

                  postlogs();
                  

            	}else if(data['success'] == 0){

            		  var message = (data['message']);
                  $('#succes').append('<div class="alert alert-success alert-dismissible" role="alert">'+message+'</div>');

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
	            			message : 'Please Enter your password'
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

function postlogs(){

  $.ajax({
    url:'../adminLogs',
    dataType:'json',
    type:'post',
    data:{
      tag:"update_logs"
    }
  }).done(function(data){
    if (data['success'] == 1) {
      console.log(data['message']);
    }
  })
  .fail(function(data){

  });

}

