package com.template.service;

public interface SNSService {

	String createTopic(String topicName);

	String addSubscriberToTopic(String processId, String arn, String phone);

}
