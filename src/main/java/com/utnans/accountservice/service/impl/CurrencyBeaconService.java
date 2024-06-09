package com.utnans.accountservice.service.impl;

import com.utnans.accountservice.dto.integrations.CurrencyBeaconResponse;
import com.utnans.accountservice.exception.CurrencyConversionException;
import com.utnans.accountservice.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

@Service
@RequiredArgsConstructor
public class CurrencyBeaconService implements CurrencyService {

    private static final String API_KEY = "api_key";
    private static final String FROM = "from";
    private static final String TO = "to";
    private static final String AMOUNT = "amount";

    @Value("${app.currency-beacon.api-key}")
    private String apiKey;

    @Value("${app.currency-beacon.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    @Override
    public BigDecimal getConvertedValue(Currency baseCurrency, Currency targetCurrency, BigDecimal amount) {
        var uri = UriComponentsBuilder.fromHttpUrl("%s/convert".formatted(baseUrl))
                .queryParam(API_KEY, apiKey)
                .queryParam(FROM, baseCurrency.getCurrencyCode())
                .queryParam(TO, targetCurrency.getCurrencyCode())
                .queryParam(AMOUNT, amount)
                .toUriString();

        var response = restTemplate.getForEntity(uri, CurrencyBeaconResponse.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new CurrencyConversionException();
        }

        var convertedValue = new BigDecimal(response.getBody().value());

        // Round to the next decimal place; no freebies
        return convertedValue.setScale(2, RoundingMode.CEILING);
    }
}
