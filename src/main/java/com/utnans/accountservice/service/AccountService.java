package com.utnans.accountservice.service;

import com.utnans.accountservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final CurrencyService currencyService;
    private final AccountRepository accountRepository;



}
