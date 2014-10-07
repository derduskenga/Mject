$(document).ready(function(){
$('#search_user').select2({
			placeholder: "Type to search",
			multiple:true,
			minimumInputLength: 1,
			ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
					url: "../myfriends",
					dataType: 'json',
					data: function (term, page) {
						return {
							q: term, // search term
							page_limit: 4,
						};
			    },
				results: function (data, page) { // parse the results into the format expected by Select2.
					// since we are using custom formatting functions we do not need to alter remote JSON data
					if(data.redir != null){
									BootstrapDialog.show({
											title: 'Session Expired',
											type: BootstrapDialog.TYPE_DEFAULT,
											message: 'Sorry, your session has expired. Please login to continue.',
											buttons: [{ 
												label: 'OK',
												cssClass: 'btn-default', 
												autospin: false,
												action: function(dialogRef){    
													dialogRef.close();
													window.location.href=data['redir'];	
												}
											}]
							});		
					}
					return {results: data.friends};
				}
			},
			formatResult: friendFormatResult, // see function outside this document.ready func
			formatSelection: friendFormatSelection, // see function outside this document.ready func
			dropdownCssClass: "bigdrop", // apply css that makes the dropdown taller
			escapeMarkup: function (m) { return m; } // we do not want to escape markup since we are displaying html in results
			}).on("select2-selecting", function(e) {
				console.log("selecting val=" + e.val + " choice=" + e.object.text);
				window.location.href = "../user/?user="+e.val;;
			});
});