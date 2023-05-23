package com.template.service;

public interface SendSMSService {
	
	String sendTOTP(String processID, String phone, String message,String code,String source);

	String sendSMS(String processId, String phone, String message,String code,String source);

}
