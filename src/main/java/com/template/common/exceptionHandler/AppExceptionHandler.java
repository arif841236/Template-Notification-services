package com.template.common.exceptionHandler;

import com.google.gson.Gson;
import com.template.common.dto.common.LoggingResponseMessage;
import com.template.common.dto.common.MessageTypeConst;
import com.template.common.exception.NotificationException;
import com.template.common.exception.ServiceException;
import com.template.common.exception.TemplateErrorResponce;
import com.template.common.exception.TemplateException;
import com.template.common.utility.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class AppExceptionHandler {

    @Autowired
    Gson gson;

    @Value("${message.error}")
    private String message;

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Object> handleGeneralException(Exception ex){

		log.info(ex.getMessage());
		ErrorMessage errorMessage = new ErrorMessage("PROCESS_ERROR", null);
		ex.printStackTrace();
		return new ResponseEntity<>(errorMessage,new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@ExceptionHandler(value= MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,WebRequest request){
		log.info(ex.getMessage());
		Map<String, String> resp = new HashMap<>();
		
		ex.getBindingResult().getAllErrors().forEach(error->{
			
			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			
			resp.put(fieldName, message);
			ex.printStackTrace();
			});
		return new ResponseEntity<>(resp,new HttpHeaders(),HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(value = ServiceException.class)
	public ResponseEntity<Object> handleServiceException(ServiceException ex){
		 log.info(ex.getMessage());
		
		String erroMessageDescription = ex.getMessage();
	
		ErrorMessage errorMessage = new ErrorMessage(erroMessageDescription, ex.getIndexError());
		ex.printStackTrace();
		return new ResponseEntity<>(errorMessage,new HttpHeaders(),ex.getStatus());
		
	}

    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<TemplateErrorResponce> userExceptionHandler(NotificationException ue, WebRequest wb, Exception e) {

        TemplateErrorResponce error = new TemplateErrorResponce(HttpStatus.UNPROCESSABLE_ENTITY.value(), ue.getMessage(),wb.getDescription(false).substring(4));

        LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
                .message(message)
                .messageTypeId(MessageTypeConst.ERROR)
                .data(error)
                .build();

        log.error(gson.toJson(msgStart));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TemplateException.class)
    public ResponseEntity<TemplateErrorResponce> templateExceptionHandler(TemplateException ue, WebRequest wb) {
        TemplateErrorResponce error = new TemplateErrorResponce(HttpStatus.UNPROCESSABLE_ENTITY.value(), ue.getMessage(),wb.getDescription(false).substring(4));

        LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
                .message(message)
                .messageTypeId(MessageTypeConst.INTERNAL_ERROR)
                .data(error)
                .build();

        log.error(gson.toJson(msgStart));
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }
	@ExceptionHandler(SQLException.class)
	public final ResponseEntity<Object> sqlExceptions(SQLException ex, WebRequest request) {

		TemplateErrorResponce error = TemplateErrorResponce.builder()
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.path(request.getDescription(false).substring(4))
				.message(ex.getMessage())
				.build();

		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message(message)
				.messageTypeId(MessageTypeConst.ERROR)
				.data(error)
				.build();

		log.error(gson.toJson(msgStart));

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public final ResponseEntity<Object> illegalArgumentExceptionHandler(IllegalArgumentException ex, WebRequest request) {
		TemplateErrorResponce resp = new TemplateErrorResponce(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage(),request.getDescription(false).substring(4));

		LoggingResponseMessage msgStart = LoggingResponseMessage.builder()
				.message(message)
				.messageTypeId(MessageTypeConst.ERROR)
				.data(resp)
				.build();

		log.error(gson.toJson(msgStart));

		return new ResponseEntity<>(resp, HttpStatus.UNPROCESSABLE_ENTITY);

	}

}
