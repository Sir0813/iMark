package com.dm.user.util;

import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SendMailSmtp extends Authenticator{
	
	@Value("${email.address}")
	private String address;
	
	@Value("${email.smtp}")
	private String smtp;
	
	@Value("${email.pwd}")
	private String pwd;
	
	public boolean sendEmail(String toAddress, String content,String emailSbuject) {
		boolean flag = false ;
		try {
			Properties props = new Properties();
			props.setProperty("mail.transport.protocol", "smtp");
			props.setProperty("mail.smtp.auth", "true");
			props.setProperty("mail.host", "smtp.163.com");
			props.setProperty("mail.smtp.port", "465");
			props.setProperty("mail.smtp.ssl.enable", "true");
			/*props.setProperty("mail.smtp.socketFactory.port", "465");
			props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.smtp.timeout", "10000");
			props.setProperty("mail.smtp.connectiontimeout", "10000");
			props.setProperty("mail.smtp.writetimeout", "10000");*/
			Session session = Session.getInstance(props);
			session.setDebug(false);
			Transport transport = session.getTransport();
			transport.connect(smtp, address,  pwd);
			Message message = new MimeMessage(session);
			message.setSentDate(new Date());
			String nick="";//邮件主题下面的姓名  
	        message.setFrom(new InternetAddress(nick+" <"+address+">")); 
	        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));  
			message.setSubject(MimeUtility.encodeText(emailSbuject, "gb2312", "b"));
			/*
			MimeMultipart contents = new MimeMultipart();
			正文HTML
			MimeBodyPart body = new MimeBodyPart();
			body.setContent(content, "text/html;charset=gb2312");
			contents.addBodyPart(body);
			List<String> emails = new ArrayList<String>();
			emails.add(toAddress);
			transport.sendMessage(message, InternetAddress.parse(emails.toString().replace("[", "").replace("]", "")));
			*/
			message.setText(content);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			flag = true ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag ;
	}
	
}
