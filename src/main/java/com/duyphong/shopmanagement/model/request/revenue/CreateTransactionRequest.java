package com.duyphong.shopmanagement.model.request.revenue;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class CreateTransactionRequest {
    @NotEmpty
    private BigDecimal amount;

    @NotEmpty
    private LocalDateTime transactionDate;

    private String description;

    @NotEmpty
    private String classificationId;
}
