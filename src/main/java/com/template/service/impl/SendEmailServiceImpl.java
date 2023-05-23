package com.template.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.common.dto.request.EmailSendDTO;
import com.template.common.exception.ServiceException;
import com.template.common.utility.RequestResponseLogUtil;
import com.template.repository.RequestResponseLogRepository;
import com.template.service.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Service
public class SendEmailServiceImpl implements SendEmailService {
	
	  @Autowired 
	  private JavaMailSender javaMailSender;
	  
	  @Autowired
		private RequestResponseLogUtil requestResponseLogUtil;
		
	@Autowired
	private RequestResponseLogRepository requestResponseLogRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
	  
    @Value("${spring.mail.username}") 
    private String sender;
    
    @Autowired
    private Environment environment;
	 
	    // Method 1
	    // To send a simple email
	    @Override
	    public String sendSimpleMail(EmailSendDTO details)
	    {
	 
	    	try {
	        
		            // Creating a simple mail message
		    	MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		        MimeMessageHelper mimeMessageHelper;
		 
		            // Setting up necessary details
		            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
		            mimeMessageHelper.setFrom(sender);
		            mimeMessageHelper.setTo(details.getRecipient());
		            mimeMessageHelper.setText(details.getMsgBody(), true);
		            mimeMessageHelper.setSubject(details.getSubject());
		            
		            String code = details.getCode();
		           String request = "Sender: "+sender+
		        		   ", To: "+details.getRecipient()+
		        		   ", Text: "+details.getMsgBody()+
		        		   ", Subject: "+details.getSubject();
		           String processID = details.getProcessId();
		           LocalDateTime requestedAt = LocalDateTime.now(ZoneId.of("UTC"));
		           try {
		            // Sending the mail
		            javaMailSender.send(mimeMessage);
		            requestResponseLogUtil.saveRequestResponseLog(processID, request, "SUCCESS", code+"_EMAIL", requestedAt, LocalDateTime.now(ZoneId.of("UTC")), Integer.parseInt(environment.getProperty("success")),details.getSource());
		            return "SUCESS";
		        }
		        catch (Exception e) {
		        	e.printStackTrace();
		        	requestResponseLogUtil.saveRequestResponseLog(processID, request, e.getMessage(), code+"_EMAIL", requestedAt, LocalDateTime.now(ZoneId.of("UTC")), Integer.parseInt(environment.getProperty("failure")),details.getSource());
		        	new ServiceException("Email could not be send");
		        }
	    	}
	        catch (Exception e) {
	        	e.printStackTrace();
	        	new ServiceException("Email could not be send");
	        }
	        return null;
	    }



	// Method 2
	// To send an email with attachment
	@Override
	public String sendMailWithAttachment(EmailSendDTO details) {
		// Creating a mime message

		String request = "Sender: "+sender+
				", To: "+details.getRecipient()+
				", Text: "+details.getMsgBody()+
				", Subject: "+details.getSubject();

		try {


			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,
					MimeMessageHelper.MULTIPART_MODE_MIXED,
					StandardCharsets.UTF_8.name());


			ByteArrayDataSource dataSource = new ByteArrayDataSource(details.getAttachment(), MediaType.APPLICATION_PDF_VALUE);
			mimeMessageHelper.addAttachment(details.getAttachedFileName(), dataSource);
			mimeMessageHelper.setTo(details.getRecipient());
			mimeMessageHelper.setSubject(details.getSubject());
			mimeMessageHelper.setText(details.getMsgBody());

			javaMailSender.send(message);
			requestResponseLogUtil.saveRequestResponseLog(details.getProcessId(), request, "SUCCESS", details.getCode()+"_EMAIL", LocalDateTime.now(ZoneId.of("UTC")), LocalDateTime.now(ZoneId.of("UTC")), Integer.parseInt(environment.getProperty("success")),details.getSource());

		} catch (MessagingException e) {
			e.printStackTrace();
			requestResponseLogUtil.saveRequestResponseLog(details.getProcessId(), request, e.getMessage(), details.getCode()+"_EMAIL", LocalDateTime.now(ZoneId.of("UTC")), LocalDateTime.now(ZoneId.of("UTC")), Integer.parseInt(environment.getProperty("failure")),details.getSource());

			throw new RuntimeException(e);

		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);

		}
		return "SUCCESS";
	}















//	    @Override
//	    public String sendMailWithAttachment(EmailSendDTO details)
//	    {
//	        // Creating a mime message
//	        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//	        MimeMessageHelper mimeMessageHelper;
//
//
//	        try {
//	            // Setting multipart as true for attachments to
//	            // be send
//	            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//	            mimeMessageHelper.setFrom(sender);
//	            mimeMessageHelper.setTo(details.getRecipient());
//	            mimeMessageHelper.setText(details.getMsgBody());
//	            mimeMessageHelper.setSubject(details.getSubject());
//
//	            String code = details.getCode();
//	           String request = "";
//	           String processID = details.getProcessId();
//
//	           try {
//					request = objectMapper.writeValueAsString(mimeMessageHelper);
//				} catch (JsonProcessingException e1) {
//					e1.printStackTrace();
//				}
//	           LocalDateTime requestedAt = LocalDateTime.now(ZoneId.of("UTC"));
//	           try{
//	            // Adding the attachment
//	            FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));
//
//	            mimeMessageHelper.addAttachment(file.getFilename(), file);
//	            javaMailSender.send(mimeMessage);
//	            requestResponseLogUtil.saveRequestResponseLog(processID, request, "SUCCESS", code+"_EMAIL", requestedAt, LocalDateTime.now(ZoneId.of("UTC")), Integer.parseInt(environment.getProperty("success")));
//	            return "SUCCESS";
//	           }
//	           catch(Exception e)
//	           {
//	        	   e.printStackTrace();
//		        requestResponseLogUtil.saveRequestResponseLog(processID, request, e.getMessage(), code+"_EMAIL", requestedAt, LocalDateTime.now(ZoneId.of("UTC")), Integer.parseInt(environment.getProperty("failure")));
//		        new ServiceException("Email could not be send");
//	           }
//	        }
//	        catch (Exception e) {
//	        	e.printStackTrace();
//	        	new ServiceException("Email could not be send");
//	        }
//	        return null;
//	    }

	
}
