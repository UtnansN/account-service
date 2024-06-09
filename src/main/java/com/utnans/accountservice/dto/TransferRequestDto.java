package com.utnans.accountservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequestDto {

    private static final String CURR_CODE_MESSAGE = "A valid ISO-4217 currency code must be provided";

    @NotNull(message = "Sender account may not be null")
    private String senderAccount;

    @NotNull(message = "Receiver account may not be null")
    private String receiverAccount;

    @NotNull(message = CURR_CODE_MESSAGE)
    @Pattern(regexp = "^[A-Z]{3}$", message = CURR_CODE_MESSAGE)
    private String currency;

    @NotNull(message = "Amount may not be null")
    @Positive(message = "Amount must be a positive value")
    @Digits(integer = 16, fraction = 2, message = "Amount may have up to 16 significant figures and 2 decimal places")
    private BigDecimal amount;
}
