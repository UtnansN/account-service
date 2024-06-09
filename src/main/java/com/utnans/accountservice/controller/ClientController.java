package com.utnans.accountservice.controller;

import com.utnans.accountservice.dto.AccountDto;
import com.utnans.accountservice.dto.ClientDto;
import com.utnans.accountservice.service.AccountService;
import com.utnans.accountservice.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final AccountService accountService;

    @Operation(summary = "Gets clients registered in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved clients"),
    })
    @GetMapping("clients")
    public List<ClientDto> getClients(@PageableDefault(sort = {"lastName", "firstName"}) @ParameterObject Pageable pageable) {
        return clientService.getClients(pageable);
    }

    @Operation(summary = "Get the accounts for a given client ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the latest transactions"),
            @ApiResponse(responseCode = "404", description = "The given account does not exist")
    })
    @GetMapping("client/{id}/accounts")
    public List<AccountDto> getClientAccounts(@PathVariable @Parameter(example = "1") Long id) {
        return accountService.getClientAccounts(id);
    }
}
