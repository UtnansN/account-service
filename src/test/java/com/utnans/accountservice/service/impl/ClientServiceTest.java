package com.utnans.accountservice.service.impl;

import com.utnans.accountservice.dto.ClientDto;
import com.utnans.accountservice.entity.Client;
import com.utnans.accountservice.mapper.ClientMapper;
import com.utnans.accountservice.repository.ClientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl target;

    @Test
    @DisplayName("Gets all clients given a pagination item")
    void getClients_success() {
        // Arrange
        var pageable = PageRequest.of(0, 10, Sort.by("firstName"));
        var returnPage = Page.<Client>empty(pageable); // Doesn't need any items; just checking reference

        when(clientRepository.findAll(pageable)).thenReturn(returnPage);

        var clientDto = new ClientDto();
        clientDto.setId(1L);
        var clientDto2 = new ClientDto();
        clientDto2.setId(2L);

        when(clientMapper.toClientDtos(any())).thenReturn(List.of(clientDto, clientDto2));

        // Act
        var response = target.getClients(pageable);

        // Assert
        assertThat(response).hasSize(2);
        assertThat(response.get(0).getId()).isEqualTo(clientDto.getId());
        assertThat(response.get(1).getId()).isEqualTo(clientDto2.getId());
    }
}