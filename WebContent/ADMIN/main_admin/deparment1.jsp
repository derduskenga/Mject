<div id="create_dpt_form" class="form-group">
    <form action="" method="post" id="create_admin_form">
    	<h3 style="padding-left:px;">Create New Department here</h3>
    	<div class="form-group">
    		<label for="" class="control-label col-xs-6">Name of Department:</label>
    		<div class="col-xs-6">
    			<input type="text" class="form-group" name="dpt_name" id="dpt_name">
    		</div>
    	</div>
    	<div clas="form-group">
    		 <label class="control-label col-xs-6">
				Allocate administrator:
			</label>
				<div class="col-xs-6">
	    		  <select class="form-group" type="text" id="admin_name" name="admin_name" data-bv-choice="true">
	    		  	<option></option>
	    		  </select>
	    	    </div>
    	</div>
			<label class="">
				<input type="submit" value="Create Department" id="create_department" name="create_department" class="btn btn-primary custom-btn-submit">
			</label>
        <div>
            <div id="alertSucces">
                
            </div>
        </div>
   </form>
</div>