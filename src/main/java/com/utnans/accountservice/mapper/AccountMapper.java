package com.utnans.accountservice.mapper;

import com.utnans.accountservice.dto.AccountDto;
import com.utnans.accountservice.entity.Account;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface AccountMapper {
    List<AccountDto> toAccountDtos(List<Account> accounts);
}
