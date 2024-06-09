package com.utnans.accountservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequestDto {

    private static final String CURR_CODE_MESSAGE = "A valid ISO-4217 currency code must be provided";

    @Schema(example = "39237032")
    @NotNull(message = "Sender account may not be null")
    private String senderAccount;

    @Schema(example = "79842731")
    @NotNull(message = "Receiver account may not be null")
    private String receiverAccount;

    @Schema(description = "A 3 letter currency code which must match the currency of the receiver", example = "EUR")
    @NotNull(message = CURR_CODE_MESSAGE)
    @Pattern(regexp = "^[A-Z]{3}$", message = CURR_CODE_MESSAGE)
    private String currency;

    @Schema(description = "The amount that should be credited to receiving account.", example = "50.00")
    @NotNull(message = "Amount may not be null")
    @Positive(message = "Amount must be a positive value")
    @Digits(integer = 16, fraction = 2, message = "Amount may have up to 16 significant figures and 2 decimal places")
    private BigDecimal amount;
}
