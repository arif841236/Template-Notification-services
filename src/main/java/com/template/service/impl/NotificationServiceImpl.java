package com.template.service.impl;


import com.template.common.dto.request.GetTemplateRequestDto;
import com.template.common.dto.request.SMSDTO;
import com.template.common.dto.request.SendNotificationRequestDto;
import com.template.common.dto.resposne.ResponseData;
import com.template.common.enums.Code;
import com.template.common.enums.Medium;
import com.template.common.exception.ServiceException;
import com.template.common.utility.CommonUtils;
import com.template.common.utility.NotificationSender;
import com.template.entities.Notification;
import com.template.entities.NotificationLog;
import com.template.entities.Template;
import com.template.repository.NotificationLogRepository;
import com.template.repository.NotificationRepository;
import com.template.repository.TemplateRepository;
import com.template.service.EmailService;
import com.template.service.NotificationService;
import com.template.service.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private TemplateRepository templateRepository;


    @Autowired
    private NotificationLogRepository notificationLogRepository;
    
    @Autowired
    private SMSService smsService;
    
    @Autowired
    private EmailService emailService;


    private  List<String > mediums = List.of("email","phone");
    
    @Override
	public ResponseData sendMessage(SMSDTO smsDTO) {
		
//		if(!CommonUtils.validatePhone(smsDTO.getPhone()))
//		{
//			if(!CommonUtils.validateEmail(smsDTO.getPhone()))
//			{
//				throw new ServiceException("Invalid phone");
//			}
//		}
		CommonUtils.validateCode(smsDTO.getCode());
		CommonUtils.validateParameters(smsDTO.getParameters());
		CommonUtils.validateProcessId(smsDTO.getProcessId());
		CommonUtils.validateMedium(smsDTO.getMedium());
        CommonUtils.validateSource(smsDTO);
        CommonUtils.validateEmailAndMobile(smsDTO);
		ResponseData response = null;
		
		switch(Medium.getMedium(smsDTO.getMedium()))
		{
			case EMAIL:
				response = emailService.sendEmail(smsDTO);
				break;
			case SMS:
				response = smsService.smsTOTP(smsDTO);
				break;
			case BOTH:
				response = smsService.smsTOTP(smsDTO);
				emailService.sendEmail(smsDTO);
				break;
			default:
				throw new ServiceException("Invalid medium");
				
		}
		return response;
	}

    @Override
    public ResponseData getTemplate(GetTemplateRequestDto request) {

        CommonUtils.validateCode(request.getCode());
        CommonUtils.validateParameters(request.getParameters());
        CommonUtils.validateMedium(request.getMedium());
        Map<String,String> templateList = new HashMap<>();

        String emailTemplate  = "";
        String smsTemplate  = "";

        switch(Medium.getMedium(request.getMedium()))
        {
            case EMAIL:
                emailTemplate = emailService.getEmailTemplateWithDataFilled(request.getParameters(), Code.getCode(request.getCode()));
                templateList.put("emailTemplate",emailTemplate);
                break;
            case SMS:
                smsTemplate = smsService.getSmsTemplateWithDataFilled(request.getParameters(), Code.getCode(request.getCode()));
                templateList.put("smsTemplate",smsTemplate);
                break;
            case BOTH:
                smsTemplate = smsService.getSmsTemplateWithDataFilled(request.getParameters(), Code.getCode(request.getCode()));
                templateList.put("smsTemplate",smsTemplate);
                emailTemplate  = emailService.getEmailTemplateWithDataFilled(request.getParameters(), Code.getCode(request.getCode()));
                templateList.put("emailTemplate",emailTemplate);
                break;
            default:
                throw new ServiceException("Invalid medium");

        }





        ResponseData responseData = new ResponseData();


        responseData.setStatus(true);
        responseData.setMessage("Successful");
        responseData.setData(templateList);

        return responseData;
    }


    @Override
    public ResponseData sendNotification(SendNotificationRequestDto sendNotificationRequestDto) {

        this.validateSendNotificationRequestDtoData(sendNotificationRequestDto);

        ResponseData responseData = this.sendNotificationToGivenMediums(sendNotificationRequestDto);

        return responseData;
    }


    private void validateSendNotificationRequestDtoData(SendNotificationRequestDto sendNotificationRequestDto){


        Map<String,String> errorMessage = new HashMap<>();

        if(Objects.isNull(sendNotificationRequestDto)){
            errorMessage.put("requestBody", "Request Body Can not be null");
        } else {
            if (Objects.isNull(sendNotificationRequestDto.getSentTo()) || sendNotificationRequestDto.getSentTo().equals("")) {
                errorMessage.put("Sent To", "sent to field can not be null");
            }
             if (Objects.isNull(sendNotificationRequestDto.getTemplateCode()) || sendNotificationRequestDto.getTemplateCode().equals("")) {
                errorMessage.put("Template Code", "Template Code field can not be null");
            }




             if (Objects.isNull(sendNotificationRequestDto.getMedium()) || sendNotificationRequestDto.getMedium().size()==0) {
                errorMessage.put("Medium", "Medium field can not be null");
            }
             else  {

                 for (String m:sendNotificationRequestDto.getMedium()) {
                    if(!mediums.contains(m)){
                        errorMessage.put("Template Code", "Template Code "+m+" is invalid");
                    }
                 }

             }

//            if (Objects.isNull(sendNotificationRequestDto.getData()) || sendNotificationRequestDto.getData().size()==0) {
//                errorMessage.put("Data ", "Data field can not be null");
//            }
        }

        if(errorMessage.size()!=0){
            System.out.println(errorMessage);
            throw new ServiceException("Invalid Input",errorMessage);
        }
    }

    private ResponseData sendNotificationToGivenMediums(SendNotificationRequestDto sendNotificationRequestDto){
        ResponseData responseData = new ResponseData();

        List<String> mediums = sendNotificationRequestDto.getMedium();

        List<Template> template = templateRepository.findByCode(sendNotificationRequestDto.getTemplateCode()).orElseThrow(() -> new ServiceException("Invalid Template code"));


        String message = template.get(0).getMessage();

        // user data needed to be add dynamicaly    to be done later

        NotificationSender notificationSender = new NotificationSender();

        for(String medium:mediums){
            Map<String, String> requestResponse = notificationSender.send(medium, sendNotificationRequestDto.getSentTo(), message);
            Notification notification = saveNotification(sendNotificationRequestDto.getSentTo(), template.get(0), message);
            saveRequestResponse(requestResponse,notification);
        }
        responseData.setStatus(true);
        responseData.setMessage("SUCCESSFUL");
        responseData.setData("Notification Sent Successfully");

        return responseData;
    }

    private void saveRequestResponse(Map<String, String> requestResponse, Notification notification){
        NotificationLog notificationLog = new NotificationLog();

        notificationLog.setRequest(requestResponse.get("request"));
        notificationLog.setResponse(requestResponse.get("response"));
        notificationLog.setNotification(notification);
        notificationLog.setRequestAt(LocalDateTime.parse(requestResponse.get("requestAt"), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        notificationLog.setResponseAt(LocalDateTime.parse(requestResponse.get("responseAt"), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        notificationLogRepository.save(notificationLog);
    }

    private Notification saveNotification(String sentTo,Template template,String message){
        Notification newNotification = new Notification();
        newNotification.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        newNotification.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        newNotification.setTemplate(template);
        newNotification.setSentTo(sentTo);
        newNotification.setActualMessage(message);
       return notificationRepository.save(newNotification);
    }



}
