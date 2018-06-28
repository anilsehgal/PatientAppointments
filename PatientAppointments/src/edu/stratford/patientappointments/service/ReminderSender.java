package edu.stratford.patientappointments.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ReminderSender {


	Properties mailServerProperties;
	Session getMailSession;
	MimeMessage generateMailMessage;
	
	public static ReminderSender INSTANCE = new ReminderSender();
	
	private ReminderSender(){}
	
	public void generateAndSendEmail(String to, String cc, String from, String subject, String content) throws AddressException, MessagingException {
 
		
		content = content.replaceAll("\n", "<br>");
		content = content.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
		// Step1
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		mailServerProperties.put("mail.smtp.ssl.trust", "*");
		// Step2
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("anil.sehgal10@gmail.com"));
		generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
		generateMailMessage.setSubject(subject);
		String emailBody = content;
		generateMailMessage.setContent(emailBody, "text/html");
 
		// Step3
		Transport transport = getMailSession.getTransport("smtp");
 
		// Enter your correct gmail UserID and Password
		// if you have 2FA enabled then provide App Specific Password
		transport.connect("smtp.gmail.com", "anil.sehgal10@gmail.com", "bakuryu1");
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();
	}
}
