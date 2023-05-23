package com.template.service.impl;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import com.template.common.dto.common.*;
import com.template.common.dto.resposne.ResponseData;
import com.template.common.enums.Type;
import com.template.entities.Template;
import com.template.repository.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.template.common.exception.TemplateException;
import com.template.service.ITemplateService;
import lombok.extern.slf4j.Slf4j;

/**
 * This is service layer and here all logics are written.
 * @author Md Arif
 *
 */
@Service
@Slf4j
public class TemplateServiceImpl implements ITemplateService {

	@Value("${replace.content.left}")
	String left;

	@Value("${replace.content.right}")
	String right;

	Gson gson = new GsonBuilder()
			.registerTypeAdapter(Timestamp.class, new LocalDateTimeConverter())
			.create();

	@Autowired
	TemplateRepository templateRepository;

	/**
	 * This method for save template and
	 * return template with success message.
	 */
	@Override
	public ResponseData saveTemplateModel(TemplateRequest tempRequest) {
		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message("saveTemplateModel method statrt.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.build();

		log.info(gson.toJson(msgStart));
		String type =Type.valueOf(tempRequest.getType().toUpperCase()).name();
		String code = tempRequest.getCode()==null || tempRequest.getCode().isEmpty()?tempRequest.getDescription().replaceAll(" ","_").toUpperCase():tempRequest.getCode().replaceAll(" ","_").toUpperCase();
		tempRequest.setCode(code);

		Template template = new Template();
		List<Template> templates1 = new ArrayList<>();
		if(type.equalsIgnoreCase(Type.BOTH.name())){
			templates1.add(checkAndReturnTemp(Type.EMAIL.name(),code,tempRequest));
			templates1.add(checkAndReturnTemp(Type.SMS.name(),code,tempRequest));
			templates1.add(checkAndReturnTemp(Type.ALERT.name(),code,tempRequest));

		} else if(type.equalsIgnoreCase(Type.SMS.name())){
			templates1.add(checkAndReturnTemp(Type.SMS.name(),code,tempRequest));
			templates1.add(checkAndReturnTemp(Type.ALERT.name(),code,tempRequest));
		}
        else{
			templates1.add(checkAndReturnTemp(type,code,tempRequest));
		}
        List<Template> templates2 =templateRepository.saveAll(templates1);
		LoggingResponseMessage logmsg2 = LoggingResponseMessage.builder()
				.message("TemplateModel save successfully.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.build();
		log.info(gson.toJson(logmsg2));

		ResponseData responseData = new ResponseData();
		responseData.setData(templates2);
		responseData.setStatus(true);
		responseData.setMessage("Notification Template is successfully added to the database");

		return responseData;
	}

	private Template checkAndReturnTemp(String type, String code, TemplateRequest tempRequest) {
		Optional<List<Template>> templates2 =templateRepository.findByCodeAndTypeAndDeletedOn(code,type,null);

		if((templates2.isPresent() && !templates2.get().isEmpty())){
			throw new TemplateException("Code is already present please enter another code.");
		} else{
			return setTemplateBody(tempRequest,type);
		}
	}

	private Template setTemplateBody(TemplateRequest tempRequest,String type) {
		String subject = type.equalsIgnoreCase(Type.SMS.name()) || type.equalsIgnoreCase(Type.ALERT.name()) ? null:tempRequest.getSubject();
		String createdBy = tempRequest.getCreatedBy()==null || tempRequest.getCreatedBy().equalsIgnoreCase("string")
				?"System":tempRequest.getCreatedBy();
		String message = tempRequest.getMessage();
//		message = Base64.getEncoder().encodeToString(tempRequest.getMessage().getBytes());
        message = setReplaceVariableInCaps(message,tempRequest.getEncrypt());
		Template temp = Template.builder()
				.subject(subject)
				.description(tempRequest.getDescription())
				.message(message)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.version(1).type(type)
				.createdBy(createdBy)
				.code(tempRequest.getCode())
				.status(true)
				.archive(false)
				.build();
		LoggingResponseMessage logmsg1 = LoggingResponseMessage.builder()
				.message("TemplateModel build successfully.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.build();

		log.info(gson.toJson(logmsg1));
		return temp;
}

	private String setReplaceVariableInCaps(String message,int flage) {

		message = flage==1?new String(Base64.getDecoder().decode(message), StandardCharsets.UTF_8):message;

		String message1 = message;
		while(true){
			int ind1 = message1.indexOf("[");
			int ind2 = message1.indexOf("]");
			if(ind2 == -1 && ind2 == -1) {
				break;
			}
			String word = message1.substring(ind1,ind2+1);
			log.info(word);
			message1 = message1.replace(word,"{{}}");
			log.info(message1);
			message = message.replace(word,word.toUpperCase());
		}
		return  message;
	}

	/**
	 * This method for fetch all template and
	 * return all template with success message.
	 */
	@Override
	public ResponseData getAllTemplate() {
		LoggingResponseMessage logmsg1 = LoggingResponseMessage.builder()
				.message("getAllTemplate method start.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.build();

		log.info(gson.toJson(logmsg1));

		Optional<List<Template>> list  = templateRepository.findByDeletedOn(null);

		if(!list.isPresent() || list.get().isEmpty()) {
			LoggingResponseMessage logmsg3 = LoggingResponseMessage.builder()
					.message("Template not found.")
					.messageTypeId(MessageTypeConst.ERROR)
					.data(list)
					.build();

			log.error(gson.toJson(logmsg3));

			throw new TemplateException("Template not found.");
		}
		List<Template> templates = list.get();
		List<Object> result = new ArrayList<>();
		getTemplateList(templates, result);
		LoggingResponseMessage logmsg2 = LoggingResponseMessage.builder()
				.message("Fetch all template successfully.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(result)
				.build();

		log.info(gson.toJson(logmsg2));
		ResponseData responseData = new ResponseData();
		responseData.setData(result);
		responseData.setStatus(true);
		responseData.setMessage("Notification-Template List is Fetched Successfully");
		return responseData;

	}

	private void getTemplateList(List<Template> list1, List<Object> result) {

		Map<String,Object> stringObjectMap = new HashMap<>();
		for(int i = 0; i<list1.size(); i++){

			if(stringObjectMap.containsKey(list1.get(i).getCode().concat(list1.get(i).getType())) || stringObjectMap.containsValue(list1.get(i))){
				stringObjectMap.put(list1.get(i).getCode().concat(list1.get(i).getType()),list1.get(i));
			} else{
				stringObjectMap.put(list1.get(i).getCode().concat(list1.get(i).getType()),list1.get(i));
			}
		}
		result.addAll(stringObjectMap.values());
	}

	/**
	 * This method for fetch one template with id and
	 * return template with success message.
	 */
	@Override
	public ResponseData getTemplateById(Long id) {
		LoggingResponseMessage logmsg2 = LoggingResponseMessage.builder()
				.message("getTemplateById method start.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(id)
				.build();

		log.info(gson.toJson(logmsg2));

		Optional<Template> temp2 = templateRepository.findByIdAndDeletedOn(id,null);

		if(temp2.isPresent()) {
			LoggingResponseMessage logmsg3 = LoggingResponseMessage.builder()
					.message("Fetch template by id is successfully.")
					.messageTypeId(MessageTypeConst.SUCCESS)
					.data(temp2.get())
					.build();

			log.info(gson.toJson(logmsg3));
			ResponseData responseData = new ResponseData();
			responseData.setData(temp2.get());
			responseData.setStatus(true);
			responseData.setMessage("Template is successfully fetched from the system");
			return responseData;

		}
		LoggingResponseMessage logmsg4 = LoggingResponseMessage.builder()
				.message("Template  not found.")
				.messageTypeId(MessageTypeConst.ERROR)
				.data(temp2.toString())
				.build();

		log.error(gson.toJson(logmsg4));

		throw new TemplateException("Template not found with id "+id);
	}

	/**
	 * This method for delete template with help of id and
	 * return deleted template with success message.
	 */
	@Override
	public ResponseData deleteTemplate(String code,String type) {
		LoggingResponseMessage logmsg2 = LoggingResponseMessage.builder()
				.message("deleteTemplate method start.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.build();

		log.info(gson.toJson(logmsg2));
		code = code.replaceAll(" ","_").toUpperCase();
		Optional<List<Template>> temp= templateRepository.findByCodeAndTypeAndDeletedOn(code,type.toUpperCase(),null);
		if(temp.isPresent() && !temp.get().isEmpty()) {
			List<Template> templates =temp.get();
			for(Template ls:templates){
				ls.setDeletedOn(LocalDateTime.now());
			}
			List<Template> templates1 =templateRepository.saveAll(templates);
			LoggingResponseMessage logmsg3 = LoggingResponseMessage.builder()
					.message("Template is deleted successfully.")
					.messageTypeId(MessageTypeConst.SUCCESS)
					.build();

			log.info(gson.toJson(logmsg3));
			ResponseData responseData = new ResponseData();
			responseData.setData(templates1);
			responseData.setStatus(true);
			responseData.setMessage("Template is successfully removed from the system");
			return responseData;
		}

		LoggingResponseMessage logmsg4 = LoggingResponseMessage.builder()
				.message("Template not found.")
				.messageTypeId(MessageTypeConst.ERROR)
				.build();

		log.error(gson.toJson(logmsg4));

		throw new TemplateException("Template not found.");
	}

	@Override
	public ResponseData getTemplateByCode(String code, String type) {

		log.info("Start getTemplateByCode method start."+code+"   "+type);
		code = code.replaceAll(" ","_").toUpperCase();
		Optional<List<Template>> templates = type==null || type.isEmpty()?templateRepository.findByCodeAndDeletedOn(code,null)
				:templateRepository.findByCodeAndTypeAndDeletedOn(code, type.toUpperCase(), null);
		log.info(templates.get().toString());
		if(!templates.isPresent() && templates.get().isEmpty()){
			new TemplateException("Template not found.");
		}

		return ResponseData.builder()
				.data(templates.get())
				.status(true)
				.message("Get template successfully.")
				.build();
	}

	@Override
	public ResponseData updateTemplate(UpdateRequest updateRequest) {
		String code = updateRequest.getCode().replaceAll(" ","_").toUpperCase();
		Optional<List<Template>> template =templateRepository.findByCodeAndType(code,updateRequest.getType().toUpperCase());
		if(template.isPresent() && !template.get().isEmpty()){
			List<Template> templates =template.get();
			Template template1 = templates.get(templates.size()-1);
			template1.setArchive(true);
			templateRepository.save(template1);
			Template template2 = setUpdatedTemplate(templates.get(templates.size()-1),updateRequest);
			Template template3 = templateRepository.save(template2);
			return ResponseData.builder()
					.data(template3)
					.status(true)
					.message("Updated successfully.")
					.build();
		} else{
			throw new TemplateException("Template is not available for update.");
		}
	}

	@Override
	public ResponseData inActiveStatus(String code, String type) {
		code = code.replaceAll(" ","_").toUpperCase();

		Optional<List<Template>> templates =templateRepository.findByCodeAndTypeAndDeletedOnAndStatus(code,type.toUpperCase(),null,true);

		if(templates.isPresent() && !templates.get().isEmpty()){
			List<Template> templates1 =templates.get();
			templates1.forEach(x-> x.setStatus(false));
			List<Template> templates2 = templateRepository.saveAll(templates1);
			return ResponseData.builder()
					.data(templates2)
					.message("In active template successfully.")
					.status(true)
					.build();
		} else{
			throw new TemplateException("Active templates not found.");
		}
	}

	@Override
	public ResponseData setMessageBodyElement(MessageBodyRequest messageBodyRequest,String code,String type) {
		code = code.replaceAll(" ","_").toUpperCase();
		Optional<Template> template =templateRepository.findByCodeAndTypeAndDeletedOnAndStatusAndArchive(code, type.toUpperCase(),null,true,false);

		Map<String , String> responseMap = new HashMap<>();
		if(template.isPresent() && template.get() != null){
			Template template1 =template.get();
			String message =template1.getMessage();
			if(message != null){
				for(Map.Entry<String,String> rep:messageBodyRequest.getBodyElement().entrySet()){
					message = message.replace(left+rep.getKey().toUpperCase()+right,rep.getValue());
				}
			}
			String subject =type.toUpperCase().equalsIgnoreCase(Type.EMAIL.name())?template1.getSubject():"";
			responseMap.put("producedSubject",subject);
			log.info(message);
			String msg11 = Base64.getEncoder().encodeToString(message.getBytes());
			responseMap.put("producedMessage",message);
		}
		else{
			throw  new TemplateException("Template not found.");
		}

		return  ResponseData.builder()
				.data(responseMap)
				.message("Successfully get updated message body.")
				.status(true).build();
	}

	private Template setUpdatedTemplate(Template template1, UpdateRequest updateRequest)  {
		String createdBy = updateRequest.getCreatedBy()==null || updateRequest.getCreatedBy().equalsIgnoreCase("string")
				?template1.getCreatedBy():updateRequest.getCreatedBy();
		String description = updateRequest.getDescription()==null || updateRequest.getDescription().isEmpty()?template1.getDescription():updateRequest.getDescription();
		String message = updateRequest.getMessage()==null || updateRequest.getMessage().isEmpty()? template1.getMessage():setReplaceVariableInCaps(updateRequest.getMessage(),updateRequest.getEncrypt());
		String type = Type.valueOf(updateRequest.getType().toUpperCase()).name();
		String subject = updateRequest.getSubject()==null || updateRequest.getSubject().isEmpty()?template1.getSubject():updateRequest.getSubject();
		subject = type.equalsIgnoreCase(Type.SMS.name()) || type.equalsIgnoreCase(Type.ALERT.name())?null:subject;
		int version = template1.getVersion();

		return Template.builder()
				.archive(false)
				.createdBy(createdBy)
				.code(template1.getCode())
				.createdAt(template1.getCreatedAt())
				.updatedAt(LocalDateTime.now())
				.description(description)
				.subject(subject).message(message)
				.type(type)
				.status(true)
				.version(version+1)
				.build();
	}
}
