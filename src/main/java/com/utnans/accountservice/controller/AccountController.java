package com.utnans.accountservice.controller;

import com.utnans.accountservice.dto.TransactionDto;
import com.utnans.accountservice.dto.TransferRequestDto;
import com.utnans.accountservice.service.AccountService;
import com.utnans.accountservice.service.MoneyTransferService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final MoneyTransferService moneyTransferService;

    @GetMapping("{acctNo}/transactions")
    public List<TransactionDto> getTransactions(
            @PathVariable String acctNo,
            @RequestParam(value = "offset", defaultValue = "0") @PositiveOrZero(message = "Offset may not be < 0") int offset,
            @RequestParam(value = "limit", defaultValue = "5") @Positive(message = "Limit may not be <= 0") int limit) {
        return accountService.getTransactions(acctNo, offset, limit);
    }

    @PutMapping("send")
    public void transferMoney(@Valid @RequestBody TransferRequestDto transferRequestDto) {
        moneyTransferService.transferMoney(transferRequestDto);
    }
}
