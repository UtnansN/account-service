package com.utnans.accountservice.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean("currencyServiceTemplate")
    public RestTemplate restTemplate(RestTemplateBuilder builder, CurrencyServiceErrorHandler errorHandler) {
        return builder
                .errorHandler(errorHandler)
                .build();
    }
}
