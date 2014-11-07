<%@page import="javax.servlet.http.HttpSession"%>
<% 

// HttpSession session = request.getSession();
// String level =  session.getAttribute("aLevel").toString();

if(session.getAttribute("aId")==null){
  response.sendRedirect("../");
}else if(!session.getAttribute("aLevel").toString().equals("1")){
   response.sendRedirect("../");
}else{
%>
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
      font-size:12.5px;
      color:#333333;
    }
    table.imagetable th {
      padding: 8px;
    }
    table.imagetable td {
      padding: 8px;
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
<%
}
%>