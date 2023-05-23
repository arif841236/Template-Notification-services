package com.template.common.dto.request;

import lombok.Data;

import java.util.Map;

@Data
public class GetTemplateRequestDto {

    Integer medium;

    Map<String, Object> parameters;

    String code;
}
