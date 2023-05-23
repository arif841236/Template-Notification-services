package com.template.common.dto.request;

import lombok.Data;

@Data
public class SMSSendDTO {
	
	String processId;
	
	String phone;
	
	String message;

	String code ;

	String source;

}
