package com.patika.Atm.serviceImpl;

import com.patika.Atm.dto.*;
import com.patika.Atm.exception.InsufficienciesException;
import com.patika.Atm.exception.ResourceNotFoundException;
import com.patika.Atm.model.Account;
import com.patika.Atm.model.Transaction;
import com.patika.Atm.repository.TransactionRepository;
import com.patika.Atm.service.AccountService;
import com.patika.Atm.service.TransactionService;
import com.patika.Atm.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final UserService userService;
    private final AccountService accountService;
    private final TransactionRepository transactionRepository;
    private final ModelMapper mapper;
    public TransactionServiceImpl(UserService userService, AccountService accountService, TransactionRepository transactionRepository, ModelMapper mapper) {
        this.userService = userService;
        this.accountService = accountService;
        this.transactionRepository = transactionRepository;
        this.mapper = mapper;
    }

    @Override
    public TransactionResponse withdraw(TransactionRequest transactionRequest) {
        UserResponse userResponse = userService.getUserById(transactionRequest.getUserId());
        AccountDto accountDto = accountService.getAccountById(transactionRequest.getAccountId());

        boolean containsAccountId = userResponse.getAccountDtoList().stream()
                .anyMatch(a -> Objects.equals(a.getId(), transactionRequest.getAccountId()));

        if(userResponse.getId().equals(transactionRequest.getUserId()) && containsAccountId){
            if (accountDto.getBalance().compareTo(transactionRequest.getAmount()) >= 0) {
                BigDecimal newBalance = accountDto.getBalance().subtract(transactionRequest.getAmount());
                UpdateAccountDto updateAccountDto = new UpdateAccountDto(newBalance);
                accountService.updateAccount(transactionRequest.getAccountId(), updateAccountDto);
                Transaction transaction = Transaction.builder()
                        .transactionType(TransactionType.WITHDRAW)
                        .account(mapper.map(accountDto, Account.class))
                        .amount(transactionRequest.getAmount())
                        .transactionDate(LocalDateTime.now())
                        .description("User with ID : "+ accountDto.getId() +" withdrew "+transactionRequest.getAmount()+"TL from their account")
                        .build();
                transactionRepository.save(transaction);
                return TransactionResponse.builder()
                        .newBalance(newBalance)
                        .accountId(transactionRequest.getAccountId())
                        .userId(transactionRequest.getUserId())
                        .transactionType(TransactionType.WITHDRAW)
                        .build();
            } else {
                throw new InsufficienciesException("Insufficient Balance");
            }
        }else {
            throw new ResourceNotFoundException("Account ID or User ID mismatch");
        }

    }

    @Override
    public TransactionResponse deposit(TransactionRequest transactionRequest) {
        UserResponse userResponse = userService.getUserById(transactionRequest.getUserId());
        AccountDto accountDto = accountService.getAccountById(transactionRequest.getAccountId());

        boolean containsAccountId = userResponse.getAccountDtoList()
                .stream().anyMatch(a -> Objects.equals(a.getId(), transactionRequest.getAccountId()));

        if(userResponse.getId().equals(transactionRequest.getUserId()) && containsAccountId){
            BigDecimal newBalance = accountDto.getBalance().add(transactionRequest.getAmount());
            UpdateAccountDto updateAccountDto = new UpdateAccountDto(newBalance);
            accountService.updateAccount(transactionRequest.getAccountId(), updateAccountDto);
            Transaction transaction = Transaction.builder()
                    .transactionType(TransactionType.DEPOSIT)
                    .account(mapper.map(accountDto, Account.class))
                    .amount(transactionRequest.getAmount())
                    .transactionDate(LocalDateTime.now())
                    .description("User with ID : "+ accountDto.getId() +" deposit "+transactionRequest.getAmount()+"TL from their account")
                    .build();
            transactionRepository.save(transaction);
            return TransactionResponse.builder()
                    .newBalance(newBalance)
                    .accountId(transactionRequest.getAccountId())
                    .userId(transactionRequest.getUserId())
                    .transactionType(TransactionType.DEPOSIT)
                    .build();
        }else {
            throw new ResourceNotFoundException("Account ID or User ID mismatch");
        }
    }

    @Override
    public TransactionResponse transfer(TransferRequest transferRequest) {
        UserResponse userResponse = userService.getUserById(transferRequest.getUserId());

        AccountDto fromAccount = accountService.getAccountById(transferRequest.getFromAccountId());

        AccountDto toAccount = accountService.getAccountById(transferRequest.getToAccountId());

        boolean containsAccountId = userResponse.getAccountDtoList()
                .stream().anyMatch(a -> Objects.equals(a.getId(), transferRequest.getFromAccountId()));

        if (userResponse.getId().equals(transferRequest.getUserId()) && containsAccountId){
            if (fromAccount.getBalance().compareTo(transferRequest.getAmount()) >= 0){

                BigDecimal newFromAccountBalance = fromAccount.getBalance().subtract(transferRequest.getAmount());
                UpdateAccountDto updateFromAccountDto = new UpdateAccountDto(newFromAccountBalance);
                accountService.updateAccount(fromAccount.getId(), updateFromAccountDto);

                BigDecimal newToAccountBalance = toAccount.getBalance().add(transferRequest.getAmount());
                UpdateAccountDto updateToAccountDto = new UpdateAccountDto(newToAccountBalance);
                accountService.updateAccount(fromAccount.getId(), updateToAccountDto);

                Transaction transaction = Transaction.builder()
                        .transactionType(TransactionType.WITHDRAW)
                        .account(mapper.map(fromAccount, Account.class))
                        .amount(transferRequest.getAmount())
                        .transactionDate(LocalDateTime.now())
                        .description("User with ID : "+ fromAccount.getId() +" sent "+transferRequest.getAmount()+"TL to "+fromAccount.getId()+ " accounts with id")
                        .build();
                transactionRepository.save(transaction);
                return TransactionResponse.builder()
                        .newBalance(newFromAccountBalance)
                        .accountId(transferRequest.getFromAccountId())
                        .userId(transferRequest.getUserId())
                        .transactionType(TransactionType.TRANSFER)
                        .build();
            }else {
                throw new InsufficienciesException("Insufficient Balance");
            }
        }else {
            throw new ResourceNotFoundException("From Account ID or User ID mismatch");
        }
    }

    @Override
    public TransactionPageResponse getTransactionsByAccountId(Long accountId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Transaction> transactions = transactionRepository.findByAccountIdOrderByTransactionDateDesc(accountId,pageable);
       List<TransactionDto> transactionDtoList = transactions.stream().map(transaction -> mapper.map(transaction, TransactionDto.class)).collect(Collectors.toList());
        TransactionPageResponse transactionPageResponse = TransactionPageResponse.builder()
                .transactionDtoList(transactionDtoList)
                .pageNo(transactions.getNumber())
                .pageSize(transactions.getSize())
                .totalElements(transactions.getTotalElements())
                .totalPages(transactions.getTotalPages())
                .last(transactions.isLast())
                .build();
        return transactionPageResponse;
    }




}
