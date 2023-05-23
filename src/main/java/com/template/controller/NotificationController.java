package com.template.controller;



import com.template.common.dto.request.GetTemplateRequestDto;
import com.template.common.dto.request.SMSDTO;
import com.template.common.dto.request.SendNotificationRequestDto;
import com.template.common.dto.resposne.ResponseData;
import com.template.common.exception.ServiceException;
import com.template.service.NotificationService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send-notification")
    @Hidden
    public ResponseData sendNotification(@RequestBody SendNotificationRequestDto sendNotificationRequestDto){
        return this.notificationService.sendNotification(sendNotificationRequestDto);
    }
    
    @PostMapping("/send-message")
	private ResponseData sendMessage(@RequestBody SMSDTO smsDTO) throws URISyntaxException {
		
//		try {
			return notificationService.sendMessage(smsDTO);
//		} catch (ResponseStatusException e) {
//			throw e;
//		}
//		catch (Exception e) {
//			throw new ServiceException(e.getMessage());
//		}

	}

    @PostMapping("/get-template")
    private ResponseData getTemplate(@RequestBody GetTemplateRequestDto requestDto)  {


        return notificationService.getTemplate(requestDto);
    }
}
