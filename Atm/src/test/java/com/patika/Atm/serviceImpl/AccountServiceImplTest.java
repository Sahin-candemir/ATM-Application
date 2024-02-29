package com.patika.Atm.serviceImpl;

import com.patika.Atm.dto.AccountDto;
import com.patika.Atm.dto.UpdateAccountDto;
import com.patika.Atm.dto.UserResponse;
import com.patika.Atm.exception.ResourceNotFoundException;
import com.patika.Atm.model.Account;
import com.patika.Atm.model.User;
import com.patika.Atm.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class AccountServiceImplTest {

    private AccountServiceImpl accountService;
    private AccountRepository accountRepository;
    private ModelMapper mapper;
    private UserServiceImpl userService;
    private PasswordEncoder passwordEncoder;
    @BeforeEach
    public void setup(){
        accountRepository = Mockito.mock(AccountRepository.class);
        mapper = Mockito.mock(ModelMapper.class);
        userService = Mockito.mock(UserServiceImpl.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        accountService = new AccountServiceImpl(accountRepository,userService, mapper);
    }

    @Test
    void createAccount_shouldReturnAccountDtoWithSaveNewAccount_whenUserIdExistAnd() {

        Long userId = 1L;

        UserResponse userResponse = UserResponse.builder()
                .id(2L)
                .email("shn@gmail.com")
                .firstname("Şahin")
                .lastname("Candemir")
                .accountDtoList(Collections.singletonList(new AccountDto(1L,BigDecimal.valueOf(0))))
                .build();

        User user = User.builder()
                .id(2L)
                .email("shn@gmail.com")
                .firstname("Şahin")
                .lastname("Candemir")
                .password(passwordEncoder.encode("123456"))
                .build();

        Account account = Account.builder()
                .id(1L)
                .balance(BigDecimal.valueOf(0))
                .user(user)
                .isActive(true)
                .build();
        AccountDto accountDto = AccountDto.builder()
                .balance(BigDecimal.valueOf(0))
                .id(1L)
                .build();

        when(userService.getUserById(anyLong())).thenReturn(userResponse);
        when(mapper.map(userResponse, User.class)).thenReturn(user);
        when(accountRepository.save(Mockito.any(Account.class))).thenReturn(account);

        AccountDto result = accountService.createAccount(userId);

        assertEquals(accountDto, result);

        Mockito.verify(userService).getUserById(userId);
        Mockito.verify(mapper).map(userResponse,User.class);
        Mockito.verify(accountRepository,times(1)).save(Mockito.any(Account.class));
    }


    @Test
    void updateAccount_shouldReturnAccountDtoAndUpdateBalance_whenValidAccount() {
        Long accountId = 1L;
        BigDecimal newBalance = BigDecimal.valueOf(1000);
        UpdateAccountDto updateAccountDto = new UpdateAccountDto(newBalance);

        User user = User.builder()
                .id(2L)
                .email("shn@gmail.com")
                .firstname("Şahin")
                .lastname("Candemir")
                .password(passwordEncoder.encode("123456"))
                .build();
        Account existingAccount = Account.builder()
                .id(1L)
                .balance(BigDecimal.valueOf(0))
                .user(user)
                .isActive(true)
                .build();
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(Mockito.any(Account.class))).thenReturn(existingAccount);

        AccountDto result = accountService.updateAccount(accountId,updateAccountDto);

        assertEquals(accountId, result.getId());
        assertEquals(newBalance, result.getBalance());
    }
    @Test
    void updateAccount_shouldThrowException_whenInvalidAccountId(){
        Long invalidAccountId = 999L;
        UpdateAccountDto updateAccountDto = new UpdateAccountDto(new BigDecimal(1000));

        when(accountRepository.findById(invalidAccountId)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> accountService.updateAccount(invalidAccountId,updateAccountDto));
        assertEquals("Account Not found with id"+ invalidAccountId, exception.getMessage());
    }
    @Test
    void getAccountById_shouldReturnAccountDto_whenValidAccount(){
        Long accountId = 1L;

        Account existingAccount = Account.builder()
                .id(1L)
                .balance(BigDecimal.valueOf(0))
                .user(new User())
                .isActive(true)
                .build();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        AccountDto result = accountService.getAccountById(accountId);

        assertEquals(result.getId(), existingAccount.getId());
        assertEquals(result.getBalance(),existingAccount.getBalance());
    }

    @Test
    void getActiveAccountsByUserId_shouldReturnAccountDtoListAndActiveAccount_whenValidUser(){
        Long userId = 1L;


        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account(1L, BigDecimal.valueOf(100),true, new User()));
        accounts.add(new Account(2L, BigDecimal.valueOf(100),true, new User()));

        List<AccountDto> expectedAccountDtoList = new ArrayList<>();
        expectedAccountDtoList.add(new AccountDto(1L, BigDecimal.valueOf(100)));
        expectedAccountDtoList.add(new AccountDto(2L, BigDecimal.valueOf(100)));

        when(accountRepository.findAllByUserIdAndIsActiveTrue(userId)).thenReturn(accounts);
        for (int i=0; i < accounts.size(); i++){
            when(mapper.map(accounts.get(i), AccountDto.class)).thenReturn(expectedAccountDtoList.get(i));
        }
        List<AccountDto> result = accountService.getActiveAccountsByUserId(userId);

        assertEquals(expectedAccountDtoList.size(), result.size());
        for (int i =0; i < expectedAccountDtoList.size(); i++){
            assertEquals(expectedAccountDtoList.get(i).getId(), result.get(i).getId());
            assertEquals(expectedAccountDtoList.get(i).getBalance(), result.get(i).getBalance());
        }
    }
}