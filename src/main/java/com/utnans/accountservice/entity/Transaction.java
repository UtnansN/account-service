package com.utnans.accountservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "account_transactions")
public class Transaction {

    @Id
    @SequenceGenerator(name = "TRANSACTION_ID_GENERATOR", sequenceName = "account_transactions_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRANSACTION_ID_GENERATOR")
    private Long id;

    @NotNull
    @Column(name = "exec_date")
    private LocalDateTime execDateTime;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Account senderAccount;

    @NotNull
    @PositiveOrZero
    private BigDecimal senderAmount;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private Account receiverAccount;

    @NotNull
    @PositiveOrZero
    private BigDecimal receiverAmount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return new EqualsBuilder().append(id, that.id).append(execDateTime, that.execDateTime).append(senderAccount, that.senderAccount).append(senderAmount, that.senderAmount).append(receiverAccount, that.receiverAccount).append(receiverAmount, that.receiverAmount).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(execDateTime).append(senderAccount).append(senderAmount).append(receiverAccount).append(receiverAmount).toHashCode();
    }
}
