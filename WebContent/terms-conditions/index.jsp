<!DOCTYPE html>
<html lang="en-us">
<head>
<meta charset="utf-8">

<title>HaraMbesa | Terms and Conditions</title>
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
</head>
<body>
	<div class="container-fluid">
		<%@ include file="../res/includes/header.jsp" %>
		<%-- <header class="row">
			<!--<div class="col-md-2 col-md-offset-8 text-right" style="vertical-align:middle">need account?</div> -->
			<div class="col-md-4 col-md-offset-7 text-right">
				<span> <a href="../"><button type="button" class="btn btn-danger"> Register</button> </a></span>
				<span> <a href="../"><button type="button" class="btn btn-danger"> Sign In</button> </a></span>
			</div>
		</header>		 --%>
			<div class="row">
				<div class="col-md-8 col-md-offset-2" id="terms-and-conditions">	
		<!--____________________________________________________________________________-->
					<div class="text-center">
					<h3>TERMS AND CONDITIONS<h3>
					</div>
					<hr>
					<div id="tc-content">
						
					</div>
					<!-- ___________________________________________________________________________-->	
				</div>					
			</div>
			<!-- ==========================================================================================-->
		<!-- footer========================== -->
		<% 
		String footer_path ="../res/includes/footer.jsp";
		%>
		<jsp:include page="<%= footer_path %>" flush="true" />
		<!-- end footer========================= -->
	</div> <!-- container-fluid -->
</body>
</html>