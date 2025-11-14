package com.duyphong.shopmanagement.repository;

import com.duyphong.shopmanagement.consts.TransactionType;
import com.duyphong.shopmanagement.entity.transaction.FinancialClassification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancialClassificationRepository extends JpaRepository<FinancialClassification, String> {

    /**
     * Tự động tạo truy vấn để tìm tất cả các Phân loại theo loại (INCOME/EXPENSE)
     */
    List<FinancialClassification> findByType(TransactionType type);
}
