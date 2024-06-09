package com.utnans.accountservice.service.impl;

import com.utnans.accountservice.dto.AccountDto;
import com.utnans.accountservice.dto.TransactionDto;
import com.utnans.accountservice.exception.NotFoundException;
import com.utnans.accountservice.mapper.AccountMapper;
import com.utnans.accountservice.mapper.TransactionMapper;
import com.utnans.accountservice.repository.AccountRepository;
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

    private final AccountMapper accountMapper;
    private final TransactionMapper transactionMapper;
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public List<AccountDto> getClientAccounts(Long id) {
        var clientEntity = clientRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Client with id: %s was not found".formatted(id)));
        return accountMapper.toAccountDtos(clientEntity.getAccounts());
    }

    @Override
    public List<TransactionDto> getTransactions(String acctNo, int offset, int limit) {
        // Pageable by default also has a field for sorting, but I'm going to restrict this app from using it at all.
        // Therefore, allowing controller to accept only offset and limit and creating a pageable here.

        // An extra call but probably better to throw a 404 in case the account does not exist at all.
        // Might be better to load the account, and add the ORM mappings to the transactions, however I did not figure
        // out a clean way to load both sender/receiver records into one list in a clean way, without using double entry
        // bookkeeping for the transactions (2 records - one for each side of the transaction).
        // Does not reduce the amount of database calls either way with lazily loaded entities.
        if (!accountRepository.existsById(acctNo)) {
            throw new NotFoundException("Account with id: %s was not found".formatted(acctNo));
        }

        var transactions = transactionRepository.findTransactionsForAccount(acctNo, PageRequest.of(offset, limit));
        return transactionMapper.toTransactionDtos(transactions);
    }

}
