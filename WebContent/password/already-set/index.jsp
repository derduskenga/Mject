<!DOCTYPE html>
<html lang="en-us">
<head>
<meta charset="utf-8">

<title>HaraMbesa</title>		

<meta name="description" content="">
<meta name="author" content="">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<link rel="stylesheet" type="text/css" media="screen" href="../../res/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="../../res/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="../../res/css/styleX.css">

</head>
<body>
	<div class="container-fluid">

		<header class="row">
			<!--<div class="col-md-2 col-md-offset-8 text-right" style="vertical-align:middle">need account?</div> -->
			<div class="col-md-4 col-md-offset-7 text-right">				
				<span> You have already set your password <a href="../../ADMIN/"><button type="button" class="btn btn-danger"> SIGN IN</button> </a></span>
			</div>
		</header>		
		
		<div id="content" class="row">
			<div id="json_test"></div>
			<!-- ==========================================================================================-->
			<!--===========================================================================================-->
			<div id="content_right" class="col-lg-6 col-lg-offset-3">
				<div class="row">
					<div class="col-md-11 col-md-offset-1 text-center" id="password-reset-panel-">	
						<!--____________________________________________________________________________-->
						<i class="fa fa-frown-o text-danger" style="font-size:50px; "></i><br />
						<span class="error_span">Ooops! Sorry! <br/>You had already set your password.</span>
						<span class="error_span"><a href="../../ADMIN/">Sign in</a>.</span>
						
						<!-- ___________________________________________________________________________-->	
					</div>					
				</div>
			</div><!--=========== content_right-->
			<!-- ==========================================================================================-->
		
		</div> <!--/content-->
		<!-- footer========================== -->
		<div class="navbar-fixed-bottom">
		<%@ include file="../../res/includes/footer.jsp" %>
		</div>
		<!-- end footer========================= -->
	</div> <!-- container-fluid -->
</body>
</html>