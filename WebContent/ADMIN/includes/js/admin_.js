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
     $('#create_department').click(function(e){
       e.preventDefault();
       $('#swap_area').load("deparment1.jsp",function(){
           fetch_admins()
    });
  });
     $('#manage_departments').click(function(e){
        e.preventDefault();
        $('#swap_area').load("manage_dpts.jsp");
        manageDepartments();
     });
      $('#view_logs').click(function(e){
        e.preventDefault();
        alert("working");
      });
 });

function get_admin_logs(){
   var search_log_key = $('#search_a').val();
   alert("working");
   $.ajax({
    url:'../../adminLogs',
    type:'post',
    dataType:'json',
    data:{
      tag:"get_admin_logs",search_log_key:search_log_key
    }
   }).done(function(data){

   }).fail(function(data){
    
   })
}

function fetch_admins(){
      
    $.ajax({
      url:'../../adminlogin',
      dataType:'json',
      type:'post',
      data:{
        tag:"fetch_admins"
      }
    }).done(function(data){

        if(data["success"] == 1){
          
          var rows = data['total'];

          if (rows > 0) {
             
             var names1  = data['names'];

             for (var i = 0; i < rows; i++) {
                 var names = names1[i];
                  
                  var fName = names['first_name'];
                  var sName = names['second_name'];
                  var dataID = names['adminId'];

                 $('#admin_name').append(
                      '<option data-val1="'+fName+'" data-val2="'+sName+'" data-id="'+dataID+'"><b>'+fName+'\t'+sName+'</b></option>'
                 );
             }

             create_department(); 

          }else if(data['success'] == 0){
            $('#create_admin_form').html("There was an error with your database");
          }
        }

    }).fail(function(data){

        console.log("data failed");

    });
    return false;
}

function create_department(){

      var first_name = '';
      var second_name = '';
      var adminId = '';
      var dpt = '';
   $('#create_dpt_form').bootstrapValidator({
      live:'enabled',
         
      submitHandler:function(validator, form, submitButton){
      

        first_name = $('#admin_name option:selected').attr('data-val1');
        second_name = $('#admin_name option:selected').attr('data-val2');
        adminId = $('#admin_name option:selected').attr('data-id');
        dpt = $('#dpt_name').val();

        $.ajax({

          url:'../../adminlogin',
          type:'post',
          dataType:'json',
          data:{
            tag:"create_dpt", department:dpt, first_name:first_name, second_name:second_name,adminId:adminId
          }

        })
        .done(function(data){
            
            if(data['success'] == 1){
                 $('#alertSucces').html('<div class="alert alert-success" role="alert"><strong>Success!</strong>Department Succesfully Created</div>');
            }else if(data['success'] == 0){

            }
        }).fail(function(data){

        });

        return false;

      },
      fields:{
        dpt_name:{
          validators:{
            notEmpty:{
              message:'You must provide a department name'
            }
          }
        },
        admin_name:{
          notEmpty:'You must select an administrator'
        }
      }
   });
}

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
 			var p_email = $('#p_email').val();
 			var a_level = $('input:radio[name=level]:checked').val();

 			$.ajax({
 				url: '../../adminlogin',
 				type: 'post',
 				dataType: "json",
 				data:{
 					tag:"create_admin", fname:first_name, sname:second_name, level:a_level, pri_email:p_email
 				}

 			})
 			.done(function(data){
 				if(data['success']== 1){

 					$('#create_status').append('<div class="alert alert-success alert-dismissible" role="alert"><strong>Success!</strong>Administrator Succesfully Created</div>');

 				}else if(data['success'] == 0){

 					$('#create_status').append('<div class="alert alert-danger" role="alert"><strong>Success!</strong>Department Succesfully Created</div>');

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
            },p_email:{
            		validators:{
            			notEmpty:{
            				message:'You must provide the administrators primary email'
            			}
            		}
            	}
            }
 	      });
  }

 function manageDepartments(){
      $.ajax({
         url:'../../adminlogin',
         type:'post',
         dataType:'json',
         data:{
          tag:"view_dpts"
         }
      })
      .done(function(data){

        if(data['success']== 1){

           var t = data['total'];

           if (t > 0) {

            var dpt = data['dpts'];

           for (var i = 0; i < t; i++) {


                var dpt_admin = dpt[i];

                var department = dpt_admin['department'];
                var administrator = dpt_admin['administrator'];

                $('#view_dpts_form thead').append(
                  '<tr>'+
                     '<td>'+department+'</td>'+
                     '<td>'+administrator+'</td>'+
                     '<td></td>'+
                  '</tr>'
                  );

                console.log(department);
           }

          }else if (data['success'] == 0) {
               
          }
        }

      }).fail(function(data){
            console.log("failed");
      })
 }