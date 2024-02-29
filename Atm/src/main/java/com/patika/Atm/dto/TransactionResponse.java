package com.patika.Atm.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {

    private Long userId;
    private Long accountId;
    private BigDecimal newBalance;
    private TransactionType transactionType;
}
