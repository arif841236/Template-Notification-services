package com.template.controller;

import javax.mail.MessagingException;
import javax.validation.Valid;
import com.template.common.dto.common.*;
import com.template.common.dto.resposne.ResponseData;
import com.template.sendingService.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import com.template.service.ITemplateService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * This is controller layer here all endpoints are available.
 * @author Md Arif
 *
 */
@RestController
@Tag(description = "Template API",name = "Template Service")
@Slf4j
public class TemplateController {
	@Autowired
	ITemplateService iTemplateService;

	@Autowired
	Gson gson;

	@Autowired
	EmailSendService emailSendService;

	@Autowired
	SmsSendingService sendSms;

@ApiResponse(description = "Template data",responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ResponseData.class))})
@Operation(description = "Save template data",summary = "Save template",method = "POST")
@io.swagger.v3.oas.annotations.parameters.RequestBody (description = "Fill the template request body", required = true,content = @Content(schema = @Schema(implementation = TemplateRequest.class)))
@PostMapping(value = "/" ,produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseData> savetempEntity(@RequestBody @Valid TemplateRequest notificationParameter) {
		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message("Template save method started.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(notificationParameter)
				.build();

		log.info(gson.toJson(msgStart));

		ResponseData templateModel = iTemplateService.saveTemplateModel(notificationParameter);

		LoggingResponseMessage msgEnd = LoggingResponseMessage.builder()
				.message("Template save method end.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(templateModel)
				.build();

		log.info(gson.toJson(msgEnd));

		return ResponseEntity.ok(templateModel);
	}

@ApiResponse(description = "Template data",responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ResponseData.class))})
@Operation(description = "Fetch template data",summary = "Fetch all template",method = "GET")
@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseData> getAlltempEntity() {
		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message("Template fetch method started.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.build();

		log.info(gson.toJson(msgStart));

		ResponseData templateModel = iTemplateService.getAllTemplate();

		LoggingResponseMessage msgEnd = LoggingResponseMessage.builder()
				.message("Template fetch method end.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.build();

		log.info(gson.toJson(msgEnd));

		return ResponseEntity.ok(templateModel);
	}

	@ApiResponse(description = "Template data",responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ResponseData.class))})
	@Operation(description = "Fetch template data by code and type",summary = "Fetch specific template",method = "GET")
	@Parameters({
			@Parameter(name = "code",explode = Explode.TRUE, description = "Enter code",content = @Content(schema = @Schema(implementation = String.class)))
			,@Parameter(name = "type",explode = Explode.TRUE, description = "Enter type",content = @Content(schema = @Schema(implementation = String.class)))
	})
	@GetMapping(value = "/searchByCode/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseData> getTempDataByCode(@PathVariable(value = "code") String code, @RequestParam(value = "type",required = false) String type) {
		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message("Template fetch by code initated.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.build();

		log.info(gson.toJson(msgStart));

		ResponseData templateModel = iTemplateService.getTemplateByCode(code,type);

		LoggingResponseMessage msgEnd = LoggingResponseMessage.builder()
				.message("Template fetch by code ended.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.build();

		log.info(gson.toJson(msgEnd));

		return ResponseEntity.ok(templateModel);
	}
	@ApiResponse(description = "Template data",responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ResponseData.class))})
	@Operation(description = "Get template data by id",summary = "Get template",method = "GET")
	@Parameter(name = "id",explode = Explode.TRUE, description = "Enter id",content = @Content(schema = @Schema(implementation = String.class)))
	@GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseData> getTemplateByIds(@PathVariable("id") Long notificationTemplateId) {
		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message("Template get by id  method start.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(notificationTemplateId)
				.build();

		log.info(gson.toJson(msgStart));

		ResponseData response = iTemplateService.getTemplateById(notificationTemplateId);

		LoggingResponseMessage msgEnd = LoggingResponseMessage.builder()
				.message("Template get by id  method end.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(response)
				.build();

		log.info(gson.toJson(msgEnd));

		return ResponseEntity.ok(response);
	}

	@ApiResponse(description = "Template data",responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ResponseData.class))})
	@Operation(description = "In active template data by code and type",summary = "In active templates",method = "PATCH")
	@Parameters({
			@Parameter(name = "code",explode = Explode.TRUE, description = "Enter code",content = @Content(schema = @Schema(implementation = String.class)))
			,@Parameter(name = "type",explode = Explode.TRUE, description = "Enter type",content = @Content(schema = @Schema(implementation = String.class)))
	})
	@PatchMapping(value = "/inactive/{code}/{type}")
	public ResponseEntity<ResponseData> inActiveTemplate(@PathVariable("code") String code, @PathVariable("type") String type){
		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message("Template in active method start.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.build();

		log.info(gson.toJson(msgStart));
	ResponseData responseData= iTemplateService.inActiveStatus(code,type);
		LoggingResponseMessage msgEnd = LoggingResponseMessage.builder()
				.message("Template in active method end.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.build();

		log.info(gson.toJson(msgEnd));

		return ResponseEntity.ok(responseData);
	}


@ApiResponse(description = "Template data",responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ResponseData.class))})
@Operation(description = "Delete template data by code and type",summary = "Delete templates",method = "DELETE")
@Parameters({
		@Parameter(name = "code",explode = Explode.TRUE, description = "Enter code",content = @Content(schema = @Schema(implementation = String.class)))
		,@Parameter(name = "type",explode = Explode.TRUE, description = "Enter type",content = @Content(schema = @Schema(implementation = String.class)))
})
	@DeleteMapping(value = "/delete/{code}/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseData> deleteTemplateByCodeAndType(@PathVariable("code") String code, @PathVariable("type") String type) {
		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message("Template delete by code and type method start.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.build();

		log.info(gson.toJson(msgStart));

		ResponseData response = iTemplateService.deleteTemplate(code,type);

		LoggingResponseMessage msgEnd = LoggingResponseMessage.builder()
				.message("Template delete by code and type method end.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(response)
				.build();

		log.info(gson.toJson(msgEnd));

		return ResponseEntity.ok(response);
	}
	@ApiResponse(description = "Template data",responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ResponseData.class))})
	@Operation(description = "Update template data by code and type",summary = "Update template",method = "PUT")
	@Parameters({
			@Parameter(name = "code",explode = Explode.TRUE, description = "Enter code",content = @Content(schema = @Schema(implementation = String.class)))
			,@Parameter(name = "type",explode = Explode.TRUE, description = "Enter type",content = @Content(schema = @Schema(implementation = String.class)))
	})
	@io.swagger.v3.oas.annotations.parameters.RequestBody (description = "Fill the template request body", required = true,content = @Content(schema = @Schema(implementation = UpdateRequest.class)))
	@PutMapping(value = "/updateTemplate/{code}/{type}",consumes = {MediaType.APPLICATION_JSON_VALUE},produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<ResponseData> updateTemplateData(@PathVariable("code") String code, @PathVariable("type") String type,@RequestBody @Valid UpdateRequest updateRequest) {
		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message("Template update method start.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.build();

		log.info(gson.toJson(msgStart));
		updateRequest.setCode(code);
		updateRequest.setType(type);
		ResponseData responseData =iTemplateService.updateTemplate(updateRequest);
		LoggingResponseMessage msgEnd = LoggingResponseMessage.builder()
				.message("Template update method end.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.build();

		log.info(gson.toJson(msgEnd));
		return ResponseEntity.ok(responseData);
	}

@ApiResponse(description = "EMAIL sending response",responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ResponseData.class))})
@Operation(description = "Send EMAIL to the customer.",summary = "Send EMAIL",method = "POST")
@io.swagger.v3.oas.annotations.parameters.RequestBody (description = "Fill the EMAIL request body", required = true,content = @Content(schema = @Schema(implementation = EmailRequest.class)))
@PostMapping(value="/sendEmail",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseData> sendEmail(@RequestBody @Valid EmailRequest emailRequest) throws MessagingException, IOException {

		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message("Send email method start.")
				.messageTypeId(MessageTypeConst.SUCCESS)
//				.data(emailRequest)
				.build();

		log.info(gson.toJson(msgStart));

		ResponseData response = emailSendService.sendEmail(emailRequest);

		LoggingResponseMessage msgEnd = LoggingResponseMessage.builder()
				.message("Email send method end.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(response)
				.build();

		log.info(gson.toJson(msgEnd));

		return ResponseEntity.ok(response);
	}

@ApiResponse(description = "SMS sending response",responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ResponseData.class))})
@Operation(description = "Send SMS to the customer.",summary = "Send SMS",method = "POST")
@io.swagger.v3.oas.annotations.parameters.RequestBody (description = "Fill the sms request body", required = true,content = @Content(schema = @Schema(implementation = SmsRequest.class)))
@PostMapping(value="/sendSms",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseData> sendSms(@RequestBody @Valid SmsRequest smsRequest) {

		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message("Send sms method start.")
				.messageTypeId(MessageTypeConst.SUCCESS)
//				.data(smsRequest)
				.build();

		log.info(gson.toJson(msgStart));

		ResponseData response = sendSms.sendSms(smsRequest);

		LoggingResponseMessage msgEnd = LoggingResponseMessage.builder()
				.message("Send sms method end.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(response)
				.build();

		log.info(gson.toJson(msgEnd));

		return ResponseEntity.ok(response);
	}

	@ApiResponse(description = "Get message body.",responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ResponseData.class))})
	@Operation(description = "Get updated message body.",summary = "Update message body",method = "POST")
	@io.swagger.v3.oas.annotations.parameters.RequestBody (description = "Fill the body element request body", required = true,content = @Content(schema = @Schema(implementation = MessageBodyRequest.class)))
	@Parameters({
			@Parameter(name = "code",explode = Explode.TRUE, description = "Enter code",content = @Content(schema = @Schema(implementation = String.class)))
			,@Parameter(name = "type",explode = Explode.TRUE, description = "Enter type",content = @Content(schema = @Schema(implementation = String.class)))
	})
	@PostMapping(value="/searchNReplace/{code}/{type}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseData> getMessageBody(@PathVariable("code") String code, @PathVariable("type") String type,@RequestBody @Valid MessageBodyRequest bodyRequest) {

		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message("Get message body method start.")
				.messageTypeId(MessageTypeConst.SUCCESS)
//				.data(bodyRequest)
				.build();

		log.info(gson.toJson(msgStart));

		ResponseData response = iTemplateService.setMessageBodyElement(bodyRequest,code,type);

		LoggingResponseMessage msgEnd = LoggingResponseMessage.builder()
				.message("Get message body method end.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(response)
				.build();

		log.info(gson.toJson(msgEnd));

		return ResponseEntity.ok(response);
	}

	@ApiResponse(description = "Manual EMAIL sending response",responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ResponseData.class))})
	@Operation(description = "Send Manual EMAIL to the customer.",summary = "Send Manual EMAIL",method = "POST")
	@io.swagger.v3.oas.annotations.parameters.RequestBody (description = "Fill the manual email request body", required = true,content = @Content(schema = @Schema(implementation = EmailRequestManual.class)))
	@PostMapping(value="/sendManualEmail",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseData> sendEmailManual(@RequestBody @Valid EmailRequestManual requestManual) throws MessagingException, IOException {

		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message("Send manual email method start.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.build();

		log.info(gson.toJson(msgStart));

		ResponseData response = emailSendService.sendEmailManually(requestManual);

		LoggingResponseMessage msgEnd = LoggingResponseMessage.builder()
				.message("Send manual email method end.")
				.messageTypeId(MessageTypeConst.SUCCESS)
				.data(response)
				.build();

		log.info(gson.toJson(msgEnd));

		return ResponseEntity.ok(response);
	}
}
