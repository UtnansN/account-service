package com.utnans.accountservice.mapper;

import com.utnans.accountservice.dto.TransactionDto;
import com.utnans.accountservice.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(target = "transactionId", source = "id")
    @Mapping(target = "sentAmount", source = "receiverAmount")
    @Mapping(target = "sentCurrency", source = "receiverAccount.currency")
    @Mapping(target = "senderAccountNo", source = "senderAccount.acctNo")
    @Mapping(target = "receiverAccountNo", source = "receiverAccount.acctNo")
    TransactionDto toTransactionDto(Transaction transactions);

    List<TransactionDto> toTransactionDtos(List<Transaction> transactions);
}
