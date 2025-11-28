package com.duyphong.shopmanagement.model.response.revenue;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class FinancialTransactionResponse {
    private String id;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private String description;

    // Thông tin từ Classification
    private String classificationId;
    private String classificationName;
    private String classificationType; // Enum TransactionType sẽ được MapStruct chuyển thành String
}
