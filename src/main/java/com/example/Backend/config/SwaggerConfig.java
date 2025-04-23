package com.example.Backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .servers(List.of(
                        new Server().url("https://devprep-official.store")
                ))
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Question API")
                .description("Question API. 질문 내용을 조회하고, 해당 질문에 대한 모델 답안을 조회하는 기능을 제공합니다.")
                .version("1.0.0");
    }
}
