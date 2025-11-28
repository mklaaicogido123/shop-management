package com.duyphong.shopmanagement.model.response.revenue;

import com.duyphong.shopmanagement.enums.TransactionType;
import lombok.Data;

@Data
public class FinancialClassificationResponse {
    private String name;

    private TransactionType type;

    private String description;
}
