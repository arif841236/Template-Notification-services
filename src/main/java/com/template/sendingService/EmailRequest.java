package com.template.sendingService;

import javax.validation.constraints.NotEmpty;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmailRequest {

	@NotEmpty(message = "Please enter atleast one email id.")
	private String[] to;
	
	private String[] cc;

	private String[] bcc;

	@Hidden
	private String subject;

	private String from;

	@NotEmpty(message = "Code should not be null or empty.")
	private String code;

	private Map<String, String> bodyElement;

	@Hidden
	private String message;

	private Attachment[] attachments;
}
