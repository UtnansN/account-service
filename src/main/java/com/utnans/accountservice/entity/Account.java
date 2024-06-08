package com.utnans.accountservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "account")
public class Account {

    @Id
    private String acctNo;

    @NotNull
    private Currency currency;

    @PositiveOrZero
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "senderAccount", fetch = FetchType.LAZY)
    private List<Transaction> outgoingTransactions;

    @OneToMany(mappedBy = "receiverAccount", fetch = FetchType.LAZY)
    private List<Transaction> incomingTransactions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return new EqualsBuilder().append(acctNo, account.acctNo).append(currency, account.currency).append(balance, account.balance).append(client, account.client).append(outgoingTransactions, account.outgoingTransactions).append(incomingTransactions, account.incomingTransactions).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(acctNo).append(currency).append(balance).append(client).append(outgoingTransactions).append(incomingTransactions).toHashCode();
    }
}
