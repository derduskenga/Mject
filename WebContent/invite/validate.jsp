<%@ page import = "java.sql.Connection"%>
<%@ page import = "java.sql.DriverManager"%>
<%@ page import = "java.sql.SQLException"%>
<%@ page import = "java.sql.Statement"%>
<%-- <%@ page import="java.io.BufferedReader"%>
<%@ page import ="java.io.DataOutputStream"%>
<%@ page import ="java.io.InputStreamReader"%>
<%@ page import = "java.net.URLEncoder"%>
<%@ page import ="java.net.URLConnection"%>
<%@ page import="com.google.gdata.data.contacts.*"%> --%>
<%@ page import="java.util.*"%>
<%@ page import ="java.io.OutputStreamWriter"%>
<%@ page import ="java.net.URL"%>
<%@ page import = "javax.net.ssl.HttpsURLConnection"%>
<%@ page import = "com.harambesa.DBConnection.DBConnection"%>
<%@ page import = "com.harambesa.Utility.AccessCodeRequester"%>
<%@ page import = "com.harambesa.Utility.ContactReader"%>
<%@ page import = "com.harambesa.Utility.Userdata"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<html lang="en-us">
<head>
<meta charset="utf-8">

<title>HaraMbesa</title>

    <meta name="description" content="">
    <meta name="author" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <link rel="stylesheet" type="text/css" media="screen" href="../res/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" media="screen" href="../res/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" media="screen" href="../res/css/styleX.css">
    <script type="text/javascript" src="../res/js/jquery.js"></script>
    <script type="text/javascript" src="../res/bootstrap/js/bootstrap.min.js"></script> 
    <script type="text/javascript" src="../res/bootstrap-validator/js/bootstrapValidator.min.js"></script>
    <script type="text/javascript" src="../res/js/res.js" ></script> 
    
<%--      <link rel="stylesheet" type="text/css" href="../res/datatables/bootstrap.min.css">  --%>
<%--      <script type="text/javascript" language="javascript" src="../res/datatables/jquery-1.10.2.min.js"></script>  --%>
    <script type="text/javascript" language="javascript" src="../res/datatables/jquery.dataTables.min.js"></script>
    <script type="text/javascript" language="javascript" src="../res/datatables/jquery-DT-pagination.js"></script>
    
    
    <script type="text/javascript">
        function toggle(source) {

            checkboxes = document.getElementsByName('contactCheckbox');
 
            for(var i=0; i<checkboxes.length;i++) {
                checkboxes[i].checked = source.checked;
            }
        }
    </script>
    
    		<script type="text/javascript">
		/* Table initialisation */
		$(document).ready(function() {
			$('#contactTable').dataTable( {
			        "bSort": false,      // Disable sorting
				"iDisplayLength": 15,   //records per page
				 	"sDom": "t<'row'<'col-sm-3'i><'col-sm-3'p>>",
					"sPaginationType": "bootstrap"
				} );
			} );
		</script>
		
		<style>
		.pagination {
   		    margin:0 ! important;
	}</style>
	
	
	
<script type="text/javascript">

    $(document).ready(function(){

	$("#send_mail_imported_mail").click(function(){
	
	 
	   
	    
	  var final = '';
	  $('#contactCheckbox:checked').each(function(){        
	      var values = $(this).val();
	      final += values;
	  });
	  
	 /* http://192.168.3.140:8080/mjet/mapping/mapping.js,*/
	  
	   if(final.length != 0) {
		 alert(final);
		 $('#send_mail_imported_mail').text('Inviting...');
		 $.post(" http://192.168.3.140:8080/mjet/mapping/mapping.js", {tag:"email_imported",emailsString:final},function(data){
			  
			  if(data['success']=='1'){
				$('#imported_mail_response_message').html("<div id='success-alert-for-mail-sent' class='alert alert-success'>" + data['message'] + " </div>");  
				$('#send_mail_imported_mail').text('Invite');
			  }else{
				$('#imported_mail_response_message').html("<div id='fail-alert-for-sent-mail' class='alert alert-danger'>" + data['message'] + "</div>");
				$('#send_mail_imported_mail').text('Invite');
			  }
		 }, 'JSON');
	   
	   }else{
		  $('#imported_mail_response_message').html("<div id='no-selected-email-contact' class='alert alert-warning'>No email contact(s) is selected !</div>");
		  $('#send_mail_imported_mail').text('Invite');
	   
	   }
	   

	});

    });  

</script>	

</head>

<body>
	
  <body>
	<div class="container-fluid">
		<%@ include file="../res/includes/header.jsp" %>
		<!--==New Request==-->

		<!--====-->
		<div id="content" class="row">
			<!--==content left============== -->
			<div id="main_content_left" class="col-xs-2 col-xs-offset-1">
	
			</div><!--=============content left============-->
			
			
			<!--==content middle============== -->
			<div id="main_content_middle" class="col-xs-6">
                <%
                	 AccessCodeRequester AccessCodeRequesterObj = new AccessCodeRequester(request.getParameter("code"));
			 String jsonString = AccessCodeRequesterObj.requestToken();
			//List<Userdata> contacts= new ArrayList<Userdata>();
		      //out.println(jsonString);
	 
	 if(jsonString.length()!=0){
		
		AccessCodeRequesterObj.writeToFile(jsonString);
		AccessCodeRequesterObj.readFromFileParser();
		out.println("<br>");
		//out.println("1 " + AccessCodeRequesterObj.getAccessToken() + "3 " + AccessCodeRequesterObj.getAccessType());
		//ContactReader object and pass it the access token
		ContactReader contactReader = new ContactReader(AccessCodeRequesterObj.getAccessToken());
		
		String groupId = contactReader.getIdOfMyGroup();
		HashMap <String, String>myMapL = new HashMap<String, String>();
		myMapL = contactReader.getContacts(groupId);
		Set set = myMapL.entrySet();
		Iterator i = set.iterator();
		%>
		
		You can send inviation mail for your selected contacts below
		 
		 <div id="table_section">
			<table class="table table-striped table-bordered table-condensed zebra-striped" id="contactTable">
			  
			  <thead>
				<th><b><input type="checkbox" onClick="toggle(this)">Select all</b></th>  
				<th><b>Name</b></th>
				<th><b>Email</b></th>
			    </tr>
			  </thead>
			  <tbody>
			    <% while(i.hasNext()) {
				Map.Entry me = (Map.Entry)i.next();%>
			    <tr>
				  <td><input type="checkbox" name="contactCheckbox" id="contactCheckbox" value="<%=me.getValue()+"|"+me.getKey()%>"></td>
				  <td><%out.println(me.getKey());%></td>
				  <td><%out.println(me.getValue());%></td>

			    </tr>
			    <% }%>

			<tbody>
			</table>
                
                </div>
                
	      <div id="imported_mail_response_message">
		      
	      </div>
	      
	      <div id="button-area">
		      <button type="button" class="btn btn-default" onclick="window.close()" id="close-new-invite-window">Close</button>
		      <button type="button" class="btn btn-primary" id="send_mail_imported_mail">Invite</button>
	      </div>
	
	<%
	
	///send email here
	  
	 }else{
	    out.println("This is quite embrassing! Error occured");
	 
	 }%>
			
			</div>
			<!--=========== content middle-->
			
			
			<!--==content right============== -->
			<div id="main_content_right" class="col-xs-3">
			</div><!--=========== content right-->
		</div> <!--/content-->
		<!-- footer========================== -->
		
		<%@ include file="../res/includes/footer.jsp" %>
		
		<!-- end footer========================= -->
	</div> <!-- container-fluid -->
	
	    </div>
	</div>
     
</body>
	
</body>
</html>