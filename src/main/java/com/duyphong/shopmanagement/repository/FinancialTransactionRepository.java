package com.duyphong.shopmanagement.repository;

import com.duyphong.shopmanagement.entity.transaction.FinancialTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

@Repository
public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, Long> {

    /**
     * Tìm các giao dịch xảy ra trong một khoảng thời gian cụ thể
     */
    List<FinancialTransaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Tìm các giao dịch theo một mức tiền (amount) nhất định (hoặc lớn hơn/nhỏ hơn)
     * (Chỉ là ví dụ, bạn có thể thay đổi logic này sau)
     */
    List<FinancialTransaction> findByAmountGreaterThan(BigDecimal amount);

}
