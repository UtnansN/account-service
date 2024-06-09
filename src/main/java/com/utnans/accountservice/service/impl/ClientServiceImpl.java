package com.utnans.accountservice.service.impl;

import com.utnans.accountservice.dto.ClientDto;
import com.utnans.accountservice.mapper.ClientMapper;
import com.utnans.accountservice.repository.ClientRepository;
import com.utnans.accountservice.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientMapper clientMapper;
    private final ClientRepository clientRepository;

    @Override
    public List<ClientDto> getClients(Pageable pageable) {
        var clients = clientRepository.findAll(pageable);
        return clientMapper.toClientDtos(clients.toList());
    }
}
