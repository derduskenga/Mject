<div id="create_admin" class="form-group">
    <form action="" method="post" id="create_admin_form">
    	<h3 style="padding-left:px;">Enter the new adminitrators credentials here</h3>
    	<div class="form-group">
    		<label for="" class="control-label col-xs-6">First Name:</label>
    		<div class="col-xs-6">
    			<input type="text" class="form-group" name="f_name" id="f_name">
    		</div>
    	</div>
    	<div clas="form-group">
    		 <label class="control-label col-xs-6">
				Second Name:
			</label>
				<div class="col-xs-6">
	    		  <input class="form-group" type="text" id="s_name" name="s_name" required>
	    	    </div>
    	</div>
    	<div clas="form-group">
    		 <label class="control-label col-xs-6">
				Primary email:
			</label>
				<div class="col-xs-6">
	    		  <input class="form-group" type="email" id="p_email" name="p_email" required>
	    	    </div>
    	</div>
    	<div>
    		<label class="control-label col-xs-6">
				Level:
			</label>&nbsp;
			<div class="col-xs-6">
			    1:<input class="form-group" type="radio" name="level" value="1" required />&nbsp;
				2:<input class="form-group" type="radio" name="level" value="2" required /> &nbsp;
				3:<input class="form-group" type="radio" name="level" value="3" required />
			</div>
    	</div>
			<label class="">
				<input type="submit" value="Create Admin" id="create_admin" name="create_admin" class="btn btn-primary custom-btn-submit">
			</label>
		<div>
			<div id="create_status"></div>
		</div>
   </form>
</div>