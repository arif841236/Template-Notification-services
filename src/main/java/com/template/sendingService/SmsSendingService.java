package com.template.sendingService;

import com.google.gson.Gson;
import com.template.common.dto.common.LoggingResponseMessage;
import com.template.common.dto.common.MessageTypeConst;
import com.template.common.dto.resposne.ResponseData;
import com.template.common.enums.Type;
import com.template.common.exception.NotificationException;
import com.template.common.exception.TemplateException;
import com.template.entities.Template;
import com.template.repository.TemplateRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class SmsSendingService {

    @Autowired
    Gson gson;

    @Value("${replace.content.left}")
    String left;

    @Value("${replace.content.right}")
    String right;

    @Autowired
    TemplateRepository templateRepository;

    @Value("${twilio.sid}")
    private  String accountSid;

    @Value("${twilio.token}")
    private  String token;

    @Value("${twilio.phone}")
    private  String fromNumber;


    public ResponseData sendSms(SmsRequest smsRequest) {
        LoggingResponseMessage logmsg = LoggingResponseMessage.builder()
                .data(smsRequest)
                .message("Sending process start.")
                .messageTypeId(MessageTypeConst.SUCCESS).build();

        log.info(gson.toJson(logmsg));

        setSmsBody(smsRequest);
        log.info(gson.toJson(smsRequest));

        try{
            Twilio.init(accountSid, token);
            for(String mobile:smsRequest.getTo()){
                Message.creator(new PhoneNumber(smsRequest.getCountryCode().concat(mobile)), new PhoneNumber(fromNumber), smsRequest.getMessage())
                        .create();
            }
            LoggingResponseMessage logmsg2 = LoggingResponseMessage.builder()
                    .data(smsRequest)
                    .message("Sms send successfully.")
                    .messageTypeId(MessageTypeConst.SUCCESS).build();

            log.info(gson.toJson(logmsg2));
        } catch(Exception e){
            throw new NotificationException(e.getMessage());
        }
        return ResponseData.builder()
                .status(true)
                .message("Successfully sms send.")
                .build();
    }

    private void setSmsBody(SmsRequest request) {
        String code = request.getCode().replaceAll(" ","_").toUpperCase();
        Optional<Template> templates =templateRepository.findByCodeAndTypeAndDeletedOnAndStatusAndArchive(code, Type.SMS.name(), null,true,false);
        Template template = null;
        if(templates.isPresent() && templates.get()!= null){
            template = templates.get();
        }
        else{
            throw new TemplateException("Template not found for sms.");
        }
        String replacedBody = template.getMessage();
        if(request.getBodyElement() != null && request.getBodyElement() != null) {
            for (Map.Entry<String, String> rep : request.getBodyElement().entrySet()) {
                replacedBody = replacedBody.replace(left + rep.getKey().toUpperCase() + right, rep.getValue());
            }
        }
        log.info(replacedBody);
        request.setMessage(replacedBody);
    }
}
