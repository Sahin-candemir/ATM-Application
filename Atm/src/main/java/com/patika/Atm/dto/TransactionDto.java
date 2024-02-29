package com.patika.Atm.dto;

import com.patika.Atm.model.Account;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDto {
    private Long id;
    private String description;
    private LocalDateTime transactionDate;
    private BigDecimal amount;
    private TransactionType transactionType;
    private Long accountId;
}
