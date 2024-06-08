package com.utnans.accountservice.service;

import java.math.BigDecimal;
import java.util.Currency;

public interface CurrencyService {
    BigDecimal getConvertedValue(Currency baseCurrency, Currency targetCurrency, BigDecimal amount);
}
