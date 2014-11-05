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
					<div class="row left-link profile-pic" id="profile-pic-section">
								
					</div>
					<div class="row">
							<button class="btn btn-primary btn-left-link social-market-left-buttons" id="sell-points-btn">
										Sell Points
							</button>
					</div>
					<div class="row">
							<button class="btn btn-primary btn-left-link social-market-left-buttons" id="my-points-on-sale">
										My Points On Sale
							</button>
					</div>
					<div class="row">
							<button class="btn btn-primary btn-left-link active social-market-left-buttons" id="all-points-on-sale-btn">
										All Points On Sale
							</button>
					</div>
			</div><!--=============content left============-->
			<!--==content middle============== -->
			<div id="main_content_middle" class="col-xs-6">
						<div class="social-market-error" id="social-market-error"></div>
						<div class="social-points-sale-form" id="social-points-sale-form">
								
						</div>
						<div id="social-market-offers">
								
						</div>
			</div>
			<!--=========== content middle-->
			<!--==content right============== -->
			<div id="main_content_right" class="col-xs-3">
				<div class="account-summary">
					<div class="harambesa-balance">
						<div id = "balance" class="row">
							HaraMbesa Balance: 
						</div>
						<div class="row pad-top">
							<a href="../deposits" class="pad-left">Deposit</a> &nbsp;|&nbsp;<a href="../withdraw" class="pad-left">Withdraw</a>
						</div>
					</div>
					<div class="social-points">
						<div id = 'points' class="row float-middle-ver">
							Social Points: 
						</div>
						<div class="row pad-top">
							<a href="../charity-market" class="pad-left">Put On Sale</a>
						</div>
					</div>
				</div>
			</div><!--=========== content right====-->
			<!--===========================================================================================-->
		</div> <!--/content-->
		<!-- Bid Points Modal -->
				<div class="modal fade" id="points-bidding-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myModalLabel">Bid On Points</h4>
					</div>
					<div class="modal-body" id="points-bidding-modal-body">
								<div class="row">
									<div class="col-sm-6 text-right">
										Number Of Points:
									</div>
									<div class="input-group spinner col-sm-6">
										<input type="text" class="form-control" id="points-bidding-txt" value="">
										<div class="input-group-btn-vertical">
											<button class="btn btn-default pnts-calc-btn" id="points-up-btn"><i class="fa fa-caret-up"></i></button>
											<button class="btn btn-default pnts-calc-btn" id="points-down-btn"><i class="fa fa-caret-down"></i></button>
										</div>
									</div>
								</div>
								<div class="row ppp-modal-row">
									<div class="col-sm-6 text-right">
										Price Per Point:
									</div>
									<div class="input-group spinner col-sm-6">
										<input type="text" id="ppp-bidding-txt" class="form-control" value="">
										<div class="input-group-btn-vertical">
											<button class="btn btn-default pnts-calc-btn" id="ppp-up-btn"><i class="fa fa-caret-up"></i></button>
											<button class="btn btn-default pnts-calc-btn" id="ppp-down-btn"><i class="fa fa-caret-down"></i></button>
										</div>
									</div>
								</div>
								<div class="row ppp-modal-row">
									<div class="col-sm-6 text-right">
											Total:
									</div>
									<div class="col-sm-6 input-group">
											<div id="pnts-bid-calculated-total"></div>
									</div>
								</div>
					</div>
					<div class="modal-footer">
						<button type="button" id="cancel-bid-btn" class="btn btn-default" data-dismiss="modal">Cancel</button>
						<button type="button" id="place-bid-btn" class="btn btn-primary">Place Bid</button>
						<button type="button" id="back-to-bid-btn" class="btn btn-primary hidden">Go Back</button>
						<button type="button" id="proceed-bid-btn" class="btn btn-primary hidden">Proceed</button>
					</div>
					</div><!-- /.modal-content -->
				</div><!-- /.modal-dialog -->
				</div><!-- /.modal -->
		<!-- End Bid Points Modal -->
		<!-- footer========================== -->
		<%@ include file="../res/includes/footer.jsp" %>
		<!-- ===end footer========================= -->
	</div> <!-- container-fluid -->
</body>
</html>