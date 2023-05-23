package com.template.common.dto.request;

import lombok.Data;

import java.util.Map;

@Data
public class SMSDTO {
	
	String processId;
	
	String phone;

	String email;
	
	Integer medium;

	String source;
	
	Map<String, Object> parameters;

//	byte[] attachment;
	
	String code;

}
