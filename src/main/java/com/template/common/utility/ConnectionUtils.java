package com.template.common.utility;


import com.template.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;



public class ConnectionUtils {
	
	@Value("${spring.datasource.url}")
	private String dbConnectionString;
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	
	@Scheduled(fixedRate = 1*60*1000)
    public void dbIdleDelete() {
		String[] temp = dbConnectionString.split("/");
		String dbStr = temp[temp.length-1];
		String dbName = dbStr.substring(0, dbStr.indexOf("?"));
		notificationRepository.delIdleConnection(dbName);
    }
 

}
