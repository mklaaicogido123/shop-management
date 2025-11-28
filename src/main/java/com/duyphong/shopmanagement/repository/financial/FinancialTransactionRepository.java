package com.duyphong.shopmanagement.repository.financial;

import com.duyphong.shopmanagement.entity.transaction.FinancialTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, Long> {

    /**
     * Tìm các giao dịch xảy ra trong một khoảng thời gian cụ thể
     */

    @Query(value = """
                SELECT *
                FROM financial_transaction
                WHERE transaction_date::date BETWEEN CAST(:startDate AS DATE) AND CAST(:endDate AS DATE)
            """, nativeQuery = true)
    List<FinancialTransaction> findTransactionDateBetween(@Param("startDate") String startDate,
                                                          @Param("endDate") String endDate);

    /**
     * Tìm các giao dịch theo một mức tiền (amount) nhất định (hoặc lớn hơn/nhỏ hơn)
     * (Chỉ là ví dụ, bạn có thể thay đổi logic này sau)
     */
    List<FinancialTransaction> findByAmountGreaterThan(BigDecimal amount);

}
