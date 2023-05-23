package com.template.service;


import com.template.common.dto.request.SMSDTO;
import com.template.common.dto.resposne.ResponseData;
import com.template.common.enums.Code;

import java.util.Map;

public interface SMSService {

	ResponseData smsTOTP(SMSDTO smsDTO);

	String getSmsTemplateWithDataFilled(Map<String, Object> map, Code code);

}
