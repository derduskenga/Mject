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
					<li class="active"><a href="">Profile</a></li>
					<li id="user_offer_request"><a href="">My Offers</a></li> 
					<li id="user_donation_request"><a href="">My Donation Requests</a></li>
				</ul>
					 
			</div><!--=============content left============-->
			<!--==content middle============== -->
			<div id="main_content_middle" class="col-xs-8">
				<div id="errorArea"></div>
				<div id="user_timeline"></div>	
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