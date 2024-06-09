package com.utnans.accountservice.service;

import com.utnans.accountservice.dto.AccountDto;
import com.utnans.accountservice.dto.TransactionDto;

import java.util.List;

public interface AccountService {
    List<AccountDto> getClientAccounts(Long id);

    List<TransactionDto> getTransactions(String acctNo, int offset, int limit);
}
