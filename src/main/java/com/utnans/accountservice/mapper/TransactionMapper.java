package com.utnans.accountservice.mapper;

import com.utnans.accountservice.dto.TransactionDto;
import com.utnans.accountservice.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(target = "transactionId", source = "id")
    @Mapping(target = "sentAmount", source = "receiverAmount")
    @Mapping(target = "senderAccountNo", source = "senderAccount.acctNo")
    @Mapping(target = "senderName", source = "senderAccount.client.fullName")
    @Mapping(target = "receiverAccountNo", source = "receiverAccount.acctNo")
    @Mapping(target = "receiverName", source = "receiverAccount.client.fullName")
    List<TransactionDto> toTransactionDtos(List<Transaction> transactions);
}
