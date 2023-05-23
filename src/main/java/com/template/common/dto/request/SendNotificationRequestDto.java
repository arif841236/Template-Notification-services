package com.template.common.dto.request;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SendNotificationRequestDto {

    private String sentTo;

    private String templateCode;

    private List<String> medium;

    private Map<String,Object> data;
}
