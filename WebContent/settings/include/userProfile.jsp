<!--========current profile section====== -->
<div id="profile">	
		<!--____________________________________________________________________________-->	
			<form role="form" id="update_profile_form" class="form-horizontal">
						<legend class="text-center">
							<h4>Your Current Profile.</h4>
						</legend>

						<div id="form_panel" class="panel-body">
							<!-- <div class="text-small text-center profile-update-tip"><p>Fields marked * are mandatory</p></div> -->
							<div id="profile-update-error" class="col-lg-10 col-lg-offset-1 alert-danger hidden"></div>
							<div class="form-group">
								<label class="col-lg-4 control-label" for="profile-pic-upload">Profile Picture</label>
								<div class="fileinput fileinput-new col-lg-7" data-provides="fileinput">
										<div class="fileinput-new thumbnail" style="width: 200px; height: 150px;" id="profilepic">
											<img data-src="holder.js/100%x100%" alt="" >
										</div>
										<div class="fileinput-preview fileinput-exists thumbnail" style="max-width: 200px; max-height: 150px;"></div>
										<div>
												<span class="btn btn-default btn-file"><span class="fileinput-new">Select image</span><span class="fileinput-exists">Change</span><input type="file" name="profile_pic" id="profile_pic"></span>
												<a href="#" class="btn btn-default fileinput-exists" data-dismiss="fileinput">Remove</a>
										</div>
								</div>
							</div> 

							<div class="form-group">
								<label class="col-lg-4 control-label" for="gender">Gender *</label>
								<div class="col-lg-7">
									<select  class="form-control" id="gender" name="gender" role="menu">
										<option></option>
										<option>M</option>
										<option>F</option>
									</select>
								</div>
							</div>
							<div class="form-group">
									<label class="col-sm-4 control-label" for="countrys">Country *</label>
									<div class="col-sm-7">
										<select class="form-control" id="countrys" name="countrys" role="menu">				
										</select>
										<!-- <span id="loading_country" class="hidden">
												<img  src="../res/images/loading.gif" /> Loading Countries
										</span>-->
									</div> 
							</div>
							<div class="form-group">
								<label class="col-lg-4 control-label" for="dob">Date Of Birth *</label>
								<div class="col-lg-7">
									 <input  type="text" placeholder=""   class="form-control"  id="dateofbirth" name="dateofbirth">
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-4 control-label" for="phone-country-code">Phone Number *</label>
								<!-- <div class="col-xs-3">
									 <input  type="text" placeholder="--"  class="form-control"  id="phone-country-code" name="phone-country-code">
								</div> -->
								<div class="col-xs-4">
									 <input  type="text" placeholder=""  class="form-control"  id="phone-country-code" name="phone-country-code">
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-4 control-label" for="occupation">Occupation *</label>
								<div class="col-lg-7">
									<input type="text" class="form-control" name="occupation" id="occupation">
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-4 control-label" for="orgname">Organization </label>
								<div class="col-lg-7">
									<input type="text" class="form-control" name="orgname1" id="orgname1">
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-4 control-label" for="address">Address *</label>
								<div class="col-lg-7">
									<input type="textarea" class="form-control" name="address" id="address">
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-4 control-label" for="address">Postal/Zip Code *</label>
								<div class="col-lg-7">
									<input type="textarea" class="form-control"  name="postalcode" id="postalcode">
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-4 control-label" for="city">City *</label>
								<div class="col-lg-7">
									<input type="textarea" class="form-control"  name="city" id="city">
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-4 control-label" for="postalcode">Physical / Street Address </label>
								<div class="col-lg-7">
									<textarea type="text" class="form-control" rows="3" name="physical_address" id="physical_address"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-4 control-label" for="primaryemail">Primary Email </label>
								<div class="col-lg-7">
									<textarea type="text" class="form-control" rows="3" name="primaryemail" id="primaryemail"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-4 control-label" for="bio">Bio	</label>
								<div class="col-lg-7">
									<textarea type="textarea" maxlength="500" class="form-control" rows="6" name="bio" id="bio" placeholder="Write a brief summary about yourself in 500 characters or less."></textarea>
									<small id="remainingC">Remaining: 500</small>
								</div>
							</div>
							<div class="form-group">
								<div class="col-xs-4 col-xs-offset-4 col-lgs-offset-4">
										<button type="submit"  class="btn btn-primary custom-btn-submit " id="submit-pr-btn">Update</button>	
								</div>
								<div class="col-xs-4">
										<span id="updating-profile-gif" class="hidden">
												<img  src="../res/images/loading.gif" /> Updating profile...
										</span>
								</div>
							</div>

						</div>
			</form>
		<!-- ___________________________________________________________________________-->	
</div>
