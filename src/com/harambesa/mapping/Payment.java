package com.harambesa.mapping;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.harambesa.payment.DebitCardTransaction;
import com.harambesa.payment.MobileTransactions;

public class Payment extends HttpServlet{
	HttpServletRequest request=null; 
	HttpServletResponse response=null;
	PrintWriter out = null;	
	Logger log = Logger.getLogger(Payment.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException {
		this.request=req;
		this.response=res;
		out = res.getWriter();	
		doPost(req, res);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{  
		out = response.getWriter();
		this.request=request;
		this.response=response;
		if(request.getParameter("tag")==null){
			out.print("Bad request");
		}else{
			String tag = request.getParameter("tag");
			if(request.getSession().getAttribute("entity_id")==null){
				//user not logged
				//redirect to mjet/login
				response.sendRedirect("/mjet/login/");
				JSONObject ob = new JSONObject();
				ob.put("success",0);
				ob.put("redir","../login");
				out.print(ob);
			}else{
				if(tag.equals("authorize_c")){
				response.setContentType("application/json");
				HttpSession ses = request.getSession();
				String f_name = (String)ses.getAttribute("f_name");
				String l_name = (String)ses.getAttribute("l_name");
				String entity_id = (String)ses.getAttribute("entity_id");
				String name = f_name + " " + l_name;
				
				String holder_card_number = request.getParameter("card_number");
				String cvv = request.getParameter("cvv");
				String expiry_date = request.getParameter("expiry");
				String currency = request.getParameter("currency");
				String amount = request.getParameter("amount");
				String card_type = request.getParameter("card_type");
				
				JSONObject json = new JSONObject();
				DebitCardTransaction dCard = new DebitCardTransaction(entity_id);
				
				json = dCard.authorizeCardTransaction(holder_card_number,expiry_date,name,cvv,currency,amount,card_type);		
				out.print(json);
				
			}else if(tag.equals("check_mpesa_code")){
				//response.setContentType("application/json");
				HttpSession ses = request.getSession();
				String entity_id = (String)ses.getAttribute("entity_id");
				JSONObject mpesaJson = new JSONObject();
				MobileTransactions mobi = new MobileTransactions();	
				
				String amount = request.getParameter("amount");
				String type = request.getParameter("type");
				String phone = request.getParameter("phone");
				
				mpesaJson = mobi.checkMobiMoneyTransaction(Double.parseDouble(amount),type,entity_id,phone);
				
				out.print(mpesaJson);
			}else if(tag.equals("send_money")){
				response.setContentType("application/json");
				HttpSession ses = request.getSession();
				String entity_id = (String)ses.getAttribute("entity_id");
				JSONObject mobileWithdrawJson = new JSONObject();
				
				DebitCardTransaction dCard = new DebitCardTransaction(entity_id);
				String amount = request.getParameter("amount");
				String mobile = request.getParameter("mobile");
				try{
					mobileWithdrawJson = dCard.sendMoneyToMobile(mobile,amount);
				}catch(Exception ex){
					mobileWithdrawJson.put("success",0);
					mobileWithdrawJson.put("message",ex.getMessage().toString());
				}
				out.print(mobileWithdrawJson);
			}else if(tag.equals("withdraw_from_bank")){
				response.setContentType("application/json");
				HttpSession ses = request.getSession();
				String entity_id = (String)ses.getAttribute("entity_id");
				JSONObject bankWithdrawJson = new JSONObject();
				DebitCardTransaction dCard = new DebitCardTransaction(entity_id);
				String amount = request.getParameter("withdraw_amount");
				String bank_name = request.getParameter("bank_name");
				String branch_name = request.getParameter("bank_branch");
				String account_number = request.getParameter("account_number");
				String account_names = request.getParameter("full_account_names");
				String swift_code = request.getParameter("swift_code");
				String address = request.getParameter("physical_address");
				String currency = request.getParameter("currency");
				bankWithdrawJson = dCard.saveBankWithDrawalRequest(amount,bank_name,branch_name,account_number,account_names,swift_code,address,currency);
				out.print(bankWithdrawJson);
			}
				
			}
		}

}
	}
