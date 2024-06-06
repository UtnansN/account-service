package com.utnans.accountservice.controller;

import com.utnans.accountservice.dto.ClientDto;
import com.utnans.accountservice.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("{id}")
    public ClientDto getClient(@PathVariable Long id) {
        return clientService.getClientDto(id);
    }

}
