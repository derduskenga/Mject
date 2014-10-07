<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta charset="utf-8">
<meta content="IE=edge" http-equiv="X-UA-Compatible">
<meta content="width=device-width, initial-scale=1.0" name="viewport">
		<title>Twitter bootstrap table pagination</title>
		<link rel="stylesheet" type="text/css" href="../res/datatables/bootstrap.min.css">
		<script type="text/javascript" language="javascript" src="../res/datatables/jquery-1.10.2.min.js"></script>
		<script type="text/javascript" language="javascript" src="../res/datatables/jquery.dataTables.min.js"></script>
		<script type="text/javascript" language="javascript" src="../res/datatables/jquery-DT-pagination.js"></script>
		
		<script type="text/javascript">
		/* Table initialisation */
		$(document).ready(function() {
			$('#contactTable').dataTable( {
			        "bSort": false,      // Disable sorting
				"iDisplayLength": 15,   //records per page
				 	"sDom": "t<'row'<'col-md-6'i><'col-md-6'p>>",
					"sPaginationType": "bootstrap"
				} );
			} );
		</script>
		
		<style>
		.pagination {
   		    margin:0 ! important;
	}</style>
	
	</head>
	<body>
		<div class="container">
			
<table class="table table-condensed table-bordered" id="contactTable">
	<thead>
		<tr>
			<th>S.no</th>
			<th>Column A</th>
			<th>Column B</th>
			<th>Column C</th>
		</tr>
	</thead>
	
	<tbody>
		<%
for (int i=1; i<=100; i++)
  {%>
  <tr><td><% out.println(i);%></td><td><% out.println("A-col+" + i);%></td><td><% out.println("B-col+" + i);%></td><td><% out.println("C-col+" + i);%></td></tr>
  <% }
%> </tbody>
</table>
			
		</div>
	</body>
</html>
