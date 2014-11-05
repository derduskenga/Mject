package com.harambesa.Utility;

    import com.google.gdata.client.contacts.ContactsService;
    import com.google.gdata.data.contacts.ContactEntry;
    import com.google.gdata.data.contacts.ContactFeed;
    import com.google.gdata.data.contacts.ContactGroupEntry;
    import com.google.gdata.data.contacts.ContactGroupFeed;
    import com.google.gdata.client.Query;
    import com.google.gdata.data.extensions.*;
    import com.google.gdata.util.ServiceException;


    import java.io.IOException;
    
    import java.net.URL;
    import java.util.*;
   


public class ContactReader{
    
    //class field
    public String googleAccessToken;
    ContactEntry contactEntries;
    
    String title; 
    String email; //email addresses

    
    //class constructor
    public ContactReader (String accessToken){

	this.googleAccessToken = accessToken;
    }
 
    public void setEmail(String email){
      
	this.email = email;
    
    }
    public void setTitle(String title){
	this.title = title;
    
    }
    
    //getters
    public String getEmail(){
	 return email;
    
    }
    public String getTitle(){
   
	  return title;
    }
    
    /*String contactTitlesString = "";//CSV names; comma seperated usernames
    String emailString = "";
    String relString = "";
    String labelString = "";
    String primaryString = "";*/
 
    
   /* public void setContactEntries(ContactEntry contactEntries){
	this.contactEntries = contactEntries;
    }
    
    public ContactEntry getContactEntries(){
	return contactEntries;
    }
    
    public void setTitle(String title){
	this.title = title;
    
    }
    public String getT(){
	return title;
    
    }*/
    
    //Class methods
    public String getIdOfMyGroup() {
	  ContactsService contactsService = new ContactsService(Utilities.MY_PRODUCT_NAME);
	  
	  contactsService.setHeader("Authorization", "Bearer " + googleAccessToken);

	  try {
		URL feedUrl = new URL(Utilities.GROUPS_URL);
		ContactGroupFeed resultFeed = contactsService.getFeed(feedUrl, ContactGroupFeed.class);
		// "My Contacts" group id will always be the first one in the answer
		ContactGroupEntry entry = resultFeed.getEntries().get(0);

		//return Integer.parseInt(entry.getId());
		return entry.getId();
	 
	 } catch (Exception e){
		
		System.out.println(e.getMessage().toString());
		return (e.getMessage().toString()); //for error;
	  
	  }
    }
    
    /*public void getContacts(String group) {
	  //variable holders
	  
	  String contactEmail = "";
	  String contactTitles = "";

	  
	  ContactsService contactsService = new ContactsService(Utilities.MY_PRODUCT_NAME);
	  contactsService.setHeader("Authorization", "Bearer " + googleAccessToken);

	  try {
		URL feedUrl = new URL(Utilities.CONTACTS_URL);
		Query myQuery = new Query(feedUrl);
			// to retrieve contacts of the group I found just above
		myQuery.setStringCustomParameter("group", group);
		myQuery.setMaxResults(Utilities.MAX_NB_CONTACTS);
		ContactFeed resultFeed = contactsService.query(myQuery, ContactFeed.class);
		
		
		for(int i=0; i<resultFeed.getEntries().size();i++){
		
		    ContactEntry cEntries = resultFeed.getEntries().get(i);
		    //store the title in a title setter
		     contactTitles = contactTitles + cEntries.getTitle().getPlainText() + ",";
		     
		    
		    //for email Addresses
		    for(Email email_: cEntries.getEmailAddresses()){
			 
			 contactEmail = contactEmail + email_.getAddress() + ",";		    
		   } 
		    
		}
		
		  setTitle(contactTitles);
		  setEmail(contactEmail);  
		
	  } catch (Exception e) {
	  
		//do nothing 
	  
	  }
   
   }*/
   
       public HashMap getContacts(String groupId) throws ServiceException, IOException {
        
	  ContactsService contactsService = new ContactsService(Utilities.MY_PRODUCT_NAME);
	  contactsService.setHeader("Authorization", "Bearer " + googleAccessToken);
	  
	   HashMap<String, String> myMap = new HashMap<String, String>();
	   
        // Request the feed
        URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full?max-results=100000");

        ContactFeed resultFeed = contactsService.getFeed(feedUrl, ContactFeed.class);
	String emailStr = "";
	String fullNameToDisplay="";
        // Print the results
        System.out.println(resultFeed.getTitle().getPlainText());
        for (int i = 0; i < resultFeed.getEntries().size(); i++) {
            ContactEntry entry = resultFeed.getEntries().get(i);
            if (entry.hasName()) {
                Name name = entry.getName();
                if (name.hasFullName()) {
                     fullNameToDisplay = name.getFullName().getValue();
                    if (name.getFullName().hasYomi()) {
                        fullNameToDisplay = " (" + name.getFullName().getYomi() + ")";
                    }
                    for (Email email : entry.getEmailAddresses()) {
                       //contactsList.add(new Userdata(fullNameToDisplay, email.getAddress()));
                       myMap.put(fullNameToDisplay,email.getAddress());

                    }


                } else {
                    System.out.println("\t\t (no full name found)");
                }
            }

        }
        
        setTitle(fullNameToDisplay);
        setEmail(emailStr);
        //return contactsList;
        return myMap;
    }
   
   public String[] generateArray(String original, String separator) {

		Vector<String> nodes = new Vector<String>();

		// Parse nodes into vector
		int index = original.indexOf(separator);
		while (index >= 0) {
			nodes.addElement(original.substring(0, index));
			original = original.substring(index + separator.length());
			index = original.indexOf(separator);
		}
		// Get the last node
		nodes.addElement(original);

		// Create splitted string array
		String[] result = new String[nodes.size() - 1];

		if (nodes.size() > 0) {
			for (int loop = 0; loop < nodes.size() - 1; loop++) {
				result[loop] = (String) nodes.elementAt(loop);
				// System.out.println(result[loop]); - uncomment this line to
				// see the result on output console
			}
		}

		return result;

	}
	

}