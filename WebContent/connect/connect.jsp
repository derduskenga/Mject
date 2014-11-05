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
			
			
					<div class="row left-link profile-pic">
					<a href=""><img src="../res/images/makau.jpg" alt="Create Tithing"></a>
				</div>
				<div class="row left-link">
					<a href=""><img src="../res/images/create.png" alt="Create Tithing"><imglinktxt>Create Tithing</imglinktxt></a>
				</div>
				<div class="row left-link">
					<a href=""><img src="../res/images/create.png" alt="Create Merry-Go-Rich"><imglinktxt>Create Merry-Go-Rich</imglinktxt></a>
				</div>
				
	
			</div><!--=============content left============-->
			<!--==content middle============== -->
				<div id="main_content_middle" class="col-xs-6">
					
				<!--========================================================= -->
			<%-- 		<div class="row connection_reqiest"><!--row connection_reqiest-->
						
							<div class="connect-pic col-xs-2">
									<img alt="connect-pics" src="../res/images/makau_post_pic3.jpg">
							</div>
							
							<div class="col-xs-4 details  person-details"><!--col-xs-2 post-pic-->
									<a class ="connector-name" href="#">Shirin Kangogo Suter</a>
									E-mail:kipnight
									Company:HaraMbesa
							</div><!--col-xs-2 post-pic-->
							
							<div class="btn-group"><!--btn-group-->
									<button class="btn btn-primary connect-button col-md-3" data-target="#myModal" data-toggle="modal">Connect</button>
									<button class="btn btn-primary connect-button col-md-3" data-target="#myModal" data-toggle="modal">Message</button>
									<button class="btn btn-primary disconect-button col-md-3" data-target="#myModal" data-toggle="modal">Ignore</button>
									</div><!--btn-group-->
					</div><!--row connection_reqiest--> --%>
<!--========================================================= -->



<% for(int i = 0; i < 11; i+=1) { %>
           <div class="row connection_reqiest"><!--row connection_reqiest-->
						
							<div class="connect-pic col-xs-2">
									<img alt="connect-pics" src="../res/images/makau_post_pic3.jpg">
							</div>
							
							<div class="col-xs-4 details  person-details"><!--col-xs-2 post-pic-->
									<a class ="connector-name" href="#">Shirin Kangogo Suter</a>
									E-mail:kipnight
									Company:HaraMbesa
							</div><!--col-xs-2 post-pic-->
							
							<div class="btn-group"><!--btn-group-->
									<button class="btn btn-primary connect-button col-md-3" data-target="#myModal" data-toggle="modal">Connect</button>
									<button class="btn btn-primary connect-button col-md-3" data-target="#myModal" data-toggle="modal">Message</button>
									<button class="btn btn-primary disconect-button col-md-3" data-target="#myModal" data-toggle="modal">Ignore</button>
									</div><!--btn-group-->
					</div><!--row connection_reqiest-->
        <% } %>

<!--========================================================= -->



				</div><!--main_content_middle-->
			<!--=========== content middle-->
			<!--==content right============== -->
			<div id="main_content_right" class="col-xs-3">
			Contect Right
			
			
          <div class="panel-heading text-center">
            <h3 class="panel-title">Account Summery</h3>
          </div>
          <div class="panel-body">
          
          		<ul class="list-group">
                  <li class="list-group-item"><span class="badge">Ksh 340,000</span>Account Balance</li>
                  <li class="list-group-item"><span class="badge" id="stat_prod">5444</span>Charity Points</li>
                  
                </ul>       
            
          </div><!--/panel body-->
        
			</div><!--=========== content right====-->
			<!--===========================================================================================-->
		</div> <!--/content-->
		<!-- footer========================== -->
		
		<%@ include file="../res/includes/footer.jsp" %>
		
		<!-- end footer========================= -->
	</div> <!-- container-fluid -->
</body>
</html>