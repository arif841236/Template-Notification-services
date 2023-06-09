package com.template.common.utility;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorMessage {
	
	private String message;
	
	private Map<String, String> indexError;
}
