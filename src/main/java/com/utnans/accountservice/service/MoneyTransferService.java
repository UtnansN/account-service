package com.utnans.accountservice.service;

import com.utnans.accountservice.dto.TransferRequestDto;
import jakarta.transaction.Transactional;

public interface MoneyTransferService {
    @Transactional
    void transferMoney(TransferRequestDto requestDto);
}
