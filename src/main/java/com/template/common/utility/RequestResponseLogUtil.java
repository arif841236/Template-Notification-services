package com.template.common.utility;


import com.template.common.exception.ServiceException;
import com.template.entities.RequestResponseLog;
import com.template.repository.RequestResponseLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RequestResponseLogUtil {
	
	@Autowired
	private RequestResponseLogRepository requestResponseLogRepository;
	
	public boolean saveRequestResponseLog(String processId, String request, String response, String activityType, LocalDateTime requestedAt, LocalDateTime responseAt, int status,String source)
	{
		RequestResponseLog requestResponseLog = RequestResponseLog.builder()
				.processId(processId)
				.request(request)
				.response(response)
				.activityType(activityType)
				.requestedTime(requestedAt)
				.responseTime(responseAt)
				.status(status)
				.source(source)
				.build();
		try {
			requestResponseLogRepository.save(requestResponseLog);
			return true;
		}
		catch(Exception e)
		{
			throw new ServiceException(e.getMessage());
		}		
	}


}
