package com.harambesa.mailing;

// import javax.naming.*;
// import java.util.Properties;
// import java.io.UnsupportedEncodingException;
// import javax.mail.Message;
// import javax.mail.MessagingException;
// import javax.mail.Session;
// import javax.mail.Transport;
// import javax.mail.internet.AddressException;
// import javax.mail.internet.InternetAddress;
// import javax.mail.internet.MimeMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;
import com.microtripit.mandrillapp.lutung.view.MandrillUserInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
//import com.harambesa.gServices.HarambesaUtils;

public class Mail{

Logger log = Logger.getLogger(Mail.class.getName());
//constructor
public Mail(){

}

 public void sendMail( String f_name, String email,String token,String token_id){
        MandrillApi mandrillApi = new MandrillApi("KNSchva5NG0YvP_a4o2STg");
        String msgBody = "<p><span style=\"font-size: small; font-family: arial black,avant garde;\">" +
                "Hi "+f_name+",</span></p><p><span style=\"font-family: arial black,avant garde;\">" +
                "Thank you for joining <span class=\"il\">harambesa</span>. " +
                "You have received this email because you signed up for <span class=\"il\">Harambesa</span>." +
                "</span></p><p><span style=\"font-family: arial black,avant garde;\">" +
                "Please click <a class=\"bold\" href=\"197.232.19.22:8080/mjet/confirmation/?id="+token_id+"&token="
                +token+"\" target=\"_blank\">here</a> " +
                "to activate your account or copy and paste the URL on your browser.&nbsp;</span></p>" +
                "<p><br /><span style=\"font-family: arial black,avant garde;\">//197.232.19.22:8080/mjet/confirmation/?id="
                +token_id+"&token="+token+"</span></p><p><span style=\"font-family: arial black,avant garde;\">&nbsp;</span>" +
                "</p><p><span style=\"font-family: arial black,avant garde;\">" +
                "If you feel this is an error or you did not sign up with us, please contact us on admin@harambesa.co.ke</span>" +
                "</p><p><span style=\"font-family: arial black,avant garde;\"><br />" +
                "Thank you,<br /></span></p><p><span style=\"font-family: arial black,avant garde;\">" +
                "The Harambesa Team.<br /></span></p>";


// create your message
        MandrillMessage message = new MandrillMessage();
        message.setSubject("Harambesa");
        message.setHtml(msgBody);
        message.setAutoText(true);
        message.setFromEmail("admin@harambesa.co.ke");
        message.setFromName("Admin");
// add recipients
        ArrayList<MandrillMessage.Recipient> recipients = new ArrayList<MandrillMessage.Recipient>();
        MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
        recipient.setEmail(email);
        recipient.setName(f_name);
        recipients.add(recipient);
        message.setTo(recipients);
        message.setPreserveRecipients(false);
        try {
            MandrillMessageStatus[] messageStatusReports = mandrillApi
                    .messages().send(message, false);
        } catch (MandrillApiError mandrillApiError) {
            mandrillApiError.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void sendPasswordResetMail(String email,String f_name,String passwd_reset_hash,int token_id){
        MandrillApi mandrillApi = new MandrillApi("KNSchva5NG0YvP_a4o2STg");
        String msgBody = "<p>Hi "+f_name+",</p><p><br />We recently received a request to reset your" +
                " <span class='il'>harambesa</span> account password.</p>" +
                "<p>If this was you, please click on the link below to reset your password." +
                "</p>197.232.19.22:8080/mjet/reset-password?id="+token_id+"&token="+passwd_reset_hash+"</p>" +
                "<p>If you are not the one who made this request, please ignore this message.</p><p>&nbsp;</p>" +
                "<p><span style='font-family: arial black,avant garde;'>Thanks,<br /></span></p><p>" +
                "<span style='font-family: arial black,avant garde;'>" +
                "Your friends at <span class='il'>Harambesa</span>.</span></p>";
// create your message
        MandrillMessage message = new MandrillMessage();
        message.setSubject("Harambesa");
        message.setHtml(msgBody);
        message.setAutoText(true);
        message.setFromEmail("admin@harambesa.co.ke");
        message.setFromName("Admin");
// add recipients
        ArrayList<MandrillMessage.Recipient> recipients = new ArrayList<MandrillMessage.Recipient>();
        MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
        recipient.setEmail(email);
        recipient.setName(f_name);
        recipients.add(recipient);
        message.setTo(recipients);
        message.setPreserveRecipients(false);
        try {
            MandrillMessageStatus[] messageStatusReports = mandrillApi
                    .messages().send(message, false);
        } catch (MandrillApiError mandrillApiError) {
            mandrillApiError.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }




}
    public void sendInvitationMail( String invited_name, String invitor_name, String email){
        MandrillApi mandrillApi = new MandrillApi("KNSchva5NG0YvP_a4o2STg");
        String msgBody = "<p>Hi "+invited_name+",</p><p><br />"+invitor_name+" has invited you to join harambesa.</p>";
        msgBody +="<p>Please Click <a class='bold' href='197.232.19.22:8080/mjet/signup' target='_blank'>here</a> to join.</p>";
        msgBody +="<p>&nbsp;</p>";
        msgBody +="<p><span style='font-family: arial black,avant garde;'>Thank you,<br /></span></p>";
        msgBody +="<p><span style='font-family: arial black,avant garde;'>The Harambesa Team.</span></p>";
        msgBody +="<p>&nbsp;</p>";
        // create your message
        MandrillMessage message = new MandrillMessage();
        message.setSubject("Harambesa");
        message.setHtml(msgBody);
        message.setAutoText(true);
        message.setFromEmail("admin@harambesa.co.ke");
        message.setFromName("Admin");
// add recipients
        ArrayList<MandrillMessage.Recipient> recipients = new ArrayList<MandrillMessage.Recipient>();
        MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
        recipient.setEmail(email);
        recipient.setName(invited_name);
        recipients.add(recipient);
        message.setTo(recipients);
        message.setPreserveRecipients(false);
        try {
            MandrillMessageStatus[] messageStatusReports = mandrillApi
                    .messages().send(message, false);
        } catch (MandrillApiError mandrillApiError) {
            mandrillApiError.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendInvitationMail(String invitor_name,String email){
        MandrillApi mandrillApi = new MandrillApi("KNSchva5NG0YvP_a4o2STg");
        // String invited_name = "Emmanuel";
        String msgBody = "<p>Hi,</p><p><br />"+invitor_name+" has invited you to join harambesa.</p>";
        msgBody +="<p>Please Click <a class='bold' href='197.232.19.22:8080/mjet/signup' target='_blank'>here</a> to join.</p>";
        msgBody +="<p>&nbsp;</p>";
        msgBody +="<p><span style='font-family: arial black,avant garde;'>Thank you,<br /></span></p>";
        msgBody +="<p><span style='font-family: arial black,avant garde;'>The Harambesa Team.</span></p>";
        msgBody +="<p>&nbsp;</p>";
        // create your message
        MandrillMessage message = new MandrillMessage();
        message.setSubject("Harambesa");
        message.setHtml(msgBody);
        message.setAutoText(true);
        message.setFromEmail("admin@harambesa.co.ke");
        message.setFromName("Admin");
// add recipients
        ArrayList<MandrillMessage.Recipient> recipients = new ArrayList<MandrillMessage.Recipient>();
        MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
        recipient.setEmail(email);
        // recipient.setName(invited_name);
        recipients.add(recipient);
        message.setTo(recipients);
        message.setPreserveRecipients(false);
        try {
            MandrillMessageStatus[] messageStatusReports = mandrillApi
                    .messages().send(message, false);
        } catch (MandrillApiError mandrillApiError) {
            mandrillApiError.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}