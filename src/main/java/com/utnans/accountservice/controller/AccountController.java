package com.utnans.accountservice.controller;

import com.utnans.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

}
