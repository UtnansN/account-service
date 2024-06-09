package com.utnans.accountservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDto {
    private Long transactionId;
    private LocalDateTime execDateTime;
    private BigDecimal sentAmount;
    private String sentCurrency;

    private Long senderAccountNo;
    private String senderName;
    private Long receiverAccountNo;
    private String receiverName;
}