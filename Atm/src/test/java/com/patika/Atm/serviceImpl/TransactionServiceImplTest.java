package com.patika.Atm.serviceImpl;

import com.patika.Atm.dto.*;
import com.patika.Atm.exception.InsufficienciesException;
import com.patika.Atm.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class TransactionServiceImplTest {
    private AccountServiceImpl accountService;

    private TransactionServiceImpl transactionService;
    private ModelMapper mapper;
    private UserServiceImpl userService;

    @BeforeEach
    public void setup(){
        accountService = Mockito.mock(AccountServiceImpl.class);
        mapper = Mockito.mock(ModelMapper.class);
        userService = Mockito.mock(UserServiceImpl.class);
        transactionService = new TransactionServiceImpl(userService,accountService, transactionRepository, mapper);
    }

    @Test
    void withdraw_shouldDecreaseBalanceAndReturnTransactionResponse_whenValidUserAccountAndSufficientBalance(){
        Long userId=1L;
        Long accountId=1L;
        BigDecimal initialBalance = BigDecimal.valueOf(1000);
        BigDecimal withDrawAmount = BigDecimal.valueOf(100);


        UserResponse userResponse = UserResponse.builder()
                .id(userId)
                .email("shn@gmail.com")
                .firstname("Şahin")
                .lastname("Candemir")
                .accountDtoList(Collections.singletonList(new AccountDto(accountId,initialBalance)))
                .build();

        AccountDto accountDto = AccountDto.builder()
                .id(accountId)
                .balance(initialBalance)
                .build();
        TransactionRequest transactionRequest = new TransactionRequest(userId, accountId, withDrawAmount);

        when(userService.getUserById(anyLong())).thenReturn(userResponse);
        when(accountService.getAccountById(anyLong())).thenReturn(accountDto);

        TransactionResponse result = transactionService.withdraw(transactionRequest);

        assertEquals(TransactionType.WITHDRAW, result.getTransactionType());
        assertEquals(userId, result.getUserId());
        assertEquals(accountId, result.getUserId());
        assertEquals(initialBalance.subtract(withDrawAmount), result.getNewBalance());
    }

    @Test
    void withdraw_shouldThrowException_whenInsufficientBalance(){
        Long userId=1L;
        Long accountId=1L;

        BigDecimal initialBalance = BigDecimal.valueOf(100);
        BigDecimal withDrawAmount = BigDecimal.valueOf(1000);

        UserResponse userResponse = UserResponse.builder()
                .id(userId)
                .email("shn@gmail.com")
                .firstname("Şahin")
                .lastname("Candemir")
                .accountDtoList(Collections.singletonList(new AccountDto(accountId,initialBalance)))
                .build();

        AccountDto accountDto = AccountDto.builder()
                .id(accountId)
                .balance(initialBalance)
                .build();
        TransactionRequest transactionRequest = new TransactionRequest(userId, accountId, withDrawAmount);

        when(userService.getUserById(anyLong())).thenReturn(userResponse);
        when(accountService.getAccountById(accountId)).thenReturn(accountDto);

        assertThrows(InsufficienciesException.class, () -> transactionService.withdraw(transactionRequest));
    }

    @Test
    void withdraw_shouldTrowException_whenMismatchedIds(){
        Long userId = 1L;
        Long accountId = 1L;
        BigDecimal initialBalance = BigDecimal.valueOf(500);
        BigDecimal withDrawAmount = BigDecimal.valueOf(100);

        UserResponse userResponse = UserResponse.builder()
                .id(2L)
                .email("shn@gmail.com")
                .firstname("Sahin")
                .lastname("Candemir")
                .accountDtoList(Collections.singletonList(new AccountDto(accountId,BigDecimal.valueOf(500))))
                .build();

        AccountDto accountDto = AccountDto.builder()
                .id(2L)
                .balance(initialBalance)
                .build();
        when(userService.getUserById(userId)).thenReturn(userResponse);
        when(accountService.getAccountById(accountId)).thenReturn(accountDto);

        TransactionRequest transactionRequest = new TransactionRequest(userId, accountId, withDrawAmount);

        assertThrows(ResourceNotFoundException.class, () -> transactionService.withdraw(transactionRequest));
    }

    @Test
    void deposit_shouldIncreaseBalanceAndReturnTransactionResponse_whenValidUserAndAccount(){
        Long userId=1L;
        Long accountId=1L;

        BigDecimal initialBalance = BigDecimal.valueOf(1000);
        BigDecimal depositAmount = BigDecimal.valueOf(100);

        UserResponse userResponse = UserResponse.builder()
                .id(userId)
                .email("shn@gmail.com")
                .firstname("Şahin")
                .lastname("Candemir")
                .accountDtoList(Collections.singletonList(new AccountDto(accountId,initialBalance)))
                .build();
        TransactionRequest transactionRequest = new TransactionRequest(userId,accountId, depositAmount);

        AccountDto accountDto = AccountDto.builder()
                .id(accountId)
                .balance(initialBalance)
                .build();

        when(userService.getUserById(userId)).thenReturn(userResponse);
        when(accountService.getAccountById(accountId)).thenReturn(accountDto);

        TransactionResponse result = transactionService.deposit(transactionRequest);

        assertEquals(TransactionType.DEPOSIT, result.getTransactionType());
        assertEquals(userId, result.getUserId());
        assertEquals(accountId, result.getAccountId());
        assertEquals(initialBalance.add(depositAmount), result.getNewBalance());
    }

    @Test
    void deposit_shouldThrowException_whenMismatchedIds(){
        Long userId = 1L;
        Long accountId = 1L;

        BigDecimal depositAmount = BigDecimal.valueOf(100);
        UserResponse userResponse = UserResponse.builder()
                .id(2L)
                .email("shn@gmail.com")
                .firstname("Şahin")
                .lastname("Candemir")
                .accountDtoList(Collections.singletonList(new AccountDto(accountId,BigDecimal.ZERO)))
                .build();
        TransactionRequest transactionRequest = new TransactionRequest(userId,accountId, depositAmount);

        AccountDto accountDto = AccountDto.builder()
                .id(accountId)
                .balance(new BigDecimal(100))
                .build();

        when(userService.getUserById(userId)).thenReturn(userResponse);
        when(accountService.getAccountById(accountId)).thenReturn(accountDto);

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class,() ->  transactionService.deposit(transactionRequest));

        assertEquals("Account ID or User ID mismatch", exception.getMessage());
    }

    @Test
    void transfer_shouldReturnTransactionResponse_whenValidUserAndAccount() {
        Long userId = 1L;
        Long fromAccountId = 1L;
        Long toAccountId = 2L;

        BigDecimal initialFromAccountBalance = new BigDecimal(500);
        BigDecimal initialToAccountBalance = new BigDecimal(200);
        BigDecimal transferAmount = new BigDecimal(200);

        UserResponse userResponse = new UserResponse(userId, "Şahin","Candemir","gmail.com", Arrays.asList(
                new AccountDto(fromAccountId, initialFromAccountBalance),
                new AccountDto(toAccountId, initialToAccountBalance)
        ));
        AccountDto fromAccountDto = new AccountDto(fromAccountId, initialFromAccountBalance);
        AccountDto toAccountDto = new AccountDto(toAccountId, initialToAccountBalance);

        Mockito.when(userService.getUserById(userId)).thenReturn(userResponse);
        Mockito.when(accountService.getAccountById(fromAccountId)).thenReturn(fromAccountDto);
        Mockito.when(accountService.getAccountById(toAccountId)).thenReturn(toAccountDto);

        TransferRequest transferRequest = new TransferRequest(userId, fromAccountId, toAccountId, transferAmount);

        TransactionResponse result = transactionService.transfer(transferRequest);
        assertEquals(TransactionType.TRANSFER, result.getTransactionType());
        assertEquals(userId, result.getUserId());
        assertEquals(fromAccountId, result.getAccountId());
        assertEquals(initialFromAccountBalance.subtract(transferAmount), result.getNewBalance());
    }

    @Test
    void transfer_shouldThrowException_whenReturnInsufficientBalance(){
        long userId = 1L;
        long fromAccountId = 101L;
        long toAccountId = 102L;
        BigDecimal initialFromAccountBalance = new BigDecimal(100);
        BigDecimal transferAmount = new BigDecimal(200);

        UserResponse userResponse = new UserResponse(userId, "Şahin","Candemir","gmail.com", Arrays.asList(
                new AccountDto(fromAccountId, initialFromAccountBalance),
                new AccountDto(toAccountId, new BigDecimal(200))
        ));
        AccountDto fromAccountDto = new AccountDto(fromAccountId, initialFromAccountBalance);
        AccountDto toAccountDto = new AccountDto(toAccountId, new BigDecimal(200));

        Mockito.when(userService.getUserById(userId)).thenReturn(userResponse);
        Mockito.when(accountService.getAccountById(fromAccountId)).thenReturn(fromAccountDto);
        Mockito.when(accountService.getAccountById(toAccountId)).thenReturn(toAccountDto);

        TransferRequest transferRequest = new TransferRequest(userId, fromAccountId, toAccountId, transferAmount);

        InsufficienciesException exception = assertThrows(InsufficienciesException.class, () -> transactionService.transfer(transferRequest));
        assertEquals("Insufficient Balance", exception.getMessage());
    }
    @Test
    void transfer_shouldThrowException_whenMismatchedIds(){
        long userId = 1L;
        long fromAccountId = 101L;
        long toAccountId = 102L;
        BigDecimal transferAmount = new BigDecimal(200);

        UserResponse userResponse = new UserResponse(2L, "Şahin","Candemir","gmail.com", Arrays.asList(
                new AccountDto(fromAccountId, new BigDecimal(500)),
                new AccountDto(toAccountId, new BigDecimal(200))
        ));
        AccountDto fromAccountDto = new AccountDto(fromAccountId, new BigDecimal(500));
        AccountDto toAccountDto = new AccountDto(toAccountId, new BigDecimal(200));

        when(userService.getUserById(userId)).thenReturn(userResponse);
        when(accountService.getAccountById(fromAccountId)).thenReturn(fromAccountDto);
        when(accountService.getAccountById(toAccountId)).thenReturn(toAccountDto);

        TransferRequest transferRequest = new TransferRequest(userId, fromAccountId, toAccountId, transferAmount);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> transactionService.transfer(transferRequest));
        assertEquals("From Account ID or User ID mismatch", exception.getMessage());
    }
}