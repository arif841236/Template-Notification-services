//package com.template.common;
//
//
//import com.template.common.enums.Type;
//
//import com.template.entities.Template;
//import com.template.repository.TemplateRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.List;
//
//@Configuration
//public class InsertTableBean {
//
//    @Autowired
//    private TemplateRepository templateRepository;
//
//
//    @Bean
//    public void saveTemplates(){
//
//
//        List<Template> allExistingTemplates = templateRepository.findAll();
//
//        if(allExistingTemplates.size()!=0){
//            templateRepository.deleteAll(allExistingTemplates);
//        }
//
//        List<Template> templateList = List.of(
//                new Template(1L, Type.ALERT.getValue(),"ACCOUNT_OPENING","Dear customer, Thank you for choosing us. Your new savings bank account has been opened successfully. -  Example Bank Ltd.", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(2L,Type.ALERT.getValue(),"LOAN_OFFER","Mumbai Example Bank offers home loans with the best rate of interest, NO preclosure and no penalty on part pre-payments. Reach us on E-Mail or MobileNo. - Mumbai Example Bank.", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(3L,Type.ALERT.getValue(),"SENT_OTP","Dear Customer, [OTP] is your SECRET OTP (One Time Password) to process the loan. Please do not share it with anyone.", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(4L,Type.SMS.getValue(),"SENT_OTP","Dear Customer, [OTP] is your SECRET OTP (One Time Password) to process the loan. Please do not share it with anyone.", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(5L,Type.ALERT.getValue(),"CREDIT","Dear bank cardmember, Payment of Rs [AMOUNT] was credited to your card ending [ACCOUNT_NO] on [TIME].", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(6L,Type.SMS.getValue(),"ACCOUNT_OPENING","Dear customer, your FD account no. [ACCOUNT_NO] will get matured on [TIME]. Matured amount will be automatically transferred to your savings account. Telangana Example bank", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(7L,Type.ALERT.getValue(),"SAVE_PAN","Your Pan details save Successfully.", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(8L,Type.SMS.getValue(),"SAVE_PAN","Your Pan details save Successfully.", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(9L,Type.ALERT.getValue(),"SAVE_AADHAAR","Your Address proof details save Successfully.", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(10L,Type.SMS.getValue(), "SAVE_AADHAAR","Your Address proof details save Successfully.", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(11L, Type.ALERT.getValue(), "SAVE_CONTACT","Your Contact details save Successfully.", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(12L, Type.SMS.getValue(), "SAVE_CONTACT","Your Contact details save Successfully.", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(13L, Type.ALERT.getValue(), "SAVE_EMPLOYMENT","Your Employment details save Successfully.", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(14L, Type.SMS.getValue(), "SAVE_EMPLOYMENT","Your Employment details save Successfully.", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(15L, Type.ALERT.getValue(), "SAVE_FINANCIAL","Your Financial details save Successfully.", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(16L, Type.SMS.getValue(), "SAVE_FINANCIAL","Your Financial details save Successfully.", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(17L, Type.ALERT.getValue(), "SAVE_PARTNER_DATA","Your Partner details save Successfully.", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(18L, Type.SMS.getValue(), "SAVE_PARTNER_DATA","Your Partner details save Successfully.", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(19L, Type.ALERT.getValue(), "SAVE_PERSONAL_DETAILS","Your Personal details save Successfully.", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(20L, Type.SMS.getValue(), "SAVE_PERSONAL_DETAILS","Your Personal details save Successfully.", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(21L, Type.SMS.getValue(), "SAVE_E_MANDATE","AutoPay(E-mandate) Active!\nMerchant: [MERCHANT] \n Max Txn Amount :[MAX_TXN_AMOUNT] \n Freq: [FREQUENCY] \n Start Date: [START_DATE] \n End_Date : [END_DATE] on your [BANK_NAME]", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(22L, Type.ALERT.getValue(), "SAVE_E_MANDATE","AutoPay(E-mandate) Active!\nMerchant: [MERCHANT] \n Max Txn Amount :[MAX_TXN_AMOUNT] \n Freq: [FREQUENCY] \n Start Date: [START_DATE] \n End_Date : [END_DATE] on your [BANK_NAME]", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(23L, Type.ALERT.getValue(), "SAVE_MOBILE","Your Mobile details save Successfully", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(24L, Type.SMS.getValue(), "SAVE_MOBILE","Your Mobile details save Successfully", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(25L, Type.ALERT.getValue(), "LOAN_AVAILED","Thank you for choosing us! Update! INR [LOAN_AMOUNT] was deposited in your Bank on [LOAN_DATE] for your loan [LOAN_APPLICATION_NO]. Cheque deposits in A/C are subject to clearing", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(26L, Type.SMS.getValue(), "LOAN_AVAILED","Thank you for choosing us! Update! INR [LOAN_AMOUNT] was deposited in your Bank on [LOAN_DATE] for your loan [LOAN_APPLICATION_NO]. Cheque deposits in A/C are subject to clearing", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(27L, Type.ALERT.getValue(), "SAVE_ADDRESS","Your Address details save Successfully", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(28L, Type.SMS.getValue(), "SAVE_ADDRESS","Your Address details save Successfully", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(29L, Type.ALERT.getValue(), "TERMS_AND_CONDITION","Thank you for accepting our terms and condition.", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(30L, Type.SMS.getValue(), "TERMS_AND_CONDITION","Thank you for accepting our terms and condition.", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(31L, Type.ALERT.getValue(), "DATA_PRIVACY","Thank you for believing us.", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(32L, Type.SMS.getValue(), "DATA_PRIVACY","Thank you for believing us.", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(33L, Type.ALERT.getValue(), "SUBMIT_PARTNER_DETAILS_SUCCESS","Thank you for submitting your details", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC"))),
//                new Template(34L, Type.EMAIL.getValue(), "SUBMIT_PARTNER_DETAILS_SUCCESS","<body>"
//                		+ "<p>Hi Customer,<br><br>"
//                		+ "Thank you for submitting your details.<br>"
//                		+ "Please <a href= \"[URL]\">click here</a> to avail the loan.<br><br>"
//                		+ "Regards,<br>"
//                		+ "INT Origin Team"
//                		+ "</p>"
//                		+ "</body>", LocalDateTime.now(ZoneId.of("UTC")),LocalDateTime.now(ZoneId.of("UTC")))
//
//
//
//        );
//        templateRepository.saveAll(templateList);
//
//    }
//}
