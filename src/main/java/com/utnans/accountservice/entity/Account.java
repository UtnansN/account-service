package com.utnans.accountservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

@Entity
@Getter
@Setter
public class Account {

    @Id
    private Long acctNo;

    @NotNull
    private Currency currency;

    @PositiveOrZero
    private BigDecimal balance;

    @ManyToOne
    private Client client;

    @OneToMany
    private List<Transaction> transactions;

}
