 $(function(){//page ready loader i.e loads the page when ready
   $('#profile_loader').click(function(e){
        e.preventDefault();//prevents the defaul loaded page 
        $(this).addClass('active').siblings().removeClass('active');
        $('#profile').show(500);//div to be loaded'id and the sho spee
        insertProfile();

   });
});

function insertProfile(){
    $('#profile_form').submit(function(e){
            e.preventDefault();
             var Gender = $('#gender').val();
             var Country = $('#countrys').val();
             var Phoneno = $('phoneno').val();
             var Occupation = $('#occupation').val();
             var City = $('#city').val();
             $.post('../editProfileServlet',{  gender:Gender,country:Country, phoneno:Phoneno, occupation:Occupation, city:City}, 
                function(data){
                    //console.log(data);
                    $('.settings_page').html(data.message);//fetches the server feed back and displays on the browser
                },'JSON'
                );

        }
    );
   // alert(GENDER );
}


 // function insertProfile(){
 //         message : 'Your profile has been posted succesfully'
 //        submitHandler: function(validator, form, submitButton) {
 //            var GENDER=$("#gender").val();
 //            var COUNTRYS=$("#countrys").val();
 //            var PHONENO=$("#phoneno").val();
 //            var OCCUPATION=$("#occupation").val();
 //            var CITY=$("#city").val();
            
 //           //$.post('../request_handler',
 //                {
 //                    tag:"enter_profile", gender: GENDER, countrys: COUNTRYS,phonenumber: PHONENO,occupation: OCCUPATION,city: CITY
 //                }, function(result) {
 //                alert(result);
 //                // if(result['success'] == '1'){
 //                //  $('#form_panel').html(result['message']);
 //                //  $('#logon').removeClass('hidden');
 //                //  $('#submit-btn').addClass('hidden');
 //                //  $('#sign-up-error').addClass('hidden');
 //                // }else if(result['success'] == '0'){
 //                //  $('#sign-up-error').html(result['message']);
 //                //  $('#sign-up-error').removeClass('hidden');
 //                // }
                
 //            }, 'JSON');
 //        }
 // }