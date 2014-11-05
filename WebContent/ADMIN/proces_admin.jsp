<%@page import="javax.servlet.http.HttpSession"%>
<%@page import="org.apache.logging.log4j.LogManager"%>
<%@page import="org.apache.logging.log4j.Logger"%>
<%@page import="com.harambesa.gServices.HarambesaUtils"%>
<%@page import="javax.servlet.http.HttpServletRequest"%>
<%@page import="java.io.IOException"%>
<%
  Logger log;
  HttpSession sess=request.getSession();

  if(sess.getAttribute("aId")==null || sess.getAttribute("fName")==null || sess.getAttribute("sName")==null || sess.getAttribute("uName")==null || sess.getAttribute("aLevel")==null){
	response.sendRedirect("../ADMIN/");
  }else{

     String level =  sess.getAttribute("aLevel").toString();
     

     if(level.equals("1")){

       response.sendRedirect("../ADMIN/main_admin/");

      }else if(level.equals("2")){

      response.sendRedirect("../ADMIN/admins/");
  }
}
%>
<html>

 	<head>
 		<title>Welcome</title>
 	</head>
 	<body>
 		<h1 style="positon:absolute">Welcome to admin Process</h1>
 	</body>

</html>