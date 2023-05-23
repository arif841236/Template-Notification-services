package com.template.common.dto.common;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Data
public class MessageBodyRequest {
    @NotEmpty(message = "Body element should not be null or empty.")
    private Map<String,String> bodyElement;
}
