package com.utnans.accountservice.controller;

import com.utnans.accountservice.dto.TransactionDto;
import com.utnans.accountservice.dto.TransferRequestDto;
import com.utnans.accountservice.service.AccountService;
import com.utnans.accountservice.service.MoneyTransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final MoneyTransferService moneyTransferService;

    @Operation(summary = "Get latest transactions for a given account", description = "Returns the last 5 transactions by default.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the latest transactions"),
            @ApiResponse(responseCode = "404", description = "The given account does not exist")
    })
    @GetMapping("{acctNo}/transactions")
    public List<TransactionDto> getTransactions(
            @PathVariable String acctNo,
            @RequestParam(value = "offset", defaultValue = "0") @PositiveOrZero(message = "Offset may not be < 0") int offset,
            @RequestParam(value = "limit", defaultValue = "5") @Positive(message = "Limit may not be <= 0") int limit) {
        return accountService.getTransactions(acctNo, offset, limit);
    }

    @Operation(summary = "Transfer funds between accounts",
            description = "Transfers funds between accounts, converting the sender's account currency to target account currency.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Money was transferred successfully"),
            @ApiResponse(responseCode = "400", description = "The input was malformed or the user had not enough funds to do the operation"),
            @ApiResponse(responseCode = "404", description = "One of the target accounts was not found"),
            @ApiResponse(responseCode = "424", description = "The external service call for currency conversion failed")
    })
    @PutMapping("transfer")
    public void transferMoney(@Valid @RequestBody TransferRequestDto transferRequestDto) {
        moneyTransferService.transferMoney(transferRequestDto);
    }
}
