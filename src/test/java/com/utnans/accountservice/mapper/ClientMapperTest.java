package com.utnans.accountservice.mapper;

import com.utnans.accountservice.dto.AccountDto;
import com.utnans.accountservice.dto.ClientDto;
import com.utnans.accountservice.entity.Client;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ClientMapperTest {

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private ClientMapperImpl target;

    @Test
    @DisplayName("Maps single client properly")
    void toClientDto() {
        // Arrange
        var client = client(1L, "John", "Smith", "jsmith@test.com", "123415-12345");

        var account = accountDto("12345");
        var account2 = accountDto("54323");
        var mappedAccounts = List.of(account, account2);

        when(accountMapper.toAccountDtos(any())).thenReturn(mappedAccounts);

        // Act
        var result = target.toClientDto(client);

        // Assert
        verifyBaseMappings(client, result, mappedAccounts);
    }

    @Test
    @DisplayName("Maps multiple clients properly")
    void toClientDtos() {
        // Arrange
        var client = client(1L, "John", "Smith", "jsmith@test.com", "123415-12345");
        var account = accountDto("12345");
        var account2 = accountDto("54323");
        var mappedAccounts = List.of(account, account2);

        var client2 = client(2L, "Jane", "Doe", "jdoe@test.com", "987654-98765");
        var account3 = accountDto("64542");
        var mappedAccounts2 = List.of(account3);

        //noinspection unchecked
        when(accountMapper.toAccountDtos(any())).thenReturn(mappedAccounts, mappedAccounts2);

        // Act
        var result = target.toClientDtos(List.of(client, client2));

        // Assert
        assertThat(result).hasSize(2);

        verifyBaseMappings(client, result.get(0), mappedAccounts);
        verifyBaseMappings(client2, result.get(1), mappedAccounts2);
    }

    private void verifyBaseMappings(Client client, ClientDto result, List<AccountDto> mappedAccounts) {
        assertThat(result.getId()).isEqualTo(client.getId());
        assertThat(result.getFirstName()).isEqualTo(client.getFirstName());
        assertThat(result.getLastName()).isEqualTo(client.getLastName());
        assertThat(result.getEmail()).isEqualTo(client.getEmail());
        assertThat(result.getTaxNumber()).isEqualTo(client.getTaxNumber());
        assertThat(result.getAccounts()).isEqualTo(mappedAccounts);
    }

    private Client client(Long id, String firstName, String lastName, String email, String taxNumber) {
        var client = new Client();
        client.setId(id);
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);
        client.setTaxNumber(taxNumber);
        return client;
    }

    private AccountDto accountDto(String acctNo) {
        var accountDto = new AccountDto();
        accountDto.setAcctNo(acctNo);
        return accountDto;
    }
}