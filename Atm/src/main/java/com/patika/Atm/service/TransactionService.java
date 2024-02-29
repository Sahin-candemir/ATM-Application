package com.patika.Atm.service;

import com.patika.Atm.dto.TransactionRequest;
import com.patika.Atm.dto.TransactionResponse;
import com.patika.Atm.dto.TransferRequest;

public interface TransactionService {
    TransactionResponse withdraw(TransactionRequest transactionRequest);

    TransactionResponse deposit(TransactionRequest transactionRequest);

    TransactionResponse transfer(TransferRequest transferRequest);
}
