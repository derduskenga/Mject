package com.harambesa.gServices;

  import java.io.BufferedReader;
  import java.io.DataOutputStream;
  import java.io.InputStreamReader;
  import java.net.URLEncoder;
  import java.net.URLConnection;

  import java.io.OutputStreamWriter;

  import java.net.URL;
  import javax.net.ssl.HttpsURLConnection;
  import com.harambesa.Utilities.Utilities;

public class AccessCodeRequester{
    //fields
    String access_token;
    String expiry;
    String access_type;
    String authorization_code;
    
    //constructor
    
    public AccessCodeRequester(String code){
	this.authorization_code = code;
    }
    
    
    //getters and setters
    
    //Methods
    public String requestToken(){
	String code = authorization_code;
	String clientSecret = Utilities.CLIENT_SECRET;
	String newUrl = "https://accounts.google.com/o/oauth2/token";
	String clientId = Utilities.CLIENT_ID;
	String callback = Utilities.REDIRECT_URI;

	/*String requestUrl = "https://accounts.google.com/o/oauth2/auth?client_id="
	      + clientId
	      + "&redirect_uri="
	      + callback
	      + "&scope=https://www.google.com/m8/feeds/&response_type=";*/

	String tokenUrl = new String(Utilities.ACCESS_TOKEN_REQUEST_URL);

	StringBuffer params = new StringBuffer("");
	params.append("code=" + code);
	params.append("&client_id=" + clientId);
	params.append("&client_secret=" + clientSecret);
	params.append("&redirect_uri=" + callback);
	params.append("&grant_type=authorization_code");

	try
	{
	// Send data
	      URL url = new URL(tokenUrl.toString());
	      URLConnection conn = url.openConnection();
	      conn.setDoOutput(true);
	      OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	      wr.write(params.toString());
	      wr.flush();

	      // Get the response
	      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	      String line;
	      String jsonResponse = "";
	      while ((line = rd.readLine()) != null)
	      {
		    jsonResponse = jsonResponse + line;
	      }

	      System.out.println(jsonResponse);

	      wr.close();
	      rd.close();
	}catch (Exception e){
	    //code to take care of all Exceptions
	    System.out.println("Error : " + e.getMessage().toString());
	}

	return jsonResponse;	      
    }
    
    
    public void parseJSON(){
    
    
    }
    

}
