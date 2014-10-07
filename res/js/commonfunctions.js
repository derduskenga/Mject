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

function getUrlVars() {
	var map = {};
	var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
		map[key] = value;
	});
	return map;
}


