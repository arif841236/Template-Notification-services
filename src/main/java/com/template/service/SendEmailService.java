package com.template.service;


import com.template.common.dto.request.EmailSendDTO;

public interface SendEmailService {
	
	public String sendSimpleMail(EmailSendDTO details);
	
	public String sendMailWithAttachment(EmailSendDTO details);

}
