<%@page import="com.harambesa.DBConnection.GlobalDresser"%>
<%@page import="javax.servlet.http.HttpSession"%>
<%@page import="java.util.logging.Logger" %>
<%@page import="com.harambesa.gServices.HarambesaUtils" %>
<%
	Logger logger=Logger.getLogger(this.getClass().getName());
	String ent_id = (String)request.getSession().getAttribute("entity_id");
  int pnts=0;
  if(ent_id==null){
	 response.sendRedirect("../../login/");
  }else{
	 GlobalDresser gd = new GlobalDresser(ent_id);
	  pnts = gd.getHarambesaPoints();
	 logger.info("Points:"+String.valueOf(pnts));
  }
  %>
<% if(pnts > 99){ %> 
<form class="form-horizontal" role="form" id="sell-points-form">
	<div class="form-group">
		<legend class="scheduler-border">Please fill in the following details:</legend>
	</div>
	<div class="form-group">
		<label for="pnts" class="col-sm-6  col-md-6 control-label">Number of Points:</label>
		<div class="col-sm-5 col-md-5">
			<input class="form-control" id="pnts" name="pnts" type="text" />
			<small>(Min. 100 points)</small>
		</div>
	</div>
	<div class="form-group">
		<label for="ppp" class="col-sm-6 col-md-6 control-label">Price Per Point:</label>
		<div class="col-sm-2 col-md-2">																									
			<input class="form-control" id="ppp" name="ppp" type="text" />
		</div>
		<div class="col-sm-3 col-md-3 form-group">						
			<select class="form-control" id="curr" name="curr">
					<option value="1">KES</option>
					<option value="2">USD</option>
			</select>
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-5 col-sm-offset-6 col-xs-12 text-right">
				<button type="button" class="btn cancel-points-sale-btn btn-default" id="cancel-points-sale-btn">Cancel</button>
				<button type="submit" class="btn btn-default btn-action">Sell Points</button>
		</div>
	</div>
</form>
<hr>
<% }else{ %>

<div id="too-little-points-div" class="text-center bg-warning" style="padding-top:10px; padding-bottom:10px; margin-bottom:5px;">The minimum amount of points you can sell is 100. Your balance is <%=pnts%></div>
<script type="text/javascript">
setTimeout(removeTooLittlePointsDiv, 5000);
function removeTooLittlePointsDiv(){
	$( '#too-little-points-div').slideUp("normal", function() { $( '#too-little-points-div').remove();});
}
</script>
<% } %>