package com.template.common.dto.common;

//import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TemplateRequest {

	@NotEmpty(message = "Type should not be empty or null.")
	private String type;
	private String description;
	private String subject;
	@NotEmpty(message = "Message should not be empty or null.")
	private String message;
	private int encrypt;
	private String code;
	private String createdBy;
}
