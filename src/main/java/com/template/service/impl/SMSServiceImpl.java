package com.template.service.impl;


import com.template.common.dto.request.SMSDTO;
import com.template.common.dto.resposne.ResponseData;
import com.template.common.enums.Code;
import com.template.common.enums.PlaceHolder;
import com.template.common.enums.Type;
import com.template.common.exception.ServiceException;
import com.template.entities.Template;
import com.template.repository.TemplateRepository;
import com.template.service.SMSService;
import com.template.service.SendSMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
@Service
public class SMSServiceImpl implements SMSService {
	
	@Autowired
	private TemplateRepository templateRepository;
	
	@Autowired
	private SendSMSService sendSMSService;
	
	private static final String SUCCESS_MESSAGE = "SUCESS";
	
	
	@Override
	public ResponseData smsTOTP(SMSDTO smsDTO) {
				
		String message = this.sendOTPMessageCreation(smsDTO.getParameters(), Code.getCode(smsDTO.getCode()), Type.SMS);
		String responseMessage = this.sendOTPMessageCreation(smsDTO.getParameters(), Code.getCode(smsDTO.getCode()), Type.ALERT);
		
		
		sendSMSService.sendTOTP(smsDTO.getProcessId(), smsDTO.getPhone(), message,Code.getCode(smsDTO.getCode()).getValue(),smsDTO.getSource());
		
		ResponseData responseData = new ResponseData();
		responseData.setMessage(SUCCESS_MESSAGE);
		responseData.setStatus(Boolean.TRUE);
		
		Map<String, String> map = new HashMap<>();
		map.put("message", responseMessage);
		responseData.setData(map);
		return responseData;
	}

	@Override
	public String getSmsTemplateWithDataFilled(Map<String, Object> map, Code code) {
		return sendOTPMessageCreation(map,code,Type.SMS);
	}


	public synchronized String sendOTPMessageCreation(Map<String, Object> map, Code code, Type type)
	{

		List<Template> template = templateRepository.findByCodeAndType(code.getValue(), type.getValue()).orElseThrow(() -> new ServiceException("Template doesn't exist"));

		StringBuilder message = Objects.isNull(template.get(0).getMessage()) ? new StringBuilder("") : new StringBuilder(template.get(0).getMessage());
		if(!Objects.isNull(map)) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				String placeholder = PlaceHolder.getPlaceHolder(entry.getKey()).getValue();
				System.out.println(placeholder + " " + entry.getValue());
				message = new StringBuilder((message.toString().replaceAll(Pattern.quote(placeholder), entry.getValue().toString())));
			}
		}
		return message.toString();
		
	}

	

}
