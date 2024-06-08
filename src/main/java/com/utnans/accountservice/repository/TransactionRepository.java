package com.utnans.accountservice.repository;

import com.utnans.accountservice.entity.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT tx FROM Transaction tx " +
            "WHERE tx.senderAccount.acctNo = :acctNo OR tx.receiverAccount.acctNo = :acctNo " +
            "ORDER BY tx.execDateTime DESC")
    List<Transaction> findTransactionsForAccount(String acctNo, Pageable pageable);
}
