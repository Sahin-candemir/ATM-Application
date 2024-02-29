package com.patika.Atm.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

    @NotNull(message = "User id is mandatory")
    private Long userId;

    @NotNull(message = "From Account id is mandatory")
    private Long fromAccountId;

    @NotNull(message = "To Account id is mandatory")
    private Long toAccountId;

    @Min(value = 0, message = "Amount should greater then 0")
    private BigDecimal amount;
}
