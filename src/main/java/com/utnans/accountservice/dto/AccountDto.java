package com.utnans.accountservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {
    private String acctNo;
    private String currency;
    private BigDecimal balance;
}
