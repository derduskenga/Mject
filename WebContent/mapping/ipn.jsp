<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="javax.servlet.http.HttpSession"%>
<%@page import="com.harambesa.payment.MobileTransactions"%>
<%@page import="javax.servlet.http.Part"%>
<%@page import="java.util.Scanner"%>
<%@page import="javax.servlet.annotation.MultipartConfig"%>
<%@page trimDirectiveWhitespaces="true"%>
<%
	
	//if(request.getSession().getAttribute("entity_id")==null){
		//out.print("Access denied !");
	//}else{
		//String api_type = request.getParameter("api_type");
		//if(api_type.equals("Initiate")){
			//initial LIPISHA call
			//response.setContentType("text/html;charset=UTF-8");
			JSONObject jsonReaponse = new JSONObject();
			//MobileTransactions mobi = new MobileTransactions();
			
		

			String apikey = "";

			Part apikeypart = request.getPart("api_key");
			try {
				Scanner scanner = new Scanner(apikeypart.getInputStream());
				apikey = scanner.nextLine(); // read filename from stream

			}catch(Exception ex){
				out.print("Exception here " + ex.getMessage().toString());
			}finally{
				out.print(apikey);
			}
			
			
			
			/*String entity_id = (String)request.getSession().getAttribute("entity_id");
			String api_key = request.getParameter("api_key");
			String api_signature= request.getParameter("api_signature");
			String api_version = request.getParameter("api_version");
			String transaction = request.getParameter("transaction");// instead of transaction_reference
			String transaction_type = request.getParameter("transaction_type");
			String transaction_method = request.getParameter("transaction_method");
			String transaction_date = request.getParameter("transaction_date");
			double transaction_amount = Double.parseDouble(request.getParameter("transaction_amount"));
			String transaction_paybill = request.getParameter("transaction_paybill");
			String transaction_account_number = request.getParameter("transaction_account_number");// instead of transaction_account
			String transaction_account_name = request.getParameter("transaction_account_name");
			String transaction_name = request.getParameter("transaction_name");
			String transaction_mobile = request.getParameter("transaction_mobile");
			String transaction_email = request.getParameter("transaction_email");
			String transaction_reference = request.getParameter("transaction_reference"); //instead of transaction_merchant_reference
			String transaction_merchant_reference = request.getParameter("transaction_merchant_reference");*/
			
			/*mobi.setAmount(transaction_amount);
			mobi.setMobileReference(transaction_reference);
			mobi.setMobile(transaction_mobile);
			
			jsonReaponse.put("api_key",api_key);
			jsonReaponse.put("api_signature",api_signature);
			jsonReaponse.put("api_version",api_version);
			jsonReaponse.put("transaction",transaction);
			jsonReaponse.put("transaction_status_code","001");
			jsonReaponse.put("transaction_status","Success");
			jsonReaponse.put("transaction_status_description","Transaction post successfully");
			out.print("bad request in initiate");
			out.print(jsonReaponse);*/
			
		/*}else if(api_type.equals("Receipt")){
			String transaction = request.getParameter("transaction");
			String transaction_status_code = request.getParameter("transaction_status_code");
			String transaction_status = request.getParameter("transaction_status");
			String transaction_status_description = request.getParameter("transaction_status_description");
			if(transaction_status_code.equals("001")){
				mobi.setTransactionReference(transaction);
				mobi.storeMobiMoneyTransaction();
			} 
			
			
			
		}*/
	//}

%>