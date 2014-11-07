package com.harambesa.yahoo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;

public class YahooCallback extends HttpServlet {
	private String CLIENT_ID = "dj0yJmk9RGtHWXZES3U5YXFWJm"
			+ "Q9WVdrOVFWbFJhM0JLTmpRbWNHb"
			+ "zlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD1jMQ--";
	private String CLIENT_SECRET = "02534b3305b10ce1fc304232f6b263841e6046d8";

	private String message;

	public void init() throws ServletException {
		// Do required initialization
		message = "Hello World";
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			OAuthConsumer consumer = new DefaultOAuthConsumer(
					"dj0yJmk9RGtHWXZES3U5YXFWJmQ9WVdrOVFWbFJhM0JLTmp"
							+ "RbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD1jMQ--",
					"02534b3305b10ce1fc304232f6b263841e6046d8");

			OAuthProvider provider = new CommonsHttpOAuthProvider(
					"https://api.login.yahoo.com/oauth/v2/get_request_token",
					"https://api.login.yahoo.com/oauth/v2/get_token",
					"https://api.login.yahoo.com/oauth/v2/request_auth");
			HttpSession session = request.getSession();
			consumer.setTokenWithSecret(session.getAttribute("Token").toString(), 
					session.getAttribute("Token_Secret").toString());
			provider.retrieveAccessToken(consumer,
					request.getParameter("oauth_token"));

			System.out.println("Access token: " + consumer.getToken());
			System.out.println("Token secret: " + consumer.getTokenSecret());
			URL url = new URL(
					"https://social.yahooapis.com/v1/user/me/contacts?format=json");
			// URL url = new
			// URL("http://social.yahooapis.com/v1/user/me/contacts?view=tinyusercard&count=max&format=xml");
			HttpURLConnection token_request = (HttpURLConnection) url
					.openConnection();
			
			
			consumer.sign(request);

			System.out.println("Sending request...");
			token_request.connect();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					request.getInputStream()));
			String inputLine;
			StringBuilder b = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				b.append(inputLine).append("\n");
			}
			in.close();

			System.out.println("output : " + b.toString());
			response.setContentType("text/plain");
		    PrintWriter out = response.getWriter();

		    out.println(b.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void destroy() {
		// do nothing.
	}

}
