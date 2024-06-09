package com.utnans.accountservice.service;

import com.utnans.accountservice.dto.TransferRequestDto;
import org.springframework.transaction.annotation.Transactional;

public interface MoneyTransferService {
    @Transactional
    void transferMoney(TransferRequestDto requestDto);
}
