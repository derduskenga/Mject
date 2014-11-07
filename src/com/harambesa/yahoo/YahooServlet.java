package com.harambesa.yahoo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.http.HttpParameters;

public class YahooServlet extends HttpServlet {
	 private String  CLIENT_ID = "dj0yJmk9RGtHWXZES3U5YXFWJm"
	 		+ "Q9WVdrOVFWbFJhM0JLTmpRbWNHb"
	 		+ "zlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD1jMQ--";
	 private String  CLIENT_SECRET = "02534b3305b10ce1fc304232f6b263841e6046d8";
	 
	  private String message;

	  public void init() throws ServletException
	  {
	      // Do required initialization
	      message = "Hello World";
	  }

	  public void doGet(HttpServletRequest request,
	                    HttpServletResponse response)
	            throws ServletException, IOException
	  {
		  try {
	            OAuthConsumer consumer = new DefaultOAuthConsumer("dj0yJmk9RGtHWXZES3U5YXFWJmQ9WVdrOVFWbFJhM0JLTmp"
	            		+ "RbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD1jMQ--",
	                    "02534b3305b10ce1fc304232f6b263841e6046d8");

	     OAuthProvider provider = new CommonsHttpOAuthProvider(
	                    "https://api.login.yahoo.com/oauth/v2/get_request_token",
	                    "https://api.login.yahoo.com/oauth/v2/get_token",
	                    "https://api.login.yahoo.com/oauth/v2/request_auth");

	            System.out.println("Fetching request token...");
	            String authUrl = provider.retrieveRequestToken(consumer, "http://127.0.0.1/mjet/callback");

	            System.out.println("Request token: " + consumer.getToken());
	            System.out.println("Token secret: " + consumer.getTokenSecret());
	            HttpSession session = request.getSession();
	              
	            session.setAttribute("Token",consumer.getToken());
	            session.setAttribute("Token_Secret",consumer.getToken());
	            System.out.println("Now visit:\n" + authUrl + "\n... and grant this app authorization");
	            System.out.println("Enter the verification code and hit ENTER when you're done:");

	           //BareBonesBrowserLaunch.openURL(authUrl);
	        	response.sendRedirect(authUrl);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	  }
	    public static String convertStreamToString(InputStream is) throws UnsupportedEncodingException {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
	        StringBuilder sb = new StringBuilder();
	        String line = null;
	        try {
	            while ((line = reader.readLine()) != null) {
	                sb.append(line).append("\n");
	            }
	        } catch (IOException e) {
	        } finally {
	            try {
	                is.close();
	            } catch (IOException e) {
	            }
	        }
	        return sb.toString();
	    }
	  public void destroy()
	  {
	      // do nothing.
	  }

}
