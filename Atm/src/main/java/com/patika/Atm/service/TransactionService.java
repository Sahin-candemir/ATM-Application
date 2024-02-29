package com.patika.Atm.service;

import com.patika.Atm.dto.TransactionPageResponse;
import com.patika.Atm.dto.TransactionRequest;
import com.patika.Atm.dto.TransactionResponse;
import com.patika.Atm.dto.TransferRequest;
import com.patika.Atm.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    TransactionResponse withdraw(TransactionRequest transactionRequest);

    TransactionResponse deposit(TransactionRequest transactionRequest);

    TransactionResponse transfer(TransferRequest transferRequest);

    TransactionPageResponse getTransactionsByAccountId(Long accountId, int pageNo, int pageSize, String sortBy, String sortDir);
}
