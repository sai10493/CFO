package com.verizon.cfo.servlet;

	import javax.mail.Authenticator;
	import javax.mail.Message;
	import javax.mail.PasswordAuthentication;
	import javax.mail.Session;
	import javax.mail.Transport;
	import javax.mail.internet.InternetAddress;
	import javax.mail.internet.MimeMessage;

	public class EmailerClass {
		int cid;
		  String toEmail = ""; 
		  
		EmailerClass(String email)
		{
			toEmail=email;
			
		}
		


		public boolean send(String emailMessage){

		    boolean m=false; 
			try{
		         final String fromEmail = "wemonitor.verizon@gmail.com"; //requires valid gmail id
		         final String password = "Weshallpass";// correct password for gmail id
		         System.out.println("add password to program");
		         
		       // can be any email id 

		         System.out.println("TLSEmail Start");
		         java.util.Properties props = System.getProperties();
		         props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
		         props.put("mail.smtp.port", "587"); //TLS Port
		         props.put("mail.smtp.auth", "true"); //enable authentication
		         props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

		             //create Authenticator object to pass in Session.getInstance argument
		         Authenticator auth = new Authenticator()
		        {
		             //override the getPasswordAuthentication method
		             protected PasswordAuthentication getPasswordAuthentication() {
		                 return new PasswordAuthentication(fromEmail, password);
		             }

		         };
		         
		         
		         
				          
		Session session = Session.getInstance(props, auth);

		         MimeMessage message = new MimeMessage(session);
		         message.setFrom(new InternetAddress(fromEmail));
		         message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

		         System.out.println("Mail Check 2");

		         message.setSubject("Verizon Finance-Service Report");
		         message.setText(emailMessage);

		         System.out.println("Mail Check 3");

		         Transport.send(message);
		         System.out.println("Mail Sent");
		         
		         m=true;
		     }catch(Exception ex){
		         System.out.println("Mail fail");
		         System.out.println(ex);
		     }
			return m;
		 }

	}


