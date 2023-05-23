package com.template.common.exception;

public class TemplateException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public TemplateException() {
	}

	public TemplateException(String msg) {
		super(msg);
	}
}
