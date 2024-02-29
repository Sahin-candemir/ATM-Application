package com.patika.Atm.service;

import com.patika.Atm.dto.AccountDto;
import com.patika.Atm.dto.UpdateAccountDto;

import java.util.List;

public interface AccountService {
    AccountDto createAccount(Long userId);

    List<AccountDto> getActiveAccountsByUserId(Long userId);

    AccountDto updateAccount(Long id, UpdateAccountDto updateAccountDto);

    AccountDto getAccountById(Long id);
}
