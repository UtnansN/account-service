package com.utnans.accountservice.service;

import com.utnans.accountservice.dto.AccountDto;
import com.utnans.accountservice.dto.TransactionDto;
import com.utnans.accountservice.dto.TransferRequestDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountService {
    List<AccountDto> getClientAccounts(Long id);

    List<TransactionDto> getLatestTransactions(String acctNo, Pageable pageable);

    void transferMoney(TransferRequestDto transferRequestDto);
}
