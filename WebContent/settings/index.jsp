<!DOCTYPE html>
<html lang="en-us">
<head>
<meta charset="utf-8">
<title>HaraMbesa</title>		
	<%@ include file="../res/header_links.jsp" %>
	<link rel="stylesheet" href="settings.css">
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
					<li class="active"><a href="">Profile</a></li>
					<li id="passwordLoader"><a href="">Password Reset</a></li>
					<li id="emailNotificationsLoader"><a href="">Email Notifications</a></li>

				</ul>	 
			</div><!--=============content left============-->
			<!--==content middle============== -->
			<div id="main_content_middle" class="col-xs-6">
				<div id="errorArea"></div>
				<div class="settings_page">
					<div class="text-center">
						<img src="../res/images/loading_site_green_small.gif">
						<span> .......... Please wait as data is being loaded</span>
					</div>
				</div> 
			</div>
			<!--=========== content middle-->
			<!--==content right============== -->
			<div id="main_content_right" class="col-xs-3">
				 
			</div><!--=========== content right====-->
			<!--===========================================================================================-->
		</div> <!--/content-->
		 
		<!-- footer========================== -->
		<%@ include file="../res/includes/footer.jsp" %>
		<!-- ===end footer========================= -->
	</div> <!-- container-fluid -->
</body>
</html>