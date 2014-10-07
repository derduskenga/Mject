var clickedBtnId = null;
$(document).ready(function(e){
	
	$('#sell-points-btn').click(function(e){
			showSellPointsForm();
	});
	
	$('#my-points-on-sale').click(function(e){
			showMyPointsOnSale($(this));
	 });
	
	if($('.social-market-left-buttons') != null){
		$('.social-market-left-buttons').click(function(e){
				changeColor($(this));
		});	
		
		$('#all-points-on-sale-btn').click(function(e){
				loadPointSaleOffers(0);
		 });
	}
	
	if($('#social-market-offers') != null){
						bindSpinnerButtons();
						loadPointSaleOffers(0);
	}
	
	$('.pnts-calc-btn').click(function(e){
			//calculatePointsBidTotal();
	});
	
	$('#place-bid-btn').click(function(e){
			placeBidOnPoints();
	 });
});

function placeBidOnPoints(){
		var points = $('#points-bidding-txt').val();
		var ppp  = $('#ppp-bidding-txt').val();
		var total =parseFloat(Math.round((points * ppp) * 100) / 100).toFixed(2);
		showConfirmBidOnPoints(points, ppp, total);
}

function showConfirmBidOnPoints(points, ppp, total){
		var bidding_html  = $('#points-bidding-modal-body').html();
		$('#back-to-bid-btn').click(function(e){
					$('#points-bidding-modal-body').html(bidding_html);
					$('#points-bidding-txt').val(points);
					$('#ppp-bidding-txt').val(ppp);
					$('#cancel-bid-btn').text('Cancel')
					changeBidButtons();
					bindSpinnerButtons();
		 });
		$('#cancel-bid-btn').click(function(e){
					$('#points-bidding-modal-body').html(bidding_html);
					changeBidButtons();
		 });
		$('#place-bid-btn').addClass('hidden');
		$('#back-to-bid-btn').removeClass('hidden'); 
		$('#proceed-bid-btn').removeClass('hidden'); 
		$('#points-bidding-modal-body').html('<div class="bg-info">You are about to bid '+points+' points at the rate of '+ppp+' (KES) for a total of '+total+' (KES).</div>');
		$('#proceed-bid-btn').off('click');
		$('#proceed-bid-btn').click(function(e){
		$('#points-bidding-modal-body').html('<div class="center-block"><img alt="Placing bid..." class="center-block" src="../res/images/loading_site_green_small.gif"></div>');
			var newDataRequest = $.ajax({
						type: "POST",
						url: "../socialmarket",
 						timeout: 30000, // timeout after 30 seconds
						dataType: "json", //JSON
						data: { tag:'placebidonpoints', pnts: points, ppp: ppp, psid: clickedBtnId}
					});	
					newDataRequest.done(function(data){
						if(data["success"]==0){
									if(data["redir"]){
										showSessExpired();
									}else{
										$('#points-bidding-modal-body').html('<div class="center-block bg-danger text-center">'+data["message"]+'</div>');
									}
						}else if(data["success"]==1){
										$('#points-bidding-modal-body').html('<div class="center-block bg-success text-center">'+data["message"]+'</div>');
										$('#place-bid-btn').addClass('hidden');
										$('#back-to-bid-btn').text('Edit Bid');
										$('#proceed-bid-btn').addClass('hidden');
										$('#cancel-bid-btn').text('Finish');
						}
					});
					newDataRequest.fail(function(jqXHR, exception){
						if (jqXHR.status === 0){
							alert('Sorry, could not establishing a network connection.');
						}else if (jqXHR.status == 404){
							alert('Requested page not found. [404]');
						}else if (jqXHR.status == 500){
							alert('Internal Server Error [500].');
						}else if (exception === 'parsererror'){
							alert('Requested JSON parse failed.');
						}else if (exception === 'timeout'){
							alert('Time out error.');
						}else if (exception === 'abort'){
							alert('Sorry, Request was aborted.');
						}else{
							alert('Sorry, an error occured.');
						}
					});
		});
}

function changeBidButtons(){
		$('#place-bid-btn').removeClass('hidden');
		$('#back-to-bid-btn').addClass('hidden'); 
		$('#proceed-bid-btn').addClass('hidden'); 
}

function calculatePointsBidTotal(){
		var points = $('#points-bidding-txt').val();
		var ppp  = $('#ppp-bidding-txt').val();
		var total = parseFloat(Math.round((points * ppp) * 100) / 100).toFixed(2);
		$('#pnts-bid-calculated-total').text(total +"(KES)");
}
//placebidonpoints
//place-bid-btn
function bindSpinnerButtons(){
	 	
	$('#points-up-btn').click(function(e){
				if(isNaN($('#points-bidding-txt').val()) || $('#points-bidding-txt').val()==""){
					$('#points-bidding-txt').val(0);
				}
				if( $('#pnts-'+clickedBtnId).html()>$('#points-bidding-txt').val()){
					 $('#points-bidding-txt').val(parseInt($('#points-bidding-txt').val()) + 1);
					 calculatePointsBidTotal();
				}
		});
		$('#points-down-btn').click(function(e){
			if(isNaN($('#points-bidding-txt').val()) || $('#points-bidding-txt').val()=="" ){
					$('#points-bidding-txt').val(1);
				}	
			if($('#points-bidding-txt').val()<2){
				
			}else{
			$('#points-bidding-txt').val(parseInt($('#points-bidding-txt').val()) - 1);
			calculatePointsBidTotal();
			}
		});
		$('#ppp-up-btn').click(function(e){
				if(isNaN($('#ppp-bidding-txt').val()) || $('#ppp-bidding-txt').val()==""){
					$('#ppp-bidding-txt').val(0.01);
				}
				$('#ppp-bidding-txt').val(parseFloat(Math.round((parseFloat($('#ppp-bidding-txt').val()) + 0.01) * 100) / 100).toFixed(2));
				calculatePointsBidTotal();
		});
		$('#ppp-down-btn').click(function(e){
				if(isNaN($('#ppp-bidding-txt').val()) || $('#ppp-bidding-txt').val()==""){
					$('#ppp-bidding-txt').val(0.01);
				}
			if($('#ppp-bidding-txt').val()<0.02){
				
			}else{
			$('#ppp-bidding-txt').val(parseFloat(Math.round((parseFloat($('#ppp-bidding-txt').val()) - 0.01) * 100) / 100).toFixed(2));
			calculatePointsBidTotal();
			}
		});
}

function showMyPointsOnSale(btnObj){
	$.post("../socialmarket/", {tag : "mypointsonsale"},function(data){
					
					if(data['success']=='0'){
						if(data['redir'] != null)
							showSessExpired();
						else
							appendErrorMessage(data['message']);
					}
					else if(data['success']==1){
						if(data['total'] < 1){
							$('#social-market-error').html("");
							$("#social-points-sale-form").html("");
							$('#social-market-offers').html('<div class="bg-warning text-center no-points-on-sale-div">You do not have any points on sale at the moment.</div>');
								applyNoPointsSaleDivCss();
						}else{
							var all_sales = data['psale_details'];	
							$('#social-market-offers').html("");
							$('#social-market-error').html("");
							$('.no-points-on-sale-div').remove();
							$("#social-points-sale-form").html("");
							displayMyPointsOnSale(all_sales);
						}
					}
			});
}

function applyNoPointsSaleDivCss(){
	$(".no-points-on-sale-div").css({
			"padding-top":"10px",
			"padding-bottom":"10px"
	});
}

function loadPointSaleOffers(startpoint){
			$.post("../socialmarket/", {tag : "fetchall"},function(data){
					if(data['redir'] != null)
							showSessExpired();
					else if(data['success']=='0'){
							appendErrorMessage(data['message']);
					}
					else if(data['success']==1){
						if(startpoint==0){
							$('#social-market-offers').html("");
							$('#social-market-error').html("");
						}
						if(data['psale_details'] == null){
							$('.no-points-on-sale-div').remove();
							$("#social-points-sale-form").html("");
							$('#social-market-error').html("");
							$('#social-market-offers').html(data['message']);
						}else{
							var all_sales = data['psale_details'];
							$('.no-points-on-sale-div').remove();
							$("#social-points-sale-form").html("");
							displayPointsOnSale(all_sales);
						}
					}
			});
}

function showSellPointsForm(){
	$( '#social-points-sale-form').load('../res/extras/social_points_sales_form.jsp');
	$('.no-points-on-sale-div').remove();
	 setTimeout(activateBValOnSellPoints, 1000);
	 activateBValOnSellPoints();
	 setTimeout(bindCancelBtn, 1000);
	 setTimeout(bindAllPointsMkt, 1000);
}

function bindCancelBtn(){
	$('#cancel-points-sale-btn').click(function(e){
		$('#social-points-sale-form').html("");
	});
}

function bindAllPointsMkt(){
	$('#all-points-on-sale-btn').click(function(e){
			$('#social-points-sale-form').html("");
			loadPointSaleOffers(0);
	});
}

function showSessExpired(redir){
		BootstrapDialog.show({
					title: 'Session Expired',
					type: BootstrapDialog.TYPE_DEFAULT,
					message: 'Sorry, Please login to continue.',
					buttons: [{ 
						label: 'OK',
						cssClass: 'btn-default', 
						autospin: false,
						action: function(dialogRef){    
							dialogRef.close();
							window.location.href="../login";	
						}
				 }]
		});	
}

function appendErrorMessage(message){
		$('#social-market-error').html('<div class="text-warning">'+message+'</div>');
}

function displayPointsOnSale(on_sale){
		var i = on_sale.length;
		for(var k=0; k<i; k++ ){
					var div='<div class="sales-post row"><div 	class="owner-photo col-md-2"><img class="img-responsive" alt="" src="../profilepic/'+on_sale[k]['pic']+'"></div><div class="col-md-8"><div class="row"><div class="owner-label col-md-4">Owner:</div><div class="owner-name col-md-8">'+on_sale[k]['name']+'</div></div><div class="row"><div class="points-label col-md-4">Number of Points:</div><div class="no-of-points col-md-8" id="pnts-'+on_sale[k]['psid']+'">'+on_sale[k]['nop']+'</div></div><div class="row"><div class="price-points-label col-md-4">Price Per Point:</div><div class="no-of-points col-md-8" id="ppp-'+on_sale[k]['psid']+'">'+on_sale[k]['ppp']+'</div></div><div class="row"><div class="total-p-points-label col-md-4">Total:</div><div class="total-p-points col-md-8">'+parseFloat(Math.round((on_sale[k]['nop']*on_sale[k]['ppp']) * 100) / 100).toFixed(2)+' (KES) </div></div><div class="row"><div class="points-sale-actions row" id="'+on_sale[k]['psid']+'">';
				
				if(on_sale[k]['placed_bid']==0 || on_sale[k]['placed_bid']==1 && on_sale[k]['b_acc']==true){
					div +='<button type="button" id="buybtn-'+on_sale[k]['psid']+'" class="btn btn-default buy-btn col-md-3" data-toggle="tooltip" data-placement="bottom" title="(All points at the current price)">Buy</button><div data-toggle="modal" data-target="#points-bidding-modal" data-toggle="tooltip"><button type="button" class="btn btn-default bid-btn col-md-3 text-left" id="bidbtn-'+on_sale[k]['psid']+'" data-placement="bottom" title="(Suggest number of points and price per point)">Bid</button>';
				}else{
				   div +='<button type="button" id="buybtn-'+on_sale[k]['psid']+'" class="btn btn-default buy-btn col-md-3" data-toggle="tooltip" data-placement="bottom" title="(All points at the current price)">Buy</button><div data-toggle="modal" data-target="#points-bidding-modal" data-toggle="tooltip"><button type="button" class="btn btn-default bid-btn col-md-3 text-left" bppp="'+on_sale[k]['bppp']+'" bnop="'+on_sale[k]['bnop']+'" id="bidbtn-'+on_sale[k]['psid']+'" data-placement="bottom" title="(Your current bid is '+on_sale[k]['bnop']+' points @ '+on_sale[k]['bppp']+'(KES). Total='+multiplyAmt(on_sale[k]['bnop'],on_sale[k]['bppp'])+' )">Rebid</button></div><div><button type="button" class="btn btn-default delbid-btn col-md-3 text-left"  id="delbidbtn-'+on_sale[k]['psid']+'" data-placement="bottom" title="(Delete your bid for '+on_sale[k]['bnop']+' points @ '+on_sale[k]['bppp']+' (KES). Total='+multiplyAmt(on_sale[k]['bnop'],on_sale[k]['bppp'])+'(KES) )">Delete Bid</button>';
				}
				div+='</div></div></div></div></div>';
				$('#social-market-offers').append(div);
		}
		$('.buy-btn').tooltip();
		$('.bid-btn').tooltip();
		$('.delbid-btn').tooltip();
		activateButtonsAsLinks();
}
				
function multiplyAmt(amt,rate){
			var total = parseFloat(Math.round((amt*rate) * 100) / 100).toFixed(2);
			console.log("Total:"+total);
			return total;
}

function bidOnThesePoints(btn){
			var id = btn.attr("id").split("-");
			var pnts= $('#pnts-'+id[1]).html();
			var ppp = $('#ppp-'+id[1]).html()
			clickedBtnId=id[1];
			$('#points-bidding-txt').val(pnts);
			$('#ppp-bidding-txt').val(ppp);
			$('#pnts-bid-calculated-total').text(parseFloat(Math.round((pnts*ppp) * 100) / 100).toFixed(2) +" (KES)");
}

function activateButtonsAsLinks(){
		$('.buy-btn').click(function(e){
					buyThesePoints($(this), $(this).attr("bnop"), $(this).attr("bppp"));
		});
		$('.bid-btn').click(function(e){
				bidOnThesePoints($(this));
		});
		$('.delbid-btn').click(function(e){
				deleteThisBid($(this));
		});
}

function deleteThisBid(btn,bnop,bppp){
		var buyPointsDialog = new BootstrapDialog({
								title: 'Buy Points',
								type: BootstrapDialog.TYPE_DEFAULT,
								message: '<div class="center-block text-center">Are you sure you want to cancel your bid?</div>',
								buttons: [{ 
									label: 'Cancel',
									cssClass: 'btn-default', 
									autospin: false,
									action: function(dialogRef){    
										dialogRef.close();
									}
									},{
									label: 'Proceed',
									cssClass: 'btn-primary', 
									autospin: false,
									action: function(dialogRef){    
								var id = btn.parent().parent().attr('id');
								var newDataRequest = $.ajax({
									type: "POST",
									url: "../socialmarket",
									timeout: 30000, // timeout after 30 seconds
									dataType: "json", //JSON
									data: { tag:'cancelmybid', bid:id }
								});	
								newDataRequest.done(function(data){
														if(data["success"]==0){
															if(data["redir"]){
																showSessExpired();
															}else{
																alert(data['message']);
															}
														}else if(data["success"]==1){
															buyPointsDialog.setMessage(data["message"]);
															buyPointsDialog.setButtons([{
																label: 'Ok',
																cssClass: 'btn-default',
																action: function(dialogItself) {
																	dialogItself.close();
																	$('#delbidbtn-'+id).remove();
																	$('#bidbtn-'+id).text("Bid");
																	$('.bid-btn').removeAttr("title");
																	//$('#bidbtn-'+id).attr("title","(Suggest number of points and price per point)");
																	$('#bidbtn-'+id).tooltip('hide')
																					.attr('data-original-title', "(Suggest number of points and price per point)")
																					.tooltip('fixTitle');
																}
															}]);
														}
													});
													newDataRequest.fail(function(jqXHR, exception){
														if (jqXHR.status === 0){
															alert('Sorry, could not establishing a network connection.');
														}else if (jqXHR.status == 404){
															alert('Requested page not found. [404]');
														}else if (jqXHR.status == 500){
															alert('Internal Server Error [500].');
														}else if (exception === 'parsererror'){
															alert('Requested JSON parse failed.');
														}else if (exception === 'timeout'){
															alert('Time out error.');
														}else if (exception === 'abort'){
															alert('Sorry, Request was aborted.');
														}else{
															alert('Sorry, an error occured.');
														}
													});
											newDataRequest.fail(function(jqXHR, exception){
												if (jqXHR.status === 0){
													alert('Sorry, could not establishing a network connection.');
												}else if (jqXHR.status == 404){
													alert('Requested page not found. [404]');
												}else if (jqXHR.status == 500){
													alert('Internal Server Error [500].');
												}else if (exception === 'parsererror'){
													alert('Requested JSON parse failed.');
												}else if (exception === 'timeout'){
													alert('Time out error.');
												}else if (exception === 'abort'){
													alert('Sorry, Request was aborted.');
												}else{
													alert('Sorry, an error occured.');
												}
											});
							}
						}]
					});	
					buyPointsDialog.open();
					
}

function changeColor(btn){
		$('.social-market-left-buttons').removeClass('active');
		btn.addClass('active');
}

function buyThesePoints(btn){
					var buyPointsDialog = new BootstrapDialog({
								title: 'Buy Points',
								type: BootstrapDialog.TYPE_DEFAULT,
								message: '<div class="center-block"><img alt="Buying Points..." class="center-block" src="../res/images/loading_site_green_small.gif"></div>',
								buttons: [{ 
									label: 'Cancel',
									cssClass: 'btn-default', 
									autospin: false,
									action: function(dialogRef){    
										dialogRef.close();
									}
								}]
					});	
					buyPointsDialog.open();
					/////////////////////////////1
					var newDataRequest = $.ajax({
								type: "GET",
								url: "../socialmarket",
								timeout: 30000, // timeout after 30 seconds
								dataType: "json", //JSON
								data: { tag:'getmybalance'}
						});	
						newDataRequest.done(function(data){
							if(data["success"]==0){
									if(data["redir"]){
										showSessExpired();
									}else{
										balance = data['message'];
									}
							}else if(data["success"]==1){
								var balance= data['balance'];		
								///////////////
										if(!balance){
													buyPointsDialog.setMessage("Error");	
													}else{
													var id = btn.parent().attr('id');
													var newDataRequest = $.ajax({
														type: "POST",
														url: "../socialmarket",
														timeout: 30000, // timeout after 30 seconds
														dataType: "json", //JSON
														data: { tag:'buypoints', psid:id }
													});	
													newDataRequest.done(function(data){
														if(data["success"]==0){
																	if(data["redir"]){
																		showSessExpired();
																	}else{
																		alert(data['message']);
																	}
														}else if(data["success"]==1){
																	if(data['total'] > balance){
																			buyPointsDialog.setMessage("You do not have enough cash in your harambesa account to complete this transaction.</br> Your balance is "+balance+" (KES). Please load your harambesa account to be able to purchase these points. ");
																			buyPointsDialog.setButtons( [{
																				label: 'Ok',
																				cssClass: 'btn-default',
																				action: function(dialogItself) {
																					dialogItself.close();
																				}
																				}])
																	}else{
																			buyPointsDialog.setMessage(data['message']);
																			buyPointsDialog.setButtons( [{
																				label: 'Cancel',
																				cssClass: 'btn-default',
																				action: function(dialogItself) {
																					dialogItself.close();
																				}
																				},{
																				label: 'Proceed',
																				cssClass: 'btn-primary',
																				action: function() {
																					var newDataRequest = $.ajax({
																								type: "POST",
																								url: "../socialmarket",
																								timeout: 30000, // timeout after 30 seconds
																								dataType: "json", //JSON
																								data: { tag:'buypoints', psid: id, commit: "y"}
																							});	
																							newDataRequest.done(function(data){
																								if(data["success"]==0){
																										if(data["redir"]){
																											showSessExpired();
																										}else{
																											alert(data['message']);
																										}
																								}else if(data["success"]==1){
																										buyPointsDialog.setMessage(data["message"]);
																										buyPointsDialog.setButtons([{
																											label: 'Ok',
																											cssClass: 'btn-default',
																											action: function(dialogItself) {
																												dialogItself.close();
																												$('#'+id).html('<button type="button" class="btn .accepted-bid-btn"><i class="fa fa-check"></i>Sold</button>');
																											}
																										}]);
																								}
																							});
																							newDataRequest.fail(function(jqXHR, exception){
																								if (jqXHR.status === 0){
																									alert('Sorry, could not establishing a network connection.');
																								}else if (jqXHR.status == 404){
																									alert('Requested page not found. [404]');
																								}else if (jqXHR.status == 500){
																									alert('Internal Server Error [500].');
																								}else if (exception === 'parsererror'){
																									alert('Requested JSON parse failed.');
																								}else if (exception === 'timeout'){
																									alert('Time out error.');
																								}else if (exception === 'abort'){
																									alert('Sorry, Request was aborted.');
																								}else{
																									alert('Sorry, an error occured.');
																								}
																							});
																				}
																		}]);
															}
														}
													});
													newDataRequest.fail(function(jqXHR, exception){
														if (jqXHR.status === 0){
															alert('Sorry, could not establishing a network connection.');
														}else if (jqXHR.status == 404){
															alert('Requested page not found. [404]');
														}else if (jqXHR.status == 500){
															alert('Internal Server Error [500].');
														}else if (exception === 'parsererror'){
															alert('Requested JSON parse failed.');
														}else if (exception === 'timeout'){
															alert('Time out error.');
														}else if (exception === 'abort'){
															alert('Sorry, Request was aborted.');
														}else{
															alert('Sorry, an error occured.');
														}
													});
										}
								
								///////////////
							}
						});
						newDataRequest.fail(function(jqXHR, exception){
							if (jqXHR.status === 0){
								alert('Sorry, could not establishing a network connection.');
							}else if (jqXHR.status == 404){
								alert('Requested page not found. [404]');
							}else if (jqXHR.status == 500){
								alert('Internal Server Error [500].');
							}else if (exception === 'parsererror'){
								alert('Requested JSON parse failed.');
							}else if (exception === 'timeout'){
								alert('Time out error.');
							}else if (exception === 'abort'){
								alert('Sorry, Request was aborted.');
							}else{
								alert('Sorry, an error occured.');
							}
							balance =  "There seems to be a problem with the network, please try again.";
						});
					
					
					
					////////////////////////////2
}

function getUserBalance(){
						var newDataRequest = $.ajax({
								type: "GET",
								url: "../socialmarket",
								timeout: 30000, // timeout after 30 seconds
								dataType: "json", //JSON
								data: { tag:'getmybalance'}
						});	
						newDataRequest.done(function(data){
							if(data["success"]==0){
									if(data["redir"]){
										showSessExpired();
									}else{
										balance = data['message'];
									}
							}else if(data["success"]==1){
								var balance= data['balance'];		
								///////////////
																
								///////////////
							}
						});
						newDataRequest.fail(function(jqXHR, exception){
							if (jqXHR.status === 0){
								alert('Sorry, could not establishing a network connection.');
							}else if (jqXHR.status == 404){
								alert('Requested page not found. [404]');
							}else if (jqXHR.status == 500){
								alert('Internal Server Error [500].');
							}else if (exception === 'parsererror'){
								alert('Requested JSON parse failed.');
							}else if (exception === 'timeout'){
								alert('Time out error.');
							}else if (exception === 'abort'){
								alert('Sorry, Request was aborted.');
							}else{
								alert('Sorry, an error occured.');
							}
							balance =  "There seems to be a problem with the network, please try again.";
						});
			return balance;
}
			
function activateBValOnSellPoints(){			
					$('#sell-points-form').bootstrapValidator({
								message: 'This value is not valid',
								submitHandler: function(validator, form, submitButton) {
									var pnts=$("#pnts").val();
									var ppp=$("#ppp").val();
									var curr= $("#curr").val();
									$.post("../socialmarket",
										{
											tag:"sellmypoints",pnts:pnts, ppp:ppp, curr: curr		  
										}, function(result) {
											if(result['success'] == '1'){
												$( '#social-points-sale-form').html('<div class="bg-success" id="point-sale-success">'+result['message']+'</div>');
												addPointSaleCSS();
												setTimeout(hideSellSuccessMessage, 5000);
											}else if(result['success'] == '0'){
													if(result['redir'] != null){
															//window.location.href="../"+result['redir'];
															showSessExpired("login");
													}else{
															$('#point-sale-success').remove();
															$( '#social-points-sale-form').prepend('<div class="bg-warning" id="point-sale-success">'+result['message']+'</div>');
													}
											}
									}, 'JSON');
								},
								feedbackIcons: {
									valid: 'fa fa-check',
									invalid: 'fa fa-times',
									validating: 'fa fa-refresh'
								},
								fields: {
									pnts: {
										validators: {
											notEmpty: {
												message: 'Please enter number of points'
											},
											integer: {
														message: 'Please enter a whole number'
											},
											greaterThan: {
														value:99,
														message: '(Min. 100 points)'
											}
										}
									},
									ppp: {
										validators: {
											notEmpty: {
												message: 'Please enter your price per point'
											},
											numeric: {
														message: 'Please enter valid price per point'
											},
											callback: {
												message: 'format x.xx',
												callback: function(value, validator){
													// Determine whether regex is matched
													var regX= new RegExp(/^\s*-?(\d+(\.\d{1,2})?|\.\d{1,2})\s*$/);
													return regX.test(value); 
												}
											},
											greaterThan: {
														value:0,
														message: '(Min. 1000 points)'
											}
										}
									}	
								}
					});
}

function hideSellSuccessMessage(){
	$( '#point-sale-success').slideUp("normal", function() { $( '#point-sale-success').remove();});
}

function addPointSaleCSS(){
			$( '#point-sale-success').css({
				"padding" : "15px 10px 15px 10px"
			});
}
function displayMyPointsOnSale(on_sale){
	var i = on_sale.length;
	for(var k=0; k<i; k++ ){
		$('#social-market-offers').append('<div class="sales-post row"><div 	class="owner-photo col-md-2"><img class="img-responsive" alt="" src="../profilepic/'+on_sale[k]['pic']+'"></div><div class="col-md-8"><div class="row"><div class="owner-label col-md-4">Owner:</div><div class="owner-name col-md-8">'+on_sale[k]['name']+'</div></div><div class="row"><div class="points-label col-md-4">Number of Points:</div><div class="no-of-points col-md-8">'+on_sale[k]['nop']+'  <em>(Sold:'+on_sale[k]['sold']+')</em></div></div><div class="row"><div class="price-points-label col-md-4">Price Per Point:</div><div class="no-of-points col-md-8">'+on_sale[k]['ppp']+'</div></div><div class="row"><div class="total-p-points-label col-md-4">Total:</div><div class="total-p-points col-md-8">'+parseFloat(Math.round((on_sale[k]['nop']*on_sale[k]['ppp']) * 100) / 100).toFixed(2)+' (KES)</div></div><div class="row"><div class="points-sale-actions row" id="'+on_sale[k]['psid']+'"><div class="bids-btn col-md-4 text-left row" data-toggle="tooltip" data-placement="bottom" title="(Number of bids)">Bids(0)</div></div><div class="row specific-points-sale-div" id="bids-'+on_sale[k]['psid']+'"></div></div></div></div>');
		}
		$('.bids-btn').tooltip();
		setTimeout(loadNoOfBids, 1000);
}

function loadNoOfBids(){
			var psArray = new Array();
			$('.bids-btn').each(function( index ) {
				psArray[ index ] = $( this ).parent().attr('id');
			});
			console.log(psArray);
			if(psArray.length>0){
					var newDataRequest = $.ajax({
						type: "POST",
						url: "../socialmarket",
 						timeout: 30000, // timeout after 30 seconds
						dataType: "json", //JSON
						data: { tag:'fetchnumberofbids', sids: psArray }
					});	
					newDataRequest.done(function(data){
						if(data["success"]==0){
									if(data["redir"]){
										showSessExpired();
									}else{
										alert(data['message']);
									}
						}else if(data["success"]==1){
							var bids = data['bids'];
							for(var k=0; k< bids.length; k++){
								var link = $('#'+bids[k]["key"]+' .bids-btn');
								if(bids[k]["val"]<1){
									//link.removeAttr("href");
								}else{		
									link.text("Show Bids("+bids[k]["val"]+")");
									$('#'+bids[k]["key"]+' .bids-btn').wrapInner('<a href="javascript: "></a>');
									bindThisBidsLink(link, bids[k]['key']);
								}
							}
						}
					});
					newDataRequest.fail(function(jqXHR, exception){
						if (jqXHR.status === 0){
							alert('Sorry, could not establishing a network connection.');
						}else if (jqXHR.status == 404){
							alert('Requested page not found. [404]');
						}else if (jqXHR.status == 500){
							alert('Internal Server Error [500].');
						}else if (exception === 'parsererror'){
							alert('Requested JSON parse failed.');
						}else if (exception === 'timeout'){
							alert('Time out error.');
						}else if (exception === 'abort'){
							alert('Sorry, Request was aborted.');
						}else{
							alert('Sorry, an error occured.');
						}
					});
		}
}

function bindThisBidsLink(link, id){
		link.click(function(e){
			var newDataRequest = $.ajax({
						type: "POST",
						url: "../socialmarket",
 						timeout: 30000, // timeout after 30 seconds
						dataType: "json", //JSON
						data: { tag:'getallbidsforspecps', psid: id}
					});	
					newDataRequest.done(function(data){
						if(data["success"]==0){
									if(data["redir"]){
										showSessExpired();
									}else{
										alert(data['message']);
									}
						}else if(data["success"]==1){
								var bids = data['bids'];
								showMeBidsForPointSale(bids, id);
						}
					});
					newDataRequest.fail(function(jqXHR, exception){
						if (jqXHR.status === 0){
							alert('Sorry, could not establishing a network connection.');
						}else if (jqXHR.status == 404){
							alert('Requested page not found. [404]');
						}else if (jqXHR.status == 500){
							alert('Internal Server Error [500].');
						}else if (exception === 'parsererror'){
							alert('Requested JSON parse failed.');
						}else if (exception === 'timeout'){
							alert('Time out error.');
						}else if (exception === 'abort'){
							alert('Sorry, Request was aborted.');
						}else{
							alert('Sorry, an error occured.');
						}
					});
		});
}

function showMeBidsForPointSale(bids,psid){
	var bid_div = "";
	var k;
	var len = bids.length;
	var bid = "";
	for(k=0; k<len; k++ ){
		bid += '<div class="points-bid row"><div class="bid-owner-photo col-md-2"><img class="img-responsive" alt="" src="../profilepic/'+bids[k]['pic_path']+'"></div><div class="col-md-8"><div class="row"><div class="owner-label col-md-7">Bidder:</div><div class="owner-name col-md-5">'+bids[k]['bnm']+'</div></div><div class="row"><div class="points-label col-md-7">Number of Points:</div><div class="no-of-points col-md-4">'+bids[k]['nop']+'</div></div><div class="row"><div class="price-points-label col-md-7">Price Per Point:</div><div class="no-of-points col-md-4">'+bids[k]['ppp']+'</div></div><div class="row"><div class="total-p-points-label col-md-7">Total:</div><div class="total-p-points col-md-5">'+parseFloat(Math.round((bids[k]['nop']*bids[k]['ppp']) * 100) / 100).toFixed(2)+' (KES)</div></div><div class="row"><div class="points-sale-actions row" id="bar-'+bids[k]['psbid']+'">';
		if(bids[k]['acc'] == false){
			bid += '<button href="#" type="button" class="accept-bid-btn col-xs-5">Accept</button><button type="button" class="decline-bid-btn col-xs-5 col-xs-offset-1">Decline</button>';}
		else
			bid +='<button type="button" class="btn .accepted-bid-btn"><i class="fa fa-check"></i>Accepted</button>';
		
		bid+='</div></div></div></div>';
		}
		$('#bids-'+psid).html(bid);
		loadBidsBtnStyling();
		bindBidsActionsButtons();
}

function bindBidsActionsButtons(){
	$('.accept-bid-btn').click(function(e){
			var id = $(this).parent().attr('id').split("-");
			showConfirmBidAccept(id[1]);
	});
	$('.decline-bid-btn').click(function(e){
		var ids = $(this).parent().attr('id').split("-");
			
		var buyPointsDialog = new BootstrapDialog({
					title: 'Decline Bid',
					type: BootstrapDialog.TYPE_INFO,
					animate: false,
					size: BootstrapDialog.SIZE_SMALL,
					message: '<div>You are about to decline this bid. Do you want to proceed?</div>',
					buttons: [{
						label: 'Yes',
						cssClass: 'btn-primary',
						action: function(dialogItself){
									//Action here
									var newDataRequest = $.ajax({
										type: "POST",
										url: "../socialmarket",
										timeout: 30000, // timeout after 30 seconds
										dataType: "json", //JSON
										data: { tag:'declinebid', bidid: ids[1]}
									});	
									newDataRequest.done(function(data){
										if(data["success"]==0){
													if(data["redir"]){
														showSessExpired();
													}else{
														buyPointsDialog.setMessage(data['message']);
													}
										}else if(data["success"]==1){
														buyPointsDialog.setMessage(data['message']);
										}
										buyPointsDialog.setButtons([{
												label: 'Ok',
												cssClass: 'btn-primary',
												action: function(dialog){
													dialog.close();
												}
										}]);
									});
									newDataRequest.fail(function(jqXHR, exception){
										if (jqXHR.status === 0){
											alert('Sorry, could not establishing a network connection.');
										}else if (jqXHR.status == 404){
											alert('Requested page not found. [404]');
										}else if (jqXHR.status == 500){
											alert('Internal Server Error [500].');
										}else if (exception === 'parsererror'){
											alert('Requested JSON parse failed.');
										}else if (exception === 'timeout'){
											alert('Time out error.');
										}else if (exception === 'abort'){
											alert('Sorry, Request was aborted.');
										}else{
											alert('Sorry, an error occured.');
										}
									});
									//Action here
						}
						}, {
						label: 'Cancel',
						action: function(dialogItself){
							dialogItself.close();
						}
						}]
				});
		buyPointsDialog.open();
	});
}

function showConfirmBidAccept(bidId){
		BootstrapDialog.show({
					title: 'Accept Bid',
					type: BootstrapDialog.TYPE_INFO,
					animate: false,
					size: BootstrapDialog.SIZE_SMALL,
					message: '<div>You are about to accept this bid. Do you want to proceed?</div>',
					buttons: [{
						label: 'Yes',
						cssClass: 'btn-primary',
						action: function(dialogItself){
							 saveBidAccepted(bidId);
							 dialogItself.close();
						}
						}, {
						label: 'Cancel',
						action: function(dialogItself){
							dialogItself.close();
						}
						}]
				});
}

function saveBidAccepted(bidId){
					var newDataRequest = $.ajax({
						type: "POST",
						url: "../socialmarket",
 						timeout: 30000, // timeout after 30 seconds
						dataType: "json", //JSON
						data: { tag:'acceptspecbid', bid: bidId}
					});	
					newDataRequest.done(function(data){
						if(data["success"]==0){
									if(data["redir"]){
										showSessExpired();
									}else{
										alert(data['message']);
									}
						}else if(data["success"]==1){
								$('#bar-'+bidId).html('<button type="button" class="btn .accepted-bid-btn"><i class="fa fa-check"></i>Accepted</button>');
						}
					});
					newDataRequest.fail(function(jqXHR, exception){
						if (jqXHR.status === 0){
							alert('Sorry, could not establishing a network connection.');
						}else if (jqXHR.status == 404){
							alert('Requested page not found. [404]');
						}else if (jqXHR.status == 500){
							alert('Internal Server Error [500].');
						}else if (exception === 'parsererror'){
							alert('Requested JSON parse failed.');
						}else if (exception === 'timeout'){
							alert('Time out error.');
						}else if (exception === 'abort'){
							alert('Sorry, Request was aborted.');
						}else{
							alert('Sorry, an error occured.');
						}
					});
}

function loadBidsBtnStyling(){
	$("<link/>", {
   rel: "stylesheet",
   type: "text/css",
   href:"../res/css/buttonstyling.css"
}).appendTo("head");
}

function prependNewPointsSales( id, nop, pnts){
			$('#social-market-offers').append('<div class="sales-post row"><div 	class="owner-photo col-md-2"><img class="img-responsive" alt="" src="'+getProfilePicPath()+'"></div><div class="col-md-8"><div class="row"><div class="owner-label col-md-4">Owner:</div><div class="owner-name col-md-8">'+on_sale[k]['name']+'</div></div><div class="row"><div class="points-label col-md-4">Number of Points:</div><div class="no-of-points col-md-8">'+on_sale[k]['nop']+'</div></div><div class="row"><div class="price-points-label col-md-4">Price Per Point:</div><div class="no-of-points col-md-8">'+on_sale[k]['ppp']+'</div></div><div class="row"><div class="total-p-points-label col-md-4">Total:</div><div class="total-p-points col-md-8">'+on_sale[k]['nop']*on_sale[k]['ppp']+'</div></div><div class="row"><div class="points-sale-actions row" id="'+on_sale[k]['psid']+'"><button type="button" class="btn btn-default buy-btn col-md-4" data-toggle="tooltip" data-placement="bottom" title="(All points at the current price)">Buy</button><button type="button" class="btn btn-default bid-btn col-md-4 text-left" data-toggle="tooltip" data-placement="bottom" title="(Suggest number of points and price per point)">Bid</button></div></div></div></div>');
			$('.buy-btn').tooltip();
			$('.bid-btn').tooltip();
}

function getProfilePicPath(){
		
}