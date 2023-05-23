package com.template.common.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SNSSubscriptionDTO {
		
	private String processId;
	
	@JsonProperty("phone") 
	private String phone;

}
