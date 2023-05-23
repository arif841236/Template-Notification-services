package com.template.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class for showing otp error exception
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TemplateErrorResponce {
	private Integer status;
	private String message;
	private String path;
}
