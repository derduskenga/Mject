
<%@ include file="includes/header.jsp" %>

</head>

<body>
	<div class="container-fluid" id="container">
		<%@ include file="includes/header1.jsp"%>
	 <div class="page-content-fluid">
       <div id="left_side_bar">
        &nbsp;
          <ul class="nav nav-pills nav-stacked">
            <li class="active" >
              <a href="#">Administrators</a>
              <ul>
                <li id="create_admins"><a href="">Create Admin</a></li>
                <li id="view_admins"><a href="">View Admins</a></li>
                <li id="view_logs"><a href="">Admin logs</a></li>
              </ul>
            </li>
            &nbsp;
          </ul>
          <ul class="nav nav-pills nav-stacked">
            <li class="active" id="2">
              <a href="#">Deparments</a>
                <ul>
                  <li id="create_department"><a href="#">Create New Department</a></li>
                  <li id="manage_departments"><a href="#">Manage Deparments</a></li>
                    <ul>
                      <li>View and update</li>
                    </ul>
                </ul>
            </li>
            &nbsp;
          </ul>
          <ul class="nav nav-pills nav-stacked">
            <li class="active">
               <a href="">Manage Site</a>
               <ul>
                <li><a href="#">Administrators log in summary</a></li>
                <li><a href="#">2</a></li>
               </ul>
            </li>
          </ul>
     </div>
     <div id="content_area">

        <div id="swap_area">
          Progress summary
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