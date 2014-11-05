<script type="text/javascript" src="../res/js/messages.js" ></script> 
<!--start message-->
	<div class="modal fade" id="message-modal" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
			<form role="form" class="form-horizontal" id="message-form">
					<div class="modal-header" id="all-messages-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-times"></i></button>
							<div class="row modal-title">
									<div class="col-xs-8 modal-header-txt">
											<h4 id="all-messages-link"><a>All Messages</a><frenname id="header-name"></frenname></h4>
									</div>
									<div class="col-xs-2 new-msg-modal-icon">
										<a><div id="msg-spec-icon"><i class="fa fa-envelope-o" data-toggle="tooltip" data-placement="bottom" title="New Message" id="new-message-icon"></i></div></a>
									</div>
							</div>
					</div>
					<div class="modal-body" id="message-modal-body">
						<div id="new-message-display-area">		
								<div id="message-error"></div>
								<div class="form-group">
										<div class="col-lg-12">
													<div class="input-group">
															<span class="input-group-addon">
																<span class="">To:</span>
															</span>
															<input type="text" class="form-control input-sm" id="e7" name="to" placeholder="Name">
													</div>
										</div>
								</div>
								<div class="form-group row" id="message-text-div">
										<div class="col-lg-12">
											<textarea type="textarea" class="form-control" rows="5" name="message" id="message" placeholder="Type your message here..."></textarea>
										</div>
								</div>
						</div>
						<div id="all-messages-display-area" class="hidden">
								<div id="error-loading-messages">		
								</div>
								<div id="messages-loading-div">
										<p><img src="../res/images/loading_messages.gif" class="img-responsive center-block"></img></p>
								</div>
						</div>
					</div>
					<div class="modal-footer" id="messaging-modal-footer">
						<div id="messaging-footer-new-msg">
								<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
								<button type="submit" class="btn btn-primary">Send</button>
						</div>
				</form>
						<div id="messaging-footer-all" class="hidden">
				<form role="form" class="form-horizontal" id="message-form-all">
								<div class="form-group row" id="message-text-div">
										<div class="col-xs-9 col-xs-offset-1">
											<textarea type="textarea" class="form-control" rows="3" name="message_all" id="message_all" placeholder="Type your message here..."></textarea>
										</div>
										<div class="col-xs-2" id="messaging-footer-new-all-msg">
											<button type="submit" id="message-send-btn-all" class="btn btn-primary center-block">Send</button>
										</div>
								</div>	
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
<!--end message -->