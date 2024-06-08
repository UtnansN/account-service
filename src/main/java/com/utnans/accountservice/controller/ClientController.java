package com.utnans.accountservice.controller;

import com.utnans.accountservice.dto.AccountDto;
import com.utnans.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("client")
@RequiredArgsConstructor
public class ClientController {

    private final AccountService accountService;

    @GetMapping("{id}/accounts")
    public List<AccountDto> getClientAccounts(@PathVariable Long id) {
        return accountService.getClientAccounts(id);
    }
}
