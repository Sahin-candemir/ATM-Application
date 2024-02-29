package com.patika.Atm.controller;

import com.patika.Atm.dto.*;
import com.patika.Atm.model.Transaction;
import com.patika.Atm.service.TransactionService;
import com.patika.Atm.util.AppConstants;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/{accountId}")
    public ResponseEntity<TransactionPageResponse> getTransactionsByAccountId(
            @PathVariable Long accountId,
            @RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.DEFAULT_SORT_DIRECTION,required = false) String sortDir
    ){
        return new ResponseEntity<>(transactionService.getTransactionsByAccountId(accountId, pageNo,pageSize,sortBy,sortDir), HttpStatus.OK);
    }
}
