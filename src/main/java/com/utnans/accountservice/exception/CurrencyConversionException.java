package com.utnans.accountservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FAILED_DEPENDENCY,
        reason = "There was an issue during currency conversion. Please try again later or with the same currency in both accounts.")
public class CurrencyConversionException extends RuntimeException {
    public CurrencyConversionException(String message) {
        super(message);
    }
}
