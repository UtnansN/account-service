package com.utnans.accountservice.config;

import com.utnans.accountservice.exception.CurrencyConversionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Slf4j
@Component
// Not going to make this generic because only currency service in this app.
public class CurrencyServiceErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is5xxServerError() ||
                response.getStatusCode().is4xxClientError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        log.error("Failed to call currency service. Status code: {}, body: {}", response.getStatusCode(), new String(response.getBody().readAllBytes()));
        throw new CurrencyConversionException("There was an issue contacting Currency Service. Please try again later");
    }
}