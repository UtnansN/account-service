package com.utnans.accountservice.service.impl;

import com.utnans.accountservice.dto.integrations.CurrencyBeaconResponse;
import com.utnans.accountservice.exception.CurrencyConversionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyBeaconServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CurrencyBeaconService target;

    @Test
    @DisplayName("Currency conversion call is made and rounded to the next cent")
    void getConvertedValue_successfulCall_returnsRoundedValue() {
        // Arrange
        var amount = "400";
        var apiKey = "secretKey";
        var baseUrl = "https://whatever.com";
        var fromCurrency = "USD";
        var toCurrency = "EUR";

        ReflectionTestUtils.setField(target, "baseUrl", baseUrl);
        ReflectionTestUtils.setField(target, "apiKey", apiKey);

        final var expectedUri = "%s/convert?api_key=%s&from=%s&to=%s&amount=%s".formatted(baseUrl, apiKey, fromCurrency, toCurrency, amount);

        var baseCurrency = Currency.getInstance(fromCurrency);
        var targetCurrency = Currency.getInstance(toCurrency);

        var response = new CurrencyBeaconResponse(12345L, baseCurrency.getCurrencyCode(), targetCurrency.getCurrencyCode(), amount, "500.591");
        when(restTemplate.getForEntity(expectedUri, CurrencyBeaconResponse.class)).thenReturn(ResponseEntity.ok(response));

        // Act
        var result = target.getConvertedValue(baseCurrency, targetCurrency, new BigDecimal(amount));

        // Assert
        // Result must be rounded up to next decimal
        assertThat(result).isEqualByComparingTo("500.60");
    }

    @Test
    @DisplayName("Throws exception if the service call returns a bad response code")
    void getConvertedValue_badReturnCode_throwsException() {
        // Arrange
        ReflectionTestUtils.setField(target, "baseUrl", "https://whatever.com");
        ReflectionTestUtils.setField(target, "apiKey", "secretKey");

        when(restTemplate.getForEntity(anyString(), eq(CurrencyBeaconResponse.class)))
                .thenReturn(ResponseEntity.badRequest().build());

        // Act
        var exception = assertThrows(CurrencyConversionException.class, () ->
                        target.getConvertedValue(Currency.getInstance("USD"), Currency.getInstance("EUR"), new BigDecimal("400")),
                "A currency conversion exception should be thrown if there is a bad response code");

        // Assert
        assertThat(exception).hasMessage("There was an issue calling currency service");
    }

    @Test
    @DisplayName("Throws exception if the service call returns null body")
    void getConvertedValue_nullBody_throwsException() {
        // Arrange
        ReflectionTestUtils.setField(target, "baseUrl", "https://whatever.com");
        ReflectionTestUtils.setField(target, "apiKey", "secretKey");

        when(restTemplate.getForEntity(anyString(), eq(CurrencyBeaconResponse.class)))
                .thenReturn(ResponseEntity.ok(null));

        // Act
        var exception = assertThrows(CurrencyConversionException.class, () ->
                        target.getConvertedValue(Currency.getInstance("USD"), Currency.getInstance("EUR"), new BigDecimal("400")),
                "A currency conversion exception should be thrown if there is a bad response code");

        // Assert
        assertThat(exception).hasMessage("There was an issue calling currency service");
    }
}