package com.patika.Atm.controller;

import com.patika.Atm.dto.AccountDto;
import com.patika.Atm.dto.TransactionRequest;
import com.patika.Atm.dto.TransactionResponse;
import com.patika.Atm.dto.TransferRequest;
import com.patika.Atm.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody TransactionRequest transactionRequest){
        return new ResponseEntity<>(transactionService.withdraw(transactionRequest), HttpStatus.OK);
    }
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@RequestBody TransactionRequest transactionRequest){
        return new ResponseEntity<>(transactionService.deposit(transactionRequest), HttpStatus.OK);
    }
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@RequestBody TransferRequest transferRequest){
        return new ResponseEntity<>(transactionService.transfer(transferRequest), HttpStatus.OK);
    }
}
