package com.utnans.accountservice.mapper;

import com.utnans.accountservice.dto.ClientDto;
import com.utnans.accountservice.entity.Client;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR, // Don't want to create spring context for simple tests
        uses = AccountMapper.class)
public interface ClientMapper {
    ClientDto toClientDto(Client client);
    List<ClientDto> toClientDtos(List<Client> clients);
}
