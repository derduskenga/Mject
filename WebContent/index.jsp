<%@ page import = "java.sql.Connection"%>
<%@ page import = "java.sql.DriverManager"%>
<%@ page import = "java.sql.SQLException"%>
<%@ page import = "java.sql.Statement"%>
<%@ page import = "com.harambesa.DBConnection.DBConnection"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%	
	DBConnection db=new DBConnection();
	out.print(db._getMessage());
	if(db._getConnection()!=null){
		out.print("DB Connection Works Fine");
	}
	db.closeDB();
%>