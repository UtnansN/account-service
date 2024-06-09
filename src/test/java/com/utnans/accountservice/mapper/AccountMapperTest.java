package com.utnans.accountservice.mapper;

import com.utnans.accountservice.dto.AccountDto;
import com.utnans.accountservice.entity.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AccountMapperTest {

    private final AccountMapper target = new AccountMapperImpl();

    @Test
    @DisplayName("Maps AccountDto values properly")
    void toAccountDto_success() {
        // Arrange
        var account = getAccount("12345", "HKD", "100.55");

        // Act
        var result = target.toAccountDto(account);

        // Assert
        compareMappings(account, result);
    }

    @Test
    @DisplayName("Maps AccountDto list values properly")
    void toAccountDtos_success() {
        // Arrange
        var account = getAccount("12345", "HKD", "100.55");
        var account2 = getAccount("96543", "USD", "550.22");

        // Act
        var result = target.toAccountDtos(List.of(account, account2));

        // Assert
        assertThat(result).hasSize(2);
        compareMappings(account, result.get(0));
        compareMappings(account2, result.get(1));
    }

    private void compareMappings(Account account, AccountDto result) {
        assertThat(result.getAcctNo()).isEqualTo(account.getAcctNo());
        assertThat(result.getCurrency()).isEqualTo(account.getCurrency().getCurrencyCode());
        assertThat(result.getBalance()).isEqualTo(account.getBalance());
    }

    private Account getAccount(String acctNo, String currencyCode, String balance) {
        var account = new Account();
        account.setAcctNo(acctNo);
        account.setCurrency(Currency.getInstance(currencyCode));
        account.setBalance(new BigDecimal(balance));
        return account;
    }
}