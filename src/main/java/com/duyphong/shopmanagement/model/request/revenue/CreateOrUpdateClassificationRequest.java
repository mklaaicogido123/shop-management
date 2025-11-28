package com.duyphong.shopmanagement.model.request.revenue;


import com.duyphong.shopmanagement.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrUpdateClassificationRequest {

    private String name;

    private TransactionType type;

}
