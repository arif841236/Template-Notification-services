package com.template;

//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.util.Optional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//import com.template.smsandemail.dto.TemplateModel;
//import com.template.model.common.TemplateRequest;
//import com.template.model.common.TemplateResponce;
//import com.template.repository.ITemplateRepository;
//import com.template.service.impl.TemplateServiceImpl;

//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
class NotificationApplicationTests {

//	@Mock
//	ITemplateRepository iTemplateRepository;
//
//	@InjectMocks
//	TemplateServiceImpl iTemplateService;
//
//	@BeforeEach
//	void setUp() {
//		this.iTemplateService = new TemplateServiceImpl(iTemplateRepository);
//	}
//
//	@Test
//	void contextLoads() {
//
//		TemplateRequest templateModel = TemplateRequest.builder().createdBy("System")
//				.notificationChannel(new String[] { "email", "sms" }).notificationType("otp").processName("ONBOARDING")
//				.templateBody(
//						" {$$otp$$} is your OTP for {$$processName$$}, generated at {$$created_time$$} and valid for next 3 mins.")
//				.build();
//		TemplateResponce responce = iTemplateService.saveTemplateModel(templateModel);
//		assertEquals(200, responce.getStatus());
//
//	}
//
//	@Test
//	void testDelete() {
//		Optional<TemplateModel> optional =Optional.of(TemplateModel.builder().createdAt(Timestamp.valueOf(LocalDateTime.now())).build());
//		when(iTemplateRepository.findById(27)).thenReturn(optional);
//		TemplateResponce deleteTemplate = iTemplateService.deleteTemplate(27);
//		assertEquals(200, deleteTemplate.getStatus());
//	}

}
