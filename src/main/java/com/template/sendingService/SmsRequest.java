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
public class SmsRequest {

//	@Pattern(regexp = "(0|91)?[6-9]\\d{9}", message = "Please enter valid mobile number")
	@NotEmpty(message = "Please enter atleast one mobile number")
	private String[] to;
	
	@NotEmpty(message = "Please enter country code.")
	private String countryCode;

	@Hidden
	private String message;

	@NotEmpty(message = "Please enter code.")
	private String code;

	private Map<String,String> bodyElement;

}
