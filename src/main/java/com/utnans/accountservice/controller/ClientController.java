package com.utnans.accountservice.controller;

import com.utnans.accountservice.dto.AccountDto;
import com.utnans.accountservice.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("client")
@RequiredArgsConstructor
public class ClientController {

    private final AccountService accountService;

    @Operation(summary = "Get the accounts for a given client ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the latest transactions"),
            @ApiResponse(responseCode = "404", description = "The given account does not exist")
    })
    @GetMapping("{id}/accounts")
    public List<AccountDto> getClientAccounts(@PathVariable Long id) {
        return accountService.getClientAccounts(id);
    }
}
