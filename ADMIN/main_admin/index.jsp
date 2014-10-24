<!DOCTYPE html>
<html lang="en-us">
<head>
<meta charset="utf-8">

<title>HaraMbesa | Home</title>		

<meta name="description" content="">
<meta name="author" content="">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<link rel="stylesheet" type="text/css" media="screen" href="../../res/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="../../res/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="../../res/jasny-bootstrap/css/jasny-bootstrap.min.css"></link>
<link rel="stylesheet" type="text/css" media="screen" href="../../res/css/stylez.css">
<link rel="stylesheet" type="text/css" media="screen" href="../../res/css/styleX.css">

<link rel="stylesheet" type="text/css" media="screen" href="../../res/bootstrap-datepicker/css/datepicker.css">
<link rel="stylesheet" type="text/css" media="screen" href="../../res/bootstrap-datepicker/css/datepicker3.css">
<link rel="stylesheet" type="text/css" media="screen" href="../../res/bootstrap-datepicker/css/style.css">
<link rel="stylesheet" type="text/css" media="screen" href="../../res/bootstrap-datepicker/css/datepicker.css"></link>
<link rel="stylesheet" type="text/css" media="screen" href="../../res/select/select2.css"/>
<link rel="stylesheet" type="text/css" media="screen" href="../../res/css/select2.css">
<link rel="stylesheet" type="text/css" href="../../res/tipsy/css/main.css" />
<link rel="stylesheet" type="text/css" href="../includes/pro_drop_1.css" />
<link rel="stylesheet" type="text/css" href="../includes/css/admin_layout.css" />
<link rel="stylesheet" type="text/css" href="../includes/css/menu.css" />
<!-- CSS goes in the document HEAD or added to your external stylesheet -->
<style type="text/css">
    table.imagetable {
      font-family: verdana,arial,sans-serif;
      font-size:11px;
      color:#333333;
      border-width: 1px;
      border-color: #999999;
      border-collapse: collapse;
    }
    table.imagetable th {
      background:#b5cfd2 url('cell-blue.jpg');
      border-width: 1px;
      padding: 8px;
      border-style: solid;
      border-color: #999999;
    }
    table.imagetable td {
      background:#dcddc0 url('cell-grey.jpg');
      border-width: 1px;
      padding: 8px;
      border-style: solid;
      border-color: #999999;
    }
</style>

<script type="text/javascript" src="../../res/js/jquery.js"></script>
<script type="text/javascript" src="../../res/bootstrap/js/bootstrap.min.js"></script> 
<script type="text/javascript" src="../../res/bootstrap-validator/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src="../../res/jasny-bootstrap/js/jasny-bootstrap.min.js"></script>
<script type="text/javascript" src="../../res/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
<script type="text/javascript"  src="../../res/select/select2.js"></script>
<script type="text/javascript" src="../../res/js/log4javascript.js" ></script> 
<script type="text/javascript" src="../includes/js/admin_.js"></script>
<script type="text/javascript" src="../includes/js/script.js"></script>


</head>

<body>
	<div class="container-fluid" id="container">
		<%@ include file="../../res/includes/header.jsp"%>
	 <div class="page-content-fluid">
       <div id="left_side_bar">
        &nbsp;
          <ul class="nav nav-pills nav-stacked">
            <li class="active" >
              <a href="#">Administrators</a>
              <ul>
                <li id="create_admins"><a href="">Create Admin</a></li>
                <li id="view_admins"><a href="">View Admins</a></li>
              </ul>
            </li>
            &nbsp;
          </ul>
          <ul class="nav nav-pills nav-stacked">
            <li class="active" id="2">
              <a href="#">Deparments</a>
                <ul>
                  <li id=""><a href="#">Create New Department</a></li>
                  <li id=""><a href="#">View Deparments</a></li>
                </ul>
            </li>
            &nbsp;
          </ul>
          <ul class="nav nav-pills nav-stacked">
            <li class="active">
               <a href="">Manage Site</a>
               <ul>
                <li><a href="#">1</a></li>
                <li><a href="#">2</a></li>
               </ul>
            </li>
          </ul>
     </div>
     <div id="content_area">

        <div id="swap_area">
          Welcome Super Admin
        </div>

     </div>
<!-- 
     <div id="right_side_bar">
       
     </div> -->
		
		<%@ include file="../../res/includes/footer.jsp" %>
		
		<!-- end footer========================= -->
	</div> <!-- container-fluid -->
</body>
</html>

<!-- Modal -->
<div id="dialog-example" class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-body" id="suspend_status">
         <h3>Are you sure you Want to suspend this Administrator?</h3>
         <input type="text" value=""/>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary" val="" id="suspend_btn" data-id="">Suspend</button>
      </div>
    </div>
  </div>
</div>

<div id="dialog-example1" class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-body" id="diasble_status">
         <h3>Are you sure you Want to suspend this Administrator?</h3>
         <input type="text" value=""/>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary" val="" id="disable_btn" data-id="">Disable</button>
      </div>
    </div>
  </div>
</div>