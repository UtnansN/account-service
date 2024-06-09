package com.utnans.accountservice.service;

import com.utnans.accountservice.dto.ClientDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {
    List<ClientDto> getClients(Pageable pageable);
}
