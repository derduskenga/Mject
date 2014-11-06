<!DOCTYPE html>
<html lang="en-us">
<head>
<meta charset="utf-8">
<title>HaraMbesa</title>		
	<%@ include file="../res/header_links.jsp" %>
</head>
<body>
	<div class="container-fluid">
		<%@ include file="../res/includes/header.jsp" %>
		<!--==New Request==-->
		<!--====-->
		<div id="content" class="row">
			<!--==content left============== -->
			<div id="main_content_left" class="col-xs-2 col-xs-offset-1">
				<ul class="nav nav-pills nav-stacked">
					<li class="active" id="newUsers"><a href="">New Users</a></li>
					<li id="registeredUsers"><a href="">Registered Users</a></li>
					<li id="activeUsers"><a href="">Active Users</a></li>
					<li id="findUsers"><a href="">Find Users</a></li>
				</ul>	
					 
			</div><!--=============content left============-->
			<!--==content middle============== -->
			<div id="main_content_middle" class="col-xs-8">
				<div id="errorArea"></div>
				<div id="selectMonth" class="row"></div> 
				<div class="app_users"></div>

			</div>
			<!--=========== content middle-->
			<!--==content right============== -->
			<div id="main_content_right" class="col-xs-1">
				 
			</div><!--=========== content right====-->
			<!--===========================================================================================-->
		</div> <!--/content-->
		 
		<!-- footer========================== -->
		<%@ include file="../res/includes/footer.jsp" %>
		<!-- ===end footer========================= -->
	</div> <!-- container-fluid -->
</body>
</html>