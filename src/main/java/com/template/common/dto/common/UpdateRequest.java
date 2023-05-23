package com.template.common.dto.common;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateRequest {
    @Hidden
    private String code;
    private String message;
    private int encrypt;
    private String description;
    @Hidden
    private String type;
    private String subject;
    private String createdBy;
}
