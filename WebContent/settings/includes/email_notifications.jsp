<div class="col-xs-12">
	<h4 class="text-center">Notify me via email when this events happen</h4>

	<form id="emailNotificationForm" method="post">
		<div id="general" class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title clearfix">General Settings 
					<span class="pull-right sign" >
						<span class="glyphicon glyphicon-plus"></span>
					</span>
				</h4>
			</div>
			<div class="panel-body">
				<div class="form-group">
					<label class="label-control col-xs-6" for="connectionRequest">Person places connection request</label> 
					<div class="checkbox">
						<label class="radio-inline">
							<input type="radio" name="connectionRequest" value=true> Yes 
						</label>
						<label class="radio-inline"> 
							<input type="radio" name="connectionRequest" value=false checked> No
						</label>
					</div> 
				</div>
			</div>
		</div>
		<!-- ============ DONATIONS =========== -->
		<div id="donations"  class="panel panel-default">
			<div class="panel-heading clearfix">
				<h4 class="panel-title">Donation Settings
					<span class="pull-right sign" >
						<span class="glyphicon glyphicon-plus"></span>
					</span>
				</h4>
			</div>
			<div class="panel-body">
				<div class="form-group">
					<label class="label-control col-xs-6" for="connectionRequiresDonation">Connection requires a donation</label> 
					<div class="checkbox">
						<label class="radio-inline">
							<input type="radio" name="connectionRequiresDonation" value=true> Yes 
						</label>
						<label class="radio-inline"> 
							<input type="radio" name="connectionRequiresDonation" value=false checked> No
						</label>
					</div> 
				</div>
				<div class="form-group">
					<label class="label-control col-xs-6" for="receivedDonation">
						Received a Donation
					</label> 
					<div class="checkbox">
						<label class="radio-inline">
							<input type="radio" name="receivedDonation" value=true> Yes 
						</label>
						<label class="radio-inline"> 
							<input type="radio" name="receivedDonation" value=false checked> No
						</label>
					</div> 
				</div>
			</div>
		</div>

		<!-- ========== OFFERS =========== -->
		<div id="offers"  class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title clearfix">Offer Settings
					<span class="pull-right sign" >
						<span class="glyphicon glyphicon-plus"></span>
					</span>
				</h4>
			</div>
			<div class="panel-body">
				<div class="form-group">
					<label class="label-control col-xs-6" for="connectionPlaceOffer">Connection place an offer</label> 
					<div class="checkbox">
						<label class="radio-inline">
							<input type="radio" name="connectionPlaceOffer" value=true> Yes 
						</label>
						<label class="radio-inline"> 
							<input type="radio" name="connectionPlaceOffer" value=false checked> No
						</label>
					</div> 
				</div>
				<div class="form-group">
					<label class="label-control col-xs-6" for="ApplicationsOnMyOffer">
						Person Applies For My Offer
					</label> 
					<div class="checkbox">
						<label class="radio-inline">
							<input type="radio" name="ApplicationsOnMyOffer" value=true> Yes 
						</label>
						<label class="radio-inline"> 
							<input type="radio" name="ApplicationsOnMyOffer" value=false checked> No
						</label>
					</div> 
				</div>
				
				<div class="form-group">
					<label class="label-control col-xs-6" for="myOfferApplicationDenied">
						MY Application On an offer is Denied
					</label> 
					<div class="checkbox">
						<label class="radio-inline">
							<input type="radio" name="myOfferApplicationDenied" value=true> Yes 
						</label>
						<label class="radio-inline"> 
							<input type="radio" name="myOfferApplicationDenied" value=false checked> No
						</label>
					</div> 
				</div>

				<div class="form-group">
					<label class="label-control col-xs-6" for="myOfferApplicationAccepted">
						My Application On An Offer Is Accepted
					</label> 
					<div class="checkbox">
						<label class="radio-inline">
							<input type="radio" name="myOfferApplicationAccepted" value=true> Yes 
						</label>
						<label class="radio-inline"> 
							<input type="radio" name="myOfferApplicationAccepted" value=false checked> No
						</label>
					</div> 
				</div>
			</div>
		</div>

		<!-- ================= POINTS ========== -->
		<div id="points"  class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">Point Settings
					<span class="pull-right sign" >
						<span class="glyphicon glyphicon-plus"></span>
					</span>
				</h4>
			</div>
			<div class="panel-body">
				<div class="form-group">
					<label class="label-control col-xs-6" for="connectionSellsPoints">Connection place points on sale</label> 
					<div class="checkbox">
						<label class="radio-inline">
							<input type="radio" name="connectionSellsPoints" value=true> Yes 
						</label>
						<label class="radio-inline"> 
							<input type="radio" name="connectionSellsPoints" value=false checked> No
						</label>
					</div> 
				</div>
				<div class="form-group">
					<label class="label-control col-xs-6" for="bidsOnMyPoints">A Bid on my point is placed</label> 
					<div class="checkbox">
						<label class="radio-inline">
							<input type="radio" name="bidsOnMyPoints" value=true> Yes 
						</label>
						<label class="radio-inline"> 
							<input type="radio" name="bidsOnMyPoints" value=false checked> No
						</label>
					</div> 
				</div>
				<div class="form-group">
					<label class="label-control col-xs-6" for="buyerAcceptsBid">Buyer accepts bid</label> 
					<div class="checkbox">
						<label class="radio-inline">
							<input type="radio" name="buyerAcceptsBid" value=true> Yes 
						</label>
						<label class="radio-inline"> 
							<input type="radio" name="buyerAcceptsBid" value=false checked> No
						</label>
					</div> 
				</div>
				<div class="form-group">
					<label class="label-control col-xs-6" for="purchasesOnMyPoints">My points are bought</label> 
					<div class="checkbox">
						<label class="radio-inline">
							<input type="radio" name="purchasesOnMyPoints" value=true> Yes 
						</label>
						<label class="radio-inline"> 
							<input type="radio" name="purchasesOnMyPoints" value=false checked> No
						</label>
					</div> 
				</div>
			</div>

		</div> 
		<div class="form-group"> 
			<div class="col-xs-3 col-xs-offset-7">
				<button class="btn btn-default" type="submit">Save</button>
			</div> 
		</div> 
	</form>
</div>