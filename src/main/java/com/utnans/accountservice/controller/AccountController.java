package com.utnans.accountservice.controller;

import com.utnans.accountservice.dto.TransactionDto;
import com.utnans.accountservice.dto.TransferRequestDto;
import com.utnans.accountservice.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("{acctNo}/transactions")
    public List<TransactionDto> getTransactions(@PathVariable String acctNo,
                                                @PageableDefault(value = 5) Pageable pageable) {
        return accountService.getLatestTransactions(acctNo, pageable);
    }

    @PutMapping("send")
    public void transferMoney(@Valid @RequestBody TransferRequestDto transferRequestDto) {
        accountService.transferMoney(transferRequestDto);
    }
}
