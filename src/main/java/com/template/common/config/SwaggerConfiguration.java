package com.template.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

@Bean
OpenAPI customCofiguration() {
    return new OpenAPI()
            .components(new Components())
            .info(new Info().title("Notification Template Api Documentation")
                            .description("Api Documentation").version("V1"));
           }
}
