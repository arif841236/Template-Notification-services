package com.template.common.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
public class ServiceException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	private final String message;
	private final Map<String, String> indexError;
	private final HttpStatus status;
	
	public ServiceException(String message) {
		this.message=message;
		this.indexError = null;
		this.status = HttpStatus.BAD_REQUEST;
	}
	
	public ServiceException(String message, HttpStatus status) {
		this.indexError = null;
		this.message=message;
		this.status = status;
	}

	public ServiceException(String message, Map<String, String> index) {
		this.message = message;
		this.indexError = index;
		this.status = HttpStatus.BAD_REQUEST;
	}
	
}
