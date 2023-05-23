package com.template.service.impl;


import com.template.common.config.AWSConfig;
import com.template.common.exception.ServiceException;
import com.template.common.utility.RequestResponseLogUtil;
import com.template.service.SNSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.CreateTopicResponse;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class SNSServiceImpl implements SNSService {
		
	@Autowired
	private RequestResponseLogUtil requestResponseLogUtil;
		
	@Autowired
	private Environment environment;
	
	private static final String successResponse = "SUCCESS";

	@Override
	public String createTopic(String topicName) {
		// Topic name cannot contain spaces
	    final CreateTopicRequest topicCreateRequest = CreateTopicRequest.builder().name(topicName).build();

	     SnsClient snsClient;
		try {
			snsClient = AWSConfig.getSnsClient();
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}

	     final CreateTopicResponse topicCreateResponse = snsClient.createTopic(topicCreateRequest);

	    if (topicCreateResponse.sdkHttpResponse().isSuccessful()) {
	        System.out.println("Topic creation successful");
	        System.out.println("Topic ARN: " + topicCreateResponse.topicArn());
	        System.out.println("Topics: " + snsClient.listTopics());
	    } else {
	        throw new ResponseStatusException(
	            HttpStatus.INTERNAL_SERVER_ERROR, topicCreateResponse.sdkHttpResponse().statusText().isPresent()?topicCreateResponse.sdkHttpResponse().statusText().get():""
	        );
	    }

	    snsClient.close();

	    return "Topic ARN: " + topicCreateResponse.topicArn();
	}

	@Override
	public String addSubscriberToTopic(String processId, String arn, String phone) {
		 SnsClient snsClient;
			try {
				snsClient = AWSConfig.getSnsClient();
			} catch (Exception e) {
				throw new ServiceException(e.getMessage());
			}

	    final SubscribeRequest subscribeRequest = SubscribeRequest.builder()
	            .topicArn(arn)
	            .protocol("sms")
	            .endpoint(phone)
	            .build();

	    SubscribeResponse subscribeResponse;
	    LocalDateTime requestedAt = null;
	    String request="";
	    try {
	    	request = "Subscription initiated for protocol SMS";
	    	requestedAt = LocalDateTime.now(ZoneId.of("UTC"));
	    	subscribeResponse = snsClient.subscribe(subscribeRequest);
	    }
	    catch(Exception e)
	    {
	    	requestResponseLogUtil.saveRequestResponseLog(processId, request, e.getMessage(), environment.getProperty("activitytype.subscription"), requestedAt, LocalDateTime.now(ZoneId.of("UTC")), Integer.parseInt(environment.getProperty("failure")),null);
	    	throw new ServiceException(e.getMessage());
	    }
	    finally
	    {
	    	snsClient.close();
	    }

	    if (subscribeResponse.sdkHttpResponse().isSuccessful()) {
	        System.out.println("Subscriber creation successful");
	        requestResponseLogUtil.saveRequestResponseLog(processId, request, "Subscriber creation successful", environment.getProperty("activitytype.subscription"), requestedAt, LocalDateTime.now(ZoneId.of("UTC")), Integer.parseInt(environment.getProperty("success")),null);
	    } else {
	    	requestResponseLogUtil.saveRequestResponseLog(processId, request, subscribeResponse.sdkHttpResponse().statusText().isPresent()?subscribeResponse.sdkHttpResponse().statusText().get():"", environment.getProperty("activitytype.subscription"), requestedAt, LocalDateTime.now(ZoneId.of("UTC")), Integer.parseInt(environment.getProperty("failure")),null);
	        throw new ResponseStatusException(
	            HttpStatus.INTERNAL_SERVER_ERROR, subscribeResponse.sdkHttpResponse().statusText().isPresent()?subscribeResponse.sdkHttpResponse().statusText().get():""
	        );
	    }

	    return successResponse;
	}
	
	

}
