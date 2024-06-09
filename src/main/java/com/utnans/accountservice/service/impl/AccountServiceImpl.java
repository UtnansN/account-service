package com.utnans.accountservice.service.impl;

import com.utnans.accountservice.dto.AccountDto;
import com.utnans.accountservice.dto.TransactionDto;
import com.utnans.accountservice.mapper.AccountMapper;
import com.utnans.accountservice.mapper.TransactionMapper;
import com.utnans.accountservice.repository.ClientRepository;
import com.utnans.accountservice.repository.TransactionRepository;
import com.utnans.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final ClientRepository clientRepository;
    private final AccountMapper accountMapper;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;

    @Override
    public List<AccountDto> getClientAccounts(Long id) {
        var clientEntity = clientRepository.getReferenceById(id);
        return accountMapper.toAccountDtos(clientEntity.getAccounts());
    }

    @Override
    public List<TransactionDto> getLatestTransactions(String acctNo, Pageable pageable) {
        var transactions = transactionRepository.findTransactionsForAccount(acctNo, pageable);
        return transactionMapper.toTransactionDtos(transactions);
    }

}
