 $(function(){//page ready loader i.e loads the page when ready
      $('#create_admins').click(function(e){
        e.preventDefault();//prevents the defaul action loaded page 
        $('#swap_area').load("create.jsp",function(){
            create_admin()
        });
   }); 
       $('#view_admins').click(function(e){
       e.preventDefault();
       $('#swap_area').load("view_.jsp",function(){
           view_admins()
    });
  });

     $('#manage_admins').click(function(e){
       e.preventDefault();
       $('#swap_area').load("manage_.jsp",function(){
           //view_admins()
    });
  });


 });



 function view_admins(){

      var adminRequest = $.ajax({
            
            url:'../../adminlogin',
            type:'post',
            dataType:'json',
            data:{
                tag:"view_admins"
            }
      })
      .done(function(data){
        console.log("Inside data success function");
          if(data['success'] == 1){
            console.log("Success");
            var rows = data['total'];//variable that contains the total number of raws
            if(rows>0){

            var admins = data['admins'];//array containing individual admin detail/columns
            
             
            for (var x = 0; x < rows; x++) {
                var admin = admins[x];

                var e_id = admin['e_id'];
                var f_name = admin['f_name'];
                var l_name = admin['l_name'];
                var usrname = admin['u_name'];
                var profile_pic = admin['profile_pic'];
                var primary_email = admin['p_email'];
                var mobile_number = admin['m_number'];
                var date_enrolled = admin['date_enrolled'];
                var adminlevel = admin['a_level'];
            
               $('#view_all_admin #tableHead').append(
                    '<tr>'+
                    '<td>'+f_name+'</td>'+
                    '<td>'+l_name+'</td>'+
                    '<td>'+usrname+'</td>'+
                    '<td>'+primary_email+'</td>'+
                    '<td>'+mobile_number+'</td>'+
                    '<td>'+date_enrolled+'</td>'+
                    '<td>'+adminlevel+'</td>'+
                    '<td><input data-val1="'+f_name+'" data-val2="'+l_name+'" type="Submit" value="Suspend" class="suspend_admin_btn" id="suspend_" data-toggle="modal" data-id="'+e_id+'"></td>'+
                    '<td><input data-val3="'+f_name+'" data-val4="'+l_name+'" type="Submit" value="Diasble" class="disable_admin_btn" id="disable" data-toggle="modal" data-id2="'+e_id+'"></td>'+
                   '</tr>'
                );
               console.log(e_id);

            
          } 

          bindSuspendAndDisableAdminBtns();
           
            }else{


            }

          }else if(data['success'] == 0){



          }
      })
      .fail(function(data){

      })
 }


 function bindSuspendAndDisableAdminBtns(){
        $('.suspend_admin_btn').click(function(e){
            e.preventDefault();
            var this_id = $(this).attr('data-id');
            var first_name = $(this).attr('data-val1');
            var second_name = $(this).attr('data-val2');

            $('#dialog-example').modal('show');
            $.post('view_.jsp',
            {
              id:$(this).attr('data-id')
            },
            function(data){
              $('.modal-body').html("<h5>Are you Sure you want to Suspend"+" <b>"+first_name+" "+second_name+"</b>'s Account</h5>");
              $('#container .modal-footer #suspend_btn').val(this_id).click(function(e){
                suspend_admin();
              });
            });
        });
       $('.disable_admin_btn').click(function(e){
            e.preventDefault();
            var this_id2 = $(this).attr('data-id2');
            var first_name2 = $(this).attr('data-val3');
            var second_name2 = $(this).attr('data-val4');

            $('#dialog-example1').modal('show');
            $.post('view_.jsp',
            {
              id:$(this).attr('data-id2')
            },
            function(data){
              $('.modal-body').html("<h5>Are you Sure you want to Disasble"+" <b>"+first_name2+" "+second_name2+"</b>'s Account</h5>");
              $('#container .modal-footer #disable_btn').val(this_id2).click(function(e){
                disable_admin();
              });
            });
       });
 }

 function disable_admin(){
    var disableId = $('#disable_btn').val();
    $.ajax({
      url:'../../adminlogin',
      type:'post',
      dataType:'json',
      data:{
        tag:"disable_admin",admin:disableId
      }
    })
    .done(function(data){
        if(data['success']==1){

          $('#diasble_status').html("Account has been succesfully disabled");

        }else if(data['success']==0){
           
           $('#diasble_status').html("Account not succesfully disabled");

        }
    })
    .fail(function(data){
        
    });
    return false;
 }

function suspend_admin(){
   
   var admin_id = $('#suspend_btn').val();
   $.ajax({
      url:'../../adminlogin',
      type:'post',
      dataType:"json",
      data:{
        tag:"suspend", admin_id:admin_id
      }
   })
   .done(function(data){

       if(data['success'] == 1){

          $('#suspend_status').html("Suspension was successful");

       }else if(data['success' == 0]){
         $('#suspend_status').html("Suspension failed");
       }

   }).fail(function(data){

   });
   return false;
}

 function create_admin(){ 
 	$('#create_admin_form').bootstrapValidator({

 		 live:'enabled',

 		submitHandler : function(validator , form, submitButton){

 			var first_name = $('#f_name').val();
 			var second_name = $('#s_name').val();
 			var temp_password = $('#password').val();
 			var c_temp_password = $('#c_password').val();
 			var p_email = $('#p_email').val();
 			var a_level = $('input:radio[name=level]:checked').val();

 			$.ajax({
 				url: '../../adminlogin',
 				type: 'post',
 				dataType: "json",
 				data:{
 					tag:"create_admin", fname:first_name, sname:second_name, tmppassword:temp_password, level:a_level, pri_email:p_email
 				}

 			})
 			.done(function(data){
 				if(data['success']== 1){

 					$('#swap_area').html('Administrator Succesfully Created');

 				}else if(data['success'] == 0){

 					$('#swap_area').html('Administrator not succesfully Created');

 				}

 			})
 			.fail(function(data){
 				console.log(data);
 				alert('Sorry, an error occured while you were creating a new admin. Please try again after sometime');

 			});
 			return false;

 		},
 		fields:{
            f_name:{
            	validators:{
            		notEmpty:{
            			message:'Please provide a first name for the new administrator'
            		}
            	}
            },s_name:{
            	validators:{
            		notEmpty:{
            			notEmpty:{
            				message:'Please provide a second name for the new administrator'
            			}
            		}
            	}
            },c_password:{
            		validators:{
            			notEmpty:{
            				message:'Please confirm the temporary passwrod that you provided'
            			}
            		}
            	},p_email:{
            		validators:{
            			notEmpty:{
            				message:'You must provide the administrators primary email'
            			}
            		}
            	}
            	,password:{
            		validators:{
            			notEmpty:{
            				message:'Please provide a temporary password for the new administrator'
            			}
            		}
            	}
            }

 

 	});

 }