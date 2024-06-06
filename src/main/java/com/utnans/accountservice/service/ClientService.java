package com.utnans.accountservice.service;

import com.utnans.accountservice.dto.ClientDto;
import com.utnans.accountservice.mapper.ClientMapper;
import com.utnans.accountservice.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientMapper clientMapper;
    private final ClientRepository clientRepository;

    public ClientDto getClientDto(Long id) {
        var clientEntity = clientRepository.getReferenceById(id);
        return clientMapper.toClientDto(clientEntity);
    }

}
