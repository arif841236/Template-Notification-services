package com.template.controller;


import com.template.common.dto.request.SNSSubscriptionDTO;
import com.template.common.exception.ServiceException;
import com.template.service.SNSService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URISyntaxException;

@RestController
@Hidden
public class SNSController {
	
	
	@Autowired
	private SNSService snsService;
	
	@PostMapping("/create-topic")
	private String createTopic(@RequestParam("topic_name") String topicName) throws URISyntaxException {
		System.out.println("in createTopic");
		try {
			return snsService.createTopic(topicName);
		} catch (ResponseStatusException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
	@PostMapping("/add-subscribers")
	private String addSubscriberToTopic(@RequestParam("arn") String arn, @RequestBody SNSSubscriptionDTO snsSubscriptionDTO) throws URISyntaxException {
		System.out.println("in addSubscriberToTopic");
//		if(Objects.isNull(arn))
//		{
//			String arn = "arn:aws:sns:ap-south-1:183629752129:LOS-OTP:dd500dea-e99c-4507-90ca-2611b97ce3b4";
//		}
		try {
			return snsService.addSubscriberToTopic(snsSubscriptionDTO.getProcessId(), arn, snsSubscriptionDTO.getPhone());
		} catch (ResponseStatusException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}

	}
	
	
	
}
