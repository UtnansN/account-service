package com.utnans.accountservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Transaction {

    @Id
    private Long id;

    @ManyToOne
    private Account sender;

    @ManyToOne
    private Account receiver;
}
