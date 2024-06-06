package com.utnans.accountservice.mapper;

import com.utnans.accountservice.dto.ClientDto;
import com.utnans.accountservice.entity.Client;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ClientMapper {

    ClientDto toClientDto(Client client);
}
