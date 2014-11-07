package com.harambesa.mapping;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.harambesa.gServices.Offer;
public class OfferMapping extends HttpServlet{
	HttpServletRequest request=null; 
	HttpServletResponse response=null;
	PrintWriter out = null;	
	Logger log = Logger.getLogger(OfferMapping.class.getName());
	
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
		HttpSession session = request.getSession();
		if(request.getSession().getAttribute("entity_id")==null){
			JSONObject ob = new JSONObject();
			response.sendRedirect("/mjet/login/");
			ob.put("success",0);
			ob.put("redir","../login");
			out.print(ob);
		}else if(request.getParameter("tag")==null){
			//page accesed directly and this is bad request
			out.print("Bad request");
		}else{
			String tag = request.getParameter("tag");
			if(tag.equals("save_offer")){      
				JSONObject obj = new JSONObject();
				response.setContentType("application/json");
				String offered_amount = request.getParameter("offered_amount");
				String offered_currency = request.getParameter("offered_currency");
				String offered_category = request.getParameter("offered_category");
				String offer_details = request.getParameter("offer_details");
				String offer_summary = request. getParameter("offer_summary");
		  
				Offer offer = new Offer((String)request.getSession().getAttribute("entity_id"));
				String offer_owner_names = (String)request.getSession().getAttribute("f_name") + " " + (String)session.getAttribute("l_name");
				obj = offer.savefferPost(offered_amount,offered_currency,offered_category,offer_details,offer_summary,offer_owner_names);
				
				if((Integer)obj.get("success") == 1){
					obj.put("names", request.getSession().getAttribute("f_name") + " " + (String)session.getAttribute("l_name"));
					obj.put("profile_pic_path", "../profilepic/" + request.getSession().getAttribute("profile_pic_path")); 
					obj.put("offer_type","money");
				}
				out.print(obj);
				 
			}else if(tag.equals("pull_offer_posts")){
				HttpSession p_session = request.getSession();
				String user_entity_id = (String)p_session.getAttribute("entity_id");
				Offer offer = new Offer(user_entity_id);
				JSONObject obj = new JSONObject();
				obj = offer.pullAllOffers();
				response.setContentType("application/json");
				out.print(obj);
			}else if(tag.equals("save_offer_application")){ 
				HttpSession p_session = request.getSession();
				JSONObject objS = new JSONObject();
				if((String)p_session.getAttribute("entity_id") != null){
					String user_entity_id = (String)p_session.getAttribute("entity_id");
					String application_text = request.getParameter("application_text");
					String offer_id = request.getParameter("offer_id");
					String table_name = request.getParameter("table_name");
					Offer offer = new Offer(user_entity_id);
					objS = offer.saveOfferApplication(offer_id,application_text,table_name);
					response.setContentType("application/json");
					out.print(objS);
				}else{
					objS.put("success",0);
					objS.put("redir","../login");
					out.print(objS);
				}
			}else if(tag.equals("offer_countries")){
				Offer offer = new Offer((String)request.getSession().getAttribute("entity_id"));
				response.setContentType("application/json");
				out.print(offer.getCountrys());
			}else if(tag.equals("offer_services")){
				Offer offer = new Offer((String)request.getSession().getAttribute("entity_id"));
				response.setContentType("application/json");
				out.print(offer.getServices());
			}else if(tag.equals("offer_items")){
				Offer offer = new Offer((String)request.getSession().getAttribute("entity_id"));
				response.setContentType("application/json");
				out.print(offer.getItems());
			}else if(tag.equals("save_service_offer")){
				String service_entity_id = (String)request.getSession().getAttribute("entity_id"); 
				Offer offer = new Offer(service_entity_id);
				String service_name = request.getParameter("service_name");
				String service_countries = request.getParameter("service_countries");
				String state = request.getParameter("state");
				String residence = request.getParameter("residence");
				String hours_a_day = request.getParameter("hours_a_day");
				String start_date = request.getParameter("start_date");
				String service_offer_details = request.getParameter("service_offer_details");
				String service_offer_owner_names = (String)request.getSession().getAttribute("f_name") + " " + (String)request.getSession().getAttribute("l_name");
				
				JSONObject obj = new JSONObject();
				
				try{
					obj = offer.saveServiceOffer(service_name,service_countries,state,residence,hours_a_day,start_date,service_offer_details,service_offer_owner_names);
				}catch(Exception ex){
					//out.print(ex.getMessage().toString());
				}
				response.setContentType("application/json");
				obj.put("profile_pic_path",(String)request.getSession().getAttribute("profile_pic_path"));
				obj.put("names",(String)request.getSession().getAttribute("f_name") + " " + (String)request.getSession().getAttribute("l_name"));
				obj.put("offer_type","service"); //3 offer_types service;money;material;
				out.print(obj);
				
			}else if(tag.equals("save_application_acceptance")){
				
				Offer offer = new Offer((String)request.getSession().getAttribute("entity_id"));
				String offer_id = request.getParameter("offer_id");
				String application_id = request.getParameter("application_id");
				String offer_owner_entity_id = request.getParameter("offer_owner_entity_id");
				String table_name = request.getParameter("table_name");
				String applicant_entity_id = request.getParameter("applicant_entity_id");
				
				JSONObject json = new JSONObject();
				json = offer.saveApplicationAcceptance(offer_id,application_id,offer_owner_entity_id,applicant_entity_id,table_name);
				response.setContentType("application/json");
				out.print(json);
				
			}else if(tag.equals("accept_money_application")){
				Offer offer = new Offer((String)request.getSession().getAttribute("entity_id"));
				String offer_id = request.getParameter("offer_id");
				String application_id = request.getParameter("application_id");
				String offer_owner_entity_id = request.getParameter("offer_owner_entity_id");
				String applicant_entity_id = request.getParameter("applicant_entity_id");
				String offer_type = request.getParameter("offer_type");
				String table_name = request.getParameter("table_name");
				
				JSONObject jsonObj = new JSONObject();		
				jsonObj = offer.saveAcceptedMoneyApplication(offer_id,application_id,offer_owner_entity_id,applicant_entity_id,offer_type,table_name);
				response.setContentType("application/json");
				out.print(jsonObj);
			}
		}
		}
}