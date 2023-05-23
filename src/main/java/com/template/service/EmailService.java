package com.template.service;


import com.template.common.dto.request.SMSDTO;
import com.template.common.dto.resposne.ResponseData;
import com.template.common.enums.Code;
import com.template.common.enums.Type;

import java.util.Map;

public interface EmailService {

	ResponseData sendEmail(SMSDTO smsDTO);


	String getEmailTemplateWithDataFilled(Map<String, Object> map, Code code);

}
