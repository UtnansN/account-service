package com.utnans.accountservice.mapper;

import com.utnans.accountservice.dto.AccountDto;
import com.utnans.accountservice.entity.Account;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDto toAccountDto(Account account);

    List<AccountDto> toAccountDtos(List<Account> accounts);
}
