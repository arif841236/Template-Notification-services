package com.template.service;

import com.template.common.dto.common.MessageBodyRequest;
import com.template.common.dto.common.TemplateRequest;
import com.template.common.dto.common.UpdateRequest;
import com.template.common.dto.resposne.ResponseData;

public interface ITemplateService {

	public ResponseData saveTemplateModel(TemplateRequest tempRequest);

	public ResponseData getAllTemplate();
	
	public ResponseData getTemplateById(Long id);
	
	public ResponseData deleteTemplate(String code, String type);

	public ResponseData getTemplateByCode(String code, String type);

	public ResponseData updateTemplate(UpdateRequest templateRequest);

	public ResponseData inActiveStatus(String code,String type);

	public ResponseData setMessageBodyElement(MessageBodyRequest messageBodyRequest,String code,String type);

}
