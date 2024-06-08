package com.utnans.accountservice.dto.integrations;

public record CurrencyBeaconResponse(Long timestamp, String from, String to, String amount, String value) {
}
