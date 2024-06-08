package com.utnans.accountservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequestDto {
    @NotNull
    private Long senderAccount;

    @NotNull
    private Long receiverAccount;

    @NotBlank
    private String currency;

    @NotNull
    @Positive
    private BigDecimal amount;
}
