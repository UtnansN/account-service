package com.utnans.accountservice.service.impl;

import com.utnans.accountservice.dto.TransferRequestDto;
import com.utnans.accountservice.entity.Account;
import com.utnans.accountservice.entity.Transaction;
import com.utnans.accountservice.exception.BadRequestException;
import com.utnans.accountservice.repository.AccountRepository;
import com.utnans.accountservice.repository.TransactionRepository;
import com.utnans.accountservice.service.CurrencyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoneyTransferServiceImplTest {

    @Mock
    private CurrencyService currencyService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private MoneyTransferServiceImpl target;

    @Test
    @DisplayName("Transfer given two valid accounts with same currencies and sufficient funds should save transaction data")
    void transferMoney_sameCurrencies_success() {
        // Arrange
        var currency = Currency.getInstance("USD");
        var amount = BigDecimal.valueOf(250);

        var dto = baseRequest();
        dto.setCurrency("USD");
        dto.setAmount(amount);

        var sender = account("98765", "1000", currency);
        when(accountRepository.findById(dto.getSenderAccount())).thenReturn(Optional.of(sender));
        var receiver = account("12345", "5000", currency);
        when(accountRepository.findById(dto.getReceiverAccount())).thenReturn(Optional.of(receiver));

        // Act
        target.transferMoney(dto);

        // Assert
        var transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        verify(currencyService, never()).getConvertedValue(any(), any(), any());
        verify(accountRepository).saveAll(List.of(sender, receiver));
        verify(transactionRepository).save(transactionCaptor.capture());

        assertThat(sender.getBalance()).isEqualByComparingTo("750");
        assertThat(receiver.getBalance()).isEqualByComparingTo("5250");

        var transaction = transactionCaptor.getValue();
        assertThat(transaction.getExecDateTime()).isCloseTo(LocalDateTime.now(), within(2, ChronoUnit.SECONDS));
        assertThat(transaction.getSenderAccount()).isEqualTo(sender);
        assertThat(transaction.getReceiverAccount()).isEqualTo(receiver);
        assertThat(amount).isEqualTo(transaction.getSenderAmount()).isEqualTo(transaction.getReceiverAmount());
    }

    @Test
    @DisplayName("Transfer given two valid accounts with different currencies and sufficient funds should save transaction data")
    void transferMoney_differentCurrencies_success() {
        // Arrange
        var senderCurrency = Currency.getInstance("EUR");
        var receiverCurrency = Currency.getInstance("USD");
        var amount = BigDecimal.valueOf(250);
        var convertedAmount = BigDecimal.valueOf(500.25);

        var dto = baseRequest();
        dto.setCurrency(receiverCurrency.getCurrencyCode());
        dto.setAmount(amount);

        var sender = account("98765", "1000", senderCurrency);
        when(accountRepository.findById(dto.getSenderAccount())).thenReturn(Optional.of(sender));
        var receiver = account("12345", "5000", receiverCurrency);
        when(accountRepository.findById(dto.getReceiverAccount())).thenReturn(Optional.of(receiver));

        when(currencyService.getConvertedValue(receiverCurrency, senderCurrency, amount)).thenReturn(convertedAmount);

        // Act
        target.transferMoney(dto);

        // Assert
        var transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        verify(accountRepository).saveAll(List.of(sender, receiver));
        verify(transactionRepository).save(transactionCaptor.capture());

        assertThat(sender.getBalance()).isEqualByComparingTo("499.75");
        assertThat(receiver.getBalance()).isEqualByComparingTo("5250");

        var transaction = transactionCaptor.getValue();
        assertThat(transaction.getExecDateTime()).isCloseTo(LocalDateTime.now(), within(2, ChronoUnit.SECONDS));
        assertThat(transaction.getSenderAccount()).isEqualTo(sender);
        assertThat(transaction.getReceiverAccount()).isEqualTo(receiver);
        assertThat(transaction.getSenderAmount()).isEqualTo(convertedAmount);
        assertThat(transaction.getReceiverAmount()).isEqualTo(amount);
    }

    @Test
    @DisplayName("Throws exception when sender account is not found")
    void transferMoney_senderNotFound_exceptionThrown() {
        // Arrange
        var dto = baseRequest();
        when(accountRepository.findById(dto.getSenderAccount())).thenReturn(Optional.empty());

        // Act
        var exception = assertThrows(BadRequestException.class, () -> target.transferMoney(dto),
                "An exception should be thrown if sender is not found");

        // Assert
        assertThat(exception).hasMessage("Sending account was not found");
    }

    @Test
    @DisplayName("Throws exception when receiver account is not found")
    void transferMoney_receiverNotFound_exceptionThrown() {
        // Arrange
        var dto = baseRequest();
        when(accountRepository.findById(dto.getSenderAccount())).thenReturn(Optional.of(new Account()));
        when(accountRepository.findById(dto.getReceiverAccount())).thenReturn(Optional.empty());

        // Act
        var exception = assertThrows(BadRequestException.class, () -> target.transferMoney(dto),
                "An exception should be thrown if receiver is not found");

        // Assert
        assertThat(exception).hasMessage("Receiving account was not found");
    }

    @Test
    @DisplayName("Throws exception when currency is not recognized")
    void transferMoney_currencyNotRecognized_exceptionThrown() {
        // Arrange
        var dto = baseRequest();
        dto.setCurrency("XYZ");

        when(accountRepository.findById(dto.getSenderAccount())).thenReturn(Optional.of(new Account()));
        when(accountRepository.findById(dto.getReceiverAccount())).thenReturn(Optional.of(new Account()));

        // Act
        var exception = assertThrows(BadRequestException.class, () -> target.transferMoney(dto),
                "An exception should be thrown if currency is not recognized");

        // Assert
        assertThat(exception).hasMessage("Currency code was not recognized");
    }

    @Test
    @DisplayName("Throws exception when receiver currency does not match request currency")
    void transferMoney_mismatchingCurrencies_exceptionThrown() {
        // Arrange
        var dto = baseRequest();
        dto.setCurrency("USD");

        when(accountRepository.findById(dto.getSenderAccount())).thenReturn(Optional.of(new Account()));
        var receiver = account("12345", "5000.00", Currency.getInstance("EUR"));
        when(accountRepository.findById(dto.getReceiverAccount())).thenReturn(Optional.of(receiver));

        // Act
        var exception = assertThrows(BadRequestException.class, () -> target.transferMoney(dto),
                "An exception should be thrown if currency is does not match receiver currency");

        // Assert
        assertThat(exception).hasMessage("Unable to transfer USD to an EUR account");
    }

    @Test
    @DisplayName("Throws exception when the sender has insufficient funds to transfer between same currency accounts")
    void transferMoney_sameCurrencyAccounts_notEnoughFunds_exceptionThrown() {
        // Arrange
        var currency =  Currency.getInstance("USD");

        var dto = baseRequest();
        dto.setAmount(new BigDecimal("250.50"));
        dto.setCurrency(currency.getCurrencyCode());


        var sender = account("98765", "100", currency);
        when(accountRepository.findById(dto.getSenderAccount())).thenReturn(Optional.of(sender));

        var receiver = account("12345", "5000.00", currency);
        when(accountRepository.findById(dto.getReceiverAccount())).thenReturn(Optional.of(receiver));

        // Act
        var exception = assertThrows(BadRequestException.class, () -> target.transferMoney(dto),
                "An exception should be thrown if sender has insufficient funds");

        // Assert
        assertThat(exception).hasMessage("Sender has insufficient funds to fulfil the request");
    }

    @Test
    @DisplayName("Throws exception when the sender has insufficient funds to transfer between different currency accounts")
    void transferMoney_differentCurrencyAccounts_notEnoughFunds_exceptionThrown() {
        // Arrange

        // Sent amount should be lower than sender balance, but after-conversion result should be higher to test
        // whether field is checked correctly (so it is not a comparison with unconverted value).
        var sentAmount = new BigDecimal("50");
        var senderCurrency =  Currency.getInstance("EUR");
        var receiverCurrency =  Currency.getInstance("USD");

        var dto = baseRequest();
        dto.setAmount(sentAmount);
        dto.setCurrency(receiverCurrency.getCurrencyCode());

        var sender = account("98765", "100", senderCurrency);
        when(accountRepository.findById(dto.getSenderAccount())).thenReturn(Optional.of(sender));

        var receiver = account("12345", "5000.00", receiverCurrency);
        when(accountRepository.findById(dto.getReceiverAccount())).thenReturn(Optional.of(receiver));

        when(currencyService.getConvertedValue(receiverCurrency, senderCurrency, sentAmount))
                .thenReturn(BigDecimal.valueOf(250));

        // Act
        var exception = assertThrows(BadRequestException.class, () -> target.transferMoney(dto),
                "An exception should be thrown if sender has insufficient funds");

        // Assert
        assertThat(exception).hasMessage("Sender has insufficient funds to fulfil the request");
    }

    private TransferRequestDto baseRequest() {
        var dto = new TransferRequestDto();
        dto.setSenderAccount("12345");
        dto.setReceiverAccount("98765");
        dto.setAmount(BigDecimal.valueOf(500));
        dto.setCurrency("USD");
        return dto;
    }

    private Account account(String acctNo, String balance, Currency currency) {
        var account = new Account();
        account.setAcctNo(acctNo);
        account.setBalance(new BigDecimal(balance));
        account.setCurrency(currency);
        return account;
    }
}