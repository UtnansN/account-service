package com.utnans.accountservice.service.impl;

import com.utnans.accountservice.dto.AccountDto;
import com.utnans.accountservice.dto.TransactionDto;
import com.utnans.accountservice.dto.TransferRequestDto;
import com.utnans.accountservice.entity.Account;
import com.utnans.accountservice.entity.Transaction;
import com.utnans.accountservice.exception.BadRequestException;
import com.utnans.accountservice.mapper.AccountMapper;
import com.utnans.accountservice.mapper.TransactionMapper;
import com.utnans.accountservice.repository.AccountRepository;
import com.utnans.accountservice.repository.ClientRepository;
import com.utnans.accountservice.repository.TransactionRepository;
import com.utnans.accountservice.service.AccountService;
import com.utnans.accountservice.service.CurrencyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final CurrencyService currencyService;
    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AccountMapper accountMapper;
    private final TransactionMapper transactionMapper;

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

    @Override
    @Transactional
    public void transferMoney(TransferRequestDto requestDto) {
        // Generally might be a wise idea to get both accounts in one fetch instead of getting by ID twice to reduce
        // database load. In this case going with fetching by id for simplicity.
        var senderAccount = accountRepository.findById(requestDto.getSenderAccount())
                .orElseThrow(() -> new BadRequestException("Sending account was not found"));
        var receiverAccount = accountRepository.findById(requestDto.getReceiverAccount())
                .orElseThrow(() -> new BadRequestException("Receiving account was not found"));

        Currency requestCurrency;
        try {
            requestCurrency = Currency.getInstance(requestDto.getCurrency());
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException("Currency code was not recognized");
        }

        var senderCurrency = senderAccount.getCurrency();
        var receiverCurrency = receiverAccount.getCurrency();
        verifyRequestedCurrency(requestCurrency, receiverCurrency);

        // Do currency conversion if required.
        final BigDecimal senderValue;
        final BigDecimal receiverValue = requestDto.getAmount();
        if (!senderCurrency.equals(receiverCurrency)) {
            senderValue = currencyService.getConvertedValue(receiverCurrency, senderCurrency, receiverValue);
        } else {
            // BigDecimal is immutable so this is safe.
            senderValue = receiverValue;
        }

        // Check if sender has the required funds to complete this transaction.
        if (senderAccount.getBalance().compareTo(senderValue) < 0) {
            throw new BadRequestException("Sender has insufficient funds to fulfil the request");
        }

        processTransfer(senderAccount, senderValue, receiverAccount, receiverValue);
    }

    private void verifyRequestedCurrency(Currency requestedCurrency, Currency receiverCurrency) {
        if (!requestedCurrency.equals(receiverCurrency)) {
            throw new BadRequestException("Unable to transfer %s to an %s account"
                    .formatted(requestedCurrency.getCurrencyCode(), receiverCurrency.getCurrencyCode()));
        }
    }

    private void processTransfer(Account senderAccount, BigDecimal senderValue,
                                 Account receiverAccount, BigDecimal receiverValue) {
        var origSenderBalance = senderAccount.getBalance();
        senderAccount.setBalance(origSenderBalance.subtract(senderValue));

        var origReceiverBalance = receiverAccount.getBalance();
        receiverAccount.setBalance(origReceiverBalance.add(receiverValue));
        accountRepository.saveAll(List.of(senderAccount, receiverAccount));

        var transaction = buildTransaction(senderAccount, senderValue, receiverAccount, receiverValue);
        transactionRepository.save(transaction);
    }

    private Transaction buildTransaction(Account senderAccount, BigDecimal senderValue, Account receiverAccount, BigDecimal receiverValue) {
        var transaction = new Transaction();
        transaction.setExecDateTime(LocalDateTime.now());
        transaction.setSenderAccount(senderAccount);
        transaction.setSenderAmount(senderValue);
        transaction.setReceiverAccount(receiverAccount);
        transaction.setReceiverAmount(receiverValue);
        return transaction;
    }

}
