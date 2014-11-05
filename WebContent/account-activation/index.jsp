<%@ page import=" javax.servlet.http.HttpSession"%>

<%
HttpSession sess=request.getSession();

String conf_msg = ((String)sess.getAttribute("conf_msg"));
String conf_title= ((String)sess.getAttribute("conf_title"));
if(conf_msg==null || conf_title==null)
	response.sendRedirect("../error");
else{
%>

<!DOCTYPE html>
<html lang="en-us">
<head>
<meta charset="utf-8">
<!--<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">-->

<title>HaraMbesa</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<link rel="stylesheet" type="text/css" media="screen" href="../res/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="../res/jasny-bootstrap/css/jasny-bootstrap.min.css"></link>
<link rel="stylesheet" type="text/css" media="screen" href="../res/bootstrap-datepicker/css/datepicker.css"></link>
<link href="../res/select/select2.css" rel="stylesheet"/>
<link rel="stylesheet" type="text/css" media="screen" href="../res/css/styleX.css">


<script type="text/javascript" src="../res/js/jquery.js"></script>
<script type="text/javascript" src="../res/bootstrap/js/bootstrap.min.js"></script> 
<script type="text/javascript" src="../res/bootstrap-validator/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src="../res/jasny-bootstrap/js/jasny-bootstrap.min.js"></script>
<script type="text/javascript" src="../res/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
<script type="text/javascript"  src="../res/select/select2.js"></script>
<script type="text/javascript" src="../res/js/landing.js" ></script> 

<!-- GOOGLE FONT -->
<!-- <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Open+Sans:400italic,700italic,300,400,700"> -->

</head>
<body>

	<div class="container-fluid confirmation-pg">
		<header class="row">
			<!--<div class="col-md-2 col-md-offset-8 text-right" style="vertical-align:middle">need account?</div> -->
			<div class="col-md-4 col-md-offset-7 text-right">				
				<span><a href="logon3.jsp"><button type="button" class="btn btn-danger"> SIGN IN</button> </a></span>
			</div>
		</header>
		<div class="row text-center">
					<img src="../res/images/logo.png">
		</div>
		<div class ="im-centered">
		<div class="row">
			<p><h3><%= conf_title %></h3></p>
			<p><h5><%= conf_msg %></h5></p>			
		</div>
		</div>
				<!-- footer========================== -->
		<div class="navbar-fixed-bottom">
			<%@ include file="../res/includes/footer.jsp" %>
		</div>
		<!-- end footer========================= -->
	</div> <!-- container-fluid -->
</body>
</html>
<% 
} %>