package com.template.common.config;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;


public class AWSConfig {
	
	private static final String ACCESS_KEY = "AKIASVQJPU5A67MVZS4P";
	private static final String SECRET_KEY = "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";
	
	private AWSConfig()
	{
		
	}
	
	public static SnsClient getSnsClient() {
	    return SnsClient.builder()
	            .credentialsProvider(getAwsCredentials(
	            		ACCESS_KEY,
	            		SECRET_KEY))
	            .region(Region.AP_SOUTH_1) // Set your selected region
	            .build();
		
		
	}
	
	private static AwsCredentialsProvider getAwsCredentials(String accessKeyID, String secretAccessKey) {
	    AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKeyID, secretAccessKey);
	    return () -> awsBasicCredentials;
	}
}
