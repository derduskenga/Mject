<%@ page import = "java.sql.Connection"%>
<%@ page import = "java.sql.DriverManager"%>
<%@ page import = "java.sql.SQLException"%>
<%@ page import = "java.sql.ResultSet"%>
<%@ page import = "java.sql.Statement"%>
<%@ page import = "com.harambesa.DBConnection.DBConnection"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ page import = "java.sql.Statement"%>
<%@ page import=" org.json.simple.JSONObject"%>
<%@ page import=" org.json.simple.JSONArray"%>

<!DOCTYPE html>
<html lang="en-us">
<head>
<meta charset="utf-8">
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
<script type="text/javascript" src="../res/js/connectionrequests.js" ></script> 


<title>HaraMbesa</title>		

</head>
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
				
			

				</div><!--main_content_middle-->
			<!--=========== content middle-->
			<!--==content right============== -->
			<div id="main_content_right" class="col-xs-3">
			Contect Right
			
			
			</div><!--=========== content right====-->
			<!--===========================================================================================-->
		</div> <!--/content-->
		<!-- footer========================== -->
		
		<%@ include file="../res/includes/footer.jsp" %>
		
		<!-- end footer========================= -->
	</div> <!-- container-fluid -->
</body>
</html>