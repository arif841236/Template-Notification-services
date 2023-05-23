package com.template.sendingService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.*;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.*;
import com.template.common.dto.resposne.ResponseData;
import com.template.common.enums.Type;
import com.template.entities.Template;
import com.template.repository.TemplateRepository;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import com.template.common.exception.TemplateException;
import com.template.common.dto.common.LoggingResponseMessage;
import com.template.common.dto.common.MessageTypeConst;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.util.ResourceUtils.getFile;

/**
 * This classs for email service
 * @author Md Arif
 *
 */
@Component
@Slf4j
public class EmailSendService {

    @Value("${spring.mail.username}")
    String username;
    @Value("${spring.mail.password}")
    String password;

    @Autowired
    Gson gson;

    @Value("${replace.content.left}")
    String left;

    @Value("${replace.content.right}")
    String right;

    @Autowired
    TemplateRepository templateRepository;

    public EmailRequest setEmailContent(EmailRequest request) {
        String code = request.getCode().replaceAll(" ","_").toUpperCase();
        Optional<Template> templates =templateRepository.findByCodeAndTypeAndDeletedOnAndStatusAndArchive(code, Type.EMAIL.name(), null,true,false);
        Template template = null;
        if(templates.isPresent() && templates.get()!=null){
            template =templates.get();
        }
        else{
            throw new TemplateException("Template not found for email send");
        }
        String replacedBody = template.getMessage();
        if(request.getBodyElement() != null && request.getBodyElement() != null) {
            for (Map.Entry<String, String> rep : request.getBodyElement().entrySet()) {
                replacedBody = replacedBody.replace(left + rep.getKey().toUpperCase() + right, rep.getValue());
            }
        }
        log.info(replacedBody);
        request.setMessage(replacedBody);
        request.setSubject(template.getSubject());
        log.info(username);
        if(request.getFrom() == null || request.getFrom().isBlank()) {
            request.setFrom(username);
        }

        return request;
    }

    /**
     * This method to send email
     * @param request
     * @return its return EmailResponse message with success
     */
    public ResponseData sendEmail(EmailRequest request) throws MessagingException, IOException {
        LoggingResponseMessage logmsg = LoggingResponseMessage.builder()
                .message("Sending process start.")
                .messageTypeId(MessageTypeConst.SUCCESS).build();

        log.info(gson.toJson(logmsg));

        setEmailContent(request);

        sendSimpleEmail(request);

     return ResponseData.builder()
                 .status(true)
                 .message("Successfully email send.")
                 .build();
    }

   public ResponseData sendSimpleEmail(EmailRequest eContent) throws MessagingException, IOException {
        log.info(gson.toJson(LoggingResponseMessage.builder()
                .message("sendMail method start.")
                .messageTypeId(MessageTypeConst.SUCCESS)
                .statusCode(HttpStatus.OK).build()));
        if(eContent.getAttachments().length>10){
            throw new TemplateException("Attachment should not be above 10 file.");
        }
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        log.info(gson.toJson(LoggingResponseMessage.builder()
                .message("Email properties set successfully.")
                .messageTypeId(MessageTypeConst.SUCCESS)
                .statusCode(HttpStatus.OK).build()));
        Session session = Session.getInstance(props,new javax.mail.Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        log.info(gson.toJson(LoggingResponseMessage.builder()
                .message("Email session create successfully.")
                .messageTypeId(MessageTypeConst.SUCCESS)
                .statusCode(HttpStatus.OK).build()));

        javax.mail.Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(eContent.getFrom()));

        for(String str : eContent.getTo()) {
            message.addRecipient(RecipientType.TO, new InternetAddress(str));
        }

        if(eContent.getBcc() != null){
            for(String str : eContent.getBcc()) {
                message.addRecipient(RecipientType.BCC, new InternetAddress(str));
            }
        }

        if(eContent.getCc() != null) {
            for (String str : eContent.getCc()) {
                message.addRecipient(RecipientType.CC, new InternetAddress(str));
            }
        }
        message.setSubject(eContent.getSubject());

        //3) create MimeBodyPart object and set your message text
        MimeBodyPart messageBodyPart1 = new MimeBodyPart();
        Multipart multipart = new MimeMultipart();
        String htmlText = eContent.getMessage();

        messageBodyPart1.setContent(htmlText, "text/html");
        message.setHeader("Content-Type", "text/html");

        List<File> fileList = new ArrayList<>();

        for(Attachment atc: eContent.getAttachments()){

            MimeBodyPart messageBodyPart2 = new MimeBodyPart();
            File file1 = saveImageFromBase64(atc.getFilename(), atc.getContent());
            fileList.add(file1);
            checkFileLength(file1,fileList);
            messageBodyPart2.attachFile(file1, getFileContent(atc.getContent()), null);
            multipart.addBodyPart(messageBodyPart2);
        }

        multipart.addBodyPart(messageBodyPart1);
        message.setContent(multipart);
        Transport.send(message);
        for(File file:fileList){
            Files.delete(file.toPath());
        }
        log.info(gson.toJson(LoggingResponseMessage.builder()
                .message("Email send successfully.")
                .messageTypeId(MessageTypeConst.SUCCESS)
                .statusCode(HttpStatus.OK).build()));

        return ResponseData.builder()
                .status(true)
                .message("Successfully email send.")
                .build();
    }

    public ResponseData sendEmailManually(EmailRequestManual message1) throws MessagingException, IOException {

        if(message1.getAttachments().length>10){
            throw new TemplateException("Attachment should not be above 10 file.");
        }
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        log.info(gson.toJson(LoggingResponseMessage.builder()
                .message("Email properties set successfully.")
                .messageTypeId(MessageTypeConst.SUCCESS)
                .statusCode(HttpStatus.OK).build()));
        Session session = Session.getInstance(props,new javax.mail.Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);

            }
        });

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        MimeMessage message = new MimeMessage(session);
        Multipart multipart = new MimeMultipart();

        message.setFrom(message1.getFrom());
        message.setSubject(message1.getSubject());
        for(String str : message1.getTo()) {
            message.addRecipient(RecipientType.TO, new InternetAddress(str));
        }

        if(message1.getBcc() != null){
            for(String str : message1.getBcc()) {
                message.addRecipient(RecipientType.BCC, new InternetAddress(str));
            }
        }

        if(message1.getCc() != null) {
            for (String str : message1.getCc()) {
                message.addRecipient(RecipientType.CC, new InternetAddress(str));
            }
        }

        List<File> fileList = new ArrayList<>();
        for(Attachment atc: message1.getAttachments()){
            log.info(atc.getFilename());
            MimeBodyPart messageBodyPart2 = new MimeBodyPart();
            File file1 = saveImageFromBase64(atc.getFilename(), atc.getContent());
            fileList.add(file1);
            checkFileLength(file1, fileList);
            messageBodyPart2.attachFile(file1, getFileContent(atc.getContent()), null);
            multipart.addBodyPart(messageBodyPart2);
        }
        String htmlText = message1.getMessage();
        messageBodyPart.setContent(htmlText, "text/html");
        message.setHeader("Content-Type", "text/html");
        multipart.addBodyPart(messageBodyPart);
        message.setContent(multipart);
        Transport.send(message);

        for(File file:fileList){
            Files.delete(file.toPath());
        }
        return ResponseData.builder()
                .status(true)
                .message("Successfully email send.")
                .build();
    }

    private File saveImageFromBase64(String fileName,String base64) throws IOException {

        long timestamp = Instant.now().toEpochMilli();
        byte[] decode = Base64.getDecoder().decode(base64);
        String fileExtension = mimeTypeMap.get(getFileContent(base64));
        log.info(fileExtension);
        if(fileExtension != null) {
            return getFile(fileName,fileExtension,decode);
        }
        else {
            throw new TemplateException("Invalid fileB64 resource.");
        }
    }
    private static final Map<String, String> mimeTypeMap;
    static {
        Map<String, String> map = new HashMap<>();
        map.put("application/pdf", ".pdf");
        map.put("application/msword", ".doc");
        map.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx");
        map.put("application/vnd.ms-excel", ".xls");
        map.put("application/x-tika-ooxml", ".docx");
        map.put("image/png", ".png");
        map.put("image/jpeg", ".jpg");
        map.put("application/zip",".xlsx");
        mimeTypeMap = Collections.unmodifiableMap(map);
    }

    private String getFileContent(String base64){
        byte[] decode = Base64.getDecoder().decode(base64);
        Tika tika = new Tika();
        String ext = tika.detect(decode);
        log.info(ext);
        return ext;
    }

    private File getFile(String fileName, String string, byte[] bytes) throws IOException {
        File file2 =new File(fileName+string);

        try (FileOutputStream fileOutputStream = new FileOutputStream(file2)){
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage());
        }
        return file2;
    }

    private void checkFileLength(File file, List<File> fileList) throws IOException {
        if(file.length() > 5242880*2) {
            for(File fileD:fileList){
                Files.delete(fileD.toPath());
            }
            throw new TemplateException("File size should not be above 10MB");
        }
    }
}
