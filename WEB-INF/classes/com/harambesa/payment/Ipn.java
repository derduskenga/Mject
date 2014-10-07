package com.harambesa.payment;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;
import javax.servlet.http.Part;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import com.harambesa.gServices.HarambesaUtils;
import com.harambesa.payment.MobileTransactions;

@WebServlet(name = "Ipn", urlPatterns = {"/Ipn/*"})
@MultipartConfig
public class Ipn extends HttpServlet {
	Logger log = Logger.getLogger(Ipn.class.getName());
	PrintWriter out = null;
	/**
	* Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	* methods.
	*
	* @param request servlet request
	* @param response servlet response
	* @throws ServletException if a servlet-specific error occurs
	* @throws IOException if an I/O error occurs
	*/
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		JSONObject jsonRes = new JSONObject();
		MobileTransactions mobi = new MobileTransactions();
		try{
			out = response.getWriter();
			/* TODO output your page here. You may use following sample code. */
			//out.println("hello");
			String api_type = "";
						
							
			Part api_type_part = request.getPart("api_type");
			Scanner api_type_scanner = new Scanner(api_type_part.getInputStream());
			api_type = api_type_scanner.nextLine();
									
			//out.println(apikey);
								
			if(api_type.equals("Initiate")){
				String api_key = "";
				String api_signature= "";
				String api_version = "";
				String transaction_reference = "";
				//String transaction = "";
				String transaction_type = "";
				String transaction_method = "";
				String transaction_date = "";
				double transaction_amount = 0.0;
				String transaction_paybill = "";
				String transaction_account = "";
				String transaction_account_name = "";
				String transaction_name = "";
				String transaction_mobile = "";
				String transaction_email = "";
				int transaction_merchant_reference = 0;
					
				try{
					Part api_key_part = request.getPart("api_key"); 
					Part api_signature_part = request.getPart("api_signature");
					Part api_version_part = request.getPart("api_version"); 
					Part transaction_reference_part = request.getPart("transaction_reference"); 
					//Part transaction_part = request.getPart("transaction");
					
					Part transaction_paybill_part = request.getPart("transaction_paybill");
					Part transaction_date_part = request.getPart("transaction_date");
					Part transaction_amount_part = request.getPart("transaction_amount");
					Part transaction_merchant_reference_part= request.getPart("transaction_merchant_reference");
					Part transaction_name_part = request.getPart("transaction_name");
					Part transaction_mobile_part = request.getPart("transaction_mobile");
					
					Scanner api_key_scanner = new Scanner(api_key_part.getInputStream());
					Scanner api_signature_scanner = new Scanner(api_signature_part.getInputStream());
					Scanner api_version_scanner = new Scanner(api_version_part.getInputStream());
					Scanner transaction_reference_scanner = new Scanner(transaction_reference_part.getInputStream());
					//Scanner transaction_scanner = new Scanner(transaction_part.getInputStream());
					Scanner transaction_paybill_scanner = new Scanner(transaction_paybill_part.getInputStream());
					Scanner transaction_date_scanner = new Scanner(transaction_date_part.getInputStream());
					Scanner transaction_amount_scanner = new Scanner(transaction_amount_part.getInputStream());
					Scanner transaction_merchant_reference_scanner = new Scanner(transaction_merchant_reference_part.getInputStream());
					Scanner transaction_name_scanner = new Scanner(transaction_name_part.getInputStream());
					Scanner transaction_mobile_scanner = new Scanner(transaction_mobile_part.getInputStream());
					
					
					api_key = api_key_scanner.nextLine();
					api_signature= api_signature_scanner.nextLine();
					api_version = api_version_scanner.nextLine();
					
					transaction_reference = transaction_reference_scanner.nextLine();
					//transaction = transaction_scanner.nextLine();
					transaction_date = transaction_date_scanner.nextLine();
					transaction_amount = Double.parseDouble(transaction_amount_scanner.nextLine());
					transaction_paybill = transaction_paybill_scanner.nextLine();
					transaction_name = transaction_name_scanner.nextLine();
					transaction_mobile = transaction_mobile_scanner.nextLine();
					transaction_merchant_reference = Integer.parseInt(transaction_merchant_reference_scanner.nextLine());
					
					mobi.tempStore(transaction_reference,transaction_date,transaction_amount,transaction_name,transaction_paybill,transaction_mobile,transaction_merchant_reference);
					
					//add data to the json object created
					jsonRes.put("api_key",api_key);
					jsonRes.put("api_signature",api_signature);
					jsonRes.put("api_version",api_version);
					jsonRes.put("api_type","Receipt");
					jsonRes.put("transaction_reference",transaction_reference);
					//jsonRes.put("transaction",transaction);
					jsonRes.put("transaction_status_code","001");
					jsonRes.put("transaction_status","Success");
					jsonRes.put("transaction_status_description","Transaction received successfully.");
					out.print(jsonRes);
					
				}catch(Exception ex){
					log.severe("Error: " + HarambesaUtils.getStackTrace(ex));
					jsonRes.put("api_key",api_key);
					jsonRes.put("api_signature",api_signature);
					jsonRes.put("api_version",api_version);
					jsonRes.put("api_type",api_type);
					jsonRes.put("transaction_reference",transaction_reference);
					//jsonRes.put("transaction",transaction);
					jsonRes.put("transaction_status_code","002");
					jsonRes.put("transaction_status","Fail");
					jsonRes.put("transaction_status_description","There was an unknown error");
					out.print(jsonRes);
				}
				
			}else if(api_type.equals("Acknowledge")){
				log.info("API type is " + api_type);
				String api_key = "";
				String api_signature= "";
				String api_version = "";
				
				//String transaction = "";
				String transaction_reference = "";
				String transaction_status_code = "";
				String transaction_status = "";
				String transaction_status_description = "";
				
				try{
					//Part transaction_part = request.getPart("transaction");
					Part transaction_reference_part = request.getPart("transaction_reference");
					Part transaction_status_code_part = request.getPart("transaction_status_code");
					Part transaction_status_part = request.getPart("transaction_status");
					Part transaction_status_description_part = request.getPart("transaction_status_description");
					
					//Scanner transaction_scanner = new Scanner(transaction_part.getInputStream());
					Scanner transaction_reference_scanner = new Scanner(transaction_reference_part.getInputStream());
					Scanner transaction_status_code_scanner = new Scanner(transaction_status_code_part.getInputStream());
					Scanner transaction_status_scanner = new Scanner(transaction_status_part.getInputStream());
					Scanner transaction_status_description_scanner = new Scanner(transaction_status_description_part.getInputStream());
					
					//transaction = transaction_scanner.nextLine();
					transaction_reference = transaction_reference_scanner.nextLine();
					transaction_status_code = transaction_status_code_scanner.nextLine();
					transaction_status = transaction_status_scanner.nextLine();
					transaction_status_description = transaction_status_description_scanner.nextLine();
					
					log.info("transaction_reference is: " + transaction_reference);
					
					if(transaction_status.equalsIgnoreCase("Success")){
						//update the mobile_money_temp
						//then trigger to record the transfer info to the transaction table
						mobi.updateTempStore(transaction_reference);
					}else{
						log.info("Acknowledged was not successful problem is " + transaction_status_description);
						//no update will be made
					}
				}catch(Exception ex){
					log.info("At Receipt now");
					log.severe("Erro here Receipt: " + HarambesaUtils.getStackTrace(ex));
				}
			}else{
				log.info("No type matched your interest");
			}
		}catch(ServletException sex){
			log.severe("Error is here: " + HarambesaUtils.getStackTrace(sex));
		}catch(IOException ioe){
			log.severe("Error is here: " + HarambesaUtils.getStackTrace(ioe));
		}catch(Exception ex){
			log.severe("Error is here blanket Exception: " + HarambesaUtils.getStackTrace(ex));
		}finally{
			
		}
	}
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		//It is a post so the code does not boil to here
		//redirect the user
		response.sendRedirect("../mjet/login");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		processRequest(request, response);
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "This servelet Handles IPN posts from LIPISHA API";
    }

}
