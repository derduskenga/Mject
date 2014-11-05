package com.harambesa.Utility;

public class Utilities{
	
	//google related static Strings: Created by Kenga.
	public static String CLIENT_ID = "717736851971-to50m7t8dqd96nb1f8spss316hbadhk6.apps.googleusercontent.com";
	public static String CLIENT_SECRET = "2mPBZIXpzRtRHNPN76Xe1kSk";
	public static String DEVELOPER_EMAIL_ADDRESS ="717736851971-to50m7t8dqd96nb1f8spss316hbadhk6@developer.gserviceaccount.com";
	
	public static String REDIRECT_URI = "http://localhost:8080/mjet/invite/validate.jsp";
	public static String ACCESS_TOKEN_REQUEST_URL = "https://accounts.google.com/o/oauth2/token";
	public static String charset = "UTF-8";
	
	//json variables from acces token
	
	public static String ACCESS_TOKEN = "access_token";
	public static String ACCESS_TYPE = "token_type";
	public static String EXPIRY= "expires_in";
	
	public static final String GROUPS_URL = "https://www.google.com/m8/feeds/groups/default/full";
	public static String MY_PRODUCT_NAME = "Harambesa";
	
	public static final String CONTACTS_URL = "https://www.google.com/m8/feeds/contacts/default/full";
	public static final int MAX_NB_CONTACTS = 100000;
	public static final String LIPISHA_FEED_FILE = "lipishaResponse.json";
	
	/*{ "access_token" : "ya29.1.AADtN_V95Op_dqbXITsyjUGCp2-wD-WewEUvjCkEY19WQ2g5Ww6UDrQCZnvy7Tw", "token_type" : "Bearer", "expires_in" : 3587}*/ 
	
	//Agregators
	
	public static String LIPISHA_API_KEY = "51d98aad6bd75e88fe9c312ee174e30d";
	public static String LIPISHA_API_SIGNATURE = "WBsuNDbfMUDaIFERecYVngtVjdbS4BQbrAToiDsKm4jfzmJdaeOqTWwOcHF40JeIZBNrQ0sWZ72K0yYdAC81XMSJdvRmTJWgj012aD4bN5A1gUs0787Wtwl5V28cfIqkv3fJHOvE+TNdzuxh8FQoJTZQtQWJl3v8jZD8k6nhAUA=";
	
	public String LIPISHA_IPN_API_KEY = "95c6faf2ec665548f16eccf8546494ca";
	public String LIPISHA_IPN_API_SIGNATURE = "T8wF962THupskIiAzk0IS9VCsXr7FVWQk9bHg8Iju1xxuqBXiO5TEgukAR2N1PXOjKPu9az9nCjkVU109UIcKRgEk2qFiGrThsOiMHKCCPeZJVoJjUa0VVjgjOB2l5fUIAB/RJPVELhiyMeKQqHMhA51swS/f+tlSpKW0Z3OjFc=";
	
	//public static String LIPISHA_AUTHORIZE_CARD_TRANSACTION_FUNCTION_URL = "https://www.lipisha.com/payments/accounts/index.php/v2/api/authorize_card_transaction"; 
	//public static String LIPISHA_COMPLETE_CARD_TRANSACTION_FUNCTION_URL = "https://www.lipisha.com/payments/accounts/index.php/v2/api/complete_card_transaction";
	//public static String LIPISHA_REVERSE_CARD_AUTHORIZATION_FUNCTION_URL = "https://www.lipisha.com/payments/accounts/index.php/v2/api/reverse_card_authorization";
	//public static String LIPISHA_SEND_MONEY_FUNCTION_URL = "https://www.lipisha.com/payments/accounts/index.php/v2/api/send_money";
	
	public static String LIPISHA_AUTHORIZE_CARD_TRANSACTION_FUNCTION_URL = "https://www.lipisha.com/payments/accounts/index.php/v2/api/test_authorize_card_transaction"; 
	public static String LIPISHA_COMPLETE_CARD_TRANSACTION_FUNCTION_URL = "https://www.lipisha.com/payments/accounts/index.php/v2/api/test_complete_card_transaction";
	public static String LIPISHA_REVERSE_CARD_AUTHORIZATION_FUNCTION_URL = "https://www.lipisha.com/payments/accounts/index.php/v2/api/test_reverse_card_authorization";
	public static String LIPISHA_SEND_MONEY_FUNCTION_URL = "https://www.lipisha.com/payments/accounts/index.php/v2/api/send_money"; 
	
	public static String LIPISHA_API_VERSION = "1.3.0";
	public static String LIPISHA_API_TYPE = "Callback";
	public static String HARAMBESA_ACCOUNT_NUMBER = "01494";
	public static String HARAMBESA_ACCOUNT_NUMBER_PAYOUT = "01558";
	
	public static double WITHDRAWAL_RATE = 4.50;//This is a percentage earnings for Harambesa for the amount withdrawn
	public static double MPESA_CHARGES = 60;
}