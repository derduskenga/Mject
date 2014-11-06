package com.harambesa.Utility;
 
  import java.io.BufferedReader;
  import java.io.DataOutputStream;
  import java.io.InputStreamReader;
  import java.net.URLEncoder;
  import java.net.URLConnection;
  import java.io.OutputStreamWriter;
  import java.net.URL;
  import javax.net.ssl.HttpsURLConnection;
  import org.json.simple.JSONArray;
  import org.json.simple.JSONObject;
  import org.json.simple.parser.JSONParser;
  import org.json.simple.parser.ParseException;
  import java.io.FileWriter;
  import java.io.FileReader;
  import java.io.FileNotFoundException;
  import java.io.IOException;

public class AccessCodeRequester{
    //fields
    String access_token;
   // long expiry;
    String access_type;
    
    String authorization_code;
    
    //constructor
    
    public AccessCodeRequester(String code){
	this.authorization_code = code;
    }
    
    //setters
    public void setAccessToken(String token){
    
	this.access_token = token;
      
    }
    //public void setExpiry(long expiry){
	
	//this.expiry=expiry;
    
   // }
    public void setAccessType(String access_type){
    
	this.access_type = access_type;
    }
    
    //getters
    public String getAccessToken(){
	
	return access_token; 
    
    }
    //public long getExpiry(){
	//return expiry;
    
   // }
    public String getAccessType(){
	
	return access_type;
    
    }
    
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
	String jsonResponse = "";
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
	      
	      while ((line = rd.readLine()) != null)
	      {
		    jsonResponse = jsonResponse + line;
	      }

	      //System.out.println(jsonResponse);

	      wr.close();
	      rd.close();
	}catch (Exception e){
	    //code to take care of all Exceptions
	    //System.out.println("Error : " + e.getMessage().toString());
	}

	return jsonResponse;	      
    }    
    public void writeToFile(String jsonStr){
	  try {

		FileWriter file = new FileWriter("accessToken.json");
		file.write(jsonStr);
		file.flush();
		file.close();

	  } catch (IOException e) {
		e.printStackTrace();
	  }
    
    }
    
    
    public void readFromFileParser(){
    
	JSONParser parser = new JSONParser();
 
	try {
 
		Object obj = parser.parse(new FileReader("accessToken.json"));
 
		JSONObject jsonObject = (JSONObject) obj;
 
		//String name = (String) jsonObject.get();

		//long age = (Long) jsonObject.get("age");
		
		setAccessToken((String)jsonObject.get(Utilities.ACCESS_TOKEN));
		//setExpiry((Long)jsonObject.get(Utilities.EXPIRY));
		setAccessType((String)jsonObject.get(Utilities.ACCESS_TYPE));
		
 
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (ParseException e) {
		e.printStackTrace();
	}
    }
    

}
