package com.utnans.accountservice.service.impl;

import com.utnans.accountservice.dto.integrations.CurrencyBeaconResponse;
import com.utnans.accountservice.exception.CurrencyConversionException;
import com.utnans.accountservice.service.CurrencyService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

@Service
public class CurrencyBeaconService implements CurrencyService {

    @Value("${app.currency-beacon.api-key}")
    private String apiKey;

    @Value("${app.currency-beacon.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public CurrencyBeaconService(@Qualifier("currencyServiceTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public BigDecimal getConvertedValue(Currency baseCurrency, Currency targetCurrency, BigDecimal amount) {
        var uri = UriComponentsBuilder.fromHttpUrl("%s/convert".formatted(baseUrl))
                .queryParam(CurrencyBeaconConstants.API_KEY, apiKey)
                .queryParam(CurrencyBeaconConstants.FROM, baseCurrency.getCurrencyCode())
                .queryParam(CurrencyBeaconConstants.TO, targetCurrency.getCurrencyCode())
                .queryParam(CurrencyBeaconConstants.AMOUNT, amount)
                .toUriString();

        var response = restTemplate.getForEntity(uri, CurrencyBeaconResponse.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new CurrencyConversionException("There was an issue calling currency service");
        }

        var convertedValue = new BigDecimal(response.getBody().value());

        // Round to the next decimal place; no freebies
        return convertedValue.setScale(2, RoundingMode.CEILING);
    }
}
