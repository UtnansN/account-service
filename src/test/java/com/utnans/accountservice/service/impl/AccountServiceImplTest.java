package com.utnans.accountservice.service.impl;

import com.utnans.accountservice.dto.AccountDto;
import com.utnans.accountservice.dto.TransactionDto;
import com.utnans.accountservice.entity.Account;
import com.utnans.accountservice.entity.Client;
import com.utnans.accountservice.entity.Transaction;
import com.utnans.accountservice.exception.NotFoundException;
import com.utnans.accountservice.mapper.AccountMapper;
import com.utnans.accountservice.mapper.TransactionMapper;
import com.utnans.accountservice.repository.AccountRepository;
import com.utnans.accountservice.repository.ClientRepository;
import com.utnans.accountservice.repository.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl target;

    @Test
    @DisplayName("Returns accounts if client is found")
    void getClientAccounts_clientFound_returnsAccounts() {
        // Arrange
        var accountList = List.of(new Account(), new Account());
        var client = new Client();
        client.setAccounts(accountList);

        var dto1 = new AccountDto();
        dto1.setAcctNo("12345678");
        var dto2 = new AccountDto();
        dto2.setAcctNo("98765432");
        var accountDtos = List.of(dto1, dto2);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(accountMapper.toAccountDtos(accountList)).thenReturn(accountDtos);

        // Act
        var result = target.getClientAccounts(1L);

        // Assert
        assertThat(result).isEqualTo(accountDtos);
    }

    @Test
    @DisplayName("Throws exception if client is not found")
    void getClientAccounts_clientNotFound_throwsException() {
        // Arrange
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        var exception = assertThrows(NotFoundException.class, () -> target.getClientAccounts(1L),
                "Expected AccountService to throw an exception if client was not found");

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Client with id: 1 was not found");
    }

    @Test
    @DisplayName("Returns transactions given an account number, offset and limit")
    void getTransactions_accountFound_returnsTransactions() {
        // Arrange
        final var acctNo = "12345";
        final var offset = 3;
        final var limit = 10;

        var pageCaptor = ArgumentCaptor.forClass(Pageable.class);
        var transactions = List.of(new Transaction());
        var transactionDtos = List.of(new TransactionDto());

        when(transactionRepository.findTransactionsForAccount(eq(acctNo), pageCaptor.capture())).thenReturn(transactions);
        when(transactionMapper.toTransactionDtos(transactions)).thenReturn(transactionDtos);
        when(accountRepository.existsById(acctNo)).thenReturn(true);

        // Act
        var result = target.getTransactions(acctNo, offset, limit);

        // Assert
        assertThat(result).isEqualTo(transactionDtos);

        var capturedPageable = pageCaptor.getValue();
        assertThat(capturedPageable.getPageNumber()).isEqualTo(offset);
        assertThat(capturedPageable.getPageSize()).isEqualTo(limit);
    }

    @Test
    @DisplayName("Throws an exception if trying to get the transactions for an account which does not exist")
    void getTransactions_accountNotFound_throwsException() {
        // Arrange
        final var acctNo = "12345";

        when(accountRepository.existsById(acctNo)).thenReturn(false);

        // Act
        var exception = assertThrows(NotFoundException.class, () -> target.getTransactions(acctNo, 3, 10),
                "An exception must be thrown if the given account number is not found");

        // Assert
        assertThat(exception).hasMessage("Account with id: 12345 was not found");

    }
}