<!DOCTYPE html>
<html lang="en-us">
<head>
<meta charset="utf-8">
<title>HaraMbesa | User Bio</title>
	<%@ include file="../res/header_links.jsp" %>
</head>
	<body>
		<div class="container-fluid">
			<%@ include file="../res/includes/header.jsp" %>
			<div id="content" class="row">
				<div id="json_test"></div>
				<!-- ==========================================================================================-->
				<!--===========================================================================================-->
				<div id="content_right" class="col-lg-6 col-lg-offset-3">
					<div class="row">	
						<!--display user bio here -->
						<div id="user-bio">
								
						</div>
						<!--End display user bio here -->
					</div>
				</div><!--=========== content_right-->
				<!-- ==========================================================================================-->
			</div> <!--/content-->
			<!-- footer========================== -->
			<div class="row navbar-fixed-bottom">
			<%@ include file="../res/includes/footer.jsp" %>
			</div>
			<!-- end footer========================= -->
		</div> <!-- container-fluid -->	
	</body>
		<script type="text/javascript" src="../res/js/commonfunctions.js" ></script>
		<script type="text/javascript" src="../res/js/user-bio.js" ></script> 
</html>