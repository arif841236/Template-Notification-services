package com.template.common.utility;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class NotificationSender {



    public Map<String,String> send(String medium,String sendTo,String message){
        Map<String ,String > requestResponse = new HashMap<>();

        requestResponse.put("request","request to be added");
        requestResponse.put("requestAt", LocalDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));

        System.out.println("Sending Notification to "+ sendTo+" via "+medium);

        requestResponse.put("response","reposnse to be added");
        requestResponse.put("responseAt", LocalDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        return requestResponse;
    }
}
