package com.utnans.accountservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class ClientDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String taxNumber;
    private List<AccountDto> accounts;
}
