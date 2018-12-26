package com.vneto.java.utils.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {

	private String from = null;
	private String password = null;
	private String to = null;
	private String subject = null;
	private String body = null;
	   
	private Properties properties = new Properties();

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public MailSender(String from, String passwd, Properties props) {
		this.from = from;
		this.password = passwd;
		this.properties.putAll(props);
	}
	
	public MailSender(String from, String passwd) {
		this.from = from;
		this.password = passwd;
	}

	public MailSender() {

	}

	public Message createMail() throws AddressException, MessagingException {

		boolean auth = !isStrNullOrEmpty(from) && !isStrNullOrEmpty(password); 
		
		Session session = null;
		
		if(auth) {
			session = Session.getDefaultInstance(properties,  
	            new javax.mail.Authenticator() {
	               protected PasswordAuthentication 
	               getPasswordAuthentication() {
	                  return new 
	                  PasswordAuthentication(from, password);
	               }
	            }
	         );
		}
		else {
			properties.setProperty("mail.smtp.auth", "false");
			session = Session.getDefaultInstance(properties);
		}
		
         Message message = new MimeMessage(session);
         message.setFrom(new InternetAddress(from));
         message.setRecipients(Message.RecipientType.TO, 
            InternetAddress.parse(to));
         message.setSubject(subject);
         message.setText(body);
		
         System.out.println("Message: ");
         System.out.println("From: " + message.getFrom()[0]);
         System.out.println("To: " + message.getRecipients(Message.RecipientType.TO)[0]);
         System.out.println(message);
         
         return message;

	}

	public void sendMail(Message message) throws MessagingException {
		Transport.send(message);
	}

	private boolean isStrNullOrEmpty(String str) {
		return str == null || str.length() == 0;
	}
}
