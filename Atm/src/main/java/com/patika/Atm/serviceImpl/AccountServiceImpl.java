package com.patika.Atm.serviceImpl;

import com.patika.Atm.dto.*;
import com.patika.Atm.exception.ResourceNotFoundException;
import com.patika.Atm.model.Account;
import com.patika.Atm.model.User;
import com.patika.Atm.repository.AccountRepository;
import com.patika.Atm.service.AccountService;
import com.patika.Atm.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserService userService;
    private final ModelMapper mapper;

    public AccountServiceImpl(AccountRepository accountRepository, UserService userService, ModelMapper mapper) {
        this.accountRepository = accountRepository;
        this.userService = userService;
        this.mapper = mapper;
    }
    @Override
    public AccountDto createAccount(Long userId) {
        UserResponse userResponse = userService.getUserById(userId);
        User user = mapper.map(userResponse, User.class);
        Account account = Account.builder()
                .balance(BigDecimal.valueOf(0))
                .user(user)
                .isActive(true)
                .build();
        Account savedAccount = accountRepository.save(account);

        return AccountDto.builder()
                .id(savedAccount.getId())
                .balance(savedAccount.getBalance())
                .build();
    }
    @Override
    public List<AccountDto> getActiveAccountsByUserId(Long userId) {
        List<Account> accounts =accountRepository.findAllByUserIdAndIsActiveTrue(userId);
        return accounts.stream().map(account -> mapper.map(account, AccountDto.class)).toList();
    }
    @Override
    public AccountDto updateAccount(Long id, UpdateAccountDto updateAccountDto) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account Not found with id"+id));

        account.setBalance(updateAccountDto.getQuantity());
        Account savedAccount = accountRepository.save(account);
        return AccountDto.builder()
                .id(savedAccount.getId())
                .balance(savedAccount.getBalance())
                .build();
    }
    @Override
    public AccountDto getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: "+id));
        return AccountDto.builder()
                .id(account.getId())
                .balance(account.getBalance())
                .build();
    }
}