package com.utnans.accountservice.mapper;

import com.utnans.accountservice.dto.TransactionDto;
import com.utnans.accountservice.entity.Account;
import com.utnans.accountservice.entity.Client;
import com.utnans.accountservice.entity.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionMapperTest {

    private final TransactionMapper target = new TransactionMapperImpl();

    @Test
    @DisplayName("Maps TransactionDto values properly")
    void toTransactionDto_success() {
        // Arrange
        var execTime = LocalDateTime.of(2023, 1, 1, 12, 30);

        var sender = account("65423", "USD", "John", "Smith");
        var receiver = account("63432", "HKD", "Bob", "Dylan");

        var tx = transaction(123L, execTime, sender, receiver, "1000", "5000");

        // Act
        var result = target.toTransactionDto(tx);

        // Assert
        compareMappings(tx, result);
    }

    @Test
    @DisplayName("Maps TransactionDto list values properly")
    void toTransactionDtos_success() {
        // Arrange
        var execTime = LocalDateTime.of(2023, 1, 1, 12, 30);
        var execTime2 = LocalDateTime.of(2020, 5, 12, 15, 26);

        var sender = account("65423", "USD", "John", "Smith");
        var sender2 = account("45902", "EUR", "Jane", "Doe");

        var receiver = account("63432", "HKD", "Bob", "Dylan");
        var receiver2 = account("23123", "AUD", "Maurice", "Mauritius");

        var tx = transaction(123L, execTime, sender, receiver, "1000", "5000");
        var tx2 = transaction(456L, execTime2, sender2, receiver2, "400", "200");

        // Act
        var result = target.toTransactionDtos(List.of(tx, tx2));

        // Assert
        assertThat(result).hasSize(2);
        compareMappings(tx, result.get(0));
        compareMappings(tx2, result.get(1));
    }

    private void compareMappings(Transaction transaction, TransactionDto result) {
        assertThat(result.getTransactionId()).isEqualTo(transaction.getId());
        assertThat(result.getExecDateTime()).isEqualTo(transaction.getExecDateTime());
        assertThat(result.getSentCurrency()).isEqualTo(transaction.getReceiverAccount().getCurrency().getCurrencyCode());
        assertThat(result.getSentAmount()).isEqualTo(transaction.getReceiverAmount());

        assertThat(result.getSenderName()).isEqualTo(transaction.getSenderAccount().getClient().getFullName());
        assertThat(result.getSenderAccountNo()).isEqualTo(transaction.getSenderAccount().getAcctNo());
        assertThat(result.getReceiverName()).isEqualTo(transaction.getReceiverAccount().getClient().getFullName());
        assertThat(result.getReceiverAccountNo()).isEqualTo(transaction.getReceiverAccount().getAcctNo());
    }

    private Transaction transaction(Long id, LocalDateTime execDateTime,
                                    Account senderAccount, Account receiverAccount,
                                    String senderAmount, String receiverAmount) {
        var transaction = new Transaction();
        transaction.setId(id);
        transaction.setExecDateTime(execDateTime);
        transaction.setSenderAccount(senderAccount);
        transaction.setSenderAmount(new BigDecimal(senderAmount));
        transaction.setReceiverAccount(receiverAccount);
        transaction.setReceiverAmount(new BigDecimal(receiverAmount));
        return transaction;
    }

    private static Account account(String acctNo, String currency, String clientFirstName, String clientLastName) {
        var client = new Client();
        client.setFirstName(clientFirstName);
        client.setFirstName(clientLastName);

        var senderAccount = new Account();
        senderAccount.setAcctNo(acctNo);
        senderAccount.setCurrency(Currency.getInstance(currency));
        senderAccount.setClient(client);
        return senderAccount;
    }
}