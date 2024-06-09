package com.utnans.accountservice.service.impl;

import com.utnans.accountservice.dto.AccountDto;
import com.utnans.accountservice.dto.TransactionDto;
import com.utnans.accountservice.exception.BadRequestException;
import com.utnans.accountservice.mapper.AccountMapper;
import com.utnans.accountservice.mapper.TransactionMapper;
import com.utnans.accountservice.repository.ClientRepository;
import com.utnans.accountservice.repository.TransactionRepository;
import com.utnans.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
        var clientEntity = clientRepository.findById(id).orElseThrow(() ->
                new BadRequestException("Unable to find accounts for id: %s".formatted(id)));
        return accountMapper.toAccountDtos(clientEntity.getAccounts());
    }

    @Override
    public List<TransactionDto> getTransactions(String acctNo, int offset, int limit) {
        // Pageable by default also has a field for sorting, but I'm going to restrict this app from using it at all.
        // Therefore, allowing controller to accept only offset and limit and creating a pageable here.
        var transactions = transactionRepository.findTransactionsForAccount(acctNo, PageRequest.of(offset, limit));
        return transactionMapper.toTransactionDtos(transactions);
    }

}
