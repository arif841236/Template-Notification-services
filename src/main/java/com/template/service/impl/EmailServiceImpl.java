package com.template.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.template.common.dto.request.EmailSendDTO;
import com.template.common.dto.request.SMSDTO;
import com.template.common.dto.resposne.ResponseData;
import com.template.common.enums.Code;
import com.template.common.enums.PlaceHolder;
import com.template.common.enums.Type;
import com.template.common.exception.ServiceException;
import com.template.entities.Template;
import com.template.repository.TemplateRepository;
import com.template.service.EmailService;
import com.template.service.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class EmailServiceImpl implements EmailService {
	
	@Autowired
	SendEmailService sendEmailService;
	
	@Autowired
	private TemplateRepository templateRepository;
	
	private static final String SUCCESS_MESSAGE = "SUCESS";
	
	@Override
	public ResponseData sendEmail(SMSDTO smsDTO) {

		ObjectMapper objectMapper = new ObjectMapper();
		
		Map<String, Object> parameters = smsDTO.getParameters();
	
		String message = this.emailMessageCreation(parameters, Code.getCode(smsDTO.getCode()), Type.EMAIL);
		String responseMessage = this.emailMessageCreation(parameters, Code.getCode(smsDTO.getCode()), Type.ALERT);


		byte[] attachment = null;
		String fileName = "";

		//------
		if(!Objects.isNull(parameters)) {
			for(Map.Entry<String, Object> entry: parameters.entrySet())
			{
				if(entry.getKey() == PlaceHolder.ATTACHMENT.getKey())
				{
					try {
						attachment = objectMapper.readValue(entry.getValue().toString(),byte[].class);
					} catch (JsonProcessingException e) {
						throw new RuntimeException(e);
					}
//					attachment = entry.getValue();
				}
				else if(entry.getKey() == PlaceHolder.FILE_NAME.getKey()){
					fileName = entry.getValue().toString();
				}

			}
		}
//// ------------


		EmailSendDTO emailSendDTO =EmailSendDTO.builder()
		.code(Code.getCode(smsDTO.getCode()).getValue())
		.processId(smsDTO.getProcessId())
		.recipient(smsDTO.getEmail())
		.msgBody(message)
		.subject("INT Origin")
		.attachment(attachment)
		.attachedFileName(fileName)
		.source(smsDTO.getSource())
		.build();
		
		if(Objects.isNull(attachment))
		{
			sendEmailService.sendSimpleMail(emailSendDTO);
		}
		else
		{
			sendEmailService.sendMailWithAttachment(emailSendDTO);
		}
		
		ResponseData responseData = new ResponseData();
		responseData.setMessage(SUCCESS_MESSAGE);
		responseData.setStatus(Boolean.TRUE);
		
		Map<String, String> map = new HashMap<>();
		map.put("message", responseMessage);
		responseData.setData(map);
		return responseData;
	}

	@Override
	public String getEmailTemplateWithDataFilled(Map<String, Object> map, Code code) {

		return emailMessageCreation(map,code,Type.EMAIL);

	}


	public synchronized String emailMessageCreation(Map<String, Object> map, Code code, Type type)
	{

		List<Template> template = templateRepository.findByCodeAndType(code.getValue(), type.getValue()).orElseThrow(() -> new ServiceException("Template doesn't exist"));

		StringBuilder message = Objects.isNull(template.get(0).getMessage()) ? new StringBuilder("") : new StringBuilder(template.get(0).getMessage());
		if(!Objects.isNull(map)) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				if(entry.getKey() != PlaceHolder.ATTACHMENT.getKey()){
					String placeholder = PlaceHolder.getPlaceHolder(entry.getKey()).getValue();
					System.out.println(placeholder + " " + entry.getValue());
					message = new StringBuilder((message.toString().replaceAll(Pattern.quote(placeholder), entry.getValue().toString())));
				}

			}
		}
		return message.toString();
		
	}


}
