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

				<div id="content_right" class="col-sm-6 col-sm-offset-3">
					<div class="row">	
						<!--display user bio here -->
						<div id="user-bio" class="col-sm-12">
								
						</div>
						<!--End display user bio here -->
						<div id="donation_offers"  class="col-sm-12">
						<!-- Donations -->
							<div class="btn-group">
							  	<button type="button" class="btn btn-info">Donations</button>
							  	<button type="button" class="btn btn-info dropdown-toggle" data-toggle="dropdown">
							    	<span class="caret"></span>
							    	<span class="sr-only">Toggle Dropdown</span>
							  	</button>
							  	<ul class="dropdown-menu" role="menu">
							    	<li id="user_donation_requests"><a href="">Requests</a></li>
							    	<li id="user_donation_contributions"><a href="">Contributions</a></li> 
							  	</ul>
							</div>

						<!-- Offers -->
							<div class="btn-group">
							  	<button type="button" class="btn btn-info">Offers</button>
							  	<button type="button" class="btn btn-info dropdown-toggle" data-toggle="dropdown">
							    	<span class="caret"></span>
							    	<span class="sr-only">Toggle Dropdown</span>
							  	</button>
							  	<ul class="dropdown-menu" role="menu">
							    	<li id="user_offer_applications"><a href="">Applications</a></li>
							    	<li id="user_offer_contribution"><a href="">Contributions</a></li> 
							  	</ul>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-sm-12" id="user_timeline">
							
						</div>
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