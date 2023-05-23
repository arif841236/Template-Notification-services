package com.template.controller;


import com.template.common.dto.request.EmailSendDTO;
import com.template.common.dto.request.SMSSendDTO;
import com.template.common.exception.ServiceException;
import com.template.service.SMSService;
import com.template.service.SendEmailService;
import com.template.service.SendSMSService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URISyntaxException;
import java.util.Objects;

@RestController
@Hidden
public class SMSController {
	@Autowired
	private SMSService smsService;
	
	@Autowired
	private SendSMSService sendSMSService;
	
	@Autowired
	private SendEmailService sendEmailService;
	
	
	
	@PostMapping("/demo/send-sms")
	private String sendSMSDemo(@RequestBody SMSSendDTO smsSendDTO) throws URISyntaxException {
		
		try {
			return sendSMSService.sendTOTP(smsSendDTO.getProcessId(), smsSendDTO.getPhone(), smsSendDTO.getMessage(),smsSendDTO.getCode()+"_SMS",smsSendDTO.getSource());
		} catch (ResponseStatusException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}

	}
	
	@PostMapping("/demo/send-email")
	private String sendSMSDemo(@RequestBody EmailSendDTO emailSendDTO) throws URISyntaxException {
		
		try {
			if(Objects.isNull(emailSendDTO.getAttachment()))
			{
				return sendEmailService.sendSimpleMail(emailSendDTO);
			}
			else
			{
				return sendEmailService.sendMailWithAttachment(emailSendDTO);
			}
		} catch (ResponseStatusException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}

	}

}
