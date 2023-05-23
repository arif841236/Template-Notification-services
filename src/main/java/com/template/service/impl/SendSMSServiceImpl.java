package com.template.service.impl;


import com.template.common.config.AWSConfig;
import com.template.common.exception.ServiceException;
import com.template.common.utility.RequestResponseLogUtil;
import com.template.entities.RequestResponseLog;
import com.template.repository.RequestResponseLogRepository;
import com.template.service.SNSService;
import com.template.service.SendSMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Service
public class SendSMSServiceImpl implements SendSMSService {
	
	@Autowired
	private RequestResponseLogUtil requestResponseLogUtil;
	
	@Autowired
	private RequestResponseLogRepository requestResponseLogRepository;
	
	@Autowired
	private SNSService snsService;
	
	@Autowired
	private Environment environment;
	
	private static final String ARN = "arn:aws:sns:ap-south-1:183629752129:LOS-OTP";
	
	private static final String successResponse = "SUCCESS";
	
	@Override
	public String sendTOTP(String processID, String phone, String message,String code,String source) {
		System.out.println("processID: "+processID+", phone: "+phone+", message: "+message);
		RequestResponseLog requestResponseLog = requestResponseLogRepository.findByProcessIdAndActivityTypeAndStatus(processID, environment.getProperty("activitytype.subscription"), Integer.parseInt(environment.getProperty("success")));
		if(Objects.isNull(requestResponseLog))
		{
			if(snsService.addSubscriberToTopic(processID, ARN, phone).equals(successResponse))
			{
				sendSMS(processID, phone, message,code,source);
			}
			else
			{
				throw new ServiceException("Failed to Subscribe to the Topic.");
			}
		}
		else
		{
			sendSMS(processID, phone, message,code,source);
		}
		return successResponse;
	}

	@Override
	public String sendSMS(String processId, String phone, String message,String code,String source) {
	    SnsClient snsClient;
		try {
			snsClient = AWSConfig.getSnsClient();
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}

	    final PublishRequest publishRequest = PublishRequest.builder()
	            .phoneNumber(phone)
	            .message(message)
	            .build();

	    PublishResponse publishResponse = null;
	    LocalDateTime requestedAt = null;
	    String request="";
	    try {
	    	requestedAt = LocalDateTime.now(ZoneId.of("UTC"));
	    	request = message.replaceAll("[0-9]", "X");
	    	publishResponse = snsClient.publish(publishRequest);
	    }
	    catch(Exception e)
	    {
	    	requestResponseLogUtil.saveRequestResponseLog(processId, request, e.getMessage(), code+"_SMS", requestedAt, LocalDateTime.now(ZoneId.of("UTC")), Integer.parseInt(environment.getProperty("failure")),source);
	    	throw new ServiceException(e.getMessage());
	    }
	    finally
	    {
	    	snsClient.close();
	    }
	    
    	if (publishResponse.sdkHttpResponse().isSuccessful()) {
	        System.out.println("Message publishing to phone successful");
	        requestResponseLogUtil.saveRequestResponseLog(processId, request, "Message publishing to phone successful. " + "Message-ID: " + publishResponse.messageId(),code+"_SMS", requestedAt, LocalDateTime.now(ZoneId.of("UTC")), Integer.parseInt(environment.getProperty("success")),source);
	    } else {
	    	requestResponseLogUtil.saveRequestResponseLog(processId, request, publishResponse.sdkHttpResponse().statusText().orElse("")
	    		, code+"_SMS", requestedAt, LocalDateTime.now(ZoneId.of("UTC")), Integer.parseInt(environment.getProperty("failure")),source);
	        throw new ResponseStatusException(
	            HttpStatus.INTERNAL_SERVER_ERROR, publishResponse.sdkHttpResponse().statusText().orElse("")
	        );
	    }
    	
	   
	    return successResponse;
		
	}

	

}
