package com.duyphong.shopmanagement.mapper.financial;

import com.duyphong.shopmanagement.entity.transaction.FinancialTransaction;
import com.duyphong.shopmanagement.model.response.revenue.FinancialTransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "classificationId", source = "classification.id")
    @Mapping(target = "classificationName", source = "classification.name")
    @Mapping(target = "classificationType", source = "classification.type")
    FinancialTransactionResponse toDto(FinancialTransaction entity);

    List<FinancialTransactionResponse> toDtoList(List<FinancialTransaction> entities);
}