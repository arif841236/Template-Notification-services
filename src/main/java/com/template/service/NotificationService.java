package com.template.service;


import com.template.common.dto.request.GetTemplateRequestDto;
import com.template.common.dto.request.SMSDTO;
import com.template.common.dto.request.SendNotificationRequestDto;
import com.template.common.dto.resposne.ResponseData;

public interface NotificationService {

    public ResponseData sendNotification(SendNotificationRequestDto sendNotificationRequestDto);
    
    public ResponseData sendMessage(SMSDTO smsDTO);

    public ResponseData getTemplate(GetTemplateRequestDto request);

}
